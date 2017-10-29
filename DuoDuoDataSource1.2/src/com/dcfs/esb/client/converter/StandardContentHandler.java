package com.dcfs.esb.client.converter;


import java.util.Stack;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import com.dc.eai.data.Array;
import com.dc.eai.data.CompositeData;
import com.dc.eai.data.Field;
import com.dc.eai.data.FieldAttr;
import com.dc.eai.data.FieldType;

/**
 * 
 * @author ex-wanghuaxi
 *
 * 2008-10-08
 */
public class StandardContentHandler implements ContentHandler 
{
	private static Log log = LogFactory.getLog(StandardContentHandler.class);
    
	//鏈�悗鏁版嵁鍩熺殑鍐呭
    protected StringBuffer _lastValue;
    //鏈�悗鏁版嵁鍩熺殑鍩熷悕
    protected String _fieldName = null;
    //鏈�悗鏁版嵁鍩熺殑灞炴�
    protected FieldAttr _fieldAttr = null;
	//杩斿洖鐨凜ompositeData
	private CompositeData data = null;
	
	//褰撳墠瀵硅薄鐨勬爤锛屾爤椤跺璞℃槸褰撳墠鐨勬搷浣滃璞�
	private Stack stack = new Stack();
	
	public StandardContentHandler(CompositeData data)
	{
		this.data = data;
	}
	
	/*
	 * 鎴彇鍐呭
	 * (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#characters(char[], int, int)
	 */
	public void characters(char[] arg0, int arg1, int arg2) 
		throws SAXException 
	{
        if(_lastValue==null)
        	_lastValue = new StringBuffer();
        _lastValue.append(new String(arg0,arg1,arg2));
	}
	
	/*
	 * 寮�鏂囨。澶勭悊
	 * (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#startDocument()
	 */
	public void startDocument() throws SAXException 
	{
		//灏嗗熀纭�殑data瀵硅薄鍏ユ爤
		stack.push(data);
	}

	/**
	 * 缁撴潫鏂囨。澶勭悊
	 */
	public void endDocument() throws SAXException 
	{
		//灏嗗熀纭�殑data瀵硅薄鍑烘爤
		stack.pop();
	}

	public void startElement(String namespaceURI, String localName,
            String qName, Attributes atts) throws SAXException 
	{
        _lastValue = null;
        _fieldAttr = null;
       // _fieldLen  = null;
        
        if(StandardConverter.ARRAY.equalsIgnoreCase(qName))
        {//鏁扮粍锛屽垱寤哄璞�
        	Object peek_obj = stack.peek();
    		Array array = new Array();
    		
    		//鍒ゆ柇鏍堥《瀵硅薄鏄綍绫诲瀷
    		if(peek_obj instanceof Array)
    			((Array) peek_obj).addArray(array);
    		else
    			((CompositeData) peek_obj).addArray(_fieldName, array);    		
    		stack.push(array);
        }
        else if(StandardConverter.STRUCT.equalsIgnoreCase(qName))
    	{//瀵硅薄鏄粍鍚堬紝鍒欐坊鍔犵粍鍚堝璞�
        	Object peek_obj = stack.peek();
    		CompositeData data = new CompositeData();
    		
    		//鍒ゆ柇鏍堜腑鐨勫璞¤繘琛屾坊鍔�
    		if(peek_obj instanceof Array)
    			((Array) peek_obj).addStruct(data);
    		else
    			((CompositeData) peek_obj).addStruct(_fieldName, data);
    		stack.push(data);
    	}
        else if(StandardConverter.DATA.equalsIgnoreCase(qName) )
        {//澶勭悊鍖呭ご鍜屾姤鏂囦綋
        	_fieldName = atts.getValue("name");
        }
        else if(StandardConverter.FIELD.equalsIgnoreCase(qName))
        {//鍗曚釜鏁版嵁鍩�
        	
        	//鑾峰彇绫诲瀷
        	String type = atts.getValue("type");
        	if(type==null)
        		type="string";
        	
        	//鑾峰彇鍩熼暱搴︼紝榛樿涓�
        	int len = 0;
        	String length = atts.getValue("length");
        	if(length != null)
        	{
        		len = Integer.parseInt(length);
        	}

        	//鑾峰彇鍩熺殑绮惧害锛岄粯璁や负0
        	int sc = 0;
        	String scale = atts.getValue("scale");        	
        	if(scale != null)
        	{
        		sc = Integer.parseInt(scale);
        	}

        	//灞炰簬鍩�
        	if(type!=null)
        	{
        		_fieldAttr = new FieldAttr(FieldType.getEnum(type),len,sc);
        	}
        }
	}
	
	public void endElement(String namespaceURI, String localName, String qName)
			throws SAXException 
	{
		//瀵逛簬鏁扮粍鎴栨槸缁勫悎锛屽潎浣滃嚭鏍堝鐞�
        if(StandardConverter.ARRAY.equalsIgnoreCase(qName) ||
        		StandardConverter.STRUCT.equalsIgnoreCase(qName))
        {
        	stack.pop();
        }
        else if(_fieldAttr!= null )
        {

        	Field field = new Field(_fieldAttr);
        	String value = "";
        	if(_lastValue!=null)
        		value =  _lastValue.toString();
        	boolean flag = this.setField(field, _fieldAttr, value);
        	if(flag)
        	{
        		//灏嗗垱寤虹殑鍩熸坊鍔犲埌缁勫悎涓�
        		Object peek_obj = stack.peek();
        		if( peek_obj instanceof Array)
        			( (Array) peek_obj ).addField(field);
        		else
        			//( (CompositeData) peek_obj).addField(_fieldName.toUpperCase(), field);
        			( (CompositeData) peek_obj).addField(_fieldName, field);
        	}
        }
        else if (!StandardConverter.ROOT.equalsIgnoreCase(qName) && _fieldName != null)
        {
        	log.debug("鏁版嵁鍩焄"+_fieldName+"]鏃犳硶澶勭悊");
        }
        
        _lastValue = null;
        _fieldName = null;
        _fieldAttr = null;

	}
	
    /**
     * 濡傛灉鍑虹幇涓嶅悎娉曠殑鎯呭喌锛屾瘮濡傚悜鏁板瓧鍨嬪煙涓缃┖涓诧紝杩斿洖false
     * 
     * @param field
     * @param attr
     * @param value
     * @return
     */
	public boolean setField(Field field, FieldAttr attr, String value)
	{
        FieldType fieldType = attr.getType();
		if (fieldType == FieldType.FIELD_STRING)
		{
        	char[] srcdata = value.toCharArray();
			char[] tarData = new char[srcdata.length];
			int index = 0;
			for (int i = 0,len=value.length(); i < len;)
			{
				if (srcdata[i] == '\\' && i+3<len)
				{
					if (srcdata[i + 1] == '\\' && srcdata[i + 2] == '0'
							&& srcdata[i + 3] == 'x')
					{
						if(i+4<len&& srcdata[i+4]=='\\')
						{
							tarData[index++]=srcdata[i++];
							tarData[index++]=srcdata[i++];
							tarData[index++]=srcdata[i++];
							tarData[index++]=srcdata[i++];
							i++;
							continue;
						}
						else if(i+5<len)
						{
							try
							{
						String temp = String.valueOf(srcdata[i + 4])
								+ String.valueOf(srcdata[i + 5]);
								tarData[index] = (char) Integer.parseInt(temp, 16);
								index++;
						i = i + 6;
								continue;
							}
							catch(Exception e)
							{
							}
						}

					}
				}

				tarData[index++] = srcdata[i++];
			}
			String tarValue = String.copyValueOf(tarData, 0, index);
			boolean spaceTrim = StandardConverter.isSpaceTrim();
			if (spaceTrim == true)
				field.setValue(tarValue.trim());
			else
				field.setValue(tarValue);
		} else if (fieldType == FieldType.FIELD_BYTE)
		{
            field.setValue(new Byte(value));
		} else if (fieldType == FieldType.FIELD_DOUBLE)
		{
			if (value.trim().equals(""))
			{
				field.setValue(0.0);
				return false;
			} else
			{
                field.setValue(new Double(value));
            }
		} else if (fieldType == FieldType.FIELD_FLOAT)
		{
			if (value.trim().equals(""))
			{
				field.setValue(0.0);
				return false;
			} else
			{
                field.setValue(new Float(value));
            }
        } else if (fieldType == FieldType.FIELD_INT
				|| fieldType == FieldType.FIELD_INT24)
		{
			if (value.trim().equals(""))
			{
				field.setValue(0);
				return false;
			} else
			{
                field.setValue(new Integer(value));
            }
		} else if (fieldType == FieldType.FIELD_LONG)
		{
			if (value.trim().equals(""))
			{
				field.setValue(0);
				return false;
			} else
			{
                field.setValue(new Long(value));
            }
		} else if (fieldType == FieldType.FIELD_SHORT)
		{
			if (value.trim().equals(""))
			{
				field.setValue(0);
				return false;
			} else
			{
                field.setValue(new Short(value));
            }
		} else
		{ // FieldType.FIELD_IMAGE
        	//byte [] bytes = StandardCdToXml.decodeBase64(value);
            //field.setValue(bytes);
        }
        return true;
    }

	public void endPrefixMapping(String arg0) throws SAXException 
	{
		// TODO Auto-generated method stub

	}

	public void ignorableWhitespace(char[] arg0, int arg1, int arg2)
			throws SAXException 
	{
		// TODO Auto-generated method stub

	}

	public void processingInstruction(String arg0, String arg1)
			throws SAXException 
	{
		// TODO Auto-generated method stub

	}

	public void setDocumentLocator(Locator arg0) 
	{
		// TODO Auto-generated method stub

	}

	public void skippedEntity(String arg0) throws SAXException 
	{
		// TODO Auto-generated method stub

	}

	public void startPrefixMapping(String arg0, String arg1)
			throws SAXException 
	{
		// TODO Auto-generated method stub

	}
}