package com.xiaojia.daniujia.ui.act;

import android.os.Bundle;

import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.ui.frag.FilePreViewFragment;

/**
 * Created by Administrator on 2016/9/19.
 */
public class ActFilePreView extends ActDnjBase {
    private FilePreViewFragment preViewFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_act_fragment_container);
        preViewFragment = new FilePreViewFragment();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.id_framework, preViewFragment).commit();
        }
    }


}
