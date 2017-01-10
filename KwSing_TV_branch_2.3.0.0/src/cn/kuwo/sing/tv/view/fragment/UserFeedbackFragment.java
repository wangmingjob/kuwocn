/**
 * Copyright (c) 2005, Kuwo, Inc. All rights reserved. 
 */
package cn.kuwo.sing.tv.view.fragment;

import com.umeng.analytics.MobclickAgent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import cn.kuwo.framework.context.AppContext;
import cn.kuwo.sing.tv.R;
import cn.kuwo.sing.tv.context.Constants;
import cn.kuwo.sing.tv.controller.UserFeedbackController;

/**
 * @Package cn.kuwo.sing.tv.view.fragment
 * 
 * @Date 2013年9月16日 下午1:40:19
 *
 * @author wangming
 *
 */
public class UserFeedbackFragment extends BaseFragment {
	private UserFeedbackController mUserFeedbackController;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.user_feedback_layout, container, false);
		return view;
	}
	
	@Override
	protected void onLoadController() {
		mUserFeedbackController = new UserFeedbackController(getActivity());
		loadController(mUserFeedbackController);
	}
}
