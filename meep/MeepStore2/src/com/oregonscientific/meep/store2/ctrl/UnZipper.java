package com.oregonscientific.meep.store2.ctrl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Observable;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.IOUtils;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.oregonscientific.meep.control.MeepStorageCtrl;
import com.oregonscientific.meep.store2.global.MeepStoreLog;

public class UnZipper extends Observable {

    private static final String TAG = "UnZip";
    private String mFileName, mFilePath, mDestinationPath;

    public UnZipper (String fileName, String filePath, String destinationPath) {
        mFileName = fileName;
        mFilePath = filePath;
        mDestinationPath = destinationPath;
    }

    public UnZipper(String fileAbsPath, String destinationPath){
    	mFilePath = fileAbsPath;
        mDestinationPath = destinationPath;

    }
    
    
    public String getFileName () {
        return mFileName;
    }

    public String getFilePath() {
        return mFilePath;
    }

    public String getDestinationPath () {
        return mDestinationPath;
    }

    public void unzip () {
        String fullPath = mFilePath + "/" + mFileName + ".zip";
        
        fullPath = mFilePath;
        
        MeepStoreLog.logcatMessage(TAG, "unzipping " + mFilePath + " to " + mDestinationPath);
        new UnZipTask().execute(fullPath, mDestinationPath);
    }

    private class UnZipTask extends AsyncTask<String, Void, Boolean> {

        @SuppressWarnings("rawtypes")
        @Override
        protected Boolean doInBackground(String... params) {
            String filePath = params[0];
            String destinationPath = params[1];

            File archive = new File(filePath);
            try {
                ZipFile zipfile = new ZipFile(archive);
                int idx = 0;
                
                for (Enumeration e = zipfile.entries(); e.hasMoreElements();) {
                    ZipEntry entry = (ZipEntry) e.nextElement();
                    unzipEntry(zipfile, entry, destinationPath);
                    idx++;
                    int p = (int)((double)idx / (double)zipfile.size() *100); 
                    
                    
                    Bundle b = new Bundle();
                    b.putInt("percent", p);
                    b.putBoolean("complete", false);
                    MeepStoreLog.logcatMessage("storedownload", "notify observer:" + p);
                    setChanged();
                    notifyObservers(b);
                }
            } catch (Exception e) {
                Log.e(TAG, "Error while extracting file " + archive, e);
                return false;
            }

            return true;
        }

        @Override
		protected void onPostExecute(Boolean result) {
        	setChanged();
			Bundle b = new Bundle();
			
			if (result) {
				b.putInt("percent", 100);
				b.putBoolean("complete", true);
				notifyObservers(b);
			} else {
				b.putInt("percent", 0);
				b.putBoolean("complete", true);
				notifyObservers(b);
			}
			// notifyObservers(result);
		}

        private void unzipEntry(ZipFile zipfile, ZipEntry entry,
                String outputDir) throws IOException {

            if (entry.isDirectory()) {
                createDir(new File(outputDir, entry.getName()));
                return;
            }

            File outputFile = new File(outputDir, entry.getName());
            if (!outputFile.getParentFile().exists()) {
                createDir(outputFile.getParentFile());
            }

            Log.v(TAG, "Extracting: " + entry);
            BufferedInputStream inputStream = new BufferedInputStream(zipfile.getInputStream(entry));
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile));

            try {
               IOUtils.copy(inputStream, outputStream);
            } finally {
                outputStream.close();
                inputStream.close();
            }
        }

        private void createDir(File dir) {
            if (dir.exists()) {
                return;
            }
            Log.v(TAG, "Creating dir " + dir.getName());
            if (!dir.mkdirs()) {
                throw new RuntimeException("Can not create dir " + dir);
            }
            // @TODO: verify if this is working
            MeepStorageCtrl.changeFolderPermission(dir);
        }
    }
} 