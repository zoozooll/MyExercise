package com.dcfs.esb.client.exception;

public class TimeoutException extends Exception{
	
	private final static long serialVersionUID = 1L;
	
	public TimeoutException(){
		super();
	}
	
	public TimeoutException(String message){
		super(message);
	}
	
	public TimeoutException(Throwable cause){
		super(cause);
	}
	
	public TimeoutException(String message,Throwable cause){
		super(message,cause);
	}
}
