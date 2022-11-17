import shutil
import glob
import os


with open('mujava/session_XmlFriendlyNameCoder/result/XmlFriendlyNameCoder/traditional_mutants/mutation_result.txt', 'r') as f:
    lines = f.readlines()
f.close()

res = []
for n, line in enumerate(lines):
    line = line.strip()
    if line == 'OK (2 tests)':
        tmp = lines[n-5].strip()
        if tmp.startswith('XmlFriendlyNameCoder.'):
            continue
        else:
            res.append(tmp)
# print(res, len(res))
for mut in res:
    mut = mut[:-5]
    text_files = glob.glob(f'mujava/session_XmlFriendlyNameCoder/result/XmlFriendlyNameCoder/traditional_mutants/**/{mut}', recursive = True)
    # print(text_files)
    if len(text_files) == 1:
        mut_d = text_files[0]
        sub_d = mut_d.split('/')[-2]
        os.makedirs(f'programs/XmlFriendlyNameCoder/alive/{sub_d}', exist_ok = True)
        src = mut_d + '/XmlFriendlyNameCoder.java'
        dest = f'programs/XmlFriendlyNameCoder/alive/{sub_d}/{mut}.java'
        shutil.copy(src, dest)
    
# for line in lines:
#     prog = line.split(':')[2]
#     if prog == 'boolean_equals(double,double)':
#         name, linenum = line.split(':')[0], line.split(':')[1]
#         if linenum != '127':
#             if os.path.isfile(f'programs/MathUtils/alive/boolean_equals(double,double)/{name}.java'):
#                 os.remove(f'programs/MathUtils/alive/boolean_equals(double,double)/{name}.java')
