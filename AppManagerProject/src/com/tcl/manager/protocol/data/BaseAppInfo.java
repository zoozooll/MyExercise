package com.tcl.manager.protocol.data;

import java.util.ArrayList;

public class BaseAppInfo
{
	/** App 主键Id */
	public int id;
	/** App 包名（和cpu,分辨率有关) */
	public String appPkg;
	/** APP名称（和国家语言有关） */
	public String name;
	/** 是否收费：0:不收费，1：收费 */
	public int charge;
	/** 支付方式及其key */
	public ArrayList<Pay> pays;
	/** -1:没有下载限制 0：有下载限制，还未达到最大下载次数，1：有下载限制，已达到最大下载次数 */
	public int dldLimit;
	/** App版本号 */
	public String version;
	/** 星级 */
	public int star;
	/** App 大小(单位MB) */
	public String size;
	/** 下载次数 */
	public int dldTimes;
	/** App摘要(和国家语言有关) */
	public String summary;
	/** App图标url */
	public String iconUrl;
	/** App 下载地址 */
	public String downloadUrl;
	/** 0:自有App,1:来自google play 的App */
	public int sourceFrom;
	/** App列表中的排序,priority越小排列越前 */
	public int priority;
	
	public BaseAppInfo()
	{
		;
	}

	public static class Pay
	{
		/** 支付类型 1:AMAX付费方式，2:Fotumo付费方式 */
		public int type;
		/** 支付方式对应详情 */
		public PayAttr payAttr;
	}

	public static class PayAttr
	{
		/** App价格 */
		public double price;
		/** 货币代码 */
		public String crcyCode;
		/** 货币符号 */
		public String crcySymb;
		/** 支付方式优先级 */
		public int priority;
		/** AMX付费才有的字段，服务类型 */
		public String serviceType;
		/** Fortumo付费才有的字段，和key共同决定价格 */
		public String keySecret;
		/** Fortumo付费才有的字段，代表价格的key */
		public String key;
	}
}