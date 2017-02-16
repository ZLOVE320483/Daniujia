package com.xiaojia.daniujia.ui.views;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.domain.resp.ExpertHomeVo;

/**
 * Created by ZLOVE on 2016/9/12.
 */
public class SelectServiceTypeWindow extends PopupWindow implements View.OnClickListener {

    private View txtConsultView;
    private TextView tvTxtConsult;
    private TextView tvTxtConsultPrice;

    private View phoneConsultView;
    private TextView tvPhoneConsult;
    private TextView tvPhoneConsultPrice;

    private View offlineConsultView;
    private TextView tvOfflineConsult;
    private TextView tvOfflineConsultPrice;

    private Button btnConfirm;

    private View contentView;
    private ExpertHomeVo.ServiceInfo serviceInfo;
    private SelectServiceTypeListener listener;

    public SelectServiceTypeWindow(View view, ExpertHomeVo.ServiceInfo serviceInfo, SelectServiceTypeListener listener) {
        super(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.contentView = view;
        this.serviceInfo = serviceInfo;
        this.listener = listener;
    }

    public void initView() {
        txtConsultView = contentView.findViewById(R.id.txt_consult_container);
        tvTxtConsult = (TextView) contentView.findViewById(R.id.txt_consult);
        tvTxtConsultPrice = (TextView) contentView.findViewById(R.id.txt_consult_price);

        phoneConsultView = contentView.findViewById(R.id.phone_consult_container);
        tvPhoneConsult = (TextView) contentView.findViewById(R.id.phone_consult);
        tvPhoneConsultPrice = (TextView) contentView.findViewById(R.id.phone_consult_price);

        offlineConsultView = contentView.findViewById(R.id.offline_consult_container);
        tvOfflineConsult = (TextView) contentView.findViewById(R.id.offline_consult);
        tvOfflineConsultPrice = (TextView) contentView.findViewById(R.id.offline_consult_price);

        btnConfirm = (Button) contentView.findViewById(R.id.confirm);

        txtConsultView.setOnClickListener(this);
        phoneConsultView.setOnClickListener(this);
        offlineConsultView.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
    }

    public void initData() {
        if (serviceInfo.getPhoto() == null || !serviceInfo.getPhoto().isEnabled()) {
            txtConsultView.setVisibility(View.GONE);
        } else {
            tvTxtConsultPrice.setText("￥" + serviceInfo.getPhoto().getPrice() + "元/次");
        }

        if (serviceInfo.getPhone() == null || !serviceInfo.getPhone().isEnabled()) {
            phoneConsultView.setVisibility(View.GONE);
        } else {
            tvPhoneConsultPrice.setText("￥" + serviceInfo.getPhone().getPrice() + "元/分钟");
        }

        if (serviceInfo.getChat() == null || !serviceInfo.getChat().isEnabled()) {
            offlineConsultView.setVisibility(View.GONE);
        } else {
            tvOfflineConsultPrice.setText("￥" + serviceInfo.getChat().getPrice() + "元/小时");
        }
    }

    @Override
    public void onClick(View v) {
        if (v == txtConsultView) {
            if (listener != null) {
                listener.selectServiceType(1);
            }
        } else if (v == phoneConsultView) {
            if (listener != null) {
                listener.selectServiceType(2);
            }
        } else if (v == offlineConsultView) {
            if (listener != null) {
                listener.selectServiceType(3);
            }
        } else if (v == btnConfirm) {

        }
        dismiss();
    }

    public interface SelectServiceTypeListener {
        void selectServiceType(int serviceType);
    }
}
