package com.godyhm.test.bendtriangle;

import android.app.Activity;
import android.os.Bundle;

public class TriangleActivity extends Activity {
	private TriangleView m_view = null;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main);
        m_view = new TriangleView(this);
        setContentView(m_view);
    }
}