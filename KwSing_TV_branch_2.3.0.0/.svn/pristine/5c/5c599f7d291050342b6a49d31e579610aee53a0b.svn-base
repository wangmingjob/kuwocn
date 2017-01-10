package cn.kuwo.sing.util.lyric;

import java.util.List;

public class Lyric {
	public final static int LYRIC_TYPE_ERROR = -1;
	public final static int LYRIC_TYPE_LRC = 0;
	public final static int LYRIC_TYPE_LRCX = 1;
	public final static int LYRIC_TYPE_KDTX = 2;
	
	private List<Sentence> mSentences = null;  //句子
	private int mTotalTimespan = -1;                 
	private int mType = LYRIC_TYPE_LRC;
	private double mEnvelope;   //包络最大幅度值

	
	public void setSentences(List<Sentence> mSentences) {
		this.mSentences = mSentences;
	}
	public List<Sentence> getSentences() {
		return mSentences;
	}
	public void setTotalTimespan(int mTotalTimespan) {
		this.mTotalTimespan = mTotalTimespan;
	}
	public int getTotalTimespan() {
		return mTotalTimespan;
	}
	public int getType() {
		return mType;
	}
	public void setType(int mType) {
		this.mType = mType;
	}
	public double getEnvelope() {
		return mEnvelope;
	}

	public void setEnvelope(double mEnvelope) {
		this.mEnvelope = mEnvelope;
	}

}
