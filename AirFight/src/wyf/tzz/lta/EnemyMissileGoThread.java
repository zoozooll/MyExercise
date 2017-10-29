package wyf.tzz.lta;

public class EnemyMissileGoThread extends Thread 
{
	MySurfaceView msv;			//MySurfaceView������
	float[] planeXYZ;			//�ɻ���xyz����
	float[] planeLWH;			//�ɻ��ĳ����
	float[] missileXYZ;			//�ڵ���xyz����
	float[] missileLWH;			//�ڵ��ĳ����
	boolean overFlag=false;		//�߳��Ƿ������־
	boolean isCollied=false;	//���ڵ��Ƿ���ײ���ɻ��ı�־
	
	public EnemyMissileGoThread(MySurfaceView msv)
	{
		this.msv=msv;
		missileLWH=msv.enemyMissile.getLengthWidthHeight();		//�õ��ڵ��ĳ����
		planeLWH=msv.plane.getLWH();							//�õ��ɻ��ĳ����
	}
	
	@Override
	public void run(){
		while(!overFlag)
		{
			
			testLive();											//����ڵ��Ƿ񳬹�����������
			testForCollision();									//�ڵ���hero������ײ���
			for(int i=0;i<msv.enemyMissileGroup.size();i++)		//�����л��ڵ��б�
	       	{
				msv.enemyMissileGroup.get(i).go();				//�ڵ�����
	       	}
			try {
				Thread.sleep(50);								//��˯��50����
			} catch (InterruptedException e) {e.printStackTrace();}
		}	
	}
	
	public void testLive(){										//����ڵ��Ƿ񳬹�����������		
		for(int i=0;i<msv.enemyMissileGroup.size();i++)			//�����л��ڵ��б�
       	{
			if(msv.enemyMissileGroup.get(i).getXYZ()[2]-msv.plane.z>1.5f)//����л��ڵ�������hero��z����1.5fʱ
      		{
				msv.enemyMissileGroup.remove(i);				//�ӵл��ڵ��б�ɾ�����ڵ�
      		}
       	}	
	}
	
	public void testForCollision()	{							//�ڵ���hero������ײ���
		for(int i=0;i<msv.enemyMissileGroup.size();i++)			//�����л��ڵ��б�
       	{
			missileXYZ=msv.enemyMissileGroup.get(i).getXYZ();	//�õ��ڵ���xyz����
			planeXYZ=msv.plane.getXYZ();						//�õ��ɻ���xyz����
			isCollied=collisionTestUnit(planeXYZ,planeLWH,missileXYZ,missileLWH);//��ײ���
			if(isCollied==true)
			{
				msv.overFlag=true;								//��msv��overFlag��־��Ϊtrue
				msv.keyThread.pauseFlag=true;					//�����������̵߳�pauseFlag��־��Ϊtrue
				msv.activity.playSoundPool(1, 0);				//���ŷɻ���ը����
				return;
			}	
       	}
	}
	
	public boolean collisionTestUnit(
			float[] planeXYZ,float[] planeLWH,					//�ɻ���xyz�������
			float[] obstacleXYZ,float[] obstacleLWH)			//�ϰ����xyz�������
	{
		float overlapLength=(planeLWH[0]+obstacleLWH[0])/2		//x�������ཻ�ĳ���
			-Math.abs(planeXYZ[0] - obstacleXYZ[0]);
		float overlapWidth=(planeLWH[1]+obstacleLWH[1])/2		//y�������ཻ�ĳ���
			-Math.abs(planeXYZ[2] - obstacleXYZ[2]);
		float overlapHeight=(planeLWH[2]+obstacleLWH[2])/2		//z�������ཻ�ĳ���
			-Math.abs(planeXYZ[1] - obstacleXYZ[1]);
		if(overlapLength>0&&overlapWidth>0&&overlapHeight>0)
		{
			float overlapVolume=overlapLength*overlapWidth*overlapHeight;	//�ཻ�����
			float planeVolume=planeLWH[0]*planeLWH[1]*planeLWH[2];			//�ɻ������
			
			if(overlapVolume/planeVolume>=0.0001f)				//����ཻ����ȳ���0.0001f����Ϊ��ײ
			{
				return true;
			}
		}
		return false;											//����û����ײ
	}
}