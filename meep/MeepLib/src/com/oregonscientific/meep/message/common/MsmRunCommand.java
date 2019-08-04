package com.oregonscientific.meep.message.common;

public class MsmRunCommand extends MeepServerMessage {

	private String command = null;
	private String parameter = null;
    private String message = null;
	
	public MsmRunCommand(String proc, String opcode) {
		super(proc, opcode);
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
