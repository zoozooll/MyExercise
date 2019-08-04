#include "BnCallBack.h"
#include "GameManager.h"
#include "BnInterface.h"


BnCallBack::BnCallBack()
{
    
}

BnCallBack::~BnCallBack()
{
    
}

//参加游戏的人员回调（数组包含userkey,username,gamescore,gamedata）
void BnCallBack::BnGameUserSelectedNotification(CCArray *userlist)
{
    BnHideSDK();//隐藏SDK
    deGameManager->changeGameState(EGameState::PLAYING);//进入游戏过程
}

//退出登陆通知
void BnCallBack::BnLogoutNotify()
{
     BnHideSDK();//隐藏SDK
     deGameManager->setLoginFlag(0);//退出登陆，返回到登陆界面
     deGameManager->changeGameState(EGameState::LOGINOUT);
}

//音乐开关通知
void BnCallBack::BnMusicTurnOffAndOn(bool isOn)
{
	 
    if (isOn) {
        deGameManager->setMusic(1);
        
    }else{
        deGameManager->setMusic(0);
        
    }
	 
}

//音效开关通知
void BnCallBack::BnSoundTurnOffAndOn(bool isOn)
{
	 
    if (isOn) {
        deGameManager->setSound(1);
    }else{
        deGameManager->setSound(0);
    }
	 
}

//登录回调:1为登陆成功，2为失败，3为取消登录
void BnCallBack::BnLoginResult(int isLogin)
{
	 
    if(isLogin==1)
    {
        deGameManager->setLoginFlag(1);
    }
    else
    {
        deGameManager->setLoginFlag(0);
    }
	 
}

//游戏结束后返回按键回调
void BnCallBack::BnGameBackNotification()
{
    BnHideSDK();//隐藏SDK
    deGameManager->changeGameState(EGameState::RANDING);//返回游戏排行榜
}


