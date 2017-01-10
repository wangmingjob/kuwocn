/**
 * Copyright (c) 2005, Kuwo, Inc. All rights reserved. 
 */
package cn.kuwo.sing.phone4tv.view.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import cn.kuwo.sing.phone4tv.R;
import cn.kuwo.sing.phone4tv.commons.file.PreferencesManager;

/**
 * @Package cn.kuwo.sing.phone4tv.view.activity
 *
 * @Date 2014-3-4, 下午3:25:59
 *
 * @Author wangming
 *
 */
public class SplashActivity extends Activity {
	private static final String LOG_TAG = "SplashActivity";
	private static final int DURATION_SPLASH = 1000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//无标题和全屏必须在setContentView之前
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		final View view = View.inflate(this, R.layout.activity_splash, null);
		setContentView(view);
		
		checkUpdate();
		
		//跳转
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				redirectTo();
			}
		}, DURATION_SPLASH);
	}
	
	private void checkUpdate() {
		
	}

	private void redirectTo() {
		Intent intent = null;
//		boolean isFirstRunning = PreferencesManager.getBoolean("isFirstRunning", true);
//		if(isFirstRunning) 
//			intent = new Intent(this, UserGuideActivity.class);
//		else
			intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();
	}
}