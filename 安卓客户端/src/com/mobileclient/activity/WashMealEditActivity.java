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
	// 声明确定添加按钮
	private Button btnUpdate;
	// 声明套餐idTextView
	private TextView TV_mealId;
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
	// 声明洗衣店下拉框
	private Spinner spinner_washShopObj;
	private ArrayAdapter<String> washShopObj_adapter;
	private static  String[] washShopObj_ShowText  = null;
	private List<WashShop> washShopList = null;
	/*洗衣店管理业务逻辑层*/
	private WashShopService washShopService = new WashShopService();
	protected String carmera_path;
	/*要保存的洗衣套餐信息*/
	WashMeal washMeal = new WashMeal();
	/*洗衣套餐管理业务逻辑层*/
	private WashMealService washMealService = new WashMealService();

	private int mealId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
		// 设置当前Activity界面布局
		setContentView(R.layout.washmeal_edit); 
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("编辑洗衣套餐信息");
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
		/*单击图片显示控件时进行图片的选择*/
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
		// 获取所有的洗衣店
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
		// 将可选内容与ArrayAdapter连接起来
		washShopObj_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, washShopObj_ShowText);
		// 设置图书类别下拉列表的风格
		washShopObj_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_washShopObj.setAdapter(washShopObj_adapter);
		// 添加事件Spinner事件监听
		spinner_washShopObj.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				washMeal.setWashShopObj(washShopList.get(arg2).getShopUserName()); 
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// 设置默认值
		spinner_washShopObj.setVisibility(View.VISIBLE);
		btnUpdate = (Button) findViewById(R.id.BtnUpdate);
		Bundle extras = this.getIntent().getExtras();
		mealId = extras.getInt("mealId");
		/*单击修改洗衣套餐按钮*/
		btnUpdate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*验证获取洗衣套餐*/ 
					if(ET_mealName.getText().toString().equals("")) {
						Toast.makeText(WashMealEditActivity.this, "洗衣套餐输入不能为空!", Toast.LENGTH_LONG).show();
						ET_mealName.setFocusable(true);
						ET_mealName.requestFocus();
						return;	
					}
					washMeal.setMealName(ET_mealName.getText().toString());
					/*验证获取套餐说明*/ 
					if(ET_introduce.getText().toString().equals("")) {
						Toast.makeText(WashMealEditActivity.this, "套餐说明输入不能为空!", Toast.LENGTH_LONG).show();
						ET_introduce.setFocusable(true);
						ET_introduce.requestFocus();
						return;	
					}
					washMeal.setIntroduce(ET_introduce.getText().toString());
					/*验证获取套餐价格*/ 
					if(ET_price.getText().toString().equals("")) {
						Toast.makeText(WashMealEditActivity.this, "套餐价格输入不能为空!", Toast.LENGTH_LONG).show();
						ET_price.setFocusable(true);
						ET_price.requestFocus();
						return;	
					}
					washMeal.setPrice(Float.parseFloat(ET_price.getText().toString()));
					if (!washMeal.getMealPhoto().startsWith("upload/")) {
						//如果图片地址不为空，说明用户选择了图片，这时需要连接服务器上传图片
						WashMealEditActivity.this.setTitle("正在上传图片，稍等...");
						String mealPhoto = HttpUtil.uploadFile(washMeal.getMealPhoto());
						WashMealEditActivity.this.setTitle("图片上传完毕！");
						washMeal.setMealPhoto(mealPhoto);
					} 
					/*获取出版日期*/
					Date publishDate = new Date(dp_publishDate.getYear()-1900,dp_publishDate.getMonth(),dp_publishDate.getDayOfMonth());
					washMeal.setPublishDate(new Timestamp(publishDate.getTime()));
					/*调用业务逻辑层上传洗衣套餐信息*/
					WashMealEditActivity.this.setTitle("正在更新洗衣套餐信息，稍等...");
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

	/* 初始化显示编辑界面的数据 */
	private void initViewData() {
	    washMeal = washMealService.GetWashMeal(mealId);
		this.TV_mealId.setText(mealId+"");
		this.ET_mealName.setText(washMeal.getMealName());
		this.ET_introduce.setText(washMeal.getIntroduce());
		this.ET_price.setText(washMeal.getPrice() + "");
		byte[] mealPhoto_data = null;
		try {
			// 获取图片数据
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
