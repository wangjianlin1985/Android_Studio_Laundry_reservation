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
	// 声明返回按钮
	private Button btnReturn;
	// 声明种类id控件
	private TextView TV_classId;
	// 声明种类名称控件
	private TextView TV_className;
	// 声明种类描述控件
	private TextView TV_classDesc;
	/* 要保存的洗衣店种类信息 */
	WashClass washClass = new WashClass(); 
	/* 洗衣店种类管理业务逻辑层 */
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
		setContentView(R.layout.washclass_detail);
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("查看洗衣店种类详情");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		// 通过findViewById方法实例化组件
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
	/* 初始化显示详情界面的数据 */
	private void initViewData() {
	    washClass = washClassService.GetWashClass(classId); 
		this.TV_classId.setText(washClass.getClassId() + "");
		this.TV_className.setText(washClass.getClassName());
		this.TV_classDesc.setText(washClass.getClassDesc());
	} 
}
