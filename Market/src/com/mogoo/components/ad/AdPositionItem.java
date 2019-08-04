package com.mogoo.components.ad;

import java.util.ArrayList;
import java.util.List;

/**
 * 表示一个广告位信息的item<br>
 * 表示一个<position></position>标签对里的信息<br>
 * 请参考:http://192.168.0.177:8088/AD/getAppPosition.action?appId=10000004&
 * positionId=9&uid=123&aid=123&akey=123
 * 
 * @author Administrator
 * 
 */
class AdPositionItem
{

	// 一个广告位的信息
	// http://www.goodboyenglish.com/test/test2.txt?appId=356&positionId=1,2,3,4
	// <position
	// alias="本周最佳应用软件"
	// appId="131"
	// changeTime="5"
	// changeType="1"
	// height="90"
	// pId="1"
	// width="220">
	// pId 广告位Id
	// appId 应用Id
	// alias 广告位别名
	// width 广告位宽度
	// height 广告位高度
	// changeType 轮循类型 0：不轮循，1：左右轮循，2：上下轮循
	// changeTime 轮循时间，以秒为单位

	/** 广告位别名 */
	private String alias;
	/** 应用Id */
	private String appId;
	/** 轮循时间，以秒为单位 */
	private int changeTime;
	/** 轮循类型 0：不轮循，1：左右轮循，2：上下轮循 */
	private int changeType = 1;
	/** 广告位Id */
	private String pId;
	/** 广告位高度 */
	private String height;
	/** 广告位宽度 */
	private String width;

	private List<AdvertiseItem> advertiseItemList;

	AdPositionItem()
	{
		advertiseItemList = new ArrayList<AdvertiseItem>();
	}

	List<AdvertiseItem> getAdvertiseItemList()
	{
		return advertiseItemList;
	}

	void addAdvertiseItemList(AdvertiseItem item)
	{
		if (item != null)
			advertiseItemList.add(item);
	}

	/**
	 * 广告位别名
	 * 
	 * @return
	 */
	public String getAlias()
	{
		return alias;
	}

	void setAlias(String alias)
	{
		this.alias = alias;
	}

	/**
	 * 应用Id
	 * 
	 * @return
	 */
	public String getAppId()
	{
		return appId;
	}

	void setAppId(String appId)
	{
		this.appId = appId;
	}

	/**
	 * 轮循时间，以秒为单位
	 * 
	 * @return
	 */
	public int getChangeTime()
	{
		return changeTime;
	}

	void setChangeTime(String changeTime)
	{
		if (changeTime != null)
			this.changeTime = Integer.parseInt(changeTime.trim());
	}

	/**
	 * 轮循类型 0：不轮循，1：左右轮循，2：上下轮循
	 * 
	 * @return
	 */
	public int getChangeType()
	{
		return changeType;
	}

	void setChangeType(String changeType)
	{

		if (changeType != null)
			this.changeType = Integer.parseInt(changeType.trim());
	}

	/**
	 * 广告位Id
	 * 
	 * @return
	 */
	public String getpId()
	{
		return pId;
	}

	void setpId(String pId)
	{
		this.pId = pId;
	}

	/**
	 * 广告位高度
	 * 
	 * @return
	 */
	String getHeight()
	{
		return height;
	}

	void setHeight(String height)
	{
		this.height = height;
	}

	/**
	 * 广告位宽度
	 * 
	 * @return
	 */
	String getWidth()
	{
		return width;
	}

	void setWidth(String width)
	{
		this.width = width;
	}

}
