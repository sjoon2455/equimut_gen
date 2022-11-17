import os
from collections import defaultdict
import re
import pickle
import argparse
# from checkSAT import checkPositive, addDeclareFun, aggregatePath, stripReplace
from checkSAT import addDeclareFun, aggregatePath, stripReplace
import csv
from time import time

def dividePathsAll(program, fname):
    FILE_PATH = f"./example/out/all_{program}_{fname}.txt"
    assert os.path.exists(FILE_PATH)
    
    with open(FILE_PATH) as f:
        paths = []
        path = []
        check = False
        for line in f:
            if "[0]" in line and "." in line and not any(c.isalpha() for c in line) and not ".1[0]" in line:
                paths.append(path)
                path = []
            line = line.strip()
            if "&& " in line:
                tmpli = line.split("&& ")
                path = path + tmpli
            else:
                path.append(line)
        paths.append(path)
    f.close()
    return paths

def dividePathsLeaves(paths):
    paths_res = []
    for path in paths:
        tmp = defaultdict(list)
        count = -1
        collect = False
        for line in path:
            if "." in line and not any(c.isalpha() for c in line):
                count += 1
                collect = True
            if collect:
                tmp[count].append(line)
        res = tmp[count-1] + tmp[count]
        paths_res.append(res)
    return paths_res

def mutDividePathsLeaves(program, fname):
    FILE_PATH = f"./example/out/leaves_{program}_{fname}.txt"
    assert os.path.exists(FILE_PATH)
    with open(FILE_PATH) as f:
        paths = []
        path = []
        check = False
        for line in f:
            line = line.strip()
            if not check and "." in line and not any(c.isalpha() for c in line):
                check = True
                path.append(line)
                continue
            if check:
                if "path" in line:
                    check = False
                    paths.append(path)
                    path = []
                    continue
                else:
                    path.append(line)
    return paths


def oriDividePathsLeaves(program, fname):
    FILE_PATH = f"./example/out/original_leaves_{program}_{fname}.txt"
    assert os.path.exists(FILE_PATH)
    with open(FILE_PATH) as f:
        paths = []
        path = []
        check = False
        for line in f:
            line = line.strip()
            if not check and "." in line and not any(c.isalpha() for c in line):
                check = True
                path.append(line)
                continue
            if check:
                if "path" in line:
                    check = False
                    paths.append(path)
                    path = []
                    continue
                else:
                    path.append(line)
    return paths


def getBranchedSpot(milestone, milestones):
    m = milestone.rsplit(".", 1)[0]
    assert m in milestones
    return m

def changePathIntoLineNum(paths):
    res = {}
    lines, milestones = [], []
    old_milestone, milestone = "", ""
    for path in paths:
        for line in path:
            if "." in line and not any(c.isalpha() for c in line) and "[" in line and "]" in line:
                old_milestone = milestone # save older one
                milestone = line.split("[")[0]
                if milestone not in milestones: # new milestone encountered!
                    if len(milestones) > 1: #when .1.2 encountered
                        m = getBranchedSpot(old_milestone, milestones)
                        res[old_milestone] = res[m] + lines[:]
                        lines = []
                    elif len(milestones) == 1: #when .1.1 encounterd
                        res[old_milestone] = lines[:]
                        lines = []
                    milestones.append(milestone) #when .1 encountered
                    
            elif "Source line:" in line:
                lineNum = int(line[line.find("(")+1:line.find(")")])
                if not lines:
                    lines.append(lineNum)
                elif lines and lines[-1] != lineNum:
                    lines.append(lineNum)

    m = getBranchedSpot(milestone, milestones)
    res[milestone] = res[m] + lines
    return list(res.values())
    

def parsePathAll(paths):
    lineToPathConditionProgramStateTuple = defaultdict(list)
    checkingBlock, checkingPathCondition, checkingProgramState, isResolveLine = False, False, False, False
    for path in paths:
        for line in path:
            if not checkingBlock: #searching for "source line" line
                if line.startswith("Source line:"): #searched!
                    lineNum = int(line[line.find("(")+1:line.find(")")])
                    if lineNum >= 1000:
                        continue
                    else:
                        checkingBlock = True
            else:
                if not checkingPathCondition and line.startswith("Path condition:"): #searching for "Path condition" line and "Local Variables" line
                    checkingPathCondition = True
                    pathConditions = []
                    continue
                elif not checkingProgramState and "Local Variables:" in line:
                    checkingProgramState = True
                    programStates = defaultdict(list)
                    continue
                    
                elif checkingPathCondition:
                    if "where:" in line:
                        isResolveLine = True
                        continue
                    if "Static store:" in line:
                        isResolveLine = False
                        checkingPathCondition = False
                        continue
                    if "pre_init(" in line or "Object" in line:
                        continue
                    assert "{" in line
                    if isResolveLine:
                        continue
                    else:
                        line = line.split(" &&")[0]
                        pathConditions.append(line)

                elif checkingProgramState:
                    if line == "}":
                        #update result
                        pc_ps = [pathConditions]
                        for var, vals in programStates.items():
                            assert len(vals) == 1
                            pc_ps.append([var, vals[0]])
                        if pathConditions not in [ll[0] for ll in lineToPathConditionProgramStateTuple[lineNum]]:
                            lineToPathConditionProgramStateTuple[lineNum].append(pc_ps)
                        checkingProgramState, checkingBlock = False, False #if local variable ended, all flags should be False
                        continue
                    assert line.startswith("Variable")
                    tmp_rhs = line.split("Name: ")[1] #   a, Type: I, Value: 1 (type: I)
                    tmp_variable = tmp_rhs.split(",")[0] # a
                    if "this" in tmp_variable:
                        continue
                    tmp_value = tmp_rhs.split("Value: ")[1].split(" (type: ")[0] # 1
                    if tmp_value not in programStates[tmp_variable]:
                        programStates[tmp_variable].append(tmp_value)
                else:
                    continue # none of our interest
    return lineToPathConditionProgramStateTuple
                    

def parsePathLeaves(paths, program):
    pathCondition_localVariable_return = []
    checkingBlock, checkingPathCondition, checkingProgramState, isResolveLine = False, False, False, False
    for path in paths:
        variableValuesTypes, pathConditions, returnValue, tmpnumret, decReturnValue = [], [], "          ", "          ", "          "
        checkingObject, thisObject = False, False
        for line in path:
            if not checkingBlock: #searching for "source line" line
                if "." in line and not any(c.isalpha() for c in line):
                    checkingBlock = True
                    
            else:
                if "Leaf state" in line and line != "Leaf state": #searched!
                    assert "returned value" in line or "raised exception" in line
                    if "raised exception" in line:
                        returnValue = '99999'
                    else:
                        returnValue = line.split("returned value: ")[1]
                        if returnValue.startswith("Object"):
                            checkingObject = True
                            tmpnumret = str(int(returnValue.split("[")[1][:-1])-1)
                            decReturnValue = returnValue.split("[")[0] + "[" + tmpnumret + "]"
                if not checkingPathCondition and line.startswith("Path condition:"): #searching for "Path condition" line and "Local Variables" line
                    checkingPathCondition = True
                    pathConditions = []
                    continue
                elif not checkingProgramState and "Local Variables:" in line:
                    checkingProgramState = True
                    continue
                    
                elif checkingPathCondition:
                    if "where:" in line:
                        isResolveLine = True
                        continue
                    if "Static store:" in line:
                        isResolveLine = False
                        checkingPathCondition = False
                        continue
                    if "pre_init(" in line or "Object" in line:
                        continue
                    # assert "{" in line
                    if line[-3:] == " &&":
                        line = line[:-3]
                    
                    if isResolveLine:
                        continue
                    else:
                        pathConditions.append(line)

                elif checkingObject:
                    if decReturnValue in line:
                        thisObject = True
                    if returnValue in line:
                        thisObject = True
                    if thisObject and "Items:" in line:
                        thisObject = False
                        checkingObject = False
                        returnValue = line.split("Items: ")[1] #overwrite returnvalue

                elif checkingProgramState:
                    if line == "}":
                        checkingProgramState, checkingBlock = False, False #if local variable ended, all flags should be False
                        continue
                    assert line.startswith("Variable")
                    if "Name: this" in line:
                        continue

                    # save variable types
                    assert "Name: " in line
                    variableName = line.split("Name: ")[1].split(", ")[0]
                    if not variableName.isidentifier():
                        if "__PARAM" in variableName or "__LOCAL" in variableName:
                            pass
                        else:
                            continue
                    variableType = line.split("type: ")[1].split(")")[0]
                    variableValue = line.split("Value: ")[1].split(" (t")[0]
                    if [variableName, variableValue, variableType] not in variableValuesTypes:
                        variableValuesTypes.append([variableName, variableValue, variableType])
                else:
                    continue # none of our interest
        if returnValue == "          ":
            continue
        for a, b, c in variableValuesTypes:
            if a in returnValue:
                returnValue = returnValue.replace(a, b)
        pathCondition_localVariable_return.append([pathConditions, returnValue, variableValuesTypes])
    return pathCondition_localVariable_return


def addMutateCandidate(d, lineNum, linenumdiff, lines, var, val, FLAG):
    newLineNum = lineNum-linenumdiff
    if FLAG=='VR':
        assert newLineNum > 0
        if (var, val) not in d[newLineNum]:
            d[newLineNum].append((var, val))
    else:
        if var in lines[lineNum-1]:
            assert newLineNum > 0
            if FLAG == 'SVR':
                if (var, val) not in d[newLineNum]:
                    d[newLineNum].append((var, val))
            else:
                if var not in d[newLineNum]:
                    d[newLineNum].append(var)
    return d

def decideMutation(paths, lines, linenumdiff, declareFuns):
    res_VR, res_SVR, _res_SVR, res_ABS = defaultdict(list), defaultdict(list), defaultdict(list), defaultdict(list)
    for lineNum, biglist in paths.items():
        newLineNum = lineNum-linenumdiff
        positiveList = [] #stores positive vars
        tmp_res_SVR = defaultdict(list)
        for iCount in range(len(biglist)):
            pclist, rst = biglist[iCount][0], biglist[iCount][1:]
            for var, val in rst:
                if "Object" in val or "<DEFAULT>" in val or "__LOCAL" in var: # could not be positive
                    continue
                val = stripReplace(val)
                isPositive = True
                try:
                    val_f = float(val) # if succeed, it is a number
                    res_SVR = addMutateCandidate(res_SVR, lineNum, linenumdiff, lines, var, val, "SVR")
                    tmp_res_SVR = addMutateCandidate(tmp_res_SVR, lineNum, linenumdiff, lines, var, val, "VR")
                    if val_f < 0:
                        isPositive = False
                    else:
                        isPositive = True
                except ValueError:
                    if checkPositive(val, pclist, declareFuns):
                        isPositive = True
                    else: 
                        isPositive = False
                    tmp_res_SVR = addMutateCandidate(tmp_res_SVR, lineNum, linenumdiff, lines, var, val, "VR")
                except Exception as e:
                    print("WHAT ERROR? ", e)
                if isPositive:
                    if var not in positiveList:
                        positiveList.append(var)
                else:
                    if var in positiveList:
                        positiveList.remove(var)
            
        for posvar in positiveList:
            res_ABS = addMutateCandidate(res_ABS, lineNum, linenumdiff, lines, posvar, '', "ABS") # lineNum: IfExample.java 기준.
        met2 = []
        lendict = len(tmp_res_SVR[newLineNum])
        for jCount in range(lendict-1):
            var1, val1 = tmp_res_SVR[newLineNum][jCount]
            for kCount in range(jCount+1, lendict):
                var2, val2 = tmp_res_SVR[newLineNum][kCount]
                if var1 == var2:
                    met2.append((var1, val1))           #(a, 3)      
                    met2.append((var2, val2))           #(a, 4)
        for v_, _v in met2:
            tmp_res_SVR[newLineNum].remove((v_, _v))
        
        lendict = len(tmp_res_SVR[newLineNum])
        for jCount in range(lendict-1):
            var1, val1 = tmp_res_SVR[newLineNum][jCount]
            for kCount in range(jCount+1, lendict):
                var2, val2 = tmp_res_SVR[newLineNum][kCount]
                if val1 == val2 and (var1, var2) not in res_VR[newLineNum]:
                    if var1 in lines[lineNum-1]:
                        res_VR[newLineNum].append((var1, var2))
                    elif var2 in lines[lineNum-1]:
                        res_VR[newLineNum].append((var2, var1))
                elif val1 != val2:
                    if (var1, var2) in res_VR[newLineNum]:
                        res_VR[newLineNum].remove((var1, var2))
                    elif (var2, var1) in res_VR[newLineNum]:
                        res_VR[newLineNum].remove((var2, var1))
                    if not res_VR[newLineNum]:
                        res_VR.pop(newLineNum, None)
    
    for k, v in res_SVR.items():
        met = defaultdict(list)
        for var, val in v:
            met[var].append(val)
        for var, vals in met.items():
            tmp_li = list(set(vals))
            if len(tmp_li) == 1:
                _res_SVR[k].append((var, tmp_li[0]))
                
    return _res_SVR, res_ABS, res_VR

def write(program, dic, FLAG):
    assert FLAG in ["ABS", "SVR", "VR", "LINE"]
    if FLAG == "LINE":
        with open(f"programs/{program}/log/pathInLine.csv", "w") as f:
            writer = csv.writer(f)
            for path in dic:
                writer.writerow(path)
        f.close()

    else:
        with open(f"programs/{program}/log/onesTo{FLAG}.csv", "w") as f:
            writer = csv.writer(f)
            for key, val in dic.items():
                if FLAG == "ABS":
                    tmp = [key] + val
                    writer.writerow(tmp)    
                else:
                    for v in val:
                        tmp = [key, v[0], v[1]]
                        writer.writerow(tmp)
        f.close()
            

def main():
    parser = argparse.ArgumentParser(description='')
    parser.add_argument('--prog', '-p', help='program name?', default="Bisect")
    parser.add_argument('--ismut', '-m', help='is this a mutant program?', default="N")
    parser.add_argument('--filename', '-f', help='file name?', default="AOIS_1.java")
    parser.add_argument('--rtype', '-r', help='return type', default='int')
    parser.add_argument('--funcname', '-q', help='function name', default='.')
    args = parser.parse_args()
    program = args.prog
    ismutant = True if args.ismut == "Y" else False
    mutname = args.filename
    mutname = mutname.split(".")[0]
    rtype = args.rtype
    funcname = args.funcname

    if not ismutant:
        paths_leaves = oriDividePathsLeaves(program, funcname)
        parsedPaths_leaves = parsePathLeaves(paths_leaves, program)
        with open(f"programs/{program}/log/original.pkl", "wb") as f:
            pickle.dump(parsedPaths_leaves, f)
        f.close()

        # paths_all = dividePathsAll(program, funcname)
        # parsedPaths_all = parsePathAll(paths_all)
        # paths_leaves = dividePathsLeaves(paths_all)
        # parsedPaths_leaves = parsePathLeaves(paths_leaves, program)
        # with open(f"programs/{program}/log/original.pkl", "wb") as f:
        #     pickle.dump(parsedPaths_leaves, f)
        # f.close()
        # _, sqrt_count = aggregatePath(parsedPaths_leaves, rtype)
        # declareFuns = addDeclareFun(parsedPaths_leaves, sqrt_count)
        # filepath = 'example/src/example/IfExample.java'
        # with open(filepath, 'r') as f:
        #     lines1 = f.readlines()
        #     len1 = len(lines1)
        # f.close()
        # filepath2 = f'programs/{program}/{program}.java'
        # with open(filepath2, 'r') as f:
        #     lines2 = f.readlines()
        #     len2 = len(lines2)
        # f.close()
        # assert len1 > len2
        # linenumdiff = len1-len2
        # pathsIntoLineNum = changePathIntoLineNum(paths_all)
        # write(program, pathsIntoLineNum, "LINE")
        # onesToSVR, onesToABS, onesToVR = decideMutation(parsedPaths_all, lines1, linenumdiff, declareFuns)
        # print('abs:', onesToABS)
        # print('svr: ', onesToSVR)
        # print('vr: ', onesToVR)
        # write(program, onesToABS, "ABS")
        # write(program, onesToSVR, "SVR")
        # write(program, onesToVR, "VR")

    else:
        paths_leaves = mutDividePathsLeaves(program, funcname)
        parsedPaths_leaves = parsePathLeaves(paths_leaves, program)
        with open(f"programs/{program}/log/mutant_pkl/{mutname}.pkl", "wb") as f:
            pickle.dump(parsedPaths_leaves, f)
        f.close()
    
if __name__ == '__main__':
    main()