package com.mobileclient.activity;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import com.mobileclient.domain.WashMeal;
import com.mobileclient.domain.WashShop;
import com.mobileclient.service.WashShopService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import android.widget.ImageView;
import android.widget.TextView;
public class WashMealQueryActivity extends Activity {
	// 声明查询按钮
	private Button btnQuery;
	// 声明洗衣套餐输入框
	private EditText ET_mealName;
	// 发布日期控件
	private DatePicker dp_publishDate;
	private CheckBox cb_publishDate;
	// 声明洗衣店下拉框
	private Spinner spinner_washShopObj;
	private ArrayAdapter<String> washShopObj_adapter;
	private static  String[] washShopObj_ShowText  = null;
	private List<WashShop> washShopList = null; 
	/*洗衣店管理业务逻辑层*/
	private WashShopService washShopService = new WashShopService();
	/*查询过滤条件保存到这个对象中*/
	private WashMeal queryConditionWashMeal = new WashMeal();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
		// 设置当前Activity界面布局
		setContentView(R.layout.washmeal_query);
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("设置洗衣套餐查询条件");
		ImageView back_btn = (ImageView) this.findViewById(R.id.back_btn);
		back_btn.setOnClickListener(new android.view.View.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		btnQuery = (Button) findViewById(R.id.btnQuery);
		ET_mealName = (EditText) findViewById(R.id.ET_mealName);
		dp_publishDate = (DatePicker) findViewById(R.id.dp_publishDate);
		cb_publishDate = (CheckBox) findViewById(R.id.cb_publishDate);
		spinner_washShopObj = (Spinner) findViewById(R.id.Spinner_washShopObj);
		// 获取所有的洗衣店
		try {
			washShopList = washShopService.QueryWashShop(null);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		int washShopCount = washShopList.size();
		washShopObj_ShowText = new String[washShopCount+1];
		washShopObj_ShowText[0] = "不限制";
		for(int i=1;i<=washShopCount;i++) { 
			washShopObj_ShowText[i] = washShopList.get(i-1).getShopName();
		} 
		// 将可选内容与ArrayAdapter连接起来
		washShopObj_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, washShopObj_ShowText);
		// 设置洗衣店下拉列表的风格
		washShopObj_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_washShopObj.setAdapter(washShopObj_adapter);
		// 添加事件Spinner事件监听
		spinner_washShopObj.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				if(arg2 != 0)
					queryConditionWashMeal.setWashShopObj(washShopList.get(arg2-1).getShopUserName()); 
				else
					queryConditionWashMeal.setWashShopObj("");
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// 设置默认值
		spinner_washShopObj.setVisibility(View.VISIBLE);
		/*单击查询按钮*/
		btnQuery.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*获取查询参数*/
					queryConditionWashMeal.setMealName(ET_mealName.getText().toString());
					if(cb_publishDate.isChecked()) {
						/*获取发布日期*/
						Date publishDate = new Date(dp_publishDate.getYear()-1900,dp_publishDate.getMonth(),dp_publishDate.getDayOfMonth());
						queryConditionWashMeal.setPublishDate(new Timestamp(publishDate.getTime()));
					} else {
						queryConditionWashMeal.setPublishDate(null);
					} 
					Intent intent = getIntent();
					//这里使用bundle绷带来传输数据
					Bundle bundle =new Bundle();
					//传输的内容仍然是键值对的形式
					bundle.putSerializable("queryConditionWashMeal", queryConditionWashMeal);
					intent.putExtras(bundle);
					setResult(RESULT_OK,intent);
					finish();
				} catch (Exception e) {}
			}
			});
	}
}
