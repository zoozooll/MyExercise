package com.butterfly.vv.view;

import java.util.ArrayList;
import com.agimind.widget.SlideHolder;
import com.beem.project.btf.R;
import com.butterfly.vv.db.ImageDelayedTask;
import com.butterfly.vv.db.ormhelper.bean.VVImage;
import com.butterfly.vv.view.grid.ImageDelayedGridAdapter;
import com.butterfly.vv.view.timeflyView.HorizontalListView;
import com.butterfly.vv.view.timeflyView.ItemDeleteUtils;
import com.beem.project.btf.bbs.view.BBSCustomerDialog;
import com.beem.project.btf.constant.Constants;
import com.beem.project.btf.ui.views.SimpleDilaogView;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TextView;

public class WaitToUploadChooseView {
	private Context mContext;
	private View mView;
	private ArrayList<ImageDelayedTask> tempVvImages = new ArrayList<ImageDelayedTask>();
	private ImageDelayedGridAdapter delayedGridAdapter;
	private HorizontalListView horizontalListView;
	private ImageView iamge_delayed_left_more;
	private ImageView iamge_delayed_right_more;
	private ItemDeleteUtils itemDeleteUtils;
	private ProgressBar pb1;
	private Handler handle = new Handler();
	private LinearLayout upload_delayed_ll;
	private TextView info_count;
	private Button bt_upload_delayed;
	private BBSCustomerDialog blurDlg;
	private ArrayList<String> listimagePath;

	public WaitToUploadChooseView(Context mContext, SlideHolder slideHolder) {
		super();
		this.mContext = mContext;
		LayoutInflater inflater = (LayoutInflater) this.mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mView = inflater.inflate(R.layout.wait_to_be_upload, null);
		BT_lt lt = new BT_lt();
		horizontalListView = (HorizontalListView) mView
				.findViewById(R.id.hlvCustomList_dia);
		horizontalListView.setSlideHolder(slideHolder);
		horizontalListView.setBackgroundResource(R.color.background_normal);
		ArrayList<ImageDelayedTask> temp = new ArrayList<ImageDelayedTask>();
		ArrayList<String> listimagePath = ((Activity) mContext).getIntent()
				.getStringArrayListExtra("listimagePath");
		if (listimagePath != null) {
			for (int i = 0; i < listimagePath.size(); i++) {
				ImageDelayedTask delayedTask = new ImageDelayedTask();
				delayedTask.setUri(listimagePath.get(i));
				temp.add(delayedTask);
			}
		}
		/**
		 * 这里是添加上传图片的地方
		 */
		delayedGridAdapter = new ImageDelayedGridAdapter(mContext, temp);
		if (temp != null) {
			delayedGridAdapter.isvisiable = false;
			delayedGridAdapter.notifyDataSetChanged();
		}
		horizontalListView.setAdapter(delayedGridAdapter);
		itemDeleteUtils = new ItemDeleteUtils(delayedGridAdapter);
		delayedGridAdapter.itemDeleteUtils = itemDeleteUtils;// 20140208
		horizontalListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				System.out.println(tempVvImages.size() + " jdkfkjd ----" + arg2);
				try {
					VVImage image = new VVImage();
					image.setPath(tempVvImages.get(arg2).getUri());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		bt_upload_delayed = (Button) mView.findViewById(R.id.bt_upload_delayed);
		Button bt_delete_all_delayed = (Button) mView
				.findViewById(R.id.bt_delete_all_delayed);
		bt_upload_delayed.setOnClickListener(lt);
		bt_delete_all_delayed.setOnClickListener(lt);
		info_count = (TextView) mView.findViewById(R.id.info_count);
	}
	public void fade_in(View v) {
		Animation a = AnimationUtils.loadAnimation(mContext,
				R.anim.action_fade_in);
		if (a != null) {
			a.reset();
			if (v != null) {
				v.clearAnimation();
				v.startAnimation(a);
			}
		}
	}
	public void fade_out(View v) {
		Animation a = AnimationUtils.loadAnimation(mContext,
				R.anim.action_fade_out);
		if (a != null) {
			a.reset();
			if (v != null) {
				v.clearAnimation();
				v.startAnimation(a);
			}
		}
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.obj != null) {
				tempVvImages = (ArrayList<ImageDelayedTask>) msg.obj;
				//LogUtils.i("--tempVvImages-->" + tempVvImages.size());
				if (Constants.isDebug) {
					for (int i = 0; i < 20; i++) {
						ImageDelayedTask delayedTask = new ImageDelayedTask();
						tempVvImages.add(delayedTask);
					}
					Log.i("delayedGridAdapter", "temp" + tempVvImages);
				}
				info_count.setText("共" + tempVvImages.size() + "张图片正在上传");
				delayedGridAdapter.setValues(tempVvImages);
				delayedGridAdapter.notifyDataSetChanged();
			}
		}
	};

	public View getmView() {
		return mView;
	}
	public void invalidateImageDelayedList(ImageDelayedTask delayedTask) {
		//LogUtils.i("invalidateImageDelayedList " + delayedTask.getUri());
		tempVvImages.add(delayedTask);
		delayedGridAdapter.notifyDataSetChanged();
	}

	class BT_lt implements OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.bt_upload_delayed:
					if (bt_upload_delayed.getText().toString().equals("开始上传")) {
						delayedGridAdapter.isvisiable = false;
						delayedGridAdapter.notifyDataSetInvalidated();
						bt_upload_delayed.setText("暂停上传");
					} else if (bt_upload_delayed.getText().toString()
							.equals("暂停上传")) {
						bt_upload_delayed.setText("开始上传");
						delayedGridAdapter.isvisiable = true;
						delayedGridAdapter.notifyDataSetInvalidated();
					}
					break;
				case R.id.bt_delete_all_delayed:
					// 防止出现多个对话框
					if (blurDlg != null && blurDlg.isShowing()) {
						blurDlg.dismiss();
					}
					blurDlg = BBSCustomerDialog.newInstance(mContext,
							R.style.blurdialog);
					SimpleDilaogView simpleDilaogView = new SimpleDilaogView(
							mContext);
					// 设置标题
					simpleDilaogView.setTitle("确定全部取消上传吗?");
					// 分别设置两个按钮及定义操作
					simpleDilaogView.setPositiveButton("确定",
							new OnClickListener() {
								@Override
								public void onClick(View v) {
									blurDlg.dismiss();
								}
							});
					simpleDilaogView.setNegativeButton("取消",
							new OnClickListener() {
								@Override
								public void onClick(View v) {
									blurDlg.dismiss();
								}
							});
					View blurView = simpleDilaogView.getmView();
					blurDlg.setContentView(blurView);
					blurDlg.show();
					break;
				default:
					break;
			}
		}
	}

	int pro = 0;
	Runnable add = new Runnable() {
		@Override
		public void run() {
			pro = pb1.getProgress() + 1;
			pb1.setProgress(pro);
			// setTitle(String.valueOf(pro));
			if (pro < 100) {
				handle.postDelayed(add, 100);
			}
		}
	};

	public boolean hasImageTask() {
		return tempVvImages.size() == 0;
	}
}
