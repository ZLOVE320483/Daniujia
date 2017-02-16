package com.xiaojia.daniujia.domain.resp;

import android.content.ContentValues;
import android.database.Cursor;

import com.xiaojia.daniujia.db.DatabaseConstants;
import com.xiaojia.daniujia.db.bean.BaseBeanDB;

/**
 * Created by ZLOVE on 2016/4/18.
 */
public class ReceiveMsgVo extends BaseBeanDB {

    /**
     * eventId : 3
     * message : {"content":{"msg":"很喜欢喜欢","ctype":1},"to":"lisi","code":"1460956465784","mtype":1,"from":"weaver","dnjts":1460956465082,"type":"singleChat","messageId":36}
     */

    private int eventId;
    /**
     * content : {"msg":"很喜欢喜欢","ctype":1}
     * to : lisi
     * code : 1460956465784
     * mtype : 1
     * from : weaver
     * dnjts : 1460956465082
     * type : singleChat
     * messageId : 36
     */

    private MessageEntity data;

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setData(MessageEntity data) {
        this.data = data;
    }

    public MessageEntity getData() {
        return data;
    }

    @Override
    public BaseBeanDB readFromCursor(Cursor cursor) {
        ReceiveMsgVo receiveMsgVo = new ReceiveMsgVo();
        MessageEntity messageEntity = new MessageEntity();
        MessageEntity.ContentEntity contentEntity = new MessageEntity.ContentEntity();

        int index;
        index = cursor.getColumnIndex(DatabaseConstants.MessageColumn.COLUMN_NAME_MSG_ID);
        if (index != -1) {
            messageEntity.setMessageId(cursor.getInt(index));
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
        index = cursor.getColumnIndex(DatabaseConstants.MessageColumn.COLUMN_NAME_MSG_CONTENT_TYPE);
        if (index != -1) {
            contentEntity.setCtype(cursor.getInt(index));
        }
        index = cursor.getColumnIndex(DatabaseConstants.MessageColumn.COLUMN_NAME_MSG_CONTENT);
        if (index != -1) {
            contentEntity.setMsg(cursor.getString(index));
        }
        messageEntity.setContent(contentEntity);
        receiveMsgVo.setData(messageEntity);
        return receiveMsgVo;
    }

    @Override
    public ContentValues getContentValues(BaseBeanDB bean) {
        ReceiveMsgVo receiveMsgVo = (ReceiveMsgVo) bean;
        MessageEntity messageEntity = receiveMsgVo.getData();
        MessageEntity.ContentEntity contentEntity = messageEntity.getContent();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseConstants.MessageColumn.COLUMN_NAME_MSG_ID, messageEntity.getMessageId());
        contentValues.put(DatabaseConstants.MessageColumn.COLUMN_NAME_MSG_TYPE, messageEntity.getMtype());
        contentValues.put(DatabaseConstants.MessageColumn.COLUMN_NAME_FROM, messageEntity.getFrom());
        contentValues.put(DatabaseConstants.MessageColumn.COLUMN_NAME_TO, messageEntity.getTo());
        contentValues.put(DatabaseConstants.MessageColumn.COLUMN_NAME_CONVERSATION_TYPE, messageEntity.getType());
        contentValues.put(DatabaseConstants.MessageColumn.COLUMN_NAME_MSG_STAMP, messageEntity.getDnjts());
        contentValues.put(DatabaseConstants.MessageColumn.COLUMN_NAME_MSG_CODE, messageEntity.getCode());
        contentValues.put(DatabaseConstants.MessageColumn.COLUMN_NAME_MSG_CONTENT_TYPE, contentEntity.getCtype());
        contentValues.put(DatabaseConstants.MessageColumn.COLUMN_NAME_MSG_CONTENT, contentEntity.getMsg());
        contentValues.put(DatabaseConstants.MessageColumn.COLUMN_NAME_URL, contentEntity.getUrl());

        return contentValues;
    }

    public ContentValues getImgContentValues(BaseBeanDB bean) {
        ReceiveMsgVo receiveMsgVo = (ReceiveMsgVo) bean;

        ContentValues contentValues = new ContentValues();
        if (receiveMsgVo != null) {
            MessageEntity messageEntity = receiveMsgVo.getData();
            if (messageEntity != null) {
                contentValues.put(DatabaseConstants.MessageColumn.COLUMN_NAME_MSG_ID, messageEntity.getMessageId());
                contentValues.put(DatabaseConstants.MessageColumn.COLUMN_NAME_MSG_TYPE, messageEntity.getMtype());
                contentValues.put(DatabaseConstants.MessageColumn.COLUMN_NAME_FROM, messageEntity.getFrom());
                contentValues.put(DatabaseConstants.MessageColumn.COLUMN_NAME_TO, messageEntity.getTo());
                contentValues.put(DatabaseConstants.MessageColumn.COLUMN_NAME_CONVERSATION_TYPE, messageEntity.getType());
                contentValues.put(DatabaseConstants.MessageColumn.COLUMN_NAME_MSG_STAMP, messageEntity.getDnjts());
                contentValues.put(DatabaseConstants.MessageColumn.COLUMN_NAME_MSG_CODE, messageEntity.getCode());
            }
            if (messageEntity != null) {
                MessageEntity.ContentEntity contentEntity = messageEntity.getContent();
                if (contentEntity != null) {
                    contentValues.put(DatabaseConstants.MessageColumn.COLUMN_NAME_MSG_CONTENT_TYPE, contentEntity.getCtype());
                    contentValues.put(DatabaseConstants.MessageColumn.COLUMN_NAME_MSG_CONTENT, contentEntity.getMsg());
                }
            }
        }

        return contentValues;
    }
}
