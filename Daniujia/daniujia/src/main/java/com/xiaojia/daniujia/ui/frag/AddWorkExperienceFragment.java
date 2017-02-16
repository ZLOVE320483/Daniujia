package com.xiaojia.daniujia.ui.frag;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.squareup.okhttp.FormEncodingBuilder;
import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.ExtraConst;
import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.dlgs.BaseBottomDialog;
import com.xiaojia.daniujia.dlgs.DatePickerBottomDlg;
import com.xiaojia.daniujia.managers.OkHttpClientManager;
import com.xiaojia.daniujia.utils.SysUtil;
import com.xiaojia.daniujia.utils.TimeUtils;

import java.util.Calendar;

/**
 * Created by Administrator on 2016/5/19.
 */
public class AddWorkExperienceFragment extends BaseFragment implements View.OnClickListener, View.OnFocusChangeListener {

    private TextView work_from_tv;
    private TextView work_to_tv;

    private EditText position_et;
    private EditText company_et;
    private EditText work_experience_detail;
    private DatePickerBottomDlg dlg;
    private int currentDlg;
    private boolean isUpdate;
    private Intent intent;

    private String workFrom;
    private String workTo;
    private String DEFAULT_YEAR_FROM = "1980";
    private String DEFAULT_MONTH_FROM = "01";
    private String position;
    private String company;
    private String experienceDetail = "";
    private String UP_TO_NOW = "至今";

    @Override
    protected int getInflateLayout() {
        return R.layout.fg_add_work_experience;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(ExtraConst.FROM_YEAR)) {
                isUpdate = true;
        } else {
            isUpdate = false;
        }
    }

    @Override
    protected void setUpView(View view) {

        setBackButton(view.findViewById(R.id.id_back));
        TextView title = (TextView) view.findViewById(R.id.id_title);
        title.setText("工作经历添加");
        TextView titleRight = (TextView) view.findViewById(R.id.title_right);
        titleRight.setVisibility(View.VISIBLE);
        titleRight.setText("保存");
        titleRight.setOnClickListener(this);

        view.findViewById(R.id.work_from_time).setOnClickListener(this);
        view.findViewById(R.id.work_to_time).setOnClickListener(this);

        work_from_tv = (TextView) view.findViewById(R.id.work_from_tv);
        work_to_tv = (TextView) view.findViewById(R.id.work_to_tv);

        position_et = (EditText) view.findViewById(R.id.position_et);
        position_et.setOnFocusChangeListener(this);

        company_et = (EditText) view.findViewById(R.id.company_et);
        company_et.setOnFocusChangeListener(this);

        work_experience_detail = (EditText) view.findViewById(R.id.work_experience_detail);
        work_experience_detail.setOnFocusChangeListener(this);

        if (isUpdate) {
            work_from_tv.setText(TimeUtils.getTime(intent.getLongExtra(ExtraConst.FROM_YEAR, -1) * 1000, TimeUtils.DATE_FORMAT_YEAR_MONTH_ZERO));
            if (intent.getLongExtra(ExtraConst.TO_YEAR, -1) == 0) {
                work_to_tv.setText("至今");
            }else{
                work_to_tv.setText(TimeUtils.getTime(intent.getLongExtra(ExtraConst.TO_YEAR,-1) * 1000,TimeUtils.DATE_FORMAT_YEAR_MONTH_ZERO));
            }
            position_et.setText(intent.getStringExtra(ExtraConst.EXTRA_POSITION));
            company_et.setText(intent.getStringExtra(ExtraConst.COMPANY));
            work_experience_detail.setText(intent.getStringExtra(ExtraConst.EXTRA_PROFILE));
        }
    }

    private BaseBottomDialog.OnDialogClickListener mOnDialogClickedListener = new BaseBottomDialog.OnDialogClickListener() {
        @Override
        public void onOK(Bundle b) {
            String[] time = b.getStringArray(ExtraConst.EXTRA_DATE);
            if (time == null) {
                return;
            }
            if (currentDlg == 1) {
                if (UP_TO_NOW.equals(time[1])) {
                    if (calculateTimeLegal(UP_TO_NOW, work_to_tv.getText().toString())) {
                        work_from_tv.setText(UP_TO_NOW);
                    }else{
                        showShortToast("请选择正确的开始年月");
                    }
                }else {
                    if (calculateTimeLegal(time[0] + "-" + time[1], work_to_tv.getText().toString())) {
                        work_from_tv.setText(time[0] + "-" + time[1]);
                    }else{
                        showShortToast("请选择正确的开始年月");
                    }
                }
            }else if (currentDlg == 2) {
                if (UP_TO_NOW.equals(time[1])) {
                     work_to_tv.setText(UP_TO_NOW);
                }else {
                    if (calculateTimeLegal(work_from_tv.getText().toString(), time[0] + "-" + time[1])) {
                        work_to_tv.setText(time[0] + "-" + time[1]);
                    }else{
                        showShortToast("请选择正确的离开年月");
                    }
                }
            }
        }
    };

    private boolean calculateTimeLegal(String fromTime, String toTime) {
        if (TextUtils.isEmpty(fromTime) || TextUtils.isEmpty(toTime)) {
            return true;
        }

        if (UP_TO_NOW.equals(fromTime)) {
            return false;
        }

        if (UP_TO_NOW.equals(toTime)) {
            return true;
        }
        String fromArr[] = fromTime.split("-");
        String fromYear = fromArr[0];
        String fromMonth = fromArr[1];
        String toArr[] = toTime.split("-");
        String toYear = toArr[0];
        String toMonth = toArr[1];
        if (Integer.parseInt(fromYear) > Integer.parseInt(toYear)) {
            return false;
        }else if (Integer.parseInt(fromYear) == Integer.parseInt(toYear) && Integer.parseInt(fromMonth) >= Integer.parseInt(toMonth)) {
            return false;
        }else {
            return  true;
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.title_right) {
            addWorkExperience();
        }else if(v.getId() == R.id.work_to_time) {
            currentDlg = 2;
            String workTo = work_to_tv.getText().toString().trim();
            if (!TextUtils.isEmpty(workTo) && workTo.contains("-")) {
                String[] time = workTo.split("-");
                dlg = new DatePickerBottomDlg(getActivity(), time[0], time[1]);
            }else{
                dlg = new DatePickerBottomDlg(getActivity(),getYear()+"",UP_TO_NOW);
            }
            dlg.setOnDialogClickListener(mOnDialogClickedListener);
            dlg.show();
        }else if (v.getId() == R.id.work_from_time) {
            currentDlg = 1;
            String workFrom = work_from_tv.getText().toString().trim();
            if (!TextUtils.isEmpty(workFrom) && workFrom.contains("-")) {
                String[] time = workFrom.split("-");
                dlg = new DatePickerBottomDlg(getActivity(), time[0], time[1]);
            }else if(UP_TO_NOW.equals(workFrom)) {
                dlg = new DatePickerBottomDlg(getActivity(), getYear()+"",UP_TO_NOW);
            }else{
                dlg = new DatePickerBottomDlg(getActivity(), DEFAULT_YEAR_FROM, DEFAULT_MONTH_FROM);
            }
            dlg.setOnDialogClickListener(mOnDialogClickedListener);
            dlg.show();
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        EditText et = (EditText) v;
        if (hasFocus) {
            et.setHint("");
        }else{
            if (TextUtils.isEmpty(et.getText().toString())) {
                if(et == work_experience_detail){
                    et.setHint(R.string.add_work_hint);
                }else{
                    et.setHint(R.string.not_filled);
                }
            }
        }
    }

    public int getYear() {// 当前年份
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.YEAR);
    }

    public int getMonth() {// 当前月份
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.MONTH) + 1;
    }

    public void addWorkExperience() {
        workFrom = work_from_tv.getText().toString().trim();
        workTo = work_to_tv.getText().toString().trim();
        position = position_et.getText().toString().trim();
        company = company_et.getText().toString().trim();

        if (TextUtils.isEmpty(workFrom)) {
            showShortToast("请选择开始年月");
            return;
        }
        if (TextUtils.isEmpty(workTo)) {
            showShortToast("请选择离开年月");
            return;
        }

        if (TextUtils.isEmpty(position)) {
            showShortToast("请输入职位");
            return;
        }
        if (TextUtils.isEmpty(company)) {
            showShortToast("请输入公司");
            return;
        }

        experienceDetail = work_experience_detail.getText().toString().trim();
        String url = Config.WEB_API_SERVER + "/user/career/update";
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("userId", String.valueOf(SysUtil.getIntPref(PrefKeyConst.PREF_USER_ID)));
        if (isUpdate) {
            builder.add("careerId", intent.getIntExtra(ExtraConst.CAREER_ID, -1) + "");
        } else {
            builder.add("careerId", 0 + "");
        }
        if (UP_TO_NOW.equals(workFrom)) {
            builder.add("entryDate", getYear()+"-" + getMonth());
        }else{
            builder.add("entryDate", workFrom);
        }
        if (UP_TO_NOW.equals(workTo)) {
            builder.add("quitDate", "0");
        }else{
            builder.add("quitDate", workTo);
        }
        builder.add("position", position);
        builder.add("company", company);
        builder.add("positionDesc", experienceDetail);
        OkHttpClientManager.getInstance(getActivity()).postWithToken(url, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onResponse(String result) {
                finishActivity();
            }
        }, builder);
    }
}
