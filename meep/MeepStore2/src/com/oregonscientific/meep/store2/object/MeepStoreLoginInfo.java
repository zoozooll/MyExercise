package com.oregonscientific.meep.store2.object;

import java.util.ArrayList;

import android.graphics.Bitmap;

public class MeepStoreLoginInfo {
	public String name;
	public String birthday;
	public String coins;
	public String visitor;
	public int code;
	public String status;
	public String theme;
	public ArrayList<BannerItem> banner;
	public String url_prefix;
	public String ota;
	private ArrayList<PromotionItem> promotion;
	
	public static final String CHRISTMAS = "christmas";
	public static final String EASTER = "easter";
	public static final String HALLOWEEN = "halloween";
	public static final String BACK_TO_SCHOOL = "back_to_school";
	
	public void updateBannerImage(String bannerId, Bitmap image){
		if (banner != null) {
			for (int i = 0; i < banner.size(); i++) {
				if (banner.get(i).id.equals(bannerId)) {
					banner.get(i).setImageBitmap(image);
					break;
				}
			}
		}
	}

	public ArrayList<PromotionItem> getPromotion() {
		return promotion;
	}

	public void setPromotion(ArrayList<PromotionItem> promotion) {
		this.promotion = promotion;
	}
}
