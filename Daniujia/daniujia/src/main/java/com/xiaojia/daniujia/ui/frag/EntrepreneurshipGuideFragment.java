package com.xiaojia.daniujia.ui.frag;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaojia.daniujia.ExtraConst;
import com.xiaojia.daniujia.IntentKey;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.domain.resp.ConsultEntity;
import com.xiaojia.daniujia.domain.resp.NovationByTopicVo;
import com.xiaojia.daniujia.ui.act.ActExpertHome;
import com.xiaojia.daniujia.ui.adapters.GridViewTagSpaceAdapter;
import com.xiaojia.daniujia.ui.adapters.HomeExpertAdapter;
import com.xiaojia.daniujia.ui.adapters.HorizontalListAdapter;
import com.xiaojia.daniujia.ui.control.EntrepreneurshipGuideControl;
import com.xiaojia.daniujia.ui.views.HorizontalListView;
import com.xiaojia.daniujia.ui.views.XListView;
import com.xiaojia.daniujia.utils.ListUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/28
 */
public class EntrepreneurshipGuideFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener, XListView.IXListViewListener {

    private XListView mList;
    private LinearLayout mAllIndustry;
    private LinearLayout mSwitchIndustry;
    private EntrepreneurshipGuideControl control;
    private int mainDirectionId = 0;
    private String subDirectionCode = "0000000";
    private List<NovationByTopicVo.FilterDirectionsEntity> subDirections;
    private List<ConsultEntity> dataSource;
    private HorizontalListView horizontalListView;
    private HorizontalListAdapter mHorizonAdapter;
    private ImageView arrow_upon;
    private ImageView arrow_below;
    private GridView gridView;
    public  String fromWhere;
    private String name;
    private boolean isRefresh = true;
    private int headerCount;
    private RelativeLayout title_layout;
    private Animation animation;

    private GridViewTagSpaceAdapter mGridAdapter;
    private HomeExpertAdapter homeExpertAdapter;

    private boolean isFirstRefresh = true;

    private View noDataContainer;
    private ImageView ivNoData;
    private TextView tvNoDataTip;


    @Override
    protected int getInflateLayout() {
        return R.layout.fg_entrepreneurship_guide;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        control = new EntrepreneurshipGuideControl();
        setControl(control);
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            if (intent.hasExtra(IntentKey.INTENT_KEY_MAIN_DIRECTION)) {
                mainDirectionId = intent.getIntExtra(IntentKey.INTENT_KEY_MAIN_DIRECTION, 0);
            }
            if (intent.hasExtra(IntentKey.INTENT_KEY_SUB_DIRECTION)) {
                subDirectionCode = intent.getStringExtra(IntentKey.INTENT_KEY_SUB_DIRECTION);
            }
            if (intent.hasExtra(IntentKey.FROM_WHERE)) {
                fromWhere = intent.getStringExtra(IntentKey.FROM_WHERE);
            }
            if (intent.hasExtra(IntentKey.HOME_THEME_NAME)) {
                name = intent.getStringExtra(IntentKey.HOME_THEME_NAME);
            }
        }
        subDirections = new ArrayList<>();
        dataSource = new ArrayList<>();
    }

    @Override
    protected void setUpView(View view) {
        setBackButton(view.findViewById(R.id.iv_title_back));
        TextView title = (TextView) view.findViewById(R.id.tv_title);

        noDataContainer = view.findViewById(R.id.no_data);
        ivNoData = (ImageView) view.findViewById(R.id.id_no_data);
        tvNoDataTip = (TextView) view.findViewById(R.id.no_data_tip);

        mList = (XListView) view.findViewById(R.id.id_list);
        homeExpertAdapter = new HomeExpertAdapter(dataSource, getActivity());
        mList.setAdapter(homeExpertAdapter);
        mList.setXListViewListener(this);
        mList.setPullRefreshEnable(true);
        mList.setOnItemClickListener(this);
        horizontalListView = (HorizontalListView) view.findViewById(R.id.title_indicator);

        mHorizonAdapter = new HorizontalListAdapter(subDirections, getActivity());
        horizontalListView.setAdapter(mHorizonAdapter);
        horizontalListView.setOnItemClickListener(this);

        mAllIndustry = (LinearLayout) view.findViewById(R.id.ll_before_click);
        mSwitchIndustry = (LinearLayout) view.findViewById(R.id.ll_after_click);
        gridView = (GridView) view.findViewById(R.id.gridView);

        title_layout = (RelativeLayout) view.findViewById(R.id.title_layout);
        mGridAdapter = new GridViewTagSpaceAdapter(subDirections, getActivity());
        gridView.setAdapter(mGridAdapter);
        gridView.setOnItemClickListener(this);
        arrow_below = (ImageView) view.findViewById(R.id.image_before_click);
        arrow_below.setOnClickListener(this);

        arrow_upon = (ImageView) view.findViewById(R.id.image_after_click);
        arrow_upon.setOnClickListener(this);

        if (ExtraConst.FROM_MY_COLLECTION.equals(fromWhere)) {
            title.setText(R.string.my_collection);
            tvNoDataTip.setText("收藏专家,以备不时之需");
        } else if (ExtraConst.FROM_ENTER_GUIDE.equals(fromWhere)) {
            title.setText(name);
            ivNoData.setImageResource(R.drawable.bg_no_expert_classify);
            tvNoDataTip.setText("我们正在寻找此类专家");
        }

        control.initData(isFirstRefresh, mainDirectionId, subDirectionCode, subDirectionCode);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_after_click:
                hideGrid();
                break;
            case R.id.image_before_click:
                mAllIndustry.setVisibility(View.GONE);
                mSwitchIndustry.setVisibility(View.VISIBLE);
                mGridAdapter.setSelection(mHorizonAdapter.getSelectPosition());
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (ListUtils.isEmpty(subDirections)) {
            return;
        }
        if (parent == mList) {
            headerCount = mList.getHeaderViewsCount();
            Intent intent = new Intent(getActivity(), ActExpertHome.class);
            intent.putExtra(IntentKey.INTENT_KEY_EXPERT_ID, dataSource.get(position - headerCount).getConsultantId());
            startActivity(intent);
        } else {
            if (subDirectionCode.equals(subDirections.get(position).getCode())) {
                return;
            }
            if (parent == horizontalListView) {
                mHorizonAdapter.setSelectPosition(position);
            } else if (parent == gridView) {
                mGridAdapter.setSelection(position);
                hideGrid();
            }
            if (!subDirectionCode.equals(subDirections.get(position).getCode())) {
                isRefresh = true;
                subDirectionCode = subDirections.get(position).getCode();
                control.pageNum = 1;
            }
            isFirstRefresh = true;
            control.initData(isFirstRefresh, mainDirectionId, subDirectionCode, subDirectionCode);
        }
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        control.onRefresh(isFirstRefresh, mainDirectionId, subDirectionCode, subDirectionCode);
    }

    @Override
    public void onLoadMore() {
        isFirstRefresh = false;
        isRefresh = false;
        control.onLoadMore(isFirstRefresh, mainDirectionId, subDirectionCode, subDirectionCode);
    }

    public void refreshData(NovationByTopicVo result) {
        mList.stopRefresh();
        mList.stopLoadMore();
        isFirstRefresh = false;
        if (result == null) {
            noDataContainer.setVisibility(View.VISIBLE);
            return;
        }
        List<NovationByTopicVo.FilterDirectionsEntity> tmpSubDirections = result.getFilterDirections();
        if (!ListUtils.isEmpty(tmpSubDirections) && control.pageNum == 1) {
            subDirections.clear();
            subDirections.addAll(tmpSubDirections);
            for (int i = 0; i < subDirections.size(); i++) {
                if (subDirections.get(i).getCode().equals(subDirectionCode)) {
                    mHorizonAdapter.setSelectPosition(i);
                    mGridAdapter.setSelection(i);
                    break;
                }
            }
        }
        List<ConsultEntity> tmpList = result.getConsultants();
        if (isRefresh && ListUtils.isEmpty(tmpList)) {
            noDataContainer.setVisibility(View.VISIBLE);
        } else {
            noDataContainer.setVisibility(View.GONE);
        }

        if (isRefresh) {
            dataSource.clear();
        }
        dataSource.addAll(result.getConsultants());
        homeExpertAdapter.notifyDataSetChanged();

        if (tmpList.size() < 10) {
            mList.setPullLoadEnable(false);
        } else {
            mList.setPullLoadEnable(true);
        }

        if (subDirections.size() < 3) {
            title_layout.setVisibility(View.GONE);
            arrow_below.setVisibility(View.GONE);
        } else if (subDirections.size() < 8) {
            title_layout.setVisibility(View.VISIBLE);
            arrow_below.setVisibility(View.GONE);
            mHorizonAdapter.notifyDataSetChanged();
        } else {
            title_layout.setVisibility(View.VISIBLE);
            arrow_below.setVisibility(View.VISIBLE);
            mGridAdapter.notifyDataSetChanged();
        }
    }

    private void hideGrid() {
        if (animation == null) {
            animation = AnimationUtils.loadAnimation(getActivity(), R.anim.slow_shrink);
        }
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mSwitchIndustry.setVisibility(View.GONE);
                        mAllIndustry.setVisibility(View.VISIBLE);
                        mHorizonAdapter.setSelectPosition(mGridAdapter.getSelection());
                        horizontalListView.moveToDestination(mGridAdapter.getSelection());
                    }
                });
            }
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mSwitchIndustry.startAnimation(animation);
    }

}
