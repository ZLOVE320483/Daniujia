package com.xiaojia.daniujia.ui.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.xiaojia.daniujia.R;

/**
 * Created by ZLOVE on 2016/5/4.
 */
public class CommonDialog extends Dialog implements View.OnClickListener {

    private ConfirmAction listener;

    private TextView tvTitle;
    private TextView tvContent;
    private TextView btnConfirm;
    private View dividerView;
    private TextView btnCancel;

    private String title;
    private String content;
    private String confirmText = "";
    private String cancelText = "";
    private boolean isHide = false;

    public CommonDialog(Context context) {
        super(context, R.style.MessageBox);
    }

    public CommonDialog(Context context, ConfirmAction listener, String title, String content) {
        this(context);
        this.listener = listener;
        this.title = title;
        this.content = content;
    }

    public CommonDialog(Context context, ConfirmAction listener, String title, String content, String confirmText, boolean isHideCancel) {
        this(context);
        this.listener = listener;
        this.title = title;
        this.content = content;
        this.confirmText = confirmText;
        this.isHide = isHideCancel;
    }

    public CommonDialog(Context context, ConfirmAction listener, String title, String content, String confirmText, String cancelText) {
        this(context);
        this.listener = listener;
        this.title = title;
        this.content = content;
        this.confirmText = confirmText;
        this.cancelText = cancelText;
    }

    public CommonDialog(Context context, ConfirmAction listener, String title, String content, boolean isHideCancel) {
        this(context);
        this.listener = listener;
        this.title = title;
        this.content = content;
        this.isHide = isHideCancel;
    }

    public void showdialog() {
        // 设置窗口弹出动画
        getWindow().setWindowAnimations(R.style.centerDialogWindowAnim);
        // setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams wl = getWindow().getAttributes();
        wl.gravity = Gravity.CENTER;
        getWindow().setAttributes(wl);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_view_dialog);
        initView();
    }

    private void initView() {
        tvTitle = (TextView) findViewById(R.id.tv_dialog_title);
        tvContent = (TextView) findViewById(R.id.tv_dialog_content);
        btnConfirm = (TextView) findViewById(R.id.id_confirm);
        btnCancel = (TextView) findViewById(R.id.id_cancel);
        dividerView = findViewById(R.id.id_divider_1);

        tvTitle.setText(title);
        tvContent.setText(content);

        if (!TextUtils.isEmpty(confirmText)) {
            btnConfirm.setText(confirmText);
        }
        if (!TextUtils.isEmpty(cancelText)) {
            btnCancel.setText(cancelText);
        }

        btnConfirm.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        btnCancel.setVisibility(isHide ? View.GONE : View.VISIBLE);
        dividerView.setVisibility(isHide ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        if (v == btnConfirm) {
            if (listener != null) {
                listener.confirm();
            }
        } else if (v == btnCancel) {
            if (listener != null) {
                listener.cancel();
            }
        }
        dismiss();
    }

    public interface ConfirmAction {
        void confirm();
        void cancel();
    }


}
