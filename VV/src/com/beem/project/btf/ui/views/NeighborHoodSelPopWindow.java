package com.beem.project.btf.ui.views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.beem.project.btf.R;
import com.beem.project.btf.constant.SettingKey;
import com.beem.project.btf.utils.BBSUtils;
import com.beem.project.btf.utils.SharedPrefsUtil;
import com.btf.push.NeighborHoodPacket.NeighborHoodType;

public class NeighborHoodSelPopWindow extends PopupWindow {
	private Context mContext;
	private View mConvertView;
	private PopupWindow mPopupWindow;
	// 用于记录被选中项
	private int currentpostion = -1;
	NearBySelectAdapter adapter;
	private String[] selectSexArr = { "全部", "男生", "女生" };
	private int[] selectSexArrInt = { R.string.friend_all, R.string.friend_man,
			R.string.friend_woman };
	private int[] selectPhotoArr = { R.drawable.friend_select_all,
			R.drawable.friend_select_man, R.drawable.friend_select_woman };
	private ArrayList<HashMap<String, Object>> dataList = new ArrayList<HashMap<String, Object>>();
	private NBSelLT nbSelLT;

	public interface NBSelLT {
		public void onSelect(NeighborHoodType nbType);
	}

	public NeighborHoodSelPopWindow(Context context) {
		super(context);
		this.mContext = context;
		mPopupWindow = this;
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mConvertView = inflater.inflate(R.layout.friend_nearby_select_person,
				null);
		for (int i = 0; i < selectPhotoArr.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("selectSexArr", selectSexArr[i]);
			map.put("selectSexArrInt", selectSexArrInt[i]);
			map.put("selectPhotoArr", selectPhotoArr[i]);
			dataList.add(map);
		}
		int width = (int) BBSUtils.toPixel(mContext,
				TypedValue.COMPLEX_UNIT_DIP, 158);
		int height = LayoutParams.WRAP_CONTENT;
		setHeight(height);
		setWidth(width);
		setFocusable(true);
		setContentView(mConvertView);
		setBackgroundDrawable(new BitmapDrawable());
		// 创建偏好设置对象
		currentpostion = SharedPrefsUtil.getValue(mContext,
				SettingKey.neighborPos, 0);
		ListView lv_dialog = (ListView) mConvertView
				.findViewById(R.id.lv_dialog);
		adapter = new NearBySelectAdapter(mContext, dataList);
		lv_dialog.setAdapter(adapter);
	}

	private class NearBySelectAdapter extends BaseAdapter {
		private Context mContext;
		private List<?> dataList;

		public NearBySelectAdapter(Context mContext, List<?> dataList) {
			super();
			this.mContext = mContext;
			this.dataList = dataList;
		}
		@Override
		public int getCount() {
			return dataList.size();
		}
		@Override
		public Object getItem(int position) {
			return dataList.get(position);
		}
		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder vh = null;
			if (convertView == null) {
				vh = new ViewHolder();
				LayoutInflater inflater = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(
						R.layout.friend_nearby_select_person_item, parent,
						false);
				vh.image_btn = (ImageView) convertView
						.findViewById(R.id.image_btn);
				vh.friend_nearby_item_selected = (ImageView) convertView
						.findViewById(R.id.friend_nearby_item_selected);
				vh.text_btn = (TextView) convertView
						.findViewById(R.id.text_btn);
				convertView.setTag(vh);
			} else {
				vh = (ViewHolder) convertView.getTag();
			}
			HashMap<String, Object> dataPos = (HashMap<String, Object>) dataList
					.get(position);
			vh.image_btn.setImageResource((Integer) dataPos
					.get("selectPhotoArr"));
			vh.text_btn.setText((String) dataPos.get("selectSexArr"));
			if (position == currentpostion) {
				vh.friend_nearby_item_selected.setVisibility(View.VISIBLE);
			} else {
				vh.friend_nearby_item_selected.setVisibility(View.INVISIBLE);
			}
			convertView.setOnClickListener(new AdapterClicklis(vh, position));
			return convertView;
		}

		private class AdapterClicklis implements OnClickListener {
			private ViewHolder vh;
			private int position;

			private AdapterClicklis(ViewHolder vh, int pos) {
				super();
				this.vh = vh;
				this.position = pos;
			}
			@Override
			public void onClick(View v) {
				currentpostion = position;
				SharedPrefsUtil.putValue(mContext, SettingKey.neighborPos,
						currentpostion);// 保存到偏好设置中
				// 选择全部
				String tvStr = vh.text_btn.getText().toString();
				//LogUtils.i("select_neighborHoodType:" + tvStr);
				NeighborHoodType nbType = NeighborHoodType.all;
				if (tvStr.equals(mContext.getString(R.string.friend_all))) {
					nbType = NeighborHoodType.all;
				}
				// 选择男生
				else if (tvStr.equals(mContext.getString(R.string.friend_man))) {
					nbType = NeighborHoodType.male;
				}
				// 选择女生
				else if (tvStr
						.equals(mContext.getString(R.string.friend_woman))) {
					nbType = NeighborHoodType.female;
				}
				if (nbSelLT != null) {
					nbSelLT.onSelect(nbType);
				}
				adapter.notifyDataSetChanged();
				mPopupWindow.dismiss();
			}
		}

		public class ViewHolder {
			public ImageView image_btn;
			ImageView friend_nearby_item_selected;
			TextView text_btn;
		}
	}

	public void setNBSelLT(NBSelLT nbSelLT) {
		this.nbSelLT = nbSelLT;
	}
}
