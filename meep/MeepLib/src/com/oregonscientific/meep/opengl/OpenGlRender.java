package com.oregonscientific.meep.opengl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.R.dimen;
import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.renderscript.Program.TextureType;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebSettings.RenderPriority;
import android.widget.Adapter;

import com.oregonscientific.meep.global.Global;
import com.oregonscientific.meep.global.Global.AppType;
import com.oregonscientific.meep.message.common.OsListViewItem;
import com.oregonscientific.meep.opengl.SnakeShapeCtrl.ActionState;
import com.oregonscientific.meep.opengl.SnakeShapeCtrl.SnackCtrlState;
import com.oregonscientific.meep.opengl.StateManager.SystemState;
import com.oregonscientific.meep.opengl.StateManager.SystemStateListener;
import static com.oregonscientific.meep.opengl.MediaManager.*;

public class OpenGlRender implements GLSurfaceView.Renderer{

	private Context mContext;	
	private List<OSButton> mSubFunctionButtonList;//function menu level2 buttons(right)
 	private StateManager mStateManager;			//handle system state
 	
 	private Global.AppType mRenderType = AppType.Music;
 	private SnakeAdapter<OsListViewItem> mAdapter;
 	private Bitmap mDummyImage = null;
 	private Bitmap mDefaultImage = null;
 	private Bitmap mDefaultImageDim = null;
 	private Bitmap mBackgroundImage = null;
 	private Bitmap mBmpPreviewBg = null;
 	private Bitmap mBmpPreviewTop = null;
	
	private SnakeShapeCtrl mSnackCtrl = null;

    // Member variables
    private int mProgramObject;		//program object (drawing open gl)
    private int mWidth;				//screen width
    private int mHeight;			//screen height
    private FloatBuffer mVertices;	//vertice buffer (drawing open gl)
    private ShortBuffer mIndices;   //indices buffer (drawing open gl)
    private static final String TAG = "HelloTriangleRenderer";  //for logging

    // Uniform locations
    private int mMVPLoc;
    // Attribute locations
    private int mPositionLoc;
    // texture location
    private int mTexCoordLoc;
    // background location
    private int mBaseMapLoc;
    
    private int mBackgroundTexId;  	//id of background texture
    //private int mFunctionMenuTexId; //id of function menu button texture
    private int[] mMapTexId;
    
    OspadRenderListener onOspadRenderListener = null;
   //	private int[] mLightTextureIds;
  // int[] mDimTextureIds;
    
    List<ESTransform> mEsTransform = new ArrayList<ESTransform>();	//global transform

    ESTransform backgroundTransform = new ESTransform();			//background transform
    
    int mMovieBgTexId =0;
    
    ESTransform mMovieScreenTransform = new ESTransform();
    int mMovieScreenTexId = 0;
    ESTransform mMovieFloorTransform = new ESTransform();
    int mMovieFloorTexId = 0;
    
    private List<Integer> mTextureIdList;
    private List<Integer> mTextureIdCache;
    private List<String> mOSButtonList;
    ///** The int array is the value that textureIds */
    //private int[] mLightTextureIds = new int[100];
    ///** The int array is the value that textureIds */
   // private int[] mDimTextureIds = new int[100];
    
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
    
	public void setOnOspadRenderListener(OspadRenderListener listener)
	{
		onOspadRenderListener = listener;
	}
    
	//
    // Constructor
    //
    public OpenGlRender (Context context)
    {
    	mContext = context;
        mVertices = ByteBuffer.allocateDirect(mVerticesData.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mVertices.put(mVerticesData).position(0);
        
        mIndices = ByteBuffer.allocateDirect(mIndicesData.length * 2)
                .order(ByteOrder.nativeOrder()).asShortBuffer();
        mIndices.put(mIndicesData).position(0);
        
        mStateManager = new StateManager();
        mStateManager.setOnOspadRenderListener(new SystemStateListener( ) {
			
			@Override
			public void OnSystemStateChanged(SystemState state) {
				if(onOspadRenderListener!= null)
				{
					onOspadRenderListener.OnSystemStateChanged(state);
				}
				
			}
		});
        mTextureIdList = new ArrayList<Integer>();
        mTextureIdCache = new Vector<Integer>();
    }
    
    
    
    public StateManager getmStateManager() {
		return mStateManager;
	}

    ///
    // Create a shader object, load the shader source, and
    // compile the shader.
    //
	private int loadShader(int type, String shaderSrc) {
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
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
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

        // Load the vertex/fragment shaders
        vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vShaderStr);
        fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fShaderStr);

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
           // Log.e(TAG, "Error linking program:");
           // Log.e(TAG, GLES20.glGetProgramInfoLog(programObject));
            GLES20.glDeleteProgram(programObject);
            return;
        }
        
        // Bind vPosition to attribute 0
        // GLES20.glBindAttribLocation(programObject, 0, "vPosition");
        mPositionLoc = GLES20.glGetAttribLocation(programObject, "vPosition");
        mTexCoordLoc = GLES20.glGetAttribLocation(programObject, "a_texCoord" );
        mMVPLoc = GLES20.glGetUniformLocation(programObject, "u_mvpMatrix");
        mBaseMapLoc = GLES20.glGetUniformLocation ( programObject, "s_baseMap" );

        // Store the program object
        mProgramObject = programObject;

        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        // added by aaronli at May30 2013
        mSnackCtrl.onSnackSurfaceCreated();
        //background
        //loadBackground();
        mBackgroundTexId = MediaManager.loadTexture(mBackgroundImage);
        
        //mSnackCtrl.onSnackSurfaceCreated();
        
        if(onOspadRenderListener!= null)
        {
        	onOspadRenderListener.OnSurfaceCreated();
        }
        
        /* gen texuture ids of light texture picture items */
        int[] mLightTextureIds = mSnackCtrl.getmLightTextureIds();
        //GLES20.glGenTextures(mLightTextureIds.length, mLightTextureIds, 0);
        genTextureIds(mLightTextureIds);
        /* gen texuture ids of dim texture picture items	*/
        /*int[]mDimTextureIds = mSnackCtrl.getmDimTextureIds();
        GLES20.glGenTextures(30, mDimTextureIds, 0);*/
        //Log.d(TAG, "glGenTextures mLightTextureIds "+Arrays.toString(mLightTextureIds));
        //Log.d(TAG, "glGenTextures mDimTextureIds "+Arrays.toString(mDimTextureIds));
        //mSnackCtrl.setmDimTextureIds(mDimTextureIds);
        //mSnackCtrl.setmLightTextureIds(mLightTextureIds);
        
        // Release the variable
        vShaderStr = null;
        fShaderStr = null;
    }
    
//	private void loadBackground() {
//		String path = null;
//    	switch (mRenderType) {
//		case Music:
//			path = PATH_MUSIC_VIEWER_BG;
//			break;
//		case Movie:
//			path = PATH_MOVIE_VIEWER_BG;
//			break;
//		case Ebook:
//			path = PATH_EBOOK_VIEWER_BG;
//			break;
//		case Game:
//			path = PATH_GAME_VIEWER_BG;
//			break;
//		case Photo:
//			path = PATH_PHOTO_VIEWER_BG;
//			break;
//		case App:
//			path = PATH_APP_VIEWER_BG;
//			break;
//
//		default:
//			break;
//		}
//    	
//    	//load bg
//		if (path != null) {
//			try {
//				mBackgroundTexId = MediaManager.loadTexture(MediaManager.LoadBitmapFile(path));
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		
//		path = null;
//    }
    		
   
    
    private void updateBackgroud()
    {
		ESTransform perspective = new ESTransform();
		ESTransform modelview = new ESTransform();
		
		float aspect;
		
		// Compute the window aspect ratio
		aspect = (float) mWidth / (float) mHeight;
		
		// Generate a perspective matrix with a 60 degree FOV
		perspective.matrixLoadIdentity();
		perspective.perspective(60.0f, aspect, 1.0f, 60.0f);
		modelview.matrixLoadIdentity();
		//modelview.scale(5, 5*0.6f, 1f);
		modelview.scale(4.5f, 4.5f*0.6f, 1f);
		modelview.translate(0f, 0f, -2f);
		
		backgroundTransform.matrixMultiply(modelview.get(), perspective.get());
		
		// Release the variable
		perspective = null;
		modelview = null;
    }

    
    float sx = 5;
    float sy = 5;

    
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
		GLES20.glVertexAttribPointer(mPositionLoc, 3, GLES20.GL_FLOAT, false, 5 * 4, mVertices);
		// Load the texture coordinate
		mVertices.position(3);
		GLES20.glVertexAttribPointer(mTexCoordLoc, 2, GLES20.GL_FLOAT, false, 5 * 4, mVertices);

		GLES20.glEnableVertexAttribArray(mPositionLoc);
		GLES20.glEnableVertexAttribArray(mTexCoordLoc);

		// draw backgroud
		updateBackgroud();
		drawItem(mBackgroundTexId, backgroundTransform);
		
		if (!mSnackCtrl.isInitSnackShape()) {
			mSnackCtrl.initSnake();
		}

		if (mSnackCtrl.getSnackCtrlState() == SnackCtrlState.LEVEL1_TO_4C) {
			//mSubFunctionButtonList = mSnackCtrl.getButtonList();
			//Log.d(TAG, "loadImg : LEVEL1_TO_4C");
			if (mAdapter != null) {
				mSnackCtrl.setNextFlingTime();
				mSnackCtrl.level1To4CShowup(aspect);
				drawingButtonList();
			}
		} else if (mSnackCtrl.getSnackCtrlState() == SnackCtrlState.LEVEL4) {
			//mSubFunctionButtonList = mSnackCtrl.getButtonList();
			if (mAdapter != null) {
				if (mSnackCtrl.getActionState() == ActionState.Scroll) {
					//Log.d(TAG, "loadImg : level 4 scroll");
					mSnackCtrl.setNextFlingTime();
					mSnackCtrl.level4Scroll(aspect);
					drawingButtonList();
				} else if (mSnackCtrl.getActionState() == ActionState.Fling) {
					mSnackCtrl.setNextFlingTime();
					mSnackCtrl.level4Fling(aspect);
					drawingButtonList();
					//Log.d(TAG, "loadImg : level 4 Fling");
				} else if (mSnackCtrl.getActionState() == ActionState.Idle) {
					mSnackCtrl.level4Idle(aspect);
					drawingButtonList();
					//Log.d(TAG, "loadImg : level 4 Idle");
				}
			}
		} else if (mSnackCtrl.getSnackCtrlState() == SnackCtrlState.LEVEL4_TO_1A) {
			//Log.i(TAG, "SnackCtrlState "+mSnackCtrl.getSnackCtrlState());
			mSnackCtrl.setNextFlingTime();
			// for (int i = mSubFunctionButtonList.size() - 1; i >= 0; i--) {
			mSnackCtrl.level4_to_1Hide(aspect);
			drawingButtonList();
		} else if (mSnackCtrl.getSnackCtrlState() == SnackCtrlState.LEVEL4_TO_1C) {
			//Log.i(TAG, "SnackCtrlState "+mSnackCtrl.getSnackCtrlState());
			mSnackCtrl.setNextFlingTime();
			// for (int i = mSubFunctionButtonList.size() - 1; i >= 0; i--) {
			mSnackCtrl.level4_to_1Hide(aspect);
			drawingButtonList();
		}
		//GLES20.glFlush();
		// deleted by aaronli at Jun26 2013. deleted no used code
		//mSnackCtrl.getDefaultImageTxtID();
	}

	/**
	 * 
	 */
	private void drawingButtonList() {
		/*the deleted code is that show all texture ids array
		/ writed by arronli Mar17 2013 */
		//List<String> flag = new ArrayList<String>();
		for (int i = mSnackCtrl.getLastShowing(); i >= mSnackCtrl.getFirstShowing(); i--) {
			OSButton button = mSnackCtrl.obtainView(i);
			//if (button.isInShow()) {
				//flag.add("("+button.getTextTexId());
				int texureId = getTextureID(button);
				//flag.add(texureId +")");
				if (button != null && texureId != 0) {
					//Log.d(TAG, "drawingButtonList "+button.getName());
					drawItem(texureId, button.getButtonTransform());
					
				}
				
			//}
			//button = null;
		}
		//Log.d("mDummyImage", "DrawingButtonList "+Arrays.toString(flag.toArray()));
	}
   
	public void stopLoadingSnakeButtonImages() {
		/*if (mSnackCtrl != null) {
			mSnackCtrl.stopLoadingButtonImages();
		}*/
	}
	
	public void changedDataItem() {
		mSnackCtrl.loadDataChanged();
	}
	
    private void drawItem(int textureID, ESTransform transform)
    {
    	 //draw message menu
        GLES20.glActiveTexture ( GLES20.GL_TEXTURE0 );
        GLES20.glBindTexture ( GLES20.GL_TEXTURE_2D, textureID);
        GLES20.glUniform1i ( mBaseMapLoc, 0 );
        GLES20.glUniformMatrix4fv(mMVPLoc, 1, false,transform.getAsFloatBuffer());
        GLES20.glDrawElements ( GLES20.GL_TRIANGLES, 6, GLES20.GL_UNSIGNED_SHORT, mIndices );
        
    }
    
    // modify by aaron May 20 
   /* private int getTextureID(OSButton button)
    {
		if (button != null) {
			//Log.d(TAG, "button getTextureID "+button.getTexureID() + " "+button.getTextureBmp() + " " + button.getDimTextureBmp());
			// mofified by aaronli at Mar22 2013 made snack view run faster
			if ((button.getTexureID() == 0
					|| button.getTexureID() == mSnackCtrl.getDefaultImageTxtID())
					&& button.getTextureBmp() != null
					&& button.getDimTextureBmp() != null) {
				Bitmap bmap = button.getTextureBmp();
				Bitmap dimmedBmp = button.getDimTextureBmp();
				if (!bmap.isRecycled()) {
					int textureId = MediaManager.loadTexture(button.getTextureBmp());
					//Log.d(TAG, "texture id add "+textureId);
					//if (AppType.Photo == mRenderType) {
						osAddItextureIds(textureId);
					//}
					button.setTexureID(textureId);
					mSnackCtrl.resetDummyIndex();
					//Log.d("opengl", "loaded texture image" + button.getName() + "   " + button.getTexureID());
					bmap.recycle();
				}
				if (!dimmedBmp.isRecycled()) {
					int textureId = MediaManager.loadTexture(button.getDimTextureBmp());
					//Log.d(TAG, "texture id add "+textureId);
					//if (AppType.Photo == mRenderType) {
						osAddItextureIds(textureId);
					//}
					//Log.d("opengl", "loaded  dim texture image"+ button.getName()+ "   " + button.getDimTexId());
					button.setDimTexId(textureId);
					//dimmedBmp.recycle();
				}
				if (button.getShowIndex() == mSnackCtrl.getmSelectedButtonIdx()) {
					return button.getTexureID();
				} else {
					return button.getDimTexId();
				}
				// Release the variable
				//bmap = null;
				//dimmedBmp = null;
			} else if (button.getTexureID() != 0 || button.getTexureID() == mSnackCtrl.getDefaultImageTxtID()) {
				//Log.d(TAG, "return textureID "+button.getName());
				if (button.getShowIndex() == mSnackCtrl.getmSelectedButtonIdx()) {
					return button.getTexureID();
				} else {
					return button.getDimTexId();
				}
			} else {		
				//Log.d(TAG, "return DimDummyTexId "+button.getName() + button.getTexureID());
				if (button.getShowIndex() == mSnackCtrl.getmSelectedButtonIdx()) {
					//Log.d("mDummyImage", "return dummy " + button.getDummyTexId());
					return button.getDummyTexId();
				} else {
					//Log.d("mDummyImage", "return dim dummy " + button.getDimDummyTexId());
					return button.getDimDummyTexId();
				}
			}
		} 
		return mSnackCtrl.getDummyImageTxtID();
	}*/
    
    private int getTextureID(OSButton button)
    {
		if (button == null) {
			return -1;
		}
		// modified by aaronli at Jul2 2013. fixed #4666 #4681
		//int fDimTextureID, fLightTextureID;
		int flagTextureId;
		if (!TextUtils.isEmpty(button.getName())) {
			/*fDimTextureID = mSnackCtrl.getDefaultImageDimTxtId();
			fLightTextureID = mSnackCtrl.getDefaultImageTxtID();*/
			if (button.getShowIndex() == mSnackCtrl.getmSelectedButtonIdx()) {
				flagTextureId = mSnackCtrl.getDefaultImageTxtID();
			} else {
				flagTextureId = mSnackCtrl.getDefaultImageDimTxtId();
			}
		} else {
			//Log.d(TAG, "getTextureID "+button.getName());
			flagTextureId = mSnackCtrl.getDummyImageTxtID();
			return flagTextureId;
		}
		Bitmap bmap = button.getTextureBmp(), dimmedBmp = button.getDimTextureBmp();
		if (bmap != null && dimmedBmp != null) {
			/*if (!bmap.isRecycled()) {
				//Log.d(TAG, "loadSingleTexImage "+button.getTexureID());
				MediaManager.loadSingleTexImage(button.getTexureID(), bmap);
				fLightTextureID = button.getTexureID();
			}
			if (!dimmedBmp.isRecycled()) {
				//Log.d(TAG, "loadSingleTexImage "+button.getDimTexId());
				MediaManager.loadSingleTexImage(button.getDimTexId(), dimmedBmp);
				fDimTextureID = button.getDimTexId();
			}*/
			if (button.getShowIndex() == mSnackCtrl.getmSelectedButtonIdx()) {
				if (!bmap.isRecycled()) {
					//Log.d(TAG, "loadSingleTexImage "+button.getTexureID());
					MediaManager.loadSingleTexImage(button.getTexureID(), bmap);
					flagTextureId = button.getTexureID();
				}
			} else {
				if (!dimmedBmp.isRecycled()) {
					//Log.d(TAG, "loadSingleTexImage "+button.getDimTexId());
					MediaManager.loadSingleTexImage(button.getTexureID(), dimmedBmp);
					flagTextureId = button.getTexureID();
				}
				
			}
		}
		// fixed #4666 #4681 end
		return flagTextureId;
	}
    
    /** a textureId that bind to position 
     * modified by aaronli Mar21
     * @param button
     * @param position
     * @return
     * @deprecated
     */
    private int getTextureID(OSButton button, int position)
    {
    	int flag = 0;
		/*if (button == null) {
			return 0;
		}
		Bitmap b = null;
		if (button.isSelected()) {
			flag = mLightTextureIds[position % mLightTextureIds.length];
			b = button.getTextureBmp();
		} else {
			flag = mDimTextureIds[position % mDimTextureIds.length];
			b = button.getDimTextureBmp();
			
		}
		if (b != null) {
			MediaManager.loadSingleTexImage(flag, b);
		}*/
		return flag;
	}


	/**
	 * @param textureId
	 */
	private void osAddItextureIds(int textureId) {
		//Log.d(TAG, "texture id add "+ textureId);
		// run this method except MeepApp and MeepGame
		// add by aaronli at Apr2
		/*if (mRenderType == AppType.Game || mRenderType == AppType.App) {
			return;
		}*/
		mTextureIdCache.add(textureId);
		if (mTextureIdCache.size() < 100) {
			return;
		}
		int[] ids = new int[mTextureIdCache.size() - 100]; 
		for (int i = 0; (i < ids.length) && (mTextureIdCache.size() > 100); i ++) {
			ids[i] = mTextureIdCache.get(0);
			mTextureIdCache.remove(0);
			/*OSButton button = findOSButtonByTextureId(ids[i]);
			if (button != null) {
				button.setTexureID(mSnackCtrl.getDefaultImageTxtID());
				button.setDimTexId(mSnackCtrl.getDefaultImageDimTxtId());
			}*/
		}
		if (ids.length > 0) {
			//Log.d(TAG, "texture id arrays "+Arrays.toString(mTextureIdCache.toArray()));
			//Log.d(TAG, "texture id delete "+ Arrays.toString(ids));
			GLES20.glDeleteTextures(ids.length, ids, 0);
		}
	}
    
    private OSButton findOSButtonByTextureId(int textureId) {
    	for(OSButton b : mSubFunctionButtonList) {
    		if (b.getTexureID() == textureId || b.getDimTextTexId() == textureId) {
    			return b;
    		}
    	}
    	return null;
    }

    // /
    // Handle surface changes
    //
    public void onSurfaceChanged(GL10 glUnused, int width, int height)
    {
        mWidth = width;
        mHeight = height;
    }
    
    /**
     * on the glsurfaceview pause doing
     */
    public void onSurfacePause() {
    	mSnackCtrl.setSurefaceRenderCreated(false);
    	mSnackCtrl.clearTempViewTextureId();
    }
    
//    //function menu
    private void setSnackCtrlState(SnackCtrlState state)
    {
    	mSnackCtrl.setSnackCtrlState(state);
    	//mSnackCtrl.initFunctionMenuState();	
    }
    
	public void setFunctionMenuFling(float initX, float initY, float vx, float vy)
    {
		if (mSnackCtrl != null) {
			if (mSnackCtrl.getSnackCtrlState() == SnackCtrlState.LEVEL4) {
				mSnackCtrl.setActionState(ActionState.Fling);
				/*float speed = (float) Math.sqrt(vx * vx + vy * vy);
				if (vx > 0) {
					speed *= -1;
				}*/
				float speed = 0 - vx;
				mSnackCtrl.setFlingSpeed(speed);
			}
		}
    	
    }
    
    
    public OsListViewItem getSelectedLevel4FuncButton(float x, float y)
    {
		if (mSnackCtrl != null) {
			if (x > 475 && x < 720 && y > 120 && y < 366) {
				return mSnackCtrl.getLevel4SelectedButton();
			}
		}
    	return null;
    }
   
    
    public int getSelectedIndex() {
    	return mSnackCtrl.getmSelectedButtonIdx();
    }
    
    
    //level 4
    public void setLevel4XDistance(float xDistance)
    {
		if (mSnackCtrl != null) {
			float dx = xDistance / 15;
			mSnackCtrl.setLevel4XDistance(dx);
		}
    }
    
    /***
     * changed the snack action state to scroll
     * @author aaronli at Jun26 2013
     */
    public void setLevel4XScroll(float xDistance)
    {
    	float dx = xDistance/15;
    	if(mSnackCtrl != null) {
    		mSnackCtrl.setmNextScrollState((short) 0);
    		mSnackCtrl.setActionState(ActionState.Scroll);
    		mSnackCtrl.setLevel4XDistance(dx);
    	}
    	//mFunctionAnimationCtrl.getSnackShapeCtrl().setLevel4XDistance(dx);
    }
    
    
   
    
    boolean loop;
    public void rotate(boolean tloop)
    {
    	loop = tloop;
    }
    
    public void printDisplayList()
    {
    	if(mSubFunctionButtonList!= null)
    	{
    		for(int i= mSubFunctionButtonList.size()-1; i>=0 ; i--)
    		{
    			Log.d("debug", "debug, button Name:" + mSubFunctionButtonList.get(i).getName() + " angle:" + mSubFunctionButtonList.get(i).getPosition());
    		}
    	}
    }
    
   
    
    public void changeHomeState()
    {
    	mStateManager.setmSystemState(SystemState.HOME);
    }
    
    
//    public void setRenderType(AppType type)
//    {
//    	mRenderType = type;
//    }
   
    public AppType getRenderType()
    {
    	return mRenderType;
    }
    
//    public void (Bitmap bmp)
//    {
//    	mDummyImage = bmp;
//    }
    
 /*   public void initRender(AppType type, Bitmap background, Bitmap dummyIcon, Bitmap defaultIcon, Bitmap defaultImageDim, Bitmap previeBg, Bitmap previewTop)
    {
		mRenderType = type;
		mDummyImage = dummyIcon;
		mBackgroundImage = background;
		mDefaultImage = defaultIcon;
		mDefaultImageDim = defaultImageDim;
		mBmpPreviewBg = previeBg;
		mBmpPreviewTop = previewTop;
		mSnackCtrl = new SnackShapeCtrl(mContext, mRenderType, mDummyImage, mDefaultImage , mDefaultImageDim, mBmpPreviewBg, mBmpPreviewTop);
		
        mSnackCtrl.setOnSnackCtrlListener(new SnackShapeCtrl.SnackCtrlListener() {
			
			@Override
			public void ButtonSelectedIndexChanged(OSButton button, int index) {
				if(onOspadRenderListener!=null)
				{
					onOspadRenderListener.OnItemSelectedChanged(button);
				}
			}
		});
    }*/
    
/*    *//**
     * New method initrender add the attr {@link ISnakeDataSource}
     * @param type
     * @param dataSource
     * @param background
     * @param dummyIcon
     * @param defaultIcon
     * @param defaultImageDim
     *//*
    public void initRender(AppType type, ISnackDataSource dataSource, Bitmap background, Bitmap dummyIcon, Bitmap defaultIcon, Bitmap defaultImageDim)
    {
		mRenderType = type;
		mDummyImage = dummyIcon;
		mBackgroundImage = background;
		mDefaultImage = defaultIcon;
		mDefaultImageDim = defaultImageDim;
		mSnackCtrl = new SnackShapeCtrl(mRenderType, dataSource,  mDummyImage, mDefaultImage , mDefaultImageDim);
		
        mSnackCtrl.setOnSnackCtrlListener(new SnackShapeCtrl.SnackCtrlListener() {
			
			@Override
			public void buttonSelectedIndexChanged(Object data, int index) {
				if(onOspadRenderListener!=null)
				{
					onOspadRenderListener.OnItemSelectedChanged(data);
				}
			}

			@Override
			public void onLoadingButtonList(OSButton view) {
				if(onOspadRenderListener!=null)
				{
					onOspadRenderListener.onLoadingButtonList(view);
				}
			}
		});
    }*/
    
    public void initRender(AppType type, SnakeAdapter<OsListViewItem> adapter, Bitmap background, Bitmap dummyIcon, Bitmap defaultIcon, Bitmap defaultImageDim)
    {
		mRenderType = type;
		mDummyImage = dummyIcon;
		mBackgroundImage = background;
		mDefaultImage = defaultIcon;
		mDefaultImageDim = defaultImageDim;
		mAdapter = adapter;
		mSnackCtrl = new SnakeShapeCtrl(mRenderType, adapter,  mDummyImage, mDefaultImage , mDefaultImageDim);
		
        mSnackCtrl.setOnSnackCtrlListener(new SnakeShapeCtrl.SnackCtrlListener() {
			
			@Override
			public void buttonSelectedIndexChanged(Object data, int index) {
				Log.d(TAG, "buttonSelectedIndexChanged " + data);
				if(onOspadRenderListener!=null)
				{
					onOspadRenderListener.OnItemSelectedChanged(data);
				}
			}

			@Override
			public void onLoadingButtonList(OSButton view) {
				if(onOspadRenderListener!=null)
				{
					onOspadRenderListener.onLoadingButtonList(view);
				}
			}

			@Override
			public void onFlingStop() {
				if (mSnackCtrl != null) {
					if (mSnackCtrl.getSnackCtrlState() == SnackCtrlState.LEVEL4) {
						mSnackCtrl.setFlingToRightPosition();
					}
				}
				
			}
		});
    }
    
//    public void setBackgroundImage(Bitmap bmp)
//    {
//    	mBackgroundImage = bmp;
//    }
  
/*	public List<String> getContentPathPool() {
		if (mSnackCtrl == null) {
			return null;
		}
		return mSnackCtrl.getContentPathPool();
	}*/
    
/*    public void setAppFiltering(String[] appsCategory) {
    	if (mSnackCtrl != null) { 
    		mSnackCtrl.setAppFiltering(appsCategory);
    	} else {
    		//Log.i("database", "mSnackCtrl");
    	}
    }*/
    
/*    public void setAppItemList(List<OsListViewItem> list) {
    	if (mSnackCtrl != null) { 
    		mSnackCtrl.setAppItemList(list);
    	} else {
    		//Log.i("database", "mSnackCtrl");
    	}
    }*/
    
/*    public void setAppFiltering(String[] gameList, String[] blockList)
    {
    	if (mSnackCtrl != null) { 
    		mSnackCtrl.setAppFiltering(gameList, blockList);
    	} else {
    		//Log.i("database", "mSnackCtrl");
    	}
    }*/
    
    public void moveItemToSelected(float x , float y)
    {
    	if(x>280 && x<450 &&  y>150 && y<350)
    	{
    		if(mSnackCtrl.move1Item())
    		{
	    		mSnackCtrl.setActionState(ActionState.Fling);
	        	mSnackCtrl.resetNextFlingTime();
    		}
    	}
    	else if(x>200 && x<265 && y>150 && y< 300)
    	{
    		if(mSnackCtrl.move2Item())
    		{
	    		mSnackCtrl.setActionState(ActionState.Fling);
	        	mSnackCtrl.resetNextFlingTime();
    		}
    	}
    	
    }
    
    /**
     * action event when touch up
     * @author aaronli at Jun26 2013
     */
    public void handleMouseUpEvent(float x, float y) {
    	//Log.d(TAG, "handleMouseUpEvent");
    	if (mSnackCtrl.getActionState() == ActionState.Scroll) {
    		mSnackCtrl.scrollToFling();
    	}
    }
    
    public void deleteSelectedItem() {
    	mSnackCtrl.deleteSelectedItem();
    }
    
    public void renameSelectedItem() {
    	mSnackCtrl.renameSelectedItem();
    }
  
    public interface OspadRenderListener
    {
    	public abstract void OnSurfaceCreated();
    	public abstract void OnSystemStateChanged(SystemState state);
    	public abstract void OnItemSelectedChanged(Object itemData);
    	
    	public void onLoadingButtonList(OSButton view);
    }
    
}
