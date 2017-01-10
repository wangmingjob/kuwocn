/**
 * Copyright (c) 2013, FightingTime, Inc. All rights reserved. 
 */
package cn.kuwo.sing.tv.utils;

import java.util.ArrayList;
import java.util.List;

import com.umeng.analytics.MobclickAgent;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import cn.kuwo.framework.context.AppContext;
import cn.kuwo.sing.tv.R;
import cn.kuwo.sing.tv.context.Constants;
import cn.kuwo.sing.tv.view.widget.CirclePageIndicator;

/**
 * @Package cn.kuwo.sing.tv.utils
 * 
 * @Date Dec 2, 2013 5:29:31 PM
 *
 * @Author wangming
 *
 */
public class MicrophoneHelpDialog extends Dialog implements OnClickListener {
	private Context mContext;
	private ViewPager vpMicrophoneHelp;
	private CirclePageIndicator indicatorMicrophoneHelp;
	private List<View> mHelpViewList = new ArrayList<View>();
	private Bitmap mBitmapGuide1;
	private Bitmap mBitmapGuide2;
	private int bitmapHeight;
	private int bitmapWidth;

	public MicrophoneHelpDialog(Context context) {
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
		setContentView(R.layout.dialog_microphone_help);
		setCanceledOnTouchOutside(false);
		setCancelable(true);
		
		View view1 = View.inflate(mContext, R.layout.microphone_help_guide_1, null);
		View view2 = View.inflate(mContext, R.layout.microphone_help_guide_3, null);
		bitmapWidth = DensityUtils.dip2px(mContext, 1084);
		bitmapHeight = DensityUtils.dip2px(mContext, 548);
		mHelpViewList.add(view1); 
		mHelpViewList.add(view2);
		
		vpMicrophoneHelp = (ViewPager) findViewById(R.id.vpMicrophoneHelp);
		vpMicrophoneHelp.setAdapter(new HelpPagerAdapter(mHelpViewList));
		vpMicrophoneHelp.setCurrentItem(0, true);
		vpMicrophoneHelp.setOffscreenPageLimit(1);
		indicatorMicrophoneHelp = (CirclePageIndicator) findViewById(R.id.indicatorMicrophoneHelp);
		indicatorMicrophoneHelp.setViewPager(vpMicrophoneHelp);
		indicatorMicrophoneHelp.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				switch (position) {
				case 0:
					recycleGuide2Bitmap();
					createGuide1Bitmap();
					break;
				case 1:
					recycleGuide1Bitmap();
					createGuide2Bitmap();
					break;
				default:
					break;
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
		
		Button btMicphoneHelpYes = (Button)findViewById(R.id.btMicphoneHelpYes);
		btMicphoneHelpYes.setOnClickListener(mOnClickListener);
		Button btMicphoneHelpNo = (Button) findViewById(R.id.btMicphoneHelpNo);
		btMicphoneHelpNo.setOnClickListener(mOnClickListener);
		
		setOnDismissListener(new OnDismissListener() { 
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				recycleGuide1Bitmap();
				recycleGuide2Bitmap();
				System.gc();
			}
		});
	}
	
	private void createGuide1Bitmap() {
		ImageView ivGuide = (ImageView) mHelpViewList.get(0).findViewById(R.id.ivGuide1);
		mBitmapGuide1 = BitmapTools.createBitmapByInputstream(mContext, R.drawable.micphone_help_guide_1, bitmapWidth, bitmapHeight);
		ivGuide.setImageBitmap(mBitmapGuide1);
	}
	
	private void createGuide2Bitmap() {
		ImageView ivGuide = (ImageView) mHelpViewList.get(1).findViewById(R.id.ivGuide3);
		mBitmapGuide2 = BitmapTools.createBitmapByInputstream(mContext, R.drawable.micphone_help_guide_3, bitmapWidth, bitmapHeight);
		ivGuide.setImageBitmap(mBitmapGuide2);
	}
	
	private View.OnClickListener mOnClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			DialogUtils.toast("谢谢你的反馈！", false);
			switch (v.getId()) {
			case R.id.btMicphoneHelpYes:
				MobclickAgent.onEvent(mContext, Constants.KS_UMENG_MIC_HELP_RESULT, Constants.KS_UMENG_SUCCESS);
				dismiss();
				break;
			case R.id.btMicphoneHelpNo:
				MobclickAgent.onEvent(mContext, Constants.KS_UMENG_MIC_HELP_RESULT, Constants.KS_UMENG_FAIL);
				dismiss();
				break;
			default:
				break;
			}
		}
	};
	
	private void recycleGuide1Bitmap() {
		if(mBitmapGuide1 != null && !mBitmapGuide1.isRecycled()) {
			mBitmapGuide1.recycle();
			mBitmapGuide1 = null;
		}
	}
	private void recycleGuide2Bitmap() {
		if(mBitmapGuide2 != null && !mBitmapGuide2.isRecycled()) {
			mBitmapGuide2.recycle();
			mBitmapGuide2 = null;
		}
	}
	
	private void recycleBitmap(int position) {
		switch (position) {
		case 0:
			recycleGuide1Bitmap();
			break;
		case 1:
			recycleGuide2Bitmap();
			break;
		default:
			break;
		}
		System.gc();
	}
	
	private class HelpPagerAdapter extends PagerAdapter {
		private List<View> mViewList;
		
		public HelpPagerAdapter(List<View> viewList) {
			mViewList = viewList;
		}

		@Override
		public int getCount() {
			return mViewList.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object obj) {
			return view == obj;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			recycleBitmap(position);
			container.removeView(mViewList.get(position));
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View child = mViewList.get(position);
			ImageView ivGuide = null;
			switch (position) {
			case 0:
				ivGuide = (ImageView) child.findViewById(R.id.ivGuide1);
				mBitmapGuide1 = BitmapTools.createBitmapByInputstream(mContext, R.drawable.micphone_help_guide_1, bitmapWidth, bitmapHeight);
				ivGuide.setImageBitmap(mBitmapGuide1);
				break;
			case 1:
				ivGuide = (ImageView) child.findViewById(R.id.ivGuide3);
				mBitmapGuide2 = BitmapTools.createBitmapByInputstream(mContext, R.drawable.micphone_help_guide_3, bitmapWidth, bitmapHeight);
				ivGuide.setImageBitmap(mBitmapGuide2);
				break;
			default:
				break;
			}
			container.addView(child, 0);
			return mViewList.get(position);
		}
	}

	@Override
	public void onClick(View v) {
		
	}

}
