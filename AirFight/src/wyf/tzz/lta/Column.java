package wyf.tzz.lta;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

public class Column
{
	private FloatBuffer   mVertexBuffer;//�����������ݻ���
	   
    private FloatBuffer mTextureBuffer;//�����������ݻ���
     
    private int vCount;    //��������
    
    public float mAngleX;//��x����ת�Ƕ�
    public float mAngleY;//��y����ת�Ƕ�
    public float mAngleZ;//��z����ת�Ƕ�
    
    float mOffsetX;//��x��ƽ�ƾ���
    float mOffsetY;//��y��ƽ�ƾ���
    float mOffsetZ;//��z��ƽ�ƾ���
    
    int texId;	//����id
    float height;//���ø�
    float radius;//���ð뾶
    
    private float heightSpan=0.05f; //�߶��зֵ�Ԫ
    private float angleSpan=30;		//�Ƕ��зִ�С
	
    //Բ�����з�����������
    int col=(int) (360/angleSpan);//Բ�����з�����
    int row=10;					  //Բ�����з�����������
    
    public Column(float height,float radius,int texId)
    {
    	this.height=height;
    	this.radius=radius;
    	this.texId=texId;
    	heightSpan=height/row;		//�߶��зֵ�Ԫ
    	initVertex();				//��ʼ��������������
    	initTexture();				//��ʼ��������������
    }
   
    public void initVertex()
	{    	
    	ArrayList<Float> alVertex=new ArrayList<Float>();//��Ŷ���
    	
		for(int i=0;i<=row;i++)
		{
			float y=(i-row/2.0f)*heightSpan;//���������ζ����y����ı���
			
			float hAngle=0;					//ˮƽ��Ƕ�
			
			for(int j=0;j<=col;j++)
			{
				hAngle=j*angleSpan;			//ˮƽ���ϵĽǶ�
				
				float x=(float) (radius*Math.cos(Math.toRadians(hAngle)));//���������ζ����x����ı���
				float z=(float) (radius*Math.sin(Math.toRadians(hAngle)));//���������ζ����z����ı���
				alVertex.add(x);			//���x����
				alVertex.add(y);			//���y����
				alVertex.add(z);			//���z����
			}
		}
		
		
		
		ArrayList<Integer> alIndex=new ArrayList<Integer>();		//��������
		
		int ncol=col+1;					//ÿ��ʵ�ʶ�������
		for(int i=0;i<row;i++)
		{
			for(int j=0;j<col;j++)
			{
				int k=i*ncol+j;			//����������
					
				alIndex.add(k);			//��������
				alIndex.add(k+ncol);
				alIndex.add(k+1);
				
				alIndex.add(k+ncol);	//��������
				alIndex.add(k+ncol+1);
				alIndex.add(k+1);
			}
		}
  		vCount=alIndex.size();			//��������
  		float vertices[]=new float[alIndex.size()*3];//������������
  		
		for(int i=0;i<vCount;i++)				//����������Ӷ���
		{
			int k=alIndex.get(i);				//ȡ������������
			vertices[i*3]=alVertex.get(k*3);	//���������x����
			vertices[i*3+1]=alVertex.get(k*3+1);//���������y����
			vertices[i*3+2]=alVertex.get(k*3+2);//���������z����
		}
		ByteBuffer vbb=ByteBuffer.allocateDirect(vertices.length*4);//һ��float��4��byte
		vbb.order(ByteOrder.nativeOrder());							//�����ֽ�˳��
		mVertexBuffer=vbb.asFloatBuffer();							//װ��Ϊfloat����
		mVertexBuffer.put(vertices);								//���붥������
		mVertexBuffer.position(0);									//������ʼλ�� 
	}
    
    public void initTexture()
	{
		int tCount=row*col*2*3*2;					//��������
		float[] textures=new float[tCount];			//������������
		
		float sizew=1.0f/col;						//�����������굥Ԫ
		float sizeh=1.0f/row;
		
		for(int i=0,temp=0;i<row;i++)
		{
			float t=i*sizeh;
			for(int j=0;j<col;j++) 
			{
				float s=j*sizew;
				//����������
				textures[temp++]=s;
				textures[temp++]=t;
				
				textures[temp++]=s;
				textures[temp++]=t+sizeh;
				
				textures[temp++]=s+sizew;
				textures[temp++]=t;
				
				//����������
				textures[temp++]=s;
				textures[temp++]=t+sizeh;
				
				textures[temp++]=s+sizew;
				textures[temp++]=t+sizeh;
				
				textures[temp++]=s+sizew;
				textures[temp++]=t;
			}
		}
		
		ByteBuffer tbb=ByteBuffer.allocateDirect(textures.length*4);//һ��float��4��byte
		tbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
		mTextureBuffer=tbb.asFloatBuffer();//ת��ΪFLOAT�ͻ���
		mTextureBuffer.put(textures);//�򻺳����з��붥����������
		mTextureBuffer.position(0);//����������ʼλ��		
	}
	
    
  
	public void drawSelf(GL10 gl){
		gl.glPushMatrix();										//������ǰ����		
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);			//����������������
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);	//Ϊ����ָ��������������		
		gl.glEnable(GL10.GL_TEXTURE_2D);						//��������
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);	//������������
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTextureBuffer);//Ϊ����ָ����������
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texId);			//������
		gl.glTranslatef(mOffsetX, mOffsetY, mOffsetZ);			//ƽ��
		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, vCount);			//����		
		gl.glPopMatrix();										//�ָ�֮ǰ�����ľ���	
	}
    
	
	 public float[] getLengthWidthHeight(){
			float[] lwh={
					this.radius*2,		//��
					this.radius*2,		//��
					this.height			//��
			};			
			return lwh;
		}
}