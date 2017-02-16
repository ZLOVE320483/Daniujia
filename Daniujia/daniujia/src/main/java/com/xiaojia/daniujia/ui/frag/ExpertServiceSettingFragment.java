package com.xiaojia.daniujia.ui.frag;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.xiaojia.daniujia.ExtraConst;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.dlgs.BaseDialog;
import com.xiaojia.daniujia.dlgs.ModifyPriceDlg;
import com.xiaojia.daniujia.domain.ServiceSet;
import com.xiaojia.daniujia.domain.resp.ServiceDetailVo;
import com.xiaojia.daniujia.ui.control.ExpertServiceSettingControl;
import com.xiaojia.daniujia.ui.views.CommonDialog;
import com.xiaojia.daniujia.utils.ListUtils;
import com.xiaojia.daniujia.utils.WUtil;

import java.util.List;

/**
 * Created by ZLOVE on 2016/6/28.
 */
public class ExpertServiceSettingFragment extends BaseFragment implements View.OnClickListener {
    public static final String IS_MODIFY = "isModify";
    private TextView tvSave;
    public static final int TYPE_GRAPHIC_CONSULT = 0;
    public static final int TYPE_NET_CONSULT = 1;
    public static final int TYPE_OFFLINE_CONSULT = 2;

    private static final float DEF_GRAPHIC_CONSULT_PRICE = 10;
    private static final float DEF_NET_CONSULT_PRICE = 4;
    private static final float DEF_OFFLINE_CONSULT_PRICE = 200;

    private float mGraphicPrice = DEF_GRAPHIC_CONSULT_PRICE, mNetPrice = DEF_NET_CONSULT_PRICE,
            mOfflinePrice = DEF_OFFLINE_CONSULT_PRICE;

    private ModifyPriceDlg mGraphicDlg, mNetDlg, mOfflineDlg;

    private ExpertServiceSettingControl control;
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
    private String[] prices = new String[3];
    private String[] modifyPrices = new String[3];
    private boolean[] serviceStatus = new boolean[3];
    private TextView mTvGraphicChangePrice, mTvPhoneChangePrice, mTvOffLineChangePrice;
    private TextView mTvChangeUnit1, mTvChangeUnit2, mTvChangeUnit3;
    private ImageView mIvGraphicIcon, mIvPhoneIcon, mIvOfflineIcon;

    private boolean isGraphicVerify = false;
    private boolean isPhoneVerify = false;
    private boolean isOffLineVerify = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        control = new ExpertServiceSettingControl();
        setControl(control);
    }

    @Override
    protected int getInflateLayout() {
        return R.layout.frag_expert_service_setting;
    }

    @Override
    protected void setUpView(View view) {
        setBackButton(view.findViewById(R.id.id_back));
        ((TextView) view.findViewById(R.id.id_title)).setText("服务设置");

        tvSave = (TextView) view.findViewById(R.id.title_right);
        tvSave.setText("保存");
        tvSave.setVisibility(View.VISIBLE);
        tvSave.setOnClickListener(this);

        graphic_price = (TextView) view.findViewById(R.id.graphic_price);
        net_price = (TextView) view.findViewById(R.id.net_price);
        offline_price = (TextView) view.findViewById(R.id.offline_price);

        toggle_graphic = (ToggleButton) view.findViewById(R.id.toggle_graphic);
        toggle_net = (ToggleButton) view.findViewById(R.id.toggle_net);
        toggle_offline = (ToggleButton) view.findViewById(R.id.toggle_offline);

        adjustGraphic = view.findViewById(R.id.graphic_adjust_layout);
        adjustNet = view.findViewById(R.id.net_adjust_layout);
        adjustOffline = view.findViewById(R.id.offline_adjust_layout);

        mTvGraphicChangePrice = (TextView) view.findViewById(R.id.graphic_price_now);
        mTvPhoneChangePrice = (TextView) view.findViewById(R.id.phone_price_now);
        mTvOffLineChangePrice = (TextView) view.findViewById(R.id.offline_price_now);

        mTvChangeUnit1 = (TextView) view.findViewById(R.id.unit1_now);
        mTvChangeUnit2 = (TextView) view.findViewById(R.id.unit2_now);
        mTvChangeUnit3 = (TextView) view.findViewById(R.id.unit3_now);
        mIvGraphicIcon = (ImageView) view.findViewById(R.id.service_setting_iv_graphic);
        mIvPhoneIcon = (ImageView) view.findViewById(R.id.service_setting_iv_phone);
        mIvOfflineIcon = (ImageView) view.findViewById(R.id.service_setting_iv_offline);

        adjustGraphic.setOnClickListener(this);
        adjustNet.setOnClickListener(this);
        adjustOffline.setOnClickListener(this);

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
                    serviceStatus[0] = false;
                } else {
                    toggle_graphic.setChecked(true);
                    serviceStatus[0] = true;
                }

                graphic_price.setText(serviceproductsEntity.getPrice());
                prices[0] = graphic_price.getText().toString();
                modifyPrices[0] = serviceproductsEntity.getApplyPrice();

                mGraphicPrice = Float.valueOf((modifyPrices[0] == null || modifyPrices[0].equals("")) ? serviceproductsEntity.getPrice() : modifyPrices[0]);

                if (serviceproductsEntity.getApplyPrice() != null && !serviceproductsEntity.getApplyPrice().equals("")) {
                    isHideGraphic(false);
                    mTvGraphicChangePrice.setText(serviceproductsEntity.getApplyPrice());
                    isGraphicVerify = true;
                } else {
                    isHideGraphic(true);
                }

            } else if (serviceproductsEntity.getServicetype() == 2) {
                if (serviceproductsEntity.getStatus() == 1) {
                    toggle_net.setChecked(true);
                    serviceStatus[1] = true;
                } else {
                    toggle_net.setChecked(false);
                    serviceStatus[1] = false;
                }

                net_price.setText(serviceproductsEntity.getPrice());
                prices[1] = net_price.getText().toString();
                modifyPrices[1] = serviceproductsEntity.getApplyPrice();
                mNetPrice = Float.valueOf((modifyPrices[1] == null || modifyPrices[1].equals("")) ? serviceproductsEntity.getPrice() : modifyPrices[1]);

                if (serviceproductsEntity.getApplyPrice() != null && !serviceproductsEntity.getApplyPrice().equals("")) {
                    isHidePhone(false);
                    mTvPhoneChangePrice.setText(serviceproductsEntity.getApplyPrice());
                    isPhoneVerify = true;
                } else {
                    isHidePhone(true);
                }

            } else if (serviceproductsEntity.getServicetype() == 3) {
                if (serviceproductsEntity.getStatus() == 1) {
                    toggle_offline.setChecked(true);
                    serviceStatus[2] = true;
                } else {
                    toggle_offline.setChecked(false);
                    serviceStatus[2] = false;
                }

                offline_price.setText(serviceproductsEntity.getPrice());
                prices[2] = offline_price.getText().toString();
                modifyPrices[2] = serviceproductsEntity.getApplyPrice();
                mOfflinePrice = Float.valueOf((modifyPrices[2] == null || modifyPrices[2].equals("")) ? serviceproductsEntity.getPrice() : modifyPrices[2]);

                if (serviceproductsEntity.getApplyPrice() != null && !serviceproductsEntity.getApplyPrice().equals("")) {
                    isHideOffline(false);
                    mTvOffLineChangePrice.setText(serviceproductsEntity.getApplyPrice());
                    isOffLineVerify = true;
                } else {
                    isHideOffline(true);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == tvSave) {
            if (result == null) {
                return;
            }
            final ServiceSet ss = new ServiceSet();
            ss.setMsgserviceprice((mTvGraphicChangePrice.getText() == null || mTvGraphicChangePrice.getText().toString().equals("")) ?
                    graphic_price.getText().toString() : mTvGraphicChangePrice.getText().toString());
            ss.setMsgservicestatus(toggle_graphic.isChecked() ? "1" : "0");
            ss.setTelserviceprice((mTvPhoneChangePrice.getText() == null || mTvPhoneChangePrice.getText().toString().equals("")) ?
                    net_price.getText().toString() : mTvPhoneChangePrice.getText().toString());
            ss.setTelservicestatus(toggle_net.isChecked() ? "1" : "0");
            ss.setOfflineserviceprice((mTvOffLineChangePrice.getText() == null || mTvOffLineChangePrice.getText().toString().equals("")) ?
                    offline_price.getText().toString() : mTvOffLineChangePrice.getText().toString());
            ss.setOfflineservicestatus(toggle_offline.isChecked() ? "1" : "0");

            if ((isGraphicVerify && !modifyPrices[0].equals(mTvGraphicChangePrice.getText().toString()))
                    || (isPhoneVerify && !modifyPrices[1].equals(mTvPhoneChangePrice.getText().toString()))
                    || (isOffLineVerify && !modifyPrices[2].equals(mTvOffLineChangePrice.getText().toString()))
                    || (!isGraphicVerify && !mTvGraphicChangePrice.getText().toString().equals(""))
                    || (!isPhoneVerify && !mTvPhoneChangePrice.getText().toString().equals(""))
                    || (!isOffLineVerify && !mTvOffLineChangePrice.getText().toString().equals(""))) {
                control.modifyServiceType(ss,false);
            } else if( !(serviceStatus[0] == toggle_graphic.isChecked())
                         || !(serviceStatus[1] == toggle_net.isChecked())
                         || !(serviceStatus[2] == toggle_offline.isChecked())){
                control.modifyServiceType(ss,true);
            } else {
                finishActivity();
            }

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

    private CommonDialog modifyServiceDialog = null;

    public void modifyService() {
        if (modifyServiceDialog == null) {
            modifyServiceDialog = new CommonDialog(getActivity(), new CommonDialog.ConfirmAction() {
                @Override
                public void confirm() {
                    finishActivity(new Intent());
                }

                @Override
                public void cancel() {
                }
            }, "提示", "已收到您的约谈价格修改申请，我们会尽快进行审核", true);
        }
        modifyServiceDialog.showdialog();
    }

    public void modifyServiceStatus(){
        finishActivity(new Intent());
    }

    private BaseDialog.DialogClickListener mDialogClickListener = new BaseDialog.DialogClickListener() {
        @Override
        public void onOK(Bundle bundle) {
            int type = bundle.getInt(ExtraConst.EXTRA_CONSULT_TYPE);
            String priceStr = bundle.getString(ExtraConst.EXTRA_CONSULT_PRICE);
            String decimalPriceStr = WUtil.keepTowDecimal(priceStr);
            switch (type) {
                case 0:
                    if (isGraphicVerify) {
                        // 价格审核中
                        mTvGraphicChangePrice.setText(decimalPriceStr);
                    } else {
                        if (graphic_price.getText().toString().equals(decimalPriceStr)) {
                            isHideGraphic(true);
                            mTvGraphicChangePrice.setText("");
                        } else {
                            isHideGraphic(false);
                            mTvGraphicChangePrice.setText(decimalPriceStr);
                        }
                    }
                    mGraphicPrice = Float.valueOf(priceStr);

                    break;
                case 1:
                    if (isPhoneVerify) {
                        mTvPhoneChangePrice.setText(decimalPriceStr);
                    } else {
                        if (net_price.getText().toString().equals(decimalPriceStr)) {
                            isHidePhone(true);
                            mTvPhoneChangePrice.setText("");
                        } else {
                            isHidePhone(false);
                            mTvPhoneChangePrice.setText(decimalPriceStr);
                        }
                    }
                    mNetPrice = Float.valueOf(priceStr);

                    break;
                case 2:
                    if (isOffLineVerify) {
                        mTvOffLineChangePrice.setText(decimalPriceStr);
                    } else {
                        if (offline_price.getText().toString().equals(decimalPriceStr)) {
                            isHideOffline(true);
                            mTvOffLineChangePrice.setText("");
                        } else {
                            isHideOffline(false);
                            mTvOffLineChangePrice.setText(decimalPriceStr);
                        }
                    }

                    mOfflinePrice = Float.valueOf(priceStr);
                    break;
            }
        }
    };

    private void isHideGraphic(boolean isHide) {
        mIvGraphicIcon.setVisibility(isHide ? View.GONE : View.VISIBLE);
        mTvGraphicChangePrice.setVisibility(isHide ? View.GONE : View.VISIBLE);
        mTvChangeUnit1.setVisibility(isHide ? View.GONE : View.VISIBLE);
    }

    private void isHidePhone(boolean isHide) {
        mIvPhoneIcon.setVisibility(isHide ? View.GONE : View.VISIBLE);
        mTvPhoneChangePrice.setVisibility(isHide ? View.GONE : View.VISIBLE);
        mTvChangeUnit2.setVisibility(isHide ? View.GONE : View.VISIBLE);
    }

    private void isHideOffline(boolean isHide) {
        mIvOfflineIcon.setVisibility(isHide ? View.GONE : View.VISIBLE);
        mTvOffLineChangePrice.setVisibility(isHide ? View.GONE : View.VISIBLE);
        mTvChangeUnit3.setVisibility(isHide ? View.GONE : View.VISIBLE);
    }

}
