//
//  SnakeNode.h
//  Hello
//
//  Created by 酱油瓶 on 14-4-16.
//
//

#ifndef __Hello__SnakeNode__
#define __Hello__SnakeNode__

#include "cocos2d.h"
USING_NS_CC;



enum Direction
{
    UP = 1,
    DOWN=2,
    LEFT=3,
    RIGHT=4,
    NONE
};

class SnakeNode : public cocos2d::Sprite
{
public:
    static SnakeNode* create(int r,int c,const std::string& filename);
    static SnakeNode* createbody(int r,int c);
    SnakeNode();
    ~SnakeNode();
    Sprite* sprite;

    Direction dir;
    int row, col;
  
    void setRow(int r);
    void rePos(bool r);
    void setCol(int c);
    
private:
	virtual bool init(int r,int c,const std::string& filename);
    
 
    
};

#endif /* defined(__Hello__SnakeNode__) */
