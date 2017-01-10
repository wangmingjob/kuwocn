/**
 * Copyright (c) 2005, Kuwo, Inc. All rights reserved. 
 */
package cn.kuwo.sing.tv.view.adapter;

import java.util.ArrayList;
import java.util.List;

import cn.kuwo.framework.context.AppContext;
import cn.kuwo.framework.log.KuwoLog;
import cn.kuwo.sing.tv.R;
import cn.kuwo.sing.tv.bean.ImageObject;
import cn.kuwo.sing.tv.bean.Mtv;
import cn.kuwo.sing.tv.controller.SingerController.AnimateFirstDisplayListener;
import cn.kuwo.sing.tv.utils.DensityUtils;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @Package cn.kuwo.sing.tv.view.adapter
 *
 * @Date 2013-4-15, 下午2:48:12, 2013
 *
 * @Author wangming
 *
 */
public class SingerAdapter extends BaseAdapter {
	private static final String LOG_TAG = "SingerAdapter";
	private List<ImageObject> mImageObjectList;
	private Activity mActivity;
	private DisplayImageOptions mOptions;
	private ImageLoadingListener mAnimateFirstListener;
	
	public SingerAdapter(Activity activity, AnimateFirstDisplayListener animateFirstDisplayListener) {
		mActivity = activity;
		mImageObjectList = new ArrayList<ImageObject>();
		mOptions = new DisplayImageOptions.Builder()
		.cacheInMemory()
		.cacheOnDisc()
        .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
        .bitmapConfig(Bitmap.Config.ARGB_8888) // default
		.displayer(new SimpleBitmapDisplayer())
		.build();
	}
	
	public void setSingerList(List<ImageObject> data) {
		mImageObjectList = data;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mImageObjectList.size();
	}

	@Override
	public Object getItem(int position) {
		return mImageObjectList.get(position);
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
			view = View.inflate(mActivity, R.layout.singer_item, null);
			viewHolder = new ViewHolder();
			viewHolder.ivSingerItem = (ImageView) view.findViewById(R.id.ivSingerItem);
			viewHolder.tvSingerItemName = (TextView) view.findViewById(R.id.tvSingerItemName);
			view.setTag(viewHolder);
		}else {
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}
		ImageObject imageObject = mImageObjectList.get(position);
		ImageLoader.getInstance().displayImage(imageObject.pic, viewHolder.ivSingerItem, mOptions, mAnimateFirstListener);
		viewHolder.tvSingerItemName.setText(imageObject.name);
		return view;
	}
	
	private static class ViewHolder {
		ImageView ivSingerItem;
		TextView tvSingerItemName;
	}
}
