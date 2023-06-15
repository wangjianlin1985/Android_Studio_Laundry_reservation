package com.mobileclient.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import com.mobileclient.util.HttpUtil;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.mobileclient.app.Declare;
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
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
public class WashMealShopAddActivity extends Activity {
	// 声明确定添加按钮
	private Button btnAdd;
	// 声明洗衣套餐输入框
	private EditText ET_mealName;
	// 声明套餐说明输入框
	private EditText ET_introduce;
	// 声明套餐价格输入框
	private EditText ET_price;
	// 声明套餐图片图片框控件
	private ImageView iv_mealPhoto;
	private Button btn_mealPhoto;
	protected int REQ_CODE_SELECT_IMAGE_mealPhoto = 1;
	private int REQ_CODE_CAMERA_mealPhoto = 2;
	// 出版发布日期控件
	private DatePicker dp_publishDate;
	 
	protected String carmera_path;
	/*要保存的洗衣套餐信息*/
	WashMeal washMeal = new WashMeal();
	/*洗衣套餐管理业务逻辑层*/
	private WashMealService washMealService = new WashMealService();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN); 
		// 设置当前Activity界面布局
		setContentView(R.layout.washmeal_shop_add); 
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("添加洗衣套餐");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		ET_mealName = (EditText) findViewById(R.id.ET_mealName);
		ET_introduce = (EditText) findViewById(R.id.ET_introduce);
		ET_price = (EditText) findViewById(R.id.ET_price);
		iv_mealPhoto = (ImageView) findViewById(R.id.iv_mealPhoto);
		/*单击图片显示控件时进行图片的选择*/
		iv_mealPhoto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(WashMealShopAddActivity.this,photoListActivity.class);
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
		
		Declare declare = (Declare) WashMealShopAddActivity.this.getApplication();
		washMeal.setWashShopObj(declare.getUserName()); 
			  
		btnAdd = (Button) findViewById(R.id.BtnAdd);
		/*单击添加洗衣套餐按钮*/
		btnAdd.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*验证获取洗衣套餐*/ 
					if(ET_mealName.getText().toString().equals("")) {
						Toast.makeText(WashMealShopAddActivity.this, "洗衣套餐输入不能为空!", Toast.LENGTH_LONG).show();
						ET_mealName.setFocusable(true);
						ET_mealName.requestFocus();
						return;	
					}
					washMeal.setMealName(ET_mealName.getText().toString());
					/*验证获取套餐说明*/ 
					if(ET_introduce.getText().toString().equals("")) {
						Toast.makeText(WashMealShopAddActivity.this, "套餐说明输入不能为空!", Toast.LENGTH_LONG).show();
						ET_introduce.setFocusable(true);
						ET_introduce.requestFocus();
						return;	
					}
					washMeal.setIntroduce(ET_introduce.getText().toString());
					/*验证获取套餐价格*/ 
					if(ET_price.getText().toString().equals("")) {
						Toast.makeText(WashMealShopAddActivity.this, "套餐价格输入不能为空!", Toast.LENGTH_LONG).show();
						ET_price.setFocusable(true);
						ET_price.requestFocus();
						return;	
					}
					washMeal.setPrice(Float.parseFloat(ET_price.getText().toString()));
					if(washMeal.getMealPhoto() != null) {
						//如果图片地址不为空，说明用户选择了图片，这时需要连接服务器上传图片
						WashMealShopAddActivity.this.setTitle("正在上传图片，稍等...");
						String mealPhoto = HttpUtil.uploadFile(washMeal.getMealPhoto());
						WashMealShopAddActivity.this.setTitle("图片上传完毕！");
						washMeal.setMealPhoto(mealPhoto);
					} else {
						washMeal.setMealPhoto("upload/noimage.jpg");
					}
					/*获取发布日期*/
					Date publishDate = new Date(dp_publishDate.getYear()-1900,dp_publishDate.getMonth(),dp_publishDate.getDayOfMonth());
					washMeal.setPublishDate(new Timestamp(publishDate.getTime()));
					/*调用业务逻辑层上传洗衣套餐信息*/
					WashMealShopAddActivity.this.setTitle("正在上传洗衣套餐信息，稍等...");
					String result = washMealService.AddWashMeal(washMeal);
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
					booImageBm.compress(Bitmap.CompressFormat.JPEG, 30, jpgOutputStream);// 把数据写入文件 
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
