package com.oregonscientific.meep.control;

import java.io.File;
import java.util.List;

import android.util.Log;

public class MeepResourceCtrl {

	/**
	 *  list file with the MEEP APP type
	 * @param dir - the folder to search
	 * @param theList	-	the return file list
	 * @param types	- 	the APP type (Ebook, Game...)
	 */
	public static void traverseDir(File dir, List<File> theList, String[] types) {
		//Log.i("ebook-traversal-(Ebook)", "traverseDir LOADED");
		File[] files = dir.listFiles();
		if (files != null) {
			for (int i=0; i<files.length; i++) {
				if (files[i].isDirectory() && !files[i].isHidden()) {
					traverseDir(files[i], theList, types);
				} else {
					if(!files[i].isHidden()){
						for(String type :types){
							String fileName = files[i].getName().toLowerCase();
							
							if(fileName.contains(type)){
								//Log.d("MeepResourceCtrl", "get file name:" + files[i].getAbsolutePath());
								theList.add(files[i]);
							}
						}
					}
				}
			}
		}
	}
}
