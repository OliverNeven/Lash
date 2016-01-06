package com.oliverneven.lash.main.parser;

import com.oliverneven.lash.main.token.TokenBlock;


public class Parser {
	
	private TokenBlock main_block;
	
	public Parser(final TokenBlock main_block) {
		this.main_block = main_block;
	}
	
	/** Parses the tokens */
	public void parse() {
		
		// Parse commands
		
		
		
		
		
		
		
		
		
		
		
		
		/*
		ArrayList<TokenData> args;
		TokenData arg_token;
		int block_count = 0;
		for (int i = 0; i < tokens.size(); i ++) {
			TokenData token = tokens.get(i);
			
			//System.out.println(token);
			
			if (token.isCommand() || token.isTag() || token.isOperationCommand()) {
				
				System.out.println("Found a(n) " + token.getTokenType() + " command found!");
				
				if (token.isOperationCommand()) {
					
					args = new ArrayList<>();
					args.add(tokens.get(i - 1));
					
					for (i ++; i < tokens.size(); i ++) {
						arg_token = tokens.get(i);
						
						if (arg_token.getTokenType() == TokenType.TERMINATOR && block_count == 0)
							break;
						
						args.add(arg_token);
					}
					
					
					
				} else {
				
					args = new ArrayList<>();
					
					for (i ++; i < tokens.size(); i ++) {
						arg_token = tokens.get(i);
						
						if (arg_token.getTokenType() == TokenType.TERMINATOR && block_count == 0)
							break;
						
						args.add(arg_token);
					}
				
				}
				
				System.out.println("Parsed " + args + " as arguments for the " + token.getTokenType() + " command");
				
				if (token.isCommand() || token.isOperationCommand())
					token.getTokenType().getAction().exec(args);
				
				//System.out.println();
				
			} else if (token.isBlock()) {
				
				System.out.println("{");
				
				new Parser((TokenBlock) token.getData()).parse();
				
				System.out.println("}");
				
			}
			
		}
		*/
	}
	
}
