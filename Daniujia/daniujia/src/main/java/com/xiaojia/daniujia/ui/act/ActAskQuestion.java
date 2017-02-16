package com.xiaojia.daniujia.ui.act;

import android.os.Bundle;

import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.ui.frag.AskQuestionFragment;

/**
 * Created by Administrator on 2016/10/26.
 */
public class ActAskQuestion extends ActDnjBase {
    private AskQuestionFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_act_fragment_container);
        fragment = new AskQuestionFragment();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.id_framework, fragment).commit();
        }
    }
}
