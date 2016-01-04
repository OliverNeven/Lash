package com.oliverneven.lash.main;

import java.util.ArrayList;

import com.oliverneven.lash.main.token.TokenData;
import com.oliverneven.lash.main.token.TokenType;


public class Parser {
	
	private ArrayList<TokenData> tokens;
	
	public Parser(final ArrayList<TokenData> tokens) {
		this.tokens = tokens;
	}
	
	/** Parses the tokens */
	public void parse() {
		
		for (int i = 0; i < tokens.size(); i ++) {
			TokenData token = tokens.get(i);
			
			//System.out.println(token);

			if (token.isCommand()) {
				
				System.out.println("Found a " + token.getTokenType() + " command found!");
				
				ArrayList<TokenData> args = new ArrayList<>();
				TokenData arg_token;
				for (i ++; i < tokens.size(); i ++) {
					arg_token = tokens.get(i);
					
					if (arg_token.getTokenType() == TokenType.TERMINATOR)
						break;
					
					args.add(arg_token);
				}
				
				System.out.println("Parsed: " + args + " as arguments for the " + token.getTokenType() + " command.");
				
				token.getTokenType().getAction().exec(args);
			}
			
		}
		
	}
	
}
