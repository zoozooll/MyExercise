package com.oregonscientific.meep.message.common;

public class MsmDeleteGroup extends MeepServerMessage{

	private String group = null;
	
	public MsmDeleteGroup(String proc, String opcode) {
		super(proc, opcode);
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

}
