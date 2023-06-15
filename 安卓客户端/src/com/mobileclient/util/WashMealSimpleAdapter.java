package com.mobileclient.util;

import java.util.List;  
import java.util.Map;

import com.mobileclient.service.WashShopService;
import com.mobileclient.activity.R;
import com.mobileclient.app.Declare;
import com.mobileclient.imgCache.ImageLoadListener;
import com.mobileclient.imgCache.ListViewOnScrollListener;
import com.mobileclient.imgCache.SyncImageLoader;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater; 
import android.view.View;
import android.view.ViewGroup;  
import android.widget.ImageView; 
import android.widget.ListView;
import android.widget.SimpleAdapter; 
import android.widget.TextView; 

public class WashMealSimpleAdapter extends SimpleAdapter { 
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

    public WashMealSimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to,ListView listView) { 
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
	  if (convertView == null) convertView = mInflater.inflate(R.layout.washmeal_list_item, null);
	  convertView.setTag("listViewTAG" + position);
	  holder = new ViewHolder(); 
	  /*�󶨸�view�����ؼ�*/
	  holder.tv_mealName = (TextView)convertView.findViewById(R.id.tv_mealName);
	  holder.tv_price = (TextView)convertView.findViewById(R.id.tv_price);
	  holder.iv_mealPhoto = (ImageView)convertView.findViewById(R.id.iv_mealPhoto);
	  holder.tv_publishDate = (TextView)convertView.findViewById(R.id.tv_publishDate);
	  holder.tv_washShopObj = (TextView)convertView.findViewById(R.id.tv_washShopObj);
	  holder.tv_distance = (TextView)convertView.findViewById(R.id.tv_distance);
	  /*���ø����ؼ���չʾ����*/
	  holder.tv_mealName.setText("ϴ���ײͣ�" + mData.get(position).get("mealName").toString());
	  holder.tv_price.setText("�ײͼ۸�" + mData.get(position).get("price").toString());
	  holder.iv_mealPhoto.setImageResource(R.drawable.default_photo);
	  ImageLoadListener mealPhotoLoadListener = new ImageLoadListener(mListView,R.id.iv_mealPhoto);
	  syncImageLoader.loadImage(position,(String)mData.get(position).get("mealPhoto"),mealPhotoLoadListener);  
	  try {holder.tv_publishDate.setText("�������ڣ�" + mData.get(position).get("publishDate").toString().substring(0, 10));} catch(Exception ex){}
	  holder.tv_washShopObj.setText("ϴ�µ꣺" + (new WashShopService()).GetWashShop(mData.get(position).get("washShopObj").toString()).getShopName());
	  
	   
	  Declare declare = (Declare)((Activity)context).getApplication();
	  if(declare.getIdentify().equals("user")) {
		  holder.tv_distance.setVisibility(View.VISIBLE);
		  holder.tv_distance.setText("���룺" + mData.get(position).get("distance"));
	  }
	  
	  
	  /*�����޸ĺõ�view*/
	  return convertView; 
    } 

    static class ViewHolder{ 
    	TextView tv_mealName;
    	TextView tv_price;
    	ImageView iv_mealPhoto;
    	TextView tv_publishDate;
    	TextView tv_washShopObj;
    	TextView tv_distance;
    }
} 
