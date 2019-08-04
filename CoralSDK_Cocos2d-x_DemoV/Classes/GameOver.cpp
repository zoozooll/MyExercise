#include "GameOver.h"
#include "cocos-ext.h"
#include "CoralSdkApi/BnInterface.h"

USING_NS_CC;


CGameOver* CGameOver::create(int score,const char* gamedata, int gold,const char* str)
{
    CGameOver *go = new CGameOver();

    if (go && go->init(score,gamedata, gold, str)) {
        go->autorelease();
        return go;
    }
    else
    {
        delete go;
        go = NULL;
        return NULL;
    }
}

bool CGameOver::init(int score,const char* gamedata, int gold,const char* str)
{
    auto layer = LayerColor::create(Color4B(0, 0, 0, 100));
    this->addChild(layer, 9999);
 
    BnGameOver(score,gamedata, gold, str);
    
    return true;
}