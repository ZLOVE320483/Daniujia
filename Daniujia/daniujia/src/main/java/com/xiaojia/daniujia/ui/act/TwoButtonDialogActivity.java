package com.xiaojia.daniujia.ui.act;

import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.xiaojia.daniujia.IntentKey;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.base.AbsBaseActivity;
import com.xiaojia.daniujia.db.DatabaseManager;
import com.xiaojia.daniujia.dlgs.BaseDialog.DialogClickListener;
import com.xiaojia.daniujia.dlgs.BaseMsgDlg;
import com.xiaojia.daniujia.mqtt.MqttUtils;
import com.xiaojia.daniujia.mqtt.service.MqttServiceConstants;
import com.xiaojia.daniujia.utils.SysUtil;

import org.json.JSONObject;

public class TwoButtonDialogActivity extends AbsBaseActivity {

	private BaseMsgDlg dlg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SysUtil.clearUserInfo();
		showDilalog();
	}
	
	private void showDilalog(){
		dlg = new BaseMsgDlg(activity);
		dlgs.add(dlg);
		dlg.setBtnName(DialogInterface.BUTTON_POSITIVE, R.string.confirm);
		dlg.setTitle(R.string.system);
		dlg.setMsg(R.string.multiport);
		dlg.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_SETTINGS || keyCode == KeyEvent.KEYCODE_SEARCH
						|| keyCode == KeyEvent.KEYCODE_BACK) {
					return true;
				}
				return false;
			}
		});
		dlg.setOnClickListener(new DialogClickListener() {
			public void onOK() {
				MqttUtils.publish(MqttServiceConstants.USER_LOGOUT_REQUEST, new JSONObject(), 0);
				DatabaseManager.releaseInstance();
				SysUtil.clearUserInfo();
				sendBroadcast(new Intent(ACTION_EXIT));
				Intent intent = new Intent(TwoButtonDialogActivity.this, ActLogin.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
				intent.putExtra(IntentKey.INTENT_KEY_IS_FORM_CONFLICT, true);
				startActivity(intent);
				finish();
			}
		});
		dlg.setCanceledOnTouchOutside(false);
		dlg.show();
	}
}
