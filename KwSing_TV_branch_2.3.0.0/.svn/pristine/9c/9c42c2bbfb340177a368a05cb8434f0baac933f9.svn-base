package cn.kuwo.sing.util.lyric;

import java.io.IOException;

/**
 * 歌词解析器
 */
public interface LyricParser {
	
	public static final int LAST_SENTENCE_SPAN = 9999;
	
	/**
	 * 解析歌词头部信息
	 * @param lyrics
	 * @return
	 */
	public LyricsHeader parserHeader(byte[] lyrics);
	
	/**
	 * 解析歌词内容
	 * @param header
	 * @param lyrics
	 * @return
	 * @throws IOException 
	 */
	public Lyric parserLyrics(LyricsHeader header, byte[] lyrics) throws IOException;
	
	class LyricsHeader {
		public String version;
		public String title;
		public String ar;
		public String al;
		public String by;
		public String kuwo;
	}
	
//	class WordInfo {
//		public String ts;
//		public String content;
//	}
	
	class LyricLineInfo {
		public String startTs;
		public String content;
		public String endTs;
	}
}
