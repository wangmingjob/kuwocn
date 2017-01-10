package cn.kuwo.sing.tv.logic;

import java.util.Collection;
import java.util.Iterator;

import cn.kuwo.framework.log.KuwoLog;
import cn.kuwo.sing.util.lyric.Lyric;
import cn.kuwo.sing.util.lyric.Sentence;
import cn.kuwo.sing.util.lyric.Word;

public class LyricLogic {

	private static final String TAG = "LyricLogic";
	private Lyric lyric;
	private Sentence lastSen = null;
	private Word lastWord = null;
	
	public interface LyricListener {
		void onWordIndexChanged(Word word);
		void onSentenceIndexChanged(Sentence sentence);
		void onSentenceStart(Sentence sentence);
		void onSentenceEnd(Sentence sentence);
	}
	private LyricListener mLyricListener;
	public void setLyricListener(LyricListener listener) {
		mLyricListener = listener;
	}
	
	public void setLyric(Lyric lyric){
		this.lyric = lyric;
	}
	
	public Word getCurrentWord() {
		return lastWord;
	}

	public void setPosition(long position) {
		Sentence sentence = null;
		if(lyric != null)
			 sentence = findSentence(position, lyric, LyricLogic.SENTENCE_RESULT_NOW);
		if (sentence == null){
			if(lastSen != null) {
				onSentenceEnd(lastSen);
				lastSen = null;
			}
			return;
		}else if (lastSen!=null&&sentence!=lastSen) {
//			当前一句与后一句有叠加时,每当前一句唱完就去更新分数
			if(position>(lastSen.getTimespan()+lastSen.getTimestamp())){
				onSentenceEnd(lastSen);
				lastSen = null;
			}
			return;
		}

		if (sentence != lastSen) {
			// 触发事件
			onSentenceStart(sentence);
			onSentenceIndexChanged(sentence);
		}

		Word word = findWord(position, sentence);
		if (word!=lastWord) {
			onWordIndexChanged(word);
		}
	}
	
	private void onWordIndexChanged(Word word){   //该切换到word字
		lastWord = word;
		if (mLyricListener != null)
			mLyricListener.onWordIndexChanged(word);
	}
	
	private void onSentenceIndexChanged(Sentence sentence){
		KuwoLog.v(TAG, "onSentenceIndexChanged");
		lastSen = sentence;
		
		if (mLyricListener != null)
			mLyricListener.onSentenceIndexChanged(sentence);
	}

	private void onSentenceStart(Sentence sentence){
		if (mLyricListener != null)
			mLyricListener.onSentenceStart(sentence);
	}

	private void onSentenceEnd(Sentence sentence){
		if (mLyricListener != null)
			mLyricListener.onSentenceEnd(sentence);
	}
	
	
	public Word findWord(long position, Sentence sentence){
		Collection<Word> words = sentence.getWords();
		Word currentword = null,lastword= null;
		for(Iterator<Word> it = words.iterator(); it.hasNext(); ) {
			currentword = (Word) it.next();
			if(position < currentword.getTimestamp())
				break;
			lastword = currentword;
		}
		
		if (lastword == null)
			return null;
		
		return lastword;
	}
	
	
	public static final int SENTENCE_RESULT_NEXT = 1;
	public static final int SENTENCE_RESULT_NOW = 0;
	public static final int SENTENCE_RESULT_PREVIOUS = -1;
	
	public Sentence findSentence(long position, Lyric lyric, int sentenceResultType){   //当前时间为两句之间时，sentenceResultType决定其返回值 ,<0 返回上一句，=0返回null, >0返回下一句
//		当歌词上一句与下一句叠加时，返回的是下一句
		Collection<Sentence> sentences = lyric.getSentences();
		Sentence currentsentence,lastsentence = null;
		
		for(Iterator<Sentence> it = sentences.iterator(); it.hasNext(); ) {
			currentsentence = (Sentence) it.next();
			if(position < currentsentence.getTimestamp()){
				if (lastsentence == null)
					return sentences.iterator().next();
				
				else if(position > lastsentence.getTimestamp()+lastsentence.getTimespan()){
					
					if(sentenceResultType < 0)
					   return lastsentence;
					else if(sentenceResultType == 0)
						return null;
					else if( sentenceResultType > 0)
						return currentsentence;
				}
				else{
					return lastsentence;
				}
			}
			
			lastsentence = currentsentence;
		}
		
		return lastsentence;
	}
}
