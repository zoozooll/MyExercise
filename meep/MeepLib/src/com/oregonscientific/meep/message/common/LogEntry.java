package com.oregonscientific.meep.message.common;

public class LogEntry {
    private long event_time = 0;
    private String message = null;
    
    public LogEntry() {
        super();
    }

    public long getEvent_time() {
        return event_time;
    }

    public void setEvent_time(long event_time) {
        this.event_time = event_time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
