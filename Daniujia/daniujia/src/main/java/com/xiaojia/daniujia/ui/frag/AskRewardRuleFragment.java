package com.xiaojia.daniujia.ui.frag;

import android.view.View;

import com.xiaojia.daniujia.R;

/**
 * Created by ZLOVE on 2016/10/25.
 */
public class AskRewardRuleFragment extends BaseFragment implements View.OnClickListener {

    @Override
    protected int getInflateLayout() {
        return R.layout.frag_ask_reward_rule;
    }

    @Override
    protected void setUpView(View view) {
        view.findViewById(R.id.id_back).setOnClickListener(this);
        view.findViewById(R.id.id_confirm).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_back:
            case R.id.id_confirm:
                finishActivity();
                getActivity().overridePendingTransition(R.anim.dialog_enter, R.anim.dialog_exit);
                break;

            default:
                break;
        }
    }
}
