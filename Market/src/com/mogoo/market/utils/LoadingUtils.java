package com.mogoo.market.utils;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.mogoo.market.database.DaoFactory;
import com.mogoo.market.database.dao.IBeanDao;
import com.mogoo.market.http.HttpGetURIBuilder;
import com.mogoo.market.http.HttpSendAndRecvTask;
import com.mogoo.market.http.IPageOperator;
import com.mogoo.market.http.IStreamRspProcessor;
import com.mogoo.market.model.HotApp;
import com.mogoo.market.network.http.ErrorCode;
import com.mogoo.market.network.http.ErrorCodeUtils;
import com.mogoo.market.network.http.HttpUrls;
import com.mogoo.market.paginate.PaginateListView_1;
import com.mogoo.market.ui.LoadingFirstPage;
import com.mogoo.market.ui.MarketGroupActivity;
import com.mogoo.parser.Result;
import com.mogoo.parser.XmlResultCallback;
import com.mogoo.parser.XmlResultParser;

/*加载页面拉取数据工具类*/
public class LoadingUtils implements IStreamRspProcessor{
	private Context mContext;
	private XmlResultCallback mCallbackResult;
	private PaginateListView_1 firstListView;
	private HttpGetURIBuilder mUriBuilder;
	private XmlResultCallback xmlResultCallback;
	private ArrayList<HotApp> resultArrayList;
	private IBeanDao<HotApp> hotDao;
	
	public LoadingUtils(Context context){
		super();
		this.mContext = context;
		hotDao = DaoFactory.getHotDao(mContext);
		firstListView = new PaginateListView_1<HotApp>(mContext);
		xmlResultCallback = new HotApp.HotAppListCallback();
		resultArrayList = new ArrayList<HotApp>();
	}

	public void requestData() {
		doFirstQuery();
		
		firstListView.setXmlResultCallback(new HotApp.HotAppListCallback());
		mCallbackResult = new HotApp.HotAppListCallback();
	}

	private void doFirstQuery(){
		mUriBuilder = new FirstSearchURIBuilder();
		doQuery();
	}
	
	protected void doQuery() {
		new HttpSendAndRecvTask(false, this, mContext).execute(mUriBuilder);
	}
	
	private class FirstSearchURIBuilder extends HttpGetURIBuilder implements IPageOperator {

		private Map<String, String> params = new HashMap<String, String>();
		private int pageNo = 1;

		FirstSearchURIBuilder() {
			params.put("page", String.valueOf(pageNo));
		}

		@Override
		public Map<String, String> getParams() {
			return params;
		}

		@Override
		public String getURIString() {
			return HttpUrls.createBaseUrlWithPairs(HttpUrls.URL_RECOMMEND_APP_LIST);
		}

		public void toNextPage() {
			pageNo++;
			params.put("page", String.valueOf(pageNo));
		}

		public void toPreviousPage() {
			pageNo--;
			if (pageNo == 0) {
				pageNo = 1;
			}
			params.put("page", String.valueOf(pageNo));
		}
	
	}

	@Override
	public void onQueryResulted(InputStream response) {
		// TODO Auto-generated method stub
		Intent longdingPageFailIntent=new Intent();
		Result result = XmlResultParser.parser(response, xmlResultCallback);
		if (result != null) {
			String errorCode = result.getErrorCode();
			if (ErrorCode.isSuccessCode(errorCode)) {
				if (result.getData() != null) {

					ArrayList<HotApp> arrayList = (ArrayList<HotApp>) result.getData();
					int count = arrayList.size();
					for (HotApp t : arrayList) {
						resultArrayList.add(t);
					}
					hotDao.addBeans(arrayList);
					Bundle extraBundle = new Bundle();
					extraBundle.putSerializable("reslut", resultArrayList);
					Intent intent=new Intent(mContext,MarketGroupActivity.class);
					intent.putExtra("result", resultArrayList);
					intent.putExtra("tabnum", MarketGroupActivity.TAB_MAIN);
					
					Intent longdingPageIntent=new Intent();
					longdingPageIntent.setAction("com.android.loadingpagesuccess");
			    	mContext.sendBroadcast(longdingPageIntent);
					mContext.startActivity(intent);
				}

			} else {
				// 显示错误信息
				ErrorCodeUtils.showErrorToast(mContext, errorCode);
				
				longdingPageFailIntent.setAction("com.android.loadingpagefail");
		    	mContext.sendBroadcast(longdingPageFailIntent);
				
			}
		}
		else{
			longdingPageFailIntent.setAction("com.android.loadingpagefail");
	    	mContext.sendBroadcast(longdingPageFailIntent);
		}
	}

	@Override
	public void onCancelled() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(Exception e) {
		// TODO Auto-generated method stub
		
	}
	
	
}
