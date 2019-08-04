package com.mogoo.market.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import org.xml.sax.Attributes;

import android.database.Cursor;
import android.os.Parcelable;
import android.text.TextUtils;

import com.mogoo.market.database.ApkListSQLTable;
import com.mogoo.market.database.GameCateSQLTable;
import com.mogoo.parser.XmlResultCallback;

public class HotApp implements Serializable{

	/** 类型：应用 */
	public static String TYPE = "app";

	/** 打开类型, 0列表； 1详情 */
	private String openType = "";

	/** apk id */
	private String apkId;
	/** 名称 */
	private String name;
	/** 版本号 */
	private int versionCode;
	/** 版本字符串 */
	private String versionStr;
	/** 软件大小 */
	private int apkSize;
	/** 虚拟评分 */
	private String vScore;
	/** 真实评分 */
	private String rScore;
	/** 价格 */
	private String price;
	/** 作者 */
	private String author;
	/** 下载地址 */
	private String apkAddress = "";
	/** 图标 url */
	private String iconUrl;
	/** 包名 */
	private String packageName;

	public HotApp() {
		
	}
	
	public HotApp(String openType, String apkId, String name, int versionCode,
			String versionStr, int apkSize, String vScore, String rScore,
			String price, String author, String apkAddress, String iconUrl,
			String packageName) {
		super();
		this.openType = openType;
		this.apkId = apkId;
		this.name = name;
		this.versionCode = versionCode;
		this.versionStr = versionStr;
		this.apkSize = apkSize;
		this.vScore = vScore;
		this.rScore = rScore;
		this.price = price;
		this.author = author;
		this.apkAddress = apkAddress;
		this.iconUrl = iconUrl;
		this.packageName = packageName;
	}

	static class HotAppTag {
		/** 节点 */
		public static final String NODE = "rc";
		/** 编号 */
		public static final String ID = "id";
		/** 名称 */
		public static final String NAME = "n";
		/** 版本号 */
		public static final String VERSION_CODE = "v";
		/** 版本字符串 */
		public static final String VERSION_STR = "vn";
		/** 软件大小 */
		public static final String APK_SIZE = "s";
		/** 虚拟评分 */
		public static final String RSCORE = "xc";
		/** 真实评分 */
		public static final String VSCORE = "zc";
		/** 价格 */
		public static final String PRICE = "p";
		/** 作者 */
		public static final String AUTHOR = "a";
		/** 应用图标 */
		public static final String ICON_URL = "icp";
		/** 包名 */
		public static final String PACKAGE_NAME = "pn";
		/** 下载地址 */
		public static final String APK_ADDRESS = "ap";
	}

	/**
	 * 解析类
	 * 
	 * @author luo
	 */
	public static class HotAppListCallback extends XmlResultCallback {
		private HotApp hotapp;
		public ArrayList<HotApp> HotAppList = new ArrayList<HotApp>();
		private StringBuilder mStringBuilder = new StringBuilder();
		
		@Override
		public Object getResult() {

			return HotAppList;
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) {
			mStringBuilder.setLength(0);
			if (localName.equalsIgnoreCase(HotAppTag.NODE)) {
				for (int i = 0; i < attributes.getLength(); i++) {
					String iQName = attributes.getQName(i);
					if (iQName.equalsIgnoreCase(HotAppTag.ID)) {
						String id = attributes.getValue(iQName);
						hotapp = new HotApp();
						hotapp.setApkId(id);
					}
				}
			}
			super.startElement(uri, localName, qName, attributes);
		}

		@Override
		public void endElement(String uri, String localName, String qName) {
			String data = mStringBuilder.toString();

			if (localName.equals(HotAppTag.NAME)) {
				hotapp.name = data;
			} else if (localName.equals(HotAppTag.VERSION_STR)) {
				hotapp.versionStr = data;
			} else if (localName.equals(HotAppTag.VERSION_CODE)) {
				// 应用大小可能超出integer范围或者为null，默认为0.
				try {
					hotapp.versionCode = Integer.parseInt(data);
				}catch (NumberFormatException e) {
					hotapp.versionCode = 0;
				}
			} else if (localName.equals(HotAppTag.APK_SIZE)) {
				//应用大小可能超出integer范围或者为null，默认为0.
				// 一般不会发生.
				try {
					hotapp.apkSize = Integer.parseInt(data);
				}catch (NumberFormatException e) {
					hotapp.apkSize = 0;
				}
			} else if (localName.equals(HotAppTag.VSCORE)) {
				hotapp.vScore = data;
			} else if (localName.equals(HotAppTag.RSCORE)) {
				hotapp.rScore = data;
			} else if (localName.equals(HotAppTag.PRICE)) {
				hotapp.price = data;
			} else if (localName.equals(HotAppTag.AUTHOR)) {
				hotapp.author = data;
			} else if (localName.equals(HotAppTag.ICON_URL)) {
				hotapp.iconUrl = data;
			} else if (localName.equals(HotAppTag.PACKAGE_NAME)) {
				hotapp.packageName = data;
			}else if (localName.equals(HotAppTag.APK_ADDRESS)) {
				hotapp.apkAddress = data;
			}
			if (localName.equalsIgnoreCase(HotAppTag.NODE)) {
				HotAppList.add(hotapp);
				hotapp = null;
			}

			super.endElement(uri, localName, qName);
		}

		@Override
		public void characters(char[] ch, int start, int length) {
			super.characters(ch, start, length);
			mStringBuilder.append(ch, start, length);
		}
	}

	// 属性赋值
	public HotApp setCharacters(HotApp hotapp, String localName, String data) {

		if (localName.equals(HotAppTag.NAME)) {
			hotapp.name = data;
		} else if (localName.equals(HotAppTag.VERSION_STR)) {
			hotapp.versionStr = data;
		} else if (localName.equals(HotAppTag.VERSION_CODE)) {
			hotapp.versionCode = Integer.parseInt(data);
		} else if (localName.equals(HotAppTag.APK_SIZE)) {
			hotapp.apkSize = Integer.parseInt(data);
		} else if (localName.equals(HotAppTag.VSCORE)) {
			hotapp.vScore = data;
		} else if (localName.equals(HotAppTag.RSCORE)) {
			hotapp.rScore = data;
		} else if (localName.equals(HotAppTag.PRICE)) {
			hotapp.price = data;
		} else if (localName.equals(HotAppTag.AUTHOR)) {
			hotapp.author = data;
		} else if (localName.equals(HotAppTag.ICON_URL)) {
			hotapp.iconUrl = data;
		} else if (localName.equals(HotAppTag.PACKAGE_NAME)) {
			hotapp.packageName = data;
		}else if (localName.equals(HotAppTag.APK_ADDRESS)) {
			hotapp.apkAddress = data;
		} 

		return hotapp;
	}

	public String getOpenType() {
		return openType;
	}

	public void setOpenType(String openType) {
		this.openType = openType;
	}

	public static String getTYPE() {
		return TYPE;
	}

	public static void setTYPE(String tYPE) {
		TYPE = tYPE;
	}

	public String getApkId() {
		return apkId;
	}

	public void setApkId(String apkId) {
		this.apkId = apkId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public String getVersionStr() {
		return versionStr;
	}

	public void setVersionStr(String versionStr) {
		this.versionStr = versionStr;
	}

	public int getApkSize() {
		return apkSize;
	}

	public void setApkSize(int apkSize) {
		this.apkSize = apkSize;
	}

	public String getvScore() {
		return vScore;
	}

	public void setvScore(String vScore) {
		this.vScore = vScore;
	}

	public String getrScore() {
		// vScore(虚拟评分) + rScore(真实评分) <= 100,返回四颗星;
		// 其他情况返回五颗星.
		float virtualScoreInt = (vScore == null || TextUtils.isEmpty(vScore)) ? 0.0f : Float.parseFloat(vScore);
		float realScoreInt = (rScore == null || TextUtils.isEmpty(rScore)) ? 0.0f : Float.parseFloat(rScore);
		if(virtualScoreInt + realScoreInt <= 100.0F) {
			return "4.0";
		}else {
			return "5.0";
		}
		// return rScore;
	}

	public void setrScore(String rScore) {
		this.rScore = rScore;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getApkAddress() {
		return apkAddress;
	}

	public void setApkAddress(String apkAddress) {
		this.apkAddress = apkAddress;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public static HotApp getHotApp(Cursor cursor) {
		
		String openType = cursor.getString(ApkListSQLTable.NUMBER_APK_OPEN_TYPE);
		String apkId = cursor.getString(ApkListSQLTable.NUMBER_APK_ID);
		String name = cursor.getString(ApkListSQLTable.NUMBER_APK_NAME);
		int versionCode = cursor.getInt(ApkListSQLTable.NUMBER_APK_VERSION_CODE);
		String versionStr = cursor.getString(ApkListSQLTable.NUMBER_APK_VERSION_int);
		int apkSize = cursor.getInt(ApkListSQLTable.NUMBER_APK_SIZE);
		String vScore = cursor.getString(ApkListSQLTable.NUMBER_APK_VSCORE);
		String rScore = cursor.getString(ApkListSQLTable.NUMBER_APK_RSCORE);
		String price = cursor.getString(ApkListSQLTable.NUMBER_APK_PRICE);
		String author = cursor.getString(ApkListSQLTable.NUMBER_APK_AUTHOR);
		String apkAddress = cursor.getString(ApkListSQLTable.NUMBER_APK_ADDRESS);
		String iconUrl = cursor.getString(ApkListSQLTable.NUMBER_APK_ICONURL);
		String packageName = cursor.getString(ApkListSQLTable.NUMBER_APK_PACKAGENAME);
		HotApp apk = new HotApp(openType, apkId, name, versionCode,
				versionStr, apkSize, vScore, rScore,
				price, author, apkAddress, iconUrl,
				packageName);
		return apk;
	}
}
