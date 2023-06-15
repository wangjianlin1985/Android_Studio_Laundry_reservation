package com.mobileclient.util;

import java.util.List;  
import java.util.Map;

import com.mobileclient.service.WashMealService;
import com.mobileclient.service.UserInfoService;
import com.mobileclient.activity.R;
import com.mobileclient.imgCache.ImageLoadListener;
import com.mobileclient.imgCache.ListViewOnScrollListener;
import com.mobileclient.imgCache.SyncImageLoader;
import android.content.Context;
import android.view.LayoutInflater; 
import android.view.View;
import android.view.ViewGroup;  
import android.widget.ImageView; 
import android.widget.ListView;
import android.widget.SimpleAdapter; 
import android.widget.TextView; 

public class MealEvaluateSimpleAdapter extends SimpleAdapter { 
	/*��Ҫ�󶨵Ŀؼ���Դid*/
    private int[] mTo;
    /*map���Ϲؼ�������*/
    private String[] mFrom;
/*��Ҫ�󶨵�����*/
    private List<? extends Map<String, ?>> mData; 

    private LayoutInflater mInflater;
    Context context = null;

    private ListView mListView;
    //ͼƬ�첽���������,���ڴ滺����ļ�����
    private SyncImageLoader syncImageLoader;

    public MealEvaluateSimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to,ListView listView) { 
        super(context, data, resource, from, to); 
        mTo = to; 
        mFrom = from; 
        mData = data;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context= context;
        mListView = listView; 
        syncImageLoader = SyncImageLoader.getInstance();
        ListViewOnScrollListener onScrollListener = new ListViewOnScrollListener(syncImageLoader,listView,getCount());
        mListView.setOnScrollListener(onScrollListener);
    } 

  public View getView(int position, View convertView, ViewGroup parent) { 
	  ViewHolder holder = null;
	  ///*��һ��װ�����viewʱ=null,���½�һ������inflate��Ⱦһ��view*/
	  if (convertView == null) convertView = mInflater.inflate(R.layout.mealevaluate_list_item, null);
	  convertView.setTag("listViewTAG" + position);
	  holder = new ViewHolder(); 
	  /*�󶨸�view�����ؼ�*/
	  holder.tv_evaluateId = (TextView)convertView.findViewById(R.id.tv_evaluateId);
	  holder.tv_washMealObj = (TextView)convertView.findViewById(R.id.tv_washMealObj);
	  holder.tv_evaluateContent = (TextView)convertView.findViewById(R.id.tv_evaluateContent);
	  holder.tv_userObj = (TextView)convertView.findViewById(R.id.tv_userObj);
	  holder.tv_evaluateTime = (TextView)convertView.findViewById(R.id.tv_evaluateTime);
	  /*���ø����ؼ���չʾ����*/
	  holder.tv_evaluateId.setText("����id��" + mData.get(position).get("evaluateId").toString());
	  holder.tv_washMealObj.setText("�����ײͣ�" + (new WashMealService()).GetWashMeal(Integer.parseInt(mData.get(position).get("washMealObj").toString())).getMealName());
	  holder.tv_evaluateContent.setText("�������ݣ�" + mData.get(position).get("evaluateContent").toString());
	  holder.tv_userObj.setText("�����û���" + (new UserInfoService()).GetUserInfo(mData.get(position).get("userObj").toString()).getName());
	  holder.tv_evaluateTime.setText("����ʱ�䣺" + mData.get(position).get("evaluateTime").toString());
	  /*�����޸ĺõ�view*/
	  return convertView; 
    } 

    static class ViewHolder{ 
    	TextView tv_evaluateId;
    	TextView tv_washMealObj;
    	TextView tv_evaluateContent;
    	TextView tv_userObj;
    	TextView tv_evaluateTime;
    }
} 
