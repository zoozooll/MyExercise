package com.tcl.manager.activity;

import com.tcl.framework.log.NLog;
import com.tcl.manager.application.ManagerApplication;
import com.tcl.manager.update.UpdateChecker;
import com.tcl.manager.update.UpdateManager;
import com.tcl.manager.util.AndroidUtil;
import com.tcl.mie.manager.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * @Description: about us
 * @author wenchao.zhang
 * @date 2015年1月7日 下午3:37:00
 * @copyright TCL-MIE
 */

public class AboutUsActivity extends BaseActivity implements OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us);

        findViewById(R.id.header_action_bar_rl_left).setOnClickListener(this);
        TextView header_action_bar_tv_left = (TextView) findViewById(R.id.header_action_bar_tv_left);
        header_action_bar_tv_left.setText(R.string.main_popwindow_about_us);
        /*** debug **/
        findViewById(R.id.imageLogo).setOnClickListener(this);
        TextView textVersionname = (TextView) findViewById(R.id.textVersionname);
        textVersionname.setText(AndroidUtil.getVersionName(this));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.header_action_bar_rl_left:
            finish();
            break;

      /*  case R.id.layout_checkupdate:
        	doRealcheckUpdate();
        	Intent intent = new Intent(this, ParttimeActivity.class);
        	startActivity(intent);
            break;*/

        default:
            break;
        }
    }
    
    /**
	 * 真实的检查
	 */
	private void doRealcheckUpdate() {
		NLog.d("aaron", "doRealcheckUpdate");
		UpdateChecker uc = new UpdateChecker(this);
        uc.check();
	}

}
