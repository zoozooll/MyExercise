package com.beem.project.btf.camera.widgetscamera;

import java.util.ArrayList;
import java.util.Random;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.beem.project.btf.R;
import com.beem.project.btf.camera.widgetscamera.fragment.WatermarkChooseFragment;
import com.beem.project.btf.camera.widgetscamera.fragment.WatermarkChooseFragment.WaterMarkPlate;
import com.beem.project.btf.camera.widgetscamera.fragment.WatermarkChooseFragment.WatermarkMeta;
import com.beem.project.btf.ui.activity.base.VVBaseFragmentActivity;
import com.beem.project.btf.utils.LogUtils;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;

public class WatermarkChooseActivity extends VVBaseFragmentActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_activity_main);
		View title_top_bar = findViewById(R.id.title_top_bar);
		title_top_bar.setVisibility(View.GONE);
		ArrayList<WaterMarkPlate> plates = new ArrayList<WaterMarkPlate>();
		Random random = new Random();
		for (int i = 0; i < 10; i++) {
			WaterMarkPlate plate = null;
			ArrayList<WatermarkMeta> metas = new ArrayList<WatermarkMeta>();
			int metaNums = i % 3 == 1 ? random.nextInt(9) + 1 : random.nextInt(9) + 9;
			for (int j = 0; j < metaNums; j++) {
				WatermarkMeta meta = new WatermarkMeta(String.valueOf(i),
						Scheme.FILE.wrap("/mnt/sdcard/DCIM/gfx/answertile_temp.png_105x105"));
				metas.add(meta);
			}
			plate = new WaterMarkPlate(String.valueOf(i), metas);
			//LogUtils.i("i:" + i + " metas.size:" + metas.size());
			plates.add(plate);
		}
		WatermarkChooseFragment chooseFragment = WatermarkChooseFragment.newInstance(plates);
		FragmentManager frgmtManager = getSupportFragmentManager();
		FragmentTransaction frgmtTransaction = frgmtManager.beginTransaction();
		frgmtTransaction.add(R.id.id_content, chooseFragment, "chooseFragment");
		frgmtTransaction.commit();
	}
}
