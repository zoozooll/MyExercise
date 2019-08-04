/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.http;

import java.io.IOException;
import java.util.Arrays;

import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthSchemeFactory;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * A REST client based on loopj's AsyncHttpClient.
 */
public class RestClient {
	
	private final String TAG = getClass().getSimpleName();
	
	private static final String HOST = "portal.meeptablet.com";
	private static final String SCHEME = "https";
	private static final String VERSION = "1";
	private static final String BASE_URL = SCHEME + "://" + HOST + "/" + VERSION;
	
	private final String PREEMPTIVE_AUTH = "http.auth.scheme-preemptive";
	
	private final AsyncHttpClient mClient;
	
	/**
	 * Creates a new RestClient
	 */
	public RestClient() {
		mClient = new AsyncHttpClient();
		
		AuthSchemeFactory schemeFactory = new OSTSchemeFactory();
		DefaultHttpClient client = (DefaultHttpClient) mClient.getHttpClient();
		client.getAuthSchemes().register(OSTAuthScheme.NAME, schemeFactory);
		
		HttpContext context = mClient.getHttpContext();
		context.setAttribute(ClientContext.AUTH_SCHEME_PREF, Arrays.asList(OSTAuthScheme.NAME));
		context.setAttribute(PREEMPTIVE_AUTH, schemeFactory);
		
		client.addRequestInterceptor(new PreemptiveAuthInterceptor());
	}
	
	/**
     * Perform a HTTP GET request, without any parameters.
     * @param url the URL to send the request to.
     * @param responseHandler the response handler instance that should handle the response.
     */
	public void get(String url, AsyncHttpResponseHandler responseHandler) {
		get(url, null, responseHandler);
	}
	
	/**
     * Perform a HTTP GET request with parameters.
     * @param url the URL to send the request to.
     * @param params additional GET parameters to send with the request.
     * @param responseHandler the response handler instance that should handle the response.
     */
	public void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		mClient.get(getAbsoluteUrl(url), params, responseHandler);
	}
	
	/**
     * Perform a HTTP POST request, without any parameters.
     * @param url the URL to send the request to.
     * @param responseHandler the response handler instance that should handle the response.
     */
	public void post(String url, AsyncHttpResponseHandler responseHandler) {
		post(url, null, responseHandler);
	}
	
	/**
     * Perform a HTTP POST request with parameters.
     * @param url the URL to send the request to.
     * @param params additional POST parameters or files to send with the request.
     * @param responseHandler the response handler instance that should handle the response.
     */
	public void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		mClient.post(getAbsoluteUrl(url), params, responseHandler);
	}
	
	/**
	 * Sets authorization token for the request.
	 * 
	 * @param token the authorization token
	 */
	public void setCredentials(String token) {
		Log.d(TAG, "Token: " + token);
		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials("OST", token);
		AuthScope authScope = new AuthScope(HOST, AuthScope.ANY_PORT, AuthScope.ANY_REALM, OSTAuthScheme.NAME);
		
		DefaultHttpClient client = (DefaultHttpClient) mClient.getHttpClient();
		client.getCredentialsProvider().setCredentials(authScope, credentials);
	}
	
	private String getAbsoluteUrl(String relativeUrl) {
		boolean isRelativeUrl = relativeUrl == null ? false : !relativeUrl.startsWith("/");
		relativeUrl = isRelativeUrl ? "/" + relativeUrl : relativeUrl;
		return BASE_URL + relativeUrl;
	}
	
	/**
	 * The request interceptor to perform preemptive authentication
	 */
	private final class PreemptiveAuthInterceptor implements HttpRequestInterceptor {

		@Override
		public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
			AuthState authState = (AuthState) context.getAttribute(ClientContext.TARGET_AUTH_STATE);
			
			// If no auth scheme available yet, try to initialize it
			// preemptively
	        if (authState.getAuthScheme() == null) {
	        	AuthSchemeFactory schemeFactory = (AuthSchemeFactory) context.getAttribute(PREEMPTIVE_AUTH);
	        	AuthScheme scheme = schemeFactory == null ? null : schemeFactory.newInstance(request.getParams());
	        	CredentialsProvider provider = (CredentialsProvider) context.getAttribute(ClientContext.CREDS_PROVIDER);
                HttpHost host = (HttpHost) context.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
	        	
                Credentials credentials = null;
	        	if (scheme != null && provider != null && host != null) {
	        		credentials = provider.getCredentials(new AuthScope(host.getHostName(), host.getPort()));
	        	}
	        	
	        	// We can only perform preemptive authentication if a credentials was provided
	        	if (credentials != null) {
	        		authState.setAuthScheme(scheme);
	        		authState.setCredentials(credentials);
	        		request.addHeader(scheme.authenticate(credentials, request));
	        	}
	        }
		}
		
	}
	
	/**
	 * Implements the AuthSchemeFactory interface
	 */
	private final class OSTSchemeFactory implements AuthSchemeFactory {

		@Override
		public AuthScheme newInstance(HttpParams params) {
			return new OSTAuthScheme();
		}
		
	}
	
	/**
	 * A custom auth scheme that uses the user token as the authentication string
	 */
	private final class OSTAuthScheme implements AuthScheme {
		
		public static final String NAME = "OST";

		@Override
		public Header authenticate(Credentials credentials, HttpRequest request) throws AuthenticationException {
			if (credentials == null) {
				throw new AuthenticationException(request + " cannot be processed without a credential");
			}
			return new BasicHeader(
					com.oregonscientific.meep.http.Header.AUTHORIZATION, 
					NAME + " " + credentials.getPassword());
		}

		@Override
		public String getParameter(String name) {
			return null;
		}

		@Override
		public String getRealm() {
			// This scheme does not use realms
			return null;
		}

		@Override
		public String getSchemeName() {
			return NAME;
		}

		@Override
		public boolean isComplete() {
			// We are not a challenge based scheme so this is always true
			return true;
		}

		@Override
		public boolean isConnectionBased() {
			// This is not a connected based scheme
			return false;
		}

		@Override
		public void processChallenge(Header header) throws MalformedChallengeException {
			// Nothing to do here, this is not a challenge based auth scheme.
			// See NTLMScheme for a good example.
		}
		
	}

}
