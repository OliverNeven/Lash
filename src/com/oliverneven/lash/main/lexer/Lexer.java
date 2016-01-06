package com.oliverneven.lash.main.lexer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

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
			t += c; t = t.trim();									// Add the current character to t and trim t
			boolean reset = true;									// If t is allowed to be reset
			
			// Print the current token
			// if (v) System.out.println(t);
			
			// Check if there is a match with the current token, or a UNKOWN token will be returned
			TokenType match = TokenType.checkMatch(t);
			
			// Print the match
			if (v && match != TokenType.UNKNOWN && match != TokenType.BLOCK && match != TokenType.BLOCK_OPEN && match != TokenType.BLOCK_CLOSE) System.out.format("Found a(n) %s token.\n", match.toString());
			
			
			
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
				
				main_block.addToken(new TokenData(new Lexer(new String(t.substring(1, t.length() - 1))).lex(v), TokenType.BLOCK));
				reset = true;
				
				if (v) System.out.println("} ~~");
				
			}
			
			/* TAGS */
			
			// If an unknown token tag is found
			else if (match == TokenType.UNKNOWN)
				reset = false;
			
			// If an end of line tag is found
			else if (match == TokenType.ENDOFLINE)
				reset = true;
			
			/* DEFAULT */
			else
				main_block.addToken(new TokenData(t, match));
			
			
			if (reset)
				t = new String();
		}
		
		
		
		if (v) System.out.format("Total found tokens: %s\n", main_block);
		return main_block;
	}
	
	
	
	
	
	
	
	
	
	
	
	/*
	public TokenBlock tokenize() {
		
		String expression = new String(), variable_name = new String();
		ArrayList<TokenData> token_data_list = new ArrayList<>();
		TokenType matched_token_type;
		String token = new String(), token_char;
		for (int i = 0; i < code_chars.size(); i ++) {
			token_char = code_chars.get(i).toString();
			token += token_char;
			
			//System.out.println("  ~ '" + token_char + "'" + " == " + "'" + TokenType.BLOCK_OPEN.getRegex() + "' :" + token_char.equals(TokenType.BLOCK_OPEN.getRegex()));
			//System.out.print(": '"+ TokenType.BLOCK_CLOSE.getRegex() +"' :" + token_char.equals(TokenType.BLOCK_CLOSE.getRegex()));
			System.out.print(token);
			
			matched_token_type = TokenType.checkMatch(token.trim());
			//if (matched_token_type != TokenType.UNKOWN) {
				
				if (matched_token_type != TokenType.UNKOWN)
					System.out.println("\nFound a(n) " + matched_token_type + " token!");
					
				// If it's a command, add it as one 
				if (matched_token_type.isCommand() && !matched_token_type.isOperationCommand())
					token_data_list.add(new TokenData(matched_token_type, false));
				
				// If it's an expression, append it to the expression string
				else if (matched_token_type == TokenType.EXPRESSION)
					expression += token;
				
				// If it's not an expression, but the expression string is not empty, add it to the list and clear the string
				else if (!expression.isEmpty()) {
					token_data_list.add(new TokenData(expression.trim(), TokenType.EXPRESSION));
					expression = new String();
					token_data_list.add(new TokenData(token.trim(), matched_token_type));
				}
				
				// If it's a string, remove quotes and trim it
				else if (matched_token_type == TokenType.STRING) {
					String str = token.trim();
					token_data_list.add(new TokenData(str.substring(1, str.length() - 1), matched_token_type));
				}
				
				// If it's a variable, keep adding characters as a variable name, until an invalid variable name shows up.
				else if (matched_token_type == TokenType.VARIABLE) {
					for (i = i + 1; i < code_chars.size(); i ++) {
						
						token += code_chars.get(i);
						
						matched_token_type = TokenType.checkMatch(token.trim());
						
						if (matched_token_type != TokenType.VARIABLE) {
							token = token.trim().substring(1, token.trim().length() - 1).trim(); // Remove leading and tailing characters
							break;
						}
					}
					i --;
					
					//variable_name += token;
					token_data_list.add(new TokenData(token.trim(), TokenType.VARIABLE));
				}
				
				else if (token_char.equals(TokenType.BLOCK_OPEN.getRegex())) {
					block_indet_amount ++;
					System.out.println("Block indent = " + block_indet_amount);
				}
				
				else if (token_char.equals(TokenType.BLOCK_CLOSE.getRegex())) {
					block_indet_amount --;
					System.out.println("Block indent = " + block_indet_amount);
				}
				
				// If it's a block, tokenize and add it to the list
				if (matched_token_type == TokenType.BLOCK && block_indet_amount == 0) {
					
					
					System.out.println("{");
					
					token_data_list.add(new TokenData(new Lexer(token.trim().substring(1, token.trim().length() - 1)).tokenize(), TokenType.BLOCK));
					
					System.out.println("}");
					
					//token = new String();
				}
				
				else if (matched_token_type == TokenType.ASSINGN_EQ && code_chars.get(i + 1) == '=') {
					token_data_list.add(new TokenData(TokenType.CONDITION_EQ, true));
					i ++;
				}
				
				// If it's a comment or end of line tag, do nothing
				else if (matched_token_type == TokenType.COMMENT || matched_token_type == TokenType.ENDOFLINE || matched_token_type == TokenType.EXPRESSION);
				
				// If it's anything else, just trim it
				else if (matched_token_type != TokenType.UNKOWN) {
					token_data_list.add(new TokenData(token.trim(), matched_token_type));
				}
				
				// Clear the token
				if (matched_token_type != TokenType.BLOCK_OPEN && matched_token_type != TokenType.BLOCK_CLOSE && matched_token_type != TokenType.BLOCK && matched_token_type != TokenType.UNKOWN)
					token = new String();
			//}
			
		}
		
		System.out.println("Total tokens found: " + token_data_list);
		
		return new TokenBlock(token_data_list, code_chars);
	}
	*/
	
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
