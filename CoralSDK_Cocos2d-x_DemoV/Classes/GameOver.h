#ifndef  _GAME_OVER_H_
#define  _GAME_OVER_H_
#include "cocos2d.h"


class CGameOver : public cocos2d::Layer 
{
public:

	static CGameOver* create(int score,const char* gamedata, int gold,const char* str);



private:
	virtual bool init(int score,const char* gamedata, int gold,const char* str);
};
#endif //_GAME_OVER_H_

