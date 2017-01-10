package cn.kuwo.sing.util.lyric;

import java.util.Collection;

public class Sentence {
	private String mContent = "";  //每句歌词内容
	private int mIndex;           //索引
	private Collection<Word> mWords;  //单字
	private long mTimestamp;           //本句开始时间
	private int mTimespan;              //本句时间长度
	private double[] mSpectrum;        //频谱
	private double[] mEnvelopes;       //每句包络值

	public Collection<Word> getWords() {
		return mWords;
	}

	public void setWords(Collection<Word> value) {
		mWords = value;
	}

	public String getContent() {
		return mContent;
	}

	public void setContent(String value) {
		mContent = value;
	}

	public int getIndex() {
		return mIndex;
	}

	public void setIndex(int value) {
		mIndex = value;
	}

	public long getTimestamp() {
		return mTimestamp;
	}

	public void setTimestamp(Long value) {
		mTimestamp = value;
	}

	public int getTimespan() {
		return mTimespan;
	}

	public void setTimespan(int timespan) {
		mTimespan = timespan;
	}

	public double[] getSpectrum() {
		return mSpectrum;
	}

	public void setSpectrum(double[] mSpectrum) {
		this.mSpectrum = mSpectrum;
	}
	
	public double[] getEnvelopes() {
		return mEnvelopes;
	}

	public void setEnvelopes(double []mEnvelopes) {
		this.mEnvelopes = mEnvelopes;
	}
}
