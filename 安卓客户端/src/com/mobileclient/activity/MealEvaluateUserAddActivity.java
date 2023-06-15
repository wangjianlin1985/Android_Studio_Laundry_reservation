package com.mobileclient.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import com.mobileclient.util.HttpUtil;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.mobileclient.app.Declare;
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
public class MealEvaluateUserAddActivity extends Activity {
	// ����ȷ����Ӱ�ť
	private Button btnAdd;
	 
	/*�����ײ͹���ҵ���߼���*/
	private WashMealService washMealService = new WashMealService();
	// �����������������
	private EditText ET_evaluateContent;
	  
	 
	/*Ҫ������ײ�������Ϣ*/
	MealEvaluate mealEvaluate = new MealEvaluate();
	/*�ײ����۹���ҵ���߼���*/
	private MealEvaluateService mealEvaluateService = new MealEvaluateService();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//ȥ��title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//ȥ��Activity�����״̬��
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN); 
		// ���õ�ǰActivity���沼��
		setContentView(R.layout.mealevaluate_user_add); 
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("����ײ�����");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		Bundle bundle = this.getIntent().getExtras(); 
		int washMealObj = bundle.getInt("washMealObj");
		mealEvaluate.setWashMealObj(washMealObj);
		
		WashMealService washMealService = new WashMealService();
		String mealName = washMealService.GetWashMeal(washMealObj).getMealName();
		TextView TV_washMealObj = (TextView) findViewById(R.id.TV_washMealObj);
		TV_washMealObj.setText(mealName);
		 
		ET_evaluateContent = (EditText) findViewById(R.id.ET_evaluateContent);
		Declare declare = (Declare) MealEvaluateUserAddActivity.this.getApplication();
		mealEvaluate.setUserObj(declare.getUserName());
		
		btnAdd = (Button) findViewById(R.id.BtnAdd);
		/*��������ײ����۰�ť*/
		btnAdd.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*��֤��ȡ��������*/ 
					if(ET_evaluateContent.getText().toString().equals("")) {
						Toast.makeText(MealEvaluateUserAddActivity.this, "�����������벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_evaluateContent.setFocusable(true);
						ET_evaluateContent.requestFocus();
						return;	
					}
					mealEvaluate.setEvaluateContent(ET_evaluateContent.getText().toString());
					/*����ʱ��*/  
					mealEvaluate.setEvaluateTime("--");
					
					/*����ҵ���߼����ϴ��ײ�������Ϣ*/
					MealEvaluateUserAddActivity.this.setTitle("�����ϴ��ײ�������Ϣ���Ե�...");
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
