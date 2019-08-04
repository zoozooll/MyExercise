package com.teleca.jamendo.dialog;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;

import com.butterfly.vv.camera.Utils;
import com.butterfly.vv.db.ormhelper.bean.ImageFolder;
import com.butterfly.vv.model.ImageFolderItem;
import com.beem.project.btf.manager.ImageFolderItemManager;
import com.beem.project.btf.ui.dialog.VVBaseLoadingDlg;
import com.beem.project.btf.utils.BBSUtils;
import com.beem.project.btf.utils.ThreadUtils;
import com.teleca.jamendo.api.WSError;

/**
 * 加载时光图片组详情
 * @author yuedong bao
 */
public class GetPhotoGroupDetailDlg extends VVBaseLoadingDlg<List<ImageFolderItem>> {
	private onGetPGDetailResult onGetPGDetailLis;
	private final List<ImageFolder> folderInfos;
	private final String startTime;
	private List<ImageFolderItem> tImageFolderItems = new ArrayList<ImageFolderItem>();

	/**
	 * down是下拉刷新，up是分页加载,itemclick是点击了侧边选项
	 */
	public enum Direction {
		down, up, itemclick
	}

	public interface onGetPGDetailResult {
		public void onResult(List<ImageFolderItem> list, boolean isTimeout,
				String startTime);
	}

	public GetPhotoGroupDetailDlg(Activity activity,
			List<ImageFolder> folderInfos,
			onGetPGDetailResult onGetPGDetailLis, String startTime) {
		super(new VVBaseLoadingDlgCfg(activity));
		this.onGetPGDetailLis = onGetPGDetailLis;
		this.folderInfos = folderInfos;
		this.startTime = startTime;
	}
	@Override
	public List<ImageFolderItem> doInBackground() {
//		long t1 = System.currentTimeMillis();
//		List<Runnable> tasks = new ArrayList<Runnable>();
		/*for (final ImageFolder imgeGroupOne : folderInfos) {
			tasks.add(new Runnable() {
				@Override
				public void run() {
					ImageFolderItem folderItem;
					try {
						folderItem = ImageFolderItemManager.getInstance()
								.getImageFolderItem(imgeGroupOne.getJid(),
										imgeGroupOne.getGid(),
										imgeGroupOne.getCreateTime());
						if (folderItem != null) {
							synchronized (tImageFolderItems) {
								tImageFolderItems.add(folderItem);
							}
						} else {
							//LogUtils.e("Error:This shouldn't be there:get photoDetail is null,jid:" + imgeGroupOne.getJid()
							//								+ " gid: " + imgeGroupOne.getGid() + " crateTime :" + imgeGroupOne.getCreateTime());
						}
					} catch (WSError e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		}*/
			for (final ImageFolder imgeGroupOne : folderInfos) {
				ImageFolderItem folderItem  = null;
				try {
					folderItem = ImageFolderItemManager
						.getInstance().getImageFolderItem(
								imgeGroupOne.getJid(),
								imgeGroupOne.getGid(),
								imgeGroupOne.getCreateTime());
				} catch (WSError e) {
					setManulaTimeOut(true);
					e.printStackTrace();
					return null;
				}
				if (folderItem != null) tImageFolderItems.add(folderItem);
			}
//		ThreadUtils.executeDivideTasks(tasks);
		//LogUtils.i("getPhotoDetail_cost_time:" + (System.currentTimeMillis() - t1));
		return tImageFolderItems;
	}
	public onGetPGDetailResult getOnGetPGDetailLis() {
		return onGetPGDetailLis;
	}
	public void setOnGetPGDetailLis(onGetPGDetailResult onGetPGDetailLis) {
		this.onGetPGDetailLis = onGetPGDetailLis;
	}
	@Override
	protected void onPostExecute(List<ImageFolderItem> result) {
		super.onPostExecute(result);
		if (onGetPGDetailLis != null) {
			onGetPGDetailLis.onResult(result, false, startTime);
		}
	}
	@Override
	protected void onTimeOut() {
		if (onGetPGDetailLis != null) {
			onGetPGDetailLis.onResult(null, true, startTime);
		}
	}
}
