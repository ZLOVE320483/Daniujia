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
 * Created by Administrator on 2016/5/24.
 */
public class WorkExperienceAdapter extends SingleDataListAdapter<WorkExperienceVo.CareersEntity>{

    public WorkExperienceAdapter(List<WorkExperienceVo.CareersEntity> data, Context context) {
        super(data, context);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.work_experience, null);
        WorkExperienceVo.CareersEntity careersEntity = mData.get(position);
        TextView workAge_tv = (TextView) view.findViewById(R.id.workAge);
        TextView position_tv = (TextView) view.findViewById(R.id.position);
        TextView detail_tv = (TextView) view.findViewById(R.id.detail);
        View container = view.findViewById(R.id.container);
        long endTime;
        if (careersEntity.getQuitTime() == 0) {
            endTime = System.currentTimeMillis()/1000;
        }else{
            endTime = careersEntity.getQuitTime();
        }
        workAge_tv.setText(TimeUtils.getCareerDuration(careersEntity.getEntryTime(), endTime) + "工作经验");
        position_tv.setText(careersEntity.getPosition() + "  |  " + careersEntity.getCompany());
        if (TextUtils.isEmpty(careersEntity.getPositionDesc())) {
            container.setVisibility(View.GONE);
        } else {
            container.setVisibility(View.VISIBLE);
            detail_tv.setText(careersEntity.getPositionDesc());
        }

        View content_divide = view.findViewById(R.id.content_divide);
        View content_divide_margin = view.findViewById(R.id.content_divide_margin);
        if (position == mData.size() - 1) {
            content_divide.setVisibility(View.VISIBLE);
            content_divide_margin.setVisibility(View.GONE);
        }else{
            content_divide.setVisibility(View.GONE);
            content_divide_margin.setVisibility(View.VISIBLE);
        }
        return view;
    }
}
