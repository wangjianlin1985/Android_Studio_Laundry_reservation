package com.mobileclient.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerDragListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.mobileclient.util.HttpUtil;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.mobileclient.domain.UserInfo;
import com.mobileclient.service.UserInfoService;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
public class UserInfoAddActivity extends Activity {
	// 声明确定添加按钮
	private Button btnAdd;
	// 声明用户名输入框
	private EditText ET_user_name;
	// 声明登录密码输入框
	private EditText ET_password;
	// 声明姓名输入框
	private EditText ET_name;
	// 声明性别输入框
	private EditText ET_sex;
	// 出版生日控件
	private DatePicker dp_birthDate;
	// 声明个人照片图片框控件
	private ImageView iv_userPhoto;
	private Button btn_userPhoto;
	protected int REQ_CODE_SELECT_IMAGE_userPhoto = 1;
	private int REQ_CODE_CAMERA_userPhoto = 2;
	// 声明联系电话输入框
	private EditText ET_telephone;
	// 声明住址输入框
	private EditText ET_address;
	// 声明纬度输入框
	private EditText ET_latitude;
	// 声明经度输入框
	private EditText ET_longitude;
	// 声明注册时间输入框
	private EditText ET_regTime;
	protected String carmera_path;
	/*要保存的用户信息*/
	UserInfo userInfo = new UserInfo();
	/*用户管理业务逻辑层*/
	private UserInfoService userInfoService = new UserInfoService();
	
	MapView mMapView = null;
	BaiduMap mBaiduMap = null;
	public LocationClient mLocationClient = null;
	private MyLocationListener myListener = new MyLocationListener();


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN); 
		// 设置当前Activity界面布局
		setContentView(R.layout.userinfo_add); 
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("用户注册");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		ET_user_name = (EditText) findViewById(R.id.ET_user_name);
		ET_password = (EditText) findViewById(R.id.ET_password);
		ET_name = (EditText) findViewById(R.id.ET_name);
		ET_sex = (EditText) findViewById(R.id.ET_sex);
		dp_birthDate = (DatePicker)this.findViewById(R.id.dp_birthDate);
		iv_userPhoto = (ImageView) findViewById(R.id.iv_userPhoto);
		/*单击图片显示控件时进行图片的选择*/
		iv_userPhoto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(UserInfoAddActivity.this,photoListActivity.class);
				startActivityForResult(intent,REQ_CODE_SELECT_IMAGE_userPhoto);
			}
		});
		btn_userPhoto = (Button) findViewById(R.id.btn_userPhoto);
		btn_userPhoto.setOnClickListener(new OnClickListener() { 
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 
				carmera_path = HttpUtil.FILE_PATH + "/carmera_userPhoto.bmp";
				File out = new File(carmera_path); 
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(out)); 
				startActivityForResult(intent, REQ_CODE_CAMERA_userPhoto);  
			}
		});
		ET_telephone = (EditText) findViewById(R.id.ET_telephone);
		ET_address = (EditText) findViewById(R.id.ET_address);
		ET_latitude = (EditText) findViewById(R.id.ET_latitude);
		ET_longitude = (EditText) findViewById(R.id.ET_longitude);
		ET_regTime = (EditText) findViewById(R.id.ET_regTime);
		btnAdd = (Button) findViewById(R.id.BtnAdd);
		/*单击添加用户按钮*/
		btnAdd.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*验证获取用户名*/
					if(ET_user_name.getText().toString().equals("")) {
						Toast.makeText(UserInfoAddActivity.this, "用户名输入不能为空!", Toast.LENGTH_LONG).show();
						ET_user_name.setFocusable(true);
						ET_user_name.requestFocus();
						return;
					}
					userInfo.setUser_name(ET_user_name.getText().toString());
					/*验证获取登录密码*/ 
					if(ET_password.getText().toString().equals("")) {
						Toast.makeText(UserInfoAddActivity.this, "登录密码输入不能为空!", Toast.LENGTH_LONG).show();
						ET_password.setFocusable(true);
						ET_password.requestFocus();
						return;	
					}
					userInfo.setPassword(ET_password.getText().toString());
					/*验证获取姓名*/ 
					if(ET_name.getText().toString().equals("")) {
						Toast.makeText(UserInfoAddActivity.this, "姓名输入不能为空!", Toast.LENGTH_LONG).show();
						ET_name.setFocusable(true);
						ET_name.requestFocus();
						return;	
					}
					userInfo.setName(ET_name.getText().toString());
					/*验证获取性别*/ 
					if(ET_sex.getText().toString().equals("")) {
						Toast.makeText(UserInfoAddActivity.this, "性别输入不能为空!", Toast.LENGTH_LONG).show();
						ET_sex.setFocusable(true);
						ET_sex.requestFocus();
						return;	
					}
					userInfo.setSex(ET_sex.getText().toString());
					/*获取生日*/
					Date birthDate = new Date(dp_birthDate.getYear()-1900,dp_birthDate.getMonth(),dp_birthDate.getDayOfMonth());
					userInfo.setBirthDate(new Timestamp(birthDate.getTime()));
					if(userInfo.getUserPhoto() != null) {
						//如果图片地址不为空，说明用户选择了图片，这时需要连接服务器上传图片
						UserInfoAddActivity.this.setTitle("正在上传图片，稍等...");
						String userPhoto = HttpUtil.uploadFile(userInfo.getUserPhoto());
						UserInfoAddActivity.this.setTitle("图片上传完毕！");
						userInfo.setUserPhoto(userPhoto);
					} else {
						userInfo.setUserPhoto("upload/noimage.jpg");
					}
					/*验证获取联系电话*/ 
					if(ET_telephone.getText().toString().equals("")) {
						Toast.makeText(UserInfoAddActivity.this, "联系电话输入不能为空!", Toast.LENGTH_LONG).show();
						ET_telephone.setFocusable(true);
						ET_telephone.requestFocus();
						return;	
					}
					userInfo.setTelephone(ET_telephone.getText().toString());
					/*验证获取住址*/ 
					if(ET_address.getText().toString().equals("")) {
						Toast.makeText(UserInfoAddActivity.this, "住址输入不能为空!", Toast.LENGTH_LONG).show();
						ET_address.setFocusable(true);
						ET_address.requestFocus();
						return;	
					}
					userInfo.setAddress(ET_address.getText().toString());
					/*验证获取纬度*/ 
					if(ET_latitude.getText().toString().equals("")) {
						Toast.makeText(UserInfoAddActivity.this, "纬度输入不能为空!", Toast.LENGTH_LONG).show();
						ET_latitude.setFocusable(true);
						ET_latitude.requestFocus();
						return;	
					}
					userInfo.setLatitude(Float.parseFloat(ET_latitude.getText().toString()));
					/*验证获取经度*/ 
					if(ET_longitude.getText().toString().equals("")) {
						Toast.makeText(UserInfoAddActivity.this, "经度输入不能为空!", Toast.LENGTH_LONG).show();
						ET_longitude.setFocusable(true);
						ET_longitude.requestFocus();
						return;	
					}
					userInfo.setLongitude(Float.parseFloat(ET_longitude.getText().toString()));
					/*验证获取注册时间*/ 
					if(ET_regTime.getText().toString().equals("")) {
						Toast.makeText(UserInfoAddActivity.this, "注册时间输入不能为空!", Toast.LENGTH_LONG).show();
						ET_regTime.setFocusable(true);
						ET_regTime.requestFocus();
						return;	
					}
					userInfo.setRegTime(ET_regTime.getText().toString());
					/*调用业务逻辑层上传用户信息*/
					UserInfoAddActivity.this.setTitle("正在上传用户信息，稍等...");
					String result = userInfoService.AddUserInfo(userInfo);
					Toast.makeText(getApplicationContext(), result, 1).show(); 
					Intent intent = getIntent();
					setResult(RESULT_OK,intent);
					finish();
				} catch (Exception e) {}
			}
		});
		
		mMapView = (MapView) findViewById(R.id.bmapView);
		getMyLocation();  //调用百度地图定位用户当前位置
	}

	
	
public class MyLocationListener implements BDLocationListener {
		
		
		public void onReceiveLocation(BDLocation location){
	        //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
	        //以下只列举部分获取经纬度相关（常用）的结果信息
	        //更多结果信息获取说明，请参照类参考中BDLocation类中的说明
				
	        double latitude = location.getLatitude();    //获取纬度信息
	        double longitude = location.getLongitude();    //获取经度信息
	        float radius = location.getRadius();    //获取定位精度，默认值为0.0f
	        
	        ET_latitude.setText((float)latitude + "");
	        ET_longitude.setText((float)longitude + "");
	        
	        InitCenter(latitude,longitude);
			AddMarker(latitude,longitude);
				 
	        String coorType = location.getCoorType();
	        //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准
				
	        int errorCode = location.getLocType();
	        //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明
	    }

		private void AddMarker(double latitude, double longitude) {
			mBaiduMap.clear();
			LatLng point = new LatLng(latitude, longitude); 
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

		private void InitCenter(double latitude, double longitude) {
			//设定中心点坐标 
	        LatLng cenpt = new LatLng(latitude, longitude); 
	       
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

		 
	}
	
	
	
	private void getMyLocation() {
		//声明LocationClient类
		mLocationClient = new LocationClient(getApplicationContext());     
		//注册监听函数
	    mLocationClient.registerLocationListener(myListener);    
	     
	    LocationClientOption option = new LocationClientOption();

	    //option.setLocationMode(LocationMode.COMPASS);
	    //可选，设置定位模式，默认高精度
	    //LocationMode.Hight_Accuracy：高精度；
	    //LocationMode. Battery_Saving：低功耗；
	    //LocationMode. Device_Sensors：仅使用设备；
	    	
	    option.setCoorType("bd09ll");
	    //可选，设置返回经纬度坐标类型，默认gcj02
	    //gcj02：国测局坐标；
	    //bd09ll：百度经纬度坐标；
	    //bd09：百度墨卡托坐标；
	    //海外地区定位，无需设置坐标类型，统一返回wgs84类型坐标
	     	
	    //option.setScanSpan(1000);
	    //可选，设置发起定位请求的间隔，int类型，单位ms
	    //如果设置为0，则代表单次定位，即仅定位一次，默认为0
	    //如果设置非0，需设置1000ms以上才有效
	    	
	    option.setOpenGps(true);
	    //可选，设置是否使用gps，默认false
	    //使用高精度和仅用设备两种定位模式的，参数必须设置为true
	    	
	    option.setLocationNotify(true);
	    //可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false
	    	
	    option.setIgnoreKillProcess(false);
	    //可选，定位SDK内部是一个service，并放到了独立进程。
	    //设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)
	    
	    //option.setIgnoreCacheException(false);
	    //可选，设置是否收集Crash信息，默认收集，即参数为false

	     
	    //option.setWifiValidTime(5*60*1000);
	    //可选，7.2版本新增能力
	    //如果设置了该接口，首次启动定位时，会先判断当前WiFi是否超出有效期，若超出有效期，会先重新扫描WiFi，然后定位
	    	
	    option.setEnableSimulateGps(false);
	    //可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false
	    	
	    mLocationClient.setLocOption(option);
	    mLocationClient.start();
	}
	
 
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQ_CODE_CAMERA_userPhoto  && resultCode == Activity.RESULT_OK) {
			carmera_path = HttpUtil.FILE_PATH + "/carmera_userPhoto.bmp"; 
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(carmera_path, opts); 
			opts.inSampleSize = photoListActivity.computeSampleSize(opts, -1, 300*300);
			opts.inJustDecodeBounds = false;
			try {
				Bitmap booImageBm = BitmapFactory.decodeFile(carmera_path, opts);
				String jpgFileName = "carmera_userPhoto.jpg";
				String jpgFilePath =  HttpUtil.FILE_PATH + "/" + jpgFileName;
				try {
					FileOutputStream jpgOutputStream = new FileOutputStream(jpgFilePath);
					booImageBm.compress(Bitmap.CompressFormat.JPEG, 30, jpgOutputStream);// 把数据写入文件 
					File bmpFile = new File(carmera_path);
					bmpFile.delete();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} 
				this.iv_userPhoto.setImageBitmap(booImageBm);
				this.iv_userPhoto.setScaleType(ScaleType.FIT_CENTER);
				this.userInfo.setUserPhoto(jpgFileName);
			} catch (OutOfMemoryError err) {  }
		}

		if(requestCode == REQ_CODE_SELECT_IMAGE_userPhoto && resultCode == Activity.RESULT_OK) {
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
				this.iv_userPhoto.setImageBitmap(bm); 
				this.iv_userPhoto.setScaleType(ScaleType.FIT_CENTER); 
			} catch (OutOfMemoryError err) {  } 
			userInfo.setUserPhoto(filename); 
		}
	}
}
