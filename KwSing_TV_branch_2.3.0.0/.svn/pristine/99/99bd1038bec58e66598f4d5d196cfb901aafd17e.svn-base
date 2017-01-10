/**
 * Copyright (c) 2005, Kuwo, Inc. All rights reserved. 
 */
package cn.kuwo.sing.tv.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import cn.kuwo.framework.context.AppContext;
import cn.kuwo.sing.tv.R;
import cn.kuwo.sing.tv.bean.ImageObject;
import cn.kuwo.sing.tv.context.Constants;
import cn.kuwo.sing.tv.utils.BitmapTools;
import cn.kuwo.sing.tv.view.fragment.DetailFragment;
import cn.kuwo.sing.tv.view.fragment.SecondItemListFragment;
import cn.kuwo.sing.tv.view.fragment.SingerFragment;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;

/**
 * @Package cn.kuwo.sing.tv.view.activity
 *
 * @Date 2013-3-27, 上午11:45:38, 2013
 *
 * @Author wangming
 *
 */
public class SecondItemListActivity extends BaseFragmentActivity {
	private static final String LOG_TAG = "SecondItemListActivity";
	
	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;
	private ImageObject mParentImageObject;
	private Intent mIntent;
	public static int sSingerCount = 0;
	private Bitmap mBackgroundBitmap;
	private boolean mNeedSearch;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item_list_second_activity);
		
		mIntent = getIntent();
		mParentImageObject = (ImageObject) mIntent.getSerializableExtra("parentImageObject");
		FrameLayout container = (FrameLayout) findViewById(R.id.item_detail_container_second);
		if (container != null) {
			mTwoPane = true;
			
			if (Constants.isUseBackground)
			{
				mBackgroundBitmap = BitmapTools.createBitmapByInputstream(this, R.drawable.item_list_right_bg_second, (int)(AppContext.SCREEN_WIDTH * (1600 *1.0f/1920)), AppContext.SCREEN_HIGHT);
				container.setBackgroundDrawable(new BitmapDrawable(mBackgroundBitmap));				
			}else {
				container.setBackgroundColor(this.getResources().getColor(R.color.item_list_right_color));
			}
			
			Fragment fragment = null;
			String flag = getIntent().getStringExtra("flag");
			if(!TextUtils.isEmpty(flag)) {
				if(flag.equals("fromSingerCategory")) {
					fragment = new SingerFragment();
				}else if(flag.equals("fromMtvCategory") ) {
					fragment = new DetailFragment();
					((DetailFragment) fragment).setFromPage(Constants.FROM_HOTBAR);
				}else if(flag.equals("fromMtvCategoryOrder")) {
					fragment = new DetailFragment();
					((DetailFragment) fragment).setFromPage(Constants.FROM_CATEGORY);
				}else if (flag.equals("fromSinger"))	{
					fragment = new DetailFragment();
					((DetailFragment) fragment).setFromPage(Constants.FROM_SINGER);
				}
			}
			getSupportFragmentManager().beginTransaction().replace(R.id.item_detail_container_second, fragment).commit();
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
	
	@Override
	protected void onDestroy() {
		if (Constants.isUseBackground)
		{
			if(mBackgroundBitmap != null && !mBackgroundBitmap.isRecycled()) {
				mBackgroundBitmap.recycle();
				mBackgroundBitmap = null;
				System.gc();
			}			
		}

		super.onDestroy();
	}
	
	private void sendMessage(int what) {
		Message msg = new Message();
		msg.what = what;
		mEventBus.post(msg);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		View view = this.getCurrentFocus();
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			showForwardPage();
			break;
		case KeyEvent.KEYCODE_DPAD_UP:
			if(view.getId() == R.id.gvSinger || view.getId() == R.id.gvSingerByKeyword || view.getId() == R.id.lvFragmentDetail)
				sendMessage(Constants.MSG_SECOND_PAGE_ON_KEYDOWN_PRE_PAGE);
			break;
		case KeyEvent.KEYCODE_DPAD_DOWN:
			if(view.getId() == R.id.gvSinger || view.getId() == R.id.gvSingerByKeyword || view.getId() == R.id.lvFragmentDetail)
				sendMessage(Constants.MSG_SECOND_PAGE_ON_KEYDOWN_NEXT_PAGE);
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	public void showForwardPage() {
		Intent backIntent = null;
		if(!mIntent.getStringExtra("flag").equals("fromSinger")) {
			backIntent = new Intent(SecondItemListActivity.this, ItemListActivity.class);
			startActivity(backIntent);
		}else {
			Message msg = new Message();
			msg.what = Constants.MSG_SINGER_COUNT;
			msg.arg1 = sSingerCount;
			EventBus.getDefault().post(msg);
		}
		finish();
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		ImageLoader.getInstance().clearMemoryCache();
	}
	
	@Override
	protected void onEvent(Message msg) {
		if(msg.what == Constants.MSG_CLOSE_SECOND_PAGE)
			finish();
//		super.onEvent(msg);
	}
}
