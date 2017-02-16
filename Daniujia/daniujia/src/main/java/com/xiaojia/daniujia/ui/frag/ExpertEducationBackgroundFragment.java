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
import com.xiaojia.daniujia.domain.resp.EducationVo;
import com.xiaojia.daniujia.managers.OkHttpClientManager;
import com.xiaojia.daniujia.ui.act.ActAddEduBackground;
import com.xiaojia.daniujia.ui.adapters.ExpertEducationBackgroundAdapter;
import com.xiaojia.daniujia.utils.ListUtils;
import com.xiaojia.daniujia.utils.SysUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZLOVE on 2016/6/28.
 */
public class ExpertEducationBackgroundFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {

    private ListView listView;
    private ExpertEducationBackgroundAdapter adapter;
    private List<EducationVo.EducationsEntity> list = new ArrayList<>();

    private View footView;
    private Button btnAddBackground;

    private View containerView;
    private BaseMsgDlg deleteDialog;

    @Override
    protected int getInflateLayout() {
        return R.layout.frag_expert_work_experience;
    }

    @Override
    protected void setUpView(View view) {
        setBackButton(view.findViewById(R.id.id_back));
        ((TextView) view.findViewById(R.id.id_title)).setText("教育背景");

        containerView = view.findViewById(R.id.container);
        listView = (ListView) view.findViewById(R.id.id_list);
        adapter = new ExpertEducationBackgroundAdapter(list, getActivity());
        footView = LayoutInflater.from(getActivity()).inflate(R.layout.foot_view_add_edu, null);
        btnAddBackground = (Button) footView.findViewById(R.id.add_background);
        listView.addFooterView(footView);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(this);
        listView.setOnItemClickListener(this);

        btnAddBackground.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    @Override
    public void onClick(View v) {
        if (v == btnAddBackground) {
            startActivity(new Intent(getActivity(), ActAddEduBackground.class));
        }
    }

    private void initData() {
        String url = Config.WEB_API_SERVER_V3 + "/user/educations/new/" + SysUtil.getIntPref(PrefKeyConst.PREF_USER_ID);
        OkHttpClientManager.getInstance(getActivity()).getWithToken(url, new OkHttpClientManager.ResultCallback<EducationVo>() {
            @Override
            public void onResponse(EducationVo result) {
                if (result == null) {
                    return;
                }
                if (!ListUtils.isEmpty(result.getEducations())) {
                    list.clear();
                    list.addAll(result.getEducations());
                    adapter.notifyDataSetChanged();
                }
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
        deleteDialog.setMsg("确定删除该教育背景？");
        deleteDialog.setCanceledOnTouchOutside(false);
        deleteDialog.setOnClickListener(new BaseDialog.DialogClickListener() {
            public void onOK() {
                String url = Config.WEB_API_SERVER_V3 + "/user/expert/education/del";
                FormEncodingBuilder builder = new FormEncodingBuilder();
                builder.add("userid", String.valueOf(SysUtil.getIntPref(PrefKeyConst.PREF_USER_ID)));
                builder.add("education_id", list.get(position).getEduId() + "");
                OkHttpClientManager.getInstance(getActivity()).postWithToken(url, new OkHttpClientManager.ResultCallback<String>() {
                    @Override
                    public void onResponse(String result) {
                        list.remove(position);
                        adapter.notifyDataSetChanged();
                        containerView.postInvalidate();
                    }
                }, builder);
            }
        });
        deleteDialog.show();
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        EducationVo.EducationsEntity educationsEntity = list.get(position);
        Intent intent = new Intent(getActivity(), ActAddEduBackground.class);
        intent.putExtra(ExtraConst.FROM_YEAR, educationsEntity.getBeginTime());
        intent.putExtra(ExtraConst.TO_YEAR, educationsEntity.getEndTime());
        intent.putExtra(ExtraConst.EXTRA_SCHOOL, educationsEntity.getName());
        intent.putExtra(ExtraConst.QUAILTIFICATION, educationsEntity.getDegree());
        intent.putExtra(ExtraConst.EXTRA_DEGREE_ID, educationsEntity.getEduId());
        startActivity(intent);
    }
}
