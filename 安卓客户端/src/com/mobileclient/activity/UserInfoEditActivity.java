package com.mobileclient.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

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
import com.mobileclient.util.ImageService;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Spinner;
import android.widget.Toast;

public class UserInfoEditActivity extends Activity {
	// 声明确定添加按钮
	private Button btnUpdate;
	// 声明用户名TextView
	private TextView TV_user_name;
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
	
	private String user_name;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
		// 设置当前Activity界面布局
		setContentView(R.layout.userinfo_edit); 
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("编辑用户信息");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		TV_user_name = (TextView) findViewById(R.id.TV_user_name);
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
				intent.setClass(UserInfoEditActivity.this,photoListActivity.class);
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
		btnUpdate = (Button) findViewById(R.id.BtnUpdate);
		Bundle extras = this.getIntent().getExtras();
		user_name = extras.getString("user_name");
		/*单击修改用户按钮*/
		btnUpdate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*验证获取登录密码*/ 
					if(ET_password.getText().toString().equals("")) {
						Toast.makeText(UserInfoEditActivity.this, "登录密码输入不能为空!", Toast.LENGTH_LONG).show();
						ET_password.setFocusable(true);
						ET_password.requestFocus();
						return;	
					}
					userInfo.setPassword(ET_password.getText().toString());
					/*验证获取姓名*/ 
					if(ET_name.getText().toString().equals("")) {
						Toast.makeText(UserInfoEditActivity.this, "姓名输入不能为空!", Toast.LENGTH_LONG).show();
						ET_name.setFocusable(true);
						ET_name.requestFocus();
						return;	
					}
					userInfo.setName(ET_name.getText().toString());
					/*验证获取性别*/ 
					if(ET_sex.getText().toString().equals("")) {
						Toast.makeText(UserInfoEditActivity.this, "性别输入不能为空!", Toast.LENGTH_LONG).show();
						ET_sex.setFocusable(true);
						ET_sex.requestFocus();
						return;	
					}
					userInfo.setSex(ET_sex.getText().toString());
					/*获取出版日期*/
					Date birthDate = new Date(dp_birthDate.getYear()-1900,dp_birthDate.getMonth(),dp_birthDate.getDayOfMonth());
					userInfo.setBirthDate(new Timestamp(birthDate.getTime()));
					if (!userInfo.getUserPhoto().startsWith("upload/")) {
						//如果图片地址不为空，说明用户选择了图片，这时需要连接服务器上传图片
						UserInfoEditActivity.this.setTitle("正在上传图片，稍等...");
						String userPhoto = HttpUtil.uploadFile(userInfo.getUserPhoto());
						UserInfoEditActivity.this.setTitle("图片上传完毕！");
						userInfo.setUserPhoto(userPhoto);
					} 
					/*验证获取联系电话*/ 
					if(ET_telephone.getText().toString().equals("")) {
						Toast.makeText(UserInfoEditActivity.this, "联系电话输入不能为空!", Toast.LENGTH_LONG).show();
						ET_telephone.setFocusable(true);
						ET_telephone.requestFocus();
						return;	
					}
					userInfo.setTelephone(ET_telephone.getText().toString());
					/*验证获取住址*/ 
					if(ET_address.getText().toString().equals("")) {
						Toast.makeText(UserInfoEditActivity.this, "住址输入不能为空!", Toast.LENGTH_LONG).show();
						ET_address.setFocusable(true);
						ET_address.requestFocus();
						return;	
					}
					userInfo.setAddress(ET_address.getText().toString());
					/*验证获取纬度*/ 
					if(ET_latitude.getText().toString().equals("")) {
						Toast.makeText(UserInfoEditActivity.this, "纬度输入不能为空!", Toast.LENGTH_LONG).show();
						ET_latitude.setFocusable(true);
						ET_latitude.requestFocus();
						return;	
					}
					userInfo.setLatitude(Float.parseFloat(ET_latitude.getText().toString()));
					/*验证获取经度*/ 
					if(ET_longitude.getText().toString().equals("")) {
						Toast.makeText(UserInfoEditActivity.this, "经度输入不能为空!", Toast.LENGTH_LONG).show();
						ET_longitude.setFocusable(true);
						ET_longitude.requestFocus();
						return;	
					}
					userInfo.setLongitude(Float.parseFloat(ET_longitude.getText().toString()));
					/*验证获取注册时间*/ 
					if(ET_regTime.getText().toString().equals("")) {
						Toast.makeText(UserInfoEditActivity.this, "注册时间输入不能为空!", Toast.LENGTH_LONG).show();
						ET_regTime.setFocusable(true);
						ET_regTime.requestFocus();
						return;	
					}
					userInfo.setRegTime(ET_regTime.getText().toString());
					/*调用业务逻辑层上传用户信息*/
					UserInfoEditActivity.this.setTitle("正在更新用户信息，稍等...");
					String result = userInfoService.UpdateUserInfo(userInfo);
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
		LatLng point = new LatLng(userInfo.getLatitude(), userInfo.getLongitude()); 
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
	    LatLng cenpt = new LatLng(userInfo.getLatitude(), userInfo.getLongitude()); 
	   
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
	    userInfo = userInfoService.GetUserInfo(user_name);
		this.TV_user_name.setText(user_name);
		this.ET_password.setText(userInfo.getPassword());
		this.ET_name.setText(userInfo.getName());
		this.ET_sex.setText(userInfo.getSex());
		Date birthDate = new Date(userInfo.getBirthDate().getTime());
		this.dp_birthDate.init(birthDate.getYear() + 1900,birthDate.getMonth(), birthDate.getDate(), null);
		byte[] userPhoto_data = null;
		try {
			// 获取图片数据
			userPhoto_data = ImageService.getImage(HttpUtil.BASE_URL + userInfo.getUserPhoto());
			Bitmap userPhoto = BitmapFactory.decodeByteArray(userPhoto_data, 0, userPhoto_data.length);
			this.iv_userPhoto.setImageBitmap(userPhoto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.ET_telephone.setText(userInfo.getTelephone());
		this.ET_address.setText(userInfo.getAddress());
		this.ET_latitude.setText(userInfo.getLatitude() + "");
		this.ET_longitude.setText(userInfo.getLongitude() + "");
		this.ET_regTime.setText(userInfo.getRegTime());
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
