package com.mogoo.market.model;

import com.mogoo.market.R;
import com.mogoo.market.config.Configuration;
import com.mogoo.market.paginate.PaginateCondition;
import com.mogoo.market.paginate.PaginateListView_1;
import android.content.Context;
import com.mogoo.parser.XmlResultCallback;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public abstract class NetWorkTaskPageImpl extends BaseNetWork {
    
    
    protected ListView mListView;
    protected ListAdapter mListAdapter; 
    protected Context mContext;
    protected Object mResultCallback;
    protected String mBaseUrl;
    
    public NetWorkTaskPageImpl(Context ctx) {
    
        super(ctx);
    	this.mContext=ctx;
    }
    
    
    /**
     * 列表适配器
     * @return
     */
    public abstract ListAdapter getListAdapter();
    
    /**
     * 显示列表的视图
     * @return
     */
    public abstract ListView getListView();
    
    /**
     * 分页大小属性key
     * @return
     */
    public String getPageSizeKey(){
        return "";
    }

    
    
    @SuppressWarnings("rawtypes")
    public void onStart() { 
        PaginateListView_1 pageListView = (PaginateListView_1)mListView;

        pageListView.setAdapter(getListAdapter());
        pageListView.setXmlResultCallback((XmlResultCallback)getResultCallback());
        
//        PaginateCondition condition = new PaginateCondition();
//        condition.setPageSize(Configuration.getInstance(mContext).getPropertyInt(getPageSizeKey(), 20));
//        condition.setNetworkParams(getActionUrl(), getRequestType(), getRequestParams(null));   
//        pageListView.setmPaginateCondition(condition);
//        pageListView.start();
        
    }

    
     
}
