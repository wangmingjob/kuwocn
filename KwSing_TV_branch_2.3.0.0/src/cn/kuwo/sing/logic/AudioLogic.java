package cn.kuwo.sing.logic;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import cn.kuwo.framework.log.KuwoLog;
import cn.kuwo.framework.utils.IOUtils;

public class AudioLogic {
	private final static String TAG = "AudioLogic";
    
    private final int QUEUE_SIZE = 5;
	private final Queue <Double> queue = new LinkedBlockingQueue <Double> (QUEUE_SIZE);
	
   // 计算每次的值
    public double computeSingle(byte[] bytes){
//    	KuwoLog.i(TAG, bytes[0] + " " + bytes[1] + " " + bytes[2] + " " + bytes[3] + " " + bytes[4] + " " + bytes[5] + " " + bytes[6] + " " + bytes[7]);
    	short[] data = IOUtils.convertToShortArray(bytes, 0, bytes.length);
    		
    	int lenth = data.length/2;
    	double temp_env = 0;
    	for(int i = 0; i < lenth; i +=2){
    		double frame_amp = 0;
    		for(int j = i; j < i+2; ++j){
    			frame_amp += data[i];
    		}
    		frame_amp /=2;
    		temp_env += frame_amp * frame_amp;
    	}
    	temp_env /= (lenth/2);
    	temp_env = ((temp_env > 1) ? Math.log10(temp_env) : 0.0) / 9;
    	temp_env = (temp_env < 0.5) ? 0.0 : ((temp_env - 0.5) * 2);
    	temp_env *= 100;
    	if(queue.size() >= QUEUE_SIZE){
    		queue.poll();
    	}
    	queue.offer(temp_env);
    	
    	return temp_env;
    }
    
    // 计算队列里的平均值(每句的值）
    public int computeMean(){
    	int ret = 0;

    	synchronized (queue) {
    		while(queue.size() > QUEUE_SIZE)
    			queue.poll();
    		
    		while(!queue.isEmpty()){
    			ret += queue.poll();
    			KuwoLog.d(TAG, "取值" + ret);
    		}
		} 
    	
    	int ave = ret / QUEUE_SIZE;
    	total = total + ave;
    	sentenceNum++;
    	return ave;
    }
    
    private int total;
    private int sentenceNum=0;
    
    public int computTotal() {
    	if(sentenceNum == 0){
    		return 0;
    	}
		return total/sentenceNum;
	}
	
    private static boolean libLoaded = false;
	static {
		if (!libLoaded) {
			try {
//				System.loadLibrary("kwrev");
				System.loadLibrary("kwscore");
			} catch (UnsatisfiedLinkError  e) {
				e.printStackTrace();
			}
			libLoaded = true;
		}
	}

	public native void revInit();

	/**
	 * 对一段wav添加混音效果，输出的wav数据也放在samples 指向的buffer里。
	 * 
	 * @param samp_freq
	 *            采样率
	 * @param sf
	 *            PCM类型，传固定值 1
	 * @param nchannels
	 *            声道数
	 * @param samples
	 *            wav数组
	 * @param samplesSize
	 *            wav数据 short值的个数
	 */
	public native void revProcess(int samp_freq,  int nchannels, byte[] samples);
	
	
	//public void revProcess(int samp_freq, int nchannels, byte[] samples){
	//	revProcess(samp_freq, 1, nchannels, samples, samples.length/2);
	//}

	/**
	 * 设置混音效果
	 * 
	 * @param rSize
	 *            房间大小
	 */
	public native void revSet(int rSize);

	/**
	 * 析构全局变量
	 */
	public native void revRelease();
	

	private boolean hasStarted = false;
	/************************************************************************/
	/* 
	 初始化Score对象时调用
	prarm:
		nRecWavSampleRate:录制音频的采样率
		nChannel：录制音频的声道数
		dEnvMax: 原唱包络归一化所用的值
	*/
	/************************************************************************/
	public native void scoreInit(int nRecWavSampleRate, int nChannel, double dbEnvRate);

	/************************************************************************/
	/*
	说明：有新的wav数据到达时调用
	param：
		pWavData:数据
		nLen:short数组长度
	有新的自唱wav数据到达，要求参数：44100hz，2 channel， 16位采样位数
	44.1K采样，双声道，16位采样深度，1秒钟数据=44100*2*sizeof(short);
	*/
	/************************************************************************/
	
	private native void scoreOnWavNewDataComing(short[] pWavData);
	public void scoreOnWavComing(short[] pWavData){
		if (!hasStarted)
			return;
//		KuwoLog.i(TAG, "scoreOnWavNewDataComing length:" + pWavData.length);
		scoreOnWavNewDataComing(pWavData);
	}
	
	/************************************************************************/
	/*
	说明：一句歌开始唱的时候调用
	param：
		pdbSpec:该句频谱数组
		pdbEnv：该句包络数组
		nSpecLen:数组长度
		nEnvLen: 数组长度
	*/
	/************************************************************************/
	
	private native void scoreSentenceStart(double[] pdbSpec, double[] pdbEnv);
	public void scoreStart(double[] pdbSpec, double[] pdbEnv){
		KuwoLog.v(TAG, "scoreStart pdbSpec length:" + pdbSpec.length +"    pdbEnv length:" + pdbEnv.length);
		hasStarted = true;
		scoreSentenceStart(pdbSpec, pdbEnv);
	}

	/************************************************************************/
	/*
	说明：一句歌结束的时候调用
	返回值：
		得分
	*/
	/************************************************************************/
	private native int scoreSentenceEnd();
	public int scoreEnd(){
		hasStarted = false;
		return scoreSentenceEnd();
	}
	
	public native int pcmResampleProcess(short[] pSrcBuffer, int nSrcBufferLen, byte[] pDesBuffer, int n_src_sample_rate, int n_dest_sample_rate);
//	JNIEXPORT jint JNICALL Java_cn_kuwo_sing_logic_AudioLogic_pcmResampleProcess
//	  (JNIEnv * env, jobject job,
//			  jshortArray pSrcBuffer, int nSrcBufferLen,
//			  jshortArray pDesBuffer,
//			  jint n_src_sample_rate, jint n_dest_sample_rate);
	
}
