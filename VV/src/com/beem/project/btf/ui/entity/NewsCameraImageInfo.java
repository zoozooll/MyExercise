package com.beem.project.btf.ui.entity;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.util.Log;

import com.beem.project.btf.BeemApplication;
import com.beem.project.btf.R;
import com.beem.project.btf.utils.PictureUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

/** 娱乐相机素材实体类 */
public class NewsCameraImageInfo implements Serializable {
	private static final long serialVersionUID = 3554739543102352402L;
	private static final String TAG = "NewsCameraImageInfo";
	private String[] righttoparray = BeemApplication.getContext()
			.getResources().getStringArray(R.array.RightTopArray);
	private String[] livearray = BeemApplication.getContext().getResources()
			.getStringArray(R.array.LiveArray);
	private String[] moviearray = BeemApplication.getContext().getResources()
			.getStringArray(R.array.MovieArray);

	public enum NewsTextType {
		/**
		 * SubTitle-字幕,BigTitle-大标题,SmallTitle-小标题,Marquee-跑马,Area-省份 ,Time-时间
		 * ,RightTop-右上,Movie-宽屏,Live-直播
		 */
		subtitle("字幕"), bigtitle("大标题"), smalltitle("小标题"), marquee("跑马"),
		area("省份"), time("时间"), righttop("右上"), movie("宽屏"), live("直播"),
		toptitle("海报标题");
		private String description;

		private NewsTextType(String name) {
			this.description = name;
		}
		public String getName() {
			return description;
		}
	}

	//字幕
	private NewsTextInfo subtitle;
	//字幕画笔
	private Paint subtitle_paint;
	//大标题
	private NewsTextInfo bigtitle;
	//大标题画笔
	private Paint bigtitle_paint;
	//小标题
	private NewsTextInfo smalltitle;
	//小标题画笔
	private Paint smalltitle_paint;
	//跑马
	private NewsTextInfo marquee;
	//跑马画笔
	private Paint marquee_paint;
	//地区 
	private NewsTextInfo area;
	//地区画笔
	private Paint area_paint;
	//时间
	private NewsTextInfo time;
	//时间画笔
	private Paint time_paint;
	//右上标识
	private String RightTop = righttoparray[0];
	//直播标识
	private String Live = livearray[0];
	//宽屏标示
	private String Movie = moviearray[0];
	//素材场景图
	private NewsImageInfo imageinfo;
	//画布对象
	private Canvas canvas;
	//历史图像
	private Bitmap oldBitmap;
	//头条新闻的标题
	private List<NewsTextInfo> toptitles;
	private boolean isdefaultsrcBitmap = true;

	public Bitmap getCombineBitmap() {
		if (isdefaultsrcBitmap) {
			return null;
		}
		return oldBitmap;
	}
	public NewsTextInfo getSubtitle() {
		return subtitle;
	}
	public void setSubtitle(NewsTextInfo subtitle) {
		this.subtitle = subtitle;
	}
	public NewsTextInfo getBigtitle() {
		return bigtitle;
	}
	public void setBigtitle(NewsTextInfo bigtitle) {
		this.bigtitle = bigtitle;
	}
	public NewsTextInfo getSmalltitle() {
		return smalltitle;
	}
	public void setSmalltitle(NewsTextInfo smalltitle) {
		this.smalltitle = smalltitle;
	}
	public NewsTextInfo getMarquee() {
		return marquee;
	}
	public void setMarquee(NewsTextInfo marquee) {
		this.marquee = marquee;
	}
	public NewsTextInfo getArea() {
		return area;
	}
	public void setArea(NewsTextInfo area) {
		this.area = area;
	}
	public NewsImageInfo getImageinfo() {
		return imageinfo;
	}
	public NewsTextInfo getTime() {
		return time;
	}
	public void setTime(NewsTextInfo time) {
		this.time = time;
	}
	public void setImageinfo(NewsImageInfo imageinfo) {
		this.imageinfo = imageinfo;
	}
	public NewsTextInfo getTextInfo(NewsTextType type) {
		switch (type) {
			case subtitle: {
				return subtitle;
			}
			case bigtitle: {
				return bigtitle;
			}
			case smalltitle: {
				return smalltitle;
			}
			case marquee: {
				return marquee;
			}
			case area: {
				return area;
			}
			default:
				return subtitle;
		}
	}
	public List<NewsTextInfo> getToptitlesTextInfo() {
		return toptitles;
	}
	public void setToptitlesTextInfo(List<NewsTextInfo> toptitles) {
		this.toptitles = toptitles;
	}
	public String getTextString(NewsTextType type) {
		switch (type) {
			case righttop: {
				return RightTop;
			}
			case movie: {
				return Movie;
			}
			case live: {
				return Live;
			}
			default:
				return RightTop;
		}
	}
	public String getRightTop() {
		return RightTop;
	}
	public void setRightTop(String rightTop) {
		RightTop = rightTop;
	}
	public String getLive() {
		return Live;
	}
	public void setLive(String live) {
		Live = live;
	}
	public String getMovie() {
		return Movie;
	}
	public void setMovie(String movie) {
		Movie = movie;
	}
	//刷新画笔设置
	public void ChangePaintSetting() {
		setPaint(subtitle, subtitle_paint);
		setPaint(bigtitle, bigtitle_paint);
		setPaint(smalltitle, smalltitle_paint);
		setPaint(marquee, marquee_paint);
		setPaint(area, area_paint);
		setPaint(time, time_paint);
	}
	//设置单个画笔
	private void setPaint(NewsTextInfo tf, Paint paint) {
		if (tf != null) {
			if (paint == null) {
				paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			}
			paint.setTextSize(tf.getTextsize());
			paint.setColor(tf.getTextcolor());
			if (tf.Iscenter()) {
				paint.setTextAlign(Paint.Align.CENTER);
			}
			if (tf.Isshadow()) {
				paint.setShadowLayer(5, 0, 0, Color.parseColor("#000000"));
			}
			if (tf.getTexttype().equals(NewsTextType.subtitle.toString())) {
				this.subtitle_paint = paint;
			} else if (tf.getTexttype()
					.equals(NewsTextType.bigtitle.toString())) {
				this.bigtitle_paint = paint;
			} else if (tf.getTexttype().equals(
					NewsTextType.smalltitle.toString())) {
				this.smalltitle_paint = paint;
			} else if (tf.getTexttype().equals(NewsTextType.marquee.toString())) {
				this.marquee_paint = paint;
			} else if (tf.getTexttype().equals(NewsTextType.area.toString())) {
				this.area_paint = paint;
			} else if (tf.getTexttype().equals(NewsTextType.time.toString())) {
				Typeface font = Typeface.create(Typeface.SANS_SERIF,
						Typeface.BOLD);
				paint.setTypeface(font);
				this.time_paint = paint;
			}
			Log.i(TAG, "1~~paint~~" + paint);
		} else {
			paint = null;
			Log.i(TAG, "2~~paint~~" + paint);
		}
	}
	//合成图像
	public Bitmap combineBitmap() {
		Bitmap template_pic = getTemplateBitmap();
		Bitmap src_pic = getdefaultBitmap();
		if (oldBitmap != null && !oldBitmap.isRecycled()) {
			oldBitmap.recycle();
		}
		//新建一块蒙版画布
		Bitmap newb = null;
		if (template_pic != null) {
			newb = Bitmap.createBitmap(template_pic.getWidth(),
					template_pic.getHeight(), Config.ARGB_8888);
		} else {
			newb = Bitmap.createBitmap(src_pic.getWidth(), src_pic.getHeight(),
					Config.ARGB_8888);
		}
		this.canvas = new Canvas(newb);
		float scale = 1f * newb.getWidth() / src_pic.getWidth();
		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);
		//绘制实时图像
		this.canvas.drawBitmap(src_pic, matrix, null);
		drawMovie();
		//绘制模板图像
		this.canvas.drawBitmap(template_pic, 0, 0, null);
		drawRightTop();
		drawLive();
		//绘制文字
		drawText();
		DisplayMetrics dm = BeemApplication.getContext().getResources()
				.getDisplayMetrics();
		int screenWidth = dm.widthPixels;
		float scale2 = 1f * screenWidth / newb.getWidth();
		oldBitmap = newb;
		Bitmap retBitmap = zoomBitmap(newb, scale2);
		return retBitmap;
	}
	//销毁bitmap
	private boolean destroyBitmap(Bitmap bm) {
		if (bm != null && !bm.isRecycled()) {
			bm.recycle();
			return true;
		}
		return false;
	}
	//绘制右上标识
	private void drawRightTop() {
		Bitmap temp = getRightTopBitmap();
		if (temp != null) {
			this.canvas.drawBitmap(temp, 750 - temp.getWidth() / 2,
					47 - temp.getHeight() / 2, null);
		}
	}
	//绘制直播标识
	private void drawLive() {
		Bitmap temp = getLiveBitmap();
		if (temp != null) {
			this.canvas.drawBitmap(temp, 750 - temp.getWidth() / 2,
					90 - temp.getHeight() / 2, null);
		}
	}
	//绘制宽屏标识
	private void drawMovie() {
		Bitmap temp = getMovieBitmap();
		if (temp != null) {
			this.canvas.drawBitmap(temp, 0, 0, null);
		}
	}
	//绘制文字方法
	private void drawText() {
		Log.i(TAG, "~drawText~");
		drawOneText(subtitle, subtitle_paint);
		drawOneText(bigtitle, bigtitle_paint);
		drawOneText(smalltitle, smalltitle_paint);
		drawOneText(marquee, marquee_paint);
		drawOneText(area, area_paint);
		drawOneText(time, time_paint);
	}
	//绘制单个文字方法
	private void drawOneText(NewsTextInfo tf, Paint paint) {
		if (this.canvas != null && tf != null && paint != null) {
			float x = tf.getTextposition()[0];
			float y = tf.getTextposition()[1];
			//y轴校正
			FontMetrics fontMetrics = paint.getFontMetrics();
			// fontMetrics.bottom为正，fontMetrics.top为负
			float fontTotalHeight = fontMetrics.bottom - fontMetrics.top;
			float offY = fontTotalHeight / 2 + fontMetrics.bottom;
			if (tf.Iscenter()) {
				//x轴校正
				x = x + paint.measureText(tf.getDefaultNoteText()) / 2;
			}
			this.canvas.drawText(tf.getNotetext(), x, y + offY, paint);
			Log.i(TAG, "~x~" + x + "~y~" + y + "~note~" + tf.getNotetext()
					+ " canvas.getWidth:" + canvas.getWidth()
					+ " canvas.getHeight:" + canvas.getHeight());
		} else {
			Log.i(TAG, "~canvas~" + this.canvas + "~NewsTextInfo~" + tf
					+ "~paint~" + paint);
		}
	}
	//获取模板图像
	public Bitmap getTemplateBitmap() {
		return ImageLoader.getInstance().loadImageSync(imageinfo.getPath());
	}
	//释放模板图像
	public void releaseTemplateBitmap(Bitmap bitmap) {
		if (bitmap != null && !bitmap.isRecycled()) {
			bitmap.recycle();
			ImageLoader.getInstance().getMemoryCache()
					.remove(imageinfo.getPath());
		}
	}
	//获取右上标识图片
	private Bitmap getRightTopBitmap() {
		if (RightTop.equals(righttoparray[0])) {
			return null;
		} else if (RightTop.equals(righttoparray[1])) {
			return ImageLoader.getInstance().loadImageSync(
					"assets://image/NewsCameraTv/cccv_com.png");
		} else if (RightTop.equals(righttoparray[2])) {
			return ImageLoader.getInstance().loadImageSync(
					"assets://image/NewsCameraTv/cntv_logo.png");
		}
		return null;
	}
	//获取直播标识图片
	private Bitmap getLiveBitmap() {
		if (Live.equals(livearray[0])) {
			return null;
		} else if (Live.equals(livearray[1])) {
			return ImageLoader.getInstance().loadImageSync(
					"assets://image/NewsCameraTv/live1.png");
		} else if (Live.equals(livearray[2])) {
			return ImageLoader.getInstance().loadImageSync(
					"assets://image/NewsCameraTv/live2.png");
		}
		return null;
	}
	//获取宽屏图片
	private Bitmap getMovieBitmap() {
		if (Movie.equals(moviearray[1])) {
			return null;
		} else if (Movie.equals(moviearray[0])) {
			return ImageLoader.getInstance().loadImageSync(
					"assets://image/NewsCameraTv/movie_screen.png");
		}
		return null;
	}
	//获取默认图片
	private Bitmap getdefaultBitmap() {
		Bitmap temp = PictureUtil.decodeUriAsBitmap(PictureUtil
				.getClipTempImage());
		if (temp != null) {
			isdefaultsrcBitmap = false;
			return temp;
		}
		return ImageLoader.getInstance().loadImageSync(
				"assets://image/NewsCameraTv/templete_default.png");
	}
	//缩放图片以适应屏幕
	private Bitmap zoomBitmap(Bitmap bm, float scale) {
		// 获得图片的宽高
		int width = bm.getWidth();
		int height = bm.getHeight();
		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);
		// 得到新的图片
		Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
				true);
		return newbm;
	}
	@Override
	public String toString() {
		return "NewsCameraImageInfo [righttoparray="
				+ Arrays.toString(righttoparray) + ", livearray="
				+ Arrays.toString(livearray) + ", moviearray="
				+ Arrays.toString(moviearray) + ", subtitle=" + subtitle
				+ ", subtitle_paint=" + subtitle_paint + ", bigtitle="
				+ bigtitle + ", bigtitle_paint=" + bigtitle_paint
				+ ", smalltitle=" + smalltitle + ", smalltitle_paint="
				+ smalltitle_paint + ", marquee=" + marquee
				+ ", marquee_paint=" + marquee_paint + ", area=" + area
				+ ", area_paint=" + area_paint + ", time=" + time
				+ ", time_paint=" + time_paint + ", RightTop=" + RightTop
				+ ", Live=" + Live + ", Movie=" + Movie + ", imageinfo="
				+ imageinfo + ", canvas=" + canvas + ", oldBitmap=" + oldBitmap
				+ "]";
	}
}
