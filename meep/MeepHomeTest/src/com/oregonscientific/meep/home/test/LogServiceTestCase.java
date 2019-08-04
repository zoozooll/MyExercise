package com.oregonscientific.meep.home.test;

import java.util.ArrayList;
import java.util.List;

import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;

import com.oregonscientific.meep.ServiceManager;
import com.oregonscientific.meep.logging.Level;
import com.oregonscientific.meep.logging.LogManager;
import com.oregonscientific.meep.logging.LogRecord;
import com.oregonscientific.meep.logging.LogService;
import com.oregonscientific.meep.msm.Message;

public class LogServiceTestCase extends BaseServiceTestCase<LogService> {
	
private final String TAG = NotificationServiceTestCase.class.getName();
	
	public LogServiceTestCase(Class<LogService> serviceClass) {
		super(serviceClass);
	}
	
	public LogServiceTestCase() {
		super(LogService.class);
	}
	
	public void testCreateLog() {
		final LogManager logManager = (LogManager) ServiceManager.getService(getContext(), ServiceManager.LOG_SERVICE);
		
		LogRecord log = new LogRecord(LogManager.LOG_SYSTEM, Level.DEBUG, "Testing 123");
		log.setResourceBundleName("System");
		log.setResourceName("install");
		log.setParameters(new String[] { "com.oregonscientific.meep.home" });
		
		logManager.log(log, Test.DATA_ACCOUNT_USER_ID);
	}
	
	public void testCreateLogMessage() {
		List<LogRecord> records = new ArrayList<LogRecord>();
		
		LogRecord log = new LogRecord(LogManager.LOG_SYSTEM, Level.DEBUG, "Testing 123");
		log.setResourceBundleName("System");
		log.setResourceName("install");
		log.setParameters(new String[] { "com.oregonscientific.meep.home" });
		
		records.add(log);
		
		Message message = new Message(Message.PROCESS_SYSTEM, Message.OPERATION_CODE_LOG);
		message.addProperty("logs", records);
		
		Log.i(TAG, message.toJson());
	}
	
	@SmallTest
	public void testSendLogs() {
		final LogManager logManager = (LogManager) ServiceManager.getService(getContext(), ServiceManager.LOG_SERVICE);
		
		// Creates a LogRecord
		LogRecord log = new LogRecord(LogManager.LOG_SYSTEM, Level.DEBUG, "Testing 123");
		log.setResourceBundleName("System");
		log.setResourceName("install");
		log.setParameters(new String[] { "com.oregonscientific.meep.home" });
		logManager.log(log, Test.DATA_ACCOUNT_USER_ID);
		
		// Sends log records to server
		logManager.startLogger(LogManager.MIN_LOG_INTERVAL);
		
		waitUntilComplete();
		assertTrue(mTestResult);
	}

}
