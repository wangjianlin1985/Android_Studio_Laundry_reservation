package com.mobileclient.activity;

import java.util.Date;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.map.BaiduMap.OnMarkerDragListener;
import com.baidu.mapapi.model.LatLng;
import com.mobileclient.domain.WashShop;
import com.mobileclient.service.WashShopService;
import com.mobileclient.domain.WashClass;
import com.mobileclient.service.WashClassService;
import com.mobileclient.util.HttpUtil;
import com.mobileclient.util.ImageService;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import android.widget.Toast;
public class WashShopDetailActivity extends Activity {
	// 声明返回按钮
	private Button btnReturn;
	// 声明洗衣店账号控件
	private TextView TV_shopUserName;
	// 声明登录密码控件
	private TextView TV_password;
	// 声明洗衣店名称控件
	private TextView TV_shopName;
	// 声明洗衣店种类控件
	private TextView TV_washClassObj;
	// 声明洗衣店照片图片框
	private ImageView iv_shopPhoto;
	// 声明店家电话控件
	private TextView TV_telephone;
	// 声明入驻日期控件
	private TextView TV_addDate;
	// 声明店铺地址控件
	private TextView TV_address;
	// 声明纬度控件
	private TextView TV_latitude;
	// 声明经度控件
	private TextView TV_longitude;
	/* 要保存的洗衣店信息 */
	WashShop washShop = new WashShop(); 
	/* 洗衣店管理业务逻辑层 */
	private WashShopService washShopService = new WashShopService();
	private WashClassService washClassService = new WashClassService();
	private String shopUserName;
	MapView mMapView = null;
	BaiduMap mBaiduMap = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN); 
		// 设置当前Activity界面布局
		setContentView(R.layout.washshop_detail);
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("查看洗衣店详情");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		// 通过findViewById方法实例化组件
		btnReturn = (Button) findViewById(R.id.btnReturn);
		TV_shopUserName = (TextView) findViewById(R.id.TV_shopUserName);
		TV_password = (TextView) findViewById(R.id.TV_password);
		TV_shopName = (TextView) findViewById(R.id.TV_shopName);
		TV_washClassObj = (TextView) findViewById(R.id.TV_washClassObj);
		iv_shopPhoto = (ImageView) findViewById(R.id.iv_shopPhoto); 
		TV_telephone = (TextView) findViewById(R.id.TV_telephone);
		TV_addDate = (TextView) findViewById(R.id.TV_addDate);
		TV_address = (TextView) findViewById(R.id.TV_address);
		TV_latitude = (TextView) findViewById(R.id.TV_latitude);
		TV_longitude = (TextView) findViewById(R.id.TV_longitude);
		Bundle extras = this.getIntent().getExtras();
		shopUserName = extras.getString("shopUserName");
		btnReturn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				WashShopDetailActivity.this.finish();
			}
		}); 
		initViewData();
		
		mMapView = (MapView) findViewById(R.id.bmapView);
		InitCenter();
		AddMarker();
		
	}
	
	private void AddMarker() {
		mBaiduMap.clear();
		LatLng point = new LatLng(washShop.getLatitude(), washShop.getLongitude()); 
		//构建Marker图标
		BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_geo);   
		//在地图上添加Marker，并显示     
		//构建MarkerOption，用于在地图上添加Marker   
		MarkerOptions options = new MarkerOptions()  
		    .position(point) 
		    .icon(bitmap) 
		    .zIndex(9)  //设置Marker所在层级
			.draggable(false);  //设置手势拖拽
	  
		Marker marker = (Marker) (mBaiduMap.addOverlay(options));  
		 
		//调用BaiduMap对象的setOnMarkerDragListener方法设置Marker拖拽的监听
		mBaiduMap.setOnMarkerDragListener(new OnMarkerDragListener() {
		    public void onMarkerDrag(Marker marker) {
		        //拖拽中
		    }
		    public void onMarkerDragEnd(Marker marker) {
		        //拖拽结束
		    	LatLng latLng = marker.getPosition();
		    	//Toast.makeText(OrderInfoUserAddActivity.this, latLng.latitude + "," + latLng.longitude, Toast.LENGTH_LONG).show();
		    	TV_latitude.setText((float)latLng.latitude + "");
		    	TV_latitude.setText((float)latLng.longitude + "");
		    	
		    }
		    public void onMarkerDragStart(Marker marker) {
		        //开始拖拽
		    }
		});
		
	}
	
	
	private void InitCenter() {
		//设定中心点坐标 
	    LatLng cenpt = new LatLng(washShop.getLatitude(), washShop.getLongitude()); 
	   
	    //定义地图状态
	    MapStatus mMapStatus = new MapStatus.Builder()
	    .target(cenpt)
	    .zoom(18)
	    .build();
	    //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
	
	
	    MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
	    //改变地图状态
	    mBaiduMap = mMapView.getMap(); 
	    mBaiduMap.setMapStatus(mMapStatusUpdate);  
	
	    
	    UiSettings mUiSettings = mBaiduMap.getUiSettings();
		//实例化UiSettings类对象 
		//mUiSettings.setCompassEnabled(true);  
		mUiSettings.setScrollGesturesEnabled(true);
		mUiSettings.setZoomGesturesEnabled(true);
		mUiSettings.setOverlookingGesturesEnabled(true);
		mMapView.showScaleControl(false);
		 
		
		/* 就是在用户点击到地图或者滑动地图时候，让ScrollView不截断点击事件，并传递给子View处理，也就是地图去处理点击事件；
		 * 当用户手指抬起的时候，将ScrollView的状态恢复至之前的状态，也就是ScrollView可以截断点击事件*/
		final ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
		mMapView.getChildAt(0).setOnTouchListener(new View.OnTouchListener() {
	        @Override
	        public boolean onTouch(View v, MotionEvent event) {
	            if(event.getAction() == MotionEvent.ACTION_UP){
	                //允许ScrollView截断点击事件，ScrollView可滑动
	            	scrollView.requestDisallowInterceptTouchEvent(false);
	            }else{
	                //不允许ScrollView截断点击事件，点击事件由子View处理
	            	scrollView.requestDisallowInterceptTouchEvent(true);
	            }
	            return false;
	        }
	    });
	}



	/* 初始化显示详情界面的数据 */
	private void initViewData() {
	    washShop = washShopService.GetWashShop(shopUserName); 
		this.TV_shopUserName.setText(washShop.getShopUserName());
		this.TV_password.setText(washShop.getPassword());
		this.TV_shopName.setText(washShop.getShopName());
		WashClass washClassObj = washClassService.GetWashClass(washShop.getWashClassObj());
		this.TV_washClassObj.setText(washClassObj.getClassName());
		byte[] shopPhoto_data = null;
		try {
			// 获取图片数据
			shopPhoto_data = ImageService.getImage(HttpUtil.BASE_URL + washShop.getShopPhoto());
			Bitmap shopPhoto = BitmapFactory.decodeByteArray(shopPhoto_data, 0,shopPhoto_data.length);
			this.iv_shopPhoto.setImageBitmap(shopPhoto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.TV_telephone.setText(washShop.getTelephone());
		Date addDate = new Date(washShop.getAddDate().getTime());
		String addDateStr = (addDate.getYear() + 1900) + "-" + (addDate.getMonth()+1) + "-" + addDate.getDate();
		this.TV_addDate.setText(addDateStr);
		this.TV_address.setText(washShop.getAddress());
		this.TV_latitude.setText(washShop.getLatitude() + "");
		this.TV_longitude.setText(washShop.getLongitude() + "");
	} 
}
