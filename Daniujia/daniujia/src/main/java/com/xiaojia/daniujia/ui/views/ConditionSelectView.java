package com.xiaojia.daniujia.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.domain.resp.ExpertInfoEntity;
import com.xiaojia.daniujia.ui.adapters.ConditionSelectLeftAdapter;
import com.xiaojia.daniujia.ui.adapters.ConditionSelectRightAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/15.
 */
public class ConditionSelectView extends LinearLayout {
    private ListView mLeftList;
    private ListView mRightList;
    private ConditionSelectLeftAdapter mLeftAdapter;
    private ConditionSelectRightAdapter mRightAdapter;
    private int mLeftCurrentSelect = 0;
    private int mRightCurrentSelect = 0;
    private RightListViewItemClickListener mRightItemClickListener;

    private List<ExpertInfoEntity.Direction.SubDirection> mRightData;
    private List<ExpertInfoEntity.Direction> mData;
    private int mAlreadySelectedPosition = -1;

    private boolean isOnlyRightList = false;

    public ConditionSelectView(Context context) {
        this(context, null);
    }

    public ConditionSelectView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ConditionSelectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(HORIZONTAL);
        View view = LayoutInflater.from(context).inflate(R.layout.condition_select_view, null);
        addView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mLeftList = (ListView) view.findViewById(R.id.list_left);
        mRightList = (ListView) view.findViewById(R.id.list_right);

        mData = new ArrayList<>();
        mRightData = new ArrayList<>();

        // 适配器在于数据绑定的时候
        mLeftAdapter = new ConditionSelectLeftAdapter(mData, context, true);
        mRightAdapter = new ConditionSelectRightAdapter(mRightData, context, false);

        mLeftList.setAdapter(mLeftAdapter);
        mRightList.setAdapter(mRightAdapter);

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mLeftList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mLeftCurrentSelect = position;
                mLeftAdapter.setmCurrentSelect(position);
                mLeftAdapter.notifyDataSetChanged();

                if (mData.get(position) == null || mData.get(position).getSubDirections() == null
                        || mData.get(position).getSubDirections().size() == 0)
                    return;

                mRightAdapter.setData(mData.get(position).getSubDirections());
                if (mAlreadySelectedPosition == position){
                    mRightAdapter.setmCurrentSelect(mRightCurrentSelect);
                } else {
                    mRightAdapter.setmCurrentSelect(-1);
                }
                mRightAdapter.notifyDataSetChanged();
            }
        });

        mRightList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mRightCurrentSelect = position;
                mRightAdapter.setmCurrentSelect(position);
                mRightAdapter.notifyDataSetChanged();

                if (mRightItemClickListener != null) {
                    mAlreadySelectedPosition = -1;
                    if (isOnlyRightList){
                        mRightItemClickListener.onRightItemClickListener(0, mRightCurrentSelect, 0, "",
                                mRightAdapter.getItemData(mRightCurrentSelect).getSubDirectionId(),
                                mRightAdapter.getItemData(mRightCurrentSelect).getName());
                    } else {
                        mRightItemClickListener.onRightItemClickListener(mLeftCurrentSelect,
                                mRightCurrentSelect, mLeftAdapter.getItemData(mLeftCurrentSelect).getMainDirectionId(),
                                mLeftAdapter.getItemData(mLeftCurrentSelect).getName(),
                                mRightAdapter.getItemData(mRightCurrentSelect).getSubDirectionId(),
                                mRightAdapter.getItemData(mRightCurrentSelect).getName());
                    }

                }
            }
        });
    }

    public void setmLeftCurrentSelect(int mLeftCurrentSelect) {
        this.mLeftCurrentSelect = mLeftCurrentSelect;
        mAlreadySelectedPosition = mLeftCurrentSelect;
    }

    public void setmRightCurrentSelect(int mRightCurrentSelect) {
        this.mRightCurrentSelect = mRightCurrentSelect;
    }

    public void setLeftData(List<ExpertInfoEntity.Direction> mLeftData) {
        if (mLeftData == null || mLeftData.size() == 0){
            mLeftAdapter.clearData();
            mLeftAdapter.notifyDataSetChanged();
            mRightAdapter.clearData();
            mRightAdapter.notifyDataSetChanged();
            return;
        }
        isOnlyRightList = false;
        mLeftAdapter.setmCurrentSelect(mLeftCurrentSelect);
        mLeftAdapter.setData(mLeftData);
        mLeftAdapter.notifyDataSetChanged();

        // 别的tab切换过来的时候，如果未选择，那么右边的ListView数据需要清空
        if (mLeftCurrentSelect >= mLeftData.size()){
            mRightAdapter.clearData();
            mRightAdapter.notifyDataSetChanged();
            return;
        }
        // 坑爹的需求：在切换tab的时候，竟然要求左边的ListView在没有选中的情况下，右边需要显示数据
        // 这就等于是说，选中或者未选中左边ListView的时候，右边都需要数据。
        if (mLeftCurrentSelect == -1)
            mLeftCurrentSelect = 0;

        if (mLeftData.get(mLeftCurrentSelect) == null || mLeftData.get(mLeftCurrentSelect).getSubDirections() == null
                || mLeftData.get(mLeftCurrentSelect).getSubDirections().size() == 0){
            mRightAdapter.clearData();
            mRightAdapter.notifyDataSetChanged();
            return;
        }

        // 引用型变量，改变adapter数据源的时候，其实也是在改变mRightData
        mRightAdapter.setmCurrentSelect(mRightCurrentSelect);

        // 注意这里显示左边ListView选中的数据
        mRightAdapter.setData(mLeftData.get(mLeftCurrentSelect).getSubDirections());
        mRightAdapter.notifyDataSetChanged();
    }

    public void setRightData(List<ExpertInfoEntity.Direction.SubDirection> rightData){
        if (rightData != null && rightData.size() == 0)
            return;
        isOnlyRightList = true;
        mRightAdapter.setmCurrentSelect(mRightCurrentSelect);
        mRightAdapter.setData(rightData);
        mRightAdapter.notifyDataSetChanged();
    }

    public void isGoneRightList(boolean flag) {
        mRightList.setVisibility(flag ? GONE : VISIBLE);
    }

    public void isGoneLeftList(boolean flag){
        mLeftList.setVisibility(flag ? GONE : VISIBLE);
    }

    public void setRightShowBlueLine(boolean isShow){
        mRightAdapter.setLeftList(isShow);
    }

    public void setRightItemClickListener(RightListViewItemClickListener mRightItemClickListener) {
        this.mRightItemClickListener = mRightItemClickListener;
    }

    public interface RightListViewItemClickListener {
        void onRightItemClickListener(int leftPosition, int rightPosition,
                                      int leftId, String leftName, int rightId, String rightName);
    }
}
