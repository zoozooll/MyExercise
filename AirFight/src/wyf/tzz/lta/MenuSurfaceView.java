package wyf.tzz.lta;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MenuSurfaceView extends SurfaceView 
implements SurfaceHolder.Callback  //ʵ���������ڻص��ӿ�
{
	GL_Demo activity;
	Paint paint;					//����	
	Bitmap[] menu=new Bitmap[5];	//�˵���ͼƬ����
	Bitmap bg;						//����ͼƬ
	
	int currentIndex=2;				//��ǰѡ�еĲ˵����	
	float mPreviousX;				//�ϴδ��ص�X����
	float mPreviousY;				//�ϴδ��ص�Y����	
	float changePercent=0;			//�������еİٷֱ�
	int anmiState=0;				//0-û�ж���  1-������  2-������
	
	int currentSelectWidth;			//��ǰ�˵�����
	int currentSelectHeight;		//��ǰ�˵���߶�
	float currentSelectX;			//��ǰ�˵���Xλ��
	float currentSelectY;			//��ǰ�˵���Yλ��	
			
	float leftWidth;				//���ڵ�ǰ�˵������˵���Ŀ��		
	float leftHeight;				//���ڵ�ǰ�˵������˵���ĸ߶�	
	float tempxLeft;				//���ڵ�ǰ�˵������˵����X����
	float tempyLeft;				//���ڵ�ǰ�˵������˵����Y����	
	
	float rightWidth;				//���ڵ�ǰ�˵����Ҳ�˵���Ŀ��	
	float rightHeight;				//���ڵ�ǰ�˵����Ҳ�˵���ĸ߶�	
	float tempxRight;				//���ڵ�ǰ�˵����Ҳ�˵����X����
	float tempyRight;				//���ڵ�ǰ�˵����Ҳ�˵����Y����	
	
	final int ABOUT_VIEW=0;			//����
	final int OPTION_VIEW=1;		//����
	final int KSMS_VIEW=2;			//����ģʽ
	final int HELP_VIEW=3;			//����
	final int EXIT_VIEW=4;			//�˳�
	
	//���±䳣�����ڲ˵���������
	static int screenWidth=480;		//��Ļ���
	static int screenHeight=320;	//��Ļ�߶�
	static int bigWidth=130;		//ѡ�в˵���Ŀ��
	static int bigHeight=130;		//ѡ�в˵���ĸ߶�
	static int smallWidth=80;		//δѡ�в˵���Ŀ��
	static int smallHeight=(int)(((float)smallWidth/bigWidth)*bigHeight);//δѡ�в˵���ĸ߶�
    
	static int selectX=screenWidth/2-bigWidth/2;//ѡ�в˵����������Ļ�ϵ�Xλ��
	static int selectY=screenHeight/2-80;		//ѡ�в˵����ϲ�����Ļ�ϵ�Yλ��
	static int span=10;							//�˵���֮��ļ��
	static int slideSpan=30;					//������ֵ
	
	static int totalSteps=10;					//�������ܲ���
	static float percentStep=1.0f/totalSteps;	//ÿһ���Ķ����ٷֱ�
	static int timeSpan=20;						//ÿһ�������ļ��ʱ��
	
	public MenuSurfaceView(GL_Demo activity) {
		super(activity);
		this.activity = activity;
		this.getHolder().addCallback(this);		//�����������ڻص��ӿڵ�ʵ����
		paint = new Paint();					//��������
		paint.setAntiAlias(true);				//�򿪿����
		initBitmap();							//��ʼ��ͼƬ
		init();			//��ʼ����ǰ�����������ҵĲ˵����λ�ô�С����
	}
	
	public void initBitmap(){
		//��ʼ��ͼƬ
		menu[0]=BitmapFactory.decodeResource(activity.getResources(), R.drawable.menu1);
		menu[1]=BitmapFactory.decodeResource(activity.getResources(), R.drawable.menu2);
		menu[2]=BitmapFactory.decodeResource(activity.getResources(), R.drawable.menu3);
		menu[3]=BitmapFactory.decodeResource(activity.getResources(), R.drawable.menu4);
		menu[4]=BitmapFactory.decodeResource(activity.getResources(), R.drawable.menu5);
		bg=BitmapFactory.decodeResource(activity.getResources(), R.drawable.background2);
	}
	
	public void init()						//��ʼ����ǰ�����������ҵĲ˵����λ�ô�С����
	{
		currentSelectWidth=bigWidth;		//��ǰѡ�в˵����
		currentSelectHeight=bigHeight;		//��ǰѡ�в˵��߶�
		currentSelectX=selectX;				//��ǰѡ�в˵�Xλ��
		currentSelectY=selectY;				//��ǰѡ�в˵�Yλ��	
		rightWidth=smallWidth;				//�����Ҳ�Ŀ��		
		leftWidth=smallWidth;				//�������Ŀ��		
		leftHeight=smallHeight;				//�������ĸ߶�	
		rightHeight=smallHeight;			//�����Ҳ�ĸ߶�
		tempxLeft=currentSelectX-(span+leftWidth);					//��������X
		tempyLeft=currentSelectY+(currentSelectHeight-leftHeight);	//��������Y����	
		tempxRight=currentSelectX+(span+currentSelectWidth);		//�����Ҳ��X	
		tempyRight=currentSelectY+(currentSelectHeight-rightHeight);//�����Ҳ��Y����
	}
	
	

	public void onDraw(Canvas canvas){
		canvas.drawBitmap(bg, 0, 0, paint);//���Ʊ���
		Bitmap selectBM=menu[currentIndex];//��ȡ��ǰ�˵���ͼƬ
		//���ݲ�����������ڻ��Ƶ�ǰ�˵����ͼƬ
		selectBM=Bitmap.createScaledBitmap(
				selectBM, 
				currentSelectWidth, 
				currentSelectHeight, 
				false
		);
			
		//���Ƶ�ǰ�Ĳ˵���
		canvas.drawBitmap(selectBM, currentSelectX, currentSelectY, paint);
		//����ǰ�˵���ǵ�һ������ƽ��ڵ�ǰ�˵������Ĳ˵���
		if(currentIndex>0){	
			//���ų�������ͼƬ
			Bitmap left=Bitmap.createScaledBitmap
			(
					menu[currentIndex-1], 
					(int)leftWidth, 
					(int)leftHeight, 
					false
			);		
			//����ͼƬ
			canvas.drawBitmap(left, tempxLeft, tempyLeft, paint);
		}			
		
		//����ǰ�˵�������һ������ƽ��ڵ�ǰ�˵����Ҳ�Ĳ˵���
		if(currentIndex<menu.length-1)
		{
			Bitmap right=Bitmap.createScaledBitmap	//���ų�������ͼƬ
			(
					menu[currentIndex+1], 
					(int)rightWidth, 
					(int)rightHeight, 
					false
			);	
			canvas.drawBitmap(right, tempxRight, tempyRight, paint);//����ͼƬ
		}
		
		//�����������δѡ�еĲ˵�
		for(int i=currentIndex-2;i>=0;i--){	
			float tempx=tempxLeft-(span+smallWidth)*(currentIndex-1-i);//����Xֵ
			if(tempx<-smallWidth){								//�����Ƴ���������Ļ�����û�����
				break;
			}
			int tempy=selectY+(bigHeight-smallHeight);			//����Yֵ
			Bitmap tempbm=Bitmap.createScaledBitmap				//���ų�������ͼƬ
			(
					menu[i], 
					smallWidth, 
					smallHeight, 
					false
			);
			canvas.drawBitmap(tempbm, tempx, tempy, paint);	//����ͼƬ
		}
		
		for(int i=currentIndex+2;i<menu.length;i++)			//���һ�������δѡ�еĲ˵�
		{	
			//����Xֵ
            float tempx=tempxRight+rightWidth+span+(span+smallWidth)*(i-(currentIndex+1)-1);			
			if(tempx>screenWidth)
			{//�����Ƴ���������Ļ�����û�����
				break;
			}			
			
			int tempy=selectY+(bigHeight-smallHeight);		//����Yֵ	
			Bitmap tempbm=Bitmap.createScaledBitmap			//���ų�������ͼƬ
			(
					menu[i], 
					smallWidth, 
					smallHeight, 
					false
			);	
			//����ͼƬ
			canvas.drawBitmap(tempbm, tempx, tempy, paint);		
		}
	}
	
	//�ػ����ķ���
	public void repaint()
	{
		SurfaceHolder holder=this.getHolder();		//SurfaceHolder
		Canvas canvas = holder.lockCanvas();		//��ȡ����
		try{
			synchronized(holder){
				onDraw(canvas);						//����
			}			
		}
		catch(Exception e){e.printStackTrace();}
		finally{
			if(canvas != null){
				holder.unlockCanvasAndPost(canvas);
			}
		}
	}

	//�����¼��ص�����
    @Override 
    public boolean onTouchEvent(MotionEvent e) {
    	
    	if(anmiState!=0)
    	{//�������������򴥿���Ч
    		return true;
    	}
    	//��ȡ��ǰ���ص��XY����
        float x = e.getX();
        float y = e.getY();
        
        //���ݴ��صĲ�ͬ����ִ�в�ͬ��ҵ���߼�
        switch (e.getAction())  {
        	case MotionEvent.ACTION_MOVE:
        		break;
        	case MotionEvent.ACTION_DOWN:
        	  //������Ϊ���´��ر����¼XYλ��
        	  mPreviousX=x;//��¼���ر�Xλ��
        	  mPreviousY=y;//��¼���ر�Yλ��
            break;
            case MotionEvent.ACTION_UP:
              //������Ϊ̧�������Xλ�ƵĲ�ִͬ���󻬡��һ���ѡ�в˵����ҵ���߼�	
              float dx=x- mPreviousX; //����Xλ��
              if(dx<-slideSpan)
              {//��Xλ��С����ֵ�����󻬶�
            	  if(currentIndex<menu.length-1)
            	  {//����ǰ�˵�������һ���˵��������󻬶�
            		  //���㻬����ɺ�ĵ�ǰ�˵�����
            		  int afterCurrentIndex=currentIndex+1;
            		  //����״ֵ̬����Ϊ2-������
            		  anmiState=2;
            		  //�����̲߳��Ŷ���������״ֵ̬
            		  new MenuAnmiThread(this,afterCurrentIndex).start();
            	  } 
              }
              else if(dx>slideSpan)
              {//��Xλ�ƴ�����ֵ�����һ���
            	  if(currentIndex>0)
            	  {//����ǰ�˵���ǵ�һ���˵��������󻬶�
            		  //���㻬����ɺ�ĵ�ǰ�˵�����
            		  int afterCurrentIndex=currentIndex-1;
            		  //����״ֵ̬����Ϊ2-������
            		  anmiState=1;
            		  //�����̲߳��Ŷ���������״ֵ̬
            		  new MenuAnmiThread(this,afterCurrentIndex).start();
            	  }            	  
              }
              else
              {
            	  if(			//��Xλ������ֵ�����ж��з�ѡ��ĳ�˵���
                     mPreviousX>selectX&&mPreviousX<selectX+bigWidth&&
                     mPreviousY>selectY&&mPreviousY<selectY+menu[currentIndex].getHeight()&&
                     x>selectX&&x<selectX+bigWidth&&
                     y>selectY&&y<selectY+menu[currentIndex].getHeight()
            	  )
            	  {
            		  switch(currentIndex)
            		  {
            		  case KSMS_VIEW:
        				  activity.setLoadView();//���ؽ���
        				  break;
        			   case OPTION_VIEW:
        				   activity.setSoundView();//���ý���
        				   break;
        			   case ABOUT_VIEW:
        				   activity.setAboutView();//���ڽ���
        				   break;
        			   case HELP_VIEW:
        				   activity.setHelpView();//��������
        				   break;
        			   case EXIT_VIEW:
        				   System.exit(0);//�˳�����
        				   break;
            		  }
            	  }
              }                           
            break;            
        }   
        return true;        
    }
	
	public void surfaceCreated(SurfaceHolder holder) {//����ʱ������
		repaint();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {}	//����ʱ������
	
}