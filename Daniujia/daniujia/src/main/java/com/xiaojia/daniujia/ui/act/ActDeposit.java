package com.xiaojia.daniujia.ui.act;

import android.os.Bundle;

import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.ui.frag.DepositFragment;

/**
 * Created by ZLOVE on 2016/4/11.
 */
public class ActDeposit extends ActDnjBase {

    private DepositFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_act_fragment_container);
        fragment = new DepositFragment();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.id_framework, fragment).commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (fragment != null && !fragment.isDetached()){
            fragment.onBackClick();
        } else {
            super.onBackPressed();
        }

    }
}
