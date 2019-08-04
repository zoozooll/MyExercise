package com.mogoo.market.model;

import android.content.Context;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.mogoo.market.config.Configuration;
import com.mogoo.market.paginate.PaginateCondition;
import com.mogoo.market.paginate.PaginateListView;
import com.mogoo.parser.XmlResultCallback;

public abstract class NetWorkTaskPageImplAppdetail extends BaseNetWork {
    
    
    protected ListView mListView;
    protected ListAdapter mListAdapter; 
    private Context context;

    public NetWorkTaskPageImplAppdetail(Context ctx) {
    
        super(ctx);
    	this.context=ctx;
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
        PaginateListView pageListView = (PaginateListView)mListView;

        pageListView.setAdapter(getListAdapter());
        pageListView.setXmlResultCallback((XmlResultCallback)getResultCallback());
        
        PaginateCondition condition = new PaginateCondition();
        condition.setPageSize(Configuration.getInstance(mContext).getPropertyInt(getPageSizeKey(), 20));
        condition.setNetworkParams(getActionUrl(), getRequestType(), getRequestParams(null));   
        pageListView.setmPaginateCondition(condition);
        pageListView.start();
        
    }

    
     
}
