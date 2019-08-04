package com.moonlight.smooth;

/*
 * Copyright (C) 2011 Patrik Akerfeldt
 * Copyright (C) 2011 Jake Wharton
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import android.view.View;

/**
 * A PageIndicator is responsible to show an visual indicator on the total views number and the
 * current visible view.
 */
public interface PageIndicator {
	/**
	 * <p>
	 * Set the current page of both the ViewPager and indicator.
	 * </p>
	 * <p>
	 * This <strong>must</strong> be used if you need to set the page before the views are drawn on
	 * screen (e.g., default start page).
	 * </p>
	 * @param item
	 */
	void setCurrentItem(int item);
	/**
	 * Set a indicator item change listener which will receive forwarded events.
	 * @param listener
	 */
	void setonIndicatorItemClickListener(IndicatorItemClickListener listener);

	/**
	 * A IndicatorItemClickListener is responsible to listen the indicator click event.
	 */
	interface IndicatorItemClickListener {
		void onClick(View v, int index, Object data);
	}
}
