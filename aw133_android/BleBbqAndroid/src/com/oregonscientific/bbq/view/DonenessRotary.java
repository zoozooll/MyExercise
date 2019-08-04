/**
 * 
 */
package com.oregonscientific.bbq.view;

import com.oregonscientific.bbq.R;
import com.oregonscientific.bbq.bean.BBQDataSet.DonenessLevel;
import com.oregonscientific.bbq.bean.DonenessTemperature;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author aaronli
 *
 */
public class DonenessRotary extends RotaryBar {

	private int[] colors = new int[6];
	private OnDonenessChangedListener mListener;
	
	/**
	 * @param contextR
	 * @param attrs
	 * @param defStyle
	 */
	public DonenessRotary(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setColors();
		//setLongClickable(false);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public DonenessRotary(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	/**
	 * @param context
	 */
	public DonenessRotary(Context context) {
		this(context, null);
	}
	
	public void setColors() {
		
		Resources res = getResources();
		colors[0] = res.getColor(R.color.doneness_rare);
		colors[1] = res.getColor(R.color.doneness_mediumrare);
		colors[2] = res.getColor(R.color.doneness_medium);
		colors[3] = res.getColor(R.color.doneness_mediumwell);
		colors[4] = res.getColor(R.color.doneness_welldone);
		colors[5] = res.getColor(R.color.doneness_overdone);

	}
	
	public void initAngles(DonenessTemperature dt) {
		predonenessAngle = 0;
		Resources res = getResources();
		colors[0] = res.getColor(R.color.doneness_rare);
		colors[1] = res.getColor(R.color.doneness_mediumrare);
		colors[2] = res.getColor(R.color.doneness_medium);
		colors[3] = res.getColor(R.color.doneness_mediumwell);
		colors[4] = res.getColor(R.color.doneness_welldone);
		colors[5] = res.getColor(R.color.doneness_overdone);
		
		final float[] temFs = {dt.getRareTemperature(), dt.getMediumrareTemperature(), dt.getMediumTemperature(),
				dt.getMediumwellTemperature(), dt.getWelldoneTemperature()};
		for (int i = 0, size = temFs.length; i < size; i++) {
			if (temFs [i] > 0) {
				RotaryPair pair = new RotaryPair(); 
				if (predonenessAngle >= 0) {
					predonenessAngle = -temperatureFToAngle(temFs[i]);
					pair.angle = 0;
				} else {
					pair.angle = -25;
				}
				pair.level = DonenessLevel.get(i);
				pair.color = colors[i];
				dataArray.put(i, pair);
			} else {
				dataArray.delete(i);
			}
			
		}
		endColor = colors[5];
		
	}
	
	public void refreshAngles (DonenessTemperature dt) {
		initAngles(dt);
		invalidate();
	}
	
	/* (non-Javadoc)
	 * @see com.oregonscientific.bbq.view.RotaryBar#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mListener != null) {
			mListener.onDonenessTemperatureChanged(angleToTemperatureF(-predonenessAngle));
		}
		return super.onTouchEvent(event);
	}

	private float angleToTemperatureF(float angle) {
		return (angle - 20f) * (375f - 100f)/(340f - 20f) + 100f;
	}
	
	private float temperatureFToAngle(float temF) {
		return (temF - 100f) * (340f - 20f)/(375f - 100f) + 20f;
	}
	
	/**
	 * @param mListener the mListener to set
	 */
	public void setmListener(OnDonenessChangedListener mListener) {
		this.mListener = mListener;
	}

	public static interface OnDonenessChangedListener{
		
		public void onDonenessTemperatureChanged(float firstTemF);
	}
}
