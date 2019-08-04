package com.tcl.base.http;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;

import android.text.TextUtils;

import com.tcl.framework.log.NLog;
import com.tcl.framework.network.http.NetworkError;

/** 
 * @Description: 
 * @author wenbiao.xie 
 * @date 2014年10月8日 下午6:14:43 
 * @copyright TCL-MIE
 */

public abstract class StringProvider implements EntityProvider, EntityParser {
	
	String mDefaultCharset;
	public StringProvider() {
		this("utf-8");
	}
	
	public StringProvider(String charset) {
		this.mDefaultCharset = charset;
	}
	
	@Override
	public int parse(HttpEntity entity) {
		int err = NetworkError.FAIL_UNKNOWN;
		try {
			String content = EntityUtils.toString(entity, mDefaultCharset);
			if (TextUtils.isEmpty(content))
				err = NetworkError.FAIL_IO_ERROR;
			else 
				err = parseReponse(content);
		} catch (ParseException e) {
			NLog.printStackTrace(e);
			err = NetworkError.FAIL_IO_ERROR;
		} catch (IOException e) {
			NLog.printStackTrace(e);
			err = NetworkError.FAIL_IO_ERROR;
		}
		return err;
	}
	
	protected abstract int parseReponse(String content);

}
