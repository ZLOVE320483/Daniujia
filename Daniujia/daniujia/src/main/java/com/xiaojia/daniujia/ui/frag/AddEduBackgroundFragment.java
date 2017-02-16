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
import com.xiaojia.daniujia.dlgs.QualificationBottomDlg;
import com.xiaojia.daniujia.domain.QualificationItem;
import com.xiaojia.daniujia.managers.OkHttpClientManager;
import com.xiaojia.daniujia.utils.SysUtil;
import com.xiaojia.daniujia.utils.TimeUtils;
import com.xiaojia.daniujia.utils.WUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Administrator on 2016/5/19.
 */
public class AddEduBackgroundFragment extends BaseFragment implements View.OnClickListener {

    private String schoolFrom = "2002-09";
    private String schoolTo = "2006-07";
    private String school;
    private String degree;

    private TextView school_from_tv;
    private TextView school_to_tv;
    private EditText school_name;
    private TextView qualification_tv;
    private int currentDlg;
    private DatePickerBottomDlg mDateDlg;
    private QualificationBottomDlg mQualification;
    private Intent intent;
    private boolean isUpdate;
    private String UP_TO_NOW = "至今";

    @Override
    protected int getInflateLayout() {
        return R.layout.fg_add_edu_backgroung;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQualification = new QualificationBottomDlg(getActivity());
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
        title.setText("添加教育背景");

        TextView titleRight = (TextView) view.findViewById(R.id.title_right);
        titleRight.setText("保存");
        titleRight.setVisibility(View.VISIBLE);
        titleRight.setOnClickListener(this);

        view.findViewById(R.id.school_from_time).setOnClickListener(this);
        school_from_tv = (TextView) view.findViewById(R.id.school_from_tv);

        view.findViewById(R.id.school_to_time).setOnClickListener(this);
        school_to_tv = (TextView) view.findViewById(R.id.school_to_tv);

        school_name = (EditText) view.findViewById(R.id.school_name);
        view.findViewById(R.id.qualification_layout).setOnClickListener(this);

        qualification_tv = (TextView) view.findViewById(R.id.qualification_tv);

        if (isUpdate) {
            school_from_tv.setText(TimeUtils.getTime(intent.getLongExtra(ExtraConst.FROM_YEAR, -1) * 1000, TimeUtils.DATE_FORMAT_YEAR_MONTH_ZERO));
            if (intent.getLongExtra(ExtraConst.TO_YEAR, -1) == 0) {
                school_to_tv.setText(TimeUtils.getTime(System.currentTimeMillis() / 1000, TimeUtils.DATE_FORMAT_YEAR_MONTH_ZERO));
            } else {
                school_to_tv.setText(TimeUtils.getTime(intent.getLongExtra(ExtraConst.TO_YEAR, -1) * 1000, TimeUtils.DATE_FORMAT_YEAR_MONTH_ZERO));
            }
            school_name.setText(intent.getStringExtra(ExtraConst.EXTRA_SCHOOL));
            qualification_tv.setText(WUtil.transformDegree(intent.getIntExtra(ExtraConst.QUAILTIFICATION, 0)));
        }
    }

    private BaseBottomDialog.OnDialogClickListener mOnDialogClickedListener = new BaseBottomDialog.OnDialogClickListener() {

        @Override
        public void onOK(Bundle b) {
            String[] birthArr = b.getStringArray(ExtraConst.EXTRA_DATE);
            String qualification = b.getString(ExtraConst.EXTRA_PROFILE_CMP_SCALE);
            int tagId = b.getInt(ExtraConst.EXTRA_PROFILE_CMP_SCALE_ID, 0);
            if (currentDlg == 1) {
                if (UP_TO_NOW.equals(birthArr[1])) {
                    if (calculateTimeLegal(UP_TO_NOW, school_to_tv.getText().toString())) {
                        school_from_tv.setText(UP_TO_NOW);
                    } else {
                        showShortToast("请选择正确的开始年月");
                    }
                } else {
                    if (calculateTimeLegal(birthArr[0] + "-" + birthArr[1], school_to_tv.getText().toString())) {
                        school_from_tv.setText(birthArr[0] + "-" + birthArr[1]);
                    } else {
                        showShortToast("请选择正确的开始年月");
                    }
                }
            } else if (currentDlg == 2) {
                if (UP_TO_NOW.equals(birthArr[1])) {
                    school_to_tv.setText(UP_TO_NOW);
                } else {
                    if (calculateTimeLegal(school_from_tv.getText().toString(), birthArr[0] + "-" + birthArr[1])) {
                        school_to_tv.setText(birthArr[0] + "-" + birthArr[1]);
                    } else {
                        showShortToast("请选择正确的离开年月");
                    }
                }
            } else if (currentDlg == 3) {
                if (!TextUtils.isEmpty(qualification)) {
                    qualification_tv.setText(qualification);
                    qualification_tv.setTag(tagId);
                }
            }
            currentDlg = 0;
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
        } else if (Integer.parseInt(fromYear) == Integer.parseInt(toYear) && Integer.parseInt(fromMonth) >= Integer.parseInt(toMonth)) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_right:
                saveEducationBackground();
                break;
            case R.id.school_from_time:
                String workFrom = school_from_tv.getText().toString().trim();
                if (!TextUtils.isEmpty(workFrom) && workFrom.contains("-")) {
                    String[] time = workFrom.split("-");
                    mDateDlg = new DatePickerBottomDlg(getActivity(), time[0], time[1]);
                } else if (UP_TO_NOW.equals(workFrom)) {
                    mDateDlg = new DatePickerBottomDlg(getActivity(), getYear() + "", UP_TO_NOW);
                } else {
                    mDateDlg = new DatePickerBottomDlg(getActivity(), "2002", "09");
                }
                mDateDlg.setOnDialogClickListener(mOnDialogClickedListener);
                mDateDlg.show();
                currentDlg = 1;
                break;
            case R.id.school_to_time:
                if (mDateDlg == null) {
                    mDateDlg = new DatePickerBottomDlg(getActivity(), "2006", "07");
                }
                String workTo = school_to_tv.getText().toString().trim();
                if (!TextUtils.isEmpty(workTo) && workTo.contains("-")) {
                    String[] time = workTo.split("-");
                    mDateDlg = new DatePickerBottomDlg(getActivity(), time[0], time[1]);
                } else if (UP_TO_NOW.equals(workTo)) {
                    mDateDlg = new DatePickerBottomDlg(getActivity(), getYear() + "", UP_TO_NOW);
                } else {
                    mDateDlg = new DatePickerBottomDlg(getActivity(), "2006", "07");
                }
                mDateDlg.setOnDialogClickListener(mOnDialogClickedListener);
                mDateDlg.show();
                currentDlg = 2;
                break;
            case R.id.qualification_layout:
                mQualification.setOnDialogClickListener(mOnDialogClickedListener);
                List<QualificationItem> list = new ArrayList<>();
                list.add(new QualificationItem(1, "学士"));
                list.add(new QualificationItem(2, "硕士"));
                list.add(new QualificationItem(3, "博士"));
                list.add(new QualificationItem(4, "其他"));
                mQualification.show(list);
                currentDlg = 3;
                break;
        }
    }

    private void saveEducationBackground() {
        schoolFrom = school_from_tv.getText().toString().trim();
        schoolTo = school_to_tv.getText().toString().trim();
        school = school_name.getText().toString().trim();
        degree = qualification_tv.getText().toString().trim();
        if (TextUtils.isEmpty(schoolFrom)) {
            showShortToast("请选择入学年月");
            return;
        }
        if (TextUtils.isEmpty(schoolTo)) {
            showShortToast("请选择毕业年月");
            return;
        }
        if (TextUtils.isEmpty(school)) {
            showShortToast("请输入学校");
            return;
        }
        if (TextUtils.isEmpty(degree)) {
            showShortToast("请选择学历");
            return;
        }
        String url = Config.WEB_API_SERVER_V3 + "/user/education/update";
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("userId", String.valueOf(SysUtil.getIntPref(PrefKeyConst.PREF_USER_ID)));
        if (isUpdate) {
            builder.add("eduId", intent.getIntExtra(ExtraConst.EXTRA_DEGREE_ID, -1) + "");
        } else {
            builder.add("eduId", "0");
        }

        if (UP_TO_NOW.equals(schoolTo)) {
            builder.add("endDate", "0");
        } else {
            builder.add("endDate", schoolTo);
        }
        builder.add("beginDate", schoolFrom);
        builder.add("name", school);
        builder.add("degree", qualification_tv.getTag() + "");
        OkHttpClientManager.getInstance(getActivity()).postWithToken(url, new OkHttpClientManager.ResultCallback<Object>() {
            @Override
            public void onResponse(Object result) {
                finishActivity();
            }
        }, builder);
    }

    public int getYear() {// 当前年份
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.YEAR);
    }
}
