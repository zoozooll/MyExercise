#include "BnInterface.h"
#include "fmjson/FMjson_lib.h"
#include "fmjson/FMContentJsonDictionary.h"

#if (CC_TARGET_PLATFORM == CC_PLATFORM_IOS)
#include "coralsdk.h"
#endif

#if (CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID)  
#include <jni.h>  
#include "platform/android/jni/JniHelper.h"  
#include <android/log.h>  

//LOGI日志
#define  LOG_TAG    "libibmphotophun"   //定义logcat中tag标签
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)

#define JNIPAGECLASS "com/friendou/Interface/coralsdk"				//jni 包名类名
#define JNIFUNCTION "ReceiveCocos2dData"                                 //jni 函数名
#define JNIFUNCTIONTYPE "(Ljava/lang/String;)Ljava/lang/String;"        //jni 函数参数和返回值类型

#endif

USING_NS_CC;

using namespace cocos2d;
using namespace CFMJson;

#if (CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID)
const char* JniToAndroid(const char* data)
{
	JniMethodInfo minfo;  
	jstring jstr;
	jobject jobj;
	const char *tReturn = NULL;

	if (JniHelper::getStaticMethodInfo(minfo, JNIPAGECLASS,  
		JNIFUNCTION, JNIFUNCTIONTYPE))  
	{
		jstr = minfo.env->NewStringUTF(data);
		jstring ReturnStr = (jstring)minfo.env->CallStaticObjectMethod(minfo.classID, minfo.methodID,jstr);		

		tReturn = minfo.env->GetStringUTFChars(ReturnStr, NULL);

		minfo.env->DeleteLocalRef(jstr);
		minfo.env->DeleteLocalRef(ReturnStr);
		minfo.env->DeleteLocalRef(minfo.classID);
	}

	return tReturn;

}
#endif

void BnLogin(int type)
{
#if (CC_TARGET_PLATFORM == CC_PLATFORM_IOS)
    IOSLogin(type);
#endif

#if (CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID)

	char buf[2] = {0};
	memset(buf, 0, 2);
	sprintf(buf,"%d",type);
	FMJSONDictionary* Loginjson = new FMJSONDictionary();	//创建jsonDictionary
	Loginjson->insertItem("action", "1");
	Loginjson->insertItem("type", buf);
	string tReturn = Loginjson->getDescription();			//转成string
	JniToAndroid(tReturn.c_str());							//tReturn.c_str();转成字符串const char *
	delete Loginjson;

#endif
}

bool BnGetLoginStatus()
{
#if (CC_TARGET_PLATFORM == CC_PLATFORM_IOS)
    bool isLogin = IOSGetLoginStatus();
    return isLogin;
#endif

#if (CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID)

	FMJSONDictionary * LoginStJson = new FMJSONDictionary();
	LoginStJson->insertItem("action","2");
	string tReturn = LoginStJson->getDescription();
	const char* isLogin = JniToAndroid(tReturn.c_str());
	delete LoginStJson;
	if (strcmp(isLogin, "YES") == 0)
	{
		return true;
	}
	else
	{
		return false;
	}

#endif
}

void BnShowGameHome()
{
#if (CC_TARGET_PLATFORM == CC_PLATFORM_IOS)
    
    IOSShowGameHome();
    
#endif
    
#if (CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID)
    
	FMJSONDictionary * ShowGameHomeJson = new FMJSONDictionary();
	ShowGameHomeJson->insertItem("action","4");
	string tReturn = ShowGameHomeJson->getDescription();
	JniToAndroid(tReturn.c_str());
	delete ShowGameHomeJson;
    
#endif
}

//隐藏SDK
void BnHideSDK()
{
#if (CC_TARGET_PLATFORM == CC_PLATFORM_IOS)
    IOSBnHideSDK();
#endif
    
#if (CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID)
    
    FMJSONDictionary * HideSDKJson = new FMJSONDictionary();
	HideSDKJson->insertItem("action","8");
	string tReturn = HideSDKJson->getDescription();
	JniToAndroid(tReturn.c_str());
	delete HideSDKJson;
#endif
}

void BnGameOver(int score,const char* gamedata, int gold, const char* baseStr)
{
#if (CC_TARGET_PLATFORM == CC_PLATFORM_IOS)

    //IOSGameOver(score, gold, baseStr);
    IOSFinishGame(score,gamedata, gold, baseStr);
#endif

#if (CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID)

	char Score[10] = {0};
	char Gold[10] = {0};
	memset(Score,0,10);
	memset(Gold,0,10);
	sprintf(Score,"%d",score);
	sprintf(Gold,"%d",gold);
	FMJSONDictionary* FinishGameJson = new FMJSONDictionary();
	FinishGameJson->insertItem("action", "3");
	FinishGameJson->insertItem("score", Score);
	FinishGameJson->insertItem("gold", Gold);
	FinishGameJson->insertItem("baseStr", baseStr);
    FinishGameJson->insertItem("gamedata", gamedata);
	string tReturn = FinishGameJson->getDescription();
	JniToAndroid(tReturn.c_str());
	delete FinishGameJson;

#endif

}

//==============拓展功能===============

BnUser *BnGetProfile()
{
    
#if (CC_TARGET_PLATFORM == CC_PLATFORM_IOS)
    char *buf = new char[512];
    memset(buf, 0, 512);
    const char *UserName = IOSGetProfile();
    if(UserName == NULL)
    {
        return NULL;
    }
    CFMJson::Reader reader;
    CFMJson::FMValue root;
    
    if (!reader.parse(UserName, root, false)) {
        return NULL;
    }
    
    CFMJson::FMValue arr = root;
    BnUser *User = new BnUser();
    User->BnSetUserKey(arr["userkey"].asCString());
    User->BnSetUserName(arr["username"].asCString());
    User->BnSetUserHead(arr["headurl"].asCString());

    return User;
    
#endif

#if (CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID)

	char *buf = new char[512];
	memset(buf, 0, 512);
	FMJSONDictionary * GetProfileJson = new FMJSONDictionary();
	GetProfileJson->insertItem("action","5");
	string tReturn = GetProfileJson->getDescription();
	const char* UserData = JniToAndroid(tReturn.c_str());
	delete GetProfileJson;

	CFMJson::Reader reader;
	CFMJson::FMValue root;

	if (!reader.parse(UserData, root, false)) {
		return NULL;
	}

	CFMJson::FMValue arr = root;
	BnUser *User = new BnUser();
	User->BnSetUserKey(arr["userkey"].asCString());
	User->BnSetUserName(arr["username"].asCString());
	User->BnSetUserHead(arr["headurl"].asCString());

	return User;
	
#endif
}

bool BnGetMusicOnStatus()
{
#if (CC_TARGET_PLATFORM == CC_PLATFORM_IOS)
    bool isOn = IOSGetMusicOnStatus();
    
    return isOn;
#endif

#if (CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID)
	FMJSONDictionary * GetMusicOnJson = new FMJSONDictionary();
	GetMusicOnJson->insertItem("action","6");
	string tReturn = GetMusicOnJson->getDescription();
	const char* isOn = JniToAndroid(tReturn.c_str());
	delete GetMusicOnJson;
	if (strcmp(isOn, "YES") == 0)
	{
		return true;
	}
	else
	{
		return false;
	}
#endif

}

bool BnGetSoundOnStatus()
{
#if (CC_TARGET_PLATFORM == CC_PLATFORM_IOS)
    bool isOn = IOSGetSoundOnStatus();
    
    return isOn;
#endif

#if (CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID)

	FMJSONDictionary * GetSoundOnJson = new FMJSONDictionary();
	GetSoundOnJson->insertItem("action","7");
	string tReturn = GetSoundOnJson->getDescription();
	const char* isOn = JniToAndroid(tReturn.c_str());
	delete GetSoundOnJson;
	if (strcmp(isOn, "YES") == 0)
	{
		return true;
	}
	else
	{
		return false;
	}

#endif
}

//增加或减少金币
bool BnSetGameGold(int gold)
{
#if (CC_TARGET_PLATFORM == CC_PLATFORM_IOS)
    return IOSBnSetGameGold(gold);
#endif
    
#if (CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID)
    char Gold[20] = {0};
	memset(Gold,0,20);
	sprintf(Gold,"%d",gold);
    FMJSONDictionary * SetGameGoldJson = new FMJSONDictionary();
	SetGameGoldJson->insertItem("action","9");
    SetGameGoldJson->insertItem("gold",Gold);
	string tReturn = SetGameGoldJson->getDescription();
    const char* Result = JniToAndroid(tReturn.c_str());
	delete SetGameGoldJson;
	if (strcmp(Result, "YES") == 0)
	{
		return true;
	}
	else
	{
		return false;
	}
#endif
}

//获取金币
int BnGetGameGold()
{
#if (CC_TARGET_PLATFORM == CC_PLATFORM_IOS)
    
    return IOSBnGetGameGold();
#endif
    
#if (CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID)
    
    int GoldNum = 0;
    FMJSONDictionary * GetGameGoldJson = new FMJSONDictionary();
	GetGameGoldJson->insertItem("action","10");
	string tReturn = GetGameGoldJson->getDescription();
	const char *Gold = JniToAndroid(tReturn.c_str());
    if(Gold == NULL)
    {
        __android_log_print(ANDROID_LOG_INFO,"Test","Game Gold is NULL!");
        return 0;
    }
    GoldNum = atoi(Gold);
	delete GetGameGoldJson;
    return GoldNum;
#endif
    
}

//page:1、爱心 2、金币 3、钻石
void BnShowStoreView(int page)
{
#if (CC_TARGET_PLATFORM == CC_PLATFORM_IOS)
    IOSBnShowStoreView(page);
#endif
    
#if (CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID)
    
    int GoldNum = 0;
    FMJSONDictionary * ShowShopJson = new FMJSONDictionary();
	ShowShopJson->insertItem("action","11");
	ShowShopJson->insertItem("page",page);
	string tReturn = ShowShopJson->getDescription();
	JniToAndroid(tReturn.c_str());
    
	delete ShowShopJson;
    
#endif
	
}

//道具保存
void BnSetGamePropSave(const char *prop)
{
#if (CC_TARGET_PLATFORM == CC_PLATFORM_IOS)
    IOSBnSetGamePropSave(prop);
#endif
#if (CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID)
    
    int GoldNum = 0;
    FMJSONDictionary * GamePropJson = new FMJSONDictionary();
	GamePropJson->insertItem("action","12");
	GamePropJson->insertItem("prop",prop);
	string tReturn = GamePropJson->getDescription();
	JniToAndroid(tReturn.c_str());
    
	delete GamePropJson;
    
#endif
}

//获取道具
const char *BnGetGameProp()
{
#if (CC_TARGET_PLATFORM == CC_PLATFORM_IOS)
    return IOSBnGetGameProp();
#endif
#if (CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID)
    
    FMJSONDictionary * PropJson = new FMJSONDictionary();
	PropJson->insertItem("action","13");
	string tReturn = PropJson->getDescription();
	const char *Prop = JniToAndroid(tReturn.c_str());
    if(Prop == NULL)
    {
        __android_log_print(ANDROID_LOG_INFO,"BNLog","Game Prop is NULL!");
        return "";
    }
    delete PropJson;
    return Prop;
#endif
}

//获取钻石数量
int BnGetGameDiamond()
{
#if (CC_TARGET_PLATFORM == CC_PLATFORM_IOS)
    
    return IOSBnGetGameDiamond();
#endif
    
#if (CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID)
    
    int DiamondNum = 0;
    FMJSONDictionary *GetGameDiamondJson = new FMJSONDictionary();
	GetGameDiamondJson->insertItem("action","14");
	string tReturn = GetGameDiamondJson->getDescription();
	const char *Diamond = JniToAndroid(tReturn.c_str());
    if(Diamond == NULL)
    {
        __android_log_print(ANDROID_LOG_INFO,"BNLog","Game Diamond is NULL!");
        return 0;
    }
    DiamondNum = atoi(Diamond);
	delete GetGameDiamondJson;
    return DiamondNum;
#endif
}

//增加或减少钻石
bool BnSetGameDiamond(int DiamondNum)
{
#if (CC_TARGET_PLATFORM == CC_PLATFORM_IOS)
    return IOSBnSetGameDiamond(DiamondNum);
#endif
    
#if (CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID)
    char Diamond[20] = {0};
	memset(Diamond,0,20);
	sprintf(Diamond,"%d",DiamondNum);
    FMJSONDictionary * SetGameDiamondJson = new FMJSONDictionary();
	SetGameDiamondJson->insertItem("action","15");
    SetGameDiamondJson->insertItem("diamond",Diamond);
	string tReturn = SetGameDiamondJson->getDescription();
    
    const char* Result = JniToAndroid(tReturn.c_str());
	delete SetGameDiamondJson;
	if (strcmp(Result, "YES") == 0)
	{
		return true;
	}
	else
	{
		return false;
	}
#endif
}

//获取爱心数量
int BnGetGamePower()
{
#if (CC_TARGET_PLATFORM == CC_PLATFORM_IOS)
    
    return IOSBnGetGamePower();
#endif
    
#if (CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID)
    
    int PowerNum = 0;
    FMJSONDictionary *GetGamePowerJson = new FMJSONDictionary();
	GetGamePowerJson->insertItem("action","16");
	string tReturn = GetGamePowerJson->getDescription();
	const char *Power = JniToAndroid(tReturn.c_str());
    if(Power == NULL)
    {
        __android_log_print(ANDROID_LOG_INFO,"BNLog","Game Power is NULL!");
        return 0;
    }
    PowerNum = atoi(Power);
	delete GetGamePowerJson;
    return PowerNum;
#endif
}

//增加或减少爱心
bool BnSetGamePower(int PowerNum)
{
#if (CC_TARGET_PLATFORM == CC_PLATFORM_IOS)
    return IOSBnSetGamePower(PowerNum);
#endif
    
#if (CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID)
    char Power[20] = {0};
	memset(Power,0,20);
	sprintf(Power,"%d",PowerNum);
    FMJSONDictionary * SetGamePowerJson = new FMJSONDictionary();
	SetGamePowerJson->insertItem("action","17");
    SetGamePowerJson->insertItem("power",PowerNum);
	string tReturn = SetGamePowerJson->getDescription();
    
    const char* Result = JniToAndroid(tReturn.c_str());
	delete SetGamePowerJson;
	if (strcmp(Result, "YES") == 0)
	{
		return true;
	}
	else
	{
		return false;
	}
#endif
}
