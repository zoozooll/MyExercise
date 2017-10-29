package wyf.tzz.lta;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Airscrew 
{
	private FloatBuffer mVertexBuffer;	//�����������ݻ���
	private FloatBuffer mTextureBuffer;	//�����������ݻ���
	
	private float mAngleZ;				//��z����ת�Ƕ� 
	
	int vCount=6;						//��������
	final int angleSpan=8;				//ÿƬ������ҶƬ�Ƕ�
	int texId;							//����ID
	float scale;						//�ߴ�
	float zSpan=0;						//��������z���ϵ�ƫ��
	
	public Airscrew(float scale,int texId){
		this.scale=scale;	
		this.texId = texId;	
		zSpan=scale/12;		//��������z���ϵ�ƫ��
		initVertex();		//��ʼ��������������
		initTexture();		//��ʼ��������������
	}
	
	 public void initVertex()
		{   //����������
			float x=(float) (this.scale*Math.cos(Math.toRadians(angleSpan)));//���������ζ����x����ı���
			
			float y=(float) (this.scale*Math.sin(Math.toRadians(angleSpan)));//���������ζ����y����ı���
			
			float z=zSpan;														 //���������ζ����z����ı���
			
			//�������껺�������ʼ��
			float[] vertices=
			{				
				//���������������ε�����
				0,0,0,
				x,y,0,
				x,-y,-z,
				
				//�����ڲ����������ε�����
				0,0,0,
				x,-y,-z,
				x,y,0,								
			};
		   
			
			ByteBuffer vbb=ByteBuffer.allocateDirect(vertices.length*4);	 //���������������ݻ��壬��һ��Float�ĸ��ֽ�
			vbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
			mVertexBuffer=vbb.asFloatBuffer();//ת��Ϊfloat�ͻ���
			mVertexBuffer.put(vertices);//�򻺳����з��붥����������
			mVertexBuffer.position(0);//���û�������ʼλ��
		}
	
	public void initTexture()
	{
		float[] textures=generateTextures();	//����������������
		ByteBuffer tbb=ByteBuffer.allocateDirect(textures.length*4);
		tbb.order(ByteOrder.nativeOrder());		//�����ֽ�˳��
		mTextureBuffer=tbb.asFloatBuffer();		//ת��Ϊfloat�ͻ���
		mTextureBuffer.put(textures);			//�򻺳����з��붥����������
		mTextureBuffer.position(0);				//���û�������ʼλ��
	}
	
	public float[] generateTextures(){
		float[] textures=new float[]{			//����������������
				0,0,1,0,0,1,
				0,0,0,1,1,0,
	       };
		return textures;
	}
	

	
	public void drawSelf(GL10 gl)
	{
		gl.glPushMatrix();										//������ǰ����
		gl.glRotatef(mAngleZ, 0, 0, 1);							//��z����ת
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);			//����������������
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer); //Ϊ����ָ��������������
		
		gl.glEnable(GL10.GL_TEXTURE_2D);						//��������
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);	//������������
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTextureBuffer);//Ϊ����ָ����������
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texId);			//������
		
		gl.glDrawArrays(GL10.GL_TRIANGLES,0, vCount);			//����
		gl.glRotatef(60, 0, 0, 1);								//��y����ת
		gl.glDrawArrays(GL10.GL_TRIANGLES,0, vCount);			//����
		gl.glRotatef(60, 0, 0, 1);								//��y����ת
		gl.glDrawArrays(GL10.GL_TRIANGLES,0, vCount);			//����
		gl.glRotatef(60, 0, 0, 1);								//��y����ת
		gl.glDrawArrays(GL10.GL_TRIANGLES,0, vCount);			//����
		gl.glRotatef(60, 0, 0, 1);								//��y����ת
		gl.glDrawArrays(GL10.GL_TRIANGLES,0, vCount);			//����
		gl.glRotatef(60, 0, 0, 1);								//��y����ת
		gl.glDrawArrays(GL10.GL_TRIANGLES,0, vCount);			//����	
		gl.glPopMatrix();										//�ָ�֮ǰ�����ľ���
		mAngleZ=mAngleZ+27;										//��z����ת�Ƕ��Լӣ���������������ת
	}
}