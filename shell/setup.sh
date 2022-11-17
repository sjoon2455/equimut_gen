# run this script when program/program/program.java is slightly changed and you want to rerun the whole process.
for fileName in Defroster; do
    # equivalent mutantbench
    # cp programs/$fileName/$fileName.java ../MutantBench/programs/$fileName.java
    # cd ../MutantBench/mutantbench/
    # python3 benchmark.py bash examples/DEMInterface/interface_dem.sh DEM java --programs $fileName --equivalency equivalent
    # cp -r generated/$fileName/mutants/ ../../equimut-gen/programs/$fileName/equivalent/
    # cd ../../equimut-gen/

    # mujava prepare
    # cp programs/$fileName/$fileName.java mujava/src/$fileName.java
    # cd mujava/
    # rm -rf session_$fileName
    # java mujava.cli.testnew session_$fileName $fileName.java
    # java mujava.cli.genmutes session_$fileName
    # cd ../
    # evosuite
    # cd programs/$fileName/
    # javac $fileName.java
    # $EVOSUITE -class $fileName -projectCP .
    # cd equivalent/
    # mkdir -p tmp/
    # for tmpp in *; do
    #     if [[ $tmpp == *java ]]; then
    #         echo $tmpp
    #         echo $tmpp
    #         echo $tmpp
    #         echo $tmpp
    #         echo $tmpp
    #         cp $tmpp tmp/BubbleSort.java
    #         cd tmp/
    #         javac BubbleSort.java
    #         cd ../
    #         cd ../evosuite-tests/
    #         java -cp $CLASSPATH:.:../equivalent/tmp/ org.junit.runner.JUnitCore BubbleSort_ESTest
    #         cd ../equivalent/
    #     fi
    # done
    # python3 ../../../modifyTestFile.py --test=${fileName}_ESTest.java
    # export CLASSPATH=$CLASSPATH:/Users/sj/Desktop/coinse/equimut-gen/programs/$fileName:/Users/sj/Desktop/coinse/equimut-gen/programs/$fileName/evosuite-tests/
    # cd evosuite-tests/
    # javac ${fileName}_ESTest.java
    # java -cp $CLASSPATH:.:../ org.junit.runner.JUnitCore ${fileName}_ESTest
    # cp ${fileName}_ESTest.class ../../../mujava/session_$fileName/testset/${fileName}_ESTest.class
    # cd ../../../

    # mujava runmutes on tests
    export CLASSPATH=/Users/sj/Desktop/coinse/equimut-gen/mujava/mujava.jar:/Users/sj/Desktop/coinse/equimut-gen/mujava/openjava.jar:/Users/sj/Desktop/coinse/equimut-gen/mujava/commons-io.jar:/Users/sj/Desktop/coinse/equimut-gen/mujava/tools.jar:/Users/sj/Desktop/coinse/equimut-gen/mujava/junit.jar:/Users/sj/Desktop/coinse/equimut-gen/mujava/hamcrest-core.jar:/Users/sj/Desktop/coinse/equimut-gen/programs/$fileName/evosuite-tests/
    cd mujava/
    # if [[ $fileName == *Utils ]]; then
    #     java mujava.cli.runmutes -p 0.3 ${fileName}_ESTest session_$fileName
    # else 
        java mujava.cli.runmutes ${fileName}_ESTest session_$fileName
    # fi
    # cd ../
    # ./exp-multifun-equivalent-arg.sh $fileName
    # ./exp-multifun-alive-arg.sh $fileName
done
# java mujava.cli.runmutes -p 0.1 ArrayUtils_ESTest session_ArrayUtils
# java mujava.cli.runmutes MathUtils_ESTest session_MathUtils
# export CLASSPATH=/data/mujava/mujava.jar:/data/mujava/openjava.jar:/data/mujava/commons-io.jar:/data/mujava/tools.jar:/data/mujava/junit.jar:/data/mujava/hamcrest-core.jar:/usr/lib/jvm/java-1.8.0-openjdk-amd64/lib/tools.jar

# scp  coinse@143.248.135.59:/home/coinse/Projects/ClassifyingMutants/Others/mujava

# export CLASSPATH=/data/mujava/mujava.jar:/data/mujava/openjava.jar:/data/mujava/commons-io.jar:/data/mujava/tools.jar:/data/mujava/junit.jar:/data/mujava/hamcrest-core.jar