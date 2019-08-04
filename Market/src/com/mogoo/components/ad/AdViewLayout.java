package com.mogoo.components.ad;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.Animation;
import android.widget.LinearLayout;

import com.mogoo.components.ad.utils.AdapterDisplay;

/**
 * 展示组件，给开发者调用的layout
 * <p>
 * 用法如下: <br>
 * 1.在AndroidManifest.xml里添加如下权限:<br>
 * <br>
 * <a href="../../../motone/module/ad/menifest.txt">点击参看需要添加的权限</a>
 * <p>
 * <br>
 * 2.在values里新建一个attrs.xml文件: <br>
 * <a href="../../../motone/module/ad/attrs.txt">attrs.xml</a>
 * <p>
 * <br>
 * 3.假如你的布局文件为: <br>
 * <a href="../../../motone/module/ad/xmlsample.txt">xmlsample.xml</a>
 * <p>
 * <br>
 * 4.在这个Activity里使用: <br>
 * <a href="../../../motone/module/ad/MogooTestActivity.txt">MogooTestActivity.
 * java</a>
 * <p>
 *
 * @author wishwingliao@163.com 2011-12-20
 */
public class AdViewLayout extends LinearLayout
{

	private MogooLayoutParent adLayout;
	// private MogooAnimationManager mogooAnimationManager;
	// private View adLayout;
	private Context mContext;
	private static final String tag = "AdViewLayout";
	
	//add by csq
	private DoubleRowSlideButtomView buttomView;

	/**
	 * 这个构造函数是xml布局时要调用到，必须声明
	 *
	 * @param context
	 * @param attributeset
	 */
	public AdViewLayout(Context context, AttributeSet attributeset)
	{
		super(context, attributeset);
		this.mContext = context;
		initAttribute(context, attributeset);
	}

	/**
	 * 构建一个默认值的展示组件
	 *
	 * @param context
	 */
	public AdViewLayout(Context context)
	{
		super(context);
		this.mContext = context;
		// mogooAnimationManager = new MogooAnimationManager(context);
	}

	/**
	 * 注册一些信息(必须)
	 *
	 * @param appId
	 *            应用ID
	 * @param uid
	 *            手机唯一标识
	 * @param akey
	 *            鉴权akey
	 * @param aid
	 *            账号Id
	 * @param statisticsIp
	 *            统计商务广告点击量时用到的IP地址<br>
	 *            假如统计接口为:http://192.168.0.177:8088/AD/sendAppAdvertise.action<br>
	 *            那么:应该设的IP地址为:http://192.168.0.177:8088
	 */
	public void registerMogooInfo(String appId, String uid, String akey,
			String aid, String statisticsIp)
	{
		MogooInfo.appId = appId;
		MogooInfo.uid = uid;
		MogooInfo.akey = akey;
		MogooInfo.aid = aid;
		MogooInfo.statisticsIp = statisticsIp;
	}

	private void initAttribute(Context context, AttributeSet attributeset)
	{
		int backgroudcolor = 0xff000000;
		int backgroudtran = 255;
		int textcolor = -1;
		addAD(context, attributeset, 0, backgroudcolor, textcolor,
				backgroudtran);

	}

	private void addAD(Context paramContext, AttributeSet paramAttributeSet,
			int i1, int backgroudcolor, int textcolor, int backgroudtran)
	{
		int i2 = textcolor;
		int i3 = backgroudcolor;
		int i4 = backgroudtran;
		try
		{
			// 读取xml布局里的属性值
			if (paramAttributeSet != null)
			{
				String str = "http://schemas.android.com/apk/res/"
						+ paramContext.getPackageName();
				i2 = paramAttributeSet.getAttributeUnsignedIntValue(str,
						"textColor", -1);
				i3 = paramAttributeSet.getAttributeUnsignedIntValue(str,
						"backgroundColor", -16777216);
				i4 = paramAttributeSet.getAttributeUnsignedIntValue(str,
						"backgroundTransparent", 255);

				if (i4 > 255)
					i4 = 255;
				if (i4 < 0)
					i4 = 0;
				MogooInfo.textColor = i2;
				MogooInfo.backgroundColor = i3;
				MogooInfo.backgroundTransparent = i4;
				MogooInfo.adHeight = paramAttributeSet
						.getAttributeUnsignedIntValue(str, "adHeight", -1);

				MogooInfo.updateTime = paramAttributeSet
						.getAttributeUnsignedIntValue(str, "updateTime", 0);
				MogooInfo.refreshTime = paramAttributeSet
						.getAttributeUnsignedIntValue(str, "refreshTime", -1);

				MogooInfo.IS_DOUBLE_ROW = paramAttributeSet
						.getAttributeBooleanValue(str, "isDoubleRow", false);
				MogooInfo.DEFAULT_AD_PIC_RES_ID = paramAttributeSet
						.getAttributeResourceValue(str, "defaultAdImg", 0);

				MogooInfo.url = paramAttributeSet.getAttributeValue(str, "url");
				// MogooInfo.base_url = paramAttributeSet.getAttributeValue(str,
				// "baseUrl");

				MogooInfo.PAGE = paramAttributeSet
						.getAttributeUnsignedIntValue(str, "page", 1);

				MogooInfo.AD_PADING = paramAttributeSet
						.getAttributeUnsignedIntValue(str, "adPading", 5);

				if (MogooInfo.adHeight < 0)
				{
					loadDefaultHeight();
				}

				MogooInfo.Log(tag, "InitInfo.DEFAULT_AD_PIC_RES_ID:"
						+ MogooInfo.DEFAULT_AD_PIC_RES_ID);
				MogooInfo.Log(tag, "InitInfo.IS_DOUBLE_ROW:"
						+ MogooInfo.IS_DOUBLE_ROW);
				MogooInfo.Log(tag, "InitInfo.PAGE:" + MogooInfo.PAGE);
				MogooInfo.Log(tag, "InitInfo.url:" + MogooInfo.url);
				// MogooInfo.Log(tag, "InitInfo.base_url:" +
				// MogooInfo.base_url);
				MogooInfo.Log(tag, "InitInfo.updateTime:"
						+ MogooInfo.updateTime);
				MogooInfo.Log(tag, "InitInfo.refreshTime:"
						+ MogooInfo.refreshTime);
				MogooInfo.Log(tag, "InitInfo.adHeight:" + MogooInfo.adHeight);
			}

		}
		catch (Exception localException2)
		{

		}

		// showMe();

	}

	private void loadDefaultHeight()
	{
		// 双行
		if (MogooInfo.IS_DOUBLE_ROW)
		{
			MogooInfo.adHeight = AdapterDisplay.getDoubleRowAdHeight(mContext,
					MogooInfo.AD_PADING);
		}
		// 单行
		else
		{
			MogooInfo.adHeight = AdapterDisplay.getSingleRowAdHeight(mContext);
		}
	}

	/**
	 * 设置点击展示位时的事件监听器<br>
	 * 监听器类必须是AdOnClickListener接口的实现类。
	 * <p>
	 * 如果没有设置此项，组件只会默认处理一部分点击事件响应<br>
	 *
	 * @param listener
	 *            请参阅: @link com.motone.module.ad.AdOnClickListener
	 */
	public void setAdOnClickListener(AdOnClickListener listener)
	{
		if (adLayout != null)
			adLayout.setAdOnClickListener(listener);
	}
	
	/**
	 * add by csq:设置底部圆点视图
	 * @param buttomView
	 */
	public void setAdButtomView(DoubleRowSlideButtomView buttomView) {
		this.buttomView = buttomView;
	}

	/**
	 * 显示广告
	 */
	public void showMe()
	{
		this.removeAllViews();
		// 双行
		if (MogooInfo.IS_DOUBLE_ROW)
		{
			// 双行，多页，可滑动
			if (MogooInfo.PAGE > 1)
			{
				MogooInfo.Log(tag, "双行，可滑动");
				adLayout = AdLayoutViewCreator.getAdLayoutView(mContext,
						AdLayoutViewCreator.doubleSlide);
				
				//add by csq
				if(this.buttomView!=null)
				{
					adLayout.setButtomView(buttomView);
				}
			}
			// 双行，单页，不可滑动
			else
			{
				MogooInfo.Log(tag, "双行,不可滑动");
				adLayout = AdLayoutViewCreator.getAdLayoutView(mContext,
						AdLayoutViewCreator.doubleNotSlide);
			}

		}
		// 单行
		else
		{
			adLayout = AdLayoutViewCreator.getAdLayoutView(mContext,
					AdLayoutViewCreator.singleNotSlide);

		}

		if (adLayout != null)
			addView(adLayout);

		setAdOnClickListener(new DefalutOnClickListener(mContext));

	}

	/**
	 * 设置展示文本时，文本的颜色;展示图片时不起作用
	 * <p>
	 * 此属性亦可在XML布局文件里设定<br>
	 *
	 * @param value
	 */
	public void setTextColor(int value)
	{
		MogooInfo.textColor = value;
	}

	/**
	 * 设置展示文本时，展示区的背景色;展示图片时不起作用
	 * <p>
	 * 此属性亦可在XML布局文件里设定<br>
	 *
	 * @param value
	 */
	public void setBackgroundColor(int value)
	{
		MogooInfo.backgroundColor = value;
	}

	/**
	 * 设置展示文本时，展示区的透明度;展示图片时不起作用
	 * <p>
	 * 此属性亦可在XML布局文件里设定<br>
	 *
	 * @param transparent
	 */
	public void setBackgroundTransparent(int transparent)
	{
		if (transparent > 255)
		{
			MogooInfo.backgroundTransparent = 255;

		}
		else if (transparent < 0)
		{
			MogooInfo.backgroundTransparent = 0;
		}
		else
		{
			MogooInfo.backgroundTransparent = transparent;
		}
	}

	/**
	 * 设置缺省的图片资源ID，在未获取到服务器数据时显示此图片;<br>
	 * 展示文本时不起作用
	 * <p>
	 * 此属性亦可在XML布局文件里设定<br>
	 *
	 * @param imageResId
	 */
	public void setDefaultImageResId(int imageResId)
	{
		MogooInfo.DEFAULT_AD_PIC_RES_ID = imageResId;
	}

	/**
	 * 设置获取展示数据的服务器具体地址。<br>
	 * 比如:http://ftp-leo3780267.q5.dns-dns.net/test/test2.txt?appId=356&
	 * positionId=1,2,3,4
	 * <p>
	 * 此属性亦可在XML布局文件里设定<br>
	 *
	 * @param url
	 */
	public void setUrl(String url)
	{
		MogooInfo.url = url;
	}

	/**
	 * 设置请求服务器间隔时长(单位为:秒)<br>
	 * 默认为0,表示只在显示时请求一次服务器，之后不会自动去请求
	 * <p>
	 * 此属性亦可在XML布局文件里设定<br>
	 *
	 * @param timeValue
	 */
	public void setUpdateTime(int timeValue)
	{
		MogooInfo.updateTime = timeValue;
	}

	/**
	 * 设置轮播间隔时长(单位为:秒)，默认为-1，表示由服务器返回的数据来决定;<br>
	 * 如果想自行决定轮播时长的话，请设置为大于0的值<br>
	 * 如果不想让其轮播，也不想让服务器返回的数据来作决定的话，设置为0即可<br>
	 * 当 setIsDoubleRow属性设置为true时(双行展示)，设置此值无效
	 * <p>
	 * 此属性亦可在XML布局文件里设定<br>
	 *
	 * @param timeValue
	 */
	public void setRefreshTime(int timeValue)
	{
		MogooInfo.refreshTime = timeValue;
	}

	/**
	 * 设置轮播时的动画<br>
	 * 如果没有设置，但是服务器有返回轮播方式的数据，则使用服务器指定的方式<br>
	 * 如果没有设置，服务器也没有返回轮播方式的数据，则默认从左至右轮播<br>
	 * 预定义的动画值请参考:com.motone.module.ad.utils.MogooAnimation文档<br>
	 */
	public void setAnimation(Animation anim)
	{
		MogooInfo.animation = anim;
	}

	/**
	 * 设置是否为双行展示，默认为false
	 *
	 * @param value
	 */
	public void setIsDoubleRow(boolean value)
	{
		MogooInfo.IS_DOUBLE_ROW = value;
	}

	/**
	 * 设置双行展示时可滑动总页数<br>
	 * 只有把setIsDoubleRow属性设置为true时，此属性才有效
	 * <p>
	 * 此属性亦可在XML布局文件里设定<br>
	 *
	 * @param value
	 */
	public void setTotalPage(int value)
	{
		MogooInfo.PAGE = value;
	}

	/**
	 * 设置双行展示时，展示位之间的间距<br>
	 * 只有把setIsDoubleRow属性设置为true时，此属性才有效
	 * <p>
	 * 此属性亦可在XML布局文件里设定<br>
	 *
	 * @param value
	 */
	public void setAdPading(int value)
	{
		MogooInfo.AD_PADING = value;
	}

	/**
	 * 设置展示区域的高度<br>
	 * 如果不设置的话，则使用默认高度
	 * <p>
	 * 此属性亦可在XML布局文件里设定<br>
	 *
	 * @param value
	 */
	public void setAdHeight(int value)
	{
		if (value < 0)
			return;
		MogooInfo.adHeight = value;
		// showMe();
	}

	/**
	 * 是否打印debug消息
	 *
	 * @param debug
	 */
	public void setDebug(boolean debug)
	{
		MogooInfo.debug = debug;
	}
	
	
	/**
	 * add by csq:广告多页时有效，是否自动翻页显示，设置自动翻页时间
	 *
	 * autoSnapTime autoSnapTime>0,自动翻页，否则手动翻页
	 */
	public void setAutoSnapTime(int autoSnapTime)
	{
		MogooInfo.autoSnapTime = autoSnapTime;
	}
	
	/**
	 * add by csq:广告多页时有效，停止自动翻页显示
	 */
	public void stopAutoSnap()
	{
		MogooInfo.autoSnapTime = 0;
	}
	
	
	public MogooLayoutParent getAdView() {
		return adLayout;
	}
}
