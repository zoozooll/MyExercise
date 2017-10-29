package com.dcfs.esb.client.converter;

import java.io.StringReader;

import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.dc.eai.config.IOConfig;
import com.dc.eai.conv.InputPacket;
import com.dc.eai.conv.OutputPacket;
import com.dc.eai.conv.PackageConverter;
import com.dc.eai.data.CompositeData;
import com.dcfs.esb.client.config.Config;

/**
 * 
 * 鏍囧噯鎷嗗寘缁勫寘妯″紡
 * 
 * @author ex-wanghuaxi
 * 
 * 2008-10-08
 */
public class StandardConverter implements PackageConverter
{
	private static Log log = LogFactory.getLog(StandardConverter.class);

	public static final String ROOT = "service";
	public static final String SYS_HEAD = "SYS_HEAD";
	public static final String sys_header = "sys-header";
	public static final String BODY = "BODY";
	public static final String body = "body";
	public static final String DATA = "data";
	public static final String FIELD = "field";
	public static final String ARRAY = "array";
	public static final String STRUCT = "struct";
	private static boolean xmlOptimize = false;
	private static boolean spaceTrim = false;
	private static SAXParserFactory factory = null;
	private static StandardCdToXml cdToXml = null;

	static
	{
		try
		{
			init();
		} catch (Exception e)
		{
			log.error("鎷嗙粍鍖呭垵濮嬪寲鍑洪敊", e);
		}
	}

	/**
	 * 鍔熻兘 :鏍囧噯鎷嗙粍鍖呭垵濮嬪寲
	 */
	public static void init()
	{
		if(log.isInfoEnabled())
			log.info("鏍囧噯鎷嗙粍鍖呭垵濮嬪寲寮�");
		factory = SAXParserFactory.newInstance();
		factory.setValidating(false);
		cdToXml = new StandardCdToXml();
		setConvertFlag();
		if(log.isInfoEnabled())
			log.info("鏍囧噯鎷嗙粍鍖呭垵濮嬪寲鎴愬姛");
	}

	/**
	 * 鍔熻兘 :妫�煡鎷嗙粍鍖呯浉鍏崇殑閰嶇疆淇℃伅
	 */
	private static void setConvertFlag()
	{
		String xmlFlag = Config.getLogProperty(Config.XML_OPTIMIZE_FLAG);
		String trimFlag = Config.getLogProperty(Config.SPACE_TRIM_FLAG);
		if (trimFlag != null)
			StandardConverter.setSpaceTrim("true".equalsIgnoreCase(trimFlag));
		if (xmlFlag != null)
			StandardConverter.setXmlOptimize("true".equalsIgnoreCase(xmlFlag));

		if (log.isInfoEnabled())
			log.info(new StringBuffer("spaceTrim=").append(spaceTrim).append(
					",setXmlOptimize=").append(xmlOptimize));
	}

	/**
	 * 鍔熻兘: 缁勫寘
	 */
	public void pack(OutputPacket packet, CompositeData data, IOConfig iocfg)
	{
		// 鎵ц缁勫寘
		String xmlStr;
		byte[] xmlData = null;
		try
		{
			xmlStr = cdToXml.convert(data);
			xmlData = xmlStr.getBytes("UTF-8");
			int length = xmlData.length;
			packet.ensure(length);
			System.arraycopy(xmlData, 0, packet.getBuff(), packet.getOffset(),
					length);
			packet.advance(length);
		} catch (Exception e)
		{
//			log.error("缁勫寘鍑洪敊锛� + e, e);
		}
	}

	public String packXmlStr(CompositeData data)
	{
		String xmlStr = null;

		try
		{
			xmlStr = cdToXml.convert(data);

		} catch (Exception e)
		{
//			log.error("缁勫寘鍑洪敊锛� + e, e);
		}
		return xmlStr;
	}

	/**
	 * 鍔熻兘锛氭媶鍖�
	 */
	public void unpack(InputPacket packet, CompositeData data, IOConfig iocfg)
	{
		XMLReader parser = null;
		try
		{

			parser = factory.newSAXParser().getXMLReader();

			StandardContentHandler handler = new StandardContentHandler(data);
			parser.setContentHandler(handler);

			String inString = new String(packet.getBuff(), "UTF-8").trim();
			parser.parse(new InputSource(new StringReader(inString)));
		} catch (Exception e)
		{
//			log.error("鎷嗗寘鍑洪敊锛� + e, e);
		}
	}

	public void unpackXmlStr(String xmlStr, CompositeData data)
	{
		XMLReader parser = null;
		try
		{
			parser = factory.newSAXParser().getXMLReader();
			StandardContentHandler handler = new StandardContentHandler(data);
			parser.setContentHandler(handler);
			parser.parse(new InputSource(new StringReader(xmlStr)));
		} catch (Exception e)
		{
//			log.error("鎷嗗寘鍑洪敊锛� + e, e);
		}
	}

	public static boolean isXmlOptimize()
	{
		return xmlOptimize;
	}

	public static void setXmlOptimize(boolean xmlOptimize)
	{
		StandardConverter.xmlOptimize = xmlOptimize;
	}

	public static boolean isSpaceTrim()
	{
		return spaceTrim;
	}

	public static void setSpaceTrim(boolean spaceTrim)
	{
		StandardConverter.spaceTrim = spaceTrim;
	}
}