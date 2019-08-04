package com.mogoo.parser;

import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import com.mogoo.network.http.HttpUtils;

/**
 * 解析器
 * 用于解析HTTP协议返回结果
 * @ author 张永辉
 * @ date 2011-11-16
 */
public class XmlResultParser {
	private static final String TAG = "XmlResultParser" ;
	/**
	 * 解析HTTP协议返回结果
	 */
	public static Result parser(InputStream is,XmlResultCallback callback){
		Result result = null ;

		try{
	    	SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
	    	XmlParserHandler handler = new XmlParserHandler() ;
	    	handler.setXmlResultCallback(callback) ;
			parser.parse(is,handler);
			result=handler.getResult() ;
    	}catch(Exception e){
    		result=null;
    		/** add by lcq:2012-6-14 注意：对于程序的异常不要捕捉所有的Exception，同时要尽可能详细的
    		 * 输出异常信息，绝对不要捕捉异常不做处理 */
    		e.printStackTrace();
    		/** add end */
    	}
	
		return result;
	}
}
