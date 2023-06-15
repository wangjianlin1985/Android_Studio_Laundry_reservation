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
	// ����ȷ����Ӱ�ť
	private Button btnAdd;
	// ���������ײ�������
	private Spinner spinner_washMealObj;
	private ArrayAdapter<String> washMealObj_adapter;
	private static  String[] washMealObj_ShowText  = null;
	private List<WashMeal> washMealList = null;
	/*�����ײ͹���ҵ���߼���*/
	private WashMealService washMealService = new WashMealService();
	// �����������������
	private EditText ET_evaluateContent;
	// ���������û�������
	private Spinner spinner_userObj;
	private ArrayAdapter<String> userObj_adapter;
	private static  String[] userObj_ShowText  = null;
	private List<UserInfo> userInfoList = null;
	/*�����û�����ҵ���߼���*/
	private UserInfoService userInfoService = new UserInfoService();
	// ��������ʱ�������
	private EditText ET_evaluateTime;
	protected String carmera_path;
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
		setContentView(R.layout.mealevaluate_add); 
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
		spinner_washMealObj = (Spinner) findViewById(R.id.Spinner_washMealObj);
		// ��ȡ���еı����ײ�
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
		// ����ѡ������ArrayAdapter��������
		washMealObj_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, washMealObj_ShowText);
		// ���������б�ķ��
		washMealObj_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// ��adapter ��ӵ�spinner��
		spinner_washMealObj.setAdapter(washMealObj_adapter);
		// ����¼�Spinner�¼�����
		spinner_washMealObj.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				mealEvaluate.setWashMealObj(washMealList.get(arg2).getMealId()); 
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// ����Ĭ��ֵ
		spinner_washMealObj.setVisibility(View.VISIBLE);
		ET_evaluateContent = (EditText) findViewById(R.id.ET_evaluateContent);
		spinner_userObj = (Spinner) findViewById(R.id.Spinner_userObj);
		// ��ȡ���е������û�
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
		// ����ѡ������ArrayAdapter��������
		userObj_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, userObj_ShowText);
		// ���������б�ķ��
		userObj_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// ��adapter ��ӵ�spinner��
		spinner_userObj.setAdapter(userObj_adapter);
		// ����¼�Spinner�¼�����
		spinner_userObj.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				mealEvaluate.setUserObj(userInfoList.get(arg2).getUser_name()); 
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// ����Ĭ��ֵ
		spinner_userObj.setVisibility(View.VISIBLE);
		ET_evaluateTime = (EditText) findViewById(R.id.ET_evaluateTime);
		btnAdd = (Button) findViewById(R.id.BtnAdd);
		/*��������ײ����۰�ť*/
		btnAdd.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*��֤��ȡ��������*/ 
					if(ET_evaluateContent.getText().toString().equals("")) {
						Toast.makeText(MealEvaluateAddActivity.this, "�����������벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_evaluateContent.setFocusable(true);
						ET_evaluateContent.requestFocus();
						return;	
					}
					mealEvaluate.setEvaluateContent(ET_evaluateContent.getText().toString());
					/*��֤��ȡ����ʱ��*/ 
					if(ET_evaluateTime.getText().toString().equals("")) {
						Toast.makeText(MealEvaluateAddActivity.this, "����ʱ�����벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_evaluateTime.setFocusable(true);
						ET_evaluateTime.requestFocus();
						return;	
					}
					mealEvaluate.setEvaluateTime(ET_evaluateTime.getText().toString());
					/*����ҵ���߼����ϴ��ײ�������Ϣ*/
					MealEvaluateAddActivity.this.setTitle("�����ϴ��ײ�������Ϣ���Ե�...");
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
