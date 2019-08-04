package com.tcl.manager.activity.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.tcl.manager.activity.AppDetailActivity;
import com.tcl.manager.activity.MainActivity;
import com.tcl.manager.activity.entity.ExpandableListItem;
import com.tcl.manager.activity.entity.OptimizeChildItem;
import com.tcl.manager.util.MemoryInfoProvider;
import com.tcl.manager.view.UIHelper;
import com.tcl.mie.manager.R;

/**
 * @Description:
 * @author wenchao.zhang
 * @date 2015年1月4日 下午7:18:33
 * @copyright TCL-MIE
 */

public class MainAdapter extends BaseAdapter {
	private List<ExpandableListItem> parent = new ArrayList<ExpandableListItem>();
	private Context mContext;
	private Handler handler;

	public MainAdapter(Context context, Handler handler) {
		this.mContext = context;
		this.handler = handler;
	}

	public void setList(List<ExpandableListItem> parent) {
		this.parent = parent;
		this.notifyDataSetChanged();
	}

	public void addList(List<ExpandableListItem> list) {
		this.parent.addAll(list);
		this.notifyDataSetChanged();
	}

	public void addItem(ExpandableListItem item) {
		this.parent.add(item);
		this.notifyDataSetChanged();
	}

	/**
	 * 内存和杀死进程加载完成
	 */
	public void setLoadingFinish() {
		if (this.parent.size() >= 2) {
			this.parent.get(0).setLoading(false);
			this.parent.get(1).setLoading(false);
			this.notifyDataSetChanged();
		}
	}

	/**
	 * 设置停止app数量和释放内存
	 * 
	 * @param appCount
	 * @param memory
	 */
	public void setAppStopedLabel(int appCount, long memory) {
		if (this.parent.size() >= 1) {
			String lable = appCount
					+ mContext.getResources().getString(
							R.string.main_list_item_label_03);
			this.parent.get(0).setCount(lable);
		}
		setMemoryLabel(memory);
		this.notifyDataSetChanged();
	}

	private void setMemoryLabel(long memory) {
		if (this.parent.size() >= 2) {
			String memoryStr = MemoryInfoProvider.byteToMB(mContext, memory);
			this.parent.get(1).setCount(memoryStr);
		}
	}

	public void clear() {
		this.parent.clear();
		this.notifyDataSetChanged();
	}

	public static interface IViewType {
		int NORMAL = 0;
		int EXPANDABLE = 1;
	}
	

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		return parent.get(position).isCanExpand() ? IViewType.EXPANDABLE
				: IViewType.NORMAL;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return parent.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return parent.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final ExpandableListItem object = this.parent.get(position);
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
			switch (getItemViewType(position)) {
			case IViewType.NORMAL:
				convertView = inflater.inflate(R.layout.main_list_view_item_01,
						parent, false);
				holder.statusIcon = (ImageView) convertView
						.findViewById(R.id.main_list_view_item_icon);
				holder.desc = (TextView) convertView
						.findViewById(R.id.main_list_view_item_desc);
				holder.count = (TextView) convertView
						.findViewById(R.id.main_list_view_item_count);
				holder.loadIcon = (ImageView) convertView
						.findViewById(R.id.main_list_view_item_load_icon);
				RotateAnimation ra = new RotateAnimation(0, 359,
						Animation.RELATIVE_TO_SELF, 0.5f,
						Animation.RELATIVE_TO_SELF, 0.5f);
				ra.setDuration(500);
				ra.setRepeatCount(-1);
				ra.setInterpolator(new LinearInterpolator());
				holder.loadIcon.startAnimation(ra);
				break;
			case IViewType.EXPANDABLE:
				convertView = inflater.inflate(R.layout.main_list_view_item_02,
						parent, false);
				holder.statusIcon = (ImageView) convertView
						.findViewById(R.id.main_list_view_item_icon);
				holder.desc = (TextView) convertView
						.findViewById(R.id.main_list_view_item_desc);
				holder.count = (TextView) convertView
						.findViewById(R.id.main_list_view_item_count);
				holder.childListView = (ListView) convertView
						.findViewById(R.id.main_list_item_childlist);
				holder.relative = (RelativeLayout) convertView
						.findViewById(R.id.main_list_view_item_linear_layout);
				break;

			default:
				break;
			}
			if (convertView != null) convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (getItemViewType(position) == IViewType.NORMAL) {
			if (object.isLoading()) {
				holder.loadIcon.setVisibility(View.VISIBLE);
				holder.statusIcon.setVisibility(View.GONE);
			} else {
				holder.loadIcon.setVisibility(View.GONE);
				holder.statusIcon.setVisibility(View.VISIBLE);
				holder.statusIcon.setImageResource(object.getImgResId());
			}
		}else{
			holder.statusIcon.setImageResource(object.getImgResId());
		}
		
		
		holder.desc.setText(object.getDesc());
		holder.count.setText(object.getCount());
	
		if (holder.childListView != null) {
			ChildAdapter childAdapter = new ChildAdapter(object
					.getChildren(), position);
			holder.childListView.setAdapter(childAdapter);
			
			holder.childListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
//					Intent activity = new Intent(mContext, AppDetailActivity.class);
//					activity.putExtra(AppDetailActivity.EXTRA_PACKGENAME, object.getChildren().get(arg2).getPgkName());
//					mContext.startActivity(activity);
				}
			});
			final ListView lv = holder.childListView;
		
			holder.relative.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (lv.getVisibility() == View.GONE) {
						expand(lv);
					} else {
						collapse(lv);
					}
				}
			});
			//更新子视图
			childAdapter.notifyDataSetChanged();
		}
		// final View view = convertView;
		// convertView.setLayoutParams(new ListView.LayoutParams(
		// AbsListView.LayoutParams.MATCH_PARENT,
		// AbsListView.LayoutParams.WRAP_CONTENT));

		return convertView;
	}

	private class ChildAdapter extends BaseAdapter {

		List<OptimizeChildItem> children;
		private int groupPosition;

		public ChildAdapter(List<OptimizeChildItem> children, int groupPosition) {
			this.children = children;
			this.groupPosition = groupPosition;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return children.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return children.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolderChild holder = null;
			if (convertView == null) {
				holder = new ViewHolderChild();
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.main_list_view_item_child_view, parent, false);
				holder.icon = (ImageView) convertView
						.findViewById(R.id.main_list_view_item_child_icon);
				holder.name = (TextView) convertView
						.findViewById(R.id.main_list_view_item_child_appname);
				holder.desc = (TextView) convertView
						.findViewById(R.id.main_list_view_item_child_desc);
				holder.percent = (TextView) convertView
						.findViewById(R.id.main_list_view_item_child_percent);
				holder.checkBox = (ImageView) convertView
						.findViewById(R.id.main_list_view_item_child_checkbox);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolderChild) convertView.getTag();
			}

			final OptimizeChildItem item = this.children.get(position);
			holder.icon.setImageDrawable(item.getAppIcon());
			holder.name.setText(item.getAppName());
			holder.desc.setText(item.getAppDesc());
			holder.percent.setText(item.getData());
			holder.checkBox.setBackgroundResource(R.drawable.ic_open);
			holder.checkBox.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(final View v1) {
					String title = "";
					String msg = "";
					Resources res = mContext.getResources();
					String confirMsg = res
							.getString(R.string.app_data_battery_btn_turn_off_tips_text);
					switch (item.getType()) {
					case OptimizeChildItem.TYPE_BATTERY:
						title = res.getString(R.string.dialog_autostart_off_title);
						msg = res
								.getString(R.string.app_auto_start_turn_off_guide_text);

						break;
					case OptimizeChildItem.TYPE_DATA:
						title = res.getString(R.string.dialog_mobiledata_off_title);
						msg = res
								.getString(R.string.app_data_access_turn_off_guide_text);
						break;

					default:
						break;
					}

					UIHelper.showCustomDialog(mContext, title, msg, confirMsg,
							null, new OnClickListener() {
								@Override
								public void onClick(View v) {
									Message msg = handler
											.obtainMessage(MainActivity.MSG_SHUTDOWN_AUTOSTART);
									msg.obj = item;
									handler.sendMessage(msg);
									((ImageView) v1)
											.setBackgroundResource(R.drawable.ic_closed);
									remove(groupPosition, position);
								}
							});
				}
			});

			return convertView;

		}

	}

	/**
	 * 移除某个子项
	 * 
	 * @param groupPosition
	 * @param childPosition
	 */
	public void remove(int groupPosition, int childPosition) {
		ExpandableListItem groupItem = this.parent.get(groupPosition);
		groupItem.getChildren().remove(childPosition);
		// 若没有子项了，父项也移除叼
		if (groupItem.isCanExpand() && groupItem.getChildren().size() <= 0) {
			this.parent.remove(groupPosition);
		}
		this.notifyDataSetChanged();
	}

	private class ViewHolder {
		ImageView statusIcon, loadIcon;
		TextView desc, count;
		RelativeLayout relative;
		ListView childListView;

	}

	private class ViewHolderChild {
		ImageView icon;
		TextView name, desc, percent;
		ImageView checkBox;
	}

	private void expand(final View v) {
		int width = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);

		int height = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		v.measure(width, height);
		// v.measure(LayoutParams.MATCH_PARENT, DensityUtil.dip2px(this, 70));
		final int targetHeight = v.getMeasuredHeight();
		v.getLayoutParams().height = 0;
		v.setVisibility(View.VISIBLE);

		Animation animation = new Animation() {
			@Override
			protected void applyTransformation(float interpolatedTime,
					Transformation t) {
				if (interpolatedTime == 1) {
					// isOpened = true;
				}

				v.getLayoutParams().height = (interpolatedTime == 1) ? LayoutParams.WRAP_CONTENT
						: (int) (targetHeight * interpolatedTime);
				v.requestLayout();
			}

			@Override
			public boolean willChangeBounds() {
				return true;
			}
		};
		animation.setDuration(300);
		v.startAnimation(animation);
	}

	private void collapse(final View v) {
		final int initialHeight = v.getMeasuredHeight();
		Animation animation = new Animation() {
			@Override
			protected void applyTransformation(float interpolatedTime,
					Transformation t) {
				if (interpolatedTime == 1) {
					v.setVisibility(View.GONE);
					// isOpened = false;
				} else {
					v.getLayoutParams().height = initialHeight
							- (int) (initialHeight * interpolatedTime);
					v.requestLayout();
				}
			}

			@Override
			public boolean willChangeBounds() {
				return true;
			}
		};

		animation.setDuration(300);
		v.startAnimation(animation);
	}

}
