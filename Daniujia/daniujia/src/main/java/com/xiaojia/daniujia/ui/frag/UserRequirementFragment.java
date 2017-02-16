package com.xiaojia.daniujia.ui.frag;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaojia.daniujia.IntentKey;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.domain.resp.RequireListEntity;
import com.xiaojia.daniujia.domain.resp.RequireListVo;
import com.xiaojia.daniujia.ui.act.ActRequirementDetail;
import com.xiaojia.daniujia.ui.adapters.RequirementAdapter;
import com.xiaojia.daniujia.ui.control.UserRequirementControl;
import com.xiaojia.daniujia.ui.views.PagerSlidingTabStrip;
import com.xiaojia.daniujia.ui.views.XListView;
import com.xiaojia.daniujia.utils.LogUtil;
import com.xiaojia.daniujia.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/3.
 */
public class UserRequirementFragment extends BaseFragment implements ViewPager.OnPageChangeListener {
    //type: searching : 寻找中 confirmed : 已确认
    private static final String SEARCHING = "searching";
    private static final String CONFIRMED = "confirmed";

    private final int PAGER_NUMBER = 10;
    private static String[] titles = {"进行中需求", "已结束需求"};

    private PagerSlidingTabStrip mRequireTab;
    private ViewPager mVpContent;

    private ArrayList<View> pagerViews = new ArrayList<>();
    private LayoutInflater mLayoutInflater;
    private RequirementAdapter mLvAdapterLooking;
    private RequirementAdapter mLvAdapterConfirm;

    private UserRequirementControl control;
    private int mLookingPagerIndex = 0;
    private int mConfirmPagerIndex = 0;

    private String type = SEARCHING;

    // 两个数据源
    private List<RequireListEntity> mSearchingData;
    private List<RequireListEntity> mConfirmData;
    private XListView mSearchingListView;
    private XListView mConfirmListView;
    private RelativeLayout mLlSearchingNoData;
    private RelativeLayout mLlConfirmNoData;

    private int searchingCount = 0;
    private int confirmCount = 0;

    private int[] unRead = new int[2];

    private boolean isFirstRequest = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        control = new UserRequirementControl();
        setControl(control);

    }

    @Override
    protected int getInflateLayout() {
        return R.layout.frag_user_requirement;
    }

    @Override
    protected void setUpView(View view) {
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            if (intent.hasExtra(IntentKey.INTENT_KEY_DEMAND_ID)) {
                int demandId = intent.getIntExtra(IntentKey.INTENT_KEY_DEMAND_ID, 0);
                Intent detailIntent = new Intent(getContext(), ActRequirementDetail.class);
                detailIntent.putExtra(IntentKey.INTENT_KEY_DEMAND_ID, demandId);
                startActivityForResult(detailIntent, IntentKey.REQ_CODE_EDIT_REQUIREMENT);
            }
        }
        ((TextView) view.findViewById(R.id.id_title)).setText("我的顾问需求");
        setBackButton(view.findViewById(R.id.id_back));
        mRequireTab = (PagerSlidingTabStrip) view.findViewById(R.id.requirement_tab);
        mVpContent = (ViewPager) view.findViewById(R.id.requirement_viewpager);

        mLayoutInflater = LayoutInflater.from(getActivity());
        mSearchingData = new ArrayList<>();
        mConfirmData = new ArrayList<>();

        // 第一个boolean值，是否不显示合作专家，第二个boolean，是否显示状态数据
        mLvAdapterLooking = new RequirementAdapter(mSearchingData, getContext(), true, true);
        mLvAdapterConfirm = new RequirementAdapter(mConfirmData, getContext(), true, true);


        createPagerView(titles);
        mVpContent.setAdapter(new MyViewPagerAdapter());
        mVpContent.addOnPageChangeListener(this);

        mRequireTab.setViewPager(mVpContent);
        mRequireTab.setTextSize(ScreenUtils.sp2px(15));

        control.initRequireData(true, type, PAGER_NUMBER, mLookingPagerIndex, true);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0) {
            type = SEARCHING;
        } else if (position == 1) {
            type = CONFIRMED;
        }

        if (isFirstRequest) {
            control.initRequireData(true, type, PAGER_NUMBER,
                    type.equals(SEARCHING) ? mLookingPagerIndex : mConfirmPagerIndex, true);
            isFirstRequest = false;

        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

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

    private void createPagerView(String[] titles) {
        if (pagerViews == null) {
            pagerViews = new ArrayList<>();
        }
        pagerViews.clear();
        for (int i = 0; i < titles.length; i++) {
            View view = mLayoutInflater.inflate(R.layout.item_requirement_lv, null);
            XListView listView = (XListView) view.findViewById(R.id.item_requirement_list);
            RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.no_data_container);
            // 设置适配器和点击事件
            if (i == 0) {
                mSearchingListView = listView;
                mLlSearchingNoData = relativeLayout;
                ((TextView) view.findViewById(R.id.no_data_tip)).setText("暂时没有进行中需求");
                listView.setAdapter(mLvAdapterLooking);
                listView.setXListViewListener(new LookingForIXList());
                listView.setOnItemClickListener(new LookingForItemClickListener());
            } else {
                mConfirmListView = listView;
                mLlConfirmNoData = relativeLayout;
                ((TextView) view.findViewById(R.id.no_data_tip)).setText("暂时没有已结束需求");
                listView.setAdapter(mLvAdapterConfirm);
                listView.setXListViewListener(new ConfirmIXList());
                listView.setOnItemClickListener(new ConfirmItemClickListener());
            }
            pagerViews.add(view);
        }
    }

    public void setRequireData(RequireListVo requireData, boolean isFresh) {
        if (requireData == null) {
            if (type.equals(SEARCHING) && mSearchingData.isEmpty()) {
                mLlSearchingNoData.setVisibility(View.VISIBLE);
                mSearchingListView.setVisibility(View.GONE);
            }
            if (type.equals(CONFIRMED) && mConfirmData.isEmpty()) {
                mLlConfirmNoData.setVisibility(View.VISIBLE);
                mConfirmListView.setVisibility(View.GONE);
            }

            return;
        }

        if (requireData.getItems() == null) {
            if (type.equals(SEARCHING) && mSearchingData.isEmpty()) {
                mLlSearchingNoData.setVisibility(View.VISIBLE);
                mSearchingListView.setVisibility(View.GONE);
            }
            if (type.equals(CONFIRMED) && mConfirmData.isEmpty()) {
                mLlConfirmNoData.setVisibility(View.VISIBLE);
                mConfirmListView.setVisibility(View.GONE);
            }
            return;
        }

        if (requireData.getSearchingChanged() != 0) {
            unRead[0] = 1;
            searchingCount = requireData.getSearchingChanged();
        }

        if (requireData.getConfirmedChanged() != 0) {
            unRead[1] = 2;
            confirmCount = requireData.getConfirmedChanged();
        }

        mRequireTab.showUnReadTab(unRead);

        if (type.equals(SEARCHING)) {
            if (isFresh) {
                mSearchingData.clear();
            }

            if (requireData.getItems().size() == 0 && mSearchingData.isEmpty()) {
                mLlSearchingNoData.setVisibility(View.VISIBLE);
                mSearchingListView.setVisibility(View.GONE);
            } else {
                mLlSearchingNoData.setVisibility(View.GONE);
                mSearchingListView.setVisibility(View.VISIBLE);
            }

            mLookingPagerIndex = requireData.getNextCursor();

            mSearchingData.addAll(requireData.getItems());
            mLvAdapterLooking.notifyDataSetChanged();

            if (requireData.getItems().size() < PAGER_NUMBER) {
                mSearchingListView.setPullLoadEnable(false);
            } else {
                mSearchingListView.setPullLoadEnable(true);
            }

        } else {
            if (isFresh) {
                mConfirmData.clear();
            }

            if (requireData.getItems().size() == 0 && mConfirmData.isEmpty()) {
                mLlConfirmNoData.setVisibility(View.VISIBLE);
                mConfirmListView.setVisibility(View.GONE);
            } else {
                mLlConfirmNoData.setVisibility(View.GONE);
                mConfirmListView.setVisibility(View.VISIBLE);
            }

            mConfirmPagerIndex = requireData.getNextCursor();

            mConfirmData.addAll(requireData.getItems());
            mLvAdapterConfirm.notifyDataSetChanged();

            if (requireData.getItems().size() < PAGER_NUMBER) {
                mConfirmListView.setPullLoadEnable(false);
            } else {
                mConfirmListView.setPullLoadEnable(true);
            }
        }
    }

    private class LookingForIXList implements XListView.IXListViewListener {

        @Override
        public void onRefresh() {
            LogUtil.d("zp_test", "LookingForIXList onRefresh");
            mLookingPagerIndex = 0;
            control.initRequireData(false, type, PAGER_NUMBER, mLookingPagerIndex, true);
        }

        @Override
        public void onLoadMore() {
            LogUtil.d("zp_test", "LookingForIXList onLoadMore");
            // 利用游标选择下一页数据的开始点，下一页数据就不要index++操作了
            // 放在了数据返回后的next cursor上
            control.initRequireData(false, type, PAGER_NUMBER, mLookingPagerIndex, false);
        }
    }

    private class ConfirmIXList implements XListView.IXListViewListener {

        @Override
        public void onRefresh() {
            LogUtil.d("zp_test", "ConfirmIXList onRefresh");
            mConfirmPagerIndex = 0;
            control.initRequireData(false, type, PAGER_NUMBER, mConfirmPagerIndex, true);
        }

        @Override
        public void onLoadMore() {
            LogUtil.d("zp_test", "ConfirmIXList onLoadMore");
            control.initRequireData(false, type, PAGER_NUMBER, mConfirmPagerIndex, false);
        }
    }

    private class LookingForItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position - mSearchingListView.getHeaderViewsCount() < 0 ||
                    position - mSearchingListView.getHeaderViewsCount() >= mSearchingData.size())
                return;

            RequireListEntity entity = mSearchingData.get(position - mSearchingListView.getHeaderViewsCount());
            Intent intent = new Intent(getContext(), ActRequirementDetail.class);
            intent.putExtra(IntentKey.INTENT_KEY_DEMAND_ID, entity.getDemandId());
            startActivityForResult(intent, IntentKey.REQ_CODE_EDIT_REQUIREMENT);

            if (entity.isStatusChanged() || entity.isApplicantChanged()) {
                entity.setApplicantChanged(false);
                entity.setStatusChanged(false);

                searchingCount--;
                if (searchingCount == 0) {
                    unRead[0] = 0;
                    mRequireTab.showUnReadTab(unRead);
                }
                mLvAdapterLooking.notifyDataSetChanged();
            }
        }
    }

    private class ConfirmItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position - mConfirmListView.getHeaderViewsCount() < 0 ||
                    position - mConfirmListView.getHeaderViewsCount() >= mConfirmData.size())
                return;

            RequireListEntity requireListEntity = mConfirmData.get(position - mConfirmListView.getHeaderViewsCount());
            Intent intent = new Intent(getContext(), ActRequirementDetail.class);
            intent.putExtra(IntentKey.INTENT_KEY_DEMAND_ID,requireListEntity.getDemandId());
            startActivity(intent);

            if (requireListEntity.isStatusChanged() || requireListEntity.isApplicantChanged()){
                requireListEntity.setApplicantChanged(false);
                requireListEntity.setStatusChanged(false);

                confirmCount--;
                if (confirmCount == 0) {
                    unRead[1] = 0;
                    mRequireTab.showUnReadTab(unRead);
                }
                mLvAdapterConfirm.notifyDataSetChanged();
            }

        }
    }

    public void stopListRefresh() {
        if (mConfirmListView != null) {
            mConfirmListView.stopRefresh();
            mConfirmListView.stopLoadMore();
        }

        if (mSearchingListView != null) {
            mSearchingListView.stopRefresh();
            mSearchingListView.stopLoadMore();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentKey.REQ_CODE_EDIT_REQUIREMENT && data != null) {
            boolean isEdit = data.getBooleanExtra(IntentKey.INTENT_KEY_IS_REQUIREMENT_EDIT, false);
            if (isEdit) {
                mLookingPagerIndex = 0;
                control.initRequireData(false, type, PAGER_NUMBER, mLookingPagerIndex, true);
            }
        }
    }
}
