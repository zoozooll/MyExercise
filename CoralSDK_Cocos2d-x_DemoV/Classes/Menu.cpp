#include "Menu.h"
#include "cocos2d.h"
#include "GameManager.h"
#include "CoralSdkApi/BnInterface.h"

/*
 本游戏是基于珊瑚SDK开发，http://dev.friendou.com
 QQ：240795663 技术交流群：282732191 欢迎新手一起学习；
 */

USING_NS_CC;

void CMenu::onKeyReleased(cocos2d::EventKeyboard::KeyCode keyCode, cocos2d::Event* event)
{
    if(keyCode==cocos2d::EventKeyboard::KeyCode::KEY_BACKSPACE)
    {
        Director::getInstance()->end();
    }
    
}
CMenu::~CMenu()
{
 
}


bool CMenu::init()
{
    if ( !Layer::init() )
    {
        return false;
    }
 
    
    this->setKeyboardEnabled(true);

 	auto bg = Sprite::createWithSpriteFrameName(MENU_BG);
    bg->setAnchorPoint(Point::ZERO);
	bg->setPosition(Point::ZERO);
	this->addChild(bg, 0);
    
    
    auto bnfunc = CallFuncN::create(std::bind(&CMenu::BnFunc, this));
    this->runAction(bnfunc);
    
    return true;
}

bool CMenu::InitMap()
{
    return true;
}

void CMenu::BnFunc()
{
    BnShowGameHome();

    ParticleSnow * ccps = ParticleSnow::create();
    ccps->setSpeed(200.0f);
    ccps->setAutoRemoveOnFinish(true);
    this->addChild(ccps);
}