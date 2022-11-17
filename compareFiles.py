import argparse
from collections import defaultdict

def splitline(line, delim):
    a, b, c = line.split(delim)[0], line.split(delim)[1], line.split(delim)[2]
    d = delim.join(line.split(delim)[3:])
    return a, b, c, d

def isSimilar(a, b):
    output_list = [li[2:] for li in difflib.ndiff(a, b) if li[0] != ' ' and li[2:] not in ['(', ')', '{', '}', ';', ' ', '\n', '\t']]
    return a==b or not output_list
    

def isInclusive(num_b, before_b, after_b, dict_a):
    isInc = False
    for [n, b, a] in dict_a[num_b]:
        if isSimilar(before_b, b) and isSimilar(after_b, a):
            return True, n

    return isInc, ""

def main():
    parser = argparse.ArgumentParser(description='')
    parser.add_argument('--prog', '-f', help='program name', default='Bisect')
    args = parser.parse_args()
    prog = args.prog
    path_A = f"programs/{prog}/myequivalent/mutants.txt"
    path_B = f"programs/{prog}/equivalent/mutants.txt"
    
    with open(path_A, 'r') as fa:
        lines_a = fa.readlines()
    fa.close()
    
    with open(path_B, 'r') as fb:
        lines_b = fb.readlines()
    fb.close()

    res_a, res_b, inclusive = [], [], []
    dict_a, dict_b = defaultdict(list), defaultdict(list)
    for line_a in lines_a:
        name_a, num_a, before_a, after_a = splitline(line_a, ":")
        dict_a[num_a].append([name_a, before_a, after_a])
    for line_b in lines_b:
        name_b, num_b, before_b, after_b = splitline(line_b, ":")
        isInc, _name_a = isInclusive(num_b, before_b, after_b, dict_a)
        if isInc:
            inclusive.append([_name_a, name_b])
        else:
            res_b.append(name_b)
    for ele in dict_a.values():
        res_a.append(ele)
    return res_a, res_b, inclusive
        
    

    

if __name__ == '__main__':
    res_a, res_b, inclusive = main()
    print(f"------------------------{prog}------------------------")
    print(f"My exclusive ones(amount: {len(res_a)}) are: ", res_a)
    print(f"Exclusive ones(amount: {len(res_b)}) on the dataset are: ", res_b)
    print(f"Inclusive ones(amount: {len(inclusive)}) are: ", inclusive)

