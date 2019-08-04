/**
 * Copyright 2010 Mark Wyszomierski
 */

package com.mogoo.market.widget;


import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.mogoo.market.R;


/**
 * A multi-button control which can only have one pressed button at
 * any given time. Its functionality is quite similar to a tab control.
 * Tabs can't be skinned in android 1.5, and our main frame is a tab
 * host - which causes some different android problems, thus this control
 * was created.
 * 
 * @date September 15, 2010
 * @author Mark Wyszomierski (markww@gmail.com)
 */
public class SegmentedButton extends LinearLayout {

    private StateListDrawable mBgLeftOn;
    private StateListDrawable mBgRightOn;
    private StateListDrawable mBgCenterOn;
    private StateListDrawable mBgLeftOff;
    private StateListDrawable mBgRightOff;
    private StateListDrawable mBgCenterOff;
    private int mSelectedButtonIndex = 0;
    
    private List<String> mButtonTitles = new ArrayList<String>();
//    private int mColorOnStart;
//    private int mColorOnEnd;
//    private int mColorOffStart;
//    private int mColorOffEnd;
//    private int mColorSelectedStart;
//    private int mColorSelectedEnd;
//    private int mColorStroke;
//    private int mStrokeWidth;
//    private int mCornerRadius;
    private int mTextStyle;
    private int mBtnPaddingTop;
    private int mBtnPaddingBottom;
    
    private int mNormalDrawable;
    private int mPressedDrawable;
    private int mSelectedDrawable;
    
	private int mTextSelectedColor = 0xFFFFFFFF;
	private int mTextUnSelectedColor = 0xFFFFFFFF;
	
	private int mBtnHeight = 0;
	private int mBtnWidth = 0;
	
    private Context mContext;
    
	private OnTouchListenerSegmentedButton mOnTouchListenerExternal;
    
    
    public SegmentedButton(Context context) {
        super(context);
        mContext = context;
    }
    
    public SegmentedButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SegmentedButton, 0, 0);
        
        CharSequence btnText1 = a.getString(R.styleable.SegmentedButton_btnText1);
        CharSequence btnText2 = a.getString(R.styleable.SegmentedButton_btnText2);
        if (btnText1 != null) {
            mButtonTitles.add(btnText1.toString());
        }
        if (btnText2 != null) {
            mButtonTitles.add(btnText2.toString());
        }
//        mColorOnStart = a.getColor(R.styleable.SegmentedButton_gradientColorOnStart, 0xFF0000);
//        mColorOnEnd = a.getColor(R.styleable.SegmentedButton_gradientColorOnEnd, 0xFF0000);
//        mColorOffStart = a.getColor(R.styleable.SegmentedButton_gradientColorOffStart, 0xFF0000);
//        mColorOffEnd = a.getColor(R.styleable.SegmentedButton_gradientColorOffEnd, 0xFF0000);
//        mColorStroke = a.getColor(R.styleable.SegmentedButton_strokeColor, 0xFF0000);
//        mColorSelectedEnd = a.getColor(R.styleable.SegmentedButton_gradientColorSelectedEnd, 0xFF0000);
//        mColorSelectedStart = a.getColor(R.styleable.SegmentedButton_gradientColorSelectedStart, 0xFF0000);
//        mStrokeWidth = a.getDimensionPixelSize(R.styleable.SegmentedButton_strokeWidth, 1);
//        mCornerRadius = a.getDimensionPixelSize(R.styleable.SegmentedButton_cornerRadius, 4);
        
        mTextStyle = a.getResourceId(R.styleable.SegmentedButton_textStyle, -1);
        mBtnPaddingTop = a.getDimensionPixelSize(R.styleable.SegmentedButton_btnPaddingTop, 0);
        mBtnPaddingBottom = a.getDimensionPixelSize(R.styleable.SegmentedButton_btnPaddingBottom, 0);
        mNormalDrawable = a.getResourceId(R.styleable.SegmentedButton_normalDrawable, 0);
        mPressedDrawable = a.getResourceId(R.styleable.SegmentedButton_pressedDrawable, 0);
        mSelectedDrawable = a.getResourceId(R.styleable.SegmentedButton_selectedDrawable, 0);
        
        mTextSelectedColor = a.getColor(R.styleable.SegmentedButton_textSelectedColor, mTextSelectedColor);
        mTextUnSelectedColor = a.getColor(R.styleable.SegmentedButton_textUnSelectedColor, mTextUnSelectedColor);
        
        mBtnHeight = a.getDimensionPixelSize(R.styleable.SegmentedButton_btnHeight, 0);
        mBtnWidth = a.getDimensionPixelSize(R.styleable.SegmentedButton_btnWidth, 0);
        
        a.recycle();
        buildDrawables();
/*        buildDrawables(mColorOnStart, mColorOnEnd, mColorOffStart, mColorOffEnd, 
                mColorSelectedStart, mColorSelectedEnd, mCornerRadius, mColorStroke, 
                mStrokeWidth);*/
        
        if (mButtonTitles.size() > 0) {
            _addButtons(new String[mButtonTitles.size()]);
        }
    }
    
    public void clearButtons() {
        removeAllViews();
    }
    
    public void addButtons(String ... titles) {
        _addButtons(titles);
    }
    
    private void _addButtons(String[] titles) {

		for (int i = 0; i < titles.length; i++) {

			Button button = new Button(getContext());
			button.setText(titles[i]);
			button.setTag(new Integer(i));
			button.setOnTouchListener(mOnTouchListener);
			if (mTextStyle != -1) {
				button.setTextAppearance(getContext(), mTextStyle);
			}

			if (titles.length == 1) {
				// Don't use a segmented button with one button.
				return;
			} else if (titles.length == 2) {
				if (i == 0) {
					button.setBackgroundDrawable(mBgLeftOff);
				} else {
					button.setBackgroundDrawable(mBgRightOn);
				}
			} else {
				if (i == 0) {
					button.setBackgroundDrawable(mBgLeftOff);
				} else if (i == titles.length - 1) {
					button.setBackgroundDrawable(mBgRightOn);
				} else {
					button.setBackgroundDrawable(mBgCenterOn);
				}
			}
			button.setWidth(mBtnWidth);
			button.setHeight(mBtnHeight);
			LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(0,
					LinearLayout.LayoutParams.WRAP_CONTENT, 1);
			addView(button, llp);
			button.setTextColor(mTextUnSelectedColor);
			button.setPadding(0, mBtnPaddingTop, 0, mBtnPaddingBottom);
		}
    }
    
    private ColorStateList creatColorStateList(int normal, int pressed, int focused, int unable) {
		int[] colors = new int[] { pressed, focused, normal, focused, unable,
				normal };
		int[][] states = new int[6][];
		states[0] = new int[] { android.R.attr.state_pressed,
				android.R.attr.state_enabled };
		states[1] = new int[] { android.R.attr.state_enabled,
				android.R.attr.state_focused };
		states[2] = new int[] { android.R.attr.state_enabled };
		states[3] = new int[] { android.R.attr.state_focused };
		states[4] = new int[] { android.R.attr.state_window_focused };
		states[5] = new int[] {};
		ColorStateList colorList = new ColorStateList(states, colors);
		return colorList;
    }
    
    private void buildDrawables(int colorOnStart,
                                int colorOnEnd,
                                int colorOffStart,
                                int colorOffEnd,
                                int colorSelectedStart,
                                int colorSelectedEnd,
                                float crad,
                                int strokeColor,
                                int strokeWidth) 
    {
        // top-left, top-right, bottom-right, bottom-left
        float[] radiiLeft = new float[] {
            crad, crad, 0, 0, 0, 0, crad, crad        
        };
        float[] radiiRight = new float[] {
            0, 0, crad, crad, crad, crad, 0, 0        
        };
        float[] radiiCenter = new float[] {
            0, 0, 0, 0, 0, 0, 0, 0        
        };
        
        GradientDrawable leftOn = buildGradientDrawable(colorOnStart, colorOnEnd, strokeWidth, strokeColor);
        leftOn.setCornerRadii(radiiLeft);
        GradientDrawable leftOff = buildGradientDrawable(colorOffStart, colorOffEnd, strokeWidth, strokeColor);
        leftOff.setCornerRadii(radiiLeft);
        GradientDrawable leftSelected = buildGradientDrawable(colorSelectedStart, colorSelectedEnd, strokeWidth, strokeColor);
        leftSelected.setCornerRadii(radiiLeft);
        
        GradientDrawable rightOn = buildGradientDrawable(colorOnStart, colorOnEnd, strokeWidth, strokeColor);
        rightOn.setCornerRadii(radiiRight);
        GradientDrawable rightOff = buildGradientDrawable(colorOffStart, colorOffEnd, strokeWidth, strokeColor);
        rightOff.setCornerRadii(radiiRight);
        GradientDrawable rightSelected = buildGradientDrawable(colorSelectedStart, colorSelectedEnd, strokeWidth, strokeColor);
        rightSelected.setCornerRadii(radiiRight);

        GradientDrawable centerOn = buildGradientDrawable(colorOnStart, colorOnEnd, strokeWidth, strokeColor);
        centerOn.setCornerRadii(radiiCenter);
        GradientDrawable centerOff = buildGradientDrawable(colorOffStart, colorOffEnd, strokeWidth, strokeColor);
        centerOff.setCornerRadii(radiiCenter);
        GradientDrawable centerSelected = buildGradientDrawable(colorSelectedStart, colorSelectedEnd, strokeWidth, strokeColor);
        centerSelected.setCornerRadii(radiiCenter);
        
        List<int[]> onStates = buildOnStates();
        List<int[]> offStates = buildOffStates();
        
        mBgLeftOn = new StateListDrawable();
        mBgRightOn = new StateListDrawable();
        mBgCenterOn = new StateListDrawable();
        mBgLeftOff = new StateListDrawable();
        mBgRightOff = new StateListDrawable();
        mBgCenterOff = new StateListDrawable();
        for (int[] it : onStates) {
            mBgLeftOn.addState(it, leftSelected);
            mBgRightOn.addState(it, rightSelected);
            mBgCenterOn.addState(it, centerSelected);
            mBgLeftOff.addState(it, leftSelected);
            mBgRightOff.addState(it, rightSelected);
            mBgCenterOff.addState(it, centerSelected);
        }
        for (int[] it : offStates) {
            mBgLeftOn.addState(it, leftOn);
            mBgRightOn.addState(it, rightOn);
            mBgCenterOn.addState(it, centerOn);
            mBgLeftOff.addState(it, leftOff);
            mBgRightOff.addState(it, rightOff);
            mBgCenterOff.addState(it, centerOff);
        }
    }

    private void buildDrawables() {
    	Drawable normalGD = (Drawable) mContext.getResources().getDrawable(mNormalDrawable);
    	Drawable pressedGD = (Drawable) mContext.getResources().getDrawable(mPressedDrawable);
    	Drawable selectedGD = (Drawable) mContext.getResources().getDrawable(mSelectedDrawable);

    	
    	List<int[]> onStates = buildOnStates();
        List<int[]> offStates = buildOffStates();
        
        mBgLeftOn = new StateListDrawable();
        mBgRightOn = new StateListDrawable();
        mBgCenterOn = new StateListDrawable();
        mBgLeftOff = new StateListDrawable();
        mBgRightOff = new StateListDrawable();
        mBgCenterOff = new StateListDrawable();
        
        for (int[] it : onStates) {
            mBgLeftOn.addState(it, selectedGD);
            mBgRightOn.addState(it, selectedGD);
            mBgCenterOn.addState(it, selectedGD);
            mBgLeftOff.addState(it, selectedGD);
            mBgRightOff.addState(it, selectedGD);
            mBgCenterOff.addState(it, selectedGD);
        }
        for (int[] it : offStates) {
            mBgLeftOn.addState(it, normalGD);
            mBgRightOn.addState(it, normalGD);
            mBgCenterOn.addState(it, normalGD);
            mBgLeftOff.addState(it, pressedGD);
            mBgRightOff.addState(it, pressedGD);
            mBgCenterOff.addState(it, pressedGD);
        }
    }
    
    private List<int[]> buildOnStates() {
        List<int[]> res = new ArrayList<int[]>();
        res.add(new int[] { 
            android.R.attr.state_focused, android.R.attr.state_enabled});
        res.add(new int[] { 
                android.R.attr.state_focused, android.R.attr.state_selected, android.R.attr.state_enabled});
        res.add(new int[] { 
                android.R.attr.state_pressed});
        return res;
    }
    
    private List<int[]> buildOffStates() {
        List<int[]> res = new ArrayList<int[]>();
        res.add(new int[] { 
            android.R.attr.state_enabled});
        res.add(new int[] { 
                android.R.attr.state_selected, android.R.attr.state_enabled});
        return res;
    }

    private GradientDrawable buildGradientDrawable(int colorStart, int colorEnd, int strokeWidth, int strokeColor) {
        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[] { colorStart, colorEnd });
        gd.setShape(GradientDrawable.RECTANGLE);
        gd.setStroke(strokeWidth, strokeColor);
        return gd;
    }
    
	private OnTouchListener mOnTouchListener = new OnTouchListener() {

		public boolean onTouch(View view, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				Button btnNext = (Button) view;
				int btnNextIndex = ((Integer) btnNext.getTag()).intValue();
				if (btnNextIndex == mSelectedButtonIndex) {
					return false;
				}
				handleStateChange(mSelectedButtonIndex, btnNextIndex);
				if (mOnTouchListenerExternal != null) {
					mOnTouchListenerExternal.onTouch(mSelectedButtonIndex);
				}
			}
			return false;
		}
	};
    
    private void handleStateChange(int btnLastIndex, int btnNextIndex) {
        int count = getChildCount();
        Button btnLast = (Button)getChildAt(btnLastIndex);
        Button btnNext = (Button)getChildAt(btnNextIndex);
        
        if (count < 3) {
            if (btnLastIndex == 0) {
                btnLast.setBackgroundDrawable(mBgLeftOn);
            } else {
                btnLast.setBackgroundDrawable(mBgRightOn);
            }
            if (btnNextIndex == 0) {
                btnNext.setBackgroundDrawable(mBgLeftOff);
            } else {
                btnNext.setBackgroundDrawable(mBgRightOff);
            }
        } else {
            if (btnLastIndex == 0) {
                btnLast.setBackgroundDrawable(mBgLeftOn);
               
            } else if (btnLastIndex == count-1) {
                btnLast.setBackgroundDrawable(mBgRightOn);
            } else {
                btnLast.setBackgroundDrawable(mBgCenterOn);
            }
            if (btnNextIndex == 0) {
                btnNext.setBackgroundDrawable(mBgLeftOff);
            } else if (btnNextIndex == count-1) {
                btnNext.setBackgroundDrawable(mBgRightOff);
            } else {
                btnNext.setBackgroundDrawable(mBgCenterOff);
            }
        }
        
        btnLast.setPadding(0, mBtnPaddingTop, 0, mBtnPaddingBottom);
        btnNext.setPadding(0, mBtnPaddingTop, 0, mBtnPaddingBottom);
        
        btnNext.setTextColor(mTextSelectedColor);
       
        if(btnLast != btnNext) {
        	 btnLast.setTextColor(mTextUnSelectedColor);
        }
        
        mSelectedButtonIndex = btnNextIndex;
    }
    
    public int getSelectedButtonIndex() {
        return mSelectedButtonIndex;
    }
    
    public void setPushedButtonIndex(int index) {
        handleStateChange(mSelectedButtonIndex, index);
    }
    
	public void setOnTouchListener(OnTouchListenerSegmentedButton listener) {
		mOnTouchListenerExternal = listener;
	}

	public interface OnTouchListenerSegmentedButton {
		public void onTouch(int index);
	}
}