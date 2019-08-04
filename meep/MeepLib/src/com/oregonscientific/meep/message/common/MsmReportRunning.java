package com.oregonscientific.meep.message.common;

public class MsmReportRunning extends MeepServerMessage {
	private String runningTask = null;

	public MsmReportRunning(String proc, String opcode) {
		super(proc, opcode);
	}

	public String getRunningTask() {
		return runningTask;
	}

	public void setRunningTask(String runningTask) {
		this.runningTask = runningTask;
	}
}
