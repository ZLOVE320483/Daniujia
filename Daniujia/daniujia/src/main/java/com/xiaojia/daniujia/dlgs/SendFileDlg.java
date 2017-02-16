package com.xiaojia.daniujia.dlgs;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.ui.views.imageview.RoundedImageView;
import com.xiaojia.daniujia.utils.DisplayUtil;
import com.xiaojia.daniujia.utils.ScreenUtils;

/**
 * Created by Administrator on 2016/9/20.
 */
public class SendFileDlg extends BaseDialog implements View.OnClickListener {
    private RoundedImageView ivIcon;
    private TextView mTvPersonNane;
    private TextView mTvFileName;
    private TextView mTvLeaveMessage;
    private TextView mTvCancel;
    private TextView mTvConfirm;

    public SendFileDlg(Activity context) {
        super(context, R.layout.dlg_send_file);
        ivIcon = (RoundedImageView) mView.findViewById(R.id.dlg_send_file_head);
        mTvFileName = (TextView) mView.findViewById(R.id.dlg_file_name);
        mTvLeaveMessage = (TextView) mView.findViewById(R.id.dlg_send_file_leave_word);
        mTvCancel = (TextView) mView.findViewById(R.id.dlg_send_file_cancel);
        mTvConfirm = (TextView) mView.findViewById(R.id.dlg_send_file_confirm);

    }

    public SendFileDlg(Activity context,String iconUrl,String fileName,String personName) {
        super(context, R.layout.dlg_send_file);
        ivIcon = (RoundedImageView) mView.findViewById(R.id.dlg_send_file_head);
        mTvFileName = (TextView) mView.findViewById(R.id.dlg_file_name);
        mTvLeaveMessage = (TextView) mView.findViewById(R.id.dlg_send_file_leave_word);
        mTvCancel = (TextView) mView.findViewById(R.id.dlg_send_file_cancel);
        mTvConfirm = (TextView) mView.findViewById(R.id.dlg_send_file_confirm);
        mTvPersonNane = (TextView) mView.findViewById(R.id.dlg_send_person_name);

        mTvConfirm.setOnClickListener(this);
        mTvCancel.setOnClickListener(this);
        mTvFileName.setText(fileName);
        mTvPersonNane.setText(personName);
        DisplayUtil.display(iconUrl).resize(ScreenUtils.dip2px(36.0f), ScreenUtils.dip2px(36.0f)).into(ivIcon);
    }

    public void showDialog() {
        // 设置窗口弹出动画
        getWindow().setWindowAnimations(R.style.centerDialogWindowAnim);
        // setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams wl = getWindow().getAttributes();
        wl.gravity = Gravity.CENTER;
        getWindow().setAttributes(wl);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setCanceledOnTouchOutside(false);
        show();
    }


    @Override
    public void onClick(View v) {
        if (v == mTvCancel){
            if (mClickListener != null){
                mClickListener.onOK("cancel");
            }
        } else if(v == mTvConfirm){
            if (mClickListener != null){
                if (mTvLeaveMessage.getText() != null){
                    mClickListener.onOK(mTvLeaveMessage.getText().toString());
                }

            }
        }
    }
}
