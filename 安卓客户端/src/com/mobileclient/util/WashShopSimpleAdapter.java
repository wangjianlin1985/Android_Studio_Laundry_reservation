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
	  ///*��һ��װ�����viewʱ=null,���½�һ������inflate��Ⱦһ��view*/
	  if (convertView == null) convertView = mInflater.inflate(R.layout.washshop_list_item, null);
	  convertView.setTag("listViewTAG" + position);
	  holder = new ViewHolder(); 
	  /*�󶨸�view�����ؼ�*/
	  holder.tv_shopUserName = (TextView)convertView.findViewById(R.id.tv_shopUserName);
	  holder.tv_shopName = (TextView)convertView.findViewById(R.id.tv_shopName);
	  holder.tv_washClassObj = (TextView)convertView.findViewById(R.id.tv_washClassObj);
	  holder.iv_shopPhoto = (ImageView)convertView.findViewById(R.id.iv_shopPhoto);
	  holder.tv_telephone = (TextView)convertView.findViewById(R.id.tv_telephone);
	  holder.tv_addDate = (TextView)convertView.findViewById(R.id.tv_addDate);
	  /*���ø����ؼ���չʾ����*/
	  holder.tv_shopUserName.setText("ϴ�µ��˺ţ�" + mData.get(position).get("shopUserName").toString());
	  holder.tv_shopName.setText("ϴ�µ����ƣ�" + mData.get(position).get("shopName").toString());
	  holder.tv_washClassObj.setText("ϴ�µ����ࣺ" + (new WashClassService()).GetWashClass(Integer.parseInt(mData.get(position).get("washClassObj").toString())).getClassName());
	  holder.iv_shopPhoto.setImageResource(R.drawable.default_photo);
	  ImageLoadListener shopPhotoLoadListener = new ImageLoadListener(mListView,R.id.iv_shopPhoto);
	  syncImageLoader.loadImage(position,(String)mData.get(position).get("shopPhoto"),shopPhotoLoadListener);  
	  holder.tv_telephone.setText("��ҵ绰��" + mData.get(position).get("telephone").toString());
	  try {holder.tv_addDate.setText("��פ���ڣ�" + mData.get(position).get("addDate").toString().substring(0, 10));} catch(Exception ex){}
	  /*�����޸ĺõ�view*/
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
