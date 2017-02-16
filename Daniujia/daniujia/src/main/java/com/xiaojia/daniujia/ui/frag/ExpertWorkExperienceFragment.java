package com.xiaojia.daniujia.ui.frag;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.okhttp.FormEncodingBuilder;
import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.ExtraConst;
import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.dlgs.BaseDialog;
import com.xiaojia.daniujia.dlgs.BaseMsgDlg;
import com.xiaojia.daniujia.domain.resp.WorkExperienceVo;
import com.xiaojia.daniujia.managers.OkHttpClientManager;
import com.xiaojia.daniujia.ui.act.ActAddWorkExperience;
import com.xiaojia.daniujia.ui.adapters.ExpertWorkExperienceAdapter;
import com.xiaojia.daniujia.utils.ListUtils;
import com.xiaojia.daniujia.utils.SysUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZLOVE on 2016/6/28.
 */
public class ExpertWorkExperienceFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {

    private ListView listView;
    private ExpertWorkExperienceAdapter adapter;
    private List<WorkExperienceVo.CareersEntity> list = new ArrayList<>();

    private View footView;
    private Button btnAddExperience;

    private View containerView;
    private BaseMsgDlg deleteDialog;

    @Override
    protected int getInflateLayout() {
        return R.layout.frag_expert_work_experience;
    }

    @Override
    protected void setUpView(View view) {
        setBackButton(view.findViewById(R.id.id_back));
        ((TextView) view.findViewById(R.id.id_title)).setText("工作经历");

        containerView = view.findViewById(R.id.container);
        listView = (ListView) view.findViewById(R.id.id_list);
        adapter = new ExpertWorkExperienceAdapter(list, getActivity());
        footView = LayoutInflater.from(getActivity()).inflate(R.layout.foot_view_add_work, null);
        btnAddExperience = (Button) footView.findViewById(R.id.add_direction);
        listView.addFooterView(footView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);

        btnAddExperience.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    @Override
    public void onClick(View v) {
        if (v == btnAddExperience) {
            startActivity(new Intent(getActivity(), ActAddWorkExperience.class));
        }
    }

    private void setData(WorkExperienceVo resp) {
        if (resp == null) {
            return;
        }
        if (!ListUtils.isEmpty(resp.getCareers())) {
            list.clear();
            list.addAll(resp.getCareers());
            adapter.notifyDataSetChanged();
        }
    }

    public void initData(){
        String url = Config.WEB_API_SERVER_V3 + "/user/careers/" + SysUtil.getIntPref(PrefKeyConst.PREF_USER_ID);
        OkHttpClientManager.getInstance(getActivity()).getWithToken(url, new OkHttpClientManager.ResultCallback<WorkExperienceVo>() {
            @Override
            public void onResponse(WorkExperienceVo result) {
                setData(result);
            }
        });
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        if (deleteDialog == null) {
            deleteDialog = new BaseMsgDlg(getActivity());
        }
        deleteDialog.setBtnName(DialogInterface.BUTTON_POSITIVE, R.string.confirm);
        deleteDialog.setBtnName(DialogInterface.BUTTON_NEGATIVE, R.string.cancel);
        deleteDialog.setTitle("温馨提示");
        deleteDialog.setMsg("确定删除该工作经历？");
        deleteDialog.setCanceledOnTouchOutside(false);
        deleteDialog.setOnClickListener(new BaseDialog.DialogClickListener() {
            public void onOK() {
                String url = Config.WEB_API_SERVER_V3 + "/user/expert/careerinfo/del";
                FormEncodingBuilder builder = new FormEncodingBuilder();
                builder.add("userid", String.valueOf(SysUtil.getIntPref(PrefKeyConst.PREF_USER_ID)));
                builder.add("career_id", list.get(position).getCareerId() + "");
                OkHttpClientManager.getInstance(getActivity()).postWithToken(url, new OkHttpClientManager.ResultCallback<String>() {
                    @Override
                    public void onResponse(String result) {
                        list.remove(position);
                        adapter.notifyDataSetChanged();
                        containerView.postInvalidate();
                    }
                },builder);
            }
        });
        deleteDialog.show();
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        WorkExperienceVo.CareersEntity careersEntity = list.get(position);
        Intent intent = new Intent(getActivity(),ActAddWorkExperience.class);
        intent.putExtra(ExtraConst.FROM_YEAR, careersEntity.getEntryTime());
        intent.putExtra(ExtraConst.TO_YEAR, careersEntity.getQuitTime());
        intent.putExtra(ExtraConst.EXTRA_POSITION, careersEntity.getPosition());
        intent.putExtra(ExtraConst.COMPANY, careersEntity.getCompany());
        intent.putExtra(ExtraConst.EXTRA_PROFILE, careersEntity.getPositionDesc());
        intent.putExtra(ExtraConst.CAREER_ID, careersEntity.getCareerId());
        startActivity(intent);
    }
}
