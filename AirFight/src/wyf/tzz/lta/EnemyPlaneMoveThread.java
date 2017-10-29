package wyf.tzz.lta;

import static wyf.tzz.lta.Constant.*;

public class EnemyPlaneMoveThread extends Thread
{
	EnemyPlaneGroup epg;				//�л���
	boolean overFlag=false;				//�߳̽�����־
	MySurfaceView msv;					//MySurfaceView������
	int index=0;						//�ɻ�·�ߵ�����
	
	public EnemyPlaneMoveThread(MySurfaceView msv){
		epg=msv.epg;					//�õ��л���
		this.msv=msv;
	}
	
	@Override
	public void run(){
		while(!overFlag)							//�����δ������һֱִ��
		{
			for(int i=0;i<epg.seps.length;i++)		//�����л���
			{
				epg.seps[i].isVisible=true;			//��ÿ�ܵл���Ϊ�ɼ�
				new MoveThread(epg.seps[i],index).start();//�½��л��ƶ��̣߳�������
				try {Thread.sleep(1200);} 			//�л�˯��1200����
				catch (InterruptedException e) {e.printStackTrace();}
			}
			for(int j=0;j<ENEM_MISSILE_COUNT;j++){
				for(int i=0;i<epg.seps.length;i++)	//�����л��飬����
				{
					if(epg.seps[i].isVisible)		//����л���Ȼ�ɼ����򿪻�
					{
						if(overFlag){				//���overFlagΪtrue���˳�
							break;
						}
						epg.seps[i].fire(msv);		//�л������ڵ�
						msv.activity.playSoundPool(3, 0);//���ŵл������ڵ�����
					}
				}
				try {Thread.sleep(ENEM_MISSILE_SLEEP_TIME/ENEM_MISSILE_COUNT);} //˯��
				catch (InterruptedException e) {e.printStackTrace();}
			}
			
			while(true)
			{
				boolean flag=false;					//��־
				for(int i=0;i<epg.seps.length;i++)	//�����л���
				{
					flag=flag||epg.seps[i].isVisible;	//������ел������ɼ�,�������һ��					
				}
				if(!flag)
				{
					break;								//������ел������ɼ�,�������һ�֣�����ѭ��	
				}
				try {Thread.sleep(200);} catch (InterruptedException e) {e.printStackTrace();}//˯��
			}
			index=(index+TOTAL_POINT_PER_PATH)%40;		//������ֵ������������һ��·��
		}
	}
}