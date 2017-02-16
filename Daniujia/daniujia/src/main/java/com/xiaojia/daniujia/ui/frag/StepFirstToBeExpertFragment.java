package com.xiaojia.daniujia.ui.frag;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.xiaojia.daniujia.dlgs.IndustryBottomDlg;
import com.xiaojia.daniujia.dlgs.LoadingDlg;
import com.xiaojia.daniujia.domain.SerializableMap;
import com.xiaojia.daniujia.domain.resp.BaseInfoVo;
import com.xiaojia.daniujia.domain.resp.IndustryAndFunctionVo;
import com.xiaojia.daniujia.domain.resp.StepFirstVo;
import com.xiaojia.daniujia.domain.resp.UserLabelVo;
import com.xiaojia.daniujia.managers.ThreadWorker;
import com.xiaojia.daniujia.ui.act.ActChangeUserLabel;
import com.xiaojia.daniujia.ui.act.ActProfileEdited;
import com.xiaojia.daniujia.ui.act.ActSecondStepToBeExpert;
import com.xiaojia.daniujia.ui.act.ImagePreActivity;
import com.xiaojia.daniujia.ui.views.FlowLayout;
import com.xiaojia.daniujia.utils.ListUtils;
import com.xiaojia.daniujia.utils.QiniuUtil;
import com.xiaojia.daniujia.utils.ScreenUtils;
import com.xiaojia.daniujia.utils.SysUtil;
import com.xiaojia.daniujia.utils.WUtil;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/17.
 */
public class StepFirstToBeExpertFragment extends BaseFragment implements View.OnClickListener, View.OnFocusChangeListener {

    private static final int FILL_PROFILE = 1000;
//    private StepFirstControl control;

    private TextView tvFifth;
    private TextView unit;
    private TextView industryTv;
    private TextView genderTv;

    private RelativeLayout header_ll;
    private ImageView avatar;

    private FlowLayout labels;
    private LinearLayout industry_layout;
    private RelativeLayout gender_layout;

    private GenderBottomDlg genderDlg;
    private IndustryBottomDlg industryDlg;

    private EditText name;
    private EditText workAge;
    private EditText company;
    private TextView content;
    private EditText position;
    private TextView no_label;
    public  StepFirstVo resultVo;
    private RelativeLayout account_profile;
    private TextView account_profile_title;

    private boolean isModify = false;
    public  IndustryAndFunctionVo result;
    private int tagCont;

    @Override
    protected int getInflateLayout() {
        return R.layout.fg_step1_to_be_expert;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        control = new StepFirstControl();
//        setControl(control);
        industryDlg = new IndustryBottomDlg(getActivity());
        industryDlg.setOnDialogClickListener(bottomDlgClickListener);
        genderDlg = new GenderBottomDlg(getActivity());
        genderDlg.setOnDialogClickListener(bottomDlgClickListener);
    }

    @Override
    protected void setUpView(View view) {
        setBackButton(view.findViewById(R.id.id_back));
        TextView next = (TextView) view.findViewById(R.id.title_right);
        next.setVisibility(View.VISIBLE);
        next.setOnClickListener(this);
        TextView title = (TextView) view.findViewById(R.id.id_title);
        title.setText("资料完善");
        tvFifth = (TextView) view.findViewById(R.id.complete_tv);

        avatar = (ImageView) view.findViewById(R.id.avatar);
        header_ll = (RelativeLayout) view.findViewById(R.id.header_ll);
        if (!TextUtils.isEmpty(SysUtil.getPref(PrefKeyConst.PREF_USER_AVATAR))) {
            Picasso.with(getActivity()).load(SysUtil.getPref(PrefKeyConst.PREF_USER_AVATAR)).error(R.drawable.ic_avatar_default).into(avatar);
        }else{
            Picasso.with(getActivity()).load(R.drawable.ic_avatar_default).into(avatar);
        }
        header_ll.setOnClickListener(this);
        avatar.setOnClickListener(this);

        name = (EditText) view.findViewById(R.id.name);
        name.setOnFocusChangeListener(this);
        workAge = (EditText) view.findViewById(R.id.workAge);
        workAge.setOnFocusChangeListener(this);

        content = (TextView) view.findViewById(R.id.content);
        content.setOnClickListener(this);

        unit = (TextView) view.findViewById(R.id.unit);
        company = (EditText) view.findViewById(R.id.belong_cmp);
        account_profile_title = (TextView) view.findViewById(R.id.account_profile_title);
        account_profile_title.setOnClickListener(this);

        company.setOnFocusChangeListener(this);
        position = (EditText) view.findViewById(R.id.position);
        position.setOnFocusChangeListener(this);

        industryTv = (TextView) view.findViewById(R.id.industry);

        genderTv = (TextView) view.findViewById(R.id.gender);

        LinearLayout label_edit_layout = (LinearLayout) view.findViewById(R.id.label_edit_layout);
        label_edit_layout.setOnClickListener(this);

        industry_layout = (LinearLayout) view.findViewById(R.id.industry_layout);
        industry_layout.setOnClickListener(this);

        gender_layout = (RelativeLayout) view.findViewById(R.id.gender_layout);
        gender_layout.setOnClickListener(this);

        account_profile = (RelativeLayout) view.findViewById(R.id.account_profile);
        account_profile.setOnClickListener(this);

        labels = (FlowLayout) view.findViewById(R.id.labels);
        no_label = (TextView) view.findViewById(R.id.no_label);

//        control.initDataToBeExpert();
    }

    @Override
    public void onClick(View v) {
        if (resultVo == null) {
            return;
        }
        switch (v.getId()) {
            case R.id.title_right:
                String profileTx = content.getText().toString().trim();
                String companyTx = company.getText().toString();
                String nameTx = name.getText().toString();
                String positionTx = position.getText().toString();
                String workAgeTx = workAge.getText().toString();
                if (TextUtils.isEmpty(resultVo.getUser().getImgurl().trim())) {
                    showShortToast("您的头像还未上传");
                    return;
                }
                if (TextUtils.isEmpty(nameTx)) {
                    showShortToast("您的姓名还未填写");
                    return;
                }
                if (TextUtils.isEmpty(workAgeTx)) {
                    showShortToast("您的工作年限还未填写");
                    return;
                }
                if (TextUtils.isEmpty(industryTv.getText().toString().trim())) {
                    showShortToast("您的行业与职能还未选择");
                    return;
                }
                if (TextUtils.isEmpty(companyTx)) {
                    showShortToast("您的所在公司还未填写");
                    return;
                }
                if (TextUtils.isEmpty(position.getText().toString().trim())) {
                    showShortToast("您的职位还未填写");
                    return;
                }
                if (TextUtils.isEmpty(genderTv.getText().toString().trim())) {
                    showShortToast("您的性别还未选择");
                    return;
                }
                if (tagCont == 0) {
                    showShortToast("您的标签还未编辑");
                    return;
                }
                if (TextUtils.isEmpty(profileTx)) {
                    showShortToast("您的个人简介还未填写");
                    return;
                }
                resultVo.setProfile(profileTx);
                resultVo.getUser().setCompany(companyTx);
                resultVo.getUser().setName(nameTx);
                resultVo.getUser().setPosition(positionTx);
                resultVo.getUser().setWorkage(Integer.parseInt(workAgeTx));
                resultVo.getUser().setUserId(SysUtil.getIntPref(PrefKeyConst.PREF_USER_ID));
//                control.saveDataToBeExpert(resultVo);
                break;
            case R.id.industry_layout:
//                control.showIndustry();
                break;
            case R.id.gender_layout:
                genderDlg.show();
                break;
            case R.id.label_edit_layout:
                Intent intent = new Intent(getActivity(), ActChangeUserLabel.class);
                intent.putExtra(IntentKey.INTENT_KEY_FUNCTION_ID, resultVo.getUser().getFunctionId());
                startActivityForResult(intent, IntentKey.REQ_CODE_CHANGE_USER_LABEL);
                break;
            case R.id.account_profile:
            case R.id.content:
                Intent intent_profile = new Intent(getActivity(), ActProfileEdited.class);
                intent_profile.putExtra(ExtraConst.EXTRA_PROFILE, resultVo.getProfile());
                startActivityForResult(intent_profile, FILL_PROFILE);
                break;
            case R.id.header_ll:
//                control.modifyAvatar();
                break;
            case R.id.avatar:
                if (!TextUtils.isEmpty(resultVo.getUser().getImgurl())) {
                    SerializableMap myMap = new SerializableMap();
                    Intent picIntent = new Intent(getContext(), ImagePreActivity.class);
                    Bundle bundle = new Bundle();
                    SparseArray<Object> map = new SparseArray<>();
                    map.put(0, resultVo.getUser().getImgurl());
                    myMap.setMap(map);
                    bundle.putParcelable(IntentKey.INTENT_KEY_IMAGE_MAP, myMap);
                    bundle.putString(IntentKey.INTENT_CLICK_VALUE, resultVo.getUser().getImgurl());
                    picIntent.putExtra("bundle", bundle);
                    getActivity().startActivity(picIntent);
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILL_PROFILE) {
            if (data != null) {
                String stringExtra = data.getStringExtra(ExtraConst.ACCOUNT_PROFILE);
                resultVo.setProfile(stringExtra);
                content.setText(stringExtra);
            }
        } else if (requestCode == IntentKey.REQ_CODE_CHANGE_USER_LABEL) {
            if (data != null && data.hasExtra(IntentKey.INTENT_KEY_USER_LABEL)) {
                ArrayList<UserLabelVo.MyTagInfo> tagInfos = (ArrayList<UserLabelVo.MyTagInfo>) data.getSerializableExtra(IntentKey.INTENT_KEY_USER_LABEL);
                modifyTag(tagInfos);
            }
        }
    }

    public void modifyTag(List<UserLabelVo.MyTagInfo> tagInfos) {
        if (ListUtils.isEmpty(tagInfos)) {
            tagCont = 0;
            no_label.setVisibility(View.VISIBLE);
            labels.setVisibility(View.GONE);
        } else {
            tagCont = tagInfos.size();
            no_label.setVisibility(View.GONE);
            labels.setVisibility(View.VISIBLE);
            labels.removeAllViews();
            for (final UserLabelVo.MyTagInfo item : tagInfos) {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.grid_item_hot_label, null);
                TextView tvLabel = (TextView) view.findViewById(R.id.id_name);
                tvLabel.setText(item.getName());
                ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.leftMargin = 5;
                lp.rightMargin = 5;
                lp.topMargin = 5;
                lp.bottomMargin = 5;
                labels.addView(view, lp);
            }
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v == workAge) {
            if (unit != null) {
                if (hasFocus) {
                    unit.setVisibility(View.VISIBLE);
                    workAge.setHint("");
                } else {
                    if ("0".equals(workAge.getText().toString()) || TextUtils.isEmpty(workAge.getText().toString())) {
                        workAge.setText("");
                        workAge.setHint("未填写");
                        unit.setVisibility(View.GONE);
                    } else {
                        unit.setVisibility(View.VISIBLE);
                        if (resultVo != null && resultVo.getUser()!= null) {
                            resultVo.getUser().setWorkage(Integer.parseInt(workAge.getText().toString()));
                        }
                    }
                }
            }
        }
    }

    public BaseBottomDialog.OnDialogClickListener bottomDlgClickListener = new BaseBottomDialog.OnDialogClickListener() {
        @Override
        public void onOK(Bundle b) {
            if (!SysUtil.getBooleanPref(PrefKeyConst.PREF_IS_LOGIN) || resultVo == null) {
                return;
            }
            String[] industryArray = b.getStringArray(ExtraConst.EXTRA_PROFILE_ADDR);
            char gender = b.getChar(ExtraConst.EXTRA_PROFILE_GENDER, ' ');
            if (industryArray != null) {
                industryTv.setText(industryArray[0] + " " + industryArray[1]);
                if (resultVo.getUser().getFunctionId() != Integer.parseInt(industryArray[2])) {
                    resultVo.getUser().setFunctionName(industryArray[1]);
                    resultVo.getUser().setNovationName(industryArray[0]);
//                    control.modifyTag(Integer.parseInt(industryArray[2]));
                }
                resultVo.getUser().setFunctionId(Integer.parseInt(industryArray[2]));
            } else if (gender != ' ') {
                if (gender == 'M') {
                    genderTv.setText(R.string.male);
                    resultVo.getUser().setGender(1);
                } else if (gender == 'F') {
                    genderTv.setText(R.string.female);
                    resultVo.getUser().setGender(2);
                }
            }
        }
    };

    public void refreshData(StepFirstVo result) {
        if (result == null) {
            return;
        }
        resultVo = result;
        if (SysUtil.getIntPref(PrefKeyConst.PREF_USER_IDENTITY) == 2 || resultVo.getApplyStepValue() >= 15) {
            isModify = true;
            tvFifth.setText("修改完成");
        }
        name.setText(result.getUser().getName());
        workAge.setText(result.getUser().getWorkage() + "");
        if (result.getUser().getWorkage() != 0) {
            unit.setVisibility(View.VISIBLE);
        }else{
            unit.setVisibility(View.GONE);
        }
        company.setText(result.getUser().getCompany());
        position.setText(result.getUser().getPosition());
        industryTv.setText(result.getUser().getNovationName() + " " + result.getUser().getFunctionName());
        if (result.getUser().getGender() != 0) {
            genderTv.setText(result.getUser().getGender()==2 ? "女":"男");
        }
        if (ListUtils.isEmpty(result.getTags())) {
            no_label.setVisibility(View.VISIBLE);
            labels.setVisibility(View.GONE);
        }else{
            tagCont = result.getTags().size();
            no_label.setVisibility(View.GONE);
            labels.setVisibility(View.VISIBLE);
            for (final BaseInfoVo.Tag item : result.getTags()) {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.grid_item_hot_label, null);
                TextView tvLabel = (TextView) view.findViewById(R.id.id_name);
                tvLabel.setText(item.getName());
                ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.leftMargin = 5;
                lp.rightMargin = 5;
                lp.topMargin = 5;
                lp.bottomMargin = 5;
                labels.addView(view, lp);
            }
        }
        content.setText(Html.fromHtml(result.getProfile()));
    }

    public void gotoStepSecond() {
        Intent intent = new Intent(getActivity(), ActSecondStepToBeExpert.class);
        intent.putExtra(IntentKey.INTENT_KEY_TO_BE_EXPERT_IS_MODIFY, isModify);
        startActivity(intent);
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
                                resultVo.getUser().setImgurl(url);
                                SysUtil.savePref(PrefKeyConst.PREF_USER_AVATAR, url);
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
            Picasso.with(getActivity()).load(file).error(R.drawable.ic_avatar_default)
                    .resize(ScreenUtils.dip2px(60), ScreenUtils.dip2px(60)).into(avatar);
        }
    }

    public void cacheData(IndustryAndFunctionVo result) {
        this.result = result;
    }
}
