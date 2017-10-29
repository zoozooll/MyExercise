/*package com.dcfs.esb.client.connector;

import sun.net.NetworkClient;


public class HttpClientTimeout extends NetworkClient
{
	private static int connTimeout  = 10000;
	private static int soTimeout = 30000; 
	
	static 
	{
		defaultConnectTimeout = connTimeout;
		defaultSoTimeout = soTimeout; 
	}
	
	*//**
	 * 功能 : 
	 * @param timeout
	 *//*
	public static void setDefaultConnectTimeout(int timeout)
	{
		connTimeout = timeout;
		defaultConnectTimeout = connTimeout;
	}
	
	*//**
	 * 功能 : 
	 * @return
	 *//*
	public static int getDefaultConnectTimeout()
	{
		return connTimeout;
	}
	
	*//**
	 * 功能 : 
	 * @param timeout
	 *//*
	public static void setDefaultSoTimeout(int timeout)
	{
		String java_version = System.getProperty("java.version");
		if(java_version != null &&java_version.startsWith("1.4"))
			soTimeout = timeout/2;
		else
			soTimeout = timeout;
		defaultSoTimeout = soTimeout;
	}
	
	*//**
	 * 功能 : 
	 * @return
	 *//*
	public static int getDefaultSoTimeout()
	{
		return soTimeout;
	}
}
*/