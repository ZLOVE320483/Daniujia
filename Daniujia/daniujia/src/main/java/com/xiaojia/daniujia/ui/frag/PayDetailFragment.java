package com.xiaojia.daniujia.ui.frag;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.xiaojia.daniujia.InsideMsg;
import com.xiaojia.daniujia.IntentKey;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.ctrls.AlipayController;
import com.xiaojia.daniujia.ctrls.WXPayController;
import com.xiaojia.daniujia.domain.resp.PayBalanceCouponVo;
import com.xiaojia.daniujia.domain.resp.PayOrderRespVo;
import com.xiaojia.daniujia.managers.ThreadWorker;
import com.xiaojia.daniujia.msg.MsgHelper;
import com.xiaojia.daniujia.msg.OnMsgCallback;
import com.xiaojia.daniujia.msg.annotation.OnMsg;
import com.xiaojia.daniujia.ui.act.ActCoupon;
import com.xiaojia.daniujia.ui.control.PayDetailControl;
import com.xiaojia.daniujia.utils.ListUtils;
import com.xiaojia.daniujia.utils.SysUtil;

import java.util.ArrayList;

/**
 * Created by ZLOVE on 2016/5/30.
 */
public class PayDetailFragment extends BaseFragment implements View.OnClickListener {

    private PayDetailControl control;

    private TextView tvOrderNum;
    private TextView tvServiceType;
    private TextView tvPerPrice;
    private TextView tvServiceUnit;
    private TextView tvPerUnit;
    private TextView tvTime;
    private TextView tvTotalPrice;
    private View balanceView;
    private TextView tvBalance;
    private View noCouponView;
    private View couponView;
    private TextView tvCouponPrice;

    private View thirdPayContainer;
    private TextView tvNeedPrice;
    private View aliPayView;
    private RadioButton rbAliPay;
    private View weChatPayView;
    private RadioButton rbWeChatPay;
    private Button btnConfirmPay;

    private int orderId;
    private String orderNum;
    private String expertName;
    private int serviceType;
    private String perPrice;
    private int time;
    private String totalPrice;
    private ArrayList<PayBalanceCouponVo.CouponEntity> coupons = new ArrayList<>();
    private int couponPrice = 0;

    private float balancePrice = 0.0f;
    private float sumPrice = 0.0f;
    private int couponId = 0;
    private int payMethod = 1;
    private AlipayController mAlipayCtrl;
    private WXPayController mWxpayCtrl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        control = new PayDetailControl();
        setControl(control);
        MsgHelper.getInstance().registMsg(this);
    }

    @Override
    protected int getInflateLayout() {
        return R.layout.frag_pay_detail;
    }

    @Override
    protected void setUpView(View view) {
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            if (intent.hasExtra(IntentKey.INTENT_KEY_ORDER_ID)) {
                orderId = intent.getIntExtra(IntentKey.INTENT_KEY_ORDER_ID, 0);
            }
            if (intent.hasExtra(IntentKey.INTENT_KEY_ORDER_NUM)) {
                orderNum = intent.getStringExtra(IntentKey.INTENT_KEY_ORDER_NUM);
            }
            if (intent.hasExtra(IntentKey.INTENT_KEY_EXPERT_NAME)) {
                expertName = intent.getStringExtra(IntentKey.INTENT_KEY_EXPERT_NAME);
            }
            if (intent.hasExtra(IntentKey.INTENT_KEY_ORDER_SERVICE_TYPE)) {
                serviceType = intent.getIntExtra(IntentKey.INTENT_KEY_ORDER_SERVICE_TYPE, 1);
            }
            if (intent.hasExtra(IntentKey.INTENT_KEY_PER_PRICE)) {
                perPrice = intent.getStringExtra(IntentKey.INTENT_KEY_PER_PRICE);
            }
            if (intent.hasExtra(IntentKey.INTENT_KEY_ORDER_TIME)) {
                time = intent.getIntExtra(IntentKey.INTENT_KEY_ORDER_TIME, 0);
            }
            if (intent.hasExtra(IntentKey.INTENT_KEY_ORDER_TOTAL_PRICE)) {
                totalPrice = intent.getStringExtra(IntentKey.INTENT_KEY_ORDER_TOTAL_PRICE);
                sumPrice = Float.valueOf(totalPrice);
            }
        }

        setBackButton(view.findViewById(R.id.id_back));
        ((TextView) view.findViewById(R.id.id_title)).setText("支付详情");
        tvOrderNum = (TextView) view.findViewById(R.id.order_num);
        tvServiceType = (TextView) view.findViewById(R.id.service);
        tvPerPrice = (TextView) view.findViewById(R.id.price);
        tvServiceUnit = (TextView) view.findViewById(R.id.service_unit);
        tvPerUnit = (TextView) view.findViewById(R.id.unit);
        tvTime = (TextView) view.findViewById(R.id.time);
        tvTotalPrice = (TextView) view.findViewById(R.id.total_price);
        balanceView = view.findViewById(R.id.balance_container);
        tvBalance = (TextView) view.findViewById(R.id.balance_price);
        noCouponView = view.findViewById(R.id.no_coupon_container);
        couponView = view.findViewById(R.id.coupon_container);
        tvCouponPrice = (TextView) view.findViewById(R.id.coupon_price);
        thirdPayContainer = view.findViewById(R.id.third_pay_container);
        tvNeedPrice = (TextView) view.findViewById(R.id.need_price);
        aliPayView = view.findViewById(R.id.alipay_layout);
        rbAliPay = (RadioButton) view.findViewById(R.id.alipay_rb);
        weChatPayView = view.findViewById(R.id.wxpay_layout);
        rbWeChatPay = (RadioButton) view.findViewById(R.id.wxpay_rb);
        btnConfirmPay = (Button) view.findViewById(R.id.btn_pay_confirm);

        couponView.setOnClickListener(this);
        aliPayView.setOnClickListener(this);
        weChatPayView.setOnClickListener(this);
        btnConfirmPay.setOnClickListener(this);
        rbAliPay.setChecked(true);
        rbWeChatPay.setChecked(false);

        tvOrderNum.setText(orderNum);
        if (serviceType == 1) {
            tvServiceUnit.setText("次数");
            tvServiceType.setText(expertName + "的图文咨询");
            tvPerUnit.setText(" 元/次");
            tvTime.setText(time + "次");
        } else if (serviceType == 2) {
            tvServiceUnit.setText("时长");
            tvServiceType.setText(expertName + "的电话咨询");
            tvPerUnit.setText(" 元/分钟");
            tvTime.setText(time + "分钟");
        } else if (serviceType == 3) {
            tvServiceUnit.setText("时长");
            tvServiceType.setText(expertName + "的线下咨询");
            tvPerUnit.setText(" 元/小时");
            tvTime.setText(time + "小时");
        }
        tvPerPrice.setText(perPrice);
        tvTotalPrice.setText(totalPrice);

        control.getBalanceCouponRequest();
    }

    public void setData(PayBalanceCouponVo payBalanceCouponVo) {
        if (payBalanceCouponVo == null) {
            return;
        }
        balancePrice = Float.valueOf(payBalanceCouponVo.getBalance());
        tvBalance.setText(payBalanceCouponVo.getBalance());

        if (ListUtils.isEmpty(payBalanceCouponVo.getCoupons())) {
            noCouponView.setVisibility(View.VISIBLE);
            couponView.setVisibility(View.GONE);
        } else {
            coupons.addAll(payBalanceCouponVo.getCoupons());
            noCouponView.setVisibility(View.GONE);
            couponView.setVisibility(View.VISIBLE);
            couponPrice = coupons.get(0).getValue();
            couponId = coupons.get(0).getCouponId();
            tvCouponPrice.setText(String.valueOf(couponPrice));
        }
        if (couponPrice >= sumPrice) {
            balanceView.setVisibility(View.GONE);
            tvNeedPrice.setText("0.0");
        } else if ((couponPrice + balancePrice) >= sumPrice) {
            balanceView.setVisibility(View.VISIBLE);
            tvNeedPrice.setText("0.0");
        } else {
            payMethod = 2;
            balanceView.setVisibility(View.VISIBLE);
            thirdPayContainer.setVisibility(View.VISIBLE);
            float needPrice = sumPrice - balancePrice - couponPrice;
            tvNeedPrice.setText(SysUtil.formatFloatNum(needPrice));
        }
    }

    @Override
    public void onClick(View v) {
        if (v == couponView) {
            Intent intent = new Intent(getActivity(), ActCoupon.class);
            intent.putExtra(IntentKey.INTENT_KEY_FROM_PAY, true);
            intent.putExtra(IntentKey.INTENT_KEY_USER_COUPONS, coupons);
            startActivityForResult(intent, IntentKey.REQ_CODE_SELECT_COUPON);
        } else if (v == aliPayView) {
            payMethod = 2;
            rbAliPay.setChecked(true);
            rbWeChatPay.setChecked(false);
        } else if (v == weChatPayView) {
            payMethod = 3;
            rbAliPay.setChecked(false);
            rbWeChatPay.setChecked(true);
        } else if (v == btnConfirmPay) {
            Log.d("ZLOVE", "couponId---111---" + couponId);
            control.createPayOrderRequest(payMethod, orderId, balancePrice, couponId);
        }
    }

    public void setCreateOrderResp(final PayOrderRespVo resp) {
        if (resp == null) {
            return;
        }
        if (payMethod == 1) {
            showShortToast(R.string.pay_success);
            paySuccess();
        } else if (payMethod == 2) {
           mAlipayCtrl = new AlipayController(getActivity());
            ThreadWorker.execute(new Runnable() {
                @Override
                public void run() {
                    mAlipayCtrl.pay(resp.getQueryString());
                }
            });
        } else if (payMethod == 3) {
            IWXAPI wxApi = WXAPIFactory.createWXAPI(getActivity(), null);
            mWxpayCtrl = new WXPayController(getActivity());
            if (wxApi.isWXAppInstalled() && wxApi.isWXAppSupportAPI()){
                ThreadWorker.execute(new Runnable() {
                    @Override
                    public void run() {
                        mWxpayCtrl.pay(resp);
                    }
                });
            }else{
                showShortToast("请先安装微信");
            }
        }
    }

    @OnMsg(msg = { InsideMsg.PAY_ALI_SUCCESS, InsideMsg.PAY_WEIXIN_SUCCESS, InsideMsg.UI_NOTIFY_FINISH }, useLastMsg = false)
    OnMsgCallback onMsgCallback = new OnMsgCallback() {
        @Override
        public boolean handleMsg(Message msg) {
            switch (msg.what) {
                case InsideMsg.PAY_ALI_SUCCESS:
                case InsideMsg.PAY_WEIXIN_SUCCESS:
                    showShortToast(R.string.pay_success);
                    paySuccess();
                    break;
                case InsideMsg.UI_NOTIFY_FINISH:
                    finishActivity();
                    break;
            }
            return false;
        }
    };

    private void paySuccess() {
        Intent intent = new Intent();
        finishActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentKey.REQ_CODE_SELECT_COUPON) {
            if (data != null) {
                if (data.hasExtra((IntentKey.INTENT_KEY_COUPON_VALUE))) {
                    couponPrice = data.getIntExtra(IntentKey.INTENT_KEY_COUPON_VALUE, 0);
                }
                if (data.hasExtra(IntentKey.INTENT_KEY_COUPON_ID)) {
                    couponId = data.getIntExtra(IntentKey.INTENT_KEY_COUPON_ID, 0);
                }
                Log.d("ZLOVE", "couponPrice---" + couponPrice);
                Log.d("ZLOVE", "couponId---" + couponId);
                tvCouponPrice.setText(String.valueOf(couponPrice));
                if (couponPrice >= sumPrice) {
                    balanceView.setVisibility(View.GONE);
                    tvNeedPrice.setText("0.0");
                } else if ((couponPrice + balancePrice) >= sumPrice) {
                    balanceView.setVisibility(View.VISIBLE);
                    tvNeedPrice.setText("0.0");
                } else {
                    payMethod = 2;
                    thirdPayContainer.setVisibility(View.VISIBLE);
                    float needPrice = sumPrice - balancePrice - couponPrice;
                    tvNeedPrice.setText(SysUtil.formatFloatNum(needPrice));
                }
            }
        }
    }
}
