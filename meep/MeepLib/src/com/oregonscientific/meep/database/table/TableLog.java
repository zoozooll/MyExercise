package com.oregonscientific.meep.database.table;


public class TableLog {
    public static final String S_TABLE_NAME = "logs";
    public static final String S_ID = "id";
    public static final String S_EVENT_TIME = "eventTime";
    public static final String S_LOGTYPE = "logType";
    public static final String S_MESSAGE = "message";

    private long id = 0;
    private long eventTime = 0;
    private String logType = null;
    private String message = null;

    public TableLog() {
        super();
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getEventTime() {
        return eventTime;
    }

    public void setEventTime(long eventTime) {
        this.eventTime = eventTime;
    }

    public static String getCreateSql() {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS ");
        sb.append(S_TABLE_NAME);
        sb.append(" (");
        sb.append(S_ID);
        sb.append(" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, ");
        sb.append(S_EVENT_TIME);
        sb.append(" INTEGER NOT NULL, ");
        sb.append(S_LOGTYPE);
        sb.append(" VARCHAR(10) NOT NULL, ");
        sb.append(S_MESSAGE);
        sb.append(" VARCHAR(255) NOT NULL");
        sb.append(" )");
        return sb.toString();
    }

    public static String getSelectSql(long lastId) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM ");
        sb.append(S_TABLE_NAME);
        sb.append(" WHERE ");
        sb.append(S_ID);
        sb.append(">");
        sb.append(lastId);
        return sb.toString();
    }

    public static String getDeleteSql(int lastId) {
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM ");
        sb.append(S_TABLE_NAME);
        sb.append(" WHERE ");
        sb.append(S_ID);
        sb.append("<=");
        sb.append(lastId);
        return sb.toString();
    }

    public String getInsertSql() {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ");
        sb.append(S_TABLE_NAME);
        sb.append(" (");
        sb.append(S_EVENT_TIME);
        sb.append(",");
        sb.append(S_LOGTYPE);
        sb.append(",");
        sb.append(S_MESSAGE);
        sb.append(") VALUES (");
        sb.append(this.getEventTime());
        sb.append(",'");
        sb.append(this.getLogType());
        sb.append("','");
        sb.append(this.getMessage());
        sb.append("'");
        sb.append(")");

        return sb.toString();
    }

    public static String getDropTableSql() {
        StringBuilder sb = new StringBuilder();
        sb.append("DROP TABLE ");
        sb.append(S_TABLE_NAME);
        return sb.toString();
    }
}
