package com.beem.project.btf.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.beem.project.btf.BeemApplication;

import android.os.Environment;
import android.util.Log;

/**
 * @func 日志类 1.可控制日志等级 2.可查看调用处（调用所处类，方法，多少行）3.可跟踪调用栈
 * @author yuedong bao
 * @time 2014-12-24 下午3:02:52
 */
public class LogUtils {
	private final static String tagName = "vv_log";
	private static final String LOG_FILE = Environment
			.getExternalStorageDirectory().getPath()
			+ File.separator
			+ tagName
			+ File.separator
			+ new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())
			+".txt";
	private static final boolean isDebug = true && BeemApplication.sDebuggerEnabled;//控制日志输出 
	private static int curLogLevel;
	private static String msgT;
	private static String msgC;
	// 日志类存在多线程问题，需要加锁
	private static Lock lock = new ReentrantLock();
	private static Writer smackWriter;
	
	static {
		try {
			File logfile = new File(LOG_FILE);
			if (!logfile.getParentFile().exists()) {
				logfile.getParentFile().mkdirs();
			}
			smackWriter = new FileWriter(logfile);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private LogUtils() {
	}
	public static void initTrace(String msg, int... isPrintStack) {
		int isPrintStackOne = isPrintStack.length > 0 ? isPrintStack[0] : 0;
		StackTraceElement[] currentThread = Thread.currentThread()
				.getStackTrace();
		int curentIndex = getCallStackPos(currentThread);
		if (curentIndex > -1) {
			msgT = "[" + currentThread[curentIndex].getFileName() + ":"
					+ currentThread[curentIndex].getLineNumber() + " "
					+ currentThread[curentIndex].getMethodName() + "()] ";
			msgC = "msg:[" + msg + "] ";
			if (isPrintStackOne > 0) {
				String callTraceStack = getCallBackStr(currentThread,
						curentIndex, isPrintStack);
				msgC += callTraceStack;
			}
		} else {
			throw new IllegalArgumentException(
					"cannot find the callstack position ");
		}
	}
	public static String getCallBackStr(StackTraceElement[] currentThread,
			int curentIndex, int... isPrintStack) {
		int isPrintStackOne = isPrintStack.length > 0 ? isPrintStack[0] : 0;
		StringBuilder sb = new StringBuilder();
		sb.append("callTraceStacks:[");
		int loopMin = Math.min(curentIndex + isPrintStackOne,
				currentThread.length);
		for (int i = curentIndex; i < loopMin; i++) {
			sb.append(currentThread[i].getFileName() + ":"
					+ currentThread[i].getLineNumber() + " "
					+ currentThread[i].getMethodName() + "()");
			if (i < loopMin - 1) {
				sb.append("<--");
			}
		}
		sb.append("]");
		return sb.toString();
	}
	public static int getCallStackPos(StackTraceElement[] currentThread) {
		if (currentThread == null)
			return -1;
		for (int i = 0; i < currentThread.length; i++) {
			if (currentThread[i].isNativeMethod())
				continue;
			if (currentThread[i].getClassName().equals(Thread.class.getName()))
				continue;
			if (currentThread[i].getClassName()
					.equals(LogUtils.class.getName()))
				continue;
			return i;
		}
		return -1;
	}
	public static void e(String msg, int... printStackNum) {
		log(Log.ERROR, msg, printStackNum);
	}
	public static void w(String msg, int... printStackNum) {
		log(Log.WARN, msg, printStackNum);
	}
	public static void d(String msg, int... printStackNum) {
		log(Log.DEBUG, msg, printStackNum);
	}
	public static void v(String msg, int... printStackNum) {
		log(Log.VERBOSE, msg, printStackNum);
	}
	public static void i(String msg, int... printStackNum) {
		log(Log.INFO, msg, printStackNum);
	}
	private static void log(int logLevel, String msg, int... printStackNum) {
		if (curLogLevel > logLevel || !isDebug) {
			return;
		}
		lock.lock();
		
		try {
			initTrace(msg, printStackNum);
			switch (logLevel) {
				case Log.VERBOSE: {
					Log.v(tagName, msgT + msgC);
					try {
						smackWriter.write("V| " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(new Date()) + " | " + msgT + msgC + "\r\n");
						smackWriter.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				}
				case Log.DEBUG: {
					Log.d(tagName, msgT + msgC);
					try {
						smackWriter.write("D|" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(new Date()) + " | " +msgT + msgC + "\r\n");
						smackWriter.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				}
				case Log.INFO: {
					Log.i(tagName, msgT + msgC);
					try {
						smackWriter.write("I|" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(new Date()) + " | " +msgT + msgC + "\r\n");
						smackWriter.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				}
				case Log.WARN: {
					Log.w(tagName, msgT + msgC);
					try {
						smackWriter.write("W|" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(new Date()) + " | " +msgT + msgC + "\r\n");
						smackWriter.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				}
				case Log.ERROR: {
					Log.e(tagName, msgT + msgC);
					try {
						smackWriter.write("E|" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(new Date()) + " | " +msgT + msgC + "\r\n");
						smackWriter.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				}
			}
		} finally {
			lock.unlock();
		}
	}
	public static void setLogLevel(int level) {
		curLogLevel = level;
	}
}
