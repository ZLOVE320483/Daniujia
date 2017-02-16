package com.xiaojia.daniujia.ui.frag;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
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
import com.xiaojia.daniujia.domain.resp.UserLabelVo;
import com.xiaojia.daniujia.managers.ThreadWorker;
import com.xiaojia.daniujia.ui.act.ActChangeUserLabel;
import com.xiaojia.daniujia.ui.act.ImagePreActivity;
import com.xiaojia.daniujia.ui.control.AccountControl;
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
 * Created by Administrator on 2016/5/9
 */
public class AccountFragment extends BaseFragment implements View.OnClickListener, View.OnFocusChangeListener {

    private AccountControl control;
    private ImageView avatar;
    private EditText name;
    private EditText workAge;
    private TextView unit;
    private TextView industryTv;
    private EditText belong_cmp;
    private EditText position;
    private TextView genderTv;
    private FlowLayout labels;
    public BaseInfoVo.UserEntity userInfo;
    private RelativeLayout header_ll;
    private GenderBottomDlg genderDlg;
    private IndustryBottomDlg industryDlg;

    private int workYear = 0;
    private TextView label;
    public IndustryAndFunctionVo result;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        control = new AccountControl();
        setControl(control);
        industryDlg = new IndustryBottomDlg(getActivity());
        industryDlg.setOnDialogClickListener(bottomDlgClickListener);
        genderDlg = new GenderBottomDlg(getActivity());
        genderDlg.setOnDialogClickListener(bottomDlgClickListener);
    }

    @Override
    protected int getInflateLayout() {
        return R.layout.activity_account_baseinfo;
    }

    @Override
    protected void setUpView(View view) {
        setBackButton(view.findViewById(R.id.back));

        TextView titleRight = (TextView) view.findViewById(R.id.title_right_tv);
        titleRight.setOnClickListener(this);

        avatar = (ImageView) view.findViewById(R.id.avatar);
        header_ll = (RelativeLayout) view.findViewById(R.id.header_ll);

        header_ll.setOnClickListener(this);
        avatar.setOnClickListener(this);

        name = (EditText) view.findViewById(R.id.name);
        name.setOnFocusChangeListener(this);
        workAge = (EditText) view.findViewById(R.id.workAge);
        workAge.setOnFocusChangeListener(this);
        unit = (TextView) view.findViewById(R.id.unit);

        LinearLayout industryLayout = (LinearLayout) view.findViewById(R.id.industry_layout);
        industryLayout.setOnClickListener(this);
        industryTv = (TextView) view.findViewById(R.id.industry);

        belong_cmp = (EditText) view.findViewById(R.id.belong_cmp);
        belong_cmp.setOnFocusChangeListener(this);
        position = (EditText) view.findViewById(R.id.position);
        position.setOnFocusChangeListener(this);

        RelativeLayout gender_layout = (RelativeLayout) view.findViewById(R.id.gender_layout);
        gender_layout.setOnClickListener(this);
        genderTv = (TextView) view.findViewById(R.id.gender);

        LinearLayout label_layout = (LinearLayout) view.findViewById(R.id.label_edit_layout);
        label_layout.setOnClickListener(this);
        labels = (FlowLayout) view.findViewById(R.id.labels);

        label = (TextView) view.findViewById(R.id.no_label);
        control.initData();
    }

    @Override
    public void onClick(View v) {
        if (userInfo == null) {
            return;
        }
        switch (v.getId()) {
            case R.id.avatar:
                if (!TextUtils.isEmpty(userInfo.getImgurl())) {
                    SerializableMap myMap = new SerializableMap();
                    Intent intent = new Intent(getContext(),ImagePreActivity.class);
                    Bundle bundle = new Bundle();
                    SparseArray<Object> map = new SparseArray<>();
                    map.put(0,userInfo.getImgurl());
                    myMap.setMap(map);
                    bundle.putParcelable(IntentKey.INTENT_KEY_IMAGE_MAP, myMap);
                    bundle.putString(IntentKey.INTENT_CLICK_VALUE, userInfo.getImgurl());
                    intent.putExtra("bundle", bundle);
                    getActivity().startActivity(intent);
                }
                break;
            case R.id.header_ll:
                control.modifyAvatar();
                break;
            case R.id.title_right_tv:
                userInfo.setName(name.getText().toString());
                userInfo.setCompany(belong_cmp.getText().toString());
                userInfo.setPosition(position.getText().toString());
                if (!TextUtils.isEmpty(workAge.getText().toString().trim())) {
                    workYear = Integer.valueOf(workAge.getText().toString().trim());
                }
                userInfo.setWorkage(workYear);
                control.saveData(userInfo);
                break;
            case R.id.industry_layout:
                control.showIndustry();
                break;
            case R.id.gender_layout:
                genderDlg.show();
                break;
            case R.id.label_edit_layout:
                Intent intent = new Intent(getActivity(), ActChangeUserLabel.class);
                intent.putExtra(IntentKey.INTENT_KEY_FUNCTION_ID, userInfo.getFunctionId());
                startActivityForResult(intent, IntentKey.REQ_CODE_CHANGE_USER_LABEL);
                break;
        }
    }

    @SuppressLint("SetTextI18n")
    public void refreshData(BaseInfoVo result) {

        if (!isAdded()) {
            return;
        }
        if (result == null) {
            return;
        }
        userInfo = result.getUser();
        if (avatar != null) {
            if (!TextUtils.isEmpty(result.getUser().getImgurl())) {
                Picasso.with(getActivity()).load(result.getUser().getImgurl()).error(R.drawable.ic_avatar_default)
                        .into(avatar);
            }else{
                Picasso.with(getActivity()).load(R.drawable.ic_avatar_default).into(avatar);
            }
        }

        if (name != null) {
            name.setText(result.getUser().getName());
        }

        if (result.getUser().getWorkage() != 0) {
            workAge.setText(result.getUser().getWorkage() + "");
            if (unit != null) {
                unit.setVisibility(View.VISIBLE);
            }
        }
        if (industryTv != null) {
            industryTv.setText(result.getUser().getNovationName() + " "  + result.getUser().getFunctionName());
        }
        if (belong_cmp != null) {
            belong_cmp.setText(result.getUser().getCompany());
        }
        if (position != null) {
            position.setText(result.getUser().getPosition());
        }
        if (genderTv != null) {
            int genderInt = result.getUser().getGender();
            if (genderInt == 0) {
                genderTv.setText(null);
            } else if (genderInt == 1) {
                genderTv.setText("男");
            } else {
                genderTv.setText("女");
            }
        }
        modifyLabels(result.getTags());
    }

    public void modifyLabels(List<BaseInfoVo.Tag> tags) {
        labels.removeAllViews();
        if (labels != null && !ListUtils.isEmpty(tags)) {
            label.setVisibility(View.GONE);
            labels.setVisibility(View.VISIBLE);
            for (final BaseInfoVo.Tag item : tags) {
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
        }else{
            label.setVisibility(View.VISIBLE);
            labels.setVisibility(View.GONE);
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
                        if (userInfo != null) {
                            userInfo.setWorkage(Integer.parseInt(workAge.getText().toString()));
                        }
                    }
                }
            }
        }
    }

    public BaseBottomDialog.OnDialogClickListener bottomDlgClickListener = new BaseBottomDialog.OnDialogClickListener() {
        @Override
        public void onOK(Bundle b) {
            if (!SysUtil.getBooleanPref(PrefKeyConst.PREF_IS_LOGIN)) {
                return;
            }
            String[] industryArray = b.getStringArray(ExtraConst.EXTRA_PROFILE_ADDR);
            char gender = b.getChar(ExtraConst.EXTRA_PROFILE_GENDER, ' ');
            if (industryArray != null) {
                industryTv.setText(industryArray[0] + " " + industryArray[1]);
                if (userInfo.getFunctionId() != Integer.parseInt(industryArray[2])) {
                    control.modifyTag(Integer.parseInt(industryArray[2]));
                }
                userInfo.setFunctionId(Integer.parseInt(industryArray[2]));
            } else if (gender != ' ') {
                if (gender == 'M') {
                    genderTv.setText(R.string.male);
                    userInfo.setGender(1);
                } else if (gender == 'F') {
                    genderTv.setText(R.string.female);
                    userInfo.setGender(2);
                }
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
                                userInfo.setImgurl(url);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentKey.REQ_CODE_CHANGE_USER_LABEL) {
            if (data != null && data.hasExtra(IntentKey.INTENT_KEY_USER_LABEL)) {
                ArrayList<UserLabelVo.MyTagInfo> tagInfos = (ArrayList<UserLabelVo.MyTagInfo>) data.getSerializableExtra(IntentKey.INTENT_KEY_USER_LABEL);
                if (ListUtils.isEmpty(tagInfos)) {
                    label.setVisibility(View.VISIBLE);
                    labels.setVisibility(View.GONE);
                } else {
                    label.setVisibility(View.GONE);
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
        }
    }

    public void cacheIndustryData(IndustryAndFunctionVo result) {
        this.result = result;
    }
}
