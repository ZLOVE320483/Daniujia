package com.xiaojia.daniujia.ui.frag;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.IntentKey;
import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.domain.resp.MyAppointExpert;
import com.xiaojia.daniujia.managers.OkHttpClientManager;
import com.xiaojia.daniujia.ui.act.ActExpertOrderDetail;
import com.xiaojia.daniujia.ui.adapters.ExpertOrderAdapter;
import com.xiaojia.daniujia.ui.views.XListView;
import com.xiaojia.daniujia.utils.ListUtils;
import com.xiaojia.daniujia.utils.SysUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZLOVE on 2016/5/9.
 */
public class ExpertOrderDoingFragment extends BaseFragment implements AdapterView.OnItemClickListener, XListView.IXListViewListener {

    private View noDataContainer;
    private TextView tvNoDataTip;
    private ImageView ivNoUser;
    private XListView listView;
    private ExpertOrderAdapter adapter;
    private List<MyAppointExpert.OrdersEntity> list = new ArrayList<>();
    private int pageNum = 1;
    private boolean isRefresh = true;
    private boolean isFirstRefresh = true;

    @Override
    protected int getInflateLayout() {
        return R.layout.frag_my_expert_list;
    }

    @Override
    protected void setUpView(View view) {
        noDataContainer = view.findViewById(R.id.no_data_container);
        tvNoDataTip = (TextView) view.findViewById(R.id.no_data_tip);
        ivNoUser = (ImageView) view.findViewById(R.id.id_no_data);
        ivNoUser.setImageResource(R.drawable.bg_no_user);
        listView = (XListView) view.findViewById(R.id.id_list);
        adapter = new ExpertOrderAdapter(list, getActivity());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        listView.setXListViewListener(this);
        listView.setPullRefreshEnable(true);

        if (ListUtils.isEmpty(list)) {
            requestData();
        }
    }

    public void refreshData(MyAppointExpert result) {
        isFirstRefresh = false;
        listView.stopRefresh();
        listView.stopLoadMore();
        if (result == null) {
            noDataContainer.setVisibility(View.VISIBLE);
            tvNoDataTip.setText("您还没有进行中的服务订单");
            return;
        } else {
            noDataContainer.setVisibility(View.GONE);
        }
        List<MyAppointExpert.OrdersEntity> tmpList = result.getOrders();

        if (isRefresh) {
            list.clear();
        }
        list.addAll(result.getOrders());
        if (ListUtils.isEmpty(list)) {
            noDataContainer.setVisibility(View.VISIBLE);
            tvNoDataTip.setText("您还没有进行中的服务订单");
            return;
        } else {
            noDataContainer.setVisibility(View.GONE);
        }
        adapter.notifyDataSetChanged();

        if (tmpList.size() < 10) {
            listView.setPullLoadEnable(false);
        } else {
            listView.setPullLoadEnable(true);
        }
    }

    public void requestData() {
        String url = Config.WEB_API_SERVER_V3 + "/user/order/need/serve/list/" + SysUtil.getIntPref(PrefKeyConst.PREF_USER_ID) + "/2/" + pageNum;
        OkHttpClientManager.getInstance(getActivity()).getWithToken(isFirstRefresh, url, new OkHttpClientManager.ResultCallback<MyAppointExpert>() {
            @Override
            public void onResponse(MyAppointExpert result) {
                refreshData(result);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int headerCount = listView.getHeaderViewsCount();
        MyAppointExpert.OrdersEntity item = adapter.getItem(position - headerCount);
        if (item == null) {
            return;
        }
        if (!item.isRead()) {
            item.setIsRead(true);
            adapter.notifyDataSetChanged();
        }
        Intent intent = new Intent(getActivity(), ActExpertOrderDetail.class);
        intent.putExtra(IntentKey.INTENT_KEY_ORDER_ID, item.getOrderId());
        getActivity().startActivityForResult(intent, IntentKey.REQ_CODE_ORDER_DETAIL);
    }

    @Override
    public void onRefresh() {
        pageNum = 1;
        isRefresh = true;
        requestData();
    }

    @Override
    public void onLoadMore() {
        pageNum++;
        isRefresh = false;
        isFirstRefresh = false;
        requestData();
    }
}
