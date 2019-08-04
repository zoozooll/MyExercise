package com.butterfly.vv.adapter;

import java.util.Collection;
import java.util.List;

import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.beem.project.btf.BeemApplication;
import com.butterfly.vv.model.Start;

/**
 * @ClassName: CommonPhotoAdapter
 * @Description: BaseAdapter通用类
 * @author: yuedong bao
 * @date: 2015-3-9 上午9:38:19
 */
public abstract class CommonPhotoAdapter<Input> extends BaseAdapter {
	// 记录当前Adapter数据状态,表示是否从服务器或者离线包取过数据了
	private DataStatus status = DataStatus.empty;
	private List<Input> items;
	private final Start nextOnline;
	private final Start nextOffline;
	private final AdapterView<?> listView;

	protected CommonPhotoAdapter(AdapterView<?> listView) {
		super();
		this.items = initItems();
		this.nextOnline = new Start(null);
		this.nextOffline = new Start(null);
		this.listView = listView;
	}
	public abstract List<Input> initItems();
	public final void clearItems() {
		items.clear();
	}
	public void addItems(Collection<Input> inputs) {
		for (Input input : inputs) {
			addItem(input);
		}
	}
	public void setItems(Collection<Input> inputs) {
		clearItems();
		items.addAll(inputs);
	}
	@Override
	public final Input getItem(int index) {
		return items.get(index);
	}
	public final List<Input> getItems() {
		return items;
	}
	public void addItem(Input input) {
		items.add(input);
	}
	public final void setItem(int index, Input input) {
		items.set(index, input);
	}
	public void removeItem(Input input) {
		items.remove(input);
	}
	public void removeItems(List<Input> inputs) {
		items.removeAll(inputs);
	}
	public void removeItem(int index) {
		items.remove(index);
	}
	public DataStatus getStatus() {
		return status;
	}
	public void setStatus(DataStatus status) {
		this.status = status;
	}

	public enum DataStatus {
		empty, offline, memory;
	}

	@Override
	public final int getCount() {
		return items.size();
	}
	public Start getStart() {
		if (!BeemApplication.isNetworkOk())
			return nextOffline;
		else
			return nextOnline;
	}
	/**
	 * @Title: updateView
	 * @Description: listView局部刷新
	 * @param: @param position
	 * @return: void
	 * @throws:
	 */
	public final void updateSingleRow(int position) {
		/*int visiblePos = listView.getFirstVisiblePosition();
		int offset = position - visiblePos;
		if (listView instanceof ListView) {
			// 注意listview这些有头部的
			offset = position + ((ListView) listView).getHeaderViewsCount() - visiblePos;
		}
		// 只有在可见区域才更新
		if (offset >= 0) {
			View view = listView.getChildAt(offset);
			getView(position, view, listView);
		}*/
		notifyDataSetChanged();
	}
	/**
	 * @Title: getSingRowView
	 * @Description: 获取单个SingleRow中的View
	 * @param: @param position
	 * @param: @return
	 * @return: 不可见position返回null，否则返回该position对应View
	 * @throws:
	 */
	public final View getSingRowView(int position) {
		int visiblePos = listView.getFirstVisiblePosition();
		int offset = position - visiblePos;
		if (listView instanceof ListView) {
			// 注意listview这些有头部的
			offset = position + ((ListView) listView).getHeaderViewsCount()
					- visiblePos;
		}
		// 只有在可见区域才更新
		if (offset >= 0) {
			View view = listView.getChildAt(offset);
			return view;
		}
		return null;
	}
}
