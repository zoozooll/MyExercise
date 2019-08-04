package com.beem.project.btf.ui.dialog;

import java.util.List;

import android.content.Context;

import com.butterfly.vv.model.ImageFolderItem;
import com.butterfly.vv.model.Start;
import com.butterfly.vv.service.ContactService;
import com.butterfly.vv.service.ShareTranceService;
import com.beem.project.btf.BeemApplication;
import com.pullToRefresh.ui.PullToRefreshBase.PullType;
import com.teleca.jamendo.api.WSError;
import com.teleca.jamendo.api.WSError.Type;

/**
 * @func 排名脚印获取数据dialog
 * @author yuedong bao
 * @date 2015-2-10 下午4:40:58
 */
public class ShareRankingFootPrintLoadingDialog extends
		VVBaseLoadingDlg<List<ImageFolderItem>> {
	private Start start;
	private int limit;
	private ShareType shareType;
	private IShareRstLis shareRstLis;
	private PullType pullType;

	// 脚印 排名
	public ShareRankingFootPrintLoadingDialog(Context ctx, ShareType shareType,
			Start start, IShareRstLis shareRstLis, PullType pullType) {
		super(new VVBaseLoadingDlgCfg(ctx).setBindXmpp(false));
		this.limit = 4;
		this.start = start;
		this.shareType = shareType;
		this.shareRstLis = shareRstLis;
		this.pullType = pullType;
	}
	@Override
	protected List<ImageFolderItem> doInBackground() {
		List<ImageFolderItem> retVal = null;
		try {
			retVal = ShareTranceService.getFootprintOrTopN(start, limit,
					!BeemApplication.isNetworkOk(), shareType);
		} catch (WSError e) {
			e.printStackTrace();
			setManulaTimeOut(e.type == Type.SocketTimeoutException
					|| e.type == Type.ConnectTimeoutException);
		}
		return retVal;
	}
	@Override
	protected void onPostExecute(List<ImageFolderItem> result) {
		if (shareRstLis != null) {
			shareRstLis.onShareResult(result, shareType, start, pullType);
		}
	}

	public interface IShareRstLis {
		public void onShareResult(List<ImageFolderItem> items,
				ShareType shareType, Start start, PullType pullType);
		public void onTimeOut(ShareType shareType);
	}

	public enum ShareType {
		TopN, FootPrint;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		//		if (shareType == ShareType.FootPrint) {
		//			ContactService.getInstance().synGeoInfo();
		//		}
	}
	@Override
	protected void onTimeOut() {
		super.onTimeOut();
		if (shareRstLis != null) {
			shareRstLis.onTimeOut(shareType);
		}
	}
}
