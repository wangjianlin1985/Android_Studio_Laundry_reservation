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
	// 声明确定添加按钮
	private Button btnAdd;
	// 声明洗衣店账号输入框
	private EditText ET_shopUserName;
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN); 
		// 设置当前Activity界面布局
		setContentView(R.layout.washshop_add); 
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("添加洗衣店");
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
		// 设置下拉列表的风格
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
		/*单击添加洗衣店按钮*/
		btnAdd.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*验证获取洗衣店账号*/
					if(ET_shopUserName.getText().toString().equals("")) {
						Toast.makeText(WashShopAddActivity.this, "洗衣店账号输入不能为空!", Toast.LENGTH_LONG).show();
						ET_shopUserName.setFocusable(true);
						ET_shopUserName.requestFocus();
						return;
					}
					washShop.setShopUserName(ET_shopUserName.getText().toString());
					/*验证获取登录密码*/ 
					if(ET_password.getText().toString().equals("")) {
						Toast.makeText(WashShopAddActivity.this, "登录密码输入不能为空!", Toast.LENGTH_LONG).show();
						ET_password.setFocusable(true);
						ET_password.requestFocus();
						return;	
					}
					washShop.setPassword(ET_password.getText().toString());
					/*验证获取洗衣店名称*/ 
					if(ET_shopName.getText().toString().equals("")) {
						Toast.makeText(WashShopAddActivity.this, "洗衣店名称输入不能为空!", Toast.LENGTH_LONG).show();
						ET_shopName.setFocusable(true);
						ET_shopName.requestFocus();
						return;	
					}
					washShop.setShopName(ET_shopName.getText().toString());
					if(washShop.getShopPhoto() != null) {
						//如果图片地址不为空，说明用户选择了图片，这时需要连接服务器上传图片
						WashShopAddActivity.this.setTitle("正在上传图片，稍等...");
						String shopPhoto = HttpUtil.uploadFile(washShop.getShopPhoto());
						WashShopAddActivity.this.setTitle("图片上传完毕！");
						washShop.setShopPhoto(shopPhoto);
					} else {
						washShop.setShopPhoto("upload/noimage.jpg");
					}
					/*验证获取店家电话*/ 
					if(ET_telephone.getText().toString().equals("")) {
						Toast.makeText(WashShopAddActivity.this, "店家电话输入不能为空!", Toast.LENGTH_LONG).show();
						ET_telephone.setFocusable(true);
						ET_telephone.requestFocus();
						return;	
					}
					washShop.setTelephone(ET_telephone.getText().toString());
					/*获取入驻日期*/
					Date addDate = new Date(dp_addDate.getYear()-1900,dp_addDate.getMonth(),dp_addDate.getDayOfMonth());
					washShop.setAddDate(new Timestamp(addDate.getTime()));
					/*验证获取店铺地址*/ 
					if(ET_address.getText().toString().equals("")) {
						Toast.makeText(WashShopAddActivity.this, "店铺地址输入不能为空!", Toast.LENGTH_LONG).show();
						ET_address.setFocusable(true);
						ET_address.requestFocus();
						return;	
					}
					washShop.setAddress(ET_address.getText().toString());
					/*验证获取纬度*/ 
					if(ET_latitude.getText().toString().equals("")) {
						Toast.makeText(WashShopAddActivity.this, "纬度输入不能为空!", Toast.LENGTH_LONG).show();
						ET_latitude.setFocusable(true);
						ET_latitude.requestFocus();
						return;	
					}
					washShop.setLatitude(Float.parseFloat(ET_latitude.getText().toString()));
					/*验证获取经度*/ 
					if(ET_longitude.getText().toString().equals("")) {
						Toast.makeText(WashShopAddActivity.this, "经度输入不能为空!", Toast.LENGTH_LONG).show();
						ET_longitude.setFocusable(true);
						ET_longitude.requestFocus();
						return;	
					}
					washShop.setLongitude(Float.parseFloat(ET_longitude.getText().toString()));
					/*调用业务逻辑层上传洗衣店信息*/
					WashShopAddActivity.this.setTitle("正在上传洗衣店信息，稍等...");
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
