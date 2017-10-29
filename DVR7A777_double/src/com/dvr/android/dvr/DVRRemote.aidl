package com.dvr.android.dvr;
 interface DVRRemote {
	int getDVRStatus();
	String DVRTakePicture();
    int DVRStartRecord();
    int DVRStopRecord();
    int DVRLockVideo();
    String DVRMicroVideo();
    void sendPhotoAndVideoPath();
}
