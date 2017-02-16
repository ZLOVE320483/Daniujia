package com.xiaojia.daniujia.dlgs;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.xiaojia.daniujia.R;

/**
 * Created by Administrator on 2016/8/16.
 */
public class CommonTipDlg extends BaseDialog{
    private TextView mTvCancel;
    private TextView mTvConfirm;
    private TextView mContent;
    private TextView mTitle;
    private View viewLine;

    public CommonTipDlg(Activity context) {
        super(context, R.layout.dlg_common_tip);
        mTvConfirm = (TextView) mView.findViewById(R.id.dlg_common_confirm);
        mTvCancel = (TextView) mView.findViewById(R.id.dlg_common_cancel);
        initView();
    }

    public CommonTipDlg(Activity context, String title,String content, String buttonOk, String buttonCancel) {
        super(context, R.layout.dlg_common_tip);
        mTitle = (TextView) mView.findViewById(R.id.dlg_common_title);
        mTitle.setText(title);
        mTvConfirm = (TextView) mView.findViewById(R.id.dlg_common_confirm);
        mTvCancel = (TextView) mView.findViewById(R.id.dlg_common_cancel);
        mContent = (TextView) mView.findViewById(R.id.dlg_common_content);
        viewLine = mView.findViewById(R.id.button_view);
        mTvConfirm.setText(buttonOk);

        if (buttonCancel.isEmpty()){
            mTvCancel.setVisibility(View.GONE);
            viewLine.setVisibility(View.GONE);
        } else {
            mTvCancel.setText(buttonCancel);
        }

        mContent.setText(content);
        initView();
    }

    public void setContentPosition(int gravity){
        mContent.setGravity(gravity);
    }

    public void setTitleVisible(int visible){
        mTitle.setVisibility(visible);
    }

    private void initView(){
        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
                    mClickListener.onCancel();
                }
                dismiss();
            }
        });

        mTvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
                    mClickListener.onOK();
                }
                dismiss();
            }
        });
    }

    public void setContent(String content){
        mContent.setText(content);
    }

    public void setContentColor(String color){
        mContent.setTextColor(Color.parseColor(color));
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

}
