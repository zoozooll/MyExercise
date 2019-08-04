package com.mogoo.market.model;

import com.mogoo.market.network.http.HttpUrls;
import com.mogoo.market.network.http.NetworkTaskParameter;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import com.mogoo.parser.Result;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public abstract class UploadAppInfoReq extends NetWorkTaskImpl {

    /** 上传间隔时间 */
    public static long INTERVAL_TIME = 24 * 60 * 60 * 1000;
    
    public UploadAppInfoReq(Context ctx) {
        super(ctx);
    }

    @Override
    public boolean isBackgroundTask() {
        
        return true;
    }
    
    @Override
    public String getActionUrl() {
        
        return HttpUrls.URL_UPLOAD_APP_LIST;
    }
    
    @Override
    public int getRequestType() {
        
        return NetworkTaskParameter.REQUEST_TYPE_POST;
    }

    @Override
    public Object getResultCallback() {
        
        return new UploadAppInfo.UploadAppInfoCallBack();
    }
    
    
    //获取已经安的软件
    public Map<String, String> getInstallAPKInfoMap(Context context) {
        Map<String, String> map = new IdentityHashMap<String, String>();
        ArrayList<PackageInfo> packageInfoList = (ArrayList<PackageInfo>) context.getPackageManager().
                getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
        for(int i=0; i<packageInfoList.size(); i++){
            String pkgName = "";
            PackageInfo pkgInfo = packageInfoList.get(i);
            if(pkgInfo.packageName != null){
                pkgName = pkgInfo.packageName;
            }
            map.put(new String("app"), pkgName +":"+ pkgInfo.versionCode);
        }
        return map;
    }
    
    public String getParmaData(Context context) {
        StringBuffer sbUrl = new StringBuffer("?");
        ArrayList<PackageInfo> packageInfoList = (ArrayList<PackageInfo>) context.getPackageManager().
                getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
        for(int i=0; i<packageInfoList.size(); i++){
            String pkgName = "";
            PackageInfo pkgInfo = packageInfoList.get(i);
            if(pkgInfo.packageName != null){
                pkgName = pkgInfo.packageName;
            }
            sbUrl.append("app=");
            sbUrl.append(pkgName +":"+ pkgInfo.versionCode);
            sbUrl.append("&");
        }
        return sbUrl.toString();
    }
    @SuppressWarnings({
            "rawtypes", "unchecked"
    })
    
    public String getParmaData(HashMap<String, String> hashMap) {
        StringBuffer sbUrl = new StringBuffer("?");
        if(hashMap.size()>0){
            Set<Entry<String, String>> set = hashMap.entrySet();
            Iterator it = set.iterator();
            while (it.hasNext()) {
                Entry<String, String> entry = (Entry<String, String>) it.next();
                sbUrl.append(entry.getKey() +"="+ entry.getValue());
            }
        }
        return sbUrl.toString();
    }
    
    //上传数据
    public static void onUploadAppInfo(final Context context, final onRequestCallBack callBack) {
        
        new UploadAppInfoReq(context) {
            
            @Override
            public Map<String, String> getRequestParams(Map<String, String> paramMap) {
                paramMap = getInstallAPKInfoMap(context);
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
