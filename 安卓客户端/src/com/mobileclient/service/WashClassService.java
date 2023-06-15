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

/*ϴ�µ��������ҵ���߼���*/
public class WashClassService {
	/* ���ϴ�µ����� */
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

	/* ��ѯϴ�µ����� */
	public List<WashClass> QueryWashClass(WashClass queryConditionWashClass) throws Exception {
		String urlString = HttpUtil.BASE_URL + "WashClassServlet?action=query";
		if(queryConditionWashClass != null) {
		}

		/* 2�����ݽ�����������һ������SAXParser����xml�ļ���ʽ
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
		//��2���ǻ���json���ݸ�ʽ���������ǲ��õ��ǵ�2��
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

	/* ����ϴ�µ����� */
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

	/* ɾ��ϴ�µ����� */
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
			return "ϴ�µ�������Ϣɾ��ʧ��!";
		}
	}

	/* ��������id��ȡϴ�µ�������� */
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
