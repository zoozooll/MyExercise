package wyf.tzz.lta;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;
//�������
public class Plane_Wing {
	private FloatBuffer   mVertexBuffer;//�����������ݻ���
    private FloatBuffer   mTextureBuffer;//������ɫ���ݻ���
    int vCount;
    int texId;
    float mAngleX ;
    
    public Plane_Wing(int texId,float width,float height,float length)
    {
    	this.texId=texId;
    	
    	//�����������ݵĳ�ʼ��================begin============================
        vCount=144;
        float vertices[]=new float[]
        {
        		
        		//�ϱ���
        		0,height,length,-width,height,length,-17.0f/15.0f*width,3.0f/5.0f*height,length,//OAB        		
        		
        		0,height,length,-17.0f/15.0f*width,3.0f/5.0f*height,length,-17.0f/15.0f*width,-3.0f/5.0f*height,length,//OBC
        		
        		0,height,length,-17.0f/15.0f*width,-3.0f/5.0f*height,length,-width,-height,length,//OCD
        		
        		0,height,length,-width,-height,length,-1.0f/3.0f*width,-height,length,//ODE

        		0,height,length,-1.0f/3.0f*width,-height,length,-2.0f/15.0f*width,-2.0f/5.0f*height,length,//OEF
        		
        		0,height,length,-2.0f/15.0f*width,-2.0f/5.0f*height,length,2.0f/15.0f*width,-2.0f/5.0f*height,length,//OFG
        		
        		0,height,length,2.0f/15.0f*width,-2.0f/5.0f*height,length,1.0f/3.0f*width,-height,length,//OGH
        		
        		0,height,length,1.0f/3.0f*width,-height,length,width,-height,length,//OHI        		
        		
        		0,height,length,width,-height,length,17.0f/15.0f*width,-3.0f/5.0f*height,length,//OIJ
        		
        		0,height,length,17.0f/15.0f*width,-3.0f/5.0f*height,length,17.0f/15.0f*width,3.0f/5.0f*height,length,//OJK
        		
        		0,height,length,17.0f/15.0f*width,3.0f/5.0f*height,length,width,height,length,//OKL
        		
        		//�±���
        		0,height,-length,-width,height,-length,-17.0f/15.0f*width,3.0f/5.0f*height,-length,//OAB        		
        		
        		0,height,-length,-17.0f/15.0f*width,3.0f/5.0f*height,-length,-17.0f/15.0f*width,-3.0f/5.0f*height,-length,//OBC
        		
        		0,height,-length,-17.0f/15.0f*width,-3.0f/5.0f*height,-length,-width,-height,-length,//OCD
        		
        		0,height,-length,-width,-height,-length,-1.0f/3.0f*width,-height,-length,//ODE

        		0,height,-length,-1.0f/3.0f*width,-height,-length,-2.0f/15.0f*width,-2.0f/5.0f*height,-length,//OEF
        		
        		0,height,-length,-2.0f/15.0f*width,-2.0f/5.0f*height,-length,2.0f/15.0f*width,-2.0f/5.0f*height,-length,//OFG
        		
        		0,height,-length,2.0f/15.0f*width,-2.0f/5.0f*height,-length,1.0f/3.0f*width,-height,-length,//OGH
        		
        		0,height,-length,1.0f/3.0f*width,-height,-length,width,-height,-length,//OHI        		
        		
        		0,height,-length,width,-height,-length,17.0f/15.0f*width,-3.0f/5.0f*height,-length,//OIJ
        		
        		0,height,-length,17.0f/15.0f*width,-3.0f/5.0f*height,-length,17.0f/15.0f*width,3.0f/5.0f*height,-length,//OJK
        		
        		0,height,-length,17.0f/15.0f*width,3.0f/5.0f*height,-length,width,height,-length,//OKL
        		
        		//����
        		-width,height,length,-width,height,-length,-17.0f/15.0f*width,3.0f/5.0f*height,-length,//A//A1//B1        		
        		
        		-width,height,length,-17.0f/15.0f*width,3.0f/5.0f*height,-length,-17.0f/15.0f*width,3.0f/5.0f*height,length,//A//B1//B        		
        		
        		-17.0f/15.0f*width,3.0f/5.0f*height,length,-17.0f/15.0f*width,3.0f/5.0f*height,-length,-17.0f/15.0f*width,-3.0f/5.0f*height,-length,//B//B1//C1
        		
        		-17.0f/15.0f*width,3.0f/5.0f*height,length,-17.0f/15.0f*width,-3.0f/5.0f*height,-length,-17.0f/15.0f*width,-3.0f/5.0f*height,length,//B//C1//C
        		
        		-17.0f/15.0f*width,-3.0f/5.0f*height,length,-17.0f/15.0f*width,-3.0f/5.0f*height,-length,-width,-height,-length,//CC1D1
        		
        		-17.0f/15.0f*width,-3.0f/5.0f*height,length,-width,-height,-length,-width,-height,length,//C//D1//D
        		
        		-width,-height,length,-width,-height,-length,-1.0f/3.0f*width,-height,-length,//D//D1//E1        		
        		
        		-width,-height,length,-1.0f/3.0f*width,-height,-length,-1.0f/3.0f*width,-height,length,//D//E1//E\\		
        		
        		-1.0f/3.0f*width,-height,length,-1.0f/3.0f*width,-height,-length,-2.0f/15.0f*width,-2.0f/5.0f*height,-length,//E\\//E1//F1
        		
        		-1.0f/3.0f*width,-height,length,-2.0f/15.0f*width,-2.0f/5.0f*height,-length,-2.0f/15.0f*width,-2.0f/5.0f*height,length,//E\\//F1//F
        		
        		-2.0f/15.0f*width,-2.0f/5.0f*height,length,-2.0f/15.0f*width,-2.0f/5.0f*height,-length,2.0f/15.0f*width,-2.0f/5.0f*height,-length,//F//F1//G1
        		
        		-2.0f/15.0f*width,-2.0f/5.0f*height,length,2.0f/15.0f*width,-2.0f/5.0f*height,-length,2.0f/15.0f*width,-2.0f/5.0f*height,length,//F//G1//G
        		
        		2.0f/15.0f*width,-2.0f/5.0f*height,length,2.0f/15.0f*width,-2.0f/5.0f*height,-length,1.0f/3.0f*width,-height,-length,//G//G1//H1
        		
        		2.0f/15.0f*width,-2.0f/5.0f*height,length,1.0f/3.0f*width,-height,-length,1.0f/3.0f*width,-height,length,//G//H1//H

        		1.0f/3.0f*width,-height,length,1.0f/3.0f*width,-height,-length,width,-height,-length,//H//H1//I1        		
        		
        		1.0f/3.0f*width,-height,length,width,-height,-length,width,-height,length,//H//I1//I
        	
        		width,-height,length,width,-height,-length,17.0f/15.0f*width,-3.0f/5.0f*height,-length,//I//I1//J1	
        		
        		width,-height,length,17.0f/15.0f*width,-3.0f/5.0f*height,-length,17.0f/15.0f*width,-3.0f/5.0f*height,length,//I//J1//J

        		17.0f/15.0f*width,-3.0f/5.0f*height,length,17.0f/15.0f*width,-3.0f/5.0f*height,-length,17.0f/15.0f*width,3.0f/5.0f*height,-length,//J//J1//K1
	
        		17.0f/15.0f*width,-3.0f/5.0f*height,length,17.0f/15.0f*width,3.0f/5.0f*height,-length,17.0f/15.0f*width,3.0f/5.0f*height,length,//J//K1//K

        		17.0f/15.0f*width,3.0f/5.0f*height,length,17.0f/15.0f*width,3.0f/5.0f*height,-length,width,height,-length,//K//K1//L1
  		
        		17.0f/15.0f*width,3.0f/5.0f*height,length,width,height,-length,width,height,length,//K//L1//L
  
        		width,height,length,width,height,-length,0,height,-length,//L//L1//O1
      		
        		width,height,length,0,height,-length,0,height,length,//L//o1//O 
        		
        		0,height,length,0,height,-length,0,height,-length,//oo1a1
        		
        		0,height,length,0,height,-length,0,height,length,//oo1a
        		
        };
		
        //���������������ݻ���
        //vertices.length*4����Ϊһ�������ĸ��ֽ�
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
        mVertexBuffer = vbb.asFloatBuffer();//ת��Ϊint�ͻ���
        mVertexBuffer.put(vertices);//�򻺳����з��붥����������
        mVertexBuffer.position(0);//���û�������ʼλ��
        //�ر���ʾ�����ڲ�ͬƽ̨�ֽ�˳��ͬ���ݵ�Ԫ�����ֽڵ�һ��Ҫ����ByteBuffer
        //ת�����ؼ���Ҫͨ��ByteOrder����nativeOrder()�������п��ܻ������
        //�����������ݵĳ�ʼ��================end============================
        
        //�����������ݵĳ�ʼ��================begin============================
        float textures[]=new float[]
        {
        	
        	//�ϱ���															
        		0.488f,0.0040f,0.051f,0.0080f,0.0f,0.051f,
        		0.488f,0.0040f,0.0f,0.051f,0.0f,0.203f,
        		0.488f,0.0040f,0.0f,0.203f,0.055f,0.23f,
        		0.488f,0.0040f,0.055f,0.23f,0.344f,0.223f,
        		0.488f,0.0040f,0.344f,0.223f,0.391f,0.168f,
        		0.488f,0.0040f,0.391f,0.168f,0.582f,0.164f,
        		0.488f,0.0040f,0.582f,0.164f,0.664f,0.211f,
        		0.488f,0.0040f,0.664f,0.211f,0.938f,0.203f,
        		0.488f,0.0040f,0.938f,0.203f,0.98f,0.164f,
        		0.488f,0.0040f,0.98f,0.164f,0.984f,0.059f,
        		0.488f,0.0040f,0.984f,0.059f,0.914f,0.0040f,
        		
        	//�±���
        		0.0040f,0.277f,0.0040f,0.391f,0.137f,0.352f,
        		0.0040f,0.277f,0.0040f,0.391f,0.137f,0.352f,
        		0.0040f,0.277f,0.0040f,0.391f,0.137f,0.352f,
        		0.0040f,0.277f,0.0040f,0.391f,0.137f,0.352f,
        		0.0040f,0.277f,0.0040f,0.391f,0.137f,0.352f,
        		0.0040f,0.277f,0.0040f,0.391f,0.137f,0.352f,
        		0.0040f,0.277f,0.0040f,0.391f,0.137f,0.352f,
        		0.0040f,0.277f,0.0040f,0.391f,0.137f,0.352f,
        		0.0040f,0.277f,0.0040f,0.391f,0.137f,0.352f,
        		0.0040f,0.277f,0.0040f,0.391f,0.137f,0.352f,
        		0.0040f,0.277f,0.0040f,0.391f,0.137f,0.352f,
        		
        	//����
        		0.0040f,0.277f,0.0040f,0.391f,0.137f,0.352f,
        		0.0040f,0.277f,0.0040f,0.391f,0.137f,0.352f,
        		0.0040f,0.277f,0.0040f,0.391f,0.137f,0.352f,
        		0.0040f,0.277f,0.0040f,0.391f,0.137f,0.352f,
        		0.0040f,0.277f,0.0040f,0.391f,0.137f,0.352f,
        		0.0040f,0.277f,0.0040f,0.391f,0.137f,0.352f,
        		0.0040f,0.277f,0.0040f,0.391f,0.137f,0.352f,
        		0.0040f,0.277f,0.0040f,0.391f,0.137f,0.352f,
        		0.0040f,0.277f,0.0040f,0.391f,0.137f,0.352f,
        		0.0040f,0.277f,0.0040f,0.391f,0.137f,0.352f,
        		0.0040f,0.277f,0.0040f,0.391f,0.137f,0.352f,
        		0.0040f,0.277f,0.0040f,0.391f,0.137f,0.352f,
        		0.0040f,0.277f,0.0040f,0.391f,0.137f,0.352f,
        		0.0040f,0.277f,0.0040f,0.391f,0.137f,0.352f,
        		0.0040f,0.277f,0.0040f,0.391f,0.137f,0.352f,
        		0.0040f,0.277f,0.0040f,0.391f,0.137f,0.352f,
        		0.0040f,0.277f,0.0040f,0.391f,0.137f,0.352f,
        		0.0040f,0.277f,0.0040f,0.391f,0.137f,0.352f,
        		0.0040f,0.277f,0.0040f,0.391f,0.137f,0.352f,
        		0.0040f,0.277f,0.0040f,0.391f,0.137f,0.352f,
        		0.0040f,0.277f,0.0040f,0.391f,0.137f,0.352f,
        		0.0040f,0.277f,0.0040f,0.391f,0.137f,0.352f,
        		0.0040f,0.277f,0.0040f,0.391f,0.137f,0.352f,
        		0.0040f,0.277f,0.0040f,0.391f,0.137f,0.352f,
        		0.0040f,0.277f,0.0040f,0.391f,0.137f,0.352f,
        		0.0040f,0.277f,0.0040f,0.391f,0.137f,0.352f,  
        };

        
        //���������������ݻ���
        ByteBuffer tbb = ByteBuffer.allocateDirect(textures.length*4);
        tbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
        mTextureBuffer= tbb.asFloatBuffer();//ת��ΪFloat�ͻ���
        mTextureBuffer.put(textures);//�򻺳����з��붥����ɫ����
        mTextureBuffer.position(0);//���û�������ʼλ��
        //�ر���ʾ�����ڲ�ͬƽ̨�ֽ�˳��ͬ���ݵ�Ԫ�����ֽڵ�һ��Ҫ����ByteBuffer
        //ת�����ؼ���Ҫͨ��ByteOrder����nativeOrder()�������п��ܻ������
        //�����������ݵĳ�ʼ��================end============================
    }

    public void drawSelf(GL10 gl)
    {        
    	
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
