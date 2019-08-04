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
public class UnderlineResultParser {
	private static final String TAG = "UnderlineResultParser" ;
	/**
	 * 解析HTTP协议返回结果
	 */
	public static Result parser(InputStream is,UnderlineResultCallback callback){
		Result result = null ;

		String response = HttpUtils.streamToString(is).toString() ;
		
		String [] strs = parseUnderLineResponse(response) ;
		if(strs.length>=2){
			result = new Result() ;
			result.setSession(strs[0]) ;
			result.setErrorCode(strs[1]) ;
			if(callback!=null){
				result.setData(callback.handleUnderline(strs));
			}
		}else{
			return null ;
		}
	
		return result;
	}
	
	/**
	 * 解析响应数据
	 */
	public static String[] parseUnderLineResponse(String response) {
		if (response == null) {
			throw new IllegalArgumentException("Parameter is null.");
		}
		String[] strs = response.split("\\|\\|");
		return strs;
	}
}
