package com.xiaojia.daniujia.ui.act;

import android.os.Bundle;

import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.base.AbsBaseActivity;
import com.xiaojia.daniujia.ui.frag.HomeNewFragment;

/**
 * Created by Administrator on 2016/10/21.
 */
public class ActTest extends AbsBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_act_fragment_container);
        HomeNewFragment fragment = new HomeNewFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.id_framework, fragment).commit();
    }
}
