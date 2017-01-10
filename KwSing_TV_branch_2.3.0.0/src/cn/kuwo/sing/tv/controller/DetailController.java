/**
 * Copyright (c) 2005, Kuwo, Inc. All rights reserved. 
 */
package cn.kuwo.sing.tv.controller;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import cn.kuwo.framework.log.KuwoLog;
import cn.kuwo.sing.tv.R;
import cn.kuwo.sing.tv.bean.ImageObject;
import cn.kuwo.sing.tv.bean.Mtv;
import cn.kuwo.sing.tv.bean.PagedData;
import cn.kuwo.sing.tv.bean.UserMtv;
import cn.kuwo.sing.tv.context.Constants;
import cn.kuwo.sing.tv.logic.ListLogic;
import cn.kuwo.sing.tv.logic.PagedDataHandler;
import cn.kuwo.sing.tv.view.adapter.DetailAdapter;
import cn.kuwo.sing.tv.view.adapter.UserMtvAdapter;

import com.haarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import de.greenrobot.event.EventBus;

/**
 * @Package cn.kuwo.sing.tv.controller
 *
 * @Date 2013-3-27, 下午4:52:57, 2013
 *
 * @Author wangming
 *
 */
public class DetailController extends BaseController {
	private static final String LOG_TAG = "DetailController";
	private String mFlag;
	private ImageObject mImageObject;
	private ListView lvDetail;
	private int mTotalPageNum;
	private int mCurrentPageNum; //从1开始
	private Button btItemDetailPreviousPage;
	private Button btItemDetailNextPagel;
	private ListLogic mListLogic;
	private TextView tvItemDetailPagePrompt;
	private DetailAdapter mAdapter;
	private UserMtvAdapter mUserMtvAdapter;
	private ProgressBar progressBar;
	private Button btDetailRequestFail;
	private LinearLayout llDetailRequestFail;
	private int mFromPage;
	private boolean mMtvListBottom = false;
	private EventBus mEventBus;
	private ImageView ivDetailPrePagePrompt;
	private ImageView ivDetailNextPagePrompt;
	private static final int LIST_ITEM_ANIMATION_DURATION = 400;
	private ListViewDecorator mListViewDecorator;

	public DetailController(Activity activity, int fromPage) {
		super(activity);
		mEventBus = EventBus.getDefault();
		mEventBus.register(this);
		mFromPage = fromPage;
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
		default:
			break;
		}
	}

	private void loadNextPage() {
		if(activity.getCurrentFocus() == lvDetail) {
			mMtvListBottom = true;
			if(mCurrentPageNum < mTotalPageNum) {
				ivDetailNextPagePrompt.setImageResource(R.drawable.iv_next_page_pressed);
				loadMtvList(mCurrentPageNum+1);
			}
		}
	}

	private void loadPrePage() {
		if(activity.getCurrentFocus() == lvDetail) {
			mMtvListBottom = false;
			if(lvDetail.getVisibility() == View.VISIBLE && mCurrentPageNum > 1) {
				ivDetailPrePagePrompt.setImageResource(R.drawable.iv_pre_page_pressed);
				loadMtvList(mCurrentPageNum-1);
			}
		}
	}

	private void initData() {
		Intent data = activity.getIntent();
		mFlag = data.getStringExtra("flag");
		mImageObject = (ImageObject) data.getSerializableExtra("currentImageObject");
		mListLogic = new ListLogic();
	}

	private void initView() {
		ivDetailPrePagePrompt = (ImageView)activity.findViewById(R.id.ivDetailPrePagePrompt);
		ivDetailPrePagePrompt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				loadPrePage();
			}
		});
		ivDetailNextPagePrompt = (ImageView)activity.findViewById(R.id.ivDetailNextPagePrompt);
		ivDetailNextPagePrompt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				loadNextPage();
			}
		});
		progressBar =  (ProgressBar) activity.findViewById(R.id.progressBar_detail_item);
		progressBar.setVisibility(View.INVISIBLE);
		llDetailRequestFail = (LinearLayout) activity.findViewById(R.id.llDetailRequestFail);
		llDetailRequestFail.setVisibility(View.INVISIBLE);
		btDetailRequestFail = (Button) activity.findViewById(R.id.btDetailRequestFail);
		btDetailRequestFail.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				loadMtvList(mCurrentPageNum+1);
			}
		});
		lvDetail = (ListView) activity.findViewById(R.id.lvFragmentDetail);
//		try {
//	         Field f = View.class.getDeclaredField("mScrollCache");
//	         f.setAccessible(true);
//	         Object scrollabilityCache  = f.get(lvFragmentDetail);
//	         f = f.getType().getDeclaredField("scrollBar");
//	         f.setAccessible(true); 
//	         Object scrollBarDrawable = f.get(scrollabilityCache);
//	         f = f.getType().getDeclaredField("mVerticalThumb");
//	         f.setAccessible(true); 
//	         Drawable drawable = (Drawable) f.get(scrollBarDrawable); 
//	         drawable = activity.getResources().getDrawable(R.drawable.list_scrollbar);
//	         f.set(scrollBarDrawable, drawable);
//	     } catch (Exception e) {
//	         KuwoLog.e(LOG_TAG, e);
//	     }
		mAdapter = new DetailAdapter(activity, ListLogic.MTV_LIST_PAGE_SIZE, mFromPage);
		mUserMtvAdapter = new UserMtvAdapter(activity, ListLogic.USER_MTV_LIST_PAGE_SIZE);
		switch (mImageObject.type) {
		case 0:
			mListViewDecorator = new ListViewDecorator(lvDetail, new int[]{R.id.btMtvSing, R.id.btMtvAdd});
			break;
		case 1:
			mListViewDecorator = new ListViewDecorator(lvDetail, new int[]{R.id.btMtvSing, R.id.btMtvAdd});
			break;
		case 2:
			mListViewDecorator = new ListViewDecorator(lvDetail, new int[]{R.id.ivUserMtvSing});
			break;
		default:
			break;
		}
		
		tvItemDetailPagePrompt = (TextView) activity.findViewById(R.id.tvItemDetailPagePrompt);
	}

	private void processLogic() {
		loadMtvList(1);
	}
	
	/**
	 * Page number from 1
	 * 
	 * @param pageNum
	 */
	private void loadMtvList(int pageNum) {
		llDetailRequestFail.setVisibility(View.INVISIBLE);
		int type = mImageObject.type;
		if("fromSinger".equals(mFlag)) {
			mListLogic.getMtvListBySinger(mImageObject.name, pageNum, new DetailDataHandler());
		}else if("fromMtvCategory".equals(mFlag)) {
			switch (type) {
			case 0:
				KuwoLog.d(LOG_TAG, "getCommonMtvList");
				mListLogic.getMtvList(mImageObject.id, pageNum, new DetailDataHandler());
				break;
			case 1:
				KuwoLog.d(LOG_TAG, "getMtvListByLang");
				mListLogic.getMtvListByLang(mImageObject.id, pageNum, new DetailDataHandler());
				break;
			case 2:
				KuwoLog.d(LOG_TAG, "getUserMtvList");
				mListLogic.getUserMtvList(pageNum, new UserMtvDataHandler());
				break;
			default:
				break;
			}
//			progressBar.spin();
		}else if("fromMtvCategoryOrder".equals(mFlag)) {
			switch (type) {
			case 0:
				KuwoLog.d(LOG_TAG, "getCommonMtvList");
				mListLogic.getMtvList(mImageObject.id, pageNum, new DetailDataHandler());
				break;
			default:
				break;
			}
		}
	}
	
	@Override
	public void onBackPressed() {
		AnimateFirstDisplayListener.displayedImages.clear();
		super.onBackPressed();
	}
	
	private void loadingUserMtvAnimationSetting() {
		if(Constants.isUseAnimation) {
			SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(mUserMtvAdapter);
			swingBottomInAnimationAdapter.setAbsListView(lvDetail);
			lvDetail.setAdapter(swingBottomInAnimationAdapter);
		}else {
			lvDetail.setAdapter(mUserMtvAdapter);
		}
	}
	
	private class UserMtvDataHandler extends PagedDataHandler<UserMtv> {

		@Override
		public void onSuccess(final PagedData<UserMtv> data) {
			llDetailRequestFail.setVisibility(View.INVISIBLE);
			Message msg = new Message();
			msg.what = Constants.MSG_SONG_COUNT;
			msg.arg1 = data.total;
			EventBus.getDefault().post(msg);
			if (data.total<=ListLogic.USER_MTV_LIST_PAGE_SIZE)
				mTotalPageNum = 1;
			else 
				mTotalPageNum = (data.total%ListLogic.USER_MTV_LIST_PAGE_SIZE == 0) ? data.total/ListLogic.USER_MTV_LIST_PAGE_SIZE : (data.total/ListLogic.USER_MTV_LIST_PAGE_SIZE+1);
			
			mCurrentPageNum = data.page;
			loadingUserMtvAnimationSetting();
			mUserMtvAdapter.setCurrentPageNum(mCurrentPageNum);
			mUserMtvAdapter.setMtvList(data.data);
			tvItemDetailPagePrompt.setText(mCurrentPageNum+" / "+mTotalPageNum);
//			progressBar.stopSpinning();
			lvDetail.setVisibility(View.VISIBLE);
			if(data.data.size() > 0 && mCurrentPageNum == 1) {
				ivDetailPrePagePrompt.setImageResource(R.drawable.iv_page_translate);
				List<View> children = lvDetail.getFocusables(View.FOCUS_RIGHT);
				if(children.size() == 0)
					return;
				children.get(0).requestFocus();
			}else {
				ivDetailPrePagePrompt.setImageResource(R.drawable.iv_pre_page_normal);
			}
			if(data.data.size() > 0 && mCurrentPageNum == mTotalPageNum) 
				ivDetailNextPagePrompt.setImageResource(R.drawable.iv_page_translate);
			else
				ivDetailNextPagePrompt.setImageResource(R.drawable.iv_next_page_normal);
			
			if(Constants.isUseAnimation) {
				new Handler().postDelayed(new Runnable() {
					
					@Override
					public void run() {
						if(mMtvListBottom)
							lvDetail.setSelection(data.data.size()-1);
						else
							lvDetail.setSelection(0); 
					}
				}, LIST_ITEM_ANIMATION_DURATION);
			}else {
				if(mMtvListBottom)
					lvDetail.setSelection(data.data.size()-1);
				else
					lvDetail.setSelection(0); 
			}
			
		}
		
		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);
			tvItemDetailPagePrompt.setText(mCurrentPageNum+1+" / "+mTotalPageNum);
			lvDetail.setVisibility(View.INVISIBLE);
			llDetailRequestFail.setVisibility(View.VISIBLE);
			btDetailRequestFail.requestFocus();
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
	
	private void loadingAnimationSetting() {
		if(Constants.isUseAnimation) {
			SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(mAdapter);
			swingBottomInAnimationAdapter.setAnimationDurationMillis(LIST_ITEM_ANIMATION_DURATION);
			swingBottomInAnimationAdapter.setAbsListView(lvDetail);
			lvDetail.setAdapter(swingBottomInAnimationAdapter);
		}else {
			lvDetail.setAdapter(mAdapter);
		}
	}
	
	private class DetailDataHandler extends PagedDataHandler<Mtv> {

		@Override
		public void onSuccess(final PagedData<Mtv> data) {
			Message msg = new Message();
			msg.what = Constants.MSG_SONG_COUNT;
			msg.arg1 = data.total;
			EventBus.getDefault().post(msg);
			if (data.total <= ListLogic.MTV_LIST_PAGE_SIZE) 
				mTotalPageNum = 1;
			else 
				mTotalPageNum = (data.total%ListLogic.MTV_LIST_PAGE_SIZE == 0) ? data.total/ListLogic.MTV_LIST_PAGE_SIZE : (data.total/ListLogic.MTV_LIST_PAGE_SIZE+1);
			if("fromSinger".equals(mFlag)) {
				mCurrentPageNum = data.page+1;
			}else if("fromMtvCategory".equals(mFlag)) {
				mCurrentPageNum = data.page;
			}else if("fromMtvCategoryOrder".equals(mFlag)) {
				mCurrentPageNum = data.page;
			}
			loadingAnimationSetting();
			mAdapter.setCurrentPageNum(mCurrentPageNum);
			mAdapter.setMtvList(data.data);
			tvItemDetailPagePrompt.setText(mCurrentPageNum+" / "+mTotalPageNum);
			lvDetail.setVisibility(View.VISIBLE);
			if(data.data.size() > 0 && mCurrentPageNum == 1) {
				ivDetailPrePagePrompt.setImageResource(R.drawable.iv_page_translate);
				List<View> children = lvDetail.getFocusables(View.FOCUS_RIGHT);
				if(children.size() == 0)
					return;
				children.get(0).requestFocus();
			}else {
				ivDetailPrePagePrompt.setImageResource(R.drawable.iv_pre_page_normal);
			}
			
			if(data.data.size() > 0 && mCurrentPageNum == mTotalPageNum) 
				ivDetailNextPagePrompt.setImageResource(R.drawable.iv_page_translate);
			else
				ivDetailNextPagePrompt.setImageResource(R.drawable.iv_next_page_normal);
			
			if(Constants.isUseAnimation) {
				new Handler().postDelayed(new Runnable() {
					
					@Override
					public void run() {
						if(mMtvListBottom) 
							lvDetail.setSelection(data.data.size()-1);
						else 
							lvDetail.setSelection(0);
					}
				}, LIST_ITEM_ANIMATION_DURATION);
			}else {
				if(mMtvListBottom) 
					lvDetail.setSelection(data.data.size()-1);
				else 
					lvDetail.setSelection(0);
			}
		}
		
		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);
			tvItemDetailPagePrompt.setText(mCurrentPageNum+1+" / "+mTotalPageNum);
			lvDetail.setVisibility(View.INVISIBLE);
			llDetailRequestFail.setVisibility(View.VISIBLE);
			btDetailRequestFail.requestFocus();
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
