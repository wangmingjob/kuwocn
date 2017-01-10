/**
 * Copyright (c) 2005, Kuwo, Inc. All rights reserved. 
 */
package cn.kuwo.sing.tv.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.kuwo.sing.tv.R;
import cn.kuwo.sing.tv.controller.OrderedMtvController;
import cn.kuwo.sing.tv.controller.PlayOrderedMtvController;

/**
 * @Package cn.kuwo.sing.tv
 *
 * @Date 2013-3-20, 下午5:42:51, 2013
 *
 * @Author wangming
 *
 */
public class PlayOrderedMtvFragment extends BaseFragment {
	private PlayOrderedMtvController mOrderedMtvController;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.play_ordered_mtv_layout, container, false);
		return view;
	}
	
	@Override
	protected void onLoadController() {
		mOrderedMtvController = new PlayOrderedMtvController(getActivity());
		loadController(mOrderedMtvController);
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}
}
