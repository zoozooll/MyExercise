/**
 * 
 */
package com.dvr.android.dvr.playback;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.dvr.android.dvr.DRVApp;
import com.dvr.android.dvr.bean.DrivePath;
import com.dvr.android.dvr.bean.DrivePosition;
import com.dvr.android.dvr.file.PathHelper;
import com.dvr.android.dvr.R;
///import com.google.android.maps.GeoPoint;
///import com.google.android.maps.MapActivity;
///import com.google.android.maps.MapController;
///import com.google.android.maps.MapView;
///import com.google.android.maps.Overlay;

/**
 * 视频或�?图片回放界面<BR>
 * [功能详细描述]
 * 
 * @author sunshine
 * @version [yecon Android_Platform, 20 Feb 2012]
 */
public class PlayBackActivity /*extends MapActivity*/ extends Activity implements OnPlayBackListener
{
    private static final String TAG = "PlayBackActivity";

    private static final int UPDATE_PLAY_TIME = 0;

    private static final int INIT_MAPVIEW = 1;

    private static final int POINT_COLOR = 0xA079D6F0;

  ///private MapView mMapView = null;

  ///private MapController mMapController = null;
    /**
     * 路径点集�?     */
    private ArrayList<DrivePosition> positions = new ArrayList<DrivePosition>();;

    private Point mDrawPoint = new Point();

    private PathEffect effect = null;

    private PlayBacker mPlayer = null;

    private SeekBar mSeekBar = null;

    private Toast mToast = null;

    /**
     * 坐标路径
     */
    private Path mPath = new Path();

    private ImageButton playBtn = null;

    /**
     * 经度�?     */
    private TextView infoLatitude = null;

    /**
     * 纬度�?     */
    private TextView infoLongitude = null;

    /**
     * 时间
     */
    private TextView infoTime = null;

    /**
     * 速度
     */
    private TextView infoSpeed = null;

    /**
     * �?���?�?��化按�?     */
    private ImageButton maxButton = null;

    private int mCurrentIndex = -1;

    private Paint pathPaint = new Paint();

    private Paint pointPaint = new Paint();

    private boolean lockMove = false;

    private boolean isStop = false;

    /**
     * 地图是否是全�?     */
    private boolean isMapFullScreen = false;

    /**
     * 回放视频路径
     */
    private String playbackFilePath = null;

    /**
     * 我的回放类型
     */
    private int mPlayType;

    private DRVApp app = null;
    
	private Intent mBRCIntent = new Intent("com.dvr.android.dvr.RECEIVER");  


    private Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                // 获取播放时间
                case UPDATE_PLAY_TIME:
                    updatePlayingTime(true);
                    break;
                case INIT_MAPVIEW:
                    //initMap();
                    break;
            }
        }
    };

    private OnClickListener clickListener = new OnClickListener()
    {
        public void onClick(View v)
        {
            switch (v.getId())
            {
                // 播放暂停按钮
                case R.id.playback_play_id:
                    doPlay();
                    break;
                // �?���?               
                    /*case R.id.playback_map_max_id:
                    doMax();
                    break;*/
            }
        }
    };

    private Runnable initRunnable = new Runnable()
    {
        public void run()
        {
            asyncInit();
        }
    };

    @Override
    protected void onCreate(Bundle icicle)
    {
        super.onCreate(icicle);
        this.setContentView(R.layout.playback);
        playbackFilePath = this.getIntent().getStringExtra(PlayBacker.PLAYBACK_KEY_VALUE_FILEPATH);
        mPlayType =
            this.getIntent().getIntExtra(PlayBacker.PLAYBACK_KEY_VALUE_FILETYPE, PlayBacker.TYPE_UNDEFINE);

        // playbackFilePath = "/sdcard/yeconDVR/regular/20120329055048.mp4";
        // mPlayType = PlayBacker.TYPE_VIDEO;

        if (null == playbackFilePath || mPlayType == PlayBacker.TYPE_UNDEFINE)
        {
            finish();
            return;
        }
        //
        if (mPlayType == PlayBacker.TYPE_VIDEO)
        {
            mPlayer = new VideoPlayBack(this);
        }
        else
        {
            mPlayer = new ImagePlayBack(this);
        }
        initView();
        app = (DRVApp) this.getApplication();
        // asyncInit();

        // 如果使用地图SDK，请初始化地图Activity
        // app.getBMapManager().start();
        new Thread(initRunnable).start();
        // initMap();
        startPlayBack(playbackFilePath);
        

    }

    /**
     * 异步初始�?     */
    private void asyncInit()
    {
        loadInfoXml();
        // Log.d("TAG", "size positons " + positions.size());
        mHandler.obtainMessage(INIT_MAPVIEW).sendToTarget();
    }

    /**
     * 初始化地�?     */
    /*private void initMap()
    {
        mMapView.setVisibility(View.VISIBLE);
        mMapController = mMapView.getController();
        mMapView.setEnabled(true);
        mMapView.setClickable(true);
        mMapView.setBuiltInZoomControls(true);
        mMapView.setTraffic(false);
        // 加载绘制�?        MyLocationOverlay myLocationOverlay = new MyLocationOverlay();
        List<Overlay> list = mMapView.getOverlays();
        list.add(myLocationOverlay);

        DrivePosition firstDrivePostion = findFirstValidPosition();
        if (null != firstDrivePostion)
        {
            if (-1 == mCurrentIndex)
            {
                // 现定位到记录的第�?��目标
                GeoPoint firstGPSPoint = gpsConvertGeoPoint(firstDrivePostion);

                // GeoPoint firstPoint = new GeoPoint((int)30.659259*1000000,(int)104.065762 * 1000000);
                mMapController.animateTo(firstGPSPoint);
                mMapView.getProjection().toPixels(firstGPSPoint, mDrawPoint);
                mMapController.setZoom(15);
                Log.d("TAG", "valid size positons " + positions.size());
            }
        }
        else
        {
            mMapController.setZoom(3);
        }
        effect = new CornerPathEffect(10);
    }*/

    public String getFileName(String pathandname){  
        
        int start=pathandname.lastIndexOf("/");  
        int end=pathandname.lastIndexOf(".");  
        if(start!=-1 && end!=-1){  
            return pathandname.substring(start+1,end);    
        }else{  
            return null;  
        }  
          
    } 
    
    private void loadInfoXml()
    {
        String xmlFileName = null;
        String playbackFileName;
        int index = playbackFilePath.lastIndexOf('.');
        playbackFileName = getFileName(playbackFilePath);
        if(playbackFileName.length() > 15)  //不可删除文件名如f20130110112033_l  len=17
 	    {
        	xmlFileName = playbackFilePath.substring(0, index - 2) + PathHelper.XML_SUFFIX;   
 	    }
        else
        {
        	xmlFileName = playbackFilePath.substring(0, index) + PathHelper.XML_SUFFIX;
        }
        if (null != xmlFileName)
        {
            DrivePath path = PathHelper.readPathFromXML(xmlFileName);
            if (null != path)
            {
                positions = path.getPathData();
            }
            // if(mPlayType == PlayBacker.TYPE_IMAGE)
            // {}
            // hideInfoView();
        }
    }

    private void doPlay()
    {
        if (mPlayer.getPlayerState() == OnPlayBackListener.PLAY)
        {
            mPlayer.pause();
        }
        else if (mPlayer.getPlayerState() == OnPlayBackListener.PAUSE)
        {
            replay();
        }
        else
        {
            mPlayer.start();
        }
    }

    private void doMax()
    {
        if (isMapFullScreen)
        {
            mapFullScreen(false);
        }
        else
        {
            mapFullScreen(true);
        }
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        isStop = true;
        if (null != mPlayer)
        {
            mPlayer.stop();
        }
        this.finish();
    }

    /**
     * 找到第一个数据有效点
     * 
     * @return
     */
    private DrivePosition findFirstValidPosition()
    {
        if (null == positions)
        {
            return null;
        }
        for (int i = 0; i < positions.size(); i++)
        {
            DrivePosition p = positions.get(i);
            Log.d(TAG, "find p = " + p.toString());
            // 如果数据有效返回
            if (p.mValid)
            {
                return p;
            }
        }
        return null;
    }

    /**
     * 地图是否全屏
     * 
     * @param full true全屏 false 非全�?     */
    private void mapFullScreen(boolean full)
    {
        // 地图全屏
        /*if (full)
        {
            this.findViewById(R.id.playback_player_id).setVisibility(View.GONE);
            this.findViewById(R.id.playback_center_line).setVisibility(View.GONE);
            this.findViewById(R.id.playback_id_info).setVisibility(View.GONE);
            this.findViewById(R.id.playback_right_line).setVisibility(View.GONE);
            isMapFullScreen = true;
        }
        else
        {
            this.findViewById(R.id.playback_player_id).setVisibility(View.VISIBLE);
            this.findViewById(R.id.playback_center_line).setVisibility(View.VISIBLE);
            this.findViewById(R.id.playback_id_info).setVisibility(View.VISIBLE);
            this.findViewById(R.id.playback_right_line).setVisibility(View.VISIBLE);
            isMapFullScreen = false;
        }*/
    }

    public void replay()
    {
        mPlayer.replay();
    }

    /**
     * 弹出toast提示
     * 
     * @param tip string
     */
    public void showToast(String tip)
    {
        if (null == mToast)
        {
            mToast = Toast.makeText(this, "", Toast.LENGTH_LONG);
        }
        mToast.setText(tip);
        mToast.show();
    }

    private void startPlayBack(String filePath)
    {
        if (OnPlayBackListener.PLAY != mPlayer.getPlayerState())
        {
            mPlayer.setListener(this);
            mPlayer.setSourceFile(filePath);
            mPlayer.start();
        }
    }

    /**
     * 改变播放按钮背景new
     */
    private void chagePlayBtnState(boolean play)
    {
        if (play)
        {
            playBtn.setImageResource(R.drawable.playback_pause_button);
        }
        else
        {
            playBtn.setImageResource(R.drawable.playback_play_button);
        }
    }

    private void initView()
    {
        mPlayer.isViewShow(this.findViewById(R.id.playback_operatorbar));
        playBtn = (ImageButton) this.findViewById(R.id.playback_play_id);
        if (mPlayer.isViewShow(playBtn))
        {
            playBtn.setOnClickListener(clickListener);
        }
        // 图片界面是否隐藏
        if (mPlayer.isViewShow(this.findViewById(R.id.playback_image_id)))
        {
            mPlayer.setDisplayView(this.findViewById(R.id.playback_image_id));
        }
        if (mPlayer.isViewShow(this.findViewById(R.id.playback_video_id)))
        {
            mPlayer.setDisplayView(this.findViewById(R.id.playback_video_id));
        }

        mSeekBar = (SeekBar) this.findViewById(R.id.playback_seek_id);
        if (mPlayer.isViewShow(mSeekBar))
        {
            mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
            {
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
                {
                    if (fromUser)
                    {
                        mPlayer.seekTo(progress * mPlayer.getDuration() / mSeekBar.getMax());
                    }
                }

                public void onStartTrackingTouch(SeekBar seekBar)
                {
                }

                public void onStopTrackingTouch(SeekBar seekBar)
                {
                }
            });
        }
        //mMapView = (MapView) this.findViewById(R.id.view_map);
        //mMapView.setVisibility(View.INVISIBLE);
        /*infoLatitude = (TextView) findViewById(R.id.playback_info_latitude_id);
        infoLongitude = (TextView) findViewById(R.id.playback_info_longitude_id);
        infoSpeed = (TextView) findViewById(R.id.playback_info_speed_id);*/
        infoTime = (TextView) findViewById(R.id.playback_info_time_id);
        //maxButton = (ImageButton) findViewById(R.id.playback_map_max_id);
        //maxButton.setOnClickListener(clickListener);
        initPaint();
    }

    /**
     * 初始化画�?     */
    private void initPaint()
    {
        pathPaint.setStrokeWidth(5);
        pathPaint.setColor(Color.RED);
        pathPaint.setStyle(Paint.Style.STROKE);
        pathPaint.setPathEffect(effect);

        pointPaint.setStrokeWidth(16);
        pointPaint.setColor(POINT_COLOR);
        pointPaint.setStyle(Paint.Style.FILL);
        pointPaint.setPathEffect(effect);
    }

    /**
     * 根据时间偏移，查找GPS位置
     */
    private void updatePosition(int offset)
    {
        Log.d("TAG", "updatePosition offset = " + offset);

        int pos = findPostion(offset / 1000);
        // 如果有位置找不到则显示前�?��点的位置
        if (pos >= 0)
        {
            mCurrentIndex = pos;
            showPosition(mCurrentIndex);
        }
    }

    /**
     * 找到位置
     * 
     * @param offset
     * @return
     */
    private int findPostion(int offset)
    {
        if (null == positions || 0 == positions.size())
        {
            return -1;
        }

        for (int i = 0; i < positions.size(); i++)
        {
            DrivePosition dp = positions.get(i);
            if (dp.mDelta == offset)
            {
                return i;
            }
        }

        return -1;
    }

    /**
     * 展示某点信息
     * 
     * @param offset
     */
    private void showPosition(int index)
    {
        if (null == positions || 0 == positions.size())
        {
            return;
        }
        DrivePosition dp = positions.get(index);
        if (null != dp)
        {
            NumberFormat bnf = NumberFormat.getInstance();
            bnf.setMinimumFractionDigits(2);
            bnf.setMaximumFractionDigits(2);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (dp.mValid)
            {
                //infoLatitude.setText(StringUtil.loactionToString(dp.mLatitude, true));
                //infoLongitude.setText(StringUtil.loactionToString(dp.mLongitude, false));
                //infoSpeed.setText(bnf.format(dp.mSpeed));
                //mMapView.postInvalidate();
            }
            infoTime.setText(dateFormat.format(new Date(dp.mTime)));
        }
    }

    /**
     * 画出行车路径
     */
    private void drawPath(Canvas canvas)
    {

        if (mCurrentIndex < 0 || mCurrentIndex >= positions.size())
        {
            return;
        }

        DrivePosition dp = positions.get(mCurrentIndex);
        if (null != dp && dp.mValid)
        {
            //buildPath(mCurrentIndex);
            // //绘制路径
            canvas.drawPath(mPath, pathPaint);
            //drawCurrentPoint(canvas, dp);
        }
    }

    /**
     * 绘制�?��蓝点
     * 
     * @param canvas
     * @param dp
     */
    /*private void drawCurrentPoint(Canvas canvas, DrivePosition dp)
    {
        GeoPoint convertedPoint = gpsConvertGeoPoint(dp);
        Point gp = new Point();
        mMapView.getProjection().toPixels(convertedPoint, gp);
        canvas.drawCircle(gp.x, gp.y, 14, pointPaint);
        if (!lockMove && mPlayer.getPlayerState() == OnPlayBackListener.PLAY)
        {
            mMapController.animateTo(convertedPoint);
        }
    }*/

    /**
     * GPS转到到屏幕坐�?     * 
     * @param dp
     * @return
     */
    /*private Point gpsToPoint(DrivePosition dp)
    {
        GeoPoint convertedPoint = gpsConvertGeoPoint(dp);
        Point gp = new Point();
        mMapView.getProjection().toPixels(convertedPoint, gp);
        return gp;
    }*/

    /**
     * GPS坐标转换为geopoint坐标
     * 
     * @param dp
     * @return
     */
    /*private GeoPoint gpsConvertGeoPoint(DrivePosition dp)
    {
        GeoPoint p = new GeoPoint((int) (dp.mLatitude * 1E6), (int) (dp.mLongitude * 1E6));
//        Bundle b = CoordinateConvert.fromWgs84ToBaidu(p);
//        GeoPoint convertedPoint = CoordinateConvert.bundleDecode(b);
        return p;
    }*/

    /**
     * 构�?要回复的图形path
     * 
     * @return
     */
    /*public void buildPath(int end)
    {
        mPath.reset();
        // 如果只有�?��点，就不�?��画path�?        if (0 == end)
        {
            return;
        }
        boolean first = true;
        for (int i = 0; i <= end; i++)
        {
            DrivePosition dp = positions.get(i);
            if (!dp.mValid)
            {
                continue;
            }

            Point gp = gpsToPoint(dp);
            if (first)
            {
                mPath.moveTo(gp.x, gp.y);
                first = false;
            }
            else
            {
                mPath.lineTo(gp.x, gp.y);
            }
        }
    }*/

    /*
     * (non-Javadoc)
     * @see com.google.android.maps.MapActivity#isRouteDisplayed()
     */
    /*@Override
    protected boolean isRouteDisplayed()
    {
        // TODO Auto-generated method stub
        return false;
    }

    class MyLocationOverlay extends Overlay
    {
        public MyLocationOverlay()
        {
        }

        @Override
        public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when)
        {
            super.draw(canvas, mapView, shadow);
            drawPath(canvas);
            return true;
        }

        @Override
        public boolean onTouchEvent(MotionEvent event, MapView view)
        {

            switch (event.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    lockMove = true;
                    break;
                case MotionEvent.ACTION_UP:
                    lockMove = false;
                    break;
            }
            return super.onTouchEvent(event, view);
        }
    }*/

    @Override
    protected void onDestroy()
    {		
    	//mBRCIntent.putExtra("msgtype", Config.MSG_ACTIVITY_START_PREVIEW); 
		//sendBroadcast(mBRCIntent);  
        super.onDestroy();
    }

    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
    	
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		//mBRCIntent.putExtra("msgtype", Config.MSG_ACTIVITY_CLOSE_PREVIEW); 
		//sendBroadcast(mBRCIntent);  
		super.onStart();
	}

	private void updatePlayingTime(boolean loop)
    {
        Log.d("TAG", "updatePlayingTime()");
        if (OnPlayBackListener.PLAY != mPlayer.getPlayerState() || isStop)
        {
            return;
        }
        int now = mPlayer.currentTime();

        int progress = (mSeekBar.getMax() * now) / mPlayer.getDuration();

        mSeekBar.setProgress(progress);

        updatePosition(now);
        if (loop)
        {
            mHandler.sendEmptyMessageDelayed(UPDATE_PLAY_TIME, 500);
        }
    }

    private void updatePlayingInfoOnce()
    {
        updatePosition(0);
    }

    public void onError(int code, String info)
    {
        showToast(info);
        this.finish();
    }

    public void onStateChange(int state)
    {
        Log.d("TAG", "onStateChange = " + state);
        if (OnPlayBackListener.PLAY == state)
        {
            chagePlayBtnState(true);
            mHandler.removeMessages(UPDATE_PLAY_TIME);
            updatePlayingTime(true);
        }
        else if (OnPlayBackListener.COMPLETE == state)
        {
            chagePlayBtnState(false);
            mSeekBar.setProgress(mSeekBar.getMax());
        }
        else if (OnPlayBackListener.ONE_SHOT == state)
        {
            updatePlayingInfoOnce();
        }
        else
        {
            chagePlayBtnState(false);
        }
    }
}
