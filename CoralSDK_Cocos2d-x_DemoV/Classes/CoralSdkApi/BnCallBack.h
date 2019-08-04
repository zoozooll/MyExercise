#pragma once
#include "BnSDKCallback.h"
#include "cocos2d.h"

using namespace cocos2d;

class BnCallBack:public BnSDKCallback
{
public:
	BnCallBack();
	~BnCallBack();
public:
    
    //参加游戏的人员回调（数组包含userkey,username,gamescore,gamedata）1001
	void BnGameUserSelectedNotification(CCArray *userlist);
    
    //退出登陆通知 1003
	void BnLogoutNotify();
    
    //音乐开关通知 1004
	void BnMusicTurnOffAndOn(bool isOn);
    
    //音效开关通知 1005
	void BnSoundTurnOffAndOn(bool isOn);
    
    //登录回调:1为登陆成功，2为失败，3为取消登录
	void BnLoginResult(int isLogin);
    
    //游戏结束后返回按键回调 1007
    void BnGameBackNotification();

};

