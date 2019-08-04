#ifndef __Hello__GameLayer__
#define __Hello__GameLayer__

#include "cocos2d.h"
#include "SnakeNode.h"
USING_NS_CC;



/*
 本游戏是基于珊瑚SDK开发，http://dev.friendou.com
 QQ：240795663 技术交流群：282732191 欢迎新手一起学习；
 */


class GameLayer: public cocos2d::Layer
{
private:
    DrawNode* drawnode;
    SnakeNode* food;
    SnakeNode* head;
    cocos2d::Vector<SnakeNode*> body;
    
    
    ~GameLayer();
    GameLayer();
    
public:
    void BnFunc(float f);
    virtual bool init();
    void update(float dt);
    bool Over();
    CREATE_FUNC(GameLayer);
    static cocos2d::Scene* createScene();
    void refreshfood(float dt);
    
    void Steering(Ref *sender);

    static int grid[12][12];
    static int gridwidth;
    static int gridcount;
    static int linewidth;
    static int startX;
    static int startY;
    static int defaultbodynum;
    
    Label* scorelabel;
    int score;
    
    
    Sprite* foodtag;
 
    virtual void onKeyReleased(cocos2d::EventKeyboard::KeyCode keyCode, cocos2d::Event* event);
};


#endif /* defined(__Hello__GameLayer__) */
