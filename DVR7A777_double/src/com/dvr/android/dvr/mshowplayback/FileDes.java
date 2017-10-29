package com.dvr.android.dvr.mshowplayback;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;

import android.R.integer;

import com.dvr.android.dvr.R;

public class FileDes implements Serializable {
	
    private static final long serialVersionUID = 1L;
    public static final int FILE_TYPE_DIR = 1;
    public static final int FILE_TYPE_VIDEO = 2;
    public static final int FILE_TYPE_IMAGE = 3;
    public String filePath = null;
    public String fileName = null;
    public String mimeType = null;
    public String disname = null;
    public long size = 0;
    public long duration = 0;
    public String discript = null;
    public int fileIcon = R.drawable.file_dir_icon;
    public long id ;
    public int fileType = FILE_TYPE_DIR;
    
    public boolean isDir = false;
    public boolean isLastDir = true; //liujie add 1009
    Hashtable<String, FileDes> videoCameraTable = new Hashtable<String, FileDes>();//liujie add 1009
    public ArrayList<FileDes> mediaFileDess = new ArrayList<FileDes>();
    
    public FileDes mParent;
    
    public boolean canDelete = true;
}
