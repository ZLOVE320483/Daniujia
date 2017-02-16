package com.xiaojia.daniujia.ui.frag;

import android.os.Bundle;
import android.view.View;

import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.ui.control.GraphicOrderFirstStepControl;

/**
 * Created by Administrator on 2016/5/5.
 */
public class GraphicOrderFirstStepFragment extends BaseFragment{

    GraphicOrderFirstStepControl control;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getInflateLayout() {
        return R.layout.fg_first_graphic_order;
    }

    @Override
    protected void setUpView(View view) {

    }
}
