package com.oliverneven.lash.main.token;

import java.util.ArrayList;

import com.oliverneven.lash.main.Lash;

public enum TokenType {
	
	UNKOWN(),
	ENDOFLINE("<LN>", false),
	TERMINATOR(";", false),
	COMMENT("(¤.*¤)|(#.*" + ENDOFLINE.getRegex() + ")", true),
	
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
	
	VARIABLE("\\$[a-zA-Z][a-zA-Z0-9]*", true),
	VARIABLE_ASSINGN_EQ("=", true, new TokenAction() {
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
			} else 
				Lash.VARIABLE_REGISTRY.assignVariable((String) args.get(0).getData(), args.get(1));
			
			return true;
		}
	}),
	
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
