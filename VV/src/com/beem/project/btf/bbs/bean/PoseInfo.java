package com.beem.project.btf.bbs.bean;

public class PoseInfo {
	private String id;
	private String poss;
	private String praCoord;
	private String seedCoord;

	public PoseInfo(String id, String poss, String praCoord, String seedCoord) {
		super();
		this.id = id;
		this.poss = poss;
		this.praCoord = praCoord;
		this.seedCoord = seedCoord;
	}
	@Override
	public String toString() {
		return "PoseInfo [id=" + id + ", index=" + poss + ", praCoord="
				+ praCoord + ", seedCoord=" + seedCoord + "]";
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPraCoord() {
		return praCoord;
	}
	public void setPraCoord(String praCoord) {
		this.praCoord = praCoord;
	}
	public String getSeedCoord() {
		return seedCoord;
	}
	public void setSeedCoord(String seedCoord) {
		this.seedCoord = seedCoord;
	}
	public String getPoss() {
		return poss;
	}
	public void setPoss(String poss) {
		this.poss = poss;
	}
}
