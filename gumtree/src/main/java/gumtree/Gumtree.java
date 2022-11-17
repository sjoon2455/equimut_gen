package gumtree;

import com.github.gumtreediff.client.Run;
import com.github.gumtreediff.gen.TreeGenerators;
import com.github.gumtreediff.matchers.MappingStore;
import com.github.gumtreediff.matchers.Matcher;
import com.github.gumtreediff.matchers.Matchers;
import com.github.gumtreediff.tree.Tree;
import com.github.gumtreediff.gen.jdt.JdtTreeGenerator;
import com.github.gumtreediff.tree.TreeContext;

import com.github.gumtreediff.actions.SimplifiedChawatheScriptGenerator;
import com.github.gumtreediff.actions.EditScriptGenerator;
import com.github.gumtreediff.actions.EditScript;
import com.github.gumtreediff.actions.model.Action;

import java.io.IOException;
import java.util.List;

/**
 * Hello world!
 *
 */
public class Gumtree 
{
    public static void main( String[] args ) throws IOException
    {
        System.out.println( "Hello World!" );
        Run.initGenerators(); // registers the available parsers
		String srcFile = "f1.java";
		String dstFile = "f2.java";
		Tree src = new JdtTreeGenerator().generateFrom().file(srcFile).getRoot();
		Tree dst = new JdtTreeGenerator().generateFrom().file(dstFile).getRoot();
		Matcher defaultMatcher = Matchers.getInstance().getMatcher(); // retrieves the default matcher
		MappingStore mappings = defaultMatcher.match(src, dst); // computes the mappings between the trees
		// String a = mappings.toString();
		// System.out.println("a: " + a);
		EditScriptGenerator editScriptGenerator = new SimplifiedChawatheScriptGenerator(); // instantiates the simplified Chawathe script generator
		EditScript actions = editScriptGenerator.computeActions(mappings); // computes the edit script
		List<Action> actionsAll = actions.asList();
		System.out.println("actionsAll: " + actionsAll);

    }
    
}
