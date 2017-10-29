package com.vstar3d.View;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.google.vrtoolkit.cardboard.CardboardView;
import com.google.vrtoolkit.cardboard.Eye;
import com.google.vrtoolkit.cardboard.HeadTransform;
import com.google.vrtoolkit.cardboard.Viewport;
import com.vstar3d.Obj.AppObj;
import com.vstar3d.Obj.Base;
import com.vstar3d.Obj.MenuObj;
import com.vstar3d.VRLauncher.MainActivity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Point;
//import android.hardware.display.DisplayManager;
import android.os.Build;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.os.Handler;

@SuppressLint("NewApi")
public class HomeView extends CardboardView {

    private static final String TAG = "3dvstar";
    private VrRender mRenderer;
    private MenuObj mMenuObj;
    private AppObj mAppObj;
    private Activity mainActivity;
    Context mContext;
    public void onPause() {
        super.onPause();

    }

    public void onResume() {
        super.onResume();

    }

    public void sendResetMessage(){
        mRenderer.sendMessage();
    }

    public void setActivity(Activity activity) {
        this.mainActivity = activity;
    }

    private void init(Context context) {
        setEGLContextClientVersion(2);
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        setSettingsButtonEnabled(false);
        setAlignmentMarkerEnabled(false);
        setDistortionCorrectionEnabled(false);
        setVRModeEnabled(true);
        mContext=context;
        mRenderer = new VrRender(context);
        setRenderer(mRenderer);
    }

    public HomeView(Context context) {
        super(context);
        init(context);
        // TODO Auto-generated constructor stub
    }

    public HomeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private class VrRender implements CardboardView.Renderer {

        private Context mContext = null;
        private int mWidth = 0;
        private int mHeight = 0;
        private int mProgram2d;
        private int mMVPMatrixHandle;
        private int attribPosition;
        private int attribTexCoord;
        private int textureId;
        private int textureIdR;
        private FloatBuffer textureBuffer;
        private FloatBuffer vertexBuffer;
        private ShortBuffer IndicesBuffer;

        public VrRender(Context context) {
            mContext = context;
        }

        public void sendMessage() {
            myHandler.sendEmptyMessage(0);
        }

        boolean isSended = false;
        Handler myHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 0){
                    isSended = true;
                }
            }
        };

        private final String vertexShaderCode =
                "uniform mat4 u_MVPMatrix;" +
                        "attribute vec4 a_position;" +
                        "attribute vec2 a_texCoord;" +
                        "varying vec2 v_texCoord;" +
                        "void main()" +
                        "{" +
                        "    gl_Position = u_MVPMatrix * a_position;" +//"   gl_Position = modelViewProjectionMatrix * position;
                        "    v_texCoord = a_texCoord;" +
                        "}";


        private final String fragmentShaderCode =
                "precision lowp float;" +
                        "varying vec2 v_texCoord;" +
                        "uniform sampler2D u_samplerTexture;" +
                        "void main()" +
                        "{" +
                        "    gl_FragColor = texture2D(u_samplerTexture, v_texCoord);" +
                        "}";

        private int loadShader(int shaderType, String source) {
            int shader = GLES20.glCreateShader(shaderType);
            if (shader != 0) {
                GLES20.glShaderSource(shader, source);
                GLES20.glCompileShader(shader);
                int[] compiled = new int[1];
                GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
                if (compiled[0] == 0) {
                    Log.e(TAG, "Could not compile shader " + shaderType + ":");
                    Log.e(TAG, GLES20.glGetShaderInfoLog(shader));
                    GLES20.glDeleteShader(shader);
                    shader = 0;
                }
            }
            return shader;
        }

        private int createProgram(String vertexSource, String fragmentSource) {
            int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexSource);
            if (vertexShader == 0) {
                return 0;
            }
            int pixelShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource);
            if (pixelShader == 0) {
                return 0;
            }

            int program = GLES20.glCreateProgram();
            if (program != 0) {
                GLES20.glAttachShader(program, vertexShader);
                checkGlError("glAttachShader");
                GLES20.glAttachShader(program, pixelShader);
                checkGlError("glAttachShader");
                GLES20.glLinkProgram(program);
                int[] linkStatus = new int[1];
                GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
                if (linkStatus[0] != GLES20.GL_TRUE) {
                    Log.e(TAG, "Could not link program: ");
                    Log.e(TAG, GLES20.glGetProgramInfoLog(program));
                    GLES20.glDeleteProgram(program);
                    program = 0;
                }
            }
            return program;
        }

        private int loadTexture(String ImgName) {

            int[] textureId = new int[1];
            // Generate a texture object
            GLES20.glGenTextures(1, textureId, 0);

            Bitmap bitmap = Base.loadImageAssets(mContext, ImgName);

            // Bind to the texture in OpenGL
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
            // Load the bitmap into the bound texture.
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
            // Recycle the bitmap, since its data has been loaded into OpenGL.
            if (bitmap != null)
                bitmap.recycle();

            return textureId[0];
        }

        int numVertices = 0;
        int numIndices = 0;

        private int esGenSphere(int numSlices, float d) {
            int i;
            int j;
            int iidex = 0;
            int numParallels = numSlices / 2;
            numVertices = (numParallels + 1) * (numSlices + 1);
            numIndices = numParallels * numSlices * 6;
            float angleStep = (float) ((2.0f * Math.PI) / ((float) numSlices));
            float vertices[] = new float[numVertices * 3];
            float texCoords[] = new float[numVertices * 2];
            short indices[] = new short[numIndices];
            for (i = 0; i < numParallels + 1; i++) {
                for (j = 0; j < numSlices + 1; j++) {
                    int vertex = (i * (numSlices + 1) + j) * 3;
                    vertices[vertex + 0] = (float) (d * Math.sin(angleStep * (float) i) * Math.sin(angleStep * (float) j));
                    vertices[vertex + 1] = (float) (d * Math.cos(angleStep * (float) i));
                    vertices[vertex + 2] = (float) (d * Math.sin(angleStep * (float) i) * Math.cos(angleStep * (float) j));

                    int texIndex = (i * (numSlices + 1) + j) * 2;
                    texCoords[texIndex + 0] = 1.0f - (float) j / (float) numSlices;
                    texCoords[texIndex + 1] = ((float) i / (float) numParallels);//((float)i/(float)numParallels);//
                }
            }

            for (i = 0; i < numParallels; i++) {
                for (j = 0; j < numSlices; j++) {
                    indices[iidex++] = (short) (i * (numSlices + 1) + j);
                    indices[iidex++] = (short) ((i + 1) * (numSlices + 1) + j);
                    indices[iidex++] = (short) ((i + 1) * (numSlices + 1) + (j + 1));

                    indices[iidex++] = (short) (i * (numSlices + 1) + j);
                    indices[iidex++] = (short) ((i + 1) * (numSlices + 1) + (j + 1));
                    indices[iidex++] = (short) (i * (numSlices + 1) + (j + 1));
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

            GLES20.glEnableVertexAttribArray(attribPosition);
            GLES20.glEnableVertexAttribArray(attribTexCoord);
//		GLES20.glEnable(GLES20.GL_BLEND);
//		GLES20.glBlendFunc(GLES20.GL_ONE,GLES20.GL_ONE_MINUS_SRC_ALPHA);
//		GLES20.glDrawElements(GLES20.GL_TRIANGLES,6,GLES20.GL_UNSIGNED_SHORT,1);
//		GLES20.glEnable(GLES20.GL_BLEND);
            //   GLES20.glEnableVertexAttribArray(maPositionHandle);

            return numIndices;
        }

        private void checkGlError(String op) {
            int error;
            while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
                //    Log.e(TAG, op + ": glError " + error);
                throw new RuntimeException(op + ": glError " + error);
            }
        }

        void perspectiveM(float[] m, float yFovInDegress, float aspect, float n, float f) {
            final float angleInRadians = (float) (yFovInDegress * Math.PI / 180.0);
            final float a = (float) (1.0 / Math.tan(angleInRadians / 2.0));
            m[0] = a / aspect;
            m[1] = 0f;
            m[2] = 0f;
            m[3] = 0f;

            m[4] = 0f;
            m[5] = a;
            m[6] = 0f;
            m[7] = 0f;

            m[8] = 0f;
            m[9] = 0f;
            m[10] = -((f + n) / (f - n));
            m[11] = -1f;

            m[12] = 0f;
            m[13] = 0f;
            m[14] = -((2f * f * n) / (f - n));
            m[15] = 0f;
        }

        float[] projectionMatrix = new float[16];
        float[] modelViewMatrix = new float[16];
        final float[] temp = new float[16];

        public void update(float[] headView) {
             perspectiveM(projectionMatrix, 75.0f, mWidth / 2.0f / mHeight, 0.1f, 400.0f);
             Matrix.setIdentityM(modelViewMatrix, 0);
             //Matrix.rotateM(modelViewMatrix, 0, 90.0f, 0, 0, 1);
             float[] t = new float[16];
             Matrix.multiplyMM(t, 0, projectionMatrix, 0, modelViewMatrix, 0);
             Matrix.multiplyMM(temp, 0, t, 0, headView, 0);
             Matrix.translateM(temp, 0, 0.035f, 0.0f, 0.0f);
             GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, temp, 0);
             //mainActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        public void updateR() {
//			Matrix.translateM(temp, 0, -0.02f, 0.0f, 0.0f);
            Matrix.translateM(temp, 0, -0.035f, 0.0f, 0.0f);
            GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, temp, 0);
        }

        @Override
        public void onDrawFrame(HeadTransform headTransform, Eye eye, Eye eye1) {
            float[] headView = new float[16];
            float[] EulerAngles = new float[3];
            headTransform.getHeadView(headView, 0);
            headTransform.getEulerAngles(EulerAngles, 0);

            //Log.e("3dvstar","EulerAngles "+EulerAngles[0]+" , "+EulerAngles[1]+" , "+EulerAngles[2]);

            GLES20.glClearColor(0.0f, 1.0f, 0.0f, 1.0f);
            GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

            // Load the program, which is the basics rules to draw the vertexes and textures.
            GLES20.glUseProgram(mProgram2d);
            checkGlError("glUseProgram");

            // Activate the texture.
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            GLES20.glVertexAttribPointer(attribPosition, 3,
                    GLES20.GL_FLOAT, false,
                    12, vertexBuffer);
            GLES20.glVertexAttribPointer(attribTexCoord, 2,
                    GLES20.GL_FLOAT, false,
                    8, textureBuffer);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);

            update(headView);

            GLES20.glViewport(0, 0, mWidth / 2, mHeight);
            GLES20.glDrawElements(GLES20.GL_TRIANGLES, numIndices, GLES20.GL_UNSIGNED_SHORT, IndicesBuffer);
            if (mAppList) {
                mAppObj.Draw(attribPosition, attribTexCoord, EulerAngles, temp, mMVPMatrixHandle,true);
            } else {
                mMenuObj.Draw(attribPosition, attribTexCoord, EulerAngles, temp, mMVPMatrixHandle,true);
            }
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureIdR);
            GLES20.glViewport(mWidth / 2, 0, mWidth / 2, mHeight);

            GLES20.glVertexAttribPointer(attribPosition, 3,
                    GLES20.GL_FLOAT, false,
                    12, vertexBuffer);
            GLES20.glVertexAttribPointer(attribTexCoord, 2,
                    GLES20.GL_FLOAT, false,
                    8, textureBuffer);
            // GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);

            GLES20.glDrawElements(GLES20.GL_TRIANGLES, numIndices, GLES20.GL_UNSIGNED_SHORT, IndicesBuffer);

            updateR();
            if (mAppList) {

                mAppObj.Draw(attribPosition, attribTexCoord, EulerAngles, temp, mMVPMatrixHandle,false);
            } else {
                mMenuObj.Draw(attribPosition, attribTexCoord, EulerAngles, temp, mMVPMatrixHandle,false);
            }
        }

        @Override
        public void onFinishFrame(Viewport viewport) {
            //
        }

        @Override
        public void onSurfaceChanged(int width, int height) {
            mWidth = width;
            mHeight = height;
            GLES20.glViewport(0, 0, width / 2, height);
            //Log.e("3dvstar","3dvstar surface "+mWidth+","+mHeight);
        }

        @Override
        public void onSurfaceCreated(EGLConfig eglConfig) {
            // TODO Auto-generated method stub
            mProgram2d = createProgram(vertexShaderCode, fragmentShaderCode);
            if (mProgram2d == 0) {
                return;
            }

            attribPosition = GLES20.glGetAttribLocation(mProgram2d, "a_position");

            attribTexCoord = GLES20.glGetAttribLocation(mProgram2d, "a_texCoord");

            //    uniformTexture = GLES20.glGetUniformLocation(mProgram, "u_samplerTexture");

            mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram2d, "u_MVPMatrix");

            textureId = loadTexture("l.jpg");
            textureIdR = loadTexture("r.jpg");
            esGenSphere(200, 1.0f);

            mMenuObj = new MenuObj(mContext, mBv, mWv, mHandler); //l t r b,xParallels,yParallels,z
            mAppObj = new AppObj(mContext, mBv, mWv, mHandler);

        }

        @Override
        public void onRendererShutdown() {

        }
    }

    private int mBv = 100;

    public void SetBv(int bv) {
        mBv = bv;
        if (mMenuObj != null)
            mMenuObj.SetBv(mBv);
    }

    private int mWv = 0;

    public void SetWv(int wv) {
        mWv = wv;
        if (mMenuObj != null)
            mMenuObj.SetWv(mWv);

    }

    public void setBluetoothStatus(boolean status) {
        if (mMenuObj != null)
            mMenuObj.setBluetoothStatus(status);
    }

    public void updateTime() {
        if (mMenuObj != null)
            mMenuObj.updateTime();
    }

    private Handler mHandler = null;

    public void SetHandler(Handler mHandler) {
        // TODO Auto-generated method stub
        this.mHandler = mHandler;
        if (mMenuObj != null)
            mMenuObj.SetHandler(mHandler);
    }

    boolean mAppList = false;

    public void ShowAppList(boolean b) {
        mAppList = b;
    }

    public void LastApp() {
        if (mAppList) {
            if (mAppObj != null)
                mAppObj.LastApp();
        }

    }

    public void NextApp() {
        if (mAppList) {
            if (mAppObj != null)
                mAppObj.NextApp();
        }

    }

    public void startActivity(int i) {
        if (mAppList) {
            if (mAppObj != null)
                mAppObj.startActivity(i);
        }

    }


}
