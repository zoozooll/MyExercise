package com.mogoo.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/** 
 * 解析处理器,用于解析HTTP返回的XML格式结果
 * @ author 张永辉
 * @ date 2011-11-16
 */
public class XmlParserHandler extends DefaultHandler {
	private String TAG = "XmlParserHandler" ;
	
	private Result result ;
	private XmlResultCallback xmlResultCallback ;
	/**
	 * 当前正在解析的标签
	 */
	private String tagName ;
	
	public void setXmlResultCallback(XmlResultCallback xmlResultCallback) {
		this.xmlResultCallback = xmlResultCallback;
	}

	public Result getResult() {
		return result;
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		result = new Result() ;
	}

	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
		if(xmlResultCallback!=null){
			result.setData(xmlResultCallback.getResult());
		}
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		if(!Result.ResultTag.SESSION.equals(localName)&&
				!Result.ResultTag.ERRORCODE.equals(localName)){
			if(xmlResultCallback!=null){
				xmlResultCallback.startElement(uri, localName, qName, attributes) ;
			}
		}else{
			this.tagName = localName ;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		super.endElement(uri, localName, qName);
		if(!Result.ResultTag.SESSION.equals(localName)&&
				!Result.ResultTag.ERRORCODE.equals(localName)){
			if(xmlResultCallback!=null){
				xmlResultCallback.endElement(uri, localName, qName) ;
			}
		}else{
			this.tagName = null ;
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		super.characters(ch, start, length);
		if(Result.ResultTag.SESSION.equals(tagName)){
			result.setSession(new String(ch,start,length)) ;
		}else if(Result.ResultTag.ERRORCODE.equals(tagName)){
			result.setErrorCode(new String(ch,start,length));
		}else{
			if(xmlResultCallback!=null){
				xmlResultCallback.characters(ch, start, length) ;
			}
		}
	}
	
}
