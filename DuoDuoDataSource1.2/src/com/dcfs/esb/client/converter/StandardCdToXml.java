package com.dcfs.esb.client.converter;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



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
public class StandardCdToXml 
{	
	
	private static Log log = LogFactory.getLog(StandardCdToXml.class);
    
	/**
	 * 功能：将compositedata转换成xml报文
	 * @param data
	 * @return
	 * @throws PackException
	 */
	public String convert(CompositeData data)
	throws Exception
	{
		StringBuffer sb = new StringBuffer();

		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		sb.append("<service>\n");
		packData(sb,StandardConverter.sys_header,StandardConverter.SYS_HEAD,data.getStruct(StandardConverter.SYS_HEAD));
		packBody(sb,data);
		//packData(sb,StandardConverter.body,StandardConverter.BODY,data.getStruct(StandardConverter.BODY));
		sb.append("</service>\n");

		return sb.toString();
	}
	
	/**
	 * 功能：打包报文头
	 * @param sb
	 * @param data
	 */
	private void packBody(StringBuffer sb,CompositeData data)
	{

		sb.append("<body>\n");
		Iterator it = data.iterator();
		while (it.hasNext()) 
		{
			String name = (String) it.next();
			if(!StandardConverter.SYS_HEAD.endsWith(name))
			packField(sb,name,data.getObject(name),false);
		}
		sb.append("</body>\n");
	}
	
	/**
	 * 功能：打包报文头
	 * @param sb
	 * @param data
	 */
	private void packData(StringBuffer sb,String HEAD_NAME,String head_name,CompositeData data)
	{
		sb.append("<"+HEAD_NAME+">\n");
		packField(sb,head_name.toUpperCase(),data,false);
		sb.append("</"+HEAD_NAME+">\n");
	}
	
	/**
	 * 功能：对某一个域进行打包
	 * @param sb
	 * @param name
	 * @param field
	 */
	private void packField(StringBuffer sb,String name,Object field,boolean array_flag)
	{
		if(field instanceof Field)
		{
			Field the_field = (Field) field;
			if(array_flag == false)
				sb.append("<data name=\""+name+"\">\n");
			else
				sb.append("<data>\n");
			String value =  convFieldToString(the_field);
			
			String type = getFieldType(the_field);
			boolean xmlOptimize = StandardConverter.isXmlOptimize();
			boolean spaceTrim = StandardConverter.isSpaceTrim();
			if("string".equalsIgnoreCase(type)&& xmlOptimize==true)
			{
				sb.append("<field>");
			}else
			{
				sb.append("<field type=\""+getFieldType(the_field))
				.append("\" length=\""+the_field.getAttr().getLength())
				.append("\" scale=\""+the_field.getAttr().getScale()+"\">");	
			}
			if (spaceTrim == true)
				sb.append(value.trim());
			else
				sb.append(value);
			sb.append("</field>\n");
				sb.append("</data>\n");
		}
		else if(field instanceof Array)
		{
			Array the_array = (Array) field;
			if(array_flag == false)
				sb.append("<data name=\""+name+"\">\n");
			else
				sb.append("<data>\n");
			sb.append("<array>\n");
			for(int i = 0;i<the_array.size();i++)
			{
				String curr_name = name+"_"+i;
				packField(sb,curr_name,the_array.getObject(i),true);
			}
			sb.append("</array>\n");
				sb.append("</data>\n");
		}
		else if(field instanceof CompositeData)
		{
			CompositeData the_data = (CompositeData)field;
			if(array_flag == false)
				sb.append("<data name=\""+name+"\">\n");
			else
				sb.append("<data>\n");
			sb.append("<struct>\n");
			Iterator it = the_data.iterator();

			while (it.hasNext()) 
			{
				String curr_name = (String) it.next();
				Object curr_field = the_data.getObject(curr_name);
				packField(sb,curr_name,curr_field,false);
			}
			sb.append("</struct>\n");
				sb.append("</data>\n");
		}
	}
	
	/**
	 * 功能：获取域的数据类型
	 * @param field
	 * @return
	 */
	private String getFieldType(Field field)
	{
		if(field.getFieldType() == FieldType.FIELD_STRING)
			return "string";
		else if(field.getFieldType() == FieldType.FIELD_IMAGE)
			return "image";
		else if(field.getFieldType() == FieldType.FIELD_BYTE)
			return "byte";
		else if(field.getFieldType() == FieldType.FIELD_SHORT)
			return "short";
		else if(field.getFieldType() == FieldType.FIELD_INT24)
			return "int24";
		else if(field.getFieldType() == FieldType.FIELD_INT)
			return "int";
		else if(field.getFieldType() == FieldType.FIELD_LONG)
			return "long";
		else if(field.getFieldType() == FieldType.FIELD_FLOAT)
			return "float";
		else if(field.getFieldType() == FieldType.FIELD_DOUBLE)
			return "double";
		else 
			return "string";
	}
	
    /**
	 * 将域值转换为字符串，正确
	 * 
	 * @param field
	 * @return
	 */
	private String convFieldToString(Field field)
	{
		FieldType type = field.getFieldType();
	       StringBuffer value = new StringBuffer();
	       if (type == FieldType.FIELD_DOUBLE
	           || type == FieldType.FIELD_FLOAT)
	       {
	           FieldAttr fa = field.getAttr();
	           int len = fa.getLength();
	           int scale = fa.getScale();
	           DecimalFormat df = new DecimalFormat(getDecimalFormat(len,scale));
	           if(type == FieldType.FIELD_DOUBLE) {
	              if(field.getValue()!=null)
	                  value.append(df.format(field.doubleValue()));
	           }
	           else {
	              if(field.getValue()!=null)
	                  value.append(df.format(field.floatValue()));
	           }

	       } else if (type == FieldType.FIELD_IMAGE)
	       {
	           byte [] bytes = (byte []) field.getValue();
	       }
	       else
	       {
	           if(field.getValue()!=null)
	              value.append(field.getValue());
	       }
	           
	       return replaceXML(value).toString();
	}
	
	/**
	 * 功能：获取数值的格式
	 * 
	 * @param len
	 * @param scale
	 * @return
	 */
	private String getDecimalFormat(int len, int scale)
	{
		StringBuffer sb = new StringBuffer();
		
		//判断是否存在个位数
		if (len < scale)
			len = scale + 1;
		
		//控制整数部分的输出
		for (int i = 0; i < len - scale - 1; i++)
			sb.append("#");

		//判断是否存在小数，scale为0则作整数处理，scale>0，则补充小数点
		if (scale == 0)
			sb.append("0");
		else
		{
			sb.append("0.");
			for (int i = 0; i < scale; i++)
				sb.append("0");
		}

		return sb.toString();
	}
    
	/**
	 * 功能 :
	 * 
	 * @param value
	 * @return
	 */
	private static StringBuffer replaceXML(StringBuffer value)
	{
		for (int k = 0, len=value.length(); k < len; k++)
		{//value.length()
			switch (value.charAt(k))
			{
			case '&':
				value.replace(k, k + 1, "&amp;");
				len  = value.length();
				break;
			case '<':
				value.replace(k, k + 1, "&lt;");
				len  = value.length();
				break;
			case '>':
				value.replace(k, k + 1, "&gt;");
				len  = value.length();
				break;
			case '\'':
				value.replace(k, k + 1, "&apos;");
				len  = value.length();
				break;
			case '\"':
				value.replace(k, k + 1, "&quot;");
				len  = value.length();
				break;
			case '\\':// 遇到\则判断是否是\\0x，如果是则插入\进行特殊处理
				if (k + 3 < len && value.charAt(k + 1) == '\\'
						&& value.charAt(k + 2) == '0'
						&& value.charAt(k + 3) == 'x')
				{
					value.insert(k + 4, '\\');
					k = k + 4;
					len = value.length();
				}
				break;
			default:
				char c = value.charAt(k);
				if (c < 32 && c != '\n' && c != '\r' && c != '\t') {
					String str = Integer.toHexString(value.charAt(k));
					if (str.length() == 2)
						str = "\\\\0x" + str;
					else if (str.length() == 1)
						str = "\\\\0x0" + str;
					value.replace(k, k + 1, str);
					k=k+5;
					len = value.length();
				}
				break;
			}
		}
		return value;
	}
    

    

	
}