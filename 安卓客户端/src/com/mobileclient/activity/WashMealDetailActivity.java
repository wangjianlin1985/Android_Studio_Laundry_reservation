package com.mobileclient.activity;

import java.util.Date;

import com.mobileclient.app.Declare;
import com.mobileclient.domain.WashMeal;
import com.mobileclient.service.WashMealService;
import com.mobileclient.domain.WashShop;
import com.mobileclient.service.WashShopService;
import com.mobileclient.util.ActivityUtils;
import com.mobileclient.util.HttpUtil;
import com.mobileclient.util.ImageService;
import android.app.Activity;
import android.content.Intent;
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
public class WashMealDetailActivity extends Activity {
	// �������ذ�ť
	private Button btnReturn;
	// �����ײ�id�ؼ�
	private TextView TV_mealId;
	// ����ϴ���ײͿؼ�
	private TextView TV_mealName;
	// �����ײ�˵���ؼ�
	private TextView TV_introduce;
	// �����ײͼ۸�ؼ�
	private TextView TV_price;
	// �����ײ�ͼƬͼƬ��
	private ImageView iv_mealPhoto;
	// �����������ڿؼ�
	private TextView TV_publishDate;
	// ����ϴ�µ�ؼ�
	private TextView TV_washShopObj;
	/* Ҫ�����ϴ���ײ���Ϣ */
	WashMeal washMeal = new WashMeal(); 
	/* ϴ���ײ͹���ҵ���߼��� */
	private WashMealService washMealService = new WashMealService();
	private WashShopService washShopService = new WashShopService();
	private int mealId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//ȥ��title 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//ȥ��Activity�����״̬��
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN); 
		// ���õ�ǰActivity���沼��
		setContentView(R.layout.washmeal_detail);
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("�鿴ϴ���ײ�����");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		// ͨ��findViewById����ʵ�������
		btnReturn = (Button) findViewById(R.id.btnReturn);
		TV_mealId = (TextView) findViewById(R.id.TV_mealId);
		TV_mealName = (TextView) findViewById(R.id.TV_mealName);
		TV_introduce = (TextView) findViewById(R.id.TV_introduce);
		TV_price = (TextView) findViewById(R.id.TV_price);
		iv_mealPhoto = (ImageView) findViewById(R.id.iv_mealPhoto); 
		TV_publishDate = (TextView) findViewById(R.id.TV_publishDate);
		TV_washShopObj = (TextView) findViewById(R.id.TV_washShopObj);
		Bundle extras = this.getIntent().getExtras();
		mealId = extras.getInt("mealId");
		btnReturn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				WashMealDetailActivity.this.finish();
			}
		}); 
		initViewData();
		
		//�鿴��������
		Button btnShop = (Button) findViewById(R.id.btnShop);
		btnShop.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) { 
				Intent intent = new Intent();
            	intent.setClass(WashMealDetailActivity.this, WashShopDetailActivity.class);
            	Bundle bundle = new Bundle();
            	bundle.putString("shopUserName", washMeal.getWashShopObj());
            	intent.putExtras(bundle);
            	startActivity(intent);
			}
		});
		
		
		//�û��µ�
		Button btnOrder = (Button) findViewById(R.id.btnOrder);
		btnOrder.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(WashMealDetailActivity.this, OrderInfoUserAddActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("washMealObj", washMeal.getMealId());
				intent.putExtras(bundle);
				startActivityForResult(intent,ActivityUtils.ADD_CODE);
			}
		});
		
		Declare declare = (Declare) WashMealDetailActivity.this.getApplication();
		if(!declare.getIdentify().equals("user")) {
			btnOrder.setVisibility(View.GONE);
		}
		
		Button btnEvaluate = (Button) findViewById(R.id.btnEvaluate);
		btnEvaluate.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(WashMealDetailActivity.this, MealEvaluateUserListActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("washMealObj", washMeal.getMealId());
				intent.putExtras(bundle);
				startActivityForResult(intent,ActivityUtils.ADD_CODE);
			}
		});
		
		
		
		
	}
	/* ��ʼ����ʾ������������ */
	private void initViewData() {
	    washMeal = washMealService.GetWashMeal(mealId); 
		this.TV_mealId.setText(washMeal.getMealId() + "");
		this.TV_mealName.setText(washMeal.getMealName());
		this.TV_introduce.setText(washMeal.getIntroduce());
		this.TV_price.setText(washMeal.getPrice() + "");
		byte[] mealPhoto_data = null;
		try {
			// ��ȡͼƬ����
			mealPhoto_data = ImageService.getImage(HttpUtil.BASE_URL + washMeal.getMealPhoto());
			Bitmap mealPhoto = BitmapFactory.decodeByteArray(mealPhoto_data, 0,mealPhoto_data.length);
			this.iv_mealPhoto.setImageBitmap(mealPhoto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Date publishDate = new Date(washMeal.getPublishDate().getTime());
		String publishDateStr = (publishDate.getYear() + 1900) + "-" + (publishDate.getMonth()+1) + "-" + publishDate.getDate();
		this.TV_publishDate.setText(publishDateStr);
		WashShop washShopObj = washShopService.GetWashShop(washMeal.getWashShopObj());
		this.TV_washShopObj.setText(washShopObj.getShopName());
	} 
}
