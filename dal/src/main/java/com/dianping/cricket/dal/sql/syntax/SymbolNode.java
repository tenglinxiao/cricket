package com.dianping.cricket.dal.sql.syntax;

import com.dianping.cricket.api.exception.InvalidCaseException;

public class SymbolNode extends SyntaxNode {

	public SymbolNode(char symbol) {
		super(String.valueOf(symbol));
	}
	
	public SymbolNode(String name) {
		super(name);
	}
	
	public SyntaxNode adjustAdd(SyntaxNode node) {
		try {
			return super.adjustAdd(node);
		} catch (InvalidCaseException e) {
			SymbolNode parent = new SymbolNode(getName());
			parent.setLeft(this);
			return parent;
		}
	}

	@Override
	public FieldNode findFieldNodes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FunctionNode findFunctionNodes() {
		// TODO Auto-generated method stub
		return null;
	}

}
