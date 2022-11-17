import shutil
import os

def main(prog):
    src = f'session_{prog}/result/{prog}/original/{prog}.java'
    dest = f'../programs/{prog}/{prog}.java'
    dest2 = f'../../../MutantBench/programs/{prog}.java'
    shutil.copy(src, dest)
    shutil.copy(src, dest2)


if __name__ == "__main__":
    for f in os.listdir('src/'):
        if f.endswith('.java') and f not in ['ArrayUtils.java', 'Simulator.java', 'XmlFriendlyNameCoder.java']:
            prog = f[:-5]
            main(prog)