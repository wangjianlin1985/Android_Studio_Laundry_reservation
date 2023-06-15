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
	// ������ѯ��ť
	private Button btnQuery;
	// ����ϴ�µ��˺������
	private EditText ET_shopUserName;
	// ����ϴ�µ����������
	private EditText ET_shopName;
	// ����ϴ�µ�����������
	private Spinner spinner_washClassObj;
	private ArrayAdapter<String> washClassObj_adapter;
	private static  String[] washClassObj_ShowText  = null;
	private List<WashClass> washClassList = null; 
	/*ϴ�µ��������ҵ���߼���*/
	private WashClassService washClassService = new WashClassService();
	// ������ҵ绰�����
	private EditText ET_telephone;
	// ��פ���ڿؼ�
	private DatePicker dp_addDate;
	private CheckBox cb_addDate;
	/*��ѯ�����������浽���������*/
	private WashShop queryConditionWashShop = new WashShop();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//ȥ��title 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//ȥ��Activity�����״̬��
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
		// ���õ�ǰActivity���沼��
		setContentView(R.layout.washshop_query);
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("����ϴ�µ��ѯ����");
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
		// ��ȡ���е�ϴ�µ�����
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
		// ����ϴ�µ����������б�ķ��
		washClassObj_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// ��adapter ��ӵ�spinner��
		spinner_washClassObj.setAdapter(washClassObj_adapter);
		// ����¼�Spinner�¼�����
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
		// ����Ĭ��ֵ
		spinner_washClassObj.setVisibility(View.VISIBLE);
		ET_telephone = (EditText) findViewById(R.id.ET_telephone);
		dp_addDate = (DatePicker) findViewById(R.id.dp_addDate);
		cb_addDate = (CheckBox) findViewById(R.id.cb_addDate);
		/*������ѯ��ť*/
		btnQuery.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*��ȡ��ѯ����*/
					queryConditionWashShop.setShopUserName(ET_shopUserName.getText().toString());
					queryConditionWashShop.setShopName(ET_shopName.getText().toString());
					queryConditionWashShop.setTelephone(ET_telephone.getText().toString());
					if(cb_addDate.isChecked()) {
						/*��ȡ��פ����*/
						Date addDate = new Date(dp_addDate.getYear()-1900,dp_addDate.getMonth(),dp_addDate.getDayOfMonth());
						queryConditionWashShop.setAddDate(new Timestamp(addDate.getTime()));
					} else {
						queryConditionWashShop.setAddDate(null);
					} 
					Intent intent = getIntent();
					//����ʹ��bundle��������������
					Bundle bundle =new Bundle();
					//�����������Ȼ�Ǽ�ֵ�Ե���ʽ
					bundle.putSerializable("queryConditionWashShop", queryConditionWashShop);
					intent.putExtras(bundle);
					setResult(RESULT_OK,intent);
					finish();
				} catch (Exception e) {}
			}
			});
	}
}
