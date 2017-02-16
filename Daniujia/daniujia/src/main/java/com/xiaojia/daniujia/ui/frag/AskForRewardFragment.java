package com.xiaojia.daniujia.ui.frag;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import com.xiaojia.daniujia.IntentKey;
import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.ask.AskRewardWebActivity;
import com.xiaojia.daniujia.ui.act.ActAskRewardRule;
import com.xiaojia.daniujia.ui.act.ActLogin;
import com.xiaojia.daniujia.ui.views.PagerSlidingTabStrip;
import com.xiaojia.daniujia.utils.SysUtil;

/**
 * Created by ZLOVE on 2016/10/24.
 */
public class AskForRewardFragment extends BaseFragment implements View.OnClickListener {

    private static String titles[] = {"进行中", "已截止"};

    private View ruleView;
    private Button btnPublish;
    private PagerSlidingTabStrip tabHead;
    private ViewPager viewPager;
    private AskForwardPagerAdapter pagerAdapter;

    private AskRewardDoingFragment doingFragment;
    private AskRewardFinishedFragment finishedFragment;

    @Override
    protected int getInflateLayout() {
        return R.layout.frag_ask_for_reward;
    }

    @Override
    protected void setUpView(View view) {
        ruleView = view.findViewById(R.id.id_rule);
        btnPublish = (Button) view.findViewById(R.id.btn_publish);
        tabHead = (PagerSlidingTabStrip) view.findViewById(R.id.tab_head);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);

        pagerAdapter = new AskForwardPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabHead.setViewPager(viewPager);
        btnPublish.setOnClickListener(this);
        ruleView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnPublish) {
            if (!SysUtil.getBooleanPref(PrefKeyConst.PREF_IS_LOGIN)) {
                Intent intent = new Intent(getActivity(), ActLogin.class);
                startActivityForResult(intent, IntentKey.REQ_CODE_ASK_LOGIN);
            } else {
                Intent intent = new Intent(getActivity(), AskRewardWebActivity.class);
                intent.putExtra(IntentKey.INTENT_KEY_ASK_REWARD_FROM, IntentKey.ASK_REWARD_PUBLISH);
                startActivity(intent);
            }
        } else if (v == ruleView) {
            Intent intent = new Intent(getActivity(), ActAskRewardRule.class);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.dialog_enter, R.anim.dialog_exit);
        }
    }

    class AskForwardPagerAdapter extends FragmentPagerAdapter {

        public AskForwardPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    if (doingFragment == null) {
                        doingFragment = new AskRewardDoingFragment();
                    }
                    return doingFragment;

                case 1:
                    if (finishedFragment == null) {
                        finishedFragment = new AskRewardFinishedFragment();
                    }
                    return finishedFragment;

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentKey.REQ_CODE_ASK_LOGIN && data != null) {
            Intent intent = new Intent(getActivity(), AskRewardWebActivity.class);
            intent.putExtra(IntentKey.INTENT_KEY_ASK_REWARD_FROM, IntentKey.ASK_REWARD_PUBLISH);
            startActivity(intent);
        }
    }
}
