package wyf.tzz.lta;
import javax.microedition.khronos.opengles.GL10;
import static wyf.tzz.lta.Constant.*;
//��ʾ��þ�����������
public class Score {						
	ScoreRect[] numbers=new ScoreRect[10];		//���������������
	int goal=0;									//hero�÷���
	
	public Score(int texId){					//����0-9ʮ�����ֵ��������
		for(int i=0;i<10;i++){
			numbers[i]=new ScoreRect
            (
            	 texId,								//����ͼƬID
            	 ICON_WIDTH*0.7f/2,					//ͼƬ���
            	 ICON_HEIGHT*0.7f,					//ͼƬ�߶�
           		 new float[]						//������������
		             {
		           	  0.1f*i,0, 0.1f*i,1, 0.1f*(i+1),0,
		           	  0.1f*i,1, 0.1f*(i+1),1,  0.1f*(i+1),0
		             }
             ); 
		}
	}
	
	public void drawSelf(GL10 gl)
	{		
		String scoreStr=goal+"";						//���÷���תΪString����
		gl.glPushMatrix();								//���浱ǰ����
		for(int i=0;i<scoreStr.length();i++)
		{
			char c=scoreStr.charAt(i);					//���÷��е�ÿ�������ַ�����
			gl.glTranslatef(ICON_WIDTH*0.7f, 0, 0);		//��x����ƽ��
			numbers[c-'0'].drawSelf(gl);				//����ÿ������
		}
		gl.glPopMatrix();								//�ظ�֮ǰ����
	}
}
