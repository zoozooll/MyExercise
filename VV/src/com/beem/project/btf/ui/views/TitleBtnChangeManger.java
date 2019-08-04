package com.beem.project.btf.ui.views;

import java.util.ArrayList;

import android.view.View;
import android.view.View.OnClickListener;

public class TitleBtnChangeManger {
	private OnTitleBtnChangeListener headLineChangeLis;
	private ArrayList<CustomTitleBtn> arrays;
	private int curPage;

	public interface OnTitleBtnChangeListener {
		boolean onViewChange(int pageIndex);
	}

	public void setOnViewChangeListener(
			OnTitleBtnChangeListener headLineChangeLis) {
		this.headLineChangeLis = headLineChangeLis;
	}
	public TitleBtnChangeManger(ArrayList<CustomTitleBtn> array) {
		this.arrays = array;
		init();
	}
	public void performClick(int i) {
		arrays.get(i).performClick();
	}
	private void init() {
		curPage = 0;
		for (int i = 0; i < arrays.size(); i++) {
			arrays.get(i).setTag(i);
			arrays.get(i).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					int clickPage = (Integer) v.getTag();
					if (clickPage != curPage) {
						if(headLineChangeLis.onViewChange(clickPage)) {
							setSelected(clickPage);
						}
					}
				}
			});
		}
		setSelected(curPage);
	}
	public void setSelected(int index) {
		for (int i = 0; i < arrays.size(); i++) {
			arrays.get(i).setSelected(false);
		}
		arrays.get(index).setSelected(true);
		curPage = index;
	}
}
