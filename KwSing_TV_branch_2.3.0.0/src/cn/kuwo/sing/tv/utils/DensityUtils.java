package cn.kuwo.sing.tv.utils;

import cn.kuwo.sing.tv.R;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.TextView;

public class DensityUtils {
	
	public static void setTextSizeSpUnit4XiaoMi(Context context, TextView tv) {
		tv.getTextSize();
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
//		if(metrics.density == 2.0 && metrics.widthPixels == 1920) {
//		}
		float textSize = context.getResources().getDimension(R.dimen.image_text);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize*0.75f);
	}
	
	public static void setTextSizeSpUnit4XiaoMi2(Context context, TextView tv) {
		tv.getTextSize();
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
//		if(metrics.density == 2.0 && metrics.widthPixels == 1920) {
//		}
		float textSize = context.getResources().getDimension(R.dimen.image_text);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize*0.5f);
	}
	
	/**
	 * Complex unit is sp
	 * 
	 * @param context
	 * @param fontSize
	 * @return
	 */
	public static float convertToSpUnit(Context context, int dimenId) {
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		float fontSize = context.getResources().getDimension(dimenId);
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, fontSize, metrics);
	}

	/**
	 * change dip to px
	 * 
	 * @param context
	 * @param dpValue
	 * @return
	 */
	public static int dip2px(Context context, float dpValue) {
		final float density = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * density + 0.5f);
	}

	/**
	 * change px to dip
	 * 
	 * @param context
	 * @param pxValue
	 * @return
	 */
	public static int px2dip(Context context, float pxValue) {
		final float density = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / density + 0.5f);
	}
	
	 /**
	  * 将px值转换为sp值，保证文字大小不变
	  * 
	  * @param pxValue
	  * @param fontScale（DisplayMetrics类中属性scaledDensity）
	  * @return
	  */
	 public static int px2sp(Context context, float pxValue) {
		 final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
	  return (int) (pxValue / fontScale + 0.5f);
	 }


}
