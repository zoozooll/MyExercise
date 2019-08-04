package com.oregonscientific.meep.meepmusic;

	import java.util.ArrayList;
	import java.util.Date;
	import java.util.List;

	import android.content.BroadcastReceiver;
	import android.content.Context;
	import android.content.Intent;
	import android.content.IntentFilter;
	import android.util.Log;

	import com.google.gson.Gson;
	import com.oregonscientific.meep.database.table.TableBlacklist;
	import com.oregonscientific.meep.database.table.TableChat;
	import com.oregonscientific.meep.database.table.TableFriend;
	import com.oregonscientific.meep.database.table.TableFriendGroup;
	import com.oregonscientific.meep.database.table.TableRecommendation;
	import com.oregonscientific.meep.global.Global;
	import com.oregonscientific.meep.global.Global.AppType;
	import com.oregonscientific.meep.message.common.Chat;
	import com.oregonscientific.meep.message.common.Friend;
	import com.oregonscientific.meep.message.common.MeepAppMessage;
	import com.oregonscientific.meep.message.common.MeepAppMessage.Category;
	import com.oregonscientific.meep.message.common.MeepServerMessage;
	import com.oregonscientific.meep.message.common.MeepServerMessageSendText;
	import com.oregonscientific.meep.message.common.MeepServerMessageSignIn;
	import com.oregonscientific.meep.message.common.MsmOnline;
	import com.oregonscientific.meep.message.common.MsmSearchFriend;

	public class MessageReceiver {

		public interface OnMessageReceivedListener
		{
			public abstract void onQueryMusicBlackListReceived(List<TableBlacklist> musicBlacklist);
//			public abstract void onQueryMusicRecommendationListReceived(List<TableRecommendation> musicRecommendationlist);
		}

		BroadcastReceiver mMsgReceiver = null;
		private Context mContext = null;
		private Gson mGson = null;
		
		private OnMessageReceivedListener mOnMessageReceivedListener = null;
		
		public MessageReceiver(Context context) {
			mContext = context;
			initMessageReceiver();
		}
		
		private OnMessageReceivedListener getOnMessageReceivedListener(){
			return mOnMessageReceivedListener;
		}
		
		public void setOnMessageReceivedListener(OnMessageReceivedListener listener) {
			mOnMessageReceivedListener = listener;
		}

		private void initMessageReceiver() {
			mGson = new Gson();
			mMsgReceiver = new BroadcastReceiver() {
				@Override
				public void onReceive(Context context, Intent intent) {
					String message = intent.getStringExtra(Global.STRING_MESSAGE);
					try {
						if (message != null) {
							Log.w("music", "websocket2 Message:" + message);
							MeepAppMessage meepAppMsg = mGson.fromJson(message, MeepAppMessage.class);
							Log.d("music", "from:" + meepAppMsg.getFrom() );//+ " cat:" + meepAppMsg.getCatagory() + " opcode:" + meepAppMsg.getOpcode() );
							if (getOnMessageReceivedListener() != null) {
								if (meepAppMsg.getCatagory() == Category.DATABASE_QUERY) {
									decodeDbQueryMsg(meepAppMsg, intent);
								} 
							}
						} else {
							Log.w("music", "websocket2 Message: cannot get broadcast message (App to msgService)");
						}
					} catch (Exception e) {
						Log.e("music", "initMessageReceiver error:" + e.toString());
					}
				}
			};

			IntentFilter filter = new IntentFilter();
			filter.addAction(Global.INTENT_MSG_PREFIX + Global.AppType.Youtube);
			mContext.registerReceiver(mMsgReceiver, filter);

		}
		

		private void decodeDbQueryMsg(MeepAppMessage meepAppMsg, Intent intent) {
			try
			{
				String tableName = meepAppMsg.getMessage();

				if (tableName.equals(TableBlacklist.S_TABLE_NAME)) {
					List<TableBlacklist> musicBlacklist = new ArrayList<TableBlacklist>();
					String[] array = intent.getStringArrayExtra(Global.STRING_LIST);

					for (int i = 0; i < array.length; i++) {
						TableBlacklist blackListItem = mGson.fromJson(array[i], TableBlacklist.class);
						musicBlacklist.add(blackListItem);
					}
					Log.d("database", "music black list");
					getOnMessageReceivedListener().onQueryMusicBlackListReceived(musicBlacklist);
					
//				} else if (tableName.equals(TableRecommendation.S_TABLE_NAME)) {
//					List<TableRecommendation> musicRecommendation = new ArrayList<TableRecommendation>();
//					String[] array = intent.getStringArrayExtra(Global.STRING_LIST);
	//
//					for (int i = 0; i < array.length; i++) {
//						TableRecommendation recommendationItem = mGson.fromJson(array[i], TableRecommendation.class);
//						musicRecommendation.add(recommendationItem);
//					}
//					Log.d("database", "music recommendation list");
//					getOnMessageReceivedListener().onQueryMusicRecommendationListReceived(musicRecommendation);
				} 
			}
			catch (Exception e) {
				Log.d("database", "decode app message err:" +e.toString());
			}
		}
		

		public void close()
		{
			mContext.unregisterReceiver(mMsgReceiver);
		}
				
	}

