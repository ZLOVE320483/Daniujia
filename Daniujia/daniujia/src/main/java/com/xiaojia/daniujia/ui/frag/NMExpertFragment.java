package com.xiaojia.daniujia.ui.frag;
import android.view.View;

import com.xiaojia.daniujia.R;

/**
 * Created by Administrator on 2016/6/30.
 */
public class NMExpertFragment extends BaseFragment{

    @Override
    protected int getInflateLayout() {
        return R.layout.fg_nm_expert;
    }

    @Override
    protected void setUpView(View view) {
        setBackButton(view.findViewById(R.id.tv_title_back));
    }
}
