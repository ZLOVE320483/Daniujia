package com.xiaojia.daniujia.ui.act;

import android.os.Bundle;

import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.base.AbsBaseActivity;
import com.xiaojia.daniujia.ui.frag.CouponFragment;

/**
 * Created by Administrator on 2016/5/3.
 */
public class ActCoupon extends AbsBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_act_fragment_container);
        getSupportFragmentManager().beginTransaction().replace(R.id.id_framework, new CouponFragment()).commit();
    }
}
