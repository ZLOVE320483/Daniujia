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
import com.xiaojia.daniujia.ui.adapters.GridViewTagSpaceAdapterIndustry;
import com.xiaojia.daniujia.ui.adapters.HomeExpertAdapter;
import com.xiaojia.daniujia.ui.adapters.HorizontalListAdapterIndustry;
import com.xiaojia.daniujia.ui.control.IndustryControl;
import com.xiaojia.daniujia.ui.views.HorizontalListView;
import com.xiaojia.daniujia.ui.views.XListView;
import com.xiaojia.daniujia.utils.ListUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2016/4/28
 */
public class IndustryFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener, XListView.IXListViewListener {

    private IndustryControl control;
    private XListView mList;
    private LinearLayout mSwitchIndustry;
    /**
     * 这个是整个横向滑动的layout布局，显示的布局为一个横向滑动的ListView，隐藏的布局为
     * 一个向下箭头和一个TextView和一个GridView。当横向滑动的item数目小于3个时，整个布
     * 局隐藏；当在3个和8个之间时，横向ListView显示，当在8个以上时，ListView和向下箭头
     * 显示，按向下箭头显示出GridView。
     */
    private RelativeLayout title_layout;
    private LinearLayout mAllIndustry;

    private HorizontalListAdapterIndustry mHorizonAdapter;
    private GridViewTagSpaceAdapterIndustry mGridAdapter;

    private HorizontalListView horizontalListView;
    private HomeExpertAdapter homeExpertAdapter;

    private int mainDirectionId = 0;
    private String subDirectionCode = "0000000";
    private List<NovationByTopicVo.FilterDirectionsEntity> subDirections;
    private List<ConsultEntity> dataSource;

    private ImageView arrow_upon;
    private ImageView arrow_below;

    private GridView gridView;

    private View noDataContainer;
    private ImageView ivNoData;
    private TextView tvNoDataTip;

    private boolean isFirstRefresh = true;

    private int headerCount;
    private String titleStr = "";
    private int startPosition;
    private Animation animation;

    @Override
    protected int getInflateLayout() {
        return R.layout.fg_entrepreneurship_guide;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        control = new IndustryControl();
        setControl(control);
        subDirections = new ArrayList<>();
        dataSource = new ArrayList<>();

        Intent intent = getActivity().getIntent();
        if (intent != null) {
            if (intent.hasExtra(IntentKey.INTENT_KEY_MAIN_DIRECTION)) {
                mainDirectionId = intent.getIntExtra(IntentKey.INTENT_KEY_MAIN_DIRECTION, 0);
            }
            if (intent.hasExtra(IntentKey.INTENT_KEY_SUB_DIRECTION)) {
                subDirectionCode = intent.getStringExtra(IntentKey.INTENT_KEY_SUB_DIRECTION);
            }
            if (intent.hasExtra(ExtraConst.EXTRA_WEB_TITLE)) {
                titleStr = getActivity().getIntent().getStringExtra(ExtraConst.EXTRA_WEB_TITLE);
            }
            if (intent.hasExtra(ExtraConst.EXTRA_POSITION)) {
                String position = intent.getStringExtra(ExtraConst.EXTRA_POSITION);
                startPosition = Integer.parseInt(position);
            }
        }

    }

    @Override
    protected void setUpView(View view) {
        setBackButton(view.findViewById(R.id.iv_title_back));
        TextView title = (TextView) view.findViewById(R.id.tv_title);
        title.setText(titleStr);

        noDataContainer = view.findViewById(R.id.no_data);
        ivNoData = (ImageView) view.findViewById(R.id.id_no_data);
        tvNoDataTip = (TextView) view.findViewById(R.id.no_data_tip);

        ivNoData.setImageResource(R.drawable.bg_no_expert_classify);
        tvNoDataTip.setText("我们正在寻找此类专家");

        mList = (XListView) view.findViewById(R.id.id_list);
        homeExpertAdapter = new HomeExpertAdapter(dataSource, getActivity());

        mList.setAdapter(homeExpertAdapter);
        mList.setOnItemClickListener(this);
        mList.setXListViewListener(this);
        mList.setPullRefreshEnable(true);

        headerCount = mList.getHeaderViewsCount();

        horizontalListView = (HorizontalListView) view.findViewById(R.id.title_indicator);

        mHorizonAdapter = new HorizontalListAdapterIndustry(subDirections, getActivity());
        horizontalListView.setAdapter(mHorizonAdapter);
        horizontalListView.setOnItemClickListener(this);

        mAllIndustry = (LinearLayout) view.findViewById(R.id.ll_before_click);
        mSwitchIndustry = (LinearLayout) view.findViewById(R.id.ll_after_click);
        gridView = (GridView) view.findViewById(R.id.gridView);
        title_layout = (RelativeLayout) view.findViewById(R.id.title_layout);

        mGridAdapter = new GridViewTagSpaceAdapterIndustry(subDirections, getActivity());
        gridView.setAdapter(mGridAdapter);
        gridView.setOnItemClickListener(this);

        arrow_below = (ImageView) view.findViewById(R.id.image_before_click);
        arrow_below.setOnClickListener(this);

        arrow_upon = (ImageView) view.findViewById(R.id.image_after_click);
        arrow_upon.setOnClickListener(this);
        control.initData(isFirstRefresh, mainDirectionId, subDirectionCode);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_after_click:

                hideGrid();
                break;
            case R.id.image_before_click:
                mSwitchIndustry.setVisibility(View.VISIBLE);
                mAllIndustry.setVisibility(View.GONE);
                mGridAdapter.setSelection(mHorizonAdapter.getSelectPosition());
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent == mList) {
            if (ListUtils.isEmpty(dataSource)) {
                return;
            }
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
                subDirectionCode = subDirections.get(position).getCode();
                control.pageNum = 1;
            }
            isFirstRefresh = true;
            control.initData(isFirstRefresh, mainDirectionId, subDirectionCode);
        }
    }

    @Override
    public void onRefresh() {
        control.onRefresh(isFirstRefresh, mainDirectionId, subDirectionCode);
    }

    @Override
    public void onLoadMore() {
        isFirstRefresh = false;
        control.onLoadMore(isFirstRefresh, mainDirectionId, subDirectionCode);
    }

    public void refreshData(NovationByTopicVo result) {
        isFirstRefresh = false;
        mList.stopRefresh();
        mList.stopLoadMore();
        if (result == null) {
            noDataContainer.setVisibility(View.VISIBLE);
            return;
        }
        initHorizontal(result);

        List<ConsultEntity> tmpList = result.getConsultants();
        if (control.pageNum == 1 && ListUtils.isEmpty(tmpList)) {
            noDataContainer.setVisibility(View.VISIBLE);
        } else {
            noDataContainer.setVisibility(View.GONE);
        }

        if (control.pageNum == 1) {
            dataSource.clear();
        }
        dataSource.addAll(tmpList);
        homeExpertAdapter.notifyDataSetChanged();

        if (tmpList.size() < 10) {
            mList.setPullLoadEnable(false);
        } else {
            mList.setPullLoadEnable(true);
        }
    }

    private void initHorizontal(NovationByTopicVo result) {
        if (!ListUtils.isEmpty(result.getFilterDirections()) && control.pageNum == 1) {
            subDirections.clear();
            subDirections.addAll(result.getFilterDirections());
        }
        if (result.getFilterDirections().size() < 3) {
            title_layout.setVisibility(View.GONE);
            arrow_below.setVisibility(View.GONE);
        } else if (result.getFilterDirections().size() < 8) {
            title_layout.setVisibility(View.VISIBLE);
            arrow_below.setVisibility(View.GONE);
            if (startPosition != -1) {
                mHorizonAdapter.setSelectPosition(startPosition);
                startPosition = -1;
            } else {
                mHorizonAdapter.notifyDataSetChanged();
            }
        } else {
            title_layout.setVisibility(View.VISIBLE);
            arrow_below.setVisibility(View.VISIBLE);
            if (startPosition != -1) {
                mHorizonAdapter.setSelectPosition(startPosition);
                startPosition = -1;
            }
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
                mSwitchIndustry.setVisibility(View.GONE);
                mAllIndustry.setVisibility(View.VISIBLE);
                mHorizonAdapter.setSelectPosition(mGridAdapter.getSelection());
                horizontalListView.moveToDestination(mGridAdapter.getSelection());
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        mSwitchIndustry.startAnimation(animation);
    }
}
