package example;
import java.util.concurrent.TimeUnit;
import jbse.apps.run.RunParameters;
import jbse.apps.run.Run;
import static jbse.apps.run.RunParameters.DecisionProcedureType.Z3;

import static jbse.apps.run.RunParameters.StateFormatMode.TEXT;
import static jbse.apps.run.RunParameters.StepShowMode.ALL;
import static jbse.apps.run.RunParameters.StepShowMode.LEAVES;
public class RunIf {
    public static void main(String[] args)   {
    	
        final RunParameters p = new RunParameters();
        set(p);
        

//        Working Directory = /Users/sj/Desktop/coinse/equimut-gen/example

        final Run r = new Run(p);
        r.run();
    }
    
    private static void set(RunParameters p) {
        p.addUserClasspath("./bin");
        p.setJBSELibPath("../jbse/build/libs/jbse-0.10.0-SNAPSHOT.jar");
        p.setMethodSignature("example/IfExample", "(II)I", "min");
        p.setDecisionProcedureType(Z3);
        p.setExternalDecisionProcedurePath("/Library/Frameworks/Python.framework/Versions/3.7/bin/z3");
        p.setStateFormatMode(TEXT);
        p.setDepthScope(30);
		p.setTimeout(5000, TimeUnit.MILLISECONDS);
		p.setStepShowMode(ALL);
		p.setOutputFilePath("./out/original_leaves_Min_min.txt");
    }
}
