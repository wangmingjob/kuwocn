/**
 * Copyright (c) 2005, Kuwo, Inc. All rights reserved. 
 */
package cn.kuwo.sing.tv.view.fragment;

import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.kuwo.sing.tv.R;
import cn.kuwo.sing.tv.context.Constants;
import cn.kuwo.sing.tv.controller.PinyinOrderController;
import cn.kuwo.sing.tv.utils.DialogUtils;

/**
 * @Package cn.kuwo.sing.tv
 *
 * @Date 2013-3-20, 下午5:43:22, 2013
 *
 * @Author wangming
 *
 */
public class PinyinOrderFragment extends BaseFragment {
	private PinyinOrderController mPinyinOrderController;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.pinyin_order_layout, container, false);
		return view;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}
	
	@Override
	protected void onLoadController() {
		mPinyinOrderController = new PinyinOrderController(getActivity());
		loadController(mPinyinOrderController);
	}
}
