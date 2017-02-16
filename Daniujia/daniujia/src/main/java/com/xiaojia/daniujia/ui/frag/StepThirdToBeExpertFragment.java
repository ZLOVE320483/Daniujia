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
import com.xiaojia.daniujia.domain.resp.EducationVo;
import com.xiaojia.daniujia.managers.OkHttpClientManager;
import com.xiaojia.daniujia.ui.act.ActAddEduBackground;
import com.xiaojia.daniujia.ui.act.ActFourthStepToBeExpert;
import com.xiaojia.daniujia.ui.adapters.EducationAdapter;
import com.xiaojia.daniujia.utils.ListUtils;
import com.xiaojia.daniujia.utils.SysUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/17.
 */
public class StepThirdToBeExpertFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {

    private ListView myListView;
    private List<EducationVo.EducationsEntity> dataSource;
    private EducationAdapter mAdapter;
    private View divide;

    private TextView tvFifth;
    private boolean isModify = false;

    @Override
    protected int getInflateLayout() {
        return R.layout.fg_step3_to_be_expert;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataSource = new ArrayList();
    }

    @Override
    protected void setUpView(View view) {
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(IntentKey.INTENT_KEY_TO_BE_EXPERT_IS_MODIFY)) {
            isModify = intent.getBooleanExtra(IntentKey.INTENT_KEY_TO_BE_EXPERT_IS_MODIFY, false);
        }
        setBackButton(view.findViewById(R.id.id_back));
        TextView title = (TextView) view.findViewById(R.id.id_title);
        title.setText(R.string.edu_background);
        tvFifth = (TextView) view.findViewById(R.id.complete_tv);
        TextView next = (TextView) view.findViewById(R.id.title_right);
        next.setOnClickListener(this);
        next.setVisibility(View.VISIBLE);
        divide = view.findViewById(R.id.content_divide);
        myListView = (ListView) view.findViewById(R.id.list);
        myListView.addFooterView(LayoutInflater.from(getActivity()).inflate(R.layout.foot_view_add_edu, null));

        myListView.setOnItemLongClickListener(this);
        myListView.setOnItemClickListener(this);

        mAdapter = new EducationAdapter(dataSource, getActivity());
        myListView.setAdapter(mAdapter);
        view.findViewById(R.id.add_background).setOnClickListener(this);

        if (SysUtil.getIntPref(PrefKeyConst.PREF_USER_IDENTITY) == 2 || isModify) {
            tvFifth.setText("修改完成");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    public void initData() {
        String url = Config.WEB_API_SERVER_V3 + "/user/educations/new/" + SysUtil.getIntPref(PrefKeyConst.PREF_USER_ID);
        OkHttpClientManager.getInstance(getActivity()).getWithToken(url, new OkHttpClientManager.ResultCallback<EducationVo>() {
            @Override
            public void onResponse(EducationVo result) {
                if (result == null) {
                    return;
                }
                dataSource.clear();
                if (ListUtils.isEmpty(result.getEducations())) {
                    divide.setVisibility(View.GONE);
                } else {
                    divide.setVisibility(View.VISIBLE);
                }
                dataSource.addAll(result.getEducations());
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.title_right) {
            if (ListUtils.isEmpty(dataSource)) {
                showShortToast("至少添加一条");
                return;
            }
            Intent intent = new Intent(getActivity(), ActFourthStepToBeExpert.class);
            intent.putExtra(IntentKey.INTENT_KEY_TO_BE_EXPERT_IS_MODIFY, isModify);
            startActivity(intent);
        } else if (v.getId() == R.id.add_background) {
            startActivity(new Intent(getActivity(), ActAddEduBackground.class));
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

        BaseMsgDlg dialog = new BaseMsgDlg(getActivity());
        dialog.setBtnName(DialogInterface.BUTTON_POSITIVE, R.string.confirm);
        dialog.setBtnName(DialogInterface.BUTTON_NEGATIVE, R.string.cancel);
        dialog.setTitle("温馨提示");
        dialog.setMsg("确定删除该教育背景？");
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnClickListener(new BaseDialog.DialogClickListener() {
            public void onOK() {
                String url = Config.WEB_API_SERVER_V3 + "/user/expert/education/del";
                FormEncodingBuilder builder = new FormEncodingBuilder();
                builder.add("userid", String.valueOf(SysUtil.getIntPref(PrefKeyConst.PREF_USER_ID)));
                builder.add("education_id", dataSource.get(position).getEduId() + "");
                OkHttpClientManager.getInstance(getActivity()).postWithToken(url, new OkHttpClientManager.ResultCallback<String>() {
                    @Override
                    public void onResponse(String result) {
                        dataSource.remove(position);
                        mAdapter.notifyDataSetChanged();
                    }
                }, builder);
            }
        });
        dialog.show();
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        EducationVo.EducationsEntity educationsEntity = dataSource.get(position);
        Intent intent = new Intent(getActivity(), ActAddEduBackground.class);
        intent.putExtra(ExtraConst.FROM_YEAR, educationsEntity.getBeginTime());
        intent.putExtra(ExtraConst.TO_YEAR, educationsEntity.getEndTime());
        intent.putExtra(ExtraConst.EXTRA_SCHOOL, educationsEntity.getName());
        intent.putExtra(ExtraConst.QUAILTIFICATION, educationsEntity.getDegree());
        intent.putExtra(ExtraConst.EXTRA_DEGREE_ID, educationsEntity.getEduId());
        startActivity(intent);
    }
}
