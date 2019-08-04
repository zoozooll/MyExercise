package com.tcl.manager.activity.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tcl.manager.activity.MainActivity;
import com.tcl.manager.activity.entity.ExpandableListItem;
import com.tcl.manager.activity.entity.OptimizeChildItem;
import com.tcl.manager.util.MemoryInfoProvider;
import com.tcl.manager.view.UIHelper;
import com.tcl.mie.manager.R;

/**
 * @Description:
 * @author wenchao.zhang
 * @date 2014年12月30日 下午3:57:55
 * @copyright TCL-MIE
 */

public class MainExpandableAdapter extends BaseExpandableListAdapter {

	private List<ExpandableListItem> parent = new ArrayList<ExpandableListItem>();
	private Context mContext;
	private Handler handler;

	public MainExpandableAdapter(Context context, Handler handler) {
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
	public int getGroupType(int groupPosition) {
		// TODO Auto-generated method stub
		return parent.get(groupPosition).isCanExpand() ? IViewType.EXPANDABLE
				: IViewType.NORMAL;
	}

	@Override
	public int getGroupTypeCount() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return parent.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return parent.get(groupPosition).getChildren().size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return parent.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return parent.get(groupPosition).getChildren().get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		final ExpandableListItem object = this.parent.get(groupPosition);
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
			switch (getGroupType(groupPosition)) {
			case IViewType.NORMAL:
				convertView = inflater.inflate(R.layout.main_list_view_item_01,
						parent, false);
				holder.statusIcon = (ImageView) convertView
						.findViewById(R.id.main_list_view_item_icon);
				holder.desc = (TextView) convertView
						.findViewById(R.id.main_list_view_item_desc);
				holder.count = (TextView) convertView
						.findViewById(R.id.main_list_view_item_count);

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
				break;

			default:
				break;
			}
			if (convertView != null) convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.statusIcon.setImageResource(object.getImgResId());
		holder.desc.setText(object.getDesc());
		holder.count.setText(object.getCount());
		// final View view = convertView;
		// convertView.setLayoutParams(new ListView.LayoutParams(
		// AbsListView.LayoutParams.MATCH_PARENT,
		// AbsListView.LayoutParams.WRAP_CONTENT));

		return convertView;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
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

		final OptimizeChildItem item = this.parent.get(groupPosition)
				.getChildren().get(childPosition);
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
					title = res.getString(R.string.dialog_autostart_on_title);
					msg = res
							.getString(R.string.app_auto_start_turn_off_guide_text);

					break;
				case OptimizeChildItem.TYPE_DATA:
					title = res.getString(R.string.dialog_mobiledata_on_title);
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
								remove(groupPosition, childPosition);
							}
						});
			}
		});

		return convertView;
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

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

	private class ViewHolder {
		ImageView statusIcon;
		TextView desc, count;

	}

	private class ViewHolderChild {
		ImageView icon;
		TextView name, desc, percent;
		ImageView checkBox;
	}

}
