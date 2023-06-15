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
	// ������ѯ��ť
	private Button btnQuery;
	// ����ϴ���ײ������
	private EditText ET_mealName;
	// �������ڿؼ�
	private DatePicker dp_publishDate;
	private CheckBox cb_publishDate;
	// ����ϴ�µ�������
	private Spinner spinner_washShopObj;
	private ArrayAdapter<String> washShopObj_adapter;
	private static  String[] washShopObj_ShowText  = null;
	private List<WashShop> washShopList = null; 
	/*ϴ�µ����ҵ���߼���*/
	private WashShopService washShopService = new WashShopService();
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
		setContentView(R.layout.washmeal_query);
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
		spinner_washShopObj = (Spinner) findViewById(R.id.Spinner_washShopObj);
		// ��ȡ���е�ϴ�µ�
		try {
			washShopList = washShopService.QueryWashShop(null);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		int washShopCount = washShopList.size();
		washShopObj_ShowText = new String[washShopCount+1];
		washShopObj_ShowText[0] = "������";
		for(int i=1;i<=washShopCount;i++) { 
			washShopObj_ShowText[i] = washShopList.get(i-1).getShopName();
		} 
		// ����ѡ������ArrayAdapter��������
		washShopObj_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, washShopObj_ShowText);
		// ����ϴ�µ������б�ķ��
		washShopObj_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// ��adapter ��ӵ�spinner��
		spinner_washShopObj.setAdapter(washShopObj_adapter);
		// ����¼�Spinner�¼�����
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
		// ����Ĭ��ֵ
		spinner_washShopObj.setVisibility(View.VISIBLE);
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
