package com.xiaojia.daniujia.ui.act;

import android.os.Bundle;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.base.AbsBaseActivity;
import com.xiaojia.daniujia.ui.frag.IndustryFragment;

/**
 * Created by Administrator on 2016/5/27.
 */
public class ActIndustry extends AbsBaseActivity {

    IndustryFragment fragment;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_act_fragment_container);
        fragment = new IndustryFragment();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.id_framework, fragment).commit();
        }
    }
}
