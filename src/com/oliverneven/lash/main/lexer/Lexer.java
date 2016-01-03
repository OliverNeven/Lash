package com.oliverneven.lash.main.lexer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.oliverneven.lash.main.Lash;


public class Lexer {
	
	private File code_file;
	
	public Lexer(File code_file) {
		this.code_file = code_file;
	}
	
	/** Tokenizes the file */
	public void tokenize() {
		ArrayList<Character> code_chars;
		try {
			code_chars = listCharacters(code_file);
		} catch (IOException e) {
			Lash.err(e);
			return;
		}
		
		Action action = null;
		String token = new String();
		int state_func = 0;
		boolean append_next = true, state_string = false;
		for (char c : code_chars) {
			
			//System.out.println(token);
			
			if (c == '\"' || c == '\'') {
				append_next = false;
				if (state_string) {
					//System.out.println("Found ending of a string!");
					if (state_func == 1) {
						action.exec(token);
						token = "";
						state_func = 0;
					}
					state_string = false;
				} else {
					//System.out.println("Found beginning of a string!");
					state_string = true;
				}
			} else if (Action.eq(token, "echo"))  {
				//System.out.println("Found an echo!");
				action = Action.ECHO_ACTION;
				token = "";
				state_func = 1;
			}if (c == ' ') {
				if (!state_string)
					append_next = false;
			}
			
			
			if (append_next)
				token += c;
			else 
				append_next = true;
		}
		
	}
	
	
	/** Reads and returns all the characters from a file 
	 * @throws IOException */
	private ArrayList<Character> listCharacters(File file) throws IOException {
		
		FileReader rf = new FileReader(file);
		BufferedReader br = new BufferedReader(rf);
		
		ArrayList<Character> file_characters = new ArrayList<>();
		String temp_line;
		while ((temp_line = br.readLine()) != null)
			for (char c : temp_line.toCharArray())
				file_characters.add(c);
		
		return file_characters;
	}
	
	
	public File getFile() {
		return code_file;
	}
	public void setFile(File code_file) {
		this.code_file = code_file;
	}
	
	
}
