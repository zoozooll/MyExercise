/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.http;

/**
 * HTTP headers
 */
public class Header {
	
	/**
	 * Content-Types that are acceptable for the response
	 * 
	 * @see <a
	 *      href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.1">14.1 Accept</a>
	 */
	public static final String ACCEPT = "Accept";
	
	/**
	 * Character sets that are acceptable
	 * 
	 * @see <a
	 *      href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.2">14.2 Accept-Charset</a>
	 */
	public static final String ACCEPT_CHARSET = "Accept-Charset";
	
	/**
	 * Acceptable encodings. 
	 * 
	 * @see <a
	 *      href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.3">14.3 Accept-Encoding</a>
	 */
	public static final String ACCEPT_ENCODING = "Accept-Encoding";
	
	/**
	 * Acceptable human languages for response
	 * 
	 * @see <a
	 *      href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.4">14.4 Accept-Language</a>
	 */
	public static final String ACCEPT_LANGUAGE = "Accept-Language";
	
	/**
	 * What partial content range types this server supports
	 * 
	 * @see <a
	 *      href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.5">14.5 Accept-Ranges</a>
	 */
	public static final String ACCEPT_RANGES = "Accept-Ranges";
	
	/**
	 * Authentication credentials for HTTP authentication
	 * 
	 * @see <a
	 *      href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.8">14.8 Authorization</a>
	 */
	public static final String AUTHORIZATION = "Authorization";
	
}
