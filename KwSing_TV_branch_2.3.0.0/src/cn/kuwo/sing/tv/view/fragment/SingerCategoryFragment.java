/**
 * Copyright (c) 2005, Kuwo, Inc. All rights reserved. 
 */
package cn.kuwo.sing.tv.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.kuwo.sing.tv.R;
import cn.kuwo.sing.tv.controller.SingerCategoryController;

/**
 * @Package cn.kuwo.sing.tv
 *
 * @Date 2013-3-20, 下午5:58:19, 2013
 *
 * @Author wangming
 *
 */
public class SingerCategoryFragment extends BaseFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.singer_category_layout, container, false);
		return view;
	}
	
	@Override
	protected void onLoadController() {
		loadController(new SingerCategoryController(getActivity()));
	}
	
}
