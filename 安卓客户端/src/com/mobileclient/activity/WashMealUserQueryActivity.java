package com.mobileclient.activity;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.mobileclient.domain.WashClass;
import com.mobileclient.domain.WashMeal;
import com.mobileclient.domain.WashShop;
import com.mobileclient.service.WashClassService;
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
public class WashMealUserQueryActivity extends Activity {
	// ������ѯ��ť
	private Button btnQuery;
	// ����ϴ���ײ������
	private EditText ET_mealName;
	// �������ڿؼ�
	private DatePicker dp_publishDate;
	private CheckBox cb_publishDate;
	// ����ϴ�µ�����������
	private Spinner spinner_washClassObj;
	private ArrayAdapter<String> washClassObj_adapter;
	private static  String[] washClassObj_ShowText  = null;
	private List<WashClass> washClassList = null; 
	/*ϴ�µ��������ҵ���߼���*/
	private WashClassService washClassService = new WashClassService();
	
	// ����ϴ�������������
	private Spinner spinner_orderRule;
	private ArrayAdapter<String> orderRule_adapter;
	private static  String[] orderRule_ShowText  = null;
	
	/*��ѯ�����������浽���������*/
	private WashMeal queryConditionWashMeal = new WashMeal();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//ȥ��title 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//ȥ��Activity�����״̬��
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
		// ���õ�ǰActivity���沼��
		setContentView(R.layout.washmeal_user_query);
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("����ϴ���ײͲ�ѯ����");
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
		spinner_washClassObj = (Spinner) findViewById(R.id.Spinner_washClassObj);
		// ��ȡ���е�ϴ�µ�
		try {
			washClassList = washClassService.QueryWashClass(null);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		int washClassCount = washClassList.size();
		washClassObj_ShowText = new String[washClassCount+1];
		washClassObj_ShowText[0] = "������";
		for(int i=1;i<=washClassCount;i++) { 
			washClassObj_ShowText[i] = washClassList.get(i-1).getClassName();
		} 
		// ����ѡ������ArrayAdapter��������
		washClassObj_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, washClassObj_ShowText);
		// ����ϴ�µ������б�ķ��
		washClassObj_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// ��adapter ��ӵ�spinner��
		spinner_washClassObj.setAdapter(washClassObj_adapter);
		// ����¼�Spinner�¼�����
		spinner_washClassObj.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				if(arg2 != 0)
					queryConditionWashMeal.setWashClassObj(washClassList.get(arg2-1).getClassId()); 
				else
					queryConditionWashMeal.setWashClassObj(0);
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// ����Ĭ��ֵ
		spinner_washClassObj.setVisibility(View.VISIBLE);
		
		//���û�ȡ����������������
		spinner_orderRule = (Spinner) findViewById(R.id.Spinner_orderRule); 
		 
		orderRule_ShowText = new String[] {"Ĭ������","����ӽ���Զ","�۸�ӵ͵���"}; 
		// ����ѡ������ArrayAdapter��������
		orderRule_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, orderRule_ShowText);
		// ����ϴ�µ������б�ķ��
		orderRule_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// ��adapter ��ӵ�spinner��
		spinner_orderRule.setAdapter(orderRule_adapter);
		// ����¼�Spinner�¼�����
		spinner_orderRule.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				 queryConditionWashMeal.setOrderRule(arg2);
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// ����Ĭ��ֵ
		spinner_washClassObj.setVisibility(View.VISIBLE);
		 
		
		/*������ѯ��ť*/
		btnQuery.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*��ȡ��ѯ����*/
					queryConditionWashMeal.setMealName(ET_mealName.getText().toString());
					if(cb_publishDate.isChecked()) {
						/*��ȡ��������*/
						Date publishDate = new Date(dp_publishDate.getYear()-1900,dp_publishDate.getMonth(),dp_publishDate.getDayOfMonth());
						queryConditionWashMeal.setPublishDate(new Timestamp(publishDate.getTime()));
					} else {
						queryConditionWashMeal.setPublishDate(null);
					} 
					Intent intent = getIntent();
					//����ʹ��bundle��������������
					Bundle bundle =new Bundle();
					//�����������Ȼ�Ǽ�ֵ�Ե���ʽ
					bundle.putSerializable("queryConditionWashMeal", queryConditionWashMeal);
					intent.putExtras(bundle);
					setResult(RESULT_OK,intent);
					finish();
				} catch (Exception e) {}
			}
		});
	}
}
