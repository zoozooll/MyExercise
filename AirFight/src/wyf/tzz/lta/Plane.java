package wyf.tzz.lta;

import static wyf.tzz.lta.Constant.*;



import javax.microedition.khronos.opengles.GL10;


public class Plane
{
	MySurfaceView surface;		//MySurfaceView ������
	DrawSpheroid bodyback;		//��������
	DrawSpheroid bodyhead;		//��ͷ����
	DrawSpheroid cabin;			//��������
	Plane_Wing frontwing;		//ǰ����
	Plane_Wing frontwing2;		//ǰ����
	Plane_BackWing backwing;	//�����
	Plane_TopWing topwing;		//��β��
	
	Column cylinder;			//Բ����
	Column cylinder2;			//Բ����
	Column cylinder3;			//�ڹ�
	Airscrew screw;   			//������
	float mAngleX;				//��x����ת�ĽǶ�
    float mAngleY;				//��y����ת�ĽǶ�
    float mAngleZ;				//��z����ת�ĽǶ�
	
	float x;					//��x�᷽���ƫ����
    float y;					//��y�᷽���ƫ����
    float z;					//��z�᷽���ƫ����

    float initAngleY=-90;		//��ʼʱ��x������
    

    float[] planePartLWH=		//��÷ɻ��ĳ��߿�
	{
    		BODYBACK_B*2,BODYBACK_C*2,BODYBACK_A+BODYHEAD_A,	//����
			
	};
	
	public Plane(MySurfaceView surface)
	{
		this.surface=surface;      
		//��ø����������� 
		bodyback=new DrawSpheroid(BODYBACK_A*PLANE_SIZE,BODYBACK_B*PLANE_SIZE,BODYBACK_C*PLANE_SIZE,18,-90,90,-90,90,surface.mRenderer.planeBodyId);
		bodyhead=new DrawSpheroid(BODYHEAD_A*PLANE_SIZE,BODYHEAD_B*PLANE_SIZE,BODYHEAD_C*PLANE_SIZE,18,-90,90,-90,90,surface.mRenderer.planeHeadId);
		cabin=new DrawSpheroid(CABIN_A*PLANE_SIZE,CABIN_B*PLANE_SIZE,CABIN_C*PLANE_SIZE,18,0,360,-90,90,surface.mRenderer.planeCabinId);
		
		frontwing = new Plane_Wing(surface.mRenderer.frontWingId,0.4f*PLANE_SIZE,0.12f*PLANE_SIZE,0.004f*PLANE_SIZE);		
		frontwing2 = new Plane_Wing(surface.mRenderer.frontWing2Id,0.4f*PLANE_SIZE,0.12f*PLANE_SIZE,0.004f*PLANE_SIZE);
		backwing = new Plane_BackWing(surface.mRenderer.bacckWingId,0.14f*PLANE_SIZE,0.06f*PLANE_SIZE,0.004f*PLANE_SIZE);
		topwing = new Plane_TopWing(surface.mRenderer.topWingId,0.05f*PLANE_SIZE,0.07f*PLANE_SIZE,0.01f*PLANE_SIZE);
	 	
		cylinder = new Column(0.18f*PLANE_SIZE,0.006f*PLANE_SIZE,surface.mRenderer.cylinder1Id);//����Բ��	
		cylinder2 = new Column(0.1f*PLANE_SIZE,0.015f*PLANE_SIZE,surface.mRenderer.frontWing2Id);//����Բ��
		cylinder3 = new Column(0.15f*PLANE_SIZE,0.02f*PLANE_SIZE,surface.mRenderer.frontWing2Id);//����Բ
		screw =  new Airscrew(0.17f*PLANE_SIZE,surface.mRenderer.screw1Id);
		//�����ɻ���С
		for(int i=0;i<planePartLWH.length;i++)
		{
			planePartLWH[i]=planePartLWH[i]*PLANE_SIZE;
		}
	}
	    
	public void drawSelf(GL10 gl)
	{
		gl.glPushMatrix();
		
		gl.glTranslatef(x, y, z);
		
    	gl.glRotatef(mAngleZ, 0, 0, 1);			//��Z����ת    		
        gl.glRotatef(mAngleY+initAngleY, 0, 1, 0);//��Y����ת
        gl.glRotatef(mAngleX, 1, 0, 0);			//��X����ת
        
        gl.glPushMatrix();
        gl.glRotatef(180, 1, 0, 0);
        bodyback.drawSelf(gl);					//������
        gl.glPopMatrix();
        
        gl.glPushMatrix();
        gl.glRotatef(180, 0, 1, 0);
        bodyhead.drawSelf(gl); 					//����ͷ
        gl.glRotatef(90, 0, 1, 0);
        gl.glTranslatef(0, 0, 0.2f*ENEMYPLANE_SIZE);
        screw.drawSelf(gl); 					//������
        gl.glPopMatrix();
        
        gl.glPushMatrix();
        gl.glTranslatef(0, BODYBACK_B*ENEMYPLANE_SIZE/5f, 0);
        gl.glTranslatef(0f*ENEMYPLANE_SIZE, 0, 0);
        cabin.drawSelf(gl);						//����
        gl.glPopMatrix();

        //ǰ����
        gl.glPushMatrix();
        gl.glRotatef(90, 0, 1, 0);
        gl.glRotatef(-90, 1, 0, 0);
        gl.glTranslatef(0, 0, 0.12f*PLANE_SIZE);
        frontwing.drawSelf(gl);					//��ǰ����	
        gl.glTranslatef(0, 0, -0.2f*PLANE_SIZE);
        frontwing2.drawSelf(gl);				//��ǰ����
        gl.glTranslatef(-0.12f*PLANE_SIZE, 0,0.03f*PLANE_SIZE );
        cylinder3.drawSelf(gl);					//����Բ��1
        gl.glTranslatef(0.24f*PLANE_SIZE, 0,0 );
        cylinder3.drawSelf(gl);					//����Բ��2
        gl.glPopMatrix();			
       
        //����Բ��
        gl.glPushMatrix();
        gl.glTranslatef(0.07f*PLANE_SIZE, 0.016f*PLANE_SIZE, -0.4f*PLANE_SIZE);
        cylinder.drawSelf(gl);
        gl.glTranslatef(-0.14f*PLANE_SIZE, 0, 0);
        cylinder.drawSelf(gl);
        gl.glTranslatef(0, 0, 0.8f*PLANE_SIZE);
        cylinder.drawSelf(gl);
        gl.glTranslatef(0.14f*PLANE_SIZE, 0, 0);
        cylinder.drawSelf(gl);
        gl.glPopMatrix();
        
        //����Բ�� 
        gl.glPushMatrix();
        gl.glTranslatef(0, 0.096f*PLANE_SIZE, 0.08f*PLANE_SIZE);
        gl.glRotatef(30, 1, 0, 0);
        cylinder2.drawSelf(gl);
        gl.glTranslatef(0,  -0.096f*PLANE_SIZE, -0.16f*PLANE_SIZE);
        gl.glRotatef(-60, 1, 0, 0);
        cylinder2.drawSelf(gl);
        gl.glPopMatrix(); 
      
        //β��			
        gl.glPushMatrix();													
        gl.glTranslatef(0.6f*PLANE_SIZE, 0, 0);
        gl.glRotatef(90, 0, 1, 0);
        gl.glRotatef(-90, 1, 0, 0);
        backwing.drawSelf(gl);
        gl.glPopMatrix();
 
        //��β��
        gl.glPushMatrix();
        gl.glTranslatef(0.6f*PLANE_SIZE, 0, 0);
        topwing.drawSelf(gl);
        gl.glPopMatrix();
        
        gl.glPopMatrix();
	}
	
	public float[] getLWH()
	{		
		return planePartLWH;
	}
	
	//��÷ɻ��� x,y,z����
	public float[] getXYZ()
	{		
		float[] xyz=new float[]{x,y,z+PLANE_SIZE*(BODYBACK_A-BODYHEAD_A)/2};
		return xyz;
	}
    
	 public void fire(MySurfaceView msv)
    {
    	//�ӵ��ĳ���λ��
    	float startX=x;
    	float startY=y-0.05f;
    	float startZ=z-0.5f;
    	//�ӵ��ĳ����ٶ�
    	float vx=0;
    	float vy=0;
    	float vz=-1*HERO_MISSILE_SPEED;	
    	Missile missile;    	
    	
		startX=x+planePartLWH[0];		//��һ���ڵ��ĳ���x����λ��
		missile =new Missile(msv.heroMissile,startX,startY,startZ,vx,vy,vz);//�����ڵ�    	
    	msv.heroMissileGroup.add(missile);//���ڵ���ӵ��ڵ��б�    	
    	startX=x-planePartLWH[0];		//�ڶ����ڵ��ĳ���x����λ��    	
    	missile =new Missile(msv.heroMissile,startX,startY,startZ,vx,vy,vz);//�����ڵ�    	
    	msv.heroMissileGroup.add(missile);//���ڵ���ӵ��ڵ��б�    	
    }
}