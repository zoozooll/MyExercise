package wyf.tzz.lta;

import javax.microedition.khronos.opengles.GL10;

public class Missile{
	Column column;		//Բ��������
	float startX;		//�ڵ��ĳ���xλ��
	float startY;		//�ڵ��ĳ���yλ��
	float startZ;		//�ڵ��ĳ���zλ��
	float vx;			//�ڵ��ĳ���x�����ٶ�
	float vy;			//�ڵ��ĳ���y�����ٶ�
	float vz;			//�ڵ��ĳ���z�����ٶ�
	float x;			//�ڵ���ʵʱxλ��
	float y;			//�ڵ���ʵʱyλ��
	float z;			//�ڵ���ʵʱzλ��	
	
	public Missile(Column column,float startX,float startY,float startZ,float vx,float vy,float vz){
		this.column=column;
		this.startX=startX;		//�ڵ��ĳ�x��λ��
		this.startY=startY;		//�ڵ��ĳ�x��λ��
		this.startZ=startZ;
		this.vx=vx;				//�ڵ��ĳ���x�����ٶ�
		this.vy=vy;				//�ڵ��ĳ���y�����ٶ�
		this.vz=vz;
		x=startX;				//�ڵ���ʵʱxλ��
		y=startY;				//�ڵ���ʵʱyλ��
		z=startZ;	
	}

	public float[] getXYZ(){	//����ڵ���xyz����
		float[] result = new float[]{x,y,z};	//�ڵ���ʵʱxyzλ��
		return result;
	}
	
	public void go(){			//�ڵ�����xyz����仯
		x+=vx;					//�ڵ���ʵʱxλ�ñ仯
		y+=vy;
		z+=vz;
	}
	
	public void drawSelf(GL10 gl){
		gl.glPushMatrix();				//������ǰ����	
		gl.glTranslatef(x, y, z);		//ƽ��
		gl.glRotatef(90, 1, 0, 0);		//��ת
		column.drawSelf(gl);			//����
		gl.glPopMatrix();				//�ָ�֮ǰ�����ľ���	
	}
}