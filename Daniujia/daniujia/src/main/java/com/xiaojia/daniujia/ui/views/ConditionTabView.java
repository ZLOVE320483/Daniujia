package com.xiaojia.daniujia.ui.views;

import android.content.Context;
import android.graphics.Color;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaojia.daniujia.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/12/15.
 */
public class ConditionTabView extends LinearLayout {
    public static final String SELECTING = "selecting";
    public static final String SELECTED = "selected";
    public static final String DEFAULT = "default";

    private String mType = DEFAULT;
    private ConditionItemClick listener;
    private ArrayList<TabData> data;
    private ArrayList<TextView> mChildTv;
    private ArrayList<ImageView> mChildIv;

    private LayoutInflater inflater;

    public ConditionTabView(Context context) {
        this(context, null);
    }

    public ConditionTabView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ConditionTabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(HORIZONTAL);
        inflater = LayoutInflater.from(context);
    }

    public void initTab(ArrayList<TabData> data) {
        if (data == null)
            throw new RuntimeException("tab的数据不能为空");
        mChildTv = new ArrayList<>();
        mChildIv = new ArrayList<>();

        this.data = data;

        for (int i = 0; i < data.size(); i++) {
            View view = inflater.inflate(R.layout.item_condition_tab, null);
            LayoutParams params = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
            addView(view, params);
            ImageView iv = (ImageView) view.findViewById(R.id.item_condition_iv);
            TextView tv = (TextView) view.findViewById(R.id.item_condition_tv);
            View cutLine = view.findViewById(R.id.item_condition_view);
            tv.setText(data.get(i).getText());
            mChildIv.add(iv);
            mChildTv.add(tv);

            final int position = i;

            if (i == data.size() - 1) {
                cutLine.setVisibility(GONE);
            }

            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.OnConditionClickListener(position);
                    }
                }
            });
        }

    }

    public void setTabText(int position, String text){
        data.get(position).setText(text);
    }

    public void updateTabStyle(ArrayList<TabData> data) {
        if (data == null)
            return;

        this.data = data;

        for (int i = 0; i < mChildTv.size(); i++) {
            String type = data.get(i).getType();
            if (type == null)
                break;
            TextView tv = mChildTv.get(i);
            TextPaint tp = tv.getPaint();
            ImageView iv = mChildIv.get(i);

            tv.setText(data.get(i).getText());
            if (type.equals(DEFAULT)) {
                tv.setTextColor(Color.parseColor("#333333"));
                iv.setImageResource(data.get(i).getResIdNormal());
                tp.setFakeBoldText(false);

            } else if (type.equals(SELECTED)) {
                tv.setTextColor(Color.parseColor("#2594e9"));
                tp.setFakeBoldText(false);
                iv.setImageResource(data.get(i).getResIdSelected());
            } else if (type.equals(SELECTING)) {
                tv.setTextColor(Color.parseColor("#2594e9"));
                tp.setFakeBoldText(true);
                iv.setImageResource(data.get(i).getRedIdSelecting());

            }

        }
    }

    public static class TabData {
        private String text;
        private int resIdNormal;
        private int resIdSelected;
        private int redIdSelecting;
        private String type = DEFAULT;

        public TabData(String text, int resIdNormal, int resIdSelected) {
            this.text = text;
            this.resIdNormal = resIdNormal;
            this.resIdSelected = resIdSelected;
        }

        public TabData(String text, int resIdNormal, int resIdSelected, int redIdSelecting) {
            this.resIdNormal = resIdNormal;
            this.resIdSelected = resIdSelected;
            this.redIdSelecting = redIdSelecting;
            this.text = text;
        }

        public int getRedIdSelecting() {
            return redIdSelecting;
        }

        public void setRedIdSelecting(int redIdSelecting) {
            this.redIdSelecting = redIdSelecting;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public int getResIdNormal() {
            return resIdNormal;
        }

        public void setResIdNormal(int resIdNormal) {
            this.resIdNormal = resIdNormal;
        }

        public int getResIdSelected() {
            return resIdSelected;
        }

        public void setResIdSelected(int resIdSelected) {
            this.resIdSelected = resIdSelected;
        }
    }

    public void setListener(ConditionItemClick listener) {
        this.listener = listener;
    }

    public interface ConditionItemClick {
        void OnConditionClickListener(int position);
    }

}
