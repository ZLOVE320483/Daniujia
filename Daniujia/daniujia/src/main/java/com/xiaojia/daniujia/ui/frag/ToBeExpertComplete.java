package com.xiaojia.daniujia.ui.frag;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.IntentKey;
import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.children.ChildrenShareWebActivity;
import com.xiaojia.daniujia.dlgs.KnowledgeShareDlg;
import com.xiaojia.daniujia.domain.resp.ChildShareRespVo;
import com.xiaojia.daniujia.managers.OkHttpClientManager;
import com.xiaojia.daniujia.ui.act.ActExpertEducationBackground;
import com.xiaojia.daniujia.ui.act.ActExpertHome;
import com.xiaojia.daniujia.ui.act.ActExpertServiceSetting;
import com.xiaojia.daniujia.ui.act.ActExpertWorkExperience;
import com.xiaojia.daniujia.ui.act.ActMain;
import com.xiaojia.daniujia.utils.SysUtil;

/**
 * Created by Administrator on 2016/6/28.
 */
public class ToBeExpertComplete extends BaseFragment implements View.OnClickListener {

    private KnowledgeShareDlg dialog;

    private String fromWhere = "";

    @Override
    protected int getInflateLayout() {
        return R.layout.fg_to_be_expert_complete;
    }

    @Override
    protected void setUpView(View view) {
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(IntentKey.FROM_WHERE)) {
            fromWhere = intent.getStringExtra(IntentKey.FROM_WHERE);
        }
        setBackButton(view.findViewById(R.id.id_back));
        ImageView step4 = (ImageView) view.findViewById(R.id.step4);
        step4.setImageResource(R.drawable.finish_white);

        TextView title = (TextView) view.findViewById(R.id.id_title);
        title.setText("申请完成");

        ((TextView) view.findViewById(R.id.title_right)).setText("确定");
        view.findViewById(R.id.preview).setOnClickListener(this);
        view.findViewById(R.id.title_right).setOnClickListener(this);
        view.findViewById(R.id.word_experience).setOnClickListener(this);
        view.findViewById(R.id.edu_background).setOnClickListener(this);
        view.findViewById(R.id.service_set).setOnClickListener(this);
        SysUtil.savePref(PrefKeyConst.PREF_APPLY_STEP, 15);
        getKnowledgeIconState();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_right:
                Intent mainIntent = new Intent(getActivity(), ActMain.class);
                mainIntent.putExtra(IntentKey.INTENT_KEY_TO_BE_EXPERT_COMPLETE, true);
                mainIntent.putExtra(IntentKey.FROM_WHERE, fromWhere);
                startActivity(mainIntent);
                break;
            case R.id.preview:
                Intent preIntent = new Intent(getActivity(), ActExpertHome.class);
                preIntent.putExtra(IntentKey.INTENT_KEY_EXPERT_ID, SysUtil.getIntPref(PrefKeyConst.PREF_USER_ID));
                preIntent.putExtra(IntentKey.INTENT_KEY_FROM_EXPERT_PREVIEW, true);
                startActivity(preIntent);
                break;
            case R.id.word_experience:
                startActivity(new Intent(getActivity(), ActExpertWorkExperience.class));
                break;
            case R.id.edu_background:
                startActivity(new Intent(getActivity(), ActExpertEducationBackground.class));
                break;
            case R.id.service_set:
                startActivity(new Intent(getActivity(), ActExpertServiceSetting.class));
                break;
        }
    }

    public void getKnowledgeIconState(){
        String url = Config.WEB_API_SERVER + "/init";
        OkHttpClientManager.getInstance(getActivity()).getWithToken(false, url, new OkHttpClientManager.ResultCallback<ChildShareRespVo>() {
            @Override
            public void onResponse(ChildShareRespVo respVo) {
                if (respVo != null && !TextUtils.isEmpty(respVo.getCharityExpert())) {
                    if (dialog == null) {
                        dialog = new KnowledgeShareDlg(getActivity());
                        dialog.setJoinClickListener(new KnowledgeShareDlg.OnJoinClickListener() {
                            @Override
                            public void onClickJoin() {
                                Intent intent = new Intent(getActivity(), ChildrenShareWebActivity.class);
                                intent.putExtra("_child_join", true);
                                startActivity(intent);
                            }
                        });
                    }
                    dialog.showDialog();
                }
            }
        });
    }
}
