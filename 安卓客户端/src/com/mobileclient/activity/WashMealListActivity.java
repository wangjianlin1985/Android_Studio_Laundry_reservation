package com.mobileclient.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mobileclient.app.Declare;
import com.mobileclient.domain.WashMeal;
import com.mobileclient.service.WashMealService;
import com.mobileclient.util.ActivityUtils;import com.mobileclient.util.WashMealSimpleAdapter;
import com.mobileclient.util.HttpUtil;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class WashMealListActivity extends Activity {
	WashMealSimpleAdapter adapter;
	ListView lv; 
	List<Map<String, Object>> list;
	int mealId;
	/* ϴ���ײͲ���ҵ���߼������ */
	WashMealService washMealService = new WashMealService();
	/*�����ѯ����������ϴ���ײͶ���*/
	private WashMeal queryConditionWashMeal;

	private MyProgressDialog dialog; //������	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//ȥ��title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//ȥ��Activity�����״̬��
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
		setContentView(R.layout.washmeal_list);
		dialog = MyProgressDialog.getInstance(this);
		Declare declare = (Declare) getApplicationContext();
		String username = declare.getUserName();
		//�������ؼ�
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(WashMealListActivity.this, WashMealQueryActivity.class);
				startActivityForResult(intent,ActivityUtils.QUERY_CODE);//�˴���requestCodeӦ�������������е��õ�requestCodeһ��
			}
		});
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("ϴ���ײͲ�ѯ�б�");
		ImageView add_btn = (ImageView) this.findViewById(R.id.add_btn);
		add_btn.setOnClickListener(new android.view.View.OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(WashMealListActivity.this, WashMealAddActivity.class);
				startActivityForResult(intent,ActivityUtils.ADD_CODE);
			}
		});
		setViews();
	}

	//���������������secondActivity�з���ʱ���ô˺���
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==ActivityUtils.QUERY_CODE && resultCode==RESULT_OK){
        	Bundle extras = data.getExtras();
        	if(extras != null)
        		queryConditionWashMeal = (WashMeal)extras.getSerializable("queryConditionWashMeal");
        	setViews();
        }
        if(requestCode==ActivityUtils.EDIT_CODE && resultCode==RESULT_OK){
        	setViews();
        }
        if(requestCode == ActivityUtils.ADD_CODE && resultCode == RESULT_OK) {
        	queryConditionWashMeal = null;
        	setViews();
        }
    }

	private void setViews() {
		lv = (ListView) findViewById(R.id.h_list_view);
		dialog.show();
		final Handler handler = new Handler();
		new Thread(){
			@Override
			public void run() {
				//�����߳��н����������ݲ���
				list = getDatas();
				//������ʧ��handler��֪ͨ���߳��������
				handler.post(new Runnable() {
					@Override
					public void run() {
						dialog.cancel();
						adapter = new WashMealSimpleAdapter(WashMealListActivity.this, list,
	        					R.layout.washmeal_list_item,
	        					new String[] { "mealName","price","mealPhoto","publishDate","washShopObj" },
	        					new int[] { R.id.tv_mealName,R.id.tv_price,R.id.iv_mealPhoto,R.id.tv_publishDate,R.id.tv_washShopObj,},lv);
	        			lv.setAdapter(adapter);
					}
				});
			}
		}.start(); 

		// ��ӳ������
		lv.setOnCreateContextMenuListener(washMealListItemListener);
		lv.setOnItemClickListener(new OnItemClickListener(){
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
            	int mealId = Integer.parseInt(list.get(arg2).get("mealId").toString());
            	Intent intent = new Intent();
            	intent.setClass(WashMealListActivity.this, WashMealDetailActivity.class);
            	Bundle bundle = new Bundle();
            	bundle.putInt("mealId", mealId);
            	intent.putExtras(bundle);
            	startActivity(intent);
            }
        });
	}
	private OnCreateContextMenuListener washMealListItemListener = new OnCreateContextMenuListener() {
		public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
			menu.add(0, 0, 0, "�༭ϴ���ײ���Ϣ"); 
			menu.add(0, 1, 0, "ɾ��ϴ���ײ���Ϣ");
		}
	};

	// �����˵���Ӧ����
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == 0) {  //�༭ϴ���ײ���Ϣ
			ContextMenuInfo info = item.getMenuInfo();
			AdapterContextMenuInfo contextMenuInfo = (AdapterContextMenuInfo) info;
			// ��ȡѡ����λ��
			int position = contextMenuInfo.position;
			// ��ȡ�ײ�id
			mealId = Integer.parseInt(list.get(position).get("mealId").toString());
			Intent intent = new Intent();
			intent.setClass(WashMealListActivity.this, WashMealEditActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt("mealId", mealId);
			intent.putExtras(bundle);
			startActivityForResult(intent,ActivityUtils.EDIT_CODE);
		} else if (item.getItemId() == 1) {// ɾ��ϴ���ײ���Ϣ
			ContextMenuInfo info = item.getMenuInfo();
			AdapterContextMenuInfo contextMenuInfo = (AdapterContextMenuInfo) info;
			// ��ȡѡ����λ��
			int position = contextMenuInfo.position;
			// ��ȡ�ײ�id
			mealId = Integer.parseInt(list.get(position).get("mealId").toString());
			dialog();
		}
		return super.onContextItemSelected(item);
	}

	// ɾ��
	protected void dialog() {
		Builder builder = new Builder(WashMealListActivity.this);
		builder.setMessage("ȷ��ɾ����");
		builder.setTitle("��ʾ");
		builder.setPositiveButton("ȷ��", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				String result = washMealService.DeleteWashMeal(mealId);
				Toast.makeText(getApplicationContext(), result, 1).show();
				setViews();
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("ȡ��", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	private List<Map<String, Object>> getDatas() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			/* ��ѯϴ���ײ���Ϣ */
			List<WashMeal> washMealList = washMealService.QueryWashMeal(queryConditionWashMeal);
			for (int i = 0; i < washMealList.size(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("mealId",washMealList.get(i).getMealId());
				map.put("mealName", washMealList.get(i).getMealName());
				map.put("price", washMealList.get(i).getPrice());
				/*byte[] mealPhoto_data = ImageService.getImage(HttpUtil.BASE_URL+ washMealList.get(i).getMealPhoto());// ��ȡͼƬ����
				BitmapFactory.Options mealPhoto_opts = new BitmapFactory.Options();  
				mealPhoto_opts.inJustDecodeBounds = true;  
				BitmapFactory.decodeByteArray(mealPhoto_data, 0, mealPhoto_data.length, mealPhoto_opts); 
				mealPhoto_opts.inSampleSize = photoListActivity.computeSampleSize(mealPhoto_opts, -1, 100*100); 
				mealPhoto_opts.inJustDecodeBounds = false; 
				try {
					Bitmap mealPhoto = BitmapFactory.decodeByteArray(mealPhoto_data, 0, mealPhoto_data.length, mealPhoto_opts);
					map.put("mealPhoto", mealPhoto);
				} catch (OutOfMemoryError err) { }*/
				map.put("mealPhoto", HttpUtil.BASE_URL+ washMealList.get(i).getMealPhoto());
				map.put("publishDate", washMealList.get(i).getPublishDate());
				map.put("washShopObj", washMealList.get(i).getWashShopObj());
				list.add(map);
			}
		} catch (Exception e) { 
			Toast.makeText(getApplicationContext(), "", 1).show();
		}
		return list;
	}

}
