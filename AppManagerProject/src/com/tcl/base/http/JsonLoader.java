package com.tcl.base.http;

import java.io.IOException;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.tcl.framework.log.NLog;
import com.tcl.framework.network.http.NetworkError;

public class JsonLoader implements AbortableInternetLoader {
	
	final PostJsonProvider mProvider;
	HttpLoader mListLoader;
	
	public JsonLoader(JsonProvider provider) {
		
		if (provider instanceof PostJsonProvider)
			this.mProvider = (PostJsonProvider)provider;
		else 			
			this.mProvider = new WrappedJsonProvider(provider);
		
		mListLoader = new HttpLoader(provider.getURL());
		mListLoader.setCallback(mLoadCallback);
	}
		
	public boolean load() {
		mListLoader.cancel();
		Map<String, String> paramsMap = mProvider.getParams();
		Map<String, byte[]> entityMap = mProvider.getPostEntities();
		
		return mListLoader.load(paramsMap, entityMap, mProvider.supportPost());
	}


	public void cancel() {
		mListLoader.cancel();
	}
	
	private JSONObject toJson(HttpEntity entity) {
		JSONObject obj = null;
		try {
			byte[] bytes = EntityUtils.toByteArray(entity);
//			byte[] md5 = MD5.encode16(bytes);
//			String hash = StringUtils.bytesToHexes(md5);
			String jsonStr = new String(bytes, "utf-8");
			NLog.i("JsonLoader", "json result: %s", jsonStr);
			obj = new SmartJSONObject(jsonStr);
//			obj.put(JsonConstants.HASH, hash);
			return obj;
		} catch (ParseException e) {
			NLog.printStackTrace(e);
		} catch (IOException e) {
			NLog.printStackTrace(e);
		} catch (JSONException e) {
			NLog.printStackTrace(e);	
		} 
		
		return null;
	}
	
	private HttpLoader.LoadCallback mLoadCallback = new HttpLoader.LoadCallback() {

		@Override
		public void onLoaded(int err, HttpEntity entity) {
			
			if (err == NetworkError.SUCCESS && entity != null) {
				JSONObject obj = toJson(entity);
				if (obj == null) {
					err = NetworkError.FAIL_IO_ERROR;
				}
				else {
					err = mProvider.parse(obj);
				}
				
				if (err == NetworkError.SUCCESS) {
					mProvider.onSuccess();
				} else {
					mProvider.onError(err);
				}
			}
			
			else if (err == NetworkError.CANCEL) {
				mProvider.onCancel();
			}
			
			else {				
				mProvider.onError(err);
			}
		}
		
	};
	
	static class WrappedJsonProvider extends FilterJsonProvider implements PostJsonProvider {

		public WrappedJsonProvider(JsonProvider provider) {
			super(provider);
		}	

		@Override
		public Map<String, byte[]> getPostEntities() {
			return null;
		}
	}
}
