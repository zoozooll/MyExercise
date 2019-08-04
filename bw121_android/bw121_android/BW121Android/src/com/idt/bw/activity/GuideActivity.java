package com.idt.bw.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GuideActivity extends Activity {
	private ViewPager guidePages;
	private View guidePagea,guidePageb,guidePagec,guidePaged,guidePagee;
	private ArrayList<View> pagerViews;
	private LinearLayout dianGroup;
	private RelativeLayout over_guide;
	private GuideViewPagerAdapter guideViewPagerAdapter;
	private ImageView[] dianImgViews; 
	private ImageView dianImg;  
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guide);
		
		init();

	}
	public void init(){
		LayoutInflater inflater = getLayoutInflater();  
		guidePagea = inflater.inflate(R.layout.guide_picture_a, null);
		guidePageb = inflater.inflate(R.layout.guide_picture_b, null);
		guidePagec = inflater.inflate(R.layout.guide_picture_c, null);
		guidePaged = inflater.inflate(R.layout.guide_picture_d, null);
		guidePagee = inflater.inflate(R.layout.guide_picture_e, null);
		pagerViews = new ArrayList<View>();
		pagerViews.add(guidePagea);
		pagerViews.add(guidePageb);
		pagerViews.add(guidePagec);
		pagerViews.add(guidePaged);
		pagerViews.add(guidePagee);
		guidePages = (ViewPager) findViewById(R.id.guidePages);
		dianGroup = (LinearLayout) findViewById(R.id.dianGroup);
		over_guide = (RelativeLayout)guidePagee.findViewById(R.id.over_guide);
		
		
		dianImgViews = new ImageView[pagerViews.size()];  
		
		for (int i = 0; i < pagerViews.size(); i++) {  
			dianImg = new ImageView(GuideActivity.this);  
			dianImg.setLayoutParams(new LayoutParams(20,20));  
			dianImg.setPadding(20, 0, 20, 0);  
			dianImgViews[i] = dianImg;  
            if (i == 0) {  
                //默认选中第一张图片
            	dianImgViews[i].setBackgroundResource(R.drawable.producttour_page_orange);  
            } else {  
            	dianImgViews[i].setBackgroundResource(R.drawable.producttour_page_gery);  
            }  
            dianGroup.addView(dianImgViews[i]);  
        }
		
		guideViewPagerAdapter = new GuideViewPagerAdapter();
		guidePages.setAdapter(guideViewPagerAdapter);
		guidePages.setOnPageChangeListener(new GuidePageChangeListener());
		
		over_guide.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				GuideActivity.this.finish();
			}
		});
	}
	
	class GuideViewPagerAdapter extends PagerAdapter{
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return pagerViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;  
		}
		
		@Override  
        public Object instantiateItem(View arg0, int arg1) {  
            // TODO Auto-generated method stub  
            ((ViewPager) arg0).addView(pagerViews.get(arg1));  
            return pagerViews.get(arg1);  
        }
		
		@Override  
        public void destroyItem(View arg0, int arg1, Object arg2) {  
            // TODO Auto-generated method stub  
            ((ViewPager) arg0).removeView(pagerViews.get(arg1));  
        }
	}
	
	/** 指引页面改监听器 */
    class GuidePageChangeListener implements OnPageChangeListener {  
        @Override  
        public void onPageScrollStateChanged(int arg0) {  
            // TODO Auto-generated method stub  
        	//有三种状态（0，1，2）。arg0 ==1的时辰默示正在滑动，arg0==2的时辰默示滑动完毕了，arg0==0的时辰默示什么都没做。
        	//Log.e("cdf","---------------------==================="+arg0);
        	/*if(arg0==2){
        		GuideActivity.this.finish();
        	}*/
        }  
  
        @Override  
        public void onPageScrolled(int arg0, float arg1, int arg2) {  
            // TODO Auto-generated method stub  
        	/*arg0 :当前页面，及你点击滑动的页面
        	arg1:当前页面偏移的百分比
        	arg2:当前页面偏移的像素位置  */
        	/*Log.e("cdf","-------------"+arg0+"--"+arg1+"--"+arg2);
        	if(arg0 == (pagerViews.size()-1)){
        		Log.e("cdf","==========="+arg0+"--"+arg1+"--"+arg2);
        		//if(arg2>100){
        			GuideActivity.this.finish();
        		//}
        	}*/
        }  
  
        @Override  
        public void onPageSelected(int arg0) {  
            for (int i = 0; i < dianImgViews.length; i++) {  
            	dianImgViews[arg0].setBackgroundResource(R.drawable.producttour_page_orange);  
                if (arg0 != i) {  
                	dianImgViews[i].setBackgroundResource(R.drawable.producttour_page_gery);  
                }  
            }
  
        }  
    }  
}
