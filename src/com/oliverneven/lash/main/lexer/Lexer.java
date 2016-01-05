package com.oliverneven.lash.main.lexer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.oliverneven.lash.main.Lash;
import com.oliverneven.lash.main.token.TokenData;
import com.oliverneven.lash.main.token.TokenType;


public class Lexer {
	
	private ArrayList<Character> code_chars;
	private Lexer block_lexer;
	
	public Lexer(final File code_file) {
		try {
			code_chars = listCharacters(code_file);
		} catch (IOException e) {
			code_chars = new ArrayList<>();
			Lash.err(e);
		}
	}
	public Lexer(final String code_string) {
		this.code_chars = new ArrayList<>();
		for (char c : code_string.toCharArray())
			this.code_chars.add(c);
		
		// System.out.println(code_chars);
	}
	public Lexer(final ArrayList<Character> code_chars) {
		this.code_chars = code_chars;
	}
	
	
	/** Tokenizes the file */
	public ArrayList<TokenData> tokenize() {
		
		String expression = new String(), variable_name = new String();
		ArrayList<TokenData> token_data_list = new ArrayList<>();
		TokenType matched_token_type;
		String token = new String();
		for (int i = 0; i < code_chars.size(); i ++) {
			token += code_chars.get(i);
			
			//System.out.println(token);
			
			matched_token_type = TokenType.checkMatch(token.trim());
			if (matched_token_type != TokenType.UNKOWN) {
				
				System.out.println("Found a(n) " + matched_token_type + " token!");
					
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
				
				// If it's a block, tokenize and add it to the list
				else if (matched_token_type == TokenType.BLOCK) {
					
					System.out.println("{");
					
					block_lexer = new Lexer(token.trim().substring(1, token.trim().length() - 1));
					ArrayList<TokenData> block_token_data_list = block_lexer.tokenize();
					
					token_data_list.add(new TokenData(TokenType.OPEN_BLOCK, true));
					for (TokenData block_token_data : block_token_data_list) {
						token_data_list.add(block_token_data);
					}	
					token_data_list.add(new TokenData(TokenType.CLOSE_BLOCK, true));
					token_data_list.add(new TokenData(";", TokenType.TERMINATOR));
					
					System.out.println("}");
					
				}
				
				else if (matched_token_type == TokenType.ASSINGN_EQ && code_chars.get(i + 1) == '=') {
					token_data_list.add(new TokenData(TokenType.CONDITION_EQ, true));
					i ++;
				}
				
				// If it's a comment or end of line tag, do nothing
				else if (matched_token_type == TokenType.COMMENT || matched_token_type == TokenType.ENDOFLINE || matched_token_type == TokenType.EXPRESSION);
				
				// If it's anything else, just trim it
				else {
					token_data_list.add(new TokenData(token.trim(), matched_token_type));
				}
				
				// Clear the token
				token = new String();
			}
			
		}
		
		System.out.println("Total tokens found: " + token_data_list);
		
		return token_data_list;
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
