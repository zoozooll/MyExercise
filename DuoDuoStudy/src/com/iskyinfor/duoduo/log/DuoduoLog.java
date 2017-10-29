package com.iskyinfor.duoduo.log;

import android.util.Log;


public class DuoduoLog{

	//此常量控制为日志开关
	private static final boolean SHOW_LOG = true;
	
	/**
     * Send a {@link #VERBOSE} log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int v(String tag, String msg) {
    	if (SHOW_LOG){
    		return Log.v(tag, msg);
    		
    	}
    	else {
    		return -1;
    	}
    }

    /**
     * Send a {@link #VERBOSE} log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    public static int v(String tag, String msg, Throwable tr) {
    	if (SHOW_LOG){
    		return Log.v(tag, msg,tr);
    		
    	}
    	else {
    		return -1;
    	}
    }

    /**
     * Send a {@link #DEBUG} log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int d(String tag, String msg) {
    	if (SHOW_LOG){
    		return Log.d(tag, msg);
    		
    	}
    	else {
    		return -1;
    	}
    }

    /**
     * Send a {@link #DEBUG} log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    public static int d(String tag, String msg, Throwable tr) {
    	if (SHOW_LOG){
    		return Log.d(tag, msg,tr);
    		
    	}
    	else {
    		return -1;
    	}
    }

    /**
     * Send an {@link #INFO} log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int i(String tag, String msg) {
    	if (SHOW_LOG){
    		return Log.v(tag, msg);
    		
    	}
    	else {
    		return -1;
    	}
    }

    /**
     * Send a {@link #INFO} log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    public static int i(String tag, String msg, Throwable tr) {
    	if (SHOW_LOG){
    		return Log.i(tag, msg,tr);
    	}
    	else {
    		return -1;
    	}
    }

    /**
     * Send a {@link #WARN} log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int w(String tag, String msg) {
    	if (SHOW_LOG){
    		return Log.w(tag, msg);
    		
    	}
    	else {
    		return -1;
    	}
    }

    /**
     * Send a {@link #WARN} log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    public static int w(String tag, String msg, Throwable tr) {
    	if (SHOW_LOG){
    		return Log.w(tag, msg,tr);
    		
    	}
    	else {
    		return -1;
    	}
    }


    /**
     * Send a {@link #WARN} log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param tr An exception to log
     */
    public static int w(String tag, Throwable tr) {
    	if (SHOW_LOG){
    		return Log.w(tag, tr);
    	}
    	else {
    		return -1;
    	}
    }

    /**
     * Send an {@link #ERROR} log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int e(String tag, String msg) {
    	if (SHOW_LOG){
    		return Log.e(tag, msg);
    		
    	}
    	else {
    		return -1;
    	}
    }

    /**
     * Send a {@link #ERROR} log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    public static int e(String tag, String msg, Throwable tr) {
    	if (SHOW_LOG){
    		return Log.e(tag, msg,tr);
    	}
    	else {
    		return -1;
    	}
    }

    /**
     * What a Terrible Failure: Report a condition that should never happen.
     * The error will always be logged at level ASSERT with the call stack.
     * Depending on system configuration, a report may be added to the
     * {@link android.os.DropBoxManager} and/or the process may be terminated
     * immediately with an error dialog.
     * @param tag Used to identify the source of a log message.
     * @param msg The message you would like logged.
     */
    public static int wtf(String tag, String msg) {
    	if (SHOW_LOG){
    		return Log.wtf(tag, msg);
    		
    	}
    	else {
    		return -1;
    	}
    }

    /**
     * What a Terrible Failure: Report an exception that should never happen.
     * Similar to {@link #wtf(String, String)}, with an exception to log.
     * @param tag Used to identify the source of a log message.
     * @param tr An exception to log.
     */
    public static int wtf(String tag, Throwable tr) {
        return wtf(tag, tr.getMessage(), tr);
    }

    /**
     * What a Terrible Failure: Report an exception that should never happen.
     * Similar to {@link #wtf(String, Throwable)}, with a message as well.
     * @param tag Used to identify the source of a log message.
     * @param msg The message you would like logged.
     * @param tr An exception to log.  May be null.
     */
    public static int wtf(String tag, String msg, Throwable tr) {
    	if (SHOW_LOG){
    		return Log.wtf(tag, msg,tr);
    	}
    	else {
    		return -1;
    	}
    }

    /**
     * Low-level logging call.
     * @param priority The priority/type of this log message
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @return The number of bytes written.
     */
    public static int println(int priority, String tag, String msg) {
    	if (SHOW_LOG){
    		return Log.println(priority, tag, msg);
    	}
    	else {
    		return -1;
    	}
    }
}
