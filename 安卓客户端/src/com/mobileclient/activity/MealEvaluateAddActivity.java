package com.mobileclient.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import com.mobileclient.util.HttpUtil;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.mobileclient.domain.MealEvaluate;
import com.mobileclient.service.MealEvaluateService;
import com.mobileclient.domain.WashMeal;
import com.mobileclient.service.WashMealService;
import com.mobileclient.domain.UserInfo;
import com.mobileclient.service.UserInfoService;
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
public class MealEvaluateAddActivity extends Activity {
	// 声明确定添加按钮
	private Button btnAdd;
	// 声明被评套餐下拉框
	private Spinner spinner_washMealObj;
	private ArrayAdapter<String> washMealObj_adapter;
	private static  String[] washMealObj_ShowText  = null;
	private List<WashMeal> washMealList = null;
	/*被评套餐管理业务逻辑层*/
	private WashMealService washMealService = new WashMealService();
	// 声明评价内容输入框
	private EditText ET_evaluateContent;
	// 声明评价用户下拉框
	private Spinner spinner_userObj;
	private ArrayAdapter<String> userObj_adapter;
	private static  String[] userObj_ShowText  = null;
	private List<UserInfo> userInfoList = null;
	/*评价用户管理业务逻辑层*/
	private UserInfoService userInfoService = new UserInfoService();
	// 声明评价时间输入框
	private EditText ET_evaluateTime;
	protected String carmera_path;
	/*要保存的套餐评价信息*/
	MealEvaluate mealEvaluate = new MealEvaluate();
	/*套餐评价管理业务逻辑层*/
	private MealEvaluateService mealEvaluateService = new MealEvaluateService();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN); 
		// 设置当前Activity界面布局
		setContentView(R.layout.mealevaluate_add); 
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("添加套餐评价");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		spinner_washMealObj = (Spinner) findViewById(R.id.Spinner_washMealObj);
		// 获取所有的被评套餐
		try {
			washMealList = washMealService.QueryWashMeal(null);
		} catch (Exception e1) { 
			e1.printStackTrace(); 
		}
		int washMealCount = washMealList.size();
		washMealObj_ShowText = new String[washMealCount];
		for(int i=0;i<washMealCount;i++) { 
			washMealObj_ShowText[i] = washMealList.get(i).getMealName();
		}
		// 将可选内容与ArrayAdapter连接起来
		washMealObj_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, washMealObj_ShowText);
		// 设置下拉列表的风格
		washMealObj_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_washMealObj.setAdapter(washMealObj_adapter);
		// 添加事件Spinner事件监听
		spinner_washMealObj.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				mealEvaluate.setWashMealObj(washMealList.get(arg2).getMealId()); 
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// 设置默认值
		spinner_washMealObj.setVisibility(View.VISIBLE);
		ET_evaluateContent = (EditText) findViewById(R.id.ET_evaluateContent);
		spinner_userObj = (Spinner) findViewById(R.id.Spinner_userObj);
		// 获取所有的评价用户
		try {
			userInfoList = userInfoService.QueryUserInfo(null);
		} catch (Exception e1) { 
			e1.printStackTrace(); 
		}
		int userInfoCount = userInfoList.size();
		userObj_ShowText = new String[userInfoCount];
		for(int i=0;i<userInfoCount;i++) { 
			userObj_ShowText[i] = userInfoList.get(i).getName();
		}
		// 将可选内容与ArrayAdapter连接起来
		userObj_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, userObj_ShowText);
		// 设置下拉列表的风格
		userObj_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_userObj.setAdapter(userObj_adapter);
		// 添加事件Spinner事件监听
		spinner_userObj.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				mealEvaluate.setUserObj(userInfoList.get(arg2).getUser_name()); 
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// 设置默认值
		spinner_userObj.setVisibility(View.VISIBLE);
		ET_evaluateTime = (EditText) findViewById(R.id.ET_evaluateTime);
		btnAdd = (Button) findViewById(R.id.BtnAdd);
		/*单击添加套餐评价按钮*/
		btnAdd.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*验证获取评价内容*/ 
					if(ET_evaluateContent.getText().toString().equals("")) {
						Toast.makeText(MealEvaluateAddActivity.this, "评价内容输入不能为空!", Toast.LENGTH_LONG).show();
						ET_evaluateContent.setFocusable(true);
						ET_evaluateContent.requestFocus();
						return;	
					}
					mealEvaluate.setEvaluateContent(ET_evaluateContent.getText().toString());
					/*验证获取评价时间*/ 
					if(ET_evaluateTime.getText().toString().equals("")) {
						Toast.makeText(MealEvaluateAddActivity.this, "评价时间输入不能为空!", Toast.LENGTH_LONG).show();
						ET_evaluateTime.setFocusable(true);
						ET_evaluateTime.requestFocus();
						return;	
					}
					mealEvaluate.setEvaluateTime(ET_evaluateTime.getText().toString());
					/*调用业务逻辑层上传套餐评价信息*/
					MealEvaluateAddActivity.this.setTitle("正在上传套餐评价信息，稍等...");
					String result = mealEvaluateService.AddMealEvaluate(mealEvaluate);
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
	}
}
