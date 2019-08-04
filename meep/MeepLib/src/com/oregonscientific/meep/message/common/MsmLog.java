package com.oregonscientific.meep.message.common;

public class MsmLog extends MeepServerMessage {

    private Object[] logs = null;
    
    public MsmLog(String proc, String opcode) {
        super(proc, opcode);
    }

    public Object[] getLogs() {
        return logs;
    }

    public void setLogs(Object[] logs) {
        this.logs = logs;
    }
}
