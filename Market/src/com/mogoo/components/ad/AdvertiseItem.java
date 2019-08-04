package com.mogoo.components.ad;

/**
 * 表示一个展示位所包含的信息<br>
 * 请参阅：http://ftp-leo3780267.q5.dns-dns.net/test/test2.txt?appId=356&positionId=
 * 1,2,3,4<br>
 * 格式类似:<br>
 * // <advertise> // // <adId> // 10 // </adId> // // <showContent> //
 * /upload/store/topic/ad1.png // </showContent> // // <showType> // 0 //
 * </showType> // // <openType> // 0 // </openType> // // <openContent> //
 * /client/getTopicAppList.action?topicId=8 // </openContent> // // <adType> //
 * 1 // </adType> // </advertise>
 * 
 * @author Administrator
 * 
 */
public class AdvertiseItem
{

	/** 广告Id */
	private String adId;
	/** 需要显示的广告图片url */
	private String imgurl;
	/** 打开方式，0：列表打开，1：直接打开详细页 */
	private int openType;
	/** 点击广告后所需打开的URL */
	private String url;
	/** 广告类型，0：商务广告，1：推广广告 */
	private int adType;
	/** 显示广告时的类型:0为文本，1为图片 */
	private int showType;
	/**
	 * 标题
	 */
	private String title;

	/** 广告位Id */
	private String pId;

	// 2011.12.29
	// <advertise>
	// <adId>18</adId>
	// <title>测试打开浏览器</title>
	// <imgurl>
	// http://ftp-leo3780267.q5.dns-dns.net/test/upload/store/topic/ad1.png
	// </imgurl>
	// <showType>1</showType>
	// <openType>1</openType>
	// <url>www.baidu.com</url>
	// <adType>0</adType>
	// </advertise>

	/**
	 * 获取广告Id
	 * 
	 * @return
	 */
	public String getAdId()
	{
		return adId;
	}

	void setAdId(String adId)
	{
		this.adId = adId;
	}

	/**
	 * 获取展示位上展示的图片的url
	 * 
	 * @return
	 */
	public String getImgurl()
	{
		return imgurl;
	}

	void setShowImgurl(String imgurl)
	{
		this.imgurl = imgurl;
	}

	/**
	 * 获取点击展示位后的打开方式（具体由服务器端定义）
	 * 
	 * @return
	 */
	public int getOpenType()
	{
		return openType;
	}

	void setOpenType(String openType)
	{
		if (openType != null)
			this.openType = Integer.parseInt(openType.trim());
	}

	/**
	 * 获取展示位的数据内容。<br>
	 * 即系：点击展示位后所需展示的数据<br>
	 * 比如：一个URL地址或一个电话号码或一个email地址，这个值是服务器返回的
	 * 
	 * @return
	 */
	public String getUrl()
	{
		return url;
	}

	void setUrl(String url)
	{
		this.url = url;
	}

	/**
	 * 获取广告类型，0：商务广告，1：推广广告（具体由服务器端定义）
	 * 
	 * @return
	 */
	public int getAdType()
	{
		return adType;
	}

	void setAdType(String adType)
	{
		if (adType != null)
			this.adType = Integer.parseInt(adType.trim());
	}

	/**
	 * 获取展示位展示的数据类型:0为文本，1为图片（具体由服务器端定义）
	 * 
	 * @return
	 */
	public int getShowType()
	{
		return showType;
	}

	void setShowType(String showType)
	{
		if (showType != null)
			this.showType = Integer.parseInt(showType.trim());
	}

	/**
	 * 获取广告标题内容
	 * 
	 * @return
	 */
	public String getTitle()
	{
		return title;
	}

	void setTitle(String title)
	{
		this.title = title;
	}

	
	/**
	 * 获取广告位ID
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
	
	
	

}
