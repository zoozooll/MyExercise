#pragma version(1)
#pragma rs java_package_name(com.aaron.fallsnow)

#include "snow.rsh"
#define LEAF_SIZE 0.55f
// Things we need to set from the application
float g_glWidth;
float g_glHeight;
float g_meshWidth;
float g_meshHeight;
float g_xOffset;
float g_rotate;

rs_program_vertex g_PVWater;
rs_program_vertex g_PVSky;

rs_program_fragment g_PFSky;
rs_program_fragment g_PFBackground;

rs_allocation g_TRiverbed;

rs_mesh g_WaterMesh;

Constants_t *g_Constants;
rs_program_store g_PFSBackground;

static float g_DT;
static int64_t g_LastTime;
static Drop_t gDrops[MAX_DROP];

void init() {
    int ct;
    //gMaxDrops = 10;
    for (ct=0; ct<MAX_DROP; ct++) {
        gDrops[ct].ampS = 0;
        gDrops[ct].ampE = 0;
        gDrops[ct].spread = 1;
    }

    //initLeaves();
    g_LastTime = rsUptimeMillis();
    g_DT = 0.1f;
}

static void updateDrop(int ct) {
    gDrops[ct].spread += 30.f * g_DT;
    gDrops[ct].ampE = gDrops[ct].ampS / gDrops[ct].spread;
}

static void drop(int x, int y, float s) {
	
    int ct;
    int iMin = 0;
    float minAmp = 100.f;
    for (ct = 0; ct < MAX_DROP; ct++) {
        if (gDrops[ct].ampE < minAmp) {
            iMin = ct;
            minAmp = gDrops[ct].ampE;
        }
    }
    gDrops[iMin].ampS = s;
    gDrops[iMin].spread = 0;
    gDrops[iMin].x = x;
    gDrops[iMin].y = g_meshHeight - y - 1;
    updateDrop(iMin);
}

static void generateRipples() {
    int ct;
    for (ct = 0; ct < MAX_DROP; ct++) {
        Drop_t * d = &gDrops[ct];
        float *v = (float*)&g_Constants->Drop01;
        v += ct*4;
        *(v++) = d->x;
        *(v++) = d->y;
        *(v++) = d->ampE * 0.12f;
        *(v++) = d->spread;
    }
    g_Constants->Offset.x = g_xOffset;

    for (ct = 0; ct < MAX_DROP; ct++) {
        updateDrop(ct);
    }
}

/* static void drawLeaves() {
    rsgBindProgramFragment(g_PFSky);
    //rsgBindProgramStore(g_PFSLeaf);
    rsgBindProgramVertex(g_PVSky);
    //rsgBindTexture(g_PFSky, 0, g_TLeaves);

    int newLeaves = 0;
    int i = 0;

    rs_matrix4x4 matrix;
    rsMatrixLoadIdentity(&matrix);
    rsgProgramVertexLoadModelMatrix(&matrix);
} */

static void drawRiverbed() {
	
    rsgBindProgramFragment(g_PFBackground);
    rsgBindProgramStore(g_PFSBackground);
	 //rsgBindProgramVertex(g_PVSky);
	
    rsgBindTexture(g_PFBackground, 0, g_TRiverbed);
	rsgDrawMesh(g_WaterMesh);
    
	//rsgProgramFragmentConstantColor(g_PFBackground, 1.0f, 1.0f, 1.0f, 1.0f);
	
	//rsMatrixTranslate(&matrix, 0, 0, 0);
	// rsMatrixRotate(&matrix, 90.0f, 0.0f, 0.0f, 1.0f);
	/* rsgProgramVertexLoadModelMatrix(&matrix);
	rsgDrawQuadTexCoords(0, 0, 0, 0.5f, 1.0f,
                       1.0f, 0, 0, 0.5f, 1.0f,
                      1.0f,  1.0f, 0, 0.5f, 0.0f, 
                      0,  1.0f, 0, 0.5f, 0.0f);  */
	
} 

void addDrop(int x, int y) {
    drop(x, y, 2);
}

int root(void) {
    rsgClearColor(0.f, 0.f, 0.f, 0.f);
    // Compute dt in seconds.
    int64_t newTime = rsUptimeMillis();
    g_DT = (newTime - g_LastTime) * 0.001f;
    g_DT = min(g_DT, 0.2f);
    g_LastTime = newTime;
    g_Constants->Rotate = (float) g_rotate;
    int ct;
    int add = 0;
    for (ct = 0; ct < MAX_DROP; ct++) {
        if (gDrops[ct].ampE < 0.005f) {
            add = 1;
        }
    }
    rsgBindProgramVertex(g_PVWater);	
    generateRipples();
    drawRiverbed();
    return 50;
}