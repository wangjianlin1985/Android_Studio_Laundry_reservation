package com.mobileclient.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.mobileclient.util.HttpUtil;
import com.mobileclient.util.ImageService;
import com.mobileclient.domain.WashMeal;
import com.mobileclient.service.WashMealService;
import com.mobileclient.domain.WashShop;
import com.mobileclient.service.WashShopService;
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
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Spinner;
import android.widget.Toast;

public class WashMealEditActivity extends Activity {
	// ����ȷ����Ӱ�ť
	private Button btnUpdate;
	// �����ײ�idTextView
	private TextView TV_mealId;
	// ����ϴ���ײ������
	private EditText ET_mealName;
	// �����ײ�˵�������
	private EditText ET_introduce;
	// �����ײͼ۸������
	private EditText ET_price;
	// �����ײ�ͼƬͼƬ��ؼ�
	private ImageView iv_mealPhoto;
	private Button btn_mealPhoto;
	protected int REQ_CODE_SELECT_IMAGE_mealPhoto = 1;
	private int REQ_CODE_CAMERA_mealPhoto = 2;
	// ���淢�����ڿؼ�
	private DatePicker dp_publishDate;
	// ����ϴ�µ�������
	private Spinner spinner_washShopObj;
	private ArrayAdapter<String> washShopObj_adapter;
	private static  String[] washShopObj_ShowText  = null;
	private List<WashShop> washShopList = null;
	/*ϴ�µ����ҵ���߼���*/
	private WashShopService washShopService = new WashShopService();
	protected String carmera_path;
	/*Ҫ�����ϴ���ײ���Ϣ*/
	WashMeal washMeal = new WashMeal();
	/*ϴ���ײ͹���ҵ���߼���*/
	private WashMealService washMealService = new WashMealService();

	private int mealId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//ȥ��title
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		//ȥ��Activity�����״̬��
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
		// ���õ�ǰActivity���沼��
		setContentView(R.layout.washmeal_edit); 
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("�༭ϴ���ײ���Ϣ");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		TV_mealId = (TextView) findViewById(R.id.TV_mealId);
		ET_mealName = (EditText) findViewById(R.id.ET_mealName);
		ET_introduce = (EditText) findViewById(R.id.ET_introduce);
		ET_price = (EditText) findViewById(R.id.ET_price);
		iv_mealPhoto = (ImageView) findViewById(R.id.iv_mealPhoto);
		/*����ͼƬ��ʾ�ؼ�ʱ����ͼƬ��ѡ��*/
		iv_mealPhoto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(WashMealEditActivity.this,photoListActivity.class);
				startActivityForResult(intent,REQ_CODE_SELECT_IMAGE_mealPhoto);
			}
		});
		btn_mealPhoto = (Button) findViewById(R.id.btn_mealPhoto);
		btn_mealPhoto.setOnClickListener(new OnClickListener() { 
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 
				carmera_path = HttpUtil.FILE_PATH + "/carmera_mealPhoto.bmp";
				File out = new File(carmera_path); 
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(out)); 
				startActivityForResult(intent, REQ_CODE_CAMERA_mealPhoto);  
			}
		});
		dp_publishDate = (DatePicker)this.findViewById(R.id.dp_publishDate);
		spinner_washShopObj = (Spinner) findViewById(R.id.Spinner_washShopObj);
		// ��ȡ���е�ϴ�µ�
		try {
			washShopList = washShopService.QueryWashShop(null);
		} catch (Exception e1) { 
			e1.printStackTrace(); 
		}
		int washShopCount = washShopList.size();
		washShopObj_ShowText = new String[washShopCount];
		for(int i=0;i<washShopCount;i++) { 
			washShopObj_ShowText[i] = washShopList.get(i).getShopName();
		}
		// ����ѡ������ArrayAdapter��������
		washShopObj_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, washShopObj_ShowText);
		// ����ͼ����������б�ķ��
		washShopObj_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// ��adapter ��ӵ�spinner��
		spinner_washShopObj.setAdapter(washShopObj_adapter);
		// ����¼�Spinner�¼�����
		spinner_washShopObj.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				washMeal.setWashShopObj(washShopList.get(arg2).getShopUserName()); 
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// ����Ĭ��ֵ
		spinner_washShopObj.setVisibility(View.VISIBLE);
		btnUpdate = (Button) findViewById(R.id.BtnUpdate);
		Bundle extras = this.getIntent().getExtras();
		mealId = extras.getInt("mealId");
		/*�����޸�ϴ���ײͰ�ť*/
		btnUpdate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*��֤��ȡϴ���ײ�*/ 
					if(ET_mealName.getText().toString().equals("")) {
						Toast.makeText(WashMealEditActivity.this, "ϴ���ײ����벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_mealName.setFocusable(true);
						ET_mealName.requestFocus();
						return;	
					}
					washMeal.setMealName(ET_mealName.getText().toString());
					/*��֤��ȡ�ײ�˵��*/ 
					if(ET_introduce.getText().toString().equals("")) {
						Toast.makeText(WashMealEditActivity.this, "�ײ�˵�����벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_introduce.setFocusable(true);
						ET_introduce.requestFocus();
						return;	
					}
					washMeal.setIntroduce(ET_introduce.getText().toString());
					/*��֤��ȡ�ײͼ۸�*/ 
					if(ET_price.getText().toString().equals("")) {
						Toast.makeText(WashMealEditActivity.this, "�ײͼ۸����벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_price.setFocusable(true);
						ET_price.requestFocus();
						return;	
					}
					washMeal.setPrice(Float.parseFloat(ET_price.getText().toString()));
					if (!washMeal.getMealPhoto().startsWith("upload/")) {
						//���ͼƬ��ַ��Ϊ�գ�˵���û�ѡ����ͼƬ����ʱ��Ҫ���ӷ������ϴ�ͼƬ
						WashMealEditActivity.this.setTitle("�����ϴ�ͼƬ���Ե�...");
						String mealPhoto = HttpUtil.uploadFile(washMeal.getMealPhoto());
						WashMealEditActivity.this.setTitle("ͼƬ�ϴ���ϣ�");
						washMeal.setMealPhoto(mealPhoto);
					} 
					/*��ȡ��������*/
					Date publishDate = new Date(dp_publishDate.getYear()-1900,dp_publishDate.getMonth(),dp_publishDate.getDayOfMonth());
					washMeal.setPublishDate(new Timestamp(publishDate.getTime()));
					/*����ҵ���߼����ϴ�ϴ���ײ���Ϣ*/
					WashMealEditActivity.this.setTitle("���ڸ���ϴ���ײ���Ϣ���Ե�...");
					String result = washMealService.UpdateWashMeal(washMeal);
					Toast.makeText(getApplicationContext(), result, 1).show(); 
					Intent intent = getIntent();
					setResult(RESULT_OK,intent);
					finish();
				} catch (Exception e) {}
			}
		});
		initViewData();
	}

	/* ��ʼ����ʾ�༭��������� */
	private void initViewData() {
	    washMeal = washMealService.GetWashMeal(mealId);
		this.TV_mealId.setText(mealId+"");
		this.ET_mealName.setText(washMeal.getMealName());
		this.ET_introduce.setText(washMeal.getIntroduce());
		this.ET_price.setText(washMeal.getPrice() + "");
		byte[] mealPhoto_data = null;
		try {
			// ��ȡͼƬ����
			mealPhoto_data = ImageService.getImage(HttpUtil.BASE_URL + washMeal.getMealPhoto());
			Bitmap mealPhoto = BitmapFactory.decodeByteArray(mealPhoto_data, 0, mealPhoto_data.length);
			this.iv_mealPhoto.setImageBitmap(mealPhoto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Date publishDate = new Date(washMeal.getPublishDate().getTime());
		this.dp_publishDate.init(publishDate.getYear() + 1900,publishDate.getMonth(), publishDate.getDate(), null);
		for (int i = 0; i < washShopList.size(); i++) {
			if (washMeal.getWashShopObj().equals(washShopList.get(i).getShopUserName())) {
				this.spinner_washShopObj.setSelection(i);
				break;
			}
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQ_CODE_CAMERA_mealPhoto  && resultCode == Activity.RESULT_OK) {
			carmera_path = HttpUtil.FILE_PATH + "/carmera_mealPhoto.bmp"; 
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(carmera_path, opts); 
			opts.inSampleSize = photoListActivity.computeSampleSize(opts, -1, 300*300);
			opts.inJustDecodeBounds = false;
			try {
				Bitmap booImageBm = BitmapFactory.decodeFile(carmera_path, opts);
				String jpgFileName = "carmera_mealPhoto.jpg";
				String jpgFilePath =  HttpUtil.FILE_PATH + "/" + jpgFileName;
				try {
					FileOutputStream jpgOutputStream = new FileOutputStream(jpgFilePath);
					booImageBm.compress(Bitmap.CompressFormat.JPEG, 30, jpgOutputStream);// ������д���ļ� 
					File bmpFile = new File(carmera_path);
					bmpFile.delete();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} 
				this.iv_mealPhoto.setImageBitmap(booImageBm);
				this.iv_mealPhoto.setScaleType(ScaleType.FIT_CENTER);
				this.washMeal.setMealPhoto(jpgFileName);
			} catch (OutOfMemoryError err) {  }
		}

		if(requestCode == REQ_CODE_SELECT_IMAGE_mealPhoto && resultCode == Activity.RESULT_OK) {
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
				this.iv_mealPhoto.setImageBitmap(bm); 
				this.iv_mealPhoto.setScaleType(ScaleType.FIT_CENTER); 
			} catch (OutOfMemoryError err) {  } 
			washMeal.setMealPhoto(filename); 
		}
	}
}
