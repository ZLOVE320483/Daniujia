package com.xiaojia.daniujia.ui.act;

import android.os.Bundle;

import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.base.AbsBaseActivity;
import com.xiaojia.daniujia.ui.frag.StepThirdToBeExpertFragment;
import com.xiaojia.daniujia.ui.frag.StepThirdToBeExpertNew;

/**
 * Created by Administrator on 2016/5/17.
 */
public class ActThirdStepToBeExpert extends AbsBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_act_fragment_container);
        getSupportFragmentManager().beginTransaction().replace(R.id.id_framework,new StepThirdToBeExpertNew()).commit();
    }
}
