package com.xiaojia.daniujia.dlgs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.xiaojia.daniujia.R;

/**
 * Created by ZLOVE on 2017/1/3.
 */
public class ConfirmConsultantDialog extends Dialog implements View.OnClickListener {

    private EditText etContent;
    private View confirmView;
    private View cancelView;
    private int expertId;

    private ConfirmConsultantListener listener;

    public ConfirmConsultantDialog(Context context) {
        super(context, R.style.MessageBox);
    }

    public ConfirmConsultantDialog(Context context, ConfirmConsultantListener listener) {
        this(context);
        this.listener = listener;
    }

    public void setExpertId(int expertId) {
        this.expertId = expertId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_view_confirm_consultant);
        initView();
    }

    private void initView() {
        etContent = (EditText) findViewById(R.id.id_content);
        confirmView = findViewById(R.id.id_confirm);
        cancelView = findViewById(R.id.id_cancel);

        confirmView.setOnClickListener(this);
        cancelView.setOnClickListener(this);
    }

    public void showDialog() {
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
    public void onClick(View v) {
        if (v == cancelView) {

        } else if (v == confirmView) {
            String content = etContent.getText().toString().trim();
            if (listener != null) {
                listener.confirm(expertId, content);
            }
        }
        dismiss();
    }

    public interface ConfirmConsultantListener {
        void confirm(int expertId, String content);
    }
}
