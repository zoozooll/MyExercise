package com.iskinfor.servicedata.pojo;
/**
 * 读书笔记
 * @author Administrator
 *
 */
public class ReadingNotes {
	private String proId; //商品ID
	private String noteData;//做笔记的日期
	private int notePage;//那一夜做笔记
	private String noteContent;//做笔记的内容
	private int pgs;//总笔记条数
	private Product product;//做笔记的产品
	public String getProId() {
		return proId;
	}
	public void setProId(String proId) {
		this.proId = proId;
	}
	public String getNoteData() {
		return noteData;
	}
	public void setNoteData(String noteData) {
		this.noteData = noteData;
	}
	public int getNotePage() {
		return notePage;
	}
	public void setNotePage(int notePage) {
		this.notePage = notePage;
	}
	public String getNoteContent() {
		return noteContent;
	}
	public void setNoteContent(String noteContent) {
		this.noteContent = noteContent;
	}
	public int getPgs() {
		return pgs;
	}
	public void setPgs(int pgs) {
		this.pgs = pgs;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
}
