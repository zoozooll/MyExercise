package com.dcfs.esb.client.connector;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class HTTPClientConnector extends HTTPClientConnectorNew
{
	private final static Log log = LogFactory.getLog(HTTPClientConnector.class);

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

		// 濡傛灉璁よ瘉鐨処D涓虹┖锛屽垯杩涜璁よ瘉鐨勬搷浣�
		/*if (authToken == null || "".equals(authToken))
			HCC.Logon();*/

		// 鍒濆鍖栫浉鍏冲弬鏁�
		URL url = null;
		URLConnection conn = null;
		DataOutputStream dos = null;
		DataInputStream dis = null;
		byte[] rspBytes = null;

		try
		{
			// 寤虹珛涓嶦SB鐨勮繛鎺�
//			url = new URL(null, httpUrl, "60");
			url = new URL(httpUrl);
//			url = new URL("", "http://192.168.166.144/DuoUser/UserServlet", "120");
//			url = new URL("http://192.168.1.254:8089/DuoBookShop/servlet");
			conn = url.openConnection();
			conn.setDoOutput(true);
//			conn.connect();
			// info绾у埆鐨勬棩蹇楋紝璁板綍XML鎶ユ枃鐨勯暱搴�
			if (log.isInfoEnabled())
				log.info(new StringBuffer("寮�鍙戦�鏁版嵁锛屽彂閫乆ML鎶ユ枃鐨勯暱搴︽槸锛�")
						.append(xmlByte.length));


			/*******************************************************************
			 * 澶勭悊璇锋眰鐨勬暟鎹�
			 ******************************************************************/
			dos = new DataOutputStream(conn.getOutputStream());
			dos.writeInt(xmlByte.length);
			dos.write(xmlByte);
			dos.flush();

			/*******************************************************************
			 * 澶勭悊鍝嶅簲鐨勬暟鎹�
			 ******************************************************************/
			dis = new DataInputStream(conn.getInputStream());
			int rspLen = dis.readInt();
			rspBytes = new byte[rspLen];
			dis.readFully(rspBytes);

			// info绾у埆鐨勬棩蹇楋紝璁板綍XML鎶ユ枃鐨勯暱搴�
			if (log.isInfoEnabled())
				log.info(new StringBuffer("璇诲彇鍝嶅簲鎶ユ枃缁撴潫锛岄暱搴︿负锛�").append(rspLen));

			/*// debug绾у埆鐨勬棩蹇楋紝璁板綍XML鎶ユ枃鐨勫唴瀹�
			if (log.isDebugEnabled())
				log.debug(new StringBuffer("鏁版嵁鐨刋ML鎶ユ枃鏄�).append(new String(
						rspBytes, "UTF-8")));*/

		}catch(Exception e){
			e.printStackTrace();
		}finally
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
}
