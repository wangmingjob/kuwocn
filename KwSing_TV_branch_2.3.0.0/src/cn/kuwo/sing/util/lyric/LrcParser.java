package cn.kuwo.sing.util.lyric;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.util.EncodingUtils;

import cn.kuwo.framework.utils.StringUtils;

import android.text.TextUtils;

public class LrcParser implements LyricParser {

	@Override
	public LyricsHeader parserHeader(byte[] data) {
		String lyrics = EncodingUtils.getString(data, "gbk");
		String ver = getValue(lyrics, "ver:");
		String ti = getValue(lyrics, "ti:");
		String ar = getValue(lyrics, "ar:");
		String al = getValue(lyrics, "al:");
		String by = getValue(lyrics, "by:");
		String kuwo = getValue(lyrics, "kuwo:");
		
		LyricsHeader header = new LyricsHeader();
		header.version = ver;
		header.title = ti;
		header.ar = ar;
		header.al = al;
		header.by = by;
		header.kuwo = kuwo;
		return header;
	}

	@Override
	public Lyric parserLyrics(LyricsHeader header, byte[] data) {
		String lrcx = EncodingUtils.getString(data, "gbk");
		Lyric lyric = new Lyric();
		lrcx += "[99:99:99]";//给歌词补位
		Pattern pattern = Pattern.compile("((\\[[0-9|\\:|\\.|\\-]*?\\])+)([\\w\\W]*?)(?=(\\[([0-9|\\:|\\.|\\-]*?)\\]))");
		Matcher matcher = pattern.matcher(lrcx);
		List<LyricLineInfo> lineInfos = new ArrayList<LyricLineInfo>();
		while(matcher.find()){
			String startTs = matcher.group(1);//TODO:
			String content =  matcher.group(3);
			String endTs =  matcher.group(4);
			List<String> timelist = getTimeList(startTs);
			for(String time: timelist){
				LyricLineInfo lineInfo = new LyricLineInfo();
				lineInfo.startTs = time;
				lineInfo.content = content;
				lineInfo.endTs = endTs;
				lineInfos.add(lineInfo);
			}
		}
		
		//按照开始时间排序
		Collections.sort(lineInfos, new Comparator<LyricLineInfo>() {
			@Override
			public int compare(LyricLineInfo o1, LyricLineInfo o2) {
				return (int) (parseTime(o1.startTs) - parseTime(o2.startTs));
			}
		});

		List<Sentence> sentences = new ArrayList<Sentence>();
		//更新终止时间并且构造句子
		for(int i=0; i< lineInfos.size(); i++){
			LyricLineInfo lineInfo = lineInfos.get(i);
			if(i < lineInfos.size() - 1)
				lineInfo.endTs = lineInfos.get(i+1).startTs;
			else
				lineInfo.endTs = "[99:99:99]";
			
//			System.out.println(lineInfo.startTs + lineInfo.content);
			
			Sentence sentence = parserLine(i, lineInfo);
			sentences.add(sentence);
		}
		
		int length = sentences.size();
		int mTotalTimespan = 0;
		if(length > 0){
			Sentence sentence = sentences.get(length - 1);
			mTotalTimespan = (int) (sentence.getTimestamp() + sentence.getTimespan());
		}
		lyric.setType(Lyric.LYRIC_TYPE_LRC);
		lyric.setSentences(sentences);
		lyric.setTotalTimespan(mTotalTimespan);
		return lyric;
	}
	
	private List<String> getTimeList(String time) {
		Pattern pattern = Pattern.compile("\\[[0-9|\\:|\\.|\\-]*?\\]");
		Matcher matcher = pattern.matcher(time);
		List<String> timslist = new ArrayList<String>();
		while(matcher.find()){
			String t = matcher.group();
			timslist.add(t);
		}
		return timslist;
	}
	
	private Sentence parserLine(int index, LyricLineInfo lineInfo) {
		String content = lineInfo.content.trim();
		if(TextUtils.isEmpty(content)){
			content = " ";
		}
		Sentence sentence = new Sentence();
		sentence.setIndex(index);
		sentence.setContent(content);
		long startTs = parseTime(lineInfo.startTs);
		long endTs = 0l;
		if("[99:99:99]".equals(lineInfo.endTs)){
			endTs = startTs + LyricParser.LAST_SENTENCE_SPAN;
		}else{
			endTs = parseTime(lineInfo.endTs);
		}
		sentence.setTimestamp(startTs);
		sentence.setTimespan((int) (endTs - startTs));
		
		char ch[] = content.toCharArray();
		long s = 0l;
		int span = sentence.getTimespan()/ch.length;
		List<Word> wordlist = new ArrayList<Word>();
		for(int i=0; i< ch.length; i++){
			Word word = new Word();
			word.setIndex(i);
			
			StringBuffer wordtxt = new StringBuffer();
			wordtxt.append(ch[i]);
			while(i + 1 < ch.length && StringUtils.isLetter(ch[i + 1]) && !Character.isSpace(ch[i + 1])){
				wordtxt.append(ch[i]);
				i++;
			}
			
			int wordlength = wordtxt.toString().toCharArray().length;
			word.setContent(wordtxt.toString());
			word.setTimestamp(s);
			word.setTimespan(span * wordlength);
			
			s += word.getTimespan();
			
			wordlist.add(word);
		}
		sentence.setWords(wordlist);
		//更新Word
		return sentence;
	}
	
	private String getValue(String txt, String prex){
		Pattern pattern = Pattern.compile("\\[" + prex + "(.*?)\\]");
		Matcher matcher = pattern.matcher(txt);
		while(matcher.find()){
			return matcher.group(1);
		}
		return null;
	}
	
	private long parseTime(String time) {
		time = time.replace("[", "").replace("]", "");
		
		int min = 0;
		int sec = 0;
		int milsec = 0;
		try {
			int colonIndex = time.indexOf(":");
			if (colonIndex != -1) {
				min = Integer.parseInt(time.substring(0, colonIndex));
				int dotIndex = time.indexOf(".");
				if (dotIndex != -1) {
					milsec = Integer.parseInt(time.substring(dotIndex + 1));
					sec = Integer.parseInt(time.substring(colonIndex + 1,
							dotIndex));
				} else {
					sec = Integer.parseInt(time.substring(colonIndex + 1));
				}
			}
		} catch (NumberFormatException e) {
		}
		long timestamp = (min * 60 + sec) * 1000 + milsec;
		return timestamp;
	}
}
