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
	// �������ذ�ť
	private Button btnReturn;
	// ����ϴ�µ��˺ſؼ�
	private TextView TV_shopUserName;
	// ������¼����ؼ�
	private TextView TV_password;
	// ����ϴ�µ����ƿؼ�
	private TextView TV_shopName;
	// ����ϴ�µ�����ؼ�
	private TextView TV_washClassObj;
	// ����ϴ�µ���ƬͼƬ��
	private ImageView iv_shopPhoto;
	// ������ҵ绰�ؼ�
	private TextView TV_telephone;
	// ������פ���ڿؼ�
	private TextView TV_addDate;
	// �������̵�ַ�ؼ�
	private TextView TV_address;
	// ����γ�ȿؼ�
	private TextView TV_latitude;
	// �������ȿؼ�
	private TextView TV_longitude;
	/* Ҫ�����ϴ�µ���Ϣ */
	WashShop washShop = new WashShop(); 
	/* ϴ�µ����ҵ���߼��� */
	private WashShopService washShopService = new WashShopService();
	private WashClassService washClassService = new WashClassService();
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
		setContentView(R.layout.washshop_detail);
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("�鿴ϴ�µ�����");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		// ͨ��findViewById����ʵ�������
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
		//����Markerͼ��
		BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_geo);   
		//�ڵ�ͼ�����Marker������ʾ     
		//����MarkerOption�������ڵ�ͼ�����Marker   
		MarkerOptions options = new MarkerOptions()  
		    .position(point) 
		    .icon(bitmap) 
		    .zIndex(9)  //����Marker���ڲ㼶
			.draggable(false);  //����������ק
	  
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
		    	TV_latitude.setText((float)latLng.latitude + "");
		    	TV_latitude.setText((float)latLng.longitude + "");
		    	
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
		 * ���û���ָ̧���ʱ�򣬽�ScrollView��״̬�ָ���֮ǰ��״̬��Ҳ����ScrollView���Խضϵ���¼�*/
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



	/* ��ʼ����ʾ������������ */
	private void initViewData() {
	    washShop = washShopService.GetWashShop(shopUserName); 
		this.TV_shopUserName.setText(washShop.getShopUserName());
		this.TV_password.setText(washShop.getPassword());
		this.TV_shopName.setText(washShop.getShopName());
		WashClass washClassObj = washClassService.GetWashClass(washShop.getWashClassObj());
		this.TV_washClassObj.setText(washClassObj.getClassName());
		byte[] shopPhoto_data = null;
		try {
			// ��ȡͼƬ����
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
