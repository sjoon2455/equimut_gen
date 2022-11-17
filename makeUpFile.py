import argparse
import os

def makeupProgramAndSave(f, prog):
    new_lines = []
    print(f)
    with open(f, 'r') as ff:
        lines = ff.readlines()
        for n, line in enumerate(lines):
            if line.strip() == 'package example':
                continue
            elif 'public class IfExample' in line.strip():
                if "{" in line:
                    new_lines.append(f'public class {prog} {{')
                else:
                    new_lines.append(f'public class {prog}')
            else:
                new_lines.append(line)
    ff.close()

    with open(f, 'w') as ff:
        ff.writelines(new_lines)
    ff.close()    



def main():
    parser = argparse.ArgumentParser(description='')
    parser.add_argument('--prog', '-p', help='program name?', default="Bisect")
    args = parser.parse_args()
    prog = args.prog
    ori = 'example/src/IfExample.java'
    for f in os.listdir(f'programs/{prog}/equivalent/'):
        if '_' in f and f.endswith('.java'):
            makeupProgramAndSave(f'programs/{prog}/equivalent/{f}', prog)


main()