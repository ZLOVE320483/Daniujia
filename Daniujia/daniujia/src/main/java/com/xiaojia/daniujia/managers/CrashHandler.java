package com.xiaojia.daniujia.managers;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Looper;
import android.widget.Toast;

import com.xiaojia.daniujia.base.AbsBaseActivity;
import com.xiaojia.daniujia.utils.LogFileUtils;
import com.xiaojia.daniujia.utils.SysUtil;

import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class CrashHandler implements UncaughtExceptionHandler {

	private static CrashHandler instance;
	private UncaughtExceptionHandler mExceptionHandler;
	Context mContext;
	Map<String, String> infos;
	
	public synchronized static CrashHandler getInstance(){
		if(instance == null){
			instance  = new CrashHandler();
		}
		return instance;
	}
	
	private CrashHandler(){
		infos = new HashMap<String, String>();
	}
	
	public void init(Context context){
		mContext = context;
		mExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}
	
	boolean handleException(Throwable ex){
		if(ex == null){
			return false;
		}
		ex.printStackTrace();
		saveCrash2Fiel(ex);
		SysUtil.clearUserInfo();
		new Thread(){
			public void run() {
				Looper.prepare();
				Toast.makeText(mContext, "sorry the program will close in a minute", Toast.LENGTH_SHORT).show();
				Looper.loop();
			}
		}.start();
		AbsBaseActivity.exitAll();
		return true;
	}
	
	void collectDeviceInfo(Context context){
		try{
			PackageManager manager = context.getPackageManager();
			PackageInfo pi = manager.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
			if(pi != null){
				String versionName = pi.versionName == null ? "null" : pi.versionName;
				String versionCode = pi.versionCode + "";
				infos.put("versionName", versionName);
				infos.put("versionCode", versionCode);
			}
			Field[] fields = Build.class.getDeclaredFields();
			for(Field field: fields){
				field.setAccessible(true);
				infos.put(field.getName(), field.get(null).toString());	//get(null) ? throws NullPoiterException
			}
		}catch(Exception e){
			
		}
	}
	
	void saveCrash2Fiel(Throwable ex){
		StringBuffer sb = new StringBuffer("**********CrashHandler**********");
		collectDeviceInfo(mContext);
		for(Map.Entry<String, String> entry : infos.entrySet()){
			String key = entry.getKey();
			String value = entry.getValue();
			sb.append(key + "=" + value + "\n");
		}
		LogFileUtils.getInstance().log2File(sb.toString(), new Exception(ex));
	}
	
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (handleException(ex) && mExceptionHandler != null) {
			mExceptionHandler.uncaughtException(thread, ex);
		}
		AbsBaseActivity.exitAll();
	}

}
