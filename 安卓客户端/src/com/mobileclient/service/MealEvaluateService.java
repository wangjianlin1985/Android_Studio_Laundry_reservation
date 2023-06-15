package com.mobileclient.service;

import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mobileclient.domain.MealEvaluate;
import com.mobileclient.util.HttpUtil;

/*套餐评价管理业务逻辑层*/
public class MealEvaluateService {
	/* 添加套餐评价 */
	public String AddMealEvaluate(MealEvaluate mealEvaluate) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("evaluateId", mealEvaluate.getEvaluateId() + "");
		params.put("washMealObj", mealEvaluate.getWashMealObj() + "");
		params.put("evaluateContent", mealEvaluate.getEvaluateContent());
		params.put("userObj", mealEvaluate.getUserObj());
		params.put("evaluateTime", mealEvaluate.getEvaluateTime());
		params.put("action", "add");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "MealEvaluateServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 查询套餐评价 */
	public List<MealEvaluate> QueryMealEvaluate(MealEvaluate queryConditionMealEvaluate) throws Exception {
		String urlString = HttpUtil.BASE_URL + "MealEvaluateServlet?action=query";
		if(queryConditionMealEvaluate != null) {
			urlString += "&washMealObj=" + queryConditionMealEvaluate.getWashMealObj();
			urlString += "&userObj=" + URLEncoder.encode(queryConditionMealEvaluate.getUserObj(), "UTF-8") + "";
			urlString += "&evaluateTime=" + URLEncoder.encode(queryConditionMealEvaluate.getEvaluateTime(), "UTF-8") + "";
		}

		/* 2种数据解析方法，第一种是用SAXParser解析xml文件格式
		URL url = new URL(urlString);
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();
		XMLReader xr = sp.getXMLReader();

		MealEvaluateListHandler mealEvaluateListHander = new MealEvaluateListHandler();
		xr.setContentHandler(mealEvaluateListHander);
		InputStreamReader isr = new InputStreamReader(url.openStream(), "UTF-8");
		InputSource is = new InputSource(isr);
		xr.parse(is);
		List<MealEvaluate> mealEvaluateList = mealEvaluateListHander.getMealEvaluateList();
		return mealEvaluateList;*/
		//第2种是基于json数据格式解析，我们采用的是第2种
		List<MealEvaluate> mealEvaluateList = new ArrayList<MealEvaluate>();
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(urlString, null, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				MealEvaluate mealEvaluate = new MealEvaluate();
				mealEvaluate.setEvaluateId(object.getInt("evaluateId"));
				mealEvaluate.setWashMealObj(object.getInt("washMealObj"));
				mealEvaluate.setEvaluateContent(object.getString("evaluateContent"));
				mealEvaluate.setUserObj(object.getString("userObj"));
				mealEvaluate.setEvaluateTime(object.getString("evaluateTime"));
				mealEvaluateList.add(mealEvaluate);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mealEvaluateList;
	}

	/* 更新套餐评价 */
	public String UpdateMealEvaluate(MealEvaluate mealEvaluate) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("evaluateId", mealEvaluate.getEvaluateId() + "");
		params.put("washMealObj", mealEvaluate.getWashMealObj() + "");
		params.put("evaluateContent", mealEvaluate.getEvaluateContent());
		params.put("userObj", mealEvaluate.getUserObj());
		params.put("evaluateTime", mealEvaluate.getEvaluateTime());
		params.put("action", "update");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "MealEvaluateServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 删除套餐评价 */
	public String DeleteMealEvaluate(int evaluateId) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("evaluateId", evaluateId + "");
		params.put("action", "delete");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "MealEvaluateServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "套餐评价信息删除失败!";
		}
	}

	/* 根据评价id获取套餐评价对象 */
	public MealEvaluate GetMealEvaluate(int evaluateId)  {
		List<MealEvaluate> mealEvaluateList = new ArrayList<MealEvaluate>();
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("evaluateId", evaluateId + "");
		params.put("action", "updateQuery");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "MealEvaluateServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				MealEvaluate mealEvaluate = new MealEvaluate();
				mealEvaluate.setEvaluateId(object.getInt("evaluateId"));
				mealEvaluate.setWashMealObj(object.getInt("washMealObj"));
				mealEvaluate.setEvaluateContent(object.getString("evaluateContent"));
				mealEvaluate.setUserObj(object.getString("userObj"));
				mealEvaluate.setEvaluateTime(object.getString("evaluateTime"));
				mealEvaluateList.add(mealEvaluate);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int size = mealEvaluateList.size();
		if(size>0) return mealEvaluateList.get(0); 
		else return null; 
	}
}
