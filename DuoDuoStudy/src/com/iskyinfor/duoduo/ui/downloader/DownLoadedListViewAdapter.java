package com.iskyinfor.duoduo.ui.downloader;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
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
import com.iskyinfor.duoduo.downloadManage.DownloadTask;
import com.iskyinfor.duoduo.ui.lesson.NeedDataInformation;

public class DownLoadedListViewAdapter extends BaseAdapter {
	private Context context = null;
	private LayoutInflater factory = null;
    private ArrayList<File> list;
	public DownLoadedListViewAdapter(Context c,ArrayList<File> dataList) {
		context = c;
		this.list=dataList;
		factory = LayoutInflater.from(context);
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
		ViewHolder holder = null;
        Log.i("liu", "loaing data");
        final File downloadTask=list.get(position);
//			if (v == null) {
				holder = new ViewHolder();
				v = factory.inflate(R.layout.downloaded_item_layout, null);
				
				holder.image = (ImageView) v.findViewById(R.id.downloaded_imge_item);
				holder.name = (TextView) v.findViewById(R.id.downloaded_txt_name);
				holder.iBtn = (ImageButton) v.findViewById(R.id.delete_file_item);

				v.setTag(holder);
//			} else {
//				holder=(ViewHolder) v.getTag();
//			}
			if(downloadTask!=null){
				holder.name.setText(downloadTask.getName()+"");
			}
			holder.iBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					list.remove(downloadTask);
					downloadTask.delete();
				notifyDataSetChanged();
					
				}
			});
		return v;
	}

	 class ViewHolder {
		ImageView image = null;
		TextView name = null;
		ProgressBar bar = null;
		ImageButton iBtn = null;
	}

}
