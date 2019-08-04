package com.oregonscientific.meep.together.bean;

import java.util.ArrayList;

import com.oregonscientific.meep.together.activity.Consts;

public class Permissions 
{
	public SettingConfig getMusic() {
		return music;
	}


	public void setMusic(SettingConfig music) {
		this.music = music;
	}


	public SettingConfig getPicture() {
		return picture;
		
	}


	public void setPicture(SettingConfig picture) {
		this.picture = picture;
	}


	public SettingConfig getCommunicator() {
		return communicator;
	}


	public void setCommunicator(SettingConfig communicator) {
		this.communicator = communicator;
	}


	public SettingConfig getInapp() {
		return inapp;
	}


	public void setInapp(SettingConfig inapp) {
		this.inapp = inapp;
	}


	public SettingConfig getSecuritylevel() {
		return securitylevel;
	}


	public void setSecuritylevel(SettingConfig securitylevel) {
		this.securitylevel = securitylevel;
	}


	public SettingConfig getApps() {
		return apps;
	}


	public void setApps(SettingConfig apps) {
		this.apps = apps;
	}


	public SettingConfig getBrowser() {
		return browser;
	}


	public void setBrowser(SettingConfig browser) {
		this.browser = browser;
	}


	public SettingConfig getYoutube() {
		return youtube;
	}


	public void setYoutube(SettingConfig youtube) {
		this.youtube = youtube;
	}


	public SettingConfig getMovie() {
		return movie;
	}


	public void setMovie(SettingConfig movie) {
		this.movie = movie;
	}


	public SettingConfig getPurchase() {
		return purchase;
	}


	public void setPurchase(SettingConfig purchase) {
		this.purchase = purchase;
	}


	public SettingConfig getGame() {
		return game;
	}


	public void setGame(SettingConfig game) {
		this.game = game;
	}


	public SettingConfig getGoogleplay() {
		return googleplay;
	}


	public void setGoogleplay(SettingConfig googleplay) {
		this.googleplay = googleplay;
	}


	public SettingConfig getEbook() {
		return ebook;
	}


	public void setEbook(SettingConfig ebook) {
		this.ebook = ebook;
	}
	
	public SettingConfig getOsgdbadword() {
		return osgdbadword;
	}


	public void setOsgdbadword(SettingConfig osgdbadword) {
		this.osgdbadword = osgdbadword;
	}


	private SettingConfig music;
	private SettingConfig picture;
	private SettingConfig communicator;
	private SettingConfig inapp;
	private SettingConfig securitylevel;
	private SettingConfig apps;
	private SettingConfig browser;
	private SettingConfig youtube;
	private SettingConfig movie;
	private SettingConfig purchase;
	private SettingConfig game;
	private SettingConfig googleplay;
	private SettingConfig ebook;
	private SettingConfig osgdbadword;
	
	private ArrayList<SettingConfig> limit;

	public ArrayList<SettingConfig> getLimit() {
		return limit;
	}
	public void setNewLimit(ArrayList<SettingConfig> limit) {
		this.limit = limit;
	}
	public void setLimit() {
		limit = new ArrayList<SettingConfig>();
		limit.add(PermissionNameIndex.APP,getApps());
		limit.add(PermissionNameIndex.GAME,getGame());
		limit.add(PermissionNameIndex.MOVIE,getMovie());
		limit.add(PermissionNameIndex.MUSIC,getMusic());
		limit.add(PermissionNameIndex.EBOOK,getEbook());
		limit.add(PermissionNameIndex.PICTURE,getPicture());
		limit.add(PermissionNameIndex.BROWSER,getBrowser());
		limit.add(PermissionNameIndex.YOUTUBE,getYoutube());
		limit.add(PermissionNameIndex.COMMUNICATOR,getCommunicator());
		limit.add(PermissionNameIndex.PURCHASE,getPurchase());
		limit.add(PermissionNameIndex.INAPP,getInapp());
		limit.add(PermissionNameIndex.GOOGLEPALY,getGoogleplay());
		limit.add(PermissionNameIndex.INTERNET_SECURITY_LEVEL,getSecuritylevel());
		limit.add(PermissionNameIndex.OSGD_BADWORD,getOsgdbadword());
	};
	public void clearLimit() {
		limit = null;
	};
	
	public void settingPermissionsObjectByName(
			String name, SettingConfig config) {
		if (name.equals(Consts.PICTURE_DISPLAY_NAME)) {
			// picture
			this.setPicture(config);
		} else if (name.equals(Consts.YOUTUBE_DISPLAY_NAME)) {
			// youtube
			this.setYoutube(config);
		} else if (name.equals(Consts.EBOOK_DISPLAY_NAME)) {
			// ebook
			this.setEbook(config);
		} else if (name.equals(Consts.MOVIE_DISPLAY_NAME)) {
			// movie
			this.setMovie(config);
		} else if (name.equals(Consts.APPS_DISPLAY_NAME)) {
			// apps
			this.setGame(config);
			this.setApps(config);
		} else if (name.equals(Consts.GAME_DISPLAY_NAME)) {
			// game
			this.setGame(config);
			this.setApps(config);
		} else if (name.equals(Consts.MUSIC_DISPLAY_NAME)) {
			// music
			this.setMusic(config);
		} else if (name.equals(Consts.BROWSER_DISPLAY_NAME)) {
			// browser
			this.setBrowser(config);
		} else if (name.equals(Consts.COMMUNICATOR_DISPLAY_NAME)) {
			// communicator
			this.setCommunicator(config);
		} else if (name.equals(Consts.INAPP_DISPLAY_NAME)) {
			// inapp
			this.setInapp(config);
		} else if (name.equals(Consts.GOOGLEPLAY_DISPLAY_NAME)) {
			// googleplay
			this.setGoogleplay(config);
		} else if (name.equals(Consts.SECURITYLEVEL_DISPLAY_NAME)) {
			// securitylevel
			this.setSecuritylevel(config);
		} else if (name.equals(Consts.PURCHASE_DISPLAY_NAME)) {
			// purchase
			this.setPurchase(config);
		} else if (name.equals(Consts.OSGD_BADWORD_DISPLAY_NAME)) {
			// osgdbadword
			this.setOsgdbadword(config);
		}
	}
	
}

