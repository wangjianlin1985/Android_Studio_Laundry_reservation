package com.mobileclient.service;

import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mobileclient.domain.WashMeal;
import com.mobileclient.util.HttpUtil;

/*洗衣套餐管理业务逻辑层*/
public class WashMealService {
	/* 添加洗衣套餐 */
	public String AddWashMeal(WashMeal washMeal) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("mealId", washMeal.getMealId() + "");
		params.put("mealName", washMeal.getMealName());
		params.put("introduce", washMeal.getIntroduce());
		params.put("price", washMeal.getPrice() + "");
		params.put("mealPhoto", washMeal.getMealPhoto());
		params.put("publishDate", washMeal.getPublishDate().toString());
		params.put("washShopObj", washMeal.getWashShopObj());
		params.put("action", "add");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "WashMealServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 查询洗衣套餐 */
	public List<WashMeal> QueryWashMeal(WashMeal queryConditionWashMeal) throws Exception {
		String urlString = HttpUtil.BASE_URL + "WashMealServlet?action=query";
		if(queryConditionWashMeal != null) {
			urlString += "&mealName=" + URLEncoder.encode(queryConditionWashMeal.getMealName(), "UTF-8") + "";
			if(queryConditionWashMeal.getPublishDate() != null) {
				urlString += "&publishDate=" + URLEncoder.encode(queryConditionWashMeal.getPublishDate().toString(), "UTF-8");
			}
			urlString += "&washShopObj=" + URLEncoder.encode(queryConditionWashMeal.getWashShopObj(), "UTF-8") + "";
		}

		/* 2种数据解析方法，第一种是用SAXParser解析xml文件格式
		URL url = new URL(urlString);
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();
		XMLReader xr = sp.getXMLReader();

		WashMealListHandler washMealListHander = new WashMealListHandler();
		xr.setContentHandler(washMealListHander);
		InputStreamReader isr = new InputStreamReader(url.openStream(), "UTF-8");
		InputSource is = new InputSource(isr);
		xr.parse(is);
		List<WashMeal> washMealList = washMealListHander.getWashMealList();
		return washMealList;*/
		//第2种是基于json数据格式解析，我们采用的是第2种
		List<WashMeal> washMealList = new ArrayList<WashMeal>();
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(urlString, null, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				WashMeal washMeal = new WashMeal();
				washMeal.setMealId(object.getInt("mealId"));
				washMeal.setMealName(object.getString("mealName"));
				washMeal.setIntroduce(object.getString("introduce"));
				washMeal.setPrice((float) object.getDouble("price"));
				washMeal.setMealPhoto(object.getString("mealPhoto"));
				washMeal.setPublishDate(Timestamp.valueOf(object.getString("publishDate")));
				washMeal.setWashShopObj(object.getString("washShopObj"));
				washMealList.add(washMeal);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return washMealList;
	}
	
	
	/* 店家查询自家的洗衣套餐 */
	public List<WashMeal> shopQueryWashMeal(String shopUserName) throws Exception {
		String urlString = HttpUtil.BASE_URL + "WashMealServlet?action=shopQuery";
		urlString += "&shopUserName=" + shopUserName;
		 
		List<WashMeal> washMealList = new ArrayList<WashMeal>();
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(urlString, null, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				WashMeal washMeal = new WashMeal();
				washMeal.setMealId(object.getInt("mealId"));
				washMeal.setMealName(object.getString("mealName"));
				washMeal.setIntroduce(object.getString("introduce"));
				washMeal.setPrice((float) object.getDouble("price"));
				washMeal.setMealPhoto(object.getString("mealPhoto"));
				washMeal.setPublishDate(Timestamp.valueOf(object.getString("publishDate")));
				washMeal.setWashShopObj(object.getString("washShopObj"));
				washMealList.add(washMeal);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return washMealList;
	}
	
	
	
	
	/* 查询用户查询附近的洗衣套餐 */
	public List<WashMeal> userQueryWashMeal(String userName,WashMeal queryConditionWashMeal) throws Exception {
		String urlString = HttpUtil.BASE_URL + "WashMealServlet?action=userQuery&userName=" + userName;
		if(queryConditionWashMeal != null) {
			urlString += "&mealName=" + URLEncoder.encode(queryConditionWashMeal.getMealName(), "UTF-8") + "";
			if(queryConditionWashMeal.getPublishDate() != null) {
				urlString += "&publishDate=" + URLEncoder.encode(queryConditionWashMeal.getPublishDate().toString(), "UTF-8");
			}
			urlString += "&washClassObj=" + queryConditionWashMeal.getWashClassObj();
			urlString += "&orderRule=" + queryConditionWashMeal.getOrderRule();
		}

		/* 2种数据解析方法，第一种是用SAXParser解析xml文件格式
		URL url = new URL(urlString);
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();
		XMLReader xr = sp.getXMLReader();

		WashMealListHandler washMealListHander = new WashMealListHandler();
		xr.setContentHandler(washMealListHander);
		InputStreamReader isr = new InputStreamReader(url.openStream(), "UTF-8");
		InputSource is = new InputSource(isr);
		xr.parse(is);
		List<WashMeal> washMealList = washMealListHander.getWashMealList();
		return washMealList;*/
		//第2种是基于json数据格式解析，我们采用的是第2种
		List<WashMeal> washMealList = new ArrayList<WashMeal>();
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(urlString, null, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				WashMeal washMeal = new WashMeal();
				washMeal.setMealId(object.getInt("mealId"));
				washMeal.setMealName(object.getString("mealName"));
				washMeal.setIntroduce(object.getString("introduce"));
				washMeal.setPrice((float) object.getDouble("price"));
				washMeal.setMealPhoto(object.getString("mealPhoto"));
				washMeal.setPublishDate(Timestamp.valueOf(object.getString("publishDate")));
				washMeal.setWashShopObj(object.getString("washShopObj"));
				washMeal.setDistance(object.getDouble("distance"));
				washMealList.add(washMeal);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return washMealList;
	}
	
	
	

	/* 更新洗衣套餐 */
	public String UpdateWashMeal(WashMeal washMeal) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("mealId", washMeal.getMealId() + "");
		params.put("mealName", washMeal.getMealName());
		params.put("introduce", washMeal.getIntroduce());
		params.put("price", washMeal.getPrice() + "");
		params.put("mealPhoto", washMeal.getMealPhoto());
		params.put("publishDate", washMeal.getPublishDate().toString());
		params.put("washShopObj", washMeal.getWashShopObj());
		params.put("action", "update");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "WashMealServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 删除洗衣套餐 */
	public String DeleteWashMeal(int mealId) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("mealId", mealId + "");
		params.put("action", "delete");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "WashMealServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "洗衣套餐信息删除失败!";
		}
	}

	/* 根据套餐id获取洗衣套餐对象 */
	public WashMeal GetWashMeal(int mealId)  {
		List<WashMeal> washMealList = new ArrayList<WashMeal>();
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("mealId", mealId + "");
		params.put("action", "updateQuery");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "WashMealServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				WashMeal washMeal = new WashMeal();
				washMeal.setMealId(object.getInt("mealId"));
				washMeal.setMealName(object.getString("mealName"));
				washMeal.setIntroduce(object.getString("introduce"));
				washMeal.setPrice((float) object.getDouble("price"));
				washMeal.setMealPhoto(object.getString("mealPhoto"));
				washMeal.setPublishDate(Timestamp.valueOf(object.getString("publishDate")));
				washMeal.setWashShopObj(object.getString("washShopObj"));
				washMealList.add(washMeal);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int size = washMealList.size();
		if(size>0) return washMealList.get(0); 
		else return null; 
	}
}
