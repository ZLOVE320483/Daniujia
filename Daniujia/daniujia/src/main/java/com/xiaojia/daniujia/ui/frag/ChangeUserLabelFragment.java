package com.xiaojia.daniujia.ui.frag;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaojia.daniujia.IntentKey;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.domain.resp.AddUserLabelVo;
import com.xiaojia.daniujia.domain.resp.UserLabelVo;
import com.xiaojia.daniujia.ui.control.ChangeUserLabelControl;
import com.xiaojia.daniujia.ui.views.FlowLayout;
import com.xiaojia.daniujia.utils.ListUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZLOVE on 2016/5/10.
 */
public class ChangeUserLabelFragment extends BaseFragment implements View.OnClickListener {

    private int functionId = 0;
    private ChangeUserLabelControl changeUserLabelControl;

    private TextView tvSave;

    private TextView tvNoLabel;
    private FlowLayout userLabelLayout;
    private ArrayList<UserLabelVo.MyTagInfo> userLabelList = new ArrayList<>();

    private EditText etEditLabel;
    private Button btnAdd;

    private FlowLayout commonLabelLayout;
    private List<UserLabelVo.CommonTagInfo> commonLabelList = new ArrayList<>();

    private String addLabel = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeUserLabelControl = new ChangeUserLabelControl();
        setControl(changeUserLabelControl);
    }

    @Override
    protected int getInflateLayout() {
        return R.layout.frag_change_label;
    }

    @Override
    protected void setUpView(View view) {
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            if (intent.hasExtra(IntentKey.INTENT_KEY_FUNCTION_ID)) {
                functionId = intent.getIntExtra(IntentKey.INTENT_KEY_FUNCTION_ID, 0);
            }
        }
        setBackButton(view.findViewById(R.id.id_back));
        ((TextView) view.findViewById(R.id.id_title)).setText("标签修改");

        tvSave = (TextView) view.findViewById(R.id.id_save);
        tvSave.setOnClickListener(this);
        tvNoLabel = (TextView) view.findViewById(R.id.id_no_label);
        userLabelLayout = (FlowLayout) view.findViewById(R.id.id_my_label);

        etEditLabel = (EditText) view.findViewById(R.id.et_label);
        btnAdd = (Button) view.findViewById(R.id.id_add);
        btnAdd.setOnClickListener(this);

        commonLabelLayout = (FlowLayout) view.findViewById(R.id.id_common_label);
        changeUserLabelControl.requestUserLabel(functionId);
    }

    @Override
    public void onClick(View v) {
        if (v == btnAdd) {
            addLabel();
        } else if (v == tvSave) {
            changeUserLabelControl.saveUserLabelRequest(functionId, userLabelList);
        }
    }

    private void addLabel() {
        addLabel = etEditLabel.getText().toString().trim();
        if (TextUtils.isEmpty(addLabel)) {
            showShortToast("请输入标签");
            return;
        }
        changeUserLabelControl.addLabelRequest(functionId, addLabel);
    }

    public void setData(UserLabelVo data) {
        if (data == null) {
            return;
        }
        setMyLabel(data.getMyTags());
        setCommonLabel(data.getCommonTags());
    }

    private void setMyLabel(List<UserLabelVo.MyTagInfo> myTagInfoList) {
        if (ListUtils.isEmpty(myTagInfoList)) {
            tvNoLabel.setVisibility(View.VISIBLE);
            userLabelLayout.setVisibility(View.GONE);
            return;
        }
        tvNoLabel.setVisibility(View.GONE);
        userLabelLayout.setVisibility(View.VISIBLE);
        this.userLabelList.addAll(myTagInfoList);

        for (final UserLabelVo.MyTagInfo item : userLabelList) {
            final View view = LayoutInflater.from(getActivity()).inflate(R.layout.grid_item_user_label, null);
            TextView tvLabel = (TextView) view.findViewById(R.id.id_label);
            ImageView ivDelete = (ImageView) view.findViewById(R.id.id_delete);
            tvLabel.setText(item.getName());
            ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.leftMargin = 5;
            lp.rightMargin = 5;
            lp.topMargin = 5;
            lp.bottomMargin = 5;
            userLabelLayout.addView(view, lp);

            ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userLabelList.remove(item);
                    userLabelLayout.removeView(view);
                    checkUserLabelEmpty();

                }
            });
        }
    }

    private void setCommonLabel(List<UserLabelVo.CommonTagInfo> commonTagInfoList) {
        if (ListUtils.isEmpty(commonTagInfoList)) {
            return;
        }
        this.commonLabelList.addAll(commonTagInfoList);

        for (final UserLabelVo.CommonTagInfo item : commonLabelList) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.grid_item_common_label, null);
            TextView tvLabel = (TextView) view.findViewById(R.id.id_content);
            tvLabel.setText(item.getName());
            ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.leftMargin = 5;
            lp.rightMargin = 5;
            lp.topMargin = 5;
            lp.bottomMargin = 5;
            commonLabelLayout.addView(view, lp);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (UserLabelVo.MyTagInfo info : userLabelList) {
                        if (info.getTagId() == item.getTagId()) {
                            showShortToast("您已添加过该标签");
                            return;
                        }
                    }
                    final UserLabelVo.MyTagInfo myTagInfo = new UserLabelVo.MyTagInfo();
                    myTagInfo.setTagId(item.getTagId());
                    myTagInfo.setName(item.getName());
                    userLabelList.add(myTagInfo);
                    final View view = LayoutInflater.from(getActivity()).inflate(R.layout.grid_item_user_label, null);
                    TextView tvLabel = (TextView) view.findViewById(R.id.id_label);
                    ImageView ivDelete = (ImageView) view.findViewById(R.id.id_delete);
                    tvLabel.setText(myTagInfo.getName());
                    ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    lp.leftMargin = 5;
                    lp.rightMargin = 5;
                    lp.topMargin = 5;
                    lp.bottomMargin = 5;
                    userLabelLayout.setVisibility(View.VISIBLE);
                    tvNoLabel.setVisibility(View.GONE);
                    userLabelLayout.addView(view, lp);
                    ivDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            userLabelList.remove(myTagInfo);
                            userLabelLayout.removeView(view);
                            checkUserLabelEmpty();
                        }
                    });
                        }
                    });
                }
            }

    public void addLabelResp(AddUserLabelVo response) {
        if (response == null) {
            return;
        }
        if (ListUtils.isEmpty(this.userLabelList)) {
            tvNoLabel.setVisibility(View.GONE);
            userLabelLayout.setVisibility(View.VISIBLE);
        }
        AddUserLabelVo.TagInfo tagInfo = response.getTag();
        final UserLabelVo.MyTagInfo myTagInfo = new UserLabelVo.MyTagInfo();
        myTagInfo.setTagId(tagInfo.getTagId());
        myTagInfo.setName(tagInfo.getName());
        this.userLabelList.add(myTagInfo);

        final View view = LayoutInflater.from(getActivity()).inflate(R.layout.grid_item_user_label, null);
        TextView tvLabel = (TextView) view.findViewById(R.id.id_label);
        ImageView ivDelete = (ImageView) view.findViewById(R.id.id_delete);
        tvLabel.setText(myTagInfo.getName());
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        lp.leftMargin = 5;
        lp.rightMargin = 5;
        lp.topMargin = 5;
        lp.bottomMargin = 5;
        userLabelLayout.addView(view, lp);

        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLabelList.remove(myTagInfo);
                userLabelLayout.removeView(view);
                checkUserLabelEmpty();
            }
        });
        etEditLabel.setText("");
        showShortToast("添加成功");
    }

    private void checkUserLabelEmpty() {
        if (ListUtils.isEmpty(userLabelList)) {
            userLabelLayout.setVisibility(View.GONE);
            tvNoLabel.setVisibility(View.VISIBLE);
        } else {
            userLabelLayout.setVisibility(View.VISIBLE);
            tvNoLabel.setVisibility(View.GONE);
        }
    }

    public void saveLabelSuccess() {
        showShortToast("保存成功");
        Intent intent = new Intent();
        intent.putExtra(IntentKey.INTENT_KEY_USER_LABEL, userLabelList);
        finishActivity(intent);
    }
}
