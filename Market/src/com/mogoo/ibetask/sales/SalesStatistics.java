package com.mogoo.ibetask.sales;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.util.Log;

import com.mogoo.commons.Eryptogram;
import com.mogoo.commons.PhoneInfo;
import com.mogoo.commons.SharedPrefUtils;
import com.mogoo.components.HttpComponent;
import com.mogoo.market.network.IBEManager;
import com.mogoo.market.utils.AppUtils;
import com.mogoo.network.NetWork;

public class SalesStatistics{
	
	private static final String RESULT_OK = "0" ;
	//判断销量统计是否已经上传成功
	public static  boolean salesUpload = false ;
	private static final String TAG = "SALES" ;
	public static String sdSavedSales = null;
	private static final String ACTION_SAVEFILE = "com.mogoo.action.SAVE_FILE";
	private static final String ACTION_SD_SALESINFO = "com.mogoo.action.SD_SALESINFO";
	private SDSalesInfoReceive  receiver = new SDSalesInfoReceive();
	/**
	 * 销量信息的KEY值定义
	 */
	private static final String UID = "uid";
	private static final String SIM = "sim";
	private static final String IMEI = "imei";
	private static final String SMSC = "smsc";
	private static final String VERSION = "ver";
	private static final String PROJECTID = "pid";
	private static final String PHONE_NUMBER = "mobile";
	private static final String RESOLUTION = "resolution";
	private static final String PHONETYPE = "mobile_type";
	private static final String AID = "aid";
	// add by fdl:为了解耦，从IBECOnstants拷贝过来，貌似没什么用处
	private static final boolean IS_SIMULATOR = false;
	
	private SalesInfoLocal mSalesInfoLocal;
	
	private String tempSalesInfo;
	
	private Context mContext;
	private boolean mIsFirstAtemp2Load = true;
	
	private static SalesStatistics instance = null;

	private SalesStatistics(Context context) {
		mContext = context;
		IntentFilter filter = new IntentFilter(ACTION_SD_SALESINFO);
		context.registerReceiver(receiver, filter);
	}
	
	/**
	 * 取得实例
	 */
	public static synchronized SalesStatistics getInstance(Context context) {
		if (instance == null) {
			instance = new SalesStatistics(context);
		}
		return instance;
	}

	/**
	 * 销统模块启动
	 */
	private synchronized void load() //Modified by HeYongquan 同时只能有一个线程上传销量
	{
		if(!salesUpload)
		{
		
		mSalesInfoLocal = null;  //Added by HeYongquan 每次上传销量时必须初始化此值
		    
		String info = getSalesInfoLocal();
		
		Log.d(SalesStatistics.class.getSimpleName(), "getSalesInfoLocal:"+info) ;

        if (!TextUtils.isEmpty(info))
        {
        	mSalesInfoLocal = new SalesInfoLocal(info);        	
        	createTempSalesInfo(); 	
        	uploadSalesStatisticsInfo();
        }else
        {

        	createTempSalesInfo(); 	
        	uploadSalesStatisticsInfo();
           }
		}
		else
		{
			Log.e(TAG,"---salesUpload---TRUE");
        }
	}


	/**
	 * 从文件中获取uid
	 * @return
	 */
	public String getUID() {
		if (mSalesInfoLocal!=null)
		{
			return mSalesInfoLocal.getUIDLocal();
		}else
		{
			return null;
		}
	}

	/**
	 * 从文件中获取sim卡号
	 * @return
	 */
	public String getSim() {

		if (mSalesInfoLocal!=null)
		{
			return mSalesInfoLocal.getSimLocal();
		}else
		{
			return null;
		}
	}

	/**
	 * 从文件中获取imei号
	 * @return
	 */
	public String getImei() {

		if (mSalesInfoLocal!=null)
		{
			return mSalesInfoLocal.getImeiLocal();
		}else
		{
			return null;
		}

	}

	/**
	 * 从文件中获取smsc
	 * @return
	 */
	public String getSmsc() {
		if (mSalesInfoLocal!=null)
		{
			return mSalesInfoLocal.getSmscLocal();
		}else
		{
			return null;
		}
	}

	/**
	 * 从文件中获取软件版本
	 * 
	 * @return
	 */
	public String getVersion() {

		if (mSalesInfoLocal!=null)
		{
			return mSalesInfoLocal.getVersionLocal();
		}else
		{
			return null;
		}
	}

	/**
	 * 从文件中获取项目号
	 * 
	 * @return
	 */
	public String getProjectId() {

		if (mSalesInfoLocal!=null)
		{
			return mSalesInfoLocal.getProjectIdLocal();
		}else
		{
			return null;
		}
	}

	/**
	 * 从文件中获取手机号码
	 * 
	 * @return
	 */
	public String getPhoneNumber() {

		if (mSalesInfoLocal!=null)
		{
			return mSalesInfoLocal.getPhoneNumberLocal();
		}else
		{
			return null;
		}
	}

	/**
	 * 从文件中获取分辨率
	 * 
	 * @return
	 */
	public String getResolution() {
		if (mSalesInfoLocal!=null)
		{
			return mSalesInfoLocal.getResolutionLocal();
		}else
		{
			return null;
		}
	}

	/**
	 * 从文件中获取手机型号
	 * 
	 * @return
	 */
	public String getPhoneType() {
		if (mSalesInfoLocal!=null)
		{
			return mSalesInfoLocal.getPhoneTypeLocal();
		}else
		{
			return null;
		}
	}
	
	/**
	 * 强制上传销量信息 
	 */
	public boolean forceUploadSalesStatisticsInfo()
	{
		boolean salesUpload = false ;
		
		if(IS_SIMULATOR)
		{
			if (tempSalesInfo == null || tempSalesInfo.equals(""))
			{
				createTempSalesInfo();
			}			
			salesUpload = uploadSalesStatisticsInfo();
			
		}else
		{
			if(NetWork.isAvailable(mContext))
			{
				
				if (tempSalesInfo == null || tempSalesInfo.equals(""))
				{
					createTempSalesInfo();
				}
				
				salesUpload = uploadSalesStatisticsInfo();
			}		
		}				
		
		return salesUpload;
	}
	
	private void createTempSalesInfo()
	{
	    tempSalesInfo = null; //Added by heyongquan 创建临时销量信息之前必须先清空
	    
	    String uid = toString(PhoneInfo.getUID(mContext));
		String sim = toString(PhoneInfo.getSim(mContext));
		String imei = toString(PhoneInfo.getImei(mContext));
		String smsc = toString(PhoneInfo.getSmsc());
		// modify by fdl:在高仿IOS或者蘑菇系统中这是系统版本，商城单独APK上传销量的时候这里就传商城的VersionName好了
		String ver = toString(AppUtils.getPackageInfo(mContext).versionName);
		String proid = toString(IBEManager.getInstance().getAKey());
		String phonenum = toString(PhoneInfo.getPhoneNumber(mContext));
		String resolution = toString(PhoneInfo.getResolution(mContext));
		String phonetype = toString("");		
		
		
		String saveUID = toString(getUID());
		String saveSIM = toString(getSim());
		String saveIMEI = toString(getImei());
		String saveSMSC = toString(getSmsc());
		String saveVERSION = toString(getVersion());
		String savePROID = toString(getProjectId());
		String savePHONENUM = toString(getPhoneNumber());
		String saveRESOLUTION = toString(getResolution());
		String savePHONETYPE = toString(getPhoneType());
		
		if (mSalesInfoLocal==null && !TextUtils.isEmpty(uid))
		{
			tempSalesInfo = UID + "=" + uid + ";" 
							+ SIM + "=" + sim + ";"
							+ IMEI + "=" + imei + ";" 
							+ SMSC + "=" + smsc + ";"
							+ VERSION + "=" + ver + ";"
							+ PROJECTID + "=" + proid	+ ";"
							+ PHONE_NUMBER + "=" + phonenum + ";"
							+ RESOLUTION + "=" + resolution + ";" 	
							+ PHONETYPE	+ "=" + phonetype;
			//add by lyl 20120211 是否需要将uid作为默认的aid上传到服务器
			//if(IBEConstants.IS_DEFAULT_AID)
			//{
			//    tempSalesInfo +=";"+ AID + "=" +uid;
			//}
			//add end
			
			tempSalesInfo +=";"+ AID + "=" +IBEManager.getInstance().getAid();
		}else
		{
			if(!sim.equalsIgnoreCase(saveSIM) || !smsc.equalsIgnoreCase(saveSMSC) || !phonenum.equalsIgnoreCase(savePHONENUM) || !imei.equals(saveIMEI)) //Modified by HeYongquan 增加对imei的判断
			{
				tempSalesInfo = UID + "=" + saveUID + ";" 
								+ SIM + "=" + sim + ";"
								+ IMEI + "=" + imei + ";" 
								+ SMSC + "=" + smsc + ";"
								+ VERSION + "=" + ver + ";"
								+ PROJECTID + "=" + proid	+ ";" 
								+ PHONE_NUMBER + "=" + phonenum + ";"
								+ RESOLUTION + "=" + resolution + ";" 	
								+ PHONETYPE	+ "=" + phonetype;
				//add by lyl 20120211 是否需要将uid作为默认的aid上传到服务器
				//if(IBEConstants.IS_DEFAULT_AID)
				//{
	            //    tempSalesInfo +=";"+ AID + "=" +saveUID;
	            //}
				//end 
				
				// modify by fdl
				tempSalesInfo +=";"+ AID + "=" +IBEManager.getInstance().getAid();
			}
		}
		
		Log.d(SalesStatistics.class.getSimpleName(), "createTempSalesInfo:"+tempSalesInfo) ;
		
	}
	
	/**
	 * 上传销量信息到服务器
	 */
	private boolean uploadSalesStatisticsInfo()
	{
	//	boolean salesUpload = false ;
		
		/**
		 * 如果网络不可用
		 */
		
		Log.d(SalesStatistics.class.getSimpleName(), "NetWork.isAvailable:"+NetWork.isAvailable(mContext)) ;
		
		if(!IS_SIMULATOR)
		{
			if(!NetWork.isAvailable(mContext)){
				return salesUpload ;
			}
		}
		
		Log.d(SalesStatistics.class.getSimpleName(), "tempSalesInfo:"+tempSalesInfo) ;
		
		if (tempSalesInfo!=null && !tempSalesInfo.equals(""))
		{
			String url = IBEManager.getInstance().getMasServer() + SalesConfig.URL_UPLOAD_SALES_INFO + "?" + tempSalesInfo.replace(';', '&') ;
			
			Log.d(SalesStatistics.class.getSimpleName(), "Url:"+url) ;
			
			//往服务器提交销量统计信息
			try{
				//String response = HttpComponent.get(url, tempSalesInfo, false) ;
			    String response = HttpComponent.getStringFromURL(url) ;
				Log.e(TAG,"---response==="+response);
				Log.d(SalesStatistics.class.getSimpleName(), "response:"+response) ;
				
				if(response!=null){
					String [] result = response.split("\\|") ;
					if(result.length == 3 && result[1].equals(RESULT_OK)){


						
                        //Modified by HeYongquan
                        
                        //当使用蘑菇IMEI时只有当上传的IMEI和服务器返回的IMEI相同时才将销量上传设置为成功
					    // commented out by fdl ,商城单独APK不许要设置IMEI
						/*if (android.ibe.ibeconfig.IBEConstants.USER_MOGOO_IMEI)
					    {
					        String uploadIMEI = toString(PhoneInfo.getImei(mContext));

					        if(uploadIMEI.equals(result[2]))
					        {
					            salesUpload = true;
					        }
					    }
					    else
					    {
					        salesUpload = true;
					    }
					    
                        //add by lyl   
                        String getImei = IBEConfigManager.getInstance().getImei();
                        if(TextUtils.isEmpty(getImei) || getImei.length() != 15){
                        IBEConfigManager.getInstance().setImei(result[2]);
                        }*/
                        //lyl add end
					    
						saveSalesInfo() ;
						salesUpload = true;
						Log.e(TAG,"---set salesUpload==="+salesUpload);
					}
				}
			}catch(Exception e){
				Log.e(SalesStatistics.class.getSimpleName(), e.getMessage(),e) ;
			}
		}
		else
		{
		    salesUpload = true; //Added by HeYongquan 当销临时销量信息为空时则销量已上传并且没有发生变化，不许要在上传销量
		}
		
		return salesUpload;
	}
	
	/**
	 * 保存销量信息到本地（2份）
	 * @author 张永辉
	 * @date 2011-12-16
	 */
	private void saveSalesInfo()
	{
		String encodeSaleInfo = Eryptogram.encryptData2(tempSalesInfo) ;
		
		Log.d(SalesStatistics.class.getSimpleName(), "encodeSaleInfo:"+encodeSaleInfo) ;
		
		//保存到本地
		if(SharedPrefUtils.setFiled(SalesConfig.SALESINFO_NAME, SalesConfig.KEY_SALES_INFO, encodeSaleInfo)){
			
			mSalesInfoLocal = new SalesInfoLocal(tempSalesInfo); 
			
			Log.d(SalesStatistics.class.getSimpleName(), "save sales info to rom:"+tempSalesInfo) ;
		}
		
		//发送广播让应用保存到SD卡中
		sendBroadCastToApp(ACTION_SAVEFILE,encodeSaleInfo);
		//long avaliableStorage = FileUtils.getAvailableBlocks();
		
		//IBELogger.debug(SalesStatistics.class, " avaliableStorage: " + avaliableStorage);
		
		//if (avaliableStorage >= 5242880)// 5M,5*1024*1024
		//{
		/*	String filePath = FileUtils.getExternalStoragePath()
					+ File.separator + "system32";
			FileUtils.saveFile(filePath, SalesConfig.SAVE_FILE_NAME, encodeSaleInfo);
			
			IBELogger.debug(SalesStatistics.class, "save into sdcard path: "+filePath );*/
		   
		//}
		//getAvaliable();
	}

	/**
	 * 功能：从本地获取需要的字符串 目前只有销量统计使用
	 */
	private String getSalesInfoLocal() 
	{
		//先从本地读取销量统计信息
		String str = SharedPrefUtils.getFiledString(SalesConfig.SALESINFO_NAME, SalesConfig.KEY_SALES_INFO, null) ;

		Log.d(SalesStatistics.class.getSimpleName(), "local sales info:"+str) ;
		
		if (str == null) {
			//如果本地无销量统计信息，从SD卡读取销量统计信息
		/*	String filePath = FileUtils.getExternalStoragePath()
					+ File.separator + "system32";
			str = FileUtils.readFile(filePath, SalesConfig.SAVE_FILE_NAME);

			IBELogger.debug(SalesStatistics.class, "sdcard sales info:"+str) ;*/
		    str = getSDSavedSales();
		}
		if (str != null) {
			str = Eryptogram.decryptData2(str);
			Log.d(SalesStatistics.class.getSimpleName(), "decrypt sales info:"+str) ;
		}
		return str;

	}
	
	/**
	 * 字符串过虑
	 */
	private String toString(String str)
	{
		String temp = "";
		if (str != null && !str.equals("")) 
		{
			temp = str.trim();
		}

		return temp;			
	}
	
	/**
	 * author: lyl 
	 * time:  20120211
	 * 上传销量统计，可能发生阻塞 --modified by HeYongquan
	 */
	public synchronized void uploadSalesStatistics(){
	    Log.e(TAG,"---uploadSalesStatistics---BEGIN");
	    if(salesUpload){
	        Log.e(TAG,"---salesUpload---=true");
	    }
	    else{
	        Log.e(TAG,"---salesUpload---=false");
            //Modified by HeYongquan 2012-4-14 增加循环上传机制，经过确认，当有网络并且销量统计未上传成功时此处需要循环上传直到上传成功
            
            while(NetWork.isAvailable(mContext) && !SalesStatistics.salesUpload)
            {
                try {
                	if (!mIsFirstAtemp2Load )
                	{
                		Thread.sleep(SalesConfig.AUTO_CREATE_SALES_TIME * 1000);
                	}
                	mIsFirstAtemp2Load = false;
                    //createTempSalesInfo(); 
                    //uploadSalesStatisticsInfo();
                    load();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

	    }
	    Log.e(TAG,"---uploadSalesStatistics---END");
	}
	/**
	 * 将sd卡保存的销量统计设置到本地，luoyl，2012情人节巨献
	 * @param strContent
	 */
	public void setSDSavedSales(String strContent){
	    sdSavedSales = strContent;
	  
	    Log.e(TAG,"---setSDSavedSales---:"+strContent);
	}
	/**
	 * 获取sd卡消息，luoyl，2012情人节巨献
	 * @return
	 */
	public String getSDSavedSales(){
	  
	    Log.e(TAG,"---getSDSavedSales()---sdSavedSales=="+sdSavedSales);
	    return sdSavedSales;
	}
	/**
	 * 发送广播
	 * @author luoyongliang
	 * @param action
	 * @param str
	 */
	public void sendBroadCastToApp(String action, String str) {
        Intent myIntent = new Intent();
        myIntent.putExtra("MESSAGE", str);
        myIntent.setAction(action);
        // myIntent.addCategory(Intent.ACTION_DEFAULT);
        mContext.sendBroadcast(myIntent);
        
        Log.e(TAG,"sendBroadCastToApp() action:"+action);
    }
	/**
	 * 处理接到的sd卡销量统计广播
	 * @author luoyongliang
	 *
	 */
	class SDSalesInfoReceive extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			try {

				String action = intent.getAction();
				if (action.equals(ACTION_SD_SALESINFO)) {
					String strContent = intent.getStringExtra("MESSAGE");
					// sdSavedSales = strContent;
					setSDSavedSales(strContent);
				}
			} catch (Throwable e) {
				Log.e(SalesStatistics.class.getSimpleName(),
						"SDSalesInfoReceive ERROR", e);
				e.printStackTrace();
			}
		}
	}
}
