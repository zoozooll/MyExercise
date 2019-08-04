package com.pullToRefresh.ui;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beem.project.btf.R;

/**
 * @ClassName: SimpleEmptydataProcessView
 * @Description: 空数据流程视图简单实现
 * @author: yuedong bao
 * @date: 2015-8-19 下午5:36:37
 */
public class SimpleEmptydataProcessView extends BaseProcessView {
	private ImageView loadEmptyImage;
	private TextView loadEmptyText;
	private Button loadEmptyBtn;

	public SimpleEmptydataProcessView(Context context) {
		super(context);
		loadEmptyImage = (ImageView) findViewById(R.id.empty_image);
		loadEmptyImage.setVisibility(View.GONE);
		loadEmptyText = (TextView) findViewById(R.id.empty_note_text);
		loadEmptyText.setVisibility(View.GONE);
		loadEmptyBtn = (Button) findViewById(R.id.empty_btn);
		loadEmptyBtn.setVisibility(View.GONE);
	}
	@Override
	public View createProcessView(Context context) {
		View container = LayoutInflater.from(context).inflate(
				R.layout.load_empty_layout, null);
		return container;
	}
	// 设置空数据时的图片
	public void setEmptydataImg(int resId, boolean... isShows) {
		boolean isShow = isShows.length > 0 ? isShows[0] : true;
		loadEmptyImage.setImageResource(resId);
		loadEmptyImage.setVisibility(isShow ? View.VISIBLE : View.INVISIBLE);
	}
	// 设置空数据时的文本提示
	public void setloadEmptyText(String text, boolean... isShows) {
		boolean isShow = isShows.length > 0 ? isShows[0] : true;
		loadEmptyText.setVisibility(isShow ? View.VISIBLE : View.INVISIBLE);
		loadEmptyText.setText(text);
	}
	// 设置按钮接口
	public void setloadEmptyBtn(String text,
			final OnClickListener tapClickListener, boolean... isShows) {
		boolean isShow = isShows.length > 0 ? isShows[0] : true;
		loadEmptyBtn.setVisibility(isShow ? View.VISIBLE : View.INVISIBLE);
		loadEmptyBtn.setText(text);
		loadEmptyBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (tapClickListener != null) {
					tapClickListener.onClick(v);
				}
			}
		});
		if (TextUtils.isEmpty(text)) {
			loadEmptyBtn.setLayoutParams(new LinearLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
			loadEmptyBtn.setBackgroundResource(R.drawable.pink_btn_selector);
		}
	}

	// 空数据时按钮动作接口
	public interface onTapClickListener {
		public void click();
	}
}
