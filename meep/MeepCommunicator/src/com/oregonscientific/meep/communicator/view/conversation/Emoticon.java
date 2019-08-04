package com.oregonscientific.meep.communicator.view.conversation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.arabidopsis.ahocorasick.AhoCorasick;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

import com.oregonscientific.meep.communicator.R;

/**
 * A class for emoticons
 */
public class Emoticon {
	
	private final static String TAG = "Emoticon";
	private final static Map<String, Integer> mEmoticonMap = new HashMap<String, Integer>();
	private final static List<String> mEmoticonCodes = new ArrayList<String>();
	public final static int EMOTICON_MAX_LENGTH = 30;
	public final static int EMOTICON_CODE_LENGTH = 7;
	
	private static String key = "Emoticon";
	private static Object mLock = new Object();
	private final static Map<String, AhoCorasick> mTreeMap = new HashMap<String, AhoCorasick>();
	
	/**
	 * Get the array of emoticon ids
	 * @return array of emoticon ids
	 */
	private static Integer[] getEmoticonIds() {
		return new Integer[] {
				R.drawable.emoticon_1,
				R.drawable.emoticon_2,
				R.drawable.emoticon_3,
				R.drawable.emoticon_4,
				R.drawable.emoticon_5,
				R.drawable.emoticon_6,
				R.drawable.emoticon_7,
				R.drawable.emoticon_8,
				R.drawable.emoticon_9,
				R.drawable.emoticon_10,
				R.drawable.emoticon_11,
				R.drawable.emoticon_12,
				R.drawable.emoticon_13,
				R.drawable.emoticon_14,
				R.drawable.emoticon_15 };
	}
	
	/**
	 * Initialize the emoticon map
	 * @param context current context
	 */
	private static void initEmoticonMap(Context context) {
		InputStream is = context.getResources().openRawResource(R.raw.emoticon_ids);
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		
		try {
			for (Integer emoticonId : getEmoticonIds()) {
				String line = reader.readLine();
				if (line != null) {
					mEmoticonCodes.add(line);
					mEmoticonMap.put(line, emoticonId);
				}
			}
			reader.close();
			is.close();
		} catch (IOException e) {
			Log.e(TAG, "Emoticons cannot be read because: " + e);
		}
	}
	
	/**
	 * Get the array of emoticon codes
	 * @return array of emoticon codes
	 */
	public static String[] getEmoticonCodes(Context context) {
		if (mEmoticonCodes == null || mEmoticonCodes.isEmpty()) {
			initEmoticonMap(context);
		}
		return mEmoticonCodes.toArray(new String[mEmoticonCodes.size()]);
	}
	
	/**
	 * Get the id of emoticon by its code
	 * @param code code of emoticon
	 * @return the id of emoticon that the emoticon code representing, null if no
	 *         match emoticon id is found
	 */
	public static Integer getEmoticonId(String code) {
		if (code != null && mEmoticonMap != null && mEmoticonMap.containsKey(code)) {
			return mEmoticonMap.get(code);
		}
		return null;
	}
	
	/**
	 * Get the emoticon from its emoticon code
	 * @param context current context
	 * @param output the emoticon code
	 * @return the emoticon that the emoticon code representing, null if no match
	 *         emoticon is found
	 */
	public static Bitmap getEmoticon(Context context, String output) {
		if (mEmoticonMap == null || mEmoticonMap.isEmpty()) {
			initEmoticonMap(context);
		}
		if (output != null && mEmoticonMap != null && mEmoticonMap.containsKey(output)) {
			return getScaledEmoticon(((BitmapDrawable) context.getResources().getDrawable(mEmoticonMap.get(output))).getBitmap());
		}
		return null;
	}
	
	/**
	 * Clear the emoticon map
	 */
	public static void clearEmoticonMap() {
		mEmoticonCodes.clear();
		mEmoticonMap.clear();
		mTreeMap.clear();
		mLock = null;
		key = null;
	}
	
	/**
	 * Scale a bitmap to the size of an emoticon
	 * @param bitmap bitmap to be resized to that of an emoticon
	 * @return bitmap in size of an emoticon
	 */
	private static Bitmap getScaledEmoticon(Bitmap bitmap) {
		int height = 0;
		int width = 0;
		if (bitmap.getHeight() > bitmap.getWidth()) {
			height = EMOTICON_MAX_LENGTH;
			width = EMOTICON_MAX_LENGTH * bitmap.getWidth() / bitmap.getHeight();
		} else if (bitmap.getWidth() > bitmap.getHeight()) {
			width = EMOTICON_MAX_LENGTH;
			height = EMOTICON_MAX_LENGTH * bitmap.getHeight() / bitmap.getWidth();
		}
		return Bitmap.createScaledBitmap(bitmap, width, height, false);
	}
	
	/**
	 * Builds a AhoCorasick emoticon tree from database
	 * @param context current context
	 * @return the Aho-Corasick search tree to which the specified key is mapped,
	 *         or null if this map contains no mapping for the key
	 */
	public static synchronized AhoCorasick getTree(Context context) {
		AhoCorasick tree = lookupTree(key);
		if (tree != null) {
			return tree;
		}
		
		tree = new AhoCorasick();
		// Adds default emoticon ids to the tree
		addEmoticonIdsToTree(context, R.raw.emoticon_ids, tree);
		
		tree.prepare();
		addTreeToMap(key, tree);
		
		return tree;
	}
	
	/**
	 * Returns the Aho-Corasick search tree to which the specified key is mapped,
	 * or null if no cached tree maps to the key.
	 * @param key the key whose associated Aho-Corasick search tree is to be
	 *          returned
	 * @return the Aho-Corasick search tree to which the specified key is mapped,
	 *         or null if this map contains no mapping for the key
	 */
	private static AhoCorasick lookupTree(String key) {
		// Quick return if the request cannot be processed
		if (key == null) {
			return null;
		}
		
		synchronized (mLock) {
			return mTreeMap.get(key);
		}
	}
	
	/**
	 * Adds each line of text in the {@link BufferedReader} to the Aho-Corasick
	 * tree
	 * @param context current context
	 * @param resId The {@link Reader} to a text file
	 * @param tree the Aho-Corasick search tree
	 * @return true if all entries in the resource is added to the tree, false
	 *         otherwise
	 */
	private static boolean addEmoticonIdsToTree(
			Context context,
			int resId,
			AhoCorasick tree) {
		// Quick return if there is nothing to process
		if (tree == null) {
			return false;
		}
		
		boolean result = false;
		try {
			InputStream is = context.getResources().openRawResource(resId);
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			String line;
			
			while ((line = reader.readLine()) != null) {
				tree.add(line.toLowerCase().getBytes(), line.toLowerCase());
			}
			// All entries are added to the tree
			result = true;
			
			reader.close();
			is.close();
		} catch (IOException e) {
			Log.e(TAG, "Cannot add all entries in the resource into the Aho-Corasick search because " + e);
		}
		return result;
	}
	
	/**
	 * Adds the given Aho-Corasick search tree to cache
	 * @param key the key with which the Aho-Corasick search tree is to be
	 *          associated with
	 * @param tree the Aho-Corasick search tree
	 */
	private static void addTreeToMap(String key, AhoCorasick tree) {
		// Quick return if the request cannot be processed
		if (key == null) {
			return;
		}
		
		synchronized (mLock) {
			if (tree == null) {
				mTreeMap.remove(key);
			} else {
				mTreeMap.put(key, tree);
			}
		}
	}
	
}