package com.xiaojia.daniujia.ui.frag;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.xiaojia.daniujia.IntentKey;
import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.ask.AskRewardWebActivity;
import com.xiaojia.daniujia.children.ChildrenShareWebActivity;
import com.xiaojia.daniujia.domain.resp.AskRewardListRespVo;
import com.xiaojia.daniujia.domain.resp.ChildShareRespVo;
import com.xiaojia.daniujia.domain.resp.ConsultEntity;
import com.xiaojia.daniujia.domain.resp.HomeEntity;
import com.xiaojia.daniujia.domain.resp.HomeNewBeanVo;
import com.xiaojia.daniujia.domain.resp.UserCenterRespVo;
import com.xiaojia.daniujia.ui.act.ActAskQuestion;
import com.xiaojia.daniujia.ui.act.ActExpertHome;
import com.xiaojia.daniujia.ui.act.ActExpertModifyInfo;
import com.xiaojia.daniujia.ui.act.ActExpertSearch;
import com.xiaojia.daniujia.ui.act.ActLogin;
import com.xiaojia.daniujia.ui.act.ActLookForExpert;
import com.xiaojia.daniujia.ui.act.ActToBeExpert;
import com.xiaojia.daniujia.ui.adapters.AskRewardListAdapter;
import com.xiaojia.daniujia.ui.adapters.HomeExpertAdapter;
import com.xiaojia.daniujia.ui.control.HomeNewControl;
import com.xiaojia.daniujia.ui.views.CustomViewpager;
import com.xiaojia.daniujia.ui.views.NoVisibleFullScreenScrollView;
import com.xiaojia.daniujia.ui.views.PagerSlidingTabStrip;
import com.xiaojia.daniujia.ui.views.RollViewPager;
import com.xiaojia.daniujia.ui.views.refreshscroll.PullToRefreshBase;
import com.xiaojia.daniujia.ui.views.refreshscroll.PullToRefreshScrollView;
import com.xiaojia.daniujia.utils.ListUtils;
import com.xiaojia.daniujia.utils.LogUtil;
import com.xiaojia.daniujia.utils.ScreenUtils;
import com.xiaojia.daniujia.utils.SysUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/21.
 */
public class HomeNewFragment extends BaseFragment implements RollViewPager.OnItemClickListener, NoVisibleFullScreenScrollView.OnScrollerListener {
    private static String[] titles = {"推荐专家", "热门悬赏"};
    private PagerSlidingTabStrip tabPaper;
    private CustomViewpager homeViewPager;
    private ArrayList<View> pagerViews = new ArrayList<>();
    private LayoutInflater mLayoutInflater;

    private HomeExpertAdapter mRecommendExpertAdapter;
    private AskRewardListAdapter mPayForAskAdapter;

    private List<ConsultEntity> recommendEntityList = new ArrayList<>();
    private List<AskRewardListRespVo.AskRewardListItem> payForAskEntityList = new ArrayList<>();

    private HomePagerChangeListener homePagerChangeListener = new HomePagerChangeListener();

    private HomeNewControl control;
    private HomeNewBeanVo homeBeanVo;
    private RollViewPager mRollView;
    private RollViewPager mRollActivity;
    private LinearLayout mRollActivityPoint;
    private LinearLayout mRollViewPoint;

    private JumpMeTabListener listener;
    private LinearLayout mLlLookForExpert;
    private CommonClickListener listenerClick = new CommonClickListener();
    private ImageView mIvAskQuestion;
    private LinearLayout mLlPayForAsk;
    private RelativeLayout mRlBecomeExpert;

    private boolean isExamine = false;
    private View viewOne,viewTwo;
    private RelativeLayout rlActivity;
    private PullToRefreshScrollView mHomeScrollView;
    private NoVisibleFullScreenScrollView mNoFullScrollView;
    private PagerSlidingTabStrip mSuspendTab;
    private ImageView mHomeSeparate;

    private int tabTop;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        control = new HomeNewControl();
        setControl(control);
    }

    @Override
    protected int getInflateLayout() {
        return R.layout.frag_home;
    }

    @Override
    protected void setUpView(View view) {
        tabPaper = (PagerSlidingTabStrip) view.findViewById(R.id.home_tab_head);
        homeViewPager = (CustomViewpager) view.findViewById(R.id.home_viewpager);
        mRollView = (RollViewPager) view.findViewById(R.id.roll_scroll_pic);
        mRollViewPoint = (LinearLayout) view.findViewById(R.id.iv_dot_ll);
        mRollActivity = (RollViewPager) view.findViewById(R.id.roll_activity);
        View searchView = view.findViewById(R.id.search_container);
        mRollActivityPoint = (LinearLayout) view.findViewById(R.id.iv_dot_ll_activity);
        mLlLookForExpert = (LinearLayout) view.findViewById(R.id.home_look_for_expert);
        mIvAskQuestion = (ImageView) view.findViewById(R.id.home_ask_question);
        mLlPayForAsk = (LinearLayout) view.findViewById(R.id.home_pay_of_ask);
        mRlBecomeExpert = (RelativeLayout) view.findViewById(R.id.home_rl_become_expert);
        viewOne = view.findViewById(R.id.home_view_line_one);
        viewTwo = view.findViewById(R.id.home_view_line_two);
        rlActivity = (RelativeLayout) view.findViewById(R.id.home_rl_activity);
        mHomeScrollView = (PullToRefreshScrollView) view.findViewById(R.id.home_scrollview);
        mSuspendTab = (PagerSlidingTabStrip) view.findViewById(R.id.suspend_tab_head);
        mHomeSeparate = (ImageView) view.findViewById(R.id.home_separate);

        mRollView.setOnItemClickLstener(this);
        mRollActivity.setOnItemClickLstener(this);

        searchView.setOnClickListener(listenerClick);
        mLlLookForExpert.setOnClickListener(listenerClick);
        mIvAskQuestion.setOnClickListener(listenerClick);
        mLlPayForAsk.setOnClickListener(listenerClick);
        mRlBecomeExpert.setOnClickListener(listenerClick);

        mLayoutInflater = LayoutInflater.from(getActivity());

        mRecommendExpertAdapter = new HomeExpertAdapter(recommendEntityList, getActivity());
        mPayForAskAdapter = new AskRewardListAdapter(payForAskEntityList, getActivity(),false);

        mHomeScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                control.initData(false);
            }
        });

        tabPaper.post(new Runnable() {
            @Override
            public void run() {
                tabTop = tabPaper.getTop();
            }
        });

        mNoFullScrollView = (NoVisibleFullScreenScrollView) mHomeScrollView.getRefreshableView();
        mNoFullScrollView.setListener(this);

        control.initData(true);
    }

    public void setListener(JumpMeTabListener listener) {
        this.listener = listener;
    }

    public PullToRefreshScrollView getHomeScrollView(){
        return mHomeScrollView;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (ListUtils.isEmpty(recommendEntityList) && !hidden && control != null) {
            control.initData(false);
        }

        if (hidden) {
            mRollView.setRunning(false);
        } else {
            mRollView.startRoll();
        }
    }

    public void setRollStop(boolean isStop){
        if (isStop) {
            mRollView.setRunning(false);
        } else {
            mRollView.startRoll();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (SysUtil.getBooleanPref(PrefKeyConst.PREF_IS_LOGIN)){
            isFirstRequest = true;
            control.requestUserInfo(false);
        }
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
        }
    }


    public void initData(HomeNewBeanVo homeBeanVo,boolean notFresh) {
        mHomeScrollView.onRefreshComplete();
        this.homeBeanVo = homeBeanVo;
        if (homeBeanVo.getItem() == null)
            return;

        if (homeBeanVo == null) {
            return;
        }

        if (homeBeanVo.getItem().isQuestionFeedback()){
            mIvAskQuestion.setVisibility(View.VISIBLE);
        } else {
            mIvAskQuestion.setVisibility(View.GONE);
        }

        initDots(homeBeanVo.getItem().getBanners().size(), mRollViewPoint, mRollView);
//        mRollView.initData(homeBeanVo.getItem().getBanners());

        if (homeBeanVo.getItem().getActivities() != null && homeBeanVo.getItem().getActivities().size() > 0) {
            goneActivity(false);
            List<HomeEntity.BannersEntity> data = handlerActivityData(homeBeanVo.getItem().getActivities());
            if (data != null){
                initDots(data.size(), mRollActivityPoint, mRollActivity);
                if (data.size() == 1) {
                    mRollActivityPoint.setVisibility(View.GONE);
                }
                mRollActivity.setShowBottomTest(false);
//                mRollActivity.initData(data);
            }

        } else {
            // 隐藏活动UI
            goneActivity(true);
        }

        recommendEntityList.clear();
        recommendEntityList.addAll(homeBeanVo.getItem().getExperts());

        payForAskEntityList.clear();
        payForAskEntityList.addAll(homeBeanVo.getItem().getRewardQuestions());

        if(pagerViews == null || pagerViews.size() == 0){
            createPagerView(titles);
        }

        if (notFresh) {
            homeViewPager.setAdapter(new MyViewPagerAdapter());
            homeViewPager.addOnPageChangeListener(homePagerChangeListener);
            tabPaper.setViewPager(homeViewPager);
            mSuspendTab.setViewPager(homeViewPager);
        } else {
            mPayForAskAdapter.notifyDataSetChanged();
            mRecommendExpertAdapter.notifyDataSetChanged();
        }
        homeViewPager.setCurrentItem(0);

    }

    private List<HomeEntity.BannersEntity> handlerActivityData(List<HomeEntity.ActivityEntity> entities){
        if (entities == null)
            return null;

        List<HomeEntity.BannersEntity> banners = new ArrayList<>();
        for (int i = 0; i < entities.size(); i++) {
            HomeEntity.BannersEntity banner = new HomeEntity.BannersEntity();
            banner.setImage(entities.get(i).getImgUrl());
            banners.add(banner);
        }
        return banners;
    }

    private void initDots(int size,LinearLayout points,RollViewPager rollViewPager) {
        if (!isAdded()) {
            return;
        }

        int padding = ScreenUtils.dip2px(4);
        points.removeAllViews();
        List<ImageView> iv_dots = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            ImageView iv_dot = new ImageView(getActivity());
            if (i == 0) {
                iv_dot.setImageResource(R.drawable.roll_dot_checked);
            } else {
                iv_dot.setImageResource(R.drawable.roll_dot_unchecked);
            }
            iv_dot.setPadding(padding, padding, padding, padding);
            points.addView(iv_dot);
            iv_dots.add(iv_dot);
        }
        rollViewPager.initDots(iv_dots);
    }

    public void onSetKnowledgeState(ChildShareRespVo respVo){
        if (respVo != null && !TextUtils.isEmpty(respVo.getCharityExpert())) {
//            knowledgeState = respVo.getCharityExpert();
        }
    }

    private void createPagerView(String[] titles){
        if (pagerViews == null){
            pagerViews = new ArrayList<>();
        }
        pagerViews.clear();
        for (int i = 0; i < titles.length; i++) {
            View view = mLayoutInflater.inflate(R.layout.item_home_pager,null);
            ListView listView = (ListView) view.findViewById(R.id.item_home_pager_lv);
            if(i == 0){
                listView.setAdapter(mRecommendExpertAdapter);
                listView.setOnItemClickListener(new RecommendItemClickListener());
            } else {
                listView.setAdapter(mPayForAskAdapter);
                listView.setOnItemClickListener(new AskItemClickListener());
            }

            setListViewHeightBasedOnChildren(listView);
            pagerViews.add(view);
            homeViewPager.setObjectForPosition(view,i);
        }

    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
//            int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
            listItem.measure(0, 0);// 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);

    }

    @Override
    public void onItemClick(int position,RollViewPager viewPager) {
        if (homeBeanVo.getItem() != null) {
            if (viewPager == mRollView) {
                if (homeBeanVo.getItem().getBanners() == null)
                    return;
                int realPosition = position % homeBeanVo.getItem().getBanners().size();
                Intent intent = new Intent(getActivity(), ActExpertHome.class);
                intent.putExtra(IntentKey.INTENT_KEY_EXPERT_ID, homeBeanVo.getItem().getBanners().get(realPosition).getCarouselId());
                startActivityForResult(intent, IntentKey.REQ_CODE_BUY_EXPERT_SERVICE);

            } else if(viewPager == mRollActivity){
                if (homeBeanVo.getItem().getActivities() == null)
                    return;
                int realPosition = position % homeBeanVo.getItem().getActivities().size();
                Intent intent = new Intent(getActivity(), ChildrenShareWebActivity.class);
                intent.putExtra(IntentKey.INTENT_KEY_CHILDREN_ACTIVITY_URL,homeBeanVo.getItem().getActivities().get(realPosition).getUrl());
                startActivity(intent);
            }
        }

    }

    @Override
    public void onScrolled(int position) {
        if (position >= tabTop){
            mSuspendTab.setVisibility(View.VISIBLE);
            mHomeSeparate.setVisibility(View.VISIBLE);
        } else {
            mSuspendTab.setVisibility(View.GONE);
            mHomeSeparate.setVisibility(View.GONE);
        }
    }


    class MyViewPagerAdapter extends PagerAdapter {
        public MyViewPagerAdapter() {
        }

        @Override
        public int getCount() {
            if (pagerViews == null || pagerViews.size() == 0)
                return 0;
            return pagerViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(pagerViews.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(pagerViews.get(position));
            return pagerViews.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

    class HomePagerChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(final int position) {
            homeViewPager.resetHeight(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private class RecommendItemClickListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (ListUtils.isEmpty(recommendEntityList)) {
                return;
            }

            ConsultEntity entity = recommendEntityList.get(position);
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
    }

    private class AskItemClickListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (ListUtils.isEmpty(payForAskEntityList)) {
                return;
            }

            AskRewardListRespVo.AskRewardListItem entity = payForAskEntityList.get(position);
            if (entity == null) {
                return;
            }

            Intent intent = new Intent(getActivity(), AskRewardWebActivity.class);
            intent.putExtra(IntentKey.INTENT_KEY_ASK_REWARD_FROM, IntentKey.ASK_REWARD_DETAIL);
            intent.putExtra(IntentKey.INTENT_KEY_ASK_REWARD_ID, entity.getId());
            startActivity(intent);
        }
    }

    private class CommonClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.search_container){
                startActivity(new Intent(getActivity(), ActExpertSearch.class));
            } else if (v.getId() == R.id.home_look_for_expert) {
                startActivity(new Intent(getActivity(), ActLookForExpert.class));
            } else if (v.getId() == R.id.home_pay_of_ask){
                if (listener != null){
//                    listener.jumpAsk();
                } else {
                    LogUtil.w("zptest","HomeNewFragment listener is null");
                }
            } else if (v.getId() == R.id.home_rl_become_expert){
                if (!SysUtil.getBooleanPref(PrefKeyConst.PREF_IS_LOGIN)) {
                    startActivity(new Intent(getActivity(), ActLogin.class));
                } else {
                    if (isFirstRequest) {
                        control.requestUserInfo(true);
                    } else {
                        goToCenter();
                    }

                }
            } else if (v.getId() == R.id.home_ask_question){
                startActivity(new Intent(getActivity(), ActAskQuestion.class));
            }
        }
    }

    private boolean isFirstRequest = true;

    public void goToCenterInfo(UserCenterRespVo result,boolean isNeedGo){
        int step = result.getUserDesc().getApplyStepValue();
        if (step >= 15) {
            isExamine = true;
        } else {
            isExamine = false;
        }
        isFirstRequest = false;

        if (isNeedGo) {
            goToCenter();
        }

    }

    private void goToCenter(){
        if (isExamine) {
            startActivity(new Intent(getActivity(), ActExpertModifyInfo.class));
        } else {
            startActivity(new Intent(getActivity(), ActToBeExpert.class));
        }
    }

    public interface JumpMeTabListener {
        void jumpMe();
    }

    private void goneActivity(boolean isGone){
        viewTwo.setVisibility(isGone == true ? View.GONE: View.VISIBLE);
        viewOne.setVisibility(isGone == true ? View.GONE: View.VISIBLE);
        rlActivity.setVisibility(isGone == true ? View.GONE: View.VISIBLE);

    }

}
