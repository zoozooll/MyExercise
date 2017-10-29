
    package com.company.hetuo;

    import android.app.Activity;
    import android.os.Bundle;
    import com.phonegap.*;

    public class DefaultActivity extends DroidGap
    {
        @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
			super.setIntegerProperty( "splashscreen", R.drawable.splash );
            super.loadUrl("file:///android_asset/www/index.html", 1000);
        }
    }
    
