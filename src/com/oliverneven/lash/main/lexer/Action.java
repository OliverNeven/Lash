package com.oliverneven.lash.main.lexer;

import com.oliverneven.lash.main.Lash;


public abstract class Action {
	
	private final String keyword;
	
	
	public Action(final String keyword) {
		this.keyword = keyword;
	}
	
	
	/** Executes the action */
	public abstract void exec(Object data);
	
	
	
	
	
	public static final Action ECHO_ACTION = new Action("echo") {
		@Override
		public void exec(Object data) {
			Lash.out((String) data);
		}
	};
	
	
	
	
	
	public static boolean eq(final String token, final String keyword) {
		return token.equalsIgnoreCase(keyword);
	}
	
	
}
