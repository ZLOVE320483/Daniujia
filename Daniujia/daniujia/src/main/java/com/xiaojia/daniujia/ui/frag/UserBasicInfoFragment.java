package com.xiaojia.daniujia.ui.frag;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.squareup.picasso.Picasso;
import com.xiaojia.daniujia.ExtraConst;
import com.xiaojia.daniujia.IntentKey;
import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.dlgs.BaseBottomDialog;
import com.xiaojia.daniujia.dlgs.GenderBottomDlg;
import com.xiaojia.daniujia.dlgs.LoadingDlg;
import com.xiaojia.daniujia.domain.resp.UserBasicInfoRespVo;
import com.xiaojia.daniujia.managers.ThreadWorker;
import com.xiaojia.daniujia.ui.act.ActSelectCity;
import com.xiaojia.daniujia.ui.control.UserBasicInfoControl;
import com.xiaojia.daniujia.ui.views.imageview.RoundedImageView;
import com.xiaojia.daniujia.utils.ApplicationUtil;
import com.xiaojia.daniujia.utils.PatternUtil;
import com.xiaojia.daniujia.utils.QiniuUtil;
import com.xiaojia.daniujia.utils.ScreenUtils;
import com.xiaojia.daniujia.utils.SysUtil;
import com.xiaojia.daniujia.utils.WUtil;

import org.json.JSONObject;

import java.io.File;

/**
 * Created by ZLOVE on 2016/6/28.
 */
public class UserBasicInfoFragment extends BaseFragment implements View.OnClickListener {

    private UserBasicInfoControl control;

    private TextView tvSave;
    private RoundedImageView ivAvatar;
    private EditText etName;
    private EditText etWorkTime;
    private TextView tvWorkUnit;
    private EditText etCorp;
    private EditText etPosition;
    private View selectCityView;
    private TextView tvCity;
    private View selectGenderView;
    private TextView tvGender;
    private GenderBottomDlg genderDlg;

    private String avatarUrl = "";
    private String name = "";
    private String workAge = "";
    private String company = "";
    private String position = "";
    private String cityName = "";
    private String curCityCode = "";
    private int userGender = 0;
    private boolean isExamine = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        control = new UserBasicInfoControl();
        setControl(control);
    }

    @Override
    protected int getInflateLayout() {
        return R.layout.frag_basic_info;
    }

    @Override
    protected void setUpView(View view) {
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            if (intent.hasExtra(IntentKey.INTENT_KEY_IS_EXAMINE)) {
                isExamine = intent.getBooleanExtra(IntentKey.INTENT_KEY_IS_EXAMINE, false);
            }
        }
        setBackButton(view.findViewById(R.id.id_back));
        ((TextView) view.findViewById(R.id.id_title)).setText("基本资料");

        tvSave = (TextView) view.findViewById(R.id.title_right);
        tvSave.setText("保存");
        tvSave.setVisibility(View.VISIBLE);
        tvSave.setOnClickListener(this);

        ivAvatar = (RoundedImageView) view.findViewById(R.id.avatar);
        etName = (EditText) view.findViewById(R.id.name);
        etWorkTime = (EditText) view.findViewById(R.id.workAge);
        etCorp = (EditText) view.findViewById(R.id.company);
        tvWorkUnit = (TextView) view.findViewById(R.id.unit);
        etPosition = (EditText) view.findViewById(R.id.position);
        selectCityView = view.findViewById(R.id.city_container);
        tvCity = (TextView) view.findViewById(R.id.city);
        selectGenderView = view.findViewById(R.id.gender_container);
        tvGender = (TextView) view.findViewById(R.id.gender);

        ivAvatar.setOnClickListener(this);
        selectCityView.setOnClickListener(this);
        selectGenderView.setOnClickListener(this);

        control.initData();
    }

    public void setData(UserBasicInfoRespVo resp) {
        if (resp == null) {
            return;
        }
        if (!TextUtils.isEmpty(resp.getImgurl())) {
            avatarUrl = resp.getImgurl();
            Picasso.with(getActivity()).load(avatarUrl).error(R.drawable.ic_avatar_default).into(ivAvatar);
        } else {
            Picasso.with(getActivity()).load(R.drawable.ic_avatar_default).into(ivAvatar);
        }
        name = resp.getName();
        etName.setText(name);
        if (resp.getWorkage() > 0) {
            workAge = String.valueOf(resp.getWorkage());
            etWorkTime.setText(workAge);
            tvWorkUnit.setVisibility(View.VISIBLE);
        }
        company = resp.getCompany();
        etCorp.setText(company);
        position = resp.getPosition();
        etPosition.setText(position);
        if (!TextUtils.isEmpty(resp.getCityName())) {
            cityName = resp.getCityName();
            tvCity.setText(cityName);
        } else {
            tvCity.setText("未选");
            tvCity.setTextColor(ApplicationUtil.getApplicationContext().getResources().getColor(R.color.third_text_color));
        }
        curCityCode = resp.getCityCode();
        userGender = resp.getGender();
        if (userGender == 1) {
            tvGender.setText("男");
        } else if (userGender == 2) {
            tvGender.setText("女");
        } else if (userGender == 0) {
            tvGender.setText("未选");
            tvGender.setTextColor(ApplicationUtil.getApplicationContext().getResources().getColor(R.color.third_text_color));
        }
    }

    @Override
    public void onClick(View v) {
        if (v == tvSave) {
            onSaveClick();
        } else if (v == selectCityView) {
            Intent intent = new Intent(getActivity(), ActSelectCity.class);
            intent.putExtra(IntentKey.INTENT_KEY_CUR_CITY_CODE, curCityCode);
            startActivityForResult(intent, IntentKey.REQ_CODE_SELECT_CITY);
        } else if (v == selectGenderView) {
            if (genderDlg == null) {
                genderDlg = new GenderBottomDlg(getActivity());
            }
            genderDlg.setOnDialogClickListener(bottomDlgClickListener);
            genderDlg.show();
        } else if (v == ivAvatar) {
            control.modifyAvatar();
        }
    }

    private void onSaveClick() {
        name = etName.getText().toString().trim();
        workAge = etWorkTime.getText().toString().trim();
        company = etCorp.getText().toString().trim();
        position = etPosition.getText().toString().trim();

        if (SysUtil.getIntPref(PrefKeyConst.PREF_USER_IDENTITY) == 2) {
            if (TextUtils.isEmpty(name)) {
                showShortToast("您是专家，姓名不能修改为空");
                return;
            }
            if (TextUtils.isEmpty(workAge)) {
                showShortToast("您是专家，工作年限不能修改为空");
                return;
            }
            if (TextUtils.isEmpty(company)) {
                showShortToast("您是专家，公司不能修改为空");
                return;
            }
            if (TextUtils.isEmpty(position)) {
                showShortToast("您是专家，职位不能修改为空");
                return;
            }
            if (TextUtils.isEmpty(curCityCode) || TextUtils.isEmpty(cityName)) {
                showShortToast("您是专家，常驻城市不能修改为空");
                return;
            }
            if (userGender == 0) {
                showShortToast("您是专家，性别不能修改为空");
                return;
            }
        } else if (isExamine) {
            if (TextUtils.isEmpty(name)) {
                showShortToast("您正在申请成为专家，姓名不能修改为空");
                return;
            }
            if (TextUtils.isEmpty(workAge)) {
                showShortToast("您正在申请成为专家，工作年限不能修改为空");
                return;
            }
            if (TextUtils.isEmpty(company)) {
                showShortToast("您正在申请成为专家，公司不能修改为空");
                return;
            }
            if (TextUtils.isEmpty(position)) {
                showShortToast("您正在申请成为专家，职位不能修改为空");
                return;
            }
            if (TextUtils.isEmpty(curCityCode) || TextUtils.isEmpty(cityName)) {
                showShortToast("您正在申请成为专家，常驻城市不能修改为空");
                return;
            }
            if (userGender == 0) {
                showShortToast("您正在申请成为专家，性别不能修改为空");
                return;
            }
        }

        String illegalName = PatternUtil.isContainsIllegalStringDnj(name);
        if (illegalName != null && !illegalName.equals("")){
            showShortToast("姓名里不能包含有“"+ illegalName +"”特殊字符");
            return;
        }

        if (!TextUtils.isEmpty(workAge)) {
            int work = Integer.parseInt(workAge);
            if (work > 80 || work <= 0){
                showShortToast("工作年限填写不符合要求");
                return;
            }
        }

        String illegalCorp = PatternUtil.isContainsIllegalStringDnj(company);
        if (illegalCorp != null && !illegalCorp.equals("")){
            showShortToast("公司里不能包含有“"+ illegalCorp +"”特殊字符");
            return;
        }

        String illegalPosition = PatternUtil.isContainsIllegalStringDnj(position);
        if (illegalPosition != null && !illegalPosition.equals("")){
            showShortToast("职位里不能包含有“"+ illegalPosition +"”特殊字符");
            return;
        }
        control.saveBasicInfoRequest(avatarUrl, name, workAge, company, position, cityName, curCityCode, userGender);
    }

    public BaseBottomDialog.OnDialogClickListener bottomDlgClickListener = new BaseBottomDialog.OnDialogClickListener() {
        @Override
        public void onOK(Bundle b) {
            char gender = b.getChar(ExtraConst.EXTRA_PROFILE_GENDER, ' ');
            if (gender != ' ') {
                if (gender == 'M') {
                    tvGender.setText(R.string.male);
                    userGender = 1;
                } else if (gender == 'F') {
                    tvGender.setText(R.string.female);
                    userGender = 2;
                }
                tvGender.setTextColor(ApplicationUtil.getApplicationContext().getResources().getColor(R.color.first_text_color));
            }
        }
    };

    public void refreshHeader(String photoPath) {
        final File file = new File(photoPath);
        final LoadingDlg loading = new LoadingDlg(getActivity());
        loading.show("保存中...");
        ThreadWorker.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    QiniuUtil.upload(file, WUtil.genAvatarFileName(), new UpCompletionHandler() {
                        @Override
                        public void complete(String s, ResponseInfo responseInfo, JSONObject jsonObject) {
                            if (responseInfo != null && responseInfo.isOK()) {
                                String url = QiniuUtil.getResUrl(s);
                                avatarUrl = url;
                                loading.dismiss();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        if (ivAvatar != null) {
            Picasso.with(getActivity()).load(file).error(R.drawable.ic_avatar_default).resize(ScreenUtils.dip2px(84), ScreenUtils.dip2px(84)).into(ivAvatar);
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
                tvCity.setText(cityName);
                tvCity.setTextColor(ApplicationUtil.getApplicationContext().getResources().getColor(R.color.first_text_color));
            }
        }
    }
}
