/**
 * Copyright (c) 2005, Kuwo, Inc. All rights reserved. 
 */
package cn.kuwo.sing.tv.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.kuwo.sing.tv.R;
import cn.kuwo.sing.tv.context.Constants;
import cn.kuwo.sing.tv.controller.MtvCategoryController;
import cn.kuwo.sing.tv.controller.MtvCategoryOrderController;

/**
 * @Package cn.kuwo.sing.tv
 *
 * @Date 2013-3-20, 下午5:44:00, 2013
 *
 * @Author wangming
 *
 */
public class MtvCategoryFragment extends BaseFragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.mtv_category_order_layout, container, false);
		return view;
	}
	
	@Override
	protected void onLoadController() {
		loadController(new MtvCategoryOrderController(getActivity()));
	}
}
