package com.beem.project.btf.camera.widgetscamera.fragment;

import java.util.ArrayList;
import java.util.List;

import org.opencv.utils.Converters;

import com.beem.project.btf.R;
import com.beem.project.btf.camera.widgetscamera.fragment.WatermarkChooseFragment.WatermarkMeta;
import com.beem.project.btf.ui.activity.TimeCameraLoadimgActivity;
import com.beem.project.btf.utils.LogUtils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.GridView;

/**
 * @ClassName: WatermarkChooseItemFragment
 * @Description: 显示水印素材图片
 * @author: yuedong bao
 * @date: 2015-11-10 下午4:01:39
 */
public class WatermarkChooseItemFragment extends Fragment {
	private View view;
	private int pageCount;
	private ArrayList<WatermarkMeta> metas;

	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (view != null) {
			((ViewGroup) view.getParent()).removeView(view);
			return view;
		}
		Bundle bundle = getArguments();
		metas = (ArrayList<WatermarkMeta>) bundle.getSerializable("metas");
		int pageSize = bundle.getInt("pageSize");
		pageCount = (int) Math.max(1, Math.ceil(1.0f * metas.size() / pageSize));
		//LogUtils.i("pageCount:" + pageCount + " metas.size:" + metas.size());
		if (pageCount > 1) {
			view = inflater.inflate(R.layout.watermark_choose_item2, container, false);
			ViewPager viewpager_child = (ViewPager) view.findViewById(R.id.viewpager_child);
			ArrayList<Fragment> fragments = new ArrayList<Fragment>();
			for (int i = 0; i < pageCount; i++) {
				Fragment fragment = WatermarkChooseItemFragment.newInstance(metas.subList(i * pageSize,
						Math.min(metas.size(), (i + 1) * pageSize)));
				fragments.add(fragment);
			}
			FragmentPagerAdapter adapter = new TimeCameraLoadimgActivity.MyFragmentPagerAdapter(getFragmentManager(),
					fragments);
			viewpager_child.setAdapter(adapter);
		} else {
			view = inflater.inflate(R.layout.watermark_choose_item1, container, false);
			final GridView gridView = (GridView) view.findViewById(R.id.gridview);
			final int numColumns = 3;
			gridView.setNumColumns(numColumns);
			gridView.setAdapter(new CommonAdpater<WatermarkMeta>(getActivity(), metas, R.layout.watermark_choose_image) {
				@Override
				public void buildViewHolder(CommonViewHolder holder, WatermarkMeta item) {
					holder.displayImage(R.id.image, item.url);
					View convertView = holder.getConvertView();
					//LogUtils.i("convertView.getWidth():" + convertView.getWidth() + " getHeight:"
							+ convertView.getHeight());
					if (convertView.getHeight() == 0) {//第一次调用getView时，parent的高度还是0,所以这里需要判断一下，并且重新设置，否则第一个子项显示不出来  
						GridView.LayoutParams layoutParams = (GridView.LayoutParams) convertView.getLayoutParams();
						layoutParams.width = GridView.LayoutParams.FILL_PARENT;
						int paddingBottom = holder.getParent().getPaddingBottom();
						int paddingTop = holder.getParent().getPaddingTop();
						int verticalLen = gridView.getVerticalFadingEdgeLength();
						layoutParams.height = (holder.getParent().getHeight() - paddingTop - paddingBottom - (numColumns - 1)
								* verticalLen)
								/ numColumns;
						convertView.setLayoutParams(layoutParams);
					}
				}
			});
		}
		return view;
	}
	public static WatermarkChooseItemFragment newInstance(List<WatermarkMeta> metas) {
		WatermarkChooseItemFragment fragment = new WatermarkChooseItemFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable("metas", new ArrayList<WatermarkMeta>(metas));
		bundle.putInt("pageSize", 9);
		fragment.setArguments(bundle);
		return fragment;
	}
}
