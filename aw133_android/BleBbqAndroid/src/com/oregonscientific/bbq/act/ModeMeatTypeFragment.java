/**
 * 
 */
package com.oregonscientific.bbq.act;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import android.app.Fragment;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.oregonscientific.bbq.R;
import com.oregonscientific.bbq.bean.BBQDataSet;
import com.oregonscientific.bbq.bean.BBQDataSet.DonenessLevel;
import com.oregonscientific.bbq.bean.BbqSettings;
import com.oregonscientific.bbq.bean.DonenessTemperature;
import com.oregonscientific.bbq.ble.BbqDonenessCallback;
import com.oregonscientific.bbq.ble.BleBbqCommandManager;
import com.oregonscientific.bbq.ble.ParseManager;
import com.oregonscientific.bbq.ble.CommandManager.BlueConnectState;
import com.oregonscientific.bbq.service.BleService;
import com.oregonscientific.bbq.utils.BbqConfig;

public class ModeMeatTypeFragment extends Fragment implements OnItemClickListener {
	
	private ModeActivity mActivity;
	private View view = null;
	private GridView meaticonname,donenesschoose;
	private MeatIconNameAdapter iconnameAdapter;
	private DonenessChooseAdapter donenessChooseAdapter;
	//private ArrayList<String> meatList;
	private String[] meatnamearray;
	private int[] meaticonarray;
	private String[] donenessChoosearray;
	private DonenessTemperature mCurrentData;
	private int mCurrentMeattypeIndex;
	private DonenessLevel mCurrentDoneness;
	private List<Integer> mArray;
	
	// Modified by aaronli at Mar 6,2014. For the custom/default doneness setting
	private boolean isCustomst;
	
	private LayoutInflater inflater;
	//private BleBbqCommandManager mCommandManager;
	
	/* (non-Javadoc)
	 * @see android.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = (ModeActivity) getActivity();
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.modemeattype, null);
		init(view);
		return view;
	}
	
	
	/* (non-Javadoc)
	 * @see android.app.Fragment#onStart()
	 */
	@Override
	public void onStart() {
		mActivity.registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
		super.onStart();
	}


	/* (non-Javadoc)
	 * @see android.app.Fragment#onStop()
	 */
	@Override
	public void onStop() {
		super.onStop();
	}


	/* (non-Javadoc)
	 * @see android.app.Fragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
	/*	if (mCommandManager != null)
			mCommandManager.setmDonenessCallback(null);*/
		super.onDestroy();
	}


	public static ModeMeatTypeFragment newInstance(String tag) {
		ModeMeatTypeFragment fragment = new ModeMeatTypeFragment();
		Bundle args = new Bundle();
        args.putString(ModeActivity.MEATTYPE_FRAGMENT, tag);
        fragment.setArguments(args);
		return fragment;
	}
	
	public void init(View v){
		
		meaticonname = (GridView) v.findViewById(R.id.gridMeattype);
		donenesschoose = (GridView) v.findViewById(R.id.gridDonenellChooser);
		
		inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
		
		Resources res=getResources();
		meatnamearray = res.getStringArray(R.array.meatname);
		meaticonarray = ParseManager.MEAT_TYPE_ICONS;
//		meatList=new ArrayList<String>(); 
//		for(int i=1;i<meatnamearray.length;i++){
//			meatList.add(meatnamearray[i]); 
//		} 
		iconnameAdapter = new MeatIconNameAdapter();
		meaticonname.setAdapter(iconnameAdapter);
		meaticonname.setSelector(new ColorDrawable(Color.TRANSPARENT));
		
		donenessChoosearray = res.getStringArray(R.array.donenessLevelStrs);
		donenessChooseAdapter = new DonenessChooseAdapter();
		donenesschoose.setAdapter(donenessChooseAdapter);
		donenesschoose.setSelector(new ColorDrawable(Color.TRANSPARENT));
		
		meaticonname.setOnItemClickListener(this);
		donenesschoose.setOnItemClickListener(this);
		
		Intent service = new Intent(BleService.SERVICE_ACTION);
		service.putExtra(BleService.KEY_COMMAND_INDEX, BleService.KEY_REQUEST_CUSTOMDONENESS);
		service.putExtra(BleService.KEY_REQUEST_CUSTOMDONENESS, mCurrentMeattypeIndex);
		service.setFlags(Service.START_FLAG_REDELIVERY);
		mActivity.startService(service);
	}
	
	public void setData(BbqSettings set) {
		/*mCommandManager = BleBbqCommandManager.getInstance(mActivity);
		mCommandManager.setmDonenessCallback(new MyDonenessCallback());*/
		mCurrentMeattypeIndex = set.getMeatTypeInt();
		if (mCurrentMeattypeIndex < 1 || mCurrentMeattypeIndex > 8) {
			mCurrentMeattypeIndex = 1;
		}
		mCurrentDoneness = set.getDonenessLevel();
//		mCommandManager.requestCustomDoneness(mCurrentMeattypeIndex);
		
		
	}
	

	/**
	 * @return the mCurrentMeattypeIndex
	 */
	public int getCurrentMeattypeIndex() {
		return mCurrentMeattypeIndex;
	}


	/**
	 * @return the mCurrentDoneness
	 */
	public DonenessLevel getCurrentDoneness() {
		return mCurrentDoneness;
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		switch (parent.getId()) {
		case R.id.gridMeattype: {
			if (mCurrentMeattypeIndex != id) {
				mCurrentMeattypeIndex = (int) id;
				iconnameAdapter.notifyDataSetChanged();
				//mCommandManager.requestCustomDoneness(mCurrentMeattypeIndex);
				if (isCustomst) {
					mCurrentData = BbqConfig.MAP_MEATTYPE_INITIAL_INDEX.get(mCurrentMeattypeIndex);
				} else {
					mCurrentData = BbqConfig.MAP_MEATTYPE_INDEX.get(mCurrentMeattypeIndex);
				}
				getCurrentDonenessArray();
				if (!mArray.isEmpty() || !mArray.contains(mCurrentDoneness)) {
					mCurrentDoneness = DonenessLevel.get(mArray.get(0));
				}
				donenessChooseAdapter.notifyDataSetChanged();
			}
			break;
		}
			
		case R.id.gridDonenellChooser: {
			mCurrentDoneness = DonenessLevel.get((int) id);
			donenessChooseAdapter.notifyDataSetChanged();
			break;
		}
			
		default:
			break;
		}
		
	}
	
	private void getCurrentDonenessArray() {
		if (mCurrentData == null) {
			return;
		}
		if (mArray == null) {
			mArray = new ArrayList<Integer>();
		} else {
			mArray.clear();
		}
		float flag = mCurrentData.getWelldoneTemperature();
		if (flag > 0) mArray.add(5);
		flag = mCurrentData.getMediumwellTemperature();
		if (flag > 0) mArray.add(4);
		flag = mCurrentData.getMediumTemperature();
		if (flag > 0) mArray.add(3);
		flag = mCurrentData.getMediumrareTemperature();
		if (flag > 0) mArray.add(2);
		flag = mCurrentData.getRareTemperature();
		if (flag > 0) mArray.add(1);
	}
	
	private IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BleService.ACTION_COMMAND_CALLBACK);
        return intentFilter;
    }
	
	private class MeatIconNameAdapter extends BaseAdapter{
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			//Log.e("cdf","s="+meatList.size());
			if(meatnamearray !=null){
				return meatnamearray.length -1;
			}else{
				return 0;
			}
		}

		@Override
		public Object getItem(int position) {
			return meatnamearray[position + 1];
		}

		@Override
		public long getItemId(int position) {
			return position + 1;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Holder holder;
			if(convertView == null){
				convertView =inflater.inflate(R.layout.iconname, parent, false);  
				holder = new Holder(); 
				holder.iv = (ImageView) convertView.findViewById(R.id.meaticon);
				holder.tv = (TextView) convertView.findViewById(R.id.meatname);
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}
			holder.iv.setImageResource(meaticonarray[position + 1]);
			//holder.tv.setText(meatnamearray[position + 1].toString());
			if (mCurrentMeattypeIndex == getItemId(position)) {
				holder.iv.setBackgroundResource(R.drawable.iconselectionbox);
				holder.tv.setText(meatnamearray[position + 1].toString());
				//convertView.setBackgroundResource(R.color.bbq_orange);
			} else {
				holder.iv.setBackgroundResource(android.R.color.transparent);
				holder.tv.setText("");
				convertView.setBackgroundResource(android.R.color.transparent);
			}
			return convertView;
		}
		
		private class Holder{
			private ImageView iv;
			private TextView tv;
		}
	}
	
	private class DonenessChooseAdapter extends BaseAdapter{
		@Override
		public int getCount() {
			if (mArray != null) {
				
				return mArray.size();
			} 
			return 0;
		}

		@Override
		public Object getItem(int position) {
			if (mArray != null) {
				return DonenessLevel.get(mArray.get(position));
			} 
			return 0;
		}

		@Override
		public long getItemId(int position) {
			if (mArray != null) {
				return mArray.get(position);
			} 
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView donenesstv = null;
			if(convertView == null){
				convertView =inflater.inflate(R.layout.donenesstext, parent, false); 
			}
			donenesstv = (TextView) convertView.findViewById(R.id.donenesstv);
			donenesstv.setText(donenessChoosearray[(int) getItemId(position)]);
			if (mCurrentDoneness!= null && mCurrentDoneness.equals(getItem(position))) {
				//convertView.setBackgroundResource(R.color.bbq_orange);
				donenesstv.setBackgroundResource(R.drawable.iconselectionbox);
			} else {
				donenesstv.setBackgroundResource(android.R.color.transparent);
				convertView.setBackgroundResource(android.R.color.transparent);
			}
			return convertView;
		}
		
	}
	
	private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BleService.ACTION_COMMAND_CALLBACK.equals(action)) {
            	final String commandIndex = intent.getStringExtra(BleService.KEY_COMMAND_INDEX);
            	if (BleService.KEY_REQUEST_CUSTOMDONENESS.equals(commandIndex)) {
            		DonenessTemperature values = (DonenessTemperature) intent.getSerializableExtra(BleService.EXTRA_DATA);
            		if (values == null || values.getMeatTypeIndex() == 0x0f) {
        				mCurrentData = BbqConfig.MAP_MEATTYPE_INDEX.get(mCurrentMeattypeIndex);
        				isCustomst = false;
					} else {
						mCurrentData = values;
						isCustomst = true;
					}
            		getCurrentDonenessArray();
					if (DonenessLevel.NA.equals(mCurrentDoneness) && !mArray.isEmpty()) {
						mCurrentDoneness = DonenessLevel.get(mArray.get(0));
					}
					donenessChooseAdapter.notifyDataSetChanged();
            	}
            }
        }
    };
	
	/*private class MyDonenessCallback implements BbqDonenessCallback {

		@Override
		public void replyDefaultDoneness() {
			
		}

		@Override
		public void replyCustomDoneness(DonenessTemperature values) {
			if (values.isCustom()) {
				mCurrentData = values;
				isCustomst = true;
			} else {
				mCurrentData = BbqConfig.MAP_MEATTYPE_INDEX.get(mCurrentMeattypeIndex);
				isCustomst = false;
			}
			mActivity.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					getCurrentDonenessArray();
					if (DonenessLevel.NA.equals(mCurrentDoneness) && !mArray.isEmpty()) {
						mCurrentDoneness = DonenessLevel.get(mArray.get(0));
					}
					donenessChooseAdapter.notifyDataSetChanged();
				}
			});
		}

		@Override
		public void indiacateClearCustomDoneness(String msg) {
			
		}

		@Override
		public void replySetCustomDoneness(String msg) {
			
		}

		@Override
		public void onSetupCustomDonenessSent(int meatTypeIndex) {
			
		}
		
	}*/
}
