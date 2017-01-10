/**
 * Copyright (c) 2013, FightingTime, Inc. All rights reserved. 
 */
package cn.kuwo.sing.tv.utils;

import com.umeng.analytics.MobclickAgent;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.kuwo.framework.context.AppContext;
import cn.kuwo.sing.tv.R;
import cn.kuwo.sing.tv.context.Constants;

/**
 * @Package cn.kuwo.sing.tv.utils
 * 
 * @Date Dec 2, 2013 5:29:31 PM
 *
 * @Author wangming
 *
 */
public class UserFeedbackDialog extends Dialog implements OnClickListener {
	private Context mContext;

	public UserFeedbackDialog(Context context) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
		mContext = context;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			dismiss();
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_user_feedback);
		setCanceledOnTouchOutside(false);
		setCancelable(true);
	}

	@Override
	public void onClick(View v) {
		
	}
}
