import statistics

def main():
    a = """.1460
.12
.1
.0
.0
.5
.1
.1
.0
.2
.0
.2
.0
.2
.3
.1
.3
.1
.0
.1
.0
.1
.2
.1
.1
.0
.1
.0
.1
.713
.1
.0
.1
.0
.0
.0
.1
.0
.0
.0
.1
.0
.4
.0
.10
.1
.0
.0
.0
.0
.0
.0
.2
.3
.0
.1
.1
.0
.0
.0
.0
.0
.0
.1
.0
.0
.1
.0
.0
.0
.0
.0
.0
.1
.0
.0
.0
.1
.0
.0
.1
.0
.0
.0
.0
.0
.0
.1
.0
.1
.0
.0
.2
.0
.0
.1
.1
.3
.0
.0
.0
.0"""
    tmp = [float(b.strip().replace('.', '')) for b in a.splitlines()]
    std = statistics.stdev(tmp)
    avg = statistics.mean(tmp)
    M = max(tmp)
    print(std)
    print(avg)
    print(M)
    
main()