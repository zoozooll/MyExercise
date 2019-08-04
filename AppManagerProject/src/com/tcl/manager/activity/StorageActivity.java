package com.tcl.manager.activity;

import java.math.BigDecimal;
import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.tcl.manager.adapter.BaseListAdapter; 
import com.tcl.manager.adapter.StorageAdapter;
import com.tcl.manager.application.ManagerApplication;
import com.tcl.manager.score.PageDataProvider; 
import com.tcl.manager.score.PageInfo;
import com.tcl.manager.util.AndroidUtil;
import com.tcl.mie.manager.R;
  

/** 
 * @Description: 
 * @author pengcheng.zhang 
 * @date 2014-12-31 下午6:05:06 
 * @copyright TCL-MIE
 */
public class StorageActivity extends BaseAppListActivity
{
	@Override
	protected String getHeaderTitle()
	{ 
		return getString(R.string.app_list_storage_title_text);
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
					if ((lhs.appSize + lhs.appDataSize) > (rhs.appSize + rhs.appDataSize))
					{
						return -1;
					}
					
					if ((lhs.appSize + lhs.appDataSize) < (rhs.appSize + rhs.appDataSize))
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
		return new StorageAdapter(this);
	} 

	@Override
	public String getDescription(List<PageInfo> list)
	{ 
		BigDecimal size = new BigDecimal(0L);
		for (PageInfo info : list)
		{ 
			size = size.add(BigDecimal.valueOf(info.appSize));
			size = size.add(BigDecimal.valueOf(info.appDataSize));
		}  
		
		return String.format(getResources().getString(R.string.app_list_storage_occupy_guide_text), AndroidUtil.formatBigSize(size), String.valueOf(list.size()));
	} 
}
