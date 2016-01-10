package com.oliverneven.lash.main.token;

import java.util.ArrayList;

import com.oliverneven.lash.main.Lash;
import com.oliverneven.lash.main.parser.Parser;

public enum TokenType {
	
	// Miscellaneous
	
	UNKNOWN(),
	ENDOFLINE("<LN>", false),
	TERMINATOR(";", false),
	COMMENT("((##).*(##))|(#.*" + ENDOFLINE.getRegex() + ")", true),
	
	// Commands
	
	ECHO("ECHO", false, new TokenAction() {
		public boolean exec(ArrayList<TokenData> args) {
			for (TokenData arg : args)
				if (arg.isCommand()) {
					Lash.out("");
					Lash.err("The argument " + arg.getTokenType() + " has no data to be printed");
					return false;
				} else if (arg.getTokenType() == VARIABLE)
					Lash.out(Lash.VARIABLE_REGISTRY.getVariable(arg.getData().toString()).getData().toString() + "");
				else
					Lash.out(arg.getData().toString() + "");
				
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
			
			// System.out.println(args.get(0).getData().toString());
			
			if (args.get(0).getData().toString().equals(new String("true")))
				new Parser((TokenBlock) args.get(1).getData()).parse(false);
			
			return true;
		}
	}),
	
	// Block
	
	BLOCK("\\{(.*)\\}", true),
	BLOCK_OPEN("{", false),
	BLOCK_CLOSE("}", false),
	
	// Variable
	
	VARIABLE("\\$[a-zA-Z_][a-zA-Z0-9_]*", true),
	
	// Assignment
	
	OP_EQUALS("=", true, new TokenAction() {
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
	
	EXPRESSION("<(\\d*\\.?(?:[0-9]|\\+|-|\\*|\\/|%|\\^|\\(|\\)|\\$[a-zA-Z_][a-zA-Z0-9_]*)\\s*)*>", true),
	CONDITION("\\[(?:true|false|\\s+|\\$[a-zA-Z_][a-zA-Z0-9_]*|(?:=|\\||&|<|>){2}|<((?:\\d*|\\+|-|\\*|\\/|%|\\^|\\(|\\)|\\$[a-zA-Z_][a-zA-Z0-9_]*)\\s*)*>|\".*\")*\\]", true),
	NUM("<NUM>", false),
	BOOL("<BOOL>", false),
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
		return UNKNOWN;
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
