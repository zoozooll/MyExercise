/*package com.dcfs.esb.client.connector;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import sun.net.www.protocol.http.Handler;
import sun.net.www.protocol.http.HttpURLConnection;

public class ClientTimeoutHandler extends Handler
{
	private int readTimeout = 120*1000;

	private static final String key = "sun.net.client.defaultConnectTimeout";

	*//**
	 * 功能 : 设置系统建立连接的默认超时时间
	 * @param connectionTimeout
	 *//*
	public static void setDefaultConnectionTimeout(int connectionTimeout )
	{
		if (connectionTimeout > 0)
			System.setProperty(key, String.valueOf(connectionTimeout));
	}
	
	*//**
	 * 功能：构造函数
	 * @param readTimeout
	 *//*
	public ClientTimeoutHandler(int readTimeout)
	{
		super();
		this.readTimeout = readTimeout;
	}

	*//**
	 * 功能：overwrite
	 *//*
	protected URLConnection openConnection(URL url) throws IOException
	{
		return new ClientHttpURLConnection(url, this, readTimeout);
	}

	*//**
	 * @author ex-chenhonghong
	 *//*
	private class ClientHttpURLConnection extends HttpURLConnection
	{
		*//**
		 * 功能：构造函数
		 * @param url
		 * @param handler1
		 * @param timeout
		 * @throws IOException
		 *//*
		protected ClientHttpURLConnection(URL url, Handler handler1, int timeout)
				throws IOException
		{
			super(url, handler1);
		}

		*//**
		 * 功能：overwrite
		 *//*
		protected void plainConnect() throws IOException
		{
			super.plainConnect();
			if (readTimeout > 0)
				super.http.setTimeout(readTimeout);
		}
	}
}

*/