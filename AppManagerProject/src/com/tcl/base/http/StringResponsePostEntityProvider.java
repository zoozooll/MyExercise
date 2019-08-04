package com.tcl.base.http;

import java.io.File;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

import com.tcl.framework.log.NLog;
import com.tcl.framework.network.http.NetworkError;

/**
 * @author difei.zou
 * @Description:
 * @date 2014/10/25 13:39
 * @copyright TCL-MIE
 */

public abstract class StringResponsePostEntityProvider implements PostEntityProvider {

	
	public StringResponsePostEntityProvider() {
		
	}
	
	public abstract void onResponse(String resp);

	
	
    @Override
    public int parse(HttpEntity entity) {
        byte[] bytes ;
        try {
            bytes = EntityUtils.toByteArray(entity);
            String jsonStr = new String(bytes, "utf-8");
            NLog.d("HTTPSERVER", "%s %s", this.getClass().getSimpleName(), jsonStr);
            onResponse(jsonStr);
        } catch (Exception e) {
            e.printStackTrace();
            return NetworkError.FAIL_UNKNOWN;
        }
        return NetworkError.SUCCESS;
    }

    @Override
    public void onError(int err) {

    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onSuccess() {

    }

    @Override
    public Map<String, String> getParams() {
        return null;
    }

    @Override
    public boolean supportPost() {
        return true;
    }

    @Override
    public Map<String, byte[]> getPostEntities() {
        return null;
    }

    @Override
    public Map<String, File> getPostFiles() {
        return null;
    }
}
