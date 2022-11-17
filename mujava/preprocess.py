import argparse
import subprocess
import os
import shutil
from rdflib import Graph
from collections import defaultdict
import difflib
import glob

def editlistoffunc(path, newpath):
    new_lines = []
    with open(path, 'r') as f:
        for line in f.readlines():
            tmp_line = line.split("(")[0]
            tmp_line = tmp_line.split("_")[1]
            new_lines.append(tmp_line)
    f.close()
    with open(newpath, 'w') as f:
        f.writelines(new_lines)
    f.close()
    return


def getTypeStatementFromCode(descriptor_line):
    typeStatement = ''
    if descriptor_line.__contains__('\n') and descriptor_line.replace('\n', '', 1).__contains__('\n'):
        typeStatement = 'Block'
    elif descriptor_line.__contains__('while'):
        typeStatement = 'While'
    elif descriptor_line.__contains__('for'):
        typeStatement = 'For'
    elif descriptor_line.__contains__('if'):
        typeStatement = 'If'
    elif descriptor_line.__contains__('=') or descriptor_line.__contains__('++') or descriptor_line.__contains__('--'):
        typeStatement = 'Assignment'
    elif descriptor_line.__contains__('return'):
        typeStatement = 'Return'
    elif descriptor_line.__contains__('('):
        typeStatement = 'Function Call'
    else:
        typeStatement = 'Declaration'

    return typeStatement

def getGraphNode(mut_line_num, blockToLineNum):
    size = len(blockToLineNum)
    if size == 1:
        return 1
    for n, (key, val) in enumerate(blockToLineNum.items()):
        if mut_line_num in val:
            if len(val) > 1 and n < size-1 and blockToLineNum[n+1][0] == val[-1]: #not the end, and the start of next block is the line number
                return key+1
            else:
                return key

# mv file to nonequivalent/ and alive/
def mvfile(mut, folder, progpath, progname, FLAG):
    # assert FLAG in ['nonequivalent', 'alive']
    folder_method = folder.split('/')[-2]
    filename = f"{progname}.java"
    src = os.path.join(folder, filename)
    dest = os.path.join(progpath, FLAG, folder_method, f"{mut}.java")
    os.makedirs(os.path.dirname(dest), exist_ok=True)
    shutil.copy(src, dest)

def splitline(line, delim):
    a, b, c = line.split(delim)[0], line.split(delim)[1], line.split(delim)[2]
    d = delim.join(line.split(delim)[3:])
    return a, b, c, d


def getKillStatusAndMakeMutationLog(path, killpath, mutpath, progpath, progname):
    res = {}
    with open(path, 'r') as f:
        metalines = f.readlines()
    f.close()

    with open(killpath, 'r') as f:
        lines = f.readlines()
        killed, live, _ = lines
        _killed = killed.split(': ')[1:][0].split(",")[:-1]
        _live = live.split(': ')[1:][0].split(",")[:-1]
        
        # path_nonequi_mutation_log = os.path.join(progpath, 'nonequivalent', "mutant_list")
        # with open(path_nonequi_mutation_log, 'a') as ff:
        #     for mut in _killed:
        #         mut = mut.strip()
        #         res[mut] = 'Killed'
        #         mvfile(mut, mutpath, progpath, progname, 'nonequivalent')
        #         for line in metalines:
        #             if line.split(":")[0] == mut:
        #                 name_, line_, before_, after_ = splitline(line, ":")
        #                 name_ = name_.lstrip()
        #                 line_ = line_.lstrip()
        #                 before_ = before_.lstrip()
        #                 after_ = after_.lstrip()
        #                 ff.write(f"{name_}:{line_}:{before_}:{after_}")
        # ff.close()
        n = 0
        for mut in _live:
            mut = mut.strip()
            if mut in ['AOIU_26', 'AOIU_27', 'AOIU_28', 'AOIU_29', 'AOIU_30', 'AOIU_31', 'AOIU_32', 'AOIU_33', 'AOIU_34', 'AOIU_35', 'AOIU_36', 'AOIU_37', 'AOIU_38', 'AOIU_39', 'AOIU_40', 'AOIU_41', 'AOIU_42', 'AOIU_43', 'AOIU_44', 'AOIU_45', 'AOIS_173', 'AOIS_174', 'AOIS_175', 'AOIS_176', 'AOIS_177', 'AOIS_178', 'AOIS_179', 'AOIS_180', 'AOIS_181', 'AOIS_182', 'AOIS_183', 'AOIS_184', 'AOIS_185', 'AOIS_186', 'AOIS_187', 'AOIS_188', 'AOIS_189', 'AOIS_190', 'AOIS_191', 'AOIS_192', 'AOIS_193', 'AOIS_194', 'AOIS_195', 'AOIS_196', 'AOIS_197', 'AOIS_198', 'AOIS_199', 'AOIS_200', 'AOIS_201', 'AOIS_202', 'AOIS_203', 'AOIS_204', 'AOIS_205', 'AOIS_206', 'AOIS_207', 'AOIS_208', 'AOIS_209', 'AOIS_210', 'AOIS_211', 'AOIS_212', 'LOI_49', 'LOI_50', 'LOI_51', 'LOI_52', 'LOI_53', 'LOI_54', 'LOI_55', 'LOI_56', 'LOI_57', 'LOI_58', 'LOI_59', 'LOI_60', 'LOI_61', 'LOI_62', 'LOI_63', 'LOI_64', 'LOI_65', 'LOI_66', 'LOI_67', 'LOI_68', 'SDL_100']:
                continue
            folder = glob.glob(mutpath+"/**/"+mut, recursive=True)[0]
            n += 1
        print(n)
            # folder = '/'.join(folder.split("/")[4:])
            # mvfile(mut, folder, progpath, progname, 'alive')
        # with open(path_alive_mutation_log, 'a') as ff:
    #         for mut in live:
    #             mut = mut.strip()
    #             res[mut] = 'Alive'
    #             mvfile(mut, mutpath, progpath, progname, 'alive')
    #             for line in metalines:
    #                 if line.split(":")[0] == mut:
    #                     name_, line_, before_, after_ = splitline(line, ":")
    #                     name_ = name_.lstrip()
    #                     line_ = line_.lstrip()
    #                     before_ = before_.lstrip()
    #                     after_ = after_.lstrip()
    #                     ff.write(f"{name_}:{line_}:{before_}:{after_}")
    #     ff.close()
    # f.close()
    return res

def getPSOS(path, blockToLineNum, killstatus):
    mutantOnNode = {}
    mutantWithOperator = {}
    with open(path, 'r') as f:
        for line in f.readlines():
            mut_name, mut_line_num = line.split(":")[0], line.split(":")[1]
            operator = mut_name.split("_")[0]
            mut_line_num = int(mut_line_num)
            graph_node = str(getGraphNode(mut_line_num, blockToLineNum))
            try:
                status = killstatus[mut_name]
                # for OS, PS features
                killed = 1 if status=="Killed" else 0
                if graph_node not in mutantOnNode:
                    mutantOnNode[graph_node] = [killed, 1]
                else:
                    mutantOnNode[graph_node][0] += killed
                    mutantOnNode[graph_node][1] += 1
                if operator not in mutantWithOperator:
                    mutantWithOperator[operator] = [killed, 1]
                else:
                    mutantWithOperator[operator][0] += killed
                    mutantWithOperator[operator][1] += 1
            except:
                continue
    return mutantOnNode, mutantWithOperator

def _getPSOS(operator, mut_line_num, blockToLineNum, mutantOnNode, mutantWithOperator):
    graph_node = str(getGraphNode(mut_line_num, blockToLineNum))
    if graph_node not in mutantOnNode:
        mutantOnNode[graph_node] = [0, 1]
    else:
        mutantOnNode[graph_node][1] += 1
    if operator not in mutantWithOperator:
        mutantWithOperator[operator] = [0, 1]
    else:
        mutantWithOperator[operator][1] += 1
    return mutantOnNode, mutantWithOperator

def getScore(li):
    return str(li[0]/li[1])

def unwrapCSV(newpath):
    tmp_tmp_path = newpath.split("/")[:4]+["blockToLineNum.csv"]
    CSVPATH = os.path.join(*tmp_tmp_path)
    blockToLineNum = {}
    with open(CSVPATH, 'r') as f:
        for n, line in enumerate(f.readlines()):
            tmp_list = line.split(",")
            blockNum = int(tmp_list[0])
            startLineNum = int(tmp_list[1])
            if n > 0:
                endLineNum = startLineNum
                blockToLineNum[blockNum-1] = list(range(tmp, endLineNum))
            tmp = startLineNum
    f.close()
    blockToLineNum[blockNum] = list(range(tmp, 1000)) #maximum number
    return blockToLineNum

def createlogfile(path, newpath, mutpath, killstatus):
    new_lines = []
    PROGNAME = newpath.split("/")[2]
    blockToLineNum = unwrapCSV(newpath)
    mutantOnNode, mutantWithOperator = getPSOS(path, blockToLineNum, killstatus)
    
    # log for equivalent muts in dataset
    res = prepare()
    
    for _, val in res.items():
        operator, mut_line_num, mut_line, mut_name = parse(val, PROGNAME)
        if operator=="a":
            continue
        mutantOnNode, mutantWithOperator = _getPSOS(operator, mut_line_num, blockToLineNum, mutantOnNode, mutantWithOperator)
        type_statement = getTypeStatementFromCode(mut_line)
        graph_node = getGraphNode(mut_line_num, blockToLineNum)
        if not graph_node:
            print(mut_line_num)
            print(PROGNAME)
        graph_node = str(graph_node)
        positionScore = getScore(mutantOnNode[graph_node])
        operatorScore = getScore(mutantWithOperator[operator])

        mut_name_line = "Mutant name: " + mut_name
        status_line = "Status: Equivalent"
        operator_line = "Operator: " + operator
        type_statement_line = "Type statement: " + type_statement
        position_score_line = "Position score: " + positionScore
        operator_score_line = "Operator score: " + operatorScore
        graph_node_line = "Program graph node: " + graph_node

        new_lines += ["#\n", mut_name_line, "\n", status_line, "\n", operator_line, "\n", type_statement_line, "\n", position_score_line, "\n", operator_score_line, "\n", graph_node_line, "\n\n"]

    
    with open(path, 'r') as f:
        for line in f.readlines():
            mut_name, mut_line_num, func_name = line.split(":")[0], line.split(":")[1], line.split(":")[2]
            if mut_name not in killstatus:
                continue
            PROGPATH = os.path.join(mutpath, mut_name, PROGNAME+".java")
            operator = mut_name.split("_")[0]
            mut_line_num = int(mut_line_num)
            
            try:
                with open(PROGPATH, 'r') as ff:
                    lines = ff.readlines() 
                    mut_line = lines[mut_line_num-1]
                ff.close()
                type_statement = getTypeStatementFromCode(mut_line)
                graph_node = getGraphNode(mut_line_num, blockToLineNum)
                if not graph_node:
                    continue
                graph_node = str(graph_node)
                status = killstatus[mut_name]
                positionScore = getScore(mutantOnNode[graph_node])
                operatorScore = getScore(mutantWithOperator[operator])

                mut_name_line = "Mutant name: " + mut_name
                status_line = "Status: " + status
                operator_line = "Operator: " + operator
                type_statement_line = "Type statement: " + type_statement
                position_score_line = "Position score: " + positionScore
                operator_score_line = "Operator score: " + operatorScore
                graph_node_line = "Program graph node: " + graph_node

                new_lines += ["#\n", mut_name_line, "\n", status_line, "\n", operator_line, "\n", type_statement_line, "\n", position_score_line, "\n", operator_score_line, "\n", graph_node_line, "\n\n"]
            except Exception as e:
                continue
    f.close()
    
    with open(newpath, 'w') as f:
        f.writelines(new_lines)
    f.close()

    return 

def modifyCreateLogFile(newpath, mutname, equivalency):

    with open(newpath, 'r') as f:
        lines = f.readlines()
        for n, line in enumerate(lines):
            if "Mutant name: " in line and mutname in line:
                nn = n
        assert "Status" in lines[nn+1]
        lines[nn+1] = "Status: " + equivalency
    f.close()
    
    with open(newpath, 'w') as f:
        f.writelines(lines)
    f.close()


def prepare():
    graph = Graph()
    a = graph.parse("../../MutantBench/mutantbench/dataset.ttl", format='turtle')
    res = defaultdict(list)
    for line in a:
        first = str(line[0])
        if 'mb:operator' in first or 'mb:person' in first or 'mb:paper' in first or 'mb:program' in first:
            continue
        second, third = str(line[1]), str(line[2])
        res[first].append(second)
        res[first].append(third)
    return res

def makeupProgramAndSave(program, diff, MUTPATH, PROGPATH):
    NAME = MUTPATH.split("/")[-1]
    MUTLOGPATH = "/".join(MUTPATH.split("/")[:-1]+['mutants.txt'])
    new_lines = []
    before = diff.split("\n")[1][1:].strip()
    if before.startswith("System.out.printf"):
        return -1, ""
    after = diff.split("\n")[2][1:].strip()
    tmp = 0
    tmp_line = ""
    with open(PROGPATH, 'r') as f:
        lines = f.readlines()
        for n, line in enumerate(lines):
            if program=="Profit":
                if before.startswith("else if"):
                    before = before[5:]
                elif before == "bonus=bonus1+(i-100000)*0.075;":
                    before = "bonus = bonus1 + i * 0.075 - 100000 * 0.075;"
                elif before == "bonus=bonus2+(i-200000)*0.05;":
                    before = "bonus = bonus2 + i * 0.05 - 200000 * 0.05;"
                elif before == "bonus=bonus4+(i-400000)*0.03;":
                    before = "bonus = bonus4 + i * 0.03 - 400000 * 0.03;"
                elif before == "bonus=bonus6+(i-600000)*0.015;":
                    before = "bonus = bonus6 + i * 0.015 - 600000 * 0.015;"
                elif before == "bonus=bonus10+(i-1000000)*0.01;":
                    before = "bonus = bonus10 + i * 0.01 - 1000000 * 0.01;"
            output_list = [li[2:] for li in difflib.ndiff(line.strip(), before) if li[0] != ' ' and li[2:] not in ['(', ')', '{', '}', ';', ' ', '\n', '\t']]
            if line == before or not output_list: # same
                new_lines.append(after + "\n")
                tmp = n
                tmp_line = after
            else:
                new_lines.append(line)
    f.close()
        
    with open(MUTPATH, 'w+') as f:
        f.writelines(new_lines)
    f.close()

    with open(MUTLOGPATH, "a") as f:
        tmp_line = f'{NAME}::{tmp_line}::{before}::{after}\n'
        f.write()
    f.close()

    return tmp+1, tmp_line

# if(year%400==0||(year%4==0&&year%100!=0))
# sum=sum+day;

#convert mutantbench OP to mujava OP (coarser)
def convertOP(op):
    if op == "ABSI":
        return "ABS"
    elif op.endswith("SDL"):
        return "SDL"
    elif op == "CR":
        return "SVR" 
    elif op == "ROD":
        return "ROR"
    elif op == "SAR":
        return "ASRS"
    elif op == "SEOR":
        return "COR"
    elif op == "SEOI":
        return "COI"
    elif op == "SEOD":
        return "COD"
    else:
        return op

# parse equivalent dataset
def parse(val, PROGNAME):
    isequivalent = operator = program = ''
    abort = False
    for n, e in enumerate(val):
        if 'mb:program#' in e:
            fullprogram = e.split('mb:program#')[1]
            program = fullprogram[:-5]
            if program != PROGNAME:
                abort = True
                break
        elif '/difference' in e:
            diff = val[n+1]
        elif 'mb:operator#' in e:
            operator = e.split('mb:operator#')[1]
            operator = convertOP(e.split('mb:operator#')[1])
            operator_count[operator] += 1
        elif '/equivalence' in e:
            isequivalent = val[n+1]
    
    if abort or operator=="" or isequivalent != "true":
        return "a", 10, "a", "a"

    MUTFILENAME = f'{operator}_{operator_count[operator]}'     # to be saved as this name
    MUTPATH = f'../programs/{program}/equivalent/{MUTFILENAME}'     # to be saved here
    PROGPATH = f"../mujava/session_{program}/result/{program}/original/{program}.java"  # standard file 
    linenumber, line = makeupProgramAndSave(program, diff, MUTPATH, PROGPATH)
    if linenumber == -1:
        return "a", 10, "a", "a"
    return operator, linenumber, line, MUTFILENAME


def createlogfile_myequivalent(mutpath, newpath, myequivalentpath, equivalency):
    assert equivalency == "Equivalent"
    target_name = mutpath.split("/")[-1].split(".")[0]
    with open(myequivalentpath, 'r') as f:
        lines = f.readlines()
    f.close()
    for line in lines:
        name = line.split(":")[0]
        if name == target_name:
            break

    mut_name, mut_line_num, func_name = line.split(":")[0], line.split(":")[1], line.split(":")[2]
    operator = mut_name.split("_")[0]
    mut_line_num = int(mut_line_num)
    
    try:
        with open(mutpath, 'r') as ff:
            lines = ff.readlines() 
            mut_line = lines[mut_line_num-1]
        ff.close()
        type_statement = getTypeStatementFromCode(mut_line)
        blockToLineNum = unwrapCSV(newpath)
        graph_node = getGraphNode(mut_line_num, blockToLineNum)
        if not graph_node:
            assert False
        graph_node = str(graph_node)
        status = "Equivalent"
        positionScore = getScore(mutantOnNode[graph_node])
        operatorScore = getScore(mutantWithOperator[operator])

        mut_name_line = "Mutant name: " + mut_name
        status_line = "Status: " + status
        operator_line = "Operator: " + operator
        type_statement_line = "Type statement: " + type_statement
        position_score_line = "Position score: " + positionScore
        operator_score_line = "Operator score: " + operatorScore
        graph_node_line = "Program graph node: " + graph_node

        new_lines += ["#\n", mut_name_line, "\n", status_line, "\n", operator_line, "\n", type_statement_line, "\n", position_score_line, "\n", operator_score_line, "\n", graph_node_line, "\n\n"]
    except Exception as e:
        assert False

    with open(myequivalentpath, 'a') as f:
        f.write(line)
    f.close()
    

def main():
    parser = argparse.ArgumentParser(description='')
    parser.add_argument('--type', '-t', help='what are you preprocessing?', default="listoffunc")
    parser.add_argument('--path', '-p', help='path to unit?', default=".")
    parser.add_argument('--newpath', '-n', help='new path to unit?', default=".")
    parser.add_argument('--progpath', '-m', help='path to mutant?', default=".")
    parser.add_argument('--killlogpath', '-k', help='path to kill log?', default=".")
    parser.add_argument('--equivalency', '-e', help='equivalent?', default="Y")
    parser.add_argument('--mutname', '-a', help='mut name?', default=".")
    parser.add_argument('--myequivalentpath', '-y', help='my equivalent path?', default=".")
    
    args = parser.parse_args()
    _type = args.type
    path = args.path
    newpath = args.newpath # ../programs/Day/log/mutants.txt
    mutpath = args.progpath # ../programs/Day/alive/AOIS_1.java
    killpath = args.killlogpath
    equivalency = "Equivalent" if args.equivalency=="Y" else "Nonequivalent"
    mutname = args.mutname
    myequivalentpath = args.myequivalentpath # ../programs/Day/myequivalent/mutants.txt 
    progpath = newpath.split("/log")[0] #  ../programs/Profit
    progname = progpath.split("/")[-1]

    if _type == "listoffunc":
        editlistoffunc(path, newpath)
    
    elif _type == "log": 
        getKillStatusAndMakeMutationLog(path, killpath, mutpath, progpath, progname)
        # createlogfile(path, newpath, mutpath, killstatus)
    
    elif _type == "mylog":
        createlogfile_myequivalent(mutpath, newpath, myequivalentpath, equivalency)

    else: # type == singlelog
        modifyCreateLogFile(newpath, mutname, equivalency)



if __name__ == '__main__':
    operator_count = defaultdict(int)
    main()