/**
 * Copyright (c) 2005, Kuwo, Inc. All rights reserved. 
 */
package cn.kuwo.sing.tv.utils;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import cn.kuwo.sing.tv.R;

/**
 * @Package cn.kuwo.sing.tv.utils
 * 
 * @Date 2013-5-30, 下午2:09:41
 * 
 * @Author wangming
 * 
 */
public class WaitingProgressBar extends LinearLayout {

	public WaitingProgressBar(Context context) {
		super(context);
		init(context);
	}

	public WaitingProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public WaitingProgressBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		View view = View.inflate(context, R.layout.waiting_progress_bar_layout, null);
		addView(view);
		ImageView ivWaitingPBview = (ImageView) this.findViewById(R.id.ivWaitingPB);
		AnimationDrawable animationDrawable = (AnimationDrawable) ivWaitingPBview.getDrawable();
		animationDrawable.start();
	}

}
