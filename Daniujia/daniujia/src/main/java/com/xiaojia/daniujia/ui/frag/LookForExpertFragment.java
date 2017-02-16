package com.xiaojia.daniujia.ui.frag;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xiaojia.daniujia.IntentKey;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.domain.resp.SearchConditionVo;
import com.xiaojia.daniujia.domain.resp.SearchDataEntity;
import com.xiaojia.daniujia.ui.act.ActExpertSearch;
import com.xiaojia.daniujia.ui.act.ActLookForExpertByCondition;
import com.xiaojia.daniujia.ui.act.ActSpeechSearch;
import com.xiaojia.daniujia.ui.adapters.LookForDirectionAdapter;
import com.xiaojia.daniujia.ui.adapters.LookForSpecialtyAdapter;
import com.xiaojia.daniujia.ui.animadapter.SwingRightInAnimationAdapter;
import com.xiaojia.daniujia.ui.control.LookForExpertControl;
import com.xiaojia.daniujia.ui.views.PagerSlidingTabStrip;
import com.xiaojia.daniujia.utils.ListUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/25.
 */
public class LookForExpertFragment extends BaseFragment implements View.OnClickListener {
    private static String[] titles = {"按行业寻找", "按专长寻找"};

    private LookForExpertControl control;
    private PagerSlidingTabStrip mTopTab;
    private ViewPager mPagerContent;

    private LayoutInflater mLayoutInflater;

    private ArrayList<View> pagerViews = new ArrayList<>();
    private ListView lvIndustry;
    private ListView lvSpecialty;
    private LookForDirectionAdapter mIndustryAdapter;
    private LookForSpecialtyAdapter mSpecialtyAdapter;

    // 行业
    private List<SearchDataEntity.Industries> mIndustryList = new ArrayList<>();
    // 专长
    private List<SearchDataEntity.Directions> mSpecialtyList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        control = new LookForExpertControl();
        setControl(control);
    }

    @Override
    protected int getInflateLayout() {
        return R.layout.frag_look_for_expert;
    }

    @Override
    protected void setUpView(View view) {
        setBackButton(view.findViewById(R.id.look_for_iv_back));
        View searchView = view.findViewById(R.id.search_container);
        searchView.setOnClickListener(this);
        View speechSearchView = view.findViewById(R.id.speech_search);
        speechSearchView.setOnClickListener(this);

        mTopTab = (PagerSlidingTabStrip) view.findViewById(R.id.look_for_tab_head);
        mPagerContent = (ViewPager) view.findViewById(R.id.look_for_viewpager);

        mLayoutInflater = LayoutInflater.from(getActivity());

        control.initSearchData();
    }


    public void refreshData(SearchConditionVo result) {
        if (result == null)
            return;

        SearchDataEntity data = result.getItem();

        if (pagerViews == null || pagerViews.size() == 0) {
            createPagerView(titles);
        }

        mPagerContent.setAdapter(new MyViewPagerAdapter());
        mTopTab.setViewPager(mPagerContent);

        if (data != null) {
            if (data.getIndustries() != null) {
                mIndustryList.clear();
                mIndustryList.addAll(data.getIndustries());
                mIndustryAdapter = new LookForDirectionAdapter(mIndustryList, getActivity());
                SwingRightInAnimationAdapter swingRightInAnimationAdapter = new SwingRightInAnimationAdapter(mIndustryAdapter);
                swingRightInAnimationAdapter.setListView(lvIndustry);
                lvIndustry.setAdapter(swingRightInAnimationAdapter);
                lvIndustry.setOnItemClickListener(new IndustryItemClickListener());
            }

            if (data.getDirections() != null) {
                mSpecialtyList.clear();
                mSpecialtyList.addAll(data.getDirections());
            }
        }

        mTopTab.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    if (mSpecialtyAdapter == null) {
                        mSpecialtyAdapter = new LookForSpecialtyAdapter(mSpecialtyList, getActivity());
                        SwingRightInAnimationAdapter swingRightInAnimationAdapter = new SwingRightInAnimationAdapter(mSpecialtyAdapter);
                        swingRightInAnimationAdapter.setListView(lvSpecialty);
                        lvSpecialty.setAdapter(swingRightInAnimationAdapter);
                        lvSpecialty.setOnItemClickListener(new SpecialtyItemClickListener());
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        if (v.getId() == R.id.search_container) {
            intent = new Intent(getActivity(), ActExpertSearch.class);
            startActivityForResult(intent, IntentKey.REQ_CODE_SPEECH_SEARCH);
        } else if (v.getId() == R.id.speech_search) {
            intent = new Intent(getActivity(), ActSpeechSearch.class);
            startActivityForResult(intent, IntentKey.REQ_CODE_SPEECH_SEARCH);
        }
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
            View view = mLayoutInflater.inflate(R.layout.item_home_pager, null);
            ListView listView = (ListView) view.findViewById(R.id.item_home_pager_lv);
            // 设置适配器和点击事件
            if (i == 0) {
                lvIndustry = listView;
            } else {
                lvSpecialty = listView;
            }
            pagerViews.add(view);
        }
    }

    private class SpecialtyItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (ListUtils.isEmpty(mSpecialtyList)) {
                return;
            }

            SearchDataEntity.Directions entity = mSpecialtyList.get(position);
            if (entity == null) {
                return;
            }

            Intent intent = new Intent(getActivity(), ActLookForExpertByCondition.class);
            intent.putExtra(IntentKey.CONDITION_MAIN_DIRECTION_ID, mSpecialtyList.get(position).getSearchValue());
            intent.putExtra(IntentKey.CONDITION_SHARE_TEXT,mSpecialtyList.get(position).getDesc());
            startActivity(intent);

        }
    }

    private class IndustryItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (ListUtils.isEmpty(mIndustryList)) {
                return;
            }

            SearchDataEntity.Industries entity = mIndustryList.get(position);
            if (entity == null) {
                return;
            }

            Intent intent = new Intent(getActivity(), ActLookForExpertByCondition.class);
            intent.putExtra(IntentKey.CONDITION_INDUSTRYPARENTID, mIndustryList.get(position).getSearchValue());
            intent.putExtra(IntentKey.CONDITION_SHARE_TEXT,mIndustryList.get(position).getDesc());
            startActivity(intent);

        }
    }

}
