package com.xiaojia.daniujia.domain.resp;

import android.content.ContentValues;
import android.database.Cursor;

import com.xiaojia.daniujia.db.DatabaseConstants;
import com.xiaojia.daniujia.db.bean.BaseBeanDB;

import java.util.List;

/**
 * Created by Administrator on 2016/4/17.
 */
public class PullConversationsVo {

    /**
     * eventId : 0
     * ok : true
     * data : [{"conversationId":"5710b858239cf841bfcbaa83","type":"singleChat","createdAt":"2016-04-17T01:22:14.761Z","isToWaiter":false,"unreadMessageCount":101,"target":{"username":"weaver","identity":2,"avatarUrl":"https://dn-daniujia.qbox.me/avatar-6-1460095029355.jpg"},"currentService":{"to":"weaver","from":"lisi","type":"text","id":"5710b858499f26bc6e3f66e2","createdAt":"2016-04-15T09:46:00.206Z","status":"pending"},"lastMessage":{"content":{"topic":"private-weaver","msg":"jsjsddj","ctype":1},"to":"weaver","code":"1460720178202","from":"lisi","mtype":1,"dnjts":1460720180263,"type":"singleChat","messageId":185}},{"conversationId":"5710b418499f26bc6e3f659c","type":"singleChat","createdAt":"2016-04-15T09:27:47.933Z","isToWaiter":true,"unreadMessageCount":0,"target":{"username":"eric"},"lastMessage":{"content":{"msg":"{\"question_id\":980,\"question_content\":\"gggbbbbbbbgggbbbbbbb\"}","ctype":1},"to":"eric","chatid":46,"code":"1460716320135","from":"lisi","mtype":1,"dnjts":1460718130244,"type":"singleChat","messageId":12}},{"conversationId":"5710a78d239cf841bfcbaa6e","type":"singleChat","createdAt":"2016-04-15T08:31:19.760Z","isToWaiter":false,"unreadMessageCount":0,"target":{"username":"15221208994"},"currentService":{"from":"15221208994","to":"lisi","type":"text","id":"5710b3fff6fad7706e69bd62","createdAt":"2016-04-15T08:34:21.869Z","status":"pending"}},{"conversationId":"5710a78d239cf841bfcbaa34","type":"singleChat","createdAt":"2016-04-15T08:31:19.760Z","isToWaiter":false,"unreadMessageCount":0,"target":{"username":"wujin"},"currentService":{"from":"wujin","to":"lisi","type":"text","id":"5710b3fff6fad7706e69bd64","createdAt":"2016-04-15T08:34:21.869Z","status":"pending"}},{"conversationId":"5710a78d239cf841bfc22a34","type":"singleChat","createdAt":"2016-04-15T08:31:19.760Z","isToWaiter":false,"unreadMessageCount":0,"target":{"username":"wangqi"},"currentService":{"from":"wangqi","to":"lisi","type":"text","id":"5710b3fff6fad7706e69bd66","createdAt":"2016-04-15T08:34:21.869Z","status":"pending"}}]
     */

    private int eventId;
    private boolean ok;
    /**
     * conversationId : 5710b858239cf841bfcbaa83
     * type : singleChat
     * createdAt : 2016-04-17T01:22:14.761Z
     * isToWaiter : false
     * unreadMessageCount : 101
     * target : {"username":"weaver","identity":2,"avatarUrl":"https://dn-daniujia.qbox.me/avatar-6-1460095029355.jpg"}
     * currentService : {"to":"weaver","from":"lisi","type":"text","id":"5710b858499f26bc6e3f66e2","createdAt":"2016-04-15T09:46:00.206Z","status":"pending"}
     * lastMessage : {"content":{"topic":"private-weaver","msg":"jsjsddj","ctype":1},"to":"weaver","code":"1460720178202","from":"lisi","mtype":1,"dnjts":1460720180263,"type":"singleChat","messageId":185}
     */

    private List<DataEntity> data;

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public int getEventId() {
        return eventId;
    }

    public boolean isOk() {
        return ok;
    }

    public List<DataEntity> getData() {
        return data;
    }

    public static class DataEntity extends BaseBeanDB {
        private String conversationId;
        private String type;
        private String createdAt;
        private boolean isToWaiter;
        private int unreadMessageCount = 0;
        /**
         * username : weaver
         * identity : 2
         * avatarUrl : https://dn-daniujia.qbox.me/avatar-6-1460095029355.jpg
         */

        private TargetEntity target;
        /**
         * to : weaver
         * from : lisi
         * type : text
         * id : 5710b858499f26bc6e3f66e2
         * createdAt : 2016-04-15T09:46:00.206Z
         * status : pending
         */

        private CurrentServiceEntity currentService;
        /**
         * content : {"topic":"private-weaver","msg":"jsjsddj","ctype":1}
         * to : weaver
         * code : 1460720178202
         * from : lisi
         * mtype : 1
         * dnjts : 1460720180263
         * type : singleChat
         * messageId : 185
         */

        private MessageEntity lastMessage;

        public void setConversationId(String conversationId) {
            this.conversationId = conversationId;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public void setIsToWaiter(boolean isToWaiter) {
            this.isToWaiter = isToWaiter;
        }

        public void setUnreadMessageCount(int unreadMessageCount) {
            this.unreadMessageCount = unreadMessageCount;
        }

        public void setTarget(TargetEntity target) {
            this.target = target;
        }

        public void setCurrentService(CurrentServiceEntity currentService) {
            this.currentService = currentService;
        }

        public void setLastMessage(MessageEntity lastMessage) {
            this.lastMessage = lastMessage;
        }

        public String getConversationId() {
            return conversationId;
        }

        public String getType() {
            return type;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public boolean isIsToWaiter() {
            return isToWaiter;
        }

        public int getUnreadMessageCount() {
            return unreadMessageCount;
        }

        public TargetEntity getTarget() {
            return target;
        }

        public CurrentServiceEntity getCurrentService() {
            return currentService;
        }

        public MessageEntity getLastMessage() {
            return lastMessage;
        }

        public static class TargetEntity {
            private String username;
            private int identity;
            private String avatarUrl;
            private String name;
            private int userId;

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public void setIdentity(int identity) {
                this.identity = identity;
            }

            public void setAvatarUrl(String avatarUrl) {
                this.avatarUrl = avatarUrl;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getUsername() {
                return username;
            }

            public int getIdentity() {
                return identity;
            }

            public String getAvatarUrl() {
                return avatarUrl;
            }

            public String getName() {
                return name;
            }
        }

        public static class CurrentServiceEntity {
            private String to;
            private String from;
            private String type;
            private String id;
            private String createdAt;
            private String currentStatus;

            public void setTo(String to) {
                this.to = to;
            }

            public void setFrom(String from) {
                this.from = from;
            }

            public void setType(String type) {
                this.type = type;
            }

            public void setId(String id) {
                this.id = id;
            }

            public void setCreatedAt(String createdAt) {
                this.createdAt = createdAt;
            }

            public void setCurrentStatus(String currentStatus) {
                this.currentStatus = currentStatus;
            }

            public String getTo() {
                return to;
            }

            public String getFrom() {
                return from;
            }

            public String getType() {
                return type;
            }

            public String getId() {
                return id;
            }

            public String getCreatedAt() {
                return createdAt;
            }

            public String getCurrentStatus() {
                return currentStatus;
            }
        }

        @Override
        public BaseBeanDB readFromCursor(Cursor cursor) {
            DataEntity dataEntity = new DataEntity();
            TargetEntity targetEntity = new TargetEntity();
            MessageEntity lastMessageEntity = new MessageEntity();
            MessageEntity.ContentEntity contentEntity = new MessageEntity.ContentEntity();
            CurrentServiceEntity currentServiceEntity = new CurrentServiceEntity();

            int index;
            index = cursor.getColumnIndex(DatabaseConstants.ConversationColumn.COLUMN_NAME_TARGET_NAME);
            if (index != -1) {
                targetEntity.setUsername(cursor.getString(index));
            }
            index = cursor.getColumnIndex(DatabaseConstants.ConversationColumn.COLUMN_NAME_NAME);
            if (index != -1) {
                targetEntity.setName(cursor.getString(index));
            }
            index = cursor.getColumnIndex(DatabaseConstants.ConversationColumn.COLUMN_NAME_TARGET_IDENTITY);
            if (index != -1) {
                targetEntity.setIdentity(cursor.getInt(index));
            }
            index = cursor.getColumnIndex(DatabaseConstants.ConversationColumn.COLUMN_NAME_TARGET_USER_ID);
            if (index != -1) {
                targetEntity.setUserId(cursor.getInt(index));
            }
            index = cursor.getColumnIndex(DatabaseConstants.ConversationColumn.COLUMN_NAME_TARGET_AVATAR);
            if (index != -1) {
                targetEntity.setAvatarUrl(cursor.getString(index));
            }
            index = cursor.getColumnIndex(DatabaseConstants.ConversationColumn.COLUMN_NAME_CONVERSATION_TYPE);
            if (index != -1) {
                dataEntity.setType(cursor.getString(index));
            }
            index = cursor.getColumnIndex(DatabaseConstants.ConversationColumn.COLUMN_NAME_IS_TO_WAITER);
            if (index != -1) {
                dataEntity.setIsToWaiter(false);
            }
            index = cursor.getColumnIndex(DatabaseConstants.ConversationColumn.COLUMN_NAME_CREATE_AT);
            if (index != -1) {
                dataEntity.setCreatedAt(cursor.getString(index));
            }
            index = cursor.getColumnIndex(DatabaseConstants.ConversationColumn.COLUMN_NAME_UNREAD_COUNT);
            if (index != -1) {
                dataEntity.setUnreadMessageCount(cursor.getInt(index));
            }
            index = cursor.getColumnIndex(DatabaseConstants.ConversationColumn.COLUMN_NAME_MSG_ID);
            if (index != -1) {
                lastMessageEntity.setMessageId(cursor.getInt(index));
            }
            index = cursor.getColumnIndex(DatabaseConstants.ConversationColumn.COLUMN_NAME_MSG_TYPE);
            if (index != -1) {
                lastMessageEntity.setMtype(cursor.getInt(index));
            }
            index = cursor.getColumnIndex(DatabaseConstants.ConversationColumn.COLUMN_NAME_FROM);
            if (index != -1) {
                lastMessageEntity.setFrom(cursor.getString(index));
            }
            index = cursor.getColumnIndex(DatabaseConstants.ConversationColumn.COLUMN_NAME_TO);
            if (index != -1) {
                lastMessageEntity.setTo(cursor.getString(index));
            }
            index = cursor.getColumnIndex(DatabaseConstants.ConversationColumn.COLUMN_NAME_MSG_STAMP);
            if (index != -1) {
                lastMessageEntity.setDnjts(cursor.getLong(index));
            }
            index = cursor.getColumnIndex(DatabaseConstants.ConversationColumn.COLUMN_NAME_MSG_CONTENT_TYPE);
            if (index != -1) {
                contentEntity.setCtype(cursor.getInt(index));
            }
            index = cursor.getColumnIndex(DatabaseConstants.ConversationColumn.COLUMN_NAME_MSG_CONTENT);
            if (index != -1) {
                contentEntity.setMsg(cursor.getString(index));
            }
            index = cursor.getColumnIndex(DatabaseConstants.ConversationColumn.COLUMN_NAME_SERVICE_STATUS);
            if (index != -1) {
                currentServiceEntity.setCurrentStatus(cursor.getString(index));
            }
            lastMessageEntity.setContent(contentEntity);
            dataEntity.setLastMessage(lastMessageEntity);
            dataEntity.setTarget(targetEntity);
            dataEntity.setCurrentService(currentServiceEntity);
            return dataEntity;
        }

        @Override
        public ContentValues getContentValues(BaseBeanDB bean) {
            DataEntity dataEntity = (DataEntity) bean;

            ContentValues contentValues = new ContentValues();
            if (dataEntity != null) {
                TargetEntity targetEntity = dataEntity.getTarget();
                if (targetEntity != null) {
                    contentValues.put(DatabaseConstants.ConversationColumn.COLUMN_NAME_TARGET_NAME, targetEntity.getUsername());
                    contentValues.put(DatabaseConstants.ConversationColumn.COLUMN_NAME_NAME, targetEntity.getName());
                    contentValues.put(DatabaseConstants.ConversationColumn.COLUMN_NAME_TARGET_IDENTITY, targetEntity.getIdentity());
                    contentValues.put(DatabaseConstants.ConversationColumn.COLUMN_NAME_TARGET_USER_ID, targetEntity.getUserId());
                    contentValues.put(DatabaseConstants.ConversationColumn.COLUMN_NAME_TARGET_AVATAR, targetEntity.getAvatarUrl());
                }
                if (dataEntity != null) {
                    contentValues.put(DatabaseConstants.ConversationColumn.COLUMN_NAME_CONVERSATION_TYPE, dataEntity.getType());
                    contentValues.put(DatabaseConstants.ConversationColumn.COLUMN_NAME_CREATE_AT, dataEntity.getCreatedAt());
                    contentValues.put(DatabaseConstants.ConversationColumn.COLUMN_NAME_IS_TO_WAITER, 0);
                    contentValues.put(DatabaseConstants.ConversationColumn.COLUMN_NAME_UNREAD_COUNT, dataEntity.getUnreadMessageCount());
                }
                MessageEntity lastMessageEntity = dataEntity.getLastMessage();
                if (lastMessageEntity != null) {
                    contentValues.put(DatabaseConstants.ConversationColumn.COLUMN_NAME_MSG_ID, lastMessageEntity.getMessageId());
                    contentValues.put(DatabaseConstants.ConversationColumn.COLUMN_NAME_MSG_TYPE, lastMessageEntity.getMtype());
                    contentValues.put(DatabaseConstants.ConversationColumn.COLUMN_NAME_FROM, lastMessageEntity.getFrom());
                    contentValues.put(DatabaseConstants.ConversationColumn.COLUMN_NAME_TO, lastMessageEntity.getTo());
                    contentValues.put(DatabaseConstants.ConversationColumn.COLUMN_NAME_MSG_STAMP, lastMessageEntity.getDnjts());
                    MessageEntity.ContentEntity contentEntity = lastMessageEntity.getContent();
                    if (contentEntity != null) {
                        contentValues.put(DatabaseConstants.ConversationColumn.COLUMN_NAME_MSG_CONTENT_TYPE, contentEntity.getCtype());
                        contentValues.put(DatabaseConstants.ConversationColumn.COLUMN_NAME_MSG_CONTENT, contentEntity.getMsg());
                    }
                }
                CurrentServiceEntity currentServiceEntity = dataEntity.getCurrentService();
                if (currentServiceEntity != null) {
                    contentValues.put(DatabaseConstants.ConversationColumn.COLUMN_NAME_SERVICE_STATUS, currentServiceEntity.getCurrentStatus());
                }
            }
            return contentValues;
        }
    }
}
