package com.dcfs.esb.client.connector;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.net.URLConnection;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dc.eai.data.CompositeData;


public class HTTPClientConnectorCd extends HTTPClientConnectorNew
{
	private final static Log log = LogFactory.getLog(HTTPClientConnector.class);

	public CompositeData doComm(CompositeData xmlByte) throws Exception
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


		// 鍒濆鍖栫浉鍏冲弬鏁�
		URL url = null;
		URLConnection conn = null;
		ObjectInputStream   dis=null;
		ObjectOutputStream   oos=null;
		CompositeData rspBytes = new CompositeData();

		try
		{
			// 寤虹珛涓嶦SB鐨勮繛鎺�
			
			url = new URL(null, httpUrl, "120");
			conn = url.openConnection();
			conn.setDoOutput(true);

			// info绾у埆鐨勬棩蹇楋紝璁板綍XML鎶ユ枃鐨勯暱搴�
			if (log.isInfoEnabled())
				log.info(new StringBuffer("寮�鍙戦�鏁版嵁锛屽彂閫佺殑XML鎶ユ枃鏄細"+xmlByte));


			/*******************************************************************
			 * 澶勭悊璇锋眰鐨勬暟鎹�
			 ******************************************************************/
			
			oos = new   ObjectOutputStream(new  BufferedOutputStream(conn.getOutputStream()));   
            oos.writeObject(xmlByte);   
            oos.flush();

			/*******************************************************************
			 * 澶勭悊鍝嶅簲鐨勬暟鎹�
			 ******************************************************************/
			
			
			dis = new   ObjectInputStream(new  BufferedInputStream(conn.getInputStream()));
			rspBytes=(CompositeData)dis.readObject(); 
			
			
			// info绾у埆鐨勬棩蹇楋紝璁板綍XML鎶ユ枃鐨勯暱搴�
			if (log.isInfoEnabled())
				log.info(new StringBuffer("璇诲彇鍝嶅簲鎶ユ枃缁撴潫锛岄暱搴︿负锛�").append(rspBytes.size()));

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
				if (null != oos)
					oos.close();
			} catch (IOException e)
			{
				log.error("鍏抽棴Http杩炴帴鐨勮緭鍑烘祦鍑洪敊");
			}
		}

		return rspBytes;
	}
}
