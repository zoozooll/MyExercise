package com.beem.project.btf.ui.views;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;

import com.beem.project.btf.R;
import com.beem.project.btf.utils.BBSUtils;
import com.beem.project.btf.utils.DimenUtils;

/**
 * 底部弹出框
 */
public class BottomPopupWindow extends PopupWindow {
	private ViewGroup pop_layout;
	private View mMenuView;
	private Context mContext;
	private ArrayList<ButtonItemInfo> viewlist = new ArrayList<ButtonItemInfo>();

	/**
	 * 弹框操作类型
	 * @category DEL删除头像
	 * @category COVER_DEL设为封面和删除
	 * @category TAKEPHOTO通过相机和图库获取图片
	 * @category SAVE_DEL图片删除和保存本地操作
	 * @category SAVE图片保存本地操作
	 */
	public interface PopupActionListener {
		void itemsClick(PopupActionType type, int i);
	}

	public enum PopupActionType {
		DEL, COVER_DEL, TAKEPHOTO, SAVE_DEL, SAVE, BLACKLIST_DEL
	}

	/**
	 * 记录按钮的信息
	 */
	private class ButtonItemInfo {
		private int backgroundresid;
		private int drawableResId;
		private String contenttext;

		public ButtonItemInfo(int resid, String text) {
			this(resid, text, 0);
		}
		public ButtonItemInfo(int resid, String text, int drawableResId) {
			this.backgroundresid = resid;
			this.contenttext = text;
			this.drawableResId = drawableResId;
		}
		public int getResid() {
			return backgroundresid;
		}
		public String getText() {
			return contenttext;
		}
		public int getDrawableResId() {
			return drawableResId;
		}
	}

	public BottomPopupWindow(Activity context,
			PopupActionListener itemsOnClick, PopupActionType type) {
		this(context, itemsOnClick, type, true);
	}
	public BottomPopupWindow(Activity context,
			PopupActionListener itemsOnClick, PopupActionType type,
			boolean isOutsideTouchable) {
		super(context);
		mContext = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.params_for_filter, null);
		switchLayout(itemsOnClick, type);
		setParams(isOutsideTouchable);
	}
	private void switchLayout(final PopupActionListener itemsOnClick,
			final PopupActionType type) {
		if (viewlist.size() > 0) {
			viewlist.clear();
		}
		pop_layout = (ViewGroup) mMenuView.findViewById(R.id.pop_layout);
		switch (type) {
			case DEL: {
				viewlist.add(new ButtonItemInfo(R.drawable.red_btn_selector,
						"删除"));
				bindview(viewlist, itemsOnClick, type);
				break;
			}
			case COVER_DEL: {
				viewlist.add(new ButtonItemInfo(R.drawable.blue_btn_selector,
						"设为封面"));
				viewlist.add(new ButtonItemInfo(R.drawable.red_btn_selector,
						"删除"));
				bindview(viewlist, itemsOnClick, type);
				break;
			}
			case TAKEPHOTO: {
				viewlist.add(new ButtonItemInfo(R.drawable.blue_btn_selector,
						"拍照"));
				viewlist.add(new ButtonItemInfo(R.drawable.blue_btn_selector,
						"相册选择"));
				bindview(viewlist, itemsOnClick, type);
				break;
			}
			case SAVE_DEL: {
				viewlist.add(new ButtonItemInfo(R.drawable.blue_btn_selector,
						"保存本地"));
				viewlist.add(new ButtonItemInfo(R.drawable.red_btn_selector,
						"删除"));
				bindview(viewlist, itemsOnClick, type);
				break;
			}
			case SAVE: {
				viewlist.add(new ButtonItemInfo(R.drawable.blue_btn_selector,
						"保存本地"));
				bindview(viewlist, itemsOnClick, type);
				break;
			}
			case BLACKLIST_DEL: {
				viewlist.add(new ButtonItemInfo(R.drawable.red_btn_selector,
						"解除黑名单", R.drawable.icon_remove_normal));
				bindview(viewlist, itemsOnClick, type);
				break;
			}
		}
	}
	private void bindview(ArrayList<ButtonItemInfo> viewlist,
			final PopupActionListener itemsOnClick, final PopupActionType type) {
		for (int i = 0; i < viewlist.size(); i++) {
			Button btn = new Button(mContext) {
				@Override
				//重写onDraw实现Button中的文字和图标居中
				protected void onDraw(Canvas canvas) {
					////LogUtils.i("onDraw_ButtonmPopupWindow");
					super.onDraw(canvas);
				}
			};
			btn.setTextAppearance(mContext, R.style.PopupWindow_btn_style);
			LayoutParams layoutParams = new LayoutParams(
					android.view.ViewGroup.LayoutParams.FILL_PARENT,
					DimenUtils.dip2px(mContext, 45));
			layoutParams.setMargins(DimenUtils.dip2px(mContext, 20),
					DimenUtils.dip2px(mContext, 10),
					DimenUtils.dip2px(mContext, 20), 0);
			btn.setLayoutParams(layoutParams);
			btn.setGravity(Gravity.CENTER);
			btn.setBackgroundResource(viewlist.get(i).getResid());
			btn.setText(viewlist.get(i).getText());
			btn.setTag(i);
			if (viewlist.get(i).getDrawableResId() > 0) {
				btn.setCompoundDrawablePadding(DimenUtils.dip2px(mContext, 5));
				btn.setCompoundDrawablesWithIntrinsicBounds(viewlist.get(i)
						.getDrawableResId(), 0, 0, 0);
				Drawable[] drawables = btn.getCompoundDrawables();
				if (drawables != null) {
					Drawable drawableLeft = drawables[0];
					Drawable drawableRight = drawables[1];
					float textWidth = btn.getPaint().measureText(
							btn.getText().toString());
					int drawablePadding = 0;
					int drawableWidth = 0;
					int totalWidth = BBSUtils.getDeviceWH(mContext)[0]
							- layoutParams.leftMargin
							- layoutParams.rightMargin;
					if (drawableLeft != null) {
						drawablePadding = btn.getCompoundDrawablePadding();
						drawableWidth = drawableLeft.getIntrinsicWidth();
					}
					if (drawableRight != null) {
						drawablePadding += btn.getCompoundDrawablePadding();
						drawableWidth += drawableRight.getIntrinsicWidth();
					}
					float bodyWidth = textWidth + drawableWidth
							+ drawablePadding;
					btn.setPadding((int) (totalWidth - bodyWidth) / 2, 0,
							(int) (totalWidth - bodyWidth) / 2, 0);
				}
			}
			if (itemsOnClick != null) {
				btn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						itemsOnClick.itemsClick(type, (Integer) v.getTag());
					}
				});
			}
			pop_layout.addView(btn);
		}
	}
	private void setParams(boolean isOutsideTouchable) {
		// 设置SelectPicPopupWindow的View
		setContentView(mMenuView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		setWidth(android.view.ViewGroup.LayoutParams.FILL_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		setHeight(android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		setAnimationStyle(R.style.Popup_Animation_UpDown);
		// 设置SelectPicPopupWindow弹出窗体的背景
		setBackgroundDrawable(new BitmapDrawable());
		if (isOutsideTouchable) {
			setFocusable(true);
			setOutsideTouchable(true);
		} else {
			setFocusable(false);
			setOutsideTouchable(false);
		}
	}
}
