package com.xiaojia.daniujia.ui.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.domain.resp.ConcreteDirection;
import com.xiaojia.daniujia.domain.resp.RelatedIndustry;
import com.xiaojia.daniujia.domain.resp.Speciality;
import com.xiaojia.daniujia.ui.views.FlowLayout;
import com.xiaojia.daniujia.utils.ListUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/6/29.
 */
public class GoodAtAdapter extends SingleDataListAdapter<Speciality> {

    private boolean isFromExpertHome = false;

    public GoodAtAdapter(List<Speciality> data, Context context) {
        super(data, context);
    }

    public void setIsFromExpertHome(boolean isFromExpertHome) {
        this.isFromExpertHome = isFromExpertHome;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (isFromExpertHome) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.good_at_item_from_expert_home, null);
        } else {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.good_at_item, null);
        }
        TextView mainName = (TextView) convertView.findViewById(R.id.mainName);
        TextView subName = (TextView) convertView.findViewById(R.id.subName);
        TextView workAge = (TextView) convertView.findViewById(R.id.workAge);
        TextView relative_industry = (TextView) convertView.findViewById(R.id.relative_industry);

        ImageView icon = (ImageView) convertView.findViewById(R.id.icon);

        Speciality speciality = mData.get(position);
        if (!TextUtils.isEmpty(speciality.getMainIconUrl())) {
            Picasso.with(mContext).load(speciality.getMainIconUrl()).into(icon);
        }

        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = 5;
        lp.rightMargin = 5;
        lp.topMargin = 1;
        lp.bottomMargin = 1;
        if (isFromExpertHome) {
            FlowLayout container = (FlowLayout) convertView.findViewById(R.id.industry_container);
            List<ConcreteDirection> concreteDirections = speciality.getConcreteDirections();
            if (!ListUtils.isEmpty(concreteDirections)) {
                for (final ConcreteDirection concreteDirection : speciality.getConcreteDirections()) {
                    final View contentView = LayoutInflater.from(mContext).inflate(R.layout.flow_item_concrete_direction, null);
                    TextView tvLabel = (TextView) contentView.findViewById(R.id.detail_relative);
                    tvLabel.setText(concreteDirection.getDirectionName());
                    container.addView(contentView, lp);
                }
            } else {
                final View contentView = LayoutInflater.from(mContext).inflate(R.layout.flow_item_concrete_direction, null);
                TextView tvLabel = (TextView) contentView.findViewById(R.id.detail_relative);
                if (TextUtils.isEmpty(speciality.getSubName()) || speciality.getSubName().equals("其他")) {
                    tvLabel.setText(speciality.getWriteName());
                } else if (!TextUtils.isEmpty(speciality.getSubName())) {
                    tvLabel.setText(speciality.getSubName());
                }
                container.addView(contentView, lp);
            }

            TextView tvWorkAge = (TextView) convertView.findViewById(R.id.workAge);
            tvWorkAge.setText(+speciality.getWorkage() + "年工作经验");
        } else {
            TextView detail_relative = (TextView) convertView.findViewById(R.id.detail_relative);
            detail_relative.setText(translateArrToString2(speciality.getConcreteDirections()));
            workAge.setText(speciality.getWorkage() + "年");
        }

        if (!TextUtils.isEmpty(speciality.getMainName())) {
            mainName.setText(speciality.getMainName());
        }

        if ("其他".equals(speciality.getSubName()) || TextUtils.isEmpty(speciality.getSubName())) {
            String content = TextUtils.isEmpty(speciality.getWriteName()) ? "其他" : speciality.getWriteName();
            subName.setText(content);
        } else {
            subName.setText(speciality.getSubName());
        }

        relative_industry.setText(translateArrToString(speciality.getRelatedIndustrys()));
        convertView.setTag(mData.get(position).getSpecId());
        return convertView;
    }

    private String translateArrToString2(List<ConcreteDirection> concreteDirections) {
        if (ListUtils.isEmpty(concreteDirections)) {
            return "";
        }
        String text = "";
        for (ConcreteDirection concrete : concreteDirections) {
            text += concrete.getDirectionName();
            text += "   ";
        }
        return text;
    }

    private String translateArrToString(List<RelatedIndustry> relatedIndustrys) {

        if (ListUtils.isEmpty(relatedIndustrys)) {
            return "";
        }
        String text = "";
        for (RelatedIndustry industry : relatedIndustrys) {
            text += industry.getIndustryName();
            text += "   ";
        }
        return text;
    }

}
