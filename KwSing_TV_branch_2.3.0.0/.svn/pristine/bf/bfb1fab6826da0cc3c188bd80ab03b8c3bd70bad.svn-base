package cn.kuwo.sing.tv.logic;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder.AudioSource;
import android.os.Build;
import cn.kuwo.framework.config.PreferencesManager;
import cn.kuwo.framework.context.AppContext;
import cn.kuwo.framework.log.KuwoLog;
import cn.kuwo.sing.tv.context.Constants;

import com.konka.android.tv.KKAudioManager;
import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.common.vo.EnumAudioInputLevelSourceType;

public class SingLogic {
	
	private static final String TAG = "SingLogic";
	private AudioRecord mAudioRecord;
	private AudioTrack mAudioTrack;
	private int mRecordBufSize, mPlayBufSize;
	private boolean isRecording = false;	//是否录放的标记  
	private float mVolume = 0.8f;
	private int mUserSoftPlayBack = 0;
	private int mSampleRate = Constants.RECORDER_SAMPLE_RATE_44100;
	private int mChannelConfig = Constants.RECORDER_CHANNEL_CONFIG;
	
	public SingLogic()
	{
		mUserSoftPlayBack = PreferencesManager.getInt(Constants.PLAYBACK_MODE, 0);
	}
	
	private int initRecorder() {
        mRecordBufSize = AudioRecord.getMinBufferSize(mSampleRate, mChannelConfig, Constants.RECORDER_AUDIO_FORMAT);
    	if (mRecordBufSize == AudioRecord.ERROR_BAD_VALUE) {
        	KuwoLog.e(TAG, "AudioRecord.ERROR_BAD_VALUE");
        	
        	mSampleRate = 8000;
        	
        	 mRecordBufSize = AudioRecord.getMinBufferSize(mSampleRate, mChannelConfig, Constants.RECORDER_AUDIO_FORMAT);
        	 
        	 if (mRecordBufSize == AudioRecord.ERROR_BAD_VALUE)
        		 return AudioRecord.ERROR_BAD_VALUE;
        }
        
    	if(mAudioRecord == null) {
        	mAudioRecord = new AudioRecord(AudioSource.DEFAULT, mSampleRate, mChannelConfig, Constants.RECORDER_AUDIO_FORMAT, mRecordBufSize);
        }else {
        	mAudioRecord.stop();
        	mAudioRecord.release();
        	mAudioRecord = null;
        }
        
        if(mAudioRecord == null)
        	mAudioRecord = new AudioRecord(AudioSource.DEFAULT, mSampleRate, mChannelConfig, Constants.RECORDER_AUDIO_FORMAT, mRecordBufSize);
        
        KuwoLog.d(TAG, "return:" + mAudioRecord.getState());
        if (mAudioRecord.getState() != AudioRecord.STATE_INITIALIZED) {
        	KuwoLog.e(TAG, "AudioRecord.STATE_UNINITIALIZED");
        	return AudioRecord.STATE_UNINITIALIZED;
        }
        KuwoLog.d("record", "initRecorder Successed");
        return AudioRecord.STATE_INITIALIZED;
	}
	
	private int initPlayer() {
        mPlayBufSize=AudioTrack.getMinBufferSize(mSampleRate, mChannelConfig, Constants.RECORDER_AUDIO_FORMAT); 
		
        if (mPlayBufSize == AudioTrack.ERROR_BAD_VALUE) {
        	KuwoLog.d(TAG, "AudioTrack.ERROR_BAD_VALUE");
        	return AudioTrack.ERROR_BAD_VALUE;
        }
        
        KuwoLog.d(TAG, "mPlayBufSize="+mPlayBufSize);

        mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, mSampleRate,  mChannelConfig, 
        		Constants.RECORDER_AUDIO_FORMAT,  
        		mPlayBufSize*4, AudioTrack.MODE_STREAM);
        
        if (mAudioTrack.getState() != AudioTrack.STATE_INITIALIZED) {
        	KuwoLog.d(TAG, "mAudioTrack.STATE_UNINITIALIZED");
        	return AudioTrack.STATE_UNINITIALIZED;
        }
        
        return AudioTrack.STATE_INITIALIZED;
	}
	
	public void start() {	
		if (initRecorder() != AudioRecord.STATE_INITIALIZED) {
			return;
		}		
		if(mUserSoftPlayBack == Constants.PLAYBACK_MODE_SOFTWARE)
			initPlayer();
		
		isRecording = true;
		new RecordPlayThread().start();
	}
		
	public float getVolume () {
		return mVolume;
	}
	
	public void exit(){
		isRecording = false;
	}
	
	public void setKtvVolume4Konka(float volume) {
		try {
			KKAudioManager.getInstance(AppContext.context).setKTVSoundVolume((short)(volume*100)); //伴奏
		} catch (Exception e) {
			KuwoLog.e(TAG, "KKAudioManager.getInstance(AppContext.context).setMicVolume mic音量设置失败");
		}
	}
	
	public void setVolume(Activity activity, float volume) {
		mVolume = volume;
		
		if (mUserSoftPlayBack == Constants.PLAYBACK_MODE_SOFTWARE && mAudioTrack != null){
			mAudioTrack.setStereoVolume(mVolume, mVolume);
			return;
		}else {
			if(Build.BRAND.equals("Tcl") || Build.BRAND.equals("SkyWorth") || Build.BRAND.equalsIgnoreCase("changhong") ||Build.BRAND.equals("Hisense")) {
				try {
					if (TvManager.getInstance() != null)
						TvManager.getInstance().getAudioManager().setInputLevel(EnumAudioInputLevelSourceType.E_VOL_SOURCE_PREMIXER_KTV_MIC_IN, (short) (volume * 100));
					return;
				} catch (Exception e) {
					KuwoLog.e(TAG, "TvManager.getInstance().getAudioManager().setInputLevel 音频输入初始化失败");
				}
			}
			
			if(Build.BRAND.equals("Konka")){
				try {
					KKAudioManager.getInstance(AppContext.context).setMicVolume((short)(volume*100)); //麦克风
				} catch (Exception e) {
					KuwoLog.e(TAG, "KKAudioManager.getInstance(AppContext.context).setMicVolume mic音量设置失败");
				}
			}
			
			if(Build.HOST.equalsIgnoreCase("letv")) {
				AudioManager am = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
				am.setParameters("karaoke_mic_vol="+mVolume*100);
			}
		}
	}
	
    private class RecordPlayThread extends Thread {  
        public void run() {  
            try {  
            	if (mRecordBufSize < 0)
            		return;
                byte[] buffer = new byte[mRecordBufSize];  
                mAudioRecord.startRecording();	// 开始录制  
                
                if(mUserSoftPlayBack == 1)
                	mAudioTrack.play();	// 开始播放  
                
                KuwoLog.d("record", "begin");
                while (isRecording) {  
                    // 从MIC保存数据到缓冲区  
                    int bufferReadResult = mAudioRecord.read(buffer, 0, mRecordBufSize);  
                    
                    if(mUserSoftPlayBack == Constants.PLAYBACK_MODE_SOFTWARE)
                    {
                    	mAudioTrack.write(buffer, 0, bufferReadResult);
                    	mAudioTrack.flush();
                    }
                    
                    // 回调
                    onDataRecord(buffer);
                } 
                
                KuwoLog.d("record", "end");
                if(mUserSoftPlayBack == Constants.PLAYBACK_MODE_SOFTWARE){
                	mAudioTrack.stop();  
                	mAudioTrack.release();
                	mAudioTrack = null;
                }
                mAudioRecord.stop();
                mAudioRecord.release();
                mAudioRecord = null;
                
            } catch (Exception t) {  
            }  
        }  
    }; 
    
    public interface OnAudioRecordListener {
    	void onDataRecord(byte[] data);
    }
    private OnAudioRecordListener mOnAudioRecordListener;
    public void setOnAudioRecordListener(OnAudioRecordListener listener) {
    	mOnAudioRecordListener = listener;
    }
    protected void onDataRecord(byte[] data) {
    	if (mOnAudioRecordListener != null)
    		mOnAudioRecordListener.onDataRecord(data);
    }
}
