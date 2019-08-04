#include "Welcome.h"
#include "GameManager.h"
#include "CoralSdkApi/BnInterface.h"


USING_NS_CC;

void CWelcome::onKeyReleased(cocos2d::EventKeyboard::KeyCode keyCode, cocos2d::Event* event)
{
    if(keyCode==cocos2d::EventKeyboard::KeyCode::KEY_BACKSPACE)
    {
        Director::getInstance()->end();
    }
    
}

CWelcome::~CWelcome()
{
//    auto cache = SpriteFrameCache::getInstance();
//    cache->removeSpriteFramesFromFile(SPRITE_CACHE_PLIST);
}

bool CWelcome::init()
{
    if ( !Layer::init() )
    {
        return false;
    }
    //cache
    SpriteFrameCache::getInstance()->addSpriteFramesWithFile(SPRITE_CACHE_PLIST);
    auto spriteSheet = SpriteBatchNode::create(SPRITE_CACHE);
    this->addChild(spriteSheet);

    
    scheduleUpdate();
    this->setKeyboardEnabled(true);
 
 	auto bg = Sprite::createWithSpriteFrameName(WELCOME_BG);
    bg->setAnchorPoint(Point::ZERO);
	bg->setPosition(Point::ZERO);
	this->addChild(bg, 0);

    
	auto logo = Sprite::createWithSpriteFrameName(WELCOME_LOGO);
	logo->setPosition(deGameManager->getWinSize().width/2 ,deGameManager->getWinSize().height/2+200);
	this->addChild(logo,1);

    auto text = LabelTTF::create("游客试玩", FONT_EN,30, Size(500,50), TextHAlignment::CENTER,TextVAlignment::CENTER);
    auto guestItem = MenuItemLabel::create(text, CC_CALLBACK_1(CWelcome::BtCallback, this));
    guestItem->setPosition(deGameManager->getWinSize().width/2 ,280);
    guestItem->setTag(TAG_WELCOME_BT_GUEST);

    
    auto spriteNormal = Sprite::createWithSpriteFrameName(WELCOME_BT_QQ_NORALIMAGE);
    auto QQItem = MenuItemSprite::create(spriteNormal,spriteNormal,spriteNormal,CC_CALLBACK_1(CWelcome::BtCallback, this));
	QQItem->setPosition(deGameManager->getWinSize().width/2 ,200);
    QQItem->setTag(TAG_WELCOME_BT_QQ);
    auto menu = Menu::create(QQItem,guestItem,NULL);
    menu->setPosition(Point::ZERO);
    menu->setTag(TAG_WELCOME_BT);
    menu->setVisible(false);
    this->addChild(menu, 1);


    auto bnfunc = CallFuncN::create(std::bind(&CWelcome::BnFunc, this));
    this->runAction(bnfunc);
   
    
    return true;
}
void CWelcome::BnFunc()
{
    ParticleSnow * ccps = ParticleSnow::create();
    ccps->setSpeed(200.0f);
    ccps->setAutoRemoveOnFinish(true);
    this->addChild(ccps);


    
    if(BnGetMusicOnStatus())
    {
        deGameManager->setMusic(1);
    }
    else
    {
        deGameManager->setMusic(0);
    }
    
    if(BnGetSoundOnStatus())
    {
        deGameManager->setSound(1);
    }
    else
    {
        deGameManager->setSound(0);
    }
    
    if(BnGetLoginStatus())
    {
        deGameManager->setLoginFlag(1);
    }
    else
    {
        Node* nd = this->getChildByTag(TAG_WELCOME_BT);
        nd->setVisible(true);

        deGameManager->setMusic(1);
    }
    
}
void CWelcome::update(float time)
{
    if(deGameManager->getLoginFlag()==1)
    {
        if(BnGetMusicOnStatus())
        {
            deGameManager->setMusic(1);
        }
        else
        {
            deGameManager->setMusic(0);
        }
        deGameManager->setLoginFlag(0);
        CWelcome::ShowLoading();
    }
    
    
}
void CWelcome::ShowLoading()
{
    Node* bt = this->getChildByTag(TAG_WELCOME_BT);
    bt->setVisible(false);
 
    
	auto loading = Label::createWithTTF("loading", FONT_EN,50, Size(200,50), TextHAlignment::LEFT,TextVAlignment::CENTER);
	loading->setPosition(Point(deGameManager->getWinSize().width/2, 200));
	loading->setTag(TAG_WELCOME_LOADING);
    loading->setVisible(true);
    this->addChild(loading, 1);
    
    
    //loading animate
    auto dt = CCDelayTime::create(0.5);
    auto ld = CallFuncN::create(std::bind(&CWelcome::Loading, this, loading));
    auto sq = CCSequence::create(dt,ld,NULL);
    
    auto loginok = CallFuncN::create(std::bind(&CWelcome::LoginOk, this));
    loading->runAction(CCSequence::create(CCRepeat::create(sq,8),loginok,NULL));

    return;
}


void CWelcome::LoginOk()
{
    deGameManager->changeGameState(EGameState::LOGINSUCCESS);
}
void CWelcome::Loading(cocos2d::Node *sender)
{
    auto loading = (Label *) sender;

    if(loading->getString().compare("loading...")==0)
    {
        loading->setString("loading");
    }
    else
    {
        loading->setString(String::createWithFormat("%s%s",loading->getString().c_str(),".")->getCString());
    }
    
}

void CWelcome::BtCallback(Ref* pSender)
{
    auto mii = (MenuItemImage*) pSender;
    switch (mii->getTag())
	{
        case TAG_WELCOME_BT_QQ:
        {
            deGameManager->playAudioSound(NULL);
            BnLogin(3);
            break;
        }
        case TAG_WELCOME_BT_GUEST:
        {
            deGameManager->playAudioSound(NULL);
            BnLogin(5);
            break;
        }
        default:
            break;
    }
}



