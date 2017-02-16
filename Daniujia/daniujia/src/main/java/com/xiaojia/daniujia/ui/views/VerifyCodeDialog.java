package com.xiaojia.daniujia.ui.views;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.utils.BitmapUtil;
import com.xiaojia.daniujia.utils.UIUtil;

/**
 * Created by ZLOVE on 2016/6/16.
 */
public class VerifyCodeDialog extends Dialog implements View.OnClickListener {

    private ImageView ivCode;
    private TextView tvChangeCode;
    private EditText etCode;
    private View cancelView;
    private View confirmView;

    private VerifyCodeListener listener;
    private String imgData;

    public VerifyCodeDialog(Context context, VerifyCodeListener listener, String imgData) {
        super(context, R.style.MessageBox);
        this.listener = listener;
        this.imgData = imgData;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dlg_view_ver_code);
        initView();
    }

    private void initView() {
        ivCode = (ImageView) findViewById(R.id.code);
        tvChangeCode = (TextView) findViewById(R.id.change);
        etCode = (EditText) findViewById(R.id.et_code);
        cancelView = findViewById(R.id.id_cancel);
        confirmView = findViewById(R.id.id_confirm);

        tvChangeCode.setOnClickListener(this);
        cancelView.setOnClickListener(this);
        confirmView.setOnClickListener(this);

        Bitmap bitmap = BitmapUtil.base64ToBitmap(imgData);
        ivCode.setImageBitmap(bitmap);
    }

    public void setImageData(String imgData) {
        if (ivCode == null) {
            return;
        }
        Bitmap bitmap = BitmapUtil.base64ToBitmap(imgData);
        ivCode.setImageBitmap(bitmap);
    }

    public void showdialog() {
        // 设置窗口弹出动画
        getWindow().setWindowAnimations(R.style.centerDialogWindowAnim);
        WindowManager.LayoutParams wl = getWindow().getAttributes();
        wl.gravity = Gravity.CENTER;
        getWindow().setAttributes(wl);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        show();
    }

    @Override
    public void onClick(View v) {
        if (v == tvChangeCode) {
            if (listener != null) {
                listener.changeCodeAction();
            }
        } else if (v == cancelView) {
            dismiss();
        } else if (v == confirmView) {
            String code = etCode.getText().toString().trim();
            if (TextUtils.isEmpty(code)) {
                UIUtil.showShortToast("请输入图片中的字符");
                return;
            }
            if (listener != null) {
                listener.confirmAction(code);
            }
            dismiss();
        }
    }

    public interface VerifyCodeListener {
        void confirmAction(String code);
        void changeCodeAction();
    }
}