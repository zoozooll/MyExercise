#include "AppDelegate.h"
#include "GameManager.h"
#include "CoralSdkApi/BnCallBack.h"
 
USING_NS_CC;

AppDelegate::AppDelegate() {
    
}

AppDelegate::~AppDelegate()
{
}

bool AppDelegate::applicationDidFinishLaunching() {
    // initialize director
    auto director = Director::getInstance();
    auto glview = director->getOpenGLView();
    if(!glview) {
        glview = GLView::create("My Game");
        director->setOpenGLView(glview);
    }
    glview->setDesignResolutionSize(640.0f, 1136.0f, ResolutionPolicy::FIXED_WIDTH);
    
    
    // turn on display FPS
    director->setDisplayStats(false);
    
    // set FPS. the default value is 1.0/60 if you don't call this
    director->setAnimationInterval(1.0 / 60);
    deGameManager;
    
    BnCallBack *pCallback = new BnCallBack();
    pCallback->SetSDKCallback(pCallback);
    deGameManager->start();
    
    
    return true;
}

// This function will be called when the app is inactive. When comes a phone call,it's be invoked too
void AppDelegate::applicationDidEnterBackground() {
    Director::getInstance()->stopAnimation();
    
    deGameManager->pauseAudioMusic();
}

// this function will be called when the app is active again
void AppDelegate::applicationWillEnterForeground() {
    Director::getInstance()->startAnimation();
    
    deGameManager->resumeAudioMusic();
}
