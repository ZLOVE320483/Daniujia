package com.xiaojia.daniujia.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.domain.resp.EducationVo;
import com.xiaojia.daniujia.utils.TimeUtils;
import com.xiaojia.daniujia.utils.WUtil;

import java.util.List;

/**
 * Created by ZLOVE on 2016/6/28.
 */
public class ExpertEducationBackgroundAdapter extends SingleDataListAdapter<EducationVo.EducationsEntity> {

    public ExpertEducationBackgroundAdapter(List<EducationVo.EducationsEntity> data, Context context) {
        super(data, context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_expert_education_background, null);
            holder.tvEducationDuration = (TextView) convertView.findViewById(R.id.education_duration);
            holder.tvSchool = (TextView) convertView.findViewById(R.id.id_school);
            holder.tvDegree = (TextView) convertView.findViewById(R.id.id_degree);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        EducationVo.EducationsEntity item = getItem(position);
        if (item != null) {
            if (item.getEndTime() == 0) {
                holder.tvEducationDuration.setText(TimeUtils.getTime(item.getBeginTime() * 1000, TimeUtils.DATE_FORMAT_CAREER) + "- 现在");
            } else {
                holder.tvEducationDuration.setText(TimeUtils.getTime(item.getBeginTime() * 1000, TimeUtils.DATE_FORMAT_CAREER) + "-" + TimeUtils.getTime(item.getEndTime() * 1000, TimeUtils.DATE_FORMAT_CAREER));
            }
            holder.tvSchool.setText(item.getName());
            holder.tvDegree.setText(WUtil.transformDegree(item.getDegree()));
        }
        return convertView;
    }

    class ViewHolder {
        TextView tvEducationDuration;
        TextView tvSchool;
        TextView tvDegree;
    }
}
