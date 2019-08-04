package com.tcl.manager.activity;
 
import java.math.BigDecimal;
import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.tcl.manager.adapter.BaseListAdapter; 
import com.tcl.manager.adapter.MemoryAdapter; 
import com.tcl.manager.adapter.OnAdapterItemListener;
import com.tcl.manager.application.ManagerApplication;
import com.tcl.manager.score.PageDataProvider;
import com.tcl.manager.score.PageInfo; 
import com.tcl.manager.util.AndroidUtil;
import com.tcl.manager.util.MemoryInfoProvider;
import com.tcl.mie.manager.R;
   
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener; 


/** 
 * @Description: 
 * @author pengcheng.zhang 
 * @date 2014-12-31 下午6:05:06 
 * @copyright TCL-MIE
 */
public class MemoryActivity extends BaseAppListActivity implements OnClickListener, OnAdapterItemListener<PageInfo>
{ 
	@Override
	protected String getHeaderTitle()
	{ 
		return getString(R.string.app_list_memory_title_text);
	}
	
	@Override
	public List<PageInfo> getPageInfoList() 
	{
		try
		{
			List<PageInfo> list = PageDataProvider.getInstance().getMemoryPageData();
			if (list == null || list.isEmpty())
			{
				return list;
			}
			
			Collections.sort(list, new Comparator<PageInfo>()
			{
				@Override
				public int compare(PageInfo lhs, PageInfo rhs)
				{ 
					if (lhs.memorySize > rhs.memorySize)
					{
						return -1;
					}
					
					if (lhs.memorySize < rhs.memorySize)
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
		BaseListAdapter<PageInfo> adapter = new MemoryAdapter(this);
		adapter.setOnAdapterItemListener(this);
		return adapter;
	} 

	@Override
	public String getDescription(List<PageInfo> list)
	{ 
		BigDecimal size = new BigDecimal(0L);
		
		for (PageInfo info : list)
		{ 
			size = size.add(BigDecimal.valueOf(info.memorySize));
		} 
		
		return String.format(getResources().getString(R.string.app_list_memory_clear_guide_text), 
			AndroidUtil.formatSize(MemoryInfoProvider.getInstance(this).availMem), 
			String.valueOf(list.size()), 
			AndroidUtil.formatBigSize(size));
	}

	@Override
	public void onItemChange(Object ...args)
	{ 
		try
		{
			Boolean flag1 = (Boolean)args[0];
			if (!flag1)
			{ 
				showProgress(R.color.dark);
				return;
			} 
			
			int flag2 = (Integer)args[1];  
			if (flag2 == 0)
			{
				updateView();
			}
			else if (flag2 == 1)
			{
				dimissProgress();
			}
			else if (flag2 == 2)
			{
				updateList();
			}
		}
		catch (Exception e)
		{ 
			e.printStackTrace();
		} 
	}

	@Override
	public void onItemLongClick(View view, PageInfo t, int position)
	{ 
		
	}
}
