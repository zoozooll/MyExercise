	package wyf.tzz.lta;
	import java.nio.ByteBuffer;
	import java.nio.ByteOrder;
	import java.nio.FloatBuffer;
	import javax.microedition.khronos.opengles.GL10;
	
	//����Ļ���
	public class Plane_TopWing {
		private FloatBuffer   mVertexBuffer;//�����������ݻ���
	    private FloatBuffer   mTextureBuffer;//������ɫ���ݻ���
	    float mAngleX;
	    float mAngleY;
	    int vCount = 42;
	    int texId; 
	    
	    public Plane_TopWing(int texId,float width,float height,float length)
	    {
	    	this.texId=texId;
	    	
	    	//�����������ݵĳ�ʼ��================begin============================
	        float vertices[]=new float[]
	        {
	        	
	        	-5.0f/6.0f*width,4.0f/3.0f*height,0,//A
	        	-width,height,-length,//B
	        	-width,height,length,//C
	        	
	        	-5.0f/6.0f*width,4.0f/3.0f*height,0,//A
	        	-width,height,length,//C
	        	width,height,length,//D
	        	
	        	-5.0f/6.0f*width,4.0f/3.0f*height,0,//A
	        	width,height,length,//D
	        	5.0f/6.0f*width,4.0f/3.0f*height,0,//O
	        	
	        	5.0f/6.0f*width,4.0f/3.0f*height,0,//O
	        	width,height,length,//D
	        	width,height,-length,//E
	        	
	        	5.0f/6.0f*width,4.0f/3.0f*height,0,//O
	        	width,height,-length,//E
	        	-width,height,-length,//B
	        	
	        	5.0f/6.0f*width,4.0f/3.0f*height,0,//O
	        	-width,height,-length,//B
	        	-5.0f/6.0f*width,4.0f/3.0f*height,0,//A
	        	
	        	-width,height,length,//C
	        	-width,0,6.0f/5.0f*length,//F
	        	width,0,6.0f/5.0f*length,//G
	        	
	        	-width,height,length,//C
	        	width,0,6.0f/5.0f*length,//G
	        	width,height,length,//D
	        	
	        	width,height,length,//D
	        	width,0,6.0f/5.0f*length,//G
	        	width,0,-6.0f/5.0f*length,//H
	        	
	        	width,height,length,//D
	        	width,0,-6.0f/5.0f*length,//H
	        	width,height,-length,//E
	        	
	        	width,height,-length,//E
	        	width,0,-6.0f/5.0f*length,//H
	        	-width,0,-6.0f/5.0f*length,//I
	        	
	        	width,height,-length,//E
	        	-width,0,-6.0f/5.0f*length,//I
	        	-width,height,-length,//B
	        	
	        	-width,height,-length,//B
	        	-width,0,-6.0f/5.0f*length,//I
	        	-width,0,6.0f/5.0f*length,//F
	        	
	        	-width,height,-length,//B
	        	-width,0,6.0f/5.0f*length,//F
	        	-width,height,length,//C
	        };
			
	        //���������������ݻ���
	        //vertices.length*4����Ϊһ�������ĸ��ֽ�
	        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
	        vbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
	        mVertexBuffer = vbb.asFloatBuffer();//ת��Ϊint�ͻ���
	        mVertexBuffer.put(vertices);//�򻺳����з��붥����������
	        mVertexBuffer.position(0);//���û�������ʼλ��
	        
	        float textures[]=new float[]
	        {
	        	0.133f,	0.211f,0.242f,0.492f,0.555f,0.289f,0.133f,	0.211f,0.242f,0.492f,0.555f,0.289f,
	        	0.133f,	0.211f,0.242f,0.492f,0.555f,0.289f,0.133f,	0.211f,0.242f,0.492f,0.555f,0.289f,
	        	0.133f,	0.211f,0.242f,0.492f,0.555f,0.289f,0.133f,	0.211f,0.242f,0.492f,0.555f,0.289f,
	        	0.133f,	0.211f,0.242f,0.492f,0.555f,0.289f,0.133f,	0.211f,0.242f,0.492f,0.555f,0.289f,
	        	0.133f,	0.211f,0.242f,0.492f,0.555f,0.289f,0.133f,	0.211f,0.242f,0.492f,0.555f,0.289f,
	        	0.133f,	0.211f,0.242f,0.492f,0.555f,0.289f,0.133f,	0.211f,0.242f,0.492f,0.555f,0.289f,
	        	0.133f,	0.211f,0.242f,0.492f,0.555f,0.289f,0.133f,	0.211f,0.242f,0.492f,0.555f,0.289f,
	        	0.133f,	0.211f,0.242f,0.492f,0.555f,0.289f,0.133f,	0.211f,0.242f,0.492f,0.555f,0.289f,
	        };

	        //���������������ݻ���
	        ByteBuffer tbb = ByteBuffer.allocateDirect(textures.length*4);
	        tbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
	        mTextureBuffer= tbb.asFloatBuffer();//ת��ΪFloat�ͻ���
	        mTextureBuffer.put(textures);//�򻺳����з��붥����ɫ����
	        mTextureBuffer.position(0);//���û�������ʼλ��
	    }

	    public void drawSelf(GL10 gl)
	    {        
	    	gl.glRotatef(mAngleY, 0, 1, 0);
	    	gl.glRotatef(mAngleX, 1, 0, 0);
	        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);//���ö�����������
	        
			//Ϊ����ָ��������������
	        gl.glVertexPointer
	        (
	        		3,				//ÿ���������������Ϊ3  xyz 
	        		GL10.GL_FLOAT,	//��������ֵ������Ϊ GL_FIXED
	        		0, 				//����������������֮��ļ��
	        		mVertexBuffer	//������������
	        );
			
	        //��������
	        gl.glEnable(GL10.GL_TEXTURE_2D);   
	        //����ʹ������ST���껺��
	        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	        //Ϊ����ָ������ST���껺��
	        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTextureBuffer);
	        //�󶨵�ǰ����
	        gl.glBindTexture(GL10.GL_TEXTURE_2D, texId);
			
	        //����ͼ��
	        gl.glDrawArrays
	        (
	        		GL10.GL_TRIANGLES, 		//�������η�ʽ���
	        		0,
	        		vCount
	        );
	        
	       
	    }
	}
