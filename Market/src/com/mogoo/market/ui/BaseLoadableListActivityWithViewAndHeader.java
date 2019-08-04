package com.mogoo.market.ui;

import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;

import com.mogoo.market.R;
import com.mogoo.market.widget.SegmentedButton;

/**
 * This is pretty much a direct copy of LoadableListActivity. It just gives the
 * caller a chance to set their own view for the empty state. This is used by
 * FriendsActivity to show a button like 'Find some friends!' when the list is
 * empty (in the case that they are a new user and have no friends initially).
 * 
 * By default, loadable_list_activity_with_view is used as the intial empty view
 * with a progress bar and textview description. The owner can then call
 * setEmptyView() with their own view to show if there are no results.
 * 
 * @date April 25, 2010
 * @author Mark Wyszomierski (markww@gmail.com)
 */
public class BaseLoadableListActivityWithViewAndHeader extends BaseListActivity {

	private SegmentedButton mHeaderButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setBaseTopLayout(R.layout.base_top_layout);

		mHeaderButton = (SegmentedButton) findViewById(R.id.segmented);

		initHeaderButton();

	}

	/**
	 * 设置顶部3个按钮的宽度，在activitygroup中会出现width不全屏的情况，要手动设置
	 */
	private void initHeaderButton() {
		LayoutParams params = mHeaderButton.getLayoutParams();
		params.width = LayoutParams.FILL_PARENT;
		mHeaderButton.setLayoutParams(params);
	}

	public SegmentedButton getHeaderButton() {
		return mHeaderButton;
	}
}
