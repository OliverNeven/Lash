package com.oliverneven.lash.main.token;

import java.util.ArrayList;

import com.oliverneven.lash.main.Lash;
import com.oliverneven.lash.main.parser.Parser;

public enum TokenType {
	
	// Miscellaneous
	
	UNKOWN(),
	ENDOFLINE("<LN>", false),
	TERMINATOR(";", false),
	COMMENT("(¤.*¤)|(#.*" + ENDOFLINE.getRegex() + ")", true),
	
	// Commands
	
	ECHO("ECHO", false, new TokenAction() {
		public boolean exec(ArrayList<TokenData> args) {
			for (TokenData arg : args)
				if (arg.isCommand()) {
					Lash.out("");
					Lash.err("The argument " + arg.getTokenType() + " has no data to be printed");
					return false;
				} else if (arg.getTokenType() == VARIABLE) {
					Lash.out(Lash.VARIABLE_REGISTRY.getVariable(arg.getData().toString()).getData().toString() + "");
				} else {
					Lash.out(arg.getData().toString() + "");
				}
			Lash.outln("");
			return true;
		}
	}),
	INPUT("INPUT", false, new TokenAction(){
		public boolean exec(ArrayList<TokenData> args) {
			
			TokenData input_variable = args.remove(args.size() - 1);
			ECHO.getAction().exec(args);
			Lash.VARIABLE_REGISTRY.assignVariable(input_variable.getData().toString(), new TokenData(Lash.RAW_INPUT.nextLine(), STRING));
			
			return true;
		}
	}),
	
	// Block statements
	
	IF("IF", false, new TokenAction(){
		public boolean exec(ArrayList<TokenData> args) {
			
			ArrayList<TokenData> block_args;
			Parser block_parser;
			for (int i = 0; i < args.size(); i ++) {
				TokenData arg = args.get(i);
				
				// If a block is found, parse it
				if (arg.getTokenType() == TokenType.OPEN_BLOCK) {
					block_args = new ArrayList<>();
					
					Lash.outln("{");
					
					for (i = i + 1; i < args.size(); i ++) {
						TokenData block_arg = args.get(i);
						
						if (block_arg.getTokenType() == TokenType.CLOSE_BLOCK)
							break;
						
						block_args.add(block_arg);
					}
					
					block_parser = new Parser(block_args);
					block_parser.parse();
				
					Lash.outln("}");
				}
				
			}
			
			return true;	
		}
	}),
	
	// Block
	
	BLOCK("\\{(.*)\\}", true),
	OPEN_BLOCK("<OBK>", false),
	CLOSE_BLOCK("<CBK>", false),
	
	// Condition
	
	CONDITION_EQ("==", false, new TokenAction() {
		public boolean exec(ArrayList<TokenData> args) {
			return true;
		}
	}),
	
	// Variable
	
	VARIABLE("\\$[a-zA-Z][a-zA-Z0-9]*", true),
	
	// Assignment
	
	ASSINGN_EQ("=", true, new TokenAction() {
		public boolean exec(ArrayList<TokenData> args) {
			
			if (args.size() > 2) {
				String data = "";
				for (int i = 1; i < args.size(); i ++) {
					TokenData arg = args.get(i);
					
					if (arg.getTokenType() == VARIABLE)
						data += Lash.VARIABLE_REGISTRY.getVariable(arg.getData().toString()).getData().toString();
					else
						data += arg.getData().toString();
				}
				Lash.VARIABLE_REGISTRY.assignVariable(args.get(0).getData().toString(), new TokenData(data, STRING));
			} else if (args.get(1).getTokenType() == VARIABLE) {
				Lash.VARIABLE_REGISTRY.assignVariable(args.get(0).getData().toString(), Lash.VARIABLE_REGISTRY.getVariable(args.get(1).getData().toString()));
			} else
				Lash.VARIABLE_REGISTRY.assignVariable(args.get(0).getData().toString(), args.get(1));
			
			return true;
		}
	}),
	
	// Data types
	
	EXPRESSION("\\(|\\)|\\d+\\.?\\d*|[+-/%^*]", true), // Same for integers and floating points
	STRING("\".*\"", true);
	
	
	
	
	
	
	
	private final String regex;
	private final boolean isRegex, isUnkown, isCommand, isOperationCommand;
	private final TokenAction action;
	TokenType() {
		this.regex = new String();
		this.isRegex = false;
		this.isUnkown = true;
		this.isCommand = false;
		this.isOperationCommand = false;
		this.action = null;
	}
	TokenType(final String regex, final boolean isRegex) {
		this.regex = regex;
		this.isRegex = isRegex;
		this.isUnkown = false;
		this.isCommand = false;
		this.isOperationCommand = false;
		this.action = null;
	}
	TokenType(final String regex, final boolean isOperationCommand, final TokenAction action) {
		this.regex = regex;
		this.isRegex = false;
		this.isUnkown = false;
		this.isCommand = true;
		this.isOperationCommand = isOperationCommand;
		this.action = action;
	}
	
	public static TokenType checkMatch(String token) {
		token = token.trim();
		for (TokenType token_type : values()) {
			if (!token_type.isUnkown && ((token_type.isRegex && token.matches(token_type.getRegex())) || (!token_type.isRegex && token.equalsIgnoreCase(token_type.getRegex()))))
				return token_type;
		}
		return UNKOWN;
	}
	
	public boolean isCommand() {
		return isCommand;
	} 
	
	public boolean isOperationCommand() {
		return isOperationCommand;
	}
	public TokenAction getAction() {
		return action;
	}
	public String getRegex() {
		return regex;
	}
	
	
}
