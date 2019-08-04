package com.oregonscientific.meep.message.common;

public class RunSqlResultMessage {
	private int mErrCode = 0;
	private String mError  = null;
	
	
	public RunSqlResultMessage(){
		setErrCode(0);
		setErrorString("");
	}
	
	public RunSqlResultMessage(int errorCode, String errorString){
		setErrCode(errorCode);
		setErrorString(errorString);
	}


	public int getErrCode() {
		return mErrCode;
	}


	public void setErrCode(int mErrCode) {
		this.mErrCode = mErrCode;
	}


	public String getErrorString() {
		return mError;
	}


	public void setErrorString(String mError) {
		this.mError = mError;
	}
}
