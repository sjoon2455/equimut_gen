f1 = 'f1.java'
f2 = 'f2.java'

with open(f1, 'r') as f_1:
    lines_1 = f_1.readlines()
f_1.close()

with open(f2, 'r') as f_2:
    lines_2 = f_2.readlines()
f_2.close()

res_1, res_2 = "", ""

for l in lines_1:
    l = l.strip()
    l = l.replace(" ", "")
    l = l.split("//")[0]
    res_1 += l

for l in lines_2:
    l = l.strip()
    l = l.replace(" ", "")
    l = l.split("//")[0]
    res_2 += l

print(res_1)
print(res_2)