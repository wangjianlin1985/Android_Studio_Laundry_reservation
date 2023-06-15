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
	/*需要绑定的控件资源id*/
    private int[] mTo;
    /*map集合关键字数组*/
    private String[] mFrom;
/*需要绑定的数据*/
    private List<? extends Map<String, ?>> mData; 

    private LayoutInflater mInflater;
    Context context = null;

    private ListView mListView;
    //图片异步缓存加载类,带内存缓存和文件缓存
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
	  ///*第一次装载这个view时=null,就新建一个调用inflate渲染一个view*/
	  if (convertView == null) convertView = mInflater.inflate(R.layout.washmeal_list_item, null);
	  convertView.setTag("listViewTAG" + position);
	  holder = new ViewHolder(); 
	  /*绑定该view各个控件*/
	  holder.tv_mealName = (TextView)convertView.findViewById(R.id.tv_mealName);
	  holder.tv_price = (TextView)convertView.findViewById(R.id.tv_price);
	  holder.iv_mealPhoto = (ImageView)convertView.findViewById(R.id.iv_mealPhoto);
	  holder.tv_publishDate = (TextView)convertView.findViewById(R.id.tv_publishDate);
	  holder.tv_washShopObj = (TextView)convertView.findViewById(R.id.tv_washShopObj);
	  holder.tv_distance = (TextView)convertView.findViewById(R.id.tv_distance);
	  /*设置各个控件的展示内容*/
	  holder.tv_mealName.setText("洗衣套餐：" + mData.get(position).get("mealName").toString());
	  holder.tv_price.setText("套餐价格：" + mData.get(position).get("price").toString());
	  holder.iv_mealPhoto.setImageResource(R.drawable.default_photo);
	  ImageLoadListener mealPhotoLoadListener = new ImageLoadListener(mListView,R.id.iv_mealPhoto);
	  syncImageLoader.loadImage(position,(String)mData.get(position).get("mealPhoto"),mealPhotoLoadListener);  
	  try {holder.tv_publishDate.setText("发布日期：" + mData.get(position).get("publishDate").toString().substring(0, 10));} catch(Exception ex){}
	  holder.tv_washShopObj.setText("洗衣店：" + (new WashShopService()).GetWashShop(mData.get(position).get("washShopObj").toString()).getShopName());
	  
	   
	  Declare declare = (Declare)((Activity)context).getApplication();
	  if(declare.getIdentify().equals("user")) {
		  holder.tv_distance.setVisibility(View.VISIBLE);
		  holder.tv_distance.setText("距离：" + mData.get(position).get("distance"));
	  }
	  
	  
	  /*返回修改好的view*/
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
