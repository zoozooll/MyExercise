package wyf.tzz.lta;

import static wyf.tzz.lta.Constant.*;

public class HeroPlaneMoveThread extends Thread{
	MySurfaceView msv;			//MySurfaceView������
	boolean overFlag=false;		//�Ƿ���Ϸ������־

	public HeroPlaneMoveThread(MySurfaceView msv){
		this.msv=msv;
	}
	
	public void run(){
		while(!overFlag){
			msv.mRenderer.land.mAngleX+=ANGLE;			//����Բ����x����ת
			msv.mRenderer.sky.mAngleX+=ANGLE/2;			//���Բ����x����ת			
			try {Thread.sleep(30);} 					//˯��
			catch (InterruptedException e) {e.printStackTrace();}
		}
	}
}