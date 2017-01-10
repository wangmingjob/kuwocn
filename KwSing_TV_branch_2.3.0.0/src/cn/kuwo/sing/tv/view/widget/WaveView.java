package cn.kuwo.sing.tv.view.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;
import cn.kuwo.framework.context.AppContext;
import cn.kuwo.framework.log.KuwoLog;
import cn.kuwo.sing.tv.R;
import cn.kuwo.sing.tv.utils.DensityUtils;
import cn.kuwo.sing.util.lyric.Lyric;
import cn.kuwo.sing.util.lyric.Sentence;
import cn.kuwo.sing.util.lyric.Word;

public class WaveView extends ImageView {

	private final String TAG = "WaveView";
	private Lyric lyric;
	private long position;
	private int envelope;
	private int color = WAVE_GREEN;

	private final int MS_PER_P = 5;	// 一像素表示多少毫秒
	private float RADIUS = 5;	// 起始圆点的半径
	private final float GAP = RADIUS + 1;	// 两个频谱间的间隙
	private final int ARROW_WIDTH = 77; // 起点位置
	private final int PADDING = 15; // 上下边距
//	private final int MIN_HIGHT = 200;
	
	public final static int WAVE_GREEN = R.drawable.sing_record_wave_green;
	public final static int WAVE_RED = R.drawable.sing_record_wave_red;
	public final static int WAVE_YELLOW = R.drawable.sing_record_wave_yellow;
	private Bitmap bmpGreen = BitmapFactory.decodeResource(getResources(), WAVE_GREEN);  
	private Bitmap bmpRed = BitmapFactory.decodeResource(getResources(), WAVE_RED);  
	private Bitmap bmpYellow = BitmapFactory.decodeResource(getResources(), WAVE_YELLOW);  
	
	
	public WaveView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public WaveView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public WaveView(Context context) {
		this(context, null);
	}
	
	public void setLyric(Lyric lyric) {
		this.lyric = lyric;
	}
	
	public void setPosition(long position) {
//		KuwoLog.v(TAG, "setPosition: " + position);
		this.position = position;
		invalidate();
	}
	
	public void setArrowValue(int envelope) {
//		KuwoLog.i(TAG, "envelope: " + envelope + "color: " + color);
		this.envelope = envelope;
		invalidate();
	} 
	
	public void setArrowColor(int color) {
//		KuwoLog.i(TAG, "envelope: " + envelope + "color: " + color);
		this.color = color;
		invalidate();
	} 
	
//	@Override
//	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
////		int h = getResources().getDrawable(R.drawable.sing_wave_bg).getBounds().height();
////		int h = getBackground().getBounds().height();
//		
//		int h = 600;
//		super.onMeasure(300, h);
//	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (canvas == null)
			return;
		
		drawBackground(canvas);
		drawWords(canvas);
		drawArrow(canvas);
	}
	
	// 绘制背景
	private void drawBackground(Canvas canvas) {
		// 清除画布
		Paint paint = new Paint();
		paint.setAlpha(0);
		paint.setColor(Color.TRANSPARENT);
		canvas.drawRect(canvas.getClipBounds(),  paint);
	}
	
	// 绘制频谱
	private void drawWords(Canvas canvas) {
//		KuwoLog.i(TAG, "drawWords, canvas width:" + canvas.getWidth());
		
		if (lyric == null || lyric.getType() != Lyric.LYRIC_TYPE_KDTX)
			return;
		
		Paint paint = new Paint();
		for (Sentence sentence : lyric.getSentences()) {
			for (Word word : sentence.getWords()) {
				float width = word.getTimespan() / MS_PER_P - GAP;
				float hight = computeHight(canvas, word.getEnvelope());
				long left = (word.getTimestamp() - position)/ MS_PER_P + ARROW_WIDTH;
				KuwoLog.v(TAG, String.format("position:%d   sentence:%d    word:%d    left: %d    stamp: %d", position, sentence.getIndex(), word.getIndex(), left, word.getTimestamp()));
				if (left + width < 0)
					continue;
				
				if (left > canvas.getWidth()) {
//					KuwoLog.i(TAG, "drawWords complete");
					return;
				}
				
				if (AppContext.SCREEN_HIGHT <= 320)
					RADIUS = 3;
				
				// 绘制
				paint.setColor(Color.rgb(255, 197, 0));
				paint.setStrokeWidth(1);
				canvas.drawCircle(left+RADIUS/2, hight+RADIUS/2, RADIUS, paint);
//				canvas.drawLine(left, hight, left + width, hight, paint);
				canvas.drawRoundRect(new RectF(left, hight, left + width, hight+RADIUS), RADIUS-1, RADIUS-1, paint);
				// FOR TEST 标记包罗值
//				paint.setColor(Color.RED);
//				canvas.drawText(word.getEnvelope()+"", left, hight, paint);
			}
		}
		
//		KuwoLog.i(TAG, "drawWords complete");
	}
	
	
	// 绘制标记
	private void drawArrow(Canvas canvas) {
		if (lyric == null)
			return;
		
		Bitmap bmp = null;
		switch (color) {
			case WAVE_GREEN: bmp = bmpGreen; break;
			case WAVE_RED: bmp = bmpRed; break;
			case WAVE_YELLOW: bmp = bmpYellow; break;
		}
	 
		if(bmp == null){
			KuwoLog.i(TAG, "图像是空的");
		}
		int hight = computeHight(canvas, envelope) - bmp.getHeight()/2;
		float constantValue = AppContext.SCREEN_WIDTH*305f/1868;
		int left = (int) (constantValue - bmpGreen.getWidth());
        canvas.drawBitmap(bmp, left, hight, null);
	}

	// 算包络值在画布中对应的Y坐标
	private int computeHight(Canvas canvas, int envelope) {
//		KuwoLog.d(TAG, "getHeight():  "+getHeight() +"canvas.getHeight(): " +canvas.getHeight());
		int x = envelope*envelope/100;
		return getHeight() - PADDING- (getHeight() - PADDING*2) * x / 100;
	}
}
