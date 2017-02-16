package com.xiaojia.daniujia.ui.frag;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.okhttp.FormEncodingBuilder;
import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.IntentKey;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.dlgs.BaseDialog;
import com.xiaojia.daniujia.dlgs.BaseMsgDlg;
import com.xiaojia.daniujia.domain.resp.BeExpertGoodAt;
import com.xiaojia.daniujia.domain.resp.Speciality;
import com.xiaojia.daniujia.managers.OkHttpClientManager;
import com.xiaojia.daniujia.ui.act.ActCompleteDirection;
import com.xiaojia.daniujia.ui.act.ActSelectDirection;
import com.xiaojia.daniujia.ui.act.ActThirdStepToBeExpert;
import com.xiaojia.daniujia.ui.adapters.GoodAtAdapter;
import com.xiaojia.daniujia.utils.ListUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/28.
 */
public class StepSecondToBeExpertNew extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private ListView mList;
    private GoodAtAdapter mAdapter;
    private List<Speciality> mData;

    private View containerView;
    private BaseMsgDlg deleteDialog;
    private String fromWhere = "";

    @Override
    protected int getInflateLayout() {
        return R.layout.fg_step2_to_be_expert_new;
    }

    @Override
    protected void setUpView(View view) {
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(IntentKey.FROM_WHERE)) {
            fromWhere = intent.getStringExtra(IntentKey.FROM_WHERE);
        }
        setBackButton(view.findViewById(R.id.id_back));

        containerView = view.findViewById(R.id.container);
        ImageView step2 = (ImageView) view.findViewById(R.id.step2);
        step2.setImageResource(R.drawable.good_at_white);
        TextView title = (TextView) view.findViewById(R.id.id_title);
        title.setText("擅长方向");

        view.findViewById(R.id.title_right).setOnClickListener(this);
        mData = new ArrayList<>();
        mList = (ListView) view.findViewById(R.id.list);
        mList.setOnItemClickListener(this);
        View footView = getActivity().getLayoutInflater().inflate(R.layout.foot_view_add_direction,null);
        footView.findViewById(R.id.add_direction).setOnClickListener(this);
        mList.addFooterView(footView);
        mAdapter = new GoodAtAdapter(mData, getActivity());
        mList.setAdapter(mAdapter);
        mList.setOnItemLongClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        requestDate();
    }

    private void requestDate(){
        String url = Config.WEB_API_SERVER_V3 + "/become/specialities";
        OkHttpClientManager.getInstance(getActivity()).getWithToken(url, new OkHttpClientManager.ResultCallback<BeExpertGoodAt>() {
            @Override
            public void onResponse(BeExpertGoodAt result) {
                if (!ListUtils.isEmpty(result.getSpecialities())) {
                    mData.clear();
                    mData.addAll(result.getSpecialities());
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_right:
                if (ListUtils.isEmpty(mData)) {
                    showShortToast("请添加擅长方向");
                    return;
                }
               saveDate();
                break;
            case R.id.add_direction:
                if (mData.size() >= 3) {
                    showShortToast("擅长方向最多可添加3个");
                    return;
                }
                Intent intent = new Intent(getActivity(), ActSelectDirection.class);
                startActivity(intent);
                break;
        }
    }

    public void saveDate(){
        Intent intent = new Intent(getActivity(), ActThirdStepToBeExpert.class);
        intent.putExtra(IntentKey.FROM_WHERE, fromWhere);
        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Speciality item = mData.get(position);
        if (item == null) {
            return;
        }
        Intent intent = new Intent(getActivity(), ActCompleteDirection.class);
        intent.putExtra(IntentKey.INTENT_KEY_DIRECTION_ITEM, item);
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        if (deleteDialog == null) {
            deleteDialog = new BaseMsgDlg(getActivity());
        }
        deleteDialog.setBtnName(DialogInterface.BUTTON_POSITIVE, R.string.confirm);
        deleteDialog.setBtnName(DialogInterface.BUTTON_NEGATIVE, R.string.cancel);
        deleteDialog.setTitle("温馨提示");
        deleteDialog.setMsg("确定删除该擅长方向？");
        deleteDialog.setCanceledOnTouchOutside(false);
        deleteDialog.setOnClickListener(new BaseDialog.DialogClickListener() {
            public void onOK() {
                String url = Config.WEB_API_SERVER_V3 + "/become/speciality/del";
                FormEncodingBuilder builder = new FormEncodingBuilder();
                builder.add("specId", mData.get(position).getSpecId() + "");
                OkHttpClientManager.getInstance(getActivity()).postWithToken(url, new OkHttpClientManager.ResultCallback<String>() {
                    @Override
                    public void onResponse(String result) {
                        mData.remove(position);
                        mAdapter.notifyDataSetChanged();
                        containerView.postInvalidate();
                    }
                }, builder);
            }
        });
        deleteDialog.show();
        return true;
    }
}
