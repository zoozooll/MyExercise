
package com.iskyinfor.duoduo.downloadManage;

import java.util.ArrayList;

/**
 * 下载任务容器类
 * 
 * @author pKF29007
 */
class DownloadQuene extends ArrayList<DownloadTask> {

    /**
     * 
     */
    private static final long serialVersionUID = 8694469130783309275L;

    /**
     * 通过resId获得下载队列中的下载任务
     * 
     * @param resId
     * @return 下载任务对象
     */
    public DownloadTask getDownloadTaskByResid(String resId) {
        DownloadTask item = null;
        for (int i = 0; i < this.size(); i++) {
            item = this.get(i);
            if (item.resourceId.equals(resId)) {
                return item;
            } else {
                item = null;
            }
        }
        return item;
    }

    /**
     * 判断是否包某ResId对应的对象是否已存在
     * 
     * @param resId
     * @return true:包含；false：不包含
     */
    private boolean containsByResId(String resId) {
        boolean result = false;
        DownloadTask item = null;
        for (int i = 0; i < this.size(); i++) {
            item = this.get(i);
            if (item.resourceId.equals(resId)) {
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * 通过RESID来移除下载队列中的下载任务
     * 
     * @param resId
     * @return true:移除成功 ；false:移除失败
     */
    public boolean removeDownloadTaskByResid(String resId) {
        boolean result = false;
        if (containsByResId(resId)) {
            DownloadTask info = this.getDownloadTaskByResid(resId);
            this.remove(info);
            result = true;
        }
        return result;
    }
    
    
}
