/**
 * Copyright (c) 2005, Kuwo, Inc. All rights reserved. 
 */
package cn.kuwo.sing.tv.controller;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import cn.kuwo.framework.log.KuwoLog;
import cn.kuwo.sing.tv.R;
import cn.kuwo.sing.tv.bean.ImageObject;
import cn.kuwo.sing.tv.bean.PagedData;
import cn.kuwo.sing.tv.context.Constants;
import cn.kuwo.sing.tv.logic.ListLogic;
import cn.kuwo.sing.tv.logic.PagedDataHandler;
import cn.kuwo.sing.tv.view.activity.SecondItemListActivity;
import cn.kuwo.sing.tv.view.adapter.SingerAdapter;

import com.haarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.PauseOnScrollListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import de.greenrobot.event.EventBus;

/**
 * @Package cn.kuwo.sing.tv.controller
 *
 * @Date 2013-4-15, 下午12:28:57, 2013
 *
 * @Author wangming
 *
 */
public class SingerController extends BaseController {
	private static final String LOG_TAG = "SingerController";
	private GridView gvSinger;
	private int mTotalPageNum;
	private int mCurrentPageNum = 1;
	private List<ImageObject> mImageObjectList;
	private ImageObject mCurrentImageObject;
	private ListLogic mListLogic;
	private TextView tvSingerPagePrompt;
	private ProgressBar progressBar;
	private LinearLayout llSingerRequestFail;
	private Button btSingerRequestFail;
	private EventBus mEventBus;
	private SingerAdapter mAdapter;
	private int mLastPosition = 0;
	private boolean mSingerListBottom = false;
	private ImageView ivSingerPrePagePrompt;
	private ImageView ivSingerNextPagePrompt;
	
	private GridView gvSingerByKeyword;
	private int mTotalPageNum4Keyword;
	private int mCurrentPageNum4Keyword = 1;
	private List<ImageObject> mImageObjectList4Keyword;
	private SingerAdapter mAdapter4Keyword;
	private int mLastPosition4Keyword = 0;
	private boolean mSingerListBottom4Keyword = false;
	
	private boolean mSearchViewVisible = false;
	private String mSearchKeyword;

	/**
	 * @param activity
	 */
	public SingerController(Activity activity) {
		super(activity);
		initData();
		initView();
		processLogic();
	}
	
	public void onEvent(Message msg) {
		switch (msg.what) {
		case Constants.MSG_SECOND_PAGE_ON_KEYDOWN_PRE_PAGE:
			loadPrePage();
			break;
		case Constants.MSG_SECOND_PAGE_ON_KEYDOWN_NEXT_PAGE:
			loadNextPage();
			break;
		case Constants.MSG_SINGER_SEARCH_KEYWORD:
			mSearchKeyword = (String)msg.obj;
			requestSingerList(mSearchKeyword);
			break;
		case Constants.MSG_SINGER_SEARCH_CLEAR:
			turnBack();
			break;
		default:
			break;
		}
	}

	private void loadNextPage() {
		if(activity.getCurrentFocus() == gvSinger) {
			mSingerListBottom = true;
			if(mCurrentPageNum < mTotalPageNum) {
				ivSingerNextPagePrompt.setImageResource(R.drawable.iv_next_page_pressed);
				loadSingerList(mCurrentPageNum+1);
			}
		}
		if(activity.getCurrentFocus() == gvSingerByKeyword) {
			mSingerListBottom4Keyword = true;
			if(mCurrentPageNum4Keyword < mTotalPageNum4Keyword) {
				ivSingerNextPagePrompt.setImageResource(R.drawable.iv_next_page_pressed);
				loadSingerListBySearch(mSearchKeyword, mCurrentPageNum4Keyword+1);
			}
		}
	}

	private void loadPrePage() {
		if(activity.getCurrentFocus() == gvSinger) {
			mSingerListBottom = false;
			if(gvSinger.getVisibility() == View.VISIBLE && mCurrentPageNum > 1) {
				ivSingerPrePagePrompt.setImageResource(R.drawable.iv_pre_page_pressed);
				loadSingerList(mCurrentPageNum-1);
			}
		}
		if(activity.getCurrentFocus() == gvSingerByKeyword) {
			mSingerListBottom4Keyword = false;
			if(gvSingerByKeyword.getVisibility() == View.VISIBLE && mCurrentPageNum4Keyword > 1) {
				ivSingerPrePagePrompt.setImageResource(R.drawable.iv_pre_page_pressed);
				loadSingerListBySearch(mSearchKeyword, mCurrentPageNum4Keyword-1);
			}
		}
	}
	
	private Interpolator accelerator = new AccelerateInterpolator();
	private Interpolator decelerator = new DecelerateInterpolator();
	private TextView tvSingerSearchEmptyPromptByKeyword;
	private FrameLayout flSinger;
	private FrameLayout flSingerByKeyword;
	private ProgressBar progressBar_singer_byKeyword;
	private LinearLayout llSingerRequestFailByKeyword;
	private Button btSingerRequestFailByKeyword;
	
	private void flipAnimation(final View fromView, final View toView) {
		ObjectAnimator visToInvis = ObjectAnimator.ofFloat(fromView, "rotationY", 0f, 90f);
		visToInvis.setDuration(500);
		visToInvis.setInterpolator(accelerator);
		final ObjectAnimator invisToVis = ObjectAnimator.ofFloat(toView, "rotationY", -90f, 0f);
		invisToVis.setDuration(500);
		invisToVis.setInterpolator(decelerator);
		
		visToInvis.addListener(new AnimatorListenerAdapter() {

			@Override
			public void onAnimationEnd(Animator animation) {
				fromView.setVisibility(View.GONE);
				invisToVis.start();
				toView.setVisibility(View.VISIBLE);
			}
		});
		visToInvis.start();
		
		fromView.setVisibility(View.GONE);
		toView.setVisibility(View.VISIBLE);
	}
	
	
	private void turnBack() {
		if(mSearchViewVisible) {
			flipAnimation(flSingerByKeyword, flSinger);
		}
		mSearchViewVisible = false;
		//重置页码
		loadSingerList(mCurrentPageNum);
	}
	private void requestSingerList(String keyword) {
		if(!mSearchViewVisible) {
			flipAnimation(flSinger, flSingerByKeyword);
		}
		mSearchViewVisible = true;
		loadSingerListBySearch(keyword, 1);
	}
	
	private void loadingKeywordAnimationSetting() {
		if(Constants.isUseAnimation) {
			SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(mAdapter4Keyword);
			swingBottomInAnimationAdapter.setAbsListView(gvSingerByKeyword);
			gvSingerByKeyword.setAdapter(swingBottomInAnimationAdapter);
		}else {
			gvSingerByKeyword.setAdapter(mAdapter4Keyword);
		}
	}
	
	private class SingerByKeywordDataHandler extends PagedDataHandler<ImageObject> {

		@Override
		public void onSuccess(PagedData<ImageObject> data) {
			KuwoLog.e(LOG_TAG, "requestData by keyword, data's size="+data.data.size());
			mTotalPageNum4Keyword = 1;
			if(data.total <= ListLogic.SINGER_LIST_PAGE_SIZE)
				mTotalPageNum4Keyword = 1;
			else
				mTotalPageNum4Keyword = (data.total%ListLogic.SINGER_LIST_PAGE_SIZE == 0) ? data.total/ListLogic.SINGER_LIST_PAGE_SIZE : (data.total/ListLogic.SINGER_LIST_PAGE_SIZE+1);
			Message msg = new Message();
			msg.what = Constants.MSG_SINGER_COUNT;
			msg.arg1 = data.total;
			EventBus.getDefault().post(msg);
			mCurrentPageNum4Keyword = data.page;
			mImageObjectList4Keyword = data.data;
			loadingKeywordAnimationSetting();
			mAdapter4Keyword.setSingerList(data.data);
			tvSingerSearchEmptyPromptByKeyword.setVisibility(View.INVISIBLE);
			ivSingerPrePagePrompt.setVisibility(View.VISIBLE);
			ivSingerNextPagePrompt.setVisibility(View.VISIBLE);
			tvSingerPagePrompt.setVisibility(View.VISIBLE);
			gvSingerByKeyword.setVisibility(View.VISIBLE);
			if(data.data.size() > 0 && mCurrentPageNum4Keyword == 1) 
				ivSingerPrePagePrompt.setImageResource(R.drawable.iv_page_translate);
			else 
				ivSingerPrePagePrompt.setImageResource(R.drawable.iv_pre_page_normal);
			
			if(data.data.size() > 0 && mCurrentPageNum4Keyword == mTotalPageNum4Keyword) 
				ivSingerNextPagePrompt.setImageResource(R.drawable.iv_page_translate);
			else
				ivSingerNextPagePrompt.setImageResource(R.drawable.iv_next_page_normal);
			tvSingerPagePrompt.setText(mCurrentPageNum4Keyword+" / "+mTotalPageNum4Keyword);
			if(mSingerListBottom4Keyword) {
				if(mLastPosition4Keyword > data.data.size()) 
					gvSingerByKeyword.setSelection(data.data.size()-1);
				else
					gvSingerByKeyword.setSelection(mLastPosition4Keyword);
					
			}else {
				gvSingerByKeyword.setSelection(mLastPosition4Keyword);
			}
		}
		
		@Override
		public void onFailure() {
			super.onFailure();
			Message msg = new Message();
			msg.what = Constants.MSG_SINGER_COUNT;
			msg.arg1 = 0;
			EventBus.getDefault().post(msg);
			ivSingerPrePagePrompt.setVisibility(View.INVISIBLE);
			ivSingerNextPagePrompt.setVisibility(View.INVISIBLE);
			tvSingerPagePrompt.setVisibility(View.INVISIBLE);
			gvSingerByKeyword.setVisibility(View.INVISIBLE);
			tvSingerSearchEmptyPromptByKeyword.setVisibility(View.VISIBLE);
		}
		
		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);
			tvSingerPagePrompt.setText(mCurrentPageNum+1+" / "+mTotalPageNum);
			gvSingerByKeyword.setVisibility(View.INVISIBLE);
			ivSingerPrePagePrompt.setVisibility(View.INVISIBLE);
			ivSingerNextPagePrompt.setVisibility(View.INVISIBLE);
			tvSingerPagePrompt.setVisibility(View.INVISIBLE);
			tvSingerSearchEmptyPromptByKeyword.setVisibility(View.INVISIBLE);
			llSingerRequestFailByKeyword.setVisibility(View.VISIBLE);
			btSingerRequestFailByKeyword.requestFocus();
		}
		
		@Override
		public void onStart() {
			progressBar_singer_byKeyword.setVisibility(View.VISIBLE);
		}
		
		@Override
		public void onFinish() {
			progressBar_singer_byKeyword.setVisibility(View.INVISIBLE);
		}
	}
	
	private void initData() {
		mEventBus = EventBus.getDefault();
		mEventBus.register(this);
		Intent data = activity.getIntent();
		mCurrentPageNum = data.getIntExtra("currentPageNum", 1);
		mCurrentImageObject = (ImageObject) data.getSerializableExtra("currentImageObject");
		mListLogic = new ListLogic();
		mAdapter = new SingerAdapter(activity, new AnimateFirstDisplayListener());
		mAdapter4Keyword = new SingerAdapter(activity, new AnimateFirstDisplayListener());
	}

	private void initView() {
		flSinger = (FrameLayout)activity.findViewById(R.id.flSinger);
		flSingerByKeyword = (FrameLayout)activity.findViewById(R.id.flSingerByKeyword);
		progressBar_singer_byKeyword = (ProgressBar)activity.findViewById(R.id.progressBar_singer_byKeyword);
		tvSingerSearchEmptyPromptByKeyword = (TextView)activity.findViewById(R.id.tvSingerSearchEmptyPromptByKeyword);
		tvSingerSearchEmptyPromptByKeyword.setVisibility(View.INVISIBLE);
		
		ivSingerPrePagePrompt = (ImageView)activity.findViewById(R.id.ivSingerPrePagePrompt);
		ivSingerPrePagePrompt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				loadPrePage();
			}
		});
		ivSingerNextPagePrompt = (ImageView)activity.findViewById(R.id.ivSingerNextPagePrompt);
		ivSingerNextPagePrompt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				loadNextPage();
			}
		});
		progressBar = (ProgressBar) activity.findViewById(R.id.progressBar_singer);
		progressBar.setVisibility(View.INVISIBLE);
		
		llSingerRequestFail = (LinearLayout) activity.findViewById(R.id.llSingerRequestFail);
		llSingerRequestFail.setVisibility(View.INVISIBLE);
		llSingerRequestFailByKeyword = (LinearLayout)activity.findViewById(R.id.llSingerRequestFailByKeyword);
		llSingerRequestFailByKeyword.setVisibility(View.INVISIBLE);
		
		btSingerRequestFailByKeyword = (Button)activity.findViewById(R.id.btSingerRequestFailByKeyword);
		btSingerRequestFailByKeyword.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
		btSingerRequestFail = (Button) activity.findViewById(R.id.btSingerRequestFail);
		btSingerRequestFail.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				loadSingerList(mCurrentPageNum+1);
			}
		});
		gvSingerByKeyword = (GridView)activity.findViewById(R.id.gvSingerByKeyword);
		try {
	         Field f = View.class.getDeclaredField("mScrollCache");
	         f.setAccessible(true);
	         Object scrollabilityCache  = f.get(gvSingerByKeyword);
	         f = f.getType().getDeclaredField("scrollBar");
	         f.setAccessible(true); 
	         Object scrollBarDrawable = f.get(scrollabilityCache);
	         f = f.getType().getDeclaredField("mVerticalThumb");
	         f.setAccessible(true); 
	         Drawable drawable = (Drawable) f.get(scrollBarDrawable); 
	         drawable = activity.getResources().getDrawable(R.drawable.list_scrollbar);
	         f.set(scrollBarDrawable, drawable);
	     } catch (Exception e) {
	         KuwoLog.e(LOG_TAG, e);
	     }
		gvSingerByKeyword.setSelector(new ColorDrawable(Color.TRANSPARENT));
		gvSingerByKeyword.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), true, true));
		gvSingerByKeyword.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ImageObject currentImageObject = mImageObjectList4Keyword.get(position);
				Intent intent = new Intent(activity, SecondItemListActivity.class);
				intent.putExtra("flag", "fromSinger");
				intent.putExtra("currentImageObject", currentImageObject);  
				intent.putExtra("needSearch", false);
				activity.startActivity(intent);
				activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			}
		});
		gvSingerByKeyword.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				KuwoLog.e(LOG_TAG, "position="+position);
				if(view != null)
					mLastPosition4Keyword = position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
		});
		flSingerByKeyword.setRotationY(-90f);
		
		gvSinger = (GridView) activity.findViewById(R.id.gvSinger);
		try {
	         Field f = View.class.getDeclaredField("mScrollCache");
	         f.setAccessible(true);
	         Object scrollabilityCache  = f.get(gvSinger);
	         f = f.getType().getDeclaredField("scrollBar");
	         f.setAccessible(true); 
	         Object scrollBarDrawable = f.get(scrollabilityCache);
	         f = f.getType().getDeclaredField("mVerticalThumb");
	         f.setAccessible(true); 
	         Drawable drawable = (Drawable) f.get(scrollBarDrawable); 
	         drawable = activity.getResources().getDrawable(R.drawable.list_scrollbar);
	         f.set(scrollBarDrawable, drawable);
	     } catch (Exception e) {
	         KuwoLog.e(LOG_TAG, e);
	     }
		gvSinger.setSelector(new ColorDrawable(Color.TRANSPARENT));
		gvSinger.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), true, true));
		gvSinger.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ImageObject currentImageObject = mImageObjectList.get(position);
				Intent intent = new Intent(activity, SecondItemListActivity.class);
				intent.putExtra("flag", "fromSinger");
				intent.putExtra("currentImageObject", currentImageObject);  
				intent.putExtra("needSearch", false);
				activity.startActivity(intent);
				activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			}
		});
		
		gvSinger.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				KuwoLog.e(LOG_TAG, "position="+position);
				if(view != null)
					mLastPosition = position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
		});
		
		tvSingerPagePrompt = (TextView) activity.findViewById(R.id.tvSingerPagePrompt);
	}

	private void processLogic() {
		loadSingerList(mCurrentPageNum);
	}
	
	private void loadSingerList(int pageNum) {
		ImageLoader.getInstance().clearMemoryCache();
		progressBar.setVisibility(View.INVISIBLE);
		llSingerRequestFail.setVisibility(View.INVISIBLE);
		mListLogic.getSingerList(mCurrentImageObject.id, pageNum, new SingerDataHandler());
	}
	
	private void loadSingerListBySearch(String keyword, int pageNum) {
		ImageLoader.getInstance().clearMemoryCache();
		progressBar_singer_byKeyword.setVisibility(View.INVISIBLE);
		llSingerRequestFailByKeyword.setVisibility(View.INVISIBLE);
		mListLogic.getSingerListByKeyword(mCurrentImageObject.inner, keyword, pageNum, new SingerByKeywordDataHandler());
	}
	
	private void loadingAnimationSetting() {
		if(Constants.isUseAnimation) {
			SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(mAdapter);
			swingBottomInAnimationAdapter.setAbsListView(gvSinger);
			gvSinger.setAdapter(swingBottomInAnimationAdapter);
		}else {
			gvSinger.setAdapter(mAdapter);
		}
	}

	private class SingerDataHandler extends PagedDataHandler<ImageObject> {
		
		@Override
		public void onSuccess(final PagedData<ImageObject> data) {
			mTotalPageNum = 1;
			if (data.total<=ListLogic.SINGER_LIST_PAGE_SIZE)
				mTotalPageNum = 1;
			else
				mTotalPageNum = (data.total%ListLogic.SINGER_LIST_PAGE_SIZE == 0) ? data.total/ListLogic.SINGER_LIST_PAGE_SIZE : (data.total/ListLogic.SINGER_LIST_PAGE_SIZE+1);
			Message msg = new Message();
			msg.what = Constants.MSG_SINGER_COUNT;
			msg.arg1 = data.total;
			EventBus.getDefault().post(msg);
			mCurrentPageNum = data.page;
			mImageObjectList = data.data;
			
			loadingAnimationSetting();
			mAdapter.setSingerList(data.data);
			ivSingerPrePagePrompt.setVisibility(View.VISIBLE);
			ivSingerNextPagePrompt.setVisibility(View.VISIBLE);
			tvSingerPagePrompt.setVisibility(View.VISIBLE);
			gvSinger.setVisibility(View.VISIBLE);
			if(data.data.size() > 0 && mCurrentPageNum == 1) {
				ivSingerPrePagePrompt.setImageResource(R.drawable.iv_page_translate);
				List<View> children = gvSinger.getFocusables(View.FOCUS_RIGHT);
				if(children.size() == 0)
					return;
				children.get(0).requestFocus();
			}else {
				ivSingerPrePagePrompt.setImageResource(R.drawable.iv_pre_page_normal);
			}
			
			if(data.data.size() > 0 && mCurrentPageNum == mTotalPageNum) 
				ivSingerNextPagePrompt.setImageResource(R.drawable.iv_page_translate);
			else
				ivSingerNextPagePrompt.setImageResource(R.drawable.iv_next_page_normal);
			tvSingerPagePrompt.setText(mCurrentPageNum+" / "+mTotalPageNum);
			KuwoLog.e(LOG_TAG, "lastPosition="+mLastPosition);
			if(mSingerListBottom) {
				if(mLastPosition > data.data.size()) 
					gvSinger.setSelection(data.data.size()-1);
				else
					gvSinger.setSelection(mLastPosition);
					
			}else {
				gvSinger.setSelection(mLastPosition);
			}
		}
		
		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);
			tvSingerPagePrompt.setText(mCurrentPageNum+1+" / "+mTotalPageNum);
			gvSinger.setVisibility(View.INVISIBLE);
			llSingerRequestFail.setVisibility(View.VISIBLE);
			btSingerRequestFail.requestFocus();
		}
		
		@Override
		public void onStart() {
			progressBar.setVisibility(View.VISIBLE);
		}
		
		@Override
		public void onFinish() {
			progressBar.setVisibility(View.INVISIBLE);
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(mEventBus != null)
			mEventBus.unregister(this);
	}
	
	public static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				} else {
					imageView.setImageBitmap(loadedImage);
				}
			}
		}
	}


}
 