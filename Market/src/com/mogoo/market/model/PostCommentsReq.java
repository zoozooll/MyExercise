package com.mogoo.market.model;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.mogoo.market.network.http.HttpUrls;
import com.mogoo.market.network.http.NetworkTaskParameter;
import com.mogoo.parser.Result;

/**
 * @author csq 提交评论
 */
public abstract class PostCommentsReq extends NetWorkTaskImpl 
{
    public PostCommentsReq(Context ctx) {
        super(ctx);
    }

    @Override
    public boolean isBackgroundTask() {
        
        return true;
    }

    @Override
    public String getActionUrl() {
        
        return HttpUrls.URL_POST_COMMENT;
    }

    @Override
    public int getRequestType() {
        
        return NetworkTaskParameter.REQUEST_TYPE_GET;
    }

    @Override
    public Object getResultCallback() {
        
        return new Comments.SubmitCommentCallBack();
    }
    
    public static void onSubmitComment(Context context, Comments comments, final onRequestCallBack callBack) {
        final Comments fComments = comments;
        new PostCommentsReq(context) {
            
            @Override
            public Map<String, String> getRequestParams(Map<String, String> paramMap) {
                paramMap = new HashMap<String, String>();
                paramMap.put("apkid", fComments.getApkId());
                paramMap.put("title", URLEncoder.encode(fComments.getTitle()));
                paramMap.put("version", fComments.getVersion());
                paramMap.put("name", URLEncoder.encode(fComments.getName()));
                paramMap.put("rating", fComments.getRating());
                paramMap.put("date", fComments.getDate());
                paramMap.put("summary", URLEncoder.encode(fComments.getComment()));
                
                return super.getRequestParams(paramMap);
            }
            
            @Override
            public void onSuccess(Result result) {
                if(callBack != null)
                    callBack.onSuccess(result) ;
            }
            
            @Override
            public void onFail(Result result) {
                if(callBack != null)
                    callBack.onFail(result);
            }
        }.onRequestServer();
        
    }

}
