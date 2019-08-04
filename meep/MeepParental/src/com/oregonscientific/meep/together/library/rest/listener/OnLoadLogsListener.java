package com.oregonscientific.meep.together.library.rest.listener;

import com.oregonscientific.meep.together.bean.ResponseBasic;
import com.oregonscientific.meep.together.bean.ResponseLoadLogs;

public interface OnLoadLogsListener {
	public void onLoadLogsSuccess(ResponseLoadLogs infoLogs);
	public void onLoadLogsFailure(ResponseBasic r);
}
