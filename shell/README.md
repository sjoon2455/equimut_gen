# Equivalent Mutant Generation
Generate equivalent mutants for Java programs using symbolic execution or control flow graph.

## Description
- `jbse/` & `z3/`: Symbolic execution tool & SMT solver. Purpose is to get path conditions and program states at certain point of an arbitrary program. 
- `example/`: Contains demo program for jbse. Target programs will be moved into here.
- `Soot/`: Java manipulation tool. Needed to extract CFG and DU-chain.
- `mutate/`: With the parsed output, manipulate java source code to create equivalent mutants. Currently support ABS(Absolute value insertion), CRS(Constant replacement with scalar value), CRP(Constant replacement), UOI(Unary operator insertion) operators.
- `parse.py`: Parses jbse output.
- `checkSAT.py`: Checks if two programs' path conditions are the same.
- `mut_op.txt`: Contains mutation operators to be used.
- `main.py`: Main code that runs the entire pipeline except `jbse`.

## Setup
### Soot
- Replace `Soot/src/main/java/dev/navids/soottutorial/hellosoot/HelloSoot.java` into `./HelloSoot.java` given in this repo.
### Running JBSE
- In order to run `jbse` for a target java file, make sure you follow the setup provided by the authors of `jbse`. Run `RunIf.java` 

## Run
- `python3 main.py --mutop [mutation operators in text file]` creates equivalent mutants for you. For this, put target programs in `programs/` folder.
