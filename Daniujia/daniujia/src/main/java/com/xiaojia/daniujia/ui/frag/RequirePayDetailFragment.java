package com.xiaojia.daniujia.ui.frag;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.utils.LogUtil;

/**
 * Created by Administrator on 2017/1/5.
 */
public class RequirePayDetailFragment extends BaseFragment implements View.OnClickListener {
    private TextView mTvPrepay;
    private TextView mTvLogo;
    private TextView mTvPreTitle;
    private CheckBox mCbInvoice;

    @Override
    protected int getInflateLayout() {
        return R.layout.frag_require_pay_detail;
    }

    @Override
    protected void setUpView(View view) {
        ((TextView)view.findViewById(R.id.id_title)).setText("支付详情");
        mTvPrepay = (TextView) view.findViewById(R.id.require_prepay);
        mCbInvoice = (CheckBox) view.findViewById(R.id.pay_detail_cb_invoice);
        mTvLogo = (TextView) view.findViewById(R.id.require_prepay_logo);
        mTvPreTitle = (TextView) view.findViewById(R.id.require_prepay_title);

        mCbInvoice.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mCbInvoice){
            LogUtil.d("zp_test","checked: " + mCbInvoice.isChecked());
        }
    }
}
