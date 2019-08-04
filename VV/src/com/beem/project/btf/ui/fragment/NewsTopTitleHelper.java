package com.beem.project.btf.ui.fragment;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.beem.project.btf.R;
import com.beem.project.btf.ui.entity.NewsTextInfo;
import com.beem.project.btf.ui.fragment.TalkFragement.FragmentType;
import com.beem.project.btf.ui.fragment.TalkFragement.SendmsgListenter;
import com.beem.project.btf.ui.views.DragTextViewSub;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Paint.FontMetrics;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

/**
 * @ClassName: NewsTopTitleHelper
 * @Description: 新闻标题helper
 * @author: yuedong bao
 * @date: 2015-11-19 下午3:43:57
 */
public class NewsTopTitleHelper {
	//父控件
	private final RelativeLayout container;
	//标题信息类
	private final Map<NewsTextInfo, View> infoViews = new HashMap<NewsTextInfo, View>();

	public NewsTopTitleHelper(RelativeLayout container) {
		super();
		this.container = container;
	}
	//将Title贴到Container上
	public void addTitles(List<NewsTextInfo> titleInfos, int templateViewW,
			int templateViewH, int marginleft, int marginTop) {
		if (titleInfos == null) {
			return;
		}
		removeAllTitles();
		Map<String, Typeface> faces = new HashMap<String, Typeface>();
		/*//LogUtils.v(" marginleft:" + marginleft + " marginTop:" + marginTop + " templateW:" + templateViewW
				+ " templateH:" + templateViewH + " container.getWidth:" + container.getWidth()
				+ " container.getHeight:" + container.getHeight());*/
		final Context context = container.getContext();
		for (final NewsTextInfo info : titleInfos) {
			if (info != null) {
				final DragTextViewSub dragTextView = new DragTextViewSub(
						context);
				RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
						android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
						android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
				float[] textPos = info.getTextposition();
				//设置textview最大尺寸
				float[] wh = info.getTextBound();
				int horizontalGravity = info.getGravity()
						& Gravity.HORIZONTAL_GRAVITY_MASK;
				switch (horizontalGravity) {
					case Gravity.CENTER_HORIZONTAL: {
						lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
						break;
					}
					case Gravity.LEFT: {
						lp.leftMargin = (int) (templateViewW * textPos[0] / 100)
								+ marginleft;
						lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
						break;
					}
					case Gravity.RIGHT: {
						lp.rightMargin = (int) ((100 - textPos[0] - wh[0])
								/ 100 * templateViewW + marginleft);
						lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
						break;
					}
				}
				lp.topMargin = (int) (templateViewH * textPos[1] / 100)
						+ marginTop;
				dragTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
						templateViewH * info.getTextsize() / 100);
				dragTextView.getPaint().setFakeBoldText(info.isBold());
				//LogUtils.v("wh_w:" + wh[0] + " wh_h:" + wh[1]);
				final int maxWidth = (int) (wh[0] * templateViewW / 100);
				int maxHeight = (int) (wh[1] * templateViewH / 100);
				dragTextView.setMaxWidth(maxWidth);
				dragTextView.setMaxHeight(maxHeight);
				//设置字体
				String fontPath = info.getFontPath();
				if (!TextUtils.isEmpty(fontPath)) {
					if (fontPath.startsWith("assets://")) {
						//本地字库
						if (!faces.containsKey(fontPath)) {
							Typeface tface = Typeface.createFromAsset(
									dragTextView.getContext().getAssets(),
									Scheme.ASSETS.crop(fontPath));
							faces.put(fontPath, tface);
						}
					} else if (fontPath.startsWith("http://")) {
						if (!faces.containsKey(fontPath)) {
							Typeface tface = null;
							//todo 获取http对应的字体
							faces.put(fontPath, tface);
						}
					}
					dragTextView.setTypeface(faces.get(fontPath));
				}
				if (info.Isshadow()) {
					dragTextView.setShadowLayer(5, 0, 0,
							Color.parseColor("#000000"));
				}
				dragTextView.setTextColor(info.getTextcolor());
				dragTextView.setText(info.getText());
				dragTextView.setTag(info);
				dragTextView.setFrameDraw(false);
				dragTextView.setCanMove(false);
				dragTextView.setMaxLines(info.getLinenum());
				dragTextView.setTinkleDrawable(R.drawable.dash_shape);
				dragTextView.setGravity(Gravity.CENTER);
				float textHeight = measureTextHeight(info, dragTextView);
				float verticalPadding = Math.max(0,
						((maxHeight - textHeight) / 2));
				float horizontalPadding = Math.min(Math.max(
						0,
						(maxWidth - measureTextWidth(info, dragTextView,
								maxWidth)) / 2),
						verticalPadding * 1.9090909090909090909090909090909f);
				//LogUtils.v("verticalPadding:" + verticalPadding + " horizontalPadding:" + horizontalPadding);
				dragTextView.setPadding((int) horizontalPadding,
						(int) verticalPadding, (int) horizontalPadding,
						(int) verticalPadding);
				dragTextView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dragTextView.tinkleOpen();
						final String orginText = TextUtils.isEmpty(dragTextView
								.getText()) ? info.getNotetext() : dragTextView
								.getText().toString();
						final TextWatcher watcher = new TextWatcher() {
							@Override
							public void onTextChanged(CharSequence s,
									int start, int before, int count) {
							}
							@Override
							public void beforeTextChanged(CharSequence s,
									int start, int count, int after) {
							}
							@Override
							public void afterTextChanged(Editable s) {
								String str = s.toString();
								dragTextView.setText(str);
								/*ExpressionUtil.showEmoteInListview(dragTextView.getContext(), dragTextView,
										ExpressionSizeType.middle, str);*/
							}
						};
						final TalkFragement talkFragement = TalkFragement.newFragment(FragmentType.dialog) ;
						talkFragement.setOnCancelListener(new OnCancelListener() {
							@Override
							public void onCancel(DialogInterface dialog) {
								dragTextView.setText(orginText);
								/*ExpressionUtil.showEmoteInListview(dragTextView.getContext(), dragTextView,
										ExpressionSizeType.middle, orginText);*/
								dragTextView.tinkleClose();
								talkFragement.removeTextChangedListener(watcher);
							}
						});
						int maxInputLen = getMaxInputlength(maxWidth, info,
								dragTextView);
						talkFragement.setContent(orginText);
						talkFragement.setNewsTopsetting(maxInputLen);
						talkFragement.show(((FragmentActivity) context)
								.getSupportFragmentManager(), "");
						talkFragement
								.setSendmsgListenter(new SendmsgListenter() {
									@Override
									public void sendmsg(TalkFragement fragment,
											String str) {
										if (TextUtils.isEmpty(str)) {
											str = info.getNotetext();
										}
										/*ExpressionUtil.showEmoteInListview(dragTextView.getContext(), dragTextView,
												ExpressionSizeType.middle, str);*/
										dragTextView.setText(str);
										dragTextView.tinkleClose();
										talkFragement
												.removeTextChangedListener(watcher);
									}
								});
						talkFragement.addTextChangedListener(watcher);
					}
				});
				container.addView(dragTextView, lp);
				infoViews.put(info, dragTextView);
			}
		}
	}
	//移除所有Title
	public void removeAllTitles() {
		if (infoViews.size() > 0) {
			for (View view : infoViews.values()) {
				container.removeView(view);
			}
			infoViews.clear();
		}
	}
	//将标题绘制到Canvas以便保存
	public void drawTitles(Canvas canvas, int orginalWidth, int orginalHeight,
			int marginHorizontal, int marginVertical) {
		Map<String, Typeface> faces = new HashMap<String, Typeface>();
		int srcImageViewWidth = container.getWidth() - marginHorizontal * 2;
		int srcImageViewHeight = container.getHeight() - marginVertical * 2;
		if (infoViews.size() > 0) {
			for (NewsTextInfo info : infoViews.keySet()) {
				DragTextViewSub sub = (DragTextViewSub) infoViews.get(info);
				float x = 1f
						* (sub.getLeft() + sub.getPaddingLeft() - marginHorizontal)
						/ srcImageViewWidth * orginalWidth;
				float y = 1f
						* (sub.getTop() + sub.getPaddingTop() - marginVertical)
						/ srcImageViewHeight * orginalHeight;
				float textWidth = 1f
						* (sub.getWidth() - sub.getPaddingLeft() - sub
								.getPaddingRight()) / srcImageViewWidth
						* orginalWidth;
				//设置paint
				Paint paint = new Paint();
				paint.setTextSize(orginalHeight * info.getTextsize() / 100);
				paint.setColor(info.getTextcolor());
				paint.setFakeBoldText(info.isBold());
				String fontPath = info.getFontPath();
				if (!TextUtils.isEmpty(fontPath)) {
					if (fontPath.startsWith("assets://")) {
						//本地字库
						if (!faces.containsKey(fontPath)) {
							Typeface tface = Typeface.createFromAsset(container
									.getContext().getAssets(), Scheme.ASSETS
									.crop(fontPath));
							faces.put(fontPath, tface);
						}
					} else if (fontPath.startsWith("http://")) {
						if (!faces.containsKey(fontPath)) {
							Typeface tface = null;
							//todo 获取http对应的字体
							faces.put(fontPath, tface);
						}
					}
					paint.setTypeface(faces.get(fontPath));
				}
				int linenum = info.getLinenum();
				String content = sub.getText().toString();
				String[] splitContent = new String[linenum];
				float[][] splitPosition = new float[linenum][2];
				int splitlen = 0;
				int line = 0;
				float lastlineHeight = 0;
				float textlen = 0;
				for (int i = 1; i < content.length();) {
					textlen = paint.measureText(content.substring(splitlen, i));
					if (textlen > textWidth) {
						//换行时不要自增i
						//LogUtils.v("drawTitles_i-->" + i + " splitlen:" + splitlen + " line:" + line + " text:"
						//								+ info.getNotetext() + " linenum:" + linenum + " textlen:" + textlen + " textWidth:"
						//								+ textWidth);
						splitContent[line] = content.substring(splitlen, i - 1);
						splitPosition[line][0] = x;
						splitPosition[line][1] = y + lastlineHeight;
						FontMetrics fm = paint.getFontMetrics();
						float height = fm.descent - fm.ascent;
						if (line > 0) {
							height += fm.leading;
							//LogUtils.v("fm.leading:" + fm.leading + " info.getNotetext:" + info.getNotetext());
						}
						line++;
						splitlen = i - 1;
						lastlineHeight += height;
					} else {
						i++;
					}
				}
				if (line == linenum - 1) {
					splitContent[line] = content.substring(splitlen,
							content.length());
					splitPosition[line][0] = x;
					splitPosition[line][1] = y + lastlineHeight;
				}
				FontMetrics fm = paint.getFontMetrics();
				float off = -fm.top;
				for (int i = 0; i < splitContent.length; i++) {
					if (!TextUtils.isEmpty(splitContent[i]))
						canvas.drawText(splitContent[i], splitPosition[i][0],
								splitPosition[i][1] + off, paint);
					//LogUtils.v("splitContent[" + i + "]-->" + splitContent[i] + " splitPosition[" + i + "][0]:"
					//							+ splitPosition[i][0] + " splitPosition[" + i + "][1]:" + splitPosition[i][1]);
				}
			}
		}
	}
	//绘制单个文字方法
	public void drawOneText(Canvas canvas, NewsTextInfo tf) {
		Paint paint = new Paint();
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setTextSize(tf.getTextsize());
		paint.setColor(tf.getTextcolor());
		if (tf.Isshadow()) {
			paint.setShadowLayer(5, 0, 0, Color.parseColor("#000000"));
		}
		if (canvas != null && tf != null && paint != null) {
			float x = tf.getTextposition()[0];
			float y = tf.getTextposition()[1];
			//y轴校正
			FontMetrics fontMetrics = paint.getFontMetrics();
			float fontTotalHeight = fontMetrics.bottom - fontMetrics.top;
			float offY = fontTotalHeight / 2 + fontMetrics.bottom;
			if (tf.Iscenter()) {
				//x轴校正
				x = x + paint.measureText(tf.getDefaultNoteText()) / 2;
			}
			canvas.drawText(tf.getNotetext(), x, y + offY, paint);
		} else {
		}
	}
	//测量绘制的drageTextView的高度
	private float measureTextHeight(NewsTextInfo info,
			DragTextViewSub dragTextView) {
		Paint paint = dragTextView.getPaint();
		FontMetrics fm = paint.getFontMetrics();
		float retval = 0;
		for (int line = 0; line < info.getLinenum(); line++) {
			retval += fm.descent - fm.ascent;
			if (line > 0) {
				retval += fm.leading;
				//LogUtils.v("fm.leading:" + fm.leading + " info.getNotetext:" + info.getNotetext());
			}
		}
		return retval;
	}
	//测量绘制的drageTextView的宽度
	private float measureTextWidth(NewsTextInfo info,
			DragTextViewSub dragTextView, int maxWidth) {
		Paint paint = dragTextView.getPaint();
		float retval = paint.measureText(info.getNotetext().substring(0,
				info.getNotetext().length() / info.getLinenum()));
		String content = dragTextView.getText().toString();
		int splitlen = 0;
		float textlen = 0;
		int maxTextWidth = maxWidth - dragTextView.getPaddingLeft()
				- dragTextView.getPaddingRight();
		int line = 0;
		int linenum = info.getLinenum();
		for (int i = 1; i < content.length();) {
			textlen = paint.measureText(content.substring(splitlen, i));
			// 加入判断， 如果指定的宽度比一个字符的宽度还小，还是让它换行；
			if (textlen > maxTextWidth && (i - splitlen > 1)) {
				//换行了,获取上一行的宽度，与先前比较取最大值
				retval = Math.max(retval,
						paint.measureText(content.substring(splitlen, i - 1)));
				splitlen = i - 1;
			} else {
				i++;
			}
			line++;
		}
		if (line == linenum - 1) {
			retval = Math.max(textlen, retval);
		}
		//LogUtils.v("the textWidth:" + retval + " maxWidth:" + maxWidth + " text:" + info.getNotetext() + " textlen:"
		//				+ textlen);
		return retval;
	}
	public int getMaxInputlength(int maxWidth, NewsTextInfo info,
			DragTextViewSub dragTextView) {
		int lineNum = info.getLinenum();
		float textViewTrueWidth = maxWidth - dragTextView.getPaddingLeft()
				- dragTextView.getPaddingRight();
		int retVal = 0;
		Paint paint = dragTextView.getPaint();
		float oneWidth = 0;
		try {
			oneWidth = paint.measureText(info.getNotetext())
					/ info.getNotetext().getBytes("gbk").length;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			oneWidth = paint.measureText("abcdefghijk0123456789") / 10;
		}
		for (int i = 0; i < lineNum; i++) {
			retVal += textViewTrueWidth / oneWidth;
			//LogUtils.v("dragText_getMaxInputlength: " + (textViewTrueWidth / oneWidth) + " text:"
			//					+ dragTextView.getText() + " oneWidth:" + oneWidth + " textViewTrueWidth:" + textViewTrueWidth);
		}
		return retVal;
	}
	//闪动所有标题
	public void tinkleAllTiltles() {
		//LogUtils.v("tinkleAllTiltles");
		if (infoViews.size() > 0) {
			for (View view : infoViews.values()) {
				DragTextViewSub sub = (DragTextViewSub) view;
				sub.tinkle();
			}
		}
	}
}
