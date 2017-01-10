package cn.kuwo.sing.tv.utils;

import java.io.File;
import java.io.InputStream;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.kuwo.framework.context.AppContext;
import cn.kuwo.framework.log.KuwoLog;
import cn.kuwo.sing.tv.R;
import cn.kuwo.sing.tv.context.AppKey;
import cn.kuwo.sing.tv.context.Constants;

import com.aispeech.AIEngine;
import com.aispeech.AIEngineConfig;
import com.aispeech.AIError;
import com.aispeech.AIResult;
import com.aispeech.common.Util;
import com.aispeech.param.ASRParams;
import com.aispeech.speech.BaseSpeechListener;
import com.aispeech.speech.SpeechEngine;
import com.aispeech.speech.SpeechReadyInfo;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;

public class ASRDialog extends Dialog implements android.view.View.OnClickListener {
	private static final String LOG_TAG = "ASRDialog";
	private Context mContext;
    private SpeechEngine engine;
    private ASRParams params;
    private TextView tvInfo;
    private TextView tvResult;
    private File audioPath;
    private Button btASRDialogOk;
    private Button btASRDialogRetryLeft;
    private RatingBar pbASRWav;
	private Button btASRDialogRetryRight;
	private Button btASRDialogCancel;
	private ImageView ivMic;
	private String queryResult;

	public ASRDialog(Context context) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
		mContext = context;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.asr_dialog);
		
		initView();
		
		audioPath = Util.getAvaiableAppDataDirPerInternal(this.getContext(), "audio");
	    // 设置云端识别引擎配置
	    AIEngineConfig config = new AIEngineConfig(this.getContext(),true);
	    // 创建语音识别引擎，设置语音识别回调
	    engine = SpeechEngine.createEngine(new AISpeechListenerImpl(), config);
	    params = new ASRParams();
	    // 语音识别云服务需要设置app信息
	    params.setAppKey(AppKey.APPKEY);
	    params.setSecretKey(AppKey.SECRETKEY);
	    // 指定语音云端识别资源名
	    params.setRes("assist");
	    params.setAttachUrl(ASRParams.INCLUDE_ATTACH_URL);
	    new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				startRecord();
			}
		}, 1000);
		
	}
	
	private void initView() {
		ivMic = (ImageView)findViewById(R.id.ivMic);
		tvInfo = (TextView)findViewById(R.id.tvASRInfo);
		tvResult = (TextView)findViewById(R.id.tvASRResult);
		btASRDialogOk = (Button) findViewById(R.id.btASRDialogOk);
		btASRDialogOk.setOnClickListener(this);
		btASRDialogRetryLeft = (Button) findViewById(R.id.btASRDialogRetryLeft);
		btASRDialogRetryLeft.setOnClickListener(this);
		btASRDialogRetryRight = (Button)findViewById(R.id.btASRDialogRetryRight);
		btASRDialogRetryRight.setOnClickListener(this);
	    btASRDialogCancel = (Button)findViewById(R.id.btASRDialogCancel);
	    btASRDialogCancel.setOnClickListener(this);
	    pbASRWav = (RatingBar) findViewById(R.id.pbASRWav);
	    
	    ivMic.setVisibility(View.INVISIBLE);
	    tvInfo.setVisibility(View.INVISIBLE);
	    tvResult.setVisibility(View.INVISIBLE);
	    pbASRWav.setVisibility(View.INVISIBLE);
	    hideAllButtons();
	}
	
	private void hideAllButtons() {
		 btASRDialogOk.setVisibility(View.INVISIBLE);
		 btASRDialogRetryLeft.setVisibility(View.INVISIBLE);
		 btASRDialogRetryRight.setVisibility(View.INVISIBLE);
		 btASRDialogCancel.setVisibility(View.INVISIBLE);
	}
	
	private void showReadyForSpeen() {
		tvInfo.setVisibility(View.VISIBLE);
		tvInfo.setText("请说话");
    	ivMic.setVisibility(View.VISIBLE);
    	ivMic.setImageResource(R.drawable.voice_say);
    	pbASRWav.setVisibility(View.VISIBLE);
    	tvResult.setVisibility(View.INVISIBLE);
    	hideAllButtons();
    	btASRDialogOk.setVisibility(View.VISIBLE);
    	btASRDialogOk.setText("说完了");
    	btASRDialogOk.requestFocus();
    	btASRDialogCancel.setVisibility(View.VISIBLE);
	}

	 private class AISpeechListenerImpl extends BaseSpeechListener {
		 
			public void onReadyForSpeech(SpeechReadyInfo params) {
				showReadyForSpeen();
	        }

	        public void onBeginningOfSpeech() {
	        	hideAllButtons();
	        	ivMic.setImageResource(R.drawable.voice_say);
	    	    tvInfo.setVisibility(View.VISIBLE);
	    	    pbASRWav.setVisibility(View.VISIBLE);
	        	tvInfo.setText("正在识别");
	        }

	        public void onEndOfSpeech() {
	        	pbASRWav.setVisibility(View.INVISIBLE);
	        }

	        public void onError(AIError error) {
	        	String errorStr = error.getError();
	        	KuwoLog.e(LOG_TAG, "未检测到输入");
	        	tvInfo.setVisibility(View.VISIBLE);
	        	if(errorStr.equals("network error")) 
	        		errorStr = "网络连接错误";
	        	tvInfo.setText("识别失败");
	        	tvResult.setVisibility(View.VISIBLE);
	        	tvResult.setText(errorStr);
	        	
	        	MobclickAgent.onEvent(mContext, Constants.KS_UMENG_VOICE_REC, Constants.KS_UMENG_FAIL);
	        	//重试
	        	ivMic.setImageResource(R.drawable.voice_error);
	        	hideAllButtons();
	        	pbASRWav.setVisibility(View.INVISIBLE);
	        	btASRDialogCancel.setVisibility(View.VISIBLE);
	        	btASRDialogRetryLeft.setVisibility(View.VISIBLE);
	        	btASRDialogRetryLeft.requestFocus();
	        }

	        public void onResults(AIResult results) {
	            if (results.isLast()) {
	                if (results.getResultClass() == String.class) {
	                	String resultContent = results.getResultObject().toString();
	                	KuwoLog.d(LOG_TAG, resultContent);
	                	try {
							JSONObject jsonObj = new JSONObject(resultContent);
							boolean resultExist = jsonObj.isNull("result");
							if(!resultExist) {
								JSONObject resultJsonObj = jsonObj.getJSONObject("result");
								KuwoLog.e(LOG_TAG, "识别成功！");
								queryResult = resultJsonObj.getString("rec");
								if(queryResult.length() == 0) {
									tvInfo.setVisibility(View.VISIBLE);
									tvInfo.setText("对不起，没听懂你的意思哦");
									tvResult.setVisibility(View.VISIBLE);
									tvResult.setText("请重试一次");
									MobclickAgent.onEvent(mContext, Constants.KS_UMENG_VOICE_REC, Constants.KS_UMENG_FAIL);
									//重试
						        	ivMic.setImageResource(R.drawable.voice_error);
						        	hideAllButtons();
						        	pbASRWav.setVisibility(View.INVISIBLE);
						        	btASRDialogCancel.setVisibility(View.VISIBLE);
						        	btASRDialogRetryLeft.setVisibility(View.VISIBLE);
						        	btASRDialogRetryLeft.requestFocus();
									return;
								}else {
									tvInfo.setVisibility(View.VISIBLE);
									tvInfo.setText("识别成功");
									ivMic.setImageResource(R.drawable.voice_say);
									pbASRWav.setVisibility(View.INVISIBLE);
									tvResult.setVisibility(View.VISIBLE);
									tvResult.setText("您是否要搜索\""+queryResult+"\"");
									hideAllButtons();
									btASRDialogOk.setVisibility(View.VISIBLE);
									btASRDialogOk.setText("确认");
									btASRDialogRetryRight.setVisibility(View.VISIBLE);
								}
							}
						} catch (JSONException e) {
							KuwoLog.e(LOG_TAG, e.toString());
							KuwoLog.printStackTrace(e);
						}
	                }
	                
	            }
	        }
	        
	        public void onRmsChanged(float progress) {
	        	float rating = progress*10;
	        	pbASRWav.setRating(rating);
	        	return;
	        }
	        
	        @Override
	        public void onInit(int status) {
	            if (status == AIEngine.OPT_SUCCESS) {
	            	KuwoLog.d(LOG_TAG, "初始化成功");
	            } else {
	            	tvInfo.setVisibility(View.VISIBLE);
	            	tvInfo.setText("初始化失败,code:" + status);
	            }
	        }
	       
	    }

	 public void startRecord() {
		 
     	hideAllButtons();
     	ivMic.setImageResource(R.drawable.voice_say);
 	    tvInfo.setVisibility(View.VISIBLE);
 	    pbASRWav.setVisibility(View.VISIBLE);
 	    tvResult.setVisibility(View.INVISIBLE);
     	tvInfo.setText("请说话");
     	
     	btASRDialogOk.setVisibility(View.VISIBLE);
    	btASRDialogOk.setText("说完了");
    	btASRDialogOk.requestFocus();
    	btASRDialogCancel.setVisibility(View.VISIBLE);
		 
         params.setAudioFilePath(new File(audioPath, System.currentTimeMillis() + ".wav").getPath());
         engine.start(params);
	 }

	 public void stopRecord() {
		 engine.stop();
	 }
	 
	 /* (non-Javadoc)
	 * @see android.app.Dialog#onStop()
	 */
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	 
	 @Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		try {
			switch (v.getId()) {
			case R.id.btASRDialogOk:
				if(btASRDialogOk.getText().equals("说完了")) {
					if(TextUtils.isEmpty(queryResult)) {
						tvInfo.setVisibility(View.VISIBLE);
			        	tvInfo.setText("识别失败");
			        	tvResult.setVisibility(View.VISIBLE);
			        	tvResult.setText("没有检测到语音");
			        	MobclickAgent.onEvent(mContext, Constants.KS_UMENG_VOICE_REC, Constants.KS_UMENG_FAIL);
			        	//重试
			        	ivMic.setImageResource(R.drawable.voice_error);
			        	hideAllButtons();
			        	pbASRWav.setVisibility(View.INVISIBLE);
			        	btASRDialogCancel.setVisibility(View.VISIBLE);
			        	btASRDialogRetryLeft.setVisibility(View.VISIBLE);
			        	btASRDialogRetryLeft.requestFocus();
					}
					break;
				}
				Message msg = new Message();
				msg.what = Constants.MSG_VOICE_QUERY_SUCCESS;
				msg.obj = queryResult;
				EventBus.getDefault().post(msg);
				MobclickAgent.onEvent(mContext, Constants.KS_UMENG_VOICE_REC, Constants.KS_UMENG_SUCCESS);
				dismiss();
				break;
			case R.id.btASRDialogRetryLeft:
				startRecord();
				break;
			case R.id.btASRDialogRetryRight:
				startRecord();
				break;
			case R.id.btASRDialogCancel:
				dismiss();
				break;	
			}
		} catch (Exception e) {
			KuwoLog.e(LOG_TAG, e);
		}
	 }
}
