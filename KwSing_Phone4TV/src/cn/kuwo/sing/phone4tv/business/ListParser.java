package cn.kuwo.sing.phone4tv.business;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
import cn.kuwo.sing.phone4tv.bean.ImageObject;
import cn.kuwo.sing.phone4tv.bean.Mtv;
import cn.kuwo.sing.phone4tv.bean.PageData;
import cn.kuwo.sing.phone4tv.bean.SearchPromptObject;
import cn.kuwo.sing.phone4tv.bean.UserMtv;
import cn.kuwo.sing.phone4tv.commmons.log.LogSystem;
import cn.kuwo.sing.phone4tv.commons.context.Constants;


public class ListParser {
	private final String LOG_TAG = "ListParser";
	private final String SINGER_PIC_PRE = "http://img4.kwcdn.kuwo.cn:81/star/starheads/";
	
	
	/**
	 * 解析首字母歌曲列表json串，返回PageData
	 * @param json
	 * @return PageData<Mtv>
	 */
	public PageData<ImageObject> parseMtvCategoryContent(int banginfo, String json){
		PageData<ImageObject> mPageData = new PageData<ImageObject>();
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
					
					if(banginfo == Constants.BANGINFO_MTV_CATEGORY) {
						mImageObject = new ImageObject();
						mImageObject.id = "网友翻唱";
						mImageObject.name = "网友翻唱";
						mImageObject.pic = "http://image.kuwo.cn/ks/tv/hotbar1.png";
						mImageObject.type = Constants.MTV_CATEGORY_TYPE_USER;
						imageObjectList.add(mImageObject);
					}
					for(int i = 0;i < mMtvJsonArray.length();i++){
						JSONObject mImageJsonObject = (JSONObject)mMtvJsonArray.get(i);
						mImageObject = new ImageObject();
						mImageObject.id = mImageJsonObject.getInt("id")+"";
						mImageObject.pic = mImageJsonObject.getString("pic1");
						mImageObject.name = mImageJsonObject.getString("name");
						mImageObject.type = Constants.MTV_CATEGORY_TYPE_COMMON;
						imageObjectList.add(mImageObject);
					}
					if(banginfo == Constants.BANGINFO_MTV_CATEGORY) {
						mImageObject = new ImageObject();
						mImageObject.id = "国语";
						mImageObject.name = "国语";
						mImageObject.pic = "http://image.kuwo.cn/ks/tv/chinese.png";
						mImageObject.type = Constants.MTV_CATEGORY_TYPE_LANGUAGE;
						imageObjectList.add(mImageObject);
						mImageObject = new ImageObject();
						mImageObject.id = "粤语";
						mImageObject.name = "粤语";
						mImageObject.pic = "http://image.kuwo.cn/ks/tv/cantonese.png";
						mImageObject.type = Constants.MTV_CATEGORY_TYPE_LANGUAGE;
						imageObjectList.add(mImageObject);
						mImageObject = new ImageObject();
						mImageObject.id = "闽南语";
						mImageObject.name = "闽南语";
						mImageObject.pic = "http://image.kuwo.cn/ks/tv/hokkien.png";
						mImageObject.type = Constants.MTV_CATEGORY_TYPE_LANGUAGE;
						imageObjectList.add(mImageObject);
						mImageObject = new ImageObject();
						mImageObject.id = "韩流";
						mImageObject.name = "韩流";
						mImageObject.pic = "http://image.kuwo.cn/ks/tv/korean.png";
						mImageObject.type = Constants.MTV_CATEGORY_TYPE_LANGUAGE;
						imageObjectList.add(mImageObject);
						mImageObject = new ImageObject();
						mImageObject.id = "欧美";
						mImageObject.name = "欧美";
						mImageObject.pic = "http://image.kuwo.cn/ks/tv/europe.png";
						mImageObject.type = Constants.MTV_CATEGORY_TYPE_LANGUAGE;
						imageObjectList.add(mImageObject);
						mImageObject = new ImageObject();
						mImageObject.id = "日音";
						mImageObject.name = "日音";
						mImageObject.pic = "http://image.kuwo.cn/ks/tv/japanese.png";
						mImageObject.type = Constants.MTV_CATEGORY_TYPE_LANGUAGE;
						imageObjectList.add(mImageObject);
					}
					mPageData.data = imageObjectList;
					return mPageData;
				} else {
					//返回取榜单列表失败，记录失败原因.无需进行查询问题原因
					LogSystem.w(LOG_TAG, "parseMtvCategoryContent : input json result is error. the json is : " + json);
					return null;
				}
			} else {
				//返回数据为空异常，记录失败原因.需要分析真正原因
				LogSystem.w(LOG_TAG, "parseMtvCategoryContent : input json is null or json length is zero.");
				return null;
			}
		} catch (Exception e) {
			//格式化json串异常，返回数据格式不正确，需要分析真正原因.
			LogSystem.w(LOG_TAG, "parseMtvCategoryContent : parse json error. the json is : " + json);
			LogSystem.e(LOG_TAG, e.getMessage());
			return null;
		}
	}
	
	public PageData<ImageObject> parseMtvCategoryOrderContent(String json){
		PageData<ImageObject> mPageData = new PageData<ImageObject>();
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
					LogSystem.e(LOG_TAG, "parseMtvCategoryOrderContent : input json result is error. the json is : " + json);
					return null;
				}
			} else {
				//返回数据为空异常，记录失败原因.需要分析真正原因
				LogSystem.e(LOG_TAG, "parseMtvCategoryOrderContent : input json is null or json length is zero.");
				return null;
			}
		} catch (Exception e) {
			//格式化json串异常，返回数据格式不正确，需要分析真正原因.
			LogSystem.e(LOG_TAG, "parseMtvCategoryOrderContent : parse json error. the json is : " + json);
			LogSystem.e(LOG_TAG, e.getMessage());
			return null;
		}
	}
	
	public PageData<ImageObject> parseSingerCategoryContent(String json){
		PageData<ImageObject> mPageData = new PageData<ImageObject>();
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
					LogSystem.e(LOG_TAG, "parseSingerCategoryContent : input json result is error. the json is : " + json);
					return null;
				}
			} else {
				//返回数据为空异常，记录失败原因.需要分析真正原因
				LogSystem.e(LOG_TAG, "parseSingerCategoryContent : input json is null or json length is zero.");
				return null;
			}
		} catch (Exception e) {
			//格式化json串异常，返回数据格式不正确，需要分析真正原因.
			LogSystem.e(LOG_TAG, "parseSingerCategoryContent : parse json error. the json is : " + json);
			LogSystem.e(LOG_TAG, e.getMessage());
			return null;
		}
	}
	
	public PageData<Mtv> parseMtvListContent4Common(String json){
		PageData<Mtv> mPageData = new PageData<Mtv>();
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
					LogSystem.e(LOG_TAG, "parseMtvListContent4Common : input json result is error. the json is : " + json);
					return null;
				}
			} else {
				//返回数据为空异常，记录失败原因.需要分析真正原因
				LogSystem.e(LOG_TAG, "parseMtvListContent4Common : input json is null or json length is zero.");
				return null;
			}
		} catch (Exception e) {
			//格式化json串异常，返回数据格式不正确，需要分析真正原因.
			LogSystem.e(LOG_TAG, "parseMtvListContent4Common : parse json error. the json is : " + json);
			LogSystem.e(LOG_TAG, e.getMessage());
			return null;
		}
	}
	
	public PageData<UserMtv> parseMtvListContent4User(String jsonContent) {
		PageData<UserMtv> pagedData = new PageData<UserMtv>();
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
				LogSystem.e(LOG_TAG, "parse user mtv list error...");
				pagedData = null;
			}
		} catch (JSONException e) {
			LogSystem.e(LOG_TAG, e.toString());
			pagedData = null;
		}
		return pagedData;
	}
	
	public PageData<Mtv> parseMtvListContent4Language(String json){
		PageData<Mtv> mPageData = new PageData<Mtv>();
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
					LogSystem.e(LOG_TAG, "parseMtvListContent4Language : input json result is error. the json is : " + json);
					return null;
				}
			} else {
				//返回数据为空异常，记录失败原因.需要分析真正原因
				LogSystem.e(LOG_TAG, "parseMtvListContent4Language : input json is null or json length is zero.");
				return null;
			}
		} catch (Exception e) {
			//格式化json串异常，返回数据格式不正确，需要分析真正原因.
			LogSystem.e(LOG_TAG, "parseMtvListContent4Language : parse json error. the json is : " + json);
			LogSystem.e(LOG_TAG, e.getMessage());
			return null;
		}
	}
	
	public PageData<Mtv> parseMtvListContent4Singer(String jsonContent){
		if(TextUtils.isEmpty(jsonContent))
			return null;
		PageData<Mtv> mPageData = new PageData<Mtv>();
		String line = null;
		String id = null;
		String name = null;
		String artist = null;
		String haskdatx = null;
		String hasEcho = null;
		Mtv mtv = null;
		List<Mtv> mtvList = new ArrayList<Mtv>();
		BufferedReader reader = new BufferedReader(new StringReader(jsonContent));
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
	
	public PageData<ImageObject> parseSingerListContent(String json){
		PageData<ImageObject> mPageData = new PageData<ImageObject>();
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
					LogSystem.e(LOG_TAG, "parseSingerList : input json result is error. the json is : " + json);
					return null;
				}
			} else {
				//返回数据为空异常，记录失败原因.需要分析真正原因
				LogSystem.e(LOG_TAG, "parseSingerList : input json is null or json length is zero.");
				return null;
			}
		} catch (Exception e) {
			//格式化json串异常，返回数据格式不正确，需要分析真正原因.
			LogSystem.e(LOG_TAG, "parseSingerList : parse json error. the json is : " + json);
			LogSystem.e(LOG_TAG, e.getMessage());
			return null;
		}
	}
	
	public PageData<Mtv> parseMtvListContentBySearch(String json){
		PageData<Mtv> mPageData = new PageData<Mtv>();
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
					LogSystem.e(LOG_TAG, "parseMtvListContentBySearch : input json result is error. the json is : " + json);
					return null;
				}
			} else {
				//返回数据为空异常，记录失败原因.需要分析真正原因
				LogSystem.e(LOG_TAG, "parseMtvListContentBySearch : input json is null or json length is zero.");
				return null;
			}
		} catch (Exception e) {
			//格式化json串异常，返回数据格式不正确，需要分析真正原因.
			LogSystem.e(LOG_TAG, "parseMtvListContentBySearch : parse json error. the json is : " + json);
			LogSystem.e(LOG_TAG, e.getMessage());
			return null;
		}
	}
	
	public PageData<ImageObject> parseSingerListContentBySearch(String json){
		PageData<ImageObject> mPageData = new PageData<ImageObject>();
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
					LogSystem.e(LOG_TAG, "parseSingerListContentBySearch : input json result is error. the json is : " + json);
					return null;
				}
			} else {
				//返回数据为空异常，记录失败原因.需要分析真正原因
				LogSystem.e(LOG_TAG, "parseSingerListContentBySearch : input json is null or json length is zero.");
				return null;
			}
		} catch (Exception e) {
			//格式化json串异常，返回数据格式不正确，需要分析真正原因.
			LogSystem.e(LOG_TAG, "parseSingerListContentBySearch : parse json error. the json is : " + json);
			LogSystem.e(LOG_TAG, e.getMessage());
			return null;
		}
	}
	
	public PageData<String> parseHotKeywordContent(String json) {
		PageData<String> pagedData = new PageData<String>();
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
				LogSystem.e(LOG_TAG, "parseHotKeywordList : input json result is error. the json is : " + json);
				return null;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return pagedData;
	}
	
	public PageData<SearchPromptObject> parseSearchPromptContent(String content) {
		PageData<SearchPromptObject> pagedData = new PageData<SearchPromptObject>();
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
				LogSystem.e(LOG_TAG, "parse search prompt content error...");
				pagedData = null;
			}
		} catch (JSONException e) {
			LogSystem.e(LOG_TAG, e.toString());
			pagedData = null;
		}
		return pagedData;
	}
}