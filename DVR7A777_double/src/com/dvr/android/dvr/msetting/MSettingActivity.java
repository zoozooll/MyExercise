package com.dvr.android.dvr.msetting;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.MediaColumns;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.dvr.android.dvr.Config;
import com.dvr.android.dvr.mshowplayback.MediaTool;
import com.dvr.android.dvr.util.PowerUitl;
import com.dvr.android.dvr.view.OnSelectListListener;
import com.dvr.android.dvr.view.PowerSavingDialog;
import com.dvr.android.dvr.view.SavePathDialog;
import com.dvr.android.dvr.R;

public class MSettingActivity extends Activity
{
	private static final int SETTING_TYPE_RECORD_MODE = 1;
	private static final int SETTING_TYPE_RECORD_QUALITY = 2;
	private static final int SETTING_TYPE_HITDELICACY = 3;
	private static final int SETTING_TYPE_RECORD_BACK = 4;
	private static final int SETTING_TYPE_VIDEO_SET = 5;
	
	private int nSettingType = SETTING_TYPE_RECORD_MODE;
	
	
    private static final int SHOWDIALOG_POWERSAVING = 1;

    private static final int SHOWDIALOG_SAVEPATH = 2;
    // 亮度参数
    public static final int BRIGHTNESS_DIM = 20;

    public static final int BRIGHTNESS_ON = 255;

    public static final int MINIMUM_BACKLIGHT = BRIGHTNESS_DIM + 10;
    public final int MAXIMUM_BACKLIGHT = BRIGHTNESS_ON;
    

    /*private UniversityAdapter pAdapter_vq;
    // 录制视频画质的集�?
    private ArrayList<University> universityList_vq = new ArrayList<University>();
    private CustomGallery gallery_vquality = null;

    private UniversityAdapter pAdapter_vl;
    private ArrayList<University> universityList_vl = new ArrayList<University>();
    private CustomGallery gallery_vlong = null;

    private Button btntwo_attact = null;


    private SeekBar seekbar_brigt = null;

    private Button btnthree_clinkscreen = null;

    private Button btnPowersaving = null;

    private Button btnthree_recordsound = null;
    // add by zw
    private Button btnone_recordmode = null;
    private Button btnthree_bganim = null;
    private Button btnthree_bgrecord = null;

    private Button btnfour_map = null;

    private Button btnfour_dir = null;
    private Button btnfour_cameraId = null;
    private Button btnfour_defealt = null;

    private Button btnfour_help = null;*/
    
    private AlertDialog dialong = null;
   
    
    //added by ji
    private MyImageButton btn_recordmode = null;
    private MyImageButton btn_recordquality = null;
    private MyImageButton btn_hitdelicacy = null;
    private MyImageButton btn_recordback = null;
    private MyImageButton btn_videoset = null;
    
    private Drawable btn_drawable_normal;
    private Drawable btn_drawable_pressed;
    
    //record mode
    private TextView mtextview_recordmode;
    private TextView mtextview_videoCycle;
    private TextView mtextview_videoCycle_value;
    private MyImageButton btn_recordmode_normal = null;
    private MyImageButton btn_recordmode_cycle = null;
    private Button btn_videoCycle_left = null;
    private Button btn_videoCycle_right = null;
    protected String[] mCycleVideoTimes = null;
    
    //RecordQuality
    private TextView mtextview_recordQuality;
    private MyImageButton btn_recordQuality_high = null;
    private MyImageButton btn_recordQuality_mid = null;
    private MyImageButton btn_recordQuality_lower = null;
    
    //hitdelicacy
    private TextView mtextview_hitdelicacy;
    private TextView mtextview_hitdelicacy_auto_saving;
    private TextView mtextview_hitdelicacy_auto_saving_open;
    private TextView mtextview_hitdelicacy_auto_saving_close;
    private TextView mtextview_click_screen;
    private TextView mtextview_click_screen_open;
    private TextView mtextview_click_screen_close;
    private TextView mtextview_power_saving;
    private TextView mtextview_power_saving_value;
    private SeekBar seekbar_delicacy = null;
    private MyImageButton btn_hitdelicacy_auto_saving = null;
    private MyImageButton btn_click_screen = null;
    private Button btn_power_save_left = null;
    private Button btn_power_save_right = null;
    protected String[] mPowerSaveString = null;
    
    //back record
    private TextView mtextview_record_back_ani;
    private TextView mtextview_record_back_ani_open;
    private TextView mtextview_record_back_ani_close;
    private TextView mtextview_record_back;
    private TextView mtextview_record_back_open;
    private TextView mtextview_record_back_close;
    private MyImageButton btn_record_back_ani = null;
    private MyImageButton btn_record_back = null;
    
    //video set
    private MyImageButton btn_save_dir = null;
    private MyImageButton btn_reset_defaul = null;

    private TextView mtextview_power_on_auto;
    private TextView mtextview_power_on_auto_open;
    private TextView mtextview_power_on_auto_close;
    private MyImageButton btn_power_on_auto_run = null;
    

    /*
     * seekbar_delicacy seekbar_brigt btnthree_clinkscreen btnthree_houtai btnthree_recordsound btnfour_map btnfour_dir
     * btnfour_defealt btnfour_help
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        /* System.out.println("onCreate MSettingActivity"); */
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.msetting);
        init();
    }
    
    private void showVideoAutoRunButton()
    {
    	if(SettingBean.mEnableAutoRun)
    	{
    		btn_power_on_auto_run.setImageDrawable(getResources().getDrawable(R.drawable.btn_control_open));
    	}
    	else
    	{
    		btn_power_on_auto_run.setImageDrawable(getResources().getDrawable(R.drawable.btn_control_close));
    	}
    }
    
    private void showvideo_setUI(boolean bShow)
    {
    	
    	mtextview_power_on_auto = ((TextView) findViewById(R.id.power_on_auto_run_text_id));
    	mtextview_power_on_auto_open = ((TextView) findViewById(R.id.power_on_auto_open_text_id));
    	mtextview_power_on_auto_close = ((TextView) findViewById(R.id.power_on_auto_close_text_id));
    	btn_power_on_auto_run = ((MyImageButton) findViewById(R.id.btn_power_on_auto_run_id));
    	
    	/*mtextview_record_back_ani = ((TextView) findViewById(R.id.record_back_ani_text_id));
    	mtextview_record_back_ani_open = ((TextView) findViewById(R.id.record_back_ani_open_text_id));
    	mtextview_record_back_ani_close = ((TextView) findViewById(R.id.record_back_ani_close_text_id));
    	btn_record_back_ani = ((MyImageButton) findViewById(R.id.btnthree_bganim));*/
    	
    	
    	mtextview_record_back = ((TextView) findViewById(R.id.record_back_text_id));
    	mtextview_record_back_open = ((TextView) findViewById(R.id.record_back_open_text_id));
    	mtextview_record_back_close = ((TextView) findViewById(R.id.record_back_close_text_id));
    	btn_record_back = ((MyImageButton) findViewById(R.id.btnthree_bgrecord));
    	
    	if(bShow)
    	{
    		
    		showVideoAutoRunButton();
    		
    		mtextview_power_on_auto.setVisibility(View.VISIBLE);
    		mtextview_power_on_auto_open.setVisibility(View.VISIBLE);
    		mtextview_power_on_auto_close.setVisibility(View.VISIBLE);
    		btn_power_on_auto_run.setVisibility(View.VISIBLE);
    		
    		//
    		//showBackRecordAniButton();
        	showBackRecordButton();
        	
    		/*mtextview_record_back_ani.setVisibility(View.VISIBLE);
    		mtextview_record_back_ani_open.setVisibility(View.VISIBLE);
    		mtextview_record_back_ani_close.setVisibility(View.VISIBLE);
    		btn_record_back_ani.setVisibility(View.VISIBLE);*/
    		
    		
    		mtextview_record_back.setVisibility(View.VISIBLE);
    		mtextview_record_back_open.setVisibility(View.VISIBLE);
    		mtextview_record_back_close.setVisibility(View.VISIBLE);
    		btn_record_back.setVisibility(View.VISIBLE);
    	}
    	else
    	{
    		mtextview_power_on_auto.setVisibility(View.INVISIBLE);
    		mtextview_power_on_auto_open.setVisibility(View.INVISIBLE);
    		mtextview_power_on_auto_close.setVisibility(View.INVISIBLE);
    		btn_power_on_auto_run.setVisibility(View.INVISIBLE);
    		
    		//
    		/*mtextview_record_back_ani.setVisibility(View.INVISIBLE);
    		mtextview_record_back_ani_open.setVisibility(View.INVISIBLE);
    		mtextview_record_back_ani_close.setVisibility(View.INVISIBLE);
    		btn_record_back_ani.setVisibility(View.INVISIBLE);*/
    		
    		mtextview_record_back.setVisibility(View.INVISIBLE);
    		mtextview_record_back_open.setVisibility(View.INVISIBLE);
    		mtextview_record_back_close.setVisibility(View.INVISIBLE);
    		btn_record_back.setVisibility(View.INVISIBLE);
    	}
    }

    private void showBackRecordAniButton()
    {
    	if(SettingBean.mBGAnim)
    	{
    		btn_record_back_ani.setImageDrawable(getResources().getDrawable(R.drawable.btn_control_open));
    	}
    	else
    	{
    		btn_record_back_ani.setImageDrawable(getResources().getDrawable(R.drawable.btn_control_close));
    	}
    }

    private void showBackRecordButton()
    {
    	if(SettingBean.mBGRecord)
    	{
    		btn_record_back.setImageDrawable(getResources().getDrawable(R.drawable.btn_control_open));
    	}
    	else
    	{
    		btn_record_back.setImageDrawable(getResources().getDrawable(R.drawable.btn_control_close));
    	}
    }
    
    private void showrecord_backUI(boolean bShow)
    {
    	btn_save_dir = ((MyImageButton) findViewById(R.id.btnfour_dir));
    	btn_reset_defaul = ((MyImageButton) findViewById(R.id.btnfour_defealt));
    	
    	if(bShow)
    	{
    		//init
    		btn_save_dir.setText(getResources().getString(R.string.msetting_mulu));
    		btn_save_dir.setColor(Color.WHITE);
    		btn_save_dir.setOffsetY(42);
    		btn_save_dir.setOffsetX(150);
    		btn_reset_defaul.setText(getResources().getString(R.string.msetting_defealt));
    		btn_reset_defaul.setColor(Color.WHITE);
    		btn_reset_defaul.setOffsetX(getResources().getDimension(R.dimen.set_record_default_x)); 		
    		btn_reset_defaul.setOffsetY(getResources().getDimension(R.dimen.set_record_default_y));

    		//btn_save_dir.setVisibility(View.VISIBLE);
    		btn_reset_defaul.setVisibility(View.VISIBLE);
    	}
    	else
    	{
    		//btn_save_dir.setVisibility(View.INVISIBLE);
    		btn_reset_defaul.setVisibility(View.INVISIBLE);
    	}
    }
    
    private void showhitdelicacyButton()
    {
    	if(SettingBean.isHitAutosave)
    	{
    		btn_hitdelicacy_auto_saving.setImageDrawable(getResources().getDrawable(R.drawable.btn_control_open));
    	}
    	else
    	{
    		btn_hitdelicacy_auto_saving.setImageDrawable(getResources().getDrawable(R.drawable.btn_control_close));
    	}
    }
    
    
    private void showClickSaveButton()
    {
    	if(SettingBean.isClickSave)
    	{
    		btn_click_screen.setImageDrawable(getResources().getDrawable(R.drawable.btn_control_open));
    	}
    	else
    	{
    		btn_click_screen.setImageDrawable(getResources().getDrawable(R.drawable.btn_control_close));
    	}
    }
    
    
    private void showPowerSaveString(boolean bInit)
    {
    	int[] values = null;
    	int i;
    	
    	values = getResources().getIntArray(R.array.setting_power_saving_value);
    	if(bInit)
    	{
    		for(i = 0; i < values.length; i++)
    		{
    			if(SettingBean.powersaving_time == values[i])
    			{
    				SettingBean.powerSaveIndex = i;
    				break;
    			}
    		}
    	}
    	else
    	{
        	SettingBean.powersaving_time = values[SettingBean.powerSaveIndex];
            PowerUitl.getPowerUitl().setValue(SettingBean.powersaving_time);
    	}
    	mtextview_power_saving_value.setText(mPowerSaveString[SettingBean.powerSaveIndex]);
    }
    
    private void showhitdelicacyUI(boolean bShow, boolean bInit)
    {
    	mtextview_hitdelicacy = ((TextView) findViewById(R.id.hit_delicacy_text_id));
    	mtextview_hitdelicacy_auto_saving = ((TextView) findViewById(R.id.hit_auto_saving_text_id));
    	mtextview_hitdelicacy_auto_saving_open = ((TextView) findViewById(R.id.hit_auto_saving_open_text_id));
    	mtextview_hitdelicacy_auto_saving_close = ((TextView) findViewById(R.id.hit_auto_saving_close_text_id));
    	mtextview_click_screen = ((TextView) findViewById(R.id.click_screen_text_id));
    	mtextview_click_screen_open = ((TextView) findViewById(R.id.click_screen_open_text_id));
    	mtextview_click_screen_close = ((TextView) findViewById(R.id.click_screen_close_text_id));
    	mtextview_power_saving = ((TextView) findViewById(R.id.power_save_text_id));
    	mtextview_power_saving_value = ((TextView) findViewById(R.id.power_save_value_text_id));
    	btn_hitdelicacy_auto_saving = ((MyImageButton) findViewById(R.id.hit_auto_saving_button_id));
    	btn_click_screen = ((MyImageButton) findViewById(R.id.click_screen_saving_button_id));
    	btn_power_save_left = ((Button) findViewById(R.id.power_save_left_button_id));
    	btn_power_save_right = ((Button) findViewById(R.id.power_save_right_button_id));
    	seekbar_delicacy = (SeekBar) findViewById(R.id.seekbar_delicacy);
        
    	if(bShow)
    	{
    		//init
    		mPowerSaveString = getResources().getStringArray(R.array.setting_power_saving_str);
    		showPowerSaveString(true);
    		
    		showhitdelicacyButton();
        	showClickSaveButton();
    		
    		mtextview_hitdelicacy.setVisibility(View.VISIBLE);
    		mtextview_hitdelicacy_auto_saving.setVisibility(View.VISIBLE);
    		mtextview_hitdelicacy_auto_saving_open.setVisibility(View.VISIBLE);
    		mtextview_hitdelicacy_auto_saving_close.setVisibility(View.VISIBLE);
    		mtextview_click_screen.setVisibility(View.VISIBLE);
    		mtextview_click_screen_open.setVisibility(View.VISIBLE);
    		mtextview_click_screen_close.setVisibility(View.VISIBLE);
    		mtextview_power_saving.setVisibility(View.VISIBLE);
    		mtextview_power_saving_value.setVisibility(View.VISIBLE);
    		btn_hitdelicacy_auto_saving.setVisibility(View.VISIBLE);
    		btn_click_screen.setVisibility(View.VISIBLE);
    		btn_power_save_left.setVisibility(View.VISIBLE);
    		btn_power_save_right.setVisibility(View.VISIBLE);
    		seekbar_delicacy.setVisibility(View.VISIBLE);
    	}
    	else
    	{
    		mtextview_hitdelicacy.setVisibility(View.INVISIBLE);
    		mtextview_hitdelicacy_auto_saving.setVisibility(View.INVISIBLE);
    		mtextview_hitdelicacy_auto_saving_open.setVisibility(View.INVISIBLE);
    		mtextview_hitdelicacy_auto_saving_close.setVisibility(View.INVISIBLE);
    		mtextview_click_screen.setVisibility(View.INVISIBLE);
    		mtextview_click_screen_open.setVisibility(View.INVISIBLE);
    		mtextview_click_screen_close.setVisibility(View.INVISIBLE);
    		mtextview_power_saving.setVisibility(View.INVISIBLE);
    		mtextview_power_saving_value.setVisibility(View.INVISIBLE);
    		btn_hitdelicacy_auto_saving.setVisibility(View.INVISIBLE);
    		btn_click_screen.setVisibility(View.INVISIBLE);
    		btn_power_save_left.setVisibility(View.INVISIBLE);
    		btn_power_save_right.setVisibility(View.INVISIBLE);
    		seekbar_delicacy.setVisibility(View.INVISIBLE);
    	}
    }
    
    private void showRecordQualityButton()
    {
    	if(SettingBean.mVideoQuality == 1)
    	{
    		btn_recordQuality_high.setImageDrawable(getResources().getDrawable(R.drawable.btn_record_quality_pressed));
    		btn_recordQuality_mid.setImageDrawable(getResources().getDrawable(R.drawable.btn_record_quality_normal)); 
    		btn_recordQuality_lower.setImageDrawable(getResources().getDrawable(R.drawable.btn_record_quality_normal));
    	}
    	else if(SettingBean.mVideoQuality == 2)
    	{
    		btn_recordQuality_high.setImageDrawable(getResources().getDrawable(R.drawable.btn_record_quality_normal));
    		btn_recordQuality_mid.setImageDrawable(getResources().getDrawable(R.drawable.btn_record_quality_pressed)); 
    		btn_recordQuality_lower.setImageDrawable(getResources().getDrawable(R.drawable.btn_record_quality_normal));
    	}
    	else if(SettingBean.mVideoQuality == 3)
    	{
    		btn_recordQuality_high.setImageDrawable(getResources().getDrawable(R.drawable.btn_record_quality_normal));
    		btn_recordQuality_mid.setImageDrawable(getResources().getDrawable(R.drawable.btn_record_quality_normal)); 
    		btn_recordQuality_lower.setImageDrawable(getResources().getDrawable(R.drawable.btn_record_quality_pressed));
    	}
    }
    
    private void showRecordQualityUI(boolean bShow)
    {
    	mtextview_recordQuality = ((TextView) findViewById(R.id.msetting_videoq));
    	btn_recordQuality_high = ((MyImageButton) findViewById(R.id.recordquality_high_button_id));
    	btn_recordQuality_mid = ((MyImageButton) findViewById(R.id.recordquality_middle_button_id));
    	btn_recordQuality_lower = ((MyImageButton) findViewById(R.id.recordquality_lower_button_id));
    	if(bShow)
    	{
    		//init
    		btn_recordQuality_high.setText(getResources().getString(R.string.msetting_gao));
    		btn_recordQuality_high.setColor(Color.WHITE);
    		btn_recordQuality_high.setOffsetY(getResources().getDimension(R.dimen.set_record_quality_high_y));
    		btn_recordQuality_high.setOffsetX(getResources().getDimension(R.dimen.set_record_quality_high_x));
    		btn_recordQuality_mid.setText(getResources().getString(R.string.msetting_zhong));
    		btn_recordQuality_mid.setColor(Color.WHITE);
    		btn_recordQuality_mid.setOffsetY(getResources().getDimension(R.dimen.set_record_quality_mid_y));
    		btn_recordQuality_mid.setOffsetX(getResources().getDimension(R.dimen.set_record_quality_mid_x));
    		btn_recordQuality_lower.setText(getResources().getString(R.string.msetting_di));
    		btn_recordQuality_lower.setColor(Color.WHITE);
    		btn_recordQuality_lower.setOffsetY(getResources().getDimension(R.dimen.set_record_quality_lower_y));
    		btn_recordQuality_lower.setOffsetX(getResources().getDimension(R.dimen.set_record_quality_lower_x));
    		
        	showRecordQualityButton();
    		
    		mtextview_recordQuality.setVisibility(View.VISIBLE);
    		btn_recordQuality_high.setVisibility(View.VISIBLE);
    		btn_recordQuality_mid.setVisibility(View.VISIBLE);
    		btn_recordQuality_lower.setVisibility(View.VISIBLE);
    	}
    	else
    	{
    		mtextview_recordQuality.setVisibility(View.INVISIBLE);
    		btn_recordQuality_high.setVisibility(View.INVISIBLE);
    		btn_recordQuality_mid.setVisibility(View.INVISIBLE);
    		btn_recordQuality_lower.setVisibility(View.INVISIBLE);
    	}
    }
    
    private void showRecordModeButton()
    {
    	if(SettingBean.mRecordMode)
    	{
    		btn_recordmode_normal.setImageDrawable(getResources().getDrawable(R.drawable.btn_record_mode_pressed));
    		btn_recordmode_cycle.setImageDrawable(getResources().getDrawable(R.drawable.btn_record_mode_noraml)); 
    	}
    	else
    	{
    		btn_recordmode_normal.setImageDrawable(getResources().getDrawable(R.drawable.btn_record_mode_noraml));
    		btn_recordmode_cycle.setImageDrawable(getResources().getDrawable(R.drawable.btn_record_mode_pressed)); 
    	}
    }
    
    private void showCycleVideoTime()
    {
		mCycleVideoTimes = getResources().getStringArray(R.array.setting_record_time_str);
		mtextview_videoCycle_value.setText(mCycleVideoTimes[SettingBean.cirVideoLong]);
    }

    private void showRecordModeUI(boolean bShow)
    {
    	mtextview_recordmode = ((TextView) findViewById(R.id.recordmode_text_id));
    	mtextview_videoCycle = ((TextView) findViewById(R.id.video_time_text_id));
    	mtextview_videoCycle_value = ((TextView) findViewById(R.id.video_time_value_text_id));
    	btn_recordmode_normal = ((MyImageButton) findViewById(R.id.recordmode_normal_button_id));
    	btn_recordmode_cycle = ((MyImageButton) findViewById(R.id.recordmode_cycle_button_id));
    	btn_videoCycle_left = ((Button) findViewById(R.id.video_time_left_button_id));
    	btn_videoCycle_right = ((Button) findViewById(R.id.video_time_right_button_id));
    	if(bShow)
    	{
    		//init
    		btn_recordmode_normal.setText(getResources().getString(R.string.msetting_regular));
    		btn_recordmode_normal.setColor(Color.WHITE);
    		btn_recordmode_normal.setOffsetX(getResources().getDimension(R.dimen.set_record_regual_offset_x));  
    		btn_recordmode_normal.setOffsetY(getResources().getDimension(R.dimen.set_record_regual_offset_y));
    		btn_recordmode_cycle.setText(getResources().getString(R.string.msetting_loop));
    		btn_recordmode_cycle.setColor(Color.WHITE);
    		btn_recordmode_cycle.setOffsetX(getResources().getDimension(R.dimen.set_record_cycle_offset_x));
    		btn_recordmode_cycle.setOffsetY(getResources().getDimension(R.dimen.set_record_cycle_offset_y));
    		
    		showRecordModeButton();
    		showCycleVideoTime();
    		
    		mtextview_recordmode.setVisibility(View.VISIBLE);
    		mtextview_videoCycle.setVisibility(View.VISIBLE);
    		mtextview_videoCycle_value.setVisibility(View.VISIBLE);
    		btn_recordmode_normal.setVisibility(View.VISIBLE);
    		btn_recordmode_cycle.setVisibility(View.VISIBLE);
    		btn_videoCycle_left.setVisibility(View.VISIBLE);
    		btn_videoCycle_right.setVisibility(View.VISIBLE);
    	}
    	else
    	{
    		mtextview_recordmode.setVisibility(View.INVISIBLE);
    		mtextview_videoCycle.setVisibility(View.INVISIBLE);
    		mtextview_videoCycle_value.setVisibility(View.INVISIBLE);
    		btn_recordmode_normal.setVisibility(View.INVISIBLE);
    		btn_recordmode_cycle.setVisibility(View.INVISIBLE);
    		btn_videoCycle_left.setVisibility(View.INVISIBLE);
    		btn_videoCycle_right.setVisibility(View.INVISIBLE);
    	}
    }

    private void setting_draw_type_content(boolean bInit)
    {
    	if(nSettingType == SETTING_TYPE_RECORD_MODE)
    	{
    		showRecordModeUI(true);  
    		showRecordQualityUI(false);
    		showhitdelicacyUI(false, true);
    		showrecord_backUI(false);
    	    showvideo_setUI(false);
    	}
    	else if(nSettingType == SETTING_TYPE_RECORD_QUALITY)
    	{
    		showRecordModeUI(false);  
    		showRecordQualityUI(true);
    		showhitdelicacyUI(false, true);
    		showrecord_backUI(false);
    	    showvideo_setUI(false);
    	}
    	else if(nSettingType == SETTING_TYPE_HITDELICACY)
    	{
    		showRecordModeUI(false);  
    		showRecordQualityUI(false);
    		showhitdelicacyUI(true, true);
    		showrecord_backUI(false);
    	    showvideo_setUI(false); 
    	}
    	else if(nSettingType == SETTING_TYPE_RECORD_BACK)
    	{
    		showRecordModeUI(false);  
    		showRecordQualityUI(false);
    		showhitdelicacyUI(false, true);
    		showrecord_backUI(true);
    	    showvideo_setUI(false); 
    	}
    	else if(nSettingType == SETTING_TYPE_VIDEO_SET)
    	{
    		showRecordModeUI(false);  
    		showRecordQualityUI(false);
    		showhitdelicacyUI(false, true);
    		showrecord_backUI(false);
    	    showvideo_setUI(true);
    	}
    }
    
    private void setting_draw_type_button()
    {
    	//btn_drawable_normal = getResources().getDrawable(R.drawable.btn_setting_main_normal);
    	//btn_drawable_pressed = getResources().getDrawable(R.drawable.btn_setting_main_pressed);
    	if(nSettingType == SETTING_TYPE_RECORD_MODE)
    	{
            btn_recordmode.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_recordmode_bg_pressed));  
            btn_recordquality.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_videoquality_bg_normal));  
            btn_hitdelicacy.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_hit_bg_normal));  
            btn_recordback.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_back_bg_normal));  
            btn_videoset.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_videoset_bg_normal));  
    	}
    	else if(nSettingType == SETTING_TYPE_RECORD_QUALITY)
    	{
    		btn_recordmode.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_recordmode_bg_normal));  
            btn_recordquality.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_videoquality_bg_pressed));  
            btn_hitdelicacy.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_hit_bg_normal));  
            btn_recordback.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_back_bg_normal));  
            btn_videoset.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_videoset_bg_normal)); 
    	}
    	else if(nSettingType == SETTING_TYPE_HITDELICACY)
    	{
    		btn_recordmode.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_recordmode_bg_normal));  
            btn_recordquality.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_videoquality_bg_normal));  
            btn_hitdelicacy.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_hit_bg_pressed));  
            btn_recordback.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_back_bg_normal));  
            btn_videoset.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_videoset_bg_normal)); 
    	}
    	else if(nSettingType == SETTING_TYPE_RECORD_BACK)
    	{
    		btn_recordmode.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_recordmode_bg_normal));  
            btn_recordquality.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_videoquality_bg_normal));  
            btn_hitdelicacy.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_hit_bg_normal));  
            btn_recordback.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_back_bg_pressed));  
            btn_videoset.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_videoset_bg_normal)); 
    	}
    	else if(nSettingType == SETTING_TYPE_VIDEO_SET)
    	{
    		btn_recordmode.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_recordmode_bg_normal));  
            btn_recordquality.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_videoquality_bg_normal));  
            btn_hitdelicacy.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_hit_bg_normal));  
            btn_recordback.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_back_bg_normal));  
            btn_videoset.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_videoset_bg_pressed)); 
    	}
    }
    
    private void init_setting_type()
    {
    	btn_recordmode = ((MyImageButton) findViewById(R.id.recordmode_button_id));
    	btn_recordquality = ((MyImageButton) findViewById(R.id.recordquality_button_id));
    	btn_hitdelicacy = ((MyImageButton) findViewById(R.id.hitdelicacy_button_id));
    	btn_recordback = ((MyImageButton) findViewById(R.id.recordback_button_id));
    	btn_videoset = ((MyImageButton) findViewById(R.id.videoset_button_id));   	   	 	
    	
    	setting_draw_type_button();
    	   	
    }
    
    /*private OnTouchListener touchListener = new OnTouchListener()
    {
    	public boolean onTouch(View v, MotionEvent event)
    	{
    		
    	}
    };*/

    private void initButtonAction()
    {
    	btn_recordmode.setOnTouchListener(new View.OnTouchListener() {			
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				/*int x , y;
				int left, top, right, bottom; 
				int[] location = new int[2]; */  
				if(event.getAction() == MotionEvent.ACTION_DOWN){       
                    //重新设置按下时的背景图片  
					nSettingType = SETTING_TYPE_RECORD_MODE;
			    	setting_draw_type_button();
			    	setting_draw_type_content(false);
			    	
			    	//test
			    	/*btn_recordmode.getLocationOnScreen(location); 
			    	x = location[0];
			    	y = location[1];
			    	left = btn_recordmode.getLeft();
			    	right = btn_recordmode.getRight();
			    	top = btn_recordmode.getTop();
			    	bottom = btn_recordmode.getBottom();
			    	x = location[0];
			    	y = location[1];*/
                 }
                 return false;
			}
		});
    	
    	btn_recordquality.setOnTouchListener(new View.OnTouchListener() {			
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction() == MotionEvent.ACTION_DOWN){       
                    //重新设置按下时的背景图片  
					nSettingType = SETTING_TYPE_RECORD_QUALITY;
			    	setting_draw_type_button();
			    	setting_draw_type_content(false);
                 }
                 return false;
			}
		});
    	
    	btn_hitdelicacy.setOnTouchListener(new View.OnTouchListener() {			
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction() == MotionEvent.ACTION_DOWN){       
                    //重新设置按下时的背景图片  
					nSettingType = SETTING_TYPE_HITDELICACY;
			    	setting_draw_type_button();
			    	setting_draw_type_content(false);
                 }
                 return false;
			}
		});
    	
    	btn_recordback.setOnTouchListener(new View.OnTouchListener() {			
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction() == MotionEvent.ACTION_DOWN){       
                    //重新设置按下时的背景图片  
					nSettingType = SETTING_TYPE_RECORD_BACK;
			    	setting_draw_type_button();
			    	setting_draw_type_content(false);
                 }
                 return false;
			}
		});
    	
    	btn_videoset.setOnTouchListener(new View.OnTouchListener() {			
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction() == MotionEvent.ACTION_DOWN){       
                    //重新设置按下时的背景图片  
					nSettingType = SETTING_TYPE_VIDEO_SET;
			    	setting_draw_type_button();
			    	setting_draw_type_content(false);
                 }
                 return false;
			}
		});
    	 
    	//record mode
	    btn_recordmode_normal.setOnClickListener(clickListener);
	    btn_recordmode_cycle.setOnClickListener(clickListener);
	    btn_videoCycle_left.setOnClickListener(clickListener);
	    btn_videoCycle_right.setOnClickListener(clickListener);

	  //RecordQuality
	    btn_recordQuality_high.setOnClickListener(clickListener);
	    btn_recordQuality_mid.setOnClickListener(clickListener);
	    btn_recordQuality_lower.setOnClickListener(clickListener);
	    
	  //hitdelicacy
        seekbar_delicacy.setMax(30);
        seekbar_delicacy.setProgress(SettingBean.mHitDelicacy);
        seekbar_delicacy.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
        {
            public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2)
            {
                if (arg2)
                {
                    SettingBean.mHitDelicacy = arg1;
                    SettingBean.onChage(SettingBean.HIT_CHANGE);
                }
            }

            public void onStartTrackingTouch(SeekBar arg0)
            {
            }

            public void onStopTrackingTouch(SeekBar arg0)
            {
            }
        });
        btn_hitdelicacy_auto_saving.setOnClickListener(clickListener);
        btn_click_screen.setOnClickListener(clickListener);
        btn_power_save_left.setOnClickListener(clickListener);
        btn_power_save_right.setOnClickListener(clickListener);
        
        //back record
        //btn_record_back_ani.setOnClickListener(clickListener);
        btn_record_back.setOnClickListener(clickListener);
        
        //video set
        btn_save_dir.setOnClickListener(clickListener);
        btn_reset_defaul.setOnClickListener(clickListener);
        btn_save_dir.setOnTouchListener(new View.OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction() == MotionEvent.ACTION_DOWN){       
                    //重新设置按下时的背景图片  
                    btn_save_dir.setImageDrawable(getResources().getDrawable(R.drawable.btn_record_quality_normal)); 
                 }
				else if(event.getAction() == MotionEvent.ACTION_UP){       
                    //重新设置按下时的背景图片  
                    btn_save_dir.setImageDrawable(getResources().getDrawable(R.drawable.btn_record_quality_pressed)); 
                 }
                 return false;
			}
		}); 
        btn_reset_defaul.setOnTouchListener(new View.OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction() == MotionEvent.ACTION_DOWN){       
                    //重新设置按下时的背景图片  
					btn_reset_defaul.setImageDrawable(getResources().getDrawable(R.drawable.btn_record_quality_normal)); 
                 }
				else if(event.getAction() == MotionEvent.ACTION_UP){       
                    //重新设置按下时的背景图片  
					btn_reset_defaul.setImageDrawable(getResources().getDrawable(R.drawable.btn_record_quality_pressed)); 
                 }
                 return false;
			}
		}); 
        btn_power_on_auto_run.setOnClickListener(clickListener);
    	
    }
    
    private void init()
    {
    	init_setting_type();
    	setting_draw_type_content(true);
    	initButtonAction();
    }

    private OnClickListener clickListener = new OnClickListener()
    {

        public void onClick(View v)
        {
            switch (v.getId())
            {
                //case R.id.btntwo_houtai:
                //    powersaving();
                //    break;
                //case R.id.btnthree_recordsound:
                //    records();
                //    break;
                case R.id.btnthree_bganim:
                	SettingBean.mBGAnim = !SettingBean.mBGAnim;
                	showBackRecordAniButton();
                    break;
                case R.id.btnthree_bgrecord:
                    SettingBean.mBGRecord = !SettingBean.mBGRecord;
                    showBackRecordButton();
                    break;
                case R.id.btn_power_on_auto_run_id:
                	SettingBean.mEnableAutoRun = !SettingBean.mEnableAutoRun;
                	showVideoAutoRunButton();
                    break;
                //case R.id.btnone_recordmode:
               //     recordmode();
                //    break;
                //case R.id.btnfour_camareid:
                //    settingCameraId();
                //    break;
               /* case R.id.btnfour_map:
                    toOfflineMap();
                    break;*/
                case R.id.btnfour_dir:
                    showDialog(SHOWDIALOG_SAVEPATH);
                    break;
                case R.id.btnfour_defealt:
                    setdefealt();
                    break;
//                case R.id.btnfour_help:
//                    showDowDialog();
//                    break;
                case R.id.recordmode_normal_button_id:
                	SettingBean.mRecordMode = true;
                	showRecordModeButton();
                	break;
                case R.id.recordmode_cycle_button_id:
                	SettingBean.mRecordMode = false;
                	showRecordModeButton();
                	break;
                case R.id.video_time_left_button_id:
            		if(SettingBean.cirVideoLong > 0)
            		{
            			SettingBean.cirVideoLong--;
            			showCycleVideoTime();
            		}
                	break;
                case R.id.video_time_right_button_id:
                	if(SettingBean.cirVideoLong < (mCycleVideoTimes.length - 1))
                	{
                		SettingBean.cirVideoLong++;
                		showCycleVideoTime();
                	}
                	break;
                case R.id.recordquality_high_button_id:
                	SettingBean.mVideoQuality = 1;
                	showRecordQualityButton();
                	break;
                case R.id.recordquality_middle_button_id:
                	SettingBean.mVideoQuality = 2;
                	showRecordQualityButton();
                	break;
                case R.id.recordquality_lower_button_id:
                	SettingBean.mVideoQuality = 3;
                	showRecordQualityButton();
                	break;
                case R.id.hit_auto_saving_button_id:
                	SettingBean.isHitAutosave = !SettingBean.isHitAutosave;
                	showhitdelicacyButton();
                	break;
                case R.id.click_screen_saving_button_id:
                	SettingBean.isClickSave = !SettingBean.isClickSave;
                	showClickSaveButton();
                	break;
                case R.id.power_save_left_button_id:
                	if(SettingBean.powerSaveIndex > 0)
            		{
            			SettingBean.powerSaveIndex--;
            			showPowerSaveString(false);
            		}
                	break;
                case R.id.power_save_right_button_id:
                	if(SettingBean.powerSaveIndex < (mPowerSaveString.length - 1))
                	{
                		SettingBean.powerSaveIndex++;
                		showPowerSaveString(false);
                	}
                	break;
            }
        }

    };

    private void showDowDialog()
    {

        // 构建Dialog显示的View对象
        LayoutInflater mLayoutInflater =
            (LayoutInflater) MSettingActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup bandbook = (ViewGroup) mLayoutInflater.inflate(R.layout.bandbook, null, true);

        AlertDialog.Builder builder = new AlertDialog.Builder(MSettingActivity.this);
        builder.setTitle(R.string.handBook);
        builder.setView(bandbook);
        builder.setNegativeButton(R.string.myshowplayback_cancel, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
            }
        });
        dialong = builder.create();
        dialong.setCanceledOnTouchOutside(false);
        dialong.show();

    }

    private void toOfflineMap()
    {
//        Intent intent = new Intent();
//        intent.setClass(MSettingActivity.this, OfflineMapActivity.class);
//        startActivity(intent);
    }

    protected void setdefealt()
    {
    	int[] values = null;
    	int i;
    	
        SettingBean.isHitAutosave = SettingBean.IsHitAutosave;
        SettingBean.isClickSave = SettingBean.IsClickSave;
        SettingBean.isHasvoice = SettingBean.IsHasvoice;
        SettingBean.isRecordSound = SettingBean.IsRecordSound;
        SettingBean.cirVideoLong = SettingBean.CirVideoLong;
        SettingBean.mVideoQuality = SettingBean.MVideoQuality;
        SettingBean.mHitDelicacy = SettingBean.MHitDelicacy;
        SettingBean.mRecordMode = SettingBean.MRecordMode;
        SettingBean.mBGRecord = SettingBean.MBGRecord;
        SettingBean.mBGAnim = SettingBean.MBGAnim;
        SettingBean.powersaving_time = SettingBean.PowerSaving_time;
        SettingBean.mPathItem = SettingBean.MPathItem;
        SettingBean.isBackCamera = SettingBean.IsBackCamera;  
        SettingBean.mEnableAutoRun = SettingBean.MEnableAutoRun;         

        seekbar_delicacy.setProgress(SettingBean.mHitDelicacy);

    	values = getResources().getIntArray(R.array.setting_power_saving_value);
    	for(i = 0; i < values.length; i++)
		{
			if(SettingBean.powersaving_time == values[i])
			{
				SettingBean.powerSaveIndex = i;
				break;
			}
		}
    }


    protected void powersaving()
    {
        showDialog(SHOWDIALOG_POWERSAVING);
    }
    
    
    protected void settingCameraId()
    {
        SettingBean.isBackCamera = !SettingBean.isBackCamera;
        if (SettingBean.isBackCamera)
        {
        	//btnfour_cameraId.setText(getResources().getString(R.string.msetting_camera_back));
        }
        else
        {
        	//btnfour_cameraId.setText(getResources().getString(R.string.msetting_camera_front));
        }

    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        // 保存数据到sharePreference
        saveme();
    }

    private void saveme()
    {
        SharedPreferences uiState = getSharedPreferences("xgx", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = uiState.edit();
        editor.putBoolean("isHitAutosave", SettingBean.isHitAutosave);
        editor.putBoolean("isClickSave", SettingBean.isClickSave);
        editor.putBoolean("isHasvoice", SettingBean.isHasvoice);
        editor.putBoolean("isRecordSound", SettingBean.isRecordSound);
        editor.putBoolean("mRecordMode", SettingBean.mRecordMode);
        editor.putBoolean("mBGRecord", SettingBean.mBGRecord);
        editor.putBoolean("mBGAnim", SettingBean.mBGAnim);
        editor.putInt("cirVideoLong", SettingBean.cirVideoLong);
        editor.putInt("mVideoQuality", SettingBean.mVideoQuality);
        editor.putInt("mHitDelicacy", SettingBean.mHitDelicacy);
        editor.putInt("powersaving_time", SettingBean.powersaving_time);
        editor.putInt("mPathItem", SettingBean.mPathItem);
        editor.putBoolean("isBackCamera", SettingBean.isBackCamera);
        editor.putBoolean("mEnableAutoRun", SettingBean.mEnableAutoRun);
        /* editor.putInt("mScreenBright", SettingBean.mScreenBright); */
        editor.commit();
    }
    

    /** * 判断是否�?��了自动亮度调�?*/

    public boolean isAutoBrightness(ContentResolver aContentResolver)
    {
        boolean automicBrightness = false;
        try
        {
            automicBrightness =
                (Settings.System.getInt(aContentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
        }
        catch (SettingNotFoundException e)
        {
            e.printStackTrace();
        }
        return automicBrightness;
    }

    /** * 停止自动亮度调节 */

    public void stopAutoBrightness(Activity activity)
    {
        Settings.System.putInt(activity.getContentResolver(),
            Settings.System.SCREEN_BRIGHTNESS_MODE,
            Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
    }

    /** * 获取屏幕的亮�?*/

    public int getScreenBrightness(Activity activity)
    {
        int nowBrightnessValue = 0;
        ContentResolver resolver = activity.getContentResolver();

        try
        {
            nowBrightnessValue =
                android.provider.Settings.System.getInt(resolver, Settings.System.SCREEN_BRIGHTNESS);
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }
        return nowBrightnessValue;
    }

    /** * 设置亮度 */

    public void setBrightness(Activity activity, int brightness)
    {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.screenBrightness = Float.valueOf(brightness) * (1f / 255f);
        activity.getWindow().setAttributes(lp);
    }

    /** * 保存亮度设置状�? */

    public void saveBrightness(ContentResolver resolver, int brightness)
    {
        Uri uri = android.provider.Settings.System.getUriFor("screen_brightness");
        android.provider.Settings.System.putInt(resolver, "screen_brightness", brightness);
        resolver.notifyChange(uri, null);
    }

    private void doPowerSavingSelect(String value, int pos)
    {
        SettingBean.powersaving_time = Integer.parseInt(value);
        PowerUitl.getPowerUitl().setValue(Integer.parseInt(value));
    }

    private void doSavePathSelect(String value, int pos)
    {
        Config.SAVE_PATH = value + Config.SAVE_FLOADER;
        // 常规录制视频保存路径
        Config.SAVE_RECORD_PATH = Config.SAVE_PATH + "/" + "video/";
        // 照片保存路径
        Config.SAVE_CAPTURE_PATH = Config.SAVE_PATH + "/" + "pic/";
        MediaTool.whereClause = MediaColumns.DATA + " like '" + Config.SAVE_PATH + "%'";
    }

    @Override
    protected Dialog onCreateDialog(int id)
    {
        switch (id)
        {
            // 省电模式
            case SHOWDIALOG_POWERSAVING:
                PowerSavingDialog savingDialog = new PowerSavingDialog(MSettingActivity.this);
                savingDialog.setOnItemClickListener(new OnSelectListListener()
                {
                    public void onSelectDialogItemClick(String value, int pos)
                    {
                        doPowerSavingSelect(value, pos);
                    }

                    public void onDialogCancel()
                    {
                        if (SettingBean.powersaving_time > 0)
                        {
                            //btnPowersaving.setText(getResources().getString(R.string.msetting_open));
                        }
                        else
                        {
                            //btnPowersaving.setText(getResources().getString(R.string.msetting_closed));
                        }
                    }
                });
                return savingDialog.createDialog();
                // 保存路径
            case SHOWDIALOG_SAVEPATH:
                SavePathDialog savePathDialog = new SavePathDialog(MSettingActivity.this);
                savePathDialog.setOnItemClickListener(new OnSelectListListener()
                {
                    public void onSelectDialogItemClick(String value, int pos)
                    {
                        doSavePathSelect(value, pos);
                    }

                    public void onDialogCancel()
                    {
                        MSettingActivity.this.removeDialog(SHOWDIALOG_SAVEPATH);
                    }
                });
                return savePathDialog.createDialog();
        }
        return super.onCreateDialog(id);
    }
}
