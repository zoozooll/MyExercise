package com.mogoo.components.ad;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.mogoo.components.ad.utils.HttpUtil;
import com.mogoo.components.ad.utils.XmlUtil;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

class AdDataCache
{
	private static final String TAG = "AdDataCache";
	private static Handler mHandler;
	/** 加载并解释XML数据失败 */
	public static final int loadingFail = 0;
	/** 加载并解释XML数据成功 */
	public static final int loadingOk = 1;

	/** 缓存一个展示位的信息 */
	public static List<AdPositionItem> adPositionItemList = new ArrayList<AdPositionItem>();

	private static void Log(String str)
	{
		MogooInfo.Log(TAG, str);
	}

	private static final void requestAdSlide(String url)
	{
		InputStream mResult = null;
		try
		{
			mResult = HttpUtil.get(url, true);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		parseXML3(mResult);

	}

	// <position pId="4" appId="10000007" alias="广告位别名1" width="100"
	// height="100" changeType="0" changeTime="10">
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
	// </position>

	// 解释的数据请参考:http://www.goodboyenglish.com/test/test2.txt?appId=356&positionId=1,2,3,4
	// 2011.12.29
	// http://ftp-leo3780267.q5.dns-dns.net/test/test5.txt?appId=356&positionId=1,2,3,4
	private static boolean parseXML3(InputStream inputStream)
	{
		adPositionItemList.clear();
		AdPositionItem adPositionItem = null;
		AdvertiseItem advertiseItem = null;
		if (inputStream == null)
			return false;

		// 获得XmlPullParser解析器
		XmlPullParser xmlParser = XmlUtil.getXmlPullParser();
		// XmlPullParser xmlParser = Xml.newPullParser();
		try
		{
			// 设置编码方式
			xmlParser.setInput(inputStream, "utf-8");
			// 获得解析到的事件类别，这里有开始文档，结束文档，开始标签，结束标签，文本等等事件。
			int evtType = xmlParser.getEventType();
			// 一直循环，直到文档结束(从第一个标签开始到最后一个标签结束)
			while (evtType != XmlPullParser.END_DOCUMENT)
			{
				switch (evtType)
				{
				// 捕捉开始标签(任何一个开始的标签)
				case XmlPullParser.START_TAG:
					String tag = xmlParser.getName();
					// <errorCode>3000</errorCode>
					if (tag.equalsIgnoreCase("errorCode"))
					{
						Log("errorCode标签开始");
						String errorCode = xmlParser.nextText().trim();
						Log("errorCode:" + errorCode);
						// 如果 errorCode 标签里的值不是3000的话，不用再往下解释xml数据
						if (!errorCode.equals("3000"))
						{
							return false;
						}
					}

					// 如果是position标签开始，则读取此标签里面的属性值
					if (tag.equalsIgnoreCase("position"))
					{
						Log("position标签开始");
						String pId = xmlParser.getAttributeValue(null, "pId");
						String appId = xmlParser.getAttributeValue(null,
								"appId");
						String alias = xmlParser.getAttributeValue(null,
								"alias");
						String width = xmlParser.getAttributeValue(null,
								"width");
						String height = xmlParser.getAttributeValue(null,
								"height");
						String changeTime = xmlParser.getAttributeValue(null,
								"changeTime");
						String changeType = xmlParser.getAttributeValue(null,
								"changeType");

						Log("alias:" + alias);
						Log("appId:" + appId);
						Log("changeTime:" + changeTime);
						Log("changeType:" + changeType);
						Log("height:" + height);
						Log("pId:" + pId);
						Log("width:" + width);

						if (pId != null && changeTime != null
								&& changeType != null)
						{
							adPositionItem = new AdPositionItem();
							adPositionItem.setAlias(alias);
							adPositionItem.setAppId(appId);
							adPositionItem.setpId(pId);
							adPositionItem.setChangeTime(changeTime);
							adPositionItem.setChangeType(changeType);
							adPositionItem.setHeight(height);
							adPositionItem.setWidth(width);
						}

					}
					else if (adPositionItem != null)
					{

						if (tag.equalsIgnoreCase("advertise"))
						{
							Log("advertise标签开始，不用读取任何值");
							advertiseItem = new AdvertiseItem();
						}
						if (advertiseItem != null)
						{
							// 设置广告位ID
							advertiseItem.setpId(adPositionItem.getpId());

							if (tag.equalsIgnoreCase("adId"))
							{
								Log("adId标签开始");
								String adId = xmlParser.nextText().trim();
								advertiseItem.setAdId(adId);
								Log("adId:" + adId);
							}
							else if (tag.equalsIgnoreCase("title"))
							{
								Log("title标签开始");
								String title = xmlParser.nextText().trim();
								advertiseItem.setTitle(title);
								Log("title:" + title);
							}
							else if (tag.equalsIgnoreCase("imgurl"))
							{
								Log("imgurl标签开始");
								String imgurl = xmlParser.nextText().trim();
								advertiseItem.setShowImgurl(imgurl);
								Log("imgurl:" + imgurl);
							}
							else if (tag.equalsIgnoreCase("showType"))
							{
								Log("showType标签开始");
								String imgurl = xmlParser.nextText().trim();
								advertiseItem.setShowType(imgurl);
								Log("showContent:" + imgurl);
							}
							else if (tag.equalsIgnoreCase("openType"))
							{
								Log("openType标签开始");
								String contentType = xmlParser.nextText()
										.trim();
								advertiseItem.setOpenType(contentType);
								Log("openType:" + contentType);
							}
							else if (tag.equalsIgnoreCase("url"))
							{
								Log("url标签开始");
								String url = xmlParser.nextText().trim();
								advertiseItem.setUrl(url);
								Log("url:" + url);
							}
							else if (tag.equalsIgnoreCase("adType"))
							{
								Log("adType标签开始");
								String adType = xmlParser.nextText().trim();
								advertiseItem.setAdType(adType);
								Log("adType:" + adType);
							}
						}

					}

					break;

				// 捕捉结束标签(任何一个结束的标签)
				case XmlPullParser.END_TAG:
					if (xmlParser.getName().equalsIgnoreCase("advertise"))
					{
						Log("advertise标签结束");
						if (adPositionItem != null)
							adPositionItem.addAdvertiseItemList(advertiseItem);
						advertiseItem = null;

					}
					else if (xmlParser.getName().equalsIgnoreCase("position"))
					{
						Log("position标签结束");
						if (adPositionItem != null)
							adPositionItemList.add(adPositionItem);
						adPositionItem = null;
					}
					break;
				default:
					break;
				}
				// 如果xml没有结束，则导航到下一个节点
				evtType = xmlParser.next();
			}
			sendMsgToHandler(loadingOk);
			return true;
		}
		catch (XmlPullParserException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		sendMsgToHandler(loadingFail);
		return false;
	}

	private static void sendMsgToHandler(int what)
	{
		Message message = new Message();
		message.what = what;
		mHandler.sendMessage(message);
	}

	/**
	 * 开启线程获取广告数据
	 *
	 * @param url
	 *            广告数据地址
	 * @param handler
	 */
	public static final void startThread(String url, Handler handler)
	{
		mHandler = handler;
		Log.e(TAG, "准备加载 广告数据...");
		Log.e(TAG, url);
		ReqestADThread rat = new ReqestADThread(url);
		Thread t = new Thread(rat);
		t.start();
		t = null;
		rat = null;
	}

	private static final class ReqestADThread implements Runnable
	{

		private String url;

		ReqestADThread(String url)
		{
			super();
			this.url = url;
		}

		public void run()
		{
			AdDataCache.requestAdSlide(url);
		}
	}

}
