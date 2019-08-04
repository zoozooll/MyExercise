package com.mogoo.market.ui;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.mogoo.market.R;

import com.mogoo.market.model.HotKeyInfo;
import com.mogoo.market.model.HotKeyInfoListReq;
import com.mogoo.market.model.onRequestCallBack;
import com.mogoo.market.uicomponent.MyToast;
import com.mogoo.market.utils.ToolsUtils;
import com.mogoo.market.widget.KeywordsFlow;
import com.mogoo.market.widget.SearchBar;
import com.mogoo.parser.Result;


/**
 * 搜索
 */
public class SearchActivity extends BaseLoadableListActivityWithViewAndHeader implements OnTouchListener,
OnGestureListener
{
	
    private View noDateView;

	/**蒙板层**/
	private ImageView mMaskLayer;
    
    private SearchBar mSearchBar;
    private KeywordsFlow keywordsFlow;
    private ViewFlipper  mViewFilpper;
	//手势检测
	private GestureDetector mGestureDetector;
	//滑动像素 ,大于这个值的时候才滑动
	private final int SliderPixD = 500;
	//当前页
	private int currentIndex =1;
	//获得 推荐组建TextView 集合 
 	//测试代码
	private  static ArrayList<HotKeyInfo> keywords=new ArrayList();
    
    
	private Context mContext;
    
    private String keyword = "";
	
	private View mMarket_center_lay;
	private View mMarket_top_lay;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		this.mContext=this;
		
		loadData();
		init();
		MyToast.makeText(this,
				getResources().getText(R.string.search_tip_text),
				Toast.LENGTH_LONG).show();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	 
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		
		//return super.onTouchEvent(event);
		return mGestureDetector.onTouchEvent(event);
	}

	public void init()
	{
		mMarket_top_lay=LayoutInflater.from(this).inflate(R.layout.top_cate, null);
	 	noDateView=new TextView(this);
		((TextView) noDateView).setText(this.getResources().getString(R.string.nodata));
		
		mGestureDetector = new GestureDetector(this);
		
 		mSearchBar = (SearchBar)mMarket_top_lay.findViewById(R.id.SearchBar);
 	 	
 		setAnimListener();
	}
	
	/**
	 * 添加动画界面的事件
	 * */
	
	public void setAnimListener(){
 		
		mMarket_center_lay=LayoutInflater.from(this).inflate(R.layout.search_activity, null);
 		keywordsFlow = (KeywordsFlow)mMarket_center_lay.findViewById(R.id.keywordsFlow);
 		keywordsFlow.setVisibility(View.VISIBLE);
 		mViewFilpper =(ViewFlipper)mMarket_center_lay.findViewById(R.id.search_viewflipper);
		mMaskLayer = (ImageView)mMarket_center_lay.findViewById(R.id.maskLayer);
		
		mSearchBar.mEditText.clearFocus();
		mSearchBar.mEditText.setText(keyword);
	//   mSearchBar.mEditText.setOnFocusChangeListener(mEditFoucListener);
		mMaskLayer.setOnClickListener(mEventListner);

		mSearchBar.mSearBtn.setOnClickListener(mEventListner);
		    
		keywordsFlow.setDuration(800l);  
	    keywordsFlow.setOnItemClickListener(mEventListner); 
	    
	    getBaseCenterLayout().setVisibility(View.VISIBLE);
		setBaseCenterLayout(mMarket_center_lay);
		
		getBaseHeaderLayout().setVisibility(View.VISIBLE);
		setBaseTopLayout(mMarket_top_lay);
	}
	
	/**
	 * 加载数据
	 * @param url--请求地址
	 * @param requestType --0：post，1:get
	 * @param params --请求参数
	 * @return 
	 */
	private void loadData()
	{
		HotKeyInfoListReq.onGetHotKey(this, new onRequestCallBack(){

			@Override
			public void onFail(Result result) {
				// TODO Auto-generated method stub
				Log.d("keywords", "keywords===========false========================================"+keywords);
			}

			@Override
			public void onSuccess(Result result) {
				// TODO Auto-generated method stub
				keywords=(ArrayList<HotKeyInfo>) result.getData();
		
				feedkeywordsFlow(keywordsFlow,keywords);
				keywordsFlow.go2Show(KeywordsFlow.ANIMATION_IN);
				Log.d("keyworws", "keywords==================================================="+keywords);
			}
			 
		});
		
	}
	
	
	
	private android.view.View.OnClickListener mEventListner = new android.view.View.OnClickListener()
    {
		
		public void onClick(View v) {
			
	        if(v.getId()==R.id.search_btn){
                        hiddenInput(mSearchBar.mEditText);
	        	keyword = mSearchBar.mEditText.getText().toString().trim();
	        	
	        }
	        else if(v instanceof TextView)
			{
				keyword = ((TextView) v).getText().toString(); 
                mSearchBar.mEditText.setText(keyword);
			}
	        if(keyword==null||keyword.equals("")){
				MyToast.makeText(mContext, R.string.key_words_tip, MyToast.LENGTH_SHORT).show();
				return;
			}
	        Intent childintent = new Intent();
	        childintent.putExtra("keyword", keyword);
	        childintent.setClass(mContext, SearchKeywordListActivity.class);
			MarketGroupActivity mga = MarketGroupActivity.getInstance();
			childintent.putExtra(MarketGroupActivity.EXTRA_BACK_INTENT, mga.getNowTabIntent());
			mga.setupTabChildView(childintent);
			mga.setupTabIntent(MarketGroupActivity.getCurrentTab(),childintent);
		}
		
	};
	
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
	}
	
	/**隐藏软键盘*/
	public void hiddenInput(EditText t){

		InputMethodManager imm=(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
		if(SearchActivity.this.getCurrentFocus()!=null)
                imm.hideSoftInputFromWindow(SearchActivity.this.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
	}
	
	
    /** 搜索框的 FocusChangeListener */
    private OnFocusChangeListener mEditFoucListener = new OnFocusChangeListener()
    {
    	public void onFocusChange(View view, boolean hasFocse)
        {
    		mSearchBar.mEditText.setInputType(InputType.TYPE_NULL);
    		
            if(hasFocse)
            {
               // mMaskLayer.setVisibility(View.VISIBLE);

            }
        }
    };
    

    
	/**---- 按下事件，获得推荐字符串对应的keyword----**/
	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub

		return true;
	}
	
	/**---- 左右切换响应的事件 ----**/
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		//keywords 为空不处理滑动事件
	    if(keywords == null || keywords.size() < 1){
	        return false;
	    }
	    
		if( velocityX < SliderPixD )
		{
			HandleNext();
			return true;
		}
		else if ( velocityX > SliderPixD ) 
		{
			HandlePre();		
			return true;
		}
		
		return false;
	}
    
	/**---- 处理上一页 左 ----**/
	private void HandlePre()
	{
		if(keywordsFlow.MAX * currentIndex >= keywords.size()){
			currentIndex=1;
		}else{
			currentIndex++;
		}
		gotoPage(KeywordsFlow.ANIMATION_OUT);
	}
	
	/**---- 处理下一页 右 ----**/
	private void HandleNext()
	{
	    if(currentIndex==1){
	        currentIndex=(int) Math.ceil(((double) keywords.size())/keywordsFlow.MAX);
	    }else{
	        currentIndex--;
	    }
		gotoPage(KeywordsFlow.ANIMATION_IN);
	}
	
	private void gotoPage(int type)
	{
		//清空数组，然后联网拉取最新的字符串
	    keywordsFlow.rubKeywords();
		feedkeywordsFlow(keywordsFlow,keywords);
		keywordsFlow.go2Show(type);
	}
	
	/**
	 * 随机生成12个不重复的数，找出对应的字符串
	 * @param keywordsFlow
	 * @param arr
	 */
    private void feedkeywordsFlow(KeywordsFlow keywordsFlow,ArrayList<HotKeyInfo> arr)
    {
    	int size = keywordsFlow.MAX * currentIndex;
    	size = arr.size()>=size? size:arr.size();
    	for(int i=keywordsFlow.MAX * (currentIndex - 1); i < size; i++)
    	{
    		HotKeyInfo key=arr.get(i);	
    		String tmp = key.getName();
    		keywordsFlow.feedKeyword(tmp);
    	}
    }
	
	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return mGestureDetector.onTouchEvent(event);
	}
	
	private long exitTime = 0;
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			if((System.currentTimeMillis() - exitTime) > 5000) {
				ToolsUtils.showExitToast(getApplicationContext());
				exitTime = System.currentTimeMillis();
//                feedkeywordsFlow(keywordsFlow,keywords);
//				keywordsFlow.go2Show(KeywordsFlow.ANIMATION_IN);
//		        getBaseCenterLayout().setVisibility(View.VISIBLE);
//		 		setBaseCenterLayout(mMarket_center_lay);
			}else {
				Intent intent = new Intent(SearchActivity.this, MarketGroupActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra(MarketGroupActivity.EXTRA_FINISH_SELF, true);
				startActivity(intent);
			}
			return true;
		}
		return false;

	}
}
