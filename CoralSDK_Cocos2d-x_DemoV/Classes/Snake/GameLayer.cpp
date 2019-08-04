#include "GameLayer.h"
#include "GameManager.h"
#include "SnakeNode.h"
#include "GameOver.h"

USING_NS_CC;



int GameLayer::gridwidth = 50;
int GameLayer::gridcount = 12;
int GameLayer::grid[12][12] = {0};
int GameLayer::linewidth = 2;
int GameLayer::startX = 0;
int GameLayer::startY = 0;
int GameLayer::defaultbodynum = 1;

void GameLayer::onKeyReleased(cocos2d::EventKeyboard::KeyCode keyCode, cocos2d::Event* event)
{
    if(keyCode==cocos2d::EventKeyboard::KeyCode::KEY_BACKSPACE)
    {
        
        Director::getInstance()->end();
    }
    
}

Scene* GameLayer::createScene()
{
    auto scene = Scene::create();
    auto layer = GameLayer::create();
    scene->addChild(layer);
    
    return scene;
}
GameLayer::GameLayer()
{
    score = 0;
}

GameLayer::~GameLayer()
{
    AnimationCache::destroyInstance();
}

bool GameLayer::init()
{
    if(!Layer::init())
    {
        return false;
    }
    
    this->setKeyboardEnabled(true);
    deGameManager->setgameflag(1);

    memset(GameLayer::grid,0,sizeof(int)*GameLayer::gridcount*GameLayer::gridcount);
    GameLayer::startX = (deGameManager->getVisibleSize().width - GameLayer::gridcount*GameLayer::gridwidth)/2;
    GameLayer::startY = deGameManager->getVisibleSize().height - GameLayer::gridcount*GameLayer::gridwidth-80;
 
    //初始化,食物，身体，蛇头
    for(int i=0;i<defaultbodynum;i++)
    {
        SnakeNode* sn = SnakeNode::create(gridcount/2 + i,gridcount/2,GAMEVIEW_SNAKE_BODY);
        this->addChild(sn);
        body.pushBack(sn);
        GameLayer::grid[sn->row][sn->col] = 1;
    }
    head = SnakeNode::create(gridcount/2 + defaultbodynum,gridcount/2,GAMEVIEW_SNAKE_HEAD);
    head->dir = Direction::NONE;
    GameLayer::grid[head->row][head->col] = 1;
    this->addChild(head);
    
    food = SnakeNode::create(0,0,GAMEVIEW_SNAKE_FOOD);

    foodtag = Sprite::create();
    foodtag->setPosition(Point(food->sprite->getPositionX()+5,food->sprite->getPositionY()+6));
    foodtag->setZOrder(100);
    foodtag->setAnchorPoint(Point::ZERO);
    foodtag->setScale(0.35);
 
    // With 2 loops and reverse
    auto cache = AnimationCache::getInstance();
    cache->addAnimationsWithFile(ANIMATIONS_CACHE_PLIST);
    auto animation2 = cache->getAnimation("quan_1");
    auto *animate = Animate::create(animation2);
    foodtag->runAction(CCRepeatForever::create(animate));

    this->addChild(foodtag,1);
    GameLayer::refreshfood(0.1);
    this->addChild(food);

 	auto score = Sprite::createWithSpriteFrameName(GAMEVIEW_STAGE);
	score->setPosition(Point(deGameManager->getWinSize().width-180, deGameManager->getWinSize().height-40));
	score->setTag(TAG_GAMELAYER_STAGE);
    score->setVisible(true);
    this->addChild(score, 1);

    scorelabel = Label::createWithTTF("0",FONT_EN, 40);
    scorelabel->setPosition(Point(score->getPositionX()+60,score->getPositionY()));
    this->addChild(scorelabel);
    
    auto spriteNormal = Sprite::createWithSpriteFrameName(GAMEVIEW_HOME);
    auto spriteSelected = Sprite::createWithSpriteFrameName(GAMEVIEW_HOME_1);
  	auto home = MenuItemSprite::create(spriteNormal,spriteSelected,nullptr,CC_CALLBACK_1(GameLayer::Steering, this));
    home->setScale(0.6);
	home->setPosition(Point(60, deGameManager->getWinSize().height-40));
	home->setTag(TAG_GAMELAYER_HOME);
 

    //网格
    DrawNode* drawnode = DrawNode::create();
    addChild(drawnode, 0);
    for (int i = 0; i<gridcount+1; i++)
    {
        drawnode->drawSegment(Point(startX+0,i*gridwidth+startY), Point(startX+gridcount*gridwidth,i*gridwidth+startY),linewidth,Color4F(192,192,192,0.1f));
        drawnode->drawSegment(Point(startX+i*gridwidth,0+startY), Point(startX+i*gridwidth,gridcount*gridwidth+startY),linewidth,Color4F(192,192,192,0.1f));
    }
    
    auto spriteNormal_l = Sprite::createWithSpriteFrameName(DIRECTION);
    auto spriteSelected_l = Sprite::createWithSpriteFrameName(DIRECTION_1);
    auto LeftItem = MenuItemSprite::create(spriteNormal_l,spriteSelected_l,nullptr,CC_CALLBACK_1(GameLayer::Steering, this));
	LeftItem->setPosition(deGameManager->getWinSize().width/2-180 ,100);
    LeftItem->setTag(TAG_GAMELAYER_BT_LEFT);
    LeftItem->setScale(0.7);
    ActionInterval * LeftItem_RT = CCRotateTo::create(0.5, 180);
    LeftItem->runAction(LeftItem_RT);

    auto spriteNormal_r = Sprite::createWithSpriteFrameName(DIRECTION);
    auto spriteSelected_r = Sprite::createWithSpriteFrameName(DIRECTION_1);
    auto RightItem = MenuItemSprite::create(spriteNormal_r,spriteSelected_r,nullptr,CC_CALLBACK_1(GameLayer::Steering, this));
	RightItem->setPosition(deGameManager->getWinSize().width/2+180 ,100);
    RightItem->setTag(TAG_GAMELAYER_BT_RIGHT);
    RightItem->setScale(0.7);
    //RightItem->setEnabled(false);
    ActionInterval * RightItem_RT = CCRotateTo::create(0.5, 0);
    RightItem->runAction(RightItem_RT);
 
    auto menu = Menu::create(home,LeftItem,RightItem,NULL);
    menu->setPosition(Point::ZERO);
    this->addChild(menu, 0);
    
    auto bg = Sprite::createWithSpriteFrameName(GAMEVIEW_BG);
    bg->setAnchorPoint(Point::ZERO);
    bg->setPosition(Point::ZERO);
    this->addChild(bg, -2);

    this->schedule(schedule_selector(GameLayer::update), 0.16);
    
    ParticleSnow * ccps = ParticleSnow::create();
    ccps->setSpeed(200.0f);
    ccps->setAutoRemoveOnFinish(true);
    this->addChild(ccps);

    return true;
}
void GameLayer::BnFunc(float f)
{
    
}
void GameLayer::Steering(Ref* pSender)
{
    deGameManager->playAudioSound(SOUND_BT);
    auto mii = (MenuItemLabel*) pSender;
    if(TAG_GAMELAYER_HOME==mii->getTag())
    {
        deGameManager->changeGameState(EGameState::RANDING);
        return;
    }
    switch (head->dir)
	{
        case Direction::NONE:
        {
            if(mii->getTag()==TAG_GAMELAYER_BT_RIGHT)
            {
                head->dir = Direction::RIGHT;
            }
            else
            {
                head->dir = Direction::LEFT;
            }
            break;
        }
        case Direction::UP:
        {
            if(mii->getTag()==TAG_GAMELAYER_BT_RIGHT)
            {
                head->dir = Direction::RIGHT;
            }
            else
            {
                head->dir = Direction::LEFT;
            }
            break;
        }
        case Direction::DOWN:
        {
            if(mii->getTag()==TAG_GAMELAYER_BT_RIGHT)
            {
                head->dir = Direction::LEFT;
            }
            else
            {
                head->dir = Direction::RIGHT;
            }
            break;
        }
        case Direction::LEFT:
        {
            if(mii->getTag()==TAG_GAMELAYER_BT_RIGHT)
            {
                head->dir = Direction::UP;
            }
            else
            {
                head->dir = Direction::DOWN;
            }
            break;
        }
        case Direction::RIGHT:
        {
            if(mii->getTag()==TAG_GAMELAYER_BT_RIGHT)
            {
                head->dir = Direction::DOWN;
            }
            else
            {
                head->dir = Direction::UP;
            }
            break;
        }
        default:
            break;
    }
}


void GameLayer::update(float dt)
{
    if(head->dir==Direction::NONE) return;

    //蛇身每一段跟随前一段移动
    for(int i = body.size() -1 ;i>=0;i--)
    {
        SnakeNode* sn = body.at(i);
        if(i!=0)
        {
            SnakeNode* pre = body.at(i-1);
            sn->dir = pre->dir;
            sn->setCol(pre->col);
            sn->setRow(pre->row);
        }
        else
        {
            sn->dir = head->dir;
            sn->setCol(head->col);
            sn->setRow(head->row);
        }
    }
    
    //根据方向来让蛇头移动
    switch (head->dir)
    {
        case UP:
            if(head->row+1 >=gridcount)GameLayer::Over();else head->setRow(head->row+1);
            break;
        case DOWN:
            if(head->row-1 <0)GameLayer::Over(); else head->setRow(head->row-1);
            break;
        case RIGHT:
            if(head->col+1>=gridcount) GameLayer::Over(); else head->setCol(head->col+1);
            break;
        case LEFT:
            if(head->col-1 < 0) GameLayer::Over();else head->setCol(head->col-1);
            break;
        default:
            break;
    }

    
    //蛇身是否碰到蛇头
    for(int i = body.size() -1 ;i>=0;i--)
    {
        SnakeNode* sn = body.at(i);
        if(head->row == sn->row && head->col == sn->col)
        {
            GameLayer::Over();
            break;
        }
    }
    
    
    //碰撞检测
    if(head->row == food->row && head->col == food->col)
    {
        ParticleSystemQuad* mSystem = CCParticleSystemQuad::create(PARTICLE_PLIST_1);
        mSystem->setBlendAdditive(true);
        mSystem->setPosition(food->sprite->getPosition());// + 60
        mSystem->setAnchorPoint(Point::ZERO);
        mSystem->setScale(1.5);
        mSystem->setStartSize(30.0);
        this->addChild(mSystem);
        mSystem->setAutoRemoveOnFinish(true);
        
        
        score++;
        scorelabel->setString(String::createWithFormat("%d",score)->getCString());
        deGameManager->playAudioSound(SOUND_EAT);
        //刷新蛋
        SnakeNode* sn1 = head;
        GameLayer::refreshfood(0.1f);

        SnakeNode* sn = SnakeNode::createbody(head->row ,head->col);
        sn->dir = head->dir;
        switch (sn->dir)
        {
            case UP:
                if(sn->row+1 >=gridcount)GameLayer::Over();else sn->setRow(head->row+1);
                break;
            case DOWN:
                if(sn->row-1 <0)GameLayer::Over(); else sn->setRow(head->row-1);
                break;
            case RIGHT:
                if(sn->col+1>=gridcount) GameLayer::Over(); else sn->setCol(head->col+1);
                break;
            case LEFT:
                if(sn->col-1 < 0) GameLayer::Over();else sn->setCol(head->col-1);
                break;
            default:
                break;
        }
        head = sn;
        this->addChild(sn,1);
        body.insert(0, sn1);
    }
}
void GameLayer::refreshfood(float f)
{
    /*
    for(int i=gridcount-1; i>=0;i--)
    {
        for(int y=0; y<gridcount;y++)
        {
            grid[i][y] = 0;
        }
    }
     */
    memset(GameLayer::grid,0,sizeof(int)*GameLayer::gridcount*GameLayer::gridcount);
    
    for(int i = body.size() -1 ;i>=0;i--)
    {
        SnakeNode* sn = body.at(i);
        grid[sn->row][sn->col] = 1;
    }
    grid[food->row][food->col] = 1;
    grid[head->row][head->col] = 1;
    grid[0][GameLayer::gridcount-1] = 1;
    grid[0][0] = 1;
    grid[GameLayer::gridcount-1][GameLayer::gridcount-1] = 1;
    grid[GameLayer::gridcount-1][0] = 1;

    for(int i=gridcount-1; i>=0;i--)
    {
        for(int y=0; y<gridcount;y++)
        {
            printf(" %d ",grid[i][y]);
        }
        printf(" \r\n");
    }
    printf(" \r\n\r\n");
    
    
    long bodysize = body.size();
    long rand      = (gridcount*gridcount-bodysize-2);
    rand          = (rand<0)?0:rand;
    rand          = arc4random()% rand;
 
    int st = 0;
    for(int i=0; i<gridcount;i++)
    {
        for(int y=0; y<gridcount;y++)
        {
            if(grid[i][y]==1)
            {
                continue;
            }
            if(st==rand)
            {
                food->row = i;
                food->col = y;
                food->rePos(false);
                
                if(foodtag!=NULL)
                foodtag->setPosition(Point(food->sprite->getPositionX()+5,food->sprite->getPositionY()+6));
                return;
            }
            st++;
        }
    }
}
bool GameLayer::Over()
{
    if(deGameManager->getgameflag()==1)
    {
        deGameManager->setgameflag(0);
        deGameManager->playAudioSound(SOUND_GAMEOVER);
        unschedule(schedule_selector(GameLayer::update));
        deGameManager->gameOver(score, "", 0, CCString::createWithFormat("     基础分值:%d\r\n     属于一般水平！",score)->getCString());
    }
    return true;
}


