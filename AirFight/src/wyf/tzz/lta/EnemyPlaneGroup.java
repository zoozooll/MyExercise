package wyf.tzz.lta;

import static wyf.tzz.lta.Constant.*;

import javax.microedition.khronos.opengles.GL10;

public class EnemyPlaneGroup{
	EnemyPlane ep;						//�л�
	TextureRect trExplo[];				//��ը��ͼ
	SingleEnemyPlane[] seps=new SingleEnemyPlane[ENEMYPLANE_COUNT];//�л�����
	
	public EnemyPlaneGroup(EnemyPlane ep ,TextureRect trExplo[]){
		this.ep=ep;
		this.trExplo=trExplo;
		for(int i=0;i<seps.length;i++)			//�����л�����
		{
			seps[i]=new SingleEnemyPlane(this);//���������л�
		}
	}
	
	public void drawSelf(GL10 gl){				//���Ʒ���	
		for(int i=0;i<seps.length;i++)			//�����л�����
		{
			seps[i].drawSelf(gl);				//�ֱ����ÿ���л�
		}
	}
}
