package com.xiaojia.daniujia.ui.frag;

import android.content.Intent;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.IntentKey;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.domain.resp.AllDirectionRespVo;
import com.xiaojia.daniujia.managers.OkHttpClientManager;
import com.xiaojia.daniujia.ui.act.ActCompleteDirection;
import com.xiaojia.daniujia.ui.adapters.ExpertDirectionAdapter;
import com.xiaojia.daniujia.utils.ListUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZLOVE on 2016/6/30.
 */
public class SelectDirectionFragment extends BaseFragment implements ExpandableListView.OnChildClickListener {

    private ExpandableListView listView;
    private List<AllDirectionRespVo.MainDirection> list = new ArrayList<>();
    private ExpertDirectionAdapter adapter;

    @Override
    protected int getInflateLayout() {
        return R.layout.frag_select_direction;
    }

    @Override
    protected void setUpView(View view) {
        setBackButton(view.findViewById(R.id.id_back));
        ((TextView) view.findViewById(R.id.id_title)).setText("选择擅长方向");

        listView = (ExpandableListView) view.findViewById(R.id.id_list);
        adapter = new ExpertDirectionAdapter(getActivity(), list);
        listView.setAdapter(adapter);
        listView.setOnChildClickListener(this);

        requestData();
    }

    private void requestData() {
        String url = Config.WEB_API_SERVER_V3 + "/become/directions/all";
        OkHttpClientManager.getInstance(getActivity()).getWithToken(url, new OkHttpClientManager.ResultCallback<AllDirectionRespVo>() {
            @Override
            public void onResponse(AllDirectionRespVo result) {
                if (result == null) {
                    return;
                }
                if (!ListUtils.isEmpty(result.getMainDirections())) {
                    list.addAll(result.getMainDirections());
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        if (ListUtils.isEmpty(list)) {
            return true;
        }
        String mainDirection = list.get(groupPosition).getMainName();
        String subDirection = list.get(groupPosition).getSubDirections().get(childPosition).getSubName();
        String mainEditIcon = list.get(groupPosition).getEditIconUrl();
        int subId = list.get(groupPosition).getSubDirections().get(childPosition).getSubId();

        Intent intent = new Intent(getActivity(), ActCompleteDirection.class);
        intent.putExtra(IntentKey.INTENT_KEY_MAIN_DIRECTION, mainDirection);
        intent.putExtra(IntentKey.INTENT_KEY_MAIN_DIRECTION_EDIT_URL, mainEditIcon);
        intent.putExtra(IntentKey.INTENT_KEY_SUB_DIRECTION, subDirection);
        intent.putExtra(IntentKey.INTENT_KEY_SUB_ID, subId);
        startActivityForResult(intent, IntentKey.REQ_CODE_SELECT_DIRECTION);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentKey.REQ_CODE_SELECT_DIRECTION) {
            if (data != null) {
                finishActivity();
            }
        }
    }
}
