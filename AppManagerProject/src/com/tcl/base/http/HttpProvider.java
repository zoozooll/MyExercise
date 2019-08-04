package com.tcl.base.http;

import java.util.Map;

/** 
 * @Description: http资源接口
 * @author wenbiao.xie 
 * @date 2014年10月8日 下午5:31:42 
 * @copyright TCL-MIE
 */

public interface HttpProvider {
	/**
	 * 获取服务网络地址
	 * @return 返回接口地址
	 */
	String getURL();
	
	/**
	 * 判断是否支持Post方法
	 * @return 返回是否支持Post方法的判断结果
	 */
	boolean supportPost();
	
	/**
	 * 获取请求参数集
	 * @return 返回参数值对
	 */
	Map<String, String> getParams();	
	
	/**
	 * 操作成功回调
	 */
	void onSuccess();
	
	/**
	 * 操作取消回调
	 */
	void onCancel();
	
	/**
	 * 发生错误时的回调
	 * @param err 错误码
	 */
	void onError(int err);
}
