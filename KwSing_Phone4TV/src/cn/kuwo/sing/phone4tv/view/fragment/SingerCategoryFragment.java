/**
 * Copyright (c) 2005, Kuwo, Inc. All rights reserved. 
 */
package cn.kuwo.sing.phone4tv.view.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import cn.kuwo.sing.phone4tv.R;
import cn.kuwo.sing.phone4tv.bean.ImageObject;
import cn.kuwo.sing.phone4tv.bean.PageData;
import cn.kuwo.sing.phone4tv.business.ListBusiness;
import cn.kuwo.sing.phone4tv.commmons.log.LogSystem;
import cn.kuwo.sing.phone4tv.commons.context.Constants;
import cn.kuwo.sing.phone4tv.commons.util.PageDataHandler;
import cn.kuwo.sing.phone4tv.commons.util.CommonUtils.AnimateFirstDisplayListener;
import cn.kuwo.sing.phone4tv.view.activity.SingerActivity;
import cn.kuwo.sing.phone4tv.view.adapter.ImageObjectAdapter;

import com.nhaarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;


/**
 * 
 * @Package cn.kuwo.sing.phone4tv.view.fragment
 * 
 * @Date Mar 6, 2014 4:13:48 PM
 *
 * @Author wangming
 *
 */
public class SingerCategoryFragment extends BaseProgressFragment {
	private static final String LOG_TAG = "SingerCategoryFragment";
	private View mContentView;
	private GridView gvFragmentSingerCategory;
	private ListBusiness mListBusiness;
	private ImageObjectAdapter mImageObjectAdapter;
	private TextView empty;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContentView = inflater.inflate(R.layout.fragment_singer_category, null);
		return inflater.inflate(R.layout.fragment_progress_empty, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// Setup content view
		setContentView(mContentView);
		// Setup text for empty content
		setEmptyText(R.string.load_data_fail);
		
		initData();
		initView();
		obtainData();
	}

	private void initData() {
		mListBusiness = new ListBusiness();
	}
	
	private void initView() {
		gvFragmentSingerCategory = (GridView) getActivity().findViewById(R.id.gvFragmentSingerCategory);
		gvFragmentSingerCategory.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ImageObject currentImageObject = (ImageObject) mImageObjectAdapter.getItem(position);
				Intent intent = new Intent(getActivity(), SingerActivity.class);
				intent.putExtra("imageObject", currentImageObject);
				getActivity().startActivity(intent);
			}
		});
		mImageObjectAdapter = new ImageObjectAdapter(getActivity(), new AnimateFirstDisplayListener());

		empty = (TextView)getActivity().findViewById(android.R.id.empty);
		empty.setOnClickListener(mOnClickListener);
	}
	
	private View.OnClickListener mOnClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case android.R.id.empty:
				obtainData();
				break;

			default:
				break;
			}
		}
	};


	private void obtainData() {
		setContentShown(false);
		mListBusiness.getSingerCategoryPageData(new SingerCategoryPageDataHandler());
	}
	
	private class SingerCategoryPageDataHandler extends PageDataHandler<ImageObject> {

		@Override
		public void onSuccess(PageData<ImageObject> data) {
			LogSystem.d(LOG_TAG, "SingerCategoryPageDataHandler onSuccess, pageData="+data.data);
			mImageObjectAdapter.setImageObjectData(1, data.data);
//			SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(mImageObjectAdapter,
//					Constants.LIST_ITEM_ANIMATION_DELAY, Constants.LIST_ITEM_ANIMATION_DURATION);
//			swingBottomInAnimationAdapter.setAbsListView(gvFragmentSingerCategory);
//			gvFragmentSingerCategory.setAdapter(swingBottomInAnimationAdapter);
			gvFragmentSingerCategory.setAdapter(mImageObjectAdapter);
			if(Build.VERSION.SDK_INT >= 14)
				setContentEmpty(false);
			setContentShown(true);
		}
		
		@Override
		public void onFailure(Throwable error, String content) {
			LogSystem.e(LOG_TAG, "SingerCategoryPageDataHandler onFailure, content="+content);
		}

	}
}
