package com.beem.project.btf.bbs.view;

import android.support.v4.view.ViewPager.OnPageChangeListener;

/**
 * @author yuedong bao
 */
public abstract class CustomerPageChangeLis implements OnPageChangeListener {
	// @Override
	// public void onPageSelected(int arg0) {
	// // 此方法是页面跳转完后得到调用，arg0是你当前选中的页面的Position（位置编号）。
	// }
	@Override
	public void onPageScrollStateChanged(int arg0) {
		// 有三种状态（0，1，2）。arg0 ==1的时辰默示正在滑动，arg0==2的时辰默示滑动完毕了，arg0==0的时辰默示什么都没做
	}
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// arg0 :当前页面，及你点击滑动的页面
		// arg1:当前页面偏移的百分比
		// arg2:当前页面偏移的像素位置
	}
}
