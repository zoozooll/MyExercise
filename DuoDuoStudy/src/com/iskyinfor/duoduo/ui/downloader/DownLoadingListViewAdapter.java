package com.iskyinfor.duoduo.ui.downloader;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.iskyinfor.duoduo.R;
import com.iskyinfor.duoduo.downloadManage.Constants;
import com.iskyinfor.duoduo.downloadManage.DownloadServiceStub;
import com.iskyinfor.duoduo.downloadManage.DownloadTask;
import com.iskyinfor.duoduo.ui.lesson.NeedDataInformation;

public class DownLoadingListViewAdapter extends BaseAdapter {
	private Context context = null;
	private LayoutInflater factory = null;
    private ArrayList<DownloadTask> list;
    private DownloadServiceStub downloadServiceStub;
	public DownLoadingListViewAdapter(Context c,ArrayList<DownloadTask> dataList) {
		context = c;
		this.list=dataList;
		factory = LayoutInflater.from(context);
		this.downloadServiceStub=new DownloadServiceStub(c);
	}

	public int getCount() {
		return list.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View v, ViewGroup parent) {
//		ViewHolder holder = null;
        Log.i("liu", "loaing data");
        final DownloadTask downloadTask=list.get(position);
//			if (v == null) {
				v = factory.inflate(R.layout.downloading_item_layout, null);
				final ViewHolder	holder=new ViewHolder();
				holder.image = (ImageView) v.findViewById(R.id.download_imge_item);
				holder.name = (TextView) v.findViewById(R.id.download_txt_name);
				holder.bar = (ProgressBar) v.findViewById(R.id.download_bar_item);
				holder.iBtn = (ImageButton) v.findViewById(R.id.download_iBtn_item);
				holder.stateText=(TextView) v.findViewById(R.id.state_text);

				v.setTag(holder);
		        v.setFocusable(true);
		        v.setLongClickable(true);
//			} else {
//				holder=(ViewHolder) v.getTag();
//			}
				
			if(downloadTask!=null){
				holder.name.setText(downloadTask.name);
				
				switch (downloadTask.taskState) {
				case DownloadTask.State.RUNNING:
					holder.bar.setVisibility(View.VISIBLE);
					holder.bar.setIndeterminate(false);
					holder.bar.setProgress(downloadTask.progress);	
					holder.iBtn.setBackgroundResource(R.drawable.pause_download);
					holder.stateText.setVisibility(View.GONE);
					break;
				case DownloadTask.State.PAUSE:
					holder.bar.setVisibility(View.GONE);
					holder.bar.setIndeterminate(false);
					holder.iBtn.setBackgroundResource(R.drawable.restart_download);
					holder.stateText.setVisibility(View.VISIBLE);
					holder.stateText.setText("下载暂停");
					break;
				case DownloadTask.State.FAIL:
					holder.bar.setVisibility(View.GONE);
					holder.bar.setIndeterminate(false);
					holder.iBtn.setBackgroundResource(R.drawable.restart_download);
					holder.stateText.setVisibility(View.VISIBLE);
					holder.stateText.setText("下载失败");
					break;
				}
				
			}
			
			holder.iBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(DownloadTask.State.RUNNING==downloadTask.taskState){
						downloadServiceStub.pauseDownloadTask(downloadTask.resourceId);
					}else if(DownloadTask.State.PAUSE==downloadTask.taskState){
						downloadServiceStub.revertDownloadTask(downloadTask.resourceId);
						startService();
						
					}else if(DownloadTask.State.FAIL==downloadTask.taskState){
						downloadServiceStub.revertDownloadTask(downloadTask.resourceId);
						startService();
						
					}
					holder.iBtn.setBackgroundResource(R.drawable.pause_download);
				}
			});
			
		
		return v;
	}

	
	private void startService(){
		Intent intent=new Intent(); 
		intent.setAction(Constants.ACTION_START_SERVICE);
		context.startService(intent);
	}
	
	 class ViewHolder {
		ImageView image = null;
		TextView name = null;
		ProgressBar bar = null;
		ImageButton iBtn = null;
		TextView stateText;
	}

}
