package wyf.tzz.lta;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

public class DrawCylinder
{
	private FloatBuffer myVertexBuffer;	//�������껺�� 
	private FloatBuffer myTexture;		//������
	
	int textureId;
	
	int vCount;							//��������
	
	float length;			//Բ������
	float circle_radius;	//Բ�ػ��뾶
	int row;				//�����зֿ��� 
	int col;				//Բ������ 
	float[][] yArray;		//����Ҷ�����ʾ�ĸ߶�����
	
	public float mAngleX;
	
	public DrawCylinder(float length,float circle_radius,int row,int col,float[][] yArray,int textureId)//Բ���Ǻ�����ã����ĵ���ԭ�㡣
	{
		this.circle_radius=circle_radius;
		this.length=length;
		this.col=col;
		this.row=row;
		this.yArray=yArray;
		this.textureId=textureId;
		
		float collength=(float)length/col;			//Բ��ÿ����ռ�ĳ���
		float rowSpan=(float)(360f/row);			//����Ƕȱ仯��λ
		 
		ArrayList<Float> val=new ArrayList<Float>();//�������б�
		
		for(float circle_degree=360.0f,i=0;circle_degree>0.0f;circle_degree-=rowSpan,i++)//ѭ����
		{
			for(int j=0;j<col;j++)//ѭ����
			{
				float x1 =(float)(j*collength-length/2);
				float y1=(float) ((circle_radius+yArray[(int)(i)][j])*Math.sin(Math.toRadians(circle_degree)));
				float z1=(float) ((circle_radius+yArray[(int)(i)][j])*Math.cos(Math.toRadians(circle_degree)));
				
				float x2 =(float)(j*collength-length/2);
				float y2=(float) ((circle_radius+yArray[(int)(i+1)][j])*Math.sin(Math.toRadians(circle_degree-rowSpan)));
				float z2=(float) ((circle_radius+yArray[(int)(i+1)][j])*Math.cos(Math.toRadians(circle_degree-rowSpan)));
				
				float x3 =(float)((j+1)*collength-length/2);
				float y3=(float) ((circle_radius+yArray[(int)(i+1)][j+1])*Math.sin(Math.toRadians(circle_degree-rowSpan)));
				float z3=(float) ((circle_radius+yArray[(int)(i+1)][j+1])*Math.cos(Math.toRadians(circle_degree-rowSpan)));
				
				float x4 =(float)((j+1)*collength-length/2);
				float y4=(float) ((circle_radius+yArray[(int)(i)][j+1])*Math.sin(Math.toRadians(circle_degree)));
				float z4=(float) ((circle_radius+yArray[(int)(i)][j+1])*Math.cos(Math.toRadians(circle_degree)));
				
				val.add(x1);val.add(y1);val.add(z1);//���������Σ���6�����������
				val.add(x2);val.add(y2);val.add(z2);
				val.add(x4);val.add(y4);val.add(z4);
				
				val.add(x2);val.add(y2);val.add(z2);
				val.add(x3);val.add(y3);val.add(z3);
				val.add(x4);val.add(y4);val.add(z4);
			}
		}
		vCount=val.size()/3;//ȷ����������
		//����
		float[] vertexs=new float[vCount*3];
		for(int i=0;i<vCount*3;i++)
		{
			vertexs[i]=val.get(i);
		}
		ByteBuffer vbb=ByteBuffer.allocateDirect(vertexs.length*4);
		vbb.order(ByteOrder.nativeOrder());
		myVertexBuffer=vbb.asFloatBuffer();
		myVertexBuffer.put(vertexs);
		myVertexBuffer.position(0);
		//����
		float[] textures=generateTexCoor(col,row);
		ByteBuffer tbb=ByteBuffer.allocateDirect(textures.length*4);
		tbb.order(ByteOrder.nativeOrder());
		myTexture=tbb.asFloatBuffer();
		myTexture.put(textures);
		myTexture.position(0);
	}
	
	public void drawSelf(GL10 gl)
	{
		gl.glPushMatrix();
		gl.glRotatef(mAngleX, 1, 0, 0);//��ת
		
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);//�򿪶��㻺��
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, myVertexBuffer);//ָ�����㻺��
		
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, myTexture);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
		
		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, vCount);//����ͼ��
		gl.glPopMatrix();
	}
	
    //�Զ��з����������������ķ���
    public float[] generateTexCoor(int bw,int bh)
    {
    	float[] result=new float[bw*bh*6*2]; 
    	float sizew=32.0f/bw;//����
    	float sizeh=32.0f/bh;//����
    	int c=0;  
    	for(int i=0;i<bh;i++)
    	{
    		for(int j=0;j<bw;j++)
    		{
    			//ÿ����һ�����Σ������������ι��ɣ��������㣬12����������
    			float s=j*sizew;
    			float t=i*sizeh;
    			
    			result[c++]=s;
    			result[c++]=t;
    		
    			result[c++]=s;
    			result[c++]=t+sizeh;
    			
    			result[c++]=s+sizew;
    			result[c++]=t;
    			   			
    			result[c++]=s;
    			result[c++]=t+sizeh;
    			
    			result[c++]=s+sizew;
    			result[c++]=t+sizeh;   
    			
    			result[c++]=s+sizew;
    			result[c++]=t;
    		}
    	}
    	return result;
    }
}