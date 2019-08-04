package com.beem.project.btf.ui.entity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;

import com.beem.project.btf.BeemApplication;
import com.beem.project.btf.R;
import com.beem.project.btf.constant.Constants;
import com.beem.project.btf.ui.entity.NewsCameraImageInfo.NewsTextType;
import com.beem.project.btf.utils.BBSUtils;
import com.butterfly.vv.db.ormhelper.DBHelper;
import com.butterfly.vv.db.ormhelper.DBHelper.DBWhere;
import com.butterfly.vv.db.ormhelper.DBHelper.DBWhere.DBWhereType;
import com.butterfly.vv.db.ormhelper.bean.BaseDB;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/** 娱乐相机文字实体类 */
@DatabaseTable
public class NewsTextInfo extends BaseDB {
	//唯一标示 templateid+texttype
	@DatabaseField(id = true)
	private String id;
	//外键与素材表关联
	@DatabaseField
	private String templateid;
	//类型(字幕，大标题，小标题.....)
	@DatabaseField
	private String texttype;
	//文字内容
	@DatabaseField(defaultValue = "")
	private String notetext;
	//文字位置
	@DatabaseField
	private String textposition;
	//文字大小
	@DatabaseField
	private String textsize;
	//文字编辑区大小
	@DatabaseField
	private String textBound;
	//文字字体路径
	@DatabaseField
	private String fontPath;
	@DatabaseField
	private String gravity;
	//文字颜色
	@DatabaseField
	private String textcolor;
	//是否水平居中
	@DatabaseField(defaultValue = "false")
	private String iscenter;
	//是否有文字阴影
	@DatabaseField(defaultValue = "false")
	private String isshadow;
	//可输入文字大小
	@DatabaseField
	private int linenum = 1;
	//是否粗体
	@DatabaseField
	private boolean isBold;
	@DatabaseField
	private long birthTime;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTemplateid() {
		return templateid;
	}
	public void setTemplateid(String templateid) {
		this.templateid = templateid;
	}
	public String getTexttype() {
		return texttype;
	}
	public void setTexttype(String texttype) {
		this.texttype = texttype;
	}
	public String getNotetext() {
		if (TextUtils.isEmpty(notetext)) {
			notetext = getDefaultNoteText();
		}
		if (texttype.equals(NewsTextType.time.toString())) {
			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat("HH:mm"); // 格式化时间
			notetext = format.format(date);
		}
		return notetext;
	}
	public String getText() {
		return notetext;
	}
	public int getGravity() {
		int retval = Gravity.CENTER;
		if (gravity.equals("left")) {
			retval = Gravity.LEFT | Gravity.CENTER_VERTICAL;
		} else if (gravity.equals("right")) {
			retval = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
		} else if (gravity.equals("left|top")) {
			retval = Gravity.LEFT | Gravity.TOP;
		}
		return retval;
	}
	public String getDefaultNoteText() {
		String defaultStr = "";
		if (texttype.equals(NewsTextType.subtitle.toString())) {
			defaultStr = BeemApplication.getContext().getResources()
					.getString(R.string.Subtitle);
		} else if (texttype.equals(NewsTextType.bigtitle.toString())) {
			defaultStr = BeemApplication.getContext().getResources()
					.getString(R.string.BigTitle);
		} else if (texttype.equals(NewsTextType.smalltitle.toString())) {
			defaultStr = BeemApplication.getContext().getResources()
					.getString(R.string.SmallTitle);
		} else if (texttype.equals(NewsTextType.marquee.toString())) {
			defaultStr = BeemApplication.getContext().getResources()
					.getString(R.string.Marquee);
		} else if (texttype.equals(NewsTextType.area.toString())) {
			defaultStr = BeemApplication.getContext().getResources()
					.getString(R.string.Area);
		} else if (texttype.equals(NewsTextType.time.toString())) {
			defaultStr = BeemApplication.getContext().getResources()
					.getString(R.string.Time);
		}
		return defaultStr;
	}
	public void setNotetext(String notetext) {
		this.notetext = notetext;
	}
	public float[] getTextposition() {
		if (!TextUtils.isEmpty(textposition)) {
			String bitmapposition[] = BBSUtils.splitString(
					Constants.TIMECAMERA_TEXTPOSITIONSPLIT, textposition);
			return new float[] { Float.parseFloat(bitmapposition[0]),
					Float.parseFloat(bitmapposition[1]) };
		} else {
			return new float[] { 0.0f, 0.0f };
		}
	}
	public void setTextposition(String textposition) {
		this.textposition = textposition;
	}
	public float[] getTextBound() {
		if (!TextUtils.isEmpty(textBound)) {
			String bitmapposition[] = BBSUtils.splitString(
					Constants.TIMECAMERA_TEXTPOSITIONSPLIT, textBound);
			return new float[] { Float.parseFloat(bitmapposition[0]),
					Float.parseFloat(bitmapposition[1]) };
		} else {
			return new float[] { 0, 0 };
		}
	}
	public void setTextbound(String textbound) {
		this.textBound = textbound;
	}
	public float getTextsize() {
		return Float.parseFloat(textsize);
	}
	public void setTextsize(String textsize) {
		this.textsize = textsize;
	}
	public int getTextcolor() {
		if (textcolor == null) {
			return Color.parseColor("#ffffff");
		}
		return Color.parseColor(textcolor);
	}
	public void setTextcolor(String textcolor) {
		this.textcolor = textcolor;
	}
	public boolean Iscenter() {
		return Boolean.parseBoolean(iscenter);
	}
	public void setIscenter(String iscenter) {
		this.iscenter = iscenter;
	}
	public boolean Isshadow() {
		return Boolean.parseBoolean(isshadow);
	}
	public void setIsshadow(String isshadow) {
		this.isshadow = isshadow;
	}
	@Override
	public void saveToDatabase() {
		fields.put(DBKey.id.toString(), id);
		DBHelper.getInstance().saveToDatabaseSync(this,
				new DBWhere(DBKey.id, DBWhereType.eq, id));
	}
	//根据id来查询数据
	public static NewsTextInfo queryItemsById(String id) {
		NewsTextInfo dbInfo = DBHelper.getInstance().queryForFirst(
				NewsTextInfo.class, new DBWhere(DBKey.id, DBWhereType.eq, id));
		return dbInfo;
	}
	//查询表某个模板中某个类型的所有标题集
	public static List<NewsTextInfo> querySomeItemsByTemplateId(
			String template_id, NewsTextType type) {
		List<NewsTextInfo> retVal = DBHelper.getInstance().queryAll(
				NewsTextInfo.class,
				new DBWhere(DBKey.templateid, DBWhereType.eq, template_id),
				new DBWhere(DBKey.texttype, DBWhereType.eq, type.toString()));
		return retVal;
	}
	@Override
	public String toString() {
		return "NewsTextInfo [id=" + id + ", templateid=" + templateid
				+ ", texttype=" + texttype + ", notetext=" + notetext
				+ ", textposition=" + textposition + ", textsize=" + textsize
				+ ", textcolor=" + textcolor + "]";
	}
	public NewsTextType getTextType() {
		return NewsTextType.valueOf(texttype);
	}
	public String getFontPath() {
		return fontPath;
	}
	public void setFontPath(String fontPath) {
		this.fontPath = fontPath;
	}
	public int getLinenum() {
		return linenum;
	}
	public boolean isBold() {
		return isBold;
	}
}
