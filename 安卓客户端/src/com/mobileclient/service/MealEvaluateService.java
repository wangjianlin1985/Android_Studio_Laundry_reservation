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

/*�ײ����۹���ҵ���߼���*/
public class MealEvaluateService {
	/* ����ײ����� */
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

	/* ��ѯ�ײ����� */
	public List<MealEvaluate> QueryMealEvaluate(MealEvaluate queryConditionMealEvaluate) throws Exception {
		String urlString = HttpUtil.BASE_URL + "MealEvaluateServlet?action=query";
		if(queryConditionMealEvaluate != null) {
			urlString += "&washMealObj=" + queryConditionMealEvaluate.getWashMealObj();
			urlString += "&userObj=" + URLEncoder.encode(queryConditionMealEvaluate.getUserObj(), "UTF-8") + "";
			urlString += "&evaluateTime=" + URLEncoder.encode(queryConditionMealEvaluate.getEvaluateTime(), "UTF-8") + "";
		}

		/* 2�����ݽ�����������һ������SAXParser����xml�ļ���ʽ
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
		//��2���ǻ���json���ݸ�ʽ���������ǲ��õ��ǵ�2��
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

	/* �����ײ����� */
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

	/* ɾ���ײ����� */
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
			return "�ײ�������Ϣɾ��ʧ��!";
		}
	}

	/* ��������id��ȡ�ײ����۶��� */
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
