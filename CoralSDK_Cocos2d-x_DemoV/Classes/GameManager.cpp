#include "GameManager.h"

#include "Welcome.h"
#include "GameOver.h"
#include "Menu.h"
#include "SimpleAudioEngine.h"

#include "Snake/GameLayer.h"
#include "None/GameView.h"

USING_NS_CC;


CGameManager::CGameManager()
{

}

CGameManager::~CGameManager()
{
    
}

static CGameManager *s_SharedManager = NULL;
CGameManager* CGameManager::getInstance()
{
	if (!s_SharedManager)
	{
		s_SharedManager = new CGameManager();
		s_SharedManager->init();

	}
	
	return s_SharedManager;
	
}

bool CGameManager::init()
{
	_visibleSize = Director::getInstance()->getVisibleSize();
    _winSize = Director::getInstance()->getWinSize();
    _origin = Director::getInstance()->getVisibleOrigin();

	return true;
}

void CGameManager::start()
{
    deGameManager->setsceneflag(0);
    changeGameState(EGameState::NONE);
}
void CGameManager::pause()
{
    changeGameState(EGameState::PAUSE);
}
void CGameManager::resume()
{
    changeGameState(EGameState::RESUME);
}



//Audio setting start
void CGameManager::pauseAudioMusic()
{
    if (deGameManager->getMusic())
    {
        CocosDenshion::SimpleAudioEngine::getInstance()->pauseBackgroundMusic();
    }
}
void CGameManager::resumeAudioMusic()
{
    if (!deGameManager->getMusic())
    {
        CocosDenshion::SimpleAudioEngine::getInstance()->resumeBackgroundMusic();
    }
}
void CGameManager::stopAudioMusic()
{
    if (deGameManager->getMusic())
    {
        CocosDenshion::SimpleAudioEngine::getInstance()->stopBackgroundMusic();
    }
}
void CGameManager::playAudioSound(const char* filename)
{
    if (deGameManager->getSound())
    {
        if(filename==NULL)
        {
            filename = SOUND_BT;
        }
        
        CocosDenshion::SimpleAudioEngine::getInstance()->playEffect(filename);
    }
}
void CGameManager::playAudioMusic(const char* filename)
{
 	if (deGameManager->getMusic())
	{
        if(filename==NULL)
        {
            filename = SOUND_BG;
        }
        CocosDenshion::SimpleAudioEngine::getInstance()->setBackgroundMusicVolume(0.7f);
        CocosDenshion::SimpleAudioEngine::getInstance()->playBackgroundMusic(filename, true);
	}
    else
    {
        CocosDenshion::SimpleAudioEngine::getInstance()->stopBackgroundMusic();
    }

}

//Audio setting end





void CGameManager::changeScene(EGameScene gs)
{
	auto *scene = Scene::create();
	switch (gs)
	{
        case EGameScene::MENU:
        {
            auto menu = CMenu::create();
            scene->addChild(menu, 1);
            Director::getInstance()->replaceScene(scene);
            deGameManager->setsceneflag(1);
            break;
        }
        case EGameScene::GAMEVIEW:
        {
            auto gameview = CGameView::create();
            scene->addChild(gameview, 1);
            Director::getInstance()->replaceScene(scene);
            deGameManager->setsceneflag(1);
            break;
        }
        case EGameScene::GAMELAYER:
        {
            auto gamelayer = CGameView::create();
            scene->addChild(gamelayer, 1);
            Director::getInstance()->replaceScene(scene);
            deGameManager->setsceneflag(1);
            break;
        }
        case EGameScene::WELCOME:
        {
            auto welcome = CWelcome::create();
            scene->addChild(welcome, 1);
            if(_sceneflag>0)
            {
                Director::getInstance()->replaceScene(scene);
            }
            else
            {
                Director::getInstance()->runWithScene(scene);
            }
            deGameManager->setsceneflag(1);
            break;
        }
        default:
            break;
	}
	
}


void CGameManager::changeGameState(EGameState gs)
{
	switch (gs)
	{
        case EGameState::NONE:
            changeScene(EGameScene::WELCOME);
            break;
        case EGameState::LOGINERROR:
            break;
        case EGameState::LOGINSUCCESS:
            changeScene(EGameScene::MENU);
            break;
        case EGameState::PLAYING:
            changeScene(EGameScene::GAMELAYER);
            break;
        case EGameState::PAUSE:
            break;
        case EGameState::RESUME:
            break;
        case EGameState::GAMEOVER:
            break;
        case EGameState::RANDING:
        {
            changeScene(EGameScene::MENU);
            break;
        }
        case EGameState::LOGINOUT:
        {
            changeScene(EGameScene::WELCOME);
            break;
        }
        default:
            break;

            
	}

}

void CGameManager::gameOver(int score,const char* gamedata, int gold,const char* str)
{
    auto gameover = CGameOver::create(score,gamedata,gold,str);
    Director::getInstance()->getRunningScene()->addChild(gameover);
    
}













void CGameManager::setMusic(int m)
{
    _Music = m;
    deGameManager->playAudioMusic(NULL);
}
int CGameManager::getMusic()
{
    return _Music;
}
void CGameManager::setSound(int s)
{
    _Sound = s;
}
int CGameManager::getSound()
{
    return _Sound;
}
