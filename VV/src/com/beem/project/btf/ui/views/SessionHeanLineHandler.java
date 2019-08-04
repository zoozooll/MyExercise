package com.beem.project.btf.ui.views;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.beem.project.btf.R;
import com.butterfly.vv.adapter.SessionsImAdapter;
import com.butterfly.vv.adapter.SessionsImAdapter.SelectListener;

/**
 * @author yuedong bao
 * @func 消息头部删除处理类
 */
public class SessionHeanLineHandler {
	private boolean selall = false;
	private OnClickListener clickListener = null;

	public SessionHeanLineHandler(final Context context, View parent,
			final SessionsImAdapter sessionsImAdapter) {
		super();
		final CustomTitleBtn session_del_llt = (CustomTitleBtn) parent
				.findViewById(R.id.leftbtn1);
		session_del_llt.setText(R.string.session_del);
		session_del_llt.setImgResource(R.drawable.session_headlines_delete);
		session_del_llt.setViewPaddingLeft();
		session_del_llt.setVisibility(View.GONE);
		final TextView headTitle = (TextView) parent
				.findViewById(R.id.topbar_title);
		headTitle.setText(R.string.session_session);
		final CustomTitleBtn setting = (CustomTitleBtn) parent
				.findViewById(R.id.rightbtn1);
		setting.setText("");
		setting.setViewPaddingRight().setVisibility(View.VISIBLE);
		setting.setImgVisibility(View.GONE);
		session_del_llt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sessionsImAdapter.delSessionByPos();
			}
		});
		// 实现选中项目接口
		sessionsImAdapter.setSelectLis(new SelectListener() {
			@Override
			public void select(boolean isSelectMod, int selecSum, int NumAll) {
				if (isSelectMod) {
					// 进入选择模式
					session_del_llt.setVisibility(View.VISIBLE);
					if (selecSum == 0) {
						// 没有选中项目，隐藏删除按钮
						headTitle.setText("选择了" + 0 + "个");
						if (NumAll == 1) {
							// 只有一个条目的情况
							selall = false;
							setting.setText("全选");
						}
					} else {
						headTitle.setText("选择了" + selecSum + "个");
						if (selecSum == NumAll) {
							// 全选了
							selall = true;
							setting.setText("全不选");
						} else {
							// 没全选
							selall = false;
							setting.setText("全选");
						}
					}
					if (clickListener == null) {
						clickListener = new OnClickListener() {
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								if (!selall) {
									sessionsImAdapter.delSelcetAll();
									setting.setText("全不选");
									selall = true;
								} else {
									sessionsImAdapter.delCancel();
									setting.setText("全选");
									selall = false;
								}
							}
						};
						setting.setOnClickListener(clickListener);
					}
				} else {
					// 退出选择模式
					setting.setText("");
					headTitle.setText(R.string.session_session);
					session_del_llt.setVisibility(View.GONE);
				}
			}
		});
	}
}
