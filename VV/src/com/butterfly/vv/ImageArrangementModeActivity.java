package com.butterfly.vv;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.beem.project.btf.BeemApplication;
import com.beem.project.btf.R;
import com.beem.project.btf.manager.ImageFolderItemManager;
import com.beem.project.btf.ui.activity.base.VVBaseFragmentActivity;
import com.beem.project.btf.ui.dialog.VVBaseLoadingDlg;
import com.beem.project.btf.ui.dialog.VVBaseLoadingDlg.VVBaseLoadingDlgCfg;
import com.beem.project.btf.ui.fragment.ImageListFragment;
import com.beem.project.btf.ui.fragment.ShareRankingFragment;
import com.beem.project.btf.ui.views.CustomTitleBtn;
import com.butterfly.vv.model.ImageFolderItem;
import com.butterfly.vv.vv.utils.CToast;
import com.teleca.jamendo.api.WSError;

/**
 * 原视图MyImagesDeleteUpload(很渣!)耦合性太高,不利于维护管理,故两种排列方式分别独立成两个Fragment,便于重复利用和管理
 * @author le yang
 */
public class ImageArrangementModeActivity extends VVBaseFragmentActivity {
	private CustomTitleBtn editbutton, back;
	private ArrangementModeType status = ArrangementModeType.LIST;
	private VVBaseLoadingDlg<ImageFolderItem> loadDataDlg;

	public enum ArrangementModeType {
		LIST, TILE
	}

	private ShareRankingFragment sharerankingfragment;
	private ImageListFragment imagelistfragment;
	private ImageFolderItem imageFolderItem;

	public static void launch(Context ctx, ImageFolderItem folderItem) {
		Intent intent = new Intent(ctx, ImageArrangementModeActivity.class);
		intent.putExtra("ImageFolderItems", folderItem);
		ctx.startActivity(intent);
	}
	public static void launch(Context ctx, String jid, String gid,
			String gid_create_time) {
		Intent intent = new Intent(ctx, ImageArrangementModeActivity.class);
		intent.putExtra("jid", jid);
		intent.putExtra("gid", gid);
		intent.putExtra("gid_create_time", gid_create_time);
		ctx.startActivity(intent);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.fragment_activity_main);
		if (getIntent().hasExtra("ImageFolderItems")) {
			imageFolderItem = getIntent()
					.getParcelableExtra("ImageFolderItems");
		}
		if (imageFolderItem == null) {
			loadDataDlg = new VVBaseLoadingDlg<ImageFolderItem>(
					new VVBaseLoadingDlgCfg(mContext).setShowWaitingView(true)) {
				@Override
				protected ImageFolderItem doInBackground() {
					String jid = getIntent().getStringExtra("jid");
					String gid = getIntent().getStringExtra("gid");
					String gid_create_time = getIntent().getStringExtra(
							"gid_create_time");
					try {
						return ImageFolderItemManager.getInstance()
								.getImageFolderItem(jid, gid, gid_create_time);
					} catch (WSError e) {
						e.printStackTrace();
						setManulaTimeOut(true);
					}
					return null;
				}
				@Override
				protected void onPostExecute(ImageFolderItem result) {
					super.onPostExecute(result);
					imageFolderItem = result;
					if (imageFolderItem != null) {
						postShowView();
					} else {
						//请求数据会遇到：{"result": "-9", "description": "Access frequency is too high"}
						CToast.showToast(BeemApplication.getContext(),
								"获取数据失败", Toast.LENGTH_SHORT);
					}
				}
			};
			loadDataDlg.execute();
		} else {
			postShowView();
		}
	}
	private void postShowView() {
		editbutton = (CustomTitleBtn) getWindow().findViewById(R.id.rightbtn1);
		editbutton.setText(R.string.ImageArrangementMode_List)
				.setImgVisibility(View.GONE).setViewPaddingRight()
				.setVisibility(View.VISIBLE);
		editbutton.setOnClickListener(MainClickListener);
		back = (CustomTitleBtn) getWindow().findViewById(R.id.leftbtn1);
		back.setTextAndImgRes("返回", R.drawable.bbs_back_selector)
				.setViewPaddingLeft().setVisibility(View.VISIBLE);
		back.setOnClickListener(MainClickListener);
		TextView contacts_textView2 = (TextView) findViewById(R.id.topbar_title);
		contacts_textView2.setVisibility(View.GONE);
		sharerankingfragment = new ShareRankingFragment();
		imagelistfragment = new ImageListFragment();
		// 根据上次退出时保存的值来显示页面
		String ModeType = mSettings.getString("ArrangementModeType",
				ArrangementModeType.LIST.toString());
		if (ModeType.equals(ArrangementModeType.LIST.toString())) {
			status = ArrangementModeType.LIST;
		} else {
			status = ArrangementModeType.TILE;
		}
		switchFragment(status);
	}

	private OnClickListener MainClickListener = new OnClickListener() {
		@Override
		public void onClick(View paramView) {
			// TODO Auto-generated method stub
			if (paramView == editbutton) {
				if (status == ArrangementModeType.LIST) {
					status = ArrangementModeType.TILE;
				} else if (status == ArrangementModeType.TILE) {
					status = ArrangementModeType.LIST;
				}
				Editor ed = mSettings.edit();
				ed.putString("ArrangementModeType", status.toString());
				ed.commit();
				switchFragment(status);
			}
			if (paramView == back) {
				finish();
			}
		}
	};

	// 用于切换Fragment
	public void switchFragment(ArrangementModeType status) {
		this.status = status;
		FragmentManager frgmtManager = getSupportFragmentManager();
		FragmentTransaction frgmtTransaction = frgmtManager.beginTransaction();
		if (status == ArrangementModeType.LIST) {
			editbutton.setText(R.string.ImageArrangementMode_List)
					.setImgVisibility(View.GONE);
			if (frgmtManager.findFragmentByTag("imagelistfragment") == null) {
				// 传递数据包
				Bundle bundle = new Bundle();
				bundle.putParcelable(ImageListFragment.IMAGELIST_FOLDERITEM,
						imageFolderItem);
				imagelistfragment.setArguments(bundle);
				// imagelistfragment添加到activity
				frgmtTransaction.add(R.id.id_content, imagelistfragment,
						"imagelistfragment");
				frgmtTransaction.commit();
				if (frgmtManager.findFragmentByTag("sharerankingfragment") != null) {
					frgmtTransaction.hide(sharerankingfragment);
				}
				frgmtTransaction.show(imagelistfragment);
			} else {
				if (frgmtManager.findFragmentByTag("sharerankingfragment") != null) {
					frgmtTransaction.hide(sharerankingfragment);
				}
				frgmtTransaction.show(imagelistfragment);
				frgmtTransaction.commit();
			}
		} else {
			editbutton.setText(R.string.ImageArrangementMode_TILE)
					.setImgVisibility(View.GONE);
			if (frgmtManager.findFragmentByTag("sharerankingfragment") == null) {
				// 传递数据包
				Bundle bundle = new Bundle();
				bundle.putParcelable(ShareRankingFragment.SR_FOLDERITEM,
						imageFolderItem);
				bundle.putString(ShareRankingFragment.SR_NEXT, null);
				sharerankingfragment.setArguments(bundle);
				// sharerankingfragment添加到activity
				if (frgmtManager.findFragmentByTag("imagelistfragment") != null) {
					frgmtTransaction.hide(imagelistfragment);
				}
				frgmtTransaction.add(R.id.id_content, sharerankingfragment,
						"sharerankingfragment");
				frgmtTransaction.show(sharerankingfragment);
				frgmtTransaction.commit();
			} else {
				if (frgmtManager.findFragmentByTag("imagelistfragment") != null) {
					frgmtTransaction.hide(imagelistfragment);
				}
				frgmtTransaction.show(sharerankingfragment);
				// 一定要记得提交操作
				frgmtTransaction.commit();
			}
		}
	}
	@Override
	public void registerVVBroadCastReceivers() {
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (loadDataDlg != null) {
			loadDataDlg.cancel(false);
		}
	}
}
