package wyf.tzz.lta;

import static wyf.tzz.lta.MenuSurfaceView.*;
//�˵������߳�
public class MenuAnmiThread extends Thread{
	MenuSurfaceView mv;					//MenuSurfaceView������
	int afterCurrentIndex;				//����������ɺ�ĵ�ǰ�˵����
	public MenuAnmiThread(MenuSurfaceView mv,int afterCurrentIndex){
		this.mv=mv;
		this.afterCurrentIndex=afterCurrentIndex;
	}
	
	public void run(){
		for(int i=0;i<=totalSteps;i++){		//ѭ��ָ���Ĵ�����ɶ���
			mv.changePercent=percentStep*i;	//����˲��İٷֱ�
			mv.init();						//��ʼ������λ��ֵ			
			if(mv.anmiState==1)
			{	//���ҵĶ���
				//���ݰٷֱȼ��㵱ǰ�˵���λ�á���С
				mv.currentSelectX=mv.currentSelectX+(bigWidth+span)*mv.changePercent;
				mv.currentSelectY=mv.currentSelectY+(bigHeight-smallHeight)*mv.changePercent;
				mv.currentSelectWidth=(int)(smallWidth+(bigWidth-smallWidth)*(1-mv.changePercent));
				mv.currentSelectHeight=(int)(smallHeight+(bigHeight-smallHeight)*(1-mv.changePercent));
				//���ڶ������ң����˵��������˼������˵����С
				mv.leftWidth=(int)(smallWidth+(bigWidth-smallWidth)*mv.changePercent);
				mv.leftHeight=(int)(smallHeight+(bigHeight-smallHeight)*mv.changePercent);				
			}
			else if(mv.anmiState==2)
			{	//����Ķ���
				//���ݰٷֱȼ��㵱ǰ�˵���λ�á���С
				mv.currentSelectX=mv.currentSelectX-(smallWidth+span)*mv.changePercent;
				mv.currentSelectY=mv.currentSelectY+(bigHeight-smallHeight)*mv.changePercent;
				mv.currentSelectWidth=(int)(smallWidth+(bigWidth-smallWidth)*(1-mv.changePercent));
				mv.currentSelectHeight=(int)(smallHeight+(bigHeight-smallHeight)*(1-mv.changePercent));
				//���ڶ��������Ҳ�˵��������˼����Ҳ�˵����С
				mv.rightWidth=(int)(smallWidth+(bigWidth-smallWidth)*mv.changePercent);
				mv.rightHeight=(int)(smallHeight+(bigHeight-smallHeight)*mv.changePercent);					
			}
			
			//������������Ĳ˵���λ��
			mv.tempxLeft=mv.currentSelectX-(span+mv.leftWidth);			
			mv.tempyLeft=mv.currentSelectY+(mv.currentSelectHeight-mv.leftHeight);	
			//����������Ҳ�Ĳ˵���λ��
			mv.tempxRight=mv.currentSelectX+(span+mv.currentSelectWidth);
			mv.tempyRight=mv.currentSelectY+(mv.currentSelectHeight-mv.rightHeight);
		
			mv.repaint();				//�ػ滭��
			try{Thread.sleep(timeSpan);
			}catch(Exception e){e.printStackTrace();}
		}
		
		mv.anmiState=0;						//������ɺ����ö���״̬Ϊ0-�޶���		
		mv.currentIndex=afterCurrentIndex; 	//������ɺ���µ�ǰ�Ĳ˵�����
		mv.init();							//��ʼ������λ��ֵ
		mv.repaint();						//�ػ滭��
	}
}
