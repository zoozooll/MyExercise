package wyf.tzz.lta;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;

// ��ʾ����������ε���
public class ScoreRect {
	private FloatBuffer   mVertexBuffer;//�����������ݻ���
    private FloatBuffer   mTextureBuffer;//������ɫ���ݻ���
    int vCount;							//��������
    int texId;							//����ID
    
    public ScoreRect(int texId,float X_UNIT_SIZE,float Y_UNIT_SIZE,float[] textures){
    	this.texId=texId;
        vCount=6;								//��������
        float vertices[]=new float[]			//������������
        {
        	-1*X_UNIT_SIZE,1*Y_UNIT_SIZE,0,
        	-1*X_UNIT_SIZE,-1*Y_UNIT_SIZE,0,
        	1*X_UNIT_SIZE,1*Y_UNIT_SIZE,0,
        	
        	-1*X_UNIT_SIZE,-1*Y_UNIT_SIZE,0,
        	1*X_UNIT_SIZE,-1*Y_UNIT_SIZE,0,
        	1*X_UNIT_SIZE,1*Y_UNIT_SIZE,0
        };
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);	//��Ϊһ�������ĸ��ֽ�
        vbb.order(ByteOrder.nativeOrder());			//�����ֽ�˳��
        mVertexBuffer = vbb.asFloatBuffer();		//ת��Ϊint�ͻ���
        mVertexBuffer.put(vertices);				//�򻺳����з��붥����������
        mVertexBuffer.position(0);					//���û�������ʼλ��
       
        ByteBuffer tbb = ByteBuffer.allocateDirect(textures.length*4);
        tbb.order(ByteOrder.nativeOrder());			//�����ֽ�˳��
        mTextureBuffer= tbb.asFloatBuffer();		//ת��ΪFloat�ͻ���
        mTextureBuffer.put(textures);				//�򻺳����з��붥����ɫ����
        mTextureBuffer.position(0);					//���û�������ʼλ��
    }

    public void drawSelf(GL10 gl) {        
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);			//���ö�����������
        gl.glVertexPointer(3,GL10.GL_FLOAT,	0, mVertexBuffer);	//ָ��������������
        gl.glEnable(GL10.GL_TEXTURE_2D);    					//��������
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);	//����ʹ������ST���껺��
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTextureBuffer);//Ϊ����ָ������ST���껺��
        gl.glBindTexture(GL10.GL_TEXTURE_2D, texId); 			//�󶨵�ǰ����
        gl.glDrawArrays(GL10.GL_TRIANGLES, 0,vCount); 			//����ͼ��
    }
}
