package com.oliverneven.lash.main.variable;

import java.util.HashMap;

import com.oliverneven.lash.main.Lash;
import com.oliverneven.lash.main.token.TokenData;


public class VariableRegistry {
	
	
	private final HashMap<String, TokenData> variable_map;
	
	
	public VariableRegistry() {
		variable_map = new HashMap<>();
	}
	public VariableRegistry(final HashMap<String, TokenData> variable_map) {
		this.variable_map = variable_map;
	}
	
	
	/** Adds a variable to the map */
	public void assignVariable(final String variable_name, final TokenData token_data) {
		if (hasVariable(variable_name))
			variable_map.remove(variable_name);
		
		variable_map.put(variable_name, token_data);
	}
	
	/** Returns a variable's containing data */
	public TokenData getVariable(final String varialbe_name) {
		if (!hasVariable(varialbe_name)) {
			Lash.err("$" + varialbe_name + " is not a declared variable");
			return null;
		} else {
			return variable_map.get(varialbe_name);
		} 
	}
	
	/** Returns the entire variable map */
	public HashMap<String, TokenData> getVariableMap() {
		return variable_map;
	}
	
	/** Removes a variable and it's data<br>WARNING: This can't be undone! */
	public void removeVariable(final String variable_name) {
		variable_map.remove(variable_name);
	}
	
	/** Clears the entire variable map<br>WARNING: This can't be undone! */
	public void clearVariableMap() {
		variable_map.clear();
	}
	
	/** Returns true, if and only if the map contains the given variable */
	public boolean hasVariable(final String variable_name) {
		return variable_map.containsKey(variable_name);
	}
	
}
