package com.xiaojia.daniujia.ui.frag;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.ui.adapters.SingleDataListAdapter;
import com.xiaojia.daniujia.ui.views.MyListView2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/6.
 */
public class PublishInstructionFragment extends BaseFragment implements View.OnClickListener {
    private MyListView2 myListView2;
    private List<ListData> listData;

    @Override
    protected int getInflateLayout() {
        return R.layout.frag_publish_insctruction;
    }

    @Override
    protected void setUpView(View view) {
        myListView2 = (MyListView2) view.findViewById(R.id.instruction_list);
        view.findViewById(R.id.id_back).setOnClickListener(this);
        view.findViewById(R.id.confirm).setOnClickListener(this);
        listData = new ArrayList<>();

        listData.add(new ListData("发布者按照需求表单填写好寻找顾问需求信息后，大牛家将有专人与您联系。"));
        listData.add(new ListData("大牛家与发布者确认好需求后，将正式发布此需求并告知发布者。"));
        listData.add(new ListData("需求正式发布后，大牛家上的认证专家可以报名参与此需求。"));
        listData.add(new ListData("发布者可通过大牛家平台即时聊天和回拨电话功能和报名专家进行沟通。"));
        listData.add(new ListData("在发布者未确定顾问人选前，报名专家可取消报名。"));
        listData.add(new ListData("发布者通过大牛家与专家沟通后，确定合作专家及给予沟通评价，并在线下完成合作签约。"));
        listData.add(new ListData("任何其他疑问，可联系大牛家客服电话：400-0903-022。"));

        myListView2.setAdapter(new MyListAdapter(listData,getContext()));

    }

    @Override
    public void onClick(View v) {
        finishActivity();
    }

    class MyListAdapter extends SingleDataListAdapter<ListData> {


        public MyListAdapter(List<ListData> data, Context context) {
            super(data, context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null){
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_instruction,null);
                viewHolder.tv = (TextView) convertView.findViewById(R.id.item_instruction_tv);
                convertView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tv.setText(mData.get(position).getText());

            return convertView;
        }


        class ViewHolder{
            TextView tv;

        }
    }

    class ListData{
        String text;
        public ListData(String text) {
            this.text = text;
        }
        public String getText() {
            return text;
        }
        public void setText(String text) {
            this.text = text;
        }
    }
}
