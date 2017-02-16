package com.xiaojia.daniujia.dlgs;

import android.app.Activity;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.xiaojia.daniujia.R;

import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by Administrator on 2016/9/9.
 */
public class ShareDialog extends BaseDialog{
    private TextView mTvWeChatCircle;
    private TextView mTvWeibo;
    private TextView mTvWeChat;
    private TextView mTvQQ;
    private ShareClickListener listener;
    private Activity activity;
    private TextView mTvCancel;

    public ShareDialog(Activity context) {
        super(context, R.layout.dlg_show_view);
        activity = context;
        mTvQQ = (TextView) mView.findViewById(R.id.share_qq);
        mTvWeChat = (TextView) mView.findViewById(R.id.share_wechat);
        mTvWeChatCircle = (TextView) mView.findViewById(R.id.share_wechat_moments);
        mTvWeibo = (TextView) mView.findViewById(R.id.share_sina_weibo);
        mTvCancel = (TextView) mView.findViewById(R.id.share_cancel);

        initClick();
    }

    private void initClick(){
        mTvQQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    listener.onShareClick(QQ.NAME);
                }
            }
        });

        mTvWeibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    listener.onShareClick(SinaWeibo.NAME);
                }
            }
        });

        mTvWeChatCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    listener.onShareClick(WechatMoments.NAME);
                }
            }
        });

        mTvWeChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onShareClick(Wechat.NAME);
                }
            }
        });

        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void showDialog() {
        // 设置窗口弹出动画
        getWindow().setWindowAnimations(R.style.MenuDialogAnimation);
        WindowManager windowManager = activity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();

        WindowManager.LayoutParams wl = getWindow().getAttributes();
        wl.gravity = Gravity.BOTTOM;
        wl.width = display.getWidth();
        getWindow().setAttributes(wl);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setCanceledOnTouchOutside(true);
        show();
    }

    public void setOnShareClickListener(ShareClickListener listener){
        this.listener = listener;
    }

    public interface ShareClickListener{
        void onShareClick(String platform);
    }
}
