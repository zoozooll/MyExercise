package wyf.tzz.lta; 

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

public class Constant {										//������
	public static final float UNIT_SIZE=1f;					//��λ��С
	public static final float NEAR=2;						//ͶӰ����near����
	public static final float Z_DISTANCE_CAMERA_PLANE=NEAR+4;//�������ɻ�z�����ϵľ���
	public static final float X_MOVE_SPAN=0.1f;				//�ɻ���x����ÿ���ƶ�����
	public static final float Y_MOVE_SPAN=0.1f;				//�ɻ���y����ÿ���ƶ�����
	public static final float Z_MOVE_SPAN=0.1f;				//�ɻ���z����ÿ���ƶ���
	
	public static final int GOAL_COUNT=10;					//ʤ���ĵ÷�����
	public static final int LIVE_COUNT=3;					//�ɻ���ʼʱ����ֵ
	
	public static final float PLANE_SIZE=1.5f;				//hero���ĳߴ�
	public static final float ENEMYPLANE_SIZE=1.5f;			//�л��ĳߴ�
	public static final float HERO_MISSILE_SPEED=0.20f;		//hero���ڵ����ٶ�
	public static final float ENEMY_MISSILE_SPEED=0.20f;	//h�л��ڵ����ٶ�	
	public static final float HERO_MISSILE_RADIUS=0.02f;	//hero�ڵ��İ뾶
	public static final float HERO_MISSILE_HEIGHT=0.1f;		//hero�ڵ��ĳ���
	public static final float ENEMY_MISSILE_RADIUS=0.015f;	//�л��ڵ��İ뾶
	public static final float ENEMY_MISSILE_HEIGHT=0.1f;	//�л��ڵ��ĳ���
	
	public static final int ENEMYPLANE_TOTOAL_STEP=150;		//�л���ÿ��·�����ߵ��ܲ���
	public static final int ENEMYPLANE_COUNT=3;				//�л�����
	public static final int ENEM_MISSILE_COUNT=6;			//�л���ÿ�γ��ַ����ڵ�������
	public static final int ENEM_MISSILE_SLEEP_TIME=10000;	//�л���ÿ�γ��ַ����ڵ��������ܵ�˯��ʱ��
	
	public static final float Z_DISTANCE_HERO_ENEMY=6;		//hero���͵л���z�����ϳ�ʼʱ�ľ���
	public static final int TOTAL_PATH=10;					//�ܹ���·������
	public static final int TOTAL_POINT_PER_PATH=4;			//ÿ��·���ж��ٸ������
	public static final float[][] path={					//�л�����·�����飬�ֱ��ʾx��y����
		{6.299f,-1.763f,1.785f,-6.299f,-6.299f,-2.156f,0.362f,-6.299f,-6.299f,-2.385f,1.855f,6.299f,6.299f,-0.912f,2.037f,6.299f,-6.299f,0.0010f,-2.658f,6.299f,-6.299f,2.939f,-2.489f,6.299f,6.299f,1.547f,-1.438f,6.299f,6.299f,0.304f,2.093f,6.299f,-6.299f,-2.395f,0.29f,-6.299f,6.299f,-2.129f,0.217f,-6.299f,},
		{-1.973f,0.541f,-1.721f,1.8f,-0.879f,1.448f,-0.886f,1.597f,-1.578f,0.61f,-1.98f,0.105f,-1.996f,1.913f,-1.194f,1.736f,-0.636f,1.819f,-1.334f,0.768f,-1.838f,1.576f,-0.666f,1.516f,-0.934f,1.717f,-0.32f,1.988f,-0.333f,1.688f,-1.142f,1.725f,-0.536f,1.545f,-1.348f,1.438f,-1.957f,0.562f,-1.464f,0.845f,},
	};
	
	public static final float EXPLODE_REC_LENGTH=0.8f;		//��ը���εĳ�	
	public static final float EXPLODE_REC_WIDTH=0.8f;		//��ը���εĿ�	
	public static final float ICON_WIDTH=0.1f;				//ͼ��ߴ�	
	public static final float ICON_HEIGHT=0.1f;				//ͼ���
	
	public final static float BODYBACK_A=0.6f;				//��������a�᳤��
	public final static float BODYBACK_B=0.08f;				//��������b�᳤��
	public final static float BODYBACK_C=0.08f;				//��������c�᳤��
	public final static float BODYHEAD_A=0.2f;				//��ͷ����a�᳤��
	public final static float BODYHEAD_B=0.08f;				//��ͷ����b�᳤��
	public final static float BODYHEAD_C=0.08f;				//��ͷ����c�᳤��
	public final static float CABIN_A=0.08f;				//��������a�᳤��
	public final static float CABIN_B=0.032f;				//��������b�᳤��
	public final static float CABIN_C=0.032f;				//��������c�᳤��
	
	public static final float LAND_HIGHEST=3f;				//½����߸߶� 
	public static float[][] yArray;							//½����ÿ������ĸ߶�����
	public static int COLS;									//½������ 
	public static int ROWS;									//½������ 
	public static void initConstant(Resources r) {			//��ʼ��½�ظ߶�����
		yArray=loadLandforms(r);					
		COLS=yArray[0].length-1;							//���������С����½�ص�����
		ROWS=yArray.length-1;								//���������С����½�ص�����
	}
	
	//�ӻҶ�ͼƬ�м���½����ÿ������ĸ߶�
	public static float[][] loadLandforms(Resources resources){
		Bitmap bt=BitmapFactory.decodeResource(resources, R.drawable.map01);	//���ػҶ�ͼ
		int colsPlusOne=bt.getWidth();							//����
		int rowsPlusOne=bt.getHeight(); 						//����
		float[][] result=new float[rowsPlusOne][colsPlusOne];
		for(int i=0;i<rowsPlusOne;i++){ 
			for(int j=0;j<colsPlusOne;j++){
				int color=bt.getPixel(j,i);						//��ͼƬ��ȡ����ɫֵ
				int r=Color.red(color); 						//ȡ��red��ɫֵ
				int g=Color.green(color); 						//ȡ��green��ɫֵ
				int b=Color.blue(color);						//ȡ��blue��ɫֵ
				int h=(r+g+b)/3;
				result[i][j]=h*LAND_HIGHEST/255;				//����������ɫֵ��ƽ��ֵ���õ��߶�
			}
		}		
		
		return result;
	}
	
	public static final float ANGLE=0.02f;		//����ԭ����ת���ٶ�
	public static final float LAND_R=30f;		//����Բ���뾶��
	public static final float LAND_L=100f;		//����Բ������

	public static final float SKY_R=50f;		//���Բ���뾶
	public static final float SKY_L=100f;		//���Բ������
	public static final float Y_TRASLATE=-(LAND_R+LAND_HIGHEST+4);	//����պ͵�����y������ƽ�Ƶľ���
}
