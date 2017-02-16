package com.xiaojia.daniujia.ui.act;

import android.os.Bundle;
import android.view.WindowManager;

import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.base.AbsBaseActivity;
import com.xiaojia.daniujia.ui.frag.GraphicOrderDetailFragment;

/**
 * Created by Administrator on 2016/4/27
 */

public class ActGraphicOrderDetail extends AbsBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_act_fragment_container);
        GraphicOrderDetailFragment fragment = new GraphicOrderDetailFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.id_framework, fragment).commit();
    }
}
