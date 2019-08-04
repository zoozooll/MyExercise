#include "GameView.h"
#include "GameManager.h"
#include "SimpleAudioEngine.h"
#include "GameOver.h"
USING_NS_CC;
using namespace std;



bool CGameView::init()
{
	if(!Layer::create())
		return false;
	// update
	scheduleUpdate();

	//touch
    
    _touchListener = EventListenerTouchOneByOne::create();
    _touchListener->setSwallowTouches(true);//设置是否想下传递触摸
    _touchListener->onTouchBegan = CC_CALLBACK_2(CGameView::onTouchBegan, this);
    _touchListener->onTouchMoved = CC_CALLBACK_2(CGameView::onTouchMoved, this);
    _touchListener->onTouchEnded = CC_CALLBACK_2(CGameView::onTouchEnded, this);
    _touchListener->onTouchCancelled = CC_CALLBACK_2(CGameView::onTouchCancelled, this);
    //_eventDispatcher->addEventListenerWithFixedPriority(_touchListener, 1);//有限级别设置正常状态
    

    //bg
 	auto bg = Sprite::createWithSpriteFrameName(GAMEVIEW_BG);
    bg->setAnchorPoint(Point::ZERO);
	bg->setPosition(Point::ZERO);
	this->addChild(bg, 0);
    
    
    auto text = LabelTTF::create("Click on the end \r\nof the game ", "",50, Size(500,50), TextHAlignment::CENTER,TextVAlignment::CENTER);
    auto uiBack = MenuItemLabel::create(text, CC_CALLBACK_1(CGameView::BackToMain, this));
    uiBack->setPosition(Point(deGameManager->getWinSize().width/2, deGameManager->getWinSize().height/2));
    auto menu = Menu::create(uiBack,NULL);
    menu->setPosition(Point::ZERO);
    this->addChild(menu);
    
    

	return true;
}
CGameView::~CGameView()
{
    _eventDispatcher->removeEventListener(_touchListener);
}

void CGameView::allowTouches(float dt)
{

}

void CGameView::disabledTouches(float dt)
{
    
}
void CGameView::BackToMain(Ref *sender)
{
    deGameManager->gameOver(10, "", 100, "这个成绩比较一般");
}
bool CGameView::onTouchBegan(Touch *touch, Event * event)
{
    return true;
}
void CGameView::onTouchEnded(Touch *touch, Event * event)
{
    
}
void CGameView::onTouchCancelled(Touch *touch, Event * event)
{
    
}
void CGameView::onTouchMoved(Touch *touch, Event * event)
{
    
}

void CGameView::update(float delta)
{
 
}

