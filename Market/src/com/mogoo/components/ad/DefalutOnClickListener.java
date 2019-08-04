package com.mogoo.components.ad;

import com.mogoo.components.ad.utils.HttpUtil;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

/**
 * 默认的监听器
 *
 * @author Administrator
 *
 */
public class DefalutOnClickListener implements AdOnClickListener
{
	private static final String tag = "DefalutOnClickListener";
	/**
	 * 用浏览器打开链接
	 */
	private static final int TARGET_TYPE_BROWSER = 2;
	/**
	 * 启动发送email的activity
	 */
	private static final int TARGET_TYPE_EMAIL = 3;
	/**
	 * 拔打电话
	 */
	private static final int TARGET_TYPE_TELEPHONE = 4;
	/**
	 * 发送短信息
	 */
	private static final int TARGET_TYPE_SMS = 5;
	/**
	 * 打开相册
	 */
	private static final int TARGET_TYPE_GALLERY = 6;
	private Context mContext;

	private void Log(String msg)
	{
		MogooInfo.Log(tag, msg);
	}

	public DefalutOnClickListener(Context context)
	{
		super();
		mContext = context;
	}

	@Override
	public void OnClick(AdvertiseItem item)
	{
		// TODO Auto-generated method stub
		Log.i(tag, "缺省的点击事件!!!");
		Log.i(tag, "item.getImgurl():" + item.getImgurl());
		Log.i(tag, "item.getUrl():" + item.getUrl());
		int openType = item.getOpenType();
		// 商务广告才处理
		if (item.getAdType() == 0)
		{

			submitsSatistics(item);
			try
			{
				switch (openType)
				{
				// 用浏览器打开
				case TARGET_TYPE_BROWSER:

					String url = item.getUrl().trim();
					// String url = "www.baidu.com";
					String http = "http://";
					if (url.indexOf(http) < 0)
					{
						url = http + url;
					}
					Uri uri = Uri.parse(url);
					mContext.startActivity(new Intent(Intent.ACTION_VIEW, uri));

					break;
				// 发信息
				case TARGET_TYPE_SMS:
					Uri smsUri = Uri.parse("smsto:" + item.getUrl());
					Intent intent = new Intent(Intent.ACTION_SENDTO, smsUri);
					mContext.startActivity(intent);
					break;
				// 拨打电话
				case TARGET_TYPE_TELEPHONE:
					Uri telUri = Uri.parse("tel:" + item.getUrl());
					Intent dialIntent = new Intent(Intent.ACTION_DIAL, telUri);
					mContext.startActivity(dialIntent);
					break;
				// 发邮件
				case TARGET_TYPE_EMAIL:
					Uri emailuri = Uri.parse("mailto:" + item.getUrl());
					Intent returnIt = new Intent(Intent.ACTION_SENDTO, emailuri);
					mContext.startActivity(returnIt);
					break;
				// 打开相册
				case TARGET_TYPE_GALLERY:
					Uri playUri = Uri.parse("file://" + item.getUrl());
					Intent returnIt1 = new Intent(Intent.ACTION_VIEW);
					returnIt1.setDataAndType(playUri, "image/*");
					mContext.startActivity(returnIt1);
					break;

				default:
					break;
				}
			}
			catch (ActivityNotFoundException e)
			{
				e.printStackTrace();
				Log(e.getMessage());
			}
		}

	}

	// 提交给商务类广告点击统计接口
	// http://192.168.0.177:8088/AD/sendAppAdvertise.action?appId=#&uid=#&adId=#&aid=#&akey=#
	private void submitsSatistics(final AdvertiseItem item)
	{
		Thread t = new Thread()
		{
			@Override
			public void run()
			{
				// 测试用
				// String url =
				// "http://192.168.0.177:8088/AD/sendAppAdvertise.action?appId=4654644654&uid=546545&adId=12&aid=#&akey=asdf6545465";
				String url = MogooInfo.statisticsIp
						+ "/AD/sendAppAdvertise.action?appId="
						+ MogooInfo.appId + "&uid=" + MogooInfo.uid + "&adId="
						+ item.getAdId() + "&aid=" + MogooInfo.aid + "&akey=a"
						+ MogooInfo.akey;

				String result = HttpUtil.getStringFromURL(url);
				Log(url);
				Log("result:" + result);
			}
		};
		t.start();
		t = null;

	}

}
