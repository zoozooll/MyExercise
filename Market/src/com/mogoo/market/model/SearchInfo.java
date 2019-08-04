package com.mogoo.market.model;

import com.mogoo.parser.UnderlineResultCallback;

public class SearchInfo {
    
    /** 搜索URL */
    private String url = "";
    


    /**
     * 添加评论解析类
     * @author luo
     */
    public static class SearchInfoCallBack extends UnderlineResultCallback {
        
        SearchInfo searchInfo = new SearchInfo();
        
        @Override
        public Object getResult() {
            
            return searchInfo;
        }
        
        @Override
        public Object handleUnderline(String[] params) {
            if(params.length > 2){
                searchInfo.url = params[2];
            }
            return searchInfo;
        }
    }



	public String getUrl() {
		return url;
	}



	public void setUrl(String url) {
		this.url = url;
	}
    
    
}
