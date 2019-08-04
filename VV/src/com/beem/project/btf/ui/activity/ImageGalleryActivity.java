/*
 Copyright (c) 2012 Roman Truba

 Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 permit persons to whom the Software is furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all copies or substantial
 portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH
 THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.beem.project.btf.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Window;
import android.widget.TextView;

import java.util.ArrayList;
import com.beem.project.btf.R;
import com.beem.project.btf.service.Contact;
import com.beem.project.btf.ui.activity.base.VVBaseFragmentActivity;
import com.beem.project.btf.ui.entity.EventBusData;
import com.beem.project.btf.ui.fragment.ImageDetailFragment;
import com.beem.project.btf.ui.views.BottomPopupWindow;
import com.butterfly.vv.db.ormhelper.bean.VVImage;
import com.butterfly.vv.model.ImageFolderItem;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import de.greenrobot.event.EventBus;

/**
 * @date 2013-11-16-下午1:39:26
 * @author yuedong bao
 * @func 图片浏览activity 启动此activity需要传递图片位置和url数组集合(已修改) 目前启动此界面需携带图片组数据结构ImageFolderItem
 */
public class ImageGalleryActivity extends VVBaseFragmentActivity {
	private ViewPager mViewPager;
	private TextView pager_current;
	private BottomPopupWindow filterPopupWindow;
	private ImageFolderItem imageFolderItem;
	private Contact mContact;
	private DisplayImageOptions options = null;
	private String sex = "2";
	private boolean isContactInfoEdit;

	// 跳转到此Activity的launch方法
	public static void launch(Context context, int index,
			ImageFolderItem folderItem, String sex, boolean isContactInfoEdit) {
		Intent intent = new Intent(context, ImageGalleryActivity.class);
		intent.putExtra("index", index);
		intent.putExtra("shareTrancItem", folderItem);
		intent.putExtra("sex", sex);
		intent.putExtra("isContactInfoEdit", isContactInfoEdit);
		context.startActivity(intent);
	}
	// 跳转到此Activity的launch方法
	public static void launch(Context context, int index,
			ImageFolderItem folderItem, String sex) {
		Intent intent = new Intent(context, ImageGalleryActivity.class);
		intent.putExtra("index", index);
		intent.putExtra("shareTrancItem", folderItem);
		intent.putExtra("sex", sex);
		context.startActivity(intent);
	}
	public static void launch(Context context, int index,
			ImageFolderItem folderItem) {
		launch(context, index, folderItem, "2");
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mygalleryurl);
		// 获取控件
		pager_current = (TextView) findViewById(R.id.pager_current);
		mViewPager = (ViewPager) findViewById(R.id.viewer);
		// 获取传过来url等值
		int pagerPosition = getIntent().getIntExtra("index", 0);
		imageFolderItem = (ImageFolderItem) getIntent().getParcelableExtra(
				"shareTrancItem");
		sex = getIntent().getStringExtra("sex");
		isContactInfoEdit = getIntent().getBooleanExtra("isContactInfoEdit",
				false);
		mContact = imageFolderItem.getContact();
		//TODO
		ImagePagerAdapter mAdapter = new ImagePagerAdapter(
				getSupportFragmentManager(), imageFolderItem);
		mViewPager.setAdapter(mAdapter);
		mViewPager.setCurrentItem(pagerPosition);
		mViewPager.setOnPageChangeListener(pageChangeLis);
		// 初始化页数
		setPagerCount(pagerPosition, imageFolderItem.getVVImages().size());
		EventBus.getDefault().register(this);
	}
	// 页数设置方法
	private void setPagerCount(int pageIndex, int pageTotal) {
		pager_current.setText(String.valueOf(pageIndex + 1) + "/" + pageTotal);
	}

	// 页面转换监听器
	private OnPageChangeListener pageChangeLis = new OnPageChangeListener() {
		@Override
		public void onPageSelected(int pos) {
			//LogUtils.i("~~onPageSelected~~" + pos);
			setPagerCount(pos, imageFolderItem.getVVImages().size());
		}
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	};

	private class ImagePagerAdapter extends FragmentStatePagerAdapter {
		private ArrayList<VVImage> fileList;
		private ImageFolderItem MyimageFolderItem;

		public ImagePagerAdapter(FragmentManager fm,
				ImageFolderItem imageFolderItem) {
			super(fm);
			this.MyimageFolderItem = imageFolderItem;
			this.fileList = imageFolderItem.getVVImages();
		}
		@Override
		public int getCount() {
			return fileList == null ? 0 : fileList.size();
		}
		@Override
		public Fragment getItem(int position) {
			if (isContactInfoEdit) {
				return ImageDetailFragment.newInstance(MyimageFolderItem,
						position, sex, isContactInfoEdit);
			} else {
				if (sex.equals("2")) {
					return ImageDetailFragment.newInstance(MyimageFolderItem,
							position);
				} else {
					return ImageDetailFragment.newInstance(MyimageFolderItem,
							position, sex);
				}
			}
		}
	}

	// 相应数据更新
	public void onEventMainThread(EventBusData data) {
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
	@Override
	public void registerVVBroadCastReceivers() {
	}
}
