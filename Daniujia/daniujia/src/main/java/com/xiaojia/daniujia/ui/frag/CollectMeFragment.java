package com.xiaojia.daniujia.ui.frag;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.xiaojia.daniujia.IntentKey;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.domain.resp.CollectMeRespVo;
import com.xiaojia.daniujia.ui.act.ActExpertHome;
import com.xiaojia.daniujia.ui.adapters.CollectMeAdapter;
import com.xiaojia.daniujia.ui.control.CollectMeControl;
import com.xiaojia.daniujia.utils.ListUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZLOVE on 2016/5/9.
 */
public class CollectMeFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    private CollectMeControl collectMeControl;

    private View noCollectionContainer;
    private CollectMeAdapter adapter;
    private List<CollectMeRespVo.FavoriteBean> list = new ArrayList<>();

    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        collectMeControl = new CollectMeControl();
        setControl(collectMeControl);
    }

    @Override
    protected int getInflateLayout() {
        return R.layout.frag_collect_me;
    }

    @Override
    protected void setUpView(View view) {
        setBackButton(view.findViewById(R.id.id_back));
        ((TextView) view.findViewById(R.id.id_title)).setText("收藏我");

        noCollectionContainer = view.findViewById(R.id.no_collect);
        listView = (ListView) view.findViewById(R.id.id_list_view);
        adapter = new CollectMeAdapter(list, getActivity());
        listView.setAdapter(adapter);
        collectMeControl.requestCollectMeInfo(1);
        listView.setOnItemClickListener(this);
    }

    public void setData(CollectMeRespVo data) {
        if (data == null) {
            noCollectionContainer.setVisibility(View.VISIBLE);
            return;
        }
        if (!ListUtils.isEmpty(data.getFavorites())) {
            this.list.addAll(data.getFavorites());
            adapter.notifyDataSetChanged();
        } else {
            noCollectionContainer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CollectMeRespVo.FavoriteBean item = adapter.getItem(position);
        if (item == null) {
            return;
        }
        if (item.getIdentity() == 1) {
            // TODO
        } else {
            Intent intent = new Intent(getActivity(), ActExpertHome.class);
            intent.putExtra(IntentKey.INTENT_KEY_EXPERT_ID, item.getUserId());
            startActivity(intent);
        }
    }
}
