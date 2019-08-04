package com.mogoo.parser;

import org.xml.sax.Attributes;

/**
 * 解析接口 
 * 用于各业务模块实现具体的结果解析
 * @ author 张永辉
 * @ date 2011-11-16
 */
public abstract class XmlResultCallback {
	/**
	 * 返回解析结果
	 */
	public Object getResult() {
		return null;
	}
	
	/**
	 * 同SAX中的startElement()
	 */
	public void startElement(String uri, String localName, String qName,Attributes attributes) {
	}
	/**
	 * 同SAX中的endElement()
	 */
	public void endElement(String uri, String localName, String qName) {
	}
	/**
	 * 同SAX中的characters()
	 */
	public void characters(char[] ch, int start, int length) {
	}
}
