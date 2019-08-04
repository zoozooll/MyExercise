package com.mogoo.parser;
/**
 * 返回结果
 * @ author 张永辉
 * @ date 2011-11-16
 */
public class Result {
	private String session ;
	private String errorCode ;
	private Object data ;
	
	public String getSession() {
		return session;
	}
	public void setSession(String session) {
		this.session = session;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
	/**
	 * 返回结果标签
	 */
	public static interface ResultTag{
		public static final String SESSION = "session" ;
		public static final String ERRORCODE = "errorcode" ;
	}
}
