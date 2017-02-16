package com.xiaojia.daniujia.dlgs;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaojia.daniujia.R;

/**
 * Created by Administrator on 2016/5/7.
 */
public class AlertBigMsgDlg extends BaseDialog implements View.OnClickListener {

    private ImageView ivIcon;
    private Button mConfirm;
    private EditText mContent;
    private TextView mTitle;
    private ImageView mClose;

    public AlertBigMsgDlg(Activity context) {
        super(context, R.layout.big_dlg_alert_msg);
        ivIcon = (ImageView) mView.findViewById(R.id.title_iv);
        mTitle = (TextView) mView.findViewById(R.id.title);
        mContent = (EditText) mView.findViewById(R.id.content);
        mConfirm = (Button) mView.findViewById(R.id.confirm);
        mClose = (ImageView) mView.findViewById(R.id.close);
        mConfirm.setOnClickListener(this);
        mClose.setOnClickListener(this);
    }

    public void initDlg(int iconRes, String title, String content){
        ivIcon.setImageResource(iconRes);
        mTitle.setText(title);
        mContent.setText(content);
        setCanceledOnTouchOutside(false);
    }

    public void initDlg(int iconRes, int title, int content){
        ivIcon.setImageResource(iconRes);
        mTitle.setText(title);
        mContent.setText(content);
    }

    public void initDlg(int titleRes,int contentRes){
        mTitle.setText(mActivity.getResources().getText(titleRes));
        mContent.setText(mActivity.getResources().getText(contentRes));
    }

    @Override
    public void onClick(View v) {
        if (isShowing()) {
            dismiss();
        }
    }
}
