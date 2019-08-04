#ifndef __BnInterface__File__
#define __BnInterface__File__

#include <iostream>
#include "cocos2d.h"
#include "fmjson/FMContentJsonDictionary.h"

using namespace cocos2d;
using namespace std;
using namespace FMCS;

#if (CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID)
#include <jni.h>
#endif

//玩家信息类
class BnUser : public cocos2d::CCLayer
{
public:
    
    //保存UserKey
    void BnSetUserKey(const char* value)
    {
        key = CCString::createWithFormat("%s",value);
        iUserKey = key->getCString();
    }
    
    //保存UserName
    void BnSetUserName(const char* value)
    {
        name = CCString::createWithFormat("%s",value);
        iUserName = name->getCString();
    }
    
    //保存UserHead
    void BnSetUserHead(const char* value)
    {
        head = CCString::createWithFormat("%s",value);
        iUserHead = head->getCString();
    }
    
    //保存GameData,游戏数据
    void BnSetGameData(const char* value)
    {
        gamedata = CCString::createWithFormat("%s",value);
        iGameData = gamedata->getCString();
    }
    
    //保存GameScore,游戏分数
    void BnSetGameScore(const char* value)
    {
        gamescore = CCString::createWithFormat("%s",value);
        iGameScore = gamescore->getCString();
    }
    
    //获取UserKey，玩家唯一标识
	const char* BnGetUserKey()
	{
		return iUserKey;
	}
    
    //获取UserName，玩家名称
	const char* BnGetUserName()
	{
        return iUserName;
	}
    
    //获取UserHead，玩家头像 文件路径
    const char* BnGetUserHead()
	{
        return iUserHead;
	}
    
    //获取GameData,游戏数据
    const char* BnGetGameData()
	{
        return iGameData;
	}
    
    //获取GameScore,游戏分数
    const char* BnGetGameScore()
	{
        return iGameScore;
	}
    
    virtual bool init()
    {
        return true;
    }
    CREATE_FUNC(BnUser);
    
private:
    const char* iUserKey;
    const char* iUserName;
    const char* iUserHead;
    const char* iGameData;
    const char* iGameScore;
    
	CCString* key;
    CCString* name;
    CCString* head;
    CCString* gamedata;
    CCString* gamescore;
    int Gamescore;

};

//设置游戏配置
void BnSetConfig();

//登陆（type：登陆来源，1好友逗 2新浪微博 3腾讯微博）
void BnLogin(int type);//cocos2d::CCString

//登陆状态（true已经登陆，false未登陆）
bool BnGetLoginStatus();

//打开SDK首页(好友排行榜界面)
void BnShowGameHome();

//隐藏SDK
void BnHideSDK();

/********
 功能点： 游戏结束传参
 功能描述：将各个数值传进来
 输入参数：score:分数, gamedata 游戏数据,gold：获得的金币数量 baseStr：基础分数（eg：‘基础分：1024 分’）
 输出参数：无
 ********/
void BnGameOver(int score,const char* gamedata,int gold, const char* baseStr);

//================拓展功能==================
//获取玩家信息
BnUser *BnGetProfile();

//获取音乐开关状态
bool BnGetMusicOnStatus();

//获取音效开关状态
bool BnGetSoundOnStatus();

//增加或减少金币
bool BnSetGameGold(int gold);

//获取金币
int BnGetGameGold();

//page:1、爱心 2、金币 3、钻石
void BnShowStoreView(int page);

//道具保存
void BnSetGamePropSave(const char *prop);

//获取道具
const char *BnGetGameProp();

//获取钻石数量
int BnGetGameDiamond();

//增加或减少钻石
bool BnSetGameDiamond(int DiamondNum);

//获取爱心数量
int BnGetGamePower();

//增加或减少爱心
bool BnSetGamePower(int PowerNum);
#endif
