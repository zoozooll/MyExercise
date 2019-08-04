package com.beem.project.btf.ui.activity.base;

import com.beem.project.btf.ui.receiver.base.VVBaseBroadCastReceiver;

import android.content.IntentFilter;

/**
 * @ClassName: IActivityAction
 * @Description: Activity之间共有行为
 * @author: yuedong bao
 * @date: 2015-3-23 下午3:39:13
 */
public interface IVVActivityAction {
	/**
	 * @Title: registerVVBroadCastReceivers
	 * @Description: 注册所有广播接收器
	 * @param:
	 * @return: void
	 * @throws:
	 */
	public void registerVVBroadCastReceivers();
	/**
	 * @Title: unRegisterVVBroadCastReceivers
	 * @Description: 反注册所有广播接收器
	 * @param:
	 * @return: void
	 * @throws:
	 */
	public void unRegisterVVBroadCastReceivers();
	/**
	 * @Title: registerVVBroadCastReceiver
	 * @Description: 注册广播接收器
	 * @param: @param castReceiver
	 * @param: @param filter
	 * @return: void
	 * @throws:
	 */
	public void registerVVBroadCastReceiver(
			VVBaseBroadCastReceiver castReceiver, IntentFilter filter);
	/**
	 * @Title: unRegisterVVBroadCastReceiver
	 * @Description: 反注册广播接收器
	 * @param: @param castReceiver
	 * @return: void
	 * @throws:
	 */
	public void unRegisterVVBroadCastReceiver(
			VVBaseBroadCastReceiver castReceiver);
	/**
	 * @Title: setupNavigateView
	 * @Description: 配置导航条
	 * @param:
	 * @return: void
	 * @throws:
	 */
	public void setupNavigateView();
}
