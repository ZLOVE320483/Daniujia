package com.xiaojia.daniujia.ui.frag;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.picasso.Picasso;
import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.ExtraConst;
import com.xiaojia.daniujia.IntentKey;
import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.dlgs.BaseBottomDialog;
import com.xiaojia.daniujia.dlgs.GenderBottomDlg;
import com.xiaojia.daniujia.dlgs.LoadingDlg;
import com.xiaojia.daniujia.domain.resp.BeExpertBaseInfo;
import com.xiaojia.daniujia.managers.OkHttpClientManager;
import com.xiaojia.daniujia.managers.ThreadWorker;
import com.xiaojia.daniujia.ui.act.ActSecondStepToBeExpert;
import com.xiaojia.daniujia.ui.act.ActSelectCity;
import com.xiaojia.daniujia.ui.control.StepFirstControl;
import com.xiaojia.daniujia.ui.views.imageview.RoundedImageView;
import com.xiaojia.daniujia.utils.DialogManager;
import com.xiaojia.daniujia.utils.PatternUtil;
import com.xiaojia.daniujia.utils.QiniuUtil;
import com.xiaojia.daniujia.utils.ScreenUtils;
import com.xiaojia.daniujia.utils.SysUtil;
import com.xiaojia.daniujia.utils.WUtil;

import org.json.JSONObject;

import java.io.File;

/**
 * Created by Administrator on 2016/6/28.
 */
public class StepFirstToBeExpertNew extends BaseFragment implements View.OnClickListener {

    private EditText et_name;
    private EditText et_experience;
    private EditText et_company;
    private EditText et_position;

    private TextView tv_location;
    private TextView tv_gender;

    private RoundedImageView avatar;

    private LinearLayout ll_city;
    private LinearLayout ll_gender;

    private StepFirstControl control;
    private String imageUrl;
    private String cityCode = "";
    private GenderBottomDlg genderDlg;

    private ImageView ivHelp;
    private ToggleButton tbSwitch;
    private int anonymous = 0;
    private String fromWhere = "";

    @Override
    protected int getInflateLayout() {
        return R.layout.fg_step1_to_be_expert_new;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        control = new StepFirstControl();
        setControl(control);
    }

    @Override
    protected void setUpView(View view) {
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(IntentKey.FROM_WHERE)) {
            fromWhere = intent.getStringExtra(IntentKey.FROM_WHERE);
        }
        setBackButton(view.findViewById(R.id.id_back));
        view.findViewById(R.id.title_right).setOnClickListener(this);

        ImageView step = (ImageView) view.findViewById(R.id.step1);
        step.setImageResource(R.drawable.basic_information_white);

        TextView title = (TextView) view.findViewById(R.id.id_title);
        title.setText("基本资料");

        et_name = (EditText) view.findViewById(R.id.name);
        et_experience = (EditText) view.findViewById(R.id.experience);
        et_company = (EditText) view.findViewById(R.id.company);
        et_position = (EditText) view.findViewById(R.id.position);

        tv_location = (TextView) view.findViewById(R.id.location);
        tv_gender = (TextView) view.findViewById(R.id.gender_tv);

        avatar = (RoundedImageView) view.findViewById(R.id.avatar);

        ll_city = (LinearLayout) view.findViewById(R.id.city);
        ll_gender = (LinearLayout) view.findViewById(R.id.gender);

        avatar.setOnClickListener(this);
        ll_city.setOnClickListener(this);
        ll_gender.setOnClickListener(this);

        genderDlg = new GenderBottomDlg(getActivity());
        genderDlg.setOnDialogClickListener(bottomDlgClickListener);

        ivHelp = (ImageView) view.findViewById(R.id.id_help);
        ivHelp.setOnClickListener(this);
        tbSwitch = (ToggleButton) view.findViewById(R.id.toggle_switch);


        tbSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    anonymous = 0;
                } else {
                    anonymous = 1;
                }
            }
        });

        control.requestBaseInfo();
    }

    BaseBottomDialog.OnDialogClickListener bottomDlgClickListener = new BaseBottomDialog.OnDialogClickListener() {
        @Override
        public void onOK(Bundle b) {
            char gender = b.getChar(ExtraConst.EXTRA_PROFILE_GENDER, ' ');
            if (gender != ' ') {
                if (gender == 'M') {
                    tv_gender.setText(R.string.male);
                } else if (gender == 'F') {
                    tv_gender.setText(R.string.female);
                }
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.city:
                Intent intent = new Intent(getActivity(), ActSelectCity.class);
                intent.putExtra(IntentKey.INTENT_KEY_CUR_CITY_CODE, cityCode);
                startActivityForResult(intent, IntentKey.REQ_CODE_SELECT_CITY);
                break;
            case R.id.gender:
                if (genderDlg != null) {
                    genderDlg.show();
                }
                break;
            case R.id.title_right:
                saveBaseInfo();
                break;
            case R.id.avatar:
                control.modifyAvatar();
                break;
            case R.id.id_help:
                DialogManager.showAnonymousDialog(getActivity());
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentKey.REQ_CODE_SELECT_CITY && data != null) {
            if (data.hasExtra(IntentKey.INTENT_KEY_CUR_CITY_CODE)) {
                cityCode = data.getStringExtra(IntentKey.INTENT_KEY_CUR_CITY_CODE);
            }
            if (data.hasExtra(IntentKey.INTENT_KEY_CUR_CITY_NAME)) {
                String cityName = data.getStringExtra(IntentKey.INTENT_KEY_CUR_CITY_NAME);
                tv_location.setText(cityName);
            }
        }
    }

    public void refreshDate(BeExpertBaseInfo result) {
        imageUrl = result.getImgurl();
        if (TextUtils.isEmpty(imageUrl)) {
            Picasso.with(getActivity()).load(R.drawable.ic_avatar_default).into(avatar);
        } else {
            Picasso.with(getActivity()).load(imageUrl).into(avatar);
        }
        et_name.setText(result.getName());
        et_position.setText(result.getPosition());
        et_company.setText(result.getCompany());
        if (result.getWorkage() != 0) {
            et_experience.setText(result.getWorkage() + "");
        }
        tv_location.setText(result.getCityName());
        if (result.getGender() != 0) {
            tv_gender.setText(result.getGender() == 1 ? "男":"女");
        }
        if (result.getAnonymous() == 0) {
            tbSwitch.setChecked(true);
        } else {
            tbSwitch.setChecked(false);
        }
        cityCode = result.getCityCode();
    }

    public void saveBaseInfo() {
        String name = et_name.getText().toString().trim();
        String experience = et_experience.getText().toString().trim();
        String company = et_company.getText().toString().trim();
        String position = et_position.getText().toString().trim();

        if (TextUtils.isEmpty(imageUrl) || imageUrl.contains("moren_new")) {
            showShortToast("请选择头像");
            return;
        }
        if (TextUtils.isEmpty(name)) {
            showShortToast("请填写姓名");
            return;
        }
        String illegalName = PatternUtil.isContainsIllegalStringDnj(name);
        if (illegalName != null && !illegalName.equals("")){
            showShortToast("姓名里不能包含有“"+ illegalName +"”特殊字符");
            return;
        }

        if (TextUtils.isEmpty(experience)) {
            showShortToast("请填写工作年限");
            return;
        }
        int work = Integer.parseInt(experience);
        if (work > 80 || work <= 0){
            showShortToast("工作年限填写不符合要求");
            return;
        }

        if (TextUtils.isEmpty(company)) {
            showShortToast("请填写公司名称");
            return;
        }
        String illegalCorp = PatternUtil.isContainsIllegalStringDnj(company);
        if (illegalCorp != null && !illegalCorp.equals("")){
            showShortToast("公司里不能包含有“"+ illegalCorp +"”特殊字符");
            return;
        }
        if (TextUtils.isEmpty(position)) {
            showShortToast("请填写职位名称");
            return;
        }
        String illegalPosition = PatternUtil.isContainsIllegalStringDnj(position);
        if (illegalPosition != null && !illegalPosition.equals("")){
            showShortToast("职位里不能包含有“"+ illegalPosition +"”特殊字符");
            return;
        }
        if (TextUtils.isEmpty(tv_location.getText().toString().trim())||TextUtils.isEmpty(cityCode)) {
            showShortToast("请选择所在城市");
            return;
        }
        if (TextUtils.isEmpty(tv_gender.getText().toString().trim())) {
            showShortToast("请选择性别");
            return;
        }
        String url = Config.WEB_API_SERVER_V3 + "/become/base/info";
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("flag", "0");
        builder.add("imgurl", imageUrl);
        builder.add("name", et_name.getText().toString().trim());
        builder.add("workage", et_experience.getText().toString().trim());
        builder.add("company", et_company.getText().toString().trim());
        builder.add("position", et_position.getText().toString().trim());
        builder.add("cityName", tv_location.getText().toString().trim());
        builder.add("cityCode", cityCode);
        builder.add("gender", "男".equals(tv_gender.getText().toString().trim()) ? "1" : "2");
        builder.add("anonymous", String.valueOf(anonymous));
        OkHttpClientManager.getInstance(getActivity()).postWithToken(url, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onResponse(String result) {
                SysUtil.savePref(PrefKeyConst.PREF_USER_AVATAR, imageUrl);
                SysUtil.savePref(PrefKeyConst.PREF_NAME, et_name.getText().toString().trim());
                Intent intent = new Intent(getActivity(), ActSecondStepToBeExpert.class);
                intent.putExtra(IntentKey.FROM_WHERE, fromWhere);
                startActivity(intent);
            }
        }, builder);
    }

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
                                imageUrl = url;
                                loading.dismiss();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        if (avatar != null) {
            Picasso.with(getActivity()).load(file).error(R.drawable.ic_avatar_default).resize(ScreenUtils.dip2px(84), ScreenUtils.dip2px(84)).into(avatar);
        }
    }

}
