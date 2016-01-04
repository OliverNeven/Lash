package com.oliverneven.lash.main.arithmetic;

import java.util.Stack;


public class ArithmeticEvaluation {
	
	private Stack<Operators> operators;
	private Stack<String> postfix;
	private Stack<Character> infix;
	private int openParentheseAmount;
	
	/** Postfix evaluation **/
	public double evalInfix(String infix) {
		if (!(infix.startsWith("(") && infix.endsWith("")))
			infix = "(" + infix + ")";
		
		Stack<String> postfix = infixToPostfix(infix.trim());
		double result = evalPostfix(postfix); 
		
		return result;
	}
	
	public double evalPostfix(final Stack<String> postfix) {
		
		final Stack<Double> operands = new Stack<>();
		
		// Loop through each element of the postfix
		for (String fix : postfix) {
			
			if (isNumeric(fix)) {
				operands.push(Double.valueOf(fix));
				continue;
			}
			
			final Operators op = Operators.isOperator(fix.charAt(0));
			if (op != null) {
				
				double operand2 = operands.pop();
				double operand1 = operands.pop();
				double result = operate(op, operand1, operand2);
				operands.push(result);
				
			}
			
		}
		
		return operands.peek();
	}
	private double operate(Operators op, double operand1, double operand2) {
		switch (op) {
			case ADD:
				return operand1 + operand2;
			case SUBTRACT:
				return operand1 - operand2;
			case MULTIPLY:
				return operand1 * operand2;
			case DIVIDE:
				return operand1 / operand2;
			case MODULO:
				return operand1 % operand2;
			case POWER:
				return Math.pow(operand1, operand2);
			case CLOSE_PARENTHESE: break;
			case OPEN_PARENTHESE: break;
			default: break;}
		return 0;
	}

	
	public Stack<String> infixToPostfix(final String infixExp) {
		
		// Initialize stacks
		operators = new Stack<>();
		postfix = new Stack<>();
		infix = charArryToStack(infixExp.toCharArray());
		openParentheseAmount = 0;
		
		// Loop through the infix stack and generate the postfix stack
		String currentOpand = "";
		for (char currentSymbol : infix) {
			
			if (Character.isDigit(currentSymbol) || Character.isAlphabetic(currentSymbol)) {
				currentOpand += currentSymbol;
				continue;
			}
			
			Operators op = Operators.isOperator(currentSymbol);
			if (op != null) {
				
				// Add the current opand to the postfix
				if (!currentOpand.equals("")) {
					postfix.add(currentOpand);
					currentOpand = "";
				}
				
				handleOperator(op);	
			}
		}
		return postfix;
	}	
	private void handleOperator(Operators op) {
		
		switch (op) {
			case ADD: case SUBTRACT: case MULTIPLY: case DIVIDE: case MODULO: case POWER:
				if (!operators.isEmpty()) {
					if (operators.peek().priority == op.priority)
						postfix.push(Character.toString(operators.pop().symbol));
					else if (operators.peek().priority > op.priority) {
						postfix.push(String.valueOf(operators.pop().symbol));
						if (operators.peek().priority == op.priority)
							postfix.push(Character.toString(operators.pop().symbol));
					}
				}
				operators.push(op);
				break;
			case OPEN_PARENTHESE:
				openParentheseAmount ++;
				operators.push(op);
				break;
			case CLOSE_PARENTHESE:
				if (openParentheseAmount >= 1)
					openParentheseAmount --;
				
				// Get operators between each perenthese
				Stack<Operators> parOperators = new Stack<>();
				while (!operators.peek().equals(Operators.OPEN_PARENTHESE)) {
					parOperators.push(Operators.isOperator(operators.pop().symbol));
				}
				operators.pop();
				
				
				// Handle each of those operators
				for (int i = parOperators.size() - 1; i >= 0; i --)
					postfix.push(String.valueOf(parOperators.get(i).symbol));
				
				break;
			default: break;
		}
	}
	
	
 	private static Stack<Character> charArryToStack(final char[] charArray) {
		final Stack<Character> stack = new Stack<>();
		for (char c : charArray)
			stack.add(c);
		return stack;
	}
 	public boolean isNumeric(String s) {
 		return s.matches("[-+]?\\d*\\.?\\d+");
 	}
 	
	
	// Each of the operators supported in this evaluation program and it's priority.
	private enum Operators {
		ADD('+', 0),		// The addition  operator, which has the lowest priority (0)
		SUBTRACT('-', 0),	// The subtraction operator, which has the lowest priority (0)
		MULTIPLY('*', 1),	// The multiplication operator, which has the second lowest priority (1)
		DIVIDE('/', 1),		// The division operator, which has the second lowest priority (1)
		MODULO('%', 1),		// The modulation operator, which has the second lowest priority (1)	
		POWER('^', 2),		// The power operator, which has the highest priority (2)
		OPEN_PARENTHESE('(', -1),	// The open parenthese, which has the special priority
		CLOSE_PARENTHESE(')', -1);	// The close parenthese, which has the special priority
		
		public final int priority;
		public final char symbol;
		private Operators(char symbol, int priority) {
			this.priority = priority;
			this.symbol = symbol;
		}
		// Returns the operator with a matching symbol, or null if no matches could be found
		public static Operators isOperator(char symbol) {
			for (Operators op : values())
				if (op.symbol == symbol)
					return op;
			return null;
		}
		@Override
		public String toString() {
			return String.valueOf(symbol);
		}
		
		
	}
}
