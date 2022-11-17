set -u
# for fileName in Simulator StringTokenizer Vector3D XmlFriendlyNameCoder ArrayUtils MathUtils WordUtils Defroster; do
for fileName in Defroster; do
    cd programs/$fileName/equivalent
    [ -e ../log/equivalency.csv ] && rm ../log/equivalency.csv
    [ -e ../log/mutant_pkl ] && rm -rf ../log/mutant_pkl
    mkdir -p ../log/mutant_pkl
    # for mutName in AORB_1.java; do
    for mutName in *; do
        if [[ $mutName == *.java ]]; then 
            cd ../../../
            tmp_ret=$(python3 moveEditRun.py --prog=$fileName --mut=programs/$fileName/equivalent/$mutName --debug=Y)
            rtype=$(cut -d' ' -f1 <<<"$tmp_ret")
            funcName=$(cut -d' ' -f2 <<<"$tmp_ret")
            cd example/
            javac -g src/example/IfExample.java
            cp src/example/IfExample.class bin/example/
            javac -classpath /Users/sj/Desktop/coinse/equimut-gen/jbse/build/libs/jbse-0.10.0-SNAPSHOT.jar src/example/RunIf.java
            cp src/example/RunIf.class bin/example/
            echo $mutName
            /Library/Java/JavaVirtualMachines/jdk1.8.0_201.jdk/Contents/Home/bin/java -Dfile.encoding=UTF-8 -classpath /Users/sj/Desktop/coinse/equimut-gen/example/bin:/Users/sj/Desktop/coinse/equimut-gen/jbse/build/libs/jbse-0.10.0-SNAPSHOT.jar:/Users/sj/Desktop/coinse/equimut-gen/jbse/build/libs/javassist.jar example.RunIf >&-
            cd ../
            python3 parse.py --prog=$fileName --ismut=Y --filename=$mutName --funcname=$funcName
            if [ ! -f example/out/all_${fileName}_${funcName}.txt ]; then
                python3 moveEditRun.py --prog=$fileName --mutatedfun=notmove
                cd example/
                javac -g src/example/IfExample.java
                cp src/example/IfExample.class bin/example/
                javac -classpath /Users/sj/Desktop/coinse/equimut-gen/jbse/build/libs/jbse-0.10.0-SNAPSHOT.jar src/example/RunIf.java
                cp src/example/RunIf.class bin/example/
                /Library/Java/JavaVirtualMachines/jdk1.8.0_201.jdk/Contents/Home/bin/java -Dfile.encoding=UTF-8 -classpath /Users/sj/Desktop/coinse/equimut-gen/example/bin:/Users/sj/Desktop/coinse/equimut-gen/jbse/build/libs/jbse-0.10.0-SNAPSHOT.jar:/Users/sj/Desktop/coinse/equimut-gen/jbse/build/libs/javassist.jar example.RunIf >&-
                cd ../
                python3 parse.py --prog=$fileName --ismut=N --func=$funcName --rtype=$rtype
            fi
            isEquivalent=$(python3 checkSAT.py --filename=$mutName --progname=$fileName --rtype=$rtype)
            echo $isEquivalent
            cd programs/$fileName/equivalent
        fi
    done
    cd ../../../../
done