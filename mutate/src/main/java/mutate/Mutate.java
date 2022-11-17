package mutate;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.nodeTypes.NodeWithIdentifier;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.Range;
import com.github.javaparser.ast.visitor.Visitable;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.utils.*;
import com.github.javaparser.utils.SourceRoot;

import soot.*;
import soot.jimple.JimpleBody;
import soot.jimple.internal.JIfStmt;
import soot.jimple.internal.AbstractDefinitionStmt;
import soot.options.Options;
import soot.toolkits.graph.ClassicCompleteUnitGraph;
import soot.toolkits.graph.CompleteUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.LocalDefs;
import soot.toolkits.scalar.LocalUses;
import soot.toolkits.scalar.LocalDefs;
import soot.toolkits.graph.Block;
import soot.toolkits.graph.BlockGraph;
import soot.toolkits.graph.BriefBlockGraph;
import soot.baf.toolkits.base.LoadStoreOptimizer;
import soot.util.Chain;
import soot.toolkits.scalar.UnitValueBoxPair;
import soot.tagkit.LineNumberTag;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ListIterator;  
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.Collections;
import java.util.Optional;
import java.time.LocalTime;
import java.util.stream.IntStream;
import java.util.stream.Collectors;

public class Mutate {
    public static boolean isMutated = false;
    public static Map<String, Integer> mutCount = new HashMap<>();
    
    public static void main(String[] args) throws IOException {
        String fileName = args[0];
        String methodName = args[1];
		String sourceDirectory = args[2];
        String FLAG = args[3];
		
        if(FLAG.equals("0")){ //create equivalent mutant
            // mutateABSSVR(fileName, methodName);
            mutateUOI(fileName, methodName);
        }
        else{ //create gfc file for these
            createGFCFile(fileName, methodName, sourceDirectory);
        }
    }

    public static void mutate(String targetVar, int line, String mutop, double replacement, String fileName, String methodName) throws IOException {
        String path = "../programs/"+fileName+"/myequivalent";
		String fName = fileName+".java";
		Path pathToSource = Paths.get(path);
        SourceRoot sourceRoot = new SourceRoot(pathToSource);            
        CompilationUnit cu = sourceRoot.parse("", fName);
        
        cu.accept(new ModifierVisitor<Void>(){
            @Override
            public Visitable visit(NameExpr n, Void arg){
                boolean isNameEqual = targetVar.equals(n.getName().asString());        
                if(isNameEqual){
                    if(n.getRange().isPresent()){
                        if(line == n.getRange().get().begin.line){
                            Optional<Node> parentNode = n.getParentNode();
                            String snippet = "";
                            if(mutop.equals("ABS")){
                                snippet = "Math.abs("+n.getName()+")";
                            }
                            else if(mutop.equals("SVR")){
                                snippet = Double.toString(replacement);
                            }
							else if(mutop.equals("UOIpostplus")){
                                snippet = n.getName()+"++";
                            }
							else if(mutop.equals("UOIpostminus")){
                                snippet = n.getName()+"--";
                            }
							else if(mutop.equals("UOIpreplus")){
                                snippet = "++"+n.getName();
                            }
                            else { // UOIpreminus
                                snippet = "--"+n.getName();
                            }
                            Node newNode = StaticJavaParser.parseExpression(snippet);
                            
                            if(parentNode.isPresent()){
                                if(!parentNode.get().getClass().getName().contains("AssignExpr")){
                                    parentNode.get().remove(n);
                                    newNode.setParentNode(parentNode.get());
                                    Mutate.isMutated = true;
                                    
                                    return newNode;
                                }
                            }
                        };    
                    }
                }
                return super.visit(n, arg);
            }
        }, null);
        if(Mutate.isMutated){
            String _mutop = simplifyMutop(mutop);
            // store changed file(that is, equivalent mutant) in a directory
            String dirName = "../programs/"+fileName+"/myequivalent/";
            Path mutStore = Paths.get(dirName);
            sourceRoot.saveAll(mutStore);

            // rename file
            Path sourceName = Paths.get(dirName+"IfExample.java");
			Mutate.mutCount.put(_mutop, Mutate.mutCount.getOrDefault(_mutop, 0)+1);
            String mutName = _mutop+"_"+Integer.toString(Mutate.mutCount.get(_mutop))+".java";
            Files.move(sourceName, sourceName.resolveSibling(mutName));
            // Files.move(sourceName, mutName);
            Mutate.isMutated = false;
        }
        

    }
    public static void mutateABSSVR(String fileName, String methodName) throws IOException{
        
        String pathToCandidates_ABS = "../programs/"+fileName+"/log/onesToABS.csv";
        String row;
        BufferedReader csvReader = new BufferedReader(new FileReader(pathToCandidates_ABS));
        while ((row = csvReader.readLine()) != null) {
            
            List<String> data = Arrays.asList(row.split(","));
            ListIterator<String> it = data.listIterator();
            int lineNum = 0;
            while(it.hasNext()){
                if(it.nextIndex() == 0){
                    lineNum = Integer.parseInt(it.next());        
                }
                else{
                    mutate(it.next(), lineNum, "ABS", 0.0, fileName, methodName);
                }
            }
        }
        csvReader.close();

        String pathToCandidates_SVR = "../programs/"+fileName+"/log/onesToSVR.csv";
        BufferedReader csvReader2 = new BufferedReader(new FileReader(pathToCandidates_SVR));
        while ((row = csvReader2.readLine()) != null) {
            String[] data = row.split(",");
            int lineNum = Integer.parseInt(data[0]);
            String toBeReplaced = data[1];
            double toReplace = Double.parseDouble(data[2]);
            mutate(toBeReplaced, lineNum, "SVR", toReplace, fileName, methodName);        
            
        }
        csvReader2.close();
    }

	public static String simplifyMutop(String mutop){
		if(mutop.startsWith("UOI")){
			return "UOI";
		}
		else{
			return mutop;
		}
	}

	public static void createGFCFile(String fileName, String methodName, String sourceDirectory) {
		String clsName = fileName;
		// String sourceDirectory = ".." + File.separator + "programs" + File.separator + fileName;
        
        G.reset();
		Options.v().set_keep_line_number(true);
        Options.v().set_prepend_classpath(true);
        Options.v().set_allow_phantom_refs(true);
        // Options.v().set_whole_program_mode(true);
        Options.v().set_soot_classpath(sourceDirectory);
		Options.v().setPhaseOption("jb", "use-original-names:true");
		Options.v().set_src_prec(Options.src_prec_jimple);
        SootClass sc = Scene.v().loadClassAndSupport(clsName);
        sc.setApplicationClass();
        Scene.v().loadNecessaryClasses();

        // Retrieve method's body
        SootClass mainClass = Scene.v().getSootClass(clsName);
		// Scene.v().addBasicClass("MathUtils",mainClass.SIGNATURES);
        // SootMethod sm = mainClass.getMethod("boolean equals(double,double)");
        SootMethod sm = mainClass.getMethodByName(methodName);
        JimpleBody body = (JimpleBody) sm.retrieveActiveBody();

		Chain<Unit> mUnits = body.getUnits();
		Body mBody=body;
		// LocalDefs mLocalDefs = LocalDefs.Factory.newLocalDefs(mBody);
		// LocalUses mLocalUses = LocalUses.Factory.newLocalUses(mBody, mLocalDefs);
		BlockGraph blockGraph = new BriefBlockGraph(mBody);
		List<Block> blocks = blockGraph.getBlocks();
		Iterator<Block> blockIt = blocks.iterator();
		List<Block> _blocks = new ArrayList<Block>();
		List<Block> __blocks = new ArrayList<Block>();
		Map<Integer, List<Integer>> blockToLineNum = new HashMap<Integer, List<Integer>>();
		Map<Integer, List<Integer>> newBlockToLineNum = new HashMap<Integer, List<Integer>>();
		Unit beforeUnit=null;
		Unit unit=null;
		Integer startLineNumOfBlock=0;
		Integer endLineNumOfBlock=0;
		List<Integer> emptyBlocks = new ArrayList<>(); 
		// Look for empty blocks
		for(Block block: blocks){
			if(isEmptyBlock(block)){
				int numBlock = block.getIndexInMethod();
				emptyBlocks.add(numBlock);
				
			}
		}
		while (blockIt.hasNext()) {
			Block block = blockIt.next();
			_blocks.add(0, block);
			Integer numBlock = block.getIndexInMethod();
			Iterator<Unit> unitIt = block.iterator();
			Integer unitCount = 0;
			while (unitIt.hasNext()) {
				if(unitCount > 0){
					beforeUnit = unit;
				}
				unit = unitIt.next();
				if(!emptyBlocks.contains(numBlock)){
					if(unitCount==1){
						// System.out.println(unit.getTags().toString());
						startLineNumOfBlock = Integer.parseInt(unit.getTags().toString().split("sline: ")[1].split(" ")[0]);
					}
				}
				unitCount++;
			}
			
			List<Integer> lineNums = Arrays.asList(startLineNumOfBlock);
			blockToLineNum.put(numBlock, lineNums);
		}
		
		Map<Integer, List<Integer>> blockToSuccessor = new HashMap<Integer, List<Integer>>();
		
		for(Block block: _blocks){
			if(isEmptyBlock(block)){
				List<Block> succs_ = block.getSuccs();
				List<Block> preds_ = block.getPreds();
				for(Block bl_pd: preds_){
					List<Block> new_succs = new ArrayList<Block>();
					for(Block bl_sc:succs_){
						new_succs.add(bl_sc);
					}
					new_succs.addAll(bl_pd.getSuccs());
					new_succs.remove(block);
					
					bl_pd.setSuccs(new_succs);
				}
			}
		}
		
		for(Block block: _blocks){
			__blocks.add(0, block);
		}

		for(Block block: __blocks){
			if(!isEmptyBlock(block)){
				int numBlock = block.getIndexInMethod();
				List<Block> succs = block.getSuccs();
				List<Integer> succNum = new ArrayList<>(); 
				for(Block succBlock: succs){
					int tmpNumBlock = succBlock.getIndexInMethod();
					tmpNumBlock = considerEmptyBlocks(emptyBlocks, tmpNumBlock);
					if (tmpNumBlock!=-5){
						succNum.add(tmpNumBlock);
					}
				}
				Integer newNumBlock = considerEmptyBlocks(emptyBlocks, numBlock);
				
				blockToSuccessor.put(newNumBlock, succNum);
				List<Integer> tmpLineNums = blockToLineNum.get(numBlock);
				newBlockToLineNum.put(newNumBlock, tmpLineNums);
			}
		}
		writeToFile(newBlockToLineNum, fileName, methodName, 0);
		writeToFile(blockToSuccessor, fileName, methodName, 1);
		
	}

	public static boolean isEmptyBlock(Block block){
		String s1 = block.getHead().toString().trim();
		String s2 = block.getTail().toString().trim();
		boolean c1 = s1.equals(s2);
		boolean c2 = s1.equals("goto [?= nop]") || s1.equals("nop");
		boolean c3 = c1 && c2;
		return c3;
	}

	public static int considerEmptyBlocks(List<Integer> l, int n){
		int largerCount = 0;
		for(int ele: l){
			if(ele == n){
				return -5;
			}
			if(ele < n){
				largerCount += 1;
			}
		}
		if(n < largerCount){
			throw new IllegalArgumentException("Why?");
		}
		return n - largerCount+1;
	}

	public static void writeToFile(Map<Integer, List<Integer>> blockToSuccessor, String fileName, String methodName, Integer flag){
		try {
			String directoryName;
			if(flag == 1){
				directoryName = ".." + File.separator + "programs" + File.separator + fileName + File.separator + "arc_prim";
			}
			else{
				directoryName = ".." + File.separator + "programs" + File.separator + fileName + File.separator + "arc_prim";
			}
            File directory = new File(directoryName);
            if (! directory.exists()){
                directory.mkdir();
            }
			String filePath;
			if(flag==1){
				filePath = ".." + File.separator + "programs" + File.separator + fileName + File.separator + "arc_prim" + File.separator + methodName + ".gfc";
			}       
            else{
				filePath = ".." + File.separator + "programs" + File.separator + fileName + File.separator + "arc_prim" + File.separator + methodName + "_blockToLineNum.csv";
			}
			File myObj = new File(filePath);
			
			if(flag == 1){
            	FileWriter fw = new FileWriter(filePath);
				fw.write(Integer.toString(blockToSuccessor.size()));
				fw.write("\n\n");
				for (Map.Entry<Integer, List<Integer>> entry : blockToSuccessor.entrySet()) {
					Integer key = entry.getKey();
					fw.write(Integer.toString(key));
					fw.write("\n");
					List<Integer> value = entry.getValue();
					String tmpLine = "\t";
					for(Integer val: value){
						tmpLine += Integer.toString(val) + " ";
					}
					tmpLine += "0";
					fw.write(tmpLine);
					if(key != blockToSuccessor.size()){
						fw.write("\n");
					}
				}
				fw.close();
			}
			else{
				PrintWriter pw = new PrintWriter(myObj);
				StringBuilder builder = new StringBuilder();
				for (Map.Entry<Integer, List<Integer>> entry : blockToSuccessor.entrySet()) {
					Integer blockNumKey = entry.getKey();
					builder.append(blockNumKey+",");
					List<Integer> lineNumValue = entry.getValue();
					for(Integer val: lineNumValue){
						builder.append(val+",");
					}
					builder.deleteCharAt(builder.lastIndexOf(","));
					builder.append("\n");
				}
				pw.write(builder.toString());
				pw.close();
			}
				
            
        } catch (IOException e) {
            e.printStackTrace();
        }

        
	}


	public static List<Integer> makeSequence(int begin, int end) {
		List<Integer> ret = new ArrayList<>(end - begin + 1);
		for (int i=begin; i<=end; i++) {
			ret.add(i);
		}
		return ret;  
	}

	public static Map<String, List<Integer>> updateListDict(String varUse, Integer blockNum, Map<String, List<Integer>> dict){
		List<Integer> tmp;
		if(dict.containsKey(varUse)){
			tmp = dict.get(varUse);
			if(!tmp.contains(blockNum)){
				tmp.add(blockNum);
			}
		}
		else{
			tmp = new ArrayList<Integer>();
			tmp.add(blockNum);
		}
		dict.put(varUse, tmp);
		return dict;
	}

	public static List<String> unitContainsVarName(Unit unit, List<String> varList){
		List<String> suchVars = new ArrayList<String>();
		List<String> rhs = new ArrayList<String>();
		if(unit instanceof AbstractDefinitionStmt){
			rhs = Arrays.asList(unit.toString().split("=")[1].split(" "));
		}
		else{
			rhs = Arrays.asList(unit.toString().split(" "));
		}
		for(String varr: rhs){
			for(String var: varList){
				if(varr.equals(var)){
					suchVars.add(var);
				}
			}
		}
		return suchVars;
	}

	public static void mutateUOI(String fileName, String methodName) throws IOException {
		String clsName = fileName;
		String sourceDirectory = "../programs/" + fileName;
        
        G.reset();
		Options.v().set_keep_line_number(true);
        Options.v().set_prepend_classpath(true);
        Options.v().set_allow_phantom_refs(true);
        Options.v().set_soot_classpath(sourceDirectory);
		Options.v().setPhaseOption("tag.ln", "on");
		Options.v().setPhaseOption("jb", "optimize:false");
		Options.v().setPhaseOption("jb", "use-original-names:true");

        SootClass sc = Scene.v().loadClassAndSupport(clsName);
        sc.setApplicationClass();
        Scene.v().loadNecessaryClasses();
        // Retrieve method's body
        SootClass mainClass = Scene.v().getSootClass(clsName);
        SootMethod sm = mainClass.getMethodByName(methodName);
        JimpleBody body = (JimpleBody) sm.retrieveActiveBody();

		Body mBody=body;
		List<String> varList = new ArrayList<String>();
		for (Local local : mBody.getLocals()) {
			String varName = local.getName();
			varList.add(varName);
		}
		LocalDefs mLocalDefs = LocalDefs.Factory.newLocalDefs(mBody);
		LocalUses mLocalUses = LocalUses.Factory.newLocalUses(mBody, mLocalDefs);
		BlockGraph blockGraph = new BriefBlockGraph(mBody);
		List<Block> blocks = blockGraph.getBlocks();
		Integer numOfBlocks = 0;
		List<Integer> emptyBlocks = new ArrayList<Integer>();
		for(Block block: blocks){
			if(isEmptyBlock(block)){
				int numBlock = block.getIndexInMethod();
				emptyBlocks.add(numBlock);
			}
		}
		Map<Integer, Integer> lineNumToBlock = new HashMap<Integer, Integer>();
		Map<Unit, Integer> mUnitToBlockMap = new HashMap<Unit, Integer>();
		Map<Integer, List<Unit>> mBlockToUnitsMap = new HashMap<Integer, List<Unit>>();
		Map<Unit, List<String>> mUnitToUseDict = new HashMap<Unit, List<String>>();
		Map<Unit, List<String>> mUnitToDefDict = new HashMap<Unit, List<String>>();
		Map<String, List<Integer>> mVarNameToBlockDict = new HashMap<String, List<Integer>>();
		Map<Integer, Map<String, List<String>>> mBlockToVarFirstLastUselineDict = new HashMap<>();
		Iterator<Block> blockIt = blocks.iterator();
		List<UnitValueBoxPair> uses;
		Iterator<UnitValueBoxPair> useIt;
		Integer startLineNumOfBlock=0;
		Integer endLineNumOfBlock=0;
		String def;
		Unit unit=null;
		Unit beforeUnit=null;
		while (blockIt.hasNext()) {
			Block block = blockIt.next();
			Integer numBlock = block.getIndexInMethod();
			Iterator<Unit> unitIt = block.iterator();
			Integer unitCount = 0;
			List<String> useVars = new ArrayList<String>();
			List<String> encounteredVars = new ArrayList<String>();
			Map<String, Integer> useVarLineNum = new HashMap<String, Integer>();
			Map<String, String> varNameToFirst = new HashMap<String, String>();
			Map<String, String> varNameToLast = new HashMap<String, String>();
			while (unitIt.hasNext()) {
				if(unitCount > 0){
					beforeUnit = unit;
				}
				unit = unitIt.next();
				if(!emptyBlocks.contains(numBlock)){
					if(unitCount==1){
						startLineNumOfBlock = Integer.parseInt(unit.getTags().toString().split("sline: ")[1].split(" ")[0]);
					}
				}
				unitCount++;
				
				// unit-block dict
				mUnitToBlockMap.put(unit, numBlock);
				if(mBlockToUnitsMap.containsKey(numBlock)){
					mBlockToUnitsMap.get(numBlock).add(unit);
				}
				else{
					List<Unit> UnitsList = new ArrayList<Unit>();
					UnitsList.add(unit);
					mBlockToUnitsMap.put(numBlock, UnitsList);
				}



				// unit-use dict
				List<String> lUse = mUnitToUseDict.get(unit); //see if you already have a list for current key
				List<String> suchVars = unitContainsVarName(unit, varList);
				if(lUse == null) { //if not create one and put it in the map
					lUse = new ArrayList<String>();
				}
				for(String use: suchVars){
					if(!lUse.contains(use)){
						lUse.add(use); //add s[0] into the list for current key
					}
					
				}
				for(String varUse: lUse){
					if(varUse.startsWith("temp") || varUse=="this"){}
					else{
						useVars.add(varUse);
						if(!encounteredVars.contains(varUse)){
							encounteredVars.add(varUse);	
							varNameToFirst.put(varUse, "use");
						}
						Integer __lineNum = Integer.parseInt(unit.getTags().toString().split("sline: ")[1].split(" ")[0]);
						useVarLineNum.put(varUse, __lineNum);
						updateListDict(varUse, numBlock, mVarNameToBlockDict);
						varNameToLast.put(varUse, "use");
					}
				}
				mUnitToUseDict.put(unit, lUse);


				// unit-def dict
				if(unit instanceof AbstractDefinitionStmt){
					def = unit.toString().split("=")[0].trim();
					List<String> lDef = mUnitToDefDict.get(unit); //see if you already have a list for current key
					if(lDef == null) { //if not create one and put it in the map
						lDef = new ArrayList<String>();
					}
					if(!lDef.contains(def)){
						lDef.add(def);
					}
					for(String varDef: lDef){
						if(varDef.startsWith("temp") || varDef=="this"){}
						else{
							if(useVars.contains(varDef)){
								Integer ___lineNum = useVarLineNum.get(varDef);
								mutate(varDef, ___lineNum, "UOIpostminus", 0.0, fileName, methodName);
								mutate(varDef, ___lineNum, "UOIpostplus", 0.0, fileName, methodName);
							}
							if(!encounteredVars.contains(varDef)){
								encounteredVars.add(varDef);
								varNameToFirst.put(varDef, "def");
							} 
							updateListDict(varDef, numBlock, mVarNameToBlockDict);
							varNameToLast.put(varDef, "def");
						}
					}
					mUnitToDefDict.put(unit, lDef);
				}
			}
			for(String encounteredVar: encounteredVars){
				List<String> tmp__ = new ArrayList<String>();
				Map<String, List<String>> mapTmp = new HashMap<String, List<String>>();
				tmp__.add(varNameToFirst.get(encounteredVar)); //is first appearance use or def?
				tmp__.add(varNameToLast.get(encounteredVar)); //is last appearance use or def?
				try{
					tmp__.add(Integer.toString(useVarLineNum.get(encounteredVar)));
				}
				catch (Exception e){
					tmp__.add("0");
				}
				mapTmp.put(encounteredVar, tmp__);
				if(mBlockToVarFirstLastUselineDict.containsKey(numBlock)){
					Map<String, List<String>> tmpM = mBlockToVarFirstLastUselineDict.get(numBlock);
					tmpM.putAll(mapTmp);
					mBlockToVarFirstLastUselineDict.put(numBlock, tmpM);
				}
				else{
					mBlockToVarFirstLastUselineDict.put(numBlock, mapTmp);
				}
			}

			if(!emptyBlocks.contains(numBlock)){
				if(unit.toString().contains("goto")){
					endLineNumOfBlock = Integer.parseInt(beforeUnit.getTags().toString().split("sline: ")[1].split(" ")[0]);
				}
				else{
					endLineNumOfBlock = Integer.parseInt(unit.getTags().toString().split("sline: ")[1].split(" ")[0]);
				}
				List<Integer> range = makeSequence(startLineNumOfBlock, endLineNumOfBlock);
				for(Integer _lineNum: range){
					lineNumToBlock.put(_lineNum, numBlock);
				}
			}
		}
		
		String pathInLine = "../programs/"+fileName+"/log/pathInLine.csv";
		String row;
        BufferedReader csvReader = new BufferedReader(new FileReader(pathInLine));
		List<List<Integer>> aggregated = new ArrayList<>();
		
        while ((row = csvReader.readLine()) != null) {
			List<Integer> pathInBlockNum = new ArrayList<Integer>();
            List<String> data = Arrays.asList(row.split(","));
            ListIterator<String> it = data.listIterator();
            while(it.hasNext()){
				Integer lineNum = Integer.parseInt(it.next())-2;
				Integer blockNum = lineNumToBlock.get(lineNum);
				Integer arraySize = pathInBlockNum.size();
				if(blockNum != null){
					if(arraySize > 0 && pathInBlockNum.get(arraySize-1) == blockNum){ // same as the previous 
						continue;
					}
					pathInBlockNum.add(blockNum);
				}
            }
			aggregated.add(pathInBlockNum);
        }
        csvReader.close();	

		// 관련된 항만 뽑는다
		for (Local local : mBody.getLocals()) {
			Map<Integer, List<Integer>> precedenceDict = new HashMap<Integer, List<Integer>>();
			String varName = local.getName();
			if(varName.startsWith("temp") || varName=="this") continue;
			List<Integer> blocksVarName = mVarNameToBlockDict.get(varName);
			for(List<Integer> pIBN: aggregated){ //path in block number
				List<Integer> tp = new ArrayList<Integer>(); // is this variable associated with this block in the path?
				for(Integer pi: pIBN){
					if(blocksVarName.contains(pi)&&!tp.contains(pi)){
						tp.add(pi);
					}
				}
				if(tp.isEmpty()){
					continue;
				}
				precedenceDict = getOrder(precedenceDict, tp);
			}

			for (Map.Entry<Integer, List<Integer>> entry : precedenceDict.entrySet()) {
				Integer beforeBlock = entry.getKey();
				List<Integer> afterBlocks = entry.getValue();
				List<String> triples = mBlockToVarFirstLastUselineDict.get(beforeBlock).get(varName);

				if(triples.get(0).equals("use")){
					boolean isDef = true;
					for(Integer afterBlock: afterBlocks){
						if(!triples.get(1).equals("def")){
							isDef = false;
						}
					}
					if(isDef){
						int line__ = Integer.parseInt(triples.get(2));
						if(line__ == 0){
							throw new IllegalArgumentException("WHAT?!!");
						}
						mutate(varName, line__, "UOIpostplus", 0.0, fileName, methodName);
						mutate(varName, line__, "UOIpostminus", 0.0, fileName, methodName);
					}
				}
			}
		}



		// mutate(String targetVar, int line, String mutop, double replacement, String fileName, String methodName)
		// if return value is not boolean 
		String retype = sm.getReturnType().toString();
		if(!retype.equals("boolean")){
			Map<Integer, String> returnLinesVar = findReturnLinesVar(fileName);
			for (Map.Entry<Integer, String> entry : returnLinesVar.entrySet()) {
				Integer returnLine = entry.getKey();
				String returnVar = entry.getValue();
				mutate(returnVar, returnLine, "UOIpostplus", 0.0, fileName, methodName);
				mutate(returnVar, returnLine, "UOIpostminus", 0.0, fileName, methodName);
			}
		}
		
	}

	public static Map<Integer, String> findReturnLinesVar(String fileName) throws IOException {
		String filePath = "../programs/" + fileName + "/" + fileName + ".java";
		Map<Integer, String> res = new HashMap<Integer, String>();
		Integer count = 0;
		Integer lineNum = 0;
		String returnVar = "";
		String line = "";
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
        while ((line = reader.readLine()) != null) {
			if(line.contains("return") && line.contains(";")){
				lineNum = count;	
				returnVar = line.split("return ")[1].split(";")[0];
				res.put(lineNum, returnVar);
			}
			count += 1;
		}
		return res;
	}

	// @ret {1: 2} dict sth like this. Which means, block 1 is always executed before block 2
	public static Map<Integer, List<Integer>> getOrder(Map<Integer, List<Integer>> precedenceDict, List<Integer> path){
		int lhs = 0;
		int rhs = 0;
		for(int i=0; i<path.size()-1; i++){
			lhs = path.get(i);
			rhs = path.get(i+1);

			if(precedenceDict.containsKey(lhs)){
				if(precedenceDict.get(lhs).contains(rhs)){
					continue;
				}
				else{
					String delim = lhs+","+rhs;
					// path.split("lhs, rhs") contains tmp -> discard tmp
					List<String> strList = new ArrayList<>();
					for (Integer integer : path) {
						strList.add(String.valueOf(integer));
					}
					List<String> tmptmp = Arrays.asList(String.join(",", strList).split(delim));
					List<Integer> rhs_ = precedenceDict.get(lhs);
					if(tmptmp.size()>1){
						List<String> tmptmptmp = tmptmp.subList(1, tmptmp.size()-1);
						for(String tt: tmptmptmp){
							for(Integer tmp: precedenceDict.get(lhs)){			
								if(Integer.parseInt(tt) == tmp){
									rhs_.remove(tmp);
								}
							}
						}
					}
					rhs_.add(rhs);
					precedenceDict.put(lhs, rhs_);
				}
			}
			else{
				List<Integer> rhs_ = new ArrayList<Integer>();
				rhs_.add(rhs);
				precedenceDict.put(lhs, rhs_);
			}
		}
		return precedenceDict;
	}
    
}
