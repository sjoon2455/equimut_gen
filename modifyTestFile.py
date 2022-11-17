import argparse


def main(testfile):
    newlines = []
    with open(testfile, 'r') as f:
        lines = f.readlines()
    f.close()

    for line in lines:
        if '@RunWith(EvoRunner.class)' in line:
            continue
        elif 'public class' in line and 'extends' in line:
            tmp_line = line.split('extends')[0] + '{'
            newlines.append(tmp_line)
        else:
            newlines.append(line)

    with open(testfile, 'w') as f:
        f.writelines(newlines)
    f.close()

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description='')
    parser.add_argument('--test', '-t', help='test file name?') #${fileName}_ESTest.java
    args = parser.parse_args()
    testfile = args.test
    main(testfile)