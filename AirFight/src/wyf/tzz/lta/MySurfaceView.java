package wyf.tzz.lta;

import java.io.IOException;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List; 

import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


import static wyf.tzz.lta.Constant.*;

class MySurfaceView extends GLSurfaceView {
	GL_Demo activity;				//Activity������
	boolean overFlag=false;			//�Ƿ�����ı�־
	boolean isWin=false;			//�Ƿ�ʤ���ı�־
	boolean isFail=false;			//�Ƿ�ʧ�ܵı�־
	SceneRenderer mRenderer;		//������Ⱦ��	
	
	Plane plane;					//hero������
	EnemyPlane ep ;					//���ܵл�
	EnemyPlaneGroup epg;			//�л���
	
	Column heroMissile;				//hero���ڵ�
	Column enemyMissile;			//�л��ڵ�
	List<Missile> heroMissileGroup  = new LinkedList<Missile>();		//hero���ڵ��б�
	List<Missile> enemyMissileGroup  = new LinkedList<Missile>();		//�л��ڵ��б�
	
	TextureRect lifeIcon;			//ͼ�����
    float cz=0f;					//�����z����
    float cy=2.2f;					//�����y����
    float cx=0f;					//�����x����

    int keyState=0;					//����״̬
    int life=LIVE_COUNT;			//����ֵ
    int delayCount=2;				//��ը�����ӳٱ���
    Score score;					//�÷��������
	
	KeyThread keyThread;			//���������߳�
	HeroPlaneMoveThread hpmt;		//hero���ƶ��߳�
	HeroMissileGoThread hmgt;		//hero���ڵ��ƶ��߳�
	EnemyMissileGoThread emgt;		//�л��ڵ��ƶ��߳�
	EnemyPlaneMoveThread epmt;		//�л��ƶ��߳�
    Handler hd;						//��Ϣ������        
    
	public MySurfaceView(GL_Demo demo) { 
        super(demo);
        this.activity=demo;        
        mRenderer = new SceneRenderer();						//����������Ⱦ��
        setRenderer(mRenderer);									//������Ⱦ��	
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);	//������ȾģʽΪ������Ⱦ       
        Constant.initConstant(this.getResources());				//��ʼ������߶�����
        
        hd=new Handler(){						//��ʼ����Ϣ������
        	@Override
        	public void handleMessage(Message msg){
        		super.handleMessage(msg);
        		switch(msg.what){
        		   case 0:     					//�ɹ���������ϰ���
	                   	activity.setWinView();  //�л���ʤ���Ļ���         
	                   	break; 
        		   case 1:						//�ɹ��������
        			   activity.setFailView();  //�л���ʧ�ܵĻ���    
        			   break;          			   
        		}
        	}
        };
        
        new Thread(){							//����һ���µ��̣߳������Ƿ�ʤ��
			@Override
			public void run()
			{
				try {Thread.sleep(2000);		//˯��2000ms
				} catch (InterruptedException e) {	e.printStackTrace();}  	
				
				while(true)	{
					if(isWin){					//���ʤ��
						over();					//�����������߳�
						try {Thread.sleep(400);} 
						catch (InterruptedException e) {e.printStackTrace();}  	
						hd.sendEmptyMessage(0);//����Ϣ
						break;
					}
					else if(isFail){			//�����Ϸʧ��
						over();					//�����������߳�
						try {Thread.sleep(400);	}
						catch (InterruptedException e) {e.printStackTrace();}  	
						hd.sendEmptyMessage(1);//����Ϣ
						break;
					}
					
					try {Thread.sleep(500);	}	//˯��
					catch (InterruptedException e) {e.printStackTrace();}  	
				}
			}
		}.start();								//�����߳�
    }

	@Override
	public boolean onKeyDown(int keyCode,KeyEvent event){	//Ϊ����������Ӽ���
		  switch(keyCode){  
			   case KeyEvent.KEYCODE_DPAD_UP:				//��������ϼ�	
				   keyState=keyState|0x1;					   
				   return true;
			   case KeyEvent.KEYCODE_DPAD_DOWN:				//��������¼�	
				   keyState=keyState|0x2;	
				   return true;
			   case KeyEvent.KEYCODE_DPAD_LEFT:				//����������	
				   keyState=keyState|0x4;	
				   return true;
			   case KeyEvent.KEYCODE_DPAD_RIGHT:			//��������Ҽ�	
				   keyState=keyState|0x8;	
				   return true;	
			   case KeyEvent.KEYCODE_DPAD_CENTER:			//��������м�	
			   case KeyEvent.KEYCODE_SPACE:					//������¿ո��	
					   keyState=keyState|0x10;		
				   return true;	
			   case KeyEvent.KEYCODE_BACK:					//������·��ؼ�	
				   over();
				   activity.setMenuView();					//�л������˵�����		
				   return true;
		  }
		  return false;										//���أ�������������ϵͳ����
	   }
	
	@Override
	public boolean onKeyUp(int keyCode,KeyEvent event){		//Ϊ����������Ӽ���
			  switch(keyCode)
			  {  
			  	   case KeyEvent.KEYCODE_DPAD_UP:			//����ϼ�����	
					   keyState=keyState&0x1E;
					   return true;
				   case KeyEvent.KEYCODE_DPAD_DOWN:			//����¼�����	
					   keyState=keyState&0x1D;
					   return true;
				   case KeyEvent.KEYCODE_DPAD_LEFT:			//����������		
					   keyState=keyState&0x1B;	
					   return true;
				   case KeyEvent.KEYCODE_DPAD_RIGHT:		//����Ҽ�����	
					   keyState=keyState&0x17;	
					   return true;	
				   case KeyEvent.KEYCODE_SPACE:				//����ո������	
				   case KeyEvent.KEYCODE_DPAD_CENTER:		//����н�����	
					     keyState=keyState&0xF;	
					     keyThread.fireCount=0;				//���ɻ�ÿ�η����ڵ���������Ϊ0
					   return true;	
			  }
			  return false;									//false��������������ϵͳ����
	   }
	
    class SceneRenderer implements GLSurfaceView.Renderer {	//����������Ⱦ����
		TextureRect trExplo[]=new TextureRect[6];			//��ը�����������
		int anmiIndex=0;									//��ը��������
		DrawCylinder land;					//½��Բ������
    	DrawCylinderSky sky;				//���Բ������    	
    	
		public int planeHeadId;				//��ͷ
		public int frontWingId;				//ǰ��������
		public int frontWing2Id;			//ǰ��������2
		public int bacckWingId;				//���������
		public int topWingId;				//�ϻ�������
		public int planeBodyId;				//��������
		public int planeCabinId;			//��������
		public int cylinder1Id;				//Բ������1
		public int cylinder2Id;				//Բ������2
		public int screw1Id;				//����������
		
		public int enemyPlaneFrontWingId;	//�л���־
		public int enemyPlaneFrontWing2Id;	//�л���־
		public int enemyPlaneBodyId;		//�л���������
		public int enemyPlaneTopWingId;		//�л���������id
		public int enemyPlaneHeadId;		//ǰ�л���ͷ����id
		public int screw2Id;//������
		
		public int landTextureId;					//������������ID
		public int skyTextureId;					//�����������ID
		
        public void onDrawFrame(GL10 gl) {
        	gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);//�����ɫ��������Ȼ���
            gl.glMatrixMode(GL10.GL_MODELVIEW);		//���õ�ǰ����Ϊģʽ����
            gl.glLoadIdentity();  					//���õ�ǰ����Ϊ��λ����
            GLU.gluLookAt( 
            		gl, 
            		cx,   							//���������x
            		cy, 							//���������Y
            		cz,  							//���������Z
            		0, 								//�۲�Ŀ�������  X
            		0,   							//�۲�Ŀ�������  Y
            		-5,   							//�۲�Ŀ�������  Z
            		0,
            		1, 								//���������
            		0
            );
            
            gl.glPushMatrix();						//�����任�����ֳ�  
            gl.glTranslatef(0, Y_TRASLATE, -30);	//ƽ��
            land.drawSelf(gl);						//���Ƶ���
            gl.glTranslatef(0, 0, 28);				//ƽ��
            sky.drawSelf(gl);						//�������
            gl.glPopMatrix();						//�ָ��任�����ֳ�
            
        	epg.drawSelf(gl);						//���Ƶл�
            
           	if(overFlag==false)	{					//���hero��������
            	plane.drawSelf(gl);            		//����hero��
            }  
            else  if(overFlag){
 		    	if(anmiIndex/delayCount<trExplo.length)		//����û�в����궯����֡	
 				{
 		    		gl.glDisable(GL10.GL_DEPTH_TEST); 		//�ر���Ȳ���
 		    		gl.glPushMatrix();						//���浱ǰ����
 		    		gl.glTranslatef(plane.x, plane.y, plane.z);	//ƽ��
 			    	gl.glEnable(GL10.GL_BLEND);				//�������
 			    	gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
 			    	trExplo[anmiIndex/delayCount].drawSelf(gl);//���Ʊ�ը������ǰ֡
 					gl.glDisable(GL10.GL_BLEND);			//�رջ��
 			    	gl.glPopMatrix();						//�ָ�֮ǰ���� 					
 					gl.glEnable(GL10.GL_DEPTH_TEST); 		//������Ȳ���
 					anmiIndex=anmiIndex+1;					//��һ֡
 				}
 		    	else										//�����������
 		    	{
 		    		anmiIndex=0;							//����ը����֡��Ϊ0	
 		    		life--;									//����ֵ��һ
 		    		if(life<=0){							//�������
 		    			isFail=true;						//��Ϸʧ��
 		    			over();								//���������߳�
 		    		}
 		    		else{
 		    			overFlag=false;						//��ʼ��һ��
 		    			keyThread.pauseFlag=false;			//�����������̵߳�pauseFlag��Ϊfalse
 		    			plane.x=0;							//����hero����x��Ϊ0
 		    			plane.y=0;							//����hero����y��Ϊ0
 		    		}
 		    	}
 	        }
           	
           	for(int i=0;i<heroMissileGroup.size();i++){		//����hero���ڵ��б�
           		heroMissileGroup.get(i).drawSelf(gl);		//����hero���ڵ�
           	}	
           	
           	for(int i=0;i<enemyMissileGroup.size();i++){	//�����л��ڵ��б�
           		enemyMissileGroup.get(i).drawSelf(gl);		//���Ƶл��ڵ�
           	}
           	
           	gl.glEnable(GL10.GL_BLEND);						//�������
           	gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);//���û������
           	gl.glDisable(GL10.GL_DEPTH_TEST); 				//�ر���Ȳ���  
           	gl.glLoadIdentity();							//���õ�ǰ����Ϊ��λ����
           	GLU.gluLookAt(gl, 0, 0, 0, 0, 0, -1, 0, 1, 0);	//����������Ĳ���
           	gl.glTranslatef(0,0,-NEAR);  					//ƽ�Ƶ������ǰ��
           
           	gl.glPushMatrix(); 								//������ǰ����	
           	gl.glTranslatef(0.8f,0.48f,0);   				//ƽ�Ƶ���Ļ���Ͻ�
            score.drawSelf(gl);								//���Ƶ÷�       
            gl.glPopMatrix();								//�ָ�֮ǰ�����ľ���	
            
        	gl.glPushMatrix(); 								//������ǰ����	
           	gl.glTranslatef(-1f,0.48f,0);       			//ƽ�Ƶ���Ļ���Ͻ�
           	for(int i=0;i<life;i++){
           		gl.glTranslatef(ICON_WIDTH*3/2, 0, 0);		//����ƽ��
           		lifeIcon.drawSelf(gl);						//���ƴ�������ֵ��icon  
            }	
            gl.glPopMatrix();								//�ָ�֮ǰ�����ľ���	
            gl.glDisable(GL10.GL_BLEND);					//�رջ��
            gl.glEnable(GL10.GL_DEPTH_TEST); 				//������Ȳ���
        }

        public void onSurfaceChanged(GL10 gl, int width, int height) {
        	gl.glViewport(0, 0, width, height); 					//�����Ӵ���С��λ�� 
            gl.glMatrixMode(GL10.GL_PROJECTION);					//���õ�ǰ����ΪͶӰ����
            gl.glLoadIdentity(); 									//���õ�ǰ����Ϊ��λ����
            float ratio = (float) width / height; 					//����͸��ͶӰ�ı�������߱� 
            gl.glFrustumf(-1, 1, -1/ratio, 1/ratio, NEAR, NEAR+50f); //����͸��ͶӰ�������
        }

        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        	gl.glDisable(GL10.GL_DITHER);							//�رտ����� 
            gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT,GL10.GL_FASTEST);//�����ض�Hint��Ŀ��ģʽ������Ϊ����Ϊʹ�ÿ���ģʽ
            gl.glClearColor(0,0,0,0);           					//������Ļ����ɫ��ɫRGBA 
            gl.glEnable(GL10.GL_DEPTH_TEST); 						//������Ȳ���
            gl.glShadeModel(GL10.GL_SMOOTH);					 	//����ƽ��
            gl.glEnable(GL10.GL_CULL_FACE);            				//�򿪱������
          
            landTextureId=initTexture(gl,R.drawable.grass);			//��ʼ��½������
            skyTextureId=initTexture(gl,R.drawable.skyball);		//��ʼ���������
            land=new DrawCylinder(LAND_L,LAND_R,ROWS,COLS,yArray,landTextureId);//��ʼ��½��Բ��
            sky=new DrawCylinderSky(SKY_L,SKY_R,18f,10,skyTextureId);			//��ճ�ʼ�����Բ��
	        
            int[] explodeImgId={R.drawable.explode1,				//��ը����ͼƬid
    				R.drawable.explode2,R.drawable.explode3,R.drawable.explode4,
    				R.drawable.explode5,R.drawable.explode6};
            int[] explodeTexId=new int[explodeImgId.length];		//��ը���������������
            for(int i=0;i<explodeTexId.length;i++)					//������ը�������֡
            {
            	explodeTexId[i]=initTexture(gl,explodeImgId[i]);  	//��ʼ����ըͼƬ����
            	trExplo[i]=new TextureRect(explodeTexId[i],EXPLODE_REC_LENGTH,EXPLODE_REC_WIDTH);//��ʼ����ը�����������
            }
            int heroMissileId=initTexture(gl,R.drawable.heromissile);//�ڵ�����id
            int numberTexId=initTexture(gl,R.drawable.number);		 //�÷�����id
            
            planeCabinId = initTexture(gl,R.drawable.planecabin);//��������            
            planeHeadId = initTexture(gl,R.drawable.planehead);	//hero��ͷ���� 
            frontWingId = initTexture(gl,R.drawable.frontwing);	//heroǰ��������
            frontWing2Id = initTexture(gl,R.drawable.frontwing2);//heroǰ�������� 2
            bacckWingId = initTexture(gl,R.drawable.planebody);	//hero���������
            topWingId = initTexture(gl,R.drawable.topwing);		
            planeBodyId = initTexture(gl,R.drawable.planebody); //hero��������
    		cylinder1Id= initTexture(gl,R.drawable.yz1);		//heroԲ��1
    		cylinder2Id = initTexture(gl,R.drawable.yz2);		//heroԲ��2
    		screw1Id = initTexture(gl,R.drawable.planecabin);	//hero����������id 
    		
    		enemyPlaneFrontWingId = initTexture(gl,R.drawable.ewing1);		//�л�ǰ��������
    		enemyPlaneFrontWing2Id = initTexture(gl,R.drawable.ewing2);		//�л�ǰ��������2
    		enemyPlaneBodyId = initTexture(gl,R.drawable.eplanebody); 		//�л���������
    		enemyPlaneTopWingId = initTexture(gl,R.drawable.etopwing);		
    		enemyPlaneHeadId =initTexture(gl,R.drawable.eplanehead);		//�л���ͷ���� 
    		screw2Id = initTexture(gl,R.drawable.etopwing);					//�л�����������id 
           
            ep=new EnemyPlane(MySurfaceView.this);				//��ʼ���л�
            plane=new Plane(MySurfaceView.this);				//��ʼ��hero��
            epg=new EnemyPlaneGroup(ep,trExplo);				//��ʼ���л���
           
            heroMissile =new Column(HERO_MISSILE_HEIGHT,HERO_MISSILE_RADIUS,heroMissileId);		//��ʼ��hero���ڵ�
            enemyMissile =new Column(ENEMY_MISSILE_HEIGHT,ENEMY_MISSILE_RADIUS,heroMissileId);	//��ʼ���л��ڵ�
            
            score=new Score(numberTexId);										//��ʼ���÷��������
            int iconId=initTexture(gl,R.drawable.icon);							//��ʼ��ͼ������id
            lifeIcon=new TextureRect(iconId,ICON_WIDTH*2/3,ICON_HEIGHT*2/3);	//��ʼ��ͼ���������
            
            keyThread=new KeyThread(MySurfaceView.this);						//��ʼ�����̼����߳�
            hpmt=new HeroPlaneMoveThread(MySurfaceView.this);					//��ʼ��hero�ɻ������߳�
            epmt=new EnemyPlaneMoveThread(MySurfaceView.this);					//��ʼ���л������߳�
            hmgt=new HeroMissileGoThread(MySurfaceView.this);					//��ʼ��hero�ڵ��߳�
            emgt=new EnemyMissileGoThread(MySurfaceView.this);					//��ʼ���л��ڵ��߳�
            
            keyThread.start();			//�������̼����߳�
            hpmt.start();				//����hero�ɻ������߳�
            epmt.start();				//�����л������߳�
            hmgt.start();				//����hero�ڵ��߳�
            emgt.start();				//�����л��ڵ��߳�            
            cz=plane.z+Z_DISTANCE_CAMERA_PLANE;		//�ı��������z����        
        }
    }
    
	public int initTexture(GL10 gl ,int drawableId){
		int[] textures=new int[1];								//�洢����ID
		gl.glGenTextures(1, textures, 0);						//��������ID
		int currTextureId=textures[0];
		gl.glBindTexture(GL10.GL_TEXTURE_2D,currTextureId);		//������ID
		InputStream is=getResources().openRawResource(drawableId);//��ʼ��ͼƬ
		
		//�����˲���ʽ    
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,GL10.GL_LINEAR_MIPMAP_NEAREST);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D,GL10.GL_TEXTURE_MAG_FILTER,GL10.GL_LINEAR_MIPMAP_LINEAR);
        ((GL11)gl).glTexParameterf(GL10.GL_TEXTURE_2D, GL11.GL_GENERATE_MIPMAP, GL10.GL_TRUE);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,GL10.GL_REPEAT);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,GL10.GL_REPEAT);

		Bitmap bitmapTmp;										//����λͼ
		try{
			bitmapTmp=BitmapFactory.decodeStream(is);			//ת��Ϊλͼ
		}
		finally{
			try{
				is.close();										//�ر���
			}catch(IOException e){e.printStackTrace();}
		}
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmapTmp, 0);//ת��Ϊλͼ
		bitmapTmp.recycle();									//�ͷ���Դ
		return currTextureId;									//����ͼƬ����ID
	}
	
	public void over(){
		keyThread.overFlag=true;			//���̼����̵߳�overFlag��־��Ϊtrue
		hpmt.overFlag=true;					//��hero�ɻ������̵߳�overFlag��־��Ϊtrue
		epmt.overFlag=true;					//���л������̵߳�overFlag��־��Ϊtrue
		hmgt.overFlag=true;					//��hero�ڵ��̵߳�overFlag��־��Ϊtrue
		emgt.overFlag=true;					//���л��ڵ��̵߳�overFlag��־��Ϊtrue 
		heroMissileGroup.clear();			//ɾ������hero�ڵ�
		enemyMissileGroup.clear();			//ɾ�����ел��ڵ�
	}
}
