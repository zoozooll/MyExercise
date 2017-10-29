package com.dcfs.esb.client;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dc.eai.data.CompositeData;
import com.dcfs.esb.client.config.Config;
import com.dcfs.esb.client.connector.HTTPClientConnector;
import com.dcfs.esb.client.connector.HTTPClientConnectorCd;
import com.dcfs.esb.client.connector.HTTPClientConnectorNew;
import com.dcfs.esb.client.converter.PackUtil;
import com.dcfs.esb.client.exception.TimeoutException;
import com.iskinfor.servicedata.CommArgs;


public class ESBClient
{

	private static Log log = LogFactory.getLog(ESBClient.class);
	private static boolean intFlag = false;
	public static final String URL = "url";
	public static final String URL1 = "url1";
	public static final String URL2 = "url2";
	public static final String URL3 = "url3";
	public static final String URL4 = "url4";
	public static final String URL5 = "url5";
	public static final String URL6 = "url6";
	public static final String URL7 = "url7";
	public static final String URL8 = "url8";
	public static final String URL9 = "url9";
	public static final String URL01 = "url01";
	public static final String URL02 = "url02";
	public static final String URL03 = "url03";
	public static final String URL04 = "url04";
	public static final String URL05 = "url05";
	public static final String URL06 = "url06";
	public static final String URL07 = "url07";
	public static final String URL08 = "url08";
	public static final String URL09 = "url09";
	
	
	/**
	 * 功能：通过CD对象请求ESB的交易，返回响应的CD对象
	 * 
	 * @param request
	 * @return
	 * @throws TimeoutException
	 */
	public static CompositeData request1(CompositeData request)
			throws TimeoutException
	{
		CompositeData response = null;
		try
		{
			String url1 = Config.getLogProperty(URL1);
			HTTPClientConnector.setHttpUrl(url1);
			response = doComm1(request);

		} catch (Exception e)
		{
			log.error(e.getMessage(), e);
			throw new TimeoutException(e.getMessage(), e);
		}
		return response;
	}
	
	public static CompositeData request2(CompositeData request)
			throws TimeoutException
	{
		CompositeData response = null;
		try
		{
			String url2 = Config.getLogProperty(URL2);
			HTTPClientConnector.setHttpUrl(url2);
			response = doComm1(request);

		} catch (Exception e)
		{
			log.error(e.getMessage(), e);
			throw new TimeoutException(e.getMessage(), e);
		}
		return response;
	}
	
	public static CompositeData request3(CompositeData request)
			throws TimeoutException
	{
		CompositeData response = null;
		try
		{
			String url3 = Config.getLogProperty(URL3);
			HTTPClientConnector.setHttpUrl(url3);
			response = doComm1(request);

		} catch (Exception e)
		{
			log.error(e.getMessage(), e);
			throw new TimeoutException(e.getMessage(), e);
		}
		return response;
	}
	
	public static CompositeData request4(CompositeData request)
			throws TimeoutException
	{
		CompositeData response = null;
		try
		{
			String url4 = Config.getLogProperty(URL4);
			HTTPClientConnector.setHttpUrl(url4);
			response = doComm1(request);

		} catch (Exception e)
		{
			log.error(e.getMessage(), e);
			throw new TimeoutException(e.getMessage(), e);
		}
		return response;
	}
	
	public static CompositeData request5(CompositeData request)
			throws TimeoutException
	{
		CompositeData response = null;
		try
		{
			String url5 = Config.getLogProperty(URL5);
			HTTPClientConnector.setHttpUrl(url5);
			response = doComm1(request);

		} catch (Exception e)
		{
			log.error(e.getMessage(), e);
			throw new TimeoutException(e.getMessage(), e);
		}
		return response;
	}
	
	public static CompositeData request6(CompositeData request)
			throws TimeoutException
	{
		CompositeData response = null;
		try
		{
			String url6 = Config.getLogProperty(URL6);
			HTTPClientConnector.setHttpUrl(url6);
			response = doComm1(request);

		} catch (Exception e)
		{
			log.error(e.getMessage(), e);
			throw new TimeoutException(e.getMessage(), e);
		}
		return response;
	}
	
	public static CompositeData request7(CompositeData request)
			throws TimeoutException
	{
		CompositeData response = null;
		try
		{
			String url7 = Config.getLogProperty(URL7);
			HTTPClientConnector.setHttpUrl(url7);
			response = doComm1(request);

		} catch (Exception e)
		{
			log.error(e.getMessage(), e);
			throw new TimeoutException(e.getMessage(), e);
		}
		return response;
	}
	
	public static CompositeData request8(CompositeData request)
			throws TimeoutException
	{
		CompositeData response = null;
		try
		{
			String url8 = Config.getLogProperty(URL8);
			HTTPClientConnector.setHttpUrl(url8);
			response = doComm1(request);

		} catch (Exception e)
		{
			log.error(e.getMessage(), e);
			throw new TimeoutException(e.getMessage(), e);
		}
		return response;
	}
	
	public static CompositeData request9(CompositeData request)
			throws TimeoutException
	{
		CompositeData response = null;
		try
		{
			String url9 = Config.getLogProperty(URL9);
			HTTPClientConnector.setHttpUrl(url9);
			response = doComm1(request);

		} catch (Exception e)
		{
			log.error(e.getMessage(), e);
			throw new TimeoutException(e.getMessage(), e);
		}
		return response;
	}
	
	/**
	 * 功能：通过CD对象请求ESB的交易，返回响应的CD对象
	 * 
	 * @param request
	 * @return
	 * @throws TimeoutException
	 */
	public static CompositeData request(CompositeData request)
			throws TimeoutException
	{
		CompositeData response = null;
		byte[] rspBytes = null;
		byte[] reqBytes = null;
		try
		{
			String url = Config.getLogProperty(URL);
			HTTPClientConnector.setHttpUrl(url);
           
			// 组包
			String reqStr = PackUtil.packXmlStr(request);

			// 组装信封头信息
			String mailer = "";;

			// 将String转换成请求byte[]，后将请求String对象置空
			reqBytes = reqStr.getBytes("UTF-8");
			reqStr = null;

			// 与ESB进行通讯，返回byte[]，后将请求byte[]对象置空
			rspBytes = doComm(mailer, reqBytes);
			// rspBytes = reqBytes;
			reqBytes = null;
			mailer = null;

			// 将响应byte[]转换成响应String，后将响应byte[]对象置空
			String rspStr = new String(rspBytes, "UTF-8").trim();
			rspBytes = null;

			// 拆包，后将rspStr对象置空
			response = PackUtil.unpackXmlStr(rspStr);
			rspStr = null;

		} catch (Exception e)
		{
			log.error(e.getMessage(), e);
			throw new TimeoutException(e.getMessage(), e);
		}

		// 返回响应数据
		return response;
	}
	
	public static CompositeData request01(CompositeData request)
			throws TimeoutException
	{
		CompositeData response = null;
		byte[] rspBytes = null;
		byte[] reqBytes = null;
		try
		{
//			String url01 = Config.getLogProperty();
			HTTPClientConnector.setHttpUrl(CommArgs.study_url);
           
			// 组包
			String reqStr = PackUtil.packXmlStr(request);

			// 组装信封头信息
			String mailer = "";;

			// 将String转换成请求byte[]，后将请求String对象置空
			reqBytes = reqStr.getBytes("UTF-8");
			reqStr = null;

			// 与ESB进行通讯，返回byte[]，后将请求byte[]对象置空
			rspBytes = doComm(mailer, reqBytes);
			// rspBytes = reqBytes;
			reqBytes = null;
			mailer = null;

			// 将响应byte[]转换成响应String，后将响应byte[]对象置空
			String rspStr = new String(rspBytes, "UTF-8").trim();
			rspBytes = null;

			// 拆包，后将rspStr对象置空
			response = PackUtil.unpackXmlStr(rspStr);
			rspStr = null;
		} catch (Exception e)
		{
			log.error(e.getMessage(), e);
			throw new TimeoutException(e.getMessage(), e);
		}

		// 返回响应数据
		return response;
	}
	
	public static CompositeData request02(CompositeData request)
			throws TimeoutException
	{
		CompositeData response = null;
		byte[] rspBytes = null;
		byte[] reqBytes = null;
		try
		{
//			String url02 = Config.getLogProperty(URL02);
			HTTPClientConnector.setHttpUrl(CommArgs.book_url);
           
			// 组包
			String reqStr = PackUtil.packXmlStr(request);

			// 组装信封头信息
			String mailer = "";;

			// 将String转换成请求byte[]，后将请求String对象置空
			reqBytes = reqStr.getBytes("UTF-8");
			reqStr = null;

			// 与ESB进行通讯，返回byte[]，后将请求byte[]对象置空
			rspBytes = doComm(mailer, reqBytes);
			// rspBytes = reqBytes;
			reqBytes = null;
			mailer = null;

			// 将响应byte[]转换成响应String，后将响应byte[]对象置空
			String rspStr = new String(rspBytes, "UTF-8").trim();
			rspBytes = null;

			// 拆包，后将rspStr对象置空
			response = PackUtil.unpackXmlStr(rspStr);
			rspStr = null;
		} catch (Exception e)
		{
			log.error(e.getMessage(), e);
			throw new TimeoutException(e.getMessage(), e);
		}

		// 返回响应数据
		return response;
	}
	
	public static CompositeData request03(CompositeData request)
			throws TimeoutException
	{
		CompositeData response = null;
		byte[] rspBytes = null;
		byte[] reqBytes = null;
		try
		{
//			String url03 = Config.getLogProperty(URL03);
			HTTPClientConnector.setHttpUrl(CommArgs.user_url);
           
			// 组包
			String reqStr = PackUtil.packXmlStr(request);
			// 组装信封头信息
			String mailer = "";;

			// 将String转换成请求byte[]，后将请求String对象置空
			reqBytes = reqStr.getBytes("UTF-8");
			reqStr = null;

			// 与ESB进行通讯，返回byte[]，后将请求byte[]对象置空
			rspBytes = doComm(mailer, reqBytes);
			// rspBytes = reqBytes;
			reqBytes = null;
			mailer = null;

			// 将响应byte[]转换成响应String，后将响应byte[]对象置空
			String rspStr = new String(rspBytes, "UTF-8").trim();
			rspBytes = null;

			// 拆包，后将rspStr对象置空
			response = PackUtil.unpackXmlStr(rspStr);
			rspStr = null;
		} catch (Exception e)
		{
			log.error(e.getMessage(), e);
			throw new TimeoutException(e.getMessage(), e);
		}

		// 返回响应数据
		return response;
	}
	
	public static CompositeData request04(CompositeData request)
			throws TimeoutException
	{
		CompositeData response = null;
		byte[] rspBytes = null;
		byte[] reqBytes = null;
		try
		{
			String url04 = Config.getLogProperty(URL04);
			HTTPClientConnector.setHttpUrl(url04);
           
			// 组包
			String reqStr = PackUtil.packXmlStr(request);

			// 组装信封头信息
			String mailer = "";;

			// 将String转换成请求byte[]，后将请求String对象置空
			reqBytes = reqStr.getBytes("UTF-8");
			reqStr = null;

			// 与ESB进行通讯，返回byte[]，后将请求byte[]对象置空
			rspBytes = doComm(mailer, reqBytes);
			// rspBytes = reqBytes;
			reqBytes = null;
			mailer = null;

			// 将响应byte[]转换成响应String，后将响应byte[]对象置空
			String rspStr = new String(rspBytes, "UTF-8").trim();
			rspBytes = null;

			// 拆包，后将rspStr对象置空
			response = PackUtil.unpackXmlStr(rspStr);
			rspStr = null;
		} catch (Exception e)
		{
			log.error(e.getMessage(), e);
			throw new TimeoutException(e.getMessage(), e);
		}

		// 返回响应数据
		return response;
	}
	
	public static CompositeData request05(CompositeData request)
			throws TimeoutException
	{
		CompositeData response = null;
		byte[] rspBytes = null;
		byte[] reqBytes = null;
		try
		{
			String url05 = Config.getLogProperty(URL05);
			HTTPClientConnector.setHttpUrl(url05);
           
			// 组包
			String reqStr = PackUtil.packXmlStr(request);

			// 组装信封头信息
			String mailer = "";;

			// 将String转换成请求byte[]，后将请求String对象置空
			reqBytes = reqStr.getBytes("UTF-8");
			reqStr = null;

			// 与ESB进行通讯，返回byte[]，后将请求byte[]对象置空
			rspBytes = doComm(mailer, reqBytes);
			// rspBytes = reqBytes;
			reqBytes = null;
			mailer = null;

			// 将响应byte[]转换成响应String，后将响应byte[]对象置空
			String rspStr = new String(rspBytes, "UTF-8").trim();
			rspBytes = null;

			// 拆包，后将rspStr对象置空
			response = PackUtil.unpackXmlStr(rspStr);
			rspStr = null;

		} catch (Exception e)
		{
			log.error(e.getMessage(), e);
			throw new TimeoutException(e.getMessage(), e);
		}

		// 返回响应数据
		return response;
	}
	
	public static CompositeData request06(CompositeData request)
			throws TimeoutException
	{
		CompositeData response = null;
		byte[] rspBytes = null;
		byte[] reqBytes = null;
		try
		{
			String url06 = Config.getLogProperty(URL06);
			HTTPClientConnector.setHttpUrl(url06);
           
			// 组包
			String reqStr = PackUtil.packXmlStr(request);

			// 组装信封头信息
			String mailer = "";;

			// 将String转换成请求byte[]，后将请求String对象置空
			reqBytes = reqStr.getBytes("UTF-8");
			reqStr = null;

			// 与ESB进行通讯，返回byte[]，后将请求byte[]对象置空
			rspBytes = doComm(mailer, reqBytes);
			// rspBytes = reqBytes;
			reqBytes = null;
			mailer = null;

			// 将响应byte[]转换成响应String，后将响应byte[]对象置空
			String rspStr = new String(rspBytes, "UTF-8").trim();
			rspBytes = null;

			// 拆包，后将rspStr对象置空
			response = PackUtil.unpackXmlStr(rspStr);
			rspStr = null;
		} catch (Exception e)
		{
			log.error(e.getMessage(), e);
			throw new TimeoutException(e.getMessage(), e);
		}

		// 返回响应数据
		return response;
	}
	
	public static CompositeData request07(CompositeData request)
			throws TimeoutException
	{
		CompositeData response = null;
		byte[] rspBytes = null;
		byte[] reqBytes = null;
		try
		{
			String url07 = Config.getLogProperty(URL07);
			HTTPClientConnector.setHttpUrl(url07);
           
			// 组包
			String reqStr = PackUtil.packXmlStr(request);

			// 组装信封头信息
			String mailer = "";;

			// 将String转换成请求byte[]，后将请求String对象置空
			reqBytes = reqStr.getBytes("UTF-8");
			reqStr = null;

			// 与ESB进行通讯，返回byte[]，后将请求byte[]对象置空
			rspBytes = doComm(mailer, reqBytes);
			// rspBytes = reqBytes;
			reqBytes = null;
			mailer = null;

			// 将响应byte[]转换成响应String，后将响应byte[]对象置空
			String rspStr = new String(rspBytes, "UTF-8").trim();
			rspBytes = null;

			// 拆包，后将rspStr对象置空
			response = PackUtil.unpackXmlStr(rspStr);
			rspStr = null;
		} catch (Exception e)
		{
			log.error(e.getMessage(), e);
			throw new TimeoutException(e.getMessage(), e);
		}

		// 返回响应数据
		return response;
	}
	
	public static CompositeData request08(CompositeData request)
			throws TimeoutException
	{
		CompositeData response = null;
		byte[] rspBytes = null;
		byte[] reqBytes = null;
		try
		{
			String url08 = Config.getLogProperty(URL08);
			HTTPClientConnector.setHttpUrl(url08);
           
			// 组包
			String reqStr = PackUtil.packXmlStr(request);

			// 组装信封头信息
			String mailer = "";;

			// 将String转换成请求byte[]，后将请求String对象置空
			reqBytes = reqStr.getBytes("UTF-8");
			reqStr = null;

			// 与ESB进行通讯，返回byte[]，后将请求byte[]对象置空
			rspBytes = doComm(mailer, reqBytes);
			// rspBytes = reqBytes;
			reqBytes = null;
			mailer = null;

			// 将响应byte[]转换成响应String，后将响应byte[]对象置空
			String rspStr = new String(rspBytes, "UTF-8").trim();
			rspBytes = null;

			// 拆包，后将rspStr对象置空
			response = PackUtil.unpackXmlStr(rspStr);
			rspStr = null;

		} catch (Exception e)
		{
			log.error(e.getMessage(), e);
			throw new TimeoutException(e.getMessage(), e);
		}

		// 返回响应数据
		return response;
	}
	
	public static CompositeData request09(CompositeData request)
			throws TimeoutException
	{
		CompositeData response = null;
		byte[] rspBytes = null;
		byte[] reqBytes = null;
		try
		{

			String url09 = Config.getLogProperty(URL09);
			HTTPClientConnector.setHttpUrl(url09);
           
			// 组包
			String reqStr = PackUtil.packXmlStr(request);

			// 组装信封头信息
			String mailer = "";;

			// 将String转换成请求byte[]，后将请求String对象置空
			reqBytes = reqStr.getBytes("UTF-8");
			reqStr = null;

			// 与ESB进行通讯，返回byte[]，后将请求byte[]对象置空
			rspBytes = doComm(mailer, reqBytes);
			// rspBytes = reqBytes;
			reqBytes = null;
			mailer = null;

			// 将响应byte[]转换成响应String，后将响应byte[]对象置空
			String rspStr = new String(rspBytes, "UTF-8").trim();
			rspBytes = null;

			// 拆包，后将rspStr对象置空
			response = PackUtil.unpackXmlStr(rspStr);
			rspStr = null;

		} catch (Exception e)
		{
			log.error(e.getMessage(), e);
			throw new TimeoutException(e.getMessage(), e);
		}

		// 返回响应数据
		return response;
	}
	
	/**
	 * 功能：通过XML报文请求ESB的数据，返回XML报文
	 * 
	 * @param request
	 * @return
	 * @throws TimeoutException
	 */
	public static byte[] request(byte[] request) throws TimeoutException
	{
		byte[] rspByte = null;
		try
		{
			
			String url = Config.getLogProperty(URL);
			HTTPClientConnector.setHttpUrl(url);
			// 组装信封头信息
			String mailer = "";;

			// 发送信封头和byte数组，并获得响应byte数组
			rspByte = doComm(mailer, request);
		} catch (Exception e)
		{
			log.error(e.getMessage(), e);
			throw new TimeoutException(e.getMessage(), e);
		}
		return rspByte;
	}
	
	public static byte[] request01(byte[] request) throws TimeoutException
	{
		byte[] rspByte = null;
		try
		{
			String url01 = Config.getLogProperty(URL01);
			HTTPClientConnector.setHttpUrl(url01);
			// 组装信封头信息
			String mailer = "";;

			// 发送信封头和byte数组，并获得响应byte数组
			rspByte = doComm(mailer, request);

		} catch (Exception e)
		{
			log.error(e.getMessage(), e);
			throw new TimeoutException(e.getMessage(), e);
		}
		return rspByte;
	}

	public static byte[] request02(byte[] request) throws TimeoutException
	{
		byte[] rspByte = null;
		try
		{
			String url02 = Config.getLogProperty(URL02);
			HTTPClientConnector.setHttpUrl(url02);
			// 组装信封头信息
			String mailer = "";;

			// 发送信封头和byte数组，并获得响应byte数组
			rspByte = doComm(mailer, request);

		} catch (Exception e)
		{
			log.error(e.getMessage(), e);
			throw new TimeoutException(e.getMessage(), e);
		}
		return rspByte;
	}
	
	public static byte[] request03(byte[] request) throws TimeoutException
	{
		byte[] rspByte = null;
		try
		{
			String url03 = Config.getLogProperty(URL03);
			HTTPClientConnector.setHttpUrl(url03);
			// 组装信封头信息
			String mailer = "";;

			// 发送信封头和byte数组，并获得响应byte数组
			rspByte = doComm(mailer, request);

		} catch (Exception e)
		{
			log.error(e.getMessage(), e);
			throw new TimeoutException(e.getMessage(), e);
		}
		return rspByte;
	}
	
	public static byte[] request04(byte[] request) throws TimeoutException
	{
		byte[] rspByte = null;
		try
		{
			
			String url04 = Config.getLogProperty(URL04);
			HTTPClientConnector.setHttpUrl(url04);
			// 组装信封头信息
			String mailer = "";;

			// 发送信封头和byte数组，并获得响应byte数组
			rspByte = doComm(mailer, request);

		} catch (Exception e)
		{
			log.error(e.getMessage(), e);
			throw new TimeoutException(e.getMessage(), e);
		}
		return rspByte;
	}
	
	public static byte[] request05(byte[] request) throws TimeoutException
	{
		byte[] rspByte = null;
		try
		{
			String url05 = Config.getLogProperty(URL05);
			HTTPClientConnector.setHttpUrl(url05);
			// 组装信封头信息
			String mailer = "";;

			// 发送信封头和byte数组，并获得响应byte数组
			rspByte = doComm(mailer, request);
		} catch (Exception e)
		{
			log.error(e.getMessage(), e);
			throw new TimeoutException(e.getMessage(), e);
		}
		return rspByte;
	}
	
	public static byte[] request06(byte[] request) throws TimeoutException
	{
		byte[] rspByte = null;
		try
		{
			String url06 = Config.getLogProperty(URL06);
			HTTPClientConnector.setHttpUrl(url06);
			// 组装信封头信息
			String mailer = "";;

			// 发送信封头和byte数组，并获得响应byte数组
			rspByte = doComm(mailer, request);

		} catch (Exception e)
		{
			log.error(e.getMessage(), e);
			throw new TimeoutException(e.getMessage(), e);
		}
		return rspByte;
	}
	
	public static byte[] request07(byte[] request) throws TimeoutException
	{
		byte[] rspByte = null;
		try
		{
			String url07 = Config.getLogProperty(URL07);
			HTTPClientConnector.setHttpUrl(url07);
			// 组装信封头信息
			String mailer = "";;

			// 发送信封头和byte数组，并获得响应byte数组
			rspByte = doComm(mailer, request);
		} catch (Exception e)
		{
			log.error(e.getMessage(), e);
			throw new TimeoutException(e.getMessage(), e);
		}
		return rspByte;
	}
	
	
	public static byte[] request08(byte[] request) throws TimeoutException
	{
		byte[] rspByte = null;
		try
		{
			String url08 = Config.getLogProperty(URL08);
			HTTPClientConnector.setHttpUrl(url08);
			// 组装信封头信息
			String mailer = "";;

			// 发送信封头和byte数组，并获得响应byte数组
			rspByte = doComm(mailer, request);
		} catch (Exception e)
		{
			log.error(e.getMessage(), e);
			throw new TimeoutException(e.getMessage(), e);
		}
		return rspByte;
	}
	
	public static byte[] request09(byte[] request) throws TimeoutException
	{
		byte[] rspByte = null;
		try
		{
			String url09 = Config.getLogProperty(URL09);
			HTTPClientConnector.setHttpUrl(url09);
			// 组装信封头信息
			String mailer = "";;

			// 发送信封头和byte数组，并获得响应byte数组
			rspByte = doComm(mailer, request);
		} catch (Exception e)
		{
			log.error(e.getMessage(), e);
			throw new TimeoutException(e.getMessage(), e);
		}
		return rspByte;
	}
	
	
	/**
	 * 功能 : 直接上送CompositeData,不转换成XML
	 * 
	 * @param mailer
	 * @param request
	 * @return
	 */
	private static CompositeData doComm1(CompositeData request) throws Exception
	{
			return (new HTTPClientConnectorCd()).doComm(request);
		
	}
	
	/**
	 * 功能 : 与ESB进行通讯
	 * 
	 * @param mailer
	 * @param request
	 * @return
	 */
	private static byte[] doComm(String mailer, byte[] request)
			throws Exception
	{
		if (Config.isUseMasterKey())
			return (new HTTPClientConnectorNew()).doComm(mailer, request);
		else
			return (new HTTPClientConnector()).doComm(mailer, request);
	}

	/**
	 * 功能：初始化日志属性
	 * 
	 * @param prop
	 */
	public static void init(Properties prop)
	{
		if (intFlag == true)
			return;
		Config.load(prop);
		intFlag = true;
	}

	/**
	 * 功能：初始化日志属性
	 * 
	 * @param prop
	 */
	public static void reInit(Properties prop)
	{
		Config.load(prop);
		intFlag = true;
	}

}