//#define LEAVES_TEXTURES_COUNT 8
//#define LEAF_SIZE 0.55f
//#define LEAVES_COUNT 14 
#define MAX_DROP 10
#define DEBUG 1

#include "rs_graphics.rsh"

typedef struct Constants {
    float4 Drop01;
    float4 Drop02;
    float4 Drop03;
    float4 Drop04;
    float4 Drop05;
    float4 Drop06;
    float4 Drop07;
    float4 Drop08;
    float4 Drop09;
    float4 Drop10;
    float4 Offset;
    float Rotate;
} Constants_t;

typedef struct Drop {
    float ampS;
    float ampE;
    float spread;
    float x;
    float y;
} Drop_t;

void addDrop(int x, int y);
