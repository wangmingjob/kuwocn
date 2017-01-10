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
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
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
import cn.kuwo.sing.tv.controller.MtvCategoryController.AnimateFirstDisplayListener;
import cn.kuwo.sing.tv.logic.ListLogic;
import cn.kuwo.sing.tv.logic.PagedDataHandler;
import cn.kuwo.sing.tv.view.activity.SecondItemListActivity;
import cn.kuwo.sing.tv.view.adapter.MtvCategoryAdapter;
import cn.kuwo.sing.tv.view.adapter.SingerCategoryAdapter;

import com.haarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.PauseOnScrollListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.umeng.analytics.MobclickAgent;

/**
 * @Package cn.kuwo.sing.tv.controller
 *
 * @Date 2013-3-26, 上午10:34:34, 2013
 *
 * @Author wangming
 *
 */
public class SingerCategoryController extends BaseController {
	private static final String LOG_TAG = "SingerCategoryController";
	private GridView gvSingerCategory;
	private int mTotalPageNum;
	private int mCurrentPageNum;
	private List<ImageObject> mImageObjectList;
	private ProgressBar progressBar;
	private Button btSingerCategoryRequestFail;
	private LinearLayout llSingerCategoryRequestFail;

	public SingerCategoryController(Activity activity) {
		super(activity);
		initData();
		initView();
		processLogic();
	}

	private void initData() {
	}

	private void initView() {
		TextView tvSingerCategoryTitle = (TextView) activity.findViewById(R.id.tvSingerCategoryTitle);
		progressBar = (ProgressBar) activity.findViewById(R.id.progressBar_singer_category);
		progressBar.setVisibility(View.INVISIBLE);
		llSingerCategoryRequestFail = (LinearLayout) activity.findViewById(R.id.llSingerCategoryRequestFail);
		llSingerCategoryRequestFail.setVisibility(View.INVISIBLE);
		btSingerCategoryRequestFail = (Button) activity.findViewById(R.id.btSingerCategoryRequestFail);
		btSingerCategoryRequestFail.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				processLogic();
				activity.findViewById(R.id.rbSingerCategory).requestFocus();
			}
		});
		gvSingerCategory = (GridView) activity.findViewById(R.id.gvSingerCategory);
		try {
	         Field f = View.class.getDeclaredField("mScrollCache");
	         f.setAccessible(true);
	         Object scrollabilityCache  = f.get(gvSingerCategory);
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
		gvSingerCategory.setSelector(new ColorDrawable(Color.TRANSPARENT));
		gvSingerCategory.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), true, true));
		gvSingerCategory.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ImageObject currentImageObject = mImageObjectList.get(position);
				Intent intent = new Intent(activity, SecondItemListActivity.class);
				intent.putExtra("flag", "fromSingerCategory");
				intent.putExtra("currentImageObject", currentImageObject);
				intent.putExtra("needSearch", true);
				MobclickAgent.onEvent(activity, Constants.KS_UMENG_SINGER_DETAIL, currentImageObject.name);
				activity.startActivity(intent);
				activity.overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
			}
		});
	}

	private void processLogic() {
		ListLogic listLogic = new ListLogic();
//		progressBar.spin();
		llSingerCategoryRequestFail.setVisibility(View.INVISIBLE);
		listLogic.getSingerBarList(new SingerCategoryDataHandler());
	}
	
	@Override
	public void onBackPressed() {
		AnimateFirstDisplayListener.displayedImages.clear();
		ImageLoader.getInstance().clearMemoryCache();
		super.onBackPressed();
	}
	
	private class SingerCategoryDataHandler extends PagedDataHandler<ImageObject> {

		@Override
		public void onSuccess(PagedData<ImageObject> data) {
			mTotalPageNum = data.total;
			mCurrentPageNum = data.page;
			mImageObjectList = data.data;
//			progressBar.stopSpinning();
			
			SingerCategoryAdapter singerCategoryAdapter = new SingerCategoryAdapter(activity, data.data, new AnimateFirstDisplayListener());
			SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(singerCategoryAdapter);
			swingBottomInAnimationAdapter.setAbsListView(gvSingerCategory);
			gvSingerCategory.setAdapter(swingBottomInAnimationAdapter);
//			gvSingerCategory.setAdapter(singerCategoryAdapter);
		}
		
		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);
			llSingerCategoryRequestFail.setVisibility(View.VISIBLE);
			btSingerCategoryRequestFail.requestFocus();
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
