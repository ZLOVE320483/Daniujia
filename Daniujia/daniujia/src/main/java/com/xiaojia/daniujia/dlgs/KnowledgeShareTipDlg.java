package com.xiaojia.daniujia.dlgs;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.xiaojia.daniujia.R;

/**
 * Created by Administrator on 2016/8/16.
 */
public class KnowledgeShareTipDlg extends BaseDialog{
    private TextView mTvNoJoin;
    private TextView mTvJoin;
    private OnJoinClickListener listener;
    private TextView mContent;

    public KnowledgeShareTipDlg(Activity context) {
        super(context, R.layout.dlg_konwledge_share_tip);
        mTvJoin = (TextView) mView.findViewById(R.id.dlg_knowledge_tip_join);
        mTvNoJoin = (TextView) mView.findViewById(R.id.dlg_knowledge_tip_no_join);
        initView();
    }

    public KnowledgeShareTipDlg(Activity context,String content,String buttonOk,String buttonCancel) {
        super(context, R.layout.dlg_konwledge_share_tip);
        mTvJoin = (TextView) mView.findViewById(R.id.dlg_knowledge_tip_join);
        mTvNoJoin = (TextView) mView.findViewById(R.id.dlg_knowledge_tip_no_join);
        mContent = (TextView) mView.findViewById(R.id.dlg_knowledge_tip_content);
        mTvJoin.setText(buttonOk);
        mTvNoJoin.setText(buttonCancel);
        mContent.setText(content);
        initView();
    }

    private void initView(){
        mTvNoJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mTvJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClickJoin();
                }
                dismiss();
            }
        });
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

    public void setJoinClickListener(OnJoinClickListener listener){
        this.listener = listener;
    }

    public interface OnJoinClickListener{
        void onClickJoin();
    }
}
