package com.aaron.poker.bean;

public class Arithmetic extends Entry {

	public static final short ARITH_ADD = 1;
	public static final short ARITH_SUBTRACT = 2;
	public static final short ARITH_MULTIPLY = 3;
	public static final short ARITH_DIVIDE = 4;
	
	private short operation;

	public short getOperation() {
		return operation;
	}

	public void setOperation(short operation) {
		this.operation = operation;
	}

}
