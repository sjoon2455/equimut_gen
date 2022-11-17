for fileName in XmlFriendlyNameCoder; do
    rm /Users/sj/Desktop/coinse/equimut-gen/mujava/session_$fileName/result/$fileName/traditional_mutants/mutation_result.txt
    cd programs/$fileName/
    # cd mujava/session_$fileName/result/$fileName/traditional_mutants/
    export CLASSPATH=$CLASSPATH:/Users/sj/Desktop/coinse/equimut-gen/programs/$fileName:/Users/sj/Desktop/coinse/equimut-gen/programs/$fileName/evosuite-tests/
    cd evosuite-tests/
    javac ${fileName}_ESTest.java
    i=0
    # cd ../
    # java -cp /Users/sj/Desktop/evosuite-1.0.6.jar:/Users/sj/Desktop/coinse/equimut-gen/mujava/hamcrest-core.jar:/Users/sj/Desktop/coinse/equimut-gen/mujava/junit.jar:.:evosuite-tests/ org.junit.runner.JUnitCore ${fileName}_ESTest >> /Users/sj/Desktop/coinse/equimut-gen/mujava/session_$fileName/result/$fileName/traditional_mutants/mutation_result.txt
    cd ../alive/
    for dirName in *; do
        if [ -d $dirName ]; then
            cd $dirName
            for mutName in *; do
                if [ -f $mutName ]; then
                    i=$((i+1))
                    # if [ $i == 500 ] || [ $i == 1000 ] || [ $i == 1500 ] || [ $i == 2000 ] || [ $i == 2500 ] || [ $i == 3000 ]; then
                    echo $i
                    # fi
                    echo $mutName >> /Users/sj/Desktop/coinse/equimut-gen/mujava/session_$fileName/result/$fileName/traditional_mutants/mutation_result.txt
                    cp $mutName $fileName.java
                    javac $fileName.java
                    java -cp /Users/sj/Desktop/evosuite-1.0.6.jar:/Users/sj/Desktop/coinse/equimut-gen/mujava/hamcrest-core.jar:/Users/sj/Desktop/coinse/equimut-gen/mujava/junit.jar:.:../../evosuite-tests/ org.junit.runner.JUnitCore ${fileName}_ESTest >> /Users/sj/Desktop/coinse/equimut-gen/mujava/session_$fileName/result/$fileName/traditional_mutants/mutation_result.txt
                fi
            done
            cd ../
        fi
    done
    # cd ../../../../../
done