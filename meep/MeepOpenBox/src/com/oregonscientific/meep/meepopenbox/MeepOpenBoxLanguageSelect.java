package com.oregonscientific.meep.meepopenbox;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Scanner;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.os.Bundle;

import com.oregonscientific.meep.SystemProperties;
import com.oregonscientific.meep.meepopenbox.view.MeepOpenBoxDialogFragment;
import com.oregonscientific.meep.meepopenbox.view.MeepOpenBoxLanguageArrayAdapter;
import com.oregonscientific.meep.meepopenbox.view.MeepOpenBoxViewManager;

import dalvik.system.DexFile;

/**
 * Activity for Language Select page
 * @author Charles
 */
public class MeepOpenBoxLanguageSelect extends MeepOpenBoxBaseActivity {

	private final String TAG = MeepOpenBoxLanguageSelect.class.getSimpleName();
	private static boolean firstRun = true;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.open_box_lang_select_layout);

		final ListView listView = (ListView) findViewById(R.id.langSelectListView);

		final LinkedHashMap<String, String> languageTable = new LinkedHashMap<String, String>();
		languageTable.put(getResources().getString(R.string.mainpage_selection_english), "en_US");
		languageTable.put(getResources().getString(R.string.mainpage_selection_french), "fr");
		languageTable.put(getResources().getString(R.string.mainpage_selection_italian), "it");
		languageTable.put(getResources().getString(R.string.mainpage_selection_spanish), "es");
		languageTable.put(getResources().getString(R.string.mainpage_selection_portuguese), "pt_BR");
		languageTable.put(getResources().getString(R.string.mainpage_selection_german), "de");
		//not using now..may be enable later
		languageTable.put(getResources().getString(R.string.mainpage_selection_japan), "ja");

		final MeepOpenBoxLanguageArrayAdapter adapter = new MeepOpenBoxLanguageArrayAdapter(this, Arrays.asList(languageTable.keySet().toArray(new String[languageTable.size()])));
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(
					AdapterView<?> parent,
					View view,
					int position,
					long id) {

				adapter.setSelectedPosition(String.valueOf(position));
				// change language immediately after choosing one.
				String language = loadLanguageString(listView, adapter.getSelectedPosition(), languageTable);
				if (language != null) {
					changeLanguage(language);
					writeLanguageFile(language);
				}
			}
		});
		listView.setAdapter(adapter);

		String position = MeepOpenBoxViewManager.getLanguagePreference(this);
		
		if(position != null && !firstRun)
		{
			adapter.setSelectedPosition(position);
		}
		else
		{
			firstRun = false;
			String language = SystemProperties.get("ro.product.locale.language");
			String region = SystemProperties.get("ro.product.locale.region");
	
			boolean matched = false;
	
			if (language != null && region != null) {
				int i = 0;
				String fullLanguageCode = language + "_" + region;
				for (String lang : languageTable.values()) {
					if (lang.equals(fullLanguageCode)) {
						adapter.setSelectedPosition(String.valueOf(i));
						matched = true;
					}
					i++;
				}
			}
	
			if (!matched && language != null) {
				int i = 0;
				for (String lang : languageTable.values()) {
					if (lang.equals(language)) {
						adapter.setSelectedPosition(String.valueOf(i));
						matched = true;
					}
					i++;
				}
			}
	
			if (!matched) {
				// still doesn't match, fallback to first item
				adapter.setSelectedPosition(String.valueOf(0));
			}
		}
		
		// change language
		String language = loadLanguageString(listView, adapter.getSelectedPosition(), languageTable);
		if (language != null) {
			changeLanguage(language);
			writeLanguageFile(language);
		}

		Button nextButton = (Button) findViewById(R.id.langSelectNextBtn);
		nextButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				String language = loadLanguageString(listView, adapter.getSelectedPosition(), languageTable);
				if (language != null) {
					MeepOpenBoxViewManager.goToNextPage(MeepOpenBoxLanguageSelect.this);
				}
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			finish();
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		Intent pageInMemory = MeepOpenBoxViewManager.getPageInPreference(this);
		if (pageInMemory != null && !pageInMemory.getComponent().getClassName().contains(MeepOpenBoxLanguageSelect.class.getSimpleName())) {
			pageInMemory.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivityForResult(pageInMemory, MeepOpenBoxViewManager.PAGE_LANGUAGE_SELECT);
			// finish();
		}
	}

	@Override
	public void onBackPressed() {
		// Disable back button
	}

	@Override
	public void setNextButtonEnabled(boolean enabled) {
		Button nextButton = (Button) findViewById(R.id.langSelectNextBtn);
		nextButton.setEnabled(enabled);
	}

	/**
	 * Loads the language string
	 * @param listView ListView
	 * @param position position of language in ListView
	 * @param languageTable HashMap of languages
	 * @return the abbreviation of language selected if any
	 */
	private String loadLanguageString(
			ListView listView,
			String position,
			LinkedHashMap<String, String> languageTable) {
		if (position != null && languageTable.containsKey(listView.getItemAtPosition(Integer.valueOf(position)).toString())) {
			return languageTable.get(listView.getItemAtPosition(Integer.valueOf(position)).toString());
		}
		DialogFragment newFragment = MeepOpenBoxDialogFragment.newInstance(MeepOpenBoxDialogFragment.LANGUAGE_NOT_SELECTED_DIALOG_ID);
		newFragment.show(getFragmentManager(), "dialog");
		setNextButtonEnabled(false);

		return null;
	}

	/**
	 * Changes the system language
	 * @param language the language to be applied
	 */
	@SuppressWarnings({ "rawtypes", "unused", "unchecked" })
	private void changeLanguage(String language) {
		if (language == null || language.compareToIgnoreCase(getResources().getConfiguration().locale.getLanguage()) == 0) {
			return;
		}

		try {
			DexFile dexfile = new DexFile(new File("/system/app/Settings.apk"));
			String libraryName = "com.android.settings.LocalePicker";
			ClassLoader classLoader = getClassLoader();
			Class localePicker = dexfile.loadClass(libraryName, classLoader);
			Class activityManagerNative = Class.forName("android.app.ActivityManagerNative");
			Class iActivityManager = Class.forName("android.app.IActivityManager");

			Method getDefault = activityManagerNative.getMethod("getDefault", null);
			Object activityManager = iActivityManager.cast(getDefault.invoke(activityManagerNative, null));

			Method getConfiguration = activityManager.getClass().getMethod("getConfiguration", null);

			Configuration configuration = (Configuration) getConfiguration.invoke(activityManager, null);
			Locale locale = new Locale(language);
			Locale.setDefault(locale);
			configuration.locale = locale;

			Class[] arguments = new Class[1];
			arguments[0] = Configuration.class;
			Method updateConfiguration = activityManager.getClass().getMethod("updateConfiguration", arguments);
			updateConfiguration.invoke(activityManager, configuration);

		} catch (Exception ex) {
			Log.e(TAG, "Cannot change language because " + ex.toString());
		}
	}

	/**
	 * Writes language file
	 */
	private void writeLanguageFile(String language) {
		try {
			String fileName = this.getFilesDir().getPath() + "/.locale";
			File file = new File(fileName.replace(".meepopenbox", ""));
			Writer writer = new BufferedWriter(new FileWriter(file));
			writer.write(language);
			writer.close();
		} catch (Exception ex) {
			Log.e(TAG, "Cannot write locale file because: " + ex.toString());
		}
	}

	/**
	 * Gets the language code saved in .locale file
	 * @return returns the iso language in file
	 */
	private String getLanguageFromFile() {
		String result = null;
		try {
			String fileName = this.getFilesDir().getPath();
			Scanner input = new Scanner(new File(fileName.replace(".meepopenbox", ""), ".locale"));
			while (input.hasNext()) {
				result = input.next();
			}
			input.close();
		} catch (Exception ex) {
			Log.e(TAG, "Cannot read language file because: " + ex.toString());
		}
		return result;
	}

}
