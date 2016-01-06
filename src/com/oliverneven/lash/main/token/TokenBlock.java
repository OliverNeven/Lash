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
