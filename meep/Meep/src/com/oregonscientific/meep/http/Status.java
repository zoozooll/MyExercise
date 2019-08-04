/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.http;

/**
 * Status to return after handling a call.
 * 
 * @author Stanley Lam
 */
public final class Status {
	
	/**
   * The request could not be understood by the server due to malformed
   * syntax.
   * 
   * @see <a
   *      href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.4.1">HTTP
   *      RFC - 10.4.1 400 Bad Request</a>
   */
  public static final int CLIENT_ERROR_BAD_REQUEST = 400;
  
  /**
   * The server has not found anything matching the Request-URI or the server
   * does not wish to reveal exactly why the request has been refused, or no
   * other response is applicable.
   * 
   * @see <a
   *      href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.4.5">HTTP
   *      RFC - 10.4.5 404 Not Found</a>
   */
  public static final int CLIENT_ERROR_NOT_FOUND = 404;
  
  /**
   * This code is reserved for future use.
   * 
   * @see <a
   *      href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.4.3">HTTP
   *      RFC - 10.4.3 402 Payment Required</a>
   */
  public static final int CLIENT_ERROR_PAYMENT_REQUIRED = 402;
  
  /**
   * This code is similar to 401 (Unauthorized), but indicates that the client
   * must first authenticate itself with the proxy.
   * 
   * @see <a
   *      href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.4.8">HTTP
   *      RFC - 10.4.8 407 Proxy Authentication Required</a>
   */
  public static final int CLIENT_ERROR_PROXY_AUTHENTIFICATION_REQUIRED = 407;

  /**
   * The server is refusing to process a request because the request entity is
   * larger than the server is willing or able to process.
   * 
   * @see <a
   *      href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.4.14">HTTP
   *      RFC - 10.4.14 413 Request Entity Too Large</a>
   */
  public static final int CLIENT_ERROR_REQUEST_ENTITY_TOO_LARGE = 413;

  /**
   * Sent by the server when an HTTP client opens a connection, but has never
   * sent a request (or never sent the blank line that signals the end of the
   * request).
   * 
   * @see <a
   *      href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.4.9">HTTP
   *      RFC - 10.4.9 408 Request Timeout</a>
   */
  public static final int CLIENT_ERROR_REQUEST_TIMEOUT = 408;
  
  /**
   * The request could not be completed due to a conflict with the current
   * state of the resource (as experienced in a version control system).
   * 
   * @see <a
   *      href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.4.10">HTTP
   *      RFC - 10.4.10 409 Conflict</a>
   */
  public static final int CLIENT_ERROR_CONFLICT = 409;

  /**
   * The server is refusing to service the request because the Request-URI is
   * longer than the server is willing to interpret.
   * 
   * @see <a
   *      href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.4.15">HTTP
   *      RFC - 10.4.15 414 Request-URI Too Long</a>
   */
  public static final int CLIENT_ERROR_REQUEST_URI_TOO_LONG = 414;

  /**
   * The request includes a Range request-header field and the selected
   * resource is too small for any of the byte-ranges to apply.
   * 
   * @see <a
   *      href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.4.17">HTTP
   *      RFC - 10.4.17 416 Requested Range Not Satisfiable</a>
   */
  public static final int CLIENT_ERROR_REQUESTED_RANGE_NOT_SATISFIABLE = 416;
  
  /**
   * The user agent expects some behavior of the server (given in an Expect
   * request-header field), but this expectation could not be met by this
   * server.
   * 
   * @see <a
   *      href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.4.18">HTTP
   *      RFC - 10.4.18 417 Expectation Failed</a>
   */
  public static final int CLIENT_ERROR_EXPECTATION_FAILED = 417;

  /**
   * The request requires user authentication.
   * 
   * @see <a
   *      href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.4.2">HTTP
   *      RFC - 10.4.2 401 Unauthorized</a>
   */
  public static final int CLIENT_ERROR_UNAUTHORIZED = 401;
  
  /**
   * This status code means that the method could not be performed on the
   * resource because the requested action depended on another action and that
   * action failed.
   * 
   * @see <a href="http://www.webdav.org/specs/rfc2518.html#STATUS_424">WEBDAV
   *      RFC - 10.5 424 Failed Dependency</a>
   */
  public static final int CLIENT_ERROR_FAILED_DEPENDENCY = 424;
  
  /**
   * The requested resource is no longer available at the server and no
   * forwarding address is known.
   * 
   * @see <a
   *      href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.4.11">HTTP
   *      RFC - 10.4.11 410 Gone</a>
   */
  public static final int CLIENT_ERROR_GONE = 410;

  /**
   * The server refuses to accept the request without a defined
   * Content-Length.
   * 
   * @see <a
   *      href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.4.12">HTTP
   *      RFC - 10.4.12 411 Length Required</a>
   */
  public static final int CLIENT_ERROR_LENGTH_REQUIRED = 411;

  /**
   * The source or destination resource of a method is locked (or temporarily
   * involved in another process).
   * 
   * @see <a href="http://www.webdav.org/specs/rfc2518.html#STATUS_423">WEBDAV
   *      RFC - 10.4 423 Locked</a>
   */
  public static final int CLIENT_ERROR_LOCKED = 423;

  /**
   * The method specified in the Request-Line is not allowed for the resource
   * identified by the Request-URI.
   * 
   * @see <a
   *      href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.4.6">HTTP
   *      RFC - 10.4.6 405 Method Not Allowed</a>
   */
  public static final int CLIENT_ERROR_METHOD_NOT_ALLOWED = 405;

  /**
   * The resource identified by the request is only capable of generating
   * response entities whose content characteristics do not match the user's
   * requirements (in Accept* headers).
   * 
   * @see <a
   *      href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.4.7">HTTP
   *      RFC - 10.4.7 406 Not Acceptable</a>
   */
  public static final int CLIENT_ERROR_NOT_ACCEPTABLE = 406;

  /**
   * Sent by the server when the user agent asks the server to carry out a
   * request under certain conditions that are not met.
   * 
   * @see <a
   *      href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.4.13">HTTP
   *      RFC - 10.4.13 412 Precondition Failed</a>
   */
  public static final int CLIENT_ERROR_PRECONDITION_FAILED = 412;

  /**
   * This status code means the server understands the content type of the
   * request entity (syntactically correct) but was unable to process the
   * contained instructions.
   * 
   * @see <a href="http://www.webdav.org/specs/rfc2518.html#STATUS_422">WEBDAV
   *      RFC - 10.3 422 Unprocessable Entity</a>
   */
  public static final int CLIENT_ERROR_UNPROCESSABLE_ENTITY = 422;

  /**
   * The server is refusing to service the request because the entity of the
   * request is in a format not supported by the requested resource for the
   * requested method.
   * 
   * @see <a
   *      href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.4.16">HTTP
   *      RFC - 10.4.16 415 Unsupported Media Type</a>
   */
  public static final int CLIENT_ERROR_UNSUPPORTED_MEDIA_TYPE = 415;

  /**
   * The server understood the request, but is refusing to fulfill it as it
   * could be explained in the entity.
   * 
   * @see <a
   *      href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.4.4">HTTP
   *      RFC - 10.4.4 403 Forbidden</a>
   */
  public static final int CLIENT_ERROR_FORBIDDEN = 403;
  
  /**
   * The server encountered an unexpected condition which prevented it from
   * fulfilling the request.
   * 
   * @see <a
   *      href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.5.1">HTTP
   *      RFC - 10.5.1 500 Internal Server Error</a>
   */
  public static final int SERVER_ERROR_INTERNAL = 500;

  /**
   * The server does not support the functionality required to fulfill the
   * request.
   * 
   * @see <a
   *      href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.5.2">HTTP
   *      RFC - 10.5.2 501 Not Implemented</a>
   */
  public static final int SERVER_ERROR_NOT_IMPLEMENTED = 501;

  /**
   * The server is currently unable to handle the request due to a temporary
   * overloading or maintenance of the server.
   * 
   * @see <a
   *      href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.5.4">HTTP
   *      RFC - 10.5.4 503 Service Unavailable</a>
   */
  public static final int SERVER_ERROR_SERVICE_UNAVAILABLE = 503;
  
  /**
   * The server, while acting as a gateway or proxy, could not connect to the
   * upstream server.
   * 
   * @see <a
   *      href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.5.5">HTTP
   *      RFC - 10.5.5 504 Gateway Timeout</a>
   */
  public static final int SERVER_ERROR_GATEWAY_TIMEOUT = 504;

  /**
   * The server does not support, or refuses to support, the HTTP protocol
   * version that was used in the request message.
   * 
   * @see <a
   *      href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.5.6">HTTP
   *      RFC - 10.5.6 505 HTTP Version Not Supported</a>
   */
  public static final int SERVER_ERROR_VERSION_NOT_SUPPORTED = 505;
	
	/**
   * The request has been accepted for processing, but the processing has not
   * been completed.
   * 
   * @see <a
   *      href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.2.3">HTTP
   *      RFC - 10.2.3 202 Accepted</a>
   */
  public static final int SUCCESS_ACCEPTED = 202;
  
  /**
   * The request has been fulfilled and resulted in a new resource being
   * created.
   * 
   * @see <a
   *      href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.2.2">HTTP
   *      RFC - 10.2.2 201 Created</a>
   */
  public static final int SUCCESS_CREATED = 201;
  
  /**
   * The server has fulfilled the request but does not need to return an
   * entity-body (for example after a DELETE), and might want to return
   * updated meta-information.
   * 
   * @see <a
   *      href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.2.5">HTTP
   *      RFC - 10.2.5 204 No Content</a>
   */
  public static final int SUCCESS_NO_CONTENT = 204;
  
  /**
   * The request has succeeded but the returned meta-information in the
   * entity-header does not come from the origin server, but is gathered from
   * a local or a third-party copy.
   * 
   * @see <a
   *      href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.2.4">HTTP
   *      RFC - 10.2.4 203 Non-Authoritative Information</a>
   */
  public static final int SUCCESS_NON_AUTHORITATIVE = 203;
  
  /**
   * The request has succeeded.
   * 
   * @see <a
   *      href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.2.1">HTTP
   *      RFC - 10.2.1 200 OK</a>
   */
  public static final int SUCCESS_OK = 200;
  
  /**
   * The server has fulfilled the partial GET request for the resource
   * assuming the request has included a Range header field indicating the
   * desired range.
   * 
   * @see <a
   *      href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.2.7">HTTP
   *      RFC - 10.2.7 206 Partial Content</a>
   */
  public static final int SUCCESS_PARTIAL_CONTENT = 206;
  
  /**
   * The server has fulfilled the request and the user agent SHOULD reset the
   * document view which caused the request to be sent.
   * 
   * @see <a
   *      href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.2.6">HTTP
   *      RFC - 10.2.6 205 Reset Content</a>
   */
  public static final int SUCCESS_RESET_CONTENT = 205;
  
  /**
   * Warning status code, typically returned by a cache or a proxy, indicating
   * that the response has been transformed.
   * 
   * @see <a
   *      href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.46">HTTP
   *      RFC - 14.46 Warning</a>
   */
  public static final int SUCCESS_TRANSFORMATION_APPLIED = 214;
  
  /**
   * Indicates if the status is a client error status, meaning "The request
   * contains bad syntax or cannot be fulfilled".
   * 
   * @param code
   *            The code of the status.
   * @return True if the status is a client error status.
   */
  public static boolean isClientError(int code) {
      return (code >= 400) && (code <= 499);
  }

  /**
   * Indicates if the status is a connector error status, meaning "The
   * connector failed to send or receive an apparently valid message".
   * 
   * @param code
   *            The code of the status.
   * @return True if the status is a server error status.
   */
  public static boolean isConnectorError(int code) {
      return (code >= 1000) && (code <= 1099);
  }

  /**
   * Indicates if the status is an error (client or server) status.
   * 
   * @param code
   *            The code of the status.
   * @return True if the status is an error (client or server) status.
   */
  public static boolean isError(int code) {
		return isClientError(code) || isServerError(code) || isConnectorError(code);
  }
  
  /**
   * Indicates if the status is a server error status, meaning "The server
   * failed to fulfill an apparently valid request".
   * 
   * @param code
   *            The code of the status.
   * @return True if the status is a server error status.
   */
  public static boolean isServerError(int code) {
      return (code >= 500) && (code <= 599);
  }

}
