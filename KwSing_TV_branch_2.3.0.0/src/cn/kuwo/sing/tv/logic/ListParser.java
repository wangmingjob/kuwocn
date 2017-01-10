package cn.kuwo.sing.tv.logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
import cn.kuwo.framework.log.KuwoLog;
import cn.kuwo.sing.tv.bean.ImageObject;
import cn.kuwo.sing.tv.bean.Mtv;
import cn.kuwo.sing.tv.bean.PagedData;
import cn.kuwo.sing.tv.bean.SearchPromptObject;
import cn.kuwo.sing.tv.bean.UserMtv;
import cn.kuwo.sing.tv.context.Constants;

public class ListParser {
	private final String TAG = "ListParser";
	private final String SINGER_PIC_PRE = "http://img4.kwcdn.kuwo.cn:81/star/starheads/";
	
	public PagedData<String> parseHotKeywordList(String json) {
		PagedData<String> pagedData = new PagedData<String>();
		try {
			JSONObject jsonObj = new JSONObject(json);
			String resultStr = (String) jsonObj.getString("result");
			List<String> keywordList = new ArrayList<String>();
			if(resultStr != null && resultStr.equals("ok")){ 
				JSONArray jsonArray = jsonObj.getJSONArray("list");
				for(int i = 0; i <= jsonArray.length()-1; i++) {
					JSONObject obj = (JSONObject) jsonArray.get(i);
					keywordList.add(obj.getString("key"));
				}
				if(keywordList.size() >= 10) {
					pagedData.data = keywordList.subList(0, 10);
				}else {
					pagedData.data = keywordList;
				}
			}else {
				//返回取榜单列表失败，记录失败原因.无需进行查询问题原因
				KuwoLog.w(TAG, "parseHotKeywordList : input json result is error. the json is : " + json);
				return null;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return pagedData;
	}
	
	/**
	 * 解析榜单json串，返回PagedData
	 * @param json
	 * @return PagedData<Mtv>
	 */
	public PagedData<Mtv> parseMtvList(String json){
		PagedData<Mtv> mPageData = new PagedData<Mtv>();
		try{
			if(json != null && json.length() > 0){
				json = json.substring(8);
				JSONObject mJSONObject = new JSONObject(json);
				String resultStr = (String) mJSONObject.getString("result");
				if(resultStr != null && resultStr.equals("ok")){
					int total = mJSONObject.getInt("total");
					int page = mJSONObject.getInt("pn");
					mPageData.total = total;
					mPageData.page = page;
					JSONArray mMtvJsonArray = mJSONObject.getJSONArray("musiclist");
					List<Mtv> mtvList = new ArrayList<Mtv>();
					for(int i = 0;i < mMtvJsonArray.length();i++){
						JSONObject mMtvJsonObject = (JSONObject)mMtvJsonArray.get(i);
						Mtv mMtv = new Mtv();
						mMtv.name = mMtvJsonObject.getString("name");
						mMtv.rid = mMtvJsonObject.getLong("id")+"";
						mMtv.artist = mMtvJsonObject.getString("artist");
						mMtv.hasEcho = (mMtvJsonObject.getInt("hasecho") == 1) ? true : false;
						mMtv.hasKdatx = (mMtvJsonObject.getInt("haskdatx") == 1) ? true : false;
						mtvList.add(mMtv);
					}
					mPageData.data = mtvList;
					return mPageData;
				} else {
					//返回取榜单列表失败，记录失败原因.无需进行查询问题原因
					KuwoLog.w(TAG, "parseMtvList : input json result is error. the json is : " + json);
					return null;
				}
			} else {
				//返回数据为空异常，记录失败原因.需要分析真正原因
				KuwoLog.w(TAG, "parseMtvList : input json is null or json length is zero.");
				return null;
			}
		} catch (Exception e) {
			//格式化json串异常，返回数据格式不正确，需要分析真正原因.
			KuwoLog.w(TAG, "parseMtvList : parse json error. the json is : " + json);
			KuwoLog.e(TAG, e.getMessage());
			return null;
		}
	}
	/**
	 * 解析语种json串，返回PagedData
	 * @param json
	 * @return PagedData<Mtv>
	 */
	public PagedData<Mtv> parseLangList(String json){
		PagedData<Mtv> mPageData = new PagedData<Mtv>();
		try{
			if(json != null && json.length() > 0){
				json = json.substring(8);
				JSONObject mJSONObject = new JSONObject(json);
				String resultStr = (String) mJSONObject.getString("result");
				if(resultStr != null && resultStr.equals("ok")){
					int total = mJSONObject.getInt("total");
					int page = mJSONObject.getInt("pn")+1;
					mPageData.total = total;
					mPageData.page = page;
					JSONArray mMtvJsonArray = mJSONObject.getJSONArray("datalist");
					List<Mtv> mtvList = new ArrayList<Mtv>();
					for(int i = 0;i < mMtvJsonArray.length();i++){
						JSONObject mMtvJsonObject = (JSONObject)mMtvJsonArray.get(i);
						Mtv mMtv = new Mtv();
						mMtv.name = mMtvJsonObject.getString("name");
						mMtv.rid = mMtvJsonObject.getLong("id")+"";
						mMtv.artist = mMtvJsonObject.getString("artist");
						mMtv.hasEcho = (mMtvJsonObject.getInt("hasecho") == 1) ? true : false;
						mMtv.hasKdatx = (mMtvJsonObject.getInt("mvkdatx") == 1) ? true : false;
						mtvList.add(mMtv);
					}
					mPageData.data = mtvList;
					return mPageData;
				} else {
					//返回取榜单列表失败，记录失败原因.无需进行查询问题原因
					KuwoLog.w(TAG, "parseMtvList : input json result is error. the json is : " + json);
					return null;
				}
			} else {
				//返回数据为空异常，记录失败原因.需要分析真正原因
				KuwoLog.w(TAG, "parseMtvList : input json is null or json length is zero.");
				return null;
			}
		} catch (Exception e) {
			//格式化json串异常，返回数据格式不正确，需要分析真正原因.
			KuwoLog.w(TAG, "parseMtvList : parse json error. the json is : " + json);
			KuwoLog.e(TAG, e.getMessage());
			return null;
		}
	}
	
	/**
	 * 解析首字母歌曲列表json串，返回PagedData
	 * @param json
	 * @return PagedData<Mtv>
	 */
	public PagedData<Mtv> parseMtvListBySong(String json){
		PagedData<Mtv> mPageData = new PagedData<Mtv>();
		try{
			if(json != null && json.length() > 0){
				JSONObject mJSONObject = new JSONObject(json);
				String resultStr = (String) mJSONObject.getString("result");
				if(resultStr != null && resultStr.equals("ok")){
					int total = mJSONObject.getInt("totalcount");
					int page = mJSONObject.getInt("pn");
					mPageData.total = total;
					mPageData.page = page;
					JSONArray mMtvJsonArray = mJSONObject.getJSONArray("list");
					List<Mtv> mtvList = new ArrayList<Mtv>();
					for(int i = 0;i < mMtvJsonArray.length();i++){
						JSONObject mMtvJsonObject = (JSONObject)mMtvJsonArray.get(i);
						Mtv mMtv = new Mtv();
						mMtv.name = mMtvJsonObject.getString("title");
						mMtv.rid = mMtvJsonObject.getLong("id")+"";
						mMtv.artist = mMtvJsonObject.getString("artist");
						mMtv.hasEcho = (mMtvJsonObject.getInt("hasecho") == 1) ? true : false;
						mMtv.hasKdatx = (mMtvJsonObject.getInt("haskdatx") == 1) ? true : false;
						mtvList.add(mMtv);
					}
					mPageData.data = mtvList;
					return mPageData;
				} else {
					//返回取榜单列表失败，记录失败原因.无需进行查询问题原因
					KuwoLog.w(TAG, "parseMtvListBySong : input json result is error. the json is : " + json);
					return null;
				}
			} else {
				//返回数据为空异常，记录失败原因.需要分析真正原因
				KuwoLog.w(TAG, "parseMtvListBySong : input json is null or json length is zero.");
				return null;
			}
		} catch (Exception e) {
			//格式化json串异常，返回数据格式不正确，需要分析真正原因.
			KuwoLog.w(TAG, "parseMtvListBySong : parse json error. the json is : " + json);
			KuwoLog.e(TAG, e.getMessage());
			return null;
		}
	}
	
	/**
	 * 解析首字母歌曲列表json串，返回PagedData
	 * @param json
	 * @return PagedData<Mtv>
	 */
	public PagedData<Mtv> parseMtvListPinyinBySong(String json){
		PagedData<Mtv> mPageData = new PagedData<Mtv>();
		try{
			if(json != null && json.length() > 0){
				json = json.substring(8);
				JSONObject mJSONObject = new JSONObject(json);
				String resultStr = (String) mJSONObject.getString("result");
				if(resultStr != null && resultStr.equals("ok")){
					int total = mJSONObject.getInt("total");
					int page = mJSONObject.getInt("pn");
					mPageData.total = total;
					mPageData.page = page+1;
					JSONArray mMtvJsonArray = mJSONObject.getJSONArray("datalist");
					List<Mtv> mtvList = new ArrayList<Mtv>();
					for(int i = 0;i < mMtvJsonArray.length();i++){
						JSONObject mMtvJsonObject = (JSONObject)mMtvJsonArray.get(i);
						Mtv mMtv = new Mtv();
						mMtv.name = mMtvJsonObject.getString("name");
						mMtv.rid = mMtvJsonObject.getLong("id")+"";
						mMtv.artist = mMtvJsonObject.getString("artist");
						mMtv.hasEcho = (mMtvJsonObject.getInt("hasecho") == 1) ? true : false;
						mMtv.hasKdatx = (mMtvJsonObject.getInt("mvkdatx") == 1) ? true : false;
						mtvList.add(mMtv);
					}
					mPageData.data = mtvList;
					return mPageData;
				} else {
					//返回取榜单列表失败，记录失败原因.无需进行查询问题原因
					KuwoLog.w(TAG, "parseMtvListBySong : input json result is error. the json is : " + json);
					return null;
				}
			} else {
				//返回数据为空异常，记录失败原因.需要分析真正原因
				KuwoLog.w(TAG, "parseMtvListBySong : input json is null or json length is zero.");
				return null;
			}
		} catch (Exception e) {
			//格式化json串异常，返回数据格式不正确，需要分析真正原因.
			KuwoLog.w(TAG, "parseMtvListBySong : parse json error. the json is : " + json);
			KuwoLog.e(TAG, e.getMessage());
			return null;
		}
	}
	
	/**
	 * 解析首字母歌曲列表json串，返回PagedData
	 * @param json
	 * @return PagedData<Mtv>
	 */
	public PagedData<ImageObject> parseBarList(int banginfo, String json){
		PagedData<ImageObject> mPageData = new PagedData<ImageObject>();
		try{
			if(json != null && json.length() > 0){
				json = json.substring(8);
				JSONObject mJSONObject = new JSONObject(json);
				String resultStr = (String) mJSONObject.getString("result");
				if(resultStr != null && resultStr.equals("ok")){
					int total = mJSONObject.getInt("total");
					mPageData.total = total+6;
					mPageData.page = 1;
					JSONArray mMtvJsonArray = mJSONObject.getJSONArray("banglist");
					List<ImageObject> imageObjectList = new ArrayList<ImageObject>();
					
					ImageObject mImageObject = null;
					
					if(banginfo == Constants.BAR_LIST_BANGINFO_DEFAULT) {
						mImageObject = new ImageObject();
						mImageObject.id = "网友翻唱";
						mImageObject.name = "网友翻唱";
						mImageObject.pic = "http://image.kuwo.cn/ks/tv/hotbar1.png";
						mImageObject.type = 2;
						imageObjectList.add(mImageObject);
					}
					for(int i = 0;i < mMtvJsonArray.length();i++){
						JSONObject mImageJsonObject = (JSONObject)mMtvJsonArray.get(i);
						mImageObject = new ImageObject();
						mImageObject.id = mImageJsonObject.getInt("id")+"";
						mImageObject.pic = mImageJsonObject.getString("pic1");
						mImageObject.name = mImageJsonObject.getString("name");
						mImageObject.type = 0;
						imageObjectList.add(mImageObject);
					}
					if(banginfo == Constants.BAR_LIST_BANGINFO_DEFAULT) {
						mImageObject = new ImageObject();
						mImageObject.id = "国语";
						mImageObject.name = "国语";
						mImageObject.pic = "http://image.kuwo.cn/ks/tv/chinese.png";
						mImageObject.type = 1;
						imageObjectList.add(mImageObject);
						mImageObject = new ImageObject();
						mImageObject.id = "粤语";
						mImageObject.name = "粤语";
						mImageObject.pic = "http://image.kuwo.cn/ks/tv/cantonese.png";
						mImageObject.type = 1;
						imageObjectList.add(mImageObject);
						mImageObject = new ImageObject();
						mImageObject.id = "闽南语";
						mImageObject.name = "闽南语";
						mImageObject.pic = "http://image.kuwo.cn/ks/tv/hokkien.png";
						mImageObject.type = 1;
						imageObjectList.add(mImageObject);
						mImageObject = new ImageObject();
						mImageObject.id = "韩流";
						mImageObject.name = "韩流";
						mImageObject.pic = "http://image.kuwo.cn/ks/tv/korean.png";
						mImageObject.type = 1;
						imageObjectList.add(mImageObject);
						mImageObject = new ImageObject();
						mImageObject.id = "欧美";
						mImageObject.name = "欧美";
						mImageObject.pic = "http://image.kuwo.cn/ks/tv/europe.png";
						mImageObject.type = 1;
						imageObjectList.add(mImageObject);
						mImageObject = new ImageObject();
						mImageObject.id = "日音";
						mImageObject.name = "日音";
						mImageObject.pic = "http://image.kuwo.cn/ks/tv/japanese.png";
						mImageObject.type = 1;
						imageObjectList.add(mImageObject);
					}
					mPageData.data = imageObjectList;
					return mPageData;
				} else {
					//返回取榜单列表失败，记录失败原因.无需进行查询问题原因
					KuwoLog.w(TAG, "parseBarList : input json result is error. the json is : " + json);
					return null;
				}
			} else {
				//返回数据为空异常，记录失败原因.需要分析真正原因
				KuwoLog.w(TAG, "parseBarList : input json is null or json length is zero.");
				return null;
			}
		} catch (Exception e) {
			//格式化json串异常，返回数据格式不正确，需要分析真正原因.
			KuwoLog.w(TAG, "parseBarList : parse json error. the json is : " + json);
			KuwoLog.e(TAG, e.getMessage());
			return null;
		}
	}
	
	/**
	 * @param json
	 * @return PagedData<Mtv>
	 */
	public PagedData<ImageObject> parseCategoryBarList(String json){
		PagedData<ImageObject> mPageData = new PagedData<ImageObject>();
		try{
			if(json != null && json.length() > 0){
				json = json.substring(8);
				JSONObject mJSONObject = new JSONObject(json);
				String resultStr = (String) mJSONObject.getString("result");
				if(resultStr != null && resultStr.equals("ok")){
					int total = mJSONObject.getInt("total");
					mPageData.total = total;
					mPageData.page = 1;
					JSONArray mMtvJsonArray = mJSONObject.getJSONArray("banglist");
					List<ImageObject> imageObjectList = new ArrayList<ImageObject>();
					
					ImageObject mImageObject = null;
					
					for(int i = 0;i < mMtvJsonArray.length();i++){
						JSONObject mImageJsonObject = (JSONObject)mMtvJsonArray.get(i);
						mImageObject = new ImageObject();
						mImageObject.id = mImageJsonObject.getInt("id")+"";
						mImageObject.pic = mImageJsonObject.getString("pic1");
						mImageObject.name = mImageJsonObject.getString("name");
						mImageObject.type = 0;
						imageObjectList.add(mImageObject);
					}
					mPageData.data = imageObjectList;
					return mPageData;
				} else {
					//返回取榜单列表失败，记录失败原因.无需进行查询问题原因
					KuwoLog.w(TAG, "parseCategoryBarList : input json result is error. the json is : " + json);
					return null;
				}
			} else {
				//返回数据为空异常，记录失败原因.需要分析真正原因
				KuwoLog.w(TAG, "parseCategoryBarList : input json is null or json length is zero.");
				return null;
			}
		} catch (Exception e) {
			//格式化json串异常，返回数据格式不正确，需要分析真正原因.
			KuwoLog.w(TAG, "parseCategoryBarList : parse json error. the json is : " + json);
			KuwoLog.e(TAG, e.getMessage());
			return null;
		}
	}
	/**
	 * 获取歌手列表.
	 * @param json
	 * @return
	 */
	public PagedData<ImageObject> parseSingerList(String json){
		PagedData<ImageObject> mPageData = new PagedData<ImageObject>();
		try{
			if(json != null && json.length() > 0){
				json = json.substring(8);
				JSONObject mJSONObject = new JSONObject(json);
				String resultStr = (String) mJSONObject.getString("result");
				if(resultStr != null && resultStr.equals("ok")){
					int total = mJSONObject.getInt("total");
					int page = mJSONObject.getInt("pn")+1;
					mPageData.total = total;
					mPageData.page = page;
					JSONArray mMtvJsonArray = mJSONObject.getJSONArray("datalist");
					List<ImageObject> imageObjectList = new ArrayList<ImageObject>();
					for(int i = 0;i < mMtvJsonArray.length();i++){
						JSONObject mImageJsonObject = (JSONObject)mMtvJsonArray.get(i);
						ImageObject mImageObject = new ImageObject();
						mImageObject.id = mImageJsonObject.getInt("id")+"";
						mImageObject.pic = SINGER_PIC_PRE + mImageJsonObject.getString("pic").replaceFirst("55","150");
						mImageObject.name = mImageJsonObject.getString("name");
						imageObjectList.add(mImageObject);
					}
					mPageData.data = imageObjectList;
					return mPageData;
				} else {
					//返回取榜单列表失败，记录失败原因.无需进行查询问题原因
					KuwoLog.w(TAG, "parseSingerList : input json result is error. the json is : " + json);
					return null;
				}
			} else {
				//返回数据为空异常，记录失败原因.需要分析真正原因
				KuwoLog.w(TAG, "parseSingerList : input json is null or json length is zero.");
				return null;
			}
		} catch (Exception e) {
			//格式化json串异常，返回数据格式不正确，需要分析真正原因.
			KuwoLog.w(TAG, "parseSingerList : parse json error. the json is : " + json);
			KuwoLog.e(TAG, e.getMessage());
			return null;
		}
	}
	
	public PagedData<Mtv> parseMtvListBySinger(String messJson){
		if(TextUtils.isEmpty(messJson))
			return null;
		PagedData<Mtv> mPageData = new PagedData<Mtv>();
		String line = null;
		String id = null;
		String name = null;
		String artist = null;
		String haskdatx = null;
		String hasEcho = null;
		Mtv mtv = null;
		List<Mtv> mtvList = new ArrayList<Mtv>();
		BufferedReader reader = new BufferedReader(new StringReader(messJson));
		int total = 0;
		int page = 0;
		try {
			int count = 0;
			while ((line = reader.readLine()) != null) {
				int pos = -1;
				if ((pos = line.indexOf("Total=")) != -1) {
					String st = line.substring(pos + 6);
					total = Integer.parseInt(st);
				}
				if((pos = line.indexOf("PN=")) != -1){
					String st = line.substring(pos+3);
					page = Integer.parseInt(st);
				}
				count++;
				if (count > 6) {
					break;
				}
			}
		}
		catch (Exception e) {
		}
		try {
			while((line = reader.readLine()) != null){
				if(line.startsWith("MUSICRID=MUSIC_")){
					id = line.substring(15).trim();
				} else if (line.startsWith("SONGNAME=")){
					name = line.substring(9).trim();
				} else if (line.startsWith("ARTIST=")){
					artist = line.substring(7).trim();
				} else if (line.startsWith("HASKDATX=")){
					haskdatx = line.substring(9).trim();
				} else if(line.startsWith("HASECHO=")){
					hasEcho = line.substring(8).trim();
				}
				
				if(line.equals("")){
					if(id == null){
						mtv = null;
					} else {
						mtv = new Mtv();
						mtv.rid = id;
						mtv.name = name;
						mtv.artist = artist;
						mtv.hasKdatx = (haskdatx != null && haskdatx.equals("1")) ? true : false;
						mtv.hasEcho = (hasEcho != null && hasEcho.equals("1")) ? true : false;
						mtvList.add(mtv);
					}
				}
			}
			mPageData.page = page;
			mPageData.total = total;
			mPageData.data = mtvList;
			return mPageData;
		} catch (IOException e) {
			return null;
		}
	}
	/**
	 * 获取用户榜单列表
	 * @param messJson
	 * @return
	 */
	public PagedData<ImageObject> parseSingerBarList(String json){
		PagedData<ImageObject> mPageData = new PagedData<ImageObject>();
		try{
			if(json != null && json.length() > 0){
				JSONObject mJSONObject = new JSONObject(json);
				String resultStr = (String) mJSONObject.getString("result");
				if(resultStr != null && resultStr.equals("ok")){
					int total = mJSONObject.getInt("size");
					mPageData.total = total;
					mPageData.page = 1;
					JSONArray mMtvJsonArray = mJSONObject.getJSONArray("list");
					List<ImageObject> imageObjectList = new ArrayList<ImageObject>();
					for(int i = 0;i < mMtvJsonArray.length();i++){
						JSONObject mImageJsonObject = (JSONObject)mMtvJsonArray.get(i);
						ImageObject mImageObject = new ImageObject();
						mImageObject.id = mImageJsonObject.getString("inner");
						mImageObject.pic = mImageJsonObject.getString("pic");
						mImageObject.name = mImageJsonObject.getString("name");
						mImageObject.inner = mImageJsonObject.getString("inner");
						imageObjectList.add(mImageObject);
					}
					mPageData.data = imageObjectList;
					return mPageData;
				} else {
					//返回取榜单列表失败，记录失败原因.无需进行查询问题原因
					KuwoLog.w(TAG, "parseSingerList : input json result is error. the json is : " + json);
					return null;
				}
			} else {
				//返回数据为空异常，记录失败原因.需要分析真正原因
				KuwoLog.w(TAG, "parseSingerList : input json is null or json length is zero.");
				return null;
			}
		} catch (Exception e) {
			//格式化json串异常，返回数据格式不正确，需要分析真正原因.
			KuwoLog.w(TAG, "parseSingerList : parse json error. the json is : " + json);
			KuwoLog.e(TAG, e.getMessage());
			return null;
		}
	}
	
	/**
	 * 获取作品播放地址
	 * @param messJson
	 * @return
	 */
	public String parseMtvUrl(String json){
		try{
			if(json != null && json.length() > 0){
				JSONObject mJSONObject = new JSONObject(json);
				String resultStr = (String) mJSONObject.getString("url");
				if(resultStr != null && resultStr.length() > 0){
					return resultStr;
				} else {
					//返回取榜单列表失败，记录失败原因.无需进行查询问题原因
					KuwoLog.w(TAG, "parseUrl : input json result is error. the json is : " + json);
					return null;
				}
			} else {
				//返回数据为空异常，记录失败原因.需要分析真正原因
				KuwoLog.w(TAG, "parseUrl : input json is null or json length is zero.");
				return null;
			}
		} catch (Exception e) {
			//格式化json串异常，返回数据格式不正确，需要分析真正原因.
			KuwoLog.w(TAG, "parseUrl : parse json error. the json is : " + json);
			KuwoLog.e(TAG, e.getMessage());
			return null;
		}
	}
	
	public String parseUserMtvUrl(String json) {
		String result = null;
		try {
			JSONObject jsonObj = new JSONObject(json);
			String resultStr = jsonObj.getString("result");
			if(resultStr != null && resultStr.equals("ok")) 
				result = jsonObj.getString("mediaUrl");
			else 
				KuwoLog.w(TAG, "request the user mtv url is error!");
		} catch (JSONException e) {
			KuwoLog.e(TAG, e);
			KuwoLog.printStackTrace(e);
		}
		return result;
	}
	
	/**
	 * 获取背景图片
	 * @param messJson
	 * @return
	 */
	public ImageObject parseTopPic(String json){
		ImageObject imageObject = new ImageObject();
		try{
			if(json != null && json.length() > 0){
				JSONObject mJSONObject = new JSONObject(json);
				String resultStr = (String) mJSONObject.getString("result");
				if(resultStr != null && resultStr.equals("ok")){
					imageObject.id = mJSONObject.getString("status");
					imageObject.name = mJSONObject.getString("status");
					imageObject.pic = mJSONObject.getString("pic");
					return imageObject;
				} else {
					//返回取榜单列表失败，记录失败原因.无需进行查询问题原因
					KuwoLog.w(TAG, "parseTopPic : input json result is error. the json is : " + json);
					return null;
				}
			} else {
				//返回数据为空异常，记录失败原因.需要分析真正原因
				KuwoLog.w(TAG, "parseTopPic : input json is null or json length is zero.");
				return null;
			}
		} catch (Exception e) {
			//格式化json串异常，返回数据格式不正确，需要分析真正原因.
			KuwoLog.w(TAG, "parseTopPic : parse json error. the json is : " + json);
			KuwoLog.e(TAG, e.getMessage());
			return null;
		}
	}
	
	/**
	 * 解析搜索提示JSON为页面数据格式
	 * 
	 * @param content
	 * @return
	 */
	public PagedData<SearchPromptObject> parseSearchPromptContent(String content) {
		PagedData<SearchPromptObject> pagedData = new PagedData<SearchPromptObject>();
		try {
			JSONObject jsonObj = new JSONObject(content);
			String resultStr = jsonObj.getString("result");
			if(!TextUtils.isEmpty(resultStr) && resultStr.equals("ok")) {
				pagedData.total = jsonObj.getInt("total");
				pagedData.data = new ArrayList<SearchPromptObject>();
				JSONArray jsonArray = jsonObj.getJSONArray("datalist");
				for(int i = 0; i < jsonArray.length(); i++) {
					SearchPromptObject searchPromptObject = new SearchPromptObject();
					JSONObject eachObj = (JSONObject) jsonArray.get(i);
					searchPromptObject.name = eachObj.getString("name");
					searchPromptObject.hot = eachObj.getString("hot");
					pagedData.data.add(searchPromptObject);
				}
			}else {
				KuwoLog.e(TAG, "parse search prompt content error...");
				pagedData = null;
			}
		} catch (JSONException e) {
			KuwoLog.e(TAG, e);
			pagedData = null;
		}
		return pagedData;
	}
	
	/**
	 * 
	 * @param jsonContent
	 * @return
	 */
	public PagedData<UserMtv> parseUserMtvList(String jsonContent) {
		PagedData<UserMtv> pagedData = new PagedData<UserMtv>();
		try {
			JSONObject jsonObj = new JSONObject(jsonContent);
			String resultStr = jsonObj.getString("result");
			if(!TextUtils.isEmpty(resultStr) && resultStr.equals("ok")) {
				pagedData.total = jsonObj.getInt("total");
				pagedData.page = jsonObj.getInt("pn");
				pagedData.data = new ArrayList<UserMtv>();
				JSONArray jsonArray = jsonObj.getJSONArray("list");
				for(int i = 0; i < jsonArray.length(); i++) {
					JSONObject eachObj = (JSONObject) jsonArray.get(i);
					UserMtv userMtv = new UserMtv();
					userMtv.uid = eachObj.getString("uid");
					userMtv.id = eachObj.getString("id");
					userMtv.rid = eachObj.getString("rid");
					userMtv.title = eachObj.getString("title");
					userMtv.upic = eachObj.getString("upic");
					userMtv.uname = eachObj.getString("uname");
					userMtv.view = eachObj.getInt("view");
					userMtv.pic = eachObj.getString("pic");
					userMtv.flower = eachObj.getInt("flower");
					userMtv.mlogurl = eachObj.getString("mlogurl");
					userMtv.mediatype = eachObj.getInt("mediatype");
					pagedData.data.add(userMtv);
				}
			}else {
				KuwoLog.e(TAG, "parse user mtv list error...");
				pagedData = null;
			}
		} catch (JSONException e) {
			KuwoLog.e(TAG, e);
			pagedData = null;
		}
		return pagedData;
	}
	
}