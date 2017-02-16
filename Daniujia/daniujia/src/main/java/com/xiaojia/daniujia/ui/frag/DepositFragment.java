package com.xiaojia.daniujia.ui.frag;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaojia.daniujia.IntentKey;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.ui.views.PagerSlidingTabStrip;
import com.xiaojia.daniujia.utils.LogUtil;

/**
 * Created by ZLOVE on 2016/6/3.
 */
public class DepositFragment extends BaseFragment implements View.OnClickListener {

    private static String[] titles = {"转到支付宝", "转到银行卡"};

    private PagerSlidingTabStrip tabHead;
    private ViewPager viewPager;
    private DepositPagerAdapter pagerAdapter;

    private DepositAlipayFragment alipayFragment;
    private DepositBankFragment bankFragment;
    private ImageView ivBack;

    @Override
    protected int getInflateLayout() {
        return R.layout.frag_deposit;
    }

    @Override
    protected void setUpView(View view) {
        ivBack = (ImageView) view.findViewById(R.id.id_back);
        ivBack.setOnClickListener(this);
        ((TextView) view.findViewById(R.id.id_title)).setText("提现");

        tabHead = (PagerSlidingTabStrip) view.findViewById(R.id.tab_head);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        pagerAdapter = new DepositPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabHead.setViewPager(viewPager);
    }

    @Override
    public void onClick(View v) {
        if (v == ivBack){
            finishFragmentPostBalance();
        }
    }

    public void onBackClick(){
        finishFragmentPostBalance();
    }

    private void finishFragmentPostBalance(){
        Intent intent = new Intent();
        float balance;
        if (viewPager.getCurrentItem() == 0){
            balance = alipayFragment.getBalance();
        }else {
            balance = bankFragment.getBalance();
        }
        LogUtil.d("zptest","balance: " + balance);
        intent.putExtra(IntentKey.INTENT_KEY_BALANCE, balance);
        finishActivity(intent);
    }

    class DepositPagerAdapter extends FragmentPagerAdapter {

        public DepositPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int arg0) {
            switch (arg0) {
                case 0: {
                    if (alipayFragment == null) {
                        alipayFragment = new DepositAlipayFragment();
                    }
                    return alipayFragment;
                }

                case 1: {
                    if (bankFragment == null) {
                        bankFragment = new DepositBankFragment();
                    }
                    return bankFragment;
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
}
