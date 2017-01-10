/**
 * Copyright (c) 2005, Kuwo, Inc. All rights reserved. 
 */
package cn.kuwo.sing.tv.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.Toast;

/**
 * @Package cn.kuwo.sing.tv.view.widget
 *
 * @Date 2013-8-12, 下午6:20:17
 *
 * @Author wangming
 *
 */
public class KwFrameLayout extends FrameLayout {

	public KwFrameLayout(Context context) {
		super(context);
	}

	public KwFrameLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public KwFrameLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	public boolean onKeyLongPress(int keyCode, KeyEvent event) {
		Toast.makeText(getContext(), "onKeyLongPress="+keyCode, 0).show();
		return super.onKeyLongPress(keyCode, event);
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		Toast.makeText(getContext(), "onInterceptTouchEvent="+ev.getAction(), 0).show();
		return super.onInterceptTouchEvent(ev);
	}
}
