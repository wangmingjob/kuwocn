package cn.kuwo.sing.tv.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import cn.kuwo.framework.context.AppContext;
import cn.kuwo.framework.utils.ViewUtils;
import cn.kuwo.sing.tv.R;

public class DialogUtils {
	private final static String LOG_TAG = "DialogUtils";
	
	public static boolean enable_toast_visible = true;
	/**
	 * 默认样式的提示
	 * 
	 * @param context
	 * @param str
	 *            显示的内容
	 * @param longToast
	 *            长时间显示还是短时间显示
	 */
	public static Toast toast(CharSequence text, boolean isLongToast) {
		if (!enable_toast_visible) {
			return null;
		}
		
		View toastRoot = ViewUtils.createView(AppContext.context, R.layout.toast);  
		TextView message = (TextView) toastRoot.findViewById(R.id.tvMessage);  
        message.setText(text);  
        
        Toast toast = new Toast(AppContext.context);  
        toast.setDuration(isLongToast ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);  
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(toastRoot);  
        toast.show(); 
        return toast;
	}
	
	public static Toast toast(CharSequence text, boolean isLongToast, int gravity) {
		if (!enable_toast_visible) {
			return null;
		}
		
		View toastRoot = ViewUtils.createView(AppContext.context, R.layout.toast);  
		TextView message = (TextView) toastRoot.findViewById(R.id.tvMessage);  
        message.setText(text);  
        
        Toast toast = new Toast(AppContext.context);  
        toast.setDuration(isLongToast ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);  
        toast.setGravity(gravity, 0, 0);
        toast.setView(toastRoot);  
        toast.show(); 
        return toast;
	}
	
	public static Toast toast(CharSequence text) {
		return toast(text, true);
	}

	/**
	 * 自定义样式的提示
	 * 
	 * @param context
	 * @param textId
	 *            资源id
	 * @param longToast
	 *            长时间显示还是短时间显示
	 */
	public static Toast toast(int textId, boolean longToast) {
		if (!enable_toast_visible) {
			return null;
		}
		CharSequence text = AppContext.context.getResources().getText(textId);
		return toast(text, longToast);
	}
	
	public static Toast toast(int textId) {
		return toast(textId, true);
	}

	public static void toast(final Activity activity, int textId, final boolean longToast) {
		if (!enable_toast_visible) {
			return;
		}
		final CharSequence text = activity.getResources().getText(textId);
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				toast(text, longToast);
			}
		});
	}

	public static AlertDialog alert(Context context, DialogInterface.OnClickListener dialogOnClickListener, int title,
			int positiveButtonId, int neutralButtonId, int negativeButtonId,
			int message) {

		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		if (title > 0)
			builder.setTitle(title);

		if (positiveButtonId != -1) {
			builder.setPositiveButton(positiveButtonId, dialogOnClickListener);
		}
		if (neutralButtonId != -1) {
			builder.setNeutralButton(neutralButtonId, dialogOnClickListener);
		}
		if (negativeButtonId != -1) {
			builder.setNegativeButton(negativeButtonId, dialogOnClickListener);
		}
		builder.setMessage(message);

		try {
			AlertDialog dialog = builder.show();
//			dialog.setCanceledOnTouchOutside(true);
			return dialog;
		} catch (Exception e) {
			return null;
		}

	}
	
	public static void alertExitDialog(Context context) {
		View view = View.inflate(context, R.layout.exit_dialog_layout , null);
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		final AlertDialog dialog = builder.create();
		Button btExitDialogOk = (Button) view.findViewById(R.id.btExitDialogOk);
		btExitDialogOk.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		Button btExitDialogCancel = (Button) view.findViewById(R.id.btExitDialogCancel);
		btExitDialogCancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.getWindow().setGravity(Gravity.CENTER);
		WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
		p.height = AppContext.SCREEN_HIGHT/2;
		p.width = AppContext.SCREEN_WIDTH;
		dialog.getWindow().setAttributes(p);
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setView(view);
		dialog.show();
	}
	
	/**
	 * 显示简单消息对话框
	 * 
	 * @param context
	 * @param dialogOnClickListener
	 * @param title
	 * @param positiveButtonId
	 * @param neutralButtonId
	 * @param negativeButtonId
	 * @param message
	 * @return
	 */
	public static AlertDialog alert(Context context, DialogInterface.OnClickListener dialogOnClickListener, int title,
			int positiveButtonId, int neutralButtonId, int negativeButtonId,
			CharSequence message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		
		if (title > 0)
			builder.setTitle(title);

		if (positiveButtonId != -1) {
			builder.setPositiveButton(positiveButtonId, dialogOnClickListener);
		}
		if (neutralButtonId != -1) {
			builder.setNeutralButton(neutralButtonId, dialogOnClickListener);
		}
		if (negativeButtonId != -1) {
			builder.setNegativeButton(negativeButtonId, dialogOnClickListener);
		}

		builder.setMessage(message);
		
		try {
			AlertDialog dialog = builder.create();
			dialog.setCancelable(false);
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
			return dialog;
		} catch (Exception e) {
			return null;
		}
	}
	
	public static ProgressBar showProgressBar(Context context) {
		ProgressBar pb = new ProgressBar(context);
		return pb;
	}
	
	
	public static ProgressDialog showHorizontalProgress(Context context, String title, String message) {
		ProgressDialog pd = new ProgressDialog(context);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setCanceledOnTouchOutside(false);
		pd.setCancelable(true);
		pd.setTitle(title);
		pd.setMessage(message);
		pd.setProgress(0);
		pd.setMax(100);
		pd.show();
		return pd;
	}
	
//	/**
//	 * @param context
//	 * @param title
//	 * @param message
//	 * @return
//	 */
//	public static RelativeLayout showCircleProgress(Context context) {
//		RelativeLayout rl = (RelativeLayout) View.inflate(context, R.layout.progress_dialog_layout, null);
//		return rl;
//	}
}
