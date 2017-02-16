package com.xiaojia.daniujia.ui.act;

import android.os.Bundle;

import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.base.AbsBaseActivity;
import com.xiaojia.daniujia.ui.frag.GraphicOrderDetailFragment;
import com.xiaojia.daniujia.ui.frag.OfflineOrderDetailFragment;

/**
 * Created by Administrator on 2016/4/27
 */

public class ActOfflineOrderDetail extends AbsBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_act_fragment_container);
        OfflineOrderDetailFragment fragment = new OfflineOrderDetailFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.id_framework, fragment).commit();
    }
}
