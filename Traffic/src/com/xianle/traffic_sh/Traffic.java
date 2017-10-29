package com.xianle.traffic_sh;

import java.util.Enumeration;
import java.util.Vector;

import net.youmi.android.AdManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TableLayout;
import android.widget.TextView;



public class Traffic extends TabActivity {
	
	TabHost.TabSpec tab4;
	String mFile;
	ProgressDialog mDialog;
	static {
		AdManager.init("4ef94353630d245b", "1fc8b2d8877c7eee", 30, false, "1.0");
	}
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mFile = this.getIntent().getStringExtra(Globals.FILENAME);
		setTitle(getIntent().getStringExtra(Globals.Title));
		
		tabHost = getTabHost();

		LayoutInflater.from(this).inflate(R.layout.tab,
				tabHost.getTabContentView(), true);

		tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator(
				this.getString(R.string.interchage)).setContent(R.id.tab1));
		tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator(
				this.getString(R.string.line)).setContent(R.id.tab2));
		tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator(
				this.getString(R.string.station)).setContent(R.id.tab3));
		tab4 = tabHost.newTabSpec("tab4").setIndicator(
				this.getString(R.string.result)).setContent(R.id.tab4);
		tabHost.addTab(tab4);

		interSearchButton = (ImageButton) findViewById(R.id.tab1_b1);
		interSearchButton.setOnClickListener(mGoListener);

		lineSearchButton = (ImageButton) findViewById(R.id.tab2_b1);
		lineSearchButton.setOnClickListener(mGoListener);

		stationSearchButton = (ImageButton) findViewById(R.id.tab3_b1);
		stationSearchButton.setOnClickListener(mGoListener);

		start = (EditText) findViewById(R.id.tab1_et1);
		end = (EditText) findViewById(R.id.tab1_et2);
		line = (EditText) findViewById(R.id.tab2_et1);
		station = (EditText) findViewById(R.id.tab3_et1);
		mDialog = CreateDialog();
		mDialog.show();

		m = new Model(this, mFile);
		Thread t = new Thread(m);
		t.start();

	}

	protected ProgressDialog CreateDialog() {
		ProgressDialog dialog = new ProgressDialog(this);
		dialog
				.setMessage(this.getResources().getString(
						R.string.pregress_diag));
		dialog.setIndeterminate(true);
		dialog.setCancelable(false);
		return dialog;
	}
    
    

	/**
	 * when button click , run this function
	 * */
	private OnClickListener mGoListener = new OnClickListener() {
		public void onClick(View v) {
			String s1, s2;
			if (v == (View) interSearchButton) {

				s1 = start.getText().toString();
				s2 = end.getText().toString();
				if(s1.trim().length() == 0 ||
						s2.trim().length() == 0)
					return;
				if (!checkTextValid(s1) || !checkTextValid(s2)
						|| (s1.indexOf(s2) != -1) || (s2.indexOf(s1) != -1)) {
					showDialog1(DIALOG_YES_NO_MESSAGE);
					return;
				}
				processinterSearch(s1, s2);

			} else if (v == (View) lineSearchButton) {

				s1 = line.getText().toString();
				if(s1.trim().length() == 0)
					return;
				if (!checkTextValid(s1)) {
					showDialog1(DIALOG_YES_NO_MESSAGE);
					return;
				}
				processRoadSearch(s1);
			} else if (v == (View) stationSearchButton) {

				s1 = station.getText().toString();
				if(s1.trim().length() == 0)
					return;
				if (!checkTextValid(s1)) {
					showDialog1(DIALOG_YES_NO_MESSAGE);
					return;
				}
				processStationSearch(s1);
			} else {
				Log.e(Globals.TAG, "error");
			}
		}
	};

	void showDialog1(int id) {
		CreateDialog(id).show();
	}

	protected Dialog CreateDialog(int id) {
		switch (id) {
		case DIALOG_YES_NO_MESSAGE:
			return new AlertDialog.Builder(this).setIcon(
					R.drawable.alert_dialog_icon).setTitle(R.string.sorry)
					.setMessage(R.string.find_nothing).setPositiveButton(
							R.string.conform,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

									/* User clicked OK so do some stuff */
								}
							}).create();
		case DIALOG_LIST:
			return new AlertDialog.Builder(Traffic.this).setTitle(
					Html.fromHtml(list_title)).setItems(road_list,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {

							draw_road_table(road_list[which], false);
							tabHost.setCurrentTabByTag("tab4");
						}
					}).create();
		case DIALOG_LIST1:
			return new AlertDialog.Builder(Traffic.this).setTitle(
					R.string.suggst_road).setItems(road_list,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {

							draw_road_table1(which);
							tabHost.setCurrentTabByTag("tab4");
							dialog.cancel();
						}
					}).create();
		case DIALOG_LIST_FOR_ROAD:
			return new AlertDialog.Builder(Traffic.this).setTitle(
					Html.fromHtml(list_title)).setItems(station_list,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {

							draw_road_table(station_list[which], false);
							tabHost.setCurrentTabByTag("tab4");
						}
					}).create();
		}
		return null;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// call the parent to attach any system level menus
		super.onCreateOptionsMenu(menu);
		int base = Menu.FIRST; // value is 1
		// menu.add(base, base, base, this.getString(R.string.exit));
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == Menu.FIRST) {
			this.finish();
		}
		// should return true if the menu item
		// is handled
		return true;
	}

	String list_title;

	private void draw_road_table(String road, boolean append) {
		String st = "";
		TextView line, upload, download;

		table = (TableLayout) findViewById(R.id.menu);
		if (table == null) {
			Log.e(Globals.TAG, "tab is null");
		}
		table.setColumnShrinkable(1, true);

		((TextView) findViewById(R.id.tab4_h2))
				.setText(getString(R.string.line));
		((TextView) findViewById(R.id.tab4_h3))
				.setText(getString(R.string.upload));
		((TextView) findViewById(R.id.tab4_h4))
				.setText(getString(R.string.download));

		line = (TextView) findViewById(R.id.tab4_et2);
		upload = (TextView) findViewById(R.id.tab4_et3);
		download = (TextView) findViewById(R.id.tab4_et4);

		line.setText(road);
		final String[] ss = m.get_stations_for_one_road(road);
		if (ss != null) {
			upload.setText(ss[0]);
			download.setText(ss[1]);// "++"
		}
		final String time = m.getTimeforOneRoad(road);
		if (time != null) {
			((TextView) findViewById(R.id.tab4_h5))
					.setText(getString(R.string.time));
			((TextView) findViewById(R.id.tab4_et5)).setText(time);
		}
		// final String mimeType = "text/html";
		// final String encoding = "utf-8";
		// WebView wv;
		//       
		// wv.loadData("<a href='x'>Hello World! - 1</a>", mimeType, encoding);

	}

	private boolean checkTextValid(String s) {
		if (s.trim().length() == 0)
			return false;
		if (s.trim().equals("") || !validateString(s)) {
			return false;
		}
		return true;
	}

	private void draw_station_road_table(String road) {
		String st = "";
		Enumeration seq;

		Vector vv = m.get_stations_for_one_road_not_care_direction(road);
		if (vv == null)
			return;
		seq = vv.elements();
		// get station for forward
		while (seq.hasMoreElements()) {
			if (st.trim().equals("")) {
				st = (String) seq.nextElement();
			} else {
				st = st + "-" + (String) seq.nextElement();
			}
		}
		// #style roaditem
		/*
		 * StringItem rr = new StringItem(road, st); this.tabbedForm.append(3,
		 * rr);
		 */
	}

	private void draw_filter_list(Vector v) {

		Enumeration seq = v.elements();
		int j = 0;
		// ChoiceItem ci;
		road_list = new String[v.size()];

		while (seq.hasMoreElements()) {
			road_list[j++] = (String) seq.nextElement();
		}
		showDialog1(DIALOG_LIST);
	}

	/*
	 * show a roads dialog which pass the station
	 */
	String[] station_list;

	private void draw_filter_list3(Vector v) {

		Enumeration seq = v.elements();
		int j = 0;
		// ChoiceItem ci;
		station_list = new String[v.size()];

		while (seq.hasMoreElements()) {
			station_list[j++] = (String) seq.nextElement();
		}
		showDialog1(DIALOG_LIST_FOR_ROAD);
	}

	private void processRoadSearch(String s) {

		Vector v;
		v = m.getSuggestRoads(s);

		if (v.size() > 1) {
			this.list_title = this.getString(R.string.select_one_road);
			draw_filter_list(v);
			// draw_road_table((String)v.elementAt(0),false);
		} else if (v.size() == 1) {
			draw_road_table((String) v.elementAt(0), false);
			// this.tabbedForm.setActiveTab(3);
			this.tabHost.setCurrentTabByTag("tab4");
			// this.display.setCurrent(tabbedForm);
		} else {
			// no road match show alert
			showDialog1(DIALOG_YES_NO_MESSAGE);
		}

	}

	/**
	 * @param s
	 *            the station name
	 */
	private void processStationSearch(String s) {
		Vector mm;

		s = s.trim();

		mm = m.get_road_from_station_key(s);

		if ((mm.size() == 0)) {
			this.showDialog1(DIALOG_YES_NO_MESSAGE);
			return;
		}

		String resultsTextFormat = getString(R.string.station_from_line);
		this.list_title = String.format(resultsTextFormat, s);
		// CharSequence styledResults = Html.fromHtml(resultsText);
		draw_filter_list3(mm);

		return;
	}

	private boolean validateString(String s) {
		if (s.trim().equals(""))
			return false;
		if (s.indexOf('?') != -1) {
			return false;
		}
		return true;
	}

	/**
	 * @param sa
	 *            the first station name
	 * @param sb
	 *            the second station name
	 */
	Vector[] vv;

	@SuppressWarnings("unchecked")
	private void processinterSearch(String sa, String sb) {

		vv = m.get_road_for_inter_station(sa, sb);

		if (vv == null) {
			this.showDialog1(DIALOG_YES_NO_MESSAGE);
			return;
		}

		Enumeration seq = vv[2].elements(); // middle station
		Enumeration seq1 = vv[0].elements(); // first road
		Enumeration seq2 = vv[1].elements(); // second road
		String road;
		String road2;
		String middleStation;

		road_list = new String[vv[0].size()];
		int j = 0;
		// get station for forward
		while (seq.hasMoreElements()) {
			middleStation = (String) seq.nextElement();
			if (middleStation.compareTo("same") == 0) {
				// two station has in one road

				road = (String) seq1.nextElement();
				draw_road_table(road, false);
				// this.tabbedForm.setActiveTab(3);
				this.tabHost.setCurrentTabByTag("tab4");
				return;
			}
			// shoule print two road

			road = (String) seq1.nextElement();
			road2 = (String) seq2.nextElement();
			road_list[j++] = road + " -> " + road2;

		}
		this.showDialog1(DIALOG_LIST1);
	}

	private void draw_road_table1(int which) {

		TextView line, upload, download;

		table = (TableLayout) findViewById(R.id.menu);
		if (table == null) {
			Log.e(Globals.TAG, "tab is null");
		}
		table.setColumnShrinkable(1, true);

		((TextView) findViewById(R.id.tab4_h2))
				.setText(getString(R.string.start_end));
		((TextView) findViewById(R.id.tab4_h3))
				.setText(getString(R.string.chufa1));
		((TextView) findViewById(R.id.tab4_h4))
				.setText(getString(R.string.daoda1));
		((TextView) findViewById(R.id.tab4_h5)).setText("");
		((TextView) findViewById(R.id.tab4_et5)).setText("");
		line = (TextView) findViewById(R.id.tab4_et2);
		upload = (TextView) findViewById(R.id.tab4_et3);
		download = (TextView) findViewById(R.id.tab4_et4);

		String line0 = (String) vv[0].elementAt(which);
		String line1 = (String) vv[1].elementAt(which);
		String start = (String) vv[3].elementAt(which);
		String middle = (String) vv[2].elementAt(which);
		String end = (String) vv[4].elementAt(which);

		line.setText(start + " -> " + end);
		CharSequence styledResults = Html.fromHtml(line0 + "\n\n"
				+ getString(R.string.chufa) + ":  " + start + "\n"
				+ getString(R.string.daoda) + ":  " + middle);
		upload.setText(styledResults);
		styledResults = Html.fromHtml(line1 + "\n\n"
				+ getString(R.string.chufa) + ":  " + middle + "\n"
				+ getString(R.string.daoda) + ":  " + end);
		download.setText(styledResults);

	}

	private static final int DIALOG_YES_NO_MESSAGE = 1;
	private static final int DIALOG_LIST = 2;
	private static final int DIALOG_LIST1 = 3;
	private static final int DIALOG_LIST_FOR_ROAD = 4;

	private TabHost tabHost;
	private TableLayout table;
	private ImageButton interSearchButton;
	private ImageButton lineSearchButton;
	private ImageButton stationSearchButton;
	private EditText start, end;
	private EditText line, station;
	Model m;
	String[] road_list;

}