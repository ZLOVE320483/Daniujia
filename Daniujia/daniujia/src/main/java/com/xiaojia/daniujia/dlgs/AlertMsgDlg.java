package com.xiaojia.daniujia.dlgs;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.utils.EditUtils;
import com.xiaojia.daniujia.utils.UIUtil;

/**
 * Created by Administrator on 2016/5/7.
 */
public class AlertMsgDlg extends BaseDialog implements View.OnClickListener {

    private ImageView ivIcon;
    private Button mConfirm;
    private Button mEdit;
    private EditText mContent;
    private TextView mTitle;
    private ImageView mClose;
    private Activity activity;
    private ModifyListener listener;
    private int modifyType = 0;

    public AlertMsgDlg(Activity context) {
        super(context, R.layout.dlg_alert_msg);
        activity = context;
        ivIcon = (ImageView) mView.findViewById(R.id.title_iv);
        mTitle = (TextView) mView.findViewById(R.id.title);
        mContent = (EditText) mView.findViewById(R.id.content);
        mConfirm = (Button) mView.findViewById(R.id.confirm);
        mEdit = (Button) mView.findViewById(R.id.edit);
        mClose = (ImageView) mView.findViewById(R.id.close);
        mContent.setFocusable(false);
        mContent.setEnabled(false);
        mContent.setFocusableInTouchMode(false);
        mConfirm.setOnClickListener(this);
        mClose.setOnClickListener(this);
        mEdit.setOnClickListener(this);
        EditUtils utils = new EditUtils(0);
        utils.init(mContent, null);
    }

    public void initDlg(int iconRes, String title, String content) {
        ivIcon.setImageResource(iconRes);
        mTitle.setText(title);
        mContent.setText(content);
        setCanceledOnTouchOutside(false);

        mConfirm.setText("知道了");
        mContent.setFocusable(false);
        mContent.setEnabled(false);
        mContent.setFocusableInTouchMode(false);
    }

    public void initDlg(int iconRes, int title, int content) {
        ivIcon.setImageResource(iconRes);
        mTitle.setText(title);
        mContent.setText(content);
    }

    public void initDlg(int titleRes, int contentRes) {
        mTitle.setText(mActivity.getResources().getText(titleRes));
        mContent.setText(mActivity.getResources().getText(contentRes));
    }

    public void setEditable(ModifyListener listener, int modifyType) {
        mEdit.setVisibility(View.VISIBLE);
        this.listener = listener;
        this.modifyType = modifyType;
        mConfirm.setText("完成");
        mEdit.setText("取消");
        mContent.setFocusable(true);
        mContent.setEnabled(true);
        mContent.setFocusableInTouchMode(true);
        mContent.requestFocus();
        mContent.setSelection(mContent.getText().toString().length());
        UIUtil.forceShowOrHideKeyBoard(mContent, "open");
    }

    @Override
    public void onClick(View v) {
        if (v == mClose) {
            if (isShowing()) {
                dismiss();
            }
        } else if (v == mConfirm) {
            String content = mContent.getText().toString().trim();
            if (TextUtils.isEmpty(content)) {
                if (modifyType == 1) {
                    showShortToast("个人简介不能为空");
                } else if (modifyType == 2) {
                    showShortToast("问题描述不能为空");
                }
                return;
            }
            if (listener != null) {
                listener.modify(content);
            }
            if (isShowing()) {
                dismiss();
            }
        } else if (v == mEdit) {
            if (isShowing()) {
                dismiss();
            }
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        UIUtil.forceShowOrHideKeyBoard(mContent, "close");
    }

    public interface ModifyListener {
        void modify(String content);
    }
}
