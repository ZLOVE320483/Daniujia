package com.xiaojia.daniujia.ui.frag;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaojia.daniujia.IntentKey;
import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.domain.resp.ConsultEntity;
import com.xiaojia.daniujia.domain.resp.ExpertSearchHotKeysVo;
import com.xiaojia.daniujia.domain.resp.ExpertSearchVo;
import com.xiaojia.daniujia.ui.act.ActCommitSuccess;
import com.xiaojia.daniujia.ui.act.ActExpertHome;
import com.xiaojia.daniujia.ui.act.ActLookForExpertByCondition;
import com.xiaojia.daniujia.ui.act.ActSpeechSearch;
import com.xiaojia.daniujia.ui.adapters.HomeExpertAdapter;
import com.xiaojia.daniujia.ui.adapters.SearchRecordAdapter;
import com.xiaojia.daniujia.ui.control.ExpertSearchControl;
import com.xiaojia.daniujia.ui.views.FlowLayout;
import com.xiaojia.daniujia.ui.views.MyListView;
import com.xiaojia.daniujia.ui.views.XListView;
import com.xiaojia.daniujia.utils.JsonUtil;
import com.xiaojia.daniujia.utils.ListUtils;
import com.xiaojia.daniujia.utils.PatternUtil;
import com.xiaojia.daniujia.utils.ScreenUtils;
import com.xiaojia.daniujia.utils.SysUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZLOVE on 2016/4/26.
 */
public class ExpertSearchFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ExpertSearchControl control;

    private EditText etSearch;
    private ImageView ivClearContent;
    private FlowLayout labelLayout;
    private MyListView recordList;
    private SearchRecordAdapter recordAdapter;
    private View speechSearchView;

    private XListView searchListView;
    private HomeExpertAdapter searchAdapter;
    private List<ConsultEntity> searchList = new ArrayList<>();

    private LinearLayout noSearchResContainer;
    private EditText etUserRequest;
    private EditText etUserMobile;
    private Button btnSubmit;

    private List<String> records = new ArrayList<>();
    private View noRecordContainer;
    private TextView tvClearRecord;
    private String record = "";
    private String searchKey;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        control = new ExpertSearchControl();
        setControl(control);
    }

    @Override
    protected int getInflateLayout() {
        return R.layout.frag_search_expert;
    }

    @Override
    protected void setUpView(View view) {
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            if (intent.hasExtra(IntentKey.REQ_CODE_SPEECH_SEARCH_KEYWORD)) {
                searchKey = intent.getStringExtra(IntentKey.REQ_CODE_SPEECH_SEARCH_KEYWORD);
            }
        }
        setBackButton(view.findViewById(R.id.id_back));
        etSearch = (EditText) view.findViewById(R.id.et_search);
        ivClearContent = (ImageView) view.findViewById(R.id.clear);
        labelLayout = (FlowLayout) view.findViewById(R.id.hot_label_layout);
        recordList = (MyListView) view.findViewById(R.id.id_list);
        noRecordContainer = view.findViewById(R.id.no_record);
        tvClearRecord = (TextView) view.findViewById(R.id.id_clear);

        recordAdapter = new SearchRecordAdapter(records, getActivity());
        recordList.setAdapter(recordAdapter);
        ivClearContent.setOnClickListener(this);
        tvClearRecord.setOnClickListener(this);
        recordList.setOnItemClickListener(this);
        speechSearchView = view.findViewById(R.id.voice_search);
        speechSearchView.setOnClickListener(this);

        searchListView = (XListView) view.findViewById(R.id.expert_search_list);
        searchAdapter = new HomeExpertAdapter(searchList, getActivity());
        searchListView.setAdapter(searchAdapter);
        searchListView.setPullRefreshEnable(false);

        noSearchResContainer = (LinearLayout) view.findViewById(R.id.no_search_res_container);

        if (!TextUtils.isEmpty(searchKey)) {
            etSearch.setText(searchKey);
            etSearch.setSelection(searchKey.length());
        }

        record = SysUtil.getPref(PrefKeyConst.PREF_EXPERT_SEARCH_RECORD);
        if (TextUtils.isEmpty(record)) {
            noRecordContainer.setVisibility(View.VISIBLE);
            recordList.setVisibility(View.GONE);
            tvClearRecord.setVisibility(View.GONE);
        } else {
            noRecordContainer.setVisibility(View.GONE);
            recordList.setVisibility(View.VISIBLE);
            tvClearRecord.setVisibility(View.VISIBLE);
            records.addAll(ListUtils.getStringList(record));
            recordAdapter.notifyDataSetChanged();
        }

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 0) {
                    ivClearContent.setVisibility(View.GONE);
                    searchListView.setVisibility(View.GONE);
                    noSearchResContainer.removeAllViews();
                    noSearchResContainer.setVisibility(View.GONE);
                } else {
                    ivClearContent.setVisibility(View.VISIBLE);
                }
            }
        });

        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String searchContent = etSearch.getText().toString().trim();
                    saveSearchRecord(searchContent);
                    search(searchContent);
                    return true;
                } else {
                    return false;
                }
            }
        });

        String hotLabels = SysUtil.getPref(PrefKeyConst.PREF_EXPERT_SEARCH_HOT_LABEL);
        long lastReqTime = SysUtil.getLongPref(PrefKeyConst.PREF_CURRENT_TIME);
        if (TextUtils.isEmpty(hotLabels) || lastReqTime == 0 || (System.currentTimeMillis() - lastReqTime > 24 * 60 * 60 * 1000)) {
            control.requestHotKeys();
        } else {
            try {
                ExpertSearchHotKeysVo result = JsonUtil.getCorrectJsonResult(hotLabels, ExpertSearchHotKeysVo.class).getData();
                setHotKey(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (ListUtils.isEmpty(searchList)) {
                    return;
                }
                ConsultEntity entity = searchList.get(position - 1);
                if (entity == null) {
                    return;
                }
                Intent intent = new Intent(getActivity(), ActExpertHome.class);
                intent.putExtra(IntentKey.INTENT_KEY_EXPERT_ID, entity.getConsultantId());
                startActivity(intent);
            }
        });
    }

    private void saveSearchRecord(String searchContent) {
        record = SysUtil.getPref(PrefKeyConst.PREF_EXPERT_SEARCH_RECORD);
        if (!TextUtils.isEmpty(record) && record.contains(searchContent)) {
            return;
        }
        records.add(searchContent);
        SysUtil.savePref(PrefKeyConst.PREF_EXPERT_SEARCH_RECORD, ListUtils.join(records));
    }

    private void search(String key) {
        if (TextUtils.isEmpty(key)) {
            showShortToast("请输入关键字");
            return;
        }
        searchKey = key;
        Intent intent = new Intent();
        intent.putExtra(IntentKey.INTENT_KEY_SPEECH_SEARCH_RESULT, searchKey);
        finishActivity(intent);
    }

    @Override
    public void onClick(View v) {
        if (v == ivClearContent) {
            etSearch.setText("");
        } else if (v == tvClearRecord) {
            record = "";
            records.clear();
            recordAdapter.notifyDataSetChanged();
            SysUtil.savePref(PrefKeyConst.PREF_EXPERT_SEARCH_RECORD, "");
            recordList.setVisibility(View.GONE);
            noRecordContainer.setVisibility(View.VISIBLE);
        } else if (v == btnSubmit) {
            String userRequestContent = etUserRequest.getText().toString().trim();
            String userMobile = etUserMobile.getText().toString().trim();
            if (TextUtils.isEmpty(userRequestContent)) {
                showShortToast("请描述您的具体要求");
                return;
            }
            if (!SysUtil.getBooleanPref(PrefKeyConst.PREF_IS_LOGIN)) {
                if (TextUtils.isEmpty(userMobile)) {
                    showShortToast("请输入手机号");
                    return;
                } else if (!PatternUtil.isPhoneNum(userMobile)) {
                    showShortToast("手机号不合法");
                    return;
                }
            }
            control.submitSearchRequest(userMobile, userRequestContent);
        } else if (v == speechSearchView) {
            Intent intent = new Intent(getActivity(), ActSpeechSearch.class);
            startActivityForResult(intent, IntentKey.REQ_CODE_SPEECH_SEARCH);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (ListUtils.isEmpty(records)) {
            return;
        }
        String key = records.get(position);
        etSearch.setText(key);
        search(key);
    }

    public void setHotKey(ExpertSearchHotKeysVo data) {
        if (data == null) {
            return;
        }

        if (data.getKeywords() == null || data.getKeywords().size() == 0)
            return;

        for (int i = 0; i < data.getKeywords().size(); i++) {
            TextView hotKey = new TextView(getContext());
            final ExpertSearchHotKeysVo.HotKeyInfo info = data.getKeywords().get(i);
            ViewGroup.MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            margin.setMargins(0, 0, ScreenUtils.dip2px(18), ScreenUtils.dip2px(10));
            hotKey.setText(info.getKeyword());
            hotKey.setTextSize(15);
            hotKey.setTextColor(Color.parseColor("#333333"));
            hotKey.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchHotKey(info.getKeyword());
                    saveSearchRecord(info.getKeyword());
                }
            });

            labelLayout.addView(hotKey, margin);
        }

    }

    public void setSearchData(ExpertSearchVo data) {
        if (data == null) {
            return;
        }
        List<ConsultEntity> searchRes = data.getConsultants();
        if (ListUtils.isEmpty(searchRes)) {
            searchListView.setVisibility(View.GONE);
            View view = LayoutInflater.from(getContext()).inflate(R.layout.view_expert_search_empty, null);
            TextView tvContentTip = (TextView) view.findViewById(R.id.search_content_tip);
            tvContentTip.setText("未找到‘" + searchKey + "’方面的专家");
            etUserRequest = (EditText) view.findViewById(R.id.id_user_request);
            etUserMobile = (EditText) view.findViewById(R.id.id_user_phone);
            btnSubmit = (Button) view.findViewById(R.id.submit);
            if (SysUtil.getBooleanPref(PrefKeyConst.PREF_IS_LOGIN)) {
                etUserMobile.setVisibility(View.GONE);
            } else {
                etUserMobile.setVisibility(View.VISIBLE);
            }
            btnSubmit.setOnClickListener(this);
            noSearchResContainer.addView(view);
            noSearchResContainer.setVisibility(View.VISIBLE);
            return;
        }
        if (!ListUtils.isEmpty(searchList)) {
            noSearchResContainer.removeAllViews();
            noSearchResContainer.setVisibility(View.GONE);
            searchList.clear();
        }
        searchListView.setVisibility(View.VISIBLE);
        searchList.addAll(searchRes);
        searchAdapter.notifyDataSetChanged();
    }

    public void searchHotKey(String hotKey) {
        etSearch.setText(hotKey);
        search(hotKey);
    }

    public void commitSuccess() {
        Intent intent = new Intent(getActivity(), ActCommitSuccess.class);
        startActivity(intent);
        finishActivity();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentKey.REQ_CODE_SPEECH_SEARCH) {
            if (data != null && data.hasExtra(IntentKey.INTENT_KEY_SPEECH_SEARCH_RESULT)) {
                String searchContent = data.getStringExtra(IntentKey.INTENT_KEY_SPEECH_SEARCH_RESULT);
                Intent intent = new Intent(getActivity(), ActLookForExpertByCondition.class);
                intent.putExtra(IntentKey.CONDITION_KEYWORD, searchContent);
                intent.putExtra(IntentKey.CONDITION_KEYWORD_FROM_VOICE, true);
                startActivity(intent);
            }
        }
    }
}
