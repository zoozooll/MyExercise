package com.oregonscientific.meep.message.common;

public class MsmGetRecommendation extends MeepServerMessage {
    private String type = null;
    private String thumbnail = null;
    private String[] list = null;
    private String[] listEntry = null;
    
    public MsmGetRecommendation(String proc, String opcode) {
        super(proc, opcode);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

    public String[] getList() {
        return list;
    }

    public void setList(String[] list) {
        this.list = list;
    }

	public String[] getListEntry() {
		return listEntry;
	}

	public void setListEntry(String[] listEntry) {
		this.listEntry = listEntry;
	}
}
