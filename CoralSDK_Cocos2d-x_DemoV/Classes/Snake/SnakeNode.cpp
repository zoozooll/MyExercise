#include "GameLayer.h"
#include "SnakeNode.h"
#include "GameManager.h"


/*
 本游戏是基于珊瑚SDK开发，http://dev.friendou.com
 QQ：240795663 欢迎新手一起学习；
 */


USING_NS_CC;

bool SnakeNode::init(int r,int c,const std::string& filename)
{
    if ( !Sprite::init() )
    {
        return false;
    }
    row = r;
    col = c;
 
    
    sprite = Sprite::createWithSpriteFrameName(filename);
    sprite->setAnchorPoint(Point::ZERO);
    sprite->setPosition(Point(GameLayer::startX+col * GameLayer::gridwidth,
                              row * GameLayer::gridwidth+GameLayer::startY));
    this->addChild(sprite);
    

    return true;
    
}
SnakeNode* SnakeNode::createbody(int r,int c)
{
    r = (r<0)?0:r;
    c = (c<0)?0:c;
    
    SnakeNode *sn = new SnakeNode();
    bool it = true;
    u_int32_t ar  = arc4random();
    if (ar % 11<=2) {
        sn->init(r,c,GAMEVIEW_SNAKE_BODY_1);
        log("2");
    }
    else if (ar % 11<=4) {
        sn->init(r,c,GAMEVIEW_SNAKE_BODY_2);
    }
    else if (ar % 11<=6) {
        sn->init(r,c,GAMEVIEW_SNAKE_BODY_3);
    }
    else if (ar % 11<=8) {
        sn->init(r,c,GAMEVIEW_SNAKE_BODY_4);
    }
    else
    {
        sn->init(r,c,GAMEVIEW_SNAKE_BODY_5);
    }
    
    
    if (sn && it) {
        sn->autorelease();
        return sn;
    }
    else
    {
        delete sn;
        sn = NULL;
        return NULL;
    }
}

SnakeNode* SnakeNode::create(int r,int c,const std::string& filename)
{
    r = (r<0)?0:r;
    c = (c<0)?0:c;
    SnakeNode *sn = new SnakeNode();
    if (sn && sn->init(r,c,filename)) {
        sn->autorelease();
        return sn;
    }
    else
    {
        delete sn;
        sn = NULL;
        return NULL;
    }
}
void SnakeNode::setRow(int r)
{
    if(r!=row)
    {
        row = (r<0)?0:r;
        SnakeNode::rePos(true);
    }
}
void SnakeNode::setCol(int c)
{
    if(c!=col)
    {
        col = (c<0)?0:c;
        SnakeNode::rePos(true);
    }
}
void SnakeNode::rePos(bool r)
{
    const Point p = Point(GameLayer::startX+col * GameLayer::gridwidth,
                          row * GameLayer::gridwidth+GameLayer::startY);
    if(r)
    {
        ActionInterval* mt = MoveTo::create(0.16, p);
        sprite->runAction(mt);
    }
    else
    {
        sprite->setPosition(p);
    }
}

SnakeNode::SnakeNode()
{
    
}
SnakeNode::~SnakeNode()
{
    
}