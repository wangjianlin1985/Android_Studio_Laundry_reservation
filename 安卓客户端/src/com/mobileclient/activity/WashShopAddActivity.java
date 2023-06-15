package com.mobileclient.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import com.mobileclient.util.HttpUtil;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
public class WashShopAddActivity extends Activity {
	// ����ȷ����Ӱ�ť
	private Button btnAdd;
	// ����ϴ�µ��˺������
	private EditText ET_shopUserName;
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//ȥ��title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//ȥ��Activity�����״̬��
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN); 
		// ���õ�ǰActivity���沼��
		setContentView(R.layout.washshop_add); 
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("���ϴ�µ�");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		ET_shopUserName = (EditText) findViewById(R.id.ET_shopUserName);
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
		// ���������б�ķ��
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
				intent.setClass(WashShopAddActivity.this,photoListActivity.class);
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
		btnAdd = (Button) findViewById(R.id.BtnAdd);
		/*�������ϴ�µ갴ť*/
		btnAdd.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*��֤��ȡϴ�µ��˺�*/
					if(ET_shopUserName.getText().toString().equals("")) {
						Toast.makeText(WashShopAddActivity.this, "ϴ�µ��˺����벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_shopUserName.setFocusable(true);
						ET_shopUserName.requestFocus();
						return;
					}
					washShop.setShopUserName(ET_shopUserName.getText().toString());
					/*��֤��ȡ��¼����*/ 
					if(ET_password.getText().toString().equals("")) {
						Toast.makeText(WashShopAddActivity.this, "��¼�������벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_password.setFocusable(true);
						ET_password.requestFocus();
						return;	
					}
					washShop.setPassword(ET_password.getText().toString());
					/*��֤��ȡϴ�µ�����*/ 
					if(ET_shopName.getText().toString().equals("")) {
						Toast.makeText(WashShopAddActivity.this, "ϴ�µ��������벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_shopName.setFocusable(true);
						ET_shopName.requestFocus();
						return;	
					}
					washShop.setShopName(ET_shopName.getText().toString());
					if(washShop.getShopPhoto() != null) {
						//���ͼƬ��ַ��Ϊ�գ�˵���û�ѡ����ͼƬ����ʱ��Ҫ���ӷ������ϴ�ͼƬ
						WashShopAddActivity.this.setTitle("�����ϴ�ͼƬ���Ե�...");
						String shopPhoto = HttpUtil.uploadFile(washShop.getShopPhoto());
						WashShopAddActivity.this.setTitle("ͼƬ�ϴ���ϣ�");
						washShop.setShopPhoto(shopPhoto);
					} else {
						washShop.setShopPhoto("upload/noimage.jpg");
					}
					/*��֤��ȡ��ҵ绰*/ 
					if(ET_telephone.getText().toString().equals("")) {
						Toast.makeText(WashShopAddActivity.this, "��ҵ绰���벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_telephone.setFocusable(true);
						ET_telephone.requestFocus();
						return;	
					}
					washShop.setTelephone(ET_telephone.getText().toString());
					/*��ȡ��פ����*/
					Date addDate = new Date(dp_addDate.getYear()-1900,dp_addDate.getMonth(),dp_addDate.getDayOfMonth());
					washShop.setAddDate(new Timestamp(addDate.getTime()));
					/*��֤��ȡ���̵�ַ*/ 
					if(ET_address.getText().toString().equals("")) {
						Toast.makeText(WashShopAddActivity.this, "���̵�ַ���벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_address.setFocusable(true);
						ET_address.requestFocus();
						return;	
					}
					washShop.setAddress(ET_address.getText().toString());
					/*��֤��ȡγ��*/ 
					if(ET_latitude.getText().toString().equals("")) {
						Toast.makeText(WashShopAddActivity.this, "γ�����벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_latitude.setFocusable(true);
						ET_latitude.requestFocus();
						return;	
					}
					washShop.setLatitude(Float.parseFloat(ET_latitude.getText().toString()));
					/*��֤��ȡ����*/ 
					if(ET_longitude.getText().toString().equals("")) {
						Toast.makeText(WashShopAddActivity.this, "�������벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_longitude.setFocusable(true);
						ET_longitude.requestFocus();
						return;	
					}
					washShop.setLongitude(Float.parseFloat(ET_longitude.getText().toString()));
					/*����ҵ���߼����ϴ�ϴ�µ���Ϣ*/
					WashShopAddActivity.this.setTitle("�����ϴ�ϴ�µ���Ϣ���Ե�...");
					String result = washShopService.AddWashShop(washShop);
					Toast.makeText(getApplicationContext(), result, 1).show(); 
					Intent intent = getIntent();
					setResult(RESULT_OK,intent);
					finish();
				} catch (Exception e) {}
			}
		});
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
