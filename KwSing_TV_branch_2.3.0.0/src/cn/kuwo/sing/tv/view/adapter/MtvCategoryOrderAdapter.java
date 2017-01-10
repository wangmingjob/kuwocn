/**
 * Copyright (c) 2005, Kuwo, Inc. All rights reserved. 
 */
package cn.kuwo.sing.tv.view.adapter;

import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.kuwo.sing.tv.R;
import cn.kuwo.sing.tv.bean.ImageObject;
import cn.kuwo.sing.tv.controller.MtvCategoryOrderController.AnimateFirstDisplayListener;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

/**
 * @Package cn.kuwo.sing.tv.view.adapter
 *
 * @Date 2013-3-25, 下午2:01:37, 2013
 *
 * @Author wangming
 *
 */
public class MtvCategoryOrderAdapter extends BaseAdapter {
	private static final String LOG_TAG = "MtvCategoryOrderAdapter";
	private List<ImageObject> mImageObjectList;
	private Activity mActivity;
	private DisplayImageOptions mOptions;
	private ImageLoadingListener mAnimateFirstListener;
	
	public MtvCategoryOrderAdapter(Activity activity, List<ImageObject> imageObjectList, AnimateFirstDisplayListener animateFirstDisplayListener) {
		mActivity = activity;
		mImageObjectList = imageObjectList;
		mAnimateFirstListener = animateFirstDisplayListener;
		mOptions = new DisplayImageOptions.Builder()
		.cacheInMemory()
		.cacheOnDisc()
		.resetViewBeforeLoading()
        .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
        .bitmapConfig(Bitmap.Config.ARGB_8888) // default
		.displayer(new SimpleBitmapDisplayer())
		.build();
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
			view = View.inflate(mActivity, R.layout.list_image_item, null);
			viewHolder = new ViewHolder();
			viewHolder.ivMtvCategoryItem = (ImageView) view.findViewById(R.id.ivListImageItem);
			viewHolder.tvMtvCategoryItemName = (TextView) view.findViewById(R.id.tvListImageItem);
			view.setTag(viewHolder);
		}else {
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}
		ImageObject imageObject = mImageObjectList.get(position);
		ImageLoader.getInstance().displayImage(imageObject.pic, viewHolder.ivMtvCategoryItem, mOptions, mAnimateFirstListener);
		viewHolder.tvMtvCategoryItemName.setText(imageObject.name);
		return view;
	}
	
	private static class ViewHolder {
		ImageView ivMtvCategoryItem;
		TextView tvMtvCategoryItemName;
	}

}
