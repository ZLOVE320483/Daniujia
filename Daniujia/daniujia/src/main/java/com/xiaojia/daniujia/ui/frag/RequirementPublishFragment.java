package com.xiaojia.daniujia.ui.frag;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaojia.daniujia.ExtraConst;
import com.xiaojia.daniujia.InsideMsg;
import com.xiaojia.daniujia.IntentKey;
import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.dlgs.BaseBottomDialog;
import com.xiaojia.daniujia.dlgs.BaseDialog;
import com.xiaojia.daniujia.dlgs.CommonBottomDlg;
import com.xiaojia.daniujia.dlgs.CommonTipDlg;
import com.xiaojia.daniujia.dlgs.YearToDayPickerBottomDlg;
import com.xiaojia.daniujia.domain.PublishRequireData;
import com.xiaojia.daniujia.domain.resp.PublishRequireVo;
import com.xiaojia.daniujia.msg.MsgHelper;
import com.xiaojia.daniujia.ui.act.ActPublishInstruction;
import com.xiaojia.daniujia.ui.act.ActSelectCity;
import com.xiaojia.daniujia.ui.act.ActUserRequirement;
import com.xiaojia.daniujia.ui.control.RequirementPublishControl;
import com.xiaojia.daniujia.ui.views.toast.ToastCompat;
import com.xiaojia.daniujia.utils.LogUtil;
import com.xiaojia.daniujia.utils.SysUtil;
import com.xiaojia.daniujia.utils.TimeUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/1/3.
 */
public class RequirementPublishFragment extends BaseFragment implements View.OnClickListener, View.OnTouchListener {
    private final String NORMAL = "normal";
    private final String CHANGE = "change";
    private String type = NORMAL;

    private RequirementPublishControl control;
    private TextView mTvPublishInstruction;
    private EditText mEtChooseCity;
    private EditText mEtAdviserName;
    private EditText mEtAdviserBudget;
    private EditText mEtDescription;
    private EditText mEtServerInstruction;
    private RelativeLayout mRlServerTime;
    private EditText mEtPublishName;
    private EditText mEtDate;
    private TextView mTvServerTime;
    private Button mBtConfirm;
    private LinearLayout mLlTip;
    private ImageView mIvDeleteTip;

    private String curCityCode;
    private String cityName;
    private YearToDayPickerBottomDlg dlg;
    private CommonBottomDlg commonDlg;

    private PublishRequireData changeData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        control = new RequirementPublishControl();
        setControl(control);

        Intent intent = getActivity().getIntent();
        if (intent != null) {
            if (intent.hasExtra(IntentKey.INTENT_VALUE_CHANGE_REQUIRE)) {
                type = CHANGE;
                changeData = (PublishRequireData) intent.getSerializableExtra(IntentKey.INTENT_VALUE_CHANGE_REQUIRE);
            }
        }
    }

    @Override
    protected int getInflateLayout() {
        return R.layout.fragment_requirement_publish;
    }

    @Override
    protected void setUpView(View view) {
        setBackButton(view.findViewById(R.id.iv_title_back));
        mTvPublishInstruction = (TextView) view.findViewById(R.id.requirement_publish);
        mEtChooseCity = (EditText) view.findViewById(R.id.choose_city);
        mEtAdviserName = (EditText) view.findViewById(R.id.adviser_name);
        mEtAdviserBudget = (EditText) view.findViewById(R.id.adviser_budget);
        mEtDescription = (EditText) view.findViewById(R.id.requirement_desc);
        mEtServerInstruction = (EditText) view.findViewById(R.id.be_present_instruction);
        mRlServerTime = (RelativeLayout) view.findViewById(R.id.server_time);
        mEtPublishName = (EditText) view.findViewById(R.id.publish_name);
        mEtDate = (EditText) view.findViewById(R.id.publish_date);
        mTvServerTime = (TextView) view.findViewById(R.id.server_time_tv);
        mBtConfirm = (Button) view.findViewById(R.id.confirm);
        mLlTip = (LinearLayout) view.findViewById(R.id.require_ll_tip);
        mIvDeleteTip = (ImageView) view.findViewById(R.id.require_delete_tip);

        mTvPublishInstruction.setOnClickListener(this);
        mEtChooseCity.setOnTouchListener(this);
        // 禁止键盘输入
        mEtChooseCity.setKeyListener(null);
        mEtDate.setOnTouchListener(this);
        mEtDate.setKeyListener(null);

        mBtConfirm.setOnClickListener(this);
        mRlServerTime.setOnClickListener(this);

        if (type.equals(CHANGE) && changeData != null) {
            mBtConfirm.setText("保 存");
            mEtAdviserBudget.setText(changeData.getBudget());
            mEtChooseCity.setText(changeData.getCityName());
            mEtAdviserName.setText(changeData.getConsultantName());
            mEtDescription.setText(changeData.getDesc());
            mTvServerTime.setText(changeData.getServiceCycle());
            mEtServerInstruction.setText(changeData.getServiceDay());
            mEtPublishName.setText(changeData.getWriteName());
            mEtDate.setText(TimeUtils.getTime((changeData.getDeadline() * 1000), TimeUtils.DATE_FORMAT_DATE));
            curCityCode = changeData.getCityCode();
        }

        if (SysUtil.getBooleanPref(PrefKeyConst.PC_TIP_HIDDEN)){
            mLlTip.setVisibility(View.GONE);
        } else {
            mIvDeleteTip.setOnClickListener(this);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                if (v == mEtChooseCity) {
                    Intent intent = new Intent(getActivity(), ActSelectCity.class);
                    intent.putExtra(IntentKey.INTENT_KEY_CUR_CITY_CODE, curCityCode);
                    startActivityForResult(intent, IntentKey.REQ_CODE_SELECT_CITY);

                } else if (v == mEtDate) {
                    if (TextUtils.isEmpty(mEtDate.getText().toString())) {
                        if (dlg == null) {
                            dlg = new YearToDayPickerBottomDlg(getActivity(), String.valueOf(TimeUtils.getYear(TimeUtils.getCurrentTimeInString(TimeUtils.DATE_FORMAT_DATE))),
                                    String.valueOf(TimeUtils.getMonth(TimeUtils.getCurrentTimeInString(TimeUtils.DATE_FORMAT_DATE))),
                                    String.valueOf(TimeUtils.getDay(TimeUtils.getCurrentTimeInString(TimeUtils.DATE_FORMAT_DATE))));
                            dlg.setOnDialogClickListener(onDialogClickListener);
                        }
                    } else {
                        String time = mEtDate.getText().toString().trim();
                        String[] date = new String[3];
                        try {
                            date = time.split("-");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        for (int i = 0; i < date.length; i++) {
                            LogUtil.d("zp_test","date: " + date[i]);
                        }

                        if (dlg == null){
                            dlg = new YearToDayPickerBottomDlg(getActivity(), date[0], date[1], date[2]);
                            dlg.setOnDialogClickListener(onDialogClickListener);
                        } else {
                            if (!TextUtils.equals(mEtDate.getText().toString().trim(), dlg.getTimeString())) {
                                dlg = new YearToDayPickerBottomDlg(getActivity(), date[0], date[1], date[2]);
                                dlg.setOnDialogClickListener(onDialogClickListener);
                            }
                        }

                    }

                    dlg.show();
                }
                return true;
        }

        return false;
    }

    private ArrayList<String> times = new ArrayList<>();

    @Override
    public void onClick(View v) {
        if (v == mTvPublishInstruction) {
            startActivity(new Intent(getActivity(), ActPublishInstruction.class));
        } else if (v == mRlServerTime) {
            times.add("1个月内");
            times.add("1-3个月");
            times.add("3-6个月");
            times.add("6个月-1年");
            times.add("1年以上");
            if (commonDlg == null) {
                commonDlg = new CommonBottomDlg(getActivity(), times);
                commonDlg.setOnDialogClickListener(onDialogClickListener);
            }
            commonDlg.show();

        } else if (v == mBtConfirm) {
            submit();
        } else if (v == mIvDeleteTip){
            SysUtil.savePref(PrefKeyConst.PC_TIP_HIDDEN,true);
            mLlTip.setVisibility(View.GONE);
        }
    }

    private void submit() {
        if (TextUtils.isEmpty(mEtAdviserName.getText().toString().trim())) {
            showShortToast("请填写顾问名称");
            return;
        }

        if (TextUtils.isEmpty(mEtAdviserBudget.getText().toString().trim())) {
            showShortToast("请填写顾问费用预算");
            return;
        }

        if (TextUtils.isEmpty(mEtDescription.getText().toString().trim())) {
            showShortToast("请填写需求描述");
            return;
        }

        if (TextUtils.isEmpty(mEtServerInstruction.getText().toString().trim())) {
            showShortToast("请填写到现场服务说明");
            return;
        }

        if (TextUtils.isEmpty(mTvServerTime.getText().toString().trim())) {
            showShortToast("请填写服务期");
            return;
        }

        if (TextUtils.isEmpty(mEtPublishName.getText().toString().trim())) {
            showShortToast("请填写发布者名称");
            return;
        }

        if (TextUtils.isEmpty(mEtChooseCity.getText().toString().trim())) {
            showShortToast("请填写所在城市");
            return;
        }

        if (TextUtils.isEmpty(mEtDate.getText().toString().trim())) {
            showShortToast("请填写需求截止日期");
            return;
        }

        PublishRequireData data = new PublishRequireData();
        data.setBudget(mEtAdviserBudget.getText().toString().trim());
        data.setCityCode(curCityCode);
        data.setConsultantName(mEtAdviserName.getText().toString().trim());
        data.setDeadline(TimeUtils.date2TimeStamp(mEtDate.getText().toString().trim(), TimeUtils.DATE_FORMAT_DATE));
        data.setDesc(mEtDescription.getText().toString().trim());
        data.setServiceCycle(mTvServerTime.getText().toString().trim());
        data.setServiceDay(mEtServerInstruction.getText().toString().trim());
        data.setWriteName(mEtPublishName.getText().toString().trim());

        if (type.equals(NORMAL)) {
            control.initRequireData(data);
        } else if (type.equals(CHANGE)) {
            control.changeRequireData(data, changeData.getRequireId());
        }

    }

    private CommonTipDlg dlgTip;

    public void publishSuccess(final PublishRequireVo requireListVo) {
        if (type.equals(NORMAL)) {
            if (dlgTip == null){
                dlgTip = new CommonTipDlg(getActivity(),"温馨提示","已收到您的寻找顾问需求信息，我们将尽快与您联系。","知道了","");
                dlgTip.setOnClickListener(new BaseDialog.DialogClickListener(){
                    @Override
                    public void onOK() {
                        Intent intent = new Intent(getActivity(), ActUserRequirement.class);
                        intent.putExtra(IntentKey.INTENT_KEY_DEMAND_ID, requireListVo.getDemandId());
                        startActivity(intent);
                        MsgHelper.getInstance().sendMsg(InsideMsg.NOTIFY_JUMP_TO_ME);
                        finishActivity();
                    }
                });
            }
            dlgTip.showDialog();
        } else if (type.equals(CHANGE)) {
            ToastCompat.makeText(getContext(),"修改成功", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            finishActivity(intent);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentKey.REQ_CODE_SELECT_CITY && data != null) {
            if (data.hasExtra(IntentKey.INTENT_KEY_CUR_CITY_CODE)) {
                curCityCode = data.getStringExtra(IntentKey.INTENT_KEY_CUR_CITY_CODE);
            }
            if (data.hasExtra(IntentKey.INTENT_KEY_CUR_CITY_NAME)) {
                cityName = data.getStringExtra(IntentKey.INTENT_KEY_CUR_CITY_NAME);
                mEtChooseCity.setText(cityName);
            }
        }
    }

    private BaseBottomDialog.OnDialogClickListener onDialogClickListener = new BaseBottomDialog.OnDialogClickListener() {
        @Override
        public void onOK(Bundle bundle) {
            super.onOK(bundle);
            String[] yearToDay = bundle.getStringArray(ExtraConst.EXTRA_DATE);
            if (yearToDay != null) {
                if (yearToDay.length == 3) {
                    mEtDate.setText(yearToDay[0] + "-" + yearToDay[1] + "-" + yearToDay[2]);
                } else {
                    mEtDate.setText("");
                }

            }

        }

        @Override
        public void onOK(String s) {
            super.onOK(s);
            int index = 0;
            try {
                index = Integer.parseInt(s);
            } catch (Exception e) {

            }
            mTvServerTime.setText(times.get(index));
            commonDlg.dismiss();
        }
    };

}
