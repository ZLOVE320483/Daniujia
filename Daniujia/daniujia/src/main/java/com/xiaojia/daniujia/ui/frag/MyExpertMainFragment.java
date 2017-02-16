package com.xiaojia.daniujia.ui.frag;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.xiaojia.daniujia.IntentKey;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.ui.views.PagerSlidingTabStripForOrder;
import com.xiaojia.daniujia.utils.LogUtil;

/**
 * Created by ZLOVE on 2016/5/5.
 */
public class MyExpertMainFragment extends BaseFragment implements ViewPager.OnPageChangeListener, MyExpertDoingFragment.MyExpertUnReadListener {

    private static String[] titles = {"进行中", "待评价", "已结束"};

    private PagerSlidingTabStripForOrder tabHead;
    private ViewPager viewPager;
    private MyExpertPagerAdapter pagerAdapter;

    private MyExpertDoingFragment doingFragment;
    private MyExpertWaitToCommentFragment waitToCommentFragment;
    private MyExpertFinishedFragment finishedFragment;

    @Override
    protected int getInflateLayout() {
        return R.layout.frag_my_expert;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setUpView(View view) {
        setBackButton(view.findViewById(R.id.id_back));
        ((TextView) view.findViewById(R.id.id_title)).setText("我约的专家");

        tabHead = (PagerSlidingTabStripForOrder) view.findViewById(R.id.tab_head);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        pagerAdapter = new MyExpertPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(this);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {}

    class MyExpertPagerAdapter extends FragmentPagerAdapter {

        public MyExpertPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int arg0) {
            switch (arg0) {
                case 0: {
                    if (doingFragment == null) {
                        doingFragment = new MyExpertDoingFragment();
                        doingFragment.setListener(MyExpertMainFragment.this);
                    }
                    return doingFragment;
                }

                case 1: {
                    if (waitToCommentFragment == null) {
                        waitToCommentFragment = new MyExpertWaitToCommentFragment();
                    }
                    return waitToCommentFragment;
                }

                case 2: {
                    if (finishedFragment == null) {
                        finishedFragment = new MyExpertFinishedFragment();
                    }
                    return finishedFragment;
                }

                default:
                    break;
            }
            return null;
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

    @Override
    public void isRead(boolean oneAllRead, boolean twoAllRead, boolean threeAllRead) {
        tabHead.setViewPager(viewPager, oneAllRead, twoAllRead, threeAllRead);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtil.d("ZLOVE", "MyExpertMainFragment---onActivityResult---requestCode---" + requestCode);
        if (requestCode == IntentKey.REQ_CODE_ORDER_DETAIL) {
            if (doingFragment != null) {
                doingFragment.onRefresh();
            }
            if (data != null && data.hasExtra(IntentKey.INTENT_KEY_ORDER_STATUS_CHANGE)) {
                boolean isChange = data.getBooleanExtra(IntentKey.INTENT_KEY_ORDER_STATUS_CHANGE, false);
                if (isChange) {
                    if (doingFragment != null) {
                        doingFragment.onRefresh();
                    }
                    if (waitToCommentFragment != null) {
                        waitToCommentFragment.onRefresh();
                    }
                    if (finishedFragment != null) {
                        finishedFragment.onRefresh();
                    }
                }
            }
        }
    }
}
