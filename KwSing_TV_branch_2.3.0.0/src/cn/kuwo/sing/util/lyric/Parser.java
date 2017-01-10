package cn.kuwo.sing.util.lyric;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.apache.commons.io.FilenameUtils;
import org.apache.http.util.EncodingUtils;

import cn.kuwo.framework.crypt.Base64Coder;
import cn.kuwo.framework.log.KuwoLog;
import cn.kuwo.framework.utils.IOUtils;
import cn.kuwo.framework.utils.ZipUtils;
import cn.kuwo.sing.util.lyric.LyricParser.LyricsHeader;

public class Parser {
	private final String TAG = "Parser";
	private final String PWD = "yeelion";

	public Lyric paraLyricFile(String path) throws IOException {
		File file = new File(path);
		return paraLyricFile(file);
	}
	
	public Lyric paraLyricFile(File file) throws IOException {
		Lyric lyric = null;
		if (file == null)
			return null;
		if (!file.exists())
			return null;

		FileInputStream fis=new FileInputStream (file);
		byte[] bytes = IOUtils.readLeftBytes(fis);
		fis.close();
		String ext = FilenameUtils.getExtension(file.getName()).toLowerCase();
		
	    KuwoLog.v(TAG, "Lyric Type: " + ext);
		if (ext.equals("lrcx")) {
			lyric = analyzeLyric(bytes, Lyric.LYRIC_TYPE_LRCX);
		} else if (ext.equals("kdtx")) {
			lyric = analyzeLyric(bytes, Lyric.LYRIC_TYPE_KDTX);
		} else{
			lyric = analyzeLyric(bytes, Lyric.LYRIC_TYPE_LRC);
	    }
	    
		return lyric;
	}
	
    public Lyric analyzeLyric(byte[] lyricBytes, int lrcx) throws IOException{
    	if (lyricBytes == null || lyricBytes.length == 0)
    		return null;
    	
    	ByteArrayInputStream in = new ByteArrayInputStream(lyricBytes);
    	int zipLength = IOUtils.readInt(in);
		int unzipLength = IOUtils.readInt(in);
	    KuwoLog.d(TAG, String.format("zipLength: %s     unzipLength:%s", zipLength, unzipLength));
					 
		// 解压
		byte[] bytes = IOUtils.readLeftBytes(in);
	    byte[] unzip = ZipUtils.unzip(bytes, 0, unzipLength);
		LyricParser parser = null;
		Lyric lyric = null;
					 
		if (lrcx == Lyric.LYRIC_TYPE_LRC) {
			parser = new LrcParser();
		} else if (lrcx == Lyric.LYRIC_TYPE_LRCX) {
			// 解密
			String data = EncodingUtils.getString(unzip, "gbk");
			data = Base64Coder.decodeString(data, "gbk", PWD);
			unzip = EncodingUtils.getBytes(data, "gbk");
						 
			parser = new LrcxParser();
		} else if (lrcx == Lyric.LYRIC_TYPE_KDTX) {
			parser = new KdtxParser();
		}
		LyricsHeader header = parser.parserHeader(unzip);
		try {
			lyric = parser.parserLyrics(header, unzip);
		} catch (IOException e) {
			KuwoLog.printStackTrace(e);
		}
		in.close();
		return lyric;
	}
}
