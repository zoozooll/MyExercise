
package wyf.tzz.lta;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;
//�������
public class Plane_BackWing {
	private FloatBuffer   mVertexBuffer;//�����������ݻ���
    private FloatBuffer   mTextureBuffer;//������ɫ���ݻ���
    int vCount ;
    int texId;
    float mAngleX ;
    
    public Plane_BackWing(int texId,float width,float height,float length)
    {
    	this.texId=texId;
    	
    	//�����������ݵĳ�ʼ��================begin============================
        float vertices[]=new float[]
        {
        		//�ϱ���
        		0,height,length,-width,height,length,-13.0f/10.0f*width,2.0f/5.0f*height,length,//OAB        		
        		
        		0,height,length,-13.0f/10.0f*width,2.0f/5.0f*height,length,-width,-height,length,//OBC
        		
        		0,height,length,-width,-height,length,width,-height,length,//OCD
        		
        		0,height,length,width,-height,length,13.0f/10.0f*width,2.0f/5.0f*height,length,//ODE

        		0,height,length,13.0f/10.0f*width,2.0f/5.0f*height,length,width,height,length,//OEF
        		
        		//�±���
        		0,height,-length,-width,height,-length,-13.0f/10.0f*width,2.0f/5.0f*height,-length,//OAB        		
        		
        		0,height,-length,-13.0f/10.0f*width,2.0f/5.0f*height,-length,-width,-height,-length,//OBC
        		
        		0,height,-length,-width,-height,-length,width,-height,-length,//OCD
        		
        		0,height,-length,width,-height,-length,13.0f/10.0f*width,2.0f/5.0f*height,-length,//ODE

        		0,height,-length,13.0f/10.0f*width,2.0f/5.0f*height,-length,width,height,-length,//OEF
     
        		-width,height,length,-width,height,-length,-13.0f/10.0f*width,2.0f/5.0f*height,-length,
        		0-width,height,length,-13.0f/10.0f*width,2.0f/5.0f*height,-length,-13.0f/10.0f*width,2.0f/5.0f*height,length,
        		-13.0f/10.0f*width,2.0f/5.0f*height,length,-13.0f/10.0f*width,2.0f/5.0f*height,-length,-width,-height,-length,
        		-13.0f/10.0f*width,2.0f/5.0f*height,length,-width,-height,-length,-width,-height,length,
        		-width,-height,length,-width,-height,-length,width,-height,-length,
        		-width,-height,length,width,-height,-length,width,-height,length,
        		width,-height,length,width,-height,-length,13.0f/10.0f*width,2.0f/5.0f*height,-length,
        		width,-height,length,13.0f/10.0f*width,2.0f/5.0f*height,-length,13.0f/10.0f*width,2.0f/5.0f*height,length,
        		13.0f/10.0f*width,2.0f/5.0f*height,length,13.0f/10.0f*width,2.0f/5.0f*height,-length,width,height,-length,
        		13.0f/10.0f*width,2.0f/5.0f*height,length,width,height,-length,width,height,length,
        		width,height,length,width,height,-length,-width,height,-length,
        		width,height,length,width,height,-length,0,height,-length,
        		width,height,length,0,height,-length,0,height,length,
        		0,height,length,0,height,-length,-width,height,-length,
        		0,height,length, -width,height,-length,	-width,height,length,	
        };
        
        vCount=vertices.length/3;
		
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
        		0.18f,0.0f,0.027f,0.0080f,0.0f,0.027f,
        		0.18f,0.0f,0.0f,0.027f,0.0f,0.09f,
        		0.18f,0.0f,0.0f,0.09f,0.035f,0.109f,
        		0.18f,0.0f,0.035f,0.109f,0.145f,0.109f,
        		0.18f,0.0f,0.145f,0.109f,0.168f,0.074f,
        		0.18f,0.0f,0.168f,0.074f,0.211f,0.07f,
        		
        	//�±���
        		0.18f,0.0f,0.027f,0.0080f,0.0f,0.027f,
        		0.18f,0.0f,0.0f,0.027f,0.0f,0.09f,
        		0.18f,0.0f,0.0f,0.09f,0.035f,0.109f,
        		0.18f,0.0f,0.035f,0.109f,0.145f,0.109f,
        		0.18f,0.0f,0.145f,0.109f,0.168f,0.074f,
        		0.18f,0.0f,0.168f,0.074f,0.211f,0.07f,
        	//����
        		0.168f,0.043f,0.152f,0.09f,0.223f,0.074f,0.168f,0.043f,0.152f,0.09f,0.223f,0.074f,
        		0.168f,0.043f,0.152f,0.09f,0.223f,0.074f,0.168f,0.043f,0.152f,0.09f,0.223f,0.074f,
        		0.168f,0.043f,0.152f,0.09f,0.223f,0.074f,0.168f,0.043f,0.152f,0.09f,0.223f,0.074f,
        		0.168f,0.043f,0.152f,0.09f,0.223f,0.074f,0.168f,0.043f,0.152f,0.09f,0.223f,0.074f,
        		0.168f,0.043f,0.152f,0.09f,0.223f,0.074f,0.168f,0.043f,0.152f,0.09f,0.223f,0.074f,
        		0.168f,0.043f,0.152f,0.09f,0.223f,0.074f,0.168f,0.043f,0.152f,0.09f,0.223f,0.074f,   		
        		0.168f,0.043f,0.152f,0.09f,0.223f,0.074f,0.168f,0.043f,0.152f,0.09f,0.223f,0.074f, 
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
