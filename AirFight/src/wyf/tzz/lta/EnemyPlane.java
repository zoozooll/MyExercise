package wyf.tzz.lta;
import static wyf.tzz.lta.Constant.*;


import javax.microedition.khronos.opengles.GL10;

public class EnemyPlane
{
	MySurfaceView surface;
	DrawSpheroid bodyback;//��������
	DrawSpheroid bodyhead;//��ͷ����
	DrawSpheroid cabin;//��������
	Plane_Wing frontwing;//ǰ����
	Plane_Wing frontwing2;//ǰ����
	Plane_BackWing backwing;//�����
	Plane_TopWing topwing;//��β��
	Column cylinder;//Բ����
	Column cylinder2;//Բ����
	Column cylinder3;//�ڹ�
	Airscrew screw;//������
	
	float mAngleX;							//��x����ת�ĽǶ�
    float mAngleY;							//��y����ת�ĽǶ�
    float mAngleZ;							//��z����ת�ĽǶ�

    float initAngleY=90;		//��ʼʱ��Y������
    
    float[] planePartLWH=
	{
    		BODYBACK_B*2,BODYBACK_C*2,BODYBACK_A+BODYHEAD_A,	//����
			
	};   
	
	public EnemyPlane(MySurfaceView surface)
	{
		this.surface=surface;
		   
		bodyback=new DrawSpheroid(BODYBACK_A*ENEMYPLANE_SIZE,BODYBACK_B*ENEMYPLANE_SIZE,BODYBACK_C*ENEMYPLANE_SIZE,18,-90,90,-90,90,surface.mRenderer.enemyPlaneBodyId);
		bodyhead=new DrawSpheroid(BODYHEAD_A*ENEMYPLANE_SIZE,BODYHEAD_B*ENEMYPLANE_SIZE,BODYHEAD_C*ENEMYPLANE_SIZE,18,-90,90,-90,90,surface.mRenderer.enemyPlaneHeadId);
		cabin=new DrawSpheroid(CABIN_A*ENEMYPLANE_SIZE,CABIN_B*ENEMYPLANE_SIZE,CABIN_C*ENEMYPLANE_SIZE,18,0,360,-90,90,surface.mRenderer.planeCabinId);
		frontwing = new Plane_Wing(surface.mRenderer.enemyPlaneFrontWingId,0.4f*ENEMYPLANE_SIZE,0.12f*ENEMYPLANE_SIZE,0.004f*ENEMYPLANE_SIZE);		
		frontwing2 = new Plane_Wing(surface.mRenderer.enemyPlaneFrontWing2Id,0.4f*ENEMYPLANE_SIZE,0.12f*ENEMYPLANE_SIZE,0.004f*ENEMYPLANE_SIZE);
		backwing = new Plane_BackWing(surface.mRenderer.enemyPlaneTopWingId,0.14f*ENEMYPLANE_SIZE,0.06f*ENEMYPLANE_SIZE,0.004f*ENEMYPLANE_SIZE);
		topwing = new Plane_TopWing(surface.mRenderer.enemyPlaneTopWingId,0.05f*ENEMYPLANE_SIZE,0.07f*ENEMYPLANE_SIZE,0.01f*ENEMYPLANE_SIZE);
		cylinder = new Column(0.18f*ENEMYPLANE_SIZE,0.006f*ENEMYPLANE_SIZE,surface.mRenderer.cylinder1Id);//����Բ��	
		cylinder2 = new Column(0.06f*ENEMYPLANE_SIZE,0.012f*ENEMYPLANE_SIZE,surface.mRenderer.frontWing2Id);//����Բ��
		cylinder3 = new Column(0.15f*ENEMYPLANE_SIZE,0.02f*ENEMYPLANE_SIZE,surface.mRenderer.frontWing2Id);//����Բ
		screw =  new Airscrew(0.17f*PLANE_SIZE,surface.mRenderer.screw2Id);
		//���Բ��̫�̣��Ͱѳ��ȼӵ�1f��С��ʱ��䵽0.8f
		
		for(int i=0;i<planePartLWH.length;i++)
		{
			planePartLWH[i]=planePartLWH[i]*ENEMYPLANE_SIZE;
		}
	}
	    
	public void drawSelf(GL10 gl)
	{
		gl.glPushMatrix();
    	gl.glRotatef(mAngleZ, 0, 0, 1);//��Z����ת    		
        gl.glRotatef(mAngleY+initAngleY, 0, 1, 0);//��Y����ת
        gl.glRotatef(mAngleX, 1, 0, 0);//��X����ת
        
        gl.glPushMatrix();
        gl.glRotatef(180, 1, 0, 0);
        bodyback.drawSelf(gl);
        gl.glPopMatrix();
        
        gl.glPushMatrix();
        gl.glRotatef(180, 0, 1, 0);
        bodyhead.drawSelf(gl); 
        gl.glRotatef(90, 0, 1, 0);
        gl.glTranslatef(0, 0, 0.2f*ENEMYPLANE_SIZE);
        screw.drawSelf(gl); //������
        gl.glPopMatrix();
        
        gl.glPushMatrix();
        gl.glTranslatef(0, BODYBACK_B*ENEMYPLANE_SIZE/5f, 0);
        gl.glTranslatef(0f*ENEMYPLANE_SIZE, 0, 0);//����Ƶ�
        cabin.drawSelf(gl);
        gl.glPopMatrix();
        					
        								      																											
        //ǰ����
        gl.glPushMatrix();
        gl.glRotatef(90, 0, 1, 0);
        gl.glRotatef(-90, 1, 0, 0);
        gl.glTranslatef(0, 0, 0.12f*ENEMYPLANE_SIZE);//����ƽ��
        frontwing.drawSelf(gl);
        gl.glTranslatef(0, 0, -0.2f*ENEMYPLANE_SIZE);//����ƽ��
        frontwing2.drawSelf(gl);
        gl.glTranslatef(-0.12f*ENEMYPLANE_SIZE, 0,0.03f*ENEMYPLANE_SIZE );//����ƽ��
        cylinder3.drawSelf(gl);
        gl.glTranslatef(0.24f*ENEMYPLANE_SIZE, 0,0 );//����ƽ��
        cylinder3.drawSelf(gl);
        gl.glPopMatrix();			
   
       
        //����Բ��
        gl.glPushMatrix();
        gl.glTranslatef(0.07f*ENEMYPLANE_SIZE, 0.016f*ENEMYPLANE_SIZE, -0.4f*ENEMYPLANE_SIZE);
        cylinder.drawSelf(gl);
        gl.glTranslatef(-0.14f*ENEMYPLANE_SIZE, 0, 0);
        cylinder.drawSelf(gl);
        gl.glTranslatef(0, 0, 0.8f*ENEMYPLANE_SIZE);
        cylinder.drawSelf(gl);
        gl.glTranslatef(0.14f*ENEMYPLANE_SIZE, 0, 0);
        cylinder.drawSelf(gl);
        gl.glPopMatrix();
        
        //����Բ�� 
        gl.glPushMatrix();
        gl.glTranslatef(0, 0.096f*ENEMYPLANE_SIZE, 0.08f*ENEMYPLANE_SIZE);
        gl.glRotatef(30, 1, 0, 0);
        cylinder2.drawSelf(gl);
        gl.glTranslatef(0,  -0.096f*ENEMYPLANE_SIZE, -0.16f*ENEMYPLANE_SIZE);
        gl.glRotatef(-60, 1, 0, 0);
        cylinder2.drawSelf(gl);
        gl.glPopMatrix(); 
      
    
        //β��			
        gl.glPushMatrix();													
        gl.glTranslatef(0.6f*ENEMYPLANE_SIZE, 0, 0);
        gl.glRotatef(90, 0, 1, 0);
        gl.glRotatef(-90, 1, 0, 0);
        backwing.drawSelf(gl);
        gl.glPopMatrix();
 
        //��β��
        gl.glPushMatrix();
        gl.glTranslatef(0.6f*ENEMYPLANE_SIZE, 0, 0);
        topwing.drawSelf(gl);
        gl.glPopMatrix();

        gl.glPopMatrix();
	}
	
	public float[] getLWH()		//���صл�����ĳ����
	{		
		return planePartLWH;
	}
    
    
	
    

}