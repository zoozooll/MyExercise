/**
 * 
 */
package com.butterfly.vv;

import org.apache.commons.lang.StringUtils;

import com.beem.project.btf.R;
import com.beem.project.btf.utils.DimenUtils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.view.ViewStub.OnInflateListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * @author Aaron Lee Created at 下午5:56:32 2015-9-14
 */
public class GalleryNavigation extends FrameLayout {
	private Context activity;
	private View rootView;
	private TextView tvw_topbar_title;
	private ImageButton btn_topbar_camera;
	private Button btn_topbar_upload;
	private ViewStub stub_topbar_center;
	private View[] tvw_gallery_topbar_tab = new View[2];
	private boolean choiseMode;
	private boolean cameraBtnHide;
	private boolean stubInflated;

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public GalleryNavigation(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	/**
	 * @param context
	 * @param attrs
	 */
	public GalleryNavigation(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	/**
	 * @param context
	 */
	public GalleryNavigation(Context context) {
		super(context);
		init(context);
	}
	private void init(Context activity) {
		this.activity = activity;
		rootView = LayoutInflater.from(activity).inflate(
				R.layout.topbar_gallery_list, null);
		addView(rootView, android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		initViews();
	}
	private void initViews() {
		tvw_topbar_title = (TextView) rootView
				.findViewById(R.id.tvw_topbar_title);
		btn_topbar_camera = (ImageButton) rootView
				.findViewById(R.id.btn_topbar_camera);
		btn_topbar_upload = (Button) rootView
				.findViewById(R.id.btn_topbar_upload);
		stub_topbar_center = (ViewStub) rootView
				.findViewById(R.id.stub_topbar_center);
	}
	public void setBtnLeftIcon(int resId) {
		tvw_topbar_title.setCompoundDrawablesWithIntrinsicBounds(getResources()
				.getDrawable(resId), null, null, null);
		tvw_topbar_title.setPadding(0, 0, DimenUtils.dip2px(activity, 15), 0);
	}
	public void setBtnRightIcon(int resId) {
		btn_topbar_camera.setImageResource(resId);
	}
	public void setStringTitle(String title, String subTitle) {
		if (StringUtils.isEmpty(subTitle)) {
			subTitle = "";
		}
		tvw_topbar_title.setText(title + subTitle);
	}
	public void setStrUpload(String stringUpload) {
		btn_topbar_upload.setText(stringUpload);
	}
	public void inflateCenter(final OnClickListener onClick) {
		stubInflated = true;
		stub_topbar_center.setOnInflateListener(new OnInflateListener() {
			@Override
			public void onInflate(ViewStub stub, View inflated) {
				tvw_gallery_topbar_tab[0] = inflated
						.findViewById(R.id.tvw_gallery_topbar_tab0);
				tvw_gallery_topbar_tab[0].setOnClickListener(onClick);
				tvw_gallery_topbar_tab[1] = inflated
						.findViewById(R.id.tvw_gallery_topbar_tab1);
				tvw_gallery_topbar_tab[1].setOnClickListener(onClick);
			}
		});
		stub_topbar_center.inflate();
		stub_topbar_center.setVisibility(View.VISIBLE);
	}
	public void setCenterText(String left, String right) {
		((TextView) tvw_gallery_topbar_tab[0]).setText(left);
		((TextView) tvw_gallery_topbar_tab[1]).setText(right);
	}
	public void hindTopbarCenter(boolean hide) {
		stub_topbar_center.setVisibility(hide ? View.GONE : View.VISIBLE);
	}
	public void setTopbarTab(int position) {
		for (int i = 0, size = tvw_gallery_topbar_tab.length; i < size; i++) {
			if (i == position) {
				tvw_gallery_topbar_tab[i].setSelected(true);
			} else {
				tvw_gallery_topbar_tab[i].setSelected(false);
			}
		}
	}
	public void setChoiseMode(String choiseText) {
		btn_topbar_camera.setVisibility(View.GONE);
		btn_topbar_upload.setVisibility(View.VISIBLE);
		btn_topbar_upload.setText(choiseText);
		stub_topbar_center.setVisibility(View.GONE);
		choiseMode = true;
	}
	public void cancelChoiseMode() {
		if (!cameraBtnHide) {
			btn_topbar_camera.setVisibility(View.VISIBLE);
		}
		btn_topbar_upload.setVisibility(View.GONE);
		if (stubInflated) {
			stub_topbar_center.setVisibility(View.VISIBLE);
		}
		choiseMode = false;
	}
	public boolean isChoiceMode() {
		return choiseMode;
	}
	public void setCenterInflated(boolean inflated) {
		stubInflated = inflated;
	}
	public void setCameraBtnHide(boolean hide) {
		this.cameraBtnHide = hide;
		if (hide) {
			btn_topbar_camera.setVisibility(View.GONE);
		} else if (choiseMode) {
			btn_topbar_camera.setVisibility(View.GONE);
		} else {
			btn_topbar_camera.setVisibility(View.VISIBLE);
		}
	}
	public void setBtnLeftListener(OnClickListener listener) {
		tvw_topbar_title.setOnClickListener(listener);
	}
	public void setBtnUploadListener(OnClickListener listener) {
		btn_topbar_upload.setOnClickListener(listener);
	}
	public void setCameraListener(OnClickListener listener) {
		btn_topbar_camera.setOnClickListener(listener);
	}
	public void setBtnLeftHide() {
		tvw_topbar_title.setVisibility(View.GONE);
	}
}
