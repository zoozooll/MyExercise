package com.dcfs.esb.client.connector;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dcfs.esb.client.config.Config;

public class HTTPClientConnectorNew implements ClientConnector
{

	private final static Log log = LogFactory
			.getLog(HTTPClientConnectorNew.class);
	protected static String httpUrl;
	protected static String httpUrl2;
	protected static String httpUrl3;
	protected static int connTimeout;
	protected static int readTimeout;
	private static byte[] workKey = null;
	private static String workDay = null;
	private static int failCalculator = 0;
	private final static String SYN_WORKKEY_FLAG = "L";
	private final static String REQ_BUSS_FLAG = "A";

	private final static HTTPClientConnectorNew HCCN = new HTTPClientConnectorNew();

	/**
	 * 鍔熻兘锛氬悓姝orkKey
	 * 
	 * @return
	 * @throws Exception
	 */
	/*public synchronized void synWorkKey() throws Exception
	{
		if (workKey != null && !"".equals(workKey))
			return;

		if (log.isInfoEnabled())
			log.info("涓嶦SB绯荤粺鍚屾瀵嗛挜寮�銆傘�銆�);
		URL url = null;
		URLConnection conn = null;
		DataOutputStream dos = null;
		DataInputStream dis = null;
		try
		{
			url = new URL(null, httpUrl, new ClientTimeoutHandler(readTimeout));
			conn = url.openConnection();
			conn.setDoOutput(true);

			*//*******************************************************************
			 * 澶勭悊璇锋眰鐨勬暟鎹�
			 ******************************************************************//*
			conn.setRequestProperty("uid", Config.getLogProperty(Config.UID));
			conn.setRequestProperty("flag", SYN_WORKKEY_FLAG);
			dos = new DataOutputStream(conn.getOutputStream());
			dos.flush();

			*//*******************************************************************
			 * 澶勭悊鍝嶅簲鐨勬暟鎹�
			 ******************************************************************//*
			dis = new DataInputStream(conn.getInputStream());
			String workKeyStr = conn.getHeaderField("workKey");
			
			// workkey鐨勫唴瀹瑰瓨鍦紝鍒欒繘琛岀浉搴旂殑澶勭悊
			if (workKeyStr != null && !"".equals(workKeyStr))
			{
				// 鍏堣繘琛宐ase64鐨勮В鐮侊紝鍚庤繘琛�Des鐨勮В瀵嗗悗寰楀埌workkey
				byte[] desStr = SecurityUtil.decodeBase64(workKeyStr);
				byte[] masterKey = Config.getScrtKey();
				workKey = SecurityUtil.decrypt3DES(desStr, masterKey);
				
				// 璇诲彇key鐨勬湁鏁堟棩鏈�
				workDay = conn.getHeaderField("workDay");
				
				// 杩炵画澶辫触鐨勬鏁扮疆涓�
				failCalculator = 0;
				if (log.isInfoEnabled())
					log.info("涓嶦SB绯荤粺鍚屾瀵嗛挜鎴愬姛锛屽瘑閽ョ殑鏃ユ湡鏄細" + workDay);

			} else
			{
				throw new Exception("鍚屾workKey澶辫触,杩斿洖鏁版嵁涓虹┖");
			}

		} finally
		{
			// 鍏抽棴dis
			try
			{
				if (null != dis)
					dis.close();
			} catch (IOException e)
			{
				log.error("鍏抽棴Http杩炴帴鐨勮緭鍏ユ祦鍑洪敊");
			}

			// 鍏抽棴dos
			try
			{
				if (null != dos)
					dos.close();
			} catch (IOException e)
			{
				log.error("鍏抽棴Http杩炴帴鐨勮緭鍑烘祦鍑洪敊");
			}
		}

	}*/

	/**
	 * 鍔熻兘 : 涓嶦SB杩涜閫氳
	 * 
	 * @param mailer
	 * @param xmlByte
	 * @return
	 * @throws Exception
	 */
	public byte[] doComm(String mailer, byte[] xmlByte) throws Exception
	{
		// 鏃ュ織
		if (log.isInfoEnabled())
		{
			log.info(new StringBuffer("璇锋眰鐨剈rl涓猴細").append(httpUrl));
			log.info(new StringBuffer("寤虹珛杩炴帴瓒呮椂鏃堕棿涓�").append(connTimeout).append(
					"姣"));
			log.info(new StringBuffer("绛夊緟鍝嶅簲瓒呮椂鏃堕棿涓�").append(readTimeout).append(
					"姣"));
		}

		// 濡傛灉WORKEY涓虹┖锛屽垯鍏堝悓姝ORKEY
/*		if (workKey == null || "".equals(workKey))
			HCCN.synWorkKey();*/

		URL url = null;
		URLConnection conn = null;
		DataOutputStream dos = null;
		DataInputStream dis = null;
		byte[] rspBytes = null;

		try
		{
			// 鐢熸垚鎶ユ枃鐨凪D5鍚庯紝閫氳繃3DES鍔犲瘑锛屽啀閫氳繃Base64杞崲鎴怱tring
			/*String md5 = SecurityUtil.encryptMd5(xmlByte);
			byte[] byteDes = SecurityUtil.encrypt3DES(md5.getBytes(), workKey);
			String strDes = SecurityUtil.encodeBase64(byteDes);*/

			// 寤虹珛涓嶦SB鐨勮繛鎺�
//			url = new URL(null, httpUrl, "60");
//			log.info("httpUrl==>"+httpUrl);
//			url = new URL("http://192.168.1.254:8089/DuoBookShop/servlet");
			url = new URL(httpUrl);
			conn = url.openConnection();
			conn.setDoOutput(true);

			// info绾у埆鐨勬棩蹇楋紝璁板綍XML鎶ユ枃鐨勯暱搴�
			if (log.isInfoEnabled())
//				log.info(new StringBuffer("寮�鍙戦�鏁版嵁锛屽彂閫乆ML鎶ユ枃鐨勯暱搴︽槸锛�)
//						.append(xmlByte.length));

			// debug绾у埆鐨勬棩蹇楋紝璁板綍XML鎶ユ枃鐨勫唴瀹�
			if (log.isDebugEnabled())
			{
				log.debug(new StringBuffer("鍙戦�鏁版嵁鐨刋ML鎶ユ枃鏄細").append(new String(
						xmlByte, "UTF-8")));
			}

			/*******************************************************************
			 * 澶勭悊璇锋眰鐨勫ご淇℃伅
			 ******************************************************************/
			conn.setRequestProperty("uid", Config.getLogProperty(Config.UID));
			//conn.setRequestProperty("series", strDes);
			conn.setRequestProperty("workDay", workDay);
			conn.setRequestProperty("flag", REQ_BUSS_FLAG);
			

			/*******************************************************************
			 * 澶勭悊璇锋眰鐨勬暟鎹�
			 ******************************************************************/
			dos = new DataOutputStream(conn.getOutputStream());
			//dos.writeUTF(mailer);
			dos.writeInt(xmlByte.length);
			dos.write(xmlByte);
			dos.flush();

			/*******************************************************************
			 * 澶勭悊鍝嶅簲鐨勬暟鎹�
			 ******************************************************************/
			dis = new DataInputStream(conn.getInputStream());
			String rslt = conn.getHeaderField("result");
			if ("S".endsWith(rslt) || "I".endsWith(rslt))
			{
				String workKeyStr = conn.getHeaderField("workKey");
				
				/*// workkey鐨勫唴瀹瑰瓨鍦紝鍒欒繘琛岀浉搴旂殑澶勭悊
				if (workKeyStr != null && !"".equals(workKeyStr))
				{
					// 鍏堣繘琛宐ase64鐨勮В鐮侊紝鍚庤繘琛�Des鐨勮В瀵嗗悗寰楀埌workkey
					byte[] desStr = SecurityUtil.decodeBase64(workKeyStr);
					if("I".endsWith(rslt)) {
						workKey = "888888888888888888888888".getBytes();
					}else {
						byte[] masterKey = Config.getScrtKey();
						workKey = SecurityUtil.decrypt3DES(desStr, masterKey);
					}
				}*/
						
					// 璇诲彇key鐨勬湁鏁堟棩鏈�
				/*String workDayStr = conn.getHeaderField("workDay");
				if(workDayStr != null && !"".equals(workDayStr))
				{
					workDay = conn.getHeaderField("workDay");
				}

				// 杩炵画澶辫触鐨勬鏁扮疆涓�
				failCalculator = 0;*/

				// 璇诲彇鍝嶅簲鐨勬姤鏂�
				int rspLen = dis.readInt();
				rspBytes = new byte[rspLen];
				dis.readFully(rspBytes);

				// info绾у埆鐨勬棩蹇楋紝璁板綍XML鎶ユ枃鐨勯暱搴�
				if (log.isInfoEnabled())
//					log.info(new StringBuffer("璇诲彇鍝嶅簲鎶ユ枃缁撴潫锛岄暱搴︿负锛�).append(rspLen));

				// debug绾у埆鐨勬棩蹇楋紝璁板綍XML鎶ユ枃鐨勫唴瀹�
				if (log.isDebugEnabled())
					log.debug(new StringBuffer("鏁版嵁鐨刋ML鎶ユ枃鏄�").append(new String(
							rspBytes, "UTF-8")));
			} else
			{
				// 澶辫触锛屽垯澶辫触璁℃暟鍣ㄥ姞1
				failCalculator++;
				if (failCalculator >= 10)
				{
					// 濡傛灉杩炵画瓒呰繃10娆″け璐ワ紝鍒欓噸鏂板悓姝ュ瘑閽�
					workKey = null;
					workDay = null;
					failCalculator = 0;
					log.error("澶辫触杩炵画瓒呰繃10娆�闇�閲嶆柊鍚屾workey锛�");
				}

				// 鎶涘嚭浜ゆ槗澶辫触鐨勫紓甯�
				throw new Exception("鎶ユ枃鏍￠獙澶辫触锛屼氦鏄撴嫆缁�");
			}
		} finally
		{
			// 鍏抽棴dis
			try
			{
				if (null != dis)
					dis.close();
			} catch (IOException e)
			{
				log.error("鍏抽棴Http杩炴帴鐨勮緭鍏ユ祦鍑洪敊");
			}

			// 鍏抽棴dos
			try
			{
				if (null != dos)
					dos.close();
			} catch (IOException e)
			{
				log.error("鍏抽棴Http杩炴帴鐨勮緭鍑烘祦鍑洪敊");
			}
		}
		return rspBytes;
	}

	/**
	 * 鍔熻兘 : 涓嶦SB杩涜閫氳
	 * 
	 * @param mailer
	 * @param xmlByte
	 * @return
	 * @throws Exception
	 */
	public byte[] doComm(byte[] xmlByte) throws Exception
	{
		return null;
	}

	/**
	 * 鍔熻兘 : 璁剧疆璇锋眰瓒呮椂鏃堕棿
	 * 
	 * @param time
	 */
	public static void setConnTimeout(int time)
	{
		// 璁剧疆绯荤粺Http寤虹珛杩炴帴鐨勮秴鏃舵椂闂�
		connTimeout = time;
		// 杩炴帴瓒呮椂鏃堕棿鏈夊彉鍖栵紝鍙婃椂鏇存柊绯荤粺鐜鍙橀噺
		//ClientTimeoutHandler.setDefaultConnectionTimeout(connTimeout);
	}

	/**
	 * 鍔熻兘 : 璁剧疆璇诲搷搴旇秴鏃舵椂闂�
	 * 
	 * @param time
	 */
	public static void setReadTimeout(int time)
	{
		// 璁剧疆绛夊緟HTTP鍝嶅簲鐨勮秴鏃舵椂闂达紝榛樿鎯呭喌涓嬶紝寤虹珛杩炴帴鏃惰缃�
		readTimeout = time;
	}

	/**
	 * 鍔熻兘 : 璁剧疆ESB鐨刄RL
	 * 
	 * @param url
	 */
	public static void setHttpUrl(String url)
	{
		httpUrl = url;
	}
}
