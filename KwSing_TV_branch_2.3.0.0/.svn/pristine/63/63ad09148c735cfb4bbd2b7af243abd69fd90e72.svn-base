package cn.kuwo.sing.tv.controller;

import java.util.List;

import android.graphics.drawable.BitmapDrawable;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import cn.kuwo.framework.log.KuwoLog;
import cn.kuwo.sing.tv.R;
import cn.kuwo.sing.tv.context.Constants;
import cn.kuwo.sing.tv.logic.ListLogic;
import cn.kuwo.sing.tv.utils.DialogUtils;
import de.greenrobot.event.EventBus;

public class ListViewDecorator {

	protected static final String TAG = "ListViewDecorator";

	private ListView mListView;
	private int mCurrentIndex = 0;
	private View mCurrentView = null;
	private View mCurrentListItem = null;
	private int[] mButtons = {R.id.btMtvSing, R.id.btMtvTop, R.id.btMtvDelete};
	private int mCurrentPosition;
	
	public ListViewDecorator(ListView view, int[] buttons) {
		mListView = view;
		mButtons = buttons;
		
		view.setOnItemSelectedListener(mOnItemSelectedListener);
		view.setOnKeyListener(mOnKeyListener );
		view.setOnItemClickListener(mOnItemClickListener );
	}
	
	private OnItemSelectedListener mOnItemSelectedListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			mCurrentListItem = view;
			mCurrentPosition = position;
			KuwoLog.e(TAG, "rowId:"+id+",position:"+position);
			if(id == 0 || id == (ListLogic.MTV_LIST_PAGE_SIZE-1)) {
				//光标移动到顶端或者底部时，发送消息
				Message msg = new Message();
				msg.what = Constants.MSG_DETAIL_FRAGMENT_LIST_ROWID;
				msg.arg1 = position;
				EventBus.getDefault().post(msg);
			}
			activeButton(mCurrentIndex);
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {

		}
		
	};
	
	private OnKeyListener mOnKeyListener = new OnKeyListener() {
		
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if (event.getAction() == KeyEvent.ACTION_DOWN) {
				int index = -1;
				switch (keyCode) {
				case KeyEvent.KEYCODE_DPAD_LEFT:
					index = mCurrentIndex - 1;
					break;
				case KeyEvent.KEYCODE_DPAD_RIGHT:
					index = mCurrentIndex + 1;
					break;
				}
				
				if(index>=0 && index<mButtons.length) {
					activeButton(index);
					return true;
				}
			}
			return false;
		}
	};
	
	private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if(mCurrentView != null && mCurrentPosition == position) 
				mCurrentView.performClick();
		}
	};
	
	public void activeButton(int index) {
		if (mCurrentListItem == null)
			return;
		
		if (mCurrentView != null) {
			mCurrentView.setActivated(false);
		}
		
		mCurrentIndex = index;
		mCurrentView = mCurrentListItem.findViewById(mButtons[mCurrentIndex]);
		if(mCurrentView != null)
			mCurrentView.setActivated(true);
	}
	
	
}
