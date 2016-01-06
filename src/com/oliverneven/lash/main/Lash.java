package com.oliverneven.lash.main;

import java.io.File;
import java.util.Scanner;

import com.oliverneven.lash.main.lexer.Lexer;
import com.oliverneven.lash.main.parser.Parser;
import com.oliverneven.lash.main.token.TokenBlock;
import com.oliverneven.lash.main.variable.VariableRegistry;


public class Lash {
	
	public final static VariableRegistry VARIABLE_REGISTRY = new VariableRegistry();
	public final static Scanner RAW_INPUT = new Scanner(System.in);
	
	public static void main(String[] args) {
		
		// TODO: Follow tutorial https://www.youtube.com/watch?v=pN5GYufp31E
		
		File code_file;
		
		/* TODO: Remove debug code */ code_file = new File("src\\sample\\sample_code.lash");
		/*
		if (args.length >= 1)
			code_file = new File(args[0]);
		else {
			err("No specified file to run");
			exit(404);
		}
		*/
		
		if (!isValidFile(code_file, true)) {
			exit(404);
		}
		
		System.out.println("\n\tTokenizing\n");
		
		Lexer lexer = new Lexer(code_file);
		TokenBlock token_block = lexer.lex(true);
		
		// if (true) return;
		
		System.out.println("\n\tParsing\n");
		
		Parser parser = new Parser(token_block);
		parser.parse();
		
		
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
	public static void err(String err) {
		System.out.println("ERROR: " + err + "!");
	}
	
	/** Exits the program */
	public static void exit(int c) {
		System.exit(c);
	}
	
	
}
