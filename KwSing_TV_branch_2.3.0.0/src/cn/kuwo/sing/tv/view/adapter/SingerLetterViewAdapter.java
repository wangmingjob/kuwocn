/**
 * Copyright (c) 2005, Kuwo, Inc. All rights reserved. 
 */
package cn.kuwo.sing.tv.view.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import cn.kuwo.sing.tv.R;

/**
 * @Package cn.kuwo.sing.tv.view.adapter
 *
 * @Date 2014-2-27, 下午5:14:33
 *
 * @Author wangming
 *
 */
public class SingerLetterViewAdapter extends BaseAdapter {
	private static final String LOG_TAG = "SingerLetterView";
	private Context mContext; 
	private List<String> mLetterViewList;
	
	public SingerLetterViewAdapter(Context context) {
		mContext = context;
		mLetterViewList = new ArrayList<String>();
		for(int i = 65; i <= 90; i++) {
			mLetterViewList.add(String.valueOf((char)i));
		}
		mLetterViewList.add("#");
		mLetterViewList.add("删");
		mLetterViewList.add("清");
	}

	@Override
	public int getCount() {
		return mLetterViewList.size();
	}

	@Override
	public Object getItem(int position) {
		return mLetterViewList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = View.inflate(mContext, R.layout.item_singer_letter_view, null);
		TextView tvSingerLetterView = (TextView) view.findViewById(R.id.tvSingerLetterView);
		tvSingerLetterView.setText(mLetterViewList.get(position)+"");
		return view;
	}

}
