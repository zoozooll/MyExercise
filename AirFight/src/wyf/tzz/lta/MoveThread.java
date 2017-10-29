package wyf.tzz.lta;

import static wyf.tzz.lta.Constant.*;

public class MoveThread extends Thread{
	SingleEnemyPlane sep;			//���ܵл�
	int index;						//�л�����·�ߵ�����
	boolean overFlag=false;			//�Ƿ��̵߳Ľ�����־
	int start;						//��������
	int target;						//�յ������
	float xSpan=0;					//�л�ÿ����x�������ƶ��ľ���
	float ySpan=0;					//�л�ÿ����y�������ƶ��ľ���
	float zSpan=(Z_DISTANCE_HERO_ENEMY+1)/(ENEMYPLANE_TOTOAL_STEP*3);//�л�ÿ����z�������ƶ��ľ���
	int step=0;						//�л���ÿ��·����ÿһ���ϵĲ���
	
	public MoveThread(SingleEnemyPlane sep,int index){
		this.sep=sep;				//���ܵл�
		this.index=index;
	}
	
	public void run(){
		start=index;
		sep.z=-Z_DISTANCE_HERO_ENEMY;
		startPath();
		
		while(!overFlag){
			if(!sep.isVisible){		//����л���ʱ���ɼ����˳�ѭ��
				break;
			}
			
			if(step>=ENEMYPLANE_TOTOAL_STEP){				//����ɻ��߶��������������������ʼ��һ��·��
				if(start==index+TOTAL_POINT_PER_PATH-2){	//˵������·���Ѿ����꣬�߳̽���
					break;
				}
				start=(start+1)%path[0].length;
				startPath();
			}
			else{
				step++;						//������һ
				sep.z=sep.z+zSpan;				//z����ÿ���ƶ�����
				sep.x=sep.x+xSpan;			//x����ÿ���ƶ�����
				sep.y=sep.y+ySpan;			//y����ÿ���ƶ�����
			}
			
			try {Thread.sleep(30);
			} catch (InterruptedException e) {e.printStackTrace();}
		}
		sep.isVisible=false;				//�߳̽���ʱ�����л���Ϊ���ɼ�
	}
	
	public void startPath(){
		step=0;								//���Ѿ��߹��Ĳ�����Ϊ0
		target=(start+1)%path[0].length;	//�յ������
		sep.x=path[0][start];				//ÿ��·������ʼx����
		sep.y=path[1][start];				//ÿ��·������ʼy����
		float xs=path[0][target]-path[0][start];		//ÿ��·��x�������ܵľ���
		float ys=path[1][target]-path[1][start];		//ÿ��·����y�������ܵľ���
		float zs=zSpan*ENEMYPLANE_TOTOAL_STEP*2;		//ÿ��·��z�������ܵľ���
		xSpan=xs/ENEMYPLANE_TOTOAL_STEP;				//�л�ÿ����x�������ƶ��ľ���
		ySpan=ys/ENEMYPLANE_TOTOAL_STEP;				//�л�ÿ����y�������ƶ��ľ���
		sep.hAngle=(float) Math.toDegrees(Math.atan(xs/zs));	//�л�ˮƽ��б�ĽǶ�
		//�л���б�ĽǶ�
		sep.vAngle=(float) Math.toDegrees(Math.atan(ys/Math.sqrt(xs*xs+zs*zs)));
	}
}