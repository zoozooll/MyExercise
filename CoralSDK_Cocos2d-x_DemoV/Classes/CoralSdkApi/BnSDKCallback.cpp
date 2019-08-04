#include "BnSDKCallback.h"
#include "fmjson/FMjson_lib.h"
#include "BnInterface.h"
#if (CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID)
#include "platform/android/jni/JniHelper.h"  
#include <android/log.h>
#endif
#include <string.h>
#include "fmjson/FMContentJsonDictionary.h"

using namespace FMCS;
using namespace cocos2d;
using namespace std;	//string

static BnSDKCallback* gSDKCallback = NULL;
static BnSDKCallback* p = NULL;

/*======================================
*
*	SDK call cocos2d-x
*
*======================================*/

void BnSDKCallback::BnGameUserSelectedNotification(CCArray *userlist)
{
}

void BnSDKCallback::BnLogoutNotify()
{
}

void BnSDKCallback::BnMusicTurnOffAndOn(bool isOn)
{
}

void BnSDKCallback::BnSoundTurnOffAndOn(bool isOn)
{
}

void BnSDKCallback::BnLoginResult(int isLogin)
{
}

//游戏结束后返回按键回调
void BnSDKCallback::BnGameBackNotification()
{
}

void BnSDKCallback::SetSDKCallback(BnSDKCallback *aCallBack)
{
    p = NULL;
	gSDKCallback = aCallBack;
}

/*=====================================
*
*	SDK call cocos2d-x
*
*=====================================*/

void GameUserSelectedNotification(const char *userlist)
{
#if (CC_TARGET_PLATFORM == CC_PLATFORM_IOS)
    
    if(NULL == gSDKCallback)
    {
        CCLOG("The gSDKCallback pointer is null, please SetSDKCallback!");
        return;
    }
    if(userlist)
    {
        CCArray *UserList =  CCArray::create();
        
        UserList->retain();
        CFMJson::Reader reader;
        CFMJson::FMValue root;
        
        if (!reader.parse(std::string(userlist), root, false))//userlist
        {
            return;
        }
        CFMJson::FMValue arr = root["userlist"];
        
        printf("arr.size() = %d\n", arr.size());
        
        if (arr.size() > 0)
        {
            for(int i = 0; i < arr.size(); i++)
            {
                BnUser *user = BnUser::create();
                user->BnSetUserKey(arr[i]["userkey"].asCString());
                user->BnSetUserName(arr[i]["username"].asCString());
                user->BnSetUserHead(arr[i]["headurl"].asCString());
                user->BnSetGameData(arr[i]["gamedata"].asCString());
                user->BnSetGameScore(arr[i]["gamescore"].asCString());

                UserList->addObject(user);
            }
        }
        gSDKCallback->BnGameUserSelectedNotification(UserList);
        
    }
    
#endif
    
#if (CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID)
    
	if (gSDKCallback)
	{
		CCArray *UserList = CCArray::create();
        CFMJson::Reader reader;
        CFMJson::FMValue root;

        if (!reader.parse(userlist, root, false))//userlist
        {
            return;
        }
        CFMJson::FMValue arr = root;
        for (int i = 0; i < arr.size(); i++)
        {
            BnUser *user = new BnUser();
            user->BnSetUserKey(arr[i]["userkey"].asCString());
            user->BnSetUserName(arr[i]["username"].asCString());
            user->BnSetUserHead(arr[i]["headurl"].asCString());
            user->BnSetGameData(arr[i]["gamedata"].asCString());
            user->BnSetGameScore(arr[i]["gamescore"].asCString());

            UserList->addObject(user);
        }
		gSDKCallback->BnGameUserSelectedNotification(UserList);

	}
    else
        __android_log_print(ANDROID_LOG_INFO,"SDKCallbackError","The gSDKCallback pointer is null ,please SetSDKCallback!");
#endif
}

void LogoutNotify()
{
	if (gSDKCallback)
	{
		gSDKCallback->BnLogoutNotify();
	}
    else
        CCLOG("The gSDKCallback pointer is null, please SetSDKCallback!");

}

void LoginResult(int isLogin)
{
	if (gSDKCallback)
	{
		gSDKCallback->BnLoginResult(isLogin);
	}
    else
        CCLOG("The gSDKCallback pointer is null, please SetSDKCallback!");
}

//结束界面按键“返回”回调
void GameBackNotification()
{
    if (gSDKCallback)
	{
		gSDKCallback->BnGameBackNotification();
	}
    else
        CCLOG("The gSDKCallback pointer is null, please SetSDKCallback!");
}

void MusicTurnOffAndOn(bool isOn)
{
	if (gSDKCallback)
	{
		gSDKCallback->BnMusicTurnOffAndOn(isOn);
	}
    else
        CCLOG("The gSDKCallback pointer is null, please SetSDKCallback!");
}

void SoundTurnOffAndOn(bool isOn)
{
	if (gSDKCallback)
	{
		gSDKCallback->BnSoundTurnOffAndOn(isOn);
	}
    else
        CCLOG("The gSDKCallback pointer is null, please SetSDKCallback!");
}

extern "C"
{
#if (CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID)
	void Java_com_friendou_Interface_coralsdk_SendtoGame(JNIEnv *env, jobject thiz, jstring info)
	{
		const char *temp = env->GetStringUTFChars(info, NULL);

		if (gSDKCallback == NULL || p != NULL)
		{
			__android_log_print(ANDROID_LOG_INFO,"SDKCallbackError","The gSDKCallback pointer is null !");
			return;
		}

		FMJSONDictionary *Recivejson = new FMJSONDictionary();

		Recivejson->initWithDescription(temp);//

		__android_log_print(ANDROID_LOG_INFO,"SDKCallback","SDKCallback message = %s", temp);

		const char *ReciveAction = Recivejson->getItemStringFMValue("action");

		if (strcmp(ReciveAction, "1001") == 0)
		{
			char const *userlist = Recivejson->getItemStringFMValue("userlist");
			__android_log_print(ANDROID_LOG_INFO,"Test","userlist = %s", userlist);

			GameUserSelectedNotification(userlist);//userlist tReturn.c_str()			
		}
		else if (strcmp(ReciveAction, "1002") == 0)
		{
//			char const *userlist = Recivejson->getItemStringFMValue("userlist");
//			GameReplayNotification(userlist);
		}
		else if (strcmp(ReciveAction, "1003") == 0)
		{	
			__android_log_print(ANDROID_LOG_INFO,"Test","LogoutNotify1003");
			LogoutNotify();
		}
		else if (strcmp(ReciveAction, "1004") == 0)
		{
			bool isOn = Recivejson->getItemBoolFMvalue("isOn",false);
			MusicTurnOffAndOn(isOn);
		}
		else if (strcmp(ReciveAction, "1005") == 0)
		{
			bool isOn = Recivejson->getItemBoolFMvalue("isOn",false);
			SoundTurnOffAndOn(isOn);
		}
		else if (strcmp(ReciveAction, "1006") == 0)
		{
			int aisLogin = Recivejson->getItemIntFMValue("isLogin",0);
			LoginResult(aisLogin);
			__android_log_print(ANDROID_LOG_INFO,"Test","LoginResult 1006 = %d", aisLogin);
		}
		else if (strcmp(ReciveAction, "1007") == 0)
		{
			__android_log_print(ANDROID_LOG_INFO,"Test","GameBackNotification   1007");
			GameBackNotification();
		}
		else if (strcmp(ReciveAction, "1008") == 0)
		{
//			ApplicationWillResignActive();
		}
		else
		{
			__android_log_print(ANDROID_LOG_INFO,"SDKCallbackError","Have no this callback !");
			return;
		}
	}
#endif
}

