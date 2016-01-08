package com.oliverneven.lash.main.token;

import java.util.ArrayList;


public class TokenBlock {
	
	
	private ArrayList<TokenData> token_list;
	private ArrayList<Character> char_list;
	
	
	public TokenBlock(final ArrayList<TokenData> block_token_data_list, final ArrayList<Character> block_char_list) {
		this.token_list = block_token_data_list;
		this.char_list = block_char_list;
	}
	public TokenBlock(final ArrayList<Character> block_char_list){
		this.token_list = new ArrayList<>();
		this.char_list = block_char_list;
	}
	public TokenBlock(final String block_token) {
		this.token_list = new ArrayList<>();
		this.char_list = new ArrayList<>();
		for (char c : block_token.toCharArray())
			this.char_list.add(c);
	}
	
	
	/** Returns the rest of tokens, from a given index, in the list until, and except, the given terminating token is found */
	public ArrayList<TokenData> charsUntilToken(int start_index, TokenType terminating_token) {
		
		// The array to return
		final ArrayList<TokenData> rest_of_token = new ArrayList<>();
		
		// Iterate through the list, starting at the given index
		for (int i = start_index; i < token_list.size(); i ++) {
			
			// Store the current token
			TokenData token = token_list.get(i);
			
			// If the token is the terminating token, return the array
			if (token.getTokenType() == terminating_token)
				return rest_of_token;
			
			// ... else add the token to the array
			else rest_of_token.add(token);
		}
		
		// No tokens are left, return the array
		return rest_of_token;
	}
	
	/** Returns the rest of characters, from a given index, in the list until a non match is found with the given regex */
	public ArrayList<Character> charsUntilNonMatch(int start_index, String regex) {
		
		// The array to return
		final ArrayList<Character> rest_of_chars = new ArrayList<>();
		
		// Iterate through the list, starting at the given index
		for (int i = start_index; i < char_list.size(); i ++) {
			
			// Store the current token
			char c = char_list.get(i);
			
			// If the regex if matched, return the array
			if (!Character.toString(c).matches(regex))
				return rest_of_chars;
			
			// ... else add the token to the array
			else rest_of_chars.add(c);
		}
		
		// No tokens are left, return the array
		return rest_of_chars;
	}
	
	
	public ArrayList<TokenData> getTokenList() {
		return token_list;
	}
	public void setTokenList(final ArrayList<TokenData> token_list) {
		this.token_list = token_list;
	}
	public void addToken(final TokenData token) {
		token_list.add(token);
	}
	
	public ArrayList<Character> getCharList() {
		return char_list;
	}
	public void setCharList(final ArrayList<Character> char_list) {
		this.char_list = char_list;
	}
	
	
	public String toString() {
		return new String("{"+ token_list +"}");	
	}
		
}
