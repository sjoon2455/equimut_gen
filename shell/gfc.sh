cd programs
# for fileName in Bisect Bubble BubbleSort Day Insert Mid Min Prime_num Profit Triangle; do
for fileName in Defroster; do
    while read funcName; do
        echo $funcName
        cd ../
        tmp_ret=$(python3 moveEditRun.py --prog=$fileName --mutatedfun=$funcName)
        funcName2=$(cut -d' ' -f2 <<<"$tmp_ret")
        echo $funcName2
        path_to_original_mutant_ver=../mujava/session_$fileName/result/$fileName/original
        # path_to_mutantbench=../programs/$fileName/common_equivalent/
        cd mutate/
        ./gradlew run --args="$fileName $funcName2 $path_to_original_mutant_ver 1"
        # ./gradlew run --args="$fileName $funcName2 $path_to_mutantbench 1"
    done < ../mujava/session_$fileName/result/$fileName/traditional_mutants/method_list
done

# ./gradlew run --args="ArrayUtils toMap ../mujava/session_ArrayUtils/result/ArrayUtils/original 1"
./gradlew run --args="MathUtils main ../mujava/session_MathUtils/result/MathUtils/original 1"