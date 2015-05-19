package com.dianping.cricket.dal.sql.syntax;

public class OperatorNode extends SyntaxNode {

	public OperatorNode(char c) {
		super(String.valueOf(c));
	}
	public OperatorNode(String operator) {
		super(operator);
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

	@Override
	public SyntaxNode adjustAdd(SyntaxNode node) {
		// TODO Auto-generated method stub
		return null;
	}

}
