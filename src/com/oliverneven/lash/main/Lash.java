package com.oliverneven.lash.main;

import java.io.File;
import java.util.Scanner;

import com.oliverneven.lash.main.eval.ArithmeticEvaluater;
import com.oliverneven.lash.main.lexer.Lexer;
import com.oliverneven.lash.main.parser.Parser;
import com.oliverneven.lash.main.token.TokenBlock;
import com.oliverneven.lash.main.variable.VariableRegistry;


public class Lash {
	
	public final static VariableRegistry VARIABLE_REGISTRY = new VariableRegistry();
	public final static Scanner RAW_INPUT = new Scanner(System.in);
	public final static ArithmeticEvaluater ARITH_EVAL = new ArithmeticEvaluater();
	
	private final static boolean DEBUG = true;
	private final static boolean SAMPLEFILE = true;
	
	public static void main(String[] args) {
		
		File code_file = null;
		
		if (SAMPLEFILE) args = new String[] {"sample_code.lash"};
		
		if (args.length >= 1)
			code_file = new File(args[0]);
		else {
			err("No specified file to run", 404);
		}
		
		
		if (!isValidFile(code_file, true)) {
			exit(404);
		}
		
		if (DEBUG) System.out.println("\n\tTokenizing\n");
		
		Lexer lexer = new Lexer(code_file);
		TokenBlock token_block = lexer.lex(DEBUG);
		
		// if (true) return;
		
		if (DEBUG) System.out.println("\n\tParsing\n");
		
		Parser parser = new Parser(token_block);
		parser.parse(DEBUG);
		
		
	}
	
	/** Checks if a file is valid to be evaluated */
	public static boolean isValidFile(File file, boolean verbal) {
		if (!file.exists()) {
			if (verbal)
				err("File does not exist");
			return false;
		}
		if (!file.isFile()) {
			if (verbal)
				err("Input is not a file");
			return false;
		}
		if (!file.canRead()) {
			if (verbal)
				err("Denide access to file");
			return false;
		}
		return true;
	}
	
	
	
	/** Prints a string to the screen */
	public static void outln(String s) {
		System.out.println(s);
	}
	public static void out(String s) {
		System.out.print(s);
	}
	
	/** Prints an error to the screen */
	public static void err(Exception e) {
		e.printStackTrace();
	}
	public static void err(Exception e, int exit_code) {
		err(e);
		exit(exit_code);
	}
	public static void err(String err) {
		System.out.println("ERROR: " + err + "!");
	}
	public static void err(String err, int exit_code) {
		err(err);
		exit(exit_code);
	}
	
	/** Exits the program */
	public static void exit(int c) {
		System.exit(c);
	}
	
	
}
