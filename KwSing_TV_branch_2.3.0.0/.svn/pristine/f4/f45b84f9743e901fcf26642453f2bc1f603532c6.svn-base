/**
 * Copyright (c) 2005, Kuwo, Inc. All rights reserved. 
 */
package cn.kuwo.sing.tv.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;

/**
 * @Package cn.kuwo.sing.tv.utils
 *
 * @Date 2013-4-23, 下午4:12:59, 2013
 *
 * @Author wangming
 * 
 * @Description 音量控制SeekBar[垂直方向]
 *
 */
public class VolumeSeekBar extends SeekBar {
	
	public VolumeSeekBar(Context context) {
		super(context);
	}
	
	public VolumeSeekBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public VolumeSeekBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(h, w, oldh, oldw);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec);
        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
    }

    protected void onDraw(Canvas c) {
        c.rotate(-90);
        c.translate(-getHeight(),0);
        super.onDraw(c);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
            	int i=0;
            	i=getMax() - (int) (getMax() * event.getY() / getHeight());
                setProgress(i);
                Log.i("Progress",getProgress()+"");
                onSizeChanged(getWidth(), getHeight(), 0, 0);
                break;

            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return true;
    }
    
    private View mLeftFocusView;
    public void setLeftFocus(View view) {
    	mLeftFocusView = view;
    }
    
    private View mRightFoucusView;
    public void setRightFocus(View view) {
    	mRightFoucusView = view;
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_LEFT:
			mLeftFocusView.requestFocus();
			return true;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			mRightFoucusView.requestFocus();
			return true;
		case KeyEvent.KEYCODE_DPAD_UP:
			keyCode = KeyEvent.KEYCODE_DPAD_RIGHT;
			break;
		case KeyEvent.KEYCODE_DPAD_DOWN:
			keyCode = KeyEvent.KEYCODE_DPAD_LEFT;
			break;
		default:
			break;
		}
    	return super.onKeyDown(keyCode, event);
    }
}
