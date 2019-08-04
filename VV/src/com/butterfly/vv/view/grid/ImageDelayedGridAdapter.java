package com.butterfly.vv.view.grid;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.LinkedBlockingDeque;

import com.beem.project.btf.R;
import com.beem.project.btf.ui.ShareChangeAlbumAuthorityActivity.IntentKey;
import com.butterfly.vv.camera.base.NativeImageLoader;
import com.butterfly.vv.camera.base.NativeImageLoader.NativeImageCallBack;
import com.butterfly.vv.db.ImageDelayedTask;
import com.butterfly.vv.service.TimeflyService;
import com.butterfly.vv.view.timeflyView.ItemDeleteUtils;
import com.butterfly.vv.vv.utils.JsonParseUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 显示等待上传的图片适配器 正在上传
 * @author zhenggen xie
 */
public class ImageDelayedGridAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	Context mContext;
	ArrayList<ImageDelayedTask> values;
	public boolean isvisiable = true;
	public LinkedBlockingDeque<Integer> linkedBlockingDeque = new LinkedBlockingDeque<Integer>();
	public ItemDeleteUtils itemDeleteUtils;
	// jid 用户id
	// gid 图片组id
	// creat_time 创建时间
	// signature 签名
	// authority 权限
	private String signature;
	private String createTime;
	private String albumAuthority;
	private String jid;
	private String albumId;
	private Point mPoint = new Point(0, 0);// 用来封装ImageView的宽和高的对象

	public ImageDelayedGridAdapter(Context context,
			ArrayList<ImageDelayedTask> values) {
		// super(context, R.layout.custom_data_view, values);
		mContext = context;
		this.values = values;
		this.mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.signature = ((Activity) context).getIntent().getStringExtra(
				IntentKey.SIGNATURE);
		this.createTime = ((Activity) context).getIntent().getStringExtra(
				IntentKey.CREATETIME);
		this.albumAuthority = ((Activity) context).getIntent().getStringExtra(
				IntentKey.ALBUMAUTHORITY);
		this.jid = ((Activity) context).getIntent().getStringExtra(
				IntentKey.JID);
		this.albumId = ((Activity) context).getIntent().getStringExtra(
				IntentKey.ALBUMID);
	}
	public ArrayList<ImageDelayedTask> getValues() {
		return values;
	}
	public void setValues(ArrayList<ImageDelayedTask> values) {
		this.values = values;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Holder holder;
		if (convertView == null) {
			// Inflate the view since it does not exist
			convertView = mInflater.inflate(R.layout.vvim_imageitem, parent,
					false);
			// Create and save off the holder in the tag so we get quick access
			// to inner fields
			// This must be done for performance reasons
			holder = new Holder();
			holder.upload_delayed_ll = (LinearLayout) convertView
					.findViewById(R.id.upload_delayed_ll);
			holder.img = (ImageView) convertView.findViewById(R.id.photo);
			holder.img.setPadding(3, 3, 3, 3);
			holder.img.setImageResource(R.drawable.deafult_imgloading);
			holder.img.setScaleType(ScaleType.FIT_XY);
			if (isvisiable) {
				holder.upload_delayed_ll.setVisibility(View.GONE);
			} else {
				holder.upload_delayed_ll.setVisibility(View.VISIBLE);
				holder.pb = (ProgressBar) convertView
						.findViewById(R.id.circularprogressbar1);
				holder.pb.setProgress(0);
				UploadTask task = new UploadTask(holder.pb,
						values.get(position), position);
				String[] params = { "re", "er" };
				task.execute(params);
			}
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		Bitmap bitmap = NativeImageLoader.getInstance().loadNativeImage(
				values.get(position).getUri(), mPoint,
				new NativeImageCallBack() {
					@Override
					public void onImageLoader(Bitmap bitmap, String path) {
						ImageView mImageView = (ImageView) holder.img
								.findViewWithTag(path);
						if (bitmap != null && mImageView != null) {
							mImageView.setImageBitmap(bitmap);
							Log.i("HHH", "bitmap==" + bitmap.getHeight()
									+ "  bitmap=" + bitmap.getWidth()
									+ " path=" + path);
						}
					}
				});
		if (bitmap != null) {
			holder.img.setImageBitmap(bitmap);
		} else {
			holder.img.setImageResource(R.drawable.deafult_imgloading);
		}
		Log.i("HUH", "getUri==" + values.get(position).getUri());
		// Bitmap bitmap =
		// NativeImageLoader.getInstance().loadNativeImage(values.get(position).getUri(),
		// mPoint,
		// new NativeImageCallBack() {
		// @Override
		// public void onImageLoader(Bitmap bitmap, String path) {
		// ImageView mImageView=(ImageView) holder.img.findViewWithTag(path);
		// if (bitmap != null && mImageView != null) {
		// mImageView.setImageBitmap(bitmap);
		// }
		//
		// }
		// });
		// if (bitmap != null) {
		// holder.img.setImageBitmap(bitmap);
		// } else {
		// holder.img.setImageResource(R.drawable.friends_sends_pictures_no);
		// }
		StringBuilder builder = new StringBuilder();
		builder.append("file://");
		builder.append(values.get(position).getUri());
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.default_headw_selector)
				.showImageForEmptyUri(R.drawable.default_headw_selector)
				.showImageOnFail(R.drawable.default_headw_selector)
				.cacheInMemory().cacheOnDisc().build();
		ImageLoader.getInstance().displayImage(builder.toString(), holder.img,
				options);
		// StringBuilder builder = new StringBuilder();
		// builder.append("file://");
		// builder.append(values.get(position).getUri());
		// BBSUtils.getImageView(builder.toString(), holder.img, null);
		return convertView;
	}

	/** View holder for the views we need access to */
	public static class Holder {
		public TextView textView;
		public ImageView img;
		public LinearLayout upload_delayed_ll;
		public ProgressBar pb;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return (values != null) ? values.size() : 0;
	}
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return values.get(position);
	}
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	class ProgressRunnable implements Runnable {
		Handler handle;// = new Handler();
		int pro = 0;
		ProgressBar bar;

		public ProgressRunnable(ProgressBar bar, Handler handle) {
			// TODO Auto-generated constructor stub
			this.handle = handle;
			this.bar = bar;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			pro = bar.getProgress() + 1;
			bar.setProgress(pro);
			// setTitle(String.valueOf(pro));
			if (pro < 100) {
				handle.postDelayed(this, 10);
			}
		}
	}

	private class UploadTask extends AsyncTask<String, Integer, Boolean> {
		private ProgressBar bar;
		private ImageDelayedTask imageDelayedTask;

		public UploadTask(ProgressBar bar, ImageDelayedTask imageDelayedTask,
				int index) {
			super();
			this.bar = bar;
			this.imageDelayedTask = imageDelayedTask;
		}
		@Override
		protected Boolean doInBackground(String... params) {
			Map<String, Object> retMap = TimeflyService.uploadPicture("1", "1",
					"1", this.imageDelayedTask.getUri(), null, null);
			Map<String, Object> mapslist = TimeflyService.uploadPicture(
					albumAuthority, "1", "1", this.imageDelayedTask.getUri(),
					jid, createTime);
			return JsonParseUtils.getResult(mapslist);
		}
		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			this.bar.setProgress(values[0]);
		}
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (result) {
				values.remove(this.imageDelayedTask);
				try {
					itemDeleteUtils.deleteOneImage(imageDelayedTask);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				notifyDataSetChanged();
			} else {
				this.bar.setProgress(0);
			}
		}
	}
}
