package com.dvr.android.dvr.mshowplayback;

import java.util.Comparator;

public class FilePathComparator implements Comparator<FileDes>
{
    public int compare(FileDes object1, FileDes object2)
    {
        //相等的情况比较名�?            
    	return object1.fileName.compareToIgnoreCase(object2.fileName);
    }
}
