package cn.kuwo.sing.tv.utils;

import cn.kuwo.sing.tv.R;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class WaitingDialog extends Dialog {
	// private static final String TAG = "WaitingDialog";
	private AnimationDrawable rocketAnimation;
	private ImageView rocketImage;
	private boolean isViewVisible = false;
	private MyHander outHander;
	private Context mContext;

	private String text = "";

	public WaitingDialog(Context context) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
		mContext = context;
	}

	@Override
	/*public void onBackPressed() {
		dismiss();
		//((Activity) mContext).finish();
	}*/
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((event.getKeyCode() == KeyEvent.KEYCODE_MENU) 
				|| (keyCode == KeyEvent.KEYCODE_BACK)
				|| (keyCode == KeyEvent.KEYCODE_DPAD_CENTER)
				|| (keyCode == KeyEvent.KEYCODE_ENTER))
			return ((Activity) mContext).onKeyDown(keyCode, event);
		
		return true;
	}


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_waiting);
		
		((TextView) findViewById(R.id.waittext)).setText(text);
		rocketImage = (ImageView) findViewById(R.id.FrameAnimationImage);
		
		// rocketImage.setBackgroundResource(R.anim.waiting_animation);
		rocketAnimation = (AnimationDrawable) rocketImage.getBackground();
		
		outHander = new MyHander();
	}

	protected void onStart() {
		super.onStart();
		isViewVisible = false;
		new MyThread(outHander).start();
		// Log.d(TAG, "WaitingDialog is show ");
	}

	protected void onStop() {
		super.onStop();
		rocketAnimation.stop();
		// Log.d(TAG, "WaitingDialog is dismiss ");
	}

	private class MyThread extends Thread {
		private Handler mHandler;

		public MyThread(Handler handler) {
			mHandler = handler;
		}

		public void run() {
			while (isViewVisible == false) {
				if (rocketImage.getVisibility() == View.VISIBLE) {
					mHandler.sendEmptyMessage(0);
					isViewVisible = true;
				}
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		}
	}

	private class MyHander extends Handler {
		public void handleMessage(Message msg) {
			// adapter.notifyDataSetChanged();
			// Log.i(TAG, "myHander update");
			rocketAnimation.start();
			// boolean isAnimationRuning =
			rocketAnimation.isRunning();
			// Log.d(TAG, "isAnimationRuning " + isAnimationRuning);
			rocketAnimation.invalidateSelf();
		}
	}

}
