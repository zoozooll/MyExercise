package com.beem.project.btf.constant;

/**
 * @ClassName: SettingKey
 * @Description: SharedPreference中的Key
 * @author: yuedong bao
 * @date: 2015-5-6 上午10:44:57
 */
public enum SettingKey {
	//account_username:保存登录真正的jid;	account_token:保存上次输入的账号，可能是电话号码，也可能是jid；
	album_auhtority, msg_vibrate, msg_sound, cacheMills, account_username,
	account_password, account_token, CONNECTION_RESOURCE_KEY,
	sendWelcomeMessage, systemTimeDeltalMils, savePhoto, first, neighborPos,
	lastClearMills, LibTimeOut, TimeCameraMaterial_id,
	TimeCameraMaterial_groupid, CommentedPos, GUIDE_SHOWED_ADDFRIENDS,
	GUIDE_SHOWED_SHARE, GUIDE_SHOWED_TIMEFLY, GUIDE_SHOWED_TIMEFLYCAMERA,
	GUIDE_SHOWED_NEWSTOP_CAMERA, GUIDE_SHOWED_NEWSTV_CAMERA, offline_msg,
	IsDowningState, NewsCameraMaterial_groupid, NewsCameraMaterial_id,
	IntroduceActivity, autosharecheck, flagmenttype, CustomViewPagerIndex,
	isClickable, sendTime;
}
