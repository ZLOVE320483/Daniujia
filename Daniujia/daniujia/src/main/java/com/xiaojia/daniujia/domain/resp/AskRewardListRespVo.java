package com.xiaojia.daniujia.domain.resp;

import java.util.List;

/**
 * Created by ZLOVE on 2016/10/24.
 */
public class AskRewardListRespVo {

    private ExpiredQuestions expiredQuestions;
    private PublishedQuestions publishedQuestions;
    private String returncode;
    private String returnmsg;

    public ExpiredQuestions getExpiredQuestions() {
        return expiredQuestions;
    }

    public void setExpiredQuestions(ExpiredQuestions expiredQuestions) {
        this.expiredQuestions = expiredQuestions;
    }

    public PublishedQuestions getPublishedQuestions() {
        return publishedQuestions;
    }

    public void setPublishedQuestions(PublishedQuestions publishedQuestions) {
        this.publishedQuestions = publishedQuestions;
    }

    public String getReturncode() {
        return returncode;
    }

    public void setReturncode(String returncode) {
        this.returncode = returncode;
    }

    public String getReturnmsg() {
        return returnmsg;
    }

    public void setReturnmsg(String returnmsg) {
        this.returnmsg = returnmsg;
    }

    public static class ExpiredQuestions {

        private List<AskRewardListItem> items;
        private int nextCursor;
        private int itemCount;
        private int totalCount;

        public List<AskRewardListItem> getItems() {
            return items;
        }

        public void setItems(List<AskRewardListItem> items) {
            this.items = items;
        }

        public int getNextCursor() {
            return nextCursor;
        }

        public void setNextCursor(int nextCursor) {
            this.nextCursor = nextCursor;
        }

        public int getItemCount() {
            return itemCount;
        }

        public void setItemCount(int itemCount) {
            this.itemCount = itemCount;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }
    }

    public static class PublishedQuestions {
        private List<AskRewardListItem> items;
        private int nextCursor;
        private int itemCount;
        private int totalCount;

        public List<AskRewardListItem> getItems() {
            return items;
        }

        public void setItems(List<AskRewardListItem> items) {
            this.items = items;
        }

        public int getNextCursor() {
            return nextCursor;
        }

        public void setNextCursor(int nextCursor) {
            this.nextCursor = nextCursor;
        }

        public int getItemCount() {
            return itemCount;
        }

        public void setItemCount(int itemCount) {
            this.itemCount = itemCount;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }
    }

    public static class AskRewardListItem {
        private int id;
        private int userId;
        private String writeName;
        private String title;
        private String imgUrl;
        private long deadline;
        private int price;
        private String content;
        private String moreContent;
        private int replyCount;
        private int followCount;
        private int answerCount;
        private int answerViewFee;
        private int viewCount;
        private String status;
        private boolean userFollowed;
        private boolean userPublished;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getWriteName() {
            return writeName;
        }

        public void setWriteName(String writeName) {
            this.writeName = writeName;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public long getDeadline() {
            return deadline;
        }

        public void setDeadline(long deadline) {
            this.deadline = deadline;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getMoreContent() {
            return moreContent;
        }

        public void setMoreContent(String moreContent) {
            this.moreContent = moreContent;
        }

        public int getReplyCount() {
            return replyCount;
        }

        public void setReplyCount(int replyCount) {
            this.replyCount = replyCount;
        }

        public int getFollowCount() {
            return followCount;
        }

        public void setFollowCount(int followCount) {
            this.followCount = followCount;
        }

        public int getAnswerCount() {
            return answerCount;
        }

        public void setAnswerCount(int answerCount) {
            this.answerCount = answerCount;
        }

        public int getAnswerViewFee() {
            return answerViewFee;
        }

        public void setAnswerViewFee(int answerViewFee) {
            this.answerViewFee = answerViewFee;
        }

        public int getViewCount() {
            return viewCount;
        }

        public void setViewCount(int viewCount) {
            this.viewCount = viewCount;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public boolean isUserFollowed() {
            return userFollowed;
        }

        public void setUserFollowed(boolean userFollowed) {
            this.userFollowed = userFollowed;
        }

        public boolean isUserPublished() {
            return userPublished;
        }

        public void setUserPublished(boolean userPublished) {
            this.userPublished = userPublished;
        }
    }

}
