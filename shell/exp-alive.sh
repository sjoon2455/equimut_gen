set -u
for fileName in BubbleSort; do
# for fileName in BubbleSort Bisect Simulator StringTokenizer Vector3D XmlFriendlyNameCoder; do
# for fileName in Prime_num; do
    tmp_ret=$(python3 moveEditRun.py --prog=$fileName)
    rtype=$(cut -d' ' -f1 <<<"$tmp_ret")
    rtypee=$(python3 processRtype.py --rtype=$rtype)
    funcName=$(cut -d' ' -f2 <<<"$tmp_ret")
    ptype=$(cut -d' ' -f3 <<<"$tmp_ret")
    mujavadirname="${rtypee}_${funcName}(${ptype})"
    # mujavadirname="${rtypee}_${funcName}(int)"
    path_to_original_mutant_ver=../mujava/session_$fileName/result/$fileName/original
    path_to_test_result=session_$fileName/result/$fileName/traditional_mutants/mutant_list
    path_to_mutation_log=mujava/session_$fileName/result/$fileName/traditional_mutants/mutation_log
    path_to_muts=session_$fileName/result/$fileName/traditional_mutants/$mujavadirname
    
    cd example/
    javac -g src/example/IfExample.java
    cp src/example/IfExample.class bin/example/
    javac -classpath /Users/sj/Desktop/coinse/equimut-gen/jbse/build/libs/jbse-0.10.0-SNAPSHOT.jar src/example/RunIf.java
    cp src/example/RunIf.class bin/example/
    /Library/Java/JavaVirtualMachines/jdk1.8.0_201.jdk/Contents/Home/bin/java -Dfile.encoding=UTF-8 -classpath /Users/sj/Desktop/coinse/equimut-gen/example/bin:/Users/sj/Desktop/coinse/equimut-gen/jbse/build/libs/jbse-0.10.0-SNAPSHOT.jar:/Users/sj/Desktop/coinse/equimut-gen/jbse/build/libs/javassist.jar example.RunIf >&-
    cd ../
    python3 parse.py --prog=$fileName --ismut=N --func=$funcName --rtype=$rtype
    # cd mutate/
    # ./gradlew run --args="$fileName $funcName $path_to_original_mutant_ver 1"
    # cd ../
    
    # ---- hitherto assume that the test kill result exists ----
    rm -rf programs/${fileName}/alive
    rm -rf programs/${fileName}/myequivalent
    mkdir -p programs/${fileName}/alive
    mkdir -p programs/${fileName}/myequivalent
    cd mujava/
    python3 preprocess.py --type=log --path=${path_to_muts}/../mutation_log --newpath=../programs/${fileName}/log/mutants.txt --progpath=$path_to_muts/ --killlogpath=${path_to_test_result}
    cd ../programs/${fileName}/alive
    # cd programs/${fileName}/alive
    mkdir -p ../log/mutant_pkl
    for mutName in *; do  # mutName includes .java
        if [[ $mutName == *.java ]]; then 
            cd ../../../
            python3 moveEditRun.py --prog=$fileName --mut=$mutName
            cd example/
            javac -g src/example/IfExample.java
            cp src/example/IfExample.class bin/example/
            javac -classpath /Users/sj/Desktop/coinse/equimut-gen/jbse/build/libs/jbse-0.10.0-SNAPSHOT.jar src/example/RunIf.java
            cp src/example/RunIf.class bin/example/
            echo $mutName
            /Library/Java/JavaVirtualMachines/jdk1.8.0_201.jdk/Contents/Home/bin/java -Dfile.encoding=UTF-8 -classpath /Users/sj/Desktop/coinse/equimut-gen/example/bin:/Users/sj/Desktop/coinse/equimut-gen/jbse/build/libs/jbse-0.10.0-SNAPSHOT.jar:/Users/sj/Desktop/coinse/equimut-gen/jbse/build/libs/javassist.jar example.RunIf >&-
            cd ../
            python3 parse.py --prog=$fileName --ismut=Y --filename=$mutName --func=$funcName --rtype=$rtype
            isEquivalent=$(python3 checkSAT.py --filename=$mutName --progname=$fileName --rtype=$rtype --alive=Y)
            echo $isEquivalent
            if [[ $isEquivalent = true ]]; then
                mv programs/$fileName/alive/$mutName programs/$fileName/myequivalent
            else
                mv programs/$fileName/alive/$mutName programs/$fileName/nonequivalent
            fi
            cd programs/${fileName}/alive
        fi
        
    done
    cd ../../../
done