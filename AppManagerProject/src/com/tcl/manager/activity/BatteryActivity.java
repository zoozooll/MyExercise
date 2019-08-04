package com.tcl.manager.activity;
 
import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.tcl.manager.adapter.BaseListAdapter; 
import com.tcl.manager.adapter.BatteryAdapter; 
import com.tcl.manager.application.ManagerApplication;
import com.tcl.manager.score.PageDataProvider;
import com.tcl.manager.score.PageInfo; 
import com.tcl.mie.manager.R; 


/** 
 * @Description: 
 * @author pengcheng.zhang 
 * @date 2014-12-31 下午6:05:06 
 * @copyright TCL-MIE
 */
public class BatteryActivity extends BaseAppListActivity
{

	@Override
	protected String getHeaderTitle()
	{ 
		return getString(R.string.app_list_battery_title_text);
	} 

	@Override
	public List<PageInfo> getPageInfoList()
	{ 
		try
		{
			List<PageInfo> list = PageDataProvider.getInstance().getAll();
			if (list == null || list.isEmpty())
			{
				return list;
			}
			
			Collections.sort(list, new Comparator<PageInfo>()
			{
				@Override
				public int compare(PageInfo lhs, PageInfo rhs)
				{
					if (lhs.batteryPercent > rhs.batteryPercent)
					{
						return -1;
					}
					
					if (lhs.batteryPercent < rhs.batteryPercent)
					{
						return 1;
					}
					
					if (lhs.appName != null && rhs.appName != null) 
					{
						Collator c = Collator.getInstance(ManagerApplication.sApplication. getResources().getConfiguration().locale);
						
						return c.compare(lhs.appName, rhs.appName);
					}
					
					return 0;
				}
			});
			
			return list;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		} 
	}

	@Override
	public BaseListAdapter<PageInfo> getAdapter()
	{ 
		return new BatteryAdapter(this);
	} 

	@Override
	public String getDescription(List<PageInfo> pageInfos)
	{
		int len = 0;
		for (PageInfo info : pageInfos)
		{
			if (info.isOpenAutoStart)
			{
				++ len;
			}
		}
		
		return String.format(getResources().getString(R.string.app_list_battery_run_guide_text), String.valueOf(len));
	}

}
