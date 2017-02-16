package com.xiaojia.daniujia.ui.frag;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xiaojia.daniujia.ExtraConst;
import com.xiaojia.daniujia.IntentKey;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.base.App;
import com.xiaojia.daniujia.domain.resp.ConsultEntity;
import com.xiaojia.daniujia.domain.resp.HomeEntity;
import com.xiaojia.daniujia.domain.resp.HomeNewBeanVo;
import com.xiaojia.daniujia.domain.resp.ShowActRespVo;
import com.xiaojia.daniujia.ui.act.ActExpertHome;
import com.xiaojia.daniujia.ui.act.ActExpertSearch;
import com.xiaojia.daniujia.ui.act.ActLookForExpert;
import com.xiaojia.daniujia.ui.act.ActLookForExpertByCondition;
import com.xiaojia.daniujia.ui.act.ActSpeechSearch;
import com.xiaojia.daniujia.ui.act.WebActivity;
import com.xiaojia.daniujia.ui.adapters.HomeExpertAdapter;
import com.xiaojia.daniujia.ui.control.HomeControl;
import com.xiaojia.daniujia.ui.views.PinnedSectionListView;
import com.xiaojia.daniujia.ui.views.RollViewPager;
import com.xiaojia.daniujia.ui.views.XListView;
import com.xiaojia.daniujia.utils.DialogManager;
import com.xiaojia.daniujia.utils.ListUtils;
import com.xiaojia.daniujia.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/25
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener, RollViewPager.OnItemClickListener, DialogManager.JoinActListener, XListView.IXListViewListener {

    private PinnedSectionListView list;
    private HomeControl control;
    private View header;
    private RollViewPager roll;

    private HomeExpertAdapter mAdapter;
    private List<ConsultEntity> consultantsEntityList = new ArrayList<>();
    private int headerViewsCount;
    private HomeNewBeanVo homeBeanVo;
    private String actUrl;
    private String actTitle;

    private JumpMeTabListener listener;

    private int jumpToLookForTag = 0;
    private String searchContent;

    public void setListener(JumpMeTabListener listener) {
        this.listener = listener;
    }

    @Override
    protected int getInflateLayout() {
        return R.layout.home_frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        control = new HomeControl();
        setControl(control);
    }

    @Override
    protected void setUpView(View view) {
        View searchView = view.findViewById(R.id.search_container);
        searchView.setOnClickListener(this);
        View speechSearchView = view.findViewById(R.id.speech_search);
        speechSearchView.setOnClickListener(this);

        list = (PinnedSectionListView) view.findViewById(R.id.list);
        list.setOnItemClickListener(this);
        list.setPullLoadEnable(false);
        list.setPullRefreshEnable(true);
        list.setXListViewListener(this);

        header = getActivity().getLayoutInflater().inflate(R.layout.home_header, null);
        View foot = getActivity().getLayoutInflater().inflate(R.layout.home_foot, null);

        list.addFooterView(foot, null, true);
        list.addHeaderView(header);

        roll = (RollViewPager) header.findViewById(R.id.roll);
        roll.setOnItemClickLstener(this);
        mAdapter = new HomeExpertAdapter(consultantsEntityList, getActivity());
        list.setAdapter(mAdapter);
        control.initHomeData(true);
        control.requestShowAct();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (ListUtils.isEmpty(consultantsEntityList) && !hidden && control != null) {
            control.initHomeData(true);
        }

        if (hidden) {
            roll.setRunning(false);
        } else {
            roll.startRoll();
        }
    }

    public void setRollStop(boolean isStop) {
        if (isStop) {
            roll.setRunning(false);
        } else {
            roll.startRoll();
        }
    }

    private void initDots(int size) {
        if (!isAdded()) {
            return;
        }
        int padding = ScreenUtils.dip2px(4);
        LinearLayout linearLayout = (LinearLayout) header.findViewById(R.id.iv_dot_ll);
        linearLayout.removeAllViews();
        List<ImageView> iv_dots = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            ImageView iv_dot = new ImageView(getActivity());
            if (i == 0) {
                iv_dot.setImageResource(R.drawable.roll_dot_checked);
            } else {
                iv_dot.setImageResource(R.drawable.roll_dot_unchecked);
            }
            iv_dot.setPadding(padding, padding, padding, padding);
            linearLayout.addView(iv_dot);
            iv_dots.add(iv_dot);
        }
        roll.initDots(iv_dots);
    }

    private void initHorizontal(List<HomeEntity.TopicEntity> topics) {
        if (topics == null)
            return;

        ImageView imageView = (ImageView) header.findViewById(R.id.image1);
        ImageView imageView2 = (ImageView) header.findViewById(R.id.image2);
        ImageView imageView3 = (ImageView) header.findViewById(R.id.image3);
        ImageView imageView4 = (ImageView) header.findViewById(R.id.image4);

        TextView textView1 = (TextView) header.findViewById(R.id.text1);
        TextView textView2 = (TextView) header.findViewById(R.id.text2);
        TextView textView3 = (TextView) header.findViewById(R.id.text3);
        TextView textView4 = (TextView) header.findViewById(R.id.text4);

        View container1 = header.findViewById(R.id.container1);
        View container2 = header.findViewById(R.id.container2);
        View container3 = header.findViewById(R.id.container3);
        View container4 = header.findViewById(R.id.container4);

        Picasso.with(App.get()).load(topics.get(0).getIconUrl()).into(imageView);
        Picasso.with(App.get()).load(topics.get(1).getIconUrl()).into(imageView2);
        Picasso.with(App.get()).load(topics.get(2).getIconUrl()).into(imageView3);
        imageView4.setImageResource(R.drawable.all_expert);

        textView1.setText(topics.get(0).getDesc());
        textView2.setText(topics.get(1).getDesc());
        textView3.setText(topics.get(2).getDesc());
        textView4.setText("全部专家");

        container1.setOnClickListener(this);
        container2.setOnClickListener(this);
        container3.setOnClickListener(this);
        container4.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.search_container:
                intent = new Intent(getActivity(), ActExpertSearch.class);
                startActivityForResult(intent, IntentKey.REQ_CODE_SPEECH_SEARCH);
                break;
            case R.id.container1:
                jumpToLookForTag = 1;
                intent = new Intent(getActivity(), ActLookForExpertByCondition.class);
                HomeEntity.TopicEntity topicOne = homeBeanVo.getItem().getTopics().get(0);
                if (topicOne != null){
                    String key = topicOne.getSearchKey();
                    if (key.equals("keyword")){
                        intent.putExtra(IntentKey.CONDITION_KEYWORD, topicOne.getSearchValue());
                    } else if (key.equals("industryParentId")){
                        intent.putExtra(IntentKey.CONDITION_INDUSTRYPARENTID, topicOne.getSearchValue());
                    } else if (key.equals("mainDirectionId")){
                        intent.putExtra(IntentKey.CONDITION_MAIN_DIRECTION_ID, topicOne.getSearchValue());
                    }
                    intent.putExtra(IntentKey.CONDITION_SHARE_TEXT,topicOne.getDesc());
                }
                startActivity(intent);
                break;
            case R.id.container2:
                jumpToLookForTag = 2;
                intent = new Intent(getActivity(), ActLookForExpertByCondition.class);
                HomeEntity.TopicEntity topicTwo = homeBeanVo.getItem().getTopics().get(1);
                if (topicTwo != null){
                    String key = topicTwo.getSearchKey();
                    if (key.equals("keyword")){
                        intent.putExtra(IntentKey.CONDITION_KEYWORD, topicTwo.getSearchValue());
                    } else if (key.equals("industryParentId")){
                        intent.putExtra(IntentKey.CONDITION_INDUSTRYPARENTID, topicTwo.getSearchValue());
                    } else if (key.equals("mainDirectionId")){
                        intent.putExtra(IntentKey.CONDITION_MAIN_DIRECTION_ID, topicTwo.getSearchValue());
                    }
                    intent.putExtra(IntentKey.CONDITION_SHARE_TEXT,topicTwo.getDesc());
                }
                startActivity(intent);
                break;
            case R.id.container3:
                jumpToLookForTag = 3;
                intent = new Intent(getActivity(), ActLookForExpertByCondition.class);
                HomeEntity.TopicEntity topicThree = homeBeanVo.getItem().getTopics().get(2);
                if (topicThree != null){
                    String key = topicThree.getSearchKey();
                    if (key.equals("keyword")){
                        intent.putExtra(IntentKey.CONDITION_KEYWORD, topicThree.getSearchValue());
                    } else if (key.equals("industryParentId")){
                        intent.putExtra(IntentKey.CONDITION_INDUSTRYPARENTID, topicThree.getSearchValue());
                    } else if (key.equals("mainDirectionId")){
                        intent.putExtra(IntentKey.CONDITION_MAIN_DIRECTION_ID, topicThree.getSearchValue());
                    }
                    intent.putExtra(IntentKey.CONDITION_SHARE_TEXT,topicThree.getDesc());
                }
                startActivity(intent);
                break;
            case R.id.container4:
                startActivity(new Intent(getActivity(), ActLookForExpert.class));
                break;

            case R.id.speech_search:
                intent = new Intent(getActivity(), ActSpeechSearch.class);
                startActivityForResult(intent, IntentKey.REQ_CODE_SPEECH_SEARCH);
                break;
        }
    }

    public void refreshData(HomeNewBeanVo homeBeanVo) {
        list.stopRefresh();
        this.homeBeanVo = homeBeanVo;
        if (homeBeanVo == null) {
            return;
        }

        initDots(homeBeanVo.getItem().getBanners().size());
        initHorizontal(homeBeanVo.getItem().getTopics());

        roll.initData(homeBeanVo.getItem().getBanners());

        this.consultantsEntityList.clear();

        ConsultEntity entity = new ConsultEntity();
        entity.setSectionType(1);
        this.consultantsEntityList.add(entity);
        this.consultantsEntityList.addAll(homeBeanVo.getItem().getExperts());

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == parent.getAdapter().getCount() - list.getFooterViewsCount()) {
            startActivity(new Intent(getActivity(), ActLookForExpert.class));
            return;
        }
        if (ListUtils.isEmpty(consultantsEntityList)) {
            return;
        }
        headerViewsCount = list.getHeaderViewsCount();
        ConsultEntity entity = this.consultantsEntityList.get(position - headerViewsCount);
        if (entity == null) {
            return;
        }
        if (entity.getSectionType() == 1) {
            return;
        }
        Intent intent = new Intent(getActivity(), ActExpertHome.class);
        intent.putExtra(IntentKey.INTENT_KEY_EXPERT_ID, entity.getConsultantId());
        startActivityForResult(intent, IntentKey.REQ_CODE_BUY_EXPERT_SERVICE);
    }

    @Override
    public void onItemClick(int position, RollViewPager viewPager) {
        int realPosition = position % homeBeanVo.getItem().getBanners().size();
        Intent intent = new Intent(getActivity(), ActExpertHome.class);
        intent.putExtra(IntentKey.INTENT_KEY_EXPERT_ID, homeBeanVo.getItem().getBanners().get(realPosition).getCarouselId());
        startActivityForResult(intent, IntentKey.REQ_CODE_BUY_EXPERT_SERVICE);
    }

    public void showActDialog(ShowActRespVo respVo) {
        if (respVo == null) {
            return;
        }
        if (!respVo.isShowActivity()) {
            return;
        }
        actUrl = respVo.getUrl();
        actTitle = respVo.getTitle();
        DialogManager.showActDialog(getActivity(), this, respVo.getImgurl());
    }

    @Override
    public void joinAct() {
        if (TextUtils.isEmpty(actUrl) || TextUtils.isEmpty(actTitle)) {
            return;
        }
        Intent intent = new Intent(getActivity(), WebActivity.class);
        intent.putExtra(ExtraConst.EXTRA_WEB_TITLE, actTitle);
        intent.putExtra(ExtraConst.EXTRA_WEB_URL, actUrl);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentKey.REQ_CODE_BUY_EXPERT_SERVICE) {
            if (data != null) {
                if (listener != null) {
                    listener.jumpMe();
                }
            }
        } else if (requestCode == IntentKey.REQ_CODE_SPEECH_SEARCH) {
            if (data != null && data.hasExtra(IntentKey.INTENT_KEY_SPEECH_SEARCH_RESULT)) {
                jumpToLookForTag = 4;
                searchContent = data.getStringExtra(IntentKey.INTENT_KEY_SPEECH_SEARCH_RESULT);
                Intent intent = new Intent(getActivity(), ActLookForExpertByCondition.class);
                intent.putExtra(IntentKey.CONDITION_KEYWORD, searchContent);
                intent.putExtra(IntentKey.CONDITION_KEYWORD_FROM_VOICE, true);
                startActivity(intent);
            }
        }
    }

    public interface JumpMeTabListener {
        void jumpMe();
    }

    @Override
    public void onRefresh() {
        control.initHomeData(false);
    }

    @Override
    public void onLoadMore() {

    }

    public void stopRefresh() {
        list.stopRefresh();
    }

    public void jumpToLookFor() {
        Intent intent;
        if (jumpToLookForTag == 1) {
            intent = new Intent(getActivity(), ActLookForExpertByCondition.class);
            HomeEntity.TopicEntity topicOne = homeBeanVo.getItem().getTopics().get(0);
            if (topicOne != null){
                String key = topicOne.getSearchKey();
                if (key.equals("keyword")){
                    intent.putExtra(IntentKey.CONDITION_KEYWORD, topicOne.getSearchValue());
                } else if (key.equals("industryParentId")){
                    intent.putExtra(IntentKey.CONDITION_INDUSTRYPARENTID, topicOne.getSearchValue());
                } else if (key.equals("mainDirectionId")){
                    intent.putExtra(IntentKey.CONDITION_MAIN_DIRECTION_ID, topicOne.getSearchValue());
                }
                intent.putExtra(IntentKey.CONDITION_SHARE_TEXT,topicOne.getDesc());
            }
            intent.putExtra("_need_show_tip", true);
            startActivity(intent);
        } else if (jumpToLookForTag == 2) {
            intent = new Intent(getActivity(), ActLookForExpertByCondition.class);
            HomeEntity.TopicEntity topicTwo = homeBeanVo.getItem().getTopics().get(1);
            if (topicTwo != null){
                String key = topicTwo.getSearchKey();
                if (key.equals("keyword")){
                    intent.putExtra(IntentKey.CONDITION_KEYWORD, topicTwo.getSearchValue());
                } else if (key.equals("industryParentId")){
                    intent.putExtra(IntentKey.CONDITION_INDUSTRYPARENTID, topicTwo.getSearchValue());
                } else if (key.equals("mainDirectionId")){
                    intent.putExtra(IntentKey.CONDITION_MAIN_DIRECTION_ID, topicTwo.getSearchValue());
                }
                intent.putExtra(IntentKey.CONDITION_SHARE_TEXT,topicTwo.getDesc());
            }
            intent.putExtra("_need_show_tip", true);
            startActivity(intent);
        } else if (jumpToLookForTag == 3) {
            intent = new Intent(getActivity(), ActLookForExpertByCondition.class);
            HomeEntity.TopicEntity topicThree = homeBeanVo.getItem().getTopics().get(2);
            if (topicThree != null){
                String key = topicThree.getSearchKey();
                if (key.equals("keyword")){
                    intent.putExtra(IntentKey.CONDITION_KEYWORD, topicThree.getSearchValue());
                } else if (key.equals("industryParentId")){
                    intent.putExtra(IntentKey.CONDITION_INDUSTRYPARENTID, topicThree.getSearchValue());
                } else if (key.equals("mainDirectionId")){
                    intent.putExtra(IntentKey.CONDITION_MAIN_DIRECTION_ID, topicThree.getSearchValue());
                }
                intent.putExtra(IntentKey.CONDITION_SHARE_TEXT,topicThree.getDesc());
            }
            intent.putExtra("_need_show_tip", true);
            startActivity(intent);
        } else if (jumpToLookForTag == 4) {
            intent = new Intent(getActivity(), ActLookForExpertByCondition.class);
            intent.putExtra(IntentKey.CONDITION_KEYWORD, searchContent);
            intent.putExtra(IntentKey.CONDITION_KEYWORD_FROM_VOICE, true);
            intent.putExtra("_need_show_tip", true);
            startActivity(intent);
        }
    }
}
