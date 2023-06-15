package com.mobileclient.activity;

import java.util.Date;
import com.mobileclient.domain.WashClass;
import com.mobileclient.service.WashClassService;
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
public class WashClassDetailActivity extends Activity {
	// �������ذ�ť
	private Button btnReturn;
	// ��������id�ؼ�
	private TextView TV_classId;
	// �����������ƿؼ�
	private TextView TV_className;
	// �������������ؼ�
	private TextView TV_classDesc;
	/* Ҫ�����ϴ�µ�������Ϣ */
	WashClass washClass = new WashClass(); 
	/* ϴ�µ��������ҵ���߼��� */
	private WashClassService washClassService = new WashClassService();
	private int classId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//ȥ��title 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//ȥ��Activity�����״̬��
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN); 
		// ���õ�ǰActivity���沼��
		setContentView(R.layout.washclass_detail);
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("�鿴ϴ�µ���������");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		// ͨ��findViewById����ʵ�������
		btnReturn = (Button) findViewById(R.id.btnReturn);
		TV_classId = (TextView) findViewById(R.id.TV_classId);
		TV_className = (TextView) findViewById(R.id.TV_className);
		TV_classDesc = (TextView) findViewById(R.id.TV_classDesc);
		Bundle extras = this.getIntent().getExtras();
		classId = extras.getInt("classId");
		btnReturn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				WashClassDetailActivity.this.finish();
			}
		}); 
		initViewData();
	}
	/* ��ʼ����ʾ������������ */
	private void initViewData() {
	    washClass = washClassService.GetWashClass(classId); 
		this.TV_classId.setText(washClass.getClassId() + "");
		this.TV_className.setText(washClass.getClassName());
		this.TV_classDesc.setText(washClass.getClassDesc());
	} 
}
