package com.xiaojia.daniujia.ui.adapters;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.domain.resp.NovationByTopicVo;
import com.xiaojia.daniujia.utils.ScreenUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/4/28
 */
public class HorizontalListAdapterIndustry extends SingleDataListAdapter<NovationByTopicVo.FilterDirectionsEntity> {

    private int selectPosition;

    public HorizontalListAdapterIndustry(List<NovationByTopicVo.FilterDirectionsEntity> data, Context context) {
        super(data, context);
    }

    // FIXME: 2016/6/15 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView tv;
        if (convertView == null) {
            tv = new TextView(mContext);
        } else {
            tv = (TextView) convertView;
        }
        int firstPaddingLeft = ScreenUtils.dip2px(16);
        int paddingTop = ScreenUtils.dip2px(6);
        int paddingTopLess = ScreenUtils.dip2px(3);
        int otherPaddingLeft = ScreenUtils.dip2px(30);

        if (position == selectPosition) {
            tv.setTextColor(mContext.getResources().getColor(R.color.blue_text));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            if (position == 0) {
                tv.setPadding(firstPaddingLeft, paddingTopLess, 0, 0);
            }else{
                tv.setPadding(otherPaddingLeft,paddingTopLess,0,0);
            }
        }else{
            tv.setTextColor(mContext.getResources().getColor(R.color.second_text_color));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            if (position == 0) {
                tv.setPadding(firstPaddingLeft,paddingTop,0,0);
            }else{
                tv.setPadding(otherPaddingLeft,paddingTop,0,0);
            }
        }
        tv.setText(mData.get(position).getName());
        return tv;
    }

    public void setSelectPosition(int position){
        this.selectPosition = position;
        notifyDataSetChanged();
    }

    public int getSelectPosition(){
        return  selectPosition;
    }
}
