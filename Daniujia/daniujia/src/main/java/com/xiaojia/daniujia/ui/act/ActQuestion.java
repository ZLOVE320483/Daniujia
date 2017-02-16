package com.xiaojia.daniujia.ui.act;

import android.os.Bundle;

import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.base.AbsBaseActivity;
import com.xiaojia.daniujia.ui.frag.QuestionFragment;

/**
 * Created by Administrator on 2016/4/27.
 */
public class ActQuestion extends AbsBaseActivity {

    private QuestionFragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_act_fragment_container);
        fragment = new QuestionFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.id_framework, fragment).commit();
    }
}
