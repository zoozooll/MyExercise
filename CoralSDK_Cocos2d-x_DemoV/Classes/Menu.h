#ifndef  _CMenu_H_
#define  _CMenu_H_
#include "cocos2d.h"
USING_NS_CC;

class CMenu : public cocos2d::Layer
{
public:
	CREATE_FUNC(CMenu);
    void BnFunc();

private:
	virtual bool init();
    bool InitMap();
    virtual void onKeyReleased(cocos2d::EventKeyboard::KeyCode keyCode, cocos2d::Event* event);
    ~CMenu();
};



#endif	//_Menu_H_