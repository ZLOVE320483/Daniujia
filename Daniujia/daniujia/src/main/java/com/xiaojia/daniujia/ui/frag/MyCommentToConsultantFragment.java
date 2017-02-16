package com.xiaojia.daniujia.ui.frag;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Request;
import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.IntentKey;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.dlgs.MyCommentToConsultantDialog;
import com.xiaojia.daniujia.domain.resp.BaseRespVo;
import com.xiaojia.daniujia.domain.resp.MyCommentToConsultantRespVo;
import com.xiaojia.daniujia.managers.OkHttpClientManager;
import com.xiaojia.daniujia.ui.adapters.MyCommentToConsultantAdapter;
import com.xiaojia.daniujia.ui.views.XListView;
import com.xiaojia.daniujia.utils.ListUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZLOVE on 2017/1/3.
 */
public class MyCommentToConsultantFragment extends BaseFragment implements View.OnClickListener, MyCommentToConsultantDialog.CommentToConsultantListener, XListView.IXListViewListener {

    private XListView listView;
    private MyCommentToConsultantAdapter adapter;
    private List<MyCommentToConsultantRespVo.Evaluation> data = new ArrayList<>();

    private MyCommentToConsultantDialog dialog;

    private int demandId;
    private int expertId;
    private int cursor = 0;
    private boolean canComment = true;

    @Override
    protected int getInflateLayout() {
        return R.layout.frag_my_comment_to_consultant;
    }

    @Override
    protected void setUpView(View view) {
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            if (intent.hasExtra(IntentKey.INTENT_KEY_DEMAND_ID)) {
                demandId = intent.getIntExtra(IntentKey.INTENT_KEY_DEMAND_ID, 0);
            }
            if (intent.hasExtra(IntentKey.INTENT_KEY_EXPERT_ID)) {
                expertId = intent.getIntExtra(IntentKey.INTENT_KEY_EXPERT_ID, 0);
            }
            if (intent.hasExtra(IntentKey.INTENT_KEY_IS_REQUIREMENT_CAN_COMMENT)) {
                canComment = intent.getBooleanExtra(IntentKey.INTENT_KEY_IS_REQUIREMENT_CAN_COMMENT, true);
            }
        }
        setBackButton(view.findViewById(R.id.id_back));
        ((TextView) view.findViewById(R.id.id_title)).setText("顾问的评价");

        listView = (XListView) view.findViewById(R.id.id_list);
        listView.setPullRefreshEnable(true);
        listView.setPullLoadEnable(false);
        adapter = new MyCommentToConsultantAdapter(data, getActivity());
        if (canComment) {
            initFootView();
        }
        listView.setAdapter(adapter);
        onRefresh();
    }

    private void initFootView() {
        View footView = LayoutInflater.from(getActivity()).inflate(R.layout.foot_view_my_comment_to_consultant, null);
        footView.findViewById(R.id.add).setOnClickListener(this);
        listView.addFooterView(footView);
    }

    @Override
    public void onRefresh() {
        cursor = 0;
        getCommentList();
    }

    @Override
    public void onLoadMore() {
        getCommentList();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add) {
            if (dialog == null) {
                dialog = new MyCommentToConsultantDialog(getActivity(), "", this);
            }
            dialog.showDialog();
        }
    }

    @Override
    public void comment(String content) {
        if (TextUtils.isEmpty(content)) {
            showShortToast("评论不能为空");
            return;
        }
        addRequirementExpertComment(content);
    }

    private void addRequirementExpertComment(String content) {
        String url = Config.WEB_API_SERVER + "/demand/evaluations/" + expertId;
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("demandId", String.valueOf(demandId));
        builder.add("content", content);
        OkHttpClientManager.getInstance(getActivity()).postWithToken(url, new OkHttpClientManager.ResultCallback<BaseRespVo>() {
            @Override
            public void onResponse(BaseRespVo result) {
                if (result.getReturncode().equals("SUCCESS")) {
                    onRefresh();
                } else {
                    showShortToast(result.getReturnmsg());
                }
            }
        }, builder);
    }

    private void getCommentList() {
        String url = Config.WEB_API_SERVER + "/demand/evaluations/" + expertId + "?demandId=" + demandId + "&cursor=" + cursor;
        OkHttpClientManager.getInstance(getActivity()).getWithToken(url, new OkHttpClientManager.ResultCallback<MyCommentToConsultantRespVo>() {
            @Override
            public void onResponse(MyCommentToConsultantRespVo respVo) {
                listView.stopRefresh();
                listView.stopLoadMore();
                if (respVo == null) {
                    return;
                }
                MyCommentToConsultantRespVo.Expert expert = respVo.getExpert();
                adapter.setExpertInfo(expert.getImgUrl(), expert.getName());
                if (respVo.getNextCursor() == 0) {
                    listView.setPullLoadEnable(false);
                } else {
                    listView.setPullLoadEnable(true);
                }
                List<MyCommentToConsultantRespVo.Evaluation> evaluations = respVo.getItems();
                if (!ListUtils.isEmpty(evaluations)) {
                    if (cursor == 0) {
                        data.clear();
                    }
                    data.addAll(evaluations);
                    adapter.notifyDataSetChanged();
                }
                cursor = respVo.getNextCursor();
            }

            @Override
            public void onFail(String errorMsg) {
                super.onFail(errorMsg);
                listView.stopRefresh();
                listView.stopLoadMore();
            }

            @Override
            public void onError(Request paramRequest, Exception e) {
                super.onError(paramRequest, e);
                listView.stopRefresh();
                listView.stopLoadMore();
            }
        });
    }
}
