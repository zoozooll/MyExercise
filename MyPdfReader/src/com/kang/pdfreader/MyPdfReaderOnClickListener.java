package com.kang.pdfreader;

import android.view.View;
import android.view.View.OnClickListener;

public class MyPdfReaderOnClickListener implements OnClickListener {
	
	private MyPdfReaderActivity activity;
	
	public MyPdfReaderOnClickListener(MyPdfReaderActivity activity) {
		super();
		this.activity = activity;
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnPreventPage:
			activity.gotoPreventpage();
			break;
			
		case R.id.btnNextPage:
			activity.gotoNextPage();
			break;
			
		case R.id.tvwPage:
			activity.showPageDialog();
			break;
			
		case R.id.tvwScalingPercent:
			activity.gotoShowScalingDialog();
			break;

		}
	}

}
