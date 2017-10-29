package com.dcfs.esb.client.connector;

import java.io.IOException;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;



public class SecurityUtil {

	public static final String DES = "DESede";
	
	/**
	 * 功能：3DES加密
	 * @param request
	 * @return
	 */
	public static byte[] encrypt3DES(byte[] value, byte[] key)
	{
		byte[] retValue = null;

		try
		{
			SecretKey deskey = new SecretKeySpec(key, DES);
			Cipher c1 = Cipher.getInstance(DES);
			c1.init(Cipher.ENCRYPT_MODE, deskey);
			retValue = c1.doFinal(value);
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}

		return retValue;
	}
	
	/**
	 * 功能：Md5加密
	 * @param request
	 * @return
	 */
	public static String encryptMd5(byte[] bytes)
	{
		StringBuffer sb = new StringBuffer();
		try
		{
			byte[] md5Str = null;
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(bytes);
			md5Str = mdTemp.digest();

			for (int i = 0; i < md5Str.length; i++)
			{
				sb.append(Integer.toHexString(
						(0x000000ff & md5Str[i]) | 0xffffff00).substring(6));
			}
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return sb.toString();
	}
	
	/**
	 * 功能：3DES解密
	 * @param request
	 * @return
	 */
	public static byte[] decrypt3DES(byte[] value, byte[] key)
	{
		byte[] retValue = null;

		try
		{
			SecretKey deskey = new SecretKeySpec(key, DES);
			Cipher c1 = Cipher.getInstance(DES);
			c1.init(Cipher.DECRYPT_MODE, deskey);
			retValue = c1.doFinal(value);
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}

		return retValue;
	}
	

    


}
