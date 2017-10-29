package com.iskinfor.servicedata.pojo;
/**
 * 书签
 * @author Administrator
 *
 */
public class BookMark {
	private int makePage;//书签页码
	private String markContent;//书签内容
	private String makeDate;//书签日期
	private int pgs;//总条数
	public int getMakePage() {
		return makePage;
	}
	public void setMakePage(int makePage) {
		this.makePage = makePage;
	}
	public String getMarkContent() {
		return markContent;
	}
	public void setMarkContent(String markContent) {
		this.markContent = markContent;
	}
	public String getMakeDate() {
		return makeDate;
	}
	public void setMakeDate(String makeDate) {
		this.makeDate = makeDate;
	}
	public int getPgs() {
		return pgs;
	}
	public void setPgs(int pgs) {
		this.pgs = pgs;
	}
	

}
