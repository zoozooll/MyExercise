package com.oregonscientific.meep.meepopenbox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.oregonscientific.meep.meepopenbox.view.MeepOpenBoxDialogFragment;
import com.oregonscientific.meep.meepopenbox.view.MeepOpenBoxTimeZoneArrayAdapter;
import com.oregonscientific.meep.meepopenbox.view.MeepOpenBoxViewManager;

/**
 * Activity for Time Zone Select page
 * @author Charles
 */
public class MeepOpenBoxTimeZoneSelect extends MeepOpenBoxBaseActivity {
	
	public static final String TAG = MeepOpenBoxTimeZoneSelect.class.getSimpleName();
	public static final String GMT = "GMT";
	public static final String UTC = "UTC";
	
	private static final String XMLTAG_TIMEZONE = "timezone";
	private static final String KEY_ID = "id";
	private static final String KEY_DISPLAYNAME = "name";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.open_box_time_zone_select_layout);
		
		final ListView listView = (ListView) findViewById(R.id.timeZoneSelectListView);
		
		final ArrayList<String> timeZoneList = new ArrayList<String>();
		List<HashMap<String, String>> zones = getZones();
		for (HashMap<String, String> map : zones) {
			timeZoneList.add(map.get(KEY_DISPLAYNAME) + " " + getUTCBias(TimeZone.getTimeZone(map.get(KEY_ID))));
		}
		Collections.sort(timeZoneList, new TimeZoneComparator());
		
		final MeepOpenBoxTimeZoneArrayAdapter adapter = new MeepOpenBoxTimeZoneArrayAdapter(this, timeZoneList);
		listView.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(
					AdapterView<?> parent,
					View view,
					int position,
					long id) {
				
				adapter.setSelectedValue((String) listView.getItemAtPosition(position));
				adapter.notifyDataSetChanged();
			}
		});
		listView.setAdapter(adapter);
		Button backButton = (Button) findViewById(R.id.timeZoneSelectBackBtn);
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				onBackPressed();
			}
		});
		
		Button nextButton = (Button) findViewById(R.id.timeZoneSelectNextBtn);
		nextButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (checkTimeZone(timeZoneList, adapter.getSelectedValue())) {
					MeepOpenBoxViewManager.goToNextPage(MeepOpenBoxTimeZoneSelect.this);
				}
			}
		});
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			setResult(Activity.RESULT_OK);
			finish();
		}
	}
	
	@Override
	public void hideBackButton() {
		Button backButton = (Button) findViewById(R.id.timeZoneSelectBackBtn);
		backButton.setVisibility(View.INVISIBLE);
		TextView backButtonText = (TextView) findViewById(R.id.timeZoneSelectBackText);
		backButtonText.setVisibility(View.INVISIBLE);
	}
	
	@Override
	public void setNextButtonEnabled(boolean enabled) {
		Button nextButton = (Button) findViewById(R.id.timeZoneSelectNextBtn);
		nextButton.setEnabled(enabled);
	}
	
	private List<HashMap<String, String>> getZones() {
		List<HashMap<String, String>> myData = new ArrayList<HashMap<String, String>>();
		
		try {
			XmlResourceParser xrp = getResources().getXml(R.xml.timezones);
			while (xrp.next() != XmlResourceParser.START_TAG)
				;
			xrp.next();
			while (xrp.getEventType() != XmlResourceParser.END_TAG) {
				while (xrp.getEventType() != XmlResourceParser.START_TAG) {
					if (xrp.getEventType() == XmlResourceParser.END_DOCUMENT) {
						return myData;
					}
					xrp.next();
				}
				if (xrp.getName().equals(XMLTAG_TIMEZONE)) {
					String id = xrp.getAttributeValue(0);
					String displayName = xrp.nextText();
					HashMap<String, String> map = new HashMap<String, String>();
					map.put(KEY_ID, id);
					map.put(KEY_DISPLAYNAME, displayName);
					myData.add(map);
				}
				while (xrp.getEventType() != XmlResourceParser.END_TAG) {
					xrp.next();
				}
				xrp.next();
			}
			xrp.close();
		} catch (XmlPullParserException xppe) {
			Log.e(TAG, "Ill-formatted timezones.xml file");
		} catch (java.io.IOException ioe) {
			Log.e(TAG, "Unable to read timezones.xml file");
		}
		return myData;
	}
	
	/**
	 * Checks whether selected time zone can be set
	 * @param mLv ListView of time zone
	 * @param value name of time zone selected
	 * @return true if time zone is set, false otherwise
	 */
	private boolean checkTimeZone(ArrayList<String> timeZoneList, String value) {
		if (value != null && timeZoneList.contains(value)) {
			int pos = value.indexOf(GMT);
			if (pos != -1) {
				String timeZone = value.substring(pos, pos + 9);
				setTimeZone(timeZone);
			}
			return true;
		}
		
		DialogFragment newFragment = MeepOpenBoxDialogFragment.newInstance(MeepOpenBoxDialogFragment.TIMEZONE_NOT_SELECTED_DIALOG_ID);
		newFragment.show(getFragmentManager(), "dialog");
		setNextButtonEnabled(false);
		
		return false;
	}
	
	/**
	 * Sets time zone
	 * @param gmt Time Zone in format "GMT+00:00"
	 */
	private void setTimeZone(String gmt) {
		if (gmt != null) {
			AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
			alarm.setTimeZone(gmt);
			TimeZone.setDefault(TimeZone.getTimeZone(gmt));
		}
	}
	
	/**
	 * Gets UTC bias from a time zone
	 * @param timeZone time zone
	 * @return UTC bias of time zone
	 */
	private String getUTCBias(TimeZone timeZone) {
		String number = "+00";
		if (timeZone == null) {
			return "(" + GMT + number + ":00)"; 
		}
		
		boolean inDaylightTime = timeZone.inDaylightTime(new Date());
		int offset = timeZone.getRawOffset();
		offset = inDaylightTime ? offset + timeZone.getDSTSavings() : offset;
		
		String formatString = offset >= 0 ? "+%02d:%02d" : "-%02d:%02d";
		offset = Math.abs(offset);
		number = String.format(
				Locale.ENGLISH, 
				formatString, 
				TimeUnit.MILLISECONDS.toHours(offset), 
				TimeUnit.MILLISECONDS.toMinutes(offset) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(offset)));
		
		return "(" + GMT + number + ")";
	}
	
	private class TimeZoneComparator implements Comparator<String> {
		@Override
		public int compare(String stringA, String stringB) {
			if (stringA == null && stringB == null) {
				return 0;
			} else if (stringA != null && stringB == null) {
				return 1;
			} else if (stringA == null && stringB != null) {
				return -1;
			} else {
				return stringA.compareTo(stringB);
			}
		}
	}
	
}