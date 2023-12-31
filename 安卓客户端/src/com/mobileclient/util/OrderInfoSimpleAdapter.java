package com.mobileclient.util;

import java.util.List;  
import java.util.Map;

import com.mobileclient.service.WashMealService;
import com.mobileclient.service.UserInfoService;
import com.mobileclient.service.OrderStateService;
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

public class OrderInfoSimpleAdapter extends SimpleAdapter { 
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

    public OrderInfoSimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to,ListView listView) { 
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
	  if (convertView == null) convertView = mInflater.inflate(R.layout.orderinfo_list_item, null);
	  convertView.setTag("listViewTAG" + position);
	  holder = new ViewHolder(); 
	  /*绑定该view各个控件*/
	  holder.tv_washMealObj = (TextView)convertView.findViewById(R.id.tv_washMealObj);
	  holder.tv_orderCount = (TextView)convertView.findViewById(R.id.tv_orderCount);
	  holder.tv_userObj = (TextView)convertView.findViewById(R.id.tv_userObj);
	  holder.tv_telephone = (TextView)convertView.findViewById(R.id.tv_telephone);
	  holder.tv_orderTime = (TextView)convertView.findViewById(R.id.tv_orderTime);
	  holder.tv_orderState = (TextView)convertView.findViewById(R.id.tv_orderState);
	  /*设置各个控件的展示内容*/
	  holder.tv_washMealObj.setText("洗衣套餐：" + (new WashMealService()).GetWashMeal(Integer.parseInt(mData.get(position).get("washMealObj").toString())).getMealName());
	  holder.tv_orderCount.setText("预订数量：" + mData.get(position).get("orderCount").toString());
	  holder.tv_userObj.setText("下单用户：" + (new UserInfoService()).GetUserInfo(mData.get(position).get("userObj").toString()).getName());
	  holder.tv_telephone.setText("联系电话：" + mData.get(position).get("telephone").toString());
	  holder.tv_orderTime.setText("下单时间：" + mData.get(position).get("orderTime").toString());
	  holder.tv_orderState.setText("订单状态：" + (new OrderStateService()).GetOrderState(Integer.parseInt(mData.get(position).get("orderState").toString())).getStateName());
	  /*返回修改好的view*/
	  return convertView; 
    } 

    static class ViewHolder{ 
    	TextView tv_washMealObj;
    	TextView tv_orderCount;
    	TextView tv_userObj;
    	TextView tv_telephone;
    	TextView tv_orderTime;
    	TextView tv_orderState;
    }
} 
