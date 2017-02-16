package com.xiaojia.daniujia.ui.control;

import com.squareup.okhttp.Request;
import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.domain.resp.UserCommentVo;
import com.xiaojia.daniujia.managers.OkHttpClientManager;
import com.xiaojia.daniujia.ui.frag.UserCommentFragment;

/**
 * Created by ZLOVE on 2016/5/5.
 */
public class UserCommentControl extends BaseControl<UserCommentFragment> {

    public void getCommentList(boolean isFirst, int expertId, int pageNum) {
        String url = Config.WEB_API_SERVER + "/consultant/service/comments/" + expertId + "/" + pageNum;
        OkHttpClientManager.getInstance(getAttachedActivity()).getWithPlatform(isFirst, url, new OkHttpClientManager.ResultCallback<UserCommentVo>() {
            @Override
            public void onResponse(UserCommentVo result) {
                if (result.getReturncode().equals("SUCCESS")) {
                    getFragment().setCommentData(result);
                } else {
                    showShortToast(result.getReturnmsg());
                }
            }

            @Override
            public void onError(Request paramRequest, Exception e) {
                super.onError(paramRequest, e);
            }

            @Override
            public void onFail(String errorMsg) {
                super.onFail(errorMsg);
            }
        });
    }
}
