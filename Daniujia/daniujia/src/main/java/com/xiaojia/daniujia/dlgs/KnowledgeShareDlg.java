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
public class KnowledgeShareDlg extends BaseDialog{
    private TextView mTvJoin;
    private TextView mTvNoJoin;
    private OnJoinClickListener listener;

    public KnowledgeShareDlg(Activity context) {
        super(context, R.layout.dlg_konwledge_share);
        mTvJoin = (TextView) mView.findViewById(R.id.dlg_knowledge_join);
        mTvNoJoin = (TextView) mView.findViewById(R.id.dlg_knowledge_no_join);
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
                if (listener != null){
                    listener.onClickJoin();
                }
                dismiss();
            }
        });
    }

    public void setJoinClickListener(OnJoinClickListener listener){
        this.listener = listener;
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

    public interface OnJoinClickListener{
        void onClickJoin();
    }

}
