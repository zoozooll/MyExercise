package com.dvr.android.dvr;

public class CheckAvin {
	
	static {
        System.loadLibrary("getstatus_jni");
    }

	public native int BACKstatus(); //返回值是 0 表示ACC存在，倒车存在，avin信号存在   返回值是 1 表示上面3者不存在
	public native int ACCstatus();
	public native int AVINstaus();
    /*static {
        System.loadLibrary("avin-jni");
    }

    // 打开设备
    public native long AVINopenDevice();

    // 暂时无用
    public native long AVINenable(long handle, boolean bEnable);

    // 读取AVIN是否联接，handle:暂时无用； avindetstaus：为0,联接上；1没有联上
    public native long AVINdetstaus(long handle, long avindetstaus);

    // 告诉底层AVIN插入fdisplayctrl :0打开，1：关闭
    public native long AVINdisplayctrl(long handle, long fdisplayctrl);

    // 关闭设备
    public native long AVINcloseDevice(long handle);

    // 倒车相关 打开USB后录时设为1，关闭USB后录时设为0
    //public native long AVINusbdrvflag(long handle);
    
    //1: USB PWR on //  0 usb pwr off
    //public native long AVINusbpwrcotrl(long handle);
*/
}