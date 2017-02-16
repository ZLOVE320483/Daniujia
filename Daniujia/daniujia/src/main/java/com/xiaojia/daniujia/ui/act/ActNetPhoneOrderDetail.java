package com.xiaojia.daniujia.ui.act;

import android.os.Bundle;

import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.base.AbsBaseActivity;
import com.xiaojia.daniujia.ui.frag.GraphicOrderDetailFragment;
import com.xiaojia.daniujia.ui.frag.NetPhoneOrderDetailFragment;

/**
 * Created by Administrator on 2016/4/27
 */

public class ActNetPhoneOrderDetail extends AbsBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_act_fragment_container);
        NetPhoneOrderDetailFragment fragment = new NetPhoneOrderDetailFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.id_framework, fragment).commit();
    }
}
