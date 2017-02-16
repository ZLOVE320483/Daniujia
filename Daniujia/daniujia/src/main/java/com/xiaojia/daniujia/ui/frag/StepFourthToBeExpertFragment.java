package com.xiaojia.daniujia.ui.frag;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.xiaojia.daniujia.ExtraConst;
import com.xiaojia.daniujia.IntentKey;
import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.dlgs.BaseDialog;
import com.xiaojia.daniujia.dlgs.ModifyPriceDlg;
import com.xiaojia.daniujia.domain.ServiceSet;
import com.xiaojia.daniujia.domain.resp.ServiceDetailVo;
import com.xiaojia.daniujia.ui.act.ActFifthStepToBeExpert;
import com.xiaojia.daniujia.ui.control.ConsultServiceControl;
import com.xiaojia.daniujia.utils.ListUtils;
import com.xiaojia.daniujia.utils.SysUtil;
import com.xiaojia.daniujia.utils.WUtil;

import java.util.List;

/**
 * Created by Administrator on 2016/5/18.
 */
public class StepFourthToBeExpertFragment extends BaseFragment implements View.OnClickListener {

    public static final int TYPE_GRAPHIC_CONSULT = 0;
    public static final int TYPE_NET_CONSULT = 1;
    public static final int TYPE_OFFLINE_CONSULT = 2;

    private static final float DEF_GRAPHIC_CONSULT_PRICE = 10;
    private static final float DEF_NET_CONSULT_PRICE = 4;
    private static final float DEF_OFFLINE_CONSULT_PRICE = 200;

    private float mGraphicPrice = DEF_GRAPHIC_CONSULT_PRICE, mNetPrice = DEF_NET_CONSULT_PRICE,
            mOfflinePrice = DEF_OFFLINE_CONSULT_PRICE;

    private ModifyPriceDlg mGraphicDlg, mNetDlg, mOfflineDlg;

    private ConsultServiceControl control;
    private TextView graphic_price;
    private TextView net_price;
    private TextView offline_price;
    private ToggleButton toggle_graphic;
    private ToggleButton toggle_net;
    private ToggleButton toggle_offline;
    private ServiceDetailVo result;

    private View adjustGraphic;
    private View adjustNet;
    private View adjustOffline;

    private TextView tvFifth;
    private boolean isModify = false;

    @Override
    protected int getInflateLayout() {
        return R.layout.fg_step4_to_be_expert;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        control = new ConsultServiceControl();
        setControl(control);
    }

    @Override
    protected void setUpView(View view) {
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(IntentKey.INTENT_KEY_TO_BE_EXPERT_IS_MODIFY)) {
            isModify = intent.getBooleanExtra(IntentKey.INTENT_KEY_TO_BE_EXPERT_IS_MODIFY, false);
        }
        setBackButton(view.findViewById(R.id.id_back));
        TextView title = (TextView) view.findViewById(R.id.id_title);
        title.setText("服务设置");
        tvFifth = (TextView) view.findViewById(R.id.complete_tv);
        TextView next = (TextView) view.findViewById(R.id.title_right);
        next.setOnClickListener(this);
        next.setVisibility(View.VISIBLE);

        graphic_price = (TextView) view.findViewById(R.id.graphic_price);
        net_price = (TextView) view.findViewById(R.id.net_price);
        offline_price = (TextView) view.findViewById(R.id.offline_price);

        toggle_graphic = (ToggleButton) view.findViewById(R.id.toggle_graphic);
        toggle_net = (ToggleButton) view.findViewById(R.id.toggle_net);
        toggle_offline = (ToggleButton) view.findViewById(R.id.toggle_offline);

        adjustGraphic = view.findViewById(R.id.graphic_adjust_layout);
        adjustNet = view.findViewById(R.id.net_adjust_layout);
        adjustOffline = view.findViewById(R.id.offline_adjust_layout);

        adjustGraphic.setOnClickListener(this);
        adjustNet.setOnClickListener(this);
        adjustOffline.setOnClickListener(this);

        if (SysUtil.getIntPref(PrefKeyConst.PREF_USER_IDENTITY) == 2 || isModify) {
            tvFifth.setText("修改完成");
        }

        control.getServiceDetail();

    }

    public void refreshView(ServiceDetailVo result) {
        this.result = result;
        if (result == null || ListUtils.isEmpty(result.getServiceproducts())) {
            return;
        }
        List<ServiceDetailVo.ServiceproductsEntity> serviceproducts = result.getServiceproducts();
        for (int i = 0; i < serviceproducts.size(); i++) {
            ServiceDetailVo.ServiceproductsEntity serviceproductsEntity = serviceproducts.get(i);
            if (serviceproductsEntity.getServicetype() == 1) {
                if (serviceproductsEntity.getStatus() == 0) {
                    toggle_graphic.setChecked(false);
                } else {
                    toggle_graphic.setChecked(true);
                }
                mGraphicPrice = Float.valueOf(serviceproductsEntity.getPrice());
                graphic_price.setText(serviceproductsEntity.getPrice());
            } else if (serviceproductsEntity.getServicetype() == 2) {
                if (serviceproductsEntity.getStatus() == 1) {
                    toggle_net.setChecked(true);
                } else {
                    toggle_net.setChecked(false);
                }
                mNetPrice = Float.valueOf(serviceproductsEntity.getPrice());
                net_price.setText(serviceproductsEntity.getPrice());
            } else if (serviceproductsEntity.getServicetype() == 3) {
                if (serviceproductsEntity.getStatus() == 1) {
                    toggle_offline.setChecked(true);
                } else {
                    toggle_offline.setChecked(false);
                }
                mOfflinePrice = Float.valueOf(serviceproductsEntity.getPrice());
                offline_price.setText(serviceproductsEntity.getPrice());
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.title_right) {
            if (result == null) {
                return;
            }
            ServiceSet ss = new ServiceSet();
            ss.setMsgserviceprice(graphic_price.getText().toString());
            ss.setMsgservicestatus(toggle_graphic.isChecked() ? "1" : "0");
            ss.setTelserviceprice(net_price.getText().toString());
            ss.setTelservicestatus(toggle_net.isChecked() ? "1" : "0");
            ss.setOfflineserviceprice(offline_price.getText().toString());
            ss.setOfflineservicestatus(toggle_offline.isChecked() ? "1" : "0");
            control.modifyServiceType(ss);
            Intent intent = new Intent(getActivity(), ActFifthStepToBeExpert.class);
            intent.putExtra(IntentKey.INTENT_KEY_TO_BE_EXPERT_IS_MODIFY, isModify);
            startActivity(intent);
        } else if (v == adjustGraphic) {
            if (mGraphicDlg == null) {
                mGraphicDlg = new ModifyPriceDlg(getActivity(), TYPE_GRAPHIC_CONSULT, WUtil.getFloatStr(mGraphicPrice));
                mGraphicDlg.setOnClickListener(mDialogClickListener);
            }
            mGraphicDlg.show();
        } else if (v == adjustNet) {
            if (mNetDlg == null) {
                mNetDlg = new ModifyPriceDlg(getActivity(), TYPE_NET_CONSULT, WUtil.getFloatStr(mNetPrice));
                mNetDlg.setOnClickListener(mDialogClickListener);
            }
            mNetDlg.show();
        } else if (v == adjustOffline) {
            if (mOfflineDlg == null) {
                mOfflineDlg = new ModifyPriceDlg(getActivity(), TYPE_OFFLINE_CONSULT, WUtil.getFloatStr(mOfflinePrice));
                mOfflineDlg.setOnClickListener(mDialogClickListener);
            }
            mOfflineDlg.show();
        }
    }

    private BaseDialog.DialogClickListener mDialogClickListener = new BaseDialog.DialogClickListener() {
        @Override
        public void onOK(Bundle bundle) {
            int type = bundle.getInt(ExtraConst.EXTRA_CONSULT_TYPE);
            String priceStr = bundle.getString(ExtraConst.EXTRA_CONSULT_PRICE);
            switch (type) {
                case 0:
                    graphic_price.setText(priceStr);
                    mGraphicPrice = Float.valueOf(priceStr);
                    break;
                case 1:
                    net_price.setText(priceStr);
                    mNetPrice = Float.valueOf(priceStr);
                    break;
                case 2:
                    offline_price.setText(priceStr);
                    mOfflinePrice = Float.valueOf(priceStr);
                    break;
            }
        }
    };
}
