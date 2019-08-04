package com.beem.project.btf.ui.fragment;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.beem.project.btf.ui.adapter.BaseCameraImageAdapter;
import com.beem.project.btf.ui.adapter.TimeCameraImageAdapter;
import com.beem.project.btf.ui.adapter.TimeCameraImageManageAdapter;
import com.beem.project.btf.ui.entity.TimeCameraImageInfo;
import com.beem.project.btf.utils.TimeCameraMaterialUtil;
import com.teleca.jamendo.api.WSError;

/**
 * @ClassName: TimeCameraImageFragement
 * @Description: 时光相机素材Fragment
 * @author: yuedong bao
 * @date: 2015-11-17 上午10:20:52
 */
public class TimeCameraImageFragement extends
		BaseImageGridFragment<TimeCameraImageInfo> {
	public enum DecadeType implements IMaterialType {
		sixty("60"), eighty("80"), thirty("30");
		private String groupid;

		private DecadeType(String num) {
			this.groupid = num;
		}
		@Override
		public String getGroupId() {
			return groupid;
		}
		@Override
		public String getTitle() {
			return groupid + "年代";
		}
		@Override
		public int getColumn() {
			return values().length;
		}
		@Override
		public int getIndex() {
			return ordinal();
		}
		@Override
		public IMaterialType[] getAllMembers() {
			return values();
		}
		@Override
		public Fragment getFragment(ActivityType actType) {
			Fragment fragment = TimeCameraImageFragement.newInstance(this,
					actType);
			return fragment;
		}
		@Override
		public String getLoadFailedMessage(ActivityType actType) {
			if (actType == ActivityType.Manage) {
				return "此年代还未下载素材，赶紧去下载哟";
			} else {
				return "暂无数据";
			}
		}
		@Override
		public IMaterialType getMaterialType(String groupId) {
			IMaterialType retVal = null;
			for (IMaterialType type : getAllMembers()) {
				if (type.getGroupId().equals(groupId)) {
					retVal = type;
					break;
				}
			}
			return retVal;
		}
		public static IMaterialType getMaterialTypeEx(String groupId) {
			IMaterialType retVal = null;
			for (IMaterialType type : thirty.getAllMembers()) {
				if (type.getGroupId().equals(groupId)) {
					retVal = type;
					break;
				}
			}
			return retVal;
		}
		@Override
		public int getNumColumns() {
			return 2;
		}
	}

	//newInstance
	public static BaseImageGridFragment<TimeCameraImageInfo> newInstance(
			IMaterialType materialType, ActivityType activityType) {
		BaseImageGridFragment<TimeCameraImageInfo> newFragment = new TimeCameraImageFragement();
		Bundle bundle = new Bundle();
		bundle.putSerializable("MaterialType", materialType);
		bundle.putSerializable("ActivityType", activityType);
		newFragment.setArguments(bundle);
		return newFragment;
	}
	@Override
	public List<TimeCameraImageInfo> queryManageMaterialDB(String model) {
		List<TimeCameraImageInfo> imageurls = TimeCameraImageInfo
				.queryWebDownload(model);
		return imageurls;
	}
	@Override
	public List<TimeCameraImageInfo> queryShowMaterialDB(String model) {
		List<TimeCameraImageInfo> imageurls = TimeCameraImageInfo
				.queryAll(model);
		return imageurls;
	}
	@Override
	public BaseCameraImageAdapter<TimeCameraImageInfo> createAdapter(
			BaseImageGridFragment.ActivityType type) {
		BaseCameraImageAdapter<TimeCameraImageInfo> retVal;
		if (type == ActivityType.Manage) {
			retVal = new TimeCameraImageManageAdapter(imageurls, mContext);
		} else {
			retVal = new TimeCameraImageAdapter(imageurls, mContext);
		}
		return retVal;
	}
	@Override
	public void requestNetAndSaveDB(String groupid) throws WSError {
		//请求网络
		String response = TimeCameraMaterialUtil.requestNetwork(groupid);
		//保存数据库
		TimeCameraMaterialUtil.SaveToDB(response, false);
	}
	@Override
	public List<TimeCameraImageInfo> onPullUpToRefresh() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<TimeCameraImageInfo> onPullDownToRefresh() {
		// TODO Auto-generated method stub
		return null;
	}
}
