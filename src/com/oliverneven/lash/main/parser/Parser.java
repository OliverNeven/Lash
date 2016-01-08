package com.oliverneven.lash.main.parser;

import java.util.ArrayList;

import com.oliverneven.lash.main.Lash;
import com.oliverneven.lash.main.token.TokenBlock;
import com.oliverneven.lash.main.token.TokenData;
import com.oliverneven.lash.main.token.TokenType;


public class Parser {
	
	private TokenBlock main_block;
	
	public Parser(final TokenBlock main_block) {
		this.main_block = main_block;
	}
	
	/** Parses the tokens */
	public void parse(boolean v) {
		
		// The previous iterated token
		TokenData pre_token = new TokenData(new String(";"), TokenType.TERMINATOR);
		
		// Iterate through each token
		for (int i = 0; i < main_block.getTokenList().size(); i ++) {
			
			// Store the current token
			TokenData token = main_block.getTokenList().get(i);		
			
			// The token arguments that will be parsed to the commands and operations
			ArrayList<TokenData> args = null;
			
			// If the token is an operation
			if (token.isOperation()) {
				
				if (v) System.out.format("Found a(n) %s operation.\n", token);
				
				// If the token is an equals operation, parse the previous and next tokens, until a terminator appears, as arguments
									
				// Check for invalid operands for the operator
				if (pre_token.getTokenType() == TokenType.TERMINATOR) 
					Lash.err("The token " + pre_token + " is an invalid operand", 404); // Do a 404 exit
				
				// Get all the tokens until a terminator appears
				args = new ArrayList<>();
				args.add(pre_token);
				args.addAll(main_block.charsUntilToken(i + 1, TokenType.TERMINATOR));
				i += args.size() - 2;
				
				// Execute the operation
				token.getTokenType().getAction().exec(args);
				
				if (v) System.out.format("Parsed %s as arguments for the %s operation.\n", args, token);
			}
			
			// If the token is an command
			else if (token.isCommand()) {
				
				if (v) System.out.format("Found a(n) %s command.\n", token);
				
				// The token arguments that will be parsed to the command
				args = main_block.charsUntilToken(i + 1, TokenType.TERMINATOR);
				i += args.size() - 2;
				
				if (v) System.out.format("Parsed %s as arguments for the %s command.\n", args, token);
			}
			
			else if (v) System.out.format("Found a(n) %s token.\n", token);
			
			System.out.println(TokenType.ECHO.isCommand());
			
			
			// Set the token as the previous token
			pre_token = token;
		}
		
		
		
		
		
		
		
		
		
		
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
