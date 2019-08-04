package com.tcl.manager.score;

import android.content.res.Resources;

import com.tcl.manager.application.ManagerApplication;
import com.tcl.manager.arithmetic.opetate.FrequencyLevel;
import com.tcl.mie.manager.R;

/**
 * @Description: 分数等级
 * @author wenchao.zhang
 * @date 2014年12月25日 下午1:18:15
 * @copyright TCL-MIE
 */

public class ScoreLevel {
	public static final int DANGER = 1;
	public static final int NORMAL = 2;
	public static final int WELL = 3;

	/**
	 * 根据分数获得等级
	 * 
	 * @param score
	 * @return
	 */
	public static int resolveToLevel(int score) {
		if (score < 0) {
			return DANGER;
		}
		if (score < 50) {
			return DANGER;
		}
		if (score < 80) {
			return NORMAL;
		}
		return WELL;
	}

	/**
	 * 根据分数获取该等级对应的颜色值
	 * 
	 * @param score
	 * @return
	 */
	public static int resolveToColor(int score) {
		int color = 0;
		switch (resolveToLevel(score)) {
		case DANGER:
			color = ManagerApplication.sApplication.getResources().getColor(
					R.color.main_danger);
			break;
		case NORMAL:
			color = ManagerApplication.sApplication.getResources().getColor(
					R.color.main_normal);
			break;
		case WELL:
			color = ManagerApplication.sApplication.getResources().getColor(
					R.color.main_good);
			break;
		default:
			break;
		}
		return color;
	}
	/**
	 * 根据分数获取该分数对应的字符串
	 * @param score
	 * @return
	 */
	public static String resolveToString(int score) {
		String str = "";
		switch (resolveToLevel(score)) {
		case DANGER:
			str = ManagerApplication.sApplication.getResources().getString(
					R.string.main_optimize_needed2);
			break;
		case NORMAL:
			str = ManagerApplication.sApplication.getResources().getString(
					R.string.main_optimize_needed2);
			break;
		case WELL:
			str = ManagerApplication.sApplication.getResources().getString(
					R.string.main_healthy);
			break;
		default:
			break;
		}
		return str;
	}

	
	public static int resolveToMiniCircle(int score) {
		int colorResId  = R.drawable.shape_oval_red;
		switch (resolveToLevel(score)) {
		case DANGER:
			colorResId = R.drawable.shape_oval_red;
			break;
		case NORMAL:
			colorResId = R.drawable.shape_oval_normal;
			break;
		case WELL:
			colorResId = R.drawable.shape_oval_well;
			break;
		default:
			break;
		}
		return colorResId;
	}
	/**
	 * 通过频率获取相对应描述字符串
	 * @param level
	 * @return
	 */
    public static String resolveToFrequencyLevelString(FrequencyLevel level){
		Resources res = ManagerApplication.sApplication.getResources();
		if(level == FrequencyLevel.OFTEN){
			return res.getString(R.string.app_detail_fre_used_text);
		}else if(level == FrequencyLevel.GENERAL){
			return res.getString(R.string.app_detail_nor_used_text);
		}else if(level == FrequencyLevel.NO_RECORD){
			return res.getString(R.string.app_detail_nev_used_text);
		}else if(level == FrequencyLevel.NOT_OFTEN){
			return res.getString(R.string.app_detail_bar_used_text);
		}
		
		return  res.getString(R.string.app_detail_nev_used_text);
	}
	
	public static int resolveToMiniappCircleResId(int score){
		int resId  = R.drawable.ic_shuaxin_green;
		switch (resolveToLevel(score)) {
		case DANGER:
			resId = R.drawable.ic_shuaxin_red;
			break;
		case NORMAL:
			resId = R.drawable.ic_shuaxin_yellow;
			break;
		case WELL:
			resId = R.drawable.ic_shuaxin_green;
			break;
		default:
			break;
		}
		return resId;
	}
}
