package com.pullToRefresh.ui;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beem.project.btf.R;

/**
 * 这个类封装了下拉刷新的布局
 * @author Li Hong
 * @since 2013-7-30
 */
public class HeaderLoadingLayout extends LoadingLayout {
	/** 旋转动画时间 */
	private static final int ROTATE_ANIM_DURATION = 200;
	/** Header的容器 */
	private RelativeLayout mHeaderContainer;
	/** 箭头图片 */
	private ImageView mArrowImageView;
	/** 进度条 */
	private ProgressBar mProgressBar;
	/** 状态提示TextView */
	private TextView mHintTextView;
	/** 最后更新时间的TextView */
	private TextView mHeaderTimeView;
	/** 最后更新时间的标题 */
	private TextView mHeaderTimeViewTitle;
	/** 向上的动画 */
	private Animation mRotateUpAnim;
	/** 向下的动画 */
	private Animation mRotateDownAnim;
	private final float pivotValue = 0.5f; // SUPPRESS CHECKSTYLE
	private final float toDegree = -180f; // SUPPRESS CHECKSTYLE

	/**
	 * 构造方法
	 * @param context context
	 */
	public HeaderLoadingLayout(Context context) {
		super(context);
		init(context);
	}
	/**
	 * 构造方法
	 * @param context context
	 * @param attrs attrs
	 */
	public HeaderLoadingLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	/**
	 * 初始化
	 * @param context context
	 */
	private void init(Context context) {
		mHeaderContainer = (RelativeLayout) findViewById(R.id.pull_to_refresh_header_content);
		mArrowImageView = (ImageView) findViewById(R.id.pull_to_refresh_header_arrow);
		mHintTextView = (TextView) findViewById(R.id.pull_to_refresh_header_hint_textview);
		mProgressBar = (ProgressBar) findViewById(R.id.pull_to_refresh_header_progressbar);
		mHeaderTimeView = (TextView) findViewById(R.id.pull_to_refresh_header_time);
		mHeaderTimeViewTitle = (TextView) findViewById(R.id.pull_to_refresh_last_update_time_text);
		// 初始化旋转动画
		mRotateUpAnim = new RotateAnimation(0.0f, toDegree,
				Animation.RELATIVE_TO_SELF, pivotValue,
				Animation.RELATIVE_TO_SELF, pivotValue);
		mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
		mRotateUpAnim.setFillAfter(true);
		mRotateDownAnim = new RotateAnimation(toDegree, 0.0f,
				Animation.RELATIVE_TO_SELF, pivotValue,
				Animation.RELATIVE_TO_SELF, pivotValue);
		mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
		mRotateDownAnim.setFillAfter(true);
	}
	@Override
	public void setLastUpdatedLabel(Date label) {
		// 如果最后更新的时间的文本是空的话，隐藏前面的标题
		String time = null;
		if (label == null) {
			date = new Date();
			time = new SimpleDateFormat("HH:mm", Locale.ENGLISH).format(date);
			mHeaderTimeView.setText(time);
		} else {
			time = new SimpleDateFormat("HH:mm", Locale.ENGLISH).format(label);
			mHeaderTimeView.setText(time);
			date = new Date();
		}
		mHeaderTimeViewTitle.setVisibility(View.VISIBLE);
	}
	@Override
	public int getContentSize() {
		if (null != mHeaderContainer) {
			return mHeaderContainer.getHeight();
		}
		return (int) (getResources().getDisplayMetrics().density * 60);
	}
	@Override
	protected View createLoadingView(Context context, AttributeSet attrs) {
		View container = LayoutInflater.from(context).inflate(
				R.layout.pull_to_refresh_header, null);
		return container;
	}
	@Override
	protected void onStateChanged(State curState, State oldState) {
		super.onStateChanged(curState, oldState);
		//LogUtils.i("curState:" + curState + " oldState:" + oldState);
	}
	@Override
	protected void onReset() {
		mArrowImageView.setVisibility(View.GONE);
		mProgressBar.setVisibility(View.GONE);
		mHintTextView.setText(R.string.pull_to_refresh_header_hint_normal);
	}
	@Override
	protected void onPullToRefresh() {
		mArrowImageView.setVisibility(View.VISIBLE);
		mProgressBar.setVisibility(View.GONE);
		if (State.RELEASE_TO_REFRESH == getPreState()) {
			mArrowImageView.clearAnimation();
			mArrowImageView.startAnimation(mRotateDownAnim);
		}
		mHintTextView.setText(R.string.pull_to_refresh_header_hint_normal);
		setLastUpdatedLabel(date);
	}
	@Override
	protected void onReleaseToRefresh() {
		mArrowImageView.setVisibility(View.VISIBLE);
		mProgressBar.setVisibility(View.GONE);
		mArrowImageView.clearAnimation();
		mArrowImageView.startAnimation(mRotateUpAnim);
		mHintTextView.setText(R.string.pull_to_refresh_header_hint_ready);
	}
	@Override
	protected void onRefreshing() {
		mArrowImageView.clearAnimation();
		mArrowImageView.setVisibility(View.GONE);
		mProgressBar.setVisibility(View.VISIBLE);
		mHintTextView.setText(R.string.pull_to_refresh_header_hint_loading);
		setLastUpdatedLabel(date);
	}
}
