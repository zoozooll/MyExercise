/**
 * 
 */
package com.butterfly.vv.view;

/**
 * A view interface is use to set whether the implemented view class is able to scroll or flip;
 * 一个view接口，用来设定实现本接口的view能否上下滑或左右翻转
 * @author Aaron Lee Created at 下午2:56:56 2015-10-26
 */
public interface BlockHolder {
	/**
	 * Set that whether the implemented view can scroll or flip;
	 * @param enable if it is false, it cannot flip or scroll
	 */
	public void setBlocked(boolean enable);
}
