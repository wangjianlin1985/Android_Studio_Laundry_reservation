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
	// ����ȷ����Ӱ�ť
	private Button btnUpdate;
	// ����ϴ�µ��˺�TextView
	private TextView TV_shopUserName;
	// ������¼���������
	private EditText ET_password;
	// ����ϴ�µ����������
	private EditText ET_shopName;
	// ����ϴ�µ�����������
	private Spinner spinner_washClassObj;
	private ArrayAdapter<String> washClassObj_adapter;
	private static  String[] washClassObj_ShowText  = null;
	private List<WashClass> washClassList = null;
	/*ϴ�µ��������ҵ���߼���*/
	private WashClassService washClassService = new WashClassService();
	// ����ϴ�µ���ƬͼƬ��ؼ�
	private ImageView iv_shopPhoto;
	private Button btn_shopPhoto;
	protected int REQ_CODE_SELECT_IMAGE_shopPhoto = 1;
	private int REQ_CODE_CAMERA_shopPhoto = 2;
	// ������ҵ绰�����
	private EditText ET_telephone;
	// ������פ���ڿؼ�
	private DatePicker dp_addDate;
	// �������̵�ַ�����
	private EditText ET_address;
	// ����γ�������
	private EditText ET_latitude;
	// �������������
	private EditText ET_longitude;
	protected String carmera_path;
	/*Ҫ�����ϴ�µ���Ϣ*/
	WashShop washShop = new WashShop();
	/*ϴ�µ����ҵ���߼���*/
	private WashShopService washShopService = new WashShopService();

	private String shopUserName;
	
	MapView mMapView = null;
	BaiduMap mBaiduMap = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//ȥ��title
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		//ȥ��Activity�����״̬��
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
		// ���õ�ǰActivity���沼��
		setContentView(R.layout.washshop_edit); 
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("�༭ϴ�µ���Ϣ");
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
		// ��ȡ���е�ϴ�µ�����
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
		// ����ѡ������ArrayAdapter��������
		washClassObj_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, washClassObj_ShowText);
		// ����ͼ����������б�ķ��
		washClassObj_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// ��adapter ��ӵ�spinner��
		spinner_washClassObj.setAdapter(washClassObj_adapter);
		// ����¼�Spinner�¼�����
		spinner_washClassObj.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				washShop.setWashClassObj(washClassList.get(arg2).getClassId()); 
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// ����Ĭ��ֵ
		spinner_washClassObj.setVisibility(View.VISIBLE);
		iv_shopPhoto = (ImageView) findViewById(R.id.iv_shopPhoto);
		/*����ͼƬ��ʾ�ؼ�ʱ����ͼƬ��ѡ��*/
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
		/*�����޸�ϴ�µ갴ť*/
		btnUpdate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*��֤��ȡ��¼����*/ 
					if(ET_password.getText().toString().equals("")) {
						Toast.makeText(WashShopEditActivity.this, "��¼�������벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_password.setFocusable(true);
						ET_password.requestFocus();
						return;	
					}
					washShop.setPassword(ET_password.getText().toString());
					/*��֤��ȡϴ�µ�����*/ 
					if(ET_shopName.getText().toString().equals("")) {
						Toast.makeText(WashShopEditActivity.this, "ϴ�µ��������벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_shopName.setFocusable(true);
						ET_shopName.requestFocus();
						return;	
					}
					washShop.setShopName(ET_shopName.getText().toString());
					if (!washShop.getShopPhoto().startsWith("upload/")) {
						//���ͼƬ��ַ��Ϊ�գ�˵���û�ѡ����ͼƬ����ʱ��Ҫ���ӷ������ϴ�ͼƬ
						WashShopEditActivity.this.setTitle("�����ϴ�ͼƬ���Ե�...");
						String shopPhoto = HttpUtil.uploadFile(washShop.getShopPhoto());
						WashShopEditActivity.this.setTitle("ͼƬ�ϴ���ϣ�");
						washShop.setShopPhoto(shopPhoto);
					} 
					/*��֤��ȡ��ҵ绰*/ 
					if(ET_telephone.getText().toString().equals("")) {
						Toast.makeText(WashShopEditActivity.this, "��ҵ绰���벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_telephone.setFocusable(true);
						ET_telephone.requestFocus();
						return;	
					}
					washShop.setTelephone(ET_telephone.getText().toString());
					/*��ȡ��������*/
					Date addDate = new Date(dp_addDate.getYear()-1900,dp_addDate.getMonth(),dp_addDate.getDayOfMonth());
					washShop.setAddDate(new Timestamp(addDate.getTime()));
					/*��֤��ȡ���̵�ַ*/ 
					if(ET_address.getText().toString().equals("")) {
						Toast.makeText(WashShopEditActivity.this, "���̵�ַ���벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_address.setFocusable(true);
						ET_address.requestFocus();
						return;	
					}
					washShop.setAddress(ET_address.getText().toString());
					/*��֤��ȡγ��*/ 
					if(ET_latitude.getText().toString().equals("")) {
						Toast.makeText(WashShopEditActivity.this, "γ�����벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_latitude.setFocusable(true);
						ET_latitude.requestFocus();
						return;	
					}
					washShop.setLatitude(Float.parseFloat(ET_latitude.getText().toString()));
					/*��֤��ȡ����*/ 
					if(ET_longitude.getText().toString().equals("")) {
						Toast.makeText(WashShopEditActivity.this, "�������벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_longitude.setFocusable(true);
						ET_longitude.requestFocus();
						return;	
					}
					washShop.setLongitude(Float.parseFloat(ET_longitude.getText().toString()));
					/*����ҵ���߼����ϴ�ϴ�µ���Ϣ*/
					WashShopEditActivity.this.setTitle("���ڸ���ϴ�µ���Ϣ���Ե�...");
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
	
	
	private void InitCenter() {
		//�趨���ĵ����� 
	    LatLng cenpt = new LatLng(washShop.getLatitude(), washShop.getLongitude()); 
	   
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


	/* ��ʼ����ʾ�༭��������� */
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
			// ��ȡͼƬ����
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
					booImageBm.compress(Bitmap.CompressFormat.JPEG, 30, jpgOutputStream);// ������д���ļ� 
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
