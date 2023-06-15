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
	// ����ȷ����Ӱ�ť
	private Button btnAdd;
	// �����û��������
	private EditText ET_user_name;
	// ������¼���������
	private EditText ET_password;
	// �������������
	private EditText ET_name;
	// �����Ա������
	private EditText ET_sex;
	// �������տؼ�
	private DatePicker dp_birthDate;
	// ����������ƬͼƬ��ؼ�
	private ImageView iv_userPhoto;
	private Button btn_userPhoto;
	protected int REQ_CODE_SELECT_IMAGE_userPhoto = 1;
	private int REQ_CODE_CAMERA_userPhoto = 2;
	// ������ϵ�绰�����
	private EditText ET_telephone;
	// ����סַ�����
	private EditText ET_address;
	// ����γ�������
	private EditText ET_latitude;
	// �������������
	private EditText ET_longitude;
	// ����ע��ʱ�������
	private EditText ET_regTime;
	protected String carmera_path;
	/*Ҫ������û���Ϣ*/
	UserInfo userInfo = new UserInfo();
	/*�û�����ҵ���߼���*/
	private UserInfoService userInfoService = new UserInfoService();
	
	MapView mMapView = null;
	BaiduMap mBaiduMap = null;
	public LocationClient mLocationClient = null;
	private MyLocationListener myListener = new MyLocationListener();


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//ȥ��title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//ȥ��Activity�����״̬��
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN); 
		// ���õ�ǰActivity���沼��
		setContentView(R.layout.userinfo_add); 
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("�û�ע��");
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
		/*����ͼƬ��ʾ�ؼ�ʱ����ͼƬ��ѡ��*/
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
		/*��������û���ť*/
		btnAdd.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*��֤��ȡ�û���*/
					if(ET_user_name.getText().toString().equals("")) {
						Toast.makeText(UserInfoAddActivity.this, "�û������벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_user_name.setFocusable(true);
						ET_user_name.requestFocus();
						return;
					}
					userInfo.setUser_name(ET_user_name.getText().toString());
					/*��֤��ȡ��¼����*/ 
					if(ET_password.getText().toString().equals("")) {
						Toast.makeText(UserInfoAddActivity.this, "��¼�������벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_password.setFocusable(true);
						ET_password.requestFocus();
						return;	
					}
					userInfo.setPassword(ET_password.getText().toString());
					/*��֤��ȡ����*/ 
					if(ET_name.getText().toString().equals("")) {
						Toast.makeText(UserInfoAddActivity.this, "�������벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_name.setFocusable(true);
						ET_name.requestFocus();
						return;	
					}
					userInfo.setName(ET_name.getText().toString());
					/*��֤��ȡ�Ա�*/ 
					if(ET_sex.getText().toString().equals("")) {
						Toast.makeText(UserInfoAddActivity.this, "�Ա����벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_sex.setFocusable(true);
						ET_sex.requestFocus();
						return;	
					}
					userInfo.setSex(ET_sex.getText().toString());
					/*��ȡ����*/
					Date birthDate = new Date(dp_birthDate.getYear()-1900,dp_birthDate.getMonth(),dp_birthDate.getDayOfMonth());
					userInfo.setBirthDate(new Timestamp(birthDate.getTime()));
					if(userInfo.getUserPhoto() != null) {
						//���ͼƬ��ַ��Ϊ�գ�˵���û�ѡ����ͼƬ����ʱ��Ҫ���ӷ������ϴ�ͼƬ
						UserInfoAddActivity.this.setTitle("�����ϴ�ͼƬ���Ե�...");
						String userPhoto = HttpUtil.uploadFile(userInfo.getUserPhoto());
						UserInfoAddActivity.this.setTitle("ͼƬ�ϴ���ϣ�");
						userInfo.setUserPhoto(userPhoto);
					} else {
						userInfo.setUserPhoto("upload/noimage.jpg");
					}
					/*��֤��ȡ��ϵ�绰*/ 
					if(ET_telephone.getText().toString().equals("")) {
						Toast.makeText(UserInfoAddActivity.this, "��ϵ�绰���벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_telephone.setFocusable(true);
						ET_telephone.requestFocus();
						return;	
					}
					userInfo.setTelephone(ET_telephone.getText().toString());
					/*��֤��ȡסַ*/ 
					if(ET_address.getText().toString().equals("")) {
						Toast.makeText(UserInfoAddActivity.this, "סַ���벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_address.setFocusable(true);
						ET_address.requestFocus();
						return;	
					}
					userInfo.setAddress(ET_address.getText().toString());
					/*��֤��ȡγ��*/ 
					if(ET_latitude.getText().toString().equals("")) {
						Toast.makeText(UserInfoAddActivity.this, "γ�����벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_latitude.setFocusable(true);
						ET_latitude.requestFocus();
						return;	
					}
					userInfo.setLatitude(Float.parseFloat(ET_latitude.getText().toString()));
					/*��֤��ȡ����*/ 
					if(ET_longitude.getText().toString().equals("")) {
						Toast.makeText(UserInfoAddActivity.this, "�������벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_longitude.setFocusable(true);
						ET_longitude.requestFocus();
						return;	
					}
					userInfo.setLongitude(Float.parseFloat(ET_longitude.getText().toString()));
					/*��֤��ȡע��ʱ��*/ 
					if(ET_regTime.getText().toString().equals("")) {
						Toast.makeText(UserInfoAddActivity.this, "ע��ʱ�����벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_regTime.setFocusable(true);
						ET_regTime.requestFocus();
						return;	
					}
					userInfo.setRegTime(ET_regTime.getText().toString());
					/*����ҵ���߼����ϴ��û���Ϣ*/
					UserInfoAddActivity.this.setTitle("�����ϴ��û���Ϣ���Ե�...");
					String result = userInfoService.AddUserInfo(userInfo);
					Toast.makeText(getApplicationContext(), result, 1).show(); 
					Intent intent = getIntent();
					setResult(RESULT_OK,intent);
					finish();
				} catch (Exception e) {}
			}
		});
		
		mMapView = (MapView) findViewById(R.id.bmapView);
		getMyLocation();  //���ðٶȵ�ͼ��λ�û���ǰλ��
	}

	
	
public class MyLocationListener implements BDLocationListener {
		
		
		public void onReceiveLocation(BDLocation location){
	        //�˴���BDLocationΪ��λ�����Ϣ�࣬ͨ�����ĸ���get�����ɻ�ȡ��λ��ص�ȫ�����
	        //����ֻ�оٲ��ֻ�ȡ��γ����أ����ã��Ľ����Ϣ
	        //��������Ϣ��ȡ˵�����������ο���BDLocation���е�˵��
				
	        double latitude = location.getLatitude();    //��ȡγ����Ϣ
	        double longitude = location.getLongitude();    //��ȡ������Ϣ
	        float radius = location.getRadius();    //��ȡ��λ���ȣ�Ĭ��ֵΪ0.0f
	        
	        ET_latitude.setText((float)latitude + "");
	        ET_longitude.setText((float)longitude + "");
	        
	        InitCenter(latitude,longitude);
			AddMarker(latitude,longitude);
				 
	        String coorType = location.getCoorType();
	        //��ȡ��γ���������ͣ���LocationClientOption�����ù�����������Ϊ׼
				
	        int errorCode = location.getLocType();
	        //��ȡ��λ���͡���λ���󷵻��룬������Ϣ�ɲ�����ο���BDLocation���е�˵��
	    }

		private void AddMarker(double latitude, double longitude) {
			mBaiduMap.clear();
			LatLng point = new LatLng(latitude, longitude); 
			//����Markerͼ��
			BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_geo);   
			//�ڵ�ͼ�����Marker������ʾ     
			//����MarkerOption�������ڵ�ͼ�����Marker   
			MarkerOptions options = new MarkerOptions()  
			    .position(point) 
			    .icon(bitmap) 
			    .zIndex(9)  //����Marker���ڲ㼶
				.draggable(true);  //����������ק
		  
			Marker marker = (Marker) (mBaiduMap.addOverlay(options));  
			 
			//����BaiduMap�����setOnMarkerDragListener��������Marker��ק�ļ���
			mBaiduMap.setOnMarkerDragListener(new OnMarkerDragListener() {
			    public void onMarkerDrag(Marker marker) {
			        //��ק��
			    }
			    public void onMarkerDragEnd(Marker marker) {
			        //��ק����
			    	LatLng latLng = marker.getPosition();
			    	//Toast.makeText(OrderInfoUserAddActivity.this, latLng.latitude + "," + latLng.longitude, Toast.LENGTH_LONG).show();
			    	ET_latitude.setText((float)latLng.latitude + "");
			    	ET_longitude.setText((float)latLng.longitude + "");
			    	
			    }
			    public void onMarkerDragStart(Marker marker) {
			        //��ʼ��ק
			    }
			});
			
		}

		private void InitCenter(double latitude, double longitude) {
			//�趨���ĵ����� 
	        LatLng cenpt = new LatLng(latitude, longitude); 
	       
	        //�����ͼ״̬
	        MapStatus mMapStatus = new MapStatus.Builder()
	        .target(cenpt)
	        .zoom(18)
	        .build();
	        //����MapStatusUpdate�����Ա�������ͼ״̬��Ҫ�����ı仯


	        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
	        //�ı��ͼ״̬
	        mBaiduMap = mMapView.getMap(); 
	        mBaiduMap.setMapStatus(mMapStatusUpdate);  
	 
	        
	        UiSettings mUiSettings = mBaiduMap.getUiSettings();
			//ʵ����UiSettings����� 
			//mUiSettings.setCompassEnabled(true);  
			mUiSettings.setScrollGesturesEnabled(true);
			mUiSettings.setZoomGesturesEnabled(true);
			mUiSettings.setOverlookingGesturesEnabled(true);
			mMapView.showScaleControl(false);
			 
			
			/* �������û��������ͼ���߻�����ͼʱ����ScrollView���ضϵ���¼��������ݸ���View����Ҳ���ǵ�ͼȥ�������¼���
			 * ���û���ָ̧���ʱ�򣬽�ScrollView��״̬�ָ���֮ǰ��״̬��Ҳ����ScrollView���Խضϵ���¼� */
			final ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
			mMapView.getChildAt(0).setOnTouchListener(new View.OnTouchListener() {
	            @Override
	            public boolean onTouch(View v, MotionEvent event) {
	                if(event.getAction() == MotionEvent.ACTION_UP){
	                    //����ScrollView�ضϵ���¼���ScrollView�ɻ���
	                	scrollView.requestDisallowInterceptTouchEvent(false);
	                }else{
	                    //������ScrollView�ضϵ���¼�������¼�����View����
	                	scrollView.requestDisallowInterceptTouchEvent(true);
	                }
	                return false;
	            }
	        });
			
		}

		 
	}
	
	
	
	private void getMyLocation() {
		//����LocationClient��
		mLocationClient = new LocationClient(getApplicationContext());     
		//ע���������
	    mLocationClient.registerLocationListener(myListener);    
	     
	    LocationClientOption option = new LocationClientOption();

	    //option.setLocationMode(LocationMode.COMPASS);
	    //��ѡ�����ö�λģʽ��Ĭ�ϸ߾���
	    //LocationMode.Hight_Accuracy���߾��ȣ�
	    //LocationMode. Battery_Saving���͹��ģ�
	    //LocationMode. Device_Sensors����ʹ���豸��
	    	
	    option.setCoorType("bd09ll");
	    //��ѡ�����÷��ؾ�γ���������ͣ�Ĭ��gcj02
	    //gcj02����������ꣻ
	    //bd09ll���ٶȾ�γ�����ꣻ
	    //bd09���ٶ�ī�������ꣻ
	    //���������λ�����������������ͣ�ͳһ����wgs84��������
	     	
	    //option.setScanSpan(1000);
	    //��ѡ�����÷���λ����ļ����int���ͣ���λms
	    //�������Ϊ0��������ζ�λ��������λһ�Σ�Ĭ��Ϊ0
	    //������÷�0��������1000ms���ϲ���Ч
	    	
	    option.setOpenGps(true);
	    //��ѡ�������Ƿ�ʹ��gps��Ĭ��false
	    //ʹ�ø߾��Ⱥͽ����豸���ֶ�λģʽ�ģ�������������Ϊtrue
	    	
	    option.setLocationNotify(true);
	    //��ѡ�������Ƿ�GPS��Чʱ����1S/1��Ƶ�����GPS�����Ĭ��false
	    	
	    option.setIgnoreKillProcess(false);
	    //��ѡ����λSDK�ڲ���һ��service�����ŵ��˶������̡�
	    //�����Ƿ���stop��ʱ��ɱ��������̣�Ĭ�ϣ����飩��ɱ������setIgnoreKillProcess(true)
	    
	    //option.setIgnoreCacheException(false);
	    //��ѡ�������Ƿ��ռ�Crash��Ϣ��Ĭ���ռ���������Ϊfalse

	     
	    //option.setWifiValidTime(5*60*1000);
	    //��ѡ��7.2�汾��������
	    //��������˸ýӿڣ��״�������λʱ�������жϵ�ǰWiFi�Ƿ񳬳���Ч�ڣ���������Ч�ڣ���������ɨ��WiFi��Ȼ��λ
	    	
	    option.setEnableSimulateGps(false);
	    //��ѡ�������Ƿ���Ҫ����GPS��������Ĭ����Ҫ��������Ϊfalse
	    	
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
					booImageBm.compress(Bitmap.CompressFormat.JPEG, 30, jpgOutputStream);// ������д���ļ� 
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
