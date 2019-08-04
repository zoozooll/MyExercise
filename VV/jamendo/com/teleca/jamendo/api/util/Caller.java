/*
 * Copyright (C) 2009 Teleca Poland Sp. z o.o. <android@teleca.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.teleca.jamendo.api.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.GZIPInputStream;

//import okio.BufferedSink;
//import okio.GzipSink;
//import okio.Okio;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.jivesoftware.smack.util.StringUtils;

import com.beem.project.btf.utils.BBSUtils;
import com.beem.project.btf.utils.FileUtil;
import com.beem.project.btf.utils.LogUtils;
//import com.squareup.okhttp.Call;
//import com.squareup.okhttp.Interceptor;
//import com.squareup.okhttp.MediaType;
//import com.squareup.okhttp.OkHttpClient;
//import com.squareup.okhttp.Request;
//import com.squareup.okhttp.RequestBody;
//import com.squareup.okhttp.Response;
import com.teleca.jamendo.api.WSError;

/**
 * @author Lukasz Wisniewski
 */
public class Caller {
	/**
	 * Cache for most recent request
	 */
	private static RequestCache requestCache = null;
	public static String JSESSIONID = null;
	private static long id = 0;;
	private static String prefix = StringUtils.randomString(6) + "-";
	private static final int HTTP_CONNTIMEOUT = 5 * 1000;
	private static final int HTTP_SOTIMEOUT = 5 * 1000;

	//	private static OkHttpClient client;
	public static synchronized String nextID() {
		return prefix + Long.toString(id++);
	}
	/*public static String doGet_okhttp(String url) throws WSError {
		String id = nextID();
		if (url.contains("?")) {
			url += "&id=" + id;
		} else {
			url += "?&id=" + id;
		}
		//LogUtils.i("doGet_url:" + url);
		url = url.replaceAll(" ", "%20");
		String data = null;
		if (client == null) {
			client = new OkHttpClient();
		}
		OkHttpClient mOkHttpClient = client.clone();
		mOkHttpClient.setConnectTimeout(HTTP_CONNTIMEOUT, TimeUnit.MILLISECONDS);
		mOkHttpClient.setReadTimeout(HTTP_SOTIMEOUT, TimeUnit.MILLISECONDS);
		Request request = new Request.Builder().url(url).header("charset", HTTP.UTF_8).build();
		Call call = mOkHttpClient.newCall(request);
		Response response;
		try {
			response = call.execute();
			data = response.body().string();
		} catch (IOException e) {
			throw new WSError(WSError.Type.SocketTimeoutException, "url:" + url + " " + e.getLocalizedMessage());
		}
		return data;
	}*/
	/**
	 * Performs HTTP GET using Apache HTTP Client v 4
	 * @param url
	 * @return
	 * @throws WSError
	 */
	/**
	 * @param url
	 * @return
	 * @throws WSError
	 */
	public static String doGet(String url) throws WSError {
		final String id = nextID();
		if (url.contains("?")) {
			url += "&id=" + id;
		} else {
			url += "?&id=" + id;
		}
		LogUtils.i("doGet request:" + url);
		url = url.replaceAll(" ", "%20");
		String data = null;
		if (requestCache != null) {
			data = requestCache.get(url);
			if (data != null) {
				return data;
			}
		}
		URI encodedUri = null;
		HttpGet httpGet = null;
		try {
			encodedUri = new URI(url);
			httpGet = new HttpGet(encodedUri);
		} catch (URISyntaxException e1) {
			// at least try to remove spaces
			String encodedUrl = url.replace(' ', '+');
			httpGet = new HttpGet(encodedUrl);
			e1.printStackTrace();
		}
		// initialize HTTP GET request objects
		DefaultHttpClient httpClient = new DefaultHttpClient();
		httpGet.addHeader("charset", HTTP.UTF_8);
		httpGet.addHeader("Accept-Encoding", "gzip");
		HttpConnectionParams.setConnectionTimeout(httpClient.getParams(),
				HTTP_CONNTIMEOUT);
		HttpConnectionParams.setSoTimeout(httpClient.getParams(),
				HTTP_SOTIMEOUT);
		HttpResponse httpResponse;
		try {
			// execute request
			try {
				if (null != JSESSIONID) {
					httpGet.setHeader("Cookie", "JSESSIONID=" + JSESSIONID);
				}
				httpResponse = httpClient.execute(httpGet);
				CookieStore cookieStore = httpClient.getCookieStore();
				List<Cookie> cookies = cookieStore.getCookies();
				for (Cookie cookie : cookies) {
					if ("JSESSIONID".equals(cookie.getName()))
						JSESSIONID = cookie.getValue();
				}
			} catch (SocketTimeoutException e) {
				throw new WSError(WSError.Type.SocketTimeoutException, "url:"
						+ url + " " + e.getLocalizedMessage());
			} catch (ConnectTimeoutException e) {
				throw new WSError(WSError.Type.ConnectTimeoutException, "url:"
						+ url + " " + e.getLocalizedMessage());
			} catch (SocketException e) {
				throw new WSError(WSError.Type.SocketException, "url:" + url
						+ " " + e.getLocalizedMessage());
			} catch (UnknownHostException e) {
				throw new WSError(WSError.Type.UnknownHostException, "url:"
						+ url + " " + e.getLocalizedMessage());
			}
			// request data
			HttpEntity httpEntity = httpResponse.getEntity();
			if (httpEntity != null) {
				InputStream inputStream = httpEntity.getContent();
				if (httpEntity.getContentEncoding() != null
						&& httpEntity.getContentEncoding().getValue()
								.contains("gzip")) {
					inputStream = new GZIPInputStream(inputStream);
				}
				data = convertStreamToString(inputStream);
				// cache the result
				if (requestCache != null) {
					requestCache.put(url, data);
				}
				FileUtil.writeFileSdcardFile(FileUtil.getHttpDebugFolder()
						.getPath() + "/" + System.currentTimeMillis() + ".txt",
						"doget==>" + url + " response:" + data);
				LogUtils.i("response:" + url + " response:" + data);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}
	private static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				// Log.i("tag", line);
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	public static void setRequestCache(RequestCache requestCache) {
		// Caller.requestCache = requestCache;
	}
	public static String createStringFromIds(int[] ids) {
		if (ids == null)
			return "";
		String query = "";
		for (int id : ids) {
			query = query + id + "+";
		}
		return query;
	}
	public static String doPost(String url, String[] paramNames,
			String[] paramValues) throws WSError {
		URI encodedUri = null;
		HttpPost httpPost = null;
		try {
			encodedUri = new URI(url);
			httpPost = new HttpPost(encodedUri);
		} catch (URISyntaxException e1) {
			// at least try to remove spaces
			String encodedUrl = url.replace(' ', '+');
			httpPost = new HttpPost(encodedUrl);
			e1.printStackTrace();
		}
		httpPost.addHeader("Accept-Encoding", "gzip");
		// initialize HTTP GET request objects
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpResponse httpResponse;
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		for (int i = 0; i < paramNames.length; i++) {
			params.add(new BasicNameValuePair(paramNames[i], paramValues[i]));
		}
		String data = null;
		try {
			// execute request
			try {
				HttpConnectionParams.setConnectionTimeout(
						httpClient.getParams(), HTTP_CONNTIMEOUT);
				HttpConnectionParams.setSoTimeout(httpClient.getParams(),
						HTTP_SOTIMEOUT);
				httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
				httpResponse = httpClient.execute(httpPost);
			} catch (SocketTimeoutException e) {
				throw new WSError(WSError.Type.SocketTimeoutException, "url:"
						+ url + " " + e.getLocalizedMessage());
			} catch (ConnectTimeoutException e) {
				throw new WSError(WSError.Type.ConnectTimeoutException, "url:"
						+ url + " " + e.getLocalizedMessage());
			} catch (SocketException e) {
				throw new WSError(WSError.Type.SocketException, "url:" + url
						+ " " + e.getLocalizedMessage());
			} catch (UnknownHostException e) {
				throw new WSError(WSError.Type.UnknownHostException, "url:"
						+ url + " " + e.getLocalizedMessage());
			}
			// request data
			HttpEntity httpEntity = httpResponse.getEntity();
			if (httpEntity != null) {
				InputStream inputStream = httpEntity.getContent();
				if (httpEntity.getContentEncoding() != null
						&& httpEntity.getContentEncoding().getValue()
								.contains("gzip")) {
					inputStream = new GZIPInputStream(inputStream);
				}
				data = convertStreamToString(inputStream);
				// cache the result
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			FileUtil.writeFileSdcardFile(FileUtil.getHttpDebugFolder()
					.getPath() + "/" + System.currentTimeMillis() + ".txt",
					"dopost==>" + url + " " + Arrays.toString(paramNames)
					+ "-->paramValues:" + Arrays.toString(paramValues)
					+ " response:" + data);
		} catch (IOException e) {
			e.printStackTrace();
		}
		LogUtils.i("doPost_url:" + url + " " + Arrays.toString(paramNames)
				+ "-->paramValues:" + Arrays.toString(paramValues)
				+ " response:" + data);
		return data;
	}
	public static String doGet(String url, String[] names, Object[] values)
			throws WSError {
		return doGet(BBSUtils.AssembleUrl(url, names, values));
	}
	/** 拦截器压缩http请求体，许多服务器无法解析 */
	/*static class GzipRequestInterceptor implements Interceptor {
		@Override
		public Response intercept(Chain chain) throws IOException {
			Request originalRequest = chain.request();
			if (originalRequest.body() == null || originalRequest.header("Content-Encoding") != null) {
				return chain.proceed(originalRequest);
			}
			Request compressedRequest = originalRequest.newBuilder().header("Content-Encoding", "gzip")
					.method(originalRequest.method(), gzip(originalRequest.body())).build();
			return chain.proceed(compressedRequest);
		}
		private RequestBody gzip(final RequestBody body) {
			return new RequestBody() {
				@Override
				public MediaType contentType() {
					//LogUtils.v("body.contentType()" + body.contentType().type());
					return body.contentType();
				}
				@Override
				public long contentLength() {
					return -1; // 无法知道压缩后的数据大小
				}
				@Override
				public void writeTo(BufferedSink sink) throws IOException {
					BufferedSink gzipSink = Okio.buffer(new GzipSink(sink));
					body.writeTo(gzipSink);
					gzipSink.close();
				}
			};
		}
	}*/
}
