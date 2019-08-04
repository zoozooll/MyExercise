package com.aaron.poker.bean;

public class Station {

	/**Œª÷√±‡∫≈*/
	private int sId;
	/**◊ÛŒª÷√*/
	private int left;
	/**∂•Œª÷√*/
	private int top;
	/**”“Œª÷√*/
	private int right;
	/**µ◊Œª÷√*/
	private int bottom;
	
	public Station() {
		super();
	}
	public Station(int sId, int left, int top, int right, int bottom) {
		super();
		this.sId = sId;
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
	}
	public int getsId() {
		return sId;
	}
	public void setsId(int sId) {
		this.sId = sId;
	}
	public int getLeft() {
		return left;
	}
	public void setLeft(int left) {
		this.left = left;
	}
	public int getTop() {
		return top;
	}
	public void setTop(int top) {
		this.top = top;
	}
	public int getRight() {
		return right;
	}
	public void setRight(int right) {
		this.right = right;
	}
	public int getBottom() {
		return bottom;
	}
	public void setBottom(int bottom) {
		this.bottom = bottom;
	}
}
