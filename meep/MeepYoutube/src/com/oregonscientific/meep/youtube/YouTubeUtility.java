package com.oregonscientific.meep.youtube;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.FactoryConfigurationError;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.oregonscientific.meep.account.Account;
import com.oregonscientific.meep.youtube.BuildConfig;
import com.oregonscientific.meep.youtube.Consts;
import com.oregonscientific.meep.youtube.R;
import com.oregonscientific.meep.customdialog.CommonPopup;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class YouTubeUtility {
	
	
	public static boolean isValidDomain(String url) {
		String regex = new StringBuilder().append("((?:(http|https|Http|Https|rtsp|Rtsp):").append("\\/\\/(?:(?:[a-zA-Z0-9\\$\\-\\_\\.\\+\\!\\*\\'\\(\\)").append("\\,\\;\\?\\&\\=]|(?:\\%[a-fA-F0-9]{2})){1,64}(?:\\:(?:[a-zA-Z0-9\\$\\-\\_").append("\\.\\+\\!\\*\\'\\(\\)\\,\\;\\?\\&\\=]|(?:\\%[a-fA-F0-9]{2})){1,25})?\\@)?)?").append("((?:(?:[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}\\.)+") // named
																																																																																															// host
		.append("(?:") // plus top level domain
		.append("(?:aero|arpa|asia|a[cdefgilmnoqrstuwxz])").append("|(?:biz|b[abdefghijmnorstvwyz])").append("|(?:cat|com|coop|c[acdfghiklmnoruvxyz])").append("|d[ejkmoz]").append("|(?:edu|e[cegrstu])").append("|f[ijkmor]").append("|(?:gov|g[abdefghilmnpqrstuwy])").append("|h[kmnrtu]").append("|(?:info|int|i[delmnoqrst])").append("|(?:jobs|j[emop])").append("|k[eghimnrwyz]").append("|l[abcikrstuvy]").append("|(?:mil|mobi|museum|m[acdghklmnopqrstuvwxyz])").append("|(?:name|net|n[acefgilopruz])").append("|(?:org|om)").append("|(?:pro|p[aefghklmnrstwy])").append("|qa").append("|r[eouw]").append("|s[abcdeghijklmnortuvyz]").append("|(?:tel|travel|t[cdfghjklmnoprtvwz])").append("|u[agkmsyz]").append("|v[aceginu]").append("|w[fs]").append("|y[etu]").append("|z[amw]))").append("|(?:(?:25[0-5]|2[0-4]") // or
																																																																																																																																																																																																										// ip
																																																																																																																																																																																																										// address
		.append("[0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(?:25[0-5]|2[0-4][0-9]").append("|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(?:25[0-5]|2[0-4][0-9]|[0-1]").append("[0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(?:25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}").append("|[1-9][0-9]|[0-9])))").append("(?:\\:\\d{1,5})?)") // plus
																																																																										// option
																																																																										// port
																																																																										// number
		.append("(\\/(?:(?:[a-zA-Z0-9\\;\\/\\?\\:\\@\\&\\=\\#\\~") // plus
																	// option
																	// query
																	// params
		.append("\\-\\.\\+\\!\\*\\'\\(\\)\\,\\_])|(?:\\%[a-fA-F0-9]{2}))*)?").append("(?:\\b|$)").toString();
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(url);
		return matcher.matches();
	}

	public static void hideKeyboard(Context context, View v) {
		// hide keyboard
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		// networkInfo will be null if no network is available, otherwise
		// connected
		if (networkInfo != null && networkInfo.isConnected()) {
			return true;
		}
		return false;
	}
	
	
	
	public static String queryLatestPlaylistVideo(PlaylistId pPlaylistId)
			throws IOException, ClientProtocolException,
			FactoryConfigurationError {

		String lVideoId = null;

		HttpClient lClient = new DefaultHttpClient();
		
		HttpGet lGetMethod = new HttpGet(OpenYouTubePlayerActivity.YOUTUBE_PLAYLIST_ATOM_FEED_URL + 
										 pPlaylistId.getId()+"?v=2&max-results=50&alt=json");
		
		HttpResponse lResp = null;

		lResp = lClient.execute(lGetMethod);
		
		ByteArrayOutputStream lBOS = new ByteArrayOutputStream();
		String lInfoStr = null;
		JSONObject lYouTubeResponse = null;
		
		try {
			
			lResp.getEntity().writeTo(lBOS);
			lInfoStr = lBOS.toString("UTF-8");
			lYouTubeResponse = new JSONObject(lInfoStr);
			
			 JSONArray lEntryArr = lYouTubeResponse.getJSONObject("feed").getJSONArray("entry");
			 JSONArray lLinkArr = lEntryArr.getJSONObject(lEntryArr.length()-1).getJSONArray("link");
			for(int i=0; i<lLinkArr.length(); i++){
				JSONObject lLinkObj = lLinkArr.getJSONObject(i);;
				String lRelVal = lLinkObj.optString("rel", null);
				if( lRelVal != null && lRelVal.equals("alternate")){
					
					String lUriStr = lLinkObj.optString("href", null);
					Uri lVideoUri = Uri.parse(lUriStr);
					lVideoId = lVideoUri.getQueryParameter("v");
					break;
				}
			}
		} catch (IllegalStateException e) {
			Log.i(YouTubeUtility.class.getSimpleName(), "Error retrieving content from YouTube", e);
		} catch (IOException e) {
			Log.i(YouTubeUtility.class.getSimpleName(), "Error retrieving content from YouTube", e);
		} catch(JSONException e){
			Log.i(YouTubeUtility.class.getSimpleName(), "Error retrieving content from YouTube", e);
		}

		return lVideoId;
	}

	
	/**
	 * Calculate the YouTube URL to load the video.  Includes retrieving a token that YouTube
	 * requires to play the video.
	 * 
	 * @param pYouTubeFmtQuality quality of the video.  17=low, 18=high
	 * @param bFallback whether to fallback to lower quality in case the supplied quality is not available
	 * @param pYouTubeVideoId the id of the video
	 * @return the url string that will retrieve the video
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws UnsupportedEncodingException
	 */
	public static String calculateYouTubeUrl(String pYouTubeFmtQuality, boolean pFallback,
			String pYouTubeVideoId) throws IOException,
			ClientProtocolException, UnsupportedEncodingException {

		String lUriStr = null;
		HttpClient lClient = new DefaultHttpClient();
		
		HttpGet lGetMethod = new HttpGet(OpenYouTubePlayerActivity.YOUTUBE_VIDEO_INFORMATION_URL + 
										 pYouTubeVideoId);
		
		HttpResponse lResp = null;

		lResp = lClient.execute(lGetMethod);
			
		ByteArrayOutputStream lBOS = new ByteArrayOutputStream();
		String lInfoStr = null;
			
		lResp.getEntity().writeTo(lBOS);
		lInfoStr = new String(lBOS.toString("UTF-8"));
		
		String[] lArgs=lInfoStr.split("&");
		Map<String,String> lArgMap = new HashMap<String, String>();
		for(int i=0; i<lArgs.length; i++){
			String[] lArgValStrArr = lArgs[i].split("=");
			if(lArgValStrArr != null){
				if(lArgValStrArr.length >= 2){
					lArgMap.put(lArgValStrArr[0], URLDecoder.decode(lArgValStrArr[1]));
				}
			}
		}
		
		//Find out the URI string from the parameters
		
		//Populate the list of formats for the video
		String lFmtList = URLDecoder.decode(lArgMap.get("fmt_list"));
		ArrayList<Format> lFormats = new ArrayList<Format>();
		if(null != lFmtList){
			String lFormatStrs[] = lFmtList.split(",");
			
			for(String lFormatStr : lFormatStrs){
				Format lFormat = new Format(lFormatStr);
				lFormats.add(lFormat);
			}
		}
		
		//Populate the list of streams for the video
		String lStreamList = lArgMap.get("url_encoded_fmt_stream_map");
		if(null != lStreamList){
			String lStreamStrs[] = lStreamList.split(",");
			ArrayList<VideoStream> lStreams = new ArrayList<VideoStream>();
			for(String lStreamStr : lStreamStrs){
				VideoStream lStream = new VideoStream(lStreamStr);
				lStreams.add(lStream);
			}	
			
			//Search for the given format in the list of video formats
			// if it is there, select the corresponding stream
			// otherwise if fallback is requested, check for next lower format
			int lFormatId = Integer.parseInt(pYouTubeFmtQuality);
			
			Format lSearchFormat = new Format(lFormatId);
			while(!lFormats.contains(lSearchFormat) && pFallback ){
				int lOldId = lSearchFormat.getId();
				int lNewId = getSupportedFallbackId(lOldId);
				
				if(lOldId == lNewId){
					break;
				}
				lSearchFormat = new Format(lNewId);
			}
			
			int lIndex = lFormats.indexOf(lSearchFormat);
			if(lIndex >= 0){
				VideoStream lSearchStream = lStreams.get(lIndex);
				lUriStr = lSearchStream.getUrl();
			}
			
		}		
		//Return the URI string. It may be null if the format (or a fallback format if enabled)
		// is not found in the list of formats for the video
		return lUriStr;
	}

	public static boolean hasVideoBeenViewed(Context pCtxt, String pVideoId) {
		SharedPreferences lPrefs = PreferenceManager.getDefaultSharedPreferences(pCtxt);

		String lViewedVideoIds = lPrefs.getString("com.keyes.screebl.lastViewedVideoIds", null);
		
		if(lViewedVideoIds == null){
			return false;
		}
		
		String[] lSplitIds =lViewedVideoIds.split(";");  
		if(lSplitIds == null || lSplitIds.length == 0){
			return false;
		}
		
		for(int i=0; i<lSplitIds.length; i++){
			if(lSplitIds[i] != null && lSplitIds[i].equals(pVideoId)){
				return true;
			}
		}
		
		return false;

	}
	
	public static void markVideoAsViewed(Context pCtxt, String pVideoId){
		
		SharedPreferences lPrefs = PreferenceManager.getDefaultSharedPreferences(pCtxt);

		if(pVideoId == null){
			return;
		}
		
		String lViewedVideoIds = lPrefs.getString("com.keyes.screebl.lastViewedVideoIds", null);

		if(lViewedVideoIds == null){
			lViewedVideoIds = "";
		}
		
		String[] lSplitIds =lViewedVideoIds.split(";");  
		if(lSplitIds == null){
			lSplitIds = new String[]{};
		}
		
		// make a hash table of the ids to deal with duplicates
		Map<String, String> lMap = new HashMap<String, String>();
		for(int i=0; i<lSplitIds.length; i++){
			lMap.put(lSplitIds[i], lSplitIds[i]);
		}
		
		// recreate the viewed list
		String lNewIdList = "";
		Set<String> lKeys = lMap.keySet();
		Iterator<String> lIter = lKeys.iterator();
		while(lIter.hasNext()){
			String lId = lIter.next();
			if( ! lId.trim().equals("")){
				lNewIdList += lId + ";";
			}
		}
		
		// add the new video id
		lNewIdList += pVideoId + ";";
		
		Editor lPrefEdit = lPrefs.edit();
		lPrefEdit.putString("com.keyes.screebl.lastViewedVideoIds", lNewIdList);
		lPrefEdit.commit();
		
	}
	
	public static int getSupportedFallbackId(int pOldId){
		final int lSupportedFormatIds[] = {13,  //3GPP (MPEG-4 encoded) Low quality 
										  17,  //3GPP (MPEG-4 encoded) Medium quality 
										  18,  //MP4  (H.264 encoded) Normal quality
										  22,  //MP4  (H.264 encoded) High quality
										  37   //MP4  (H.264 encoded) High quality
										  };
		int lFallbackId = pOldId;
		for(int i = lSupportedFormatIds.length - 1; i >= 0; i--){
			if(pOldId == lSupportedFormatIds[i] && i > 0){
				lFallbackId = lSupportedFormatIds[i-1];
			}			
		}
		return lFallbackId;
	}
	
	

	public static String getDirs(String path) {
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return path;
	}

	public static String readFile(String path) {
		// Get the text file
		File file = new File(path);
		String line = null;
		// Read text from file
		StringBuilder text = new StringBuilder();

		try {
			BufferedReader br = new BufferedReader(new FileReader(file));

			while ((line = br.readLine()) != null) {
				text.append(line);
			}
		} catch (IOException e) {
			// You'll need to add proper error handling here
		}
		Log.d("test", text.toString());
		return text.toString();
	}


	public static String getAccountToken(Context context) {
		SharedPreferences sp = context.getSharedPreferences(Consts.PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
		return sp.getString(Consts.PREFERENCE_KEY_TOKEN, "");
	}

//	public static String getAccountMeeptag(Context context) {
//		SharedPreferences sp = context.getSharedPreferences(Consts.PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
//		return sp.getString(Consts.PREFERENCE_KEY_MEEPTAG, "");
//	}
	public static String getAccountID(Context context) {
		SharedPreferences sp = context.getSharedPreferences(Consts.PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
		if(sp!=null){
			return sp.getString(Consts.PREFERENCE_KEY_ID, "");
		}else{
			return "";
		}
	}

	public static void setAccountInformation(Context context, Account account) {
		SharedPreferences sp = context.getSharedPreferences(Consts.PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.clear();
		edit.putString(Consts.PREFERENCE_KEY_TOKEN, account.getToken());
		edit.putString(Consts.PREFERENCE_KEY_FIRSTNAME, account.getFirstName());
		edit.putString(Consts.PREFERENCE_KEY_LASTNAME, account.getLastName());
		edit.putString(Consts.PREFERENCE_KEY_MEEPTAG, account.getMeepTag());
		edit.putString(Consts.PREFERENCE_KEY_ID, account.getId());
		edit.commit();
	}

	public static void alertMessage(Context context, String title,
			String message) {
		CommonPopup popup = new CommonPopup(context, title, message);
		// popup.show();
		popup.setTextGravity(Gravity.CENTER_VERTICAL);
		popup.show();
	}

	public static void alertMessage(Context context, int title, int message) {
		CommonPopup popup = new CommonPopup(context, title, message);
		// popup.show();
		popup.setTextGravity(Gravity.CENTER_VERTICAL);
		popup.show();
	}

	public static void alertDebugMessage(Context context, String message) {
		CommonPopup popup = new CommonPopup(context, "DEBUG", "FOR DEBUG: "
				+ message);
		// popup.show();
		popup.setTextGravity(Gravity.CENTER_VERTICAL);
		popup.show();
	}

	public static void printLogcatDebugMessage(String text) {
		if(BuildConfig.DEBUG)
			Log.d("DEBUG", text);
	}

	public static Message generateAlertMessageObject() {
		Bundle bundle = new Bundle();
		bundle.putInt(Consts.BUNDLE_KEY_ALERT_MESSAGE_CONTENT, R.string.cannot_use_now);
		Message message = new Message();
		message.what = Consts.MESSAGE_WHAT_ALERT_MESSAGE_CONTENT;
		message.setData(bundle);
		return message;

	}
	
	public static void openWifiSettings(Context context) {

		final Intent intent = new Intent(Intent.ACTION_MAIN, null);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		final ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.wifi.WifiSettings");
		intent.setComponent(cn);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

}
