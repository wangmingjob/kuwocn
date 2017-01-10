package cn.kuwo.sing.util.lyric;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.kuwo.framework.log.KuwoLog;
import cn.kuwo.framework.utils.IOUtils;

public class KdtxParser implements LyricParser{ 
	
	private final String TAG = "KdtxParser";
	
	@Override
	public LyricsHeader parserHeader(byte[] lyrics) {
		return null;
	}

	/**
	 * 解析带频谱歌词
	 * @throws IOException 
	 */
	@Override
	public Lyric parserLyrics(LyricsHeader header, byte[] data) throws IOException {
		if (data == null)
			return null;
		
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		Lyric lyric = new Lyric();
		lyric.setType(Lyric.LYRIC_TYPE_KDTX);
		int sentencesCount = IOUtils.readInt(in); //歌词句子总数
//		KuwoLog.i(TAG, " sentencesCount "+sentencesCount);
		if(sentencesCount <= 0 || sentencesCount >= 500) //歌词的句子总长度不超过500
			return null;
		List<Sentence> sentences = new ArrayList<Sentence>(sentencesCount);
		lyric.setSentences(sentences);
		getEverySentence(in,sentencesCount,sentences);//每句歌词开始时间持续时间和频谱数组

		double mEnvelope;
		mEnvelope = IOUtils.readDouble(in); //获取包络最大值
		lyric.setEnvelope(mEnvelope);
		
		getTotalEnvelopes(in,sentencesCount,sentences); //获取总的包络值

		int wordnum = IOUtils.readInt(in);  //歌词总字数
//		KuwoLog.i(TAG, " wordnum  "+wordnum );
		getEveryWord(in,sentencesCount,sentences);//获取每个字的开始时间持续时间包络值
		in.close();
		return lyric;
	}
	
	/**
	 * 获取总的包络值
	 * @param in   数据流
	 * @param sentencesCount  句子数目
	 * @param sentences   存放句子列表
	 * @throws IOException 
	 */
	private void getTotalEnvelopes(InputStream in,int sentencesCount,List<Sentence> sentences ) throws IOException{
		int mEnvelopenum; 
		mEnvelopenum = IOUtils.readInt(in);  //原唱所有包络个数
//		KuwoLog.i(TAG, " 包络所有值  "+mEnvelopenum );
		double[] mEnvelopes = new double[mEnvelopenum]; 
		for(int i = 0;i < mEnvelopenum; i++){   //获取原唱所有包络
			mEnvelopes[i] = IOUtils.readDouble(in); 
//			KuwoLog.i(TAG, " 包络值  "+mEnvelopes[q] );
		}
		int num=0,size;
		for(int i = 0; i < sentencesCount; i++){
			size = sentences.get(i).getTimespan()/100;
//			 KuwoLog.i(TAG, " 长度  " + size );
			double temp [] = new double [size];
			int h = 0;
			int begin = (int) (sentences.get(i).getTimestamp()/100);
			for(int j = begin; j < begin+size && j < mEnvelopenum; j++){
				temp[h] = mEnvelopes[j];
				h++;
			}
			num = num + size;
			sentences.get(i).setEnvelopes(temp);
//			KuwoLog.i(TAG, " 时间长  " + temp[0] +mEnvelopes[777]);
		}
//		double test [] = sentences.get(0).getEnvelopes();
//		int tests = 0;
//		for(int h = 0; h < 26; h++)
//			tests = tests + sentences.get(h).getTimespan();
//		KuwoLog.i(TAG, " 时间长  " + tests);
	}	
	
	/**
	 * 获取每句的开始时间、持续时间和频谱数组
	 * @param in   数据流
	 * @param sentencesCount  句子数目
	 * @param sentences   存放句子列表
	 * @throws IOException 
	 */
	private void getEverySentence(InputStream in,int sentencesCount,List<Sentence> sentences ) throws IOException{
		long beginTime,endTime; 
		int everyLenth,pinLenth;
		for(int i = 0;i < sentencesCount;i++){
			Sentence sentence = new Sentence();
			sentence.setIndex(i);
			beginTime = IOUtils.readInt(in);   //每句开始时间
//			KuwoLog.i(TAG, " beginTime "+beginTime);
			sentence.setTimestamp(beginTime);
			endTime = IOUtils.readInt(in);    //每句结束时间
//			KuwoLog.i(TAG, " endTime "+endTime);
			
			everyLenth = (int) (endTime - beginTime);   //每句句长
//			KuwoLog.i(TAG, " everyLenth "+everyLenth);
			sentence.setTimespan(everyLenth);
			pinLenth = IOUtils.readInt(in);           //获取频谱长度
//			KuwoLog.i(TAG, " pinLenth "+pinLenth);
			if(pinLenth <= 0)
				continue;
			double[] mSpectrum = new double [pinLenth] ; //频谱数组
		
			for(int j = 0;j < pinLenth;j++){    //频谱
			   Double pinValue = IOUtils.readDouble(in);
			   mSpectrum[j] = pinValue == null ? 0 : pinValue;	
//				KuwoLog.i(TAG, " pin  "+mSpectrum[j]);
			}
//			KuwoLog.i(TAG, " pin  "+mSpectrum[1] +"  "+mSpectrum[33]);
			sentence.setSpectrum(mSpectrum);
			sentences.add(sentence);
		}
	}
	
	
	
	/**
	 * 获取每个字的开始时间、持续时间、内容、包络值
	 * @param in   数据流
	 * @param sentencesCount  句子数目
	 * @param sentences   存放句子列表
	 * @throws IOException 
	 */
	private void getEveryWord(InputStream in,int sentencesCount,List<Sentence> sentences) throws IOException{
		int evesenwordnum,bytenumlenth,bytelenth,envelope;
		String sencontent = "",singlecontent;
		long beginTime,endTime; 
		int everyLenth;
		byte[] temp = new byte[50];
		for(int i = 0; i < sentencesCount; i++){
			sencontent = "";
			evesenwordnum = IOUtils.readInt(in);  //一句歌词里的字数
			bytenumlenth = IOUtils.readInt(in);  //当前句子字节数
//			KuwoLog.i(TAG, " 歌词总字数  "+ wordsnum +" 歌词总字数  "+ evesenwordnum+" 歌词总字数  "+ bytenumlenth);
			temp = IOUtils.readBytes(in, bytenumlenth);
			sencontent = new String(temp,"gbk");
//			KuwoLog.i(TAG, " 内容  "+sencontent + "  " + temp.length);
			sentences.get(i).setContent(sencontent); 
			List<Word> words = new ArrayList<Word>(evesenwordnum);
			sentences.get(i).setWords(words);
			int indexByte = 0;//记录取字的字节index
			int indexWord = 0;//记录取字的字节index
			for(int j = 0; j < evesenwordnum; j++){
				Word word = new Word();
				bytelenth = IOUtils.readInt(in); //歌词字符个数
				beginTime = IOUtils.readInt(in)*100;  //字符开始时间
				singlecontent = "";
				byte[] te = new byte [bytelenth];
				word.setTimestamp(beginTime);
				endTime = IOUtils.readInt(in)*100;  //字符结束时间
				everyLenth = (int) (endTime - beginTime);
				word.setTimespan(everyLenth);
				envelope = IOUtils.readInt(in);  //包络值
//				KuwoLog.i(TAG, " begin  "+ beginTime +" endTime  "+ endTime +" envelope  "+ envelope);
				word.setEnvelope(envelope);
				int e = 0;
				for(int p = indexByte; p < indexByte+bytelenth;p++)			
				{		
					te[e] = temp[p];
					e++;
				}
				singlecontent = new String(te,"gbk");
//				KuwoLog.i(TAG, " 单个内容  "+singlecontent );
				word.setContent(singlecontent);    //获取每个字的内容
				word.setIndex(indexWord); 	// 字节索引
				indexByte += bytelenth;
				indexWord += singlecontent.length();
				words.add(word);
			}
			
		}
	}

}