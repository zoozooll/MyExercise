package com.dvr.android.dvr;

import com.dvr.android.dvr.msetting.MyImageButton;
import com.dvr.android.dvr.msetting.SettingBean;
import com.dvr.android.dvr.util.PowerUitl;
import com.dvr.android.dvr.R;

import android.app.Activity;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class SettingActivity extends Activity implements OnTouchListener, OnClickListener {

    // added by ji
    private MyImageButton settingRecordModeBtn = null;
    private MyImageButton settingRecordQualityBtn = null;
    private MyImageButton settingHitDelicacyBtn = null;
    private MyImageButton settingRecordBackBtn = null;
    private MyImageButton settingVideoSetBtn = null;

    // setting里面五项设置layout
    private RelativeLayout recordModeLayout;// 录制模式
    private RelativeLayout recordQualityLayout;// 视频质量
    private RelativeLayout hitDelicacyLayout;// 撞击灵敏度
    private RelativeLayout startingUpBackRecordLayout;// 开机相关
    private RelativeLayout otherSettingLayout;// 其它设置

    // set valible
    private static final int SETTING_TYPE_RECORD_MODE = 1;
    private static final int SETTING_TYPE_RECORD_QUALITY = 2;
    private static final int SETTING_TYPE_HITDELICACY = 3;
    private static final int SETTING_TYPE_RECORD_BACK = 4;
    private static final int SETTING_TYPE_VIDEO_SET = 5;

    // record mode
    private TextView mtextview_videoCycle_value;
    private ImageView btn_videoCycle_left = null;
    private ImageView btn_videoCycle_right = null;
    private String[] mCycleVideoTimes = null;

    // RecordQuality
    private TextView btn_recordQuality_high = null;
    private TextView btn_recordQuality_mid = null;
    private TextView btn_recordQuality_lower = null;

    // hitdelicacy
    private TextView mtextview_power_saving_value;
    private SeekBar seekbar_delicacy = null;
    private MyImageButton btn_hitdelicacy_auto_saving = null;
    private MyImageButton btn_click_screen = null; // 功能定义为打�?��制声�?
    private ImageView btn_power_save_left = null;
    private ImageView btn_power_save_right = null;
    private String[] mPowerSaveString;

    // back record
    private MyImageButton btn_record_back = null;
    private MyImageButton btn_open_back_camera = null;

    // video set
    private TextView resetDefaulBtn;
    private TextView mtextview_version;

    private MyImageButton btn_power_on_auto_run;

    private int nSettingType = SETTING_TYPE_RECORD_MODE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.msetting);

        settingRecordModeBtn = ((MyImageButton) this.findViewById(R.id.recordmode_button_id));
        recordModeLayout = (RelativeLayout) this.findViewById(R.id.record_mode_layout);
        settingRecordQualityBtn = ((MyImageButton) this.findViewById(R.id.recordquality_button_id));
        recordQualityLayout = (RelativeLayout) this.findViewById(R.id.record_quality_layout);
        settingHitDelicacyBtn = ((MyImageButton) this.findViewById(R.id.hitdelicacy_button_id));
        hitDelicacyLayout = (RelativeLayout) this.findViewById(R.id.hit_delicacy_layout);
        settingRecordBackBtn = ((MyImageButton) this.findViewById(R.id.recordback_button_id));
        otherSettingLayout = (RelativeLayout) this.findViewById(R.id.other_setting_layout);
        settingVideoSetBtn = ((MyImageButton) this.findViewById(R.id.videoset_button_id));
        startingUpBackRecordLayout = (RelativeLayout) this.findViewById(R.id.starting_up_back_record_layout);
        // 设置对话框五项事件监听
        settingRecordModeBtn.setOnTouchListener(this);
        settingRecordQualityBtn.setOnTouchListener(this);
        settingHitDelicacyBtn.setOnTouchListener(this);
        settingRecordBackBtn.setOnTouchListener(this);
        settingVideoSetBtn.setOnTouchListener(this);
        settingDrawTypeContent();

        // 循环录制时长控制
        btn_videoCycle_left.setOnClickListener(this);
        btn_videoCycle_right.setOnClickListener(this);

        // RecordQuality
        btn_recordQuality_high.setOnClickListener(this);
        btn_recordQuality_mid.setOnClickListener(this);
        btn_recordQuality_lower.setOnClickListener(this);

        // hitdelicacy
        seekbar_delicacy.setMax(30);
        seekbar_delicacy.setProgress(SettingBean.mHitDelicacy);
        seekbar_delicacy.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
                if (arg2) {
                    SettingBean.mHitDelicacy = arg1;
                    SettingBean.onChage(SettingBean.HIT_CHANGE);
                }
            }

            public void onStartTrackingTouch(SeekBar arg0) {
            }

            public void onStopTrackingTouch(SeekBar arg0) {
            }
        });
        btn_hitdelicacy_auto_saving.setOnClickListener(this);
        btn_click_screen.setOnClickListener(this);
        btn_power_save_left.setOnClickListener(this);
        btn_power_save_right.setOnClickListener(this);

        // back record
        btn_record_back.setOnClickListener(this);
        btn_open_back_camera.setOnClickListener(this);

        // video set
        resetDefaulBtn.setOnClickListener(this);
        resetDefaulBtn.setOnLongClickListener(new View.OnLongClickListener() {

            public boolean onLongClick(View v) {
                mtextview_version = ((TextView) SettingActivity.this.findViewById(R.id.apk_version_text_id));
                if (!mtextview_version.isShown()) {
                    String strVersion;
                    try {
                        strVersion = getPackageManager().getPackageInfo("com.dvr.android.dvr", 0).versionName;
                        mtextview_version.setText("Version: " + strVersion);
                    } catch (NameNotFoundException e) {
                        e.printStackTrace();
                    }
                    mtextview_version.setVisibility(View.VISIBLE);
                } else {
                    mtextview_version.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        });
        btn_power_on_auto_run.setOnClickListener(this);
    }

    private void settingDrawTypeContent() {
        if (nSettingType == SETTING_TYPE_RECORD_MODE) {
            showRecordModeUI(true);
            showRecordQualityUI(false);
            showhitdelicacyUI(false, true);
            showrecord_backUI(false);
            showvideo_setUI(false);
        } else if (nSettingType == SETTING_TYPE_RECORD_QUALITY) {
            showRecordModeUI(false);
            showRecordQualityUI(true);
            showhitdelicacyUI(false, true);
            showrecord_backUI(false);
            showvideo_setUI(false);
        } else if (nSettingType == SETTING_TYPE_HITDELICACY) {
            showRecordModeUI(false);
            showRecordQualityUI(false);
            showhitdelicacyUI(true, true);
            showrecord_backUI(false);
            showvideo_setUI(false);
        } else if (nSettingType == SETTING_TYPE_RECORD_BACK) {
            showRecordModeUI(false);
            showRecordQualityUI(false);
            showhitdelicacyUI(false, true);
            showrecord_backUI(true);
            showvideo_setUI(false);
        } else if (nSettingType == SETTING_TYPE_VIDEO_SET) {
            showRecordModeUI(false);
            showRecordQualityUI(false);
            showhitdelicacyUI(false, true);
            showrecord_backUI(false);
            showvideo_setUI(true);
        }
    }

    /**
     * 设置-录制模式
     * */
    private void showRecordModeUI(boolean bShow) {
        mtextview_videoCycle_value = ((TextView) this.findViewById(R.id.video_time_value_text_id));
        btn_videoCycle_left = ((ImageView) this.findViewById(R.id.video_time_left_button_id));
        btn_videoCycle_right = ((ImageView) this.findViewById(R.id.video_time_right_button_id));

        mtextview_power_saving_value = ((TextView) this.findViewById(R.id.power_save_value_text_id));
        btn_power_save_left = ((ImageView) this.findViewById(R.id.power_save_left_button_id));
        btn_power_save_right = ((ImageView) this.findViewById(R.id.power_save_right_button_id));
        if (bShow) {
            showPowerSaveString(true);
            showCycleVideoTime();
            recordModeLayout.setVisibility(View.VISIBLE);
        } else {
            recordModeLayout.setVisibility(View.INVISIBLE);
        }
    }

    private void showPowerSaveString(boolean bInit) {
        int[] values = getResources().getIntArray(R.array.setting_power_saving_value);
        if (bInit) {
            for (int i = 0; i < values.length; i++) {
                if (SettingBean.powersaving_time == values[i]) {
                    SettingBean.powerSaveIndex = i;
                    break;
                }
            }
        } else {
            SettingBean.powersaving_time = values[SettingBean.powerSaveIndex];
            PowerUitl.getPowerUitl().setValue(SettingBean.powersaving_time);
        }
        mtextview_power_saving_value.setText(mPowerSaveString[SettingBean.powerSaveIndex]);
    }

    private void showCycleVideoTime() {
        mCycleVideoTimes = getResources().getStringArray(R.array.setting_record_time_str);
        mtextview_videoCycle_value.setText(mCycleVideoTimes[SettingBean.cirVideoLong]);
    }

    private void showRecordQualityUI(boolean bShow) {
        btn_recordQuality_high = ((TextView) this.findViewById(R.id.recordquality_high_button_id));
        btn_recordQuality_mid = ((TextView) this.findViewById(R.id.recordquality_middle_button_id));
        btn_recordQuality_lower = ((TextView) this.findViewById(R.id.recordquality_lower_button_id));
        if (bShow) {
            showRecordQualityButton();
            recordQualityLayout.setVisibility(View.VISIBLE);
        } else {
            recordQualityLayout.setVisibility(View.INVISIBLE);
        }
    }

    private void showRecordQualityButton() {
        if (SettingBean.mVideoQuality == 1) {
            btn_recordQuality_high.setBackground(getResources().getDrawable(R.drawable.btn_record_quality_pressed));
            btn_recordQuality_mid.setBackground(getResources().getDrawable(R.drawable.btn_record_quality_normal));
            btn_recordQuality_lower.setBackground(getResources().getDrawable(R.drawable.btn_record_quality_normal));
        } else if (SettingBean.mVideoQuality == 2) {
            btn_recordQuality_high.setBackground(getResources().getDrawable(R.drawable.btn_record_quality_normal));
            btn_recordQuality_mid.setBackground(getResources().getDrawable(R.drawable.btn_record_quality_pressed));
            btn_recordQuality_lower.setBackground(getResources().getDrawable(R.drawable.btn_record_quality_normal));
        } else if (SettingBean.mVideoQuality == 3) {
            btn_recordQuality_high.setBackground(getResources().getDrawable(R.drawable.btn_record_quality_normal));
            btn_recordQuality_mid.setBackground(getResources().getDrawable(R.drawable.btn_record_quality_normal));
            btn_recordQuality_lower.setBackground(getResources().getDrawable(R.drawable.btn_record_quality_pressed));
        }
    }

    private void showhitdelicacyUI(boolean bShow, boolean bInit) {
        btn_hitdelicacy_auto_saving = ((MyImageButton) this.findViewById(R.id.hit_auto_saving_button_id));
        btn_click_screen = ((MyImageButton) this.findViewById(R.id.click_screen_saving_button_id));
        seekbar_delicacy = (SeekBar) this.findViewById(R.id.seekbar_delicacy);

        if (bShow) {
            // init
            showhitdelicacyButton();
            showClickSaveButton();
            hitDelicacyLayout.setVisibility(View.VISIBLE);
        } else {
            hitDelicacyLayout.setVisibility(View.INVISIBLE);
        }
    }

    private void showhitdelicacyButton() {
        if (SettingBean.isHitAutosave) {
            btn_hitdelicacy_auto_saving.setImageDrawable(getResources().getDrawable(R.drawable.btn_control_open));
        } else {
            btn_hitdelicacy_auto_saving.setImageDrawable(getResources().getDrawable(R.drawable.btn_control_close));
        }
    }

    private void showClickSaveButton() {
        if (SettingBean.isRecordSound) {
            btn_click_screen.setImageDrawable(getResources().getDrawable(R.drawable.btn_control_open));
        } else {
            btn_click_screen.setImageDrawable(getResources().getDrawable(R.drawable.btn_control_close));
        }
    }

    private void showrecord_backUI(boolean bShow) {
        resetDefaulBtn = ((TextView) this.findViewById(R.id.btnfour_defealt));
        btn_open_back_camera = ((MyImageButton) this.findViewById(R.id.btn_open_back_camera_id));
        if (bShow) {
            showOpenBackCameraButton();
            otherSettingLayout.setVisibility(View.VISIBLE);
        } else {
            otherSettingLayout.setVisibility(View.INVISIBLE);
        }
    }

    private void showOpenBackCameraButton() {
        if (SettingBean.mOpenBackCamera) {
            btn_open_back_camera.setImageDrawable(getResources().getDrawable(R.drawable.btn_control_open));
        } else {
            btn_open_back_camera.setImageDrawable(getResources().getDrawable(R.drawable.btn_control_close));
        }
    }

    private void showvideo_setUI(boolean bShow) {
        btn_power_on_auto_run = ((MyImageButton) this.findViewById(R.id.btn_power_on_auto_run_id));
        btn_record_back = ((MyImageButton) this.findViewById(R.id.btnthree_bgrecord));
        if (bShow) {
            showVideoAutoRunButton();
            showBackRecordButton();
            startingUpBackRecordLayout.setVisibility(View.VISIBLE);
        } else {
            startingUpBackRecordLayout.setVisibility(View.INVISIBLE);
        }
    }

    private void showVideoAutoRunButton() {
        if (SettingBean.mEnableAutoRun) {
            btn_power_on_auto_run.setImageDrawable(getResources().getDrawable(R.drawable.btn_control_open));
        } else {
            btn_power_on_auto_run.setImageDrawable(getResources().getDrawable(R.drawable.btn_control_close));
        }
    }

    private void showBackRecordButton() {
        if (SettingBean.mBGRecord) {
            btn_record_back.setImageDrawable(getResources().getDrawable(R.drawable.btn_control_open));
        } else {
            btn_record_back.setImageDrawable(getResources().getDrawable(R.drawable.btn_control_close));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public boolean onTouch(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            switch (view.getId()) {
            case R.id.recordmode_button_id:
                nSettingType = SETTING_TYPE_RECORD_MODE;
                settingRecordModeBtn.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_recordmode_bg_pressed));
                settingRecordQualityBtn.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_videoquality_bg_normal));
                settingHitDelicacyBtn.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_hit_bg_normal));
                settingRecordBackBtn.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_back_bg_normal));
                settingVideoSetBtn.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_videoset_bg_normal));
                break;
            case R.id.recordquality_button_id:
                nSettingType = SETTING_TYPE_RECORD_QUALITY;
                settingRecordModeBtn.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_recordmode_bg_normal));
                settingRecordQualityBtn.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_videoquality_bg_pressed));
                settingHitDelicacyBtn.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_hit_bg_normal));
                settingRecordBackBtn.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_back_bg_normal));
                settingVideoSetBtn.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_videoset_bg_normal));
                break;
            case R.id.hitdelicacy_button_id:
                nSettingType = SETTING_TYPE_HITDELICACY;
                settingRecordModeBtn.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_recordmode_bg_normal));
                settingRecordQualityBtn.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_videoquality_bg_normal));
                settingHitDelicacyBtn.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_hit_bg_pressed));
                settingRecordBackBtn.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_back_bg_normal));
                settingVideoSetBtn.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_videoset_bg_normal));
                break;
            case R.id.recordback_button_id:
                nSettingType = SETTING_TYPE_RECORD_BACK;
                settingRecordModeBtn.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_recordmode_bg_normal));
                settingRecordQualityBtn.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_videoquality_bg_normal));
                settingHitDelicacyBtn.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_hit_bg_normal));
                settingRecordBackBtn.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_back_bg_pressed));
                settingVideoSetBtn.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_videoset_bg_normal));
                break;
            case R.id.videoset_button_id:
                nSettingType = SETTING_TYPE_VIDEO_SET;
                settingRecordModeBtn.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_recordmode_bg_normal));
                settingRecordQualityBtn.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_videoquality_bg_normal));
                settingHitDelicacyBtn.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_hit_bg_normal));
                settingRecordBackBtn.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_back_bg_normal));
                settingVideoSetBtn.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_videoset_bg_pressed));
                break;
            }
            settingDrawTypeContent();
        }
        return false;
    }

    public void onClick(View view) {
        switch (view.getId()) {
        case R.id.btnthree_bgrecord:
            SettingBean.mBGRecord = !SettingBean.mBGRecord;
            showBackRecordButton();
            break;
        case R.id.btn_power_on_auto_run_id:
            SettingBean.mEnableAutoRun = !SettingBean.mEnableAutoRun;
            showVideoAutoRunButton();
            break;
        case R.id.btnfour_defealt:
            setdefealt();
            break;
        case R.id.video_time_left_button_id:
            if (SettingBean.cirVideoLong > 0) {
                SettingBean.cirVideoLong--;
                showCycleVideoTime();
            }
            break;
        case R.id.video_time_right_button_id:
            if (SettingBean.cirVideoLong < (mCycleVideoTimes.length - 1)) {
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
            SettingBean.isRecordSound = !SettingBean.isRecordSound;
            showClickSaveButton();
            break;
        case R.id.btn_open_back_camera_id:
            SettingBean.mOpenBackCamera = !SettingBean.mOpenBackCamera;
            showOpenBackCameraButton();
            break;
        case R.id.power_save_left_button_id:
            if (SettingBean.powerSaveIndex > 0) {
                SettingBean.powerSaveIndex--;
                showPowerSaveString(false);
            }
            break;
        case R.id.power_save_right_button_id:
            if (SettingBean.powerSaveIndex < (mPowerSaveString.length - 1)) {
                SettingBean.powerSaveIndex++;
                showPowerSaveString(false);
            }
            break;
        }
    }

    private void setdefealt() {
        SettingBean.isHitAutosave = SettingBean.IsHitAutosave;
        SettingBean.isClickSave = SettingBean.IsClickSave;
        SettingBean.isHasvoice = SettingBean.IsHasvoice;
        SettingBean.isRecordSound = SettingBean.IsRecordSound;
        SettingBean.cirVideoLong = SettingBean.CirVideoLong;
        SettingBean.mVideoQuality = SettingBean.MVideoQuality;
        SettingBean.mHitDelicacy = SettingBean.MHitDelicacy;
        SettingBean.mRecordMode = SettingBean.MRecordMode;
        SettingBean.mBGRecord = SettingBean.MBGRecord;
        SettingBean.mOpenBackCamera = SettingBean.MOpenBackCamera;
        SettingBean.mBGAnim = SettingBean.MBGAnim;
        SettingBean.powersaving_time = SettingBean.PowerSaving_time;
        SettingBean.mPathItem = SettingBean.MPathItem;
        SettingBean.isBackCamera = SettingBean.IsBackCamera;
        SettingBean.mEnableAutoRun = SettingBean.MEnableAutoRun;

        seekbar_delicacy.setProgress(SettingBean.mHitDelicacy);

        int[] values = getResources().getIntArray(R.array.setting_power_saving_value);
        for (int i = 0; i < values.length; i++) {
            if (SettingBean.powersaving_time == values[i]) {
                SettingBean.powerSaveIndex = i;
                break;
            }
        }
    }
}
