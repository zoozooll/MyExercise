package wyf.tzz.lta;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import javax.microedition.khronos.opengles.GL10;

public class DrawSpheroid {
	private FloatBuffer  mVertexBuffer;//�����������ݻ���
	private FloatBuffer mTextureBuffer;//������
    public float mAngleX;//��x����ת�Ƕ�
    public float mAngleY;//��y����ת�Ƕ� 
    public float mAngleZ;//��z����ת�Ƕ� 
    int vCount=0;
    int textureId;
    
    float a;
    float b;		//����뾶��
    float c;
    float angleSpan;//������е�λ�зֵĽǶ�
    float hAngleBegin;//���Ȼ�����ʼ�Ƕ�
    float hAngleOver;//���Ȼ��ƽ����Ƕ�
    float vAngleBegin;//γ�Ȼ�����ʼ�Ƕ�
    float vAngleOver;//γ�Ȼ��ƽ����Ƕ�
    
    //hAngle��ʾ���ȣ�vAngle��ʾγ�ȡ�
    public DrawSpheroid(float a,float b,float c,float angleSpan,
    					float hAngleBegin,float hAngleOver,float vAngleBegin,float vAngleOver,int textureId)
    {	
    	this.a=a;
    	this.b=b;
    	this.c=c;
    	this.hAngleBegin=hAngleBegin;
    	this.hAngleOver=hAngleOver;
    	this.vAngleBegin=vAngleBegin;
    	this.vAngleOver=vAngleOver;
    	this.textureId=textureId;

    	ArrayList<Float> alVertix=new ArrayList<Float>();//��Ŷ�������
    	
        for(float vAngle=vAngleBegin;vAngle<vAngleOver;vAngle=vAngle+angleSpan)//��ֱ����angleSpan��һ��
        {
        	for(float hAngle=hAngleBegin;hAngle<hAngleOver;hAngle=hAngle+angleSpan)//ˮƽ����angleSpan��һ��
        	{//����������һ���ǶȺ�����Ӧ�Ĵ˵��������ϵ�����    		
        		float x1=(float)(a*Math.cos(Math.toRadians(vAngle))*Math.cos(Math.toRadians(hAngle)));                          
        		float y1=(float)(b*Math.cos(Math.toRadians(vAngle))*Math.sin(Math.toRadians(hAngle)));
        		float z1=(float)(c*Math.sin(Math.toRadians(vAngle)));
        		
        		float x2=(float)(a*Math.cos(Math.toRadians(vAngle+angleSpan))*Math.cos(Math.toRadians(hAngle)));
        		float y2=(float)(b*Math.cos(Math.toRadians(vAngle+angleSpan))*Math.sin(Math.toRadians(hAngle)));
        		float z2=(float)(c*Math.sin(Math.toRadians(vAngle+angleSpan)));
        		
        		float x3=(float)(a*Math.cos(Math.toRadians(vAngle+angleSpan))*Math.cos(Math.toRadians(hAngle+angleSpan)));
        		float y3=(float)(b*Math.cos(Math.toRadians(vAngle+angleSpan))*Math.sin(Math.toRadians(hAngle+angleSpan)));
        		float z3=(float)(c*Math.sin(Math.toRadians(vAngle+angleSpan)));
        		
        		float x4=(float)(a*Math.cos(Math.toRadians(vAngle))*Math.cos(Math.toRadians(hAngle+angleSpan)));
        		float y4=(float)(b*Math.cos(Math.toRadians(vAngle))*Math.sin(Math.toRadians(hAngle+angleSpan)));
        		float z4=(float)(c*Math.sin(Math.toRadians(vAngle)));
        		
        		//�����������XYZ��������Ŷ��������ArrayList
        		alVertix.add(x1);alVertix.add(y1);alVertix.add(z1);
        		alVertix.add(x2);alVertix.add(y2);alVertix.add(z2);
        		alVertix.add(x4);alVertix.add(y4);alVertix.add(z4);
        		
        		alVertix.add(x4);alVertix.add(y4);alVertix.add(z4);
        		alVertix.add(x2);alVertix.add(y2);alVertix.add(z2);
        		alVertix.add(x3);alVertix.add(y3);alVertix.add(z3); 
        	}
        } 	
        vCount=alVertix.size()/3;//���������Ϊ����ֵ������1/3����Ϊһ��������3������
    	
        //��alVertix�е�����ֵת�浽һ��int������
        float[] vertices=new float[vCount*3];
    	for(int i=0;i<alVertix.size();i++)
    	{
    		vertices[i]=alVertix.get(i);
    	}
        //���������������ݻ���
        //vertices.length*4����Ϊһ�������ĸ��ֽ�
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
        mVertexBuffer = vbb.asFloatBuffer();//ת��Ϊint�ͻ���
        mVertexBuffer.put(vertices);//�򻺳����з��붥����������
        mVertexBuffer.position(0);//���û�������ʼλ��

		//����
						
    	//��ȡ�з���ͼ����������
    	float[] texCoorArray= 
         generateTexCoor
    	 (
    			 (int)(180/angleSpan), //����ͼ�зֵ�����
    			 (int)(180/angleSpan)  //����ͼ�зֵ����� 
    	);
		
		ByteBuffer tbb=ByteBuffer.allocateDirect(texCoorArray.length*4);
		tbb.order(ByteOrder.nativeOrder());
		mTextureBuffer=tbb.asFloatBuffer();
		mTextureBuffer.put(texCoorArray);
		mTextureBuffer.position(0);
    }

    public void drawSelf(GL10 gl)
    {    	
    	gl.glRotatef(mAngleZ, 0, 0, 1);//��Z����ת    	
        gl.glRotatef(mAngleY, 0, 1, 0);//��Y����ת
        gl.glRotatef(mAngleX, 1, 0, 0);//��X����ת
        
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        
		//Ϊ����ָ��������������
        gl.glVertexPointer
        (
        		3,				//ÿ���������������Ϊ3   
        		GL10.GL_FLOAT,	//��������ֵ������
        		0, 				//����������������֮��ļ��
        		mVertexBuffer	//������������
        );
        
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTextureBuffer);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
		
        
		gl.glEnable(GL10.GL_DEPTH_TEST); 			//������Ȳ���
		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, vCount);//����ͼ��
    }
    
    //�Զ��з����������������ķ���
    public float[] generateTexCoor(int bw,int bh)
    {
    	float[] result=new float[bw*bh*6*2]; 
    	float sizew=1.0f/bw;//����
    	float sizeh=1.0f/bh;//����
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
    			
    			
    			result[c++]=s+sizew;
    			result[c++]=t;
    			
    			result[c++]=s;
    			result[c++]=t+sizeh;
    			
    			result[c++]=s+sizew;
    			result[c++]=t+sizeh;    			
    		}
    	}
    	return result;
    }  
    

}
