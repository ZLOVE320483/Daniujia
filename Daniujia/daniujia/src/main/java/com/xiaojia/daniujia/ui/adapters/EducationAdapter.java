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
 * Created by Administrator on 2016/5/24.
 */
public class EducationAdapter extends SingleDataListAdapter<EducationVo.EducationsEntity>{

    public EducationAdapter(List<EducationVo.EducationsEntity> data, Context context) {
        super(data, context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.edu_backgrounf, null);
        TextView school_from_to = (TextView) view.findViewById(R.id.school_from_to);
        TextView school_qualification = (TextView) view.findViewById(R.id.school_qualification);
        long endTime = mData.get(position).getEndTime();
        if (endTime == 0) {
            endTime = System.currentTimeMillis() / 1000;
        }
        school_from_to.setText(TimeUtils.calculateEduTime(mData.get(position).getBeginTime(),endTime));
        school_qualification.setText(mData.get(position).getName() + "  |  " + WUtil.transformDegree(mData.get(position).getDegree()));
        View divide = view.findViewById(R.id.content_divide);
        View divide_margin = view.findViewById(R.id.content_divide_margin);
        if (position == mData.size() - 1) {
            divide.setVisibility(View.VISIBLE);
            divide_margin.setVisibility(View.GONE);
        }else{
            divide.setVisibility(View.GONE);
            divide_margin.setVisibility(View.VISIBLE);
        }
        return view;
    }


}
