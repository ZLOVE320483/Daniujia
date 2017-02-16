package com.xiaojia.daniujia.base;

import android.app.Application;
import android.app.Notification;
import android.os.Environment;
import android.os.Handler;

import com.iflytek.cloud.SpeechUtility;
import com.umeng.analytics.MobclickAgent;
import com.weaver.Global;
import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.FileStorage;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.listeners.GlideImageLoader;
import com.xiaojia.daniujia.listeners.GlidePauseOnScrollListener;
import com.xiaojia.daniujia.managers.CrashHandler;
import com.xiaojia.daniujia.provider.DaniujiaFactory;
import com.xiaojia.daniujia.utils.LogUtil;
import com.xiaojia.daniujia.utils.SysUtil;

import java.io.File;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ImageLoader;
import cn.finalteam.galleryfinal.PauseOnScrollListener;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.jpush.android.api.CustomPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

public class App extends Application {

	private static final String LOG_TAG = App.class.getSimpleName();
	private static App sApp;
	private Handler handler;
	@Override
	public void onCreate() {
		SpeechUtility.createUtility(App.this, "appid=" + getString(R.string.app_id));
		super.onCreate();
		sApp = this;
		handler = new Handler();
		CrashHandler.getInstance().init(this);
		Global.init(Config.DEBUG, sApp, new File(Environment.getExternalStorageDirectory(), Config.SDCARD_DIR));
		SysUtil.init(sApp);
		FileStorage.init();
		initJPush();
		DaniujiaFactory factory = new DaniujiaFactory();
		DaniujiaFactory.initFactory(factory);     		// 初始化 JPush
		MobclickAgent.setDebugMode(!Config.DEBUG);// 友盟统计
	}

	private void initJPush() {
		JPushInterface.init(this);

		// 指定定制的 Notification Layout
		CustomPushNotificationBuilder builder = new CustomPushNotificationBuilder(this, getNotificationLayout(), R.id.iv_notify_logo, R.id.tv_notify_title, R.id.tv_notify_content);
		// 指定最顶层状态栏小图标
		builder.statusBarDrawable = getNotificationIcon();
		builder.notificationFlags = Notification.FLAG_AUTO_CANCEL
				| Notification.FLAG_SHOW_LIGHTS;  //设置为自动消失和呼吸灯闪烁
		builder.notificationDefaults = Notification.DEFAULT_SOUND
				| Notification.DEFAULT_VIBRATE
				| Notification.DEFAULT_LIGHTS;  // 设置为铃声、震动、呼吸灯闪烁都要
		JPushInterface.setPushNotificationBuilder(2, builder);
	}

	private int getNotificationIcon() {
		boolean whiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
		return whiteIcon ? R.drawable.jpush_notification_icon : R.drawable.ic_launcher;
	}

	private int getNotificationLayout() {
		boolean whiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
		return whiteIcon ? R.layout.notification_new_version : R.layout.notification;
	}

	public static void initNormalConfig() {
		FunctionConfig functionConfig = new FunctionConfig.Builder()
				.setEnablePreview(true)
				.setEnableCrop(false)
				.setForceCrop(false)
				.setEnableCamera(true)
				.setEnableEdit(true)
				.build();
		initGalleryConfig(functionConfig);
	}

	public static void initHeaderConfig() {
		FunctionConfig functionConfig = new FunctionConfig.Builder()
						.setEnableCamera(true)
						.setCropWidth(500)
						.setCropHeight(500)
						.setCropSquare(true)
						.setEnableCrop(true)
						.setEnablePreview(false)
						.setEnableEdit(true)
						.build();
		initGalleryConfig(functionConfig);
	}

	private static void initGalleryConfig(FunctionConfig functionConfig) {
		ImageLoader imageLoader = new GlideImageLoader();
		PauseOnScrollListener pauseOnScrollListener = new GlidePauseOnScrollListener(false, true);
		ThemeConfig themeConfig = ThemeConfig.DEFAULT;
		CoreConfig coreConfig = new CoreConfig.Builder(sApp,imageLoader, themeConfig)
				.setPauseOnScrollListener(pauseOnScrollListener)
				.setNoAnimcation(false)
				.setFunctionConfig(functionConfig)
				.build();
		GalleryFinal.init(coreConfig);
	}

	public static App get() {
		if (sApp == null)
			LogUtil.e(LOG_TAG, "Application Is Null");
		return sApp;
	}

	public void post(Runnable runnable){
		handler.post(runnable);
	}

	public void postDelayed(Runnable runnable, long delayMillis){
		handler.postDelayed(runnable, delayMillis);
	}

}
