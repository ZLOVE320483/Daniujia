package com.xiaojia.daniujia.ui.frag;

import android.content.Intent;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.picasso.Picasso;
import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.IntentKey;
import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.dlgs.AlertBigMsgDlg;
import com.xiaojia.daniujia.dlgs.AlertMsgDlg;
import com.xiaojia.daniujia.dlgs.BaseDialog;
import com.xiaojia.daniujia.dlgs.CommonTipDlg;
import com.xiaojia.daniujia.managers.OkHttpClientManager;
import com.xiaojia.daniujia.ui.act.ActExpertOrderDetail;
import com.xiaojia.daniujia.ui.act.ActUserOrderDetail;
import com.xiaojia.daniujia.ui.views.imageview.RoundedImageView;
import com.xiaojia.daniujia.utils.EditUtils;
import com.xiaojia.daniujia.utils.ScreenUtils;
import com.xiaojia.daniujia.utils.SysUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/4/27
 */
public class OfflineOrderDetailFragment extends BaseFragment implements View.OnClickListener, TextWatcher {

    private String avatarUrl;
    private String expertName;
    private String expertCorp;
    private String perPrice;
    private int functionId;
    private EditText questionDesc;
    private EditText person_desc;

    private RoundedImageView ivAvatar;
    private TextView tvName;
    private TextView tvPositionCorp;
    private TextView tvPerPrice;
    private TextView tvTotalPrice;

    private Button add;
    private Button reduce;
    private TextView time_count;

    private RelativeLayout calculateBroad;
    private String questionContent;
    private String accountContent;
    private String indicatorContent;
    private CommonTipDlg commonTipDlg = null;

    @Override
    protected int getInflateLayout() {
        return R.layout.offline_order_detail_fg;
    }

    @Override
    protected void setUpView(View view) {

        Intent intent = getActivity().getIntent();
        if (intent != null) {
            if (intent.hasExtra(IntentKey.INTENT_KEY_EXPERT_AVATAR)) {
                avatarUrl = intent.getStringExtra(IntentKey.INTENT_KEY_EXPERT_AVATAR);
            }
            if (intent.hasExtra(IntentKey.INTENT_KEY_EXPERT_NAME)) {
                expertName = intent.getStringExtra(IntentKey.INTENT_KEY_EXPERT_NAME);
            }
            if (intent.hasExtra(IntentKey.INTENT_KEY_EXPERT_CORP)) {
                expertCorp = intent.getStringExtra(IntentKey.INTENT_KEY_EXPERT_CORP);
            }
            if (intent.hasExtra(IntentKey.INTENT_KEY_EXPERT_PER_PRICE)) {
                perPrice = intent.getStringExtra(IntentKey.INTENT_KEY_EXPERT_PER_PRICE);
            }
            if (intent.hasExtra(IntentKey.INTENT_KEY_EXPERT_PRODUCT_ID)) {
                functionId = intent.getIntExtra(IntentKey.INTENT_KEY_EXPERT_PRODUCT_ID, 0);
            }
        }
        setBackButton(view.findViewById(R.id.iv_title_back));
        TextView title = (TextView) view.findViewById(R.id.tv_title);
        title.setText(R.string.offline_consult);

        questionDesc = (EditText) view.findViewById(R.id.question_desc);
        person_desc = (EditText) view.findViewById(R.id.person_edit);

        TextView quesNum = (TextView) view.findViewById(R.id.input_num);
        TextView perNum = (TextView) view.findViewById(R.id.person_input_num);

        EditUtils utils = new EditUtils(0);
        utils.init(questionDesc, quesNum);
        utils.init(person_desc, perNum);

        view.findViewById(R.id.refund).setOnClickListener(this);
        view.findViewById(R.id.question_example).setOnClickListener(this);
        view.findViewById(R.id.account_example).setOnClickListener(this);
        view.findViewById(R.id.confirm).setOnClickListener(this);

        calculateBroad = (RelativeLayout) view.findViewById(R.id.calculate_board);

        time_count = (TextView) view.findViewById(R.id.time_count);
        add = (Button) view.findViewById(R.id.add);
        add.setOnClickListener(this);
        reduce = (Button) view.findViewById(R.id.reduce);
        reduce.setOnClickListener(this);
        time_count.addTextChangedListener(this);

        ivAvatar = (RoundedImageView) view.findViewById(R.id.expert_header);
        tvName = (TextView) view.findViewById(R.id.expert_name);
        tvPositionCorp = (TextView) view.findViewById(R.id.position_corp);
        tvPerPrice = (TextView) view.findViewById(R.id.per_price);
        tvTotalPrice = (TextView) view.findViewById(R.id.total_price);

        if (!TextUtils.isEmpty(avatarUrl)) {
            Picasso.with(getActivity()).load(avatarUrl).resize(ScreenUtils.dip2px(40), ScreenUtils.dip2px(40)).into(ivAvatar);
        }
        tvName.setText(expertName);
        tvPositionCorp.setText(expertCorp);
        tvPerPrice.setText(perPrice + "元/小时");
        tvTotalPrice.setText(SysUtil.formatFloatNum(Float.parseFloat(perPrice)));

        ImageView titleMore = (ImageView) view.findViewById(R.id.iv_title_more);
        titleMore.setOnClickListener(this);
        getProfile();
        getQuestionContent();
        getAccountContent();
        getIndicatorContent();
    }

    private void getIndicatorContent() {
        String url = Config.WEB_API_SERVER_V3 + "/app/tip/buy_offline_tip";
        OkHttpClientManager.getInstance(getActivity()).getWithPlatform(url, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onResponse(String result) {
                if (TextUtils.isEmpty(result)) {
                    return;
                }
                result = result.replace("\\n", "n");
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    indicatorContent = jsonObject.getString("tipContent");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showAccountExample() {
        AlertMsgDlg alertMsgDlg = new AlertMsgDlg(getActivity());
        alertMsgDlg.initDlg(R.drawable.introduce,"个人简介", accountContent);
        alertMsgDlg.show();
    }

    private void getQuestionContent() {
        String url = Config.WEB_API_SERVER_V3 + "/app/tip/order_ques_demo";
        OkHttpClientManager.getInstance(getActivity()).getWithPlatform(false,url, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onResponse(String result) {
                if (TextUtils.isEmpty(result)) {
                    return;
                }
                result = result.replace("\\n", "n");
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    questionContent= jsonObject.getString("tipContent");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getProfile() {
        String url = Config.WEB_API_SERVER_V3 + "/user/order/place/" + SysUtil.getIntPref(PrefKeyConst.PREF_USER_ID);
        OkHttpClientManager.getInstance(getActivity()).getWithToken(false,url, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onResponse(String result) {
                String profile;
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    profile = jsonObject.getString("profile");
                    person_desc.setText(Html.fromHtml(profile));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_title_more:
                AlertBigMsgDlg dlg = new AlertBigMsgDlg(getActivity());
                dlg.initDlg(R.drawable.ic_question_blue,"线下约谈",indicatorContent);
                dlg.show();
                break;
            case R.id.question_example:
                showQuestionExample();
                break;
            case R.id.account_example:
                showAccountExample();
                break;
            case R.id.confirm:
                requestOrder();
                break;
            case R.id.add:
                int timeNum = Integer.parseInt(time_count.getText().toString());
                time_count.setText(timeNum + 1 + "");
                break;
            case R.id.reduce:
                int timeNum2 = Integer.parseInt(time_count.getText().toString());
                if (timeNum2 == 1) {
                    return;
                }
                time_count.setText(timeNum2 - 1 + "");
                break;
            case R.id.refund:
                AlertMsgDlg alertMsgDlg = new AlertMsgDlg(getActivity());
                alertMsgDlg.initDlg(R.drawable.reward_answers_note,"不满意退款",getResources().getString(R.string.refund_tip));
                alertMsgDlg.show();
                break;
        }
    }

    private void getAccountContent() {
        String url = Config.WEB_API_SERVER_V3 + "/app/tip/order_profile_demo";
        OkHttpClientManager.getInstance(getActivity()).getWithPlatform(false,url, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onResponse(String result) {
                if (TextUtils.isEmpty(result)) {
                    return;
                }
                result = result.replace("\\n", "n");
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    accountContent = jsonObject.getString("tipContent");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showQuestionExample() {
        AlertMsgDlg alertMsgDlg = new AlertMsgDlg(getActivity());
        alertMsgDlg.initDlg(R.drawable.description,"问题描述", questionContent);
        alertMsgDlg.show();
    }

    private void requestOrder() {
        int orderTime = Integer.parseInt(time_count.getText().toString());
        if (TextUtils.isEmpty(questionDesc.getText().toString())) {
            showShortToast("请输入问题详情");
            return;
        }
        if (TextUtils.isEmpty(person_desc.getText().toString())) {
            showShortToast("请输入个人信息");
            return;
        }
        String url = Config.WEB_API_SERVER + "/user/order/place";
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("userId", String.valueOf(SysUtil.getIntPref(PrefKeyConst.PREF_USER_ID)));
        builder.add("productId", functionId+"");
        builder.add("serviceCnt", orderTime+"");
        builder.add("quesDesc", questionDesc.getText().toString());
        builder.add("profile", person_desc.getText().toString());
        OkHttpClientManager.getInstance(getActivity()).postWithToken(url, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onResponse(String result) {
                final int orderId;
                try {
                    JSONObject object = new JSONObject(result);
                    if (object != null) {
                        if (object.getInt("canPlace") == 1) {
                            orderId = object.getInt("orderId");
                            Intent intent = new Intent();
                            intent.putExtra(IntentKey.INTENT_KEY_ORDER_ID, orderId);
                            finishActivity(intent);
                        } else {
                            final int flag = object.getInt("flag");
                            orderId = object.getInt("orderId");
                            if (commonTipDlg == null){
                                commonTipDlg = new CommonTipDlg(getActivity(),"提示", object.getString("errmsg"),
                                        "确定","查看约谈");
                                commonTipDlg.setOnClickListener(new BaseDialog.DialogClickListener(){
                                    @Override
                                    public void onOK() {
                                        commonTipDlg.dismiss();
                                    }

                                    @Override
                                    public void onCancel() {
                                        Intent intent;
                                        if (flag == 0) {
                                            intent = new Intent(getActivity(), ActUserOrderDetail.class);
                                        } else {
                                            intent = new Intent(getActivity(), ActExpertOrderDetail.class);
                                        }
                                        intent.putExtra(IntentKey.INTENT_KEY_ORDER_ID, orderId);
                                        getActivity().startActivity(intent);
                                        commonTipDlg.dismiss();
                                    }
                                });
                            }
                            commonTipDlg.showDialog();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                try {
//                    JSONObject json = new JSONObject(result);
//                    int orderId = json.getInt("orderId");
//                    Intent intent = new Intent();
//                    intent.putExtra(IntentKey.INTENT_KEY_ORDER_ID, orderId);
//                    finishActivity(intent);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }
        },builder);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        int timeCount = Integer.parseInt(s.toString());
        if (timeCount == 1) {
            calculateBroad.setEnabled(true);
        }else{
            calculateBroad.setEnabled(false);
        }
        if (!TextUtils.isEmpty(perPrice)) {
            tvTotalPrice.setText(SysUtil.formatFloatNum(Float.parseFloat(perPrice) * timeCount));
        }
    }

}
