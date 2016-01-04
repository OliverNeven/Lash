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
	
	private File code_file;
	
	public Lexer(File code_file) {
		this.code_file = code_file;
	}
	
	/** Tokenizes the file */
	public ArrayList<TokenData> tokenize() {
		ArrayList<Character> code_chars;
		try {
			code_chars = listCharacters(code_file);
		} catch (IOException e) {
			Lash.err(e);
			return null;
		}
		
		ArrayList<TokenData> token_data_list = new ArrayList<>();
		String token = new String();
		for (char c : code_chars) {
			token += c;
			
			System.out.println(token);
			
			TokenType matched_token_type = TokenType.checkMatch(token.trim());
			if (matched_token_type != TokenType.UNKOWN) {
				
				System.out.println("Found a " + matched_token_type + " token!");
				
				if (matched_token_type.isCommand())
					token_data_list.add(new TokenData(matched_token_type));
				else if (matched_token_type == TokenType.STRING)
					token_data_list.add(new TokenData(token.trim().substring(1, token.length() - 2), matched_token_type)); // Remove leading and tailing quotes for strings
				else if (matched_token_type == TokenType.COMMENT || matched_token_type == TokenType.ENDOFLINE) {} // Don't parse comments and end of line tags
				else
					token_data_list.add(new TokenData(token.trim(), matched_token_type));
				
				token = new String();
			}
			
		}
		
		System.out.println("Found tokens: " + token_data_list);
		
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
	
	
	public File getFile() {
		return code_file;
	}
	public void setFile(File code_file) {
		this.code_file = code_file;
	}
	
	
}
