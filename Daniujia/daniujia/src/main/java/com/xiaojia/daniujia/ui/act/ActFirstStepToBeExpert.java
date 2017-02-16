package com.xiaojia.daniujia.ui.act;

import android.os.Bundle;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.base.AbsBaseActivity;
import com.xiaojia.daniujia.ui.frag.StepFirstToBeExpertFragment;
import com.xiaojia.daniujia.ui.frag.StepFirstToBeExpertNew;

/**
 * Created by Administrator on 2016/5/17.
 */
public class ActFirstStepToBeExpert extends AbsBaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_act_fragment_container);
        getSupportFragmentManager().beginTransaction().replace(R.id.id_framework, new StepFirstToBeExpertNew()).commit();
    }
}

