/**
 * new adapter for snack view items.
 */
package com.oregonscientific.meep.opengl;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.oregonscientific.meep.message.common.OsListViewItem;

/**
 * @author aaronli
 *
 */
public abstract class BaseSnakeAdapter implements SnakeAdapter<OsListViewItem> {
		
		protected List<OsListViewItem> data;

		public BaseSnakeAdapter(List<OsListViewItem> data) {
			this.data = data;
		}
		
		// modified by aaronli at May30 2013.add 15 empty view for filling the snack showing
		@Override
		public int getCount() {
			
			if (!isEmpty()) {
				return data.size() + 15;
			}
			return 15;
		}

		@Override
		public OsListViewItem getItem(int position) {
			if (!isEmpty() && position < data.size()) {
				return data.get(position);
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public OSButton getView(int position, OSButton converView) {
			OSButton view;
				
			if (converView == null) {
				view = new OSButton();
			} else {
				view = converView;
			}
			if (!isEmpty() && position < data.size()) {
				final OsListViewItem item = data.get(position);
				final List<String> contentPath = new ArrayList<String>();
				contentPath.add(item.getPath());
				view.setName(item.getName());
				view.setContentPath(contentPath);
			} else {
				view.setName(null);
				view.setContentPath(null);
			}
			return view;
		}
		// end

		@Override
		public boolean isEmpty() {
			return data == null || data.size() == 0;
		}
		
		public abstract void onDelectedItem(OsListViewItem deleteItem);
		
		public abstract void onRenameItem(OsListViewItem deleteItem);

}