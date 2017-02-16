package com.xiaojia.daniujia.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.domain.resp.SelectCityBean;
import com.xiaojia.daniujia.ui.views.PinnedSectionListView;

import java.util.List;

/**
 * Created by ZLOVE on 2016/6/29.
 */
public class SelectCityAdapter extends SingleDataListAdapter<SelectCityBean> implements PinnedSectionListView.PinnedSectionListAdapter {

    private String curCityCode = "-2";

    public SelectCityAdapter(List<SelectCityBean> data, Context context) {
        super(data, context);
    }

    public void setCurCityCode(String curCityCode) {
        this.curCityCode = curCityCode;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SelectCityBean item = getItem(position);
        if (isItemViewTypePinned(item.getSectionType())) {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_section, null);
            }
            ((TextView) convertView.findViewById(R.id.id_name)).setText(item.getSectionText());
        } else {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_select_city, null);
            }
            ((TextView) convertView.findViewById(R.id.id_name)).setText(item.getSectionText());
            ImageView ivSelect = (ImageView) convertView.findViewById(R.id.id_select);
            if (item.getCityCode().equals(curCityCode)) {
                ivSelect.setVisibility(View.VISIBLE);
            } else {
                ivSelect.setVisibility(View.GONE);
            }
        }
        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        try {
            return getItem(position).getSectionType();
        } catch (Exception e) {
            e.printStackTrace();
            return SelectCityBean.ITEM;
        }
    }

    @Override
    public boolean isItemViewTypePinned(int viewType) {
        return viewType == SelectCityBean.SECTION;
    }
}
