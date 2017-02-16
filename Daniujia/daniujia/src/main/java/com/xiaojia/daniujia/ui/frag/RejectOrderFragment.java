package com.xiaojia.daniujia.ui.frag;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Request;
import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.IntentKey;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.domain.resp.BaseRespVo;
import com.xiaojia.daniujia.managers.OkHttpClientManager;
import com.xiaojia.daniujia.ui.adapters.RejectReasonAdapter;
import com.xiaojia.daniujia.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZLOVE on 2016/5/30.
 */
public class RejectOrderFragment extends BaseFragment implements AdapterView.OnItemClickListener, View.OnClickListener {

    private ListView listView;
    private RejectReasonAdapter adapter;
    private List<String> reasons = new ArrayList<>();
    private View headView;

    private EditText etReason;
    private View cancelView;
    private View confirmView;

    private int orderId = 0;
    private String rejectReason;
    private int curPos;

    @Override
    protected int getInflateLayout() {
        return R.layout.frag_reject_order_reason;
    }

    @Override
    protected void setUpView(View view) {
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            if (intent.hasExtra(IntentKey.INTENT_KEY_ORDER_ID)) {
                orderId = intent.getIntExtra(IntentKey.INTENT_KEY_ORDER_ID, 0);
            }
        }

        setBackButton(view.findViewById(R.id.id_back));
        ((TextView) view.findViewById(R.id.id_title)).setText("拒绝理由");

        listView = (ListView) view.findViewById(R.id.id_list);
        adapter = new RejectReasonAdapter(reasons, getActivity());
        initHeadView();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        etReason = (EditText) view.findViewById(R.id.reason);
        cancelView = view.findViewById(R.id.id_cancel);
        setBackButton(cancelView);
        confirmView = view.findViewById(R.id.id_confirm);
        confirmView.setOnClickListener(this);

        initData();
    }

    private void initData() {
        reasons.add("您的问题描述过于宽泛，我不能有针对性的回答，抱歉");
        reasons.add("您的问题描述不够清晰，我很难给出回答，抱歉");
        reasons.add("您提的问题我并不熟悉，不能回答，抱歉");
        reasons.add("其他原因");
        adapter.notifyDataSetChanged();

        rejectReason = reasons.get(0);
    }

    private void initHeadView() {
        headView = LayoutInflater.from(getActivity()).inflate(R.layout.list_header_reject, null);
        listView.addHeaderView(headView);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        curPos = position - listView.getHeaderViewsCount();

        etReason.clearAnimation();
        if (curPos == reasons.size() - 1) {
            etReason.setVisibility(View.VISIBLE);
        } else {
            etReason.setVisibility(View.GONE);
        }

        if (curPos < reasons.size() - 1) {
            rejectReason = reasons.get(curPos);
        }

        adapter.setCurPos(curPos);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        if (v == confirmView) {
            if (curPos == reasons.size() - 1) {
                rejectReason = etReason.getText().toString().trim();
            }
            if (TextUtils.isEmpty(rejectReason)) {
                showShortToast("请输入拒绝理由");
                return;
            }
            rejectOrderRequest();
        }
    }

    private void rejectOrderRequest() {
        String url = Config.WEB_API_SERVER + "/user/order/reject";
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("orderId", String.valueOf(orderId));
        builder.add("rejectReason", rejectReason);
        OkHttpClientManager.getInstance(getActivity()).postWithToken(true, url, new OkHttpClientManager.ResultCallback<BaseRespVo>() {
            @Override
            public void onError(Request paramRequest, Exception e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(BaseRespVo result) {
                if (result.getReturncode().equals("SUCCESS")) {
                    Intent intent = new Intent();
                    finishActivity(intent);
                } else {
                    showShortToast(result.getReturnmsg());
                }
            }

            @Override
            public void onFail(String errorMsg) {
                LogUtil.d("ZLOVE", "rejectOrderRequest---onFail---" + errorMsg);
            }
        }, builder);
    }
}
