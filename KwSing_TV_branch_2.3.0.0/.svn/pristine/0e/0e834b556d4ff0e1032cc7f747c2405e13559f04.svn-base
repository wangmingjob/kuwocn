/**
 * Copyright (c) 2005, Kuwo, Inc. All rights reserved. 
 */
package cn.kuwo.sing.tv.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.kuwo.sing.tv.R;
import cn.kuwo.sing.tv.controller.SingerController;

/**
 * @Package cn.kuwo.sing.tv.view.fragment
 *
 * @Date 2013-4-15, 下午12:27:03, 2013
 *
 * @Author wangming
 *
 */
public class SingerFragment extends BaseFragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.singer_fragment, container, false);
		return view;
	}
	
	@Override
	protected void onLoadController() {
		loadController(new SingerController(getActivity()));
	}
}
