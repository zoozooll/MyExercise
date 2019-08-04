package com.beem.project.btf.manager;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.jivesoftware.smack.util.StringUtils;

import android.text.TextUtils;
import android.util.Log;

import com.beem.project.btf.BeemApplication;
import com.beem.project.btf.constant.SettingKey;
import com.beem.project.btf.network.BDLocator;
import com.beem.project.btf.service.Contact;
import com.beem.project.btf.utils.BBSUtils;
import com.beem.project.btf.utils.DataCleanManager;
import com.beem.project.btf.utils.SharedPrefsUtil;
import com.butterfly.vv.db.ormhelper.bean.LikedPhotoGroup;
import com.butterfly.vv.service.ContactService;
import com.butterfly.vv.vv.utils.VVXMPPUtils;

/**
 * 记录登录信息
 * @author yuedong bao
 * @time 2015-1-26 下午8:29:30
 */
public class LoginManager {
	private static final String tag = LoginManager.class.getSimpleName();
	private String sessionId = "0 ";
	// 登录帐户信息
	private Contact myContact = new Contact();
	// 系统时间
	private long systemTimeDeltalMils;
	private volatile static LoginManager instance;

	private LoginManager() {
	}
	public static LoginManager getInstance() {
		if (instance == null) {
			synchronized (LoginManager.class) {
				if (instance == null) {
					instance = new LoginManager();
				}
			}
		}
		return instance;
	} 
	// 此方法需运行在异步线程中
	/**
	 * Get 
	 * @param token
	 */
	public void getMyContactInfoAsync(String token) {
		
		Contact requestMyContact = ContactService.getInstance().getContact(
				token, false, true);
		if (requestMyContact == null) {
		} else {
			myContact = requestMyContact;
			SharedPrefsUtil.putValue(BeemApplication.getContext(),
					SettingKey.account_username, requestMyContact.getJid());
			String tokenSaved = myContact.getPhoneNum();
			if (TextUtils.isEmpty(token)) {
				tokenSaved = token;
			}
			SharedPrefsUtil.putValue(BeemApplication.getContext(),
					SettingKey.account_token, tokenSaved);
		}
	}
	public void destroy() {
		instance = null;
	}
	public Contact getMyContact() {
		return myContact;
	}
	// 获取去掉server和resource的jid：如1234567890
	public String getJidParsed() {
		String jid = SharedPrefsUtil.getValue(BeemApplication.getContext(),
				SettingKey.account_username, "");
		if (TextUtils.isEmpty(jid)) {
			//			throw new IllegalStateException("jid must own value:" + jid);
			return "0";
		}
		return VVXMPPUtils.makeJidParsed(jid);
	}
	public String getToken() {
		String jid = SharedPrefsUtil.getValue(BeemApplication.getContext(),
				SettingKey.account_token, "");
		if (TextUtils.isEmpty(jid)) {
			//throw new IllegalStateException("token must own value:");
			return "0";
		}
		return VVXMPPUtils.makeJidParsed(jid);
	}
	//获取登录密码
	public String getPassword() {
		String pass = SharedPrefsUtil.getValue(BeemApplication.getContext(),
				SettingKey.account_password, "");
		return pass;
	}
	// 获取完整的jid：如1234567890@192.168.12.41/android
	public String getJidCompleted() {
		String jid = SharedPrefsUtil.getValue(BeemApplication.getContext(),
				SettingKey.account_username, "");
		if (TextUtils.isEmpty(jid)) {
			//			throw new IllegalStateException("jid must own value:" + jid);
			return "0";
		}
		return VVXMPPUtils.makeJidCompleted(jid);
	}
	// 判断是否自己的jid
	public boolean isMyJid(String jid) {
		if (jid == null) {
			return false;
		}
		String jid_ = StringUtils.parseName(jid);
		String spJid = getJidParsed();
		if (TextUtils.isEmpty(spJid) || spJid.equals("0")) {
			return false;
		}
		return jid_.equals(getJidParsed());
	}
	public String getSystemTime() {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sf.format(new Date(systemTimeDeltalMils
				+ System.currentTimeMillis()));
	}
	public Date getSysytemTimeDate() {
		Date retVal = new Date(systemTimeDeltalMils
				+ System.currentTimeMillis());
		return retVal;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void saveSessionId(String sessionId) {
		this.sessionId = sessionId;
		//		//LogUtils.i("save the session id:" + sessionId);
	}
	public void onLogin(String jid, String password) {
		SharedPrefsUtil.putValue(BeemApplication.getContext(),
				SettingKey.account_token, jid);
		SharedPrefsUtil.putValue(BeemApplication.getContext(),
				SettingKey.account_password, password);
		String saveJid = getMyContact().getPhoneNum();
		if (TextUtils.isEmpty(saveJid)) {
			saveJid = jid;
		}
		SharedPrefsUtil.putValue(BeemApplication.getContext(),
				SettingKey.account_token, saveJid);
//		MobclickAgent.onProfileSignIn(getJidParsed()) ;
//		getMyContactInfoAsync();
	}
	/**
	 * 显示是否属于登录的状态，包括在线，离线状态
	 */
	public boolean isLogined() {
		String jidParse = getJidParsed();
		return !TextUtils.isEmpty(jidParse) && !"0".equals(jidParse);
	}
	/**
	 * 注销登录时，取消所有的登录记录，以及清除session。
	 */
	public void onLogout() {
		SharedPrefsUtil.putValue(BeemApplication.getContext(),
				SettingKey.account_username, null);
		SharedPrefsUtil.putValue(BeemApplication.getContext(),
				SettingKey.account_password, null);
		saveSessionId("0");
		DataCleanManager.cleanApplicationData(BeemApplication
						.getContext(), true);
		myContact = new Contact();
//		MobclickAgent.onProfileSignOff();
	}
	public double getLon() {
		return BDLocator.getInstance().getLon();
	}
	public double getLat() {
		return BDLocator.getInstance().getLat();
	}
	public String latlon2Distance(double lat_b, double lng_b) {
		double lat_a = Double.valueOf(getLat());
		double lng_a = Double.valueOf(getLon());
		double lonlat2Distance = BBSUtils.latlon2Distance(lat_a, lng_a, lat_b,
				lng_b);
		if (lonlat2Distance < 1000) {
			return (int) (lonlat2Distance) + "m";
		} else {
			return (int) (lonlat2Distance / 1000) + "km";
		}
	}
	public String latlon2Distance(String lat_b, String lng_b) {
		if (TextUtils.isEmpty(lat_b)) {
			lat_b = "0";
		}
		if (TextUtils.isEmpty(lng_b)) {
			lng_b = "0";
		}
		return latlon2Distance(Double.valueOf(lat_b), Double.valueOf(lng_b));
	}
	public void synSystemTime(long l) {
		systemTimeDeltalMils = l;
		SharedPrefsUtil.putValue(BeemApplication.getContext(),
				SettingKey.systemTimeDeltalMils, systemTimeDeltalMils);
		//		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//		//LogUtils.v("synSystemTime time:" + sf.format(new Date(System.currentTimeMillis() + l)));
	}
}
