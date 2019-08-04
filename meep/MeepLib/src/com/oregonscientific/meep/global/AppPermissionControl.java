package com.oregonscientific.meep.global;

import java.util.ArrayList;

import android.util.Log;

import com.google.gson.Gson;
import com.oregonscientific.meep.database.table.TableAppsCategory;
import com.oregonscientific.meep.global.Global.AppType;

public class AppPermissionControl {
	public static boolean skipThisApp(String name, AppType appType, ArrayList<String> gameList, ArrayList<String> blockList) {
		boolean gameFound = false;
		
		if (appType == AppType.App) {
			// Skip our app
			if (name.indexOf("com.oregonscientific") == 0) {
				return true;
			}
		}
		
		if (gameList != null) {
			for (int i=0; i<gameList.size(); i++) {
				if (name.equalsIgnoreCase(gameList.get(i))) {
					if (appType == AppType.App) {
						// Skip Game
						return true;
					} else if (appType == AppType.Game) {
						// Only Show game
						gameFound = true;
						break;
					}
				}
			}
		}
		
		if (appType == AppType.Game && !gameFound) {
			// Game not found, skip it
			return true;
		}
		
		if (blockList != null) {
			for (int i=0; i<blockList.size(); i++) {
				if (name.equalsIgnoreCase(blockList.get(i))) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public static ArrayList<String> getGameList(String[] appsCategory) {
		Gson gson = new Gson();
		ArrayList<String> gameList = new ArrayList<String>();
		
		if (appsCategory != null) {
			for (int i=0; i<appsCategory.length; i++) {
				TableAppsCategory tableAppsCategory = gson.fromJson(appsCategory[i], TableAppsCategory.class);
				
				if (tableAppsCategory.getCategory().equalsIgnoreCase("GAMES") || tableAppsCategory.getCategory().equalsIgnoreCase("DEFAULT_GAMES")) {
					gameList.add(tableAppsCategory.getAppId());
				}
			}
		}
		
		return gameList;
	}
	
	public static ArrayList<String> getBlockList(String[] appsCategory) {
		Gson gson = new Gson();
		ArrayList<String> blockList = new ArrayList<String>();
		
		if (appsCategory != null) {
			for (int i=0; i<appsCategory.length; i++) {
				TableAppsCategory tableAppsCategory = gson.fromJson(appsCategory[i], TableAppsCategory.class);
				
				if (tableAppsCategory.getCategory().equalsIgnoreCase("BLOCKED") || tableAppsCategory.getCategory().equalsIgnoreCase("DEFAULT_BLOCKED")) {
					blockList.add(tableAppsCategory.getAppId());
				}
			}
		}
		else
		{
			blockList =  getDefaultBlockList();
		}
		
		return blockList;
	}
	
	public static ArrayList<String> getDefaultBlockList()
	{
		ArrayList<String> blockList = new ArrayList<String>();
		blockList.add("com.android.browser");
		blockList.add("com.android.calculator2");
		blockList.add("com.google.android.calendar");
		blockList.add("com.android.camera");
		blockList.add("com.android.deskclock");
		blockList.add("com.android.email");
		blockList.add("com.android.gallery3d");
		blockList.add("com.google.android.gm");
		blockList.add("com.android.music");
		blockList.add("com.android.settings");
		blockList.add("com.google.android.talk");
		blockList.add("com.android.vending");
		blockList.add("com.android.providers.downloads.ui");
		blockList.add("com.google.android.googlequicksearchbox");
		blockList.add("com.android.soundrecorder");
		blockList.add("com.oregonscientific.meep");
		blockList.add("com.softwinner.explore");
		blockList.add("com.adobe.flashplayer");
		blockList.add("com.example.android.softkeyboard");
		blockList.add("com.Reader.main");
		blockList.add("com.android.phone");
		blockList.add("com.android.vending");
		blockList.add("com.android.contacts");
		blockList.add("klye.plugin.ja");
		blockList.add("kl.ime.oh");	
		blockList.add("com.amlogic.HdmiSwitch");		
		blockList.add("com.example.showmessage");	
		blockList.add("com.android.calendar");		
		blockList.add("com.android.providers.calendar");
		blockList.add("com.android.quicksearchbox");		
		
		return blockList;
	}
}
