/**R.
 * 
 */
package com.tcl.manager.adapter;

import java.util.List;

import com.tcl.framework.log.NLog;
import com.tcl.manager.base.StatisticInfo;
import com.tcl.manager.score.InstalledAppProvider;
import com.tcl.manager.score.ScoreLevel;
import com.tcl.manager.util.AndroidUtil;
import com.tcl.manager.util.HandlerUtils;
import com.tcl.manager.util.PkgManagerTool;
import com.tcl.manager.util.TCLThreadPool;
import com.tcl.mie.manager.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author zuokang.li
 *
 */
public class StatisticAdapter extends BaseAdapter {
	
	
	private Context mContext;
	private List<StatisticInfo> data;
	private LayoutInflater mInflater;
	private Resources mRes;
	

	public StatisticAdapter(Context context, List<StatisticInfo> data) {
		super();
		this.mContext = context;
		this.data = data;
		mInflater = LayoutInflater.from(context);
		mRes = context.getResources();
	}
	
	public void setData(List<StatisticInfo> data) {
		this.data = data;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		if (data != null) {
			return data.size();
		}
		return 0;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public StatisticInfo getItem(int position) {
		if (data != null) {
			return data.get(position);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null)
		{
			convertView = View.inflate(mContext, R.layout.statistic_item, null);
			holder = new ViewHolder();
			holder.imageIcon     = (ImageView) convertView.findViewById(R.id.imageIcon );
			holder.textTitle     = (TextView) convertView.findViewById(R.id.textTitle    );
			holder.textState     = (TextView) convertView.findViewById(R.id.textState   );
			holder.textFrequency = (TextView) convertView.findViewById(R.id.textFrequency);
			holder.textScore     = (TextView) convertView.findViewById(R.id.textScore    );
			holder.imageRight    = (ImageView) convertView.findViewById(R.id.imageRight   );
			holder.layoutBottom = convertView.findViewById(R.id.layoutBottom);
			holder.textMemoery   = (TextView) convertView.findViewById(R.id.textMemoery  );
			holder.textBattery   = (TextView) convertView.findViewById(R.id.textBattery  );
			holder.textData      = (TextView) convertView.findViewById(R.id.textData     );
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		final StatisticInfo item = getItem(position);
		if (item != null) {
			
			convertView.setTag(R.id.package_name, item.pkgName);
			//if (item.icon != null) holder.imageIcon.setImageDrawable(item.icon); 
//			Drawable icon = PkgManagerTool.getIcon(mContext, item.pkgName);
//			if (icon != null) {
//				holder.imageIcon.setImageDrawable(icon);
//			} else {
//				holder.imageIcon.setImageResource(R.drawable.ic_launcher);
//			}
			loadIcon(holder, item.pkgName);
			holder.textTitle.setText(item.appName);
			
			/*OnClickListener detailListener = new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					String packageName = item.pkgName;
					Intent activity = new Intent(mContext, AppDetailActivity.class);
					activity.putExtra(AppDetailActivity.EXTRA_PACKGENAME, packageName);
					mContext.startActivity(activity);;
				}
			};*/
			//holder.textTitle.setOnClickListener(detailListener);
			String str = "";
			if (item.level != null) {
				str = mContext.getApplicationContext().getResources().getStringArray(R.array.frequencyLevel)[item.level.ordinal()];
			}
			//holder.imageIcon.setOnClickListener(detailListener);
			holder.textState.setText(str);
			StringBuffer buffer = new StringBuffer();
			/*buffer.append(item.frequencyTimes);
			buffer.append(" ");
			buffer.append(mRes.getString(R.string.app_detail_times_per_day_text));
			buffer.append("   ");*/
			buffer.append(AndroidUtil.formatTime(item.frequencySeconds, 
				" " + mRes.getString(R.string.app_detail_hours_per_day_text), 
				" " + mRes.getString(R.string.app_detail_mins_per_day_text)));
			str = buffer.toString();
			holder.textFrequency.setText(str);
			holder.textScore.setText(String.valueOf((int)item.score));
			holder.textScore.setTextColor(ScoreLevel.resolveToColor((int) item.score));
			final View bottomView = holder.layoutBottom;
			final ImageView iv = holder.imageRight;
			if (item.showBottom) {
				bottomView.setVisibility(View.VISIBLE);
				iv.setImageResource(R.drawable.ic_statistic_opened);
			} else {
				bottomView.setVisibility(View.GONE);
				iv.setImageResource(R.drawable.ic_statistic_closed);
			}
 			OnClickListener showBottomListener = new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (bottomView != null) {
						if(bottomView.getVisibility() == View.VISIBLE) {
							bottomView.setVisibility(View.GONE);
							iv.setImageResource(R.drawable.ic_statistic_closed);
							item.showBottom = false;
						} else {
							bottomView.setVisibility(View.VISIBLE);
							iv.setImageResource(R.drawable.ic_statistic_opened);
							item.showBottom = true;
						}
					}
				}
			};
			holder.textScore.setOnClickListener(showBottomListener);
			holder.imageRight.setOnClickListener(showBottomListener);
			//str = mContext.getResources().getString(R.string.statistic_item_memory, item.memory);
			str = item.isRunning ? AndroidUtil.formatSize((long)item.memory) : mRes.getString(R.string.app_detail_unrecord_text);
			holder.textMemoery.setText(str);
			str = String.format("%.1f%%", item.batteryPercent);
			holder.textBattery.setText(str);
			str = mRes.getString(R.string.app_detail_today_text) + ": " + AndroidUtil.formatSize(item.dataToday) + "\n"
					+ AndroidUtil.formatSize(item.dataPerDay) + mRes.getString(R.string.app_detail_per_day_text);
			holder.textData.setText(str);
		}
		return convertView;
	}
	
	
	protected void loadIcon(final ViewHolder holder2, final String packageName) {
		holder2.imageIcon.setTag(R.id.container, packageName);
		Bitmap icon = InstalledAppProvider.getInstance().getIcon(packageName);
		if( icon != null) {
			holder2.imageIcon.setImageBitmap(icon);
			return;
		}
		else {
			holder2.imageIcon.setImageResource(R.drawable.ic_default);
		}
		TCLThreadPool.runOnNonUIThread(new Runnable() {
			@Override
			public void run() {
				doLoad(holder2, packageName);
			}
		});
	}
	
	
	private void doLoad(final ViewHolder holder2, final String pakcageName) {
		try {
			if (holder2 != null) {
				Drawable icon = PkgManagerTool.getIcon(mContext, pakcageName);
				if (icon instanceof BitmapDrawable) {
					Bitmap bitmap = ((BitmapDrawable) icon).getBitmap();
					Bitmap target = null;
					if (bitmap.getHeight() > holder2.imageIcon.getHeight() && bitmap.getHeight() > 0 && bitmap.getWidth() > 0) {
						float rate = holder2.imageIcon.getHeight() * 1.0f / bitmap.getHeight();
						try {
							target = com.tcl.manager.util.BitmapUtil.zoomBitmap(mContext, bitmap, rate);
						} catch (Exception e) {
							e.printStackTrace();
							target = bitmap;
						}
					} else {
						target = bitmap;
					}
					NLog.d("TTT", "set target" + pakcageName + target);
					if (target != null) {
						InstalledAppProvider.getInstance().setIcon(pakcageName, target);
						setLoadedImage(holder2, target, pakcageName);
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setLoadedImage(final ViewHolder holder2, final Bitmap bitmap, final String packageName) {
		HandlerUtils.runUITask(new Runnable() {
			@Override
			public void run() {
				String key = (String) holder2.imageIcon.getTag(R.id.container);
				if (!TextUtils.isEmpty(key) && key.equals(packageName)) {
					holder2.imageIcon.setImageBitmap(bitmap);
				}
			}
		});
	}
	
	private static class ViewHolder {
		private ImageView imageIcon;
		private TextView  textTitle;
		private TextView  textState;
		private TextView  textFrequency;
		private TextView  textScore;
		private ImageView imageRight;
		private View layoutBottom;
		private TextView  textMemoery;
		private TextView  textBattery;
		private TextView  textData;

	}

}
