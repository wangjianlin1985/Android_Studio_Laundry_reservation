package com.mobileclient.activity;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import com.mobileclient.domain.WashShop;
import com.mobileclient.domain.WashClass;
import com.mobileclient.service.WashClassService;

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
public class WashShopQueryActivity extends Activity {
	// 声明查询按钮
	private Button btnQuery;
	// 声明洗衣店账号输入框
	private EditText ET_shopUserName;
	// 声明洗衣店名称输入框
	private EditText ET_shopName;
	// 声明洗衣店种类下拉框
	private Spinner spinner_washClassObj;
	private ArrayAdapter<String> washClassObj_adapter;
	private static  String[] washClassObj_ShowText  = null;
	private List<WashClass> washClassList = null; 
	/*洗衣店种类管理业务逻辑层*/
	private WashClassService washClassService = new WashClassService();
	// 声明店家电话输入框
	private EditText ET_telephone;
	// 入驻日期控件
	private DatePicker dp_addDate;
	private CheckBox cb_addDate;
	/*查询过滤条件保存到这个对象中*/
	private WashShop queryConditionWashShop = new WashShop();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
		// 设置当前Activity界面布局
		setContentView(R.layout.washshop_query);
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("设置洗衣店查询条件");
		ImageView back_btn = (ImageView) this.findViewById(R.id.back_btn);
		back_btn.setOnClickListener(new android.view.View.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		btnQuery = (Button) findViewById(R.id.btnQuery);
		ET_shopUserName = (EditText) findViewById(R.id.ET_shopUserName);
		ET_shopName = (EditText) findViewById(R.id.ET_shopName);
		spinner_washClassObj = (Spinner) findViewById(R.id.Spinner_washClassObj);
		// 获取所有的洗衣店种类
		try {
			washClassList = washClassService.QueryWashClass(null);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		int washClassCount = washClassList.size();
		washClassObj_ShowText = new String[washClassCount+1];
		washClassObj_ShowText[0] = "不限制";
		for(int i=1;i<=washClassCount;i++) { 
			washClassObj_ShowText[i] = washClassList.get(i-1).getClassName();
		} 
		// 将可选内容与ArrayAdapter连接起来
		washClassObj_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, washClassObj_ShowText);
		// 设置洗衣店种类下拉列表的风格
		washClassObj_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_washClassObj.setAdapter(washClassObj_adapter);
		// 添加事件Spinner事件监听
		spinner_washClassObj.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				if(arg2 != 0)
					queryConditionWashShop.setWashClassObj(washClassList.get(arg2-1).getClassId()); 
				else
					queryConditionWashShop.setWashClassObj(0);
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// 设置默认值
		spinner_washClassObj.setVisibility(View.VISIBLE);
		ET_telephone = (EditText) findViewById(R.id.ET_telephone);
		dp_addDate = (DatePicker) findViewById(R.id.dp_addDate);
		cb_addDate = (CheckBox) findViewById(R.id.cb_addDate);
		/*单击查询按钮*/
		btnQuery.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*获取查询参数*/
					queryConditionWashShop.setShopUserName(ET_shopUserName.getText().toString());
					queryConditionWashShop.setShopName(ET_shopName.getText().toString());
					queryConditionWashShop.setTelephone(ET_telephone.getText().toString());
					if(cb_addDate.isChecked()) {
						/*获取入驻日期*/
						Date addDate = new Date(dp_addDate.getYear()-1900,dp_addDate.getMonth(),dp_addDate.getDayOfMonth());
						queryConditionWashShop.setAddDate(new Timestamp(addDate.getTime()));
					} else {
						queryConditionWashShop.setAddDate(null);
					} 
					Intent intent = getIntent();
					//这里使用bundle绷带来传输数据
					Bundle bundle =new Bundle();
					//传输的内容仍然是键值对的形式
					bundle.putSerializable("queryConditionWashShop", queryConditionWashShop);
					intent.putExtras(bundle);
					setResult(RESULT_OK,intent);
					finish();
				} catch (Exception e) {}
			}
			});
	}
}
