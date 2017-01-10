package cn.kuwo.sing.util.lyric;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.util.EncodingUtils;

public class LrcxParser implements LyricParser {

	@Override
	public LyricsHeader parserHeader(byte[] data){
		String lrcx = EncodingUtils.getString(data, "gbk");
		String ver = getValue(lrcx, "ver:");
		String ti = getValue(lrcx, "ti:");
		String ar = getValue(lrcx, "ar:");
		String al = getValue(lrcx, "al:");
		String by = getValue(lrcx, "by:");
		String kuwo = getValue(lrcx, "kuwo:");
		
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
	public Lyric parserLyrics(LyricsHeader header, byte[] data){
		String lrcx = EncodingUtils.getString(data, "gbk");
		String kuwo = header.kuwo;
        int key = Integer.valueOf(kuwo, 8);
        int key1 = key / 10;
        int key2 = key % 10;
		
		Lyric lyric = new Lyric();
		lrcx += "[99:99:99]";//给歌词补位
		Pattern pattern = Pattern.compile("(\\[[0-9|\\:|\\.|\\-]*?\\]+)([\\w\\W]*?)(?=(\\[([0-9|\\:|\\.|\\-]*?)\\]+))");
		Matcher matcher = pattern.matcher(lrcx);
		int index = 0;
		List<Sentence> sentences = new ArrayList<Sentence>();
		while(matcher.find()){
			LyricLineInfo lineInfo = new LyricLineInfo();
			String startTs = matcher.group(1);//TODO:
			String content =  matcher.group(2);
			String endTs =  matcher.group(3);
			
			lineInfo.startTs = startTs;
			lineInfo.content = content;
			lineInfo.endTs = endTs;
			Sentence sentence = parserLine(index, lineInfo, key1, key2);
			sentences.add(sentence);
			index ++;
		}
		int length = sentences.size();
		int mTotalTimespan = 0;
		if(length > 0){
			Sentence sentence = sentences.get(length - 1);
			mTotalTimespan = (int) (sentence.getTimestamp() + sentence.getTimespan());
		}
		lyric.setType(Lyric.LYRIC_TYPE_LRCX);
		lyric.setSentences(sentences);
		lyric.setTotalTimespan(mTotalTimespan);
		return lyric;
	}
	
	private Sentence parserLine(int i, LyricLineInfo lineInfo, int key1, int key2){
		Sentence sentence = new Sentence();
		
		String regex = "<(.*?)>([\\w\\W]*?)(?=<.*?>|\n)";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(lineInfo.content);
		long startTs = parseTime(lineInfo.startTs);
		long endTs = 0l;
		if("[99:99:99]".equals(lineInfo.endTs)){
			endTs = startTs + LyricParser.LAST_SENTENCE_SPAN;
		}else{
			endTs = parseTime(lineInfo.endTs);
		}
		
		List<Word> words = new ArrayList<Word>();
		StringBuffer buffer = new StringBuffer();
		int index = 0;
		while(matcher.find()){
			String headinfo = matcher.group(1);
			String content = matcher.group(2);
			
			String hs[] = headinfo.split(",");
			int a = Integer.valueOf(hs[0]);
			int b = Integer.valueOf(hs[1]);
			long start = (a + b) / (2 * key1);
			int span = (a - b) / (2 * key2);

			Word word = new Word();
			word.setIndex(index);
			word.setTimestamp(start);
			word.setTimespan(span);
			word.setContent(content);
			index ++;
			
			words.add(word);
			buffer.append(content);
//			System.out.println("Word:" + content + "start: " + start + ", span: " + span);
		}
		sentence.setIndex(i);
		sentence.setContent(buffer.toString());
		sentence.setWords(words);
		sentence.setTimestamp(startTs);
		sentence.setTimespan((int) (endTs - startTs));
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
