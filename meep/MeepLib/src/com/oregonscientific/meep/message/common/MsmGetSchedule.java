package com.oregonscientific.meep.message.common;

//import com.google.gson.JsonObject;

public class MsmGetSchedule extends MeepServerMessage {

	
	public static enum AppName {
		
		APP_PICTURE("Picture"),
		APP_YOUTUBE("Youtube"),
		APP_EBOOK("Ebook"),
		APP_GAME("Game"),
		APP_COIN_REQUEST("Coninrequest"),
		APP_PURCHASE("Purchase"),
		APP_MOVIE("Movie"),
		APP_APPS("Apps"),
		APP_MUSIC("Music"),
		APP_BROWSER("Browser"),
		APP_SECURITY_LEVEL("SecurityLevel"),
		APP_COMMUNICATOR("communicator"),
		APP_GOOGLE_PLAY("googleplay"),
		APP_INAPP("inapp");
		
		
		private String name;
		
		AppName(String name) {
			this.name = name;
		}

		public String toString() {
			return name;
		}
		
		public static AppName fromString(String text) {
			if (text != null) {
				for (AppName b : AppName.values()) {
					if (text.equalsIgnoreCase(b.name)) {
						return b;
					}
				}
			}
			return null;
		}
	
	}
	

	public static final String[] APP_NAMES = new String[] {
		
		AppName.APP_PICTURE.toString(),
		AppName.APP_YOUTUBE.toString(),
		AppName.APP_EBOOK.toString(),
		AppName.APP_GAME.toString(),
		AppName.APP_COIN_REQUEST.toString(),
		AppName.APP_PURCHASE.toString(),
		AppName.APP_MOVIE.toString(),
		AppName.APP_APPS.toString(),
		AppName.APP_MUSIC.toString(),
		AppName.APP_BROWSER.toString(),
		AppName.APP_SECURITY_LEVEL.toString(),
		AppName.APP_COMMUNICATOR.toString(),
		AppName.APP_GOOGLE_PLAY.toString(),
		AppName.APP_INAPP.toString()
		
	};
	
	private ApplicationAccess picture = null;
	private ApplicationAccess youtube = null;
	private ApplicationAccess ebook = null;
	private ApplicationAccess game = null;
	private ApplicationAccess coinrequest = null;
	private ApplicationAccess purchase = null;
	private ApplicationAccess movie = null;
	private ApplicationAccess apps = null;
	private ApplicationAccess music = null;
	private ApplicationAccess browser = null;
	private ApplicationAccess securitylevel = null;
	private ApplicationAccess communicator = null;
	private ApplicationAccess googleplay = null;
	private ApplicationAccess inapp = null;
	private int last_update;
	
	
	public MsmGetSchedule(String proc, String opcode) {
		super(proc, opcode);
		// TODO Auto-generated constructor stub
	}
	
	/*public String toJsonString() {
		JsonObject json = new JsonObject();
		json.addProperty(STR_PROCESS, getProc());
		json.addProperty(STR_OPCODE, getOpcode());
		return json.toString();
	}*/


	public ApplicationAccess getPicture() {
		return picture;
	}


	public void setPicture(ApplicationAccess picture) {
		this.picture = picture;
	}


	public ApplicationAccess getYoutube() {
		return youtube;
	}


	public void setYoutube(ApplicationAccess youtube) {
		this.youtube = youtube;
	}


	public ApplicationAccess getEbook() {
		return ebook;
	}

	public void setEbook(ApplicationAccess ebook) {
		this.ebook = ebook;
	}


	public ApplicationAccess getGame() {
		return game;
	}


	public void setGame(ApplicationAccess game) {
		this.game = game;
	}


	public ApplicationAccess getCoinrequest() {
		return coinrequest;
	}


	public void setCoinrequest(ApplicationAccess coinrequest) {
		this.coinrequest = coinrequest;
	}


	public ApplicationAccess getPurchase() {
		return purchase;
	}


	public void setPurchase(ApplicationAccess purchase) {
		this.purchase = purchase;
	}


	public ApplicationAccess getMovie() {
		return movie;
	}


	public void setMovie(ApplicationAccess movie) {
		this.movie = movie;
	}


	public ApplicationAccess getApps() {
		return apps;
	}


	public void setApps(ApplicationAccess apps) {
		this.apps = apps;
	}


	public ApplicationAccess getMusic() {
		return music;
	}


	public void setMusic(ApplicationAccess music) {
		this.music = music;
	}


	public ApplicationAccess getBrowser() {
		return browser;
	}


	public void setBrowser(ApplicationAccess browser) {
		this.browser = browser;
	}

	public ApplicationAccess getSecurityLevel() {
		return securitylevel;
	}

	public void setSecurityLevel(ApplicationAccess securitylevel) {
		this.securitylevel = securitylevel;
	}

	public ApplicationAccess getCommunicator() {
		return communicator;
	}

	public void setCommunicator(ApplicationAccess communicator) {
		this.communicator = communicator;
	}

	public ApplicationAccess getGoogleplay() {
		return googleplay;
	}

	public void setGoogleplay(ApplicationAccess googleplay) {
		this.googleplay = googleplay;
	}

	public ApplicationAccess getInapp() {
		return inapp;
	}

	public void setInapp(ApplicationAccess inapp) {
		this.inapp = inapp;
	}
	
	public int getLast_update() {
		return last_update;
	}

	public void setLast_update(int last_update) {
		this.last_update = last_update;
	}

}
