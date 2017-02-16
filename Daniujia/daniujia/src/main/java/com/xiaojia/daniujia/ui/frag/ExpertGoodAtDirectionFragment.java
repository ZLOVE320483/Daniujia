package com.xiaojia.daniujia.ui.frag;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.okhttp.FormEncodingBuilder;
import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.IntentKey;
import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.dlgs.BaseDialog;
import com.xiaojia.daniujia.dlgs.BaseMsgDlg;
import com.xiaojia.daniujia.domain.resp.BeExpertGoodAt;
import com.xiaojia.daniujia.domain.resp.Speciality;
import com.xiaojia.daniujia.managers.OkHttpClientManager;
import com.xiaojia.daniujia.ui.act.ActCompleteDirection;
import com.xiaojia.daniujia.ui.act.ActSelectDirection;
import com.xiaojia.daniujia.ui.adapters.GoodAtAdapter;
import com.xiaojia.daniujia.utils.ListUtils;
import com.xiaojia.daniujia.utils.SysUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZLOVE on 2016/6/29.
 */
public class ExpertGoodAtDirectionFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {

    private ListView listView;
    private List<Speciality> list = new ArrayList<>();
    private GoodAtAdapter adapter;

    private Button btnAddDirection;

    private View containerView;
    private BaseMsgDlg deleteDialog;

    @Override
    protected int getInflateLayout() {
        return R.layout.frag_expert_work_experience;
    }

    @Override
    protected void setUpView(View view) {
        setBackButton(view.findViewById(R.id.id_back));
        ((TextView) view.findViewById(R.id.id_title)).setText("擅长方向");

        containerView = view.findViewById(R.id.container);
        listView = (ListView) view.findViewById(R.id.id_list);
        adapter = new GoodAtAdapter(list, getActivity());
        View footView = LayoutInflater.from(getActivity()).inflate(R.layout.foot_view_add_direction, null);
        btnAddDirection = (Button) footView.findViewById(R.id.add_direction);
        listView.addFooterView(footView);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(this);
        listView.setOnItemClickListener(this);

        btnAddDirection.setOnClickListener(this);
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
                    list.clear();
                    list.addAll(result.getSpecialities());
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == btnAddDirection) {
            if (list.size() >= 3) {
                showShortToast("擅长方向最多可添加3个");
                return;
            }
            Intent intent = new Intent(getActivity(), ActSelectDirection.class);
            startActivity(intent);
        }
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
                if (list.size() == 1) {
                    if (SysUtil.getIntPref(PrefKeyConst.PREF_USER_IDENTITY) == 2) {
                        showShortToast("您是专家，擅长方向不能修改为空");
                    } else {
                        showShortToast("您正在申请成为专家，擅长方向不能修改为空");
                    }
                    return;
                }
                String url = Config.WEB_API_SERVER_V3 + "/become/speciality/del";
                FormEncodingBuilder builder = new FormEncodingBuilder();
                builder.add("specId", list.get(position).getSpecId() + "");
                OkHttpClientManager.getInstance(getActivity()).postWithToken(url, new OkHttpClientManager.ResultCallback<String>() {
                    @Override
                    public void onResponse(String result) {
                        list.remove(position);
                        adapter.notifyDataSetChanged();
                        containerView.postInvalidate();
                    }
                }, builder);
            }
        });
        deleteDialog.show();
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Speciality item = list.get(position);
        if (item == null) {
            return;
        }
        Intent intent = new Intent(getActivity(), ActCompleteDirection.class);
        intent.putExtra(IntentKey.INTENT_KEY_DIRECTION_ITEM, item);
        startActivity(intent);
    }
}
