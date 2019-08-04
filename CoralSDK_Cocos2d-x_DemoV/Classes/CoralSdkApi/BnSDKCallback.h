#ifndef _BNSDKCALLBACK_H_
#define _BNSDKCALLBACK_H_

#include "cocos2d.h"
#include "BnInterface.h"

using namespace cocos2d;
 
//SDK call cocos2d-x
class BnSDKCallback
{
public:
	BnSDKCallback(){};
	~BnSDKCallback(){};

	void SetSDKCallback(BnSDKCallback *aCallBack);

	//参加游戏的人员回调（数组包含userkey,username,gamescore,gamedata）
	virtual void BnGameUserSelectedNotification(CCArray *userlist);

	//退出登陆通知
	virtual void BnLogoutNotify();

	//登录回调
	virtual void BnLoginResult(int isLogin);
    
    //游戏结束后返回按键回调
    virtual void BnGameBackNotification();

    
    //=============拓展功能===============
    //音乐开关通知
	virtual void BnMusicTurnOffAndOn(bool isOn);
    
	//音效开关通知
	virtual void BnSoundTurnOffAndOn(bool isOn);
	
};


void GameUserSelectedNotification(const char *userlist);

void LogoutNotify();

void LoginResult(int isLogin);

void GameBackNotification();

//==============拓展功能==============
void MusicTurnOffAndOn(bool isOn);

void SoundTurnOffAndOn(bool isOn);


#endif
