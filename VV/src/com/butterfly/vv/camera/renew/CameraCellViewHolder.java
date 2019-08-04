package com.butterfly.vv.camera.renew;

import com.butterfly.vv.camera.base.ImageInfoHolder;

import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CameraCellViewHolder {
	public ImageInfoHolder imageHolder;
	public ImageView listImageView; // 图片
	public RelativeLayout itemInfoLayout;
	public ImageView watchTag; // 钟表标记
	public TextView timeTxt; // 时间文字
	public ImageView placeTag; // 地理标记
	public TextView placeTxt; // 位置文字
	public CheckBox choiceTag; // 选择标记
}
