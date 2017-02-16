package com.xiaojia.daniujia.ui.frag;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.domain.resp.AllIndustryVo;
import com.xiaojia.daniujia.ui.adapters.AllIndustryAdapter;
import com.xiaojia.daniujia.ui.control.AllIndustryControl;

/**
 * Created by Administrator on 2016/4/26
 */
public class AllIndustryFragment extends BaseFragment{

    private AllIndustryControl control;
    private ListView listView;

    @Override
    protected int getInflateLayout() {
        return R.layout.activity_all_industry;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        control = new AllIndustryControl();
        setControl(control);
    }

    @Override
    protected void setUpView(View view) {
        setBackButton(view.findViewById(R.id.iv_title_back));
        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_title.setText("所有专家分类");
        listView = (ListView) view.findViewById(R.id.list);
        control.initData();
    }

    public void refreshData(AllIndustryVo result) {
        listView.setAdapter(new AllIndustryAdapter(result.getClassifications(), getActivity()));
    }
}
