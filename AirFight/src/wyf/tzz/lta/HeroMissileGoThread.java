package wyf.tzz.lta;

import static wyf.tzz.lta.Constant.*;

public class HeroMissileGoThread extends Thread
{
	MySurfaceView msv;			//MySurfaceView������
	float[] planeXYZ;			//�ɻ���xyz����
	float[] planeLWH;			//�ɻ��ĳ����
	float[] missileXYZ;			//�ڵ���xyz����
	float[] missileLWH;			//�ڵ��ĳ����
	boolean overFlag=false;		//�߳��Ƿ������־
	boolean isCollied=false;	//���ڵ��Ƿ���ײ���ɻ��ı�־	
	float testDistance=Z_DISTANCE_HERO_ENEMY+1;	//������
	
	public HeroMissileGoThread(MySurfaceView msv){
		this.msv=msv;
		missileLWH=msv.heroMissile.getLengthWidthHeight();	//�õ��ڵ��ĳ����
		planeLWH=msv.epg.ep.getLWH();						//�õ��ɻ��ĳ����
	}
		
	@Override
	public void run(){
		while(!overFlag){
			testLive();										//����ڵ��Ƿ񳬹�����������
			testForCollision();								//�ڵ���л�����ײ���
			for(int i=0;i<msv.heroMissileGroup.size();i++){	//����hero���ڵ��б�
				msv.heroMissileGroup.get(i).go();			//�ڵ�����
	       	}
			try {Thread.sleep(50);							//��˯��50����
			} catch (InterruptedException e) {e.printStackTrace();}
		}
	}
	
	public void testLive(){									//����ڵ��Ƿ񳬹�����������
		for(int i=0;i<msv.heroMissileGroup.size();i++){		//�����ڵ��б�
			if(msv.plane.z-msv.heroMissileGroup.get(i).getXYZ()[2]>testDistance)//����ڵ������˼����룬ɾ������
      		{
				msv.heroMissileGroup.remove(i);				//���ڵ��б�ɾ�����ڵ�
      		}
       	}
	}
	
	public void testForCollision(){							//�ڵ���л�����ײ���
		for(int i=0;i<msv.heroMissileGroup.size();i++)
       	{
			for(int j=0;j<ENEMYPLANE_COUNT;j++)
			{
				if(msv.epg.seps[j].isVisible==false){		//����л����ɼ��������
					continue;
				}
				planeXYZ=msv.epg.seps[j].getXYZ();			//�õ��л���xyz����
				missileXYZ=msv.heroMissileGroup.get(i).getXYZ();						//�õ��ڵ���xyz����
				isCollied=collisionTestUnit(planeXYZ,planeLWH,missileXYZ,missileLWH);	//��ײ���
				if(isCollied==true){
					msv.epg.seps[j].isCollied=true;			//���л�isCollied��־��Ϊtrue
					msv.epg.seps[j].isVisible=false;		//���л�isVisible��־��Ϊfalse
					msv.activity.playSoundPool(1, 0);		//���ŵл���ը����
					msv.score.goal++;						//���÷ּ�һ
					if(msv.score.goal>=Constant.GOAL_COUNT)	//������÷ִﵽҪ����ʤ��
					{
						msv.isWin=true;						
						msv.over();							//����over������ͣ�¸����߳�
					}
					break;
				}
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