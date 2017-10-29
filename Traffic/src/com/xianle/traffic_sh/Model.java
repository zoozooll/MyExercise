package com.xianle.traffic_sh;

//import java.util.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Stack;
import java.util.Vector;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

//import java.lang.*;

public class Model implements Runnable {

	// this hash table store bellow:
	// line name => line stations eg.
	// 1 => a-b-c-d-e-
	final  Hashtable<String, String> road = new Hashtable<String, String>();
	// this hash store line name to time info. the time info is just the string
	// after
	// colon (:) for every line
	// like this
	// 1 =>{8:00 12:00} firt staton {8:00 12:00} last staion
	final  Hashtable<String, String> road_time = new Hashtable<String, String>();
	public final static int MAX_NUM = 50;

	private String file;
	private Traffic parent;

	public Model(Traffic parent, String file) {
		this.parent = parent;
		this.file = file;

		return;
	}

	@Override
	public void run() {
		initRoadHash();
		class CallBack implements Runnable {
			public void run() {
				parent.mDialog.dismiss();
			}
		}
		CallBack cb = new CallBack();
		parent.runOnUiThread(cb);
	}

	public String getSationsForOneRoad(String lineName) {
		return (String) (road.get((Object) lineName));

	}

	public String getTimeforOneRoad(String lineName) {
		String time = (String) (road_time.get((Object) lineName));
		if (time == null) {
			time = (String) (road_time.get(lineName + "a"));
		}
		return time;
	}

	public Vector getSuggestRoads(String s) {

		Enumeration key_of_roads;
		String road;
		int i = 0;
		Vector temp = new Vector();

		// String mmm=new String(s.getBytes(),1,s.length()-1);
		String mmm = s.trim();
		key_of_roads = this.road.keys();

		while (key_of_roads.hasMoreElements()) {
			road = (String) key_of_roads.nextElement();
			// just skip the string end with b
			if (road.endsWith("b") == true)
				continue;
			if (road.startsWith(mmm) == true) {
				if (road.endsWith("a") == true) {
					temp.addElement((Object) road.substring(0,
							road.length() - 1));
				} else {
					temp.addElement((Object) road.toString());
				}
				i++;
			}

		}

		return temp;
	}

	/**
	 * @param sa
	 *            :the name of road
	 * @raturn a vector array vector[0]is the sequnce of sation for start to
	 *         end,vector[1] is a stack ,whitch is the sequnce of station from
	 *         start to end, you can you popup() to get sequnce from end to
	 *         start
	 */
	public String[] get_stations_for_one_road(String s) {
		String[] vv = new String[2];
		String stations = null;

		s = s.trim();
		if (s.equals(""))
			return null;

		stations = this.getSationsForOneRoad(s);

		if (stations != null) {
			// go here mean that the key is not end with 'a' or 'b'
			int index;
			String x;
			Stack down_road = new Stack();

			vv[0] = stations.toString();
			while ((index = stations.indexOf("-")) > 0) {
				x = stations.substring(0, index);
				down_road.addElement(x);
				stations = stations.substring(index + 1);
			}

			String st = "";
			while (!down_road.empty()) {
				if (st.trim().equals("")) {
					st = (String) down_road.pop();
				} else {
					st = st + "-" + (String) down_road.pop();
				}
			}
			vv[1] = st;
			return vv;

		} else {
			// go here mean that hashtable has two entry for this road
			// one is end with a,another is end with b
			vv[0] = getSationsForOneRoad(s + "a");
			if (vv[0] == null)
				return null;
			vv[1] = getSationsForOneRoad(s + "b");
			if (vv[1] == null)
				return null;
			return vv;
		}

	}

	/*
	 * just like get_stations_for_one_road function but just return all the
	 * stations this road have
	 */
	public Vector get_stations_for_one_road_not_care_direction(String s) {
		String stations = null;

		s = s.trim();
		if (s.equals(""))
			return null;

		stations = this.getSationsForOneRoad(s);

		if (stations == null) {
			// go here mean that hashtable has two entry for this road
			// one is end with a,another is end with b
			stations = getSationsForOneRoad(s + "a");

		}
		int index;
		String x;
		Vector down_road = new Vector();

		while ((index = stations.indexOf("-")) > 0) {
			x = stations.substring(0, index);
			down_road.addElement(x);
			stations = stations.substring(index + 1);

		}
		return down_road;

	}

	/**
	 * check two vector whitch contain string object, check if a string is both
	 * in the first vector and the second vector
	 * 
	 * @param v1
	 *            the vector containning strings
	 * @param v2
	 *            the vector containning strings
	 */
	private String is_there_has_a_same_string(Vector v1, Vector v2) {

		Enumeration seq1 = v1.elements();
		Enumeration seq2;
		String s1, s2;

		while (seq1.hasMoreElements()) {
			s1 = (String) seq1.nextElement();
			seq2 = v2.elements();
			while (seq2.hasMoreElements()) {
				s2 = (String) seq2.nextElement();
				if (s1.compareTo(s2) == 0) {
					return s1;
				}

			}

		}
		return null;

	}

	/**
	 * according the abbriave string ,get a full name of one station
	 * 
	 * @param v1
	 *            the vector containning stations on one road
	 * @param s
	 *            the station abrrave
	 * @return the full name of this station
	 */
	private String get_station_full_name(Vector v1, String s) {

		Enumeration seq1 = v1.elements();

		String s1, s2;
		s = s.trim();
		while (seq1.hasMoreElements()) {
			s1 = (String) seq1.nextElement();
			if (s1.indexOf(s) != -1) {
				return s1;
			}

		}

		return "";

	}

	/**
	 * @param sa
	 *            :the name of start station
	 * @param sb
	 *            :the name of end station
	 * @raturn a String array Vector[0]is the first road you should take
	 *         Vector[1] is the second road you should take Vector[2] is the
	 *         middle station you should take off from the first road and take
	 *         on the second road,if it's "same" means just need one road
	 */
	public Vector[] get_road_for_inter_station(String sa, String sb) {

		Vector road1 = new Vector();
		Vector road2 = new Vector();
		Vector road3 = new Vector();
		Vector[] vv = new Vector[5];
		Vector sta1 = new Vector();
		Vector sta2 = new Vector();
		String x = null;
		int index;
		int j = 0;

		if (sa.trim().equals("") || sa.trim().equals("")) {
			return null;
		}
		vv[0] = road1;
		vv[1] = road2;
		vv[2] = road3;
		vv[3] = sta1;
		vv[4] = sta2;
		// get the road list whitch cantain station a
		Vector tmpa = this.get_road_from_station_key(sa);

		// get the road list whitch cantain station b
		Vector tmpb = this.get_road_from_station_key(sb);

		// first check if two station can int one road
		x = is_there_has_a_same_string(tmpa, tmpb);
		if (x != null) {
			road1.addElement((Object) x.toString());
			road3.addElement((Object) "same");
			return vv;
		}
		// if get here. we have to find two road to inter change to finish this
		// task
		Enumeration seq1 = tmpa.elements();
		Enumeration seq2;
		// s1 the first road ,s2:the second road
		String s1, s2;
		Vector stations1, stations2;
		while (seq1.hasMoreElements()) {
			s1 = (String) seq1.nextElement();
			seq2 = tmpb.elements();
			while (seq2.hasMoreElements()) {
				s2 = (String) seq2.nextElement();
				// here s1 is the firt road,and s2 is the sencond road
				// check if two road have the same station and that station
				// will be the inter change station
				stations1 = get_stations_for_one_road_not_care_direction(s1);
				stations2 = get_stations_for_one_road_not_care_direction(s2);

				x = is_there_has_a_same_string(stations1, stations2);
				if (x != null) {
					j++;
					if (j > 3) { // return most 5 option
						return vv;
					}
					road1.addElement((Object) s1.toString());
					road2.addElement((Object) s2.toString());
					road3.addElement((Object) x);
					sta1.addElement((Object) get_station_full_name(stations1,
							sa.trim()));
					sta2.addElement((Object) get_station_full_name(stations2,
							sb.trim()));
				}
			}

		}
		if (road3.size() > 0) {
			return vv;
		} else {
			return null;
		}

	}

	private static final int MAX_STATION = 20;

	public Vector get_road_from_station_key(String s) {

		Enumeration key_of_roads;
		String roads;
		int i = 0;
		Vector temp = new Vector();

		// String mmm=new String(s.getBytes(),1,s.length()-1);

		String mmm = s.trim();
		key_of_roads = this.road.keys();

		while (key_of_roads.hasMoreElements()) {
			roads = (String) key_of_roads.nextElement();
			if (roads.endsWith("b") == true)
				continue;
			if (getSationsForOneRoad(roads).indexOf(mmm) != -1) {
				if (roads.endsWith("a") == true) {
					temp.addElement((Object) roads.substring(0,
							roads.length() - 1));
				} else {
					temp.addElement((Object) roads.toString());
				}
				i++;
			}
		}

		return temp;
	}

	// shang hai
	private void initRoadHash() {
		try {
			String b = null;
			String name = null;
			String line = null;
			String time = "";
			String encode = CharacterEnding.getFileEncode(new FileInputStream(file));
			Log.v(Globals.TAG, "encode is:" + encode);
			if(encode.equals("GB18030"))
				encode = "GBK";
			Log.v(Globals.TAG, "1");
			BufferedReader buf = new BufferedReader(new InputStreamReader(
					new FileInputStream(file), encode));
			int i=0;
			Log.v(Globals.TAG, "2");
			while ((b = buf.readLine()) != null) {
				// System.out.println(b);
				Log.v(Globals.TAG, ""+i);
				b.trim();
				if (b.length() == 0) {
					continue;
				}
				if (b.startsWith(".")) {
					// start with . this is a comment
					continue;
				}

				final int spaceIndex = b.indexOf(' ');
				if (spaceIndex == -1)
					continue;

				// handle bus line name
				name = b.substring(0, spaceIndex);
				if (name.trim().length() == 0)
					continue;

				name = name.replace(Character.toChars(8593)[0], 'a');
				name = name.replace(Character.toChars(8595)[0], 'b');

				// handle bus line stations
				final int colonIndex = b.indexOf(':');
//				Log.v(Globals.TAG, "line" + colonIndex);
				if (colonIndex == -1 || spaceIndex > colonIndex)
					continue;
				line = b.substring(spaceIndex + 1, colonIndex);
				if (line.trim().length() == 0)
					continue;
				line = line + "-";// append a '-' to make processing station
									// string easily for other function

				time = b.substring(colonIndex + 1, b.length());
				road.put(name, line);
				road_time.put(name, time);
				i++;
				if(i > 0xfff)
					break;
				// {
				// Context context = parent.getApplicationContext();
				// CharSequence text =encode+","+name;
				// int duration = Toast.LENGTH_SHORT;
				//
				// Toast toast = Toast.makeText(context, text, duration);
				// toast.show();
				// }

			}
			buf.close();
			Log.v(Globals.TAG, "total count:"+i);
			// road.put("aa","fff-gg-aa-");//some thing like this
		} catch (Exception e) {
			Log.v(Globals.TAG, "exception"+e.getMessage());
			e.printStackTrace();
		}
	}

}
