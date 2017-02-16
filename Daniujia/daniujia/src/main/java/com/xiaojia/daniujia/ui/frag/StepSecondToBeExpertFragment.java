package com.xiaojia.daniujia.ui.frag;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.okhttp.FormEncodingBuilder;
import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.ExtraConst;
import com.xiaojia.daniujia.IntentKey;
import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.dlgs.BaseDialog;
import com.xiaojia.daniujia.dlgs.BaseMsgDlg;
import com.xiaojia.daniujia.domain.resp.WorkExperienceVo;
import com.xiaojia.daniujia.managers.OkHttpClientManager;
import com.xiaojia.daniujia.ui.act.ActAddWorkExperience;
import com.xiaojia.daniujia.ui.act.ActThirdStepToBeExpert;
import com.xiaojia.daniujia.ui.adapters.WorkExperienceAdapter;
import com.xiaojia.daniujia.ui.control.StepSecondControl;
import com.xiaojia.daniujia.utils.ListUtils;
import com.xiaojia.daniujia.utils.SysUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/17.
 */
public class StepSecondToBeExpertFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private StepSecondControl control;
    private ListView mList;
    private List<WorkExperienceVo.CareersEntity> dataSource;
    private WorkExperienceAdapter mAdapter;
    private View divide;
    private View footView;

    private TextView tvFifth;
    private boolean isModify = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        control = new StepSecondControl();
        setControl(control);
    }

    @Override
    public void onResume() {
        super.onResume();
        control.initData();
    }

    @Override
    protected int getInflateLayout() {
        return R.layout.fg_step2_to_be_expert;
    }

    @Override
    protected void setUpView(View view) {

        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(IntentKey.INTENT_KEY_TO_BE_EXPERT_IS_MODIFY)) {
            isModify = intent.getBooleanExtra(IntentKey.INTENT_KEY_TO_BE_EXPERT_IS_MODIFY, false);
        }

        setBackButton(view.findViewById(R.id.id_back));
        dataSource = new ArrayList<>();
        TextView title = (TextView) view.findViewById(R.id.id_title);
        title.setText(R.string.work_experience);
        tvFifth = (TextView) view.findViewById(R.id.complete_tv);
        TextView next = (TextView) view.findViewById(R.id.title_right);
        next.setVisibility(View.VISIBLE);
        next.setOnClickListener(this);
        divide = view.findViewById(R.id.content_divide);
        mList = (ListView) view.findViewById(R.id.list);
        footView = LayoutInflater.from(getActivity()).inflate(R.layout.foot_view_add_work,null);
        mList.addFooterView(footView);
        mList.setOnItemClickListener(this);
        mList.setOnItemLongClickListener(this);

        mAdapter = new WorkExperienceAdapter(dataSource,getActivity());
        mList.setAdapter(mAdapter);
        footView.findViewById(R.id.add_direction).setOnClickListener(this);

        if (SysUtil.getIntPref(PrefKeyConst.PREF_USER_IDENTITY) == 2 || isModify) {
            tvFifth.setText("修改完成");
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.title_right) {
            if (ListUtils.isEmpty(dataSource)) {
                showShortToast("至少添加一条");
                return;
            }
            Intent intent = new Intent(getActivity(), ActThirdStepToBeExpert.class);
            intent.putExtra(IntentKey.INTENT_KEY_TO_BE_EXPERT_IS_MODIFY, isModify);
            startActivity(intent);
        } else if (v.getId() == R.id.add_direction) {
            startActivity(new Intent(getActivity(), ActAddWorkExperience.class));
        }
    }

    public void refreshData(WorkExperienceVo result) {
        if (result == null) {
            return;
        }
        if (dataSource != null) {
            dataSource.clear();
            if (ListUtils.isEmpty(result.getCareers())) {
                divide.setVisibility(View.GONE);
            }else{
                dataSource.addAll(result.getCareers());
                divide.setVisibility(View.VISIBLE);
            }
        }
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        WorkExperienceVo.CareersEntity careersEntity = dataSource.get(position);
        Intent intent = new Intent(getActivity(),ActAddWorkExperience.class);
        intent.putExtra(ExtraConst.FROM_YEAR, careersEntity.getEntryTime());
        intent.putExtra(ExtraConst.TO_YEAR, careersEntity.getQuitTime());
        intent.putExtra(ExtraConst.EXTRA_POSITION, careersEntity.getPosition());
        intent.putExtra(ExtraConst.COMPANY, careersEntity.getCompany());
        intent.putExtra(ExtraConst.EXTRA_PROFILE, careersEntity.getPositionDesc());
        intent.putExtra(ExtraConst.CAREER_ID, careersEntity.getCareerId());
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        BaseMsgDlg dialog = new BaseMsgDlg(getActivity());
        dialog.setBtnName(DialogInterface.BUTTON_POSITIVE, R.string.confirm);
        dialog.setBtnName(DialogInterface.BUTTON_NEGATIVE, R.string.cancel);
        dialog.setTitle("温馨提示");
        dialog.setMsg("确定删除该工作经历？");
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnClickListener(new BaseDialog.DialogClickListener() {
            public void onOK() {
                String url = Config.WEB_API_SERVER_V3 + "/user/expert/careerinfo/del";
                FormEncodingBuilder builder = new FormEncodingBuilder();
                builder.add("userid", String.valueOf(SysUtil.getIntPref(PrefKeyConst.PREF_USER_ID)));
                builder.add("career_id", dataSource.get(position).getCareerId() + "");
                OkHttpClientManager.getInstance(getActivity()).postWithToken(url, new OkHttpClientManager.ResultCallback<String>() {
                    @Override
                    public void onResponse(String result) {
                        dataSource.remove(position);
                        mAdapter.notifyDataSetChanged();
                    }
                },builder);
            }
        });
        dialog.show();
        return true;
    }
}
