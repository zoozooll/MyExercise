package com.tcl.base.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URI;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.content.res.AssetManager;

import com.tcl.framework.log.NLog;
import com.tcl.framework.network.http.NetworkError;  
import com.tcl.manager.util.LogUtil;

public class HttpTask extends Thread {
	
	private static final String TAG= "HttpTask";
	private static int DEFAULT_CONNECT_TIMEOUT = 10000;
	private static int DEFAULT_SO_TIMEOUT = 10000;
	
	private static final int HTTP_ENTITY_INVALID = -1001;
	private static final int HTTP_STATUS_INVALID = -1002;
	
	public interface HttpCallback {
		void onPrepared();
		void onCompleted(int ret, HttpEntity entity);
	}
	
	HttpClient mClient;
	HttpRequestBase mRequest;
	boolean mCancelled;
	HttpCallback mCallback;
	
	public HttpTask(Context context, HttpRequestBase request) {
		mRequest = request;
		mClient = createClient(context, request);
	}
	
	public void setCallback(HttpCallback callback) {
		this.mCallback = callback;
	}
	
	private KeyStore getKeyStore(Context context) {
		KeyStore trustStore = null;
		Certificate cer = null;
		
		try { 
			AssetManager am = context.getAssets(); 
			InputStream ins = am.open("tcl_account.cer");
			//读取证书  
	        CertificateFactory cerFactory = CertificateFactory.getInstance("X.509");  //问1  
	        cer = cerFactory.generateCertificate(ins);
	        
		} catch (Exception e) {
			NLog.printStackTrace(e);
		} 
		
		try {
			trustStore = KeyStore.getInstance("PKCS12", "BC");
			trustStore.load(null, null);
			if (cer != null) {
				trustStore.setCertificateEntry("trust", cer);
			}
		} catch (Exception e) {
			NLog.printStackTrace(e);
			return null;
		} 		
		
		return trustStore;
	}
	
	private boolean isHttps(HttpRequestBase request) {
		URI uri = request.getURI();
		return uri.getScheme().equalsIgnoreCase("https");
	}
	
	private HttpClient createClient(Context context, HttpRequestBase request) {
		
		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, DEFAULT_CONNECT_TIMEOUT);
		HttpConnectionParams.setSoTimeout(params, DEFAULT_SO_TIMEOUT);
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
		params.setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, HTTP.UTF_8);
		HttpProtocolParams.setHttpElementCharset(params, HTTP.UTF_8);
		
		try {
			
			if (isHttps(request)) {				
				KeyStore trustStore = getKeyStore(context);	
				
				if (trustStore == null) {
					throw new IllegalAccessException("not support https ssl socket");
				}
				
				SSLSocketFactory sf = new SSLSocketFactoryImp(trustStore);
				sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

				SchemeRegistry registry = new SchemeRegistry();
				registry.register(new Scheme("http", PlainSocketFactory
						.getSocketFactory(), 80));
				registry.register(new Scheme("https", sf, 443));

				ClientConnectionManager ccm = new ThreadSafeClientConnManager(
						params, registry);
				
				return new DefaultHttpClient(ccm, params);
			}
			else {
				return new DefaultHttpClient(params);
			}
			
			
		} catch (Exception e) {
			NLog.printStackTrace(e);
			return new DefaultHttpClient(params);
		}
		
	}
	
	public void cancel()
	{
		if (!isAlive())
			return;
		mCancelled = true;
		mRequest.abort();
		//mClient.getConnectionManager().shutdown();
		try {
			interrupt();
			join();
		} catch (InterruptedException e) {
		}
	}
	
	private void onHttpError(int err, int cause) 
	{
		NLog.w(TAG, "<%s> [%s] onHttpError err = %d, cause = %d", mRequest.getURI(), mRequest.toString(), err, cause);
		final HttpCallback callback = mCallback;
		if (callback != null) {
			callback.onCompleted(err, null);
		}
	}
	
	private void onHttpSuccess(HttpEntity entity) 
	{
		NLog.v(TAG, "<%s>onHttpSuccess", mRequest.toString());
		final HttpCallback callback = mCallback;
		if (callback != null) {
			callback.onCompleted(NetworkError.SUCCESS, entity);
		}
	}
	
	private void onCancel() 
	{
		NLog.v(TAG, "<%s>onHttpCancel", mRequest.toString());
		final HttpCallback callback = mCallback;
		if (callback != null) {
			callback.onCompleted(NetworkError.CANCEL, null);
		}
	}
	
	private void onPrepared() 
	{
		final HttpCallback callback = mCallback;
		if (callback != null) {
			callback.onPrepared();
		}
	}

	@Override
	public void run() {
		
		do {
			onPrepared();
			LogUtil.v("Httptask run:"+mRequest.getURI());
			LogUtil.v("Httptask run:"+mRequest.toString());
			NLog.i(TAG, "<%s>http request: %s",mRequest.toString(),mRequest.getURI());
			try {
				HttpResponse response = mClient.execute(mRequest);
				if (mCancelled)
					break;
				
				if (response == null || response.getStatusLine() == null)
				{
					onHttpError(NetworkError.FAIL_NOT_FOUND, HTTP_STATUS_INVALID);
					break;
				}
				
				int statusCode = response.getStatusLine().getStatusCode();	
				HttpEntity entity = response.getEntity();
				if (entity == null) {
					onHttpError(NetworkError.FAIL_IO_ERROR, HTTP_ENTITY_INVALID);
					break;
				}
				
				if (statusCode != HttpURLConnection.HTTP_OK) {
					onHttpError(NetworkError.FAIL_IO_ERROR, statusCode);
					break;
				}
				
				NLog.d(TAG, "<%s>http content-length: %d", mRequest, entity.getContentLength());
				onHttpSuccess(entity);
				
			} 
			catch(ConnectTimeoutException e) {
				NLog.printStackTrace(e);
				if (mCancelled)
					break;
				onHttpError(NetworkError.FAIL_CONNECT_TIMEOUT, 0);
			}
						
			catch (IOException e) {
				NLog.printStackTrace(e);
				if (mCancelled)
					break;
				
				onHttpError(NetworkError.FAIL_IO_ERROR, 0);
			}
			catch(Exception e) {
				NLog.printStackTrace(e);
				if (mCancelled)
					break;
				
				onHttpError(NetworkError.FAIL_UNKNOWN, 0);
			}
			
		} while(false);
		
		if (mCancelled) {
			mCancelled = false;
			onCancel();			
		}
	}
	
	static class SSLSocketFactoryImp extends SSLSocketFactory {
		final SSLContext sslContext = SSLContext.getInstance("TLS");

		public SSLSocketFactoryImp(KeyStore truststore)
				throws NoSuchAlgorithmException, KeyManagementException,
				KeyStoreException, UnrecoverableKeyException {
			super(truststore);

			TrustManager tm = new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return new X509Certificate[]{};
				}

				@Override
				public void checkClientTrusted(
						java.security.cert.X509Certificate[] chain,
						String authType)
						throws java.security.cert.CertificateException {
				}

				@Override
				public void checkServerTrusted(
						java.security.cert.X509Certificate[] chain,
						String authType)
						throws java.security.cert.CertificateException {
				}
			};
			sslContext.init(null, new TrustManager[] { tm }, null);
		}

		@Override
		public Socket createSocket(Socket socket, String host, int port,
				boolean autoClose) throws IOException, UnknownHostException {
			return sslContext.getSocketFactory().createSocket(socket, host,
					port, autoClose);
		}

		@Override
		public Socket createSocket() throws IOException {
			return sslContext.getSocketFactory().createSocket();
		}
	}
	
}
