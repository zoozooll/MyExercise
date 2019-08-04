#ifndef  _GAMECONFIG_H_
#define  _GAMECONFIG_H_

using namespace std;

/*
 本游戏是基于珊瑚SDK开发，http://dev.friendou.com
 QQ：240795663 技术交流群：282732191 欢迎新手一起学习；
 */



static const string WELCOME_BG                          = "welcome_bg.png";
static const string WELCOME_LOGO                        = "welcome_logo.png";
static const string WELCOME_BT_QQ_NORALIMAGE            = "welcome_qqbution.png";
static const string WELCOME_BT_QQ_SLECTEDIMAGE          = "welcome_qqbution.png";
static const string MENU_BG                             = "menu.png";
static const string GAMEVIEW_BG                         = "menu.png";

static const string GAMEVIEW_SNAKE_BODY                 = "snake_body.png";
static const string GAMEVIEW_SNAKE_FOOD                 = "snake_food.png";
static const string GAMEVIEW_SNAKE_HEAD                 = "snake_head.png";

static const string GAMEVIEW_SNAKE_BODY_1                 = "snake_body_1.png";
static const string GAMEVIEW_SNAKE_BODY_2                 = "snake_body_2.png";
static const string GAMEVIEW_SNAKE_BODY_3                 = "snake_body_3.png";
static const string GAMEVIEW_SNAKE_BODY_4                 = "snake_body_4.png";
static const string GAMEVIEW_SNAKE_BODY_5                 = "snake_body_5.png";




static const string DIRECTION                       = "direction.png";
static const string DIRECTION_1                     = "direction_1.png";
static const string GAMEVIEW_STAGE                  = "stage.png";
static const string GAMEVIEW_HOME                   = "home.png";
static const string GAMEVIEW_HOME_1                 = "home_1.png";








#if(CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID)
//platform android
static const char* SOUND_BT                             = "sound/click.mp3";
static const char* SOUND_BG                             = "sound/bg.mp3";
static const char* SOUND_EAT                            = "sound/eat.wav";
static const char* SOUND_GAMEOVER                       = "sound/gameover.wav";
static const string FONT_EN                             = "fonts/MarkerFelt.ttf";
static const string PARTICLE_PLIST_1                = "Particle/1.plist";
static const string PARTICLE_PLIST_IMG_1            = "Particle/1.png";
static const string SPRITE_CACHE_PLIST              = "sprite/sprite.plist";
static const string SPRITE_CACHE                    = "sprite/sprite.png";


static const string GAMEVIEW_QUAN_1                 = "sprite/quan_1.png";
static const string GAMEVIEW_QUAN_2                 = "sprite/quan_2.png";
static const string GAMEVIEW_QUAN_3                 = "sprite/quan_3.png";
static const string GAMEVIEW_QUAN_4                 = "sprite/quan_4.png";


static const string ANIMATIONS_CACHE_PLIST          = "sprite/animations.plist";

//platform iso
#elif (CC_TARGET_PLATFORM ==CC_PLATFORM_IOS)
static const char* SOUND_BT                             = "click.mp3";
static const char* SOUND_BG                             = "bg.mp3";
static const char* SOUND_EAT                            = "eat.wav";
static const char* SOUND_GAMEOVER                       = "gameover.wav";
static const string FONT_EN                             = "MarkerFelt.ttf";
static const string PARTICLE_PLIST_1                = "1.plist";
static const string PARTICLE_PLIST_IMG_1            = "1.png";
static const string SPRITE_CACHE_PLIST              = "sprite.plist";
static const string SPRITE_CACHE                    = "sprite.png";

static const string GAMEVIEW_QUAN_1                 = "quan_1.png";
static const string GAMEVIEW_QUAN_2                 = "quan_2.png";
static const string GAMEVIEW_QUAN_3                 = "quan_3.png";
static const string GAMEVIEW_QUAN_4                 = "quan_4.png";


static const string ANIMATIONS_CACHE_PLIST          = "animations.plist";
#else
//platform other
;
#endif





static const int TAG_WELCOME_BT          = 1000;
static const int TAG_WELCOME_BT_QQ       = 1001;
static const int TAG_WELCOME_LOADING     = 1002;
static const int TAG_WELCOME_BT_GUEST    = 1003;
 
static const int TAG_GAMELAYER_BT_UP     = 2000;
static const int TAG_GAMELAYER_BT_LEFT   = 2001;
static const int TAG_GAMELAYER_BT_RIGHT  = 2002;
static const int TAG_GAMELAYER_BT_DOWN   = 2003;
static const int TAG_GAMELAYER_STAGE     = 2004;
static const int TAG_GAMELAYER_HOME     = 2005;



enum class EGameState
{
    NONE,
    LOGINERROR,
    LOGINSUCCESS,
    RANDING,
	PLAYING,
    PAUSE,
    RESUME,
	GAMEOVER,
    LOGINOUT
};

enum class EGameScene
{
    WELCOME,
	MENU,
    GAMELAYER,
	GAMEVIEW
};

#endif
