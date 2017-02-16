package com.xiaojia.daniujia.ui.views;

/**
 * Created by ZLOVE on 2016/5/24.
 */
public interface HideChatOrderListener {
    void hideOrder();
    void userCancelOrder();
    void expertRejectOrder();
    void expertConfirmOrder();
    void expertReplyOrder();
    void userPayOrder();
    void expertFinishOrder();
    void userCommentOrder();
    void gotoChat();
    void expertGotoCall();
    void modify(String content, int modifyType);
    void gotoExpertMain();
}
