package com.oliverneven.lash.main.token;


public class TokenData {
	
	private final Object data;
	private final TokenType token_type;
	private final boolean isCommand, isTag;
	
	public TokenData(final TokenType token_type, final boolean isTag) {
		this.data = null;
		this.token_type = token_type;
		this.isCommand = !isTag;
		this.isTag = isTag;
	}
	public TokenData(final Object data, final TokenType token_type) {
		this.data = data;
		this.token_type = token_type;
		this.isCommand = false;
		this.isTag = false;
	}
	
	
	public String toString() {
		if (token_type.isCommand())
			return new String("<Command>|" + token_type);
		else if (isTag)
			return new String("<Tag>|" + token_type);
		else if (data != null)
			return new String("<" + data.toString() + ">|" + token_type);
		else
			return new String().format("<>|%s\n", token_type.toString());
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
	public boolean isTag() {
		return isTag;
	}
	public boolean isOperationCommand() {
		return token_type.isOperationCommand();
	}
	public boolean isBlock() {
		return token_type == TokenType.BLOCK;
	}
	
}
