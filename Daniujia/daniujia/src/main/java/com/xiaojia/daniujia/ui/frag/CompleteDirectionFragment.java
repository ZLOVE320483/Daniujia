package com.xiaojia.daniujia.ui.frag;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xiaojia.daniujia.IntentKey;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.domain.resp.ConcreteDirection;
import com.xiaojia.daniujia.domain.resp.ConcreteDirectionRespVo;
import com.xiaojia.daniujia.domain.resp.DirectionRelativeIndustryRespVo;
import com.xiaojia.daniujia.domain.resp.RelatedIndustry;
import com.xiaojia.daniujia.domain.resp.Speciality;
import com.xiaojia.daniujia.ui.adapters.DirectionRelativeIndustryAdapter;
import com.xiaojia.daniujia.ui.control.CompleteDirectionControl;
import com.xiaojia.daniujia.ui.views.FlowLayout;
import com.xiaojia.daniujia.utils.ApplicationUtil;
import com.xiaojia.daniujia.utils.DialogManager;
import com.xiaojia.daniujia.utils.EditUtils;
import com.xiaojia.daniujia.utils.ListUtils;
import com.xiaojia.daniujia.utils.ScreenUtils;
import com.xiaojia.daniujia.utils.UIUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZLOVE on 2016/6/30.
 */
public class CompleteDirectionFragment extends BaseFragment implements View.OnClickListener {

    private CompleteDirectionControl control;
    private TextView tvConfirm;

    private ImageView ivIcon;
    private TextView tvMainDirection;
    private TextView tvSubDirection;

    private View tipContainer;
    private TextView tvSubDirectionTip;
    private View otherSubDirectionContainer;
    private EditText etOtherSubDirection;
    private ImageView ivDelete;
    private EditText etWorkTime;

    private View container;
    private FlowLayout relativeIndustryLayout;
    private EditText etIndustry;
    private Button btnAddIndustry;
    private View searchView;

    private PopupWindow relativeIndustryPop;
    private ListView relativeIndustryListView;
    private DirectionRelativeIndustryAdapter adapter;
    private List<RelatedIndustry> relativeIndustryList = new ArrayList<>();

    private FlowLayout selectedConcreteDirectionLayout;
    private FlowLayout concreteDirectionLayout;
    private View specificSearchContainer;
    private EditText etConcreteDirection;
    private Button btnAddConcreteDirection;

    private Speciality speciality = null;
    private int specId = 0;
    private int subId = -1;
    private String mainDirection = "";
    private String mainDirectionEditUrl = "";
    private String subDirection = "";
    private String otherSubDirection = "";
    private String workAge = "";

    private List<RelatedIndustry> addRelativeIndustryList = new ArrayList<>();
    private List<ConcreteDirection> addConcreteDirectionBeanList = new ArrayList<>();

    private ImageView ivMainDirectionHelp;
    private TextView tvSubDirectionHelp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        control = new CompleteDirectionControl();
        setControl(control);
    }

    @Override
    protected int getInflateLayout() {
        return R.layout.frag_complete_direction;
    }

    @Override
    protected void setUpView(View view) {
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            if (intent.hasExtra(IntentKey.INTENT_KEY_MAIN_DIRECTION)) {
                mainDirection = intent.getStringExtra(IntentKey.INTENT_KEY_MAIN_DIRECTION);
            }
            if (intent.hasExtra(IntentKey.INTENT_KEY_MAIN_DIRECTION_EDIT_URL)) {
                mainDirectionEditUrl = intent.getStringExtra(IntentKey.INTENT_KEY_MAIN_DIRECTION_EDIT_URL);
            }
            if (intent.hasExtra(IntentKey.INTENT_KEY_SUB_DIRECTION)) {
                subDirection = intent.getStringExtra(IntentKey.INTENT_KEY_SUB_DIRECTION);
            }
            if (intent.hasExtra(IntentKey.INTENT_KEY_SUB_ID)) {
                subId = intent.getIntExtra(IntentKey.INTENT_KEY_SUB_ID, -1);
            }
            if (intent.hasExtra(IntentKey.INTENT_KEY_DIRECTION_ITEM)) {
                speciality = (Speciality) intent.getSerializableExtra(IntentKey.INTENT_KEY_DIRECTION_ITEM);
            }
        }

        setBackButton(view.findViewById(R.id.id_back));
        ((TextView) view.findViewById(R.id.id_title)).setText("完善擅长方向");
        tvConfirm = (TextView) view.findViewById(R.id.title_right);
        tvConfirm.setText("确定");
        tvConfirm.setTextColor(ApplicationUtil.getApplicationContext().getResources().getColor(R.color.second_text_color));
        tvConfirm.setVisibility(View.VISIBLE);
        tvConfirm.setOnClickListener(this);

        ivIcon = (ImageView) view.findViewById(R.id.icon);
        tvMainDirection = (TextView) view.findViewById(R.id.main_direction);
        tvSubDirection = (TextView) view.findViewById(R.id.sub_direction);
        tipContainer = view.findViewById(R.id.tip_container);
        tvSubDirectionTip = (TextView) view.findViewById(R.id.sub_direction_tip);
        otherSubDirectionContainer = view.findViewById(R.id.other_sub_direction_container);
        etOtherSubDirection = (EditText) view.findViewById(R.id.et_other_sub_direction);
        ivDelete = (ImageView) view.findViewById(R.id.iv_delete);
        etWorkTime = (EditText) view.findViewById(R.id.work_time);
        container = view.findViewById(R.id.container);
        relativeIndustryLayout = (FlowLayout) view.findViewById(R.id.relative_industry);
        etIndustry = (EditText) view.findViewById(R.id.et_industry);
        btnAddIndustry = (Button) view.findViewById(R.id.id_add_industry);
        searchView = view.findViewById(R.id.search_container);

        selectedConcreteDirectionLayout = (FlowLayout) view.findViewById(R.id.select_specific_layout);
        concreteDirectionLayout = (FlowLayout) view.findViewById(R.id.un_select_specific_layout);
        specificSearchContainer = view.findViewById(R.id.specific_search_container);
        etConcreteDirection = (EditText) view.findViewById(R.id.et_specific);
        btnAddConcreteDirection = (Button) view.findViewById(R.id.id_add_specific);

        ivMainDirectionHelp = (ImageView) view.findViewById(R.id.id_help);
        tvSubDirectionHelp = (TextView) view.findViewById(R.id.id_help_2);

        ivDelete.setOnClickListener(this);
        tipContainer.setOnClickListener(this);
        ivMainDirectionHelp.setOnClickListener(this);
        tvSubDirectionHelp.setOnClickListener(this);

        if (!TextUtils.isEmpty(mainDirectionEditUrl)) {
            Picasso.with(getActivity()).load(mainDirectionEditUrl).resize(ScreenUtils.dip2px(48), ScreenUtils.dip2px(48)).into(ivIcon);
        }
        tvMainDirection.setText(mainDirection);
        tvSubDirection.setText(subDirection);

        if (subDirection.equals("其他")) {
            tipContainer.setVisibility(View.VISIBLE);
            ivMainDirectionHelp.setVisibility(View.VISIBLE);
        }

        btnAddIndustry.setOnClickListener(this);
        btnAddIndustry.setClickable(false);

        btnAddConcreteDirection.setOnClickListener(this);
        btnAddConcreteDirection.setClickable(false);

        EditUtils editUtils = new EditUtils(20, "具体描述不要超过20个字");
        editUtils.init(etOtherSubDirection, null);

        etOtherSubDirection.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String content = etOtherSubDirection.getText().toString().trim();
                if (!hasFocus) {
                    if (!TextUtils.isEmpty(content)) {
                        tvSubDirectionTip.setText(content);
                        tvSubDirectionTip.setTextColor(ApplicationUtil.getApplicationContext().getResources().getColor(R.color.first_text_color));
                    } else {
                        tvSubDirectionTip.setText("请具体描述其他");
                        tvSubDirectionTip.setTextColor(ApplicationUtil.getApplicationContext().getResources().getColor(R.color.third_text_color));
                    }
                    tvSubDirectionTip.setGravity(Gravity.CENTER);
                    tipContainer.setVisibility(View.VISIBLE);
                    otherSubDirectionContainer.setVisibility(View.GONE);
                } else {
                    if (!TextUtils.isEmpty(content)) {
                        ivDelete.setVisibility(View.VISIBLE);
                    } else {
                        ivDelete.setVisibility(View.GONE);
                    }
                }
            }
        });

        etOtherSubDirection.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String content = s.toString().trim();
                if (!TextUtils.isEmpty(content)) {
                    ivDelete.setVisibility(View.VISIBLE);
                } else {
                    ivDelete.setVisibility(View.GONE);
                }
            }
        });

        etIndustry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String keyword = s.toString().trim();
                if (!TextUtils.isEmpty(keyword)) {
                    btnAddIndustry.setClickable(true);
                    btnAddIndustry.setBackgroundResource(R.drawable.shape_add_label);
                    btnAddIndustry.setTextColor(ApplicationUtil.getApplicationContext().getResources().getColor(R.color.blue_text));
                    control.requestRelativeIndustry(keyword);
                } else {
                    btnAddIndustry.setClickable(false);
                    btnAddIndustry.setBackgroundResource(R.drawable.shape_add_label_unchecked);
                    btnAddIndustry.setTextColor(ApplicationUtil.getApplicationContext().getResources().getColor(R.color.second_text_color));
                    if (relativeIndustryPop != null) {
                        relativeIndustryPop.dismiss();
                    }
                }
            }
        });

        etConcreteDirection.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString().trim())) {
                    btnAddConcreteDirection.setClickable(true);
                    btnAddConcreteDirection.setBackgroundResource(R.drawable.shape_add_label);
                    btnAddConcreteDirection.setTextColor(ApplicationUtil.getApplicationContext().getResources().getColor(R.color.blue_text));
                } else {
                    btnAddConcreteDirection.setClickable(false);
                    btnAddConcreteDirection.setBackgroundResource(R.drawable.shape_add_label_unchecked);
                    btnAddConcreteDirection.setTextColor(ApplicationUtil.getApplicationContext().getResources().getColor(R.color.second_text_color));
                }
            }
        });

        if (speciality != null) {
            setSpecialityData();
        }

        if (subId != -1) {
            control.requestConcreteDirection(subId);
        }
    }

    private void setSpecialityData() {
        specId = speciality.getSpecId();
        subId = speciality.getSubId();
        mainDirection = speciality.getMainName();
        subDirection = speciality.getSubName();
        otherSubDirection = speciality.getWriteName();
        workAge = String.valueOf(speciality.getWorkage());
        mainDirectionEditUrl = speciality.getMainEditIconUrl();
        if (!ListUtils.isEmpty(speciality.getRelatedIndustrys())) {
            addRelativeIndustryList.addAll(speciality.getRelatedIndustrys());
        }
        if (!ListUtils.isEmpty(speciality.getConcreteDirections())) {
            addConcreteDirectionBeanList.addAll(speciality.getConcreteDirections());
        }

        if (!TextUtils.isEmpty(mainDirectionEditUrl)) {
            Picasso.with(getActivity()).load(mainDirectionEditUrl).resize(ScreenUtils.dip2px(48), ScreenUtils.dip2px(48)).into(ivIcon);
        }

        tvMainDirection.setText(mainDirection);
        tvSubDirection.setText(subDirection);

        if (TextUtils.isEmpty(mainDirection) || mainDirection.equals("其他")) {
            tvMainDirection.setText("其他");
            otherSubDirectionContainer.setVisibility(View.VISIBLE);
            etOtherSubDirection.setText(speciality.getWriteName());
        }
        if (TextUtils.isEmpty(subDirection) || subDirection.equals("其他")) {
            tvSubDirection.setText("其他");
            otherSubDirectionContainer.setVisibility(View.VISIBLE);
            etOtherSubDirection.setText(speciality.getWriteName());
            ivMainDirectionHelp.setVisibility(View.VISIBLE);
        }
        etWorkTime.setText(workAge);

        for (final RelatedIndustry industry : addRelativeIndustryList) {
            final View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.grid_item_user_label, null);
            TextView tvLabel = (TextView) contentView.findViewById(R.id.id_label);
            ImageView ivDelete = (ImageView) contentView.findViewById(R.id.id_delete);
            tvLabel.setText(industry.getIndustryName());
            ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.leftMargin = 3;
            lp.rightMargin = 3;
            lp.topMargin = 5;
            lp.bottomMargin = 5;
            relativeIndustryLayout.addView(contentView, lp);
            ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    relativeIndustryLayout.removeView(contentView);
                    addRelativeIndustryList.remove(industry);
                    container.postInvalidate();
                }
            });
        }

        for (final ConcreteDirection concreteDirection : addConcreteDirectionBeanList) {
            final View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.grid_item_user_label, null);
            TextView tvLabel = (TextView) contentView.findViewById(R.id.id_label);
            ImageView ivDelete = (ImageView) contentView.findViewById(R.id.id_delete);
            tvLabel.setText(concreteDirection.getDirectionName());
            ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.leftMargin = 3;
            lp.rightMargin = 3;
            lp.topMargin = 5;
            lp.bottomMargin = 5;
            selectedConcreteDirectionLayout.addView(contentView, lp);
            ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedConcreteDirectionLayout.removeView(contentView);
                    addConcreteDirectionBeanList.remove(concreteDirection);
                    container.postInvalidate();
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btnAddIndustry) {
            String industry = etIndustry.getText().toString().trim();
            RelatedIndustry relativeIndustry = new RelatedIndustry();
            relativeIndustry.setIndustryId(0);
            relativeIndustry.setIndustryName(industry);
            addRelativeIndustry(relativeIndustry);
        } else if (v == btnAddConcreteDirection) {
            String concreteDirection = etConcreteDirection.getText().toString().trim();
            ConcreteDirection bean = new ConcreteDirection();
            bean.setDirectionId(0);
            bean.setDirectionName(concreteDirection);
            addConcreteDirection(bean);
        } else if (v == tvConfirm) {
            if (subDirection.equals("其他")) {
                otherSubDirection = etOtherSubDirection.getText().toString().trim();
                if (TextUtils.isEmpty(otherSubDirection)) {
                    showShortToast("请填写擅长方向");
                    return;
                }
            }
            workAge = etWorkTime.getText().toString().trim();
            if (TextUtils.isEmpty(workAge)) {
                showShortToast("请填写工作年限");
                return;
            }
            int work = Integer.parseInt(workAge);
            if (work > 80 || work <= 0){
                showShortToast("工作年限填写不符合要求");
                return;
            }
            if (ListUtils.isEmpty(addRelativeIndustryList)) {
                showShortToast("请添加相关行业");
                return;
            }
            control.addOrUpdateDirectionRequest(specId, subId, otherSubDirection, workAge, addRelativeIndustryList, addConcreteDirectionBeanList);
        } else if (v == ivMainDirectionHelp) {
            DialogManager.showTipDialog(getActivity(), "因为您子方向选择了其他,为了让用户通过您选择的主方向和子方向快速了解您的擅长方向,请具体描述一下此'其他'是指什么。");
        } else if (v == tvSubDirectionHelp) {
            DialogManager.showTipDialog(getActivity(), "如果您选择的主方向和子方向范围偏大,您可以选择或者自己添加具体方向,让用户对此擅长方向有更明确的认识。");
        } else if (v == tipContainer) {
            tipContainer.setVisibility(View.GONE);
            otherSubDirectionContainer.setVisibility(View.VISIBLE);
            etOtherSubDirection.requestFocus();
            UIUtil.showKeyboard(getActivity(), etOtherSubDirection);
        } else if (v == ivDelete) {
            etOtherSubDirection.setText("");
        }
    }

    public void handleSearchResp(DirectionRelativeIndustryRespVo result) {
        if (result == null) {
            return;
        }
        if (!ListUtils.isEmpty(result.getResultIndustrys())) {
            showRelativeIndustryPop(result.getResultIndustrys());
        } else {
            if (relativeIndustryPop != null) {
                relativeIndustryPop.dismiss();
            }
        }
    }

    public void setConcreteDirectionData(ConcreteDirectionRespVo respVo) {
        if (respVo == null) {
            return;
        }
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = 3;
        lp.rightMargin = 3;
        lp.topMargin = 5;
        lp.bottomMargin = 5;

        List<ConcreteDirection> directionBeans = respVo.getConcreteDirections();
        if (ListUtils.isEmpty(directionBeans)) {
            final ImageView imageView = new ImageView(getActivity());
            imageView.setImageResource(R.drawable.good_direction_search_hide);
            imageView.setPadding(0, 8, 0, 0);
            imageView.setScaleType(ImageView.ScaleType.CENTER);
            concreteDirectionLayout.addView(imageView, lp);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageView.setVisibility(View.GONE);
                    concreteDirectionLayout.setVisibility(View.GONE);
                    specificSearchContainer.setVisibility(View.VISIBLE);
                }
            });
            return;
        }
        for (final ConcreteDirection bean : directionBeans) {
            final View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.grid_item_common_label, null);
            TextView tvLabel = (TextView) contentView.findViewById(R.id.id_content);
            tvLabel.setText(bean.getDirectionName());
            concreteDirectionLayout.addView(contentView, lp);

            contentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addConcreteDirection(bean);
                }
            });
        }
        final ImageView imageView = new ImageView(getActivity());
        imageView.setImageResource(R.drawable.good_direction_search_hide);
        imageView.setPadding(0, 8, 0, 0);
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        concreteDirectionLayout.addView(imageView, lp);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setVisibility(View.GONE);
                specificSearchContainer.setVisibility(View.VISIBLE);
            }
        });
    }

    private void showRelativeIndustryPop(List<RelatedIndustry> tmpList) {
        if (relativeIndustryPop == null) {
            View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.common_view_list, null);
            relativeIndustryListView = (ListView) contentView.findViewById(R.id.id_list);
            adapter = new DirectionRelativeIndustryAdapter(relativeIndustryList, getActivity());
            relativeIndustryListView.setAdapter(adapter);
            relativeIndustryPop = new PopupWindow(contentView, searchView.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        relativeIndustryList.clear();
        relativeIndustryList.addAll(tmpList);
        adapter.notifyDataSetChanged();
        relativeIndustryPop.showAsDropDown(searchView);

        relativeIndustryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final RelatedIndustry item = relativeIndustryList.get(position);
                addRelativeIndustry(item);
            }
        });
    }

    private void addRelativeIndustry(final RelatedIndustry industry) {
        if (!ListUtils.isEmpty(addRelativeIndustryList)) {
            for (RelatedIndustry item : addRelativeIndustryList) {
                if (industry.getIndustryName().equals(item.getIndustryName())) {
                    showShortToast("您已经添加过该行业");
                    return;
                }
            }
        }

        final View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.grid_item_user_label, null);
        TextView tvLabel = (TextView) contentView.findViewById(R.id.id_label);
        ImageView ivDelete = (ImageView) contentView.findViewById(R.id.id_delete);
        tvLabel.setText(industry.getIndustryName());
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = 5;
        lp.rightMargin = 5;
        lp.topMargin = 5;
        lp.bottomMargin = 5;
        addRelativeIndustryList.add(industry);
        relativeIndustryLayout.addView(contentView, lp);
        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeIndustryLayout.removeView(contentView);
                addRelativeIndustryList.remove(industry);
                container.postInvalidate();
            }
        });
        if (relativeIndustryPop != null) {
            relativeIndustryPop.dismiss();
        }
        etIndustry.setText("");
    }

    private void addConcreteDirection(final ConcreteDirection bean) {
        for (ConcreteDirection item : addConcreteDirectionBeanList) {
            if (item.getDirectionName().equals(bean.getDirectionName())) {
                showShortToast("您已添加过该方向");
                return;
            }
        }
        if (addConcreteDirectionBeanList.size() >= 3) {
            showShortToast("最多添加三个具体方向");
            return;
        }
        etConcreteDirection.setText("");
        final View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.grid_item_user_label, null);
        TextView tvLabel = (TextView) contentView.findViewById(R.id.id_label);
        ImageView ivDelete = (ImageView) contentView.findViewById(R.id.id_delete);
        tvLabel.setText(bean.getDirectionName());
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = 5;
        lp.rightMargin = 5;
        lp.topMargin = 5;
        lp.bottomMargin = 5;
        selectedConcreteDirectionLayout.addView(contentView, lp);
        addConcreteDirectionBeanList.add(bean);
        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedConcreteDirectionLayout.removeView(contentView);
                addConcreteDirectionBeanList.remove(bean);
                container.postInvalidate();
            }
        });
    }
}
