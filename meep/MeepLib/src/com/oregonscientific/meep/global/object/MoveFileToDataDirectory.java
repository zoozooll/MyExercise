package com.oregonscientific.meep.global.object;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.util.Log;

import com.oregonscientific.meep.control.MeepStorageCtrl;
import com.oregonscientific.meep.global.Global;

public class MoveFileToDataDirectory {
	
	Thread thread;
	
	public MoveFileToDataDirectory(){

		final String[] fromDirList = new String[] { 
				"/mnt/sdcard/home/ebook/icon/",
				"/mnt/sdcard/home/app/icon/", 
				"/mnt/sdcard/home/game/icon/",
				"/mnt/sdcard/home/photo/icon/", 
				"/mnt/sdcard/home/movie/icon/", 
				"/mnt/sdcard/home/music/icon/",
				"/mnt/sdcard/home/ebook/icon_l/",
				"/mnt/sdcard/home/app/icon_l/", 
				"/mnt/sdcard/home/game/icon_l/",
				"/mnt/sdcard/home/photo/icon_l/", 
				"/mnt/sdcard/home/movie/icon_l/", 
				"/mnt/sdcard/home/music/icon_l/"
				};
		
		final String[] toDirList = new String[] { 
				"/data/home/ebook/icon/",
				"/data/home/app/icon/", 
				"/data/home/game/icon/",
				"/data/home/photo/icon/", 
				"/data/home/movie/icon/", 
				"/data/home/music/icon/",
				"/data/home/ebook/icon/",
				"/data/home/app/icon/", 
				"/data/home/game/icon/",
				"/data/home/photo/icon/", 
				"/data/home/movie/icon/", 
				"/data/home/music/icon/"
				};
		
		thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				for(int i =0; i< fromDirList.length;i++){
					copyFiles(fromDirList[i], toDirList[i]);
				}
			}
		});
		thread.start();
		
	}
	
	private void copyFiles(String src, String dest) {
		File dir = new File(src);
		Log.d("copyfile", "start..." + src);
		if (dir.exists() && dir.isDirectory()) {
			File[] filelist = dir.listFiles();
			String[] list = dir.list();
			Log.i("copyfile", "filelist length:" + filelist.length);
			for (int i = 0; i < filelist.length; i++) {
				File thisFile = filelist[i];
				String name = list[i].substring(0, list[i].length()-4);
				Log.i("copyfile", "name" + name);
				String encodedName = null;
				if(thisFile.getAbsolutePath().contains("/home/ebook")){
					encodedName = EncodingBase64.encode(name);
				}else{
				    encodedName = name;
				}
				Log.i("copyfile", "encoded name" + encodedName);
				String saveTo = dest + encodedName + Global.FILE_TYPE_PNG;
				
				try {
					Log.d("copyfile", "from:" + filelist[i].getAbsolutePath() + " to: " + saveTo);
					File saveToFile = new File(saveTo);
					
					if (!saveToFile.exists() || saveToFile.length() == 0) {
						copy(filelist[i], saveToFile);
					}
					MeepStorageCtrl.changeFilePermission(saveToFile);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.e("copyfile", e.toString());
				}

			}
		//	checkFile(dest);
		} else {
			Log.e("copyfile", "not a dir" + dir.getAbsolutePath());
		}
		
	}
	
//	private void checkFile(String dirPath){
//		File dir = new File(dirPath);
//		if(dir.exists() && dir.isDirectory()){
//			File[] fileArr = dir.listFiles();
//			for(File file : fileArr){
//				if(!file.canRead()){
//					MeepStorageCtrl.changeFilePermission(file.getAbsoluteFile());
//				}
//			}
//		}
//	}
	
	
	
	
	public void copy(File src, File dst) throws IOException {
	    InputStream in = new FileInputStream(src);
	    OutputStream out = new FileOutputStream(dst);

	    // Transfer bytes from in to out
	    byte[] buf = new byte[1024];
	    int len;
	    while ((len = in.read(buf)) > 0) {
	        out.write(buf, 0, len);
	    }
	    in.close();
	    out.close();
	    //src.deleteOnExit();
	}
}
