/**
 * 
 */
package com.dvr.android.dvr.bean;

import java.util.ArrayList;

/**
 * 行车路径集合<BR>
 * 为了保证效率，数据分别存储在4个队列中
 * 
 * @author sunshine
 * @version [yecon Android_Platform, 4 Feb 2012]
 */
public class DrivePath
{

    /**
     * 路径点集�?     */
    private ArrayList<DrivePosition> positions = null;
    
    /**
     * 对应的视频文件的路径
     */
//    private String mFilePath = null;

    /**
     * 构�?
     */
    public DrivePath()
    {
        positions = new ArrayList<DrivePosition>();
    }
    
    /**
     * setting
     * @param filePath
     */
//    public void setFilePath(String filePath)
//    {
//        mFilePath = filePath;
//    }
//    
//    /**
//     * getting
//     * @return
//     */
//    public String getFilePath()
//    {
//        return mFilePath;
//    }

    /**
     * 记录�?��点，应该为一秒触发一�?     * 
     * @param p
     */
    public void recordPosition(DrivePosition p)
    {
        // 如果数据有效才进行添�?//        if (p.mValid)
//        {
            positions.add(p);
//        }
    }

    /**
     * 返回路径信息集合
     * 
     * @return
     */
    public ArrayList<DrivePosition> getPathData()
    {
        return positions;
    }

    /**
     * 重置
     */
    public void clear()
    {
        positions.clear();
    }
}
