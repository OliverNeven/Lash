package com.oliverneven.lash.main.parser;

import java.util.ArrayList;
import java.util.HashMap;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

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
		
		// The previous iterated if statement condition
		boolean pre_condition = false;
		
		// Iterate through each token
		for (int i = 0; i < main_block.getTokenList().size(); i ++) {
			
			// Look for the the entry point
			if (i == 0) for (i = i; i < main_block.getTokenList().size(); i ++) {
					
				// If the start token is found, then break the loop;
				if (main_block.getTokenList().get(i).getTokenType() == TokenType.START) break;
				
				// If the start token wasn't found, enter at line 1
				if (i == main_block.getTokenList().size() - 1) {
					i = 0;
					break;
				}
			}
			
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
				i += args.size();
				
				// Evaluate expressions and conditions
				args = evaluateArguments(args);
				
				// If there is 2 or more arguments
				if (args.size() >= 2) {
				
					if (v) System.out.format("Parsed %s as arguments for the %s operation.\n", args, token);
					
					// Execute the operation
					token.getTokenType().getAction().exec(args);
					
				} else {
					
					// ... else print an error, a minimum of 2 arguments is need in a binary operation
					Lash.err(String.format("The operation %s needs a minimum of 2 arguments to be parsed", token), 404);					
				}
				
			}
			
			// If the token is an command
			else if (token.isCommand()) {
				
				if (v) System.out.format("Found a(n) %s command.\n", token);
				
				boolean condition = false;
				
				// The token arguments that will be parsed to the command
				args = main_block.charsUntilToken(i + 1, TokenType.TERMINATOR);
				i += args.size() + 1;
				
				// Evaluate expressions and conditions
				args = evaluateArguments(args);
				
				if (v) System.out.format("Parsed %s as arguments for the %s command.\n", args, token);
				
				// If the command is a while loop
				if (token.getTokenType() == TokenType.WHILE) {
					args.add(main_block.getTokenList().get(i - args.size()));
					
					// Execute the command
					condition = token.getTokenType().getAction().exec(args);
				}
				
				// If the command is an else statement
				else if (token.getTokenType() == TokenType.ELSE || token.getTokenType() == TokenType.ELIF) {
					
					// If the previous token was an if statement
					if (pre_token.getTokenType() == TokenType.IF || pre_token.getTokenType() == TokenType.ELIF) {
						
						// If it's condition was false, then execute the else statement
						if (!pre_condition)
							condition = token.getTokenType().getAction().exec(args);		
						
					} else 
						// ... else if the previous token, wasn't an if statement, then cast an error because there is nothing 'to else' from.
						Lash.err("No prior if or elif statement for the current statement", 404);
						
				} else
					// Execute the command
					condition = token.getTokenType().getAction().exec(args);
				
				// If the previous command was an if or elif statement, set the condition to its result
				if (token.getTokenType() == TokenType.IF)
					pre_condition = condition;
				else if (token.getTokenType() == TokenType.ELIF && !pre_condition)
					pre_condition = condition;
					
				
			}
			
			// If the token is a naked block (naked block: not an argument for a block statement)
			else if (token.getTokenType() == TokenType.BLOCK) {
				
				if (v) System.out.format("Found a(n) %s token.\n", token); 
				
				new Parser((TokenBlock) token.getData()).parse(v);
				
			}
			
			// ... else just print the found token
			else if (v) System.out.format("Found a(n) %s token.\n", token);
			
			
			// Set the token as the previous token
			pre_token = token;
		}
	}
	
	/** Evaluates all expressions and conditions in the given list */
	private ArrayList<TokenData> evaluateArguments(ArrayList<TokenData> args) {
		
		// Evaluate expressions and conditions inside the arguments
		for (int j = 0; j < args.size(); j ++) {
			TokenData arg = args.get(j);
			if (arg.getTokenType() == TokenType.EXPRESSION || arg.getTokenType() == TokenType.CONDITION) {
				ScriptEngineManager factory = new ScriptEngineManager();
			    ScriptEngine engine = factory.getEngineByName("JavaScript");
			    
			    // Parse variables to the engine
			    HashMap<String, TokenData> map = Lash.VARIABLE_REGISTRY.getVariableMap();
			    for (String key : map.keySet())
			    	engine.put(key, map.get(key).getData().toString());
			    
			    
			    String result = null,
			    	   infix = arg.getData().toString().substring(1, arg.getData().toString().length() - 1).replace("$", ""),
			    	   infix_to_evaluate = new String();
			    
			    // Remove braces around expressions
			    for (int k = 0; k < infix.toCharArray().length; k ++) {
			    	char c = infix.toCharArray()[k], nc;
			    	if (k != infix.toCharArray().length - 1)
			    		nc = infix.toCharArray()[k + 1];
			    	else
			    		nc = ' ';
			    	
			    	// If the braces are not apart of a less or grater operator
			    	if (!(c == '<' && nc != '<') && !(c == '>' && nc != '>'))
			    		infix_to_evaluate += c; // ... append the character
			    }
			    
			    try {
			    	result = engine.eval(infix_to_evaluate).toString();
				} catch (ScriptException e) {
					Lash.err(e, 404);
				}
			    
			    if (arg.getTokenType() == TokenType.EXPRESSION) {
			    	if (result.endsWith(".0"))
			    		result = result.substring(0, result.length() - 2);
			    	args.set(j, new TokenData(result, TokenType.NUM));
			    } else if (result.trim().equalsIgnoreCase("true"))
			    	args.set(j, new TokenData(true, TokenType.BOOL));
			    else
			    	args.set(j, new TokenData(false, TokenType.BOOL));
			}
		}
		return args;
	}
	
	/** Evaluates a token */
	public static TokenData evaluateToken(TokenData token) {
		
		if (token.getTokenType() == TokenType.EXPRESSION || token.getTokenType() == TokenType.CONDITION) {
			ScriptEngineManager factory = new ScriptEngineManager();
		    ScriptEngine engine = factory.getEngineByName("JavaScript");
		    
		    // Parse variables to the engine
		    HashMap<String, TokenData> map = Lash.VARIABLE_REGISTRY.getVariableMap();
		    for (String key : map.keySet())
		    	engine.put(key, map.get(key).getData().toString());
		    
		    
		    String result = null,
		    	   infix = token.getData().toString().substring(1, token.getData().toString().length() - 1).replace("$", ""),
		    	   infix_to_evaluate = new String();
		    
		    // Remove braces around expressions
		    for (int k = 0; k < infix.toCharArray().length; k ++) {
		    	char c = infix.toCharArray()[k], nc;
		    	if (k != infix.toCharArray().length - 1)
		    		nc = infix.toCharArray()[k + 1];
		    	else
		    		nc = ' ';
		    	
		    	// If the braces are not apart of a less or grater operator
		    	if (!(c == '<' && nc != '<') && !(c == '>' && nc != '>'))
		    		infix_to_evaluate += c; // ... append the character
		    }
		    
		    try {
		    	result = engine.eval(infix_to_evaluate).toString();
			} catch (ScriptException e) {
				Lash.err(e, 404);
			}
		    
		    if (token.getTokenType() == TokenType.EXPRESSION) {
		    	if (result.endsWith(".0"))
		    		result = result.substring(0, result.length() - 2);
		    	return new TokenData(result, TokenType.NUM);
		    } else if (result.trim().equalsIgnoreCase("true"))
		    	return new TokenData(true, TokenType.BOOL);
		    else
		    	return new TokenData(false, TokenType.BOOL);
		} else return new TokenData(null, TokenType.UNKNOWN);
		
	}
	
}
