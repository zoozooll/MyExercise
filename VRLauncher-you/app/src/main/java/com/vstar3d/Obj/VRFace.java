package com.vstar3d.Obj;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.Log;

public class VRFace {
	private int mWidth;
	private int mHeight;
	private int mL;
	private int mT;
	public int id=-1;
	public Bitmap mBitmap=null;
	private Bitmap mBitmapOn=null;
	private Bitmap mBitmapOff=null;
	private boolean mOff=false;
	private boolean mSof=false;
	private int mTextureId=0;
	
	private FloatBuffer textureBuffer;
	private FloatBuffer vertexBuffer;
	private ShortBuffer IndicesBuffer;
	public float t;
	public float b;
	public float l;
	public float r;
	
	public float[] mPot0= new float[4];
	public float[] mPot1= new float[4];
	public float[] mMout0= new float[4];
	public float[] mMout1= new float[4];
	public boolean Checked=false;
	private boolean IsOn=false;
	float[] mMatrix=null;
	int mxParallels;
	int myParallels;
	long startTime=0;
	long animationtimes=500;
	float animationvalue=0.1f;
	private void init(int l,int t,int w,int h,int xParallels ,int yParallels,float z)
	{
		mL=l;
		mT=t;
		mWidth=w;
		mHeight=h;
		mTextureId=newTexture();
		mxParallels=xParallels;
		myParallels=yParallels;
		esGenSphere(xParallels,yParallels,z);
	}
	
	public VRFace(int l,int t,int w,int h,int xParallels,int yParallels,float z,int ARGBa,int ARGBr,int ARGBg,int ARGBb) //mm x 
	{
		init(l,t,w,h ,xParallels,yParallels,z);
		
		if(mBitmap==null)
		{	mBitmap=Bitmap.createBitmap(mWidth, mHeight,  
                Bitmap.Config.ARGB_8888);
		}
		Canvas canvas = new Canvas(mBitmap);
		canvas.drawARGB(ARGBa, ARGBr,ARGBg, ARGBb);
		updateimg();
	}
	
	public VRFace(int l,int t,int w,int h,int xParallels,int yParallels,float z,Context context,String ImgName) //mm x 
	{
		init(l,t,w,h ,xParallels,yParallels,z);
		
		mBitmap=Base.loadImageAssets(context, ImgName);
		updateimg();
	}
	
	public VRFace(int l,int t,int w,int h,int xParallels,int yParallels,float z,Context context,String ImgName,String OnImgName,int id) //mm x 
	{
		this.id=id;
		init(l,t,w,h ,xParallels,yParallels,z);
		mBitmap=Base.loadImageAssets(context, ImgName);
		mBitmapOn=Base.loadImageAssets(context, OnImgName);
		mMatrix=new float[16];
	
		updateimg();
	}
	
	public VRFace(int l,int t,int w,int h,int xParallels,int yParallels,float z,Context context,Bitmap Img,Bitmap OnImg,int id) //mm x 
	{
		this.id=id;
		init(l,t,w,h ,xParallels,yParallels,z);
		mBitmap=Img;
		mBitmapOn=OnImg;
		mMatrix=new float[16];
		
		updateimg();
	}
	
	public VRFace(int l,int t,int w,int h,int xParallels,int yParallels,float z,Context context,String ImgName,String OnImgName,String OffImgName,int id) {
		this.id=id;
		init(l,t,w,h ,xParallels,yParallels,z);
		mBitmap=Base.loadImageAssets(context, ImgName);
		mBitmapOn=Base.loadImageAssets(context, OnImgName);
		mBitmapOff=Base.loadImageAssets(context, OffImgName);

		mOff=true;
		mMatrix=new float[16];
	
		updateimg();
	}

	public Bitmap GetBmp()
	 {
		return mBitmap;
	 }

	int numVertices=0;	
	int numIndices=0;
	
	protected void updateimg()
	{
		if(mSof && (mBitmapOff!=null))
		{
			Checked=false;
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureId);// 绑定要使用的纹理
			GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, mBitmapOff, 0); // 生成纹理
		}else
		if(mBitmap!=null)
		{
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureId);
			if(mBitmapOn!=null && IsOn)
			{
				GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, mBitmapOn, 0);
			}else 
			{
				Checked=false;
				GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, mBitmap, 0);
			}
		}
	}
	
	private boolean upimage=false;
	
	protected void updatecheng(float [] matrix)
	{
		if(upimage)
		{
			updateimg();
			upimage=false;
		}
		if((mSof!=mOff) && (mBitmapOff!=null))
		{
			mSof=mOff;
			updateimg();
		}
		if(mSof)
		{
			;
		}else if(id>-1)
		{
			
			boolean ison=false;
			
			
			 Matrix.multiplyMV(mMout0,0, matrix, 0,mPot0, 0);
			 Matrix.multiplyMV(mMout1,0, matrix, 0,mPot1, 0);
			 float minx=mMout0[0];
			 float maxx=mMout1[0];
			 if(minx>maxx)
			 {
				 minx=mMout1[0];
				 maxx=mMout0[0];
			 }
			 
			 float miny=mMout0[1];
			 float maxy=mMout1[1];
			 if(miny>maxy)
			 {
				 miny=mMout1[1];
				 maxy=mMout0[1];
			 }


			// if((Math.abs( mMout[0])<0.03) && Math.abs(mMout[1])<0.05 && mMout[2]>0)
			 if(minx<0 && maxx>0 && miny<0 && maxy>0 && mMout1[2]>0 )
				 ison=true;
			
			if(IsOn!=ison)
			{
				IsOn=ison;
				startTime=System.currentTimeMillis();
				updateimg();
			}
		}
	}
	
	protected void Draw(int attribPosition,int attribTexCoord,float [] EulerAngles, float[] matrix, int mMVPMatrixHandle, boolean left)
	{
		if(left)
			updatecheng(matrix);
				
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		 GLES20.glEnable(GLES20.GL_BLEND);
		   
		GLES20.glVertexAttribPointer(attribPosition, 3,
                GLES20.GL_FLOAT, false,
                12, vertexBuffer);
        GLES20.glVertexAttribPointer(attribTexCoord, 2,
                GLES20.GL_FLOAT, false,
                8, textureBuffer);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureId);           
        if(IsOn)
        {
        	updateMartix(matrix,mMVPMatrixHandle);
        }
	    GLES20.glDrawElements (GLES20.GL_TRIANGLES, numIndices, GLES20.GL_UNSIGNED_SHORT, IndicesBuffer ); 
	    if(IsOn)
        {
	    	GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, matrix, 0);
        }
	}
	float movez=0.0f;
	
	private void updateMartix(float[] matrix, int mMVPMatrixHandle) {
		
		if(mMatrix!=null)
		{
			long t=System.currentTimeMillis()-startTime;
			float z=0.0f;
			if(t>animationtimes)
			{
				t=animationtimes;
				Checked=true;
			}
			z=t*animationvalue/animationtimes;
			for(int i=0;i<15;i++)
			{
				mMatrix[i]=matrix[i];
			}
		
			mMatrix[15]=matrix[15]-z;
			GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMatrix, 0);
		}
		
	}

	private int newTexture() {

		int[] textureId = new int[1];
		
		GLES20.glGenTextures(1, textureId, 0);
		
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId[0]);
		// Set filtering
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, 
		GLES20.GL_LINEAR);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, 
		GLES20.GL_LINEAR);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, 
		GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, 
		GLES20.GL_CLAMP_TO_EDGE);
		
		return textureId[0];
	}
	
	
	private int esGenSphere(int numSlices,int numParallels,float d)
	{
		int i=0;
		int j;
		int iidex=0;
		numVertices = (numParallels+1)*(numSlices+1);
		numIndices = numParallels * numSlices *6;
		float angleFistx = (float) ((float)mL/(float)(Base.MaxWidth)*(2.0f * Math.PI));
		float angleStepX = (float) ((float)mWidth/(float)(Base.MaxWidth)*(2.0f * Math.PI)/((float)numSlices));
		float angleStepY = (float) ((float)mHeight/(float)(Base.MaxWidth)*(2.0f * Math.PI)/((float)numParallels));
		float angleFisty = (float) ((float)mT/(float)(Base.MaxWidth)*(2.0f * Math.PI));
		
		t=(float) (Math.PI/2.0f-angleFisty);
		b=(float) (t-((float)mHeight/(float)(Base.MaxWidth)*(2.0f * Math.PI)));
		l=(float) (Math.PI-angleFistx);
		r=(float) ((float) Math.PI-(angleFistx+(float)mWidth/(float)(Base.MaxWidth)*(2.0f * Math.PI)));
		//Log.e("3dvstar","l="+l+","+r);
		float vertices[] = new float[numVertices*3];
		float texCoords[] = new float[numVertices*2];
		short indices [] = new short[numIndices];
		
		float minx=1;
		float maxx=-1;
		
		float miny=1;
		float maxy=-1;
		
		float minz=1;
		float maxz=-1;
		for(i=0;i<numParallels+1;i++){
			for(j=0;j<numSlices+1;j++){
				int vertex=(i*(numSlices+1)+j)*3;
				vertices[vertex+0]=0.0f-(float) (d*Math.sin(angleFisty+angleStepY*(float)i)*Math.sin(angleFistx+angleStepX*(float)j));
				vertices[vertex+1]=(float) (d*Math.cos(angleFisty+angleStepY*(float)i));
				vertices[vertex+2]=(float) (d*Math.sin(angleFisty+angleStepY*(float)i)*Math.cos(angleFistx+angleStepX*(float)j));
				
				if(vertices[vertex+0]<minx)
					minx=vertices[vertex+0];
				
				if(vertices[vertex+0]>maxx)
					maxx=vertices[vertex+0];
					
				if(vertices[vertex+1]<miny)
					miny=vertices[vertex+1];
					
				if(vertices[vertex+1]>maxy)
					maxy=vertices[vertex+1];
						
				if(vertices[vertex+2]<minz)
					minz=vertices[vertex+2];
						
				if(vertices[vertex+2]>maxz)
					maxz=vertices[vertex+2];
				
				int texIndex=(i*(numSlices+1)+j)*2;
				texCoords[texIndex+0]=(float)j/(float)numSlices;
				texCoords[texIndex+1]=((float)i/(float)numParallels);//((float)i/(float)numParallels);//
			}
		}
		
		/*mPot[0]=(maxx-minx)/2.0f+minx;
		mPot[1]=(maxy-miny)/2.0f+miny;
		mPot[2]=(maxz-minz)/2.0f+minz;
		mPot[3]=1.0f;*/
		mPot0[0]=vertices[0];
		mPot0[1]=vertices[1];
		mPot0[2]=vertices[2];
		mPot0[3]=1.0f;
		
		i=(numVertices-1)*3;
		mPot1[0]=vertices[i+0];
		mPot1[1]=vertices[i+1];
		mPot1[2]=vertices[i+2];
		mPot1[3]=1.0f;
		
		for(i = 0; i < numParallels ; i++ ) { 
			for(j = 0; j < numSlices; j++ ) { 
				indices[iidex++]= (short) (i* (numSlices+1)+j);
				indices[iidex++]= (short)((i+1)* (numSlices+1)+j);
				indices[iidex++]= (short)((i+1)* (numSlices+1)+(j+1));		
				 
				indices[iidex++]= (short)(i* (numSlices+1)+j);
				indices[iidex++]= (short)((i+1)* (numSlices+1)+(j+1));
				indices[iidex++]= (short)(i* (numSlices+1)+(j+1));	
			}
		}
	
		
		ByteBuffer bb = ByteBuffer.allocateDirect(
				vertices.length * 4);
	    bb.order(ByteOrder.nativeOrder());
	
	    vertexBuffer = bb.asFloatBuffer();
	    vertexBuffer.put(vertices);
	    vertexBuffer.position(0);
	    
	    ByteBuffer cc = ByteBuffer.allocateDirect(
	    		texCoords.length * 4);
	    cc.order(ByteOrder.nativeOrder());
	
	    textureBuffer = cc.asFloatBuffer();
	    textureBuffer.put(texCoords);
	    textureBuffer.position(0);
	    
	    ByteBuffer dd = ByteBuffer.allocateDirect(
	    		indices.length * 2);
	    dd.order(ByteOrder.nativeOrder());
	
	    IndicesBuffer = dd.asShortBuffer();
	    IndicesBuffer.put(indices);
	    IndicesBuffer.position(0);
	return numIndices;
}

	public void setOff(boolean c) {
		mOff=c;
		if(c)
		{
			IsOn=false;
			Checked=false;
		}
	}

	public void setBmp(Bitmap Img, Bitmap OnImg) {
		mBitmap=Img;
		mBitmapOn=OnImg;
		upimage=true;
	}
}
