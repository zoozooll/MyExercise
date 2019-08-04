package com.oregonscientific.meep.meepmusic.opengl;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Environment;
import android.util.Log;

import com.oregonscientific.meep.meepmusic.R;
import com.oregonscientific.meep.meepmusic.SongObj;
import com.oregonscientific.meep.meepmusic.SongUiObj;
import com.oregonscientific.meep.meepmusic.SongUiObj.DisplayLevel;
import com.oregonscientific.meep.meepmusic.opengl.MusicUiControl.ActionState;
import com.oregonscientific.meep.meepmusic.opengl.MusicUiControl.MusicUiControlListener;
import com.oregonscientific.meep.opengl.ESTransform;
import com.oregonscientific.meep.opengl.MediaManager;
import com.oregonscientific.meep.opengl.OSButton;
import com.oregonscientific.meep.opengl.StateManager;
import com.oregonscientific.meep.opengl.StateManager.SystemState;

public class MusicPlayerRender implements GLSurfaceView.Renderer{

	private Context mContext;	
 	private StateManager mStateManager;			//handle system state
	
 	private MusicUiControl mMusicUiControl =null;
 	

    // Member variables
    private int mProgramObject;		//program object (drawing open gl)
    private int mWidth;				//screen width
    private int mHeight;			//screen height
    private FloatBuffer mVertices;	//vertice buffer (drawing open gl)
    private ShortBuffer mIndices;   //indices buffer (drawing open gl)
    private static String TAG = "HelloTriangleRenderer";  //for logging
    
    private boolean mIsSurfaceCreated =  false;

    // Uniform locations
    private int mMVPLoc;
    // Attribute locations
    private int mPositionLoc;
    // texture location
    private int mTexCoordLoc;
    // background location
    private int mBaseMapLoc;
    
    private int mBackgroundTexId;  	//id of background texture
    private int[] mMapTexId;
    
    private List<ESTransform> mEsTransform = new ArrayList<ESTransform>();	//global transform
    private ESTransform backgroundTransform = new ESTransform();			//background transform
    
    
    private int mTitleBgTextureID = 0;
    private Bitmap mTitleBackground= null;
    private OSButton mTitleBackgoundButton = null;
    
    private int mMovieBgTexId =0;
    private int mMainBgTexId = 0;
    private int mMusicBgTextId = 0;
    private int mPhotoBgTextId = 0;
    private int mEbookBgTextId = 0;
    private int mGameBgTextId = 0;
    
   
    
    
    
    private final float[] mVerticesData =
    { 
        -0.5f, 0.5f, 0.0f, // Position 0
        0.0f, 0.0f, // TexCoord 0
        -0.5f, -0.5f, 0.0f, // Position 1
        0.0f, 1.0f, // TexCoord 1
        0.5f, -0.5f, 0.0f, // Position 2
        1.0f, 1.0f, // TexCoord 2
        0.5f, 0.5f, 0.0f, // Position 3
        1.0f, 0.0f // TexCoord 3
    };

    private final short[] mIndicesData =
    { 
            0, 1, 2, 0, 2, 3 
    };
	
    public interface OspadRenderListener
    {
    	public abstract void  OnSurfaceCreated();
    	public abstract void OnSystemStateChanged(SystemState state);
    	public abstract void onMusicSelectedIndexChanged(int index);
    	
    	public abstract void onMusicRenderReady();
    }
    OspadRenderListener onOspadRenderListener = null;
	
	public void setOnOspadRenderListener(OspadRenderListener ospadRenderListener)
	{
		onOspadRenderListener = ospadRenderListener;
	}
	
	///
    // Constructor
    //
    public MusicPlayerRender (Context context)
    {
    	mContext = context;
        mVertices = ByteBuffer.allocateDirect(mVerticesData.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mVertices.put(mVerticesData).position(0);
        
        mIndices = ByteBuffer.allocateDirect(mIndicesData.length * 2)
                .order(ByteOrder.nativeOrder()).asShortBuffer();
        mIndices.put(mIndicesData).position(0);
        
        mStateManager = new StateManager();
        // new the mMusicUiControl in constructor to prevent throwing NullPointerExcepiton
        // modify by aaronli Mar15_2013
        mMusicUiControl = new MusicUiControl(mContext);
    }
    
    public StateManager getmStateManager() {
		return mStateManager;
	}

    ///
    // Create a shader object, load the shader source, and
    // compile the shader.
    //
	private int LoadShader(int type, String shaderSrc) {
		int shader;
		int[] compiled = new int[1];
		// Create the shader object
		shader = GLES20.glCreateShader(type);
		if (shader == 0)
			return 0;
		// Load the shader source
		GLES20.glShaderSource(shader, shaderSrc);
		// Compile the shader
		GLES20.glCompileShader(shader);
		// Check the compile status
		GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
		if (compiled[0] == 0) {
			Log.e(TAG, GLES20.glGetShaderInfoLog(shader));
			GLES20.glDeleteShader(shader);
			return 0;
		}
		return shader;
	}
	
    ///
    // Initialize the shader and program object
    //
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config)
    {
    	
        String vShaderStr = 
        		  "uniform mat4 u_mvpMatrix;   \n"
                + "attribute vec4 vPosition;    \n"
                + "attribute vec2 a_texCoord;   \n" 
                + "varying vec2 v_texCoord;     \n" 
                + "void main()                  \n"
                + "{                            \n"
                + "   gl_Position = u_mvpMatrix * vPosition;  \n"
                + "   v_texCoord = a_texCoord;  \n"
                + "}                            \n";

        String fShaderStr = 
                  "precision mediump float;					  		\n"
        		+ "varying vec2 v_texCoord;                         \n" 
        		+ "uniform sampler2D s_baseMap;                     \n" 
                + "void main()                                  	\n"
                + "{                                            	\n"
                + "  vec4 baseColor;                            	\n" 
                + "  baseColor = texture2D( s_baseMap, v_texCoord );\n"
                + "  gl_FragColor = baseColor;						\n"
                + "}                                            	\n";

        int vertexShader;
        int fragmentShader;
        int programObject;
        int[] linked = new int[1];
        mMusicUiControl. initSongUiList();
        // Load the vertex/fragment shaders
        vertexShader = LoadShader(GLES20.GL_VERTEX_SHADER, vShaderStr);
        fragmentShader = LoadShader(GLES20.GL_FRAGMENT_SHADER, fShaderStr);

        // Create the program object
        programObject = GLES20.glCreateProgram();

        if (programObject == 0)
            return;

        GLES20.glAttachShader(programObject, vertexShader);
        GLES20.glAttachShader(programObject, fragmentShader);
        
        // Link the program
        GLES20.glLinkProgram(programObject);

        // Check the link status
        GLES20.glGetProgramiv(programObject, GLES20.GL_LINK_STATUS, linked, 0);

        if (linked[0] == 0)
        {
            //Log.e(TAG, "Error linking program:");
            //Log.e(TAG, GLES20.glGetProgramInfoLog(programObject));
            GLES20.glDeleteProgram(programObject);
            return;
        }
        
        // Bind vPosition to attribute 0
        //GLES20.glBindAttribLocation(programObject, 0, "vPosition");
        mPositionLoc = GLES20.glGetAttribLocation(programObject, "vPosition");
        mTexCoordLoc = GLES20.glGetAttribLocation(programObject, "a_texCoord" );
        mMVPLoc = GLES20.glGetUniformLocation(programObject, "u_mvpMatrix");
        mBaseMapLoc = GLES20.glGetUniformLocation ( programObject, "s_baseMap" );

        // Store the program object
        mProgramObject = programObject;

        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        
        //background
        /*String filePath = Environment.getExternalStorageDirectory()
				+ File.separator + "home" + File.separator
				+ "music" + File.separator + "os_music_bg.png" ;*/
        try {
        	Bitmap bg = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.player_bg);
			mMainBgTexId = MediaManager.loadTexture(bg);
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        //set background to main
        mBackgroundTexId = mMainBgTexId;
        int[] mTextureIds = mMusicUiControl.getContentRenderIds();
        GLES20.glGenTextures (mTextureIds.length, mTextureIds, 0 );
        
        mMusicUiControl.setOnMusicUiControlListener(new MusicUiControlListener() {
			
			@Override
			public void onSlectedIndexChanged(int index) {
				if(onOspadRenderListener!= null)
				{
					onOspadRenderListener.onMusicSelectedIndexChanged(index);
				}
				
			}
		});
        
        if(onOspadRenderListener!= null)
        {
        	onOspadRenderListener.OnSurfaceCreated();
        	mIsSurfaceCreated = true;
        }
        Log.d(TAG, "onSurfaceCreated");
    }
    
    public boolean IsSurfaceCreated()
    {
    	return mIsSurfaceCreated;
    }

    
    
	private void updateBackgroud() {
		ESTransform perspective = new ESTransform();
		ESTransform modelview = new ESTransform();

		float aspect;

		// Compute the window aspect ratio
		aspect = (float) mWidth / (float) mHeight;

		// Generate a perspective matrix with a 60 degree FOV
		perspective.matrixLoadIdentity();

		perspective.perspective(60.0f, aspect, 1.0f, 60.0f);

		modelview.matrixLoadIdentity();
		// modelview.scale(5, 5*0.6f, 1f);
		modelview.scale(4.5f, 4.5f * 0.6f, 1f);
		modelview.translate(0f, 0f, -2f);

		backgroundTransform.matrixMultiply(modelview.get(), perspective.get());
	}


    
    // /
    // Draw a triangle using the shader pair created in onSurfaceCreated()
    //
	public void onDrawFrame(GL10 glUnused) {
		float aspect = (float) mWidth / (float) mHeight;
		// Set the viewport
		GLES20.glViewport(0, 0, mWidth, mHeight);

		// Clear the color buffer
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
		GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);

		// Use the program object
		GLES20.glUseProgram(mProgramObject);

		// Load the vertex position
		mVertices.position(0);
		GLES20.glVertexAttribPointer(mPositionLoc, 3, GLES20.GL_FLOAT, false,
				5 * 4, mVertices);
		// Load the texture coordinate
		mVertices.position(3);
		GLES20.glVertexAttribPointer(mTexCoordLoc, 2, GLES20.GL_FLOAT, false,
				5 * 4, mVertices);

		GLES20.glEnableVertexAttribArray(mPositionLoc);
		GLES20.glEnableVertexAttribArray(mTexCoordLoc);

		// draw backgroud
		updateBackgroud();
		DrawItem(mBackgroundTexId, backgroundTransform);

		// mMusicUiControl.setFirstAngle(mMusicUiControl.getFirstAngle() +
		// 0.25f);
		//Log.d(TAG, "Action State "+mMusicUiControl.getActionState());
		if (mMusicUiControl.isMusicFilesRended()) {
			
			List<SongUiObj> list = mMusicUiControl.getDisplaySongList();

			for (SongUiObj item : list) {
				if (mMusicUiControl.getActionState() == ActionState.Fling) {
					mMusicUiControl.setNextFlingTime();
					mMusicUiControl.SetFling();
				}

				// Log.d(TAG, "DrawItem "+list.get(i).getBgId());
				mMusicUiControl.functionButtonShowup(item.getBgTransform(),
						aspect, item.getPosition());
				DrawItem(mMusicUiControl.bindTitleTextureId(item), item.getBgTransform());

				/*mMusicUiControl.showTitle(item.getTitleTransform(), aspect,
						item.getPosition());
				DrawItem(getTitleTextureIdBySeat(item), item.getTitleTransform());

				mMusicUiControl.showAuthur(item.getAuthorTansform(), aspect,
						item.getPosition());
				DrawItem(getAuthorTextureIdBySeat(item), item.getAuthorTansform());*/
			}
		}

	}
    
	private int getTitleTextureId(SongUiObj item) {
		
		if (item.getDisplayLevel() == DisplayLevel.LEVEL1) {
			//Log.d(TAG, "getTitleTextureId "+item.getTitleWhiteTexId());
			return item.getTitleWhiteTexId();
		} else {
			//Log.d(TAG, "getTitleTextureId "+item.getTitleBlackTexId());
			return item.getTitleBlackTexId();
		}
		
	}
	
	// Modified by aaronli at Sep27 2013. Changed the way to getId.
	private int getTitleTextureIdBySeat(SongUiObj item) {
		if (item.getDisplayLevel() == DisplayLevel.LEVEL1) {
			return mMusicUiControl.bindTitleTextureId(item.getSongObj()
					.getTitle(), item.getTitleWhiteTexId(), 24, Color.WHITE,
					Align.LEFT);
		} else {
			return mMusicUiControl.bindTitleTextureId(item.getSongObj()
					.getTitle(), item.getTitleWhiteTexId(), 24, Color.argb(255,
					51, 77, 104), Align.LEFT);
		}
	}
	
	// Modified by aaronli at Sep27 2013. Changed the way to getId.
	private int getTitleTextureIdByWeakSeat(SongUiObj item) {
		if (item.getDisplayLevel() == DisplayLevel.LEVEL1) {
			return mMusicUiControl.bindTitleTextureId(item.getSongObj()
					.getTitle(), item.getTitleWhiteTexId(), 24, Color.WHITE,
					Align.LEFT);
		} else {
			return mMusicUiControl.bindTitleTextureId(item.getSongObj()
					.getTitle(), item.getTitleWhiteTexId(), 24, Color.argb(255,
					51, 77, 104), Align.LEFT);
		}
	}
	
	private int getAuthorTextureId(SongUiObj item) {
		if (item.getDisplayLevel() == DisplayLevel.LEVEL1) {
			//Log.d(TAG, "getAuthorTextureId "+item.getAuthorWhiteTexId());
			return item.getAuthorWhiteTexId();
		} else {
			//Log.d(TAG, "getAuthorTextureId "+item.getAuthorBlackTexId());
			return item.getAuthorBlackTexId();
		}
	}
	
	// Modified by aaronli at Sep27 2013. Changed the way to getId.
	private int getAuthorTextureIdBySeat(SongUiObj item) {
		if (item.getDisplayLevel() == DisplayLevel.LEVEL1) {
			return mMusicUiControl.bindTitleTextureId(item.getSongObj()
					.getAuthor(), item.getTitleWhiteTexId(), 16, Color.WHITE,
					Align.LEFT);
		} else {
			return mMusicUiControl.bindTitleTextureId(item.getSongObj()
					.getAuthor(), item.getTitleWhiteTexId(), 16, Color.GRAY,
					Align.LEFT);
		}
	}
	
	public void setContentPath(String contentPath)
	{
		mMusicUiControl.setContentPath(contentPath);
	}
	
	public void reloadMusicInfo(final String[] musicFileList)
	{

		mMusicUiControl.reloadMusicInfo(musicFileList);

		/*if (onOspadRenderListener != null) {
			onOspadRenderListener.onMusicRenderReady();
		}*/
	}
    
    private int getTextureID(int idx)
    {
    	if(idx%5 == 2)
    	{
    		return mMusicUiControl.getSongBgLevel1TxtId();
    	}
    	else if(idx%5 == 1 || idx%5 == 3)
    	{
    		return mMusicUiControl.getSongBgLevel2TxtId();
    	}
    	else
    	{
    		return mMusicUiControl.getSongBgLevel3TxtId();
    	}
    				
    }
   
    private void DrawItem(int textureID, ESTransform transform)
    {
    	 //draw message menu
    	//Log.d(TAG, "DrawItem "+textureID);
        GLES20.glActiveTexture ( GLES20.GL_TEXTURE0 );
        GLES20.glBindTexture ( GLES20.GL_TEXTURE_2D, textureID);
        GLES20.glUniform1i ( mBaseMapLoc, 0 );
        GLES20.glUniformMatrix4fv(mMVPLoc, 1, false,transform.getAsFloatBuffer());
        GLES20.glDrawElements ( GLES20.GL_TRIANGLES, 6, GLES20.GL_UNSIGNED_SHORT, mIndices );
        
    }

    
    public void rotateLefeMenu(float initx, float inity, float distanceX, float distanceY)
    {
    	float distance = (float)Math.sqrt(distanceX * distanceX + distanceY* distanceY);
    	Log.d("rotateLefeMenu", "rotateLefeMenu :" + distance);
    	if(StateManager.getSystemState() == SystemState.MSG_GROUP)
    	{
	    	if(distanceY < 0)
	    	{
	    		distance *=-1;
	    	}
    	}
    }
    
    public void setScroll(float distanceY)
    {
    	mMusicUiControl.setActionState(ActionState.Scroll);
    	mMusicUiControl.setScroll(distanceY);
    }
    
    public void setFling(float vx, float vy)
    { 
    	mMusicUiControl.setActionState(ActionState.Fling);
//		float speed = (float)Math.sqrt(vx*vx + vy*vy);
//		if(vy>0)
//		{
//			speed*= -1;
//		}
		mMusicUiControl.setFlingSpeed(-vy);
		//
    }
    
    public void setFlingDistance(float distance)
    {
    	mMusicUiControl.setActionState(ActionState.Fling);
    	mMusicUiControl.setFlingDistance(distance);
    }
    
    public void flingToSelected(int index) {
    	float flingToAngle = 180.f + 20.f * index;
    	float distance = flingToAngle - mMusicUiControl.getFirstAngle();
    	setFlingDistance(distance);
    }
    

    // /
    // Handle surface changes
    //
    public void onSurfaceChanged(GL10 glUnused, int width, int height)
    {
        mWidth = width;
        mHeight = height;
    }
    
    //debug
    public void printItem()
    {
    	mMusicUiControl.printItems();
    }
    
    public void setIsRepeatable(boolean isRepeatable)
    {
    	mMusicUiControl.setIsRepeatable(isRepeatable);
    }
    
    public boolean IsRepeatable()
    {
    	return mMusicUiControl.IsRepeatable();
    }
   
    
  
    
    
    
}
