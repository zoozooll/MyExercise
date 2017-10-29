package wyf.tzz.lta;

import static wyf.tzz.lta.Constant.*;

import javax.microedition.khronos.opengles.GL10;

public class SingleEnemyPlane{
	EnemyPlaneGroup epg;//�л���
	float x=10;			//�л���x����
	float y;			//�л���y����
	float z;			//�л���z����	
	float hAngle;		//�л�ˮƽ��б�ĽǶ�
	float vAngle;		//�л���ֱ��б�ĽǶ�
	boolean isVisible=true;		//�Ƿ�ɼ��ı�־
	boolean isCollied=false;	//�Ƿ���ײ�ı�־
	int anmiIndex=0;			//��ը������ͼƬ����
	int delayCount=1;			//��ը������������
	
	public SingleEnemyPlane(EnemyPlaneGroup epg){
		this.epg=epg;			//�л���
	}
	
	public void drawSelf(GL10 gl)
	{
		if(isCollied)									//���������
		{
			if(anmiIndex/delayCount<epg.trExplo.length)	//����û�в����궯����֡	
			{
	    		gl.glPushMatrix();						//������ǰ����	
	    		gl.glTranslatef(x, y, z);				//ƽ��
	    		gl.glDisable(GL10.GL_DEPTH_TEST); 		//�ر���Ȳ���
	    		gl.glEnable(GL10.GL_BLEND);				//�������
		    	gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		    	epg.trExplo[anmiIndex/delayCount].drawSelf(gl);//���Ʊ�ը������ǰ֡
				gl.glDisable(GL10.GL_BLEND);			//�رջ��
		    	gl.glPopMatrix();						//�ָ�֮ǰ���� 					
				gl.glEnable(GL10.GL_DEPTH_TEST); 		//������Ȳ���
				anmiIndex=anmiIndex+1;					//��һ֡
			}
			else{										
				isCollied=false;						//����ײ��־��Ϊfalse
				anmiIndex=0;							//����ը����֡��Ϊ0	
			}
		}
		
		if(isVisible)									//����ɼ�������Ʒɻ�
		{
			gl.glPushMatrix();
			gl.glTranslatef(x, y, z);					//ƽ��
			gl.glRotatef(hAngle, 0, 1, 0);				//ˮƽ��б
			gl.glRotatef(-vAngle, 1, 0, 0);				//��ֱ��б			
			epg.ep.drawSelf(gl);						//����
			gl.glPopMatrix();
		}
	}
	
	public float[] getXYZ(){							//���طɻ���ǰ��xyz����
		float[] xyz=new float[]{x,y,z-ENEMYPLANE_SIZE*(BODYBACK_A-BODYHEAD_A)/2};
		return xyz;
	}
	
	public void fire(MySurfaceView msv) {
		if(isCollied) {			//�����ǰ�ɻ����ɼ������ܿ���	
		  return;
		}    	
    	float startX=x+epg.ep.getLWH()[0]/2;		//�ڵ��ĳ���λ��x����
    	float startY=y-0.05f;						//�ڵ��ĳ���λ��y����
    	float startZ=z+0.5f;						//�ڵ��ĳ���λ��z����
    	
    	//�ӵ��ĳ����ٶ���xyz����ķ��ٶ�
    	float vx=(float) (ENEMY_MISSILE_SPEED*Math.cos(Math.toRadians(vAngle))*Math.sin(Math.toRadians(hAngle)));
    	float vy=(float) (ENEMY_MISSILE_SPEED*Math.sin(Math.toRadians(vAngle)));
    	float vz=(float) (ENEMY_MISSILE_SPEED*Math.cos(Math.toRadians(vAngle))*Math.cos(Math.toRadians(hAngle)));	
    	
    	Missile missile =new Missile(msv.enemyMissile,startX,startY,startZ,vx,vy,vz);//��ʼ����ߵ��ڵ�
    	msv.enemyMissileGroup.add(missile);											//���ڵ���ӵ��ڵ��б���
    	startX=x-epg.ep.getLWH()[0]/2;												//�ڵ��ĳ���λ��x����
    	missile =new Missile(msv.enemyMissile,startX,startY,startZ,vx,vy,vz);		//��ʼ���ұߵ��ڵ�
    	msv.enemyMissileGroup.add(missile);											//���ڵ���ӵ��ڵ��б���
    }
}