package com.oregonscientific.meep.message.common;

public class MsmGetBlacklist extends MeepServerMessage {
    private String type = null;
    private String[] list = null;

    public MsmGetBlacklist(String proc, String opcode) {
        super(proc, opcode);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String[] getList() {
        return list;
    }

    public void setList(String[] list) {
        this.list = list;
    }
}
