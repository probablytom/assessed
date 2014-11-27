//////////////////////////////////////////////////////////////
//
// Driver for the Fun compiler and SVM interpreter.
//
// Developed June 2012 by David Watt (University of Glasgow).
//
//////////////////////////////////////////////////////////////


import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;
import java.util.*;
import java.io.*;

public class FunRun {

	private static boolean tracing = false;

	private static PrintStream out = System.out;

	public static void main(String[] args) {
	// Compile a Fun source program to SVM code, 
	// then interpret it if it compiles successfully.
	// The source file name must be given as the 
	// first program argument.
		try {
			if (args.length == 0)
				throw new FunException();
			InputStream source =
			   new FileInputStream(args[0]);
			SVM objprog = compile(source);

			out.println("Interpretation ...");
			objprog.interpret(tracing);
		} catch (FunException x) {
			out.println("Compilation failed");
		} catch (Exception x) {
			x.printStackTrace(out);
		}
	}

	private static SVM compile (InputStream source)
			throws Exception {
	// Compile a Fun source program to SVM code.
		CommonTree ast =
		   syntacticAnalyse(source);
		contextualAnalyse(ast);
		SVM objprog =
		   codeGenerate(ast);
		return objprog;
	}

	private static CommonTree syntacticAnalyse
			(InputStream source)
			throws Exception {
	// Perform syntactic analysis of a Fun source program.
	// Print any error messages.
	// Return an AST representation of the Fun program.
	// Also print the AST.
		out.println();
		out.println("Syntactic analysis ...");
		FunLexer lexer = new FunLexer(
		   new ANTLRInputStream(source));
		CommonTokenStream tokens = 
		   new CommonTokenStream(lexer);
		FunParser parser = new FunParser(tokens);
		CommonTree ast =
		   (CommonTree) parser.program().getTree();
		int errors = parser.getNumberOfSyntaxErrors();
		out.println(errors + " syntactic errors");
		if (errors > 0)
			throw new FunException();
		out.println("AST:");
		out.println(indented(ast, "  "));
		return ast;
	}

	private static void contextualAnalyse (CommonTree ast)
			throws Exception {
	// Perform contextual analysis of a Fun program, 
	// represented by an AST.
	// Print any error messages.
		out.println("Contextual analysis ...");
		FunChecker checker =
		   new FunChecker(new CommonTreeNodeStream(ast));
		checker.program();
		int errors = checker.getNumberOfContextualErrors();
		out.println(errors + " scope/type errors");
		out.println();
		if (errors > 0)
			throw new FunException();
	}

	private static SVM codeGenerate (CommonTree ast)
			throws Exception  {
	// Perform code generation of a Fun program, 
	// represented by an AST, emitting SVM code.
	// Also print the object code.
		out.println("Code generation ...");
		FunEncoder encoder =
		   new FunEncoder(new CommonTreeNodeStream(ast));
		SVM objectprog = encoder.program();
		out.println("Object code:");
		out.println(objectprog.showCode());
		return objectprog;
	}

	private static String indented (CommonTree ast,
	                         String indent) {
	// Return an indented textual representation of the 
	// given (sub)AST.
		String s = indent + ast.getToken().getText() + "\n";
		String moreindent = "  " + indent;
		//List<CommonTree> subtrees = ast.getChildren();
		List<CommonTree> subtrees = (List<CommonTree>)ast.getChildren();
		if (subtrees != null) {
		    for (CommonTree subtree : subtrees)
				s += indented(subtree, moreindent);
		}
		return s;
	}

	private static class FunException extends Exception {
	}

}
