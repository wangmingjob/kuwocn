package cn.kuwo.sing.tv.view.activity;

import java.util.ArrayList;

import com.umeng.analytics.MobclickAgent;

import cn.kuwo.framework.log.KuwoLog;
import cn.kuwo.sing.tv.context.MainApplication;
import cn.kuwo.sing.tv.context.Constants;
import cn.kuwo.sing.tv.controller.BaseController;
import cn.kuwo.sing.tv.utils.DialogUtils;
import cn.kuwo.sing.tv.utils.ExitDialog;
import de.greenrobot.event.EventBus;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

public class BaseActivity extends Activity {
	private static final String LOG_TAG = "BaseActivity";
	private EventBus mEventBus;

	private ArrayList<BaseController> mControllers;

	public BaseActivity() {
		mControllers = new ArrayList<BaseController>();
	}
	
	public void loadController(BaseController ctrl) {
		mControllers.add(ctrl);
	}

	public void unloadController(BaseController ctrl) {
		mControllers.remove(ctrl);
	}

	/*
	 * 在这里加载Controller
	 */
	protected void onLoadController() {
		
	}
	
	protected void afterSetContentView() {
		onLoadController();
	}
	
    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        afterSetContentView();
    }

    @Override
    public void setContentView(View view, LayoutParams params) {
        super.setContentView(view, params);
        afterSetContentView();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        afterSetContentView();
    }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		Log.d("BaseActivity", "onCreate");
		mEventBus = EventBus.getDefault();
		mEventBus.register(this);
		for (BaseController observer : mControllers) {
			observer.onCreate(savedInstanceState);
		}
	}
	
	@Override
	protected void onPause() {
		for (BaseController observer : mControllers) {
			observer.onPause();
		}
		MobclickAgent.onPause(this);
		super.onPause();
	}
	
	@Override
	protected void onStart() {
		for (BaseController observer : mControllers) {
			observer.onStart();
		}
		super.onStart();
		
		MainApplication app = (MainApplication)getApplication(); 
		app.init();
	}
	
	@Override
	protected void onRestart() {
		for (BaseController observer : mControllers) {
			observer.onRestart();
		}
		super.onRestart();
	}
	
	@Override
	protected void onResume() {
		for (BaseController observer : mControllers) {
			observer.onResume();
		}
		MobclickAgent.onResume(this);
		super.onResume();
	}
	
	@Override
	protected void onStop() {
		for (BaseController observer : mControllers) {
			observer.onStop();
		}
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		mEventBus.unregister(this);
		for (BaseController observer : mControllers) {
			observer.onDestroy();
		}
		super.onDestroy();
	}
	
	@Override
	public void onBackPressed() {
		for (BaseController observer : mControllers) {
			observer.onBackPressed();
		}
		super.onBackPressed();
	}
	
	@Override
	public boolean onKeyLongPress(int keyCode, KeyEvent event) {
		boolean result = false;
		switch (keyCode) {
		case KeyEvent.KEYCODE_ENTER:
			DialogUtils.toast("长按OK键,将打开语音控制", false);
			//TODO: show the voice controller dialog
			result = true;
			break;
		default:
			break;
		}
		return result;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean result = false;
		for (BaseController observer : mControllers) {
			if (observer.onKeyDown(keyCode, event))
				result = true;
		}
		return result;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		for (BaseController observer : mControllers) {
			if (observer.onTouchEvent(event))
				return true;
		}
		return super.onTouchEvent(event);
	}
	
	protected void onEvent(Message event) {
	   if(event.equals("cn.kuwo.sing.tv.exit")) {
		   KuwoLog.d(getClass().getSimpleName(), "EventBus onEvent('cn.kuow.sing.tv.exit')");
		   this.finish();
	   }
	}
}
