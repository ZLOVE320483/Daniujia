package com.xiaojia.daniujia.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.domain.resp.AllDirectionRespVo;
import com.xiaojia.daniujia.utils.ListUtils;
import com.xiaojia.daniujia.utils.ScreenUtils;

import java.util.List;

/**
 * Created by ZLOVE on 2016/6/30.
 */
public class ExpertDirectionAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private List<AllDirectionRespVo.MainDirection> groupData;

    public ExpertDirectionAdapter(Context context, List<AllDirectionRespVo.MainDirection> mainDirections) {
        this.mContext = context;
        this.groupData = mainDirections;
    }

    @Override
    public int getGroupCount() {
        if (ListUtils.isEmpty(groupData)) {
            return 0;
        }
        return groupData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        List<AllDirectionRespVo.MainDirection.SubDirection> childData = groupData.get(groupPosition).getSubDirections();
        if (ListUtils.isEmpty(childData)) {
            return 0;
        }
        return childData.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupData.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        List<AllDirectionRespVo.MainDirection.SubDirection> childData = groupData.get(groupPosition).getSubDirections();
        if (ListUtils.isEmpty(childData)) {
            return null;
        }
        return childData.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder;
        if (convertView == null) {
            groupViewHolder = new GroupViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_direction_group, null);
            groupViewHolder.ivIcon = (ImageView) convertView.findViewById(R.id.icon);
            groupViewHolder.directionName = (TextView) convertView.findViewById(R.id.main_direction);
            groupViewHolder.ivArrow = (ImageView) convertView.findViewById(R.id.arrow);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        AllDirectionRespVo.MainDirection item = groupData.get(groupPosition);
        if (item != null) {
            Picasso.with(mContext).load(item.getIconUrl()).error(R.drawable.internet).resize(ScreenUtils.dip2px(18), ScreenUtils.dip2px(18)).into(groupViewHolder.ivIcon);
            groupViewHolder.directionName.setText(item.getMainName());
        }
        if (isExpanded) {
            groupViewHolder.ivArrow.setImageResource(R.drawable.grey_drag_top);
        } else {
            groupViewHolder.ivArrow.setImageResource(R.drawable.grey_drag_bottom);
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder;
        if (convertView == null) {
            childViewHolder = new ChildViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_direction_child, null);
            childViewHolder.directionName = (TextView) convertView.findViewById(R.id.sub_direction);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        AllDirectionRespVo.MainDirection.SubDirection item = groupData.get(groupPosition).getSubDirections().get(childPosition);
        if (item != null) {
            childViewHolder.directionName.setText(item.getSubName());
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class GroupViewHolder {
        ImageView ivIcon;
        TextView directionName;
        ImageView ivArrow;
    }

    class ChildViewHolder {
        TextView directionName;
    }
}
