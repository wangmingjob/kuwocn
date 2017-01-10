/**
 * Copyright (c) 2013, Kuwo.cn, Inc. All rights reserved. 
 */
package cn.kuwo.sing.tv.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.kuwo.sing.tv.R;
import cn.kuwo.sing.tv.controller.MoreController;

/**
 * @Package cn.kuwo.sing.tv.view.fragment
 * 
 * @Date Dec 2, 2013 10:09:10 AM
 *
 * @Author wangming
 *
 */
public class MoreFragment extends BaseFragment {
	private MoreController mMoreController;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.more_layout, container, false);
		return view;
	}
	
	@Override
	protected void onLoadController() {
		mMoreController = new MoreController(getActivity());
		loadController(mMoreController);
	}
}
