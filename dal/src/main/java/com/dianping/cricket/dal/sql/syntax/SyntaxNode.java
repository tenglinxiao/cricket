package com.dianping.cricket.dal.sql.syntax;

import com.dianping.cricket.api.exception.InvalidCaseException;

public abstract class SyntaxNode {
	private String name;
	private SyntaxNode parent;
	private SyntaxNode left;
	private SyntaxNode right;
	
	public SyntaxNode(String name) {
		this.name = name;
	}
	
	public SyntaxNode(String name, SyntaxNode parent) {
		this(name);
		this.parent = parent;
	}
	
	public SyntaxNode(String name, SyntaxNode parent, SyntaxNode left) {
		this(name, parent);
		this.left = left;
	}
	
	public SyntaxNode(String name,  SyntaxNode parent, SyntaxNode left, SyntaxNode right) {
		this(name, parent, left);
		this.right = right;
	}
	
	public String getName() {
		return name;
	}
	
	public SyntaxNode getParent() {
		return parent;
	}
	
	
	public void setParent(SyntaxNode parent) {
		this.parent = parent;
	}
	
	public void setLeft(SyntaxNode left) {
		this.left = left;
		this.left.setParent(this);
	}
	
	public void setRight(SyntaxNode right) {
		this.right = right;
		this.right.setParent(this);
	}
	
	public SyntaxNode getLeft() {
		return left;
	}
	
	public SyntaxNode getRight() {
		return right;
	}
	
	public SyntaxNode adjustAdd(SyntaxNode node) throws InvalidCaseException {
		if (this.left == null) {
			this.left = node;
		} else if (this.right == null) {
			this.right = node;
		} else {
			throw new InvalidCaseException("CAN NOT add new node to a syntax tree that both left & right nodes are offered!");
		}
		return this;
	}
	
	public abstract FieldNode findFieldNodes();
	
	public abstract FunctionNode findFunctionNodes();
}
