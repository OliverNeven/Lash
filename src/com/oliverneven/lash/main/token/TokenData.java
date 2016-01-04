package com.oliverneven.lash.main.token;


public class TokenData {
	
	private final Object data;
	private final TokenType token_type;
	private final boolean isCommand;
	
	
	public TokenData(final TokenType token_type) {
		this.data = null;
		this.token_type = token_type;
		this.isCommand = true;
	}
	public TokenData(final Object data, final TokenType token_type) {
		this.data = data;
		this.token_type = token_type;
		this.isCommand = false;
	}
	
	@Override
	public String toString() {
		if (token_type.isCommand())
			return new String("<Command>|" + token_type);
		else
			return new String("<" + data.toString() + ">|" + token_type);
	}
	
	public Object getData() {
		return data;
	}
	public TokenType getTokenType() {
		return token_type;
	}
	public boolean isCommand() {
		return isCommand;
	}
	
}
