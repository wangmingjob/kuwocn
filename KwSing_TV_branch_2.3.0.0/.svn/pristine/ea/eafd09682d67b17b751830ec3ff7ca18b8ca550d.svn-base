/**
 * Copyright (c) 2005, Kuwo, Inc. All rights reserved. 
 */
package cn.kuwo.sing.tv.controller;


import io.vov.vitamio.widget.VideoView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import cn.kuwo.framework.log.KuwoLog;
import cn.kuwo.framework.thread.LoopHandler;
import cn.kuwo.framework.utils.IOUtils;
import cn.kuwo.sing.logic.AudioLogic;
import cn.kuwo.sing.tv.R;
import cn.kuwo.sing.tv.bean.Mtv;
import cn.kuwo.sing.tv.context.Constants;
import cn.kuwo.sing.tv.context.Decoders;
import cn.kuwo.sing.tv.logic.DataHandler;
import cn.kuwo.sing.tv.logic.ListLogic;
import cn.kuwo.sing.tv.logic.LyricLogic;
import cn.kuwo.sing.tv.logic.LyricLogic.LyricListener;
import cn.kuwo.sing.tv.view.widget.SysVideoView;
import cn.kuwo.sing.tv.view.widget.WaveView;
import cn.kuwo.sing.util.lyric.Lyric;
import cn.kuwo.sing.util.lyric.Sentence;
import cn.kuwo.sing.util.lyric.Word;
import de.greenrobot.event.EventBus;

public class LyricController extends BaseController {

	private final String TAG = "LyricController";
	private AudioLogic lgcAudio;
	private LyricLogic lgcLyric;

	private WaveView wvSingWaveView;
	private VideoView mVitamioVideoView;
	private SysVideoView mSysVideoView;
	private ImageView ivSingMenuScoreMtv;
	private ImageView ivSingPerfect;
	private ViewGroup llSingScore;
	private TextView tvSingSingleScore;
	private TextView tvSingTotalScore;
	
	private static Handler handler = new Handler();
	private int total = 0;
	private TextView tvSingControllerLyric;
	private TextView tvSingSingleScoreMessage;	
	private boolean mbUseSystemDecoder = false;
	
	public LyricController(Activity activity) {
		super(activity);
		KuwoLog.d(TAG, "LyricController");
		EventBus.getDefault().register(this);
		
		mbUseSystemDecoder = Decoders.isUseSysDecoder();
		
		wvSingWaveView = (WaveView) activity.findViewById(R.id.wvSingWaveView);
		if (mbUseSystemDecoder){
			mSysVideoView = (SysVideoView) activity.findViewById(R.id.syssurface);
		}else{
			mVitamioVideoView = (VideoView) activity.findViewById(R.id.surface);
		}
		ivSingMenuScoreMtv = (ImageView) activity.findViewById(R.id.ivSingMenuScoreMtv);
		llSingScore = (ViewGroup)activity.findViewById(R.id.llSingScore);
		tvSingSingleScore = (TextView) activity.findViewById(R.id.tvSingSingleScore);
		tvSingTotalScore = (TextView) activity.findViewById(R.id.tvSingTotalScore);
		tvSingSingleScoreMessage = (TextView)activity.findViewById(R.id.tvSingSingleScoreMessage);
		
		ivSingPerfect = (ImageView) activity.findViewById(R.id.ivSingPerfect);
		ivSingPerfect.setBackgroundResource(R.anim.sing_perfect);
		
		lgcAudio = new AudioLogic();
		lgcLyric = new LyricLogic();
		
		Intent data = activity.getIntent();
		Mtv mtv = (Mtv) data.getSerializableExtra("mtv");
		if(mtv == null)
			return;
		String rid = mtv.rid;
		if(TextUtils.isEmpty(rid))  {
			tvSingControllerLyric.setVisibility(View.GONE);
			return;
		}
	}
	
	public void onEvent(Message msg) {
		switch (msg.what) {
		case Constants.MSG_OPEN_SONG:
			Mtv mtv = (Mtv) msg.obj;
			if(mtv != null)
				getMtvLyric(mtv.rid);
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
	
	public void getMtvLyric(String rid) {
		ListLogic lgcList = new ListLogic();
		lgcList.getLyricSong(rid, new DataHandler<Lyric>() {
			
			@Override
			public void onSuccess(Lyric lyric) {
				if(lyric != null) {
					wvSingWaveView.setLyric(lyric);
					// 初始化打分
					lgcAudio.scoreInit(Constants.RECORDER_SAMPLE_RATE_44100, Constants.RECORDER_CHANNEL_COUNT, lyric.getEnvelope());
					lgcLyric.setLyric(lyric);
					lgcLyric.setLyricListener(mLyricListener);
					ivSingMenuScoreMtv.setEnabled(true);
					ivSingMenuScoreMtv.setImageResource(R.drawable.score_mtv_selector);
				}else {
					llSingScore.setVisibility(View.GONE);
				}
			} 
			
			@Override
			public void onFailure() {
				ivSingMenuScoreMtv.setEnabled(false);
				ivSingMenuScoreMtv.setImageResource(R.drawable.score_mtv_disable);
				llSingScore.setVisibility(View.GONE);
//				super.onFailure();
			}
			
			@Override
			public void onFailure(Throwable error, String content) {
				ivSingMenuScoreMtv.setEnabled(false);
				ivSingMenuScoreMtv.setImageResource(R.drawable.score_mtv_disable);
				llSingScore.setVisibility(View.GONE);
//				super.onFailure(error, content);
			}
			
			@Override
			public void onFailure(Throwable error) {
				// 下载歌词失败
				ivSingMenuScoreMtv.setEnabled(false);
				ivSingMenuScoreMtv.setImageResource(R.drawable.score_mtv_disable);
				llSingScore.setVisibility(View.GONE);
			}
		});
	}
	
	public void ProcessWaveDate(byte[] data) {
		short[] wav = IOUtils.convertToShortArray(data, 0, data.length);
		lgcAudio.scoreOnWavComing(wav);
		wav = null;
		final double envelope = lgcAudio.computeSingle(data);

		handler.post(new Runnable() {
			
			@Override
			public void run() {
				// 打分
				int color = WaveView.WAVE_GREEN;
				Word lastWord = lgcLyric.getCurrentWord();
				if(lastWord != null)
					color = judgeArrowColor(envelope, lastWord.getEnvelope());
				//获取麦克风音量，如果为0，则waveview的箭头值为0
				SharedPreferences sp = activity.getSharedPreferences("kuwoTV", Context.MODE_PRIVATE);
				float microphoneVolume = sp.getFloat("microphoneVolume", 0.8f);
				if(microphoneVolume == 0.0f)
					wvSingWaveView.setArrowValue(0);
				else 
					wvSingWaveView.setArrowValue((int)envelope);
				wvSingWaveView.setArrowColor(color);
			}
		});

	}	
	
    private int judgeArrowColor(double envelope, double standard){
    	double x = Math.abs(envelope - standard);
    	int color = WaveView.WAVE_GREEN;
    	if(x > 60)
    		color = WaveView.WAVE_RED;
    	else if(x <= 60 && x >= 30)
    		color = WaveView.WAVE_YELLOW;
    	else if(x < 30)
    		color = WaveView.WAVE_GREEN;
    	return color;
    }
	
	@Override
	public void onStart() {
		mLoopHandler.start(mUpdateRunnable);
		super.onStart();
		KuwoLog.d(TAG, "onStart");
	}
	
	@Override
	public void onStop() {
		mLoopHandler.stop();
		super.onStop();
		KuwoLog.d(TAG, "onStop");
	}	
	
	private LoopHandler mLoopHandler = new LoopHandler();
	private Runnable mUpdateRunnable = new Runnable() {
		@Override
		public void run() {
			long position = 0;
			if (mbUseSystemDecoder){
				if (mSysVideoView != null)
					position = mSysVideoView.getCurrentPosition();
			}else{
				if (mVitamioVideoView != null)
					position = mVitamioVideoView.getCurrentPosition();
			}
			wvSingWaveView.setPosition(position);
			lgcLyric.setPosition(position);				
		}
	};
	
	private void showSingPerfectAnimation() {
		ivSingPerfect.setVisibility(View.VISIBLE);
		AnimationDrawable perfectDrawable = (AnimationDrawable) ivSingPerfect.getBackground();
		if(perfectDrawable.isRunning()) {
			perfectDrawable.stop();
		}else {
			perfectDrawable.start();
		}
	}
	
	
	
	private LyricListener mLyricListener = new LyricListener() {
		
		@Override
		public void onWordIndexChanged(Word word) {
		}
		
		@Override
		public void onSentenceStart(Sentence sentence) {
			KuwoLog.v(TAG, "onSentenceStart");
			lgcAudio.scoreStart(sentence.getSpectrum(), sentence.getEnvelopes());
		}
		
		@Override
		public void onSentenceIndexChanged(Sentence sentence) {
		}
		
		@Override
		public void onSentenceEnd(Sentence sentence) {
			KuwoLog.v(TAG, "onSentenceEnd");
			handler.post(new Runnable() {
				
				@Override
				public void run() {
//						TODO 每句打分没有调用JNI接口
//						int score = lgcAudio.scoreEnd();
					int score = lgcAudio.computeMean();
					tvSingSingleScore.setText(String.valueOf(score));
					if(score >= 85){
//						showSingPerfectAnimation();
						tvSingSingleScoreMessage.setText("(太棒了！)");
					}else if(score>=70){
						tvSingSingleScoreMessage.setText("(看好你哦!)");
					}else if(score>=60){
						tvSingSingleScoreMessage.setText("(加油加油！)");
					}else if(score>=30){
						tvSingSingleScoreMessage.setText("(要努力呀！)");
					}else{
						tvSingSingleScoreMessage.setText("(雷死了!)");
					}
					total = lgcAudio.computTotal();
					tvSingTotalScore.setText(String.valueOf(total));
				}
			});
		}
	};
}
