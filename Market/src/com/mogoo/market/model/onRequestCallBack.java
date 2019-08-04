package com.mogoo.market.model;

import com.mogoo.parser.Result;

public interface onRequestCallBack {
    
    /**
     * 请求失败
     */
    public void onFail(Result result);
    
    /**
     * 请求成功
     */
    public void onSuccess(Result result);

}
