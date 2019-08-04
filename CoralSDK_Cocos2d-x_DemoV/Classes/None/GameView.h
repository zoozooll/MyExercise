#ifndef  _GAME_VIEW_H_
#define  _GAME_VIEW_H_

#include "cocos2d.h"
USING_NS_CC;
using namespace std;


class CGameView : public cocos2d::Layer 
{
public:
    
	CREATE_FUNC(CGameView);
    
    ~CGameView();
	virtual bool init();
    void BackToMain(Ref *sender);

    bool onTouchBegan(Touch *touch, Event * event);
    void onTouchEnded(Touch *touch, Event * event);
    void onTouchCancelled(Touch *touch, Event * event);
    void onTouchMoved(Touch *touch, Event * event);
    void allowTouches(float dt);
    void disabledTouches(float dt);
    
private:
    EventListenerTouchOneByOne* _touchListener;
	virtual void update(float delta);
    
 
	
};
#endif //_GAME_VIEW_H_

