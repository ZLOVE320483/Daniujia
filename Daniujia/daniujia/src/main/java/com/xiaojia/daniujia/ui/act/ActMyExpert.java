package com.xiaojia.daniujia.ui.act;

import android.content.Intent;
import android.os.Bundle;

import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.ui.frag.MyExpertMainFragment;
import com.xiaojia.daniujia.utils.LogUtil;

/**
 * Created by ZLOVE on 2016/4/11.
 */
public class ActMyExpert extends ActDnjBase {

    private MyExpertMainFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_act_fragment_container);
        fragment = new MyExpertMainFragment();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.id_framework, fragment).commit();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        LogUtil.d("ZLOVE", "ActMyExpert---onActivityResult---requestCode---" + requestCode);

        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}
