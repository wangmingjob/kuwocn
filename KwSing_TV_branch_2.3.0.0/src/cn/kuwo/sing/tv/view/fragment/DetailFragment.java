/**
 * Copyright (c) 2005, Kuwo, Inc. All rights reserved. 
 */
package cn.kuwo.sing.tv.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.kuwo.sing.tv.R;
import cn.kuwo.sing.tv.controller.DetailController;

/**
 * @Package cn.kuwo.sing.tv.view.fragment
 *
 * @Date 2013-3-27, 下午4:06:33, 2013
 *
 * @Author wangming
 *
 */
public class DetailFragment extends BaseFragment {
	private static final String LOG_TAG = "DetailFragment";
	int mFromPage = -1;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.item_detail_fragment, container, false);
		return view;
	}
	
	public void setFromPage(int fromPage)
	{
		mFromPage = fromPage; 
	}
	
	@Override
	protected void onLoadController() {
		loadController(new DetailController(getActivity(), mFromPage));
	}
	
}
