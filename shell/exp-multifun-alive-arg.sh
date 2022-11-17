set -u
for fileName in $1; do
    # ---- assume that the test kill result exists ----
    rm -rf programs/${fileName}/alive
    rm -rf programs/${fileName}/myequivalent
    rm -rf programs/${fileName}/nonequivalent
    mkdir -p programs/${fileName}/alive
    mkdir -p programs/${fileName}/myequivalent
    mkdir -p programs/${fileName}/nonequivalent
    cd mujava/
    python3 preprocess.py --type=log --path=session_$fileName/result/$fileName/traditional_mutants/mutation_log --newpath=../programs/${fileName}/log/mutants.txt --progpath=session_$fileName/result/$fileName/traditional_mutants/ --killlogpath=session_$fileName/result/$fileName/traditional_mutants/mutant_list
    cd ../programs/${fileName}/alive
    # cd programs/${fileName}/alive
    mkdir -p ../log/mutant_pkl/
    for dirName in *; do
        if [[ "$dirName" != ".DS_Store" ]]; then
            cd $dirName
            for mutName in *; do  # mutName includes .java
                if [[ $mutName == *.java ]]; then 
                    cd ../../../../
                    tmp_ret=$(python3 moveEditRun.py --prog=$fileName --mut=$mutName --mutatedfun=$dirName)
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
                    python3 parse.py --prog=$fileName --ismut=Y --filename=$mutName --func=$funcName --rtype=$rtype
                    if [ ! -f example/out/all_${fileName}_${funcName}.txt ]; then
                        python3 moveEditRun.py --prog=$fileName --mutatedfun=$dirName
                        cd example/
                        javac -g src/example/IfExample.java
                        cp src/example/IfExample.class bin/example/
                        javac -classpath /Users/sj/Desktop/coinse/equimut-gen/jbse/build/libs/jbse-0.10.0-SNAPSHOT.jar src/example/RunIf.java
                        cp src/example/RunIf.class bin/example/
                        /Library/Java/JavaVirtualMachines/jdk1.8.0_201.jdk/Contents/Home/bin/java -Dfile.encoding=UTF-8 -classpath /Users/sj/Desktop/coinse/equimut-gen/example/bin:/Users/sj/Desktop/coinse/equimut-gen/jbse/build/libs/jbse-0.10.0-SNAPSHOT.jar:/Users/sj/Desktop/coinse/equimut-gen/jbse/build/libs/javassist.jar example.RunIf >&-
                        cd ../
                        python3 parse.py --prog=$fileName --ismut=N --func=$funcName --rtype=$rtype
                    isEquivalent=$(python3 checkSAT.py --filename=$mutName --progname=$fileName --rtype=$rtype --alive=Y)
                    echo $isEquivalent
                    if [[ $isEquivalent = true ]]; then
                        mv programs/$fileName/alive/$mutName programs/$fileName/myequivalent
                    else
                        mv programs/$fileName/alive/$mutName programs/$fileName/nonequivalent
                    fi
                    cd programs/${fileName}/alive/$dirName
                fi
                
            done
        fi
    done
    cd ../../../../
done