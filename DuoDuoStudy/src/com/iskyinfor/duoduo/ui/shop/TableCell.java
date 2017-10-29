package com.iskyinfor.duoduo.ui.shop;

public class TableCell {
	
	private int width;
	private int type;
	private Object content;
	
	public TableCell() {
		
	}
	
	public TableCell(int width, int type) {
		this.width = width;
		this.type = type;
		
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Object getContent() {
		return content;
	}
	public void setContent(Object content) {
		this.content = content;
	}
	
}
