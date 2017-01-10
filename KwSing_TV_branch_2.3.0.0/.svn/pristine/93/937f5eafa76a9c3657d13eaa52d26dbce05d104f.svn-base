/**
 * Copyright (c) 2005, Kuwo, Inc. All rights reserved. 
 */
package cn.kuwo.sing.tv.utils;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;

/**
 * @Package cn.kuwo.sing.tv.utils
 *
 * @Date 2013-3-29, 上午11:20:05, 2013
 *
 * @Author wangming
 *
 */
public class CommonAnimation {

	/**
	 * 	move the background view(translate animation).
	 * 
	 * @param view
	 * 			the view will be moved
	 * @param durationMillis
	 * 			translate animation duration
	 * @param fromX
	 * 			from X coordinate
	 * @param toX
	 * 			to X coordinate
	 * @param fromY
	 * 			from Y coordinate
	 * @param toY
	 * 			to Y coordinate
	 */
	public static void translate(View view, long durationMillis, boolean fillAfter, float fromX, float toX, float fromY, float toY) {
		TranslateAnimation ta = new TranslateAnimation(fromX, toX, fromY, toY);
		ta.setDuration(durationMillis);
		ta.setFillAfter(fillAfter);//this animation performed will persist when it is finished
		view.startAnimation(ta);
	}
	
	/**
	 * 	move the background view(translate animation).
	 * 
	 * @param view
	 * 			the view will be moved
	 * @param durationMillis
	 * 			translate animation duration
	 * @param fromX
	 * 			from X coordinate
	 * @param toX
	 * 			to X coordinate
	 * @param fromY
	 * 			from Y coordinate
	 * @param toY
	 * 			to Y coordinate
	 */
	public static void translateFromAbove(final Context context, final View view, final long durationMillis, boolean fillAfter, float fromX, float toX, final float fromY, final float toY) {
		TranslateAnimation translateAnimation = new TranslateAnimation(fromX, toX, fromY, toY+5);
		translateAnimation.setInterpolator(new BounceInterpolator());
		translateAnimation.setDuration(durationMillis);
		translateAnimation.setFillAfter(fillAfter);//this animation performed will persist when it is finished
		view.startAnimation(translateAnimation);
	}
	
	/**
	 * 	move the background view(translate animation).
	 * 
	 * @param view
	 * 			the view will be moved
	 * @param durationMillis
	 * 			translate animation duration
	 * @param fromX
	 * 			from X coordinate
	 * @param toX
	 * 			to X coordinate
	 * @param fromY
	 * 			from Y coordinate
	 * @param toY
	 * 			to Y coordinate
	 */
	public static void translateFromBelow(final Context context, final View view, final long durationMillis, boolean fillAfter, float fromX, float toX, final float fromY, final float toY) {
		TranslateAnimation translateAnimation = new TranslateAnimation(fromX, toX, fromY, toY+5);
//		TranslateAnimation translateAnimation = new TranslateAnimation(fromX, toX, fromY, toY-21);
		translateAnimation.setDuration(durationMillis);
		translateAnimation.setFillAfter(fillAfter);//this animation performed will persist when it is finished
		view.startAnimation(translateAnimation);
		translateAnimation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
//				TranslateAnimation shakeAnimation = new TranslateAnimation(0, 0, toY-21, toY+5);
//				shakeAnimation.setInterpolator(new BounceInterpolator());
//				shakeAnimation.setDuration(durationMillis); //500
//				shakeAnimation.setFillAfter(true);
//				view.startAnimation(shakeAnimation);
			}
		});
	}
	
	
}
