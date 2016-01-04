package com.oliverneven.lash.main.token;

import java.util.ArrayList;


public enum TokenType {
	
	UNKOWN(),
	ENDOFLINE("<EOF>", false),
	TERMINATOR(";", false),
	COMMENT("(¤.*¤)|(#.*" + ENDOFLINE.getRegex() + ")", true),
	
	ECHO("ECHO", new TokenAction() {
		public boolean exec(ArrayList<TokenData> args) {
			for (TokenData arg : args)
				System.out.print(arg.getData().toString() + "");
			System.out.println();
			return true;
		}
	}),
	
	EXPRESSION("[0-9]+",true),
	STRING("\".*\"", true);
	
	
	
	
	
	
	
	
	private final String regex;
	private final boolean isRegex;
	private final boolean isUnkown;
	private final boolean isCommand;
	private final TokenAction action;
	TokenType() {
		this.regex = new String();
		this.isRegex = false;
		this.isUnkown = true;
		this.isCommand = false;
		this.action = null;
	}
	TokenType(final String regex, final boolean isRegex) {
		this.regex = regex;
		this.isRegex = isRegex;
		this.isUnkown = false;
		this.isCommand = false;
		this.action = null;
	}
	TokenType(final String regex, final TokenAction action) {
		this.regex = regex;
		this.isRegex = false;
		this.isUnkown = false;
		this.isCommand = true;
		this.action = action;
	}
	
	public static TokenType checkMatch(String token) {
		token = token.trim();
		for (TokenType token_type : values()) {
			if (!token_type.isUnkown && ((token_type.isRegex && token.matches(token_type.regex)) || (!token_type.isRegex && token.equalsIgnoreCase(token_type.regex))))
				return token_type;
		}
		return UNKOWN;
	}
	
	public boolean isCommand() {
		return isCommand;
	} 
	public TokenAction getAction() {
		return action;
	}
	public String getRegex() {
		return regex;
	}
	
	
}
