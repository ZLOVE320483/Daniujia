package com.xiaojia.daniujia.utils;

import com.xiaojia.daniujia.IMMsgFlag;
import com.xiaojia.daniujia.domain.resp.MessageEntity;

import org.json.JSONObject;

/**
 * all the data generation about message will be better defined in ChatDetail
 * TODO
 */
public class SpecificJsonUtils {

    public static JSONObject generateChatJson_newVersion(MessageEntity chatDetail) {

        JSONObject jsonObject = new JSONObject();
        JSONObject message = new JSONObject();
        JSONObject content = new JSONObject();
        try {
            jsonObject.put("topic", "private-" + chatDetail.getTo());
            message.put("type", "singleChat");
            message.put("from", chatDetail.getFrom());
            message.put("to", chatDetail.getTo());
            message.put("mtype", chatDetail.getMtype());
            message.put("code", chatDetail.getCode());
            content.put("msg", chatDetail.getContent().getMsg());
            content.put("ctype", chatDetail.getContent().getCtype());
            content.put("height", chatDetail.getContent().getHeight());
            content.put("width", chatDetail.getContent().getWidth());
            content.put("duration", chatDetail.getContent().getDuration());
            content.put("url", chatDetail.getContent().getUrl());
            content.put("messageId", chatDetail.getContent().getMessageId());
            content.put("filename",chatDetail.getContent().getFilename());
            content.put("size",chatDetail.getContent().getSize());
            content.put("mimeType",chatDetail.getContent().getMimeType());
            content.put("sourcePlatform",chatDetail.getContent().getSourcePlatform());
            message.put("content", content);
            jsonObject.put("message", message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static String generateNotifyMsg(MessageEntity messageEntity) {
        String notifyMsg = "";
        if (messageEntity.getContent().getCtype() == IMMsgFlag.MSG_CHAT_TEXT) {
            notifyMsg = messageEntity.getContent().getMsg();
        } else if (messageEntity.getContent().getCtype() == IMMsgFlag.MSG_CHAT_IMAGE) {
            notifyMsg = "【图片】";
        } else if (messageEntity.getContent().getCtype() == IMMsgFlag.MSG_CHAT_VOICE) {
            notifyMsg = "【语音】";
        } else if (messageEntity.getContent().getCtype() == IMMsgFlag.MSG_CHAT_EVENT){
            notifyMsg = "评价消息";
        }
        return notifyMsg;
    }

}
