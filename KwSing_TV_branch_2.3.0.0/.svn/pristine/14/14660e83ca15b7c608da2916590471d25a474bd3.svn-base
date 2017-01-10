/**
 * Copyright (c) 2005, Kuwo, Inc. All rights reserved. 
 */
package cn.kuwo.sing.tv.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import cn.kuwo.framework.config.PreferencesManager;
import cn.kuwo.framework.context.AppContext;
import cn.kuwo.framework.log.KuwoLog;
import cn.kuwo.sing.tv.R;
import cn.kuwo.sing.tv.context.Constants;
import de.greenrobot.event.EventBus;

/**
 * @Package cn.kuwo.sing.tv.utils
 *
 * @Date 2013-4-18, 下午5:52:45, 2013
 *
 * @Author wangming
 *
 */
public class ExitDialog extends Dialog implements android.view.View.OnClickListener {
	private static final String LOG_TAG = "ExitDialog";
	private Context mContext;
	private Bitmap mBackgroundBitmap;
	
	public ExitDialog(Context context) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
		mContext = context;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exit_dialog_layout);
		setCanceledOnTouchOutside(false);
		setCancelable(false);
		this.getWindow().findViewById(R.id.btExitDialogOk).setOnClickListener(this);
		this.getWindow().findViewById(R.id.btExitDialogCancel).setOnClickListener(this);
		ImageView ivExitDialogBackground = (ImageView) findViewById(R.id.ivExitDialogBackground);
		mBackgroundBitmap = BitmapTools.createBitmapByInputstream(mContext, R.drawable.exit_dialog_bg, AppContext.SCREEN_WIDTH, AppContext.SCREEN_HIGHT);
		ivExitDialogBackground.setImageBitmap(mBackgroundBitmap);
		
		setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				if( mBackgroundBitmap != null && !mBackgroundBitmap.isRecycled()) {
					mBackgroundBitmap.recycle();
					mBackgroundBitmap = null;
					System.gc();
				}
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		try {
			switch (v.getId()) {
			case R.id.btExitDialogOk:
				dismiss();
				Message msg = new Message();
				msg.what = Constants.MSG_APP_EXIT;
				EventBus.getDefault().post(msg);
				PreferencesManager.put("isAppRunning", false).commit();
				android.os.Process.killProcess(android.os.Process.myPid());
				break;
			case R.id.btExitDialogCancel:
				dismiss();
				break;
			default:
				break;
			}
		} catch (Exception e) {
			KuwoLog.e(LOG_TAG, e);
		}
		
		
	}
}
