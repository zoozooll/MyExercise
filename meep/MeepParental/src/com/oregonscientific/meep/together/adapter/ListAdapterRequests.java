package com.oregonscientific.meep.together.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.oregonscientific.meep.together.R;
import com.oregonscientific.meep.together.activity.MeepTogetherMainActivity;
import com.oregonscientific.meep.together.activity.UserFunction;
import com.oregonscientific.meep.together.bean.Notification;

public class ListAdapterRequests extends ArrayAdapter<HashMap<String, Object>> {
	private final static String TAG = "ListNotification";
	private int resourceId = 0;
	private Context context;
	private LayoutInflater inflater;
	private static String[] type = new String[]{"Store","Apps","Coins","Friend"};
//	private static Integer[] typeimage = new Integer[]{R.drawable.,"Apps","Coins","Friend"};
	public ListAdapterRequests(Context context, int resourceId,
			List<HashMap<String, Object>> Items) {
		super(context, 0, Items);
		this.resourceId = resourceId;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		this.context = context;
	}

	public void refresh() {
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view;
		HashMap<String, Object> item = getItem(position);
		if (item.get("isSection") != null) {
			view = inflater.inflate(R.layout.section_logs, null);

			view.setOnClickListener(null);
			view.setOnLongClickListener(null);
			view.setLongClickable(false);

			final TextView sectionView = (TextView) view
					.findViewById(R.id.textLogsDate);
			// sectionView.setText((String) item.get("date"));
			sectionView.setText(Html.fromHtml("<u>" + (String) item.get("date")
					+ "</u>"));

		} else {
			TextView textLogsInfo;
			TextView textLogsTime;
			TextView textLogsType;
			ImageView category;

			view = inflater.inflate(resourceId, null);
			
			try {
				textLogsInfo = (TextView) view.findViewById(R.id.textLogsInfo);
				textLogsTime = (TextView) view.findViewById(R.id.textLogsTime);
				textLogsType = (TextView) view.findViewById(R.id.textLogsType);
				category = (ImageView) view.findViewById(R.id.category);
				
			} catch (ClassCastException e) {
				Log.e(TAG, "Wrong resourceId", e);
				throw e;
			}
			Notification notification = (Notification) item.get("notification");
			
			// Notification time
			textLogsTime.setText(String.format(context.getResources().getString(R.string.noti_time), (String)item.get("time")));
			// Notification type
			String type = notification.getType();
			String message = notification.getMessage();
			int typeImage = 0;
			if (Notification.S_TYPE_GOOGLEPLAY_DOWNLOAD.equals(type)) {
				type = context.getResources().getString(
						R.string.main_text_type_google);
				typeImage = R.drawable.buy_apps;
				message = String.format(context.getResources().getString(R.string.noti_googleplay),MeepTogetherMainActivity.currentKidName,message);
			} else if (Notification.S_TYPE_STORE_PURCHASE.equals(type)) {
				type = context.getResources().getString(
						R.string.main_text_type_store);
				typeImage = R.drawable.buy_apps;
				message = String.format(context.getResources().getString(R.string.noti_store),MeepTogetherMainActivity.currentKidName,message);
			} else if (Notification.S_TYPE_COIN_REQUEST.equals(type)) {
				type = context.getResources().getString(
						R.string.main_text_type_coins);
				message = String.format(context.getResources().getString(R.string.noti_request_coins),MeepTogetherMainActivity.currentKidName);
				typeImage = R.drawable.coins;
			} else if (Notification.S_TYPE_FRIEND_REQUEST.equals(type)) {
				String requester = notification.getRequester();
				String requestee = notification.getRequestee();
				type = context.getResources().getString(
						R.string.main_text_type_friend);
				message = String.format(context.getResources().getString(R.string.noti_add_friend),requester,requestee);
				typeImage = R.drawable.add_friends;
			} else {

			}
			textLogsType.setText(type);
			if(typeImage!=0)
			{
				category.setImageResource(typeImage);
			}
			// Notification message
			textLogsInfo.setText(message);
			// Notification approval
			String approval = notification.getApproval();
			if (Notification.S_APPROVAL_PENDING.equals(approval)) {
				view.findViewById(R.id.expand).setVisibility(View.VISIBLE);
			} else if (Notification.S_APPROVAL_APPROVE.equals(approval)) {
				textLogsInfo.append(context.getResources().getString(R.string.noti_approve));
				view.setClickable(true);
				textLogsInfo.setTextColor(Color.GRAY);
				textLogsTime.setTextColor(Color.GRAY);
			} else if (Notification.S_APPROVAL_REJECT.equals(approval)) {
				textLogsInfo.append(context.getResources().getString(R.string.noti_reject));
				view.setClickable(true);
				view.setClickable(true);
				textLogsInfo.setTextColor(Color.GRAY);
				textLogsTime.setTextColor(Color.GRAY);
			}
			
			//set tag
			view.setTag(notification);

			if (position % 2 == 0) {
				view.setBackgroundResource(R.color.item_bkg_one);
			} else {
				view.setBackgroundResource(R.color.item_bkg_two);
			}
		}
		

		return view;
	}
}
