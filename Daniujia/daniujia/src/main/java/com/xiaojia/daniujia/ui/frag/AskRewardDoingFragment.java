package com.xiaojia.daniujia.ui.frag;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.IntentKey;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.ask.AskRewardWebActivity;
import com.xiaojia.daniujia.domain.resp.AskRewardListRespVo;
import com.xiaojia.daniujia.managers.OkHttpClientManager;
import com.xiaojia.daniujia.ui.adapters.AskRewardListAdapter;
import com.xiaojia.daniujia.ui.views.XListView;
import com.xiaojia.daniujia.utils.ListUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZLOVE on 2016/10/24.
 */
public class AskRewardDoingFragment extends BaseFragment implements XListView.IXListViewListener, AdapterView.OnItemClickListener {

    private XListView listView;
    private AskRewardListAdapter adapter;
    private List<AskRewardListRespVo.AskRewardListItem> items = new ArrayList<>();

    private int cursor = 0;
    private boolean isRefresh = true;

    @Override
    protected int getInflateLayout() {
        return R.layout.frag_ask_forward_list;
    }

    @Override
    protected void setUpView(View view) {
        listView = (XListView) view.findViewById(R.id.id_list);
        adapter = new AskRewardListAdapter(items, getActivity(), false);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        listView.setXListViewListener(this);
        listView.setPullRefreshEnable(true);
        listView.setPullLoadEnable(true);

        if (ListUtils.isEmpty(items)) {
            requestData();
        }
    }

    private void requestData() {
        String url = Config.WEB_API_SERVER_V3 + "/reward/questions?type=published&cursor=" + cursor;
        OkHttpClientManager.getInstance(getActivity()).getWithPlatform(true, url, new OkHttpClientManager.ResultCallback<AskRewardListRespVo>() {
            @Override
            public void onResponse(AskRewardListRespVo result) {
                refreshData(result);
            }
        });

    }

    private void refreshData(AskRewardListRespVo result) {
        listView.stopRefresh();
        listView.stopLoadMore();
        if (result == null) {
            //TODO
            return;
        }
        List<AskRewardListRespVo.AskRewardListItem> tmpList = result.getPublishedQuestions().getItems();
        if (tmpList.size() < 10) {
            listView.setPullLoadEnable(false);
        } else {
            listView.setPullLoadEnable(true);
        }
        if (isRefresh) {
            items.clear();
        }
        items.addAll(tmpList);
        cursor = result.getPublishedQuestions().getNextCursor();

        if (ListUtils.isEmpty(items)) {
            // TODO
        } else {

        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        cursor = 0;
        requestData();
    }

    @Override
    public void onLoadMore() {
        isRefresh = false;
        requestData();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (ListUtils.isEmpty(items)) {
            return;
        }
        int headCount = listView.getHeaderViewsCount();
        AskRewardListRespVo.AskRewardListItem item = items.get(position - headCount);
        if (item == null) {
            return;
        }
        Intent intent = new Intent(getActivity(), AskRewardWebActivity.class);
        intent.putExtra(IntentKey.INTENT_KEY_ASK_REWARD_FROM, IntentKey.ASK_REWARD_DETAIL);
        intent.putExtra(IntentKey.INTENT_KEY_ASK_REWARD_ID, item.getId());
        startActivity(intent);
    }
}
