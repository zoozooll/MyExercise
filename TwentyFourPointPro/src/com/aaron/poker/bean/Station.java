package com.aaron.poker.bean;

public class Station {

	/**λ�ñ��*/
	private int sId;
	/**��λ��*/
	private int left;
	/**��λ��*/
	private int top;
	/**��λ��*/
	private int right;
	/**��λ��*/
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
