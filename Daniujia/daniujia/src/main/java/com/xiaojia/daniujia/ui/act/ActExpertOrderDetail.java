package com.xiaojia.daniujia.ui.act;

import android.os.Bundle;

import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.base.AbsBaseActivity;
import com.xiaojia.daniujia.ui.frag.ExpertOrderDetailFragment;

/**
 * Created by Administrator on 2016/5/26.
 */
public class ActExpertOrderDetail extends AbsBaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_act_fragment_container);
        getSupportFragmentManager().beginTransaction().replace(R.id.id_framework, new ExpertOrderDetailFragment()).commit();
    }
}
