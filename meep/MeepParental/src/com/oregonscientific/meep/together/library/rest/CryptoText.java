package com.oregonscientific.meep.together.library.rest;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;
import android.util.Log;



public class CryptoText {
	
	private String saltHeader = "$p5k2$2710$";
	private String possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	private String challenge;
	private String pbk_pass;
//	private static final String SECRET_KEY_ALGORITHM = "PBEWithSHA256And256BitAES-CBC-BC";
	private static final String TAG = "Crypto";
	
	public String getChallenge() {
		challenge = "";
	    for( int i=0; i<16; i++ ) {
	    	challenge += possible.charAt((int) Math.floor(Math.random() * possible.length()));
	    }
	    return challenge;
	}
	
	public String cryptoPass(String email,String raw)
	{
		//get salt
		String salt = saltHeader+SHA1(email) ;
		//get pkey
		byte[] pkey = PBKDF2(raw,salt);
		//get phash
		String phash = salt+"$"+BASE64(pkey);
		pbk_pass = phash;
		//get hmac
		String hmac = HmacSHA1(challenge,phash);
		return hmac;
	}
	public String getPbk_pass() {
		return pbk_pass;
	}

	public void setPbk_pass(String pbk_pass) {
		this.pbk_pass = pbk_pass;
	}

	public String regiterPss(String email,String raw)
	{
		//get salt
		String salt = saltHeader+SHA1(email) ;
		//get pkey
		byte[] pkey = PBKDF2(raw,salt);
		//get phash
		String phash = salt+"$"+BASE64(pkey);
		return phash;
	}
	
	public String SHA1(String email)
	{
		//SHA1
		String s1 = Encode("SHA-1",email);
//		Log.d(TAG,"SHA1:"+s1);
//		Log.d(TAG,"SHA1:"+s1.substring(0,8));
		return s1.substring(0,8);
	}
	
//	public byte[] PBKDF2(String raw,String salt)
//	{
//		byte[] bs = null;
//		String s256 = "";
//		try{
//			
//		//hasher sha256
//		int iterations = 10000;
//		int keySize = 256;
//		Security.addProvider(new BouncyCastleProvider());
//		SecretKeyFactory f = SecretKeyFactory.getInstance(SECRET_KEY_ALGORITHM);
//		KeySpec ks = new PBEKeySpec(raw.toCharArray(),salt.getBytes(),iterations,keySize);
//		SecretKey s = f.generateSecret(ks);
//		Key k = new SecretKeySpec(s.getEncoded(),"AES");
//		bs = k.getEncoded();
//		s256 = byteArrayToHexString(s.getEncoded());
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//		Log.d(TAG,"PBKDF2:"+s256);
//		return s256.getBytes();
//	}
	public byte[] PBKDF2(String raw,String salt)
	{
		byte [] b = null;
		try {
			Rfc2898DeriveBytes key = new Rfc2898DeriveBytes(raw, salt.getBytes(), 10000);
			b = key.getBytes(32);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
//		Log.d(TAG,"pbkdf2:"+byteArrayToHexString(b));
		return b;

	}
	
	public String BASE64(byte[] pkey)
	{
		String sbase = null;
		try {
			sbase = Base64.encodeToString(pkey,Base64.DEFAULT);
			sbase = sbase.replaceAll("\\+", ".").trim();
		} catch (Exception e) {
			e.printStackTrace();
		}
//		Log.d(TAG,"BASE64:"+sbase);
		return sbase;
		
	}
	
	public String HmacSHA1(String challenge,String phash)
	{
		String sh ="";
		//challenge&phash
		try {
		    Mac mac = Mac.getInstance("HmacSHA1");
		    SecretKeySpec secret = new SecretKeySpec(phash.getBytes("UTF8"),mac.getAlgorithm());
		    mac.init(secret);
		    byte[] digest = mac.doFinal(challenge.getBytes());
		    sh = byteArrayToHexString(digest);
//		    Log.d(TAG,"HmacSHA1:"+sh);  
		} catch (Exception e) {
			Log.e(TAG,e.getMessage());
		}
		return sh;
	}
	
	/**
	 * byte[] to HEX String
	 * @param b
	 * @return
	 */
	public static String byteArrayToHexString(byte[] b) {
		String result = "";
		for (int i=0; i < b.length; i++) {
			result +=
          Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
		}
		return result;
	}
	
    /**
     * encode string
     * @param code
     * @param message
     * @return
     */
	private String Encode(String code,String message){
		MessageDigest md;
		String encode = null;
		try {
			md = MessageDigest.getInstance(code);
			encode = byteArrayToHexString(md.digest(message
					.getBytes()));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return encode;
	}
	
}




/**
 * 
 * Rfc2898
 *
 */
class Rfc2898DeriveBytes {

    private Mac _hmacSha1;
    private byte[] _salt;
    private int _iterationCount;

    private byte[] _buffer = new byte[32];
    private int _bufferStartIndex = 0;
    private int _bufferEndIndex = 0;
    private int _block = 1;

    
    /**
     * Creates new instance.
     * @param password The password used to derive the key.
     * @param salt The key salt used to derive the key.
     * @param iterations The number of iterations for the operation.
     * @throws NoSuchAlgorithmException HmacSHA1 algorithm cannot be found.
     * @throws InvalidKeyException Salt must be 8 bytes or more. -or- Password cannot be null.
     */
    public Rfc2898DeriveBytes(byte[] password, byte[] salt, int iterations) throws NoSuchAlgorithmException, InvalidKeyException {
    	if ((salt == null) || (salt.length < 8)) { throw new InvalidKeyException("Salt must be 8 bytes or more."); }
    	if (password == null) { throw new InvalidKeyException("Password cannot be null."); }
        this._salt = salt;
        this._iterationCount = iterations;
        this._hmacSha1 = Mac.getInstance("HmacSHA256");
        this._hmacSha1.init(new SecretKeySpec(password, "HmacSHA256"));
    }
    
    /**
     * Creates new instance.
     * @param password The password used to derive the key.
     * @param salt The key salt used to derive the key.
     * @param iterations The number of iterations for the operation.
     * @throws NoSuchAlgorithmException HmacSHA1 algorithm cannot be found.
     * @throws InvalidKeyException Salt must be 8 bytes or more. -or- Password cannot be null.
     * @throws UnsupportedEncodingException UTF-8 encoding is not supported. 
     */
    public Rfc2898DeriveBytes(String password, byte[] salt, int iterations) throws InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException  {
    	this(password.getBytes("UTF8"), salt, iterations);
    }

    /**
     * Creates new instance.
     * @param password The password used to derive the key.
     * @param salt The key salt used to derive the key.
     * @throws NoSuchAlgorithmException HmacSHA1 algorithm cannot be found.
     * @throws InvalidKeyException Salt must be 8 bytes or more. -or- Password cannot be null.
     * @throws UnsupportedEncodingException UTF-8 encoding is not supported. 
     */
    public Rfc2898DeriveBytes(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
    	this(password, salt, 0x3e8);
    }


    /**
     * Returns a pseudo-random key from a password, salt and iteration count.
     * @param count Number of bytes to return.
     * @return Byte array.
     */
    public byte[] getBytes(int count) {
        byte[] result = new byte[count];
        int resultOffset = 0;
        int bufferCount = this._bufferEndIndex - this._bufferStartIndex;

        if (bufferCount > 0) { //if there is some data in buffer
            if (count < bufferCount) { //if there is enough data in buffer
            	System.arraycopy(this._buffer, this._bufferStartIndex, result, 0, count);
                this._bufferStartIndex += count;
                return result;
            }
            System.arraycopy(this._buffer, this._bufferStartIndex, result, 0, bufferCount);
            this._bufferStartIndex = this._bufferEndIndex = 0;
            resultOffset += bufferCount;
        }

        while (resultOffset < count) {
            int needCount = count - resultOffset;
            this._buffer = this.func();
            if (needCount > 32) { //we one (or more) additional passes
            	System.arraycopy(this._buffer, 0, result, resultOffset, 32);
                resultOffset += 32;
            } else {
            	System.arraycopy(this._buffer, 0, result, resultOffset, needCount);
                this._bufferStartIndex = needCount;
                this._bufferEndIndex = 32;
                return result;
            }
        }
        return result;
    }

    
    private byte[] func() {
        this._hmacSha1.update(this._salt, 0, this._salt.length);
        byte[] tempHash = this._hmacSha1.doFinal(getBytesFromInt(this._block));

        this._hmacSha1.reset();
        byte[] finalHash = tempHash;
        for (int i = 2; i <= this._iterationCount; i++) {
            tempHash = this._hmacSha1.doFinal(tempHash);
            for (int j = 0; j < 32; j++) {
                finalHash[j] = (byte)(finalHash[j] ^ tempHash[j]);
            }
        }
        if (this._block == 2147483647) {
            this._block = -2147483648;
        } else {
            this._block += 1;
        }

        return finalHash;
    }

    private byte[] getBytesFromInt(int i) {
    	return new byte[] { (byte)(i >>> 24), (byte)(i >>> 16), (byte)(i >>> 8), (byte)i };
    }
	
}
