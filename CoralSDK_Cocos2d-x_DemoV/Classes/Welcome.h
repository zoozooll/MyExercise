#ifndef __CWELCOME_SCENE_H__
#define __CWELCOME_SCENE_H__
#include "cocos2d.h"

class  CWelcome: public cocos2d::Layer
{
public:
	CREATE_FUNC(CWelcome);
    
private:
	virtual bool init();
    
    void BtCallback(cocos2d::Ref* pSender);
    void Loading(cocos2d::Node *sender);
    void ShowLoading();
    void LoginOk();
    void update(float time);
    ~CWelcome();
    void BnFunc();

    virtual void onKeyReleased(cocos2d::EventKeyboard::KeyCode keyCode, cocos2d::Event* event);
};

#endif
