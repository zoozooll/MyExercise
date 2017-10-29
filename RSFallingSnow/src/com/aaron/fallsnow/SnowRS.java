package com.aaron.fallsnow;

import static android.renderscript.ProgramStore.DepthFunc.ALWAYS;
import static android.renderscript.Sampler.Value.CLAMP;
import static android.renderscript.Sampler.Value.LINEAR;

import java.security.acl.LastOwnerException;
import java.util.Random;
import java.util.TimeZone;

import android.app.WallpaperManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.renderscript.Allocation;
import android.renderscript.Allocation.MipmapControl;
import android.renderscript.Matrix4f;
import android.renderscript.Mesh;
import android.renderscript.ProgramFragment;
import android.renderscript.ProgramFragmentFixedFunction;
import android.renderscript.ProgramStore;
import android.renderscript.ProgramVertex;
import android.renderscript.ProgramVertexFixedFunction;
import android.renderscript.RenderScriptGL;
import android.renderscript.Sampler;
import android.renderscript.ScriptC;
import android.renderscript.ProgramStore.BlendDstFunc;
import android.renderscript.ProgramStore.BlendSrcFunc;
import android.util.Log;

public class SnowRS {
	 private static final int MESH_RESOLUTION = 48;

    private static final int RSID_STATE = 0;
    private static final int RSID_CONSTANTS = 1;
    private static final int RSID_DROP = 2;

    private static final int TEXTURES_COUNT = 2;
    private static final int RSID_TEXTURE_RIVERBED = 0;
    private static final int RSID_TEXTURE_LEAVES = 1;
    
    private final BitmapFactory.Options mOptionsARGB = new BitmapFactory.Options();

    @SuppressWarnings({"FieldCanBeLocal"})
    private ProgramFragment mPfBackground;
    @SuppressWarnings({"FieldCanBeLocal"})
    private ProgramFragment mPfSky;
    @SuppressWarnings({"FieldCanBeLocal"})
    private ProgramStore mPfsBackground;
    //@SuppressWarnings({"FieldCanBeLocal"})
    //private ProgramStore mPfsLeaf;
    @SuppressWarnings({"FieldCanBeLocal"})
    private ProgramVertex mPvSky;
    @SuppressWarnings({"FieldCanBeLocal"})
    private ProgramVertex mPvWater;
    private ProgramVertexFixedFunction.Constants mPvOrthoAlloc;
    @SuppressWarnings({"FieldCanBeLocal"})
    private Sampler mSampler;

    private int mMeshWidth;
    private Allocation mUniformAlloc;

    private int mMeshHeight;
    @SuppressWarnings({"FieldCanBeLocal"})
    private Mesh mMesh;
    private WorldState mWorldState;

    private ScriptC_snow mScript;

    private ScriptField_Constants mConstants;

    private float mGlHeight;

	private int mWidth;

	private int mHeight;

	private RenderScriptGL mRS;

	private Resources mResources;

	private boolean mPreview;
	
	private Bitmap mBackBitmap;
    
	public SnowRS(int width, int height) {
        mWidth = width;
        mHeight = height;
    }

	public void stop() {
        mRS.bindRootScript(null);
    }
 
    public void start() {
        mRS.bindRootScript(mScript);
    }
     
    public void init(RenderScriptGL rs, Resources res, boolean isPreview) {
        mRS = rs;
        mResources = res;
        mPreview = isPreview;
        mScript = (ScriptC_snow) createScript();
        createMesh();
        createState();
        createProgramVertex();
        createProgramFragmentStore();
        createProgramFragment();
        loadTextures();

        mScript.bind_g_Constants(mConstants);
        mScript.setTimeZone(TimeZone.getDefault().getID());
        //renderSnow();
    }
     
    public RenderScriptGL getRS() {
        return mRS;
    }
     
    public Resources getResources() {
        return mResources;
    }
     
    public ScriptC createScript() {
        ScriptC_snow script;
        script = new ScriptC_snow(mRS, getResources(), R.raw.snow);
        
        return script;
    }
    
    public void setmBackBitmap(Bitmap backBitmap) {
		this.mBackBitmap = backBitmap;
	}
    
    private void createMesh() {
        Mesh.TriangleMeshBuilder tmb = new Mesh.TriangleMeshBuilder(mRS, 2, 0);

        final int width = mWidth > mHeight ? mHeight : mWidth;
        final int height = mWidth > mHeight ? mWidth : mHeight;

        int wResolution = MESH_RESOLUTION;
        int hResolution = (int) (MESH_RESOLUTION * height / (float) width);

        mGlHeight = 2.0f * height / (float) width;

        wResolution += 2;
        hResolution += 2;

        for (int y = 0; y <= hResolution; y++) {
            final float yOffset = (((float)y / hResolution) * 2.f - 1.f) * height / width;
            for (int x = 0; x <= wResolution; x++) {
                tmb.addVertex(((float)x / wResolution) * 2.f - 1.f, yOffset);
            }
        }

        for (int y = 0; y < hResolution; y++) {
            final boolean shift = (y & 0x1) == 0;
            final int yOffset = y * (wResolution + 1);
            for (int x = 0; x < wResolution; x++) {
                final int index = yOffset + x;
                final int iWR1 = index + wResolution + 1;
                if (shift) {
                    tmb.addTriangle(index, index + 1, iWR1);
                    tmb.addTriangle(index + 1, iWR1 + 1, iWR1);
                } else {
                    tmb.addTriangle(index, iWR1 + 1, iWR1);
                    tmb.addTriangle(index, index + 1, iWR1 + 1);
                }
            }
        }

        mMesh = tmb.create(true);

        mMeshWidth = wResolution + 1;
        mMeshHeight = hResolution + 1;

        mScript.set_g_WaterMesh(mMesh);
    }

    static class WorldState {
        public int frameCount;
        public int width;
        public int height;
        public int meshWidth;
        public int meshHeight;
        public int rippleIndex;
        public float glWidth;
        public float glHeight;
        public float skySpeedX;
        public float skySpeedY;
        public int rotate;
        public int isPreview;
        public float xOffset;
    }

    private void createState() {
        mWorldState = new WorldState();
        mWorldState.width = mWidth;
        mWorldState.height = mHeight;
        mWorldState.meshWidth = mMeshWidth;
        mWorldState.meshHeight = mMeshHeight;
        mWorldState.rippleIndex = 0;
        mWorldState.glWidth = 2.0f;
        mWorldState.glHeight = mGlHeight;
        mWorldState.skySpeedX = random(-0.001f, 0.001f);
        mWorldState.skySpeedY = random(0.00008f, 0.0002f);
        //mWorldState.rotate = mWidth > mHeight ? 1 : 0;
        mWorldState.rotate = 1;
        mWorldState.isPreview = mPreview ? 1 : 0;

        mScript.set_g_glWidth(mWorldState.glWidth);
        mScript.set_g_glHeight(mWorldState.glHeight);
        mScript.set_g_meshWidth(mWorldState.meshWidth);
        mScript.set_g_meshHeight(mWorldState.meshHeight);
        mScript.set_g_xOffset(0);
        mScript.set_g_rotate(mWorldState.rotate);
    }

    private void loadTextures() {
        //mScript.set_g_TLeaves(loadTextureARGB(R.drawable.leaves));
        mScript.set_g_TRiverbed(loadTextureBitmap());
    }

    private Allocation loadTexture(int id) {
        final Allocation allocation = Allocation.createFromBitmapResource(mRS, mResources, id);
        return allocation;
    }

    private Allocation loadTextureARGB(int id) {
        Bitmap b = BitmapFactory.decodeResource(mResources, id, mOptionsARGB);
        final Allocation allocation = Allocation.createFromBitmap(mRS, b);
        return allocation;
    }
    
	private Allocation loadTextureBitmap() {
        final Allocation allocation = Allocation.createFromBitmap(mRS, mBackBitmap,MipmapControl.MIPMAP_ON_SYNC_TO_TEXTURE, Allocation.USAGE_GRAPHICS_TEXTURE);
        return allocation;
    }

    private void createProgramFragment() {
        /*Sampler.Builder sampleBuilder = new Sampler.Builder(mRS);
        sampleBuilder.setMinification(LINEAR);
        sampleBuilder.setMagnification(LINEAR);
        sampleBuilder.setWrapS(CLAMP);
        sampleBuilder.setWrapT(CLAMP);
        mSampler = sampleBuilder.create();*/

    	mSampler = Sampler.CLAMP_LINEAR(mRS);
        ProgramFragmentFixedFunction.Builder builder = new ProgramFragmentFixedFunction.Builder(mRS);
        builder.setTexture(ProgramFragmentFixedFunction.Builder.EnvMode.REPLACE,
                           ProgramFragmentFixedFunction.Builder.Format.RGBA, 0);
        mPfBackground = builder.create();
        mPfBackground.bindSampler(mSampler, 0);
        mScript.set_g_PFBackground(mPfBackground);

        builder = new ProgramFragmentFixedFunction.Builder(mRS);
        builder.setTexture(ProgramFragmentFixedFunction.Builder.EnvMode.MODULATE,
                           ProgramFragmentFixedFunction.Builder.Format.RGBA, 0);
        mPfSky = builder.create();
        mPfSky.bindSampler(mSampler, 0);

        mScript.set_g_PFSky(mPfSky);
    }

    private void createProgramFragmentStore() {
        ProgramStore.Builder builder = new ProgramStore.Builder(mRS);
        builder.setDepthFunc(ALWAYS);
        builder.setBlendFunc(BlendSrcFunc.SRC_ALPHA, BlendDstFunc.ONE_MINUS_SRC_ALPHA);
        builder.setDitherEnabled(false);
        builder.setDepthMaskEnabled(false);
        mPfsBackground = builder.create();

        /*builder = new ProgramStore.Builder(mRS);
        builder.setDepthFunc(ALWAYS);
        builder.setBlendFunc(BlendSrcFunc.SRC_ALPHA, BlendDstFunc.ONE_MINUS_SRC_ALPHA);
        builder.setDitherEnabled(false);
        builder.setDepthMaskEnabled(true);
        mPfsLeaf = builder.create();*/

        //mScript.set_g_PFSLeaf(mPfsLeaf);
        mScript.set_g_PFSBackground(mPfsBackground);
    }

    private void createProgramVertex() {
        mPvOrthoAlloc = new ProgramVertexFixedFunction.Constants(mRS);
        Matrix4f proj = new Matrix4f();
        proj.loadProjectionNormalized(mWidth, mHeight);
        mPvOrthoAlloc.setProjection(proj);


        ProgramVertexFixedFunction.Builder builder = new ProgramVertexFixedFunction.Builder(mRS);
        mPvSky = builder.create();
        ((ProgramVertexFixedFunction)mPvSky).bindConstants(mPvOrthoAlloc);

        mScript.set_g_PVSky(mPvSky);

        mConstants = new ScriptField_Constants(mRS, 1);
        mUniformAlloc = mConstants.getAllocation();

        ProgramVertex.Builder sb = new ProgramVertex.Builder(mRS);

        String t = "\n" +
                "varying vec4 varColor;\n" +
                "varying vec2 varTex0;\n" +

                "vec2 addDrop(vec4 d, vec2 pos, float dxMul) {\n" +
                "  vec2 ret = vec2(0.0, 0.0);\n" +
                "  vec2 delta = d.xy - pos;\n" +
                "  delta.x *= dxMul;\n" +
                "  float dist = length(delta);\n" +
                "  if (dist < d.w) { \n" +
                "    float amp = d.z * dist;\n" +
                "    amp /= d.w * d.w;\n" +
                "    amp *= sin(d.w - dist);\n" +
                "    ret = delta * amp;\n" +
                "  }\n" +
                "  return ret;\n" +
                "}\n" +

                "void main() {\n" +
                "  vec2 pos = ATTRIB_position.xy;\n" +
                "  gl_Position = vec4(pos.x, pos.y, 0, 1.0);\n" +
                "  float dxMul = 1.0;\n" +

                "  varTex0 = vec2((pos.x + 1.0), (pos.y + 1.0));\n" +

                //"  if (UNI_Rotate < 0.9) {\n" +
                "    varTex0.xy *= vec2(0.50, 0.50);\n" +
                "    varTex0.x += UNI_Offset.x * 0.5;\n" +
                "    varTex0.y -= ATTRIB_position.y;\n" +
                "    pos.x += UNI_Offset.x * 3.0;\n" +
                //"  } else {\n" +
                //"    varTex0.xy *= vec2(0.5, 0.3125);\n" +
                //"    dxMul = 2.5;\n" +
                //"  }\n" +

                "  varColor = vec4(1.0, 1.0, 1.0, 1.0);\n" +
                "  pos.xy += vec2(1.0, 1.0);\n" +
                "  pos.xy *= vec2(25.0, 42.0);\n" +

                "  varTex0.xy += addDrop(UNI_Drop01, pos, dxMul);\n" +
                "  varTex0.xy += addDrop(UNI_Drop02, pos, dxMul);\n" +
                "  varTex0.xy += addDrop(UNI_Drop03, pos, dxMul);\n" +
                "  varTex0.xy += addDrop(UNI_Drop04, pos, dxMul);\n" +
                "  varTex0.xy += addDrop(UNI_Drop05, pos, dxMul);\n" +
                "  varTex0.xy += addDrop(UNI_Drop06, pos, dxMul);\n" +
                "  varTex0.xy += addDrop(UNI_Drop07, pos, dxMul);\n" +
                "  varTex0.xy += addDrop(UNI_Drop08, pos, dxMul);\n" +
                "  varTex0.xy += addDrop(UNI_Drop09, pos, dxMul);\n" +
                "  varTex0.xy += addDrop(UNI_Drop10, pos, dxMul);\n" +
                "}\n";

        sb.setShader(t);
        sb.addConstant(mUniformAlloc.getType());
        sb.addInput(mMesh.getVertexAllocation(0).getType().getElement());
        mPvWater = sb.create();
        mPvWater.bindConstants(mUniformAlloc, 0);

        mScript.set_g_PVWater(mPvWater);
    }

    void addDrop(float x, float y) {
        int dropX = (int) ((x / mWidth) * mMeshWidth);
        int dropY = (int) ((y / mHeight) * mMeshHeight);

        mScript.invoke_addDrop(dropX, dropY);
    }
    
    public static float random(float howsmall, float howbig) {

		if (howsmall >= howbig)
			return howsmall;

		return new Random().nextFloat() * (howbig - howsmall) + howsmall;

	}
  
}
