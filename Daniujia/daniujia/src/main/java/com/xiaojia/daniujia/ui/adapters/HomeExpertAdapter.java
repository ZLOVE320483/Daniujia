package com.xiaojia.daniujia.ui.adapters;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.domain.resp.ConsultEntity;
import com.xiaojia.daniujia.ui.views.PinnedSectionListView;
import com.xiaojia.daniujia.ui.views.imageview.RoundedImageView;
import com.xiaojia.daniujia.utils.ScreenUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/4/25
 */
public class HomeExpertAdapter extends SingleDataListAdapter<ConsultEntity> implements PinnedSectionListView.PinnedSectionListAdapter {
    private int mLlGoodAtWidth = 0;

    public HomeExpertAdapter(List<ConsultEntity> data, Context context) {
        super(data, context);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        final ConsultEntity item = getItem(position);
        int sectionType = item.getSectionType();

        switch (sectionType) {
            case 0:
                if (convertView == null) {
                    viewHolder = new ViewHolder();
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.recommend_expert_item, null);
                    viewHolder.llGoodAt = (LinearLayout) convertView.findViewById(R.id.expert_item_ll_good_at);
                    viewHolder.coll = (TextView) convertView.findViewById(R.id.collection);
                    viewHolder.experience = (TextView) convertView.findViewById(R.id.experience);
                    viewHolder.imageView = (RoundedImageView) convertView.findViewById(R.id.head);
                    viewHolder.name = (TextView) convertView.findViewById(R.id.name);
                    viewHolder.posit = (TextView) convertView.findViewById(R.id.position);
                    viewHolder.separator = convertView.findViewById(R.id.separate);
                    viewHolder.holl_separator = convertView.findViewById(R.id.separate_all);
                    convertView.setTag(R.id.id_views_holder, viewHolder);
                    convertView.setTag(R.id.id_type, 0);
                } else {
                    if ((Integer) convertView.getTag(R.id.id_type) == 0) {
                        viewHolder = (ViewHolder) convertView.getTag(R.id.id_views_holder);
                    } else {
                        viewHolder = new ViewHolder();
                        convertView = LayoutInflater.from(mContext).inflate(R.layout.recommend_expert_item, null);
                        viewHolder.llGoodAt = (LinearLayout) convertView.findViewById(R.id.expert_item_ll_good_at);
                        viewHolder.coll = (TextView) convertView.findViewById(R.id.collection);
                        viewHolder.experience = (TextView) convertView.findViewById(R.id.experience);
                        viewHolder.imageView = (RoundedImageView) convertView.findViewById(R.id.head);
                        viewHolder.name = (TextView) convertView.findViewById(R.id.name);
                        viewHolder.posit = (TextView) convertView.findViewById(R.id.position);
                        viewHolder.separator = convertView.findViewById(R.id.separate);
                        viewHolder.holl_separator = convertView.findViewById(R.id.separate_all);
                        convertView.setTag(R.id.id_views_holder, viewHolder);
                        convertView.setTag(R.id.id_type, 0);
                    }
                }

                if (position == mData.size() - 1) {
                    viewHolder.holl_separator.setVisibility(View.VISIBLE);
                    viewHolder.separator.setVisibility(View.GONE);
                } else {
                    viewHolder.holl_separator.setVisibility(View.GONE);
                    viewHolder.separator.setVisibility(View.VISIBLE);
                }

                final LinearLayout goodAtView = viewHolder.llGoodAt;
                final String[] content = mData.get(position).getConcretes();
                if (content == null || content.length == 0) {
                    goodAtView.setVisibility(View.GONE);
                } else {
                    goodAtView.setVisibility(View.VISIBLE);
                    if (mLlGoodAtWidth == 0) {
                        goodAtView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                mLlGoodAtWidth = goodAtView.getWidth();
                                calculateGoodAtItem(goodAtView, mLlGoodAtWidth, content);
                                if (android.os.Build.VERSION.SDK_INT > 16)
                                    goodAtView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                                else
                                    goodAtView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                            }
                        });
                    } else {
                        calculateGoodAtItem(goodAtView, mLlGoodAtWidth, content);
                    }
                }

                ConsultEntity recommendExpert = mData.get(position);
                if (!TextUtils.isEmpty(recommendExpert.getImgurl())) {
                    Picasso.with(mContext).load(recommendExpert.getImgurl()).error(R.drawable.ic_avatar_default).placeholder(R.drawable.ic_avatar_default)
                            .resize(ScreenUtils.dip2px(48), ScreenUtils.dip2px(48)).into(viewHolder.imageView);
                } else {
                    Picasso.with(mContext).load(R.drawable.ic_avatar_default).resize(ScreenUtils.dip2px(48), ScreenUtils.dip2px(48)).into(viewHolder.imageView);
                }
                viewHolder.name.setText(recommendExpert.getName());
                viewHolder.posit.setText(recommendExpert.getPosition() + " | " + recommendExpert.getCompany());
                viewHolder.coll.setText(recommendExpert.getVisitCount() + "浏览");
                viewHolder.experience.setText("工作年限 : " + recommendExpert.getWorkage() + "年");
                break;
            case 1:
                if (convertView == null) {
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_section_home_expert, null);
                    viewHolder = new ViewHolder();
                    convertView.setTag(R.id.id_views_holder, viewHolder);
                    convertView.setTag(R.id.id_type, 1);
                } else {
                    if ((Integer) convertView.getTag(R.id.id_type) == 1) {
                        convertView.getTag(R.id.id_views_holder);
                    } else {
                        convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_section_home_expert, null);
                        viewHolder = new ViewHolder();
                        convertView.setTag(R.id.id_views_holder, viewHolder);
                        convertView.setTag(R.id.id_type, 1);
                    }
                }
                break;
        }

        return convertView;
    }

    class ViewHolder {
        RoundedImageView imageView;
        TextView name;
        TextView posit;
        TextView coll;
        TextView experience;
        View separator;
        View holl_separator;
        LinearLayout llGoodAt;
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
            return 0;
        }
    }

    @Override
    public boolean isItemViewTypePinned(int viewType) {
        return viewType == 1;
    }


    private void calculateGoodAtItem(LinearLayout goodAtView, int llGoodAtWidth, String[] content) {
        int textWidthTotal = 0;
        if (goodAtView != null && goodAtView.getChildCount() > 0) {
            goodAtView.removeAllViews();
        }
        for (int i = 0; i < content.length; i++) {
            LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
            TextView tvItem = (TextView) LayoutInflater.from(mContext).inflate(R.layout.flow_item_good_at_direction, null);
            if (i == content.length - 1){
                tvItem.setText(content[i]);
            } else {
                tvItem.setText(content[i] + "/");
            }
            int tvWidth = (int) tvItem.getPaint().measureText(tvItem.getText().toString());
            textWidthTotal += (tvWidth);
//            if (i != 0) {
//                ll.setMargins(12, 0, 0, 0);
//                textWidthTotal += 12;
//            }
            if (textWidthTotal > llGoodAtWidth) {
                tvItem.setEllipsize(TextUtils.TruncateAt.END);
                tvItem.setGravity(Gravity.LEFT);
                LinearLayout.LayoutParams llNew = new LinearLayout.LayoutParams(0, ActionBar.LayoutParams.WRAP_CONTENT, 1);
//                if (i != 0) {
//                    llNew.setMargins(12, 0, 0, 0);
//                }
                tvItem.setLayoutParams(llNew);
                goodAtView.addView(tvItem);
                break;
            }

            tvItem.setLayoutParams(ll);
            goodAtView.addView(tvItem);
        }
    }
}
