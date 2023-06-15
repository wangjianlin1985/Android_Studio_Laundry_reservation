package com.mobileclient.activity;

import java.util.Date;
import com.mobileclient.domain.MealEvaluate;
import com.mobileclient.service.MealEvaluateService;
import com.mobileclient.domain.WashMeal;
import com.mobileclient.service.WashMealService;
import com.mobileclient.domain.UserInfo;
import com.mobileclient.service.UserInfoService;
import com.mobileclient.util.HttpUtil;
import com.mobileclient.util.ImageService;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import android.widget.Toast;
public class MealEvaluateDetailActivity extends Activity {
	// �������ذ�ť
	private Button btnReturn;
	// ��������id�ؼ�
	private TextView TV_evaluateId;
	// ���������ײͿؼ�
	private TextView TV_washMealObj;
	// �����������ݿؼ�
	private TextView TV_evaluateContent;
	// ���������û��ؼ�
	private TextView TV_userObj;
	// ��������ʱ��ؼ�
	private TextView TV_evaluateTime;
	/* Ҫ������ײ�������Ϣ */
	MealEvaluate mealEvaluate = new MealEvaluate(); 
	/* �ײ����۹���ҵ���߼��� */
	private MealEvaluateService mealEvaluateService = new MealEvaluateService();
	private WashMealService washMealService = new WashMealService();
	private UserInfoService userInfoService = new UserInfoService();
	private int evaluateId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//ȥ��title 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//ȥ��Activity�����״̬��
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN); 
		// ���õ�ǰActivity���沼��
		setContentView(R.layout.mealevaluate_detail);
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("�鿴�ײ���������");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		// ͨ��findViewById����ʵ�������
		btnReturn = (Button) findViewById(R.id.btnReturn);
		TV_evaluateId = (TextView) findViewById(R.id.TV_evaluateId);
		TV_washMealObj = (TextView) findViewById(R.id.TV_washMealObj);
		TV_evaluateContent = (TextView) findViewById(R.id.TV_evaluateContent);
		TV_userObj = (TextView) findViewById(R.id.TV_userObj);
		TV_evaluateTime = (TextView) findViewById(R.id.TV_evaluateTime);
		Bundle extras = this.getIntent().getExtras();
		evaluateId = extras.getInt("evaluateId");
		btnReturn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				MealEvaluateDetailActivity.this.finish();
			}
		}); 
		initViewData();
	}
	/* ��ʼ����ʾ������������ */
	private void initViewData() {
	    mealEvaluate = mealEvaluateService.GetMealEvaluate(evaluateId); 
		this.TV_evaluateId.setText(mealEvaluate.getEvaluateId() + "");
		WashMeal washMealObj = washMealService.GetWashMeal(mealEvaluate.getWashMealObj());
		this.TV_washMealObj.setText(washMealObj.getMealName());
		this.TV_evaluateContent.setText(mealEvaluate.getEvaluateContent());
		UserInfo userObj = userInfoService.GetUserInfo(mealEvaluate.getUserObj());
		this.TV_userObj.setText(userObj.getName());
		this.TV_evaluateTime.setText(mealEvaluate.getEvaluateTime());
	} 
}
