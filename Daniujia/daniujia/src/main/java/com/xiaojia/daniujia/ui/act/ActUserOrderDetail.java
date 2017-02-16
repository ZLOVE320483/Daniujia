package com.xiaojia.daniujia.ui.act;

import android.os.Bundle;

import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.base.AbsBaseActivity;
import com.xiaojia.daniujia.ui.frag.UserOrderDetailFragment;

/**
 * Created by Administrator on 2016/5/26.
 */
public class ActUserOrderDetail extends AbsBaseActivity {

    private UserOrderDetailFragment fragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_act_fragment_container);
        fragment = new UserOrderDetailFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.id_framework, fragment).commit();
    }
}
