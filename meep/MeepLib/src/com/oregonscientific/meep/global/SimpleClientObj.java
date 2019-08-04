package com.oregonscientific.meep.global;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import android.util.Log;

public class SimpleClientObj {

	private int mId;
	private String mName;
	private String mPassword;
	private String mEmail;
	private String mIconAddr;
	private ClientType mClientType;
	private String mToken;
	private String mDesc;
	private int mGroupId;
	
	public SimpleClientObj()
	{
		setId(0);
		setName("");
		setEmail("");
		setIconAddr("");
		setClientType(ClientType.FRIEND);
		setDesciption("");
		setToken("");
		setGroupId(0);
	}
	
	public int getId() {
		return mId;
	}
	public void setId(int id) {
		this.mId = id;
	}
	public String getName() {
		return mName;
	}
	public void setName(String name) {
		this.mName = name;
	}
	public String getEmail() {
		return mEmail;
	}
	public void setEmail(String email) {
		this.mEmail = email;
	}
	public String getIconAddr() {
		return mIconAddr;
	}
	public void setIconAddr(String iconAddr) {
		this.mIconAddr = iconAddr;
	}
	public ClientType getClientType() {
		return mClientType;
	}
	public void setClientType(ClientType clientType) {
		this.mClientType = clientType;
	}
	public String getToken() {
		return mToken;
	}
	public void setToken(String token) {
		this.mToken = token;
	}
	public String getDesciption() {
		return mDesc;
	}
	public void setDesciption(String desc) {
		this.mDesc = desc;
	}

	public int getGroupId() {
		return mGroupId;
	}

	public void setGroupId(int groupId) {
		this.mGroupId = groupId;
	}

	public String getPassword() {
		return mPassword;
	}

	public void setPassword(String password) {
		
		this.mPassword = encyptPassword(password);
	}	
	
	byte[] key;
	
	private String encyptPassword(String password)
	{
		try {
			KeyGenerator keyG = KeyGenerator.getInstance("AES");
			keyG.init(256);
			SecretKey secuK = keyG.generateKey();
			key = secuK.getEncoded();
			System.out.println("key: "+new String(key));
			SecretKeySpec spec = new SecretKeySpec(key, "AES");
			Cipher cipher;
			try {
				cipher = Cipher.getInstance("AES");
				
				try {
					cipher.init(Cipher.ENCRYPT_MODE, spec);
					
					byte[] encryptData;
					try {
						encryptData = cipher.doFinal(password.getBytes());
						mPassword = new String(encryptData);
						Log.i("password", "password encrypted pwd:" + mPassword);
					} catch (IllegalBlockSizeException e) {
						e.printStackTrace();
					} catch (BadPaddingException e) {
						e.printStackTrace();
					}
					
				} catch (InvalidKeyException e) {
				
					e.printStackTrace();
				}
				
				
			} catch (NoSuchPaddingException e) {
				
				e.printStackTrace();
			}
			
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	private String decryptPassword()
	{
		SecretKeySpec spec = new SecretKeySpec(key, "AES");
		Cipher cipher;
		byte[] original;
		try {
			cipher = Cipher.getInstance("AES");
			
			try {
				cipher.init(Cipher.DECRYPT_MODE, spec);
				try {
					original = cipher.doFinal(mPassword.getBytes());
					Log.i("password", "password decrypted pwd:" + original);
				} catch (IllegalBlockSizeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (BadPaddingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (InvalidKeyException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (NoSuchAlgorithmException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (NoSuchPaddingException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		//
		return "";
	}
	
	public boolean isPasswordEqualTo(String password2) {
		return decryptPassword().equals(password2);
	}
}