package com.xiaojia.daniujia.dlgs;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.utils.ScreenUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/1/4.
 */
public class CommonBottomDlg extends BaseBottomDialog implements View.OnClickListener {
    private List<String> data;
    private LinearLayout mLlText;

    public CommonBottomDlg(Activity context,List<String> data) {
        super(context, LayoutInflater.from(context).inflate(R.layout.dlg_bottom_common,null));
        this.data = data;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLlText = (LinearLayout) mView.findViewById(R.id.bottom_common_ll);
        mView.findViewById(R.id.cancel).setOnClickListener(this);

        initLlText();
    }

    private void initLlText(){
        if (mLlText.getChildCount() > 0){
            mLlText.removeAllViews();
        }

        if (data != null){
            for (int i = 0; i < data.size(); i++) {
                final int pos = i;
                Button button = new Button(mActivity);
                button.setText(data.get(i));
                button.setBackgroundColor(Color.WHITE);
                button.setTextColor(Color.parseColor("#333333"));
                button.setTextSize(16);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mClickListener != null){
                            mClickListener.onOK(String.valueOf(pos));
                        }
                    }
                });

                View line = new View(mActivity);
                line.setBackgroundColor(getContext().getResources().getColor(R.color.separate));

                mLlText.addView(button, ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtils.dip2px(48));
                if (i != data.size() -1)
                    mLlText.addView(line,ViewGroup.LayoutParams.MATCH_PARENT,1);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cancel){
            dismiss();
        }

    }
}
