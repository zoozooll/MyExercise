package wyf.tzz.lta;

import static wyf.tzz.lta.Constant.*;

public class KeyThread extends Thread
{
	MySurfaceView msv;					//MySurfaceView������
	float rotateAngle=20;				//�ɻ����������ƶ�ʱ��x��y����ת�Ƕ�
	boolean overFlag=false;				//�Ƿ���Ϸ������־
	boolean pauseFlag=false;			//�Ƿ���Ϸ��ͣ��־
	final int delayCount=4;				//�ڵ������ٶȵ��ӳٱ���
	int fireCount=0;					//ÿ�ΰ���֮�����ڵ�������
	
	public KeyThread(MySurfaceView msv){
		this.msv=msv;					//MySurfaceView������
	}
	
	public void run()
	{
		float minX=-3+X_MOVE_SPAN*4;	//�ɻ��������ƶ���������
		float maxX=3-X_MOVE_SPAN*4;		//�ɻ����������ƶ���������
		float minY=-2+Y_MOVE_SPAN*10;	//�ɻ��������ƶ���������
		float maxY=2+Y_MOVE_SPAN*3;		//�ɻ��������ƶ���������
		
		while(!overFlag){
			if(pauseFlag){
				try {Thread.sleep(500);} //˯��
				catch (InterruptedException e) {e.printStackTrace();}
				continue;
			}
			
			if((msv.keyState&0x1)!=0&&(msv.keyState&0x2)==0){	//���ϼ���δ���¼�
				msv.plane.mAngleZ=rotateAngle;					//�ɻ���бһ���Ƕ�
				if(msv.plane.y<maxY)							//���С�������ƶ���������
				{
					msv.plane.y+=Y_MOVE_SPAN;					//�ɻ��ƶ�
				}				
			}
			else if((msv.keyState&0x2)!=0&&(msv.keyState&0x1)==0)//�����¼���δ���ϼ�
			{
				msv.plane.mAngleZ=-rotateAngle;					//�ɻ���бһ���Ƕ�
				if(msv.plane.y>minY)							//���С�������ƶ���������
				{
					msv.plane.y-=Y_MOVE_SPAN;					//�ɻ��ƶ�
				}
			}
			else 												//������¼���δ���»������¼�ͬʱ����
			{
				msv.plane.mAngleZ=0;							//���Ϊ�����κμ����ɻ�����б
			}
			
			if((msv.keyState&0x4)!=0&&(msv.keyState&0x8)==0)	//�������δ���Ҽ�
			{
				msv.plane.mAngleX=rotateAngle;					//�ɻ���бһ���Ƕ�
				
				if(msv.plane.x>minX)							//���С�������ƶ���������
				{
					msv.plane.x-=X_MOVE_SPAN;
				}
			}			
			else if((msv.keyState&0x8)!=0&&(msv.keyState&0x4)==0)//�����Ҽ�δ�����
			{
				msv.plane.mAngleX=-rotateAngle;
				if(msv.plane.x<maxX)							//�ɻ���бһ���Ƕ�
				{
					msv.plane.x+=X_MOVE_SPAN;					//�ɻ��ƶ�
				}
			}
			else 												//������Ҽ���δ���»���ͬʱ����
			{
				msv.plane.mAngleX=0;
			}
			
			if((msv.keyState&0x10)!=0)							//�������OK���߿ո���������ڵ�
			{
				if(fireCount%delayCount==0)						//�ӳٷ����ٶ�
				{
					msv.plane.fire(msv);						//�����ڵ�
					msv.activity.playSoundPool(2, 0);			//���ŷ����ڵ���Ч
				}
				fireCount++;									//�����ڵ�������һ
			}
			try {Thread.sleep(50);
			} catch (InterruptedException e) {e.printStackTrace();}
		}
	}
}