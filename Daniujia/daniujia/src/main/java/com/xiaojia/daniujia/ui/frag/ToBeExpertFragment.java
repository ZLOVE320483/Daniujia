package com.xiaojia.daniujia.ui.frag;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.xiaojia.daniujia.IntentKey;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.ui.act.ActFirstStepToBeExpert;
import com.xiaojia.daniujia.utils.ScreenUtils;

/**
 * Created by Administrator on 2016/5/17.
 */
public class ToBeExpertFragment extends BaseFragment implements View.OnClickListener {
    private LinearLayout mLlWhy;

    private String fromWhere = "";

    @Override
    protected int getInflateLayout() {
        return R.layout.fg_to_be_expert;
    }

    @Override
    protected void setUpView(View view) {
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(IntentKey.FROM_WHERE)) {
            fromWhere = intent.getStringExtra(IntentKey.FROM_WHERE);
        }
        setBackButton(view.findViewById(R.id.back));
        Button confirm = (Button) view.findViewById(R.id.confirm);
        mLlWhy = (LinearLayout) view.findViewById(R.id.tv_why);
        // 根据屏幕dpi动态设置margin值，注意不同dpi文字所占用的大小也是不同的。
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) mLlWhy.getLayoutParams();
        marginLayoutParams.setMargins(ScreenUtils.dip2px(16),ScreenUtils.dip2px(30) + ScreenUtils.sp2px(8),ScreenUtils.dip2px(16),0);

        confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm:
                Intent intent = new Intent(getActivity(), ActFirstStepToBeExpert.class);
                intent.putExtra(IntentKey.FROM_WHERE, fromWhere);
                startActivity(intent);
                break;
        }
    }

}
