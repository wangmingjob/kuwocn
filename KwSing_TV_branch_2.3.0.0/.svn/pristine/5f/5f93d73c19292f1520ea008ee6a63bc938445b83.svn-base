package cn.kuwo.sing.tv.controller;

import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;

public abstract class BaseController {

	protected Activity activity;
	
	public BaseController(Activity activity) {
        this.activity = activity;
	}

	public void onCreate(Bundle savedInstanceState) {
		
	}
	
	public void onPause() {
		 MobclickAgent.onPause(activity);
	}

	public void onStart() {
		
	}

	public void onResume() {
		MobclickAgent.onResume(activity);
	}

	public void onStop() {
		
	}

	public void onRestart() {
		
	}

	public void onDestroy() {

	}

	public void onBackPressed() {
		
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return false;
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		return false;
	}
	

	// TODO
	public void onConfigurationChanged(Configuration newConfig) {
		
	}

}
