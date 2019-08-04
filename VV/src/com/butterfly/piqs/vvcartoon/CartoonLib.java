package com.butterfly.piqs.vvcartoon;

/*
 * vv 的漫画库
 * 
 */
public class CartoonLib {
	public static final int VV_SEX_MALE = 2;
	public static final int VV_SEX_FEMALE = 1;
	public static final int SUCESS = 0;
	public static final int FAILURE = 1;
	public static final int FINISH = 2;
	//画图的状态
	public static final int CARTOON_SUCESS = 0;
	public static final int CARTOON_FAILURE = 1;
	public static final int CARTOON_FINISH = 2;
	//开始准备数据
	public static final int CARTOON_START_PREPARE_MODEL_DATA = 3;
	//开始磨皮
	public static final int CARTOON_START_MOPI = 4;
	//开始画眉毛
	public static final int CARTOON_START_DRAW_EYEBROW = 5;
	//开始画眼睛
	public static final int CARTOON_START_DRAW_EYE = 6;
	//开始画鼻子
	public static final int CARTOON_START_DRAW_NOSE = 7;
	//开始画嘴巴
	public static final int CARTOON_START_DRAW_MOUTH = 8;
	//开始画胡子
	public static final int CARTOON_START_DRAW_MUSTACHE = 9;
	//准备画头发数据
	public static final int CARTOON_START_PREPAER_HAIR = 10;
	//开始画头发
	public static final int CARTOON_START_DRAW_HAIR = 11;
	//画头发返回无头发区域错误
	public static final int CARTOON_ERROR_NO_HAIR = 15;
	static {
		System.loadLibrary("piqsvvCartoon");
	}

	/*
	 * 判断漫画数据是否在准备中
	 * */
	public static boolean isPrepaer(int state) {
		if (state == CARTOON_START_PREPARE_MODEL_DATA
				|| state == CARTOON_START_MOPI) {
			return true;
		}
		return false;
	}
	
	/*
	 * 判断漫画数据是否在准备中
	 * */
	public static boolean isDrawing(int state) {
		if (state != CARTOON_SUCESS 
				&& state != CARTOON_FAILURE
				&& CARTOON_FINISH != state
				&& CARTOON_ERROR_NO_HAIR != state
				&& !isPrepaer(state)) {
			return true;
		}
		return false;
	}
	/*
	 * 设置漫画库的初始话
	 */
	public native static int InitLib(String sdcardpath, String dataPath);
	/*
	 * 设置性别
	 */
	public native static int setSex(int sex);
	// 设置需要处理的图片
	/*
	 * 
	 * 
	 * 
	 */
	public native static int setBitmapMat(long bm, int[] points);
	/* 设置特征三角形 */
	public native static int setPointsMat(int[] points, long bm);
	/*
	 * 动画获取当前画画的动作 bm 输出图像
	 */
	public native static int getcurrentActionMat(long in, int[] points);
	/*
	 * 修正头发区域时使用的 in 输入图像 out 输出图像 point 当前加入的点 返回值
	 */
	public native static int setAddpointdownMat(long m, int[] point);
	public native static int setAddpointmoveMat(long in, int[] point);
	public native static int setAddpointupMat(long in, int[] point);
	//圈选
	public native static int setAddContourMat(long in, int[] point);
	/*
	 * 修正头发区域时使用的 in 输入图像 out 输出图像 point 当前删除的点
	 */
	public native static int setDelpointdownMat(long in, int[] point);
	public native static int setDelpointmoveMat(long in, int[] point);
	public native static int setDelpointupMat(long in, int[] point);
	/*
	 * 修正的头发区域一OK bm 头发区域的掩码图
	 */
	public native static int setHairOKMat(long in);
	public native static int UndoMat(long in);
	public native static int RedoMat(long in);
	public native static int getOKMat(long bm);
	// ////////////////////////////TimeCamera///////////////////////////////////
	// 时光相机
	public native static int getSourceMat(long in);
	public native static int MaskAreaLineConnect(long in, int[] point);// 连线抠图
	public native static int MaskAreaPtGrowTouchDown(long in, int[] point);// 区域增长-按下
	public native static int MaskAreaPtGrowTouchMove(long in, int[] point);// 区域增长-移动
	public native static int MaskAreaPtGrowTouchUp(long in, int[] point);// 区域增长-弹起
	public native static int MaskAreaDeleteTouchDown(long in, int[] point);// 区域删除-按下
	public native static int MaskAreaDeleteTouchMove(long in, int[] point);// 区域删除-移动
	public native static int MaskAreaDeleteTouchUp(long in, int[] point);// 区域删除-弹起
	public native static int LastStation(long in);//上一步
	public native static int NextStation(long in);//下一步
	public native static int reurnNewMatWidthHeight(int[] point);//
	public native static int getNewSrcMat(long out);//
	//public native static int ProcessBrushing(long in);
	//public native static int psMatchColor(long backGround, long mask);
	public native static int ForeBackProc(long backGround, int[] point); //fore-back-match.
	public native static int ForeBackProcWithBackGround(long backGround,
			long out, int[] point); //fore-back-match.
	public native static int BlendProcGraduDtModif(long background, int[] point);//edge-gradually-change.fix the position.
	public native static int ProcessWatermark(long in);//加水印
	public native static int ProcessEnd();
	// ///////////////////////////////////////////////////////////////
}
