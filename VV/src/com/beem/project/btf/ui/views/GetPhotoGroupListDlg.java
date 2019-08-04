package com.beem.project.btf.ui.views;

import java.util.Map;

import org.jivesoftware.smack.util.StringUtils;

import android.app.Activity;

import com.beem.project.btf.manager.LoginManager;
import com.beem.project.btf.ui.dialog.VVBaseLoadingDlg;
import com.butterfly.vv.db.ormhelper.bean.ImageFolder;
import com.butterfly.vv.service.TimeflyService;

/**
 * @date 2013-11-19-下午3:11:36
 * @author yuedong bao
 * @func 时光界面侧滑处理类，不要拥有此类的引用
 */
public class GetPhotoGroupListDlg extends
		VVBaseLoadingDlg<Map<String, ImageFolder>> {
	private String query_jid;
	private OnGetPGListResult listener;

	public interface OnGetPGListResult {
		public void onResult(Map<String, ImageFolder> yearMap, boolean isTimeout);
	}

	public GetPhotoGroupListDlg(Activity activity, String query_jid,
			OnGetPGListResult listener) {
		super(new VVBaseLoadingDlgCfg(activity).setBindXmpp(true));
		this.query_jid = StringUtils.parseName(query_jid);
		this.listener = listener;
	}
	@Override
	public Map<String, ImageFolder> doInBackground() {
		String nowTime = LoginManager.getInstance().getSystemTime();
		Map<String, ImageFolder> yearMap = TimeflyService.getPhotoGroupList(
				query_jid, nowTime);
		if (LoginManager.getInstance().isMyJid(query_jid)) {
			TimeflyService.getPhotoGroupNotify();
		}
		return yearMap;
	}
	@Override
	protected void onPostExecute(Map<String, ImageFolder> result) {
		super.onPostExecute(result);
		if (listener != null) {
			listener.onResult(result, false);
		}
	}
	@Override
	protected void onTimeOut() {
		if (listener != null) {
			listener.onResult(null, true);
		}
	}
}
