package com.xiaojia.daniujia.ui.frag;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xiaojia.daniujia.IntentKey;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.domain.resp.UserCommentVo;
import com.xiaojia.daniujia.ui.adapters.UserCommentAdapter;
import com.xiaojia.daniujia.ui.control.UserCommentControl;
import com.xiaojia.daniujia.ui.views.XListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZLOVE on 2016/4/27.
 */
public class UserCommentFragment extends BaseFragment implements XListView.IXListViewListener {

    private UserCommentControl commentControl;

    private XListView listView;
    private UserCommentAdapter adapter;
    private List<UserCommentVo.CommentInfo> list = new ArrayList<>();
    private int expertId = 0;
    private int pageNum = 1;
    private boolean isRefresh = true;
    private boolean isFirstRefresh = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        commentControl = new UserCommentControl();
        setControl(commentControl);
    }

    @Override
    protected int getInflateLayout() {
        return R.layout.frag_user_comment;
    }

    @Override
    protected void setUpView(View view) {
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            if (intent.hasExtra(IntentKey.INTENT_KEY_EXPERT_ID)) {
                expertId = intent.getIntExtra(IntentKey.INTENT_KEY_EXPERT_ID, 0);
            }
        }
        setBackButton(view.findViewById(R.id.id_back));
        ((TextView) view.findViewById(R.id.id_title)).setText("用户评价");
        listView = (XListView) view.findViewById(R.id.id_list);
        adapter = new UserCommentAdapter(list, getActivity());
        listView.setAdapter(adapter);
        listView.setXListViewListener(this);
        listView.setPullRefreshEnable(true);

        commentControl.getCommentList(isFirstRefresh, expertId, pageNum);
    }

    public void setCommentData(UserCommentVo data) {
        isFirstRefresh = false;
        listView.stopRefresh();
        listView.stopLoadMore();
        if (data == null) {
            return;
        }
        List<UserCommentVo.CommentInfo> tmpList = data.getComments();

        if (isRefresh) {
            this.list.clear();
        }
        this.list.addAll(data.getComments());
        adapter.notifyDataSetChanged();

        if (tmpList.size() < 10) {
            listView.setPullLoadEnable(false);
        } else {
            listView.setPullLoadEnable(true);
        }
    }

    @Override
    public void onRefresh() {
        pageNum = 1;
        isRefresh = true;
        commentControl.getCommentList(isFirstRefresh, expertId, pageNum);
    }

    @Override
    public void onLoadMore() {
        pageNum++;
        isRefresh = false;
        isFirstRefresh = false;
        commentControl.getCommentList(isFirstRefresh, expertId, pageNum);
    }
}
