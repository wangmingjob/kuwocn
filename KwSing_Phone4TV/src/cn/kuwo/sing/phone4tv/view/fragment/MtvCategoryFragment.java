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
import cn.kuwo.sing.phone4tv.view.activity.DetailActivity;
import cn.kuwo.sing.phone4tv.view.adapter.ImageObjectAdapter;

import com.nhaarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;


/**
 * @Package cn.kuwo.sing.phone4tv.view.fragment.content.sub
 *
 * @Date 2014-3-5, 上午11:34:30
 *
 * @Author wangming
 * 
 * @Description 分类点歌
 *
 */
public class MtvCategoryFragment extends BaseProgressFragment {
	private static final String LOG_TAG = "MtvCategoryOrderFragment";
	private View mContentView;
	private GridView gvFragmentMtvCategoryOrder;
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
		mContentView = inflater.inflate(R.layout.fragment_mtv_category_order, null);
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
		gvFragmentMtvCategoryOrder = (GridView) getActivity().findViewById(R.id.gvFragmentMtvCategoryOrder);
		gvFragmentMtvCategoryOrder.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ImageObject currentImageObject = (ImageObject) mImageObjectAdapter.getItem(position);
				Intent intent = new Intent(getActivity(), DetailActivity.class);
				intent.putExtra("pageFrom", Constants.PAGE_FROM_MTV_CATEGORY_ORDER);
				intent.putExtra("imageObject", currentImageObject);
//				MobclickAgent.onEvent(getActivity(), Constants.KS_UMENG_HOTBAR_DETAIL, currentImageObject.name);
//				MobclickAgent.onEvent(getActivity(), Constants.KS_UMENG_HOTBAR_DETAIL_POSITION, String.valueOf(position));
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
		mListBusiness.getMtvCategoryOrderPageData(new MtvCategoryOrderPageDataHandler());
	}
	
	private class MtvCategoryOrderPageDataHandler extends PageDataHandler<ImageObject> {

		@Override
		public void onSuccess(PageData<ImageObject> data) {
			LogSystem.d(LOG_TAG, "MtvCategoryOrderPageDataHandler onSuccess, pageData="+data.data);
			mImageObjectAdapter.setImageObjectData(1, data.data);
//			SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(mImageObjectAdapter,
//					Constants.LIST_ITEM_ANIMATION_DELAY, Constants.LIST_ITEM_ANIMATION_DURATION);
//			swingBottomInAnimationAdapter.setAbsListView(gvFragmentMtvCategoryOrder);
//			gvFragmentMtvCategoryOrder.setAdapter(swingBottomInAnimationAdapter);
			gvFragmentMtvCategoryOrder.setAdapter(mImageObjectAdapter);
			if(Build.VERSION.SDK_INT >= 14)
				setContentEmpty(false);
				
			setContentShown(true);
		}
		
		@Override
		public void onFailure(Throwable error, String content) {
			LogSystem.e(LOG_TAG, "MtvCategoryOrderPageDataHandler onFailure, content="+content);
		}

	}
}
