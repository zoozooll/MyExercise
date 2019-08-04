package com.beem.project.btf.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.beem.project.btf.ui.adapter.BaseCameraImageAdapter;
import com.beem.project.btf.ui.adapter.NewsCameraImageAdapter;
import com.beem.project.btf.ui.adapter.NewsCameraImageManageAdapter;
import com.beem.project.btf.ui.entity.NewsImageInfo;
import com.beem.project.btf.utils.NewsCameraMaterialUtil;
import com.teleca.jamendo.api.WSError;

/**
 * @ClassName: NewsCameraImageFragement
 * @Description: 新闻相机素材Fragment
 * @author: yuedong bao
 * @date: 2015-11-17 上午10:20:33
 */
public class NewsCameraImageFragement extends
		BaseImageGridFragment<NewsImageInfo> {
	public enum NewsMaterialType implements IMaterialType {
		//电视新闻
		TVNews("Aa", "电视新闻", 400, 268, 2),
		//期刊
		Pictorial("Ba", "头条", 711, 1080, 3),
		//时尚
		Fashion("Bb", "时尚", 711, 1080, 3),
		//报纸
		NewsPaper("Bc", "报纸", 711, 1080, 3),
		//节日
		Festival("Bd", "节日", 711, 1080, 3);
		private final String groupid;
		private final String title;
		private final int convertViewW, convertViewH;
		private int numColumns;

		private NewsMaterialType(String num, String title, int convertW,
				int convertViewH, int numColumns) {
			this.groupid = num;
			this.title = title;
			this.convertViewW = convertW;
			this.convertViewH = convertViewH;
			this.numColumns = numColumns;
		}
		@Override
		public String getGroupId() {
			return groupid;
		}
		@Override
		public String getTitle() {
			return title;
		}
		@Override
		public Fragment getFragment(ActivityType actType) {
			Fragment fragment = null;
			fragment = NewsCameraImageFragement.newInstance(this, actType);
			return fragment;
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
		public String getLoadFailedMessage(ActivityType actType) {
			if (actType == ActivityType.Manage) {
				return title + "还未下载素材，赶紧去下载哟";
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
			for (IMaterialType type : TVNews.getAllMembers()) {
				if (type.getGroupId().equals(groupId)) {
					retVal = type;
					break;
				}
			}
			return retVal;
		}
		//是否同一组
		public boolean isPartner(PartnerType material) {
			return groupid.contains(material.val);
		}

		//伙伴类型
		public enum PartnerType {
			//电视新闻
			TV("A"),
			//头条新闻
			Top("B");
			private final String val;

			private PartnerType(String val) {
				this.val = val;
			}
			public String getString() {
				return val;
			}
		}

		//获取相同伙伴素材组
		public static List<IMaterialType> getPartnerMaterials(
				PartnerType partnerType) {
			List<IMaterialType> retList = new ArrayList<IMaterialType>();
			for (NewsMaterialType materiaOne : values()) {
				if (materiaOne.isPartner(partnerType)) {
					retList.add(materiaOne);
				}
			}
			return retList;
		}
		public float getConvertViewscale() {
			return (float) convertViewH / (float) convertViewW;
		}
		@Override
		public int getNumColumns() {
			return numColumns;
		}
	}

	//newInstance
	public static BaseImageGridFragment<NewsImageInfo> newInstance(
			IMaterialType str, ActivityType activityType) {
		BaseImageGridFragment<NewsImageInfo> newFragment = new NewsCameraImageFragement();
		Bundle bundle = new Bundle();
		bundle.putSerializable("MaterialType", str);
		bundle.putSerializable("ActivityType", activityType);
		newFragment.setArguments(bundle);
		return newFragment;
	}
	@Override
	public List<NewsImageInfo> queryManageMaterialDB(String model) {
		List<NewsImageInfo> imageurls = NewsCameraMaterialUtil
				.queryWebDownloadMaterialTemplate(model);
		return imageurls;
	}
	@Override
	public List<NewsImageInfo> queryShowMaterialDB(String model) {
		List<NewsImageInfo> imageurls = NewsCameraMaterialUtil
				.queryWebAllMaterialTemplate(model);
		return imageurls;
	}
	@Override
	public BaseCameraImageAdapter<NewsImageInfo> createAdapter(
			BaseImageGridFragment.ActivityType type) {
		BaseCameraImageAdapter<NewsImageInfo> retVal;
		if (type == ActivityType.Loadimg) {
			retVal = new NewsCameraImageAdapter(imageurls, mContext,
					(NewsMaterialType) getMaterialType());
		} else {
			retVal = new NewsCameraImageManageAdapter(imageurls, mContext,
					(NewsMaterialType) getMaterialType());
		}
		return retVal;
	}
	@Override
	public void requestNetAndSaveDB(String groupid) throws WSError {
		//请求网络
		String response = NewsCameraMaterialUtil.requestNetwork(groupid);
		//保存数据库
		NewsCameraMaterialUtil.SaveToDB(response, false);
	}
	@Override
	public List<NewsImageInfo> onPullUpToRefresh() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<NewsImageInfo> onPullDownToRefresh() {
		// TODO Auto-generated method stub
		return null;
	}
}
