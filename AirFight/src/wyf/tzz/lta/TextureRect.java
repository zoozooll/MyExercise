package wyf.tzz.lta;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;
//�������
public class TextureRect {
	private FloatBuffer   mVertexBuffer;//�����������ݻ���
    private FloatBuffer   mTextureBuffer;	//������ɫ���ݻ���
    int vCount;								//��������
    int texId;								//����ID
    
    float x;								//�÷�x����
	float y;								//�÷�y����
	float z;								//�÷�z����
	boolean isVisible=true;					//�Ƿ�ɼ��ı�־
    
    public TextureRect(int texId,float width,float height)
    {
    	this.texId=texId;    				//����ID
        vCount=6;							//��������
        float vertices[]=new float[]		//������������
        {
        	-1*width,1*height,0,
        	-1*width,-1*height,0,
        	1*width,1*height,0,
        	
        	-1*width,-1*height,0,
        	1*width,-1*height,0,
        	1*width,1*height,0
        };
		
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);	//��Ϊһ�������ĸ��ֽ�
        vbb.order(ByteOrder.nativeOrder());			//�����ֽ�˳��
        mVertexBuffer = vbb.asFloatBuffer();		//ת��Ϊint�ͻ���
        mVertexBuffer.put(vertices);				//�򻺳����з��붥����������
        mVertexBuffer.position(0);					//���û�������ʼλ��
        
        float textures[]=new float[]				//������������
        {
        	0,0,0,1,1,0,
        	0,1,1,1,1,0
        };
        ByteBuffer tbb = ByteBuffer.allocateDirect(textures.length*4);
        tbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
        mTextureBuffer= tbb.asFloatBuffer();//ת��ΪFloat�ͻ���
        mTextureBuffer.put(textures);//�򻺳����з��붥����ɫ����
        mTextureBuffer.position(0);//���û�������ʼλ��      
    }

    public void drawSelf(GL10 gl) {        
    	if(!isVisible){											//������ɼ����򲻻���
    		return;
    	}
    	gl.glTranslatef(x, y, z);								//ƽ��
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);			//���ö�����������
        gl.glVertexPointer(3,GL10.GL_FLOAT,	0, mVertexBuffer);	//Ϊ����ָ��������������
        gl.glEnable(GL10.GL_TEXTURE_2D);    					//��������
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);	//����ʹ������ST���껺��
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTextureBuffer);//Ϊ����ָ������ST���껺��
        gl.glBindTexture(GL10.GL_TEXTURE_2D, texId); 			//�󶨵�ǰ����
        gl.glDrawArrays(GL10.GL_TRIANGLES, 0,vCount); 			//����ͼ��
    }
}
