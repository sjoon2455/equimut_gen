import argparse
import subprocess
import os
import shutil
from rdflib import Graph
from collections import defaultdict
import difflib
import glob

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
        if int(mut_line_num) in val:
            if len(val) > 1 and n < size-1 and blockToLineNum[n+1][0] == val[-1]: #not the end, and the start of next block is the line number
                return key+1
            else:
                return key


def splitline(line, delim):
    a, b, c = line.split(delim)[0], line.split(delim)[1], line.split(delim)[2]
    d = delim.join(line.split(delim)[3:])
    return a, b, c, d

def getPSOS(path, blockToLineNum, killstatus):
    mutantOnNode, mutantWithOperator = {}, {}
    with open(path, 'r') as f:
        for line in f.readlines():
            mut_name, mut_line_num = line.split(":")[0], line.split(":")[1]
            operator = mut_name.split("_")[0]
            mut_line_num = int(mut_line_num)
            graph_node = str(getGraphNode(mut_line_num, blockToLineNum))
            try:
                killed = killstatus[mut_name]
                # for OS, PS features
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

def getScore(li):
    return str(li[0]/li[1])


def getKillStatus(killpath):
    res = {}
    with open(killpath, 'r') as f:
        lines = f.readlines()
        killed, live, _ = lines
        _killed = killed.split(': ')[1:][0].split(",")[:-1]
        _live = live.split(': ')[1:][0].split(",")[:-1]
    f.close()
    for k in _killed:
        k = k.strip()
        res[k] = 1
    for l in _live:
        l = l.strip()
        res[l] = 0
    return res

def unwrapCSV(csv_path, func=''):
    if func != '':
        CSVPATH = os.path.join(csv_path, f"{func}_blockToLineNum.csv")
    else:
        CSVPATH = os.path.join(csv_path, "blockToLineNum.csv")
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

def createmutantstxtfromfile(dirpath, teul, prog, killstatus, function='', mutToLineNum={}):
    if teul == 'mutantbench':
        teul_path = f"/Users/sj/Downloads/tmp/MutantBench/programs/{prog}.java"
        csv_path = os.path.join('/'.join(dirpath.split('/')[:2]), 'arc_prim')
    else: # 'mujava'
        teul_path = f"mujava/session_{prog}/result/{prog}/original/{prog}.java"
        csv_path = os.path.join('/'.join(dirpath.split('/')[:2]), 'arc_prim_my')

    blockDict, mutantOnNodeDict, mutantWithOperatorDict = {}, {}, {}
    if prog=='QuickSort':
        blockDict['quicksort'] = unwrapCSV(csv_path, 'quicksort')
        blockDict['sort'] = unwrapCSV(csv_path, 'sort')
        blockDict['swap'] = unwrapCSV(csv_path, 'swap')
        mutantOnNode, mutantWithOperator = getPSOS(f"mujava/session_{prog}/result/{prog}/traditional_mutants/mutation_log", blockDict['quicksort'], killstatus)
        mutantOnNodeDict['quicksort'] = mutantOnNode
        mutantWithOperatorDict['quicksort'] = mutantWithOperator
        
        mutantOnNode, mutantWithOperator = getPSOS(f"mujava/session_{prog}/result/{prog}/traditional_mutants/mutation_log", blockDict['sort'], killstatus)
        mutantOnNodeDict['sort'] = mutantOnNode
        mutantWithOperatorDict['sort'] = mutantWithOperator
        
        mutantOnNode, mutantWithOperator = getPSOS(f"mujava/session_{prog}/result/{prog}/traditional_mutants/mutation_log", blockDict['swap'], killstatus)
        mutantOnNodeDict['swap'] = mutantOnNode
        mutantWithOperatorDict['swap'] = mutantWithOperator
        
    elif prog=='Defroster':
        blockDict['defroster'] = unwrapCSV(csv_path, 'defroster')
        mutantOnNode, mutantWithOperator = getPSOS(f"mujava/session_{prog}/result/{prog}/traditional_mutants/mutation_log", blockDict['defroster'], killstatus)
        if teul == 'mujava':
            blockDict['Exception_handler'] = unwrapCSV(csv_path, 'Exception_handler')

    else:
        blockToLineNum = unwrapCSV(csv_path)
        mutantOnNode, mutantWithOperator = getPSOS(f"mujava/session_{prog}/result/{prog}/traditional_mutants/mutation_log", blockToLineNum, killstatus)


    if function != '':
        func = '_'.join(function.split('_')[1:]).split('(')[0]
        blockToLineNum = unwrapCSV(csv_path, func)
    
    with open(teul_path, 'r') as fo:
        o_lines = fo.readlines()
    fo.close()

    for mut in os.listdir(dirpath):
        if mut.endswith('.java') and '_' in mut:
            if teul == "mujava":
                try:
                    mut_line_num = mutToLineNum[mut.split('.')[0]]
                except:
                    mut_line_num = 0
                if prog == 'QuickSort':
                    individual_addmutantstxtfrommut(dirpath, mut, o_lines, blockDict, mutantOnNode, mutantWithOperator, mut_line_num, flag=1)
                else:
                    individual_addmutantstxtfrommut(dirpath, mut, o_lines, blockDict, mutantOnNode, mutantWithOperator, mut_line_num)
            else:
                if prog == 'QuickSort':
                    individual_addmutantstxtfrommut(dirpath, mut, o_lines, blockDict, mutantOnNode, mutantWithOperator, flag=1)
                else:
                    individual_addmutantstxtfrommut(dirpath, mut, o_lines, blockDict, mutantOnNode, mutantWithOperator)

def getMinusLines(diff):
    res = []
    for l in diff.split('\n')[2:]:
        if l.startswith('-'):
            res.append(l.strip())
    return res
    
def unwrapMutationLog(prog):
    mutToLineNum = {}
    with open(f"mujava/session_{prog}/result/{prog}/traditional_mutants/mutation_log", 'r') as f:
        lines = f.readlines()
    f.close()
    for line in lines:
        mut, lineNum = line.split(':')[0], line.split(':')[1]
        mutToLineNum[mut] = lineNum
    return mutToLineNum

def individual_addmutantstxtfrommut(dirpath, mut, o_lines, blockDict, mutantOnNode, mutantWithOperator, mut_line_num=0, flag=0):
    with open(os.path.join(dirpath, mut), 'r') as fm:
        m_lines = fm.readlines()
    fm.close()
    diff = difflib.unified_diff(o_lines, m_lines)
    diff = ''.join(diff)
    if mut_line_num == 0: # this mutant is from mutantbench
        if diff=='':
            return
        mut_line_num = diff.splitlines()[2].split("-")[1].split(",")[0]
    func = getFunction(mut_line_num, o_lines, Flag=1)
    blockToLineNum = blockDict[func]
    if flag:
        mutantOnNode = mutantOnNode[func]
        mutantWithOperator = mutantWithOperator[func]
    mut_name = mut.split('.')[0] #trim .java at the end
    operator = mut.split('_')[0]
    minusLines = getMinusLines(diff) 
    if len(minusLines) == 0:
        return
    elif len(minusLines) > 1:
        type_statement = 'Block'
    else:
        mut_line = minusLines[0]
        type_statement = getTypeStatementFromCode(mut_line)
    graph_node = getGraphNode(mut_line_num, blockToLineNum)
    # print(mut_line_num, mut)
    if not graph_node:
        return
        assert False
        
    graph_node = str(graph_node)
    if 'nonequivalent' in dirpath:
        status = "Nonequivalent"
    else:
        status = "Equivalent"
    try:
        positionScore = getScore(mutantOnNode[graph_node])
    except:
        positionScore = '1' # abs, vr, ...
    try: 
        operatorScore = getScore(mutantWithOperator[operator])
    except: 
        operatorScore = '1' # abs, vr, ...

    mut_name_line = "Mutant name: " + mut_name
    status_line = "Status: " + status
    operator_line = "Operator: " + operator
    type_statement_line = "Type statement: " + type_statement
    position_score_line = "Position score: " + positionScore
    operator_score_line = "Operator score: " + operatorScore
    graph_node_line = "Program graph node: " + graph_node

    new_lines = ["#\n", mut_name_line, "\n", status_line, "\n", operator_line, "\n", type_statement_line, "\n", position_score_line, "\n", operator_score_line, "\n", graph_node_line, "\n\n"]
    with open(os.path.join(dirpath, "mutants.txt"), 'a') as f:
        for line in new_lines:
            f.write(line)
    f.close()
    

def getFunction(mut_line_num, teul, Flag=0):
    if Flag==0:
        with open(teul, 'r') as f:
            lines = f.readlines()
        f.close()
    else:
        lines = teul
    for i in range(int(mut_line_num), -1, -1):
        line = lines[i]
        if ('public' in line or 'private' in line) and 'class ' not in line: #function line
            print(line)
            if 'defroster' in line:
                return 'defroster'
            elif 'Exception_' in line:
                return 'Exception_handler'
            elif 'quicksort' in line:
                return 'quicksort'
            elif 'sort' in line:
                return 'sort'
            elif 'swap' in line:
                return 'swap'


def createmutantstxtfromnotepad(dirpath, teul, prog, killstatus):
    if teul == 'mutantbench':
        csv_path = os.path.join('/'.join(dirpath.split('/')[:2]), 'arc_prim')
        teul_path = f"/Users/sj/Downloads/tmp/MutantBench/programs/{prog}.java"
    else: # 'mujava'
        csv_path = os.path.join('/'.join(dirpath.split('/')[:2]), 'arc_prim_my')
        teul_path = f"mujava/session_{prog}/result/{prog}/original/{prog}.java"
    blockDict = {}
    if prog=='QuickSort':
        blockDict['quicksort'] = unwrapCSV(csv_path, 'quicksort')
        blockDict['sort'] = unwrapCSV(csv_path, 'sort')
        blockDict['swap'] = unwrapCSV(csv_path, 'swap')
    elif prog=='Defroster':
        blockDict['defroster'] = unwrapCSV(csv_path, 'defroster')
        mutantOnNode, mutantWithOperator = getPSOS(f"mujava/session_{prog}/result/{prog}/traditional_mutants/mutation_log", blockDict['defroster'], killstatus)
        if teul == 'mujava':
            blockDict['Exception_handler'] = unwrapCSV(csv_path, 'Exception_handler')

    else:
        blockToLineNum = unwrapCSV(csv_path)
        mutantOnNode, mutantWithOperator = getPSOS(f"mujava/session_{prog}/result/{prog}/traditional_mutants/mutation_log", blockToLineNum, killstatus)
    with open(teul_path, 'r') as f:
        o_lines = f.readlines()
    f.close()
    with open(os.path.join(dirpath, 'dataset.txt'), 'r') as f:
        lines = f.readlines()
    f.close()
    count = 0
    for n, line in enumerate(lines):
        line = line.strip()
        if line.startswith('@'):
            count += 1
            operator = lines[n-1].strip()
            mut_name = f"{operator}_{count}"
            mut_line_num = int(line.split("-")[1].split("+")[0])
            mut_line = o_lines[mut_line_num]
            
            if prog in ['QuickSort', 'Defroster']:
                func = getFunction(mut_line_num, teul_path)
                print(mut_line_num, teul_path)
                blockToLineNum = blockDict[func]
            type_statement = getTypeStatementFromCode(mut_line)
            graph_node = getGraphNode(mut_line_num, blockToLineNum)
            if not graph_node:
                assert False
            graph_node = str(graph_node)
            if 'nonequivalent' in dirpath:
                status = "Nonequivalent"
            else:
                status = "Equivalent"
            try:
                positionScore = getScore(mutantOnNode[graph_node])
            except:
                positionScore = '1' # abs, vr, ...
            try: 
                operatorScore = getScore(mutantWithOperator[operator])
            except: 
                operatorScore = '1' # abs, vr, ...

            mut_name_line = "Mutant name: " + mut_name
            status_line = "Status: " + status
            operator_line = "Operator: " + operator
            type_statement_line = "Type statement: " + type_statement
            position_score_line = "Position score: " + positionScore
            operator_score_line = "Operator score: " + operatorScore
            graph_node_line = "Program graph node: " + graph_node

            new_lines = ["#\n", mut_name_line, "\n", status_line, "\n", operator_line, "\n", type_statement_line, "\n", position_score_line, "\n", operator_score_line, "\n", graph_node_line, "\n\n"]

            with open(os.path.join(dirpath, "mutants.txt"), 'a') as f:
                for line in new_lines:
                    f.write(line)
            f.close()
            

def main():
    parser = argparse.ArgumentParser(description='')
    parser.add_argument('--type', '-t', help='dataset or file', default="dataset")
    parser.add_argument('--dirpath', '-m', help='dir path?', default="programs/Bisect/myequivalent/")
    parser.add_argument('--teul', '-a', help='mutantbench or mujava', default="mutantbench")
    
    args = parser.parse_args()
    _type = args.type
    dirpath = args.dirpath
    _dataset = args.teul
    progname = dirpath.split("/")[1]
    
    if os.path.isfile(os.path.join(dirpath, 'mutants.txt')):
        os.remove(os.path.join(dirpath, 'mutants.txt'))

    killstatus = getKillStatus(f"mujava/session_{progname}/result/{progname}/traditional_mutants/mutant_list")
    # 다 equivalent 이다.
    if _type == 'dataset' and _dataset == 'mutantbench':
        createmutantstxtfromnotepad(dirpath, 'mutantbench', progname, killstatus)
    elif _type == 'dataset' and _dataset == 'mujava':
        createmutantstxtfromnotepad(dirpath, 'mujava', progname, killstatus)
    elif _type == 'file' and _dataset == 'mujava': #myequivalent
        mutToLineNum = unwrapMutationLog(progname)
        for f in os.listdir(dirpath):
            if os.path.isdir(os.path.join(dirpath,f)):
                if f == '.DS_Store':
                    continue
                createmutantstxtfromfile(os.path.join(dirpath, f), 'mujava', progname, killstatus, function=f, mutToLineNum=mutToLineNum)
            if f.endswith(".java"):
                createmutantstxtfromfile(dirpath, 'mujava', progname, killstatus, mutToLineNum=mutToLineNum)
                
        
        
    elif _type == 'file' and _dataset == 'mutantbench':
        createmutantstxtfromfile(dirpath, 'mutantbench', progname, killstatus)



if __name__ == '__main__':
    main()