package com.iskyinfor.duoduo.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.iskyinfor.duoduo.R;

public class SpinnerView extends Button implements OnClickListener {
	private Context mContext = null;
	
	/**
	 * 显示的文本信息
	 */
	private String[] array = null;

	/**
	 * 事件监听接口
	 */
	private SpinnserPopuViewListener popuView = null;
	
	/**
	 * 布局
	 */
	private LinearLayout layout = null;

	/**
	 * 动画效果
	 */
	private ViewFlipper mViewFlipper = null;

	/**
	 * 数据
	 */
	private int dataId;
	private int firstID;
	private int centerID;
	private int endID;
	
	/**
	 * 控制点击按钮是否消失
	 */
	private boolean flag = false;
	
	/**
	 * 发送消息
	 */
	private Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				int position = (Integer) msg.obj;
				 BackgroundResource(R.drawable.complete_btn);
				setTextValue(array[position]);
				refreshView();
				break;
			default:
				break;
			}
		};
	};

	/**
	 * 给按钮赋值
	 */
	public void setTextValue(String textValue) {
		this.setText(textValue);
	}
	/**
	 * 设置背景
	 */
	public void BackgroundResource(int ResourceId) {
		this.setBackgroundResource(ResourceId);
	}

	/**
	 * 刷新数据
	 */
	public void refreshView() 
	{
		this.invalidate();
	}
	
	/**
	 * 取到配置文件中的数据
	 * 
	 * @param context
	 * @param attrs
	 */
	private void getAttrValues(Context context, AttributeSet attrs) {

		TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.SpinnerView);
		dataId = a.getResourceId(R.styleable.SpinnerView_textInfo, 0);
		
		if (dataId != 0) {
			array = getResources().getStringArray(dataId);
		}
		
		firstID = a.getResourceId(R.styleable.SpinnerView_lesson_spinner_first,0);
		centerID = a.getResourceId(R.styleable.SpinnerView_lesson_spinner_center, 0);
		endID = a.getResourceId(R.styleable.SpinnerView_lesson_spinner_end, 0);
		a.recycle();
		mContext = context;
	}

	/**
	 * 构造器
	 * 
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public SpinnerView(Context context, AttributeSet attrs, int defStyle) 
	{
		super(context, attrs, defStyle);
		getAttrValues(context, attrs); // 取值
		this.setOnClickListener(this);
		
	}

	/**
	 * 构造器
	 * 
	 * @param context
	 * @param attrs
	 */
	public SpinnerView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		mContext = context;
		getAttrValues(context, attrs); // 取值
		this.setOnClickListener(this);
	}

	/**
	 * 构造器
	 * 
	 * @param context
	 */
	public SpinnerView(Context context) 
	{
		super(context);
		mContext = context;
		this.setOnClickListener(this);
	}

	/**
	 * 数据的get和set方法
	 * 
	 * @return
	 */
	public String[] getArray() 
	{
		return array;
	}

	public void setArray(String[] array) 
	{
		this.array = array;
	}

	/**
	 * 事件的get和set方法
	 * 
	 * @return
	 */
	public SpinnserPopuViewListener getPopuView() {
		return popuView;
	}

	public void setPopuView(SpinnserPopuViewListener popuView) {
		this.popuView = popuView;
	}
	
	/**
	 * 点击事件显示PopupWindows
	 */
	@Override
	public void onClick(View v) {
		ShowPopupWindowMethod();
		if (!flag) {
			this.popuView.show(mViewFlipper, true);
			this.setBackgroundResource(R.drawable.down01_spinner);
			flag = true;
		} else {
			this.setBackgroundResource(R.drawable.complete_btn);
			this.popuView.show(mViewFlipper, false);
			flag = false;
		}
	}

	/**
	 * 显示对话框的方法
	 * 
	 * @author yangyongjie
	 * 
	 */
	public void ShowPopupWindowMethod() {
		// 创建动画
		mViewFlipper = new ViewFlipper(mContext);
		mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(mContext,R.anim.menu_in));
		mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(mContext,R.anim.menu_out));

		// 加载布局
		layout = new LinearLayout(mContext);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));

		// 加载数据
		ListView listView = new ListView(mContext);
			listView.setVerticalScrollBarEnabled(false);
		listView.setLayoutParams(new LayoutParams(120, LayoutParams.WRAP_CONTENT));
		PopupWindowAdapter adapter = new PopupWindowAdapter(mContext, array,12, Color.BLUE, firstID, centerID, endID);
		listView.setAdapter(adapter);
		listView.setFocusable(true);
		layout.addView(listView);
		mViewFlipper.addView(layout);
	}

	/**
	 * 事件监听接口
	 * 
	 * @author yangyongjie
	 * 
	 */
	
	public interface SpinnserPopuViewListener {
		public void show(ViewFlipper v, boolean isShow);
		public void onItemClick(ListView listView, int position );
		
	}
	
	class OnclickListener implements OnClickListener{
		
		private int position;
		
		

		public OnclickListener(int position) {
			super();
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			Message message = new Message();
			message.what = 0;
			message.obj = position;
			handler.sendMessage(message);
			flag = false;
			popuView.show(null, flag);
			
		}
		
	}
	

	/**
	 * 适配器
	 * @author yangyongjie
	 *
	 */
	public class PopupWindowAdapter extends BaseAdapter {
		@SuppressWarnings("unused")
		private Context mContext = null;
		private TextView[] title = null;
		@SuppressWarnings("unused")
		private int fontSize = 0;
		@SuppressWarnings("unused")
		private int color;

		/**
		 * 给ListView上的每一项设置背景 firstId 第一项 centerID 中间部分 endID 最后一项
		 */
		private int firstId;
		private int centerID;
		private int endID;

		/**
		 * 构建菜单项
		 * 
		 * @param mContext上下文
		 * @param title标题
		 * @param fontSize字体大小
		 * @param color字体颜色
		 */

		public PopupWindowAdapter(Context mContext, String[] titles, int fontSize,
				int color, int firstId, int centerID, int endID) {
			super();
			this.mContext = mContext;
			this.title = new TextView[titles.length];
			this.fontSize = fontSize;
			this.color = color;
			this.firstId = firstId;
			this.centerID = centerID;
			this.endID = endID;

			for (int i = 0; i < titles.length; i++) {
				title[i] = new TextView(mContext);
				title[i].setText(titles[i]);
				title[i].setTextSize(fontSize);
				title[i].setTextColor(color);
				title[i].setGravity(Gravity.CENTER);
				title[i].setPadding(10, 10, 10, 10);
			}
		}

		@Override
		public int getCount() {

			return title.length;
		}

		@Override
		public Object getItem(int position) {
			return title[position];
		}

		@Override
		public long getItemId(int position) {
			return title[position].getId();
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View view = null;
			
			if (convertView == null) {
				view = title[position];
				/**
				 * 设置ListView的每一项的监听事件
				 */
				view.setOnClickListener(new OnclickListener(position));
				/**
				 * 为ListView上的每一项加背景
				 */
				if (position == 0) {
					view.setBackgroundResource(firstId);
				} else if (position == title.length - 1) {
					view.setBackgroundResource(endID);
				} else {
					view.setBackgroundResource(centerID);
				}
			} else {
				view = convertView;
			}
			return view;
		}
	}
}