package com.oregonscientific.meep.junit.test;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Enumeration;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import android.os.Bundle;
import android.test.InstrumentationTestRunner;
import android.util.Log;


public class BaseTestRunner extends InstrumentationTestRunner {

	 private Writer mWriter;
	    private XmlSerializer mTestSuiteSerializer;
	    private long mTestStarted;
	    private static final String JUNIT_XML_FILE = "TEST-all.xml";
	    
	    
	    @Override
	    public TestSuite getAllTests() {
	    	TestSuite suite = super.getAllTests();
//	    	Enumeration<Test> enumerationTest = suite.tests();
//	    	
//	    	while (enumerationTest.hasMoreElements()) {
//	    		Test test = enumerationTest.nextElement();
//	    		test.
//	    	}
			return suite;
	    	
	    }
	    
	    @Override
	    public void onStart() {
	        try {
	            startJUnitOutput(new FileWriter(new File(getTargetContext().getFilesDir(), JUNIT_XML_FILE)));
	            Log.e("TestResult", getTargetContext().getFilesDir().getAbsolutePath());
	        } catch (IOException e) {
	            throw new RuntimeException(e);
	        }
	        super.onStart();
	    }

	    void startJUnitOutput(Writer writer) {
	        try {
	            mWriter = writer;
	            mTestSuiteSerializer = newSerializer(mWriter);
	            mTestSuiteSerializer.startDocument(null, null);
	            mTestSuiteSerializer.startTag(null, "testsuites");
	            mTestSuiteSerializer.startTag(null, "testsuite");
	        } catch (Exception e) {
	            throw new RuntimeException(e);
	        }
	    }
	    
	    private XmlSerializer newSerializer(Writer writer) {
	        try {
	            XmlPullParserFactory pf = XmlPullParserFactory.newInstance();
	            XmlSerializer serializer = pf.newSerializer();
	            serializer.setOutput(writer);
	            return serializer;
	        } catch (Exception e) {
	            throw new RuntimeException(e);
	        }        
	    }
	    
	    @Override
	    public void sendStatus(int resultCode, Bundle results) {
	        super.sendStatus(resultCode, results);
	        switch (resultCode) {
	            case REPORT_VALUE_RESULT_ERROR:
	            case REPORT_VALUE_RESULT_FAILURE:
	            case REPORT_VALUE_RESULT_OK:
	            try {
	                recordTestResult(resultCode, results);
	                } catch (IOException e) {
	                    throw new RuntimeException(e);
	                }
	                break;
	            case REPORT_VALUE_RESULT_START:
	                recordTestStart(results);
	            default:
	                break;
	        }
	    }
	    
	    void recordTestStart(Bundle results) {
	        mTestStarted = System.currentTimeMillis();
	    }

	    void recordTestResult(int resultCode, Bundle results) throws IOException {
	        float time = (System.currentTimeMillis() - mTestStarted) / 1000.0f;
	        String className = results.getString(REPORT_KEY_NAME_CLASS);
	        String testMethod = results.getString(REPORT_KEY_NAME_TEST);
	        String stack = results.getString(REPORT_KEY_STACK);
	        int current = results.getInt(REPORT_KEY_NUM_CURRENT);
	        int total = results.getInt(REPORT_KEY_NUM_TOTAL);
	        
	        mTestSuiteSerializer.startTag(null, "testcase");
	        mTestSuiteSerializer.attribute(null, "classname", className);
	        mTestSuiteSerializer.attribute(null, "name", testMethod);
	        
	        if (resultCode != REPORT_VALUE_RESULT_OK) {
	            mTestSuiteSerializer.startTag(null, "failure");
	            if (stack != null) {
	                String reason = stack.substring(0, stack.indexOf('\n'));
	                String message = "";
	                int index = reason.indexOf(':');
	                if (index > -1) {
	                    message = reason.substring(index+1);
	                    reason = reason.substring(0, index);
	                }
	                mTestSuiteSerializer.attribute(null, "message", message);
	                mTestSuiteSerializer.attribute(null, "type", reason);
	                mTestSuiteSerializer.text(stack);
	            }
	            mTestSuiteSerializer.endTag(null, "failure");
	        } else {
	            mTestSuiteSerializer.attribute(null, "time", String.format("%.3f", time));
	        }
	        mTestSuiteSerializer.endTag(null, "testcase");        
	        if (current == total) {
	            mTestSuiteSerializer.startTag(null, "system-out");
	            mTestSuiteSerializer.endTag(null, "system-out");
	            mTestSuiteSerializer.startTag(null, "system-err");
	            mTestSuiteSerializer.endTag(null, "system-err");
	            mTestSuiteSerializer.endTag(null, "testsuite");
	            mTestSuiteSerializer.flush();
	        }
	    }

	    @Override
	    public void finish(int resultCode, Bundle results) {
	    	endTestSuites();
	    	super.finish(resultCode, results);
	    }

	    void endTestSuites() {
	    	try {
	    		mTestSuiteSerializer.endTag(null, "testsuites");
	    		mTestSuiteSerializer.endDocument();
	    		mTestSuiteSerializer.flush();
	    		mWriter.flush();
	    		mWriter.close();
	    	} catch (IOException e) {
	    		throw new RuntimeException(e);
	    	}
	    }
}