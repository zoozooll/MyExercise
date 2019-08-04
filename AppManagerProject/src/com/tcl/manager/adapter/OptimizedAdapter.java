/**
 * 
 */
package com.tcl.manager.adapter;

import java.util.List;

import com.tcl.framework.log.NLog;
import com.tcl.manager.activity.AppDetailActivity;
import com.tcl.manager.base.OptimizedAppInfo;
import com.tcl.manager.score.PageFunctionProvider;
import com.tcl.manager.view.UIHelper;
import com.tcl.mie.manager.R;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

/**
 * @author zuokang.li
 * 
 */
public class OptimizedAdapter extends BaseExpandableListAdapter
{
	private static final int				POSITION_BATTERY	= 0;
	private static final int				POSITION_DATA		= 1;

	private Context							mContext;
	private LayoutInflater					mInflater;
	private Resources						mRes;
	private List<List<OptimizedAppInfo>>	data;

	public OptimizedAdapter(Context context, List<List<OptimizedAppInfo>> data)
	{
		mContext = context;
		this.data = data;
		mInflater = LayoutInflater.from(context);
		mRes = context.getApplicationContext().getResources();
	}

	public void refreshData(List<List<OptimizedAppInfo>> data)
	{
		this.data = data;
		notifyDataSetChanged();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#getGroupCount()
	 */
	@Override
	public int getGroupCount()
	{
		if (data != null)
		{
			return data.size();
		}
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#getChildrenCount(int)
	 */ 
	public int getChildrenCount(int groupPosition) {
		if (getGroup(groupPosition) != null) {
			return getGroup(groupPosition).size(); 
		}
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#getGroup(int)
	 */
	@Override
	public List<OptimizedAppInfo> getGroup(int groupPosition)
	{
		if (data != null)
		{
			return data.get(groupPosition);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#getChild(int, int)
	 */
	@Override
	public OptimizedAppInfo getChild(int groupPosition, int childPosition)
	{
		if (getGroup(groupPosition) != null && getChildrenCount(groupPosition) > childPosition)
		{
			return getGroup(groupPosition).get(childPosition);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#getGroupId(int)
	 */
	@Override
	public long getGroupId(int groupPosition)
	{
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#getChildId(int, int)
	 */
	@Override
	public long getChildId(int groupPosition, int childPosition)
	{
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#hasStableIds()
	 */
	@Override
	public boolean hasStableIds()
	{
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#getGroupView(int, boolean,
	 * android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
	{
		GroupHolder viewHolder = null;

		if (convertView == null)
		{
			viewHolder = new GroupHolder();

			convertView = mInflater.inflate(R.layout.optimized_groupview, null);
			viewHolder.titleView = (TextView) convertView.findViewById(R.id.textOptimizedGroupTitle);
//			viewHolder.contentView = (TextView) convertView.findViewById(R.id.textOptimizedGroupContent);

			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (GroupHolder) convertView.getTag();
		}

		switch (groupPosition)
		{
			case POSITION_BATTERY:
			{
				String title = mContext.getResources().getString(R.string.optimized_title_battery, getChildrenCount(groupPosition));
				viewHolder.titleView.setText(title);
//				viewHolder.contentView.setText(R.string.optimized_label_battery);
			}
				break;
			case POSITION_DATA:
			{
				String title = mContext.getResources().getString(R.string.optimized_title_data, getChildrenCount(groupPosition));
				viewHolder.titleView.setText(title);
//				viewHolder.contentView.setText(R.string.optimized_label_data);

			}
				break;

			default:
				break;
		}
		return convertView;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#getChildView(int, int, boolean,
	 * android.view.View, android.view.ViewGroup)
	 */
	@Override 
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		
		ChildHolder viewHolder = null;  
	      
	    if (convertView == null) {  
	        viewHolder = new ChildHolder();  
	          
	        convertView = mInflater.inflate(R.layout.optimized_childview, null);  
	        viewHolder.imvIcon = (ImageView) convertView.findViewById(R.id.imageIcon);  
	        viewHolder.textName = (TextView) convertView.findViewById(R.id.textTitle);  
	        viewHolder.textState = (TextView) convertView.findViewById(R.id.textState);
	        viewHolder.switcher = (ImageView) convertView.findViewById(R.id.btn_switch);
	        
	        //viewHolder.eventDetail = new JumpToDetailEvent();
	        viewHolder.eventSwitch = new TurnSwitchEvent();
	        convertView.setTag(viewHolder);  
	    } else {  
	        viewHolder = (ChildHolder) convertView.getTag();  
	    }  
	    OptimizedAppInfo item = getChild(groupPosition, childPosition);
	    if (item.icon != null) {
	    	viewHolder.imvIcon.setImageDrawable(item.icon);
	    }
	    //viewHolder.eventDetail.packageName = item.pkgName;
	    //viewHolder.imvIcon.setOnClickListener(viewHolder.eventDetail);
	    viewHolder.textName.setText(item.appName);
	    viewHolder.textState.setText(item.onOff ? R.string.optimized_switch_off : R.string.optimized_switch_on );
	    viewHolder.switcher.setImageResource(item.onOff ? R.drawable.ic_closed : R.drawable.ic_open);
	    
	    viewHolder.eventSwitch.groupIndex = groupPosition;
	    viewHolder.eventSwitch.itemInfo = item;
	    viewHolder.switcher.setOnClickListener(viewHolder.eventSwitch);
		return convertView;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#isChildSelectable(int, int)
	 */
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition)
	{
		// TODO Auto-generated method stub
		return true;
	}
	

	private static class GroupHolder
	{
		private TextView	titleView;
//		private TextView	contentView;
	}

	private static class ChildHolder
	{
		private ImageView	imvIcon;
		private TextView	textName;
		private TextView	textState;
		private ImageView	switcher;
		
		//private JumpToDetailEvent eventDetail;
		private TurnSwitchEvent eventSwitch;
	}

	
	private class JumpToDetailEvent implements OnClickListener{
		
		private String packageName;

		@Override
		public void onClick(View v) {
			Intent activity = new Intent(mContext, AppDetailActivity.class);
			activity.putExtra(AppDetailActivity.EXTRA_PACKGENAME, packageName);
			mContext.startActivity(activity);
		}
		
	}
	
	private class TurnSwitchEvent implements OnClickListener {
		private int groupIndex    ;
		private OptimizedAppInfo itemInfo;

		@Override
		public void onClick(View v) {
//			if (!itemInfo.onOff) {
				showDialog(groupIndex, itemInfo);
			/*} else {
				// If it turn to CHECKED, it is no need to confirm in a dialog;
				switch (groupIndex) {
				case POSITION_BATTERY: {
					PageFunctionProvider.setAutoStart(itemInfo.pkgName, true);
				}
					break;
				case POSITION_DATA: {
					PageFunctionProvider.setDataAccess(itemInfo.pkgName, true);
				}
					break;
				default:
					break;
				}
				new RunProviderTask().execute(groupIndex, itemInfo);
			}*/
			
		}
		
	}
	
	private void showDialog(final int groupIndex, final OptimizedAppInfo itemInfo) {
		// If it turn to UNCHECKED it would need to confirm in a dialog;
		int titleRes = -1, msgRes = -1, chooseRes = -1;
		switch (groupIndex) {
		case 0:
			titleRes = itemInfo.onOff ? R.string.dialog_autostart_on_title : R.string.dialog_autostart_off_title;
			msgRes = itemInfo.onOff ? R.string.app_auto_start_turn_on_guide_text : R.string.app_auto_start_turn_off_guide_text;
			break;
		case 1:
			titleRes = itemInfo.onOff ? R.string.dialog_mobiledata_on_title : R.string.dialog_mobiledata_off_title;
			msgRes = itemInfo.onOff ? R.string.app_data_access_turn_on_guide_text : R.string.app_data_access_turn_off_guide_text;
			break;
			
		default:
			break;
		}
		chooseRes = itemInfo.onOff ? R.string.app_data_battery_btn_turn_on_tips_text : R.string.app_data_battery_btn_turn_off_tips_text;
		String title = mRes.getString(titleRes);
		String msg = mRes.getString(msgRes);
		String choose = mRes.getString(chooseRes);
		View.OnClickListener l = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.dialog_left:
					break;
				case R.id.dialog_right: {
					//new RunProviderTask().execute(groupIndex, itemInfo);
					switch (groupIndex) {
					case POSITION_BATTERY: {
						PageFunctionProvider.setAutoStart(itemInfo.pkgName, itemInfo.onOff);
					}
						break;
					case POSITION_DATA: {
						PageFunctionProvider.setDataAccess(itemInfo.pkgName, itemInfo.onOff);
					}
						break;
					default:
						break;
					}
					itemInfo.onOff = !itemInfo.onOff;
					notifyDataSetChanged();
					
				}
					break;
				default:
					break;
				}
				
			}
		};
		UIHelper.showCustomDialog(mContext, title, msg, choose, null, null, l, l);
	}
	
	/*private class RunProviderTask extends AsyncTask<Object, Void, Boolean> {
		
		private OptimizedAppInfo itemInfo;

		@Override
		protected Boolean doInBackground(Object... params) {
			if (params == null || params.length < 2) {
				return false;
			}
			int groupIndex = (Integer) params[0];
			itemInfo = (OptimizedAppInfo) params[1];
			switch (groupIndex) {
			case POSITION_BATTERY: {
				PageFunctionProvider.setAutoStart(itemInfo.pkgName, itemInfo.onOff);
			}
				break;
			case POSITION_DATA: {
				PageFunctionProvider.setDataAccess(itemInfo.pkgName, itemInfo.onOff);
			}
				break;
			default:
				break;
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean success) {
			if (success) {
				itemInfo.onOff = !itemInfo.onOff;
				notifyDataSetChanged();
			} 
		}
		
	}*/

}
