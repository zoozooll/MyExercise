package com.oregonscientific.meep.youtube.ui.fragment;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.SequenceInputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.util.Key;
import com.google.gson.Gson;
import com.oregonscientific.meep.ServiceManager;
import com.oregonscientific.meep.account.Account;
import com.oregonscientific.meep.account.AccountManager;
import com.oregonscientific.meep.global.BadWordChecker;
import com.oregonscientific.meep.notification.NotificationMessage;
import com.oregonscientific.meep.permission.Component;
import com.oregonscientific.meep.permission.Permission;
import com.oregonscientific.meep.permission.PermissionManager;
import com.oregonscientific.meep.youtube.Consts;
import com.oregonscientific.meep.youtube.MeepYoutubeActivity;
import com.oregonscientific.meep.youtube.OpenYouTubePlayerActivity;
import com.oregonscientific.meep.youtube.OsListViewItem;
import com.oregonscientific.meep.youtube.R;
import com.oregonscientific.meep.youtube.YouTubeUtility;
import com.oregonscientific.meep.youtube.YoutubeGData;
import com.oregonscientific.meep.youtube.YoutubeGData.Entry;
import com.oregonscientific.meep.util.*;

import com.oregonscientific.meep.youtube.api.YoutubeDataFeedback;
import com.oregonscientific.meep.youtube.api.YoutubeDataFeedback.VideosData;
import com.oregonscientific.meep.youtube.api.YoutubeRestRequest;
import com.oregonscientific.meep.youtube.api.YoutubeRestRequest.YoutubeRestCallBackHandler;

public class MenuRecentlyFragment extends Fragment implements YoutubeRestCallBackHandler{
	private final String COMPONENT_NAME_YOUTUBE = "securitylevel";
	private final int MSG_CAN_SEARCH = 2000;
	private final int MSG_CANNOT_SEARCH = 2001;
  
	MeepYoutubeActivity currentMeepYoutubeActivity;
	 boolean shouldPopupBlockSearchAlert = true;
	 boolean shouldSearch = false;
	 boolean isSecurityLevelHigh = false;
	 boolean enableFiltering = true;
	 boolean enableDebug = false;
	 boolean shouldUseMeepAPI = true;
	 
	 
	 String PACKAGE_NAME;
	 String[] title;
     String[] id;
     String[] small_thumbnail;
     String[] large_thumbnail;
     String[] photo;
     int[] duration;
     int i;
     int total;
     ImageDownloader imageHelper;
     int MAX_REC = 50;
     int MAX_PAGE = 10;
     int SEARCHED_REC =0;
 	 ProgressDialog dialog;
 	 
	ArrayList<HashMap<String, Object>> mylist;
	ArrayList<HashMap<String, Object>> myRecommendationOslist;
	ArrayList<HashMap<String, Object>> myRecommendationParentlist;
	ArrayList<HashMap<String, Object>> relatedList;
	
	String userID;	
	PermissionManager pm;

	
	private Gson mGson = null;
	public static final String TAG = "MenuRecentlyFragment";
	ImageView youtubeBigImageView;
	ImageView youtubeBigImagePlay;
	String videoId, videoTitle, centralTitle, viewCount;
	
	GridView myGrid = null;
	private MyAdapter adapter;
	LinearLayout gridview_item;
	ImageView picImg;
	TextView pic_title,pic_count;
	List<OsListViewItem> gridlist ;
	OsListViewItem osItem;
	
	
	TextView youtubeIdLabelSearchResultsCount;
	TextView youtubeIdLabelTitle;
	LinearLayout result_count;
	LinearLayout gridView;
	RelativeLayout content_background;
	EditText searchBox;
	Button btnSearch,btnMainPage;
	TextView txtvideoTitleOfRecentlyViewed;
	
	NotificationMessage notification;
	Context mContext;
	public static MenuRecentlyFragment newInstance() {
		Log.d(TAG, "new Instance");
		//create a new content fragment
		MenuRecentlyFragment f = new MenuRecentlyFragment();
		return f;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		Log.d(TAG, "onCreateView");
		mContext = this.getActivity();
		View view = inflater.inflate(R.layout.main_menu_recently, container, false);
		youtubeIdLabelSearchResultsCount = (TextView) view.findViewById(R.id.youtubeIdLabelSearchResultsCount);
		gridView = (LinearLayout) view.findViewById(R.id.bottom);
		result_count = (LinearLayout) view.findViewById(R.id.mid);
		content_background = (RelativeLayout) view.findViewById(R.id.content_backgroundcentr);
		btnSearch = (Button)view.findViewById(R.id.btnSearch);
		btnMainPage = (Button)view.findViewById(R.id.btnMainPage);
		searchBox = (EditText)view.findViewById(R.id.searchBox);
		txtvideoTitleOfRecentlyViewed = (TextView) view.findViewById(R.id.txtvideoTitleOfRecentlyViewed);
		

		
		gridlist = new ArrayList<OsListViewItem>();
		for(int i = 0 ;i<12 ;i++){
			osItem = new OsListViewItem();
			osItem.setName(""+i);
			osItem.setCount(i+"");
			gridlist.add(osItem);
		};
		myGrid = (GridView) view.findViewById(R.id.videoItems);
		adapter = new MyAdapter();
		myGrid.setAdapter(adapter);
		
		String format = getResources().getString(R.string.search_res); 
		String search_res = String.format(format, gridlist.size()); 
		youtubeIdLabelSearchResultsCount.setText(search_res);
		

		searchBox.setOnKeyListener(new OnKeyListener(){
		
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				 if (event.getAction() == KeyEvent.ACTION_DOWN)
			        {
			            switch (keyCode)
			            {
			                case KeyEvent.KEYCODE_DPAD_CENTER:
			                case KeyEvent.KEYCODE_ENTER:
			                	actionProcessSearch();
			                    return true;
			                default:
			                    break;
			            }
			        }
			        return false;
			}
			
		});
		
		
		
		
		PACKAGE_NAME = getActivity().getPackageName();

		imageHelper = new ImageDownloader(getActivity(),PACKAGE_NAME);
		
		
		youtubeIdLabelTitle = (TextView) view.findViewById(R.id.youtubeIdLabel);
		
		
		//Central Big Image View
		youtubeBigImageView = (ImageView) view.findViewById(R.id.youtubeBigImageView);
		youtubeBigImagePlay = (ImageView) view.findViewById(R.id.youtubeBigImagePlay);
		
		//youtubeBigImageView.setLayoutParams(params)
		
		youtubeBigImagePlay.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View pV) {
				playYoutubeVideo(videoId);
			}
		});

		youtubeBigImageView.setVisibility(View.INVISIBLE);
		youtubeBigImagePlay.setVisibility(View.INVISIBLE);
		

		btnSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.e("cdf","btnSearch clicked!");
				//searchYouTube(searchBox.getText().toString());
				
				Log.e("cdf","show------------------"+searchBox.getText().toString());
				
				actionProcessSearch();
				
			}
		});
		btnMainPage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.e("cdf","btnSearch clicked!");
				//searchYouTube(searchBox.getText().toString());
				
				Log.e("cdf","show------------------home");
				actionProcessMain();
			}
		});
		
		try{
			userID = YouTubeUtility.getAccountID(currentMeepYoutubeActivity);
		}catch(Exception e){
			userID = "";
		}

		getmPermissionManager();

		//get securitylevel
		if(!enableDebug){
			shouldSearch=false;
			retriveSecurityLevel();
		}
		return view;
	}
	
	public void hideSearchbox(){
		btnSearch.setVisibility(View.INVISIBLE);
		searchBox.setVisibility(View.INVISIBLE);							
	}
	
	public void retriveSecurityLevel() {
		try{
			ExecutorService service = Executors.newSingleThreadExecutor();
			service.execute(new Runnable() {
	
				@Override
				public void run() {				
					List<Permission> permissions = pm.getAccessScheduleBlocking(null, userID);
					if(permissions==null){
						Log.e(TAG,"permission cannot get");
						isSecurityLevelHigh = false;							
						if(shouldSearch){
							handler.sendEmptyMessage(MSG_CAN_SEARCH);
						}
						return;
					}
					for (Permission permission : permissions) {
						Component component = permission.getComponent();
						if (component != null && COMPONENT_NAME_YOUTUBE.equals(component.getDisplayName())) {
							//Log.e("XXXXX",permission.getAccessLevel().toString());
							if(permission.getAccessLevel().toString().equals("allow") || permission.getAccessLevel().toString().equals("medium")){
								if(shouldSearch){
									isSecurityLevelHigh = false;								
									handler.sendEmptyMessage(MSG_CAN_SEARCH);
								}
							}else{
								isSecurityLevelHigh = true;							
								handler.sendEmptyMessage(MSG_CANNOT_SEARCH);		
							}
						}
					}
	
					
				}
	
			});
		}catch(Exception e){
			Log.e(TAG,"retriveSecurityLevel->error:"+e.toString());
		}
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_CAN_SEARCH:
				actionProcessSearch_proc();
				break;
			case MSG_CANNOT_SEARCH:
				if (dialog != null) {
					dialog.dismiss();
				}
				if(shouldPopupBlockSearchAlert){
					//Log.e(TAG,"c");
					YouTubeUtility.alertMessage(currentMeepYoutubeActivity, R.string.youtube_title_blocked, R.string.youtube_noti_short_denysearch);
					shouldPopupBlockSearchAlert=false;
				}
				
				break;
			default:
				break;
			}
		}
	};
	

	private PermissionManager mPermissionManager;
	
	
	
	public PermissionManager getmPermissionManager() {
		if(pm == null)
			pm = (PermissionManager) ServiceManager.getService(currentMeepYoutubeActivity, ServiceManager.PERMISSION_SERVICE);
		return pm;
	}

	public void setmPermissionManager(PermissionManager mPermissionManager) {
		this.pm = mPermissionManager;
	}

	private boolean containsBadWord(String word) {
		
		String id = YouTubeUtility.getAccountID(currentMeepYoutubeActivity);
		if (pm.containsBadwordBlocking(id, word)) {
			Log.d("test","badword:"+word);
			try {
				Log.e(TAG,"d");
				YouTubeUtility.alertMessage(currentMeepYoutubeActivity, R.string.youtube_title_blocked, R.string.youtube_noti_short_blocked);
			} catch (Exception e) {
				// TODO: handle exception
			}
			return true;
		}
		return false;
	}	
	
	public void actionUpdateSearchResultUI(int totalResult,String[] id, String[] title){
		Log.e(TAG,"actionUpdateSearchResultUI: totalResult="+totalResult);
		String strMeatFormat = getResources().getString(R.string.youtube_info_about_xxx_results);  
		String strMeatMsg = String.format(strMeatFormat, totalResult); 
		youtubeIdLabelSearchResultsCount.setText(strMeatMsg);
		if (dialog != null) {
			dialog.dismiss();
		}
	}
	
	public void actionProcessSearch(){
		shouldPopupBlockSearchAlert=true;
		shouldSearch=true;
		if(enableDebug){
			isSecurityLevelHigh = false;								
			handler.sendEmptyMessage(MSG_CAN_SEARCH);
		}else{
			retriveSecurityLevel();
		}
	}
	
	public void actionProcessSearch_proc(){
		if(isSecurityLevelHigh){
			YouTubeUtility.alertMessage(currentMeepYoutubeActivity, R.string.youtube_title_blocked, R.string.youtube_noti_short_denysearch);
			shouldPopupBlockSearchAlert=false;
			return;
		}
		
		if (dialog == null) {
			dialog = ProgressDialog.show(getActivity(), "", "", true, false);
			dialog.setContentView(R.layout.progress);
			dialog.show();
		}else if(!dialog.isShowing()){
			dialog.show();
		}
		
		Handler handler = new Handler();
		handler.postDelayed(new Runnable(){
		@Override
		      public void run(){
				Log.d("actionProcessSearch",searchBox.getText().toString());
				content_background.setVisibility(View.GONE);
				result_count.setVisibility(View.VISIBLE);
				gridView.setVisibility(View.VISIBLE);
	
				InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(searchBox.getWindowToken(), 0);				
				searchYouTube(searchBox.getText().toString());
		   }
		},100);
	}

	public void actionProcessMain(){
		content_background.setVisibility(View.VISIBLE);
		searchBox.setText("");
		result_count.setVisibility(View.GONE);
		gridView.setVisibility(View.GONE);
	
		if(currentMeepYoutubeActivity!=null){
			currentMeepYoutubeActivity.initRecentlyView();
		}
		
		InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(searchBox.getWindowToken(), 0);
	}
	
	/*public void onClickMenuButton(View view) {
		switch (view.getId()) {
		case R.id.btnSearch:
			searchYouTube(searchBox.getText().toString());
			Log.e("cdf","btnSearch clicked!");
			break;
		}
	}*/
	
	
	private void toMainPage()
	{
		/*if(mainViewFragment!=null)
		{
			String url = mainViewFragment.getCurrentUrl();
			popFragment(mainViewFragment);
			mainViewFragment = null;
			//update recently
			
		}*/
		//clear search box
		searchBox.setText("");
	}	
	
	public void searchYouTube(String mSearchText){
		Log.e(TAG,"searchYouTube: text="+mSearchText);

		if(mSearchText.trim().equals("")){
			if (dialog != null) {
				dialog.dismiss();
			}			
			Log.e(TAG,"b");
			YouTubeUtility.alertMessage(currentMeepYoutubeActivity, R.string.youtube_title_blockedempty, R.string.youtube_noti_short_blockedempty);
			Log.e(TAG,"searchYouTube->empty check found:"+mSearchText);
			return;
		}		
		
		if (pm.containsBadword(userID, mSearchText)) {
			Log.e(TAG,"a");
			YouTubeUtility.alertMessage(currentMeepYoutubeActivity, R.string.youtube_title_blockedbadword, R.string.youtube_noti_short_blockedbadword);
			Log.e(TAG,"searchYouTube->badword check found:"+mSearchText);
			if (dialog != null) {
				dialog.dismiss();
			}			
			return;
		}else{


			String finalSearchText = removeStringSymbols(" "+mSearchText).trim();
			if(finalSearchText.equals("")){
				if (dialog != null) {
					dialog.dismiss();
				}			
				Log.e(TAG,"b");
				YouTubeUtility.alertMessage(currentMeepYoutubeActivity, R.string.youtube_title_blocked, R.string.youtube_noti_short_blocked);
				Log.e(TAG,"searchYouTube->badword check found:"+mSearchText);
				
				return;
			}
			finalSearchText = finalSearchText.replaceAll(" ", "+");
			try{
				 if(shouldUseMeepAPI){
					 convertJSONForMeepAPI(finalSearchText);
				 }else{
					 String urlList[];
					 urlList = new String[MAX_PAGE];
					 youtubeUrl url = new youtubeUrl("https://gdata.youtube.com/feeds/api/videos/-/" +finalSearchText);
					 url.maxResults = MAX_REC;

					 int recIndex=1;
					 for(int i=0;i<MAX_PAGE;i++){				 
						 url.startIndex = recIndex;
						 url.orderby = "viewCount";
						 recIndex+=50;
						 urlList[i] = url.toString();
						 Log.e(TAG,"URL ="+urlList[i]);
					 }	
					 convertJSON(urlList);				 
				 }
				 
				
			}catch(Exception e){
				Log.e(TAG,"searchYouTube: error="+e.toString());
			}
		}
	}
	
	public static String removeStringSymbols(String str)
	{
		char[] list = new char[]{'!','@','#','$','%','^','&','*','(',')','-','+','{','}','[','}','\\',',','.','/',':',';','|','=','_','\'','\"' };
		
		for(int i=0; i<list.length; i++)
		{
			str = str.replace(list[i], ' ');
		}
		return str;
	}
	
    public class MyAdapter extends BaseAdapter{
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			//Log.e("cdf","gridlist.size()="+gridlist.size());
			return gridlist.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return gridlist.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			// TODO Auto-generated method stub
			View v;
			if(convertView==null){
				v = LayoutInflater.from(getActivity()).inflate(R.layout.vedio_item, parent, false);
			}else{
				v = convertView;
			}
			

			if(total<=0){
				v.setVisibility(View.GONE);
				youtubeIdLabelSearchResultsCount.setVisibility(View.GONE);
			}else{
				v.setVisibility(View.VISIBLE);
				youtubeIdLabelSearchResultsCount.setVisibility(View.VISIBLE);
			}
			
			gridview_item = (LinearLayout) v.findViewById(R.id.gridview_item);
			picImg = (ImageView) v.findViewById(R.id.picVideo);
			pic_title = (TextView) v.findViewById(R.id.pic_title);
			pic_count = (TextView) v.findViewById(R.id.pic_count);
			
			Bitmap bitmap = null;
			if(position%6==0){
				bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.y_board01);
				gridview_item.setBackgroundResource(R.drawable.y_board01);
			}else if(position%6==1){
				bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.y_board02);
				gridview_item.setBackgroundResource(R.drawable.y_board02);
			}else if(position%6==2){
				bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.y_board03);
				gridview_item.setBackgroundResource(R.drawable.y_board03);
			}else if(position%6==3){
				bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.y_board04);
				gridview_item.setBackgroundResource(R.drawable.y_board04);
			}else if(position%6==4){
				bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.y_board05);
				gridview_item.setBackgroundResource(R.drawable.y_board05);
			}else if(position%6==5){
				bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.y_board06);
				gridview_item.setBackgroundResource(R.drawable.y_board06);
			}
			osItem.setImage(bitmap);
			
			//picImg.setImageBitmap();
			

						
			if(position>=0 && position<total){
				imageHelper.download(photo[position], picImg);
				pic_title.setText(title[position]);
				final String tmpID;
				final String tmpTitle;

				tmpID = id[position];			
				tmpTitle = title[position];
				
				picImg.setOnClickListener((new View.OnClickListener() {
					@Override
					public void onClick(View pV) {
						videoId = tmpID;
						centralTitle = tmpTitle;
						playYoutubeVideo(tmpID);
					}
				}));					
				
			}else{
				pic_title.setText("");
			}
			
			String format = getResources().getString(R.string.views_res); 
			int cou = Integer.parseInt(gridlist.get(position).getCount());
			String views_res = String.format(format, cou); 
			pic_count.setText(views_res);
			pic_count.setVisibility(View.INVISIBLE);
		
			
			return v;
		}
    }
    
    public void setPreviewImageById(String videoId){
    	imageHelper.download("http://i.ytimg.com/vi/"+videoId+"/0.jpg", youtubeBigImageView);
			youtubeBigImageView.setVisibility(View.VISIBLE);
			youtubeBigImagePlay.setVisibility(View.VISIBLE);
    }
	
	public void setPreviewImage(Bitmap bp){
		if(bp!=null){
			youtubeBigImageView.setImageBitmap(bp);
			youtubeBigImageView.setVisibility(View.VISIBLE);
			youtubeBigImagePlay.setVisibility(View.VISIBLE);
		}
	}
	
	public void setVideoId(String in_videoId){
		if(in_videoId==null){
			videoId = "";
		}else{
			videoId = in_videoId;
		}
		
		Log.e("setVideoId",videoId);	
	}
	
	public void setTitle(String in_videoTitle){
		if(in_videoTitle==null){
			videoTitle = "";
		}else{
			videoTitle = in_videoTitle;
			//youtubeIdLabelTitle.setText(videoTitle);			
		}
				
		if(txtvideoTitleOfRecentlyViewed!=null){
			if(videoTitle.length()>40){
				txtvideoTitleOfRecentlyViewed.setText(videoTitle.substring(0, 37)+"...");
			}else{
				txtvideoTitleOfRecentlyViewed.setText(videoTitle);
			}
		}
				
		Log.e("setVideoId",videoTitle);	
	}
	

	public void saveRecentlyView() throws IOException{
		String FILENAME = "recently_view";
		String string = "id:" + videoId + "youtubeCentralTitle:" + centralTitle + "youtubeViewCount:" + viewCount;
		Log.e("saveRecentlyView","string="+string);

		FileOutputStream fos;
		try {
			fos = getActivity().openFileOutput(FILENAME, getActivity().MODE_PRIVATE);
			fos.write(string.getBytes());
			fos.close();
		} catch (Exception e) {
			Log.e("ZZZZZ",e.toString());
		}
	}

	private void playYoutubeVideo(String videoId)
	{
		try {
			saveRecentlyView();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		Intent lVideoIntent = new Intent(null, Uri.parse("ytv://"+videoId), this.getActivity(), OpenYouTubePlayerActivity.class);
		startActivity(lVideoIntent); 	
	}
	

	//only support 2 url now
	public InputStream getJSONData(String[] url){
		DefaultHttpClient httpClient = new DefaultHttpClient();
		URI uri;
		InputStream data1 = null;
		InputStream data2 = null;
	    InputStream finalData = null;
	    
		HttpGet method;
		HttpResponse response;
		
		try {
			uri = new URI(url[0]);
			method = new HttpGet(uri);
			response = httpClient.execute(method);
			data1 = response.getEntity().getContent();
			
			Log.e("getJSONData","1");
	        if (data1 != null){
				Log.e("getJSONData","2");
	        	uri = new URI(url[1]);
				method = new HttpGet(uri);
				response = httpClient.execute(method);
				data2 = response.getEntity().getContent();
				if(data2 !=null){
					Log.e("getJSONData","3");
					finalData = new SequenceInputStream(data1,data2);
					
					if(finalData==null){
						Log.e("getJSONData","7");
					}
				}else{
					finalData = data1;
				}				
	        }else{
				Log.e("getJSONData","4");
				finalData = data1;
	        }
	        return finalData;
			
		} catch (Exception ex) {
			Log.e("getJSONData", "Http Execption:" + ex.toString());
			return finalData;
		}
	}
	
	public InputStream getJSONData(String url){
		DefaultHttpClient httpClient = new DefaultHttpClient();
		URI uri;
		InputStream data = null;
		
		try {
			uri = new URI(url);
			HttpGet method = new HttpGet(uri);
			HttpResponse response = httpClient.execute(method);
			data = response.getEntity().getContent();
		} catch (Exception ex) {
			Log.e("Youtube", "Http Execption:" + ex.toString());
		}
		return data;
	}
	
	public void convertJSONForMeepAPI(String keyword){
		Log.e(TAG,"convertJSONForMeepAPI: keyword="+keyword);
		
		YoutubeRestRequest youtubeRestRequest = new YoutubeRestRequest(this);
		youtubeRestRequest.searchYoutube(keyword);
		
		
		
		
		if(true){return;}
		String[] url = new String[0];
		
		try{
	        int count = 0;
	        Gson gson = new Gson();
	        Reader r;
	        
	        YoutubeGData[] youtubeGDataList = new YoutubeGData[url.length];
			for(int i=0;i<youtubeGDataList.length;i++){
				r = new InputStreamReader(getJSONData(url[i]));
				youtubeGDataList[i] = gson.fromJson(r, YoutubeGData.class);
				if (youtubeGDataList[i].getFeed().getEntry() != null){
					count += youtubeGDataList[i].getFeed().getEntry().size();
				}
			}
			Log.e(TAG,"convertJSON count="+count);
			
			if(count<=0){
	        	dialog.dismiss();
	        	Log.e("URL","no result");
	        	Message msg = new Message();
	        	msg.what = 2;
	        	mHandlerReadImg.sendMessage(msg);
			}else{
	        	Log.e("URL","Result:"+count);
	        	
		        SEARCHED_REC = count;
		        title = new String[count];
		        id = new String[count];
		        duration = new int[count];
		        photo = new String[count];
		        i = 0;
		        total = 0;
				
				for(int listRec=0;listRec<youtubeGDataList.length;listRec++){								
					total = youtubeGDataList[listRec].getFeed().getOpenSearch$totalResults().get$t();
			        for(Entry entry : youtubeGDataList[listRec].getFeed().getEntry()) {
			        	if(enableFiltering && (entry.getApp$control()!=null || (entry.getMedia$group().getMedia$restriction()!=null))){
			        		total--;
			        		count--;		        		
			        		continue;
			        	}else{
				        	title[i] = entry.getTitle().get$t();
				        	id[i] = entry.getMedia$group().getYt$videoid().get$t();
				        	duration[i] = entry.getMedia$group().getYt$duration().getSeconds();
				        	photo[i]= "http://i.ytimg.com/vi/"+id[i]+"/2.jpg";
				        	//Log.e(TAG,i+"---->"+title[i]);
				            i++;		        		
			        	}		
			        }
			        youtubeGDataList[listRec] = null;
				}
				
		        total = i;	
				Message msg = new Message();
				msg.what = 1;
				Bundle bundle = new Bundle();  
				bundle.putStringArray("title", title);  
				bundle.putStringArray("id", id);  
				bundle.putIntArray("duration", duration);
				bundle.putInt("total", total);
				
				gridlist.clear();
				for(int i = 0 ;i<total ;i++){
					if(i>=SEARCHED_REC){
					//	break;
					}
					osItem = new OsListViewItem();
					osItem.setName(""+i);
					osItem.setCount(i+"");
					gridlist.add(osItem);
				};
				adapter = new MyAdapter();
				adapter.notifyDataSetChanged();
				myGrid.setAdapter(adapter);

				actionUpdateSearchResultUI(total,id,title);
				msg.setData(bundle);  
				mHandlerReadImg.sendMessage(msg);				
			}		
		}catch(Exception ex){
			dialog.dismiss();
			Log.e("ST:", "JSON Exception:" + ex.toString());  
		}
	}
	
	
	public void convertJSON(String[] url){
		Log.e(TAG,"convertJSON");
		try{
	        int count = 0;
	        Gson gson = new Gson();
	        Reader r;
	   
	        YoutubeGData[] youtubeGDataList = new YoutubeGData[url.length];
			for(int i=0;i<youtubeGDataList.length;i++){
				r = new InputStreamReader(getJSONData(url[i]));
				youtubeGDataList[i] = gson.fromJson(r, YoutubeGData.class);
				if (youtubeGDataList[i].getFeed().getEntry() != null){
					count += youtubeGDataList[i].getFeed().getEntry().size();
				}
			}
			Log.e(TAG,"convertJSON count="+count);
			
			if(count<=0){
	        	dialog.dismiss();
	        	Log.e("URL","no result");
	        	Message msg = new Message();
	        	msg.what = 2;
	        	mHandlerReadImg.sendMessage(msg);
			}else{
	        	Log.e("URL","Result:"+count);
	        	
		        SEARCHED_REC = count;
		        title = new String[count];
		        id = new String[count];
		        duration = new int[count];
		        photo = new String[count];
		        i = 0;
		        total = 0;
				
				for(int listRec=0;listRec<youtubeGDataList.length;listRec++){								
					total = youtubeGDataList[listRec].getFeed().getOpenSearch$totalResults().get$t();
			        for(Entry entry : youtubeGDataList[listRec].getFeed().getEntry()) {
			        	if(enableFiltering && (entry.getApp$control()!=null || (entry.getMedia$group().getMedia$restriction()!=null))){
			        		total--;
			        		count--;		        		
			        		continue;
			        	}else{
				        	title[i] = entry.getTitle().get$t();
				        	id[i] = entry.getMedia$group().getYt$videoid().get$t();
				        	duration[i] = entry.getMedia$group().getYt$duration().getSeconds();
				        	photo[i]= "http://i.ytimg.com/vi/"+id[i]+"/2.jpg";
				        	//Log.e(TAG,i+"---->"+title[i]);
				            i++;		        		
			        	}		
			        }
			        youtubeGDataList[listRec] = null;
				}
				
		        total = i;	
				Message msg = new Message();
				msg.what = 1;
				Bundle bundle = new Bundle();  
				bundle.putStringArray("title", title);  
				bundle.putStringArray("id", id);  
				bundle.putIntArray("duration", duration);
				bundle.putInt("total", total);
				
				gridlist.clear();
				for(int i = 0 ;i<total ;i++){
					if(i>=SEARCHED_REC){
					//	break;
					}
					osItem = new OsListViewItem();
					osItem.setName(""+i);
					osItem.setCount(i+"");
					gridlist.add(osItem);
				};
				adapter = new MyAdapter();
				adapter.notifyDataSetChanged();
				myGrid.setAdapter(adapter);

				actionUpdateSearchResultUI(total,id,title);
				msg.setData(bundle);  
				mHandlerReadImg.sendMessage(msg);				
			}		
		}catch(Exception ex){
			dialog.dismiss();
			Log.e("ST:", "JSON Exception:" + ex.toString());  
		}
	}
	
	Handler mHandlerReadImg = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 1:
					//loadAndDrawImage(msg);
					break;
				case 2:
					Log.e(TAG,"HandleMessage(2)");
					YouTubeUtility.alertMessage(currentMeepYoutubeActivity, R.string.youtube_title_search_results, R.string.youtube_info_results_blocked);
					break;
				case 3:
					//loadAndDrawImage(msg);
					break;
				default:
					break;
			}
			/*Log.e("cdf","show------------------");
			content_background.setVisibility(View.GONE);
			youtubeIdLabelSearchResultsCount.setVisibility(View.VISIBLE);
			gridView.setVisibility(View.VISIBLE);*/
			
			super.handleMessage(msg);
		}
	};
	private int isNewSearch = 1;
	private void loadAndDrawImage(Message msg) {
		/*if(isNewSearch==1)
		{
			flowLayoutCentral.setLayoutHeight(0);
			flowLayoutCentral.removeAllViews();
		}*/
		//get GSON data
		Bundle bundle = msg.getData();  
		String[] title = bundle.getStringArray("title");  
		String[] id = bundle.getStringArray("id");  
		int[] duration = bundle.getIntArray("duration"); 
		int[] viewCount = bundle.getIntArray("viewCount"); 
		
		int count = id.length;
		String[] youtubePhoto = new String[count];
		String[] titleShort = new String[count];
		
		mylist = new ArrayList<HashMap<String, Object>>(); 

		isNewSearch = 0;
		flag = 0;
		for(int i=0;i<title.length;i++){
			
			
			URL url = null;
			//2013
			if(title[i]==null || id[i]==null || id[i].equals("")){
				continue;
			}
			
			if (title[i].length() > MAX_REC){
				titleShort[i] = title[i].substring(0, 35) + "...";
			}
			
			try {
				url = new URL(small_thumbnail[i]);
			} catch (MalformedURLException e) {
				Log.e("Youtube", "MalformedURLException:" + e.toString());
			}
			Log.i("ViewCount", "ViewCount"+ viewCount[i]);
			HashMap<String, Object> map = new HashMap<String, Object>();  
			map.put("youtubeId", id[i]);
			map.put("youtubeTitleShort", titleShort[i]);  
			map.put("youtubeTitleLong", title[i]);  
			map.put("youtubeImageView", small_thumbnail[i]);  
			map.put("youtubeDuration", getDuration(duration[i])); 
			String viewText = getResources().getString(R.string.youtube_title_no_of_views);
			map.put("youtubeViewCount", getViewCountFormat(viewCount[i]) + "  " + viewText); 
			mylist.add(map);
			addFlowChildren(map);
			
			
		}
	}
	


	public class youtubeUrl extends GenericUrl {
		@Key final String alt = "json";
		@Key String author;
		@Key("max-results")
		public Integer maxResults;
		@Key("start-index")
		public Integer startIndex;
		@Key String safeSearch = "strict";
		@Key String orderby = "viewCount";
		@Key int v = 2;

		public youtubeUrl(String url) {
			super(url);
	    }
	}
		
	public String getDuration(int duration){
		String formatedDuration = "00:00:00";
		
		if (duration > 0){
			int hours = (int) Math.floor(duration/60/24);
			int minutes = (int) Math.floor(duration/60);
			int seconds = duration % 60;
			formatedDuration = getFormatedTime(hours) + ":" + getFormatedTime(minutes) + ":" + getFormatedTime(seconds);
		}
		
		return formatedDuration;
	}
	  
	public String getFormatedTime(int time){
		String formatedTime = "00";
		
		if (time >= 0 && time<10){
			formatedTime = "0" + time;
		}else {
			formatedTime = String.valueOf(time);
		}
		
		return formatedTime;
	}
	
	public void parseSearchResult(Message msg){
		Bundle bundle = msg.getData();  
		String[] title = bundle.getStringArray("title");  
		String[] id = bundle.getStringArray("id");  
		int[] duration = bundle.getIntArray("duration"); 
		int[] viewCount = bundle.getIntArray("viewCount"); 
		
		int count = id.length;
		String[] youtubePhoto = new String[count];
		String[] titleShort = new String[count];
		
		mylist = new ArrayList<HashMap<String, Object>>(); 

		for(int i=0;i<title.length;i++){
			youtubePhoto[i]= "http://i.ytimg.com/vi/"+id[i]+"/2.jpg";
			URL url = null;
			//2013
			if(title[i]==null){
				continue;
			}
			
			if (title[i].length() > 40){
				titleShort[i] = title[i].substring(0, 35) + "...";
			}
			
			try {
				url = new URL(youtubePhoto[i]);
			} catch (MalformedURLException e) {
				Log.e("Youtube", "MalformedURLException:" + e.toString());
			}
			Log.i("ViewCount", "ViewCount"+ viewCount[i]);
			HashMap<String, Object> map = new HashMap<String, Object>();  
			map.put("youtubeId", id[i]);
			map.put("youtubeTitleShort", titleShort[i]);  
			map.put("youtubeTitleLong", title[i]);  
			map.put("youtubeImageView", youtubePhoto[i]);  
			map.put("youtubeDuration", getDuration(duration[i])); 
			String viewText = getResources().getString(R.string.youtube_title_no_of_views);
			map.put("youtubeViewCount", getViewCountFormat(viewCount[i]) + "  " + viewText); 
			mylist.add(map);
			addFlowChildren(map);
		}		
	}


	private int flag=0;
	public void addFlowChildren(HashMap<String,Object> map)
	{


//		View view = LayoutInflater.from(this).inflate(R.layout.center_listitem, flowLayoutCentral,false);
//		
//		TextView textId;
//		final ImageView image;    
//		TextView textTitleShort;
//		TextView textTitleLong;
//		TextView textDuration;
//		TextView textvViewCount;
//
//		try {
//			textId = (TextView)view.findViewById(R.id.youtubeId);
//			image = (ImageView)view.findViewById(R.id.youtubeImageView);
//			textTitleShort = (TextView)view.findViewById(R.id.youtubeTitleShort);
//			textTitleLong = (TextView)view.findViewById(R.id.youtubeTitleLong);
//			textDuration = (TextView)view.findViewById(R.id.youtubeDuration);
//			textvViewCount = (TextView)view.findViewById(R.id.youtubeViewCount);
//		} catch( ClassCastException e ) {
//			Log.e("Youtube related videos", "Your layout must provide an image and a text view with ID's icon and text.", e);
//			throw e;
//		}
//		Bitmap cachedImage = null;
//		try{
//			cachedImage = imageLoader.loadImage((String) map.get("youtubeImageView"), new ImageLoadedListener() {	
//				public void imageLoaded(Bitmap imageBitmap) {
//					image.setImageBitmap(imageBitmap);
//					Log.d("Youtube related videos", "youtube network image loaded");
//				}
//				
//			});
//			
//		} catch (MalformedURLException e) {
//			Log.e("Youtube related videos", "Bad remote image URL: " + e.toString());
//		}
//		textId.setText((String) map.get("youtubeId"));
//		textTitleShort.setText((String) map.get("youtubeTitleShort"));
//		textTitleLong.setText((String) map.get("youtubeTitleLong"));
//		textDuration.setText((String) map.get("youtubeDuration"));
//		textvViewCount.setText((String)map.get("youtubeViewCount"));
//		if( cachedImage != null ) {
//			image.setImageBitmap(cachedImage);
//		}else{
////			image.setImageResource(R.drawable.icon);
//		}
//		
//		view.setOnClickListener((new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View pV) {
//				
//				TextView tid = (TextView)pV.findViewById(R.id.youtubeId);
//				TextView tTitleShort = (TextView)pV.findViewById(R.id.youtubeTitleShort);
////				TextView tTitleLong = (TextView)pV.findViewById(R.id.youtubeTitleLong);
////				TextView tDuration = (TextView)pV.findViewById(R.id.youtubeDuration);
//				TextView tvViewCount = (TextView)pV.findViewById(R.id.youtubeViewCount);
//
//
//				videoId  = tid.getText().toString();
//				
//				if(videoId == null || videoId.trim().equals("")){
//					return;
//				}
//				playYoutubeVideo(videoId);
//				
//				Log.i("Click","Item:"+videoId);
//	            
//	            centralTitle.setText(tTitleShort.getText().toString());
//	            
//	            textViewCount = (TextView) findViewById(R.id.youtubeViewCountBig);
//	            textViewCount.setText(tvViewCount.getText().toString());
//	            
//	            imageView = (ImageView) findViewById(R.id.youtubeBigImageView);
//	            imagePlay = (ImageView) findViewById(R.id.youtubeBigImagePlay);
//	    	    Bitmap cachedImage = null;
//	    	    try {
//	    	    	cachedImage = imageLoader.loadImage("http://i.ytimg.com/vi/"+videoId+"/0.jpg", new ImageLoadedListener() {	
//	    	    		public void imageLoaded(Bitmap imageBitmap) {
//	    	    			imageView.setImageBitmap(imageBitmap);
//	    	    			imagePlay.setVisibility(View.GONE);
//	    	    			imagePlay.setVisibility(View.VISIBLE);
//	    	    			//notifyDataSetChanged();            
//	    	    			
//	    	    			}
//	    	    		});
//	    	    	if(cachedImage!=null){
//	    	    		imageView.setImageBitmap(cachedImage);
//	    	    		imagePlay.setVisibility(View.GONE);
//	    	    		imagePlay.setVisibility(View.VISIBLE);
//	    	    	}
//	    	    } catch (MalformedURLException e) {
//	    	    	Log.e("URL", "Bad remote image URL: " + e.toString());
//	    	    }
//			}
//		}));
//		
//		int v = 3;
//		int h = 6;
//		//add view and set padding
//        flowLayoutCentral.addView(view, new FlowLayout.LayoutParams(v, h));
//        
//        flag++;
//		if(flag>=3)
//		{
//	        //item height=122;padding=6
//	        flowLayoutCentral.addLayoutHeight(122 + h);
//	        flag-=3;
//		}
	}
	
	DecimalFormat format = new DecimalFormat("#,###,###,###,###"); 
	public String getViewCountFormat(int viewcount)
	{
		String view = format.format(viewcount);
		return view;
	}
	
	public String replaceBigViewImage(String s)
	{
		return s.replaceAll("2.jpg", "0.jpg");
	}

	public void setContainer(MeepYoutubeActivity act){
		currentMeepYoutubeActivity = act;
	}

	@Override
	public void youtubeRestCallback_success(YoutubeDataFeedback feedback) {
		//Log.e(TAG,"youtubeRestCallback_success: feedback="+feedback.toString());
    	
		dialog.dismiss();

    	ArrayList<VideosData> dataList = feedback.getVideos();
    	total = dataList.size();
		if(total<=0){
			YouTubeUtility.alertMessage(currentMeepYoutubeActivity, R.string.youtube_title_search_results, R.string.youtube_info_results_blocked);
			return;
		}
		
    	
        SEARCHED_REC = total;
        title = new String[total];
        id = new String[total];
        duration = new int[total];
        photo = new String[total];
        small_thumbnail = new String[total];
        large_thumbnail = new String[total];
  


        
		for (int i = 0; i < total; i++) {
			//Log.e(TAG,"youtubeRestCallback_success: title="+dataList.get(i).get_video_id());
        	title[i] = dataList.get(i).get_title();
        	id[i] = dataList.get(i).get_video_id();
        	duration[i] = 0;
        	photo[i]= dataList.get(i).get_small_thumbnail();
        	small_thumbnail[i]= dataList.get(i).get_small_thumbnail();
        	large_thumbnail[i]= dataList.get(i).get_large_thumbnail();
		}	


		Message msg = new Message();
		msg.what = 1;
		Bundle bundle = new Bundle();  
		bundle.putStringArray("title", title);  
		bundle.putStringArray("id", id);  
		bundle.putIntArray("duration", duration);
		bundle.putInt("total", total);

		
		gridlist.clear();
		for(int i = 0 ;i<total ;i++){
			osItem = new OsListViewItem();
			osItem.setName(""+i);
			osItem.setCount(i+"");
			gridlist.add(osItem);
		};
		adapter = new MyAdapter();
		adapter.notifyDataSetChanged();
		myGrid.setAdapter(adapter);

		actionUpdateSearchResultUI(total,id,title);
		
		msg.setData(bundle);  
		mHandlerReadImg.sendMessage(msg);
		
		Log.e(TAG,"youtubeRestCallback_success: total="+total);
	}

	@Override
	public void youtubeRestCallback_fail(String errorContent) {
		Log.e(TAG,"youtubeRestCallback_fail: error="+errorContent);
		dialog.dismiss();
		YouTubeUtility.alertMessage(currentMeepYoutubeActivity, R.string.youtube_title_search_results, R.string.youtube_info_results_blocked);
	}
	
}
