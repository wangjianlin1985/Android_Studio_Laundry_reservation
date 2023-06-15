package com.mobileclient.app;
 
import java.io.File;

import android.app.Application;
import android.content.Context;

import com.baidu.mapapi.SDKInitializer;
import com.mobileclient.util.HttpUtil;

public class Declare extends Application {

	
	@Override
	public void onCreate() {
		super.onCreate(); 
		CrashHandler crashHandler = CrashHandler.getInstance();    
	    crashHandler.init(getApplicationContext());  
	    context = this.getApplicationContext(); 
	    SDKInitializer.initialize(context);  
	    
	    File path = new File(HttpUtil.FILE_PATH);
	    if(!path.exists()) path.mkdirs();
	}
	 
	public static Context context;
	 
	
	private int id;
    private String userName;
    private String identify; //用户身份
    
    public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getIdentify() {
		return identify;
	}
	public void setIdentify(String identify) {
		this.identify = identify;
	} 
	
	
}
