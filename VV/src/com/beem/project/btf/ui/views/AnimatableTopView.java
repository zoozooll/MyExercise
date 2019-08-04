/**
 * 
 */
package com.beem.project.btf.ui.views;

import com.beem.project.btf.R;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author Aaron Lee Created at 下午5:34:42 2016-1-20
 */
public class AnimatableTopView {
	/* ******初始化状态的值，即动画未开始，或者回到初始化状态后的值,单位px****** */
	private int maxHeight;
	private int topbtnMarginTop;
	private int topbtnMarginHorizontal;
	private int topbtnWidth;
	private int firstParentBgColor;
	private int firstTopbtnBgColor;
	private int firstPlayenterWidth;
	private int firstPlayenterMarginTop;
	private int firstTitleX;
	private int firstTitleY;
	private int firstPlayenterX;
	private int firstPlayenterY;
	/* ******动画完成后的值，单位px**********/
	private int minHeight;
	private int finalParentBgColor;
	private int finalTopbtnBgColor;
	private int finalPlayenterWidth;
	private int finalPlayenterMarginTop;
	private int finalTitleY;
	private int finalTitleX;
	private int finalPlayenterX;
	private int finalPlayenterY;
	/* *****动态变化的状态值，即所有动画的效果变化都是跟着某个变量的变化而变化，
	 * 在这里所有的变化都跟着高度的变化而变化*/
	private int curParentHeight;
	private int curParentBgColor;
	private int curTopbtnBgColor;
	private int curPlayenterWidth;
	private int curPlayenterMarginTop;
	private int curTitleY;
	private int curTitleX;
	private int curPlayenterX;
	private int curPlayenterY;
	private View parent;
	private ImageView back;
	private ImageView imv_shareEdit;
	private TextView tvw_Title;
	private ImageView imv_playenter;

	public void initialize(View root) {
		parent = root;
		back = (ImageView) root.findViewById(R.id.back);
		imv_shareEdit = (ImageView) root.findViewById(R.id.imv_shareEdit);
		tvw_Title = (TextView) root.findViewById(R.id.tvw_Title);
		imv_playenter = (ImageView) root.findViewById(R.id.imv_playenter);
		maxHeight = parent.getWidth();
		topbtnMarginTop = back.getTop();
		topbtnMarginHorizontal = back.getLeft();
		topbtnWidth = back.getWidth();
		firstParentBgColor = root.getResources().getColor(R.color.transparent);
		firstPlayenterWidth = imv_playenter.getWidth();
		firstPlayenterMarginTop = imv_playenter.getTop();
		firstTitleX = tvw_Title.getLeft();
		firstTitleY = tvw_Title.getTop();
		firstPlayenterX = imv_playenter.getLeft();
		firstPlayenterY = imv_playenter.getTop();
	}
}
