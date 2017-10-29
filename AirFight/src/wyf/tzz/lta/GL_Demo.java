package wyf.tzz.lta;

import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;

public class GL_Demo extends Activity {
	SoundSurfaceView soundsv;		//������������
	MenuSurfaceView menusv;			//��ʼ�˵�����������
	LoadSurfaceView loadsv;			//���ؽ�������
	HelpSurfaceView helpsv;			//������������
	AboutSurfaceView aboutsv;		//���ڽ�������
	MySurfaceView msv;				//��Ϸ��������
	WinSurfaceView winsv;			//ʤ����������
	FailSurfaceView failsv;			//ʧ�ܽ�������	
	static final int START_GAME=0;	//���ز���ʼ��Ϸ��Message���
	Handler hd;						//��Ϣ������
	MediaPlayer mpBack;				//��Ϸ�������ֲ�����	
	SoundPool soundPool;			//������
	HashMap<Integer, Integer> soundPoolMap; //������������ID���Զ�������ID��Map
	boolean isSound=false;			//�Ƿ���������
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //����Ϊ����
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        requestWindowFeature(Window.FEATURE_NO_TITLE); 		//����Ϊȫ��
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); 
        initSound();								//���ظ�������
        setSoundView();										//�л���������������
//        setMySurfaceView();								//�л���MySurfaceView
    }
    
    @Override
    protected void onResume() {
        super.onResume();		//���ø��෽��
        if(msv!=null) {
        	msv.onResume();		//��Ϸ����ָ�
        	if(isSound){
	        	mpBack.start();	//���ű�������
        	}
        }
    }
    
    @Override
    protected void onPause() {
        super.onPause();		//���ø��෽��
        if(msv!=null) {
        	msv.onPause();		//��Ϸ������ͣ
        }
        if(mpBack!=null){
			mpBack.pause();		//����������ͣ
		}
    }  
    
    public void setMySurfaceView()
    {
    	msv = new MySurfaceView(this);		//ʵ��������������
    	msv.requestFocus();					//��ȡ����
        msv.setFocusableInTouchMode(true);	//����Ϊ�ɴ���
    	setContentView(msv);				//�л���������Ϸ����
    }
    
    public void setSoundView(){
    	soundsv = new SoundSurfaceView(this);		//ʵ����������������
    	soundsv.requestFocus();						//��ȡ����
    	soundsv.setFocusableInTouchMode(true);		//����Ϊ�ɴ���
    	setContentView(soundsv);					//�л��������������ý���    	
    }
    
    public void setMenuView(){
    	menusv = new MenuSurfaceView(this);//ʵ������ʼ�˵���������       
    	menusv.requestFocus();				//��ȡ����
    	menusv.setFocusableInTouchMode(true);//����Ϊ�ɴ���
    	setContentView(menusv);				//�л���������ʼ�˵�����
    }
    
    public void setLoadView(){
    	loadsv=new LoadSurfaceView(this);	//ʵ�������ؽ�������
    	setContentView(loadsv);   			//��ʾ��Ϸ���ؽ���
    	hd=new Handler(){					//��ʼ����Ϣ������
        	@Override
        	public void handleMessage(Message msg){
        		super.handleMessage(msg);
        		switch(msg.what){
        		   case START_GAME:      
        			   setMySurfaceView();
        		   break; 
        		}
        	}
        };
		new Thread(){
			@Override
			public void run(){
				try {Thread.sleep(1500);		//˯��1500ms
				}catch (InterruptedException e) {e.printStackTrace();}   
				hd.sendEmptyMessage(START_GAME);//����Ϣ������Ϸ
			}
		}.start();								//�����߳�
    }
   
    public void setAboutView(){
    	aboutsv=new AboutSurfaceView(this);		//ʵ�������ڽ�������
    	aboutsv.requestFocus();					//��ȡ����
    	aboutsv.setFocusableInTouchMode(true);//����Ϊ�ɴ���
    	setContentView(aboutsv);				//�л����������ڽ���
    }
    
    public void setHelpView(){
    	helpsv=new HelpSurfaceView(this);		//ʵ����������������
    	helpsv.requestFocus();					//��ȡ����
    	helpsv.setFocusableInTouchMode(true);	//����Ϊ�ɴ���
    	setContentView(helpsv);					//�л���������������
    }
    
    public void setWinView(){
    	 winsv=new WinSurfaceView(this);		//ʵ����ʤ����������
         winsv.requestFocus();					//��ȡ����
         winsv.setFocusableInTouchMode(true);	//����Ϊ�ɴ���
         setContentView(winsv);					//�л�������ʤ������
    }
    public void setFailView() {
    	  failsv=new FailSurfaceView(this);		//ʵ����ʧ�ܽ�������
    	  failsv.requestFocus();				//��ȡ����
          failsv.setFocusableInTouchMode(true); //����Ϊ�ɴ���
          setContentView(failsv);				//�л�������ʧ�ܽ���
    }
        
    public void initSound(){ 
    	mpBack = MediaPlayer.create(this, R.raw.background);		//������������
    	mpBack.setLooping(true); 									//��������ѭ������
		soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);//��ʼ��������
	    soundPoolMap = new HashMap<Integer, Integer>();   			//������Ϸ��Ч
	    soundPoolMap.put(1, soundPool.load(this, R.raw.explode, 1));//��ը��Ч
	    soundPoolMap.put(2, soundPool.load(this, R.raw.heromissile, 1)); //hero�������ڵ���Ч
	    soundPoolMap.put(3, soundPool.load(this, R.raw.enemymissile, 1));//�л������ڵ���Ч	 
    }
    
    public void playSoundPool(int sound, int loop) {
    	if(!isSound){			//�������Ϊû�д��������򷵻أ�����������
    		return;
    	}
	    AudioManager mgr = (AudioManager)this.getSystemService(Context.AUDIO_SERVICE);  //�õ����ֹ�����
	    float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);   	//�õ���ǰ����
	    float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);     	//�õ��������
	    float volume = streamVolumeCurrent / streamVolumeMax;  							//��ǰ����
	    soundPool.play(soundPoolMap.get(sound), volume, volume, 1, loop, 1f);			//�����������������
	}
}