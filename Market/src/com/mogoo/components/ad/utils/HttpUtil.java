package com.mogoo.components.ad.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
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
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class HttpUtil
{
	private static final String TAG = "DataFromUrl";
	public static String sendClickURL = "http://192.168.10.4:9000/AD/sendAdvertise.action";

	/**
	 * 功能：以HttpURLConnection 的方式获取数据
	 */
	public static String getStringFromURL(String httpUrl)
	{
		if (httpUrl == null)
		{
			return null;
		}
		URL url = null;
		StringBuffer stringBuf = new StringBuffer();
		try
		{
			url = new URL(httpUrl);
		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
		if (url != null)
		{
			try
			{
				HttpURLConnection urlConn = (HttpURLConnection) url
						.openConnection();
				// urlConn.setConnectTimeout(30);
				// urlConn.setReadTimeout(30);
				InputStreamReader in = new InputStreamReader(
						urlConn.getInputStream());
				BufferedReader buffer = new BufferedReader(in);
				String inputLine = null;
				while ((inputLine = buffer.readLine()) != null)
				{
					stringBuf.append(inputLine);
				}
				in.close();
				urlConn.disconnect();
			} catch (IOException e)
			{

				e.printStackTrace();
				return null;
			} catch (Exception e)
			{

				e.printStackTrace();
				return null;
			}
		}

		return stringBuf.toString();
	}

//	public static Bitmap downloadBitmapByUrl(String url)
//	{
//		final AndroidHttpClient client = AndroidHttpClient
//				.newInstance("Android123");
//		Log.d(TAG, "+++++downloadBitmapByUrl URL=" + url);
//		final HttpGet getRequest = new HttpGet(url);
//		Log.d(TAG, "+++++getRequest URL");
//		try
//		{
//			HttpResponse response = client.execute(getRequest);
//			final int statusCode = response.getStatusLine().getStatusCode();
//			if (statusCode != HttpStatus.SC_OK)
//			{
//				Log.e("cwjDebug", "Error " + statusCode
//						+ " while retrieving bitmap from " + url);
//				return null;
//			}
//
//			final HttpEntity entity = response.getEntity();
//			if (entity != null)
//			{
//				InputStream inputStream = null;
//				try
//				{
//					inputStream = entity.getContent();
//					final Bitmap bitmap = BitmapFactory
//							.decodeStream(inputStream);
//					return bitmap;
//				} finally
//				{
//					if (inputStream != null)
//					{
//						inputStream.close();
//					}
//					entity.consumeContent();
//				}
//			}
//		} catch (Exception e)
//		{
//			getRequest.abort();
//			Log.e("android123Debug", "Error while retrieving bitmap from "
//					+ url);
//		} finally
//		{
//			if (client != null)
//			{
//				client.close();
//			}
//		}
//		return null;
//	}

	// /////////////////////////////////////////////////////////
	// private members
	public static final int DID_START = 0;
	public static final int DID_ERROR = 1;
	public static final int DID_SUCCEED = 2;

	public static final int GET = 0;
	public static final int POST = 1;
	public static final int PUT = 2;
	public static final int DELETE = 3;
	public static final int BITMAP = 4;

	// private static final DefaultHttpClient httpClient
	// = new DefaultHttpClient();

	private static DefaultHttpClient getClient()
	{
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpConnectionParams.setSoTimeout(httpClient.getParams(), 5000);
		return httpClient;
	}

	// /////////////////////////////////////////////////////////
	// public members

	public static StringBuffer streamToString(InputStream ins)
	{
		if (ins == null)
			return null;
		InputStreamReader br = new InputStreamReader(ins);
		StringBuffer result = new StringBuffer();
		char[] buf = new char[1024 * 4];
		int r = 0;
		try
		{
			while ((r = br.read(buf)) > 0)
				result.append(buf, 0, r);
			return result;
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static Bitmap streamToBitmap(InputStream ins)
	{
		if (ins == null)
			return null;
		Bitmap bm = BitmapFactory.decodeStream(ins);
		return bm;
	}

	public static InputStream doAction(int method, String url,
			List<NameValuePair> data, boolean gzip) throws Exception
	{
		HttpResponse response = null;
		BufferedHttpEntity bufHttpEntity = null;
		HttpUriRequest request = null;

		try
		{
			switch (method)
			{
			case GET:
			{
				request = new HttpGet(url);
				break;
			}
			case POST:
			{
				HttpPost httpPost = new HttpPost(url);
				httpPost.setEntity(new UrlEncodedFormEntity(data, HTTP.UTF_8));
				request = httpPost;
				break;
			}
			case PUT:
			{
				HttpPut httpPut = new HttpPut(url);
				httpPut.setEntity(new UrlEncodedFormEntity(data, HTTP.UTF_8));
				request = httpPut;
				break;
			}
			case DELETE:
			{
				request = new HttpDelete(url);
				break;
			}
			case BITMAP:
			{
				request = new HttpGet(url);
				break;
			}
			}
			// set request settings
			request.addHeader("Accept-Encoding", "gzip");

			response = getClient().execute(request);

			if (response.getStatusLine().getStatusCode() == 200)
			{
				if (method < BITMAP)
				{
					Header contentEncoding = response
							.getFirstHeader("Content-Encoding");
					InputStream ins = response.getEntity().getContent();
					if (contentEncoding != null
							&& gzip
							&& contentEncoding.getValue().equalsIgnoreCase(
									"gzip"))
					{
						return new GZIPInputStream(ins);
					} else
					{
						return ins;
					}
				} else
				{
					bufHttpEntity = new BufferedHttpEntity(response.getEntity());
					return bufHttpEntity.getContent();
				}
			} else
			{
				return null;
			}
		} catch (Exception e)
		{
			// connection error
			throw e;
		}
	}

	public static InputStream get(String url, boolean gzip) throws Exception
	{
		return doAction(GET, url, null, gzip);
	}

	public static InputStream post(String url, List<NameValuePair> params,
			boolean gzip) throws Exception
	{
		if (params == null)
			return null;
		return doAction(POST, url, params, gzip);
	}

	public static InputStream put(String url, List<NameValuePair> params,
			boolean gzip) throws Exception
	{
		if (params == null)
			return null;
		return doAction(PUT, url, params, gzip);
	}

	public static InputStream delete(String url, boolean gzip) throws Exception
	{
		return doAction(DELETE, url, null, gzip);
	}

	public static InputStream bitmap(String url, boolean gzip) throws Exception
	{
		return doAction(BITMAP, url, null, gzip);
	}

}
