package com.oregonscientific.meep.store2.inapp.object;

import java.util.Locale;

import android.util.Log;

public class Translations {
	protected String en;
	protected String br;
	protected String fr;
	protected String es;
	protected String de;
	protected String it;
	protected String ja;

	public String getEnglish() {
		return en;
	}
	
	public String getBrazilian() {
		return br;
	}
	
	public String getFrench() {
		return fr;
	}
	
	public String getSpanish() {
		return es;
	}
	
	public String getGerman() {
		return de;
	}
	
	public String getItalian() {
		return it;
	}
	
	public String getJapanese() {
		return ja;
	}
	
	public static String LANGUAGE_BRAZILIAN = "br";
	public static String LANGUAGE_SPANISH = "es";
	public String getText()
	{
		String language = Locale.getDefault().getLanguage();
		String text = null;
		if(Locale.ENGLISH.equals(language)){
			text = getEnglish();
		} else if (LANGUAGE_BRAZILIAN.equals(language)){
			text = getBrazilian();
		}else if (Locale.FRENCH.equals(language)){
			text = getFrench();
		}else if (LANGUAGE_SPANISH.equals(language)){
			text = getSpanish();
		}else if (Locale.GERMAN.equals(language)){
			text = getGerman();
		}else if (Locale.ITALIAN.equals(language)){
			text = getItalian();
		}else if (Locale.JAPANESE.equals(language)){
			text = getJapanese();
		}
		
		if(text == null){
			text = getEnglish();
		}
		return text;
	}
	
}
