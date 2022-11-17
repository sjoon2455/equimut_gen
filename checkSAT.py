from __future__ import print_function
from z3 import *
from collections import defaultdict
import pickle
from os.path import isfile, join
import os
import argparse
import ast
import sys
import re

# def checkPositive(i, conditions, declareFuns):
#     formula = ""
#     related_conditions = []
#     for condition in conditions:
#         condition, _ = preorder(stripReplace(condition))
#         related_conditions.append(condition)
#     ccc, _ = stripReplace(i)
#     i = preorder(ccc)
#     if len(related_conditions) == 0:
#         return False # this variable could be any value    
#     for n, cond in enumerate(related_conditions):
#         if n == 0:
#             formula = cond
#         else:
#             formula = "(and " + formula + cond + ")"
#     s = Solver()
#     formula = declareFuns + "(assert (and " + formula + f"(< {i} 0)))"
#     s.from_string(formula)
#     if s.check() == "unsat": #always positive
#         return True
#     else:
#         return False

def solve(formula):
    # print(formula)
    s = Solver()
    s.reset()
    s.set("timeout", 10000)
    s.from_string(formula)
    result = s.check()
    result = str(result)
    if result == "sat":
        return result, s.model()
    else:
        return result, ""


def tokenize(expression):
    '''Generate tokens from a string following fixed rules.
    '''
    scanner = re.Scanner([
        (r'[0-9]\.[0-9]+', lambda _, t: t),
        (r'[0-9]+', lambda _, t: t),
        (r'[a-zA-Z_]+', lambda _, t: t),
        (r'\(', lambda _, t: ('P_OPEN', t)),
        (r'\)', lambda _, t: ('P_CLOSE', t)),
        (r'[+\-*/^]', lambda _, t: ('OPERATOR', t)),
        (r'\s+', None),
    ])
    tokens, _ = scanner.scan(expression)
    return tokens


def parse(tokens, in_parens=False):
    '''Parse a list of tokens that may contain brackets into a token hierarchy
    where all brackets are removed and replaced by list nesting.
    '''
    cur = []
    i = 0
    while i < len(tokens):
        t = tokens[i]
        if t[0] == 'P_OPEN':
            # If we hit an opening bracket, we memorize its position and search
            # for the corresponding closing one by counting the stacked
            # brackets.
            pos_open = i #2
            pos_close = None

            par_stack = 0
            for j, p in enumerate(tokens[i:]):
                if p[0] == 'P_OPEN':
                    # Deeper nesting, increase.
                    par_stack += 1
                elif p[0] == 'P_CLOSE':
                    # Level closed, decrease.
                    par_stack -= 1
                if par_stack == 0:
                    # If we hit level 0, we found the corresponding closing
                    # bracket for the opening one.
                    pos_close = i + j
                    break

            if pos_close is None:
                # If we did not find a corresponding closing bracket, there
                # must be some syntax error.
                raise Exception('Syntax error; missing closing bracket.')

            # For the bracketed subset we just found, we invoke a recursive
            # parsing for its contents and append the result to our hierarchy.
            elem = parse(tokens[i + 1:pos_close], in_parens=True)
            cur.append(elem)
            i = pos_close
        elif t[0] == 'P_CLOSE':
            if not in_parens:
                # If we hit a closing bracket but are not searching for one, we
                # found too many closing brackets, which is a syntax error.
                raise Exception('Syntax error; too many closing brackets.')
            return cur
        else:
            cur.append(t)
        i += 1
    return cur


class Node(object):
    def __init__(self, hierarchy, parent=None):
        if len(hierarchy) == 1 and type(hierarchy[0]) is list:
            hierarchy = hierarchy[0]  # Bracketed descent

        # Find position of operator that has the weakest binding priority and
        # use it as pivot element to split the sequence at. The weakest binding
        # is executed last, so it's the topmost node in the tree (which is
        # evaluated bottom-up).
        pivot = self._weakest_binding_position(hierarchy)

        if pivot is not None:
            self.left = Node(hierarchy[:pivot], parent=self)
            self.op = hierarchy[pivot][1]
            self.right = Node(hierarchy[pivot + 1:], parent=self)
        else:
            # There is no pivot element if there is no operator in our
            # hierarchy. If so, we hit an atomic item and this node will be
            # a leaf node.
            self.value = hierarchy[0]

    def __str__(self):
        if self.isleaf():
            return str(self.value[1])
        else:
            return f'({self.op} {self.left} {self.right})'

    def _binding_order(self, operator):
        '''Resolve operator to its binding order.'''
        if operator in '+-':
            return 1
        elif operator in '*/':
            return 2
        elif operator in '^':
            return 3
        raise Exception('Parsing error; operator binding cannot be assessed.')

    def _weakest_binding_position(self, tokens):
        '''Return position of operator with weakest binding (maintains LTR).'''
        ops = sorted([
            (i, self._binding_order(t[1]))
            for i, t in enumerate(tokens)
            if t[0] == 'OPERATOR'
        ], key=lambda e: e[1], reverse=True)
        if len(ops) == 0:
            if len(tokens) != 1:
                raise Exception('Parsing error; found sequence w/o operator.')
            return None
        return ops[-1][0]

    def isleaf(self):
        if hasattr(self, 'value'):
            return True
        return False

    def prefix(self):
        if self.isleaf():
            return self.value
        else:
            return (self.op, self.left.prefix(), self.right.prefix())

def convert(op):
    if op == "==":
        return "="
    elif op == "&&":
        return "and"
    elif op == "||":
        return "or"
    return op

def preorder(expression):
    r_operators = [ "&&", "||", "==", "!=", "<=", ">=", "<", ">"]
    a_operators = ["|", "^", "&", "<<", ">>", "+", "-", "*", "/", "%"]
    includeOperator = False
    for r_operator in r_operators:
        if r_operator in expression: 
            op = r_operator
            lhs_rhs = expression.split(op)
            op = convert(op)
            assert len(lhs_rhs) == 2
            lhs, rhs = [l.strip() for l in lhs_rhs]
            if op == "!=":
                return "(not (= "+preorder(lhs) + " " + preorder(rhs)+"))"
            return "("+op+" "+preorder(lhs) + " " + preorder(rhs)+")"
    for a_operator in a_operators:
        if a_operator in expression:
            includeOperator = True
            break
    if not includeOperator:
        return expression
    
    if a_operator == "%":
        lhs_rhs = expression.split(a_operator)
        assert len(lhs_rhs) == 2
        lhs, rhs = [l.strip() for l in lhs_rhs]
        return "(mod "+preorder(lhs) + " " + preorder(rhs)+")"
    # no relational operator here
    tokens = tokenize(expression)
    hierarchy = parse(tokens)
    root = Node(hierarchy)    
    res = str(root.prefix())
    res = res.replace("'", "")
    res = res.replace(",", "")
    return res

def toSMTLIB2Type(ty):
    ints = ["Z", "B", "I", "J", "C", "S"]
    reals = ["F", "D"]
    if ty in ints:
        return "Int"
    elif ty in reals:
        return "Real"
    elif ty == "L":
        return "(List Int)"
    elif ty == "£":
        return "IDK"
    else:
        assert False

def addDeclareFun(li, sqrt_count):
    res = ""
    for n in range(25):
        var = "V"*(n+1)
        declare_ = f"(declare-const {var} Int)"
        res += declare_
    # met = defaultdict(int)
    # for _, _, vvt in li:
    #     for _, vs, typ in vvt: #vs: {V0}, (304) + ({V0})
            
    #         if vs == "this" or vs.isnumeric():
    #             continue
    #         if met[vs] == 1:
    #             continue
    #         ty = toSMTLIB2Type(typ)
    #         if ty == "(List Int)" or ty == "IDK":
    #             continue
           
    #         symbolicValues = re.findall('{(?:[^{}])*}', vs)
    #         for symVal in symbolicValues: #['{V0}']
    #             if met[symVal] == 1:
    #                 continue
    #             else:
    #                 met[symVal] = 1
    #             symVal = 'V'*(int(symVal[2])+1)
    #             declare_ = "(declare-const " + symVal + " " + ty + ")"
    #             res += declare_
    #         met[vs] = 1
    # for n in range(sqrt_count):
    #     var_sqrt = "sqrt"*(n+1)
    #     declare_ = f"(declare-const {var_sqrt} Real)"
    #     res += declare_
    
    return res

# def findObfuscated(pc_, FLAG):
#     assert FLAG in ["__PARAM", "__LOCAL"]
#     num = 0
#     indices = [i + len(FLAG) + 1 for i in range(len(pc_)) if pc_.startswith(FLAG, i)]
#     nums = []
#     for ind in indices:
#         if pc_[ind].isdigit():
#             num = pc_[ind]
#             if ind <= len(pc_)-2 and pc_[ind+1].isdigit():
#                 num += pc_[ind+1]
#         if num not in nums:
#             nums.append(str(num))
#     return nums

def stripReplace(pc_, rtype=' ', sqrt_count=0):
    for n in range(10, 23):
        tmpname = f"V{n}"
        tmpafter = "V"*(n+1)
        pc_ = pc_.replace(tmpname, tmpafter)
    for n in range(10):
        tmpname = f"V{n}"
        tmpafter = "V"*(n+1)
        pc_ = pc_.replace(tmpname, tmpafter)
    
    while 'WIDEN' in pc_:
        ind = pc_.find('WIDEN-')
        ind2 = ind + len('WIDEN-')
        for n, p in enumerate(pc_[ind2:]):
            if not p.isalpha():
                assert p == '(', pc_
                break
        ind3 = findClosingIndex(pc_, ind2+n)
        pc_ = pc_[:ind]+pc_[ind2+n+1:ind3+1-1]+pc_[ind3+1:]

    while "java/lang/StrictMath" in pc_: # handle sqrt, log, ...
        if "sqrt" in pc_.split("java/lang/StrictMath")[1]:
            sqrt_count += 1
            sqrt_var = "sqrt"*sqrt_count
            pc_ = extractStrictMath(pc_, sqrt_var, "sqrt")
        elif "eVp" in pc_.split("java/lang/StrictMath")[1]:
            pc_ = extractStrictMath(pc_, '', "eVp")

    open_, close_, onlychar, tmp_index = False, True, True, 0
    for _ in range(30): 
        for i, p in enumerate(pc_):
            if ("[]" in rtype or "List" in rtype) and (i == 0 or i == len(pc_)-1):
                continue
            if p == "{":
                if not open_:
                    tmp_index = i
                    open_ = True
                    close_ = False
                
            elif p == "}":
                if not close_:
                    open_ = False
                    close_ = True
                    if onlychar:
                        pc_ = pc_[:i] + pc_[i + 1:]
                        pc_ = pc_[:tmp_index] + pc_[tmp_index + 1:]
                        break
                    onlychar = True
            elif open_ and not p.isalpha():
                onlychar = False
    open_, close_, onlychar, tmp_index = False, True, True, 0
    for _ in range(30):
        for i, p in enumerate(pc_):
            if p == "(":
                if not open_:
                    tmp_index = i
                    open_ = True
                    close_ = False
                
            elif p == ")":
                if not close_:
                    open_ = False
                    close_ = True
                    if onlychar:
                        pc_ = pc_[:i] + pc_[i + 1:]
                        pc_ = pc_[:tmp_index] + pc_[tmp_index + 1:]
                        break
                    onlychar = True
            
            elif open_ and not (p.isalpha() or p.isdigit() or p == ' ' or p == '.'):
                onlychar = False
    for n in range(len(pc_)-1):
        if pc_[n+1] == "d" and pc_[n].isdigit():
            pc_ = pc_[:n+1] + " " + pc_[n+2:]
        if pc_[n+1] == "." and pc_[n+2] == '0':
            if n+3<=len(pc_)-1 and pc_[n+3] == 'd':
                pc_ = pc_[:n+1] + "   " + pc_[n+4:]
            else:
                pc_ = pc_[:n+1] + "  " + pc_[n+3:]
    for n in range(len(pc_)-1):
        if pc_[n+1] == "f" and pc_[n].isdigit():
            pc_ = pc_[:n+1] + " " + pc_[n+2:]
        if pc_[n+1] == "." and pc_[n+2] == '0':
            if n+3<=len(pc_)-1 and pc_[n+3] == 'f':
                pc_ = pc_[:n+1] + "   " + pc_[n+4:]
            else:
                pc_ = pc_[:n+1] + "  " + pc_[n+3:]
    for n in range(len(pc_)-1):
        if pc_[n+1] == "L" and pc_[n].isdigit():
            pc_ = pc_[:n+1] + " " + pc_[n+2:]
        if pc_[n+1] == "." and pc_[n+2] == '0':
            if n+3<=len(pc_)-1 and pc_[n+3] == 'L':
                pc_ = pc_[:n+1] + "   " + pc_[n+4:]
            else:
                pc_ = pc_[:n+1] + "  " + pc_[n+3:]
    for n in range(len(pc_)-1):
        if pc_[n+1] == "F" and pc_[n].isdigit():
            pc_ = pc_[:n+1] + " " + pc_[n+2:]
        if pc_[n+1] == "." and pc_[n+2] == '0':
            if n+3<=len(pc_)-1 and pc_[n+3] == 'F':
                pc_ = pc_[:n+1] + "   " + pc_[n+4:]
            else:
                pc_ = pc_[:n+1] + "  " + pc_[n+3:]
    pc_ = pc_.replace(" ", "")
    
            
    while '~' in pc_: # handle bitwise flip operator
        ind = pc_.find('~')
        for n, char in enumerate(pc_[ind+1:]):
            if not char.isalpha():
                break
        if n == 0:  
            if char.isalpha(): # ~V
                pc_ = '-' + pc_[1:]
            else: # ~(V+3)
                end_index = findClosingIndex(pc_, ind+1)
                pc_ = pc_[:ind] + '-' + pc_[ind+1:end_index+1]+pc_[end_index+1:]
        else: # ~VVVVV+3, ~VVVVV
            pc_ = pc_[:ind] + '(-' + pc_[ind+1:ind+n+1]+')'+pc_[ind+n+1:]
    
    # test pass for '({V0}) < (({V2}) ^ (-1))'
    while '^-1' in pc_: # handle abs negation operator
        print(f"\n\n^ appeared!\n\n\n{pc_}\n\n\n\n")
        ind = pc_.find('^')
        assert pc_[ind+1] == '-' and pc_[ind+2] == '1'
        for n in range(ind-1, -1, -1):
            char = pc_[n]
            if not char.isalpha():
                print(char)
                break

        pc_ = pc_[:n+1] + '1/' + pc_[n+1:ind]+pc_[ind+3:]
        # if ind == 0: # ()^-1
            # if char==')':
                # //find char='('
    
    for _ in range(10):
        if "-" in pc_: # handle unary negation(-) operator
            if pc_[0] == "-":
                pc_ = "0"+pc_
            for ind in range(len(pc_)-1):
                # if pc_[ind+1] == "-" and not pc_[ind].isdigit() and not pc_[ind].isalpha() and pc_[ind] != ')':
                if pc_[ind+1] == "-" and pc_[ind] == '(':
                    pc_ = pc_[:ind+1]+"0"+pc_[ind+1:]
    
    pc_ = pc_.replace("+-", "-")
    pc_ = pc_.replace("-+", "-")
    return pc_, sqrt_count

def findClosingIndex(pc, ind, ope=0):
    assert pc[ind] == '('
    for n, char in enumerate(pc[ind:]):
        if char == '(':
            ope += 1
        elif char == ')':
            ope -= 1
        else:
            pass
        if ope == 0:
            break
    return n+ind

def extractStrictMath(pc, sqrt_var, FLAG):
    diff = 0
    operand = pc.split("@")[0].split("eVp")[1]
    ind_ = pc.find("java/lang/StrictMath:(")
    if ind_>=1 and pc[ind_-1] == '(':
        diff = 1
    ind = ind_ + len("java/lang/StrictMath:(")-1
    cl_ind = findClosingIndex(pc, ind, diff)
    if FLAG=="sqrt":
        operand = sqrt_var
    elif FLAG=="eVp":
        operand = f"2.7182818284^{operand}"
    pc = pc[:ind_-diff] + operand + pc[cl_ind+1:]
    return pc


def aggregatePath(li, rtype):
    formulas = []
    sqrt_count = 0
    for nn, k in enumerate(li):
        pc, rv, _ = k
        rv, sq = stripReplace(rv, rtype)
        if "[]" in rtype or "List" in rtype:
            pass
        else:
            rv = preorder(rv)
        tmp_formula = ""
        for n, pc_ in enumerate(pc):
            pc_, sq = stripReplace(pc_)
            sqrt_count += sq
            pc_ = preorder(pc_)
            
            if n == 0:
                tmp_formula = pc_
            else:
                tmp_formula = "(and " + tmp_formula + " " + pc_+")"
        formulas.append([tmp_formula, rv])
    return formulas, sqrt_count

def getEquations(_r, _rr):
    assert len(_r) == len(_rr)
    formula = ""
    for i in range(len(_r)):
        if _r[i][0] == '-':
            assert _r[i][1:].isdigit()
            pr = _r[i]
        else:
            pr = preorder(_r[i])
        if _rr[i][0] == '-':
            assert _rr[i][1:].isdigit()
            prr = _rr[i]
        else:
            prr = preorder(_rr[i])
        tmp = f"(= {pr} {prr})"
        formula = formula + " " + tmp
    return formula

def stringToList(r):
    tmplist = r.split(",")
    tmp = []
    size = len(tmplist)
    for n, ele in enumerate(tmplist):
        ele = ele.strip()
        if n == 0:
            assert ele[0] == '[' or ele[0] == '{'
            tmp.append(ele[1:])
        elif n == size-1:
            assert ele[-1] == ']' or ele[-1] == '}'
            tmp.append(ele[:-1])
        else:
            tmp.append(ele)
    return tmp

def stringToInt(r, stringDict):
    if r in stringDict:
        return str(stringDict[r]), stringDict
    else:
        if len(stringDict) == 0:
            stringDict[r] = 0
            return str(0), stringDict
        else:
            M = max(list(stringDict.values()))
            stringDict[r] = M+1
            return str(M+1), stringDict

def aggregateFormula(formula_ori, formula_mut, rtype):
    formula = ""
    stringDict = {}
    returnsList = False
    returnsString = False
    if '[]' in rtype or 'List' in rtype: # if return type is list
        returnsList = True
    elif rtype == 'String':
        returnsString = True
    for f, r in formula_ori:
        if returnsList:
            _r = stringToList(r)
        if returnsString:
            r, stringDict = stringToInt(r, stringDict)
        for ff, rr in formula_mut:
            if returnsList:
                _rr = stringToList(rr)
                if len(_r) != len(_rr):
                    continue
                retstring = "(not (and " + getEquations(_r, _rr) + "))"
                if f == '' and ff == '':
                    tmp_formula = retstring
                elif f == '' and ff != '':
                    tmp_formula = "(and " + ff + " " + retstring + ")"                
                elif f != '' and ff == '':
                    tmp_formula = "(and " + f + " " + retstring + ")"
                else:
                    tmp_formula = "(and (and " + f + ff + ") " + retstring + ")"
            
            else:    
                if returnsString:
                    rr, stringDict = stringToInt(rr, stringDict)
                retstring = "(not (= " + r + " " + rr + "))"
                if f == '' and ff == '':
                    tmp_formula = retstring
                elif f == '' and ff != '':
                    tmp_formula = "(and " + ff + " " + retstring + ")"
                elif f != '' and ff == '':
                    tmp_formula = "(and " + f + " " + retstring + ")"
                else:
                    tmp_formula = "(and (and " + f + ff + ") " + retstring + ")"
            
            if formula == "":
                formula = tmp_formula
            else:
                formula = "(or " + formula + tmp_formula + ")"
            
    formula = "(assert " + formula + ")"
    return formula

def main():
    parser = argparse.ArgumentParser(description='')
    parser.add_argument('--filename', '-f', help='mutant name', default='AOIS_1.java')
    parser.add_argument('--progname', '-p', help='program name', default='Bisect')
    parser.add_argument('--rtype', '-r', help='return type', default='int')
    parser.add_argument('--alive', '-a', help='alive mode?', default='N')
    args = parser.parse_args()
    filename = args.filename
    program = args.progname
    rtype = args.rtype
    alive = True if args.alive == "Y" else False
    mutname = filename.split(".")[0]
    # mutname = filename

    with open(f"programs/{program}/log/original.pkl", "rb") as ff:
        ori = pickle.load(ff)
    ff.close()
    formula_ori, sqrt_count_1 = aggregatePath(ori, rtype)
    f = f"programs/{program}/log/mutant_pkl/{mutname}.pkl"
    if alive:
        PATHEQUI = f"programs/{program}/log/alive_equivalency.csv"
    else:
        PATHEQUI = f"programs/{program}/log/equivalency.csv"
    with open(f, "rb") as fff:
        mut = pickle.load(fff)
    fff.close()
    formula_mut, sqrt_count_2 = aggregatePath(mut, rtype)
    if not formula_mut:
        print("false") #nonequivalent
        tmp_line = f"{mutname}, 0\n"
    else:
        formula = addDeclareFun(ori, sqrt_count_1 + sqrt_count_2) + aggregateFormula(formula_ori, formula_mut, rtype)
        print(formula)
        # print(ori)
        result, counterexample = solve(formula)
        if result == "unsat": # equivalent
            print("true") 
            tmp_line = f"{mutname}, 1, -\n"
        else:
            if result=="unknown":
                assert counterexample==""
                print("unknown")
                tmp_line = f"{mutname}, -1, {ctex}\n"    
            else:
                ctex = str(counterexample).replace(",", ";").replace(" ", "").replace("\n", "").replace("\t", "")
                print(f"false, {ctex}")
                tmp_line = f"{mutname}, 0, {ctex}\n"
        
    if os.path.exists(PATHEQUI):
        with open(PATHEQUI, "a") as ffff:
            ffff.write(tmp_line)
    else:
        with open(PATHEQUI, "w") as ffff:
            ffff.write("mutant name, is equivalent?, distinguished by\n") #header
            ffff.write(tmp_line)
    ffff.close()
        
    

if __name__ == '__main__':
    # main()
    # stripReplace("{{V12}, {V13}, {V14}, {V15}, {V16}, {V17}, {V18}, {V19}, {V20}, ({V6}) + (1), ({V7}) + (1), 32767, {V9}, {V10}, {V0}, {V1}, {V2}, {V3}, {V4}, {V5}}", "int[]")
    # print(preorder('(2.7182818284^V+2.7182818284^(0-(V)))/2'))
    
    # print(stripReplace('(0) < ({V0})'))
    # print(stripReplace('4*(WIDEN-I(V))==3+WIDEN-I(VVVVV)'))
    # with open(f"programs/Bubble/log/original.pkl", "rb") as ff:
        # ori = pickle.load(ff)
    # ff.close()
    # formula_ori, sqrt_count = aggregatePath(ori)
    # print(formula_ori)
    # tmp = addDeclareFun(ori, sqrt_count)
    # formula = addDeclareFun(ori, sqrt_count) + aggregateFormula(formula_ori, formula_ori, 'int[]')
    # print(formula)
    # formula = "(declare-const sqrt_1 Real) (assert (= 3 (+ -3 sqrt_1)))"
    # formula = "(declare-const V Int) (assert (and (and (< V 28) (<= (- 0 14) V)) (<= 6 V)) )"
    # s.set("timeout", 10000)
    # print(s.sexpr())
    formula = "(declare-const sqrt_1 Real) (assert (and (> sqrt_1 0) (= 3 (* sqrt_1 sqrt_1))))"
    s = Solver()
    s.from_string(formula)
    print(s.check())
    print(s.model())