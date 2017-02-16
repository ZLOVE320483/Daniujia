package com.xiaojia.daniujia.managers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.widget.RemoteViews;

import com.xiaojia.daniujia.ExtraConst;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.ui.act.ActMain;
import com.xiaojia.daniujia.ui.act.SplashActivity;
import com.xiaojia.daniujia.utils.ApplicationUtil;

import java.lang.reflect.Field;

public class DnjNotificationManager {
    private final int NOTIFY_FLAG = 1;
    private static DnjNotificationManager instance;
    private Context context;
    private NotificationManager mNotificationManager;
    private Notification notification;
    private int msgNum;
    private String launcherActivityClassName = SplashActivity.class.getName();

    private DnjNotificationManager(Context context) {
        this.context = context;
        msgNum = 0;
        mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context);
        builder.setSmallIcon(getNotificationIcon());
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), getNotificationLayout());
        //5.0对图片的像素大小有明确要求，hdpi 36像素，xhdpi 48像素
        remoteViews.setImageViewResource(R.id.iv_notify_logo, R.drawable.ic_launcher_new_version);
        builder.setContent(remoteViews);
        notification = builder.getNotification();
        notification.flags |= Notification.FLAG_AUTO_CANCEL| Notification.FLAG_SHOW_LIGHTS;
        notification.defaults = Notification.DEFAULT_ALL;
        Intent intent = new Intent(context, ActMain.class);
        intent.putExtra(ExtraConst.EXTRA_FROM_NOTIFICATION, true);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        notification.contentIntent = pendingIntent;
        mNotificationManager.notify(NOTIFY_FLAG, notification);
    }

    private int getNotificationIcon() {
        boolean whiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return whiteIcon ? R.drawable.ic_launcher_lollipop : R.drawable.ic_launcher;
    }

    private int getNotificationLayout() {
        boolean whiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return whiteIcon ? R.layout.notification_new_version : R.layout.notification;
    }

    public static synchronized DnjNotificationManager getInstance(Context context) {
        if (instance == null) {
            instance = new DnjNotificationManager(context);
        }
        return instance;
    }

    public void notify(String content) {
        ++msgNum;
        MediaPlayer player = MediaPlayer.create(ApplicationUtil.getApplicationContext(), R.raw.notify);
        player.setVolume(1.0f, 1.0f);
        player.start();
        if (notification != null) {
            if (msgNum == 1) {
                notification.contentView.setTextViewText(R.id.tv_notify_content,
                        content);
            } else {
                notification.contentView.setTextViewText(R.id.tv_notify_content,
                        "您收到" + msgNum + "条消息");
            }
            if (Build.MANUFACTURER.equalsIgnoreCase("Xiaomi")) {
                setNumToXiaomi(notification, msgNum);
            } else if (Build.MANUFACTURER.equalsIgnoreCase("Sony")) {
                sendToSony(context, String.valueOf(msgNum));
            } else if (Build.MANUFACTURER.equalsIgnoreCase("Samsung")) {
                sendToSamsumg(context, String.valueOf(msgNum));
            }

            mNotificationManager.notify(NOTIFY_FLAG, notification);
        }
    }

    private void setNumToXiaomi(Notification notification, int msgNum) {
        try {
            Class miuiNotificationClass = Class.forName("android.app.MiuiNotification");
            Object miuiNotification = miuiNotificationClass.newInstance();
            Field field = miuiNotification.getClass().getDeclaredField("messageCount");
            field.setAccessible(true);
            field.set(miuiNotification, msgNum);// 设置信息数
            field = notification.getClass().getField("extraNotification");
            field.setAccessible(true);
            field.set(notification, miuiNotification);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private void sendToSony(Context context, String number) {
        boolean isShow = true;
        if ("0".equals(number)) {
            isShow = false;
        }
        Intent localIntent = new Intent();
        localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.SHOW_MESSAGE", isShow);//是否显示
        localIntent.setAction("com.sonyericsson.home.action.UPDATE_BADGE");
        localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.ACTIVITY_NAME", launcherActivityClassName);//启动页
        localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.MESSAGE", number);//数字
        localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.PACKAGE_NAME", context.getPackageName());//包名
        context.sendBroadcast(localIntent);
    }

    private void sendToSamsumg(Context context, String number) {
        Intent localIntent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
        localIntent.putExtra("badge_count", Integer.valueOf(number));//数字
        localIntent.putExtra("badge_count_package_name", context.getPackageName());//包名
        localIntent.putExtra("badge_count_class_name", launcherActivityClassName); //启动页
        context.sendBroadcast(localIntent);
    }

    public void cancel() {
        if (mNotificationManager == null) {
            mNotificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
        }
        mNotificationManager.cancel(NOTIFY_FLAG);
    }

    public void cancelAll() {
        if (mNotificationManager == null) {
            mNotificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
        }
        mNotificationManager.cancelAll();
    }

    public void setMsgNum(int msgNum) {
        this.msgNum = msgNum;
    }

    public int getMsgNum() {
        return this.msgNum;
    }
}
