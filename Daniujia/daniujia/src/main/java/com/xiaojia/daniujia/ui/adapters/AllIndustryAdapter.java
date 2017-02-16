package com.xiaojia.daniujia.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xiaojia.daniujia.ExtraConst;
import com.xiaojia.daniujia.IntentKey;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.domain.resp.AllIndustryVo;
import com.xiaojia.daniujia.ui.act.ActIndustry;
import com.xiaojia.daniujia.ui.views.MyGridView;
import com.xiaojia.daniujia.utils.ListUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/4/26
 */

public class AllIndustryAdapter extends SingleDataListAdapter<AllIndustryVo.ClassificationEntity> {

    public AllIndustryAdapter(List<AllIndustryVo.ClassificationEntity> data, Context context) {
        super(data, context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.industy_item, null);
            viewHolder = new ViewHolder();
            viewHolder.gridView = (MyGridView) convertView.findViewById(R.id.gridView);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.text);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.gridView.setAdapter(new MyAdapter(mData.get(position), mContext));
        viewHolder.textView.setText(mData.get(position).getMainDirectionName());
        String icon = mData.get(position).getMainDirectionIcon();
        if (!TextUtils.isEmpty(icon)) {
            Picasso.with(mContext).load(icon).placeholder(R.drawable.internet)
                    .into(viewHolder.imageView);
        } else {
            viewHolder.imageView.setImageResource(R.drawable.internet);
        }
        return convertView;
    }

    class ViewHolder {
        public MyGridView gridView;
        public TextView textView;
        public ImageView imageView;
    }

    class MyAdapter extends BaseAdapter implements View.OnClickListener {
        AllIndustryVo.ClassificationEntity data;
        Context context;

        public MyAdapter(AllIndustryVo.ClassificationEntity data, Context context) {
            this.data = data;
            this.context = context;
        }

        @Override
        public int getCount() {
            if (ListUtils.isEmpty(data.getSubDirections())) {
                return 0;
            }
            return data.getSubDirections().size();
        }

        @Override
        public Object getItem(int position) {
            return data.getSubDirections().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Button btn = (Button) LayoutInflater.from(mContext).inflate(R.layout.btn, null);
            btn.setText(data.getSubDirections().get(position).getSubDirectionName());
            btn.setTag(data.getSubDirections().get(position).getSubDirectionCode());
            btn.setTag(R.id.id_position, position);
            btn.setOnClickListener(this);
            return btn;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, ActIndustry.class);
            intent.putExtra(IntentKey.INTENT_KEY_MAIN_DIRECTION, data.getMainDirectionId());
            intent.putExtra(IntentKey.INTENT_KEY_SUB_DIRECTION, (String)v.getTag());
            intent.putExtra(ExtraConst.EXTRA_WEB_TITLE, data.getMainDirectionName());
            intent.putExtra(ExtraConst.EXTRA_POSITION, v.getTag(R.id.id_position)+"");
            context.startActivity(intent);
        }
    }
}
