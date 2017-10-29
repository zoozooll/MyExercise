package com.dcfs.esb.client.converter;

import com.dc.eai.conv.InputPacket;
import com.dc.eai.conv.OutputPacket;
import com.dc.eai.data.CompositeData;

public class PackUtil
{
	private static StandardConverter standardConverter = new StandardConverter();
	
	/**
	 * 功能 : 组包
	 * @param request
	 * @return
	 */
	public static byte[] pack(CompositeData request)
	{
		OutputPacket outputPacket = new OutputPacket();
		standardConverter.pack(outputPacket,request,null);
		return outputPacket.getBuff();
	}
	
	/**
	 * 
	 * 功能 : 根据String组包
	 * @param request
	 * @return
	 */
	public static String packXmlStr(CompositeData request)
	{
		return standardConverter.packXmlStr(request);
	}
	
	/**
	 * 功能 : 拆包
	 * @param request
	 * @return
	 */
	public static CompositeData unpack(byte[] request)
	{
		CompositeData response = new CompositeData();
		standardConverter.unpack(new InputPacket(request),response,null);
		return response;
	}
	
	/**
	 * 功能 : 根据String拆包
	 * @param request
	 * @return
	 */
	public static CompositeData unpackXmlStr(String request)
	{	
		CompositeData response = new CompositeData();
		standardConverter.unpackXmlStr(request, response);
		return response;
	}

}
