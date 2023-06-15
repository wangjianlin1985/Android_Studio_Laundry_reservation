package com.mobileclient.util;

import java.util.List;  
import java.util.Map;

import com.mobileclient.service.WashClassService;
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

public class WashShopSimpleAdapter extends SimpleAdapter { 
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

    public WashShopSimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to,ListView listView) { 
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
	  if (convertView == null) convertView = mInflater.inflate(R.layout.washshop_list_item, null);
	  convertView.setTag("listViewTAG" + position);
	  holder = new ViewHolder(); 
	  /*绑定该view各个控件*/
	  holder.tv_shopUserName = (TextView)convertView.findViewById(R.id.tv_shopUserName);
	  holder.tv_shopName = (TextView)convertView.findViewById(R.id.tv_shopName);
	  holder.tv_washClassObj = (TextView)convertView.findViewById(R.id.tv_washClassObj);
	  holder.iv_shopPhoto = (ImageView)convertView.findViewById(R.id.iv_shopPhoto);
	  holder.tv_telephone = (TextView)convertView.findViewById(R.id.tv_telephone);
	  holder.tv_addDate = (TextView)convertView.findViewById(R.id.tv_addDate);
	  /*设置各个控件的展示内容*/
	  holder.tv_shopUserName.setText("洗衣店账号：" + mData.get(position).get("shopUserName").toString());
	  holder.tv_shopName.setText("洗衣店名称：" + mData.get(position).get("shopName").toString());
	  holder.tv_washClassObj.setText("洗衣店种类：" + (new WashClassService()).GetWashClass(Integer.parseInt(mData.get(position).get("washClassObj").toString())).getClassName());
	  holder.iv_shopPhoto.setImageResource(R.drawable.default_photo);
	  ImageLoadListener shopPhotoLoadListener = new ImageLoadListener(mListView,R.id.iv_shopPhoto);
	  syncImageLoader.loadImage(position,(String)mData.get(position).get("shopPhoto"),shopPhotoLoadListener);  
	  holder.tv_telephone.setText("店家电话：" + mData.get(position).get("telephone").toString());
	  try {holder.tv_addDate.setText("入驻日期：" + mData.get(position).get("addDate").toString().substring(0, 10));} catch(Exception ex){}
	  /*返回修改好的view*/
	  return convertView; 
    } 

    static class ViewHolder{ 
    	TextView tv_shopUserName;
    	TextView tv_shopName;
    	TextView tv_washClassObj;
    	ImageView iv_shopPhoto;
    	TextView tv_telephone;
    	TextView tv_addDate;
    }
} 
