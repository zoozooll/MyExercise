package com.dvr.android.dvr;

public class natiperm {

	static 
	{  
		System.loadLibrary("perm"); 
	} 
	public native int check();
}
