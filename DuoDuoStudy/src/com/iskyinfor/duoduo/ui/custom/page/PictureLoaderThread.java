package com.iskyinfor.duoduo.ui.custom.page;


import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import com.iskinfor.servicedata.CommArgs;
import com.iskinfor.servicedata.pojo.StepLesson;
import com.iskyinfor.duoduo.StaticData;

/**
 * 加载图片的线程
 * 
 * @author
 */
public class PictureLoaderThread implements Runnable {
	
    public static final String TAG = "PictureLoaderThread";

    /**
     * 数据list
     */
    private ArrayList<StepLesson> mAppList;

    /**
     * 上下文环境
     */
    @SuppressWarnings("unused")
	private Context mContext = null;

    /**
     * 去更新图片的handler
     */
    private Handler mHandler;

    private static final int ZERO = 0;

    /**
     * 构造函数
     * 
     * @param context 
     * @param handler 
     * @param appList 
     */
    public PictureLoaderThread(Context context, Handler handler, ArrayList<StepLesson> appList) {
        mContext = context;
        mHandler = handler;
        mAppList = appList;
    }

    @Override
    public void run() {
        if (mAppList != null) {
            for (int i = 0; i < mAppList.size(); i++) {
            	//加载图片
            	
                downloadImage(mAppList.get(i).getBigImgPath(), CommArgs.PATH+mAppList.get(i).getBigImgPath());
                Log.i("liu",
                		"CommArgs.PATH == >>>>>"+CommArgs.PATH+mAppList.get(i).getBigImgPath());
            }
        }
        mHandler.sendEmptyMessage(ZERO);
    }

    /**
     * 下载图片的线程
     * 
     * @param bookId 书籍id
     * @param iconUrl 图片urlַ
     */
    private void downloadImage(String fileName, String iconUrl) {
        URL url = null;
        FileOutputStream out = null;
        InputStream input = null;
        HttpURLConnection conn = null;

//        String appFileName = bookId + ".png";

        File file = new File(StaticData.IMAGE_DOWNLOAD_ADDR+File.separator + fileName);
        
        Log.i("peng", "downloadImage===:"+file.getPath());
        
        if (file.exists() && file.length() != 0)
        {
            return;
        }

        try {
            url = new URL(iconUrl);

            conn = (HttpURLConnection) url.openConnection();
            conn.connect();

            input = conn.getInputStream();

            out = new FileOutputStream(file);

            byte[] b = new byte[1024];
            int read;
            while ((read = input.read(b, 0, 1024)) > 0) 
            {
                out.write(b, 0, read);
            }
            out.flush();

        } catch (Throwable e) {
        	e.printStackTrace();
        } finally {

            if (conn != null) 
            {
                conn.disconnect();
            }
            if (input != null) 
            {
                try {
                    input.close();
                } catch (Exception e) {
                 	e.printStackTrace();
                }
            }

            if (out != null)
            {
                try {
                    out.close();
                } catch (Exception e) {
                 	e.printStackTrace();
                }
            }
        }
    }
}