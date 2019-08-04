package com.oregonscientific.meep.message.common;

public class MsmRemoteConsole extends MeepServerMessage {
	public MsmRemoteConsole(String proc, String opcode) {
		super(proc, opcode);
	}
	private String command;
	private String result;
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	
}
