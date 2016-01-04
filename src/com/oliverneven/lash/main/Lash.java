package com.oliverneven.lash.main;

import java.io.File;
import java.util.ArrayList;

import com.oliverneven.lash.main.lexer.Lexer;
import com.oliverneven.lash.main.token.TokenData;


public class Lash {
	
	public static void main(String[] args) {
		
		// TODO: Follow tutorial https://www.youtube.com/watch?v=LDDRn2f9fUk
		
		File code_file = null;
		
		/* TODO: Remove debug code */ code_file = new File("src\\sample\\hello_world.lash");
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
		
		
		Lexer lex = new Lexer(code_file);
		ArrayList<TokenData> tokens = lex.tokenize();
		
		//System.out.println(tokens + "\n");
		
		Parser par = new Parser(tokens);
		par.parse();
		
		
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
	public static void out(String s) {
		System.out.println(s);
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
