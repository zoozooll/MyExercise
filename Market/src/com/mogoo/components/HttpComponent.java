package com.mogoo.components;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class HttpComponent 
{
	private static final String TAG = "HttpComponent";
	private static final int GET = 0;
	private static final int POST = 1;
	private static final int PUT = 2;
	private static final int DELETE = 3;
	private static final int BITMAP = 4;

	private static final int TIMEOUT = 4000;
	private static final DefaultHttpClient httpClient = new DefaultHttpClient();

	private static final boolean DBG = false;
	// *************************************begin获取网络数据方法模块************************//
	// *****************************************************************************//

	/**
	 * 功能：以HttpURLConnection 的方式获取数据
	 *
	 */
	
	public static String getStringFromURL(String httpUrl) 
	{
		return getStringFromURL(httpUrl,TIMEOUT);
	}

	
	public static String getStringFromURL(String httpUrl,int timeout) 
	{
		if (httpUrl == null) {
			return null;
		}
		URL url = null;
		StringBuffer stringBuf = new StringBuffer();
		try {
			url = new URL(httpUrl);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		if (url != null) {
			try {
				HttpURLConnection urlConn = (HttpURLConnection) url
						.openConnection();
				urlConn.setConnectTimeout(timeout);
				urlConn.setReadTimeout(timeout);
				InputStreamReader in = new InputStreamReader(
						urlConn.getInputStream());
				BufferedReader buffer = new BufferedReader(in,8*1024);
				String inputLine = null;
				while ((inputLine = buffer.readLine()) != null) {
					stringBuf.append(inputLine);
				}
				in.close();
				urlConn.disconnect();
			}catch(SocketTimeoutException e) {
			    if (DBG)
                    Log.d(TAG, "getStringFromURL() SocketTimeoutException");
			    return null;
			}
			catch (IOException e) {
				if (DBG)
					Log.d(TAG, "getStringFromURL() IOException");
				e.printStackTrace();
				return null;
			} catch (Exception e) {
				if (DBG)
					Log.d(TAG, "getStringFromURL Exception");
				e.printStackTrace();
				return null;
			}
		}

		return stringBuf.toString();
	}

	/**
	 * 功能：将获取到的网络数据以String格式返回
	 *
	 * @param method
	 *            获取网络数据方法
	 * @param httpUrl
	 *            http地址
	 * @param data
	 *            要传人的数据
	 * @param gzip
	 *            是否要压缩
	 * @return String字符串
	 */
	public static String doAction(int method, String httpUrl, String data,boolean gzip,int timeout) {

		if (httpUrl == null) {
			return null;
		}

		HttpConnectionParams.setSoTimeout(httpClient.getParams(), timeout);

		HttpResponse response = null;
		BufferedHttpEntity bufHttpEntity = null;
		HttpUriRequest request = null;
		InputStream inputStream = null;

		try {
			switch (method) {
			case GET: {
				request = new HttpGet(httpUrl);
				break;
			}
			case POST: {
				HttpPost httpPost = new HttpPost(httpUrl);
				httpPost.setEntity(new StringEntity(data));
				request = httpPost;
				break;
			}
			case PUT: {
				HttpPut httpPut = new HttpPut(httpUrl);
				httpPut.setEntity(new StringEntity(data));
				request = httpPut;
				break;
			}
			case DELETE: {
				httpUrl += data;
				request = new HttpDelete(httpUrl);
				break;
			}
			case BITMAP: {
				httpUrl += data;
				request = new HttpGet(httpUrl);
				break;
			}
			}
			// set request settings
			request.addHeader("Accept-Encoding", "gzip");

			response = httpClient.execute(request);

			if (response.getStatusLine().getStatusCode() == 200) {
				if (method < BITMAP) {
					Header contentEncoding = response
							.getFirstHeader("Content-Encoding");
					InputStream ins = response.getEntity().getContent();
					if (contentEncoding != null
							&& gzip
							&& contentEncoding.getValue().equalsIgnoreCase(
									"gzip")) {
						// return new GZIPInputStream(ins);
						inputStream = new GZIPInputStream(ins);
					} else {
						inputStream = ins;
					}
				} else {
					bufHttpEntity = new BufferedHttpEntity(response.getEntity());
					inputStream = bufHttpEntity.getContent();
				}

			} else {
				inputStream = null;
			}
		} catch (Exception e) {
			// connection error
			e.printStackTrace();
		}
		if (inputStream == null) {
			return null;
		}
		return streamToString(inputStream).toString();
	}

	/**
	 * 功能:以get方法获取数据
	 *
	 * @param httpUrl
	 *            http地址
	 * @param gzip
	 *            是否要压缩
	 * @return
	 */
	public static String get(String httpUrl, String data, boolean gzip) {
		return doAction(GET, httpUrl, data, gzip,TIMEOUT);
	}

	/**
	 * 功能:以get方法获取数据
	 *
	 * @param httpUrl
	 *            http地址
	 * @param gzip
	 *            是否要压缩
	 * @return
	 */
	public static String get(String httpUrl, String data, boolean gzip,int timeout) {
		return doAction(GET, httpUrl, data, gzip,timeout);
	}
	
	/**
	 * 功能：发送post请求
	 *
	 * @param httpUrl
	 *            http地址
	 * @param data
	 *            要传人的数据
	 * @param gzip
	 *            是否要压缩
	 * @return
	 */
	public static String post(String httpUrl, String data, boolean gzip) {
		return doAction(POST, httpUrl, data, gzip,TIMEOUT);
	}
	
	/**
	 * 功能：发送post请求
	 *
	 * @param httpUrl
	 *            http地址
	 * @param data
	 *            要传人的数据
	 * @param gzip
	 *            是否要压缩
	 * @return
	 */
	public static String post(String httpUrl, String data, boolean gzip,int timeout) {
		return doAction(POST, httpUrl, data, gzip,timeout);
	}

	/**
	 * 发送put请求
	 *
	 * @param httpUrl
	 *            http地址
	 * @param data
	 *            要传人的数据
	 * @param gzip
	 *            是否要压缩
	 * @return
	 */
	public static String put(String httpUrl, String data, boolean gzip) {
		return doAction(PUT, httpUrl, data, gzip,TIMEOUT);
	}

	/**
	 * 发送put请求
	 *
	 * @param httpUrl
	 *            http地址
	 * @param data
	 *            要传人的数据
	 * @param gzip
	 *            是否要压缩
	 * @return
	 */
	public static String put(String httpUrl, String data, boolean gzip,int timeout) {
		return doAction(PUT, httpUrl, data, gzip,timeout);
	}
	
	/**
	 * \u53d1\u9001delete\u8bf7\u6c42
	 *
	 * @param httpUrl
	 *            http\u5730\u5740
	 * @param gzip
	 *            \u662f\u5426\u8981\u538b\u7f29
	 * @return
	 */
	public static String delete(String httpUrl, String data, boolean gzip) {
		return doAction(DELETE, httpUrl, data, gzip,TIMEOUT);
	}
	
	/**
	 * \u53d1\u9001delete\u8bf7\u6c42
	 *
	 * @param httpUrl
	 *            http\u5730\u5740
	 * @param gzip
	 *            \u662f\u5426\u8981\u538b\u7f29
	 * @return
	 */
	public static String delete(String httpUrl, String data, boolean gzip,int timeout) {
		return doAction(DELETE, httpUrl, data, gzip,timeout);
	}


	public static String sendStringToHttp(String url, List<NameValuePair> paras) 
	{
		return sendStringToHttp(url,paras,TIMEOUT);
	}
	
	public static String sendStringToHttp(String url, List<NameValuePair> paras,int timeout) 
	{

		HttpPost request = new HttpPost(url);
		HttpConnectionParams.setConnectionTimeout(request.getParams(), timeout);
		HttpConnectionParams.setSoTimeout(request.getParams(), timeout);
		String msg = null;
		try {
			request.setEntity(new UrlEncodedFormEntity(paras, HTTP.UTF_8));
			HttpResponse response = new DefaultHttpClient().execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				msg = EntityUtils.toString(response.getEntity());
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (DBG)
				Log.d(TAG, "sendStringToHttp() Exception!");
			return null;
		}
		return msg;
	}
	
	// private interface
	/**
	 * 功能:将InputStream 流转换成 StringBuffer
	 *
	 * @param ins
	 * @return
	 */
	private static StringBuffer streamToString(InputStream ins) {
		if (ins == null)
			return null;
		InputStreamReader br = new InputStreamReader(ins);
		BufferedReader buffer = new BufferedReader(br);
		StringBuffer result = new StringBuffer();
		String dataLine = null;
		try {
			while ((dataLine = buffer.readLine()) != null) {
				result.append(dataLine);
			}
			return result;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 功能：获取网络数据，以inputstream流格式返回
	 *
	 */
	public static InputStream getInputStreamFromURL(String httpUrl) 
	{
		return getInputStreamFromURL(httpUrl,TIMEOUT);
	}
	
	public static InputStream getInputStreamFromURL(String httpUrl,int timeout) {

		if (DBG)
			Log.d(TAG, "getInputStreamFromURL() httpurl: " + httpUrl);
		URL url = null;
		InputStream is = null;
		try {
			url = new URL(httpUrl);
		} catch (Exception e) {
			return null;
		}
		try {
			HttpURLConnection urlConn = (HttpURLConnection) url
					.openConnection();
			urlConn.setConnectTimeout(timeout);
			urlConn.setReadTimeout(timeout);
			// InputStreamReader in =new
			// InputStreamReader(urlConn.getInputStream(),"gb2312");
			is = urlConn.getInputStream();
		} catch (Exception e) {
			// e.printStackTrace();
			return null;
		}
		return is;
	}

}
