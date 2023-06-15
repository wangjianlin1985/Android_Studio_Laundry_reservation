package com.mobileclient.service;

import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mobileclient.domain.WashClass;
import com.mobileclient.util.HttpUtil;

/*洗衣店种类管理业务逻辑层*/
public class WashClassService {
	/* 添加洗衣店种类 */
	public String AddWashClass(WashClass washClass) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("classId", washClass.getClassId() + "");
		params.put("className", washClass.getClassName());
		params.put("classDesc", washClass.getClassDesc());
		params.put("action", "add");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "WashClassServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 查询洗衣店种类 */
	public List<WashClass> QueryWashClass(WashClass queryConditionWashClass) throws Exception {
		String urlString = HttpUtil.BASE_URL + "WashClassServlet?action=query";
		if(queryConditionWashClass != null) {
		}

		/* 2种数据解析方法，第一种是用SAXParser解析xml文件格式
		URL url = new URL(urlString);
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();
		XMLReader xr = sp.getXMLReader();

		WashClassListHandler washClassListHander = new WashClassListHandler();
		xr.setContentHandler(washClassListHander);
		InputStreamReader isr = new InputStreamReader(url.openStream(), "UTF-8");
		InputSource is = new InputSource(isr);
		xr.parse(is);
		List<WashClass> washClassList = washClassListHander.getWashClassList();
		return washClassList;*/
		//第2种是基于json数据格式解析，我们采用的是第2种
		List<WashClass> washClassList = new ArrayList<WashClass>();
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(urlString, null, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				WashClass washClass = new WashClass();
				washClass.setClassId(object.getInt("classId"));
				washClass.setClassName(object.getString("className"));
				washClass.setClassDesc(object.getString("classDesc"));
				washClassList.add(washClass);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return washClassList;
	}

	/* 更新洗衣店种类 */
	public String UpdateWashClass(WashClass washClass) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("classId", washClass.getClassId() + "");
		params.put("className", washClass.getClassName());
		params.put("classDesc", washClass.getClassDesc());
		params.put("action", "update");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "WashClassServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 删除洗衣店种类 */
	public String DeleteWashClass(int classId) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("classId", classId + "");
		params.put("action", "delete");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "WashClassServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "洗衣店种类信息删除失败!";
		}
	}

	/* 根据种类id获取洗衣店种类对象 */
	public WashClass GetWashClass(int classId)  {
		List<WashClass> washClassList = new ArrayList<WashClass>();
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("classId", classId + "");
		params.put("action", "updateQuery");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "WashClassServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				WashClass washClass = new WashClass();
				washClass.setClassId(object.getInt("classId"));
				washClass.setClassName(object.getString("className"));
				washClass.setClassDesc(object.getString("classDesc"));
				washClassList.add(washClass);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int size = washClassList.size();
		if(size>0) return washClassList.get(0); 
		else return null; 
	}
}
