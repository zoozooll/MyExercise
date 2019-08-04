package com.oregonscientific.meep.notification.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.oregonscientific.meep.notification.Notification;
import com.oregonscientific.meep.notification.view.SwipeDismissListViewTouchListener.OnDismissCallback;

public class MessageListView extends ListView {
	
	private OnListItemRemoveListener onListItemRemoveListener;
	
	public MessageListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		SwipeDismissListViewTouchListener touchListener = new SwipeDismissListViewTouchListener (this, new OnDismissCallback(){

			@Override
			public void onDismiss(ListView listView, int[] reverseSortedPositions) {
				@SuppressWarnings("unchecked")
				ArrayAdapter<Notification> adapter = (ArrayAdapter<Notification>) listView.getAdapter();
				// Cannot continue if the list view does not has an adapter
				if (adapter == null) {
					return;
				}
				
				for (int position : reverseSortedPositions) {
					if (position < adapter.getCount() && adapter.getItem(position) != null) {
						if (getOnListItemRemoveListener() != null) {
							long id = adapter.getItem(position).id;
    						getOnListItemRemoveListener().onRemove(id);
    					}
                    	adapter.remove(adapter.getItem(position));
					}
				}
			}
		
		});
		setOnTouchListener(touchListener);
		setOnScrollListener(touchListener.makeScrollListener());
		
	}
	
	/**
	 * @return the onListItemRemoveListener
	 */
	public OnListItemRemoveListener getOnListItemRemoveListener() {
		return onListItemRemoveListener;
	}

	/**
	 * @param onListItemRemoveListener the onListItemRemoveListener to set
	 */
	public void setOnListItemRemoveListener(OnListItemRemoveListener onListItemRemoveListener) {
		this.onListItemRemoveListener = onListItemRemoveListener;
	}


	@SuppressWarnings("unchecked")
	public void removeAllItems() {
		// can not continue if can not get the adapter
		if (getAdapter() == null) {
			return;
		}
		((ArrayAdapter<Notification>) getAdapter()).clear();
		((ArrayAdapter<Notification>) getAdapter()).notifyDataSetChanged();
		if (getOnListItemRemoveListener() != null) {
			getOnListItemRemoveListener().onRemoveAll();
		}
	}

}
