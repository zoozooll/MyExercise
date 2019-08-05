package com.yarin.android.Examples_05_11;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

public class GameView extends View implements Runnable
{
	/* ����Bitmap���� */
	Bitmap	mBitQQ	= null;
	int		BitQQwidth	= 0;
	int		BitQQheight	= 0;
	
	Paint   mPaint = null;
	     
	/* Bitmap��Ⱦ */
	//Shader mBitmapShader = null;
	
	/* ���Խ�����Ⱦ */
	//Shader mLinearGradient = null;
	
	/* �����Ⱦ */
	//Shader mComposeShader = null;
	   
	/* ���ѽ�����Ⱦ */
	Shader mRadialGradient = null;
	
	/* �ݶ���Ⱦ */
	//Shader mSweepGradient = null;
	  
	
	ShapeDrawable mShapeDrawableQQ = null;
	  
	public GameView(Context context)
	{
		super(context);
		
		/* װ����Դ */
		mBitQQ = ((BitmapDrawable) getResources().getDrawable(R.drawable.qq)).getBitmap();

		/* �õ�ͼƬ�Ŀ�Ⱥ͸߶� */
		BitQQwidth = mBitQQ.getWidth();
		BitQQheight = mBitQQ.getHeight();
		
		/* ����BitmapShader���� */
		//mBitmapShader = new BitmapShader(mBitQQ,Shader.TileMode.REPEAT,Shader.TileMode.MIRROR);
		
		/* ����LinearGradient�����ý������ɫ���� ˵��һ���⼸����� 
		 * ��һ�� ��ʼ��x����
		 * �ڶ��� ��ʼ��y����
		 * ������ ��ɫ����
		 * ���ĸ� ���Ҳ��һ����������ָ����ɫ��������λ�� ���Ϊnull �����¶��߾��ȷֲ�
		 * ����� ��Ⱦģʽ
		 * */
		//mLinearGradient = new LinearGradient(0,0,100,100,
		//									 new int[]{Color.RED,Color.GREEN,Color.BLUE,Color.WHITE},
		//									 null,Shader.TileMode.REPEAT);
		/* �������Ϊ�����Ⱦ*/
		//mComposeShader = new ComposeShader(mBitmapShader,mLinearGradient,PorterDuff.Mode.DARKEN);
		       
		/* ����RadialGradient�������ð뾶������ */
		//����ʹ����BitmapShader��LinearGradient���л��
		//��ȻҲ����ʹ�����������
		//�����Ⱦ��ģʽ�ܶ࣬���Ը����Լ���Ҫ��ѡ��
		/*mRadialGradient = new RadialGradient(50,200,50,
											 new int[]{Color.GREEN,Color.RED,Color.BLUE,Color.WHITE},
											 null,Shader.TileMode.REPEAT);*/
		
		
		mRadialGradient = new RadialGradient(200,200,300,
				 new int[]{0x00000000,Color.WHITE},
				 new float[]{0.5f,1.0f},Shader.TileMode.CLAMP);
		/* ����SweepGradient���� */
		//mSweepGradient = new SweepGradient(30,30,new int[]{Color.GREEN,Color.RED,Color.BLUE,Color.WHITE},null);

		mPaint = new Paint();
		
		/* �����߳� */
		new Thread(this).start();
	}
	
	public void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		
		//��ͼƬ�ü�Ϊ��Բ��
		/* ����ShapeDrawable���󲢶�����״Ϊ��Բ */
		mShapeDrawableQQ = new ShapeDrawable(new OvalShape());

		/* ����Ҫ���Ƶ���Բ�εĶ���ΪShapeDrawableͼƬ */
		//mShapeDrawableQQ.getPaint().setShader(mBitmapShader);
		
		/* ������ʾ���� */
		mShapeDrawableQQ.setBounds(0,0, BitQQwidth, BitQQheight);
		
		/* ����ShapeDrawableQQ */
		mShapeDrawableQQ.draw(canvas); 		
		//canvas.drawBitmap(mBitQQ, 0, 0, mPaint);
		
		//���ƽ���ľ���
		//mPaint.setShader(mLinearGradient);
		//canvas.drawRect(BitQQwidth, 0, 320, 156, mPaint);
	        
		//��ʾ�����ȾЧ��
		//mPaint.setShader(mComposeShader);
		//canvas.drawRect(0, 300, BitQQwidth, 300+BitQQheight, mPaint);
		
		//���ƻ��ν���
		mPaint.setShader(mRadialGradient);
		canvas.drawRect(0, 0, 400, 400, mPaint);
		
		//�����ݶȽ���
		//mPaint.setShader(mSweepGradient);
		//canvas.drawRect(150, 160, 300, 300, mPaint);
		
	}
	
	// �����¼�
	public boolean onTouchEvent(MotionEvent event)
	{
		return true;
	}


	// ���������¼�
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		return true;
	}


	// ���������¼�
	public boolean onKeyUp(int keyCode, KeyEvent event)
	{
		return false;
	}


	public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event)
	{
		return true;
	}
	
	
	/**
	 * �̴߳���
	 */
	public void run()
	{
		while (!Thread.currentThread().isInterrupted())
		{
			try
			{
				Thread.sleep(100);
			}
			catch (InterruptedException e)
			{
				Thread.currentThread().interrupt();
			}
			//ʹ��postInvalidate����ֱ�����߳��и��½���
			postInvalidate();
		}
	}
}

