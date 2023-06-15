package com.mobileclient.service;

import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mobileclient.domain.WashShop;
import com.mobileclient.util.HttpUtil;

/*ϴ�µ����ҵ���߼���*/
public class WashShopService {
	/* ���ϴ�µ� */
	public String AddWashShop(WashShop washShop) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("shopUserName", washShop.getShopUserName());
		params.put("password", washShop.getPassword());
		params.put("shopName", washShop.getShopName());
		params.put("washClassObj", washShop.getWashClassObj() + "");
		params.put("shopPhoto", washShop.getShopPhoto());
		params.put("telephone", washShop.getTelephone());
		params.put("addDate", washShop.getAddDate().toString());
		params.put("address", washShop.getAddress());
		params.put("latitude", washShop.getLatitude() + "");
		params.put("longitude", washShop.getLongitude() + "");
		params.put("action", "add");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "WashShopServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* ��ѯϴ�µ� */
	public List<WashShop> QueryWashShop(WashShop queryConditionWashShop) throws Exception {
		String urlString = HttpUtil.BASE_URL + "WashShopServlet?action=query";
		if(queryConditionWashShop != null) {
			urlString += "&shopUserName=" + URLEncoder.encode(queryConditionWashShop.getShopUserName(), "UTF-8") + "";
			urlString += "&shopName=" + URLEncoder.encode(queryConditionWashShop.getShopName(), "UTF-8") + "";
			urlString += "&washClassObj=" + queryConditionWashShop.getWashClassObj();
			urlString += "&telephone=" + URLEncoder.encode(queryConditionWashShop.getTelephone(), "UTF-8") + "";
			if(queryConditionWashShop.getAddDate() != null) {
				urlString += "&addDate=" + URLEncoder.encode(queryConditionWashShop.getAddDate().toString(), "UTF-8");
			}
		}

		/* 2�����ݽ�����������һ������SAXParser����xml�ļ���ʽ
		URL url = new URL(urlString);
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();
		XMLReader xr = sp.getXMLReader();

		WashShopListHandler washShopListHander = new WashShopListHandler();
		xr.setContentHandler(washShopListHander);
		InputStreamReader isr = new InputStreamReader(url.openStream(), "UTF-8");
		InputSource is = new InputSource(isr);
		xr.parse(is);
		List<WashShop> washShopList = washShopListHander.getWashShopList();
		return washShopList;*/
		//��2���ǻ���json���ݸ�ʽ���������ǲ��õ��ǵ�2��
		List<WashShop> washShopList = new ArrayList<WashShop>();
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(urlString, null, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				WashShop washShop = new WashShop();
				washShop.setShopUserName(object.getString("shopUserName"));
				washShop.setPassword(object.getString("password"));
				washShop.setShopName(object.getString("shopName"));
				washShop.setWashClassObj(object.getInt("washClassObj"));
				washShop.setShopPhoto(object.getString("shopPhoto"));
				washShop.setTelephone(object.getString("telephone"));
				washShop.setAddDate(Timestamp.valueOf(object.getString("addDate")));
				washShop.setAddress(object.getString("address"));
				washShop.setLatitude((float) object.getDouble("latitude"));
				washShop.setLongitude((float) object.getDouble("longitude"));
				washShopList.add(washShop);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return washShopList;
	}

	/* ����ϴ�µ� */
	public String UpdateWashShop(WashShop washShop) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("shopUserName", washShop.getShopUserName());
		params.put("password", washShop.getPassword());
		params.put("shopName", washShop.getShopName());
		params.put("washClassObj", washShop.getWashClassObj() + "");
		params.put("shopPhoto", washShop.getShopPhoto());
		params.put("telephone", washShop.getTelephone());
		params.put("addDate", washShop.getAddDate().toString());
		params.put("address", washShop.getAddress());
		params.put("latitude", washShop.getLatitude() + "");
		params.put("longitude", washShop.getLongitude() + "");
		params.put("action", "update");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "WashShopServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* ɾ��ϴ�µ� */
	public String DeleteWashShop(String shopUserName) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("shopUserName", shopUserName);
		params.put("action", "delete");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "WashShopServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "ϴ�µ���Ϣɾ��ʧ��!";
		}
	}

	/* ����ϴ�µ��˺Ż�ȡϴ�µ���� */
	public WashShop GetWashShop(String shopUserName)  {
		List<WashShop> washShopList = new ArrayList<WashShop>();
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("shopUserName", shopUserName);
		params.put("action", "updateQuery");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "WashShopServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				WashShop washShop = new WashShop();
				washShop.setShopUserName(object.getString("shopUserName"));
				washShop.setPassword(object.getString("password"));
				washShop.setShopName(object.getString("shopName"));
				washShop.setWashClassObj(object.getInt("washClassObj"));
				washShop.setShopPhoto(object.getString("shopPhoto"));
				washShop.setTelephone(object.getString("telephone"));
				washShop.setAddDate(Timestamp.valueOf(object.getString("addDate")));
				washShop.setAddress(object.getString("address"));
				washShop.setLatitude((float) object.getDouble("latitude"));
				washShop.setLongitude((float) object.getDouble("longitude"));
				washShopList.add(washShop);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int size = washShopList.size();
		if(size>0) return washShopList.get(0); 
		else return null; 
	}
}
