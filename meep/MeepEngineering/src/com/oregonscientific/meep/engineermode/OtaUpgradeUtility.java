package com.oregonscientific.meep.engineermode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.os.RecoverySystem;

public class OtaUpgradeUtility {
    public static final int ERROR_INVALID_UPGRADE_PACKAGE = 0;
    public static final int ERROR_FILE_DOES_NOT_EXIST = 1;
    public static final String CACHE_PARTITION = "/cache/";
    public static final String DEFAULT_PACKAGE_NAME = "update.zip";
    private Context mContext;
    private boolean mDeleteSource = false;

    public OtaUpgradeUtility(Context context) {
        mContext = context;
    }

    public interface ProgressListener extends RecoverySystem.ProgressListener {
        @Override
        public void onProgress(int progress);
        public void onVerifyFailed(int errorCode, Object object);
        public void onCopyProgress(int progress);
        public void onCopyFailed(int errorCode, Object object);
    }

    public boolean beginUpgrade(ProgressListener progressListener) {
        try {
            RecoverySystem.installPackage(mContext, new File(CACHE_PARTITION + DEFAULT_PACKAGE_NAME));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean beginUpgrade(File packageFile, ProgressListener progressListener) {
        boolean copySuccess = copyFile(packageFile, new File(CACHE_PARTITION + DEFAULT_PACKAGE_NAME), progressListener);
        if (copySuccess && mDeleteSource) {
            packageFile.delete();
        }
        if (copySuccess) {
	          return beginUpgrade(progressListener);
        }
        return false;
    }
    
    public boolean beginUpgrade(String packagePath, ProgressListener progressListener) {
    	if(packagePath.equals(CACHE_PARTITION+DEFAULT_PACKAGE_NAME))
    		return beginUpgrade(progressListener);
    	else
    		return beginUpgrade(new File(packagePath), progressListener);
    }

    public boolean verifyPackage(File packageFile) {
        try {
            RecoverySystem.verifyPackage(packageFile, null, null);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean verifyPackage(String packagePath) {
        return verifyPackage(new File(packagePath));
    }
    
    public static boolean copyFile(File src, File dst, ProgressListener listener) {
        long inSize = src.length();
        long outSize = 0;
        int progress = 0;
        listener.onCopyProgress(progress);
        try {
            if (!dst.exists()) {
                dst.createNewFile();
            }
            FileInputStream in = new FileInputStream(src);
            FileOutputStream out = new FileOutputStream(dst);
            int length = -1;
            byte[] buf = new byte[1024];
            while ((length = in.read(buf)) != -1) {
                out.write(buf, 0, length);
                outSize += length;
                int temp = (int) (((float) outSize) / inSize * 100);
                if (temp != progress) {
                    progress = temp;
                    listener.onCopyProgress(progress);
                }
            }
            out.flush();
            in.close();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean copyFile(String src, String dst, ProgressListener listener) {
        return copyFile(new File(src), new File(dst), listener);
    }
}