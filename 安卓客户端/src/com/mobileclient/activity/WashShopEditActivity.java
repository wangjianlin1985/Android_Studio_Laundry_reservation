package com.mobileclient.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

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
import com.mobileclient.util.HttpUtil;
import com.mobileclient.util.ImageService;
import com.mobileclient.domain.WashShop;
import com.mobileclient.service.WashShopService;
import com.mobileclient.domain.WashClass;
import com.mobileclient.service.WashClassService;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Spinner;
import android.widget.Toast;

public class WashShopEditActivity extends Activity {
	// 声明确定添加按钮
	private Button btnUpdate;
	// 声明洗衣店账号TextView
	private TextView TV_shopUserName;
	// 声明登录密码输入框
	private EditText ET_password;
	// 声明洗衣店名称输入框
	private EditText ET_shopName;
	// 声明洗衣店种类下拉框
	private Spinner spinner_washClassObj;
	private ArrayAdapter<String> washClassObj_adapter;
	private static  String[] washClassObj_ShowText  = null;
	private List<WashClass> washClassList = null;
	/*洗衣店种类管理业务逻辑层*/
	private WashClassService washClassService = new WashClassService();
	// 声明洗衣店照片图片框控件
	private ImageView iv_shopPhoto;
	private Button btn_shopPhoto;
	protected int REQ_CODE_SELECT_IMAGE_shopPhoto = 1;
	private int REQ_CODE_CAMERA_shopPhoto = 2;
	// 声明店家电话输入框
	private EditText ET_telephone;
	// 出版入驻日期控件
	private DatePicker dp_addDate;
	// 声明店铺地址输入框
	private EditText ET_address;
	// 声明纬度输入框
	private EditText ET_latitude;
	// 声明经度输入框
	private EditText ET_longitude;
	protected String carmera_path;
	/*要保存的洗衣店信息*/
	WashShop washShop = new WashShop();
	/*洗衣店管理业务逻辑层*/
	private WashShopService washShopService = new WashShopService();

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
		setContentView(R.layout.washshop_edit); 
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("编辑洗衣店信息");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		TV_shopUserName = (TextView) findViewById(R.id.TV_shopUserName);
		ET_password = (EditText) findViewById(R.id.ET_password);
		ET_shopName = (EditText) findViewById(R.id.ET_shopName);
		spinner_washClassObj = (Spinner) findViewById(R.id.Spinner_washClassObj);
		// 获取所有的洗衣店种类
		try {
			washClassList = washClassService.QueryWashClass(null);
		} catch (Exception e1) { 
			e1.printStackTrace(); 
		}
		int washClassCount = washClassList.size();
		washClassObj_ShowText = new String[washClassCount];
		for(int i=0;i<washClassCount;i++) { 
			washClassObj_ShowText[i] = washClassList.get(i).getClassName();
		}
		// 将可选内容与ArrayAdapter连接起来
		washClassObj_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, washClassObj_ShowText);
		// 设置图书类别下拉列表的风格
		washClassObj_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_washClassObj.setAdapter(washClassObj_adapter);
		// 添加事件Spinner事件监听
		spinner_washClassObj.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				washShop.setWashClassObj(washClassList.get(arg2).getClassId()); 
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// 设置默认值
		spinner_washClassObj.setVisibility(View.VISIBLE);
		iv_shopPhoto = (ImageView) findViewById(R.id.iv_shopPhoto);
		/*单击图片显示控件时进行图片的选择*/
		iv_shopPhoto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(WashShopEditActivity.this,photoListActivity.class);
				startActivityForResult(intent,REQ_CODE_SELECT_IMAGE_shopPhoto);
			}
		});
		btn_shopPhoto = (Button) findViewById(R.id.btn_shopPhoto);
		btn_shopPhoto.setOnClickListener(new OnClickListener() { 
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 
				carmera_path = HttpUtil.FILE_PATH + "/carmera_shopPhoto.bmp";
				File out = new File(carmera_path); 
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(out)); 
				startActivityForResult(intent, REQ_CODE_CAMERA_shopPhoto);  
			}
		});
		ET_telephone = (EditText) findViewById(R.id.ET_telephone);
		dp_addDate = (DatePicker)this.findViewById(R.id.dp_addDate);
		ET_address = (EditText) findViewById(R.id.ET_address);
		ET_latitude = (EditText) findViewById(R.id.ET_latitude);
		ET_longitude = (EditText) findViewById(R.id.ET_longitude);
		btnUpdate = (Button) findViewById(R.id.BtnUpdate);
		Bundle extras = this.getIntent().getExtras();
		shopUserName = extras.getString("shopUserName");
		/*单击修改洗衣店按钮*/
		btnUpdate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*验证获取登录密码*/ 
					if(ET_password.getText().toString().equals("")) {
						Toast.makeText(WashShopEditActivity.this, "登录密码输入不能为空!", Toast.LENGTH_LONG).show();
						ET_password.setFocusable(true);
						ET_password.requestFocus();
						return;	
					}
					washShop.setPassword(ET_password.getText().toString());
					/*验证获取洗衣店名称*/ 
					if(ET_shopName.getText().toString().equals("")) {
						Toast.makeText(WashShopEditActivity.this, "洗衣店名称输入不能为空!", Toast.LENGTH_LONG).show();
						ET_shopName.setFocusable(true);
						ET_shopName.requestFocus();
						return;	
					}
					washShop.setShopName(ET_shopName.getText().toString());
					if (!washShop.getShopPhoto().startsWith("upload/")) {
						//如果图片地址不为空，说明用户选择了图片，这时需要连接服务器上传图片
						WashShopEditActivity.this.setTitle("正在上传图片，稍等...");
						String shopPhoto = HttpUtil.uploadFile(washShop.getShopPhoto());
						WashShopEditActivity.this.setTitle("图片上传完毕！");
						washShop.setShopPhoto(shopPhoto);
					} 
					/*验证获取店家电话*/ 
					if(ET_telephone.getText().toString().equals("")) {
						Toast.makeText(WashShopEditActivity.this, "店家电话输入不能为空!", Toast.LENGTH_LONG).show();
						ET_telephone.setFocusable(true);
						ET_telephone.requestFocus();
						return;	
					}
					washShop.setTelephone(ET_telephone.getText().toString());
					/*获取出版日期*/
					Date addDate = new Date(dp_addDate.getYear()-1900,dp_addDate.getMonth(),dp_addDate.getDayOfMonth());
					washShop.setAddDate(new Timestamp(addDate.getTime()));
					/*验证获取店铺地址*/ 
					if(ET_address.getText().toString().equals("")) {
						Toast.makeText(WashShopEditActivity.this, "店铺地址输入不能为空!", Toast.LENGTH_LONG).show();
						ET_address.setFocusable(true);
						ET_address.requestFocus();
						return;	
					}
					washShop.setAddress(ET_address.getText().toString());
					/*验证获取纬度*/ 
					if(ET_latitude.getText().toString().equals("")) {
						Toast.makeText(WashShopEditActivity.this, "纬度输入不能为空!", Toast.LENGTH_LONG).show();
						ET_latitude.setFocusable(true);
						ET_latitude.requestFocus();
						return;	
					}
					washShop.setLatitude(Float.parseFloat(ET_latitude.getText().toString()));
					/*验证获取经度*/ 
					if(ET_longitude.getText().toString().equals("")) {
						Toast.makeText(WashShopEditActivity.this, "经度输入不能为空!", Toast.LENGTH_LONG).show();
						ET_longitude.setFocusable(true);
						ET_longitude.requestFocus();
						return;	
					}
					washShop.setLongitude(Float.parseFloat(ET_longitude.getText().toString()));
					/*调用业务逻辑层上传洗衣店信息*/
					WashShopEditActivity.this.setTitle("正在更新洗衣店信息，稍等...");
					String result = washShopService.UpdateWashShop(washShop);
					Toast.makeText(getApplicationContext(), result, 1).show(); 
					Intent intent = getIntent();
					setResult(RESULT_OK,intent);
					finish();
				} catch (Exception e) {}
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
			.draggable(true);  //设置手势拖拽
	  
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
		    	ET_latitude.setText((float)latLng.latitude + "");
		    	ET_longitude.setText((float)latLng.longitude + "");
		    	
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
		 * 当用户手指抬起的时候，将ScrollView的状态恢复至之前的状态，也就是ScrollView可以截断点击事件 */
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


	/* 初始化显示编辑界面的数据 */
	private void initViewData() {
	    washShop = washShopService.GetWashShop(shopUserName);
		this.TV_shopUserName.setText(shopUserName);
		this.ET_password.setText(washShop.getPassword());
		this.ET_shopName.setText(washShop.getShopName());
		for (int i = 0; i < washClassList.size(); i++) {
			if (washShop.getWashClassObj() == washClassList.get(i).getClassId()) {
				this.spinner_washClassObj.setSelection(i);
				break;
			}
		}
		byte[] shopPhoto_data = null;
		try {
			// 获取图片数据
			shopPhoto_data = ImageService.getImage(HttpUtil.BASE_URL + washShop.getShopPhoto());
			Bitmap shopPhoto = BitmapFactory.decodeByteArray(shopPhoto_data, 0, shopPhoto_data.length);
			this.iv_shopPhoto.setImageBitmap(shopPhoto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.ET_telephone.setText(washShop.getTelephone());
		Date addDate = new Date(washShop.getAddDate().getTime());
		this.dp_addDate.init(addDate.getYear() + 1900,addDate.getMonth(), addDate.getDate(), null);
		this.ET_address.setText(washShop.getAddress());
		this.ET_latitude.setText(washShop.getLatitude() + "");
		this.ET_longitude.setText(washShop.getLongitude() + "");
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQ_CODE_CAMERA_shopPhoto  && resultCode == Activity.RESULT_OK) {
			carmera_path = HttpUtil.FILE_PATH + "/carmera_shopPhoto.bmp"; 
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(carmera_path, opts); 
			opts.inSampleSize = photoListActivity.computeSampleSize(opts, -1, 300*300);
			opts.inJustDecodeBounds = false;
			try {
				Bitmap booImageBm = BitmapFactory.decodeFile(carmera_path, opts);
				String jpgFileName = "carmera_shopPhoto.jpg";
				String jpgFilePath =  HttpUtil.FILE_PATH + "/" + jpgFileName;
				try {
					FileOutputStream jpgOutputStream = new FileOutputStream(jpgFilePath);
					booImageBm.compress(Bitmap.CompressFormat.JPEG, 30, jpgOutputStream);// 把数据写入文件 
					File bmpFile = new File(carmera_path);
					bmpFile.delete();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} 
				this.iv_shopPhoto.setImageBitmap(booImageBm);
				this.iv_shopPhoto.setScaleType(ScaleType.FIT_CENTER);
				this.washShop.setShopPhoto(jpgFileName);
			} catch (OutOfMemoryError err) {  }
		}

		if(requestCode == REQ_CODE_SELECT_IMAGE_shopPhoto && resultCode == Activity.RESULT_OK) {
			Bundle bundle = data.getExtras();
			String filename =  bundle.getString("fileName");
			String filepath = HttpUtil.FILE_PATH + "/" + filename;
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true; 
			BitmapFactory.decodeFile(filepath, opts); 
			opts.inSampleSize = photoListActivity.computeSampleSize(opts, -1, 128*128);
			opts.inJustDecodeBounds = false; 
			try { 
				Bitmap bm = BitmapFactory.decodeFile(filepath, opts);
				this.iv_shopPhoto.setImageBitmap(bm); 
				this.iv_shopPhoto.setScaleType(ScaleType.FIT_CENTER); 
			} catch (OutOfMemoryError err) {  } 
			washShop.setShopPhoto(filename); 
		}
	}
}
