import argparse
import os
import subprocess
import shutil
import difflib
import sys

def getMetadataFun_long(mutatedfun):
    ret_type, method_name, parameter_num = '', '', 0
    mutatedfun = mutatedfun.replace("  ", " ")
    mutatedfun = mutatedfun.replace("( ", "(")
    mutatedfun= mutatedfun.replace(" (", "(")
    mutatedfun= mutatedfun.replace(") ", ")")
    mutatedfun = mutatedfun.replace(" )", ")")
    mutatedfun = mutatedfun.strip() #private int skipDelimiters(int startPos)
    spl = mutatedfun.split(" ")
    assert spl[0] in ['public', 'private', 'void']
    if spl[1].strip() == 'static':
        del spl[1]
    if '(' in spl[1]: #constructor
        ret_type = ''
        method_name = spl[1].split("(")[0].strip()
    else:
        ret_type = spl[1].strip()
        method_name = spl[2].split("(")[0].strip()
    if '()' in mutatedfun:
        parameter_num = 0
    elif '(' not in mutatedfun and ')' not in mutatedfun:
        parameter_num = 0
    else:
        parameter_num = mutatedfun.count(',')+1
    return ret_type, method_name, parameter_num

def getMetadataFun(mutatedfun):
    ret_type = mutatedfun.split("_")[0]
    method_name = mutatedfun.split("_")[1].split("(")[0]
    parameter_num = mutatedfun.count(',')
    if parameter_num == 0:
        parameter_num = 0
    else:
        parameter_num += 1
    return ret_type, method_name, parameter_num

def wrapMove(PROGPATH, RUNPATH, debug, prog, mutatedfun):    
    RUNPATH_TARGET = os.path.join(RUNPATH, "IfExample.java")
    CONFIGPATH = os.path.join(RUNPATH, "RunIf.java")
    src, dest = PROGPATH, RUNPATH_TARGET
    shutil.copy(src, dest)
    ret_type = ""
    method_name = ""
    with open(RUNPATH_TARGET, "r") as f:
        lines = f.readlines()
        shouldImportABS = True
        for i, line in enumerate(lines):
            line = line.strip()
            if "java.lang.Math" in line and "import" in line:
                shouldImportABS = False
            # modify class name and import java.math.
            if "public" in line and "class " in line:
                if 'implements' in line:
                    addon = line.split('implements')[1]
                    if "{" in line:
                        lines[i] = f"public class IfExample implements {addon}{{\n"
                    else:
                        lines[i] = f"public class IfExample implements {addon}\n"
                else:
                    if "{" in line:
                        lines[i] = "public class IfExample {\n"
                    else:
                        lines[i] = "public class IfExample\n"
            if line == "Defroster.AU8_tp AU8;":
                lines[i] = "    IfExample.AU8_tp AU8;\n"
            # if mutatedfun=='' and "public" in line and "class " not in line:
            #     line = line.strip()
            #     line = line.replace("  ", " ")
            #     line = line.replace("( ", "(")
            #     line = line.replace(" (", "(")
            #     line = line.replace(") ", ")")
            #     line = line.replace(" )", ")")
            #     ret_type, method_name = getMetadataFun(line)

            
            if i == len(lines)-1:
                if shouldImportABS:
                    absline = "import java.lang.Math;\n"
                    lines = ["package example;\n", absline] + lines
                else:
                    lines = ["package example;\n"] + lines
            if 'public' in line and prog in line and 'class ' not in line: #take care of constructor
                line = line.replace(prog, 'IfExample')
                lines[i] = line
    f.close()
    with open(RUNPATH_TARGET, "w") as f:
        f.writelines(lines)
    f.close()
    # editRun(RUNPATH_TARGET, CONFIGPATH, debug, prog, mutatedfun)
    if mutatedfun == 'notmove':
        return
    elif ' ' in mutatedfun:
        ret_type, method_name, parameter_num = getMetadataFun_long(mutatedfun)    
    else:
        ret_type, method_name, parameter_num = getMetadataFun(mutatedfun)
    return ret_type + " " + method_name
    # return "int[] sort int[]"
                

def editRun(RUNPATH_TARGET, CONFIGPATH, debug, prog, mutatedfun):
    if mutatedfun == 'notmove':
        with open(CONFIGPATH, "r") as f:
            lines = f.readlines()
            assert "p.setMethodSignature" in lines[25], "Manually find line!"
            assert "p.setTimeout" in lines[30], "Manually find line!"
            assert "p.setStepShowMode" in lines[31], "Manually find line!"
            assert "p.setOutputFilePath" in lines[32], "Manually find line!"

            lines[30] = '\t\tp.setTimeout(10000, TimeUnit.MILLISECONDS);\n'
            lines[31] = '\t\tp.setStepShowMode(ALL);\n'
            lines[32] = lines[32].replace('leaves', 'all')
            # lines[32] = f'\t\tp.setOutputFilePath("./out/leaves_{prog}_{method_name}.txt");\n'
            
        f.close()
        with open(CONFIGPATH, "w") as f:
            f.writelines(lines)
        f.close()

    else:
        method_sig = ''
        method_name = ''
        CLASSPATH_TARGET = RUNPATH_TARGET[:-5] + ".class"
        cmd_cp = f"javac {RUNPATH_TARGET}".split(" ")
        cmd_ms = f"javap -private -s {CLASSPATH_TARGET}".split(" ")
        subprocess.run(cmd_cp)
        assert os.path.isfile(RUNPATH_TARGET), "Compile failed"
        output_sig = subprocess.run(cmd_ms, stdout=subprocess.PIPE).stdout.decode("utf-8").splitlines()
        if ' ' in mutatedfun:
            ret_type, method_name, parameter_num = getMetadataFun_long(mutatedfun)
        else:
            ret_type, method_name, parameter_num = getMetadataFun(mutatedfun)
        for i in range(2, len(output_sig)):
            tmp = output_sig[i].strip()
            if tmp.startswith('descriptor:') or tmp == '' or tmp == '{' or tmp == '}' or "()" not in tmp:
                continue
            ret_type2, method_name2, parameter_num2 = getMetadataFun_long(output_sig[i])
            if ret_type == ret_type2 and method_name == method_name2 and parameter_num == parameter_num2:
                assert 'descriptor' in output_sig[i+1]
                method_sig = output_sig[i+1].split('descriptor: ')[1]
                break
        with open(CONFIGPATH, "r") as f:
            lines = f.readlines()
            assert "p.setMethodSignature" in lines[25], "Manually find line!"
            assert "p.setTimeout" in lines[30], "Manually find line!"
            assert "p.setStepShowMode" in lines[31], "Manually find line!"
            assert "p.setOutputFilePath" in lines[32], "Manually find line!"
            line = lines[25]
            tmp = line.split(',')
            tmp[1] = ' "' + method_sig + '"'
            tmp[2] = ' "' + method_name + '");\n'
            lines[25] = ",".join(tmp)

            if debug:
                lines[30] = '\t\tp.setTimeout(5000, TimeUnit.MILLISECONDS);\n'
                lines[31] = '\t\tp.setStepShowMode(LEAVES);\n'
                lines[32] = f'\t\tp.setOutputFilePath("./out/leaves_{prog}_{method_name}.txt");\n'
                # lines[32] = f'\t\tp.setOutputFilePath("./out/leaves_{prog}_sqrt.txt");\n'
            else:
                lines[30] = '\t\tp.setTimeout(5000, TimeUnit.MILLISECONDS);\n'
                lines[31] = '\t\tp.setStepShowMode(LEAVES);\n'
                lines[32] = f'\t\tp.setOutputFilePath("./out/original_leaves_{prog}_{method_name}.txt");\n'
                # lines[32] = f'\t\tp.setOutputFilePath("./out/all_{prog}_sqrt.txt");\n'
            
            
        f.close()
        with open(CONFIGPATH, "w") as f:
            f.writelines(lines)
        f.close()
    
#for exp-equivalent.sh
def getMutatedFun(prog_dir, mut, debug, mutatedfun):
    if mutatedfun=='notmove':
        return 'notmove'
    assert mutatedfun=='.'
    if prog_dir == "Bisect":
        return 'public static  double sqrt( double N )'
    if mut == '.':
        return ''
    funcname = ''
    with open(f'mujava/session_{prog_dir}/src/{prog_dir}.java') as fo:
        o_lines = fo.readlines()
    fo.close()
    if debug:
        mpath = mut
    else:
        mpath = f'programs/{prog_dir}/alive/{mut}'
    with open(mpath) as fm:
        m_lines = fm.readlines()
    fm.close()
    diff = difflib.unified_diff(o_lines, m_lines)
    diff = ''.join(diff)
    # get line number from diff
    linenum = diff.splitlines()[2].split("-")[1].split(",")[0]
    for i in range(int(linenum), -1, -1):
        line = m_lines[i]
        if ('public' in line or 'private' in line) and 'class ' not in line: #function line
            funcname = line.split("{")[0].strip()
            break
    if funcname=='':
        assert False
    return funcname.strip()

    
    

def main():
    parser = argparse.ArgumentParser(description='')
    parser.add_argument('--prog', '-p', help='program name?', default="Bisect")
    parser.add_argument('--mut', '-m', help='mutant name?', default=".") # AOR_1.java
    parser.add_argument('--mutatedfun', '-f', help='mutated function?', default=".") # 
    parser.add_argument('--debug', '-d', help='for debug?', default="N") # AOR_1.java
    args = parser.parse_args()
    prog_dir = args.prog
    mut = args.mut
    mutatedfun = args.mutatedfun
    debug = True if args.debug=="Y" else False
    prog = prog_dir+".java"
    if debug: # equivalent mutant
        mutatedfun = getMutatedFun(prog_dir, mut, debug, mutatedfun)
        PROGPATH = mut
        # RUNPATH = '../../../../../../../example/src/example'
        RUNPATH = 'example/src/example'
    else:
        RUNPATH = 'example/src/example'
        if mut==".": # original program
            PROGPATH = f'programs/{prog_dir}/{prog}'
        else: # alive mutant
            PROGPATH = f'programs/{prog_dir}/alive/{mutatedfun}/{mut}'
            debug = True
    
    return wrapMove(PROGPATH, RUNPATH, debug, prog_dir, mutatedfun)

if __name__ == '__main__':
    print(main())
    # getMetadataFun('private  void quicksort( int[] data, int first, int last )')
    # wrapMove('mujava/session_Bisect/result/Bisect/traditional_mutants/double_sqrt(double)/AOIS_12/Bisect.java')
    # python3 preprocess.py --type=log --path=session_WordUtils/result/WordUtils/traditional_mutants/mutation_log --newpath=../programs/WordUtils/log/mutants.txt --progpath=session_WordUtils/result/WordUtils/traditional_mutants/ --killlogpath=session_WordUtils/result/WordUtils/traditional_mutants/mutant_list
