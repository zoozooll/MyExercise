package com.beem.project.btf.camera.widgetscamera.fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.beem.project.btf.R;
import com.beem.project.btf.ui.activity.TimeCameraLoadimgActivity;
import com.beem.project.btf.ui.fragment.BaseFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @ClassName: WatermarkChooseFragment
 * @Description: 水印素材选择Fragment
 * @author: yuedong bao
 * @date: 2015-11-10 上午10:51:55
 */
public class WatermarkChooseFragment extends BaseFragment {
	private View view;
	private ArrayList<WaterMarkPlate> plates;

	@SuppressWarnings("unchecked")
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (view != null) {
			((ViewGroup) view.getParent()).removeView(view);
			return view;
		}
		view = inflater.inflate(R.layout.watermark_choose, container, false);
		plates = (ArrayList<WaterMarkPlate>) getArguments().getSerializable("WaterMarkPlate");
		ViewPager viewpager_parent = (ViewPager) view.findViewById(R.id.viewpager_parent);
		ArrayList<Fragment> fragments = new ArrayList<Fragment>();
		for (WaterMarkPlate plate : plates) {
			Fragment fragment = WatermarkChooseItemFragment.newInstance(plate.metas);
			fragments.add(fragment);
		}
		FragmentPagerAdapter adapter = new TimeCameraLoadimgActivity.MyFragmentPagerAdapter(getFragmentManager(),
				fragments);
		viewpager_parent.setAdapter(adapter);
		return view;
	}
	@Override
	protected void lazyLoad() {
		//初始化数据
	}
	public static WatermarkChooseFragment newInstance(ArrayList<WaterMarkPlate> plates) {
		WatermarkChooseFragment fragment = new WatermarkChooseFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable("WaterMarkPlate", plates);
		fragment.setArguments(bundle);
		return fragment;
	}

	//水印元素
	public static class WatermarkMeta implements Serializable {
		private static final long serialVersionUID = -2979809187578904296L;
		//远程路径
		public final String url;
		//板块名
		public final String plate_name;

		public WatermarkMeta(String plate_name,String url) {
			super();
			this.plate_name = plate_name;
			this.url = url;
		}
	}

	//水印板块
	public static class WaterMarkPlate implements Serializable {
		private static final long serialVersionUID = -7636215674481782819L;
		public final String plate_name;
		public final ArrayList<WatermarkMeta> metas;

		public WaterMarkPlate(String plate_name, ArrayList<WatermarkMeta> metas) {
			super();
			this.plate_name = plate_name;
			this.metas = metas;
		}
	}
}
