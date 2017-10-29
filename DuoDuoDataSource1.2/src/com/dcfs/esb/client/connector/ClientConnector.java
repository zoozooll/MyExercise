package com.dcfs.esb.client.connector;


public interface ClientConnector 
{
	public byte[] doComm(byte[] xmlByte) throws Exception;
	
    public byte[] doComm(String mailer, byte[] xmlByte) throws Exception;
}
