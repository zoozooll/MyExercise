package com.tcl.manager.statics;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.tcl.framework.log.NLog;
import com.tcl.framework.network.http.NetworkError;
import com.tcl.framework.notification.NotificationCenter;
import com.tcl.framework.notification.Subscriber;
import com.tcl.framework.util.CollectionUtils;
import com.tcl.framework.util.FileUtils;
import com.tcl.framework.util.PrefsUtils;
import com.tcl.framework.util.TimeConstants;
import com.tcl.manager.application.ManagerApplication;
import com.tcl.manager.util.AndroidUtil;
import com.tcl.manager.util.StringUtils;

@SuppressLint("NewApi")
public class OALogger
{
	private final static String TAG = "OALogger";
	private final static String SPLIT_PATTERN = "|";
	private final static int DEVICE_TYPE_ANDROID = 0;
	private static OALogger slogger = null;
//	private int sourceId;
//	private int position;
//	private StringBuffer statistics;
//	private String pid;

	String mLogFilePath;
	long mLastReportLogTime;
	private byte[] mData;
	// Map<String, V>
	Context mContext;
//	LinkedList<PageNavigateRecord> mPages;
	boolean mReporting;

	private OALogger(Context context, String path)
	{
		this.mLogFilePath = path;
//		mPages = new LinkedList<PageNavigateRecord>();
		mContext = context;
		mLastReportLogTime = PrefsUtils.loadPrefLong(context, "last_report_log_time", 0L);
//		statistics = new StringBuffer();
	}

	public static synchronized boolean initLogger(Context context, String dirPath)
	{
		if (slogger == null)
		{
			if (TextUtils.isEmpty(dirPath))
				throw new IllegalArgumentException("dir path is empty");

			String path = dirPath + File.separator + "oa.log";
			OALogger logger = new OALogger(context, path);
			if (!logger.openLogStream())
				return false;

			slogger = logger;
		}

		return true;
	}

	public static OALogger getLogger()
	{
		return slogger;
	}

	public static synchronized void closeLogger()
	{
		if (slogger != null)
		{
			slogger.clean();
			slogger = null;
		}
	}

	private void clean()
	{
//		mPages.clear();
		closeLogStream();
		NotificationCenter.defaultCenter().unsubscribe(ReportEvent.class, mReportSubscriber);
	}

	private static String getAccountName()
	{
		return "";
	}

	private static void writeCommonLog(StringBuffer sb)
	{
		Context context = ManagerApplication.sApplication.getApplicationContext();
		PackageManager manager = context.getPackageManager();
		try
		{
			PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
			sb.append(info.versionName);
		}
		catch (Exception e)
		{
		}

		sb.append(SPLIT_PATTERN);

		sb.append(StringUtils.getTime2GMT());

		sb.append(SPLIT_PATTERN);

		String account = getAccountName();

		if (account != null)
		{
			sb.append(account);
		}

		sb.append(SPLIT_PATTERN);
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

		if (telephonyManager.getDeviceId() != null)
		{
			sb.append(telephonyManager.getDeviceId());
		}
		sb.append(SPLIT_PATTERN);
		if (telephonyManager.getSubscriberId() != null)
		{
			sb.append(telephonyManager.getSubscriberId());
		}
		sb.append(SPLIT_PATTERN);

		String region = AndroidUtil.getMetaData(context, "REGION");
		if (!TextUtils.isEmpty(region))
		{
			sb.append(region);
		}
		sb.append(SPLIT_PATTERN);
		sb.append(DEVICE_TYPE_ANDROID).append(SPLIT_PATTERN);
		if (!TextUtils.isEmpty(Build.MODEL))
		{
			sb.append(Build.MODEL);
		}
	}

	public static String buildLog(String event, List<Object> params)
	{
		StringBuffer sb = new StringBuffer(event);
		sb.append(SPLIT_PATTERN);
		writeCommonLog(sb);
		if (!CollectionUtils.isEmpty(params))
		{
			for (Object o : params)
			{
				sb.append(SPLIT_PATTERN);
				if (o == null)
				{
					continue;
				}
				sb.append(o.toString());
			}
		}
		sb.append(SPLIT_PATTERN);
		sb.append(AndroidUtil.getMetaData(ManagerApplication.sApplication.getApplicationContext(), "CHANNEL"));
		NLog.i("buildLog", "event: %s, log: %s ", event, sb.toString());
		return sb.toString();
	}

	public void logLaunch()
	{
		String content = buildLog(OAEvent.ALL_BOOT, null);
		// sendBootReport(content);
		log(content);

	}
//
//	/**
//	 * 浏览记录
//	 * 
//	 * @param id :Source Id,浏览对象的Id,比如说专题ID，分类ID
//	 * @param pkgName 包名
//	 * @param pageName FeaturedFragment、ApplicationFragment、GameFragment、
//	 *        SpecialActivity、AppInfoActivity
//	 * 
//	 * @param duration 停留时间
//	 */
//	public String logPageNavigate(int id, String pkgName, String uiComponent, int duration)
//	{
//		List<Object> params = new ArrayList<Object>();
//		params.add(id);
//		params.add(pkgName);
//		params.add(uiComponent);
//		params.add(duration);
//		if (getStatistics().contains("Push"))
//		{
//			params.add(getPid());
//		}
//		else
//		{
//			params.add(0);
//		}
//		params.add(AndroidUtil.getnetworkInfoName(mContext));
//		String content = buildLog(OAEvent.PAGE_NAVIGATE, params);
//		log(content);
//		return buildLog(OAEvent.N_PAGE_NAVIGATE, params);
//	}
//
//	/**
//	 * PSSRC
//	 * 
//	 * push浏览记录
//	 * 
//	 * @param pid
//	 * 
//	 */
//	public String logPush(String pid)
//	{
//		List<Object> params = new ArrayList<Object>();
//		params.add(pid);
//		params.add(AndroidUtil.getnetworkInfoName(mContext));
//		String content = buildLog(OAEvent.APP_PUSH, params);
//		log(content);
//		return buildLog(OAEvent.N_APP_PUSH, params);
//	}
//
//	/**
//	 * CRC 点击量记录
//	 * 
//	 * @param sid Source Id,浏览对象的Id
//	 * @param stp App还是专题(0/1)
//	 * @param name Recom，Banner
//	 * @param position
//	 */
//	public void bannerClick(String sid, String appName, int stp, String pageName)
//	{
//		List<Object> params = new ArrayList<Object>();
//		params.add(sid);
//		params.add(appName);
//		params.add(stp);
//		params.add(pageName);
//		params.add(getPosition());
//		String content = buildLog(OAEvent.BANNER_CLICK, params);
//		log(content);
//	}

	// /**
	// * DRC下载记录
	// *
	// * @param aid
	// * appid
	// * @param pkgName
	// * 包名
	// * @param sid
	// * Source Id,下载对象所处页面id,分类id,专题id,
	// * @param pageName
	// * 下载页面名称 比如： 大Banner专题里的应用list的下载: Bbanner_SpecialActivity
	// * 大Banner专题列表里的应用详情的下载: Bbanner_SpecialActivity_AppInfoActivity
	// * 小Banner专题里的应用list的下载: Sbanner_SpecialActivity
	// * 小Banner专题列表里的应用详情的下载： Sbanner_SpecialActivity_AppInfoActivity
	// * 精品页面list的下载: FeaturedFragment 精品页面list的详情下载:
	// * FeaturedFragment_AppInfoActivity 应用页面list的下载:
	// * ApplicationFragment 应用页面list的详情下载:
	// * ApplicationFragment_AppInfoActivity 游戏页面list的下载: GameFragment
	// * 游戏页面list的详情下载: GameFragment_AppInfoActivity
	// *
	// *
	// * @param position
	// */
	// public String logAppDownload(int aid, String appName, String pkgName) {
	// if (getStatistics().length() == 0) { // 其他界面的下载事件不统计
	// return "";
	// }
	//
	// List<Object> params = new ArrayList<Object>();
	// params.add(aid);
	// params.add(appName);
	// params.add(pkgName);
	// params.add(getSourceId());
	// params.add(getStatistics());
	// params.add(getPosition());
	// if (getStatistics().contains("Push")) {
	// params.add(getPid());
	// } else {
	// params.add(0);
	// }
	// params.add(AndroidUtil.getnetworkInfoName(mContext));
	// if (DataCenter.getInstance().isInstalled(pkgName)) {
	// params.add("update");
	// } else {
	// params.add("download");
	// }
	// String content = buildLog(OAEvent.APP_DOWNLOAD, params);
	// log(content);
	// return buildLog(OAEvent.N_APP_DOWNLOAD, params);
	// }

	/**
	 * IRC 安装统计
	 * 
	 * @param aid
	 * @param pkgName
	 */
	public String logAppInstall(String mURLG)
	{
		if (StringUtils.isEmpty(mURLG))
		{
			return "";
		}
		String s = mURLG.replace("NDRC", "IRC");
		log(s);
		return s.replace("IRC", "NIRC");
	}

//	public void tracePageStart(int id, String pageName)
//	{
//		PageNavigateRecord pnr = new PageNavigateRecord(id, pageName);
//		if (mPages.size() == 0)
//		{
//			mPages.push(pnr);
//		}
//		else
//		{
//			PageNavigateRecord top = mPages.peek();
//			if (top.equals(pnr))
//			{
//				NLog.w(TAG, "Page Navigation(%d, %s) lost end event", id, pageName);
//				pnr = top;
//			}
//			else
//			{
//				mPages.push(pnr);
//			}
//		}
//
//		pnr.traceStart();
//	}

//	public void tracePageEnd(int id, String pageName)
//	{
//		if (mPages.size() == 0)
//		{
//			NLog.w(TAG, "Page Navigation(%d, %s) lost start event", id, pageName);
//			return;
//		}
//		PageNavigateRecord top = mPages.peek();
//		PageNavigateRecord pnr = new PageNavigateRecord(id, pageName);
//		if (!top.equals(pnr))
//		{
//			int index = mPages.indexOf(pnr);
//			if (index < 0)
//			{
//				NLog.w(TAG, "Page Navigation(%d, %s) lost start event", id, pageName);
//				return;
//			}
//			else
//			{
//				top = mPages.get(index);
//			}
//
//		}
//		else
//		{
//			mPages.pop();
//		}
//
//		top.traceEnd();
//	}

	public void log(String content)
	{
		NLog.v(TAG, "log %s", content);
		println(content);
	}

	public void reportSync()
	{
		NLog.v(TAG, "reportSync");
		if (!AndroidUtil.isNetConnect(mContext) || !needReport())
		{
			return;
		}
		reportLog();
	}

	private boolean needReport()
	{
		return (System.currentTimeMillis() - mLastReportLogTime >= TimeConstants.ONE_DAY_MS);
	}

	public synchronized boolean reportLog()
	{
		NLog.i(TAG, "reportLog");
		if (TextUtils.isEmpty(mLogFilePath) || mReporting)
			return false;

		String tmpPath = mLogFilePath + ".0";
		File tmpFile = new File(tmpPath);
		if (!tmpFile.exists())
		{
			NLog.i(TAG, "reportLog : not exists");
			File file = new File(mLogFilePath);
			if (file.length() == 0)
			{
				mLastReportLogTime = System.currentTimeMillis();
				PrefsUtils.savePrefLong(mContext, "last_report_log_time", mLastReportLogTime);
				return true;
			}
			closeLogStream();

			if (!file.renameTo(tmpFile))
			{
				NLog.w(TAG, "reportLog rename false");
				return false;
			}

			openLogStream();
		}

		mReporting = true;
		sendFile(tmpPath);

		return true;
	}

	private void sendFile(final String filePath)
	{
		// ThreadPool.runOnNonUIThread(new Runnable() {
		//
		// @Override
		// public void run() {
		// Map<String, byte[]> dataMap = new HashMap<String, byte[]>();
		// if (mData != null) {
		// dataMap.put("log", mData);
		// return;
		// }
		//
		// FileInputStream is = null;
		// try {
		// is = new FileInputStream(filePath);
		// byte[] data = ZlibUtil.compress(is);
		// mData = data;
		//
		// if (mData != null) {
		// NLog.e(TAG, "zipped data size: %d", data.length);
		// dataMap.put("behaviors", mData);
		// LogUtil.v("map() = " + map());
		// String json = HttpHandler.postReprot(
		// UrlConfig.getBrowseURL(), map(), dataMap);
		// JSONObject jsonArray = new JSONObject(json);
		// String statu = jsonArray
		// .getString(Constant.Parameter.STATUS);
		// LogUtil.v("sendFile = " + json);
		// if (StringUtils.isEmpty(statu)
		// || !statu.equals(Constant.SUCCESS)) {
		// // NotificationCenter.defaultCenter().unsubscribe(ReportEvent.class,
		// // mReportSubscriber);
		// // mReporting = false;
		// LogUtil.v("sendFile statu false");
		// return;
		// }
		// if (statu.equals(Constant.SUCCESS)) {
		// File file = new File(filePath);
		// file.delete();
		// LogUtil.v("delete file");
		// return;
		// }
		// }
		// } catch (FileNotFoundException e) {
		// NLog.printStackTrace(e);
		// } catch (Exception e) {
		// NLog.printStackTrace(e);
		// } finally {
		// if (is != null)
		// try {
		// is.close();
		// } catch (IOException e) {
		// }
		// }
		// }
		// });

	}

	private Subscriber<ReportEvent> mReportSubscriber = new Subscriber<ReportEvent>()
	{

		@Override
		public void onEvent(ReportEvent event)
		{
			mReporting = false;
			NotificationCenter.defaultCenter().unsubscribe(ReportEvent.class, this);
			if (event.status == NetworkError.SUCCESS)
			{
				NLog.i(TAG, "report oa log success");
				mLastReportLogTime = System.currentTimeMillis();
				PrefsUtils.savePrefLong(mContext, "last_report_log_time", mLastReportLogTime);
			}
			else
			{
				NLog.w(TAG, "report oa log failed");
			}
		}
	};

	final Object mLogLock = new Object();
	OutputStreamWriter mLogWriter = null;

	private boolean openLogStream()
	{
		if (TextUtils.isEmpty(mLogFilePath))
		{
			return false;
		}

		synchronized (mLogLock)
		{
			OutputStreamWriter writer = null;
			File file = new File(mLogFilePath);
			try
			{
				// not exist
				if (!file.exists())
				{
					FileUtils.create(file);
				}

				writer = new FileWriter(file, true);
				mLogWriter = writer;
				return true;
			}
			catch (IOException e)
			{
				return false;
			}
		}
	}

	private void closeLogStream()
	{
		if (TextUtils.isEmpty(mLogFilePath))
		{
			return;
		}

		synchronized (mLogLock)
		{
			if (mLogWriter != null)
			{
				try
				{
					mLogWriter.close();
				}
				catch (IOException e)
				{
				}

				mLogWriter = null;
			}
		}
	}

	private void println(String message)
	{
		if (mLogWriter == null)
			return;
		StringBuffer sb = new StringBuffer(message);
		sb.append("\n");
		synchronized (mLogLock)
		{
			if (mLogWriter != null)
			{
				try
				{
					mLogWriter.write(sb.toString());
					mLogWriter.flush();
				}
				catch (IOException e)
				{
					NLog.printStackTrace(e);
					closeLogStream();
				}
			}
		}
	}

//	static class PageNavigateRecord
//	{
//		int id;
//		String name;
//		long startTime;
//		int duration;
//
//		public PageNavigateRecord(int id, String name)
//		{
//			this.id = id;
//			this.name = name;
//		}
//
//		public void traceStart()
//		{
//			this.startTime = System.currentTimeMillis();
//		}
//
//		public boolean tracing()
//		{
//			return (this.startTime > 0);
//		}
//
//		public void traceEnd()
//		{
//			if (!tracing())
//				return;
//
//			long diff = System.currentTimeMillis() - this.startTime;
//			duration = (int) (diff / 1000);
//		}
//
//		@Override
//		public int hashCode()
//		{
//			final int prime = 31;
//			int result = 1;
//			result = prime * result + id;
//			result = prime * result + ((name == null) ? 0 : name.hashCode());
//			return result;
//		}
//
//		@Override
//		public boolean equals(Object obj)
//		{
//			if (this == obj)
//				return true;
//			if (obj == null)
//				return false;
//			if (getClass() != obj.getClass())
//				return false;
//			PageNavigateRecord other = (PageNavigateRecord) obj;
//			if (id != other.id)
//				return false;
//			if (name == null)
//			{
//				if (other.name != null)
//					return false;
//			}
//			else if (!name.equals(other.name))
//				return false;
//			return true;
//		}
//
//	}

//	public int getSourceId()
//	{
//		return sourceId;
//	}
//
//	public void setSourceId(int sourceId)
//	{
//		this.sourceId = sourceId;
//	}
//
//	public int getPosition()
//	{
//		return position;
//	}
//
//	public void setPosition(int position)
//	{
//		this.position = position;
//	}
//
//	public String getStatistics()
//	{
//		return statistics.toString();
//	}
//
//	public String getPid()
//	{
//		return pid;
//	}
//
//	public void setPid(String pid)
//	{
//		this.pid = pid;
//	}

	// private Map<String, String> map() {
	// Map<String, String> map = new HashMap<String, String>();
	// TelephonyManager telephonyManager = (TelephonyManager) mContext
	// .getSystemService(Context.TELEPHONY_SERVICE);
	// map.put("imsi", telephonyManager.getSubscriberId());//
	// "724556789123456789",
	// // "46000342424234324"
	// map.put("imei", telephonyManager.getDeviceId());
	// map.put("model", Constant.MODEL);// Constant.MODEL"RAV4_EMEA");
	// map.put("language",
	// mContext.getResources().getConfiguration().locale.toString());
	// map.put("region", ContextUtils.getMetaData(mContext, "REGION"));
	// map.put("req_from", ContextUtils.getMetaData(mContext, "CHANNEL"));
	// map.put("version_name", AndroidUtil.getVersionName(mContext));
	// map.put("version_code",
	// String.valueOf(AndroidUtil.getVersionCode(mContext)));
	// // ----start add
	// map.put("screen_size", AndroidUtil.getDisplayMetricsHeight(mContext)
	// + "#" + AndroidUtil.getDisplayMetricsWidth(mContext));// 手机屏幕分辨率
	// map.put("network", AndroidUtil.getnetworkInfoName(mContext));// 当前使用网络
	// map.put("os_version", AndroidUtil.getVersionRelease());// android系统版本
	// map.put("user_account", "");
	//
	// return map;
	// }

//	private void addTag(String tag)
//	{
//		this.statistics.append(tag);
//	}
//
//	public void clearTag()
//	{
//		if (this.statistics.length() > 0)
//		{
//			this.statistics.delete(0, this.statistics.length());
//		}
//	}
//
//	public void removeAppinfo()
//	{
//		if (!this.statistics.toString().contains("AppInfoActivity"))
//		{
//			return;
//		}
//		String temp = this.statistics.toString();
//		clearTag();
//		addTag(temp.replace("_AppInfoActivity", ""));
//	}
//
//	private boolean tagEndWith(String tag)
//	{
//		return this.statistics.toString().endsWith(tag);
//	}

//	public void addStatistics(String statistics)
//	{
//		NLog.v(TAG, "add statistics:%s", statistics);
//		if (statistics.equals("FeaturedFragment") || statistics.equals("ApplicationFragment") || statistics.equals("GameFragment") || statistics.equals("MoreFragment") || statistics.equals("Bbanner")
//				|| statistics.equals("Sbanner") || statistics.equals("Push") || statistics.equals("SearchActivity"))
//		{
//			clearTag();
//			addTag(statistics);
//			NLog.v(TAG, "add statistics:%s, result: %s", statistics, this.getStatistics());
//			return;
//		}
//
//		if (statistics.equals("AppInfoActivity"))
//		{
//			if (tagEndWith("AppInfoActivity"))
//			{
//				NLog.v(TAG, "add statistics:%s, result: %s", statistics, this.getStatistics());
//				return;
//			}
//			addTag("_");
//			addTag(statistics);
//			NLog.v(TAG, "add statistics:%s, result: %s", statistics, this.getStatistics());
//			return;
//		}
//
//		if (statistics.equals("SpecialActivity"))
//		{
//			if (tagEndWith("SpecialActivity"))
//			{
//				NLog.v(TAG, "add statistics:%s, result: %s", statistics, this.getStatistics());
//				return;
//			}
//			addTag("_");
//			addTag(statistics);
//			NLog.v(TAG, "add statistics:%s, result: %s", statistics, this.getStatistics());
//			return;
//		}
//		clearTag();
//		NLog.v(TAG, "add statistics:%s, result: %s", statistics, this.getStatistics());
//	}
//
//	public void addStatistics(String... params)
//	{
//		for (String p : params)
//		{
//			if (p == null)
//			{
//				continue;
//			}
//			addStatistics(p);
//		}
//	}
}
