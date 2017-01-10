/**
 * Copyright (c) 2005, Kuwo, Inc. All rights reserved. 
 */
package cn.kuwo.sing.tv.view.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import cn.kuwo.framework.log.KuwoLog;
import cn.kuwo.framework.utils.SizeUtils;
import cn.kuwo.sing.tv.R;
import cn.kuwo.sing.tv.context.Constants;
import cn.kuwo.sing.tv.utils.DensityUtils;
import cn.kuwo.sing.tv.utils.DialogUtils;

/**
 * @Package cn.kuwo.sing.tv.view.widget
 *
 * @Date 2013-4-28, 下午2:47:13, 2013
 *
 * @Author wangming
 *
 */
public class LetterView extends View {
	private static final String LOG_TAG = "LetterView";
	private Context mContext;
	private char[] letter;
	private float rectWidth;
	private float rectHeight;
	private float letterHorizontalSpace;
	private float letterVerticalSpace;
	private float textWidth;
	private float textHeight;
	private int index = -1; //A--Z的序号
	private static final int MAX_X = 13;
	private static final int MAX_Y = 2;
	private float letterViewHeight = 38f;
	private float letterHorizontalSpaceDpValue = 4f; //dp
	private float letterVerticalSpaceDpValue = 4f; //dp
	private OnPressEnterListener mOnPressEnterListener;
	private boolean letterPressed = false;

	public LetterView(Context context) {
		this(context, null);
	}
	
	public LetterView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public LetterView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		init();
	}
	
	public interface OnPressEnterListener {
		public abstract void onPressEnter(String str);
	}
	
	public void setOnPressEnterListener(OnPressEnterListener listener) {
		mOnPressEnterListener = listener;
	}
	
	private void onPressEnter(String str) {
		if(mOnPressEnterListener != null) {
			mOnPressEnterListener.onPressEnter(str);
		}
	}

	private void init() {
		letter = new char[] {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
				'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		int localIndex = findLetterIndexByCoordinate(event.getX(), event.getY());
		switch (event.getAction()) {
		case MotionEvent.ACTION_UP:
			KuwoLog.d(LOG_TAG, "ACTION_UP");
			//使view回复颜色,并将letter添加到TextView显示栏上  先down 再up
			setFocusChanged(localIndex);
			onPressEnter(String.valueOf(letter[localIndex]));
			break;
		default:
			break;
		}
		return true;
	}
	
	private int findLetterIndexByCoordinate(float x, float y) {
		int index = 0;
		int idY = (int) (y/(getMeasuredHeight()/MAX_Y));
		int idX = (int) (x/(getMeasuredWidth()/MAX_X));
		KuwoLog.d(LOG_TAG, "idX="+idX+",idY="+idY);
		if(idY == 0) {
			index = idX; //idX == 0
		}else {
			index = idX + MAX_X; //idY == 1
		}
		
		if (index == MAX_X*2)
			index = MAX_X*2-1;
		if(index == -1)
			index = 0;
		return index;
	}
	
	@Override
	protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
		super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
		if (gainFocus) {
			//don't process focus when the mouse from down 
			if(direction == 66) { // from left
				setFocusChanged(0);  //默认是A字母被聚焦
			}else if(direction == 33) { //from down
				setFocusChanged(23); //默认是X字母被聚焦
			}else if(direction == 17) { //from right
				setFocusChanged(12);  //默认是M字母被聚焦
			}else {
				setFocusChanged(0); //设置A为默认
			}
		}else {
//			setFocusC//÷÷÷÷÷÷÷hanged(-1);
		}
	}
	
	private void setFocusChanged(int localIndex) {
		index = localIndex;
		postInvalidate();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_UP: //19
			if(index/MAX_X == 0) {
				setFocusChanged(index);
			}else {
				setFocusChanged(index-MAX_X);
				return true;
			}
			break;
		case KeyEvent.KEYCODE_DPAD_DOWN: //20
			if(index/MAX_X == 0) {
				setFocusChanged(index+MAX_X);
				return true;
			}else {
				setFocusChanged(-1);
			}
			break;
		case KeyEvent.KEYCODE_DPAD_LEFT: //21
			if(index%MAX_X == 0) {
				setFocusChanged(-1);
			}else {
				setFocusChanged(index-1);
				return true;
			}
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT: //22
			if((index+1)%MAX_X == 0) {
				setFocusChanged(-1);
			}else {
				setFocusChanged(index+1);
				return true;
			}
			break;
		case KeyEvent.KEYCODE_DPAD_CENTER:
		case KeyEvent.KEYCODE_ENTER: //ok
			onPressEnter(String.valueOf(letter[index]));
			break;
			
		default:
			if (keyCode >= KeyEvent.KEYCODE_A && keyCode <= KeyEvent.KEYCODE_Z){
				int index = keyCode - KeyEvent.KEYCODE_A;
				setFocusChanged(index);
				onPressEnter(String.valueOf(letter[index]));
			}
			break;
		}
		
		switch (event.getAction()) {
		case KeyEvent.ACTION_DOWN:
			letterPressed = true;
			postInvalidate();
			letterPressed = false;
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		Paint rectPaint = new Paint();
//		rectPaint.setColor(Color.rgb(183, 216, 235));
		rectPaint.setColor(Color.TRANSPARENT);
		rectPaint.setAntiAlias(true);
//		rectPaint.setFakeBoldText(true);
		rectPaint.setStrokeWidth(1.0f);
		rectPaint.setStyle(Style.STROKE);
		rectPaint.setTextAlign(Paint.Align.CENTER);
		
		Paint textPaint = new Paint();
		textPaint.setColor(Color.WHITE);
		textPaint.setTextSize(SizeUtils.getFontSize(mContext, 22f));
//		textPaint.setFakeBoldText(true);
		textPaint.setStyle(Style.FILL);
		
		int letterViewWidth = getMeasuredWidth();
		letterViewHeight = mContext.getResources().getDimension(R.dimen.pinyin_order_letterview_height);
		DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
		if(Build.BRAND.equalsIgnoreCase("XiaoMi") && metrics.densityDpi == Constants.SCREEN_DENSITY_DPI_240 && metrics.widthPixels == Constants.SCREEN_WIDTH_PIXELS_1920) {
			letterHorizontalSpace = DensityUtils.dip2px(mContext, letterHorizontalSpaceDpValue*Constants.XIAOMI_NEWTV_SCALE);
			letterVerticalSpace = DensityUtils.dip2px(mContext, letterVerticalSpaceDpValue*Constants.XIAOMI_NEWTV_SCALE);
			float constantValue = (MAX_X*letterViewWidth)/(letterViewWidth-(MAX_X-1)*letterHorizontalSpace);
			rectWidth = (float) (getMeasuredWidth() / constantValue);
			rectHeight = (letterViewHeight-letterVerticalSpace)*0.5f*Constants.XIAOMI_NEWTV_SCALE;
		}else {
			letterHorizontalSpace = DensityUtils.dip2px(mContext, letterHorizontalSpaceDpValue);
			letterVerticalSpace = DensityUtils.dip2px(mContext, letterVerticalSpaceDpValue);
			float constantValue = (MAX_X*letterViewWidth)/(letterViewWidth-(MAX_X-1)*letterHorizontalSpace);
			rectWidth = (float) (getMeasuredWidth() / constantValue);
			rectHeight = (letterViewHeight-letterVerticalSpace)*0.5f;
		}
		textWidth = textPaint.measureText("A");
		textHeight = textWidth;
		
		if(letter.length > 0) {
			for(int i = 0; i< letter.length; i++) {
				Rect rect = createRectByIndex(i);
				if(index == i && letterPressed) {
					rectPaint.setColor(Color.rgb(0, 183, 246));
					rectPaint.setStyle(Style.FILL);
					textPaint.setColor(Color.BLACK);
					canvas.drawRect(rect, rectPaint); 
				}else if(index == i && !letterPressed){
					rectPaint.setColor(Color.rgb(0, 183, 246));
					rectPaint.setStyle(Style.FILL);
					textPaint.setColor(Color.WHITE);
//					textPaint.setColor(Color.BLACK);
					canvas.drawRect(rect, rectPaint); 
				} else {
					rectPaint.setColor(Color.WHITE);
//					rectPaint.setShadowLayer(radius, dx, dy, color)
					rectPaint.setStyle(Style.STROKE);
					textPaint.setColor(Color.WHITE);
					canvas.drawRect(rect, rectPaint);
				}
				canvas.drawText(letter[i]+"", rect.left+rectWidth/2-textWidth/2, rect.top+rectHeight/2+textHeight/2, textPaint);
			}
		}
		super.onDraw(canvas);
	}
	
	
	private Rect createRectByIndex(int i) {
		Rect rect = new Rect();
		int left = (int)((rectWidth + letterHorizontalSpace)*(i%MAX_X));
		int top = (int) ((rectHeight + letterVerticalSpace)*(i/MAX_X));
		int right = (int) (left + rectWidth);	
		int bottom = (int) (top + rectHeight);
		rect.set(left, top, right, bottom);
		return rect;
	}
}
