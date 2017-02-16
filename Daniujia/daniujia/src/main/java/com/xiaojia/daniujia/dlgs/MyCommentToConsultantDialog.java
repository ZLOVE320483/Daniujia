package com.xiaojia.daniujia.dlgs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.xiaojia.daniujia.R;

/**
 * Created by ZLOVE on 2017/1/3.
 */
public class MyCommentToConsultantDialog extends Dialog implements View.OnClickListener {

    private TextView tvTitle;
    private EditText etContent;
    private View confirmView;
    private View cancelView;

    private String expertName;

    private CommentToConsultantListener listener;

    public MyCommentToConsultantDialog(Context context) {
        super(context, R.style.MessageBox);
    }

    public MyCommentToConsultantDialog(Context context, String expertName, CommentToConsultantListener listener) {
        this(context);
        this.expertName = expertName;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_view_my_comment_to_consultant);
        initView();
    }

    private void initView() {
        tvTitle = (TextView) findViewById(R.id.id_title);
        etContent = (EditText) findViewById(R.id.id_content);
        confirmView = findViewById(R.id.id_confirm);
        cancelView = findViewById(R.id.id_cancel);

        tvTitle.setText("给" + expertName + "专家评价");
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
                listener.comment(content);
            }
        }
        dismiss();
    }

    public interface CommentToConsultantListener {
        void comment(String content);
    }
}
