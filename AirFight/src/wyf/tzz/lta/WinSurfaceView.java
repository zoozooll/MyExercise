package wyf.tzz.lta;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class WinSurfaceView extends SurfaceView
implements SurfaceHolder.Callback {
	GL_Demo myActivity;			//activity������
	int screenWidth = 480;		//��Ļ���
	int screenHeight = 320;		//��Ļ�߶�
	int picWidth=112;			//���ذ�ťͼƬ���
	int picHeight=40;			//���ذ�ťͼƬ�߶�
	Bitmap bgAbout;				//����ͼƬ
	Bitmap gameBack;			//���ذ�ťͼƬ
	
	public WinSurfaceView(GL_Demo myActivity) {		
		super(myActivity);						//���ø��෽��
		this.myActivity=myActivity;
		initBitmap();							//����ͼƬ
		this.getHolder().addCallback(this);		//���ûص�����
	} 
	
	public void initBitmap(){		//��ʼ��ͼƬ
		//���ر���ͼƬ
		bgAbout=BitmapFactory.decodeResource(getResources(), R.drawable.winbg);
		//���ط��ذ�ťͼƬ
		gameBack=BitmapFactory.decodeResource(getResources(), R.drawable.back);
	}
	
	
	@Override
	public void onDraw(Canvas canvas){			//����ͼ��
		canvas.drawBitmap(bgAbout, 0, 0,null);	//������ͼƬ
		//�����ذ�ťͼƬ
		canvas.drawBitmap(gameBack, (screenWidth-(1*picWidth)), screenHeight-2*picHeight,null);
	}
	
	//view�ı��ʱ�����
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {}
	
	

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Canvas canvas=null;				//����
		try{
			canvas=holder.lockCanvas();//��������
			synchronized(holder){
				onDraw(canvas);			//�ػ�
			}
		}catch(Exception e){e.printStackTrace();}
		finally{
			if(canvas!=null){
				holder.unlockCanvasAndPost(canvas);//���ƺ����
			}
		}
	}
	
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {}//view���ٵ�ʱ�����

	//�����¼��ص�����
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int x = (int) event.getX();					//����x����
		int y = (int) event.getY();					//����y����
		switch(event.getAction()){
			case MotionEvent.ACTION_DOWN:
				 if(x<(screenWidth-(1*picWidth))+picWidth&&x>(screenWidth-(1*picWidth))
		    	   &&y<screenHeight-2*picHeight+picHeight&&y>screenHeight-2*picHeight)
				 {									//�������ڷ��ذ�ťͼƬ��ʱ
					 myActivity.setMySurfaceView();	//�л������˵�����
				 }
				 break;
			}
		return super.onTouchEvent(event);			//�����������ϵͳ����
	}
	
	 @Override
	 public boolean onKeyDown(int keyCode,KeyEvent event)		//Ϊ������Ӽ���
	 { 
		  switch(keyCode)
		  {     
			   case KeyEvent.KEYCODE_BACK:						//������·��ؼ�
				   	myActivity.setMenuView();					//�л������˵�����
				   	return true;
		  }
		  return false;											//������������ϵͳ����
	 }
}

