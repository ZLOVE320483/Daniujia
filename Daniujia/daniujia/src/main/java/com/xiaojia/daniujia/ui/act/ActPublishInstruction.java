package com.xiaojia.daniujia.ui.act;

import android.os.Bundle;

import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.ui.frag.PublishInstructionFragment;

/**
 * Created by Administrator on 2017/1/6.
 */
public class ActPublishInstruction extends ActDnjBase {
    private PublishInstructionFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_act_fragment_container);
        fragment = new PublishInstructionFragment();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.id_framework, fragment).commit();
        }

    }

    @Override
    protected boolean toggleOverridePendingTransition() {
        return true;
    }


    @Override
    protected TransitionMode getOverridePendingTransitionMode() {
        return TransitionMode.BOTTOM;
    }
}
