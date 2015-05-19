package com.dianping.cricket.dal.sql.syntax;

import java.util.LinkedList;
import java.util.Stack;

import com.dianping.cricket.api.exception.InvalidCaseException;
public class SyntaxTree {
	private String expression;
	private Stack<SyntaxNode> stack = new Stack<SyntaxNode>();
	private LinkedList<LexicalToken> tokens = new LinkedList<LexicalToken>();
	private SyntaxNode root;
	private SyntaxNode currentParent;
	private OperatorNode currentOperator;
	private FunctionNode currentFunction;
	private FieldNode currentField;
	private SymbolNode currentSymbol;
	private StringBuilder builder = new StringBuilder();
	private SyntaxTree(String expression) {
		this.expression = expression;
	}
	
	private void parse(String expression, SyntaxNode parent) throws InvalidCaseException {
		LexicalToken token = null;
		SyntaxNode node = null;
		while((token = tokens.poll()) != null) {
			switch(token.getType()) {
			case COMMON:
				// Field should never be pushed into stack.
				currentField = new FieldNode(token.getToken());
				break;
			case COMMA:
				if (currentSymbol == null) {
					currentSymbol = new SymbolNode(token.getToken());
					currentSymbol.setLeft(currentField);
				}
				if (currentField != null) {
					currentSymbol = (SymbolNode)currentSymbol.adjustAdd(currentField);
				}
				break;
			case ALGORITHM_OPERATOR:
				if (currentOperator != null) {
					stack.push(currentOperator); 
					currentOperator = new OperatorNode(token.getToken());
				}
				break;
			case BRACE_END: 
				// Test the function must exist.
				if (currentFunction != null) {
					if (currentSymbol != null) {
						currentFunction.setLeft(currentSymbol);
						currentSymbol = null;
					} else {
						currentFunction.setLeft(currentField);
						currentField = null;
					}
				} 
				
				for (int index = stack.size() - 1; index >= 0; index--) {
					if (stack.get(index) instanceof FunctionNode) {
						currentFunction = (FunctionNode)stack.get(index);
						break;
					}
				}

				break;
			case BRACE_START:
				// if the current function node is not null, add into stack.
				if (currentFunction != null) {
					stack.add(currentFunction);
				}
				
				// create function node as current node.
				currentFunction = new FunctionNode(token.getToken());
				break;
			}
			
		}
//		
//		
//		
//		
//		
//		
//			switch (expression) {
//			// pattern for field.
//			case "[^()+-\\\\*/%,]+": 
//				parent.adjustAdd(new FieldNode(expression));
//				break;
//			// pattern match function
//			case "[a-zA-Z0-9_]+\\(\\S+\\)":
//				// If no symbol node found, then create symbol node.
//				if (!(currentNode instanceof SymbolNode)) {
//					currentNode = new SymbolNode(c);
//				}
//				
//				fieldNode = new FieldNode(builder.toString());
//				if (currentNode.getLeft() == null) {
//					currentNode.setLeft(fieldNode);
//				} else if (currentNode.getRight() == null) {
//					currentNode.setRight(fieldNode);
//				} else {
//					currentNode = currentNode.adjustAdd(fieldNode);
//				}
//				break;
//			// pattern match function params list.
//			case "([a-zA-Z0-9_]+,?)*": 
//				currentNode = new OperatorNode(c);
//				if (builder.length() > 0) {
//					currentNode.setLeft(new FieldNode(builder.toString()));
//				} 
//				if (currentParent == null) {
//					currentParent = currentNode;
//				} else {
//					currentParent.setRight(currentNode);
//				}
//				break;
//			case 2:
//				// Must a field node before right brace.
//				if (builder.length() > 0) {
//					fieldNode = new FieldNode(builder.toString());
//					if (currentParent.getLeft() == null) {
//						currentParent.setLeft(fieldNode);
//					} else if (currentParent.getRight() == null) {
//						currentParent.setRight(fieldNode);
//					} else {
//						currentParent = currentParent.adjustAdd(fieldNode);
//					}
//					builder.setLength(0);
//					break;
//				}
//				throw new InvalidCaseException("Invalid Expression, there must be one field before right brace!");
//			case 3: 
//				// Create operator node for braces.
//				currentNode = new OperatorNode(c);
//				
//				// If has text defined before, then it's a function.
//				if (builder.length() > 0) {
//					FunctionNode functionNode = new FunctionNode(builder.toString());
//					if (currentParent != null) {
//						functionNode.setLeft(currentParent);
//					}
//					
//					currentParent = functionNode;
//					root = currentParent;
//					builder.setLength(0);
//				}
//				break;
//			}
	}
	
	private LexicalTokenType isBoundary(char c) {
		if (c == '(') {
			return LexicalTokenType.BRACE_START;
		}
		
		if (c == ')') {
			return LexicalTokenType.BRACE_END;
		}
		
		if ("+-*/%".indexOf(c) > -1) {
			return LexicalTokenType.ALGORITHM_OPERATOR;
		}
		
		if (c == ',') {
			return LexicalTokenType.COMMA;
		}
		
		return LexicalTokenType.COMMON;
	}
	
	public void lexicalParse() {
		int start = -1;
		int length = 0;
		String ex = expression.replaceAll(" ", "");
		for (int index = 0; index < ex.length(); index++) {
			LexicalTokenType type = isBoundary(ex.charAt(index));
			if (type != LexicalTokenType.COMMON) {
				if (start != -1) {
					tokens.add(new LexicalToken(ex.substring(start, start + length), type));
					start = -1;
					length = 0;
				}
				tokens.add(new LexicalToken(String.valueOf(ex.charAt(index)), type));
			} else {
				if (start == -1) {
					start = index;
				}
				length++;
			}
		}
		if (start != -1) {
			tokens.add(new LexicalToken(ex.substring(start, start + length), LexicalTokenType.COMMON));
		}
	}
	
	
	public String toString() {
		SyntaxNode node = root;
		while(node != null) {
			if (node.getLeft() != null) {
				node = node.getLeft();
				continue;
			}
			break;
		}
		
		return null;
	}
	
	public static SyntaxTree parse(String expression) {
		SyntaxTree tree = new SyntaxTree(expression);
		try {
			tree.lexicalParse();
			tree.parse(null, null);
		} catch (InvalidCaseException e) {
			e.printStackTrace();
			return null;
		}
		return tree;
	}
	
	private static class LexicalToken {
		private String token;
		private LexicalTokenType type;
		
		public LexicalToken(String token, LexicalTokenType type) {
			this.token = token;
			this.type = type;
		}
		
		public String getToken() {
			return token;
		}
		
		public LexicalTokenType getType() {
			return type;
		}
		
	}
	
	private static enum LexicalTokenType {
		BRACE_START, BRACE_END, ALGORITHM_OPERATOR, COMMA, COMMON
	}

}
