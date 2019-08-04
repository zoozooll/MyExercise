package com.beem.project.btf.bbs.bean;

public class DraftInfo {
	private String id;
	private String type;
	private String content;

	public DraftInfo(String id, String type, String content) {
		super();
		this.id = id;
		this.type = type;
		this.content = content;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@Override
	public String toString() {
		return "DraftInfo [id=" + id + ", type=" + type + ", content="
				+ content + "]";
	}
}
