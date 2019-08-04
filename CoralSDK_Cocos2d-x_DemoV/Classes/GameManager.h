#ifndef  _GAME_MANAGER_H_
#define  _GAME_MANAGER_H_
#include "cocos2d.h"

#include "GameConfig.h"



#define deGameManager	CGameManager::getInstance()

class CGameView;
class CGameManager : public cocos2d::Ref
{
public:
	CGameManager();
	~CGameManager();

	static CGameManager* getInstance();
	void start();
    void pause();
    void resume();
    void playAudioMusic(const char* filename);
    void playAudioSound(const char* filename);
    void stopAudioMusic();
    void pauseAudioMusic();
    void resumeAudioMusic();
  
	void changeScene(EGameScene gs);
	void changeGameState(EGameState gs);
	
    

	inline cocos2d::Size getVisibleSize() {return _visibleSize;}
    inline cocos2d::Size getWinSize() {return _winSize;}
    inline cocos2d::Size getOrigin() {return _origin;}
	inline cocos2d::Point getScreenCenter() {return cocos2d::Point(_visibleSize.width / 2, _visibleSize.height / 2);}
 
    
    int _Music;
    int _Sound;
    void setMusic(int m);
    int getMusic();
    void setSound(int s);
    int getSound();
	void gameOver(int score,const char* gamedata, int gold, const char* str);


	CC_SYNTHESIZE(int, _LoginStatus, LoginStatus);
    CC_SYNTHESIZE(int, _LoginFlag, LoginFlag);
    CC_SYNTHESIZE(int, _sceneflag, sceneflag);
    CC_SYNTHESIZE(int, _gameflag, gameflag);

    
private:
	bool init();
	cocos2d::Size _visibleSize;
    cocos2d::Size _winSize;
    cocos2d::Size _origin;


};


#endif // _GAME_MANAGER_H_