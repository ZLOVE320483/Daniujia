package com.xiaojia.daniujia.domain.resp;

import android.content.ContentValues;
import android.database.Cursor;

import com.xiaojia.daniujia.db.DatabaseConstants;
import com.xiaojia.daniujia.db.bean.BaseBeanDB;

import java.io.Serializable;

/**
 * Created by ZLOVE on 2016/4/21.
 */
public class MessageEntity extends BaseBeanDB implements Serializable {

    private long messageId;
    private int mtype;
    private String from;
    private String to;
    private String type = "";
    private boolean read;
    private boolean isPlaying = false;

    /**
     * 该条消息发送的时间
     */
    private long dnjts;
    /**
     * 标记消息的唯一标识
     */
    private String code;
    private ContentEntity content;
    /**
     *  0 未读  1 已读
     */
    private int isRead;
    /**
     * 消息发送状态，0 发送中  2 发送失败
     */
    private int msgState;

    private int isDown = 1;
    /**
     * 语音消息是否已经播放，0未播放
     */
    private int isPlay = 1;

    private int uploadProgress = 100;

    public int getUploadProgress() {
        return uploadProgress;
    }

    public void setUploadProgress(int uploadProgress) {
        this.uploadProgress = uploadProgress;
    }

    public void setIsDown(int isDown) {
        this.isDown = isDown;
    }

    public int getIsDown() {
        return isDown;
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public int getMtype() {
        return mtype;
    }

    public void setMtype(int mtype) {
        this.mtype = mtype;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getDnjts() {
        return dnjts;
    }

    public void setDnjts(long dnjts) {
        this.dnjts = dnjts;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public int getMsgState() {
        return msgState;
    }

    public void setMsgState(int msgState) {
        this.msgState = msgState;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public boolean isRead() {
        return read;
    }

    public ContentEntity getContent() {
        return content;
    }

    public void setContent(ContentEntity content) {
        this.content = content;
    }

    public void setIsPlay(int isPlay) {
        this.isPlay = isPlay;
    }

    public int getIsPlay() {
        return isPlay;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    @Override
    public String toString() {
        return "MessageEntity{" +
                "messageId=" + messageId +
                ", mtype=" + mtype +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", type='" + type + '\'' +
                ", dnjts=" + dnjts +
                ", code='" + code + '\'' +
                ", content=" + content +
                ", isRead=" + isRead +
                ", msgState=" + msgState +
                ", isDown=" + isDown +
                ", isPlay=" + isPlay +
                '}';
    }

    public static class ContentEntity implements Serializable {
        /**
         * 消息类型
         */
        private int ctype = 1;
        /**
         *  1、文本消息：文本内容
         *  2、图片消息：图片url
         */
        private String msg = "";
        private int height = 0;
        private int width = 0;
        private String url = "";
        private int duration = 0;
        /**
         * 事件消息的订单状态  比如 order:requested
         */
        private String type = "";
        private String target = "";
        private long messageId = 0;
        private String filename;
        private long size;
        private String mimeType;
        private String localPath;
        private String sourcePlatform;
        private int demandId = 0;
        private String notice;
        private String previewUrl;

        public String getPreviewUrl() {
            return previewUrl;
        }

        public void setPreviewUrl(String previewUrl) {
            this.previewUrl = previewUrl;
        }

        public String getNotice() {
            return notice;
        }

        public void setNotice(String notice) {
            this.notice = notice;
        }

        public int getDemandId() {
            return demandId;
        }

        public void setDemandId(int demandId) {
            this.demandId = demandId;
        }

        public String getSourcePlatform() {
            return sourcePlatform;
        }

        public void setSourcePlatform(String sourcePlatform) {
            this.sourcePlatform = sourcePlatform;
        }

        public String getLocalPath() {
            return localPath;
        }

        public void setLocalPath(String localPath) {
            this.localPath = localPath;
        }

        public String getFilename() {
            return filename;
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }

        public long getSize() {
            return size;
        }

        public void setSize(long size) {
            this.size = size;
        }

        public String getMimeType() {
            return mimeType;
        }

        public void setMimeType(String mimeType) {
            this.mimeType = mimeType;
        }

        public long getMessageId() {
            return messageId;
        }

        public void setMessageId(long messageId) {
            this.messageId = messageId;
        }

        public String getType() {
            return type;
        }

        public String getTarget() {
            return target;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setTarget(String target) {
            this.target = target;
        }

        private ContentMessage message;

        public int getCtype() {
            return ctype;
        }

        public void setCtype(int ctype) {
            this.ctype = ctype;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public ContentMessage getMessage() {
            return message;
        }

        public void setMessage(ContentMessage message) {
            this.message = message;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public static class ContentMessage implements Serializable {
            private long question_id = 0;
            private String question_content = "";

            public long getQuestion_id() {
                return question_id;
            }

            public void setQuestion_id(long question_id) {
                this.question_id = question_id;
            }

            public String getQuestion_content() {
                return question_content;
            }

            public void setQuestion_content(String question_content) {
                this.question_content = question_content;
            }
        }

        @Override
        public String toString() {
            return "ContentEntity{" +
                    "ctype=" + ctype +
                    ", msg='" + msg + '\'' +
                    ", height=" + height +
                    ", width=" + width +
                    ", url='" + url + '\'' +
                    ", duration=" + duration +
                    ", type='" + type + '\'' +
                    ", target='" + target + '\'' +
                    ", messageId=" + messageId +
                    ", message=" + message +
                    '}';
        }
    }

    @Override
    public BaseBeanDB readFromCursor(Cursor cursor) {
        MessageEntity messageEntity = new MessageEntity();
        ContentEntity contentEntity = new ContentEntity();
        ContentEntity.ContentMessage contentMessage = new ContentEntity.ContentMessage();

        int index;
        index = cursor.getColumnIndex(DatabaseConstants.MessageColumn.COLUMN_NAME_MSG_ID);
        if (index != -1) {
            messageEntity.setMessageId(cursor.getLong(index));
        }
        index = cursor.getColumnIndex(DatabaseConstants.MessageColumn.COLUMN_NAME_MSG_TYPE);
        if (index != -1) {
            messageEntity.setMtype(cursor.getInt(index));
        }
        index = cursor.getColumnIndex(DatabaseConstants.MessageColumn.COLUMN_NAME_FROM);
        if (index != -1) {
            messageEntity.setFrom(cursor.getString(index));
        }
        index = cursor.getColumnIndex(DatabaseConstants.MessageColumn.COLUMN_NAME_TO);
        if (index != -1) {
            messageEntity.setTo(cursor.getString(index));
        }
        index = cursor.getColumnIndex(DatabaseConstants.MessageColumn.COLUMN_NAME_CONVERSATION_TYPE);
        if (index != -1) {
            messageEntity.setType(cursor.getString(index));
        }
        index = cursor.getColumnIndex(DatabaseConstants.MessageColumn.COLUMN_NAME_MSG_STAMP);
        if (index != -1) {
            messageEntity.setDnjts(cursor.getLong(index));
        }
        index = cursor.getColumnIndex(DatabaseConstants.MessageColumn.COLUMN_NAME_MSG_CODE);
        if (index != -1) {
            messageEntity.setCode(cursor.getString(index));
        }
        index = cursor.getColumnIndex(DatabaseConstants.MessageColumn.COLUMN_NAME_IS_DOWN);
        if (index != -1) {
            messageEntity.setIsDown(cursor.getInt(index));
        }
        index = cursor.getColumnIndex(DatabaseConstants.MessageColumn.COLUMN_NAME_IMG_UPLOAD_PROGRESS);
        if (index != -1) {
            messageEntity.setUploadProgress(cursor.getInt(index));
        }
        index = cursor.getColumnIndex(DatabaseConstants.MessageColumn.COLUMN_NAME_MSG_CONTENT_TYPE);
        if (index != -1) {
            contentEntity.setCtype(cursor.getInt(index));
        }
        index = cursor.getColumnIndex(DatabaseConstants.MessageColumn.COLUMN_NAME_MSG_EVENT_TYPE);
        if (index != -1) {
            contentEntity.setType(cursor.getString(index));
        }
        index = cursor.getColumnIndex(DatabaseConstants.MessageColumn.COLUMN_NAME_MSG_EVENT_TARGET);
        if (index != -1) {
            contentEntity.setTarget(cursor.getString(index));
        }
        index = cursor.getColumnIndex(DatabaseConstants.MessageColumn.COLUMN_NAME_MSG_CONTENT);
        if (index != -1) {
            contentEntity.setMsg(cursor.getString(index));
        }
        index = cursor.getColumnIndex(DatabaseConstants.MessageColumn.COLUMN_NAME_DEMAND_ID);
        if (index != -1) {
            contentEntity.setDemandId(cursor.getInt(index));
        }
        index = cursor.getColumnIndex(DatabaseConstants.MessageColumn.COLUMN_NAME_DEMAND_NOTICE);
        if (index != -1) {
            contentEntity.setNotice(cursor.getString(index));
        }
        index = cursor.getColumnIndex(DatabaseConstants.MessageColumn.COLUMN_NAME_FILE_PRE_VIEW_URL);
        if (index != -1) {
            contentEntity.setPreviewUrl(cursor.getString(index));
        }
        index = cursor.getColumnIndex(DatabaseConstants.MessageColumn.COLUMN_NAME_MSG_IS_READ);
        if (index != -1) {
            messageEntity.setIsRead(cursor.getInt(index));
        }
        index = cursor.getColumnIndex(DatabaseConstants.MessageColumn.COLUMN_NAME_VOICE_IS_PLAY);
        if (index != -1) {
            messageEntity.setIsPlay(cursor.getInt(index));
        }
        index = cursor.getColumnIndex(DatabaseConstants.MessageColumn.COLUMN_NAME_MSG_STATE);
        if (index != -1) {
            messageEntity.setMsgState(cursor.getInt(index));
        }
        index = cursor.getColumnIndex(DatabaseConstants.MessageColumn.COLUMN_NAME_IMG_HEIGHT);
        if (index != -1) {
            contentEntity.setHeight(cursor.getInt(index));
        }
        index = cursor.getColumnIndex(DatabaseConstants.MessageColumn.COLUMN_NAME_IMG_WIDTH);
        if (index != -1) {
            contentEntity.setWidth(cursor.getInt(index));
        }
        index = cursor.getColumnIndex(DatabaseConstants.MessageColumn.COLUMN_NAME_URL);
        if (index != -1) {
            contentEntity.setUrl(cursor.getString(index));
        }
        index = cursor.getColumnIndex(DatabaseConstants.MessageColumn.COLUMN_NAME_DURATION);
        if (index != -1) {
            contentEntity.setDuration(cursor.getInt(index));
        }
        index = cursor.getColumnIndex(DatabaseConstants.MessageColumn.COLUMN_NAME_ATTACH_FILE_NAME);
        if (index != -1) {
            contentEntity.setFilename(cursor.getString(index));
        }
        index = cursor.getColumnIndex(DatabaseConstants.MessageColumn.COLUMN_NAME_ATTACH_SIZE);
        if (index != -1) {
            contentEntity.setSize(cursor.getLong(index));
        }
        index = cursor.getColumnIndex(DatabaseConstants.MessageColumn.COLUMN_NAME_LOCAL_PATH);
        if (index != -1) {
            contentEntity.setLocalPath(cursor.getString(index));
        }
        index = cursor.getColumnIndex(DatabaseConstants.MessageColumn.COLUMN_NAME_ATTACH_SOURCE);
        if (index != -1) {
            contentEntity.setSourcePlatform(cursor.getString(index));
        }
        index = cursor.getColumnIndex(DatabaseConstants.MessageColumn.COLUMN_NAME_MIME_TYPE);
        if (index != -1) {
            contentEntity.setMimeType(cursor.getString(index));
        }
        index = cursor.getColumnIndex(DatabaseConstants.MessageColumn.COLUMN_NAME_QUESTION_ID);
        if (index != -1) {
            contentMessage.setQuestion_id(cursor.getLong(index));
        }
        index = cursor.getColumnIndex(DatabaseConstants.MessageColumn.COLUMN_NAME_QUESTION_CONTENT);
        if (index != -1) {
            contentMessage.setQuestion_content(cursor.getString(index));
        }
        contentEntity.setMessage(contentMessage);
        messageEntity.setContent(contentEntity);
        return messageEntity;
    }

    @Override
    public ContentValues getContentValues(BaseBeanDB bean) {
        MessageEntity messageEntity = (MessageEntity) bean;

        ContentValues contentValues = new ContentValues();
        if (messageEntity != null) {
            contentValues.put(DatabaseConstants.MessageColumn.COLUMN_NAME_MSG_ID, messageEntity.getMessageId());
            contentValues.put(DatabaseConstants.MessageColumn.COLUMN_NAME_MSG_TYPE, messageEntity.getMtype());
            contentValues.put(DatabaseConstants.MessageColumn.COLUMN_NAME_FROM, messageEntity.getFrom());
            contentValues.put(DatabaseConstants.MessageColumn.COLUMN_NAME_TO, messageEntity.getTo());
            contentValues.put(DatabaseConstants.MessageColumn.COLUMN_NAME_CONVERSATION_TYPE, messageEntity.getType());
            contentValues.put(DatabaseConstants.MessageColumn.COLUMN_NAME_MSG_STAMP, messageEntity.getDnjts());
            contentValues.put(DatabaseConstants.MessageColumn.COLUMN_NAME_MSG_CODE, messageEntity.getCode());
            contentValues.put(DatabaseConstants.MessageColumn.COLUMN_NAME_MSG_IS_READ, messageEntity.getIsRead());
            contentValues.put(DatabaseConstants.MessageColumn.COLUMN_NAME_VOICE_IS_PLAY, messageEntity.getIsPlay());
            contentValues.put(DatabaseConstants.MessageColumn.COLUMN_NAME_MSG_STATE, messageEntity.getMsgState());
            contentValues.put(DatabaseConstants.MessageColumn.COLUMN_NAME_IS_DOWN, messageEntity.getIsDown());
            contentValues.put(DatabaseConstants.MessageColumn.COLUMN_NAME_IMG_UPLOAD_PROGRESS, messageEntity.getUploadProgress());
        }
        ContentEntity contentEntity = messageEntity.getContent();
        if (contentEntity != null) {
            contentValues.put(DatabaseConstants.MessageColumn.COLUMN_NAME_MSG_CONTENT_TYPE, contentEntity.getCtype());
            contentValues.put(DatabaseConstants.MessageColumn.COLUMN_NAME_MSG_EVENT_TYPE, contentEntity.getType());
            contentValues.put(DatabaseConstants.MessageColumn.COLUMN_NAME_MSG_EVENT_TARGET, contentEntity.getTarget());
            contentValues.put(DatabaseConstants.MessageColumn.COLUMN_NAME_MSG_CONTENT, contentEntity.getMsg());
            contentValues.put(DatabaseConstants.MessageColumn.COLUMN_NAME_IMG_HEIGHT, contentEntity.getHeight());
            contentValues.put(DatabaseConstants.MessageColumn.COLUMN_NAME_IMG_WIDTH, contentEntity.getWidth());
            contentValues.put(DatabaseConstants.MessageColumn.COLUMN_NAME_URL, contentEntity.getUrl());
            contentValues.put(DatabaseConstants.MessageColumn.COLUMN_NAME_DURATION, contentEntity.getDuration());
            contentValues.put(DatabaseConstants.MessageColumn.COLUMN_NAME_ATTACH_FILE_NAME, contentEntity.getFilename());
            contentValues.put(DatabaseConstants.MessageColumn.COLUMN_NAME_ATTACH_SIZE, contentEntity.getSize());
            contentValues.put(DatabaseConstants.MessageColumn.COLUMN_NAME_MIME_TYPE, contentEntity.getMimeType());
            contentValues.put(DatabaseConstants.MessageColumn.COLUMN_NAME_LOCAL_PATH, contentEntity.getLocalPath());
            contentValues.put(DatabaseConstants.MessageColumn.COLUMN_NAME_ATTACH_SOURCE, contentEntity.getSourcePlatform());
            contentValues.put(DatabaseConstants.MessageColumn.COLUMN_NAME_DEMAND_ID, contentEntity.getDemandId());
            contentValues.put(DatabaseConstants.MessageColumn.COLUMN_NAME_DEMAND_NOTICE, contentEntity.getNotice());
            contentValues.put(DatabaseConstants.MessageColumn.COLUMN_NAME_FILE_PRE_VIEW_URL, contentEntity.getPreviewUrl());
            ContentEntity.ContentMessage contentMessage = contentEntity.getMessage();
            if (contentMessage != null) {
                contentValues.put(DatabaseConstants.MessageColumn.COLUMN_NAME_QUESTION_ID, contentMessage.getQuestion_id());
                contentValues.put(DatabaseConstants.MessageColumn.COLUMN_NAME_QUESTION_CONTENT, contentMessage.getQuestion_content());
            }
        }

        return contentValues;
    }
}
