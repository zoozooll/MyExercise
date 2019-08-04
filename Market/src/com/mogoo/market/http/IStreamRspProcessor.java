package com.mogoo.market.http;

import java.io.InputStream;

/**
 * 网络消息处理器
 * 
 * @author ffshow2006
 * 
 */
public interface IStreamRspProcessor {
	/**
	 * 消息到达后调用的方法
	 * 
	 * @param response
	 */
	void onQueryResulted(InputStream response);

	/**
	 * 发送过程被取消后调用的方法
	 */
	void onCancelled();

	/**
	 * 发生错误时候
	 * 
	 * @param errorCode
	 * @see {@code org.appche.HttpStatus}
	 */
	void onError(Exception e);

}
