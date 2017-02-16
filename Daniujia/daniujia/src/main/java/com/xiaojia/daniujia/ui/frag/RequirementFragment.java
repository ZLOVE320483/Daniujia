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
import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.domain.resp.RequireListEntity;
import com.xiaojia.daniujia.domain.resp.RequireListVo;
import com.xiaojia.daniujia.ui.act.ActLogin;
import com.xiaojia.daniujia.ui.act.ActRequirementDetail;
import com.xiaojia.daniujia.ui.act.ActRequirementPublish;
import com.xiaojia.daniujia.ui.adapters.RequirementAdapter;
import com.xiaojia.daniujia.ui.control.RequirementControl;
import com.xiaojia.daniujia.ui.views.PagerSlidingTabStrip;
import com.xiaojia.daniujia.ui.views.XListView;
import com.xiaojia.daniujia.utils.LogUtil;
import com.xiaojia.daniujia.utils.ScreenUtils;
import com.xiaojia.daniujia.utils.SysUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/3.
 */
public class RequirementFragment extends BaseFragment implements View.OnClickListener, ViewPager.OnPageChangeListener {
    //type: searching : 寻找中 confirmed : 已确认
    private static final String SEARCHING = "searching";
    private static final String CONFIRMED = "confirmed";
    private final int REQUEST_TO_LOGIN = 0x01;

    private final int PAGER_NUMBER = 10;
    private static String[] titles = {"顾问寻找中", "顾问已确定"};

    private PagerSlidingTabStrip mRequireTab;
    private TextView mTvPublish;
    private ViewPager mVpContent;

    private ArrayList<View> pagerViews = new ArrayList<>();
    private LayoutInflater mLayoutInflater;
    private RequirementAdapter mLvAdapterLooking;
    private RequirementAdapter mLvAdapterConfirm;

    private RequirementControl control;
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

    private boolean isFirstRequest = true;
    private int demandId;
    private int curPos = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        control = new RequirementControl();
        setControl(control);

    }

    @Override
    protected int getInflateLayout() {
        return R.layout.frag_requirement;
    }

    @Override
    protected void setUpView(View view) {
        mRequireTab = (PagerSlidingTabStrip) view.findViewById(R.id.requirement_tab);
        mTvPublish = (TextView) view.findViewById(R.id.requirement_publish);
        mVpContent = (ViewPager) view.findViewById(R.id.requirement_viewpager);

        mLayoutInflater = LayoutInflater.from(getActivity());
        mSearchingData = new ArrayList<>();
        mConfirmData = new ArrayList<>();

        mLvAdapterLooking = new RequirementAdapter(mSearchingData, getContext(), true, false);
        mLvAdapterConfirm = new RequirementAdapter(mConfirmData, getContext(), false, false);

        mTvPublish.setOnClickListener(this);

        createPagerView(titles);
        mVpContent.setAdapter(new MyViewPagerAdapter());
        mVpContent.addOnPageChangeListener(this);

        mRequireTab.setViewPager(mVpContent);
        mRequireTab.setTextSize(ScreenUtils.dip2px(15));
        control.initRequireData(true, type, PAGER_NUMBER, mLookingPagerIndex, true);
        mRequireTab.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                curPos = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == mTvPublish) {
            if (SysUtil.getBooleanPref(PrefKeyConst.PREF_IS_LOGIN)) {
                Intent intent = new Intent(getContext(), ActRequirementPublish.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(getContext(), ActLogin.class);
                startActivityForResult(intent, REQUEST_TO_LOGIN);
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && requestCode == REQUEST_TO_LOGIN) {
            Intent intent = new Intent(getContext(), ActRequirementPublish.class);
            startActivity(intent);
        }
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

        } else {
            if ((type.equals(SEARCHING) && mSearchingData.isEmpty())
                    || (type.equals(CONFIRMED) && mConfirmData.isEmpty())) {
                control.initRequireData(true, type, PAGER_NUMBER,
                        type.equals(SEARCHING) ? mLookingPagerIndex : mConfirmPagerIndex, true);
            }
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
                ((TextView)view.findViewById(R.id.no_data_tip)).setText("暂时没有此类需求");
                listView.setAdapter(mLvAdapterLooking);
                listView.setXListViewListener(new LookingForIXList());
                listView.setOnItemClickListener(new LookingForItemClickListener());
            } else {
                mConfirmListView = listView;
                mLlConfirmNoData = relativeLayout;
                ((TextView)view.findViewById(R.id.no_data_tip)).setText("暂时没有此类需求");
                listView.setAdapter(mLvAdapterConfirm);
                listView.setXListViewListener(new ConfirmIXList());
                listView.setOnItemClickListener(new ConfirmItemClickListener());
            }
            pagerViews.add(view);
        }
    }

    public void setRequireData(RequireListVo requireData, boolean isFresh) {
        if (requireData == null){
            if (type.equals(SEARCHING) && mSearchingData.isEmpty()){
                mLlSearchingNoData.setVisibility(View.VISIBLE);
                mSearchingListView.setVisibility(View.GONE);
            }
            if (type.equals(CONFIRMED) && mConfirmData.isEmpty()){
                mLlConfirmNoData.setVisibility(View.VISIBLE);
                mConfirmListView.setVisibility(View.GONE);
            }

            return;
        }

        if (requireData.getItems() == null){
            if (type.equals(SEARCHING) && mSearchingData.isEmpty()){
                mLlSearchingNoData.setVisibility(View.VISIBLE);
                mSearchingListView.setVisibility(View.GONE);
            }
            if (type.equals(CONFIRMED) && mConfirmData.isEmpty()){
                mLlConfirmNoData.setVisibility(View.VISIBLE);
                mConfirmListView.setVisibility(View.GONE);
            }
            return;
        }

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
            LogUtil.d("zp_test","mLookingPagerIndex: " + mLookingPagerIndex);

            mSearchingData.addAll(requireData.getItems());
            mLvAdapterLooking.notifyDataSetChanged();

            if (mSearchingData.size() == requireData.getTotalCount()) {
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
            LogUtil.d("zp_test","mConfirmPagerIndex: " + mConfirmPagerIndex);

            mConfirmData.addAll(requireData.getItems());
            mLvAdapterConfirm.notifyDataSetChanged();

            if (mConfirmData.size() == requireData.getTotalCount()) {
                mConfirmListView.setPullLoadEnable(false);
            } else {
                mConfirmListView.setPullLoadEnable(true);
            }
        }
    }

    private class LookingForIXList implements XListView.IXListViewListener {

        @Override
        public void onRefresh() {
            mLookingPagerIndex = 0;
            control.initRequireData(false, type, PAGER_NUMBER, mLookingPagerIndex, true);
        }

        @Override
        public void onLoadMore() {
            // 利用游标选择下一页数据的开始点，下一页数据就不要index++操作了
            // 放在了数据返回后的next cursor上
            control.initRequireData(false, type, PAGER_NUMBER, mLookingPagerIndex, false);
        }
    }

    private class ConfirmIXList implements XListView.IXListViewListener {

        @Override
        public void onRefresh() {
            mConfirmPagerIndex = 0;
            control.initRequireData(false, type, PAGER_NUMBER, mConfirmPagerIndex, true);
        }

        @Override
        public void onLoadMore() {
            control.initRequireData(false, type, PAGER_NUMBER, mConfirmPagerIndex, false);
        }
    }

    private class LookingForItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position - mSearchingListView.getHeaderViewsCount() < 0)
                return;
            demandId = mSearchingData.get(position - mSearchingListView.getHeaderViewsCount()).getDemandId();
            Intent intent = new Intent(getContext(), ActRequirementDetail.class);
            intent.putExtra(IntentKey.INTENT_KEY_DEMAND_ID, demandId);
            startActivity(intent);
        }
    }

    private class ConfirmItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position - mConfirmListView.getHeaderViewsCount() < 0)
                return;
            demandId = mConfirmData.get(position - mConfirmListView.getHeaderViewsCount()).getDemandId();
            Intent intent = new Intent(getContext(), ActRequirementDetail.class);
            intent.putExtra(IntentKey.INTENT_KEY_DEMAND_ID, demandId);
            startActivity(intent);

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

    public void jumpToDetail() {
        mVpContent.setCurrentItem(curPos);
        Intent intent = new Intent(getContext(), ActRequirementDetail.class);
        intent.putExtra(IntentKey.INTENT_KEY_DEMAND_ID, demandId);
        startActivity(intent);
    }

}
