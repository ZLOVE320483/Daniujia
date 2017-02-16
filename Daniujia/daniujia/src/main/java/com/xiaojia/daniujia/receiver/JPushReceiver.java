package com.xiaojia.daniujia.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.xiaojia.daniujia.managers.DnjNotificationManager;
import com.xiaojia.daniujia.ui.act.ActMain;
import com.xiaojia.daniujia.utils.LogUtil;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by ZLOVE on 2016/4/7.
 */
public class JPushReceiver extends BroadcastReceiver {

    protected static final String LOG_TAG = "JPUSH";

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
        LogUtil.d(LOG_TAG, "onReceive ---- " + intent.getAction());
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            if (bundle != null)
                LogUtil.d(LOG_TAG, "收到了ACTION_MESSAGE_RECEIVED。消息内容是：" + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            // 自定义消息不会展示在通知栏，完全要开发者写代码去处理
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            LogUtil.d(LOG_TAG, "收到了通知");
            if (bundle != null)
                LogUtil.d(LOG_TAG, "收到了ACTION_NOTIFICATION_RECEIVED。消息内容是：" + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            // 在这里可以做些统计，或者做些其他工作
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            LogUtil.d(LOG_TAG, "用户点击打开了通知");
            DnjNotificationManager.getInstance(context).setMsgNum(DnjNotificationManager.getInstance(context).getMsgNum()+1);
            Intent mainIntent = new Intent(context, ActMain.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mainIntent);
        } else {
            LogUtil.d(LOG_TAG, "Unhandled intent - " + intent.getAction());
        }
    }
}
