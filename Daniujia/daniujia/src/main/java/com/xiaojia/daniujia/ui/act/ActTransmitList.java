package com.xiaojia.daniujia.ui.act;

import android.os.Bundle;

import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.ui.frag.TransmitListFragment;

public class ActTransmitList extends ActDnjBase {
    private TransmitListFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_act_fragment_container);
        fragment = new TransmitListFragment();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.id_framework, fragment).commit();
        }
    }
}
