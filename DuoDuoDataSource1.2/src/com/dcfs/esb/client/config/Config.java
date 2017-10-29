package com.dcfs.esb.client.config;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Properties;

import com.dcfs.esb.client.connector.HTTPClientConnectorNew;
import com.dcfs.esb.client.connector.SecurityUtil;

public class Config
{
//	private static Log log = LogFactory.getLog(Config.class);

	private static Properties properties = null;
	private static URL cfgFileUrl = null;
	private static final String FILEPATH = "paesb.client.config";
	private static final String ESB_CLIENT_PROP = "esb-client.properties";

	// 璇锋眰ESB鐨刄RL
	public static final String URL = "url";

	// 璇锋眰ESB鐨勭敤鎴峰悕
	public static final String UID = "uid";

	/**
	 * masterKey锛氶�杩�DES瀹夊叏楠岃瘉
	 */
	public static final String MASTERKEY = "masterKey";

	/**
	 * password锛氶�杩囩敤鎴峰悕鍜屽瘑鐮佹柟寮忓姞瀵�
	 */
	public static final String PASSWORD = "password";

	/**
	 * xmlOptimizeFlag锛氭槸鍚﹁繘琛孹ML鐨勫帇缂�
	 */
	public static final String XML_OPTIMIZE_FLAG = "xmlOptimizeFlag";

	/**
	 * spaceTrimFlag锛氭媶缁勫寘鏄惁杩涜TRIM鎿嶄綔
	 */
	public static final String SPACE_TRIM_FLAG = "spaceTrimFlag";

	/**
	 * connTimeout锛氬缓绔婬TTP杩炴帴鐨勮秴鏃舵椂闂�
	 */
	public static final String CONN_TIME_OUT = "connTimeout";

	/**
	 * readTimeout锛氱瓑寰呮暟鎹搷搴旂殑瓒呮椂鏃堕棿
	 */
	public static final String READ_TIME_OUT = "readTimeout";

	/**
	 * 璁よ瘉鏂瑰紡锛�P锛氶�杩噓id鍜宲assword璁よ瘉锛堥粯璁ゅ�锛�M锛氶�杩�DES鍜孧D5璁よ瘉
	 */
	private static char authFlag = 'P';

	/**
	 * 3DES鍔犲瘑鐨勬湰鍦板瘑閽�
	 */
	private static final byte[] DES_KEY = "bank2009besb1027security".getBytes();

	/**
	 * 瑙ｇ爜鍚庣殑MasterKey
	 */
	private static byte[] scrtKey = null;
	
	static
	{
		init();
	}

	/**
	 * 
	 * 鍔熻兘 :鍒濆鍖栨搷浣滐紝鍔犺浇閰嶇疆淇℃伅锛屽垽鏂厤缃俊鎭槸鍚﹀瓨鍦�
	 */
	public static void init()
	{
		try
		{
			// 鍔犺浇閰嶇疆淇℃伅
			load();
			// 妫�煡閰嶇疆淇℃伅
			checkConfig();
			// 璁剧疆Http鐩稿叧鍙傛暟淇℃伅
			setHttConnParm();
		} catch (Exception e)
		{
//			log.error("鍒濆閰嶇疆淇℃伅寮傚父", e);
		}
	}
	public static void refresh()
	{
		init();
	}

	/**
	 * 鍔熻兘 :妫�煡閰嶇疆淇℃伅
	 */
	private static void checkConfig()
	{
		// 浠庨厤缃枃浠朵腑鎻愬彇鐩稿叧閰嶇疆淇℃伅
		String uid = getLogProperty(UID);
		String password = getLogProperty(PASSWORD);
		String masterkey = getLogProperty(MASTERKEY);
		String url = getLogProperty(URL);

		// 鍒ゆ柇UID鏄惁瀛樺湪
		if (uid == null || "".equals(uid.trim()))
//			log.error("铏氭嫙鐢ㄦ埛UID涓嶈兘涓虹┖");

		// 鍒ゆ柇URL鏄惁瀛樺湪
		if (url == null || "".equals(url.trim()))
//			log.error("璇锋眰ESB鐨刄RL涓嶈兘涓虹┖");

		// 鍒ゆ柇PASSWORD鍜孧ASTERKEY鏄惁瀛�
		if ((password == null || "".equals(password))
				&& (masterkey == null || "".equals(masterkey)))
		{
//			log.error("password鍜宮asterKey涓嶈兘鍚屾椂涓虹┖");
		}

		// 鏍规嵁閰嶇疆淇℃伅纭璁よ瘉鐨勬柟寮�
		if (masterkey != null && !"".equals(masterkey))
		{
			authFlag = 'M';
			check3DES(MASTERKEY);
		} else
		{
			authFlag = 'P';
			check3DES(PASSWORD);
		}

	}

	/**
	 * 鍔熻兘 : 妫�煡鏂囦欢涓殑瀵嗙爜鍩熸槸鍚﹀凡缁忓姞瀵�
	 * 
	 * @param key
	 */
	private static void check3DES(String key)
	{
		byte[] temp = null;
		String value = getLogProperty(key);

		// 鍊间笉瀛樺湪锛岀洿鎺ヨ繑鍥�
		if (value == null)
			return;

		// 濡傛灉key瀵瑰簲鐨勫�涓嶄互${3DES}寮�ご鐨勶紝鍒欎俊鎭渶瑕佸姞瀵嗗悗淇濆瓨鍒伴厤缃枃浠朵腑
		if (value != null && !value.startsWith("${3DES}"))
		{/*
			// 閫氳繃3DES鍔犲瘑
			temp = SecurityUtil.encrypt3DES(value.getBytes(), DES_KEY);
			// 缁忚繃BASE64缂栫爜鍚庡甫涓婂姞瀵嗗墠缂�
			value = "${3DES}" + SecurityUtil.encodeBase64(temp);
			// 璁剧疆鍥濸roperties涓�
			setLogProperty(key, value);
			// 淇濆瓨鍥為厤缃枃浠朵腑
			saveCfgFile();
		*/}

		// 鑾峰彇瀵嗛挜鐨刡ase64缂栫爜
		String baseStr = value.substring(7).trim();
		// base64瑙ｅ瘑锛岃幏鍙朾ytes
		//temp = SecurityUtil.decodeBase64(baseStr);
		// 閫氳繃3DES瑙ｅ瘑锛屽緱鍒発ey鐨勫師濮嬪�
		scrtKey = SecurityUtil.decrypt3DES(temp, DES_KEY);
	}

	private static void setHttConnParm()
	{
		int connTimeout = 0;
		try
		{
			connTimeout = Integer.parseInt(getLogProperty(CONN_TIME_OUT)) * 1000;
		} catch (Exception e)
		{
			connTimeout = 10 * 1000;
		}
		HTTPClientConnectorNew.setConnTimeout(connTimeout);
		
		int readTimeout = 0;
		try {
			readTimeout = Integer.parseInt(getLogProperty(READ_TIME_OUT))*1000;
		} catch(Exception e) {
			readTimeout = 120*1000;
		}
		HTTPClientConnectorNew.setReadTimeout(readTimeout);
		
		/*String url = getLogProperty(URL);
		if(url!=null)
			HTTPClientConnectorNew.setHttpUrl(url);
		else
			log.error("璇诲彇Http鐨刄RL鍑洪敊锛岃纭HTTP鐨刄RL鏃犺");*/
	}

	/**
	 * 鍔熻兘锛氬姞杞介厤缃枃浠�
	 */
	private static void load()
	{
		try
		{
			// 鍒濆閰嶇疆鏂囦欢鐨刄RL涓虹┖
			cfgFileUrl = null;

			// 閫氳繃鐜鍙橀噺鑾峰彇閰嶇疆鏂囦欢
			String fileName = (String) System.getProperties().get(FILEPATH);
			if (fileName != null && !"".equals(fileName))
			{
//				if (log.isInfoEnabled())
//					log.info("閫氳繃鐜鍙橀噺鑾峰彇閰嶇疆淇℃伅");
				File cfgFile = new File(fileName);
				if (cfgFile.exists())
					cfgFileUrl = cfgFile.toURL();
			}

			// 閰嶇疆鏂囦欢涓嶅瓨鍦紝灏濊瘯閫氳繃褰撳墠绫诲姞杞藉櫒鍔犺浇閰嶇疆鏂囦欢
			if (cfgFileUrl == null)
			{
//				if (log.isInfoEnabled())
//					log.info("灏濊瘯閫氳繃褰撳墠绫诲姞杞藉櫒鍔犺浇閰嶇疆淇℃伅");
				cfgFileUrl = Config.class.getResource("/" + ESB_CLIENT_PROP);
			}

			// 閰嶇疆鏂囦欢涓嶅瓨鍦紝灏濊瘯閫氳繃绯荤粺绫诲姞杞藉櫒鍔犺浇閰嶇疆鏂囦欢
			if (cfgFileUrl == null)
			{
//				if (log.isInfoEnabled())
//					log.info("灏濊瘯閫氳繃绯荤粺绫诲姞杞藉櫒鍔犺浇閰嶇疆淇℃伅");
				cfgFileUrl = System.class.getResource(ESB_CLIENT_PROP);
			}

			// 鍔犺浇閰嶇疆淇℃伅
			properties = new Properties();
			if (cfgFileUrl != null)
				properties.load(cfgFileUrl.openStream());
//			else
//				log.error(ESB_CLIENT_PROP + "閰嶇疆鏂囦欢涓嶅瓨鍦紝绯荤粺鍒濆鍖栧け璐ワ紒");

			// 鏃ュ織
//			if (log.isInfoEnabled())
//				log.info("鍔犺浇閰嶇疆淇℃伅鎴愬姛");
		} catch (Exception e)
		{
//			log.error("鍔犺浇閰嶇疆淇℃伅澶辫触", e);
		}

	}

	/**
	 * 鍔熻兘锛氫繚瀛橀厤缃枃浠�
	 */
	public static void saveCfgFile()
	{
		try
		{
			if (cfgFileUrl != null || properties != null)
			{
				String fileName = URLDecoder.decode(cfgFileUrl.getPath(),
						"UTF-8");
				FileOutputStream fos = new FileOutputStream(fileName);
				properties.store(fos, "encrypt masterKey or masterkey");
				fos.close();
//				if (log.isInfoEnabled())
//					log.info("淇濆瓨閰嶇疆鏂囦欢鎴愬姛");
			}
		} catch (Exception e)
		{
//			log.error("灏嗗瘑閽ヤ繚瀛樺埌閰嶇疆鏂囦欢鍑洪敊锛屽瓨鍦ㄥ畨鍏ㄩ殣鎮ｏ紝浣嗕笉褰卞搷浜ゆ槗", e);
		}
	}

	/**
	 * 绋嬪簭璁剧疆鍒濆鍖栨棩蹇楀睘鎬�
	 */
	public static void load(Properties prop)
	{
		// 鍔犺浇prop淇℃伅
		properties = prop;
		// 妫�煡閰嶇疆淇℃伅
		checkConfig();
		// 璁剧疆Http鐩稿叧鍙傛暟淇℃伅
		setHttConnParm();
	}

	/**
	 * 鍔熻兘锛氭牴鎹甼ey鑾峰彇瀵瑰簲鐨勫�
	 * 
	 * @param key
	 * @return
	 */
	public static String getLogProperty(String key)
	{
		String property = properties.getProperty(key);
		return property;
	}

	/**
	 * 鍔熻兘锛氳幏寰桿RL
	 * 
	 * @return
	 */
	public static String getUrl()
	{
		String url = properties.getProperty(URL);
		return url;
	}

	/**
	 * 鍔熻兘 : 鑾峰彇MasterKey
	 * 
	 * @return
	 */
	public static byte[] getScrtKey()
	{
		return scrtKey;
	}

	/**
	 * 鍔熻兘 :鍒ゆ柇鏄惁鐢�DES瀹夊叏鏈哄埗 
	 * @return
	 */
	public static boolean isUseMasterKey()
	{
		if (authFlag == 'M')
			return true;
		else
			return false;
	}

	/**
	 * 鍔熻兘锛氭牴鎹睘鎬у悕鑾峰彇灞炴�鍊�
	 * 
	 * @param propertyName
	 * @return
	 */
	public static void setLogProperty(String propertyName, String value)
	{
		properties.setProperty(propertyName, value);
	}
}