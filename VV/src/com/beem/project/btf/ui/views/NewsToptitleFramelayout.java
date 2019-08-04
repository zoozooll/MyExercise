package com.beem.project.btf.ui.views;

import com.beem.project.btf.ui.entity.NewsCameraImageInfo;
import com.beem.project.btf.ui.fragment.NewsTopTitleHelper;
import com.beem.project.btf.utils.DimenUtils;
import com.beem.project.btf.utils.PictureUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.graphics.PaintFlagsDrawFilter;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

public class NewsToptitleFramelayout extends RelativeLayout {
	private static final String TAG = "NewsToptitleFramelatout";
	private Context mContext;
	private NewsCameraImageInfo currentCameraImageInfo = new NewsCameraImageInfo();
	private ImageView materialImage;
	//编辑区域的ImageView
	private NewsToptitleImageView toptitleImageView;
	private int parentwidth, parentheight;
	private NewsTopTitleHelper titleHelper;
	private OnClickListener clickListener;
	private OnLongClickListener longClickListener;
	private Bitmap srcBitmap;
	private Bitmap bgBitmap;
	private int srcImageViewWidth = 0;
	private int srcImageViewHeight = 0;
	private float bgBitmapScale = 0;
	private boolean isdefaultsrcBitmap = true;
	private int bgBitmapWidth, bgBitmapHeight;
	private int marginHorizontal, marginVertical;
	private float[] currentImageInfoPosition, currentImageInfoSize;

	public NewsToptitleFramelayout(Context context, int parentwidth,
			int parentheight) {
		super(context);
		this.mContext = context;
		this.parentwidth = parentwidth;
		this.parentheight = parentheight;
		init();
	}
	public NewsToptitleFramelayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		init();
	}
	public void init() {
		materialImage = new ImageView(mContext);
		toptitleImageView = new NewsToptitleImageView(mContext);
		addView(toptitleImageView);
		addView(materialImage);
		titleHelper = new NewsTopTitleHelper(this);
	}
	//根据数据添加控件
	private void bindview() {
		Log.i(TAG, "~bindview~");
		//计算模板图的大小位置
		bgBitmap = currentCameraImageInfo.getTemplateBitmap();
		bgBitmapWidth = bgBitmap.getWidth();
		bgBitmapHeight = bgBitmap.getHeight();
		boolean isHorizontalTemplate = bgBitmapWidth > bgBitmapHeight;
		int leastMarginHorizontal = DimenUtils.dip2px(getContext(),
				isHorizontalTemplate ? 10 : 0);
		int leastMarginVertical = DimenUtils.dip2px(getContext(), 10);
		float scaleVertical = 1f * (parentheight - 2 * leastMarginVertical)
				/ bgBitmapHeight;
		float scaleHorizontal = 1f * (parentwidth - 2 * leastMarginHorizontal)
				/ bgBitmapWidth;
		bgBitmapScale = Math.min(scaleHorizontal, scaleVertical);
		int width = (int) (bgBitmapWidth * bgBitmapScale);
		int height = (int) (bgBitmapHeight * bgBitmapScale);
		int trueMariginHorizontal = (parentwidth - width) / 2;
		int trueMariginVertical = (parentheight - height) / 2;
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(width,
				height);
		lp.leftMargin = trueMariginHorizontal;
		lp.topMargin = trueMariginVertical;
		marginHorizontal = lp.leftMargin;
		marginVertical = lp.topMargin;
		materialImage.setImageBitmap(bgBitmap);
		materialImage.setLayoutParams(lp);
		//计算本地图片的大小位置
		RelativeLayout.LayoutParams toptitle_lp = new RelativeLayout.LayoutParams(
				width, height);
		currentImageInfoPosition = currentCameraImageInfo.getImageinfo()
				.getSrcbmposition();
		currentImageInfoSize = currentCameraImageInfo.getImageinfo()
				.getSrcbmsize();
		toptitle_lp.leftMargin = (int) (lp.leftMargin + width
				* currentImageInfoPosition[0] / 100);
		toptitle_lp.topMargin = (int) (lp.topMargin + height
				* currentImageInfoPosition[1] / 100);
		toptitle_lp.width = (int) (width * currentImageInfoSize[0] / 100);
		toptitle_lp.height = (int) (height * currentImageInfoSize[1] / 100);
		srcImageViewWidth = toptitle_lp.width;
		srcImageViewHeight = toptitle_lp.height;
		toptitleImageView.setLayoutParams(toptitle_lp);
		setEditImageView();
		titleHelper.addTitles(currentCameraImageInfo.getToptitlesTextInfo(),
				lp.width, lp.height, lp.leftMargin, lp.topMargin);
	}
	//设置可编辑图片的各种参数
	public void setEditImageView() {
		toptitleImageView.reset();
		srcBitmap = getSdcardBitmap();
		if (srcBitmap == null) {
			isdefaultsrcBitmap = true;
			srcBitmap = getdefaultBitmap();
			toptitleImageView.setScaleType(ScaleType.CENTER);
			toptitleImageView.setImageBitmap(srcBitmap);
			toptitleImageView.setBackgroundColor(Color.parseColor("#404040"));
			toptitleImageView.setOnClickListener(clickListener);
		} else {
			isdefaultsrcBitmap = false;
			//缩放Bitmap到合适的位置
			toptitleImageView.setScaleType(ScaleType.MATRIX);
			toptitleImageView.setImageBitmap(srcBitmap);
			setBitmapMatrix();
			toptitleImageView.setBackgroundColor(Color.WHITE);
			toptitleImageView.setOnClickListener(null);
			toptitleImageView.setOnLongClickListener(longClickListener);
		}
	}
	private void setBitmapMatrix() {
		int srcBitmapWidth = srcBitmap.getWidth();
		int srcBitmapHeight = srcBitmap.getHeight();
		float widthscale = 1.0f * srcImageViewWidth / srcBitmapWidth;
		float heightscale = 1.0f * srcImageViewHeight / srcBitmapHeight;
		if (widthscale >= heightscale) {
			float offy = (srcBitmapHeight * widthscale - srcImageViewHeight) / 2;
			toptitleImageView.setCenter(widthscale, 0, -offy);
		} else if (widthscale < heightscale) {
			float offx = (srcBitmapWidth * heightscale - srcImageViewWidth) / 2;
			toptitleImageView.setCenter(heightscale, -offx, 0);
		}
	}
	public NewsCameraImageInfo getCurrentCameraImageInfo() {
		return currentCameraImageInfo;
	}
	public void setCurrentCameraImageInfo(
			NewsCameraImageInfo currentCameraImageInfo) {
		if (this.currentCameraImageInfo != null) {
			this.currentCameraImageInfo.releaseTemplateBitmap(bgBitmap);
		}
		this.currentCameraImageInfo = currentCameraImageInfo;
		bindview();
	}
	public OnClickListener getClickListener() {
		return clickListener;
	}
	public void setClickListener(OnClickListener clickListener) {
		this.clickListener = clickListener;
	}
	public OnLongClickListener getLongClickListener() {
		return longClickListener;
	}
	public void setLongClickListener(OnLongClickListener longClickListener) {
		this.longClickListener = longClickListener;
	}
	//获取toptitleImageView的Bitmap
	private Bitmap getSdcardBitmap() {
		Bitmap temp = PictureUtil.decodeUriAsBitmap(PictureUtil
				.getUnClipTempImage());
		return temp;
	}
	//获取默认图片
	private Bitmap getdefaultBitmap() {
		return ImageLoader.getInstance().loadImageSync(
				"assets://image/NewsCameraToptitle/templete_default2.png");
	}
	//图片合成
	public Bitmap combineBitmap() {
		if (isdefaultsrcBitmap) {
			return null;
		}
		Bitmap newb = Bitmap.createBitmap(bgBitmap.getWidth(),
				bgBitmap.getHeight(), Config.ARGB_8888);
		Bitmap newb2 = Bitmap.createBitmap(srcImageViewWidth,
				srcImageViewHeight, Config.ARGB_8888);
		Canvas canvas2 = new Canvas(newb2);
		canvas2.drawColor(Color.WHITE);
		canvas2.drawBitmap(srcBitmap, toptitleImageView.getImageMatrix(), null);
		Matrix matrix = new Matrix();
		matrix.setScale(1.0f / bgBitmapScale, 1.0f / bgBitmapScale);
		matrix.postTranslate(bgBitmapWidth * currentImageInfoPosition[0] / 100,
				bgBitmapHeight * currentImageInfoPosition[1] / 100);
		Canvas canvas = new Canvas(newb);
		canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
				| Paint.FILTER_BITMAP_FLAG));
		// 得到新的图片
		canvas.drawBitmap(newb2, matrix, null);
		//绘制模板图像
		canvas.drawBitmap(bgBitmap, 0, 0, null);
		titleHelper.drawTitles(canvas, bgBitmapWidth, bgBitmapHeight,
				marginHorizontal, marginVertical);
		return newb;
	}
	public NewsTopTitleHelper getTitleHelper() {
		return titleHelper;
	}
	public void removeFromParent() {
		ViewGroup parent = (ViewGroup) getParent();
		if (parent != null) {
			parent.removeView(this);
		}
		currentCameraImageInfo.releaseTemplateBitmap(bgBitmap);
	}
}
