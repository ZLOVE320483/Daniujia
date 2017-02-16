package com.xiaojia.daniujia.ui.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.domain.resp.WorkExperienceVo;
import com.xiaojia.daniujia.utils.TimeUtils;

import java.util.List;

/**
 * Created by ZLOVE on 2016/6/28.
 */
public class ExpertWorkExperienceAdapter extends SingleDataListAdapter<WorkExperienceVo.CareersEntity> {

    public ExpertWorkExperienceAdapter(List<WorkExperienceVo.CareersEntity> data, Context context) {
        super(data, context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_expert_work_experience, null);
            holder = new ViewHolder();
            holder.tvWorkDuration = (TextView) convertView.findViewById(R.id.work_duration);
            holder.tvWorkTime = (TextView) convertView.findViewById(R.id.work_time);
            holder.tvPosition = (TextView) convertView.findViewById(R.id.id_position);
            holder.tvCorp = (TextView) convertView.findViewById(R.id.id_corp);
            holder.tvDesc = (TextView) convertView.findViewById(R.id.id_desc);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        WorkExperienceVo.CareersEntity item = getItem(position);
        if (item !=null) {
            if (item.getQuitTime() == 0) {
                holder.tvWorkDuration.setText(TimeUtils.getTime(item.getEntryTime() * 1000, TimeUtils.DATE_FORMAT_CAREER) + "- 现在");
            } else {
                holder.tvWorkDuration.setText(TimeUtils.getTime(item.getEntryTime() * 1000, TimeUtils.DATE_FORMAT_CAREER) + "-" + TimeUtils.getTime(item.getQuitTime() * 1000, TimeUtils.DATE_FORMAT_CAREER));
            }
            long endTime;
            if (item.getQuitTime() == 0) {
                endTime = System.currentTimeMillis()/1000;
            }else{
                endTime = item.getQuitTime();
            }
            holder.tvWorkTime.setText("[" + TimeUtils.getCareerDuration(item.getEntryTime(), endTime) + "]");
            holder.tvPosition.setText(item.getPosition());
            holder.tvCorp.setText(item.getCompany());
            if (TextUtils.isEmpty(item.getPositionDesc())) {
                holder.tvDesc.setVisibility(View.GONE);
            } else {
                holder.tvDesc.setVisibility(View.VISIBLE);
                holder.tvDesc.setText(item.getPositionDesc());
            }
        }

        return convertView;
    }

    class ViewHolder {
        TextView tvWorkDuration;
        TextView tvWorkTime;
        TextView tvPosition;
        TextView tvCorp;
        TextView tvDesc;
    }
}
