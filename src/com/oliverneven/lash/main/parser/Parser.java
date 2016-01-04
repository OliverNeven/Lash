package com.oliverneven.lash.main.parser;

import java.util.ArrayList;

import com.oliverneven.lash.main.arithmetic.ArithmeticEvaluation;
import com.oliverneven.lash.main.token.TokenData;
import com.oliverneven.lash.main.token.TokenType;


public class Parser {
	
	private ArrayList<TokenData> tokens;
	private ArithmeticEvaluation arithmetic_eval;
	
	public Parser(final ArrayList<TokenData> tokens) {
		this.tokens = tokens;
		this.arithmetic_eval = new ArithmeticEvaluation();
	}
	
	/** Parses the tokens */
	public void parse() {
		
		// Evaluate expressions
		for (int i = 0; i < tokens.size(); i ++) {
			TokenData token = tokens.get(i);
			
			if (token.getTokenType() == TokenType.EXPRESSION) {
				
				System.out.println("Expression found!");
				
				double result = arithmetic_eval.evalInfix((String) token.getData());
				tokens.set(i, new TokenData(result, TokenType.EXPRESSION));
			}
		
		}
		// Parse commands
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
