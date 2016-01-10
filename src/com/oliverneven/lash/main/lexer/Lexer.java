package com.oliverneven.lash.main.lexer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

import com.oliverneven.lash.main.Lash;
import com.oliverneven.lash.main.token.TokenBlock;
import com.oliverneven.lash.main.token.TokenData;
import com.oliverneven.lash.main.token.TokenType;


public class Lexer {
	
	
	private TokenBlock main_block;
	
	
	public Lexer(final File code_file) {
		
		// Read the file and put all the characters inside the main block
		try {
			main_block = new TokenBlock(listCharacters(code_file));
		} catch (IOException e) {
			Lash.err(e);
		}
		
	}
	public Lexer(final String code_string) {
		
		// Read the string and put all the characters inside the main block
		ArrayList<Character> temp_char_list = new ArrayList<>();
		for (char c : code_string.toCharArray())
			temp_char_list.add(c);
		main_block = new TokenBlock(temp_char_list);
	}
	public Lexer(final ArrayList<Character> code_chars) {
		
		// Put the character list inside the main block
		main_block = new TokenBlock(code_chars);
	}
	public Lexer(final TokenBlock code_block) {
		
		// Set the block as the main block
		main_block = code_block;
	}
	
	
	public TokenBlock lex(boolean v) {
		
		// Iterate through each character in the main block
		String t = new String();
		int block_index = 0;
		for (int i = 0; i < main_block.getCharList().size(); i ++) {
			String c = main_block.getCharList().get(i).toString();	// Set the current character to c
			t += c;													// Add the current character to t
			boolean reset = true;									// If t is allowed to be reset
			
			// Print the current token
			if (v) System.out.println(t);
			
			// Check if there is a match with the current token, or a UNKOWN token will be returned
			TokenType match = TokenType.checkMatch(t);
			
			// Print the match, unless some special tokens are found
			if (v && match != TokenType.UNKNOWN
				  && match != TokenType.BLOCK
				  && match != TokenType.BLOCK_OPEN
				  && match != TokenType.BLOCK_CLOSE
				  && match != TokenType.VARIABLE)
				System.out.format("Found a(n) %s token.\n", match.toString());
			
			
			
			/* BLOCK */
			
			// If an opening bracket is found
			if (c.equals(TokenType.BLOCK_OPEN.getRegex())) {
				block_index ++;
				
				reset = false;
				if (v) System.out.format("Block index: %s\n", block_index);
				
				continue;
			}
			
			// If a closing bracket is found
			else if (c.equals(TokenType.BLOCK_CLOSE.getRegex())) {
				if (block_index > 0)
					block_index --;
				else {
					Lash.err("Missing opening bracket");
					Lash.exit(404);
				}
				
				reset = false;
				if (v) System.out.format("Block index: %s\n", block_index);
			}
			
			// If a full on block is found, both with opening and closing bracket
			if (match == TokenType.BLOCK && block_index == 0) {
				
				if (v) {
					System.out.format("Found a(n) %s token.\n", match.toString());
					System.out.println("{ ~~");
				}
				
				main_block.addToken(new TokenData(new Lexer(new String(t.trim().substring(1, t.trim().length() - 1))).lex(v), TokenType.BLOCK));
				reset = true;
				
				if (v) System.out.println("} ~~");
				
			}
			
			/* VARIALE */
			
			// If a variable is found
			else if (match == TokenType.VARIABLE) {
				if (v) System.out.format("Found a(n) %s token.\n", match.toString());
				
				// Get all characters until a illegal variable name variable is found
				ArrayList<Character> variable_name_chars = main_block.charsUntilNonMatch(i ++, "[a-zA-Z0-9_]");
				
				// Increment the character loop index with the size of the fetched character list, subtracted by 1
				i += variable_name_chars.size() - 2;
				
				// Convert the character list to a string, and add it as a variable name to the main block
				main_block.addToken(new TokenData(stringOfList(variable_name_chars), TokenType.VARIABLE));	
			}
			
			/* Data types */
			
			// If a string is found
			else if (match == TokenType.STRING) {
				
				// Trim and remove quotes from the string
				t = t.trim();
				t = t.substring(1, t.length() - 1);
				
				// Add the string to the main block
				main_block.addToken(new TokenData(t, TokenType.STRING));
			}
			
			/* TAGS */
			
			// If an unknown token tag is found
			else if (match == TokenType.UNKNOWN)
				reset = false;
			
			// If an end of line tag is found
			else if (match == TokenType.ENDOFLINE)
				reset = true;
			
			/* Whitespace */
			
			// If the current character is a space
			else if (c.equals(new String(" ")))
				reset = false;
			
			/* DEFAULT & COMMANDS */
			else if (match.isCommand())
				main_block.addToken(new TokenData(match, false));
			else if (match != TokenType.COMMENT)
				main_block.addToken(new TokenData(t.trim(), match));
			
			
			if (reset)
				t = new String();
		}
		
		
		
		if (v) System.out.format("Total found tokens: %s\n", main_block);
		return main_block;
	}
	
	
	
	/** Return a string of a character list */
	private String stringOfList(ArrayList<Character> list) {
		
		// String to return
		String word = new String();
		
		// Iterate through the list, and add each item to the string
		for (char c : list)
			word += c;
		
		// Return the string
		return word;	
	}
	
	
	/** Reads and returns all the characters from a file 
	 * @throws IOException */
	private ArrayList<Character> listCharacters(File file) throws IOException {
		
		FileReader rf = new FileReader(file);
		BufferedReader br = new BufferedReader(rf);
		
		ArrayList<Character> file_characters = new ArrayList<>();
		String temp_line;
		while ((temp_line = br.readLine()) != null) {
			for (char c : temp_line.toCharArray())
				file_characters.add(c);
			for (char c : new String(TokenType.ENDOFLINE.getRegex().toString()).toCharArray()) // Add EOF tags to each line end
				file_characters.add(c);
		}	
		
		rf.close();
		br.close();
		
		return file_characters;
	}
	
	
}
