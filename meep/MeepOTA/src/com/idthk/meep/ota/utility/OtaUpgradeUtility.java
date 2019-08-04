package com.idthk.meep.ota.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import android.content.Context;
import android.os.RecoverySystem;

import com.idthk.meep.ota.ui.Utils;

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
    
    private static final String PATH_SERIAL = "/mnt/private/sn.txt";
    private static final String PATH_VERSION_NAME = "/mnt/private/meepver.txt";
    private static final String PATH_VERSION_CODE = "/system/bin/version.txt";
    
    public static String getSerialNumber() {
//		return readFile(PATH_SERIAL);
    	return getString("ro.serialno");
	}
    public static String getVersionCode() {
    	return readFile(PATH_VERSION_CODE);
//    	return getString("ro.meep.version.code");
    }
    public static String getVersionName() {
//    	return readFile(PATH_VERSION_NAME);
    	return getString("ro.meep.version.name");
    }
    
    private static String getString(String property) {
		return SystemProperties.get(property);
	}
    public static String readFile(String path) {
		// Get the text file
		File file = new File(path);
		String line = null;
		// Read text from file
		StringBuilder text = new StringBuilder();

		try {
			BufferedReader br = new BufferedReader(new FileReader(file));

			while ((line = br.readLine()) != null) {
				text.append(line);
			}
		} catch (IOException e) {
			// You'll need to add proper error handling here
		}
		Utils.printLogcatDebugMessage(text.toString());
		return text.toString();
	}
}
