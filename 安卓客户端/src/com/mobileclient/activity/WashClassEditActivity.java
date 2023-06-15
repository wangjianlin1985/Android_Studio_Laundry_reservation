package com.mobileclient.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.mobileclient.util.HttpUtil;
import com.mobileclient.util.ImageService;
import com.mobileclient.domain.WashClass;
import com.mobileclient.service.WashClassService;
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
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Spinner;
import android.widget.Toast;

public class WashClassEditActivity extends Activity {
	// 声明确定添加按钮
	private Button btnUpdate;
	// 声明种类idTextView
	private TextView TV_classId;
	// 声明种类名称输入框
	private EditText ET_className;
	// 声明种类描述输入框
	private EditText ET_classDesc;
	protected String carmera_path;
	/*要保存的洗衣店种类信息*/
	WashClass washClass = new WashClass();
	/*洗衣店种类管理业务逻辑层*/
	private WashClassService washClassService = new WashClassService();

	private int classId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
		// 设置当前Activity界面布局
		setContentView(R.layout.washclass_edit); 
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("编辑洗衣店种类信息");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		TV_classId = (TextView) findViewById(R.id.TV_classId);
		ET_className = (EditText) findViewById(R.id.ET_className);
		ET_classDesc = (EditText) findViewById(R.id.ET_classDesc);
		btnUpdate = (Button) findViewById(R.id.BtnUpdate);
		Bundle extras = this.getIntent().getExtras();
		classId = extras.getInt("classId");
		/*单击修改洗衣店种类按钮*/
		btnUpdate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*验证获取种类名称*/ 
					if(ET_className.getText().toString().equals("")) {
						Toast.makeText(WashClassEditActivity.this, "种类名称输入不能为空!", Toast.LENGTH_LONG).show();
						ET_className.setFocusable(true);
						ET_className.requestFocus();
						return;	
					}
					washClass.setClassName(ET_className.getText().toString());
					/*验证获取种类描述*/ 
					if(ET_classDesc.getText().toString().equals("")) {
						Toast.makeText(WashClassEditActivity.this, "种类描述输入不能为空!", Toast.LENGTH_LONG).show();
						ET_classDesc.setFocusable(true);
						ET_classDesc.requestFocus();
						return;	
					}
					washClass.setClassDesc(ET_classDesc.getText().toString());
					/*调用业务逻辑层上传洗衣店种类信息*/
					WashClassEditActivity.this.setTitle("正在更新洗衣店种类信息，稍等...");
					String result = washClassService.UpdateWashClass(washClass);
					Toast.makeText(getApplicationContext(), result, 1).show(); 
					Intent intent = getIntent();
					setResult(RESULT_OK,intent);
					finish();
				} catch (Exception e) {}
			}
		});
		initViewData();
	}

	/* 初始化显示编辑界面的数据 */
	private void initViewData() {
	    washClass = washClassService.GetWashClass(classId);
		this.TV_classId.setText(classId+"");
		this.ET_className.setText(washClass.getClassName());
		this.ET_classDesc.setText(washClass.getClassDesc());
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
}
