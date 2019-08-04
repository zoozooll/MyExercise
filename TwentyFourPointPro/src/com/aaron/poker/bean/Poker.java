package com.aaron.poker.bean;

public class Poker extends Entry {

	/**点数*/
	private int point;
	
	/**花型*/
	private short type;
	

	public Poker() {
		super();
	}

	public Poker(int point, short type, String image) {
		super();
		this.point = point;
		this.type = type;
		this.image = image;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public short getType() {
		return type;
	}

	public void setType(short type) {
		this.type = type;
	}
	
}
