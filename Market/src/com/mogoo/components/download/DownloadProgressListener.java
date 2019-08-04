package com.mogoo.components.download;

public interface DownloadProgressListener
{
    public long getDownloadId();
    
    public void onDownloadProgressChanged(int progress);

}
