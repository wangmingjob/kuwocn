/**
 * Copyright (c) 2005, Kuwo, Inc. All rights reserved. 
 */
package cn.kuwo.sing.tv.view.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.kuwo.sing.tv.R;

/**
 * @Package cn.kuwo.sing.tv.view.adapter
 *
 * @Date 2014-2-13, 下午2:14:40
 *
 * @Author wangming
 *
 */
public class HotKeywordListAdapter extends BaseAdapter {
	private Context mContext;
	private List<String> mHotKeywordList;
	
	public HotKeywordListAdapter(Context context) {
		mContext = context;
	}
	
	public void setHotKeywordList(List<String> hotKeywordList) {
		mHotKeywordList = hotKeywordList;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mHotKeywordList.size();
	}

	@Override
	public Object getItem(int position) {
		return mHotKeywordList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		ViewHolder viewHolder = null;
		if(convertView == null) {
			view = View.inflate(mContext, R.layout.item_hot_keyword_layout, null);
			viewHolder = new ViewHolder();
			viewHolder.tvHotKeyword = (TextView) view.findViewById(R.id.tvHotKeyword);
			view.setTag(viewHolder);
		}else {
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}
		
		final String hotKeyword = mHotKeywordList.get(position);
		viewHolder.tvHotKeyword.setText(hotKeyword);
		return view;
	}
	
	private static class ViewHolder {
		TextView tvHotKeyword;
	}

}
