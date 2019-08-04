package com.beem.project.btf.ui.dialog;

import java.util.List;

import org.lucasr.twowayview.TwoWayView;
import com.beem.project.btf.R;
import com.beem.project.btf.ui.adapter.NewsPopAdapter;
import com.beem.project.btf.ui.adapter.NewsPopAdapter.CurrentPotionListener;
import com.beem.project.btf.ui.entity.NewsImageInfo;
import com.beem.project.btf.utils.DimenUtils;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

public class NewsTopTitlePopupWindow extends PopupWindow {
	private Context mContext;
	private View mView;
	private TwoWayView tw;
	private NewsPopAdapter galleryadapter;
	private boolean isOutsideTouchable = false;
	private List<NewsImageInfo> datas = null;
	private CurrentPotionListener listener = null;

	public NewsTopTitlePopupWindow(Context context) {
		super(context);
		this.mContext = context;
		initPopupWindow();
	}
	public NewsTopTitlePopupWindow(Context context, List<NewsImageInfo> datas) {
		super(context);
		this.mContext = context;
		this.datas = datas;
		initPopupWindow();
	}
	private void initPopupWindow() {
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mView = inflater.inflate(R.layout.newspoplayout, null);
		mView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		setContentView(mView);
		tw = (TwoWayView) mView.findViewById(R.id.twoWayView2);
		tw.setItemMargin(DimenUtils.dip2px(mContext, 0));
		galleryadapter = new NewsPopAdapter(mContext, datas);
		galleryadapter.setCurrentpotionlistener(listener);
		tw.setAdapter(galleryadapter);
		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(DimenUtils.dip2px(mContext, 110));
		setBackgroundDrawable(new ColorDrawable(android.R.color.transparent));
	}
	public void setOutsideable(boolean flag) {
		isOutsideTouchable = flag;
		setOutsideTouchable(isOutsideTouchable);
	}
	public void setAlphaAnim(boolean isanim) {
		if (galleryadapter != null) {
			galleryadapter.setAlphaAnim(isanim);
		}
	}
	public List<NewsImageInfo> getDatas() {
		return datas;
	}
	public void setDatas(String groupId, List<NewsImageInfo> datas,
			int selectPostion) {
		this.datas = datas;
		galleryadapter.setItems(groupId, datas, selectPostion);
		galleryadapter.notifyDataSetChanged();
	}
	public CurrentPotionListener getL() {
		return listener;
	}
	public void setCurrentPotionListener(CurrentPotionListener l) {
		this.listener = l;
		galleryadapter.setCurrentpotionlistener(l);
	}
	public void setCurrentPotion(int position) {
		galleryadapter.setCurrentPotion(position, false);
	}
	public void smoothScrollToPosition(int position) {
		tw.smoothScrollToPosition(position);
	}
	public void setSelection(int position) {
		tw.setSelection(position);
	}
	public String getGroupid() {
		return galleryadapter.getGroupid();
	};
}
