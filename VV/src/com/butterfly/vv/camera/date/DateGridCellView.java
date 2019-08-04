package com.butterfly.vv.camera.date;

import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * DateGridCel Element
 **/
public class DateGridCellView {
	public RelativeLayout mClockLineLayout;
	public ImageView clock; // 时钟
	// public ImageView line; // 分界线
	public TextView monthday; // 时间
	public TextView num; // 位置标记
	public TextView yearplace; // 位置文字
	public GridView mImageGrid;
	// @wu 图片网格
	public ListView mImageList;
}
