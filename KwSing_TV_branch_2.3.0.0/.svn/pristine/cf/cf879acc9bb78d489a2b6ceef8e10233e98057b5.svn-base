/**
 * Copyright (c) 2013, FightingTime, Inc. All rights reserved. 
 */
package cn.kuwo.sing.tv.socket;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
import cn.kuwo.sing.tv.bean.Mtv;
import cn.kuwo.sing.tv.bean.UserMtv;

/**
 * @Package cn.kuwo.sing.tv.bean
 * 
 * @Date Mar 17, 2014 12:05:39 PM
 *
 * @Author wangming
 *
 */
public class MessageCommons{
	private static final int CMD_BASE_VALUE_NOT_PLAYING = 100;
	public static final int CMD_CONNECT = CMD_BASE_VALUE_NOT_PLAYING + 1;
	public static final int CMD_ADD = CMD_BASE_VALUE_NOT_PLAYING + 2;
	public static final int CMD_PLAY = CMD_BASE_VALUE_NOT_PLAYING + 3;
	public static final int CMD_ORDERED_MTV = CMD_BASE_VALUE_NOT_PLAYING + 4;
	public static final int CMD_ORDERED_MTV_WRITE = CMD_BASE_VALUE_NOT_PLAYING + 5;
	public static final int CMD_ORDERED_PLAY = CMD_BASE_VALUE_NOT_PLAYING + 6;
	public static final int CMD_ORDERED_TOP = CMD_BASE_VALUE_NOT_PLAYING + 7;
	public static final int CMD_ORDERED_DELETE = CMD_BASE_VALUE_NOT_PLAYING + 8;	
	
	private static final int CMD_BASE_VALUE_RESULT = 200;
	public static final int RESULT_SUCCESS = CMD_BASE_VALUE_RESULT + 1;
	public static final int RESULT_IS_PLAYING = CMD_BASE_VALUE_RESULT + 2;
	public static final int RESULT_IS_NOT_PLAYING = CMD_BASE_VALUE_RESULT + 3;
	public static final int RESULT_IS_PAUSE = CMD_BASE_VALUE_RESULT + 4;
	public static final int RESULT_CONNECT_SUCCESS = CMD_BASE_VALUE_RESULT + 5;
	public static final int RESULT_CONNECT_FAIL = CMD_BASE_VALUE_RESULT + 6;
	public static final int RESULT_SERVER_SHUTDOWN = CMD_BASE_VALUE_RESULT + 7;
	public static final int RESULT_TO_SWITCH_ORIGINAL = CMD_BASE_VALUE_RESULT + 8;
	public static final int RESULT_TO_SWITCH_ACCOMP = CMD_BASE_VALUE_RESULT + 9;
	public static final int RESULT_TO_PLAY = CMD_BASE_VALUE_RESULT + 10;
	public static final int RESULT_TO_PAUSE = CMD_BASE_VALUE_RESULT + 11;
	public static final int RESULT_ORDERED_MTV_TOP = CMD_BASE_VALUE_RESULT + 12;
	public static final int RESULT_ORDERED_MTV_DELETE = CMD_BASE_VALUE_RESULT + 13;
	
	private static final int CMD_BASE_VALUE_PLAYING = 2000;
	public static final int CMD_TO_PLAY = CMD_BASE_VALUE_PLAYING + 1;
	public static final int CMD_TO_PAUSE = CMD_BASE_VALUE_PLAYING + 2;
	public static final int CMD_TO_PRE_MTV = CMD_BASE_VALUE_PLAYING + 3;
	public static final int CMD_TO_NEXT_MTV = CMD_BASE_VALUE_PLAYING + 4;
	public static final int CMD_TO_SWITCH_ORIGINAL = CMD_BASE_VALUE_PLAYING + 5;
	public static final int CMD_TO_SWITCH_ACCOMP = CMD_BASE_VALUE_PLAYING + 6;
	public static final int CMD_ADD_MICPHONE_VOLUME = CMD_BASE_VALUE_PLAYING + 7;
	public static final int CMD_DEC_MICPHONE_VOLUME = CMD_BASE_VALUE_PLAYING + 8;
	public static final int CMD_ADD_ACCOMP_VOLUME = CMD_BASE_VALUE_PLAYING + 9;
	public static final int CMD_DEC_ACCOMP_VOLUME = CMD_BASE_VALUE_PLAYING + 10;
	public static final int CMD_TO_SCORE = CMD_BASE_VALUE_PLAYING + 11;
	public static final int CMD_TO_REPLAY = CMD_BASE_VALUE_PLAYING + 12;
	
	public static String parseRequestMessageToJson(RequestMessage requestMessage) {
		String json = null;
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("cmd", requestMessage.cmd);
			jsonObject.put("extraMessage", requestMessage.extraMessage);
			if(requestMessage.data == null) {
				jsonObject.put("data", new JSONObject());
			}else {
				JSONObject mtv = new JSONObject();
				mtv.put("date", requestMessage.data.date);
				mtv.put("rid", requestMessage.data.rid);
				mtv.put("name", requestMessage.data.name);
				mtv.put("artist", requestMessage.data.artist);
				mtv.put("hasEcho", requestMessage.data.hasEcho);
				mtv.put("hasKdatx", requestMessage.data.hasKdatx);
				jsonObject.put("data", mtv);
			}
			if(requestMessage.userData == null) {
				jsonObject.put("userData", new JSONObject());
			}else {
				JSONObject userMtv = new JSONObject();
				userMtv.put("uid", requestMessage.userData.uid);
				userMtv.put("id", requestMessage.userData.id);
				userMtv.put("rid", requestMessage.userData.rid);
				userMtv.put("title", requestMessage.userData.title);
				userMtv.put("upic", requestMessage.userData.upic);
				userMtv.put("uname", requestMessage.userData.uname);
				userMtv.put("view", requestMessage.userData.view);
				userMtv.put("pic", requestMessage.userData.pic);
				userMtv.put("flower", requestMessage.userData.flower);
				userMtv.put("mlogurl", requestMessage.userData.mlogurl);
				userMtv.put("mediatype", requestMessage.userData.mediatype);
				jsonObject.put("userData", userMtv);
			}
			jsonObject.put("time", requestMessage.time);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		json = jsonObject.toString();
		return json;
	}
	
	public static String parseResponseMessageToJson(ResponseMessage responseMessage) {
		String json = null;
		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put("cmd", responseMessage.cmd);
			jsonObj.put("result", responseMessage.result);
			jsonObj.put("time", responseMessage.time);
			JSONArray orderedMtvArray = new JSONArray();
			switch (responseMessage.cmd) {
			case MessageCommons.CMD_ORDERED_MTV_WRITE:
				if(responseMessage.data != null) {
					List<Mtv> mtvList = (List<Mtv>) responseMessage.data;
					for(int i = 0; i <= mtvList.size()-1; i++) {
						Mtv mtv = mtvList.get(i);
						JSONObject mtvObj = new JSONObject();
						mtvObj.put("date", mtv.date);
						mtvObj.put("rid", mtv.rid);
						mtvObj.put("name", mtv.name);
						mtvObj.put("artist", mtv.artist);
						mtvObj.put("hasEcho", mtv.hasEcho);
						mtvObj.put("hasKdatx", mtv.hasKdatx);
						orderedMtvArray.put(i, mtvObj);
					}
				}
				jsonObj.put("data", orderedMtvArray);
				break;

			default:
				break;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		json = jsonObj.toString();
		return json;
	}
	
	public static RequestMessage parseJsonToRequestMessage(String json) {
		RequestMessage requestMessage = new RequestMessage();
		if(TextUtils.isEmpty(json))
			return requestMessage;
		try {
			JSONObject jsonObject = new JSONObject(json);
			requestMessage.cmd = jsonObject.getInt("cmd");
			requestMessage.extraMessage = jsonObject.getString("extraMessage");
			requestMessage.time = jsonObject.getLong("time");
			if(jsonObject.getJSONObject("data").isNull("rid")) {
				requestMessage.data = null;
			}else {
				JSONObject mtvObj = jsonObject.getJSONObject("data");
				Mtv mtv = new Mtv();
				mtv.date = mtvObj.getLong("date");
				mtv.rid = mtvObj.getString("rid");
				mtv.name = mtvObj.getString("name");
				mtv.artist = mtvObj.getString("artist");
				mtv.hasEcho = mtvObj.getBoolean("hasEcho");
				mtv.hasKdatx = mtvObj.getBoolean("hasKdatx");
				requestMessage.data = mtv;
			}
			if(jsonObject.getJSONObject("userData").isNull("rid")) {
				requestMessage.userData = null;
			}else {
				JSONObject mtvObj = jsonObject.getJSONObject("userData");
				UserMtv userMtv = new UserMtv();
				userMtv.uid = mtvObj.getString("uid");
				userMtv.id = mtvObj.getString("id");
				userMtv.rid = mtvObj.getString("rid");
				userMtv.title = mtvObj.getString("title");
				userMtv.upic = mtvObj.getString("upic");
				userMtv.uname = mtvObj.getString("uname");
				userMtv.view = mtvObj.getInt("view");
				userMtv.pic = mtvObj.getString("pic");
				userMtv.flower = mtvObj.getInt("flower");
				userMtv.mlogurl = mtvObj.getString("mlogurl");
				userMtv.mediatype = mtvObj.getInt("mediatype");
				requestMessage.userData = userMtv;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return requestMessage;
		}
		return requestMessage;
	}
	
	public static ResponseMessage parseJsonToResponseMessage(String json) {
		ResponseMessage responseMessage = new ResponseMessage();
		if(TextUtils.isEmpty(json))
			return responseMessage;
		try {
			JSONObject jsonObj = new JSONObject(json);
			responseMessage.cmd = jsonObj.getInt("cmd");
			responseMessage.result = jsonObj.getInt("result");
			responseMessage.time = jsonObj.getLong("time");
			List<Mtv> orderedMtvList = new ArrayList<Mtv>();
			switch (responseMessage.cmd) {
			case MessageCommons.CMD_ORDERED_MTV_WRITE:
				JSONArray orderedMtvArray = jsonObj.getJSONArray("data");
				for(int i = 0; i <= orderedMtvArray.length()-1; i++) {
					JSONObject mtvObj = orderedMtvArray.getJSONObject(i);
					Mtv mtv = new Mtv();
					mtv.date = mtvObj.getLong("date");
					mtv.rid = mtvObj.getString("rid");
					mtv.name = mtvObj.getString("name");
					mtv.artist = mtvObj.getString("artist");
					mtv.hasEcho = mtvObj.getBoolean("hasEcho");
					mtv.hasKdatx = mtvObj.getBoolean("hasKdatx");
					orderedMtvList.add(mtv);
				}
				break;

			default:
				break;
			}
			responseMessage.data = orderedMtvList;
		} catch (JSONException e) {
			e.printStackTrace();
			return responseMessage;
		}
		return responseMessage;
	}
	
}
