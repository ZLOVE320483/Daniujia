package com.xiaojia.daniujia.ui.frag;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.xiaojia.daniujia.IntentKey;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.domain.resp.CouponVo;
import com.xiaojia.daniujia.domain.resp.GiftItem;
import com.xiaojia.daniujia.domain.resp.PayBalanceCouponVo;
import com.xiaojia.daniujia.ui.adapters.CouponAdapter;
import com.xiaojia.daniujia.ui.control.CouponControl;
import com.xiaojia.daniujia.ui.views.XListView;
import com.xiaojia.daniujia.utils.ListUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/3
 */
public class CouponFragment extends BaseFragment implements XListView.IXListViewListener, View.OnClickListener, AdapterView.OnItemClickListener {

    private CouponControl control;

    private View noDataContainer;
    private TextView tvNoDataTip;
    private XListView listView;
    private CouponAdapter adapter;
    private List<GiftItem> list = new ArrayList<>();
    private TextView tvHistory;

    private int pageNum = 1;
    private boolean isRefresh = true;
    private boolean isHistory = false;
    private boolean isFromPay = false;
    private boolean isFirstRefresh = true;
    private ArrayList<PayBalanceCouponVo.CouponEntity> coupons;

    @Override
    protected int getInflateLayout() {
        return R.layout.fg_coupon;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        control = new CouponControl();
        setControl(control);
    }

    @Override
    protected void setUpView(View view) {
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            if (intent.hasExtra(IntentKey.INTENT_KEY_FROM_PAY)) {
                isFromPay = intent.getBooleanExtra(IntentKey.INTENT_KEY_FROM_PAY, false);
            }
            if (intent.hasExtra(IntentKey.INTENT_KEY_USER_COUPONS)) {
                coupons = (ArrayList<PayBalanceCouponVo.CouponEntity>) intent.getSerializableExtra(IntentKey.INTENT_KEY_USER_COUPONS);
            }
        }

        setBackButton(view.findViewById(R.id.id_back));
        ((TextView) view.findViewById(R.id.id_title)).setText("优惠券");

        noDataContainer = view.findViewById(R.id.no_data_container);
        tvNoDataTip = (TextView) view.findViewById(R.id.no_data_tip);
        listView = (XListView) view.findViewById(R.id.id_list);
        adapter = new CouponAdapter(list, getActivity());
        listView.setAdapter(adapter);
        adapter.setIsHistory(isHistory);
        listView.setXListViewListener(this);
        listView.setPullRefreshEnable(true);
        listView.setOnItemClickListener(this);

        tvHistory = (TextView) view.findViewById(R.id.history);
        tvHistory.setOnClickListener(this);

        if (isFromPay) {
            tvHistory.setVisibility(View.GONE);
            if (!ListUtils.isEmpty(coupons)) {
                for (PayBalanceCouponVo.CouponEntity entity : coupons) {
                    GiftItem item = new GiftItem();
                    item.couponId = entity.getCouponId();
                    item.deadtime = entity.getDeadtime();
                    item.value = entity.getValue();
                    this.list.add(item);
                }
                adapter.notifyDataSetChanged();
            }
        } else {
            control.getCouponsRequest(isFirstRefresh, isHistory, pageNum);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == tvHistory) {
            pageNum = 1;
            if (isHistory) {
                isHistory = false;
                tvHistory.setText(R.string.look_up_history_coupons);
                tvNoDataTip.setText("暂时没有可用的优惠券了");
            } else {
                isHistory = true;
                tvHistory.setText(R.string.look_up_valid_coupons);
                tvNoDataTip.setText("暂时没有历史用券");
            }
            isRefresh = true;
            control.getCouponsRequest(true, isHistory, pageNum);
        }
    }

    @Override
    public void onRefresh() {
        pageNum = 1;
        isRefresh = true;
        control.getCouponsRequest(isFirstRefresh, isHistory, pageNum);
    }

    @Override
    public void onLoadMore() {
        pageNum++;
        isRefresh = false;
        control.getCouponsRequest(isFirstRefresh, isHistory, pageNum);
    }

    public void setData(CouponVo couponVo) {
        isFirstRefresh = false;
        listView.stopRefresh();
        listView.stopLoadMore();
        if (couponVo == null) {
            noDataContainer.setVisibility(View.VISIBLE);
            return;
        }
        List<GiftItem> tmpList = couponVo.gifts;

        if (isRefresh) {
            list.clear();
        }
        list.addAll(tmpList);
        if (ListUtils.isEmpty(list)) {
            noDataContainer.setVisibility(View.VISIBLE);
            return;
        }
        noDataContainer.setVisibility(View.GONE);
        adapter.setIsHistory(isHistory);
        adapter.notifyDataSetChanged();

        if (tmpList.size() < 10) {
            listView.setPullLoadEnable(false);
        } else {
            listView.setPullLoadEnable(true);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (!isFromPay) {
            return;
        }
        int headerCount = listView.getHeaderViewsCount();
        GiftItem item = list.get(position - headerCount);
        if (item == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(IntentKey.INTENT_KEY_COUPON_ID, item.couponId);
        intent.putExtra(IntentKey.INTENT_KEY_COUPON_VALUE, item.value);
        finishActivity(intent);
    }
}
