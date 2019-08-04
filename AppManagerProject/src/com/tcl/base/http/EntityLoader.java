package com.tcl.base.http;

import java.io.File;
import java.util.Map;

import org.apache.http.HttpEntity;

import com.tcl.framework.network.http.NetworkError; 

/** 
 * @Description: 
 * @author wenbiao.xie 
 * @date 2014年10月8日 下午5:30:48 
 * @copyright TCL-MIE
 */

public class EntityLoader implements AbortableInternetLoader {
	
	final PostEntityProvider mProvider;
	HttpLoader mListLoader; 
	public EntityLoader(EntityProvider provider) {
		if (provider instanceof PostEntityProvider)
			this.mProvider = (PostEntityProvider)provider;
		else {
			this.mProvider = new WrappedEntityProvider(provider);
		} 
		mListLoader = new HttpLoader(provider.getURL());
		mListLoader.setCallback(mLoadCallback);
	}

	@Override
	public boolean load() {
		mListLoader.cancel();
		Map<String, String> paramsMap = mProvider.getParams();
		Map<String, byte[]> entityMap = mProvider.getPostEntities();
		
		return mListLoader.load(paramsMap, entityMap, mProvider.supportPost());
	}

    public boolean load(boolean sync) {
        mListLoader.cancel();
        Map<String, String> paramsMap = mProvider.getParams();
        Map<String, byte[]> entityMap = mProvider.getPostEntities();
        Map<String, File> fileMap = mProvider.getPostFiles();
        if( sync) {
            return mListLoader.syncload(paramsMap, entityMap, fileMap, mProvider.supportPost());
        }
        else {
            return mListLoader.load(paramsMap, entityMap, fileMap, mProvider.supportPost());
        }
    }

	@Override
	public void cancel() {
		mListLoader.cancel();
	}
	
	private HttpLoader.LoadCallback mLoadCallback = new HttpLoader.LoadCallback() {

		@Override
		public void onLoaded(int err, HttpEntity entity) {
			
			if (err == NetworkError.SUCCESS && entity != null) {
				err = mProvider.parse(entity);
				
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
	
	static class WrappedEntityProvider implements PostEntityProvider {
		
		final EntityProvider innerProvider;
		public WrappedEntityProvider(EntityProvider provider) {
			this.innerProvider = provider;
		}

		@Override
		public String getURL() {
			return innerProvider.getURL();
		}

		@Override
		public boolean supportPost() {
			return innerProvider.supportPost();
		}

		@Override
		public Map<String, String> getParams() {
			return innerProvider.getParams();
		}

		@Override
		public void onSuccess() {
			innerProvider.onSuccess();
		}

		@Override
		public void onCancel() {
			innerProvider.onCancel();
		}

		@Override
		public void onError(int err) {
			innerProvider.onError(err);
		}

		@Override
		public int parse(HttpEntity entity) {
			return innerProvider.parse(entity);
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

}
