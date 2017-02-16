package com.xiaojia.daniujia.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Message;
import android.os.Process;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.Toast;

import com.squareup.okhttp.Request;
import com.umeng.analytics.MobclickAgent;
import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.InsideMsg;
import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.dlgs.BaseDialog.DialogClickListener;
import com.xiaojia.daniujia.dlgs.BaseMsgDlg;
import com.xiaojia.daniujia.domain.resp.UpdateVersionRespVo;
import com.xiaojia.daniujia.managers.OkHttpClientManager;
import com.xiaojia.daniujia.mqtt.MqttUtils;
import com.xiaojia.daniujia.mqtt.service.MqttServiceConstants;
import com.xiaojia.daniujia.msg.MsgHelper;
import com.xiaojia.daniujia.msg.OnMsgCallback;
import com.xiaojia.daniujia.msg.annotation.OnMsg;
import com.xiaojia.daniujia.ui.views.toast.ToastCompat;
import com.xiaojia.daniujia.utils.ApplicationUtil;
import com.xiaojia.daniujia.utils.DialogManager;
import com.xiaojia.daniujia.utils.LogUtil;
import com.xiaojia.daniujia.utils.NetUtils;
import com.xiaojia.daniujia.utils.SysUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public abstract class AbsBaseActivity extends FragmentActivity implements DialogManager.UpdateVersionListener {
	public static final String ACTION_EXIT = "com.weaver.dnj.action.EXIT";
	public static final String NET_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
	private BroadcastReceiver receiver;
	public Activity activity;
	private static BaseMsgDlg mDilaog;
	public List<Dialog> dlgs = new ArrayList<>();
	private boolean needReconnect = false;
	private int force;
	private String updateUrl;
	private static final int REQ_UPDATE_VERSION = 100;
	private String lastLinkWay = "";

	public List<Dialog> getList() {
		if (dlgs == null) {
			dlgs = new ArrayList<>();
		}
		return dlgs;
	}

	public static final void exitAll() {
		App.get().sendBroadcast(new Intent(ACTION_EXIT));
		SysUtil.clearUserInfo();
		MqttUtils.publish(MqttServiceConstants.USER_LOGOUT_REQUEST, new JSONObject(), 0);
		MqttUtils.disconnect();
		MqttUtils.handleConnectionLost();
	}

	public static void killProcess() {
		ActivityManager activityMgr = (ActivityManager) App.get().getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> mRunningProcess = activityMgr.getRunningAppProcesses();

		PackageInfo pkgInfo = SysUtil.getPkgInfo();
		String pkgName = pkgInfo.packageName;
		List<Integer> pids = new ArrayList<Integer>();
		for (ActivityManager.RunningAppProcessInfo amProcess : mRunningProcess) {
			if (amProcess.processName.startsWith(pkgName) && !amProcess.processName.equals(pkgName)) {
				pids.add(amProcess.pid);
			}
		}
		pids.add(Process.myPid());
		for (Integer pid : pids) {
			Process.killProcess(pid);
		}
	}

	@Override
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		lastLinkWay = NetUtils.getNetworkLinkWay(this);
		activity = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		IntentFilter filter = new IntentFilter();
		filter.addAction(ACTION_EXIT);
		filter.addAction(NET_CHANGE_ACTION);
		receiver = new InnerReceiver(this);
		this.registerReceiver(receiver, filter);
		MsgHelper.getInstance().registMsg(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!Config.DEBUG) {
			MobclickAgent.onResume(this);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (!Config.DEBUG) {
			MobclickAgent.onPause(this);
		}
	}

	@Override
	protected void onDestroy() {
		this.unregisterReceiver(receiver);
		MsgHelper.getInstance().unRegistMsg(this);
		receiver = null;
		if (dlgs.size() != 0) {
			for (Dialog dlg : dlgs) {
				if (dlg != null && dlg.isShowing()) {
					dlg.dismiss();
				}
			}
		}
		dlgs.clear();
		dlgs = null;
		super.onDestroy();
	}

	private class InnerReceiver extends BroadcastReceiver {
		public Activity mActivity;

		public InnerReceiver(Activity act) {
			mActivity = act;
		}

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(NET_CHANGE_ACTION)) {
				if (!NetUtils.isNetAvailable(context)) {
					needReconnect = true;
					setNetworkMethod(context);
				} else {
					LogUtil.d("ZLOVE", "isConnect---" + MqttUtils.isConnected());
					if ((lastLinkWay.equals("wifi") && NetUtils.getNetworkLinkWay(context).equals("gprs")) || (lastLinkWay.equals("gprs") && NetUtils.getNetworkLinkWay(context).equals("wifi"))) {
						needReconnect = true;
						lastLinkWay = NetUtils.getNetworkLinkWay(context);
						MqttUtils.publish(MqttServiceConstants.USER_LOGOUT_REQUEST, new JSONObject(), 0);
						MqttUtils.handleConnectionLost();
						MqttUtils.disconnect();
					}
					if (SysUtil.getBooleanPref(PrefKeyConst.PREF_IS_LOGIN) && !MqttUtils.isConnected() && needReconnect) {
						LogUtil.d("ZLOVE", "do Connect 22222222");
						needReconnect = false;
						MqttUtils.connect();
					}
				}
			} else {
				if (mActivity != null) {
					mActivity.finish();
					mActivity = null;
				}
			}
		}
	}

	public void setNetworkMethod(final Context context) {
		if (context == null) {
			return;
		}
		if (mDilaog != null && mDilaog.isShowing()) {
			mDilaog.dismiss();
		}
		mDilaog = new BaseMsgDlg(activity);
		dlgs.add(mDilaog);
		mDilaog.setBtnName(DialogInterface.BUTTON_POSITIVE, R.string.open_net);
		mDilaog.setBtnName(DialogInterface.BUTTON_NEGATIVE, R.string.cancel);
		mDilaog.setTitle("系统消息");
		mDilaog.setMsg("连接服务器失败，请检查网络！");
		mDilaog.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_SETTINGS || keyCode == KeyEvent.KEYCODE_SEARCH
						|| keyCode == KeyEvent.KEYCODE_BACK) {
					return true;
				}
				return false;
			}
		});
		mDilaog.setOnClickListener(new DialogClickListener() {
			public void onOK() {
				Intent intent;
				if (android.os.Build.VERSION.SDK_INT > 10) {
					intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
				} else {
					intent = new Intent();
					ComponentName component = new ComponentName("com.android.settings",
							"com.android.settings.WirelessSettings");
					intent.setComponent(component);
					intent.setAction("android.intent.action.VIEW");
				}
				context.startActivity(intent);
			}
		});
		mDilaog.setCanceledOnTouchOutside(false);
		if (!mDilaog.isShowing()) {
			mDilaog.show();
		}
	}

	protected void showShortToast(String msg) {
		ToastCompat.makeText(ApplicationUtil.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
	}

	protected void showShortToast(int message) {
		String msg = ApplicationUtil.getApplicationContext().getResources().getString(message);
		ToastCompat.makeText(ApplicationUtil.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
	}

	@OnMsg(msg = {InsideMsg.NOTIFY_VERSION_CHANGE}, useLastMsg = false)
	OnMsgCallback onMsgCallback = new OnMsgCallback() {
		@Override
		public boolean handleMsg(Message msg) {
			switch (msg.what) {
				case InsideMsg.NOTIFY_VERSION_CHANGE:
					doRequestVersionInfo();
					break;
			}
			return false;
		}
	};

	private void doRequestVersionInfo() {
		final String curVersion = SysUtil.getVerName();
		String url = Config.WEB_API_SERVER + "/android/latestVersion/" + curVersion;

		OkHttpClientManager.getInstance(this).getWithPlatform(false, url, new OkHttpClientManager.ResultCallback<UpdateVersionRespVo>() {
			@Override
			public void onResponse(UpdateVersionRespVo result) {
				if (result == null) {
					return;
				}
				if (result.getReturncode().equals("SUCCESS")) {
					if (result.getUpdateFlag() == 0) {
						return;
					}
					if (!result.getLatestVersion().equals(curVersion)) {
						boolean needShowDialog = SysUtil.getBooleanPref(result.getLatestVersion(), true);
						if (needShowDialog) {
							updateUrl = result.getDownloadUrl();
							force = result.getForce();
							DialogManager.showUpdateVersionDialog(AbsBaseActivity.this, result.getForce(), result.getLatestVersion(), result.getContent(), AbsBaseActivity.this);
						}
					}
				} else {
					showShortToast(result.getReturnmsg());
				}
			}

			@Override
			public void onError(Request paramRequest, Exception e) {
				super.onError(paramRequest, e);
			}

			@Override
			public void onFail(String errorMsg) {
				super.onFail(errorMsg);
			}
		});
	}

	@Override
	public void updateAction() {
		Intent browserIntent = new Intent();
		browserIntent.setAction("android.intent.action.VIEW");
		Uri content_url = Uri.parse(updateUrl);
		browserIntent.setData(content_url);
		// 用户点击下载链接跳转到浏览器页面之后返回了
		startActivityForResult(browserIntent, REQ_UPDATE_VERSION);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQ_UPDATE_VERSION) {
			if (force == 1) {
				sendBroadcast(new Intent(ACTION_EXIT));
			}
		}
	}
}
