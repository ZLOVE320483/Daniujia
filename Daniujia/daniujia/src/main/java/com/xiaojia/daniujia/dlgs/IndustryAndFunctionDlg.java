package com.xiaojia.daniujia.dlgs;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.xiaojia.daniujia.ExtraConst;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.domain.resp.IndustryAndFunctionVo;
import com.xiaojia.daniujia.ui.views.wheelview.OnWheelChangedListener;
import com.xiaojia.daniujia.ui.views.wheelview.WheelView;
import com.xiaojia.daniujia.ui.views.wheelview.adapters.ArrayWheelAdapter;
import com.xiaojia.daniujia.utils.SysUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/11.
 */
public class IndustryAndFunctionDlg extends BaseBottomDialog implements View.OnClickListener, OnWheelChangedListener {

    private IndustryAndFunctionVo industryAndFunctionVo;

    private String currentIndustryName;
    private String currentFunctionName;

    private WheelView industry;
    private WheelView function;

    private String industrys[];
    private String functions[];
    private String oldIndustryName;
    private Map<String,String[]> industry_function = new HashMap<>(); // get value from the key to setAdapter
    private List<IndustryAndFunctionVo.Novation> dataSource = new ArrayList<>(); //the dataSource to init the arr

    public IndustryAndFunctionDlg(Activity context,String industryName,String functionName,IndustryAndFunctionVo industryAndFunctionVo) {
        super(context, SysUtil.inflate(R.layout.dlg_bottom_choose_industry));
        this.currentIndustryName = industryName;
        this.currentFunctionName = functionName;
        oldIndustryName = currentIndustryName;
        this.industryAndFunctionVo = industryAndFunctionVo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpView();
        setUpData();
    }

    private void setUpData() {
        initData();
        industry.setViewAdapter(new ArrayWheelAdapter<>(mActivity, industrys));
        updateFunction();
        recoverDataFrom();
    }

    private void recoverDataFrom() {
        if (!TextUtils.isEmpty(currentIndustryName)) {
            List<String> tempList = Arrays.asList(industrys);
            industry.setCurrentItem(tempList.indexOf(currentIndustryName));
            List<String> tempList2 = Arrays.asList(functions);
            function.setCurrentItem(tempList2.indexOf(currentFunctionName));
        }
    }

    private void updateFunction() {

        String[] functions = industry_function.get(currentIndustryName);
        if (functions == null) {
            functions = new String[]{""};
        }
        function.setViewAdapter(new ArrayWheelAdapter<>(mActivity, functions));
        if (oldIndustryName.equals(currentIndustryName)) {
            function.setCurrentItem(Arrays.asList(functions).indexOf(currentFunctionName));
        }else{
            function.setCurrentItem(0);
        }
        int currentItem = function.getCurrentItem();
        if (currentItem < functions.length) {
            currentFunctionName = functions[function.getCurrentItem()];
        }else {
            currentFunctionName = functions[0];
        }
    }

    private void initData() {
        if (industryAndFunctionVo.getNovations() == null || industryAndFunctionVo.getNovations().size() == 0) {
            return;
        }
        dataSource = industryAndFunctionVo.getNovations();
        industrys = new String[dataSource.size()];
        for (int i = 0; i < dataSource.size();i++) {
            industrys[i] = dataSource.get(i).getName();
            List<IndustryAndFunctionVo.Novation.Function> funct = dataSource.get(i).functions;
            functions = new String[funct.size()];
            for (int j = 0; j < funct.size(); j++) {
                functions[j] = funct.get(j).getName();
            }
            industry_function.put(industrys[i], functions);
        }
    }

    private void setUpView() {

        industry = (WheelView) mView.findViewById(R.id.industry);
        function = (WheelView) mView.findViewById(R.id.function);

        industry.setVisibleItems(7);
        function.setVisibleItems(7);

        industry.addChangingListener(this);
        function.addChangingListener(this);

        mView.findViewById(R.id.confirm).setOnClickListener(this);
        mView.findViewById(R.id.cancel).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.confirm) {
            int currentItem1 = industry.getCurrentItem();
            int currentItem2 = function.getCurrentItem();
            int functionId = dataSource.get(currentItem1).functions.get(currentItem2).getFunctionId();
            Bundle b = new Bundle();
            String[] arr = new String[] {currentIndustryName,currentFunctionName,functionId +""};
            b.putStringArray(ExtraConst.EXTRA_PROFILE_ADDR, arr);
            mClickListener.onOK(b);
        }
        dismiss();
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel == industry) {
            currentIndustryName = industrys[newValue];
            updateFunction();
        }else{
            currentFunctionName = industry_function.get(currentIndustryName)[newValue];
        }
    }
}
