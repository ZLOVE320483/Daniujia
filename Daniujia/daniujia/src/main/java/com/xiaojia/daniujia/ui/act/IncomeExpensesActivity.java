package com.xiaojia.daniujia.ui.act;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.base.AbsBaseActivity;
import com.xiaojia.daniujia.domain.resp.AccBalanceVo;
import com.xiaojia.daniujia.managers.OkHttpClientManager;
import com.xiaojia.daniujia.managers.OkHttpClientManager.ResultCallback;
import com.xiaojia.daniujia.ui.adapters.BalanceListAdapter;
import com.xiaojia.daniujia.ui.views.XListView;
import com.xiaojia.daniujia.utils.ListUtils;
import com.xiaojia.daniujia.utils.SysUtil;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_income_expenses)
public class IncomeExpensesActivity extends AbsBaseActivity implements View.OnClickListener, XListView.IXListViewListener {
	@ViewInject(R.id.list)
	private XListView mListView;
	private BalanceListAdapter mAdapter;
	private int mCurPageNum = 1;
	private List<AccBalanceVo.RecordItem> records = new ArrayList<>();
	private boolean needShowDialog = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		initTitle();
		initData();
	}

	private void initTitle() {
		TextView titleTv = (TextView) findViewById(R.id.id_title);
		titleTv.setText(R.string.income_expenses_detail);
		findViewById(R.id.id_back).setOnClickListener(this);

		mListView.setXListViewListener(this);
		mAdapter = new BalanceListAdapter(records, this);
		mListView.setAdapter(mAdapter);
		mListView.setPullRefreshEnable(true);
		mListView.setPullLoadEnable(true);
	}

	private void initData() {
		String url = Config.WEB_API_SERVER + "/user/accountrecord/list/"
				+ SysUtil.getIntPref(PrefKeyConst.PREF_USER_ID) + "/" + mCurPageNum;
		OkHttpClientManager.getInstance(activity).getWithToken(needShowDialog, url, new ResultCallback<AccBalanceVo>() {

			@Override
			public void onResponse(AccBalanceVo result) {
				if (result == null) {
					return;
				}
				if (mCurPageNum == 1) {
					mListView.stopRefresh();
					records.clear();
				} else {
					if (result.records.size() > 0) {
						mListView.stopLoadMore();
					} else {
						mListView.setPullLoadEnable(false);
					}
				}
				if (!ListUtils.isEmpty(result.records)) {
					records.addAll(result.records);
				}
				mAdapter.notifyDataSetChanged();
			}
		});
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.id_back) {
			finish();
		}
	}

	@Override
	public void onRefresh() {
		needShowDialog = false;
		mCurPageNum = 1;
		initData();
	}

	@Override
	public void onLoadMore() {
		needShowDialog = true;
		++mCurPageNum;
		initData();
	}
}
