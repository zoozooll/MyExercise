package com.godyhm.test.bendtriangle;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.GLSurfaceView.Renderer;

public class TriangleRender implements Renderer {

	private Context m_context = null;
	private final float Width = 1.2f;
	private final float Length = 1.5f;
	//position of z axis
	private final float Pos = 0f;
	private final float Radius = 0.2f;
	private FloatBuffer mVortexBufA = null;
	private FloatBuffer mTextBufferA = null;
	private int tIdA = 0;
	private int tIdB = 0;
	
	//list of keeping the vertexes coordinates of front-side of one page
	private ArrayList<WorldCoord> listFront = null; 
	private ArrayList<TextCoord> listtextcrds = null;
	private int vortexCnt = 4; 
	private int depth = 100;
	private float mAngle = 0f;
	private TriangleView m_view = null;
	public TriangleRender(Context context,TriangleView view)
	{
		m_context = context;
		m_view = view;
	};

	private void generateCoordinates(
			float depth,
			float r,
			float angle)
	{
		float tmpAngle = 0;
		float tmp = Length*Length+Width*Width;
		float sinthta = (float) (Width/Math.sqrt(tmp));
		float costhta = (float) (Length/Math.sqrt(tmp));
		if(angle*r>Length*sinthta)
		{
			mAngle = 0f;
			m_view.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
			return;
		}
		if(listFront.size()>0)
		{
			listFront.clear();
		}	
		if(listtextcrds.size()>0)
		{
			listtextcrds.clear();
		}
		//put top left coordinate
		listFront.add(new WorldCoord(0f,Length,Pos));
		listtextcrds.add(new TextCoord(0,0));
		for(int i=0;i<=depth;i++)
		{
			tmpAngle = (float)(i/depth)*angle;
			//coordinates along the width direction
			WorldCoord xcrd = new WorldCoord();
			TextCoord txcrd = new TextCoord();
			//coordinates along the length direction
			WorldCoord ycrd = new WorldCoord();
			TextCoord tycrd = new TextCoord();
			

			//coordinate equation
			/*x=-r*tmpAngle*cos(xangle)+r*cos(xangle)*sin(tmpAngle)
			 * y=(r*cirangle-r*tmpAngle*cos(xangle)*cos(xangle))/sin(xangle)-r*sin(xangle)*sin(tmpAngle)
			 * z=r-r*cos(tmpAngle)
			 */
		    
			xcrd.x = (float) (-r*tmpAngle*costhta+
					r*costhta*Math.sin(tmpAngle));
			
			xcrd.y = (float) ((r*angle-r*tmpAngle*costhta*costhta)/sinthta-
					r*sinthta*Math.sin(tmpAngle));
			xcrd.z = -(float) (r-r*Math.cos(tmpAngle)+Pos);
			txcrd.u=0f;
			txcrd.v=(float) ((Length-r*(angle-tmpAngle)/sinthta)/Length);
			
			listFront.add(xcrd);
			listtextcrds.add(txcrd);
			if(i==depth)
			{
				return;
			}
		
			//coordinate equation
			/*x=width+(tmpAngle*r*sin(xangle)*sin(xangle)-angle*r)/cos(xangle)+r*cos(xangle)*sin(tmpAngle)
			 * y=r*tmpAngle*sin(xangle)-r*sin(xangle)*sin(tmpAngle)+Length
			 * z=r-r*cos(tmpAngle)
			 */
			ycrd.x = (float) (Width+(tmpAngle*r*sinthta*sinthta-
					angle*r)/costhta+
					r*costhta*Math.sin(tmpAngle));
			
			ycrd.y = (float) (r*tmpAngle*sinthta-
					r*Math.sin(tmpAngle)*sinthta)+Length;
			tycrd.u =(float) ((Width-r*(angle-tmpAngle)/costhta)/Width);
			tycrd.v=0f;
			ycrd.z = xcrd.z;
			listFront.add(ycrd);
			listtextcrds.add(tycrd);
		}
	}
	
	@Override
	public void onDrawFrame(GL10 gl) {
		generateCoordinates(depth, Radius,mAngle);
		mAngle+=0.02f;		
		gl.glClearColor(0, 0, 0, 1.0f);	
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT|GL10.GL_DEPTH_BUFFER_BIT);	
		gl.glLoadIdentity();
        gl.glPushMatrix();               
        gl.glRotatef(40, 0, 0, 1);
        gl.glTranslatef(0.0f, -1.25f, 0f);
        drawTriangle(gl);      
        gl.glPopMatrix(); 
	}
	
	private void drawTriangle(GL10 gl)
	{
		WorldCoord wc1 = null;
		WorldCoord wc2 = null;
		WorldCoord wc3 = null;
		WorldCoord wc4 = null;
		TextCoord tc1 = null;
		TextCoord tc2 = null;
		TextCoord tc3 = null;
		TextCoord tc4 = null;
		int vertexCount = listFront.size();
		if(vertexCount<1)
		{
			return;
		}
		
		gl.glFrontFace(GL10.GL_CCW);
		for(int i=0;i<vertexCount-3;i+=2)
		{
			wc1 = listFront.get(i);
			wc2 = listFront.get(i+1);
			wc3 = listFront.get(i+2);
			wc4 = listFront.get(i+3);
			tc1 = listtextcrds.get(i);
			tc2 = listtextcrds.get(i+1);
			tc3 = listtextcrds.get(i+2);
			tc4 = listtextcrds.get(i+3);
			putVertextToBuf(mVortexBufA, wc1,wc2,wc3,wc4);
//			gl.glColor4f(1.0f-i*0.002f, 0.5f+i*0.002f, 0.3f+i*0.002f, 1f);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, tIdA);
			putTexureCoords(mTextBufferA, tc1,tc2,tc3,tc4);
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTextBufferA.position(0));
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVortexBufA.position(0));    
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0,mVortexBufA.capacity()/3);
		}
		gl.glFrontFace(GL10.GL_CW);
		for(int i=0;i<vertexCount-3;i+=2)
		{
			wc1 = listFront.get(i+3);
			wc2 = listFront.get(i+2);
			wc3 = listFront.get(i+1);
			wc4 = listFront.get(i);
			tc1 = listtextcrds.get(i+3);
			tc2 = listtextcrds.get(i+2);
			tc3 = listtextcrds.get(i+1);
			tc4 = listtextcrds.get(i);
			putVertextToBuf(mVortexBufA, wc1,wc2,wc3,wc4);
//			gl.glColor4f(1.0f-i*0.002f, 0.5f+i*0.002f, 0.3f, 1f);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, tIdB);
			putTexureCoords(mTextBufferA, tc1,tc2,tc3,tc4);
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTextBufferA.position(0));
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVortexBufA.position(0));    
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0,mVortexBufA.capacity()/3);
		}
	}
	
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		gl.glViewport(0, 0, width, height);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		gl.glDisable(GL10.GL_DITHER);
        gl.glShadeModel(GL10.GL_SMOOTH);
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1f);//
		gl.glClearDepthf(1.0f); //
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glDepthFunc(GL10.GL_LEQUAL);
        gl.glEnable(GL10.GL_CULL_FACE);
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT,
                  GL10.GL_FASTEST); 
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnable(GL10.GL_TEXTURE_2D);
	      gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glEnable(GL10.GL_BLEND);
		
		ByteBuffer vBufA = ByteBuffer.allocateDirect(vortexCnt*3*4);
		vBufA.order(ByteOrder.nativeOrder());
		mVortexBufA = vBufA.asFloatBuffer();
		
		ByteBuffer tbufA = ByteBuffer.allocateDirect(vortexCnt*2 * 4);
        tbufA.order(ByteOrder.nativeOrder());
        mTextBufferA = tbufA.asFloatBuffer();

		listFront = new ArrayList<WorldCoord>();
		listtextcrds = new ArrayList<TextCoord>();		
		initTextures(gl);
    }
    
    private void initTextures(GL10 gl)
    {
    	int [] textures = new int[2];
		gl.glGenTextures(2, textures,0);
		tIdA = textures[0];
		tIdB = textures[1];
		LoadTexture(gl, tIdA, getBitmapByPath("03.png"));
		LoadTexture(gl, tIdB, getBitmapByPath("04.jpg"));
    }
   
	private void putVertextToBuf(
			FloatBuffer fbuf,
			WorldCoord wcd1,
			WorldCoord wcd2,
			WorldCoord wcd3,
			WorldCoord wcd4)
	{
		 float[] coords = {
				 wcd1.x,wcd1.y,wcd1.z,
				 wcd2.x,wcd2.y,wcd2.z,
				 wcd3.x,wcd3.y,wcd3.z,
		    	 wcd4.x,wcd4.y,wcd4.z    		
		    };
		 
	     if(fbuf!=null)
	     {
	    	 fbuf.clear();
	     }	     
	     fbuf.put(coords);
	     fbuf.position(0);
	}
	
	private void putTexureCoords(
			FloatBuffer fbuf,
			TextCoord t1,
			TextCoord t2,
			TextCoord t3,
			TextCoord t4)
	{
		 float[] TextureArrayFront = {t1.u,t1.v,t2.u,t2.v,t3.u,t3.v,t4.u,t4.v};
	     if(fbuf!=null)
	     {
	    	 fbuf.clear();
	     }
	     fbuf.put(TextureArrayFront);
	     fbuf.position(0);      
	}
	
	private void LoadTexture(
			GL10 gl,
			int TextureId,
			Bitmap bitmap)
	{
		if(null==bitmap)
		{
			return;
		}
		gl.glBindTexture(GL10.GL_TEXTURE_2D, TextureId);     
	    GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);        
	    gl.glTexParameterf(GL10.GL_TEXTURE_2D,GL10.GL_TEXTURE_MIN_FILTER,GL10.GL_LINEAR);
	    gl.glTexParameterf(GL10.GL_TEXTURE_2D,GL10.GL_TEXTURE_MAG_FILTER,GL10.GL_LINEAR);
	    gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE,GL10.GL_REPLACE);
	    bitmap.recycle(); 
	}
    
	public Bitmap getBitmapByPath(String path)
	{
	    Bitmap bitmap = null;
	    InputStream inStream = null;
	    AssetManager aMger = m_context.getAssets();
	    try
        {
            inStream = aMger.open(path);
        }
        catch(IOException e) 
        {
            e.printStackTrace();
        }
        bitmap =  BitmapFactory.decodeStream(inStream);   
        try
        {
            if(inStream!=null)
            {
                inStream.close();
            }
        } 
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return bitmap;	    
	}
	
	public class TextCoord
	{
		public TextCoord() 
		{
			u = 0;
			v = 0;
		}
		public TextCoord(float ucrd,float vcrd) 
		{
			u = ucrd;
			v = vcrd;
		}
		public float u;
		public float v;
	}
	
	public class WorldCoord
	{
		public WorldCoord() 
		{
			x = 0;
			y = 0;
			z = 0;
		}
		public WorldCoord(float xcrd,float ycrd,float zcrd) 
		{
			x = xcrd;
			y = ycrd;
			z = zcrd;
		}
		public float x;
		public float y;
		public float z;
	}
}
