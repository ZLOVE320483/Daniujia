package com.xiaojia.daniujia.ui.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaojia.daniujia.IMMsgFlag;
import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.domain.resp.MessageEntity;
import com.xiaojia.daniujia.domain.resp.PullConversationsVo;
import com.xiaojia.daniujia.ui.views.imageview.RoundedImageView;
import com.xiaojia.daniujia.utils.ApplicationUtil;
import com.xiaojia.daniujia.utils.DisplayUtil;
import com.xiaojia.daniujia.utils.MessageUtil;
import com.xiaojia.daniujia.utils.ScreenUtils;
import com.xiaojia.daniujia.utils.SysUtil;
import com.xiaojia.daniujia.utils.TimeUtils;

import java.util.List;

/**
 * Created by ZLOVE on 2016/4/10.
 */
public class ConversationAdapter extends SingleDataListAdapter<PullConversationsVo.DataEntity> {

    public ConversationAdapter(List<PullConversationsVo.DataEntity> data, Context context) {
        super(data, context);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_chat_record, null);
            holder.ivHead = (RoundedImageView) convertView.findViewById(R.id.iv_cr_head);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_cr_name);
            holder.tvContent = (TextView) convertView.findViewById(R.id.tv_cr_content);
            holder.tvTime = (TextView) convertView.findViewById(R.id.tv_cr_time);
            holder.tvUnReadCount = (TextView) convertView.findViewById(R.id.tv_cr_unread);
            holder.ivServerType = (ImageView) convertView.findViewById(R.id.iv_cr_server_type);
            holder.ivState = (ImageView) convertView.findViewById(R.id.image_state);
            holder.ivSendFail = (ImageView) convertView.findViewById(R.id.item_send_fail);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final PullConversationsVo.DataEntity item = getItem(position);
        if (item != null) {
            int unReadCount = item.getUnreadMessageCount();
            if (unReadCount > 99) {
                holder.tvUnReadCount.setVisibility(View.VISIBLE);
                holder.tvUnReadCount.setText("99+");
            } else if (unReadCount > 0 && unReadCount <= 99) {
                holder.tvUnReadCount.setVisibility(View.VISIBLE);
                holder.tvUnReadCount.setText(String.valueOf(unReadCount));
            } else {
                holder.tvUnReadCount.setVisibility(View.GONE);
            }
            if (item.getTarget().getUsername().equals(SysUtil.getPref(PrefKeyConst.PREF_USER_NAME))) {
                holder.tvUnReadCount.setVisibility(View.GONE);
            }
            PullConversationsVo.DataEntity.TargetEntity entity = item.getTarget();
            if (entity != null) {
                if (TextUtils.isEmpty(entity.getAvatarUrl())) {
                    holder.ivHead.setImageResource(R.drawable.ic_avatar_default);
                } else {
                    DisplayUtil.display(entity.getAvatarUrl()).resize(ScreenUtils.dip2px(60.0f), ScreenUtils.dip2px(60.0f)).into(holder.ivHead);
                }
                if (!TextUtils.isEmpty(entity.getName())) {
                    if (entity.getName().equals("eric")) {
                        holder.tvName.setText(R.string.app_name);
                    } else {
                        holder.tvName.setText(entity.getName());
                    }
                } else {
                    holder.tvName.setText(entity.getUsername());
                }
            }
            MessageEntity conversationMessage = item.getLastMessage();
            if (conversationMessage != null) {
                if (conversationMessage.getDnjts() == 0){
                    holder.tvTime.setText("");
                } else {
                    holder.tvTime.setText(TimeUtils.timeFormateShow(conversationMessage.getDnjts(), 0));
                }
                MessageEntity.ContentEntity content = conversationMessage.getContent();
                if (content != null) {
                    int msgContentType = content.getCtype();
                    if (msgContentType == IMMsgFlag.MSG_CHAT_TEXT) {
                        if (!TextUtils.isEmpty(content.getMsg())) {
                            holder.tvContent.setText(MessageUtil.toSpannableString(content.getMsg(), ApplicationUtil.getApplicationContext(), null, true));
                        }
                    } else if (msgContentType == IMMsgFlag.MSG_CHAT_IMAGE) {
                        holder.tvContent.setText("[图片]");
                    } else if (msgContentType == IMMsgFlag.MSG_CHAT_VOICE) {
                        holder.tvContent.setText("[语音]");
                    } else if (msgContentType == IMMsgFlag.MSG_CHAT_EVENT) {
                        holder.tvContent.setText("[通知消息]");
                    } else if (msgContentType == IMMsgFlag.MSG_CHAT_ATTACH) {
                        holder.tvContent.setText("[附件]");
                    } else if (msgContentType == IMMsgFlag.MSG_CHAT_REQUIREMENT) {
                        holder.tvContent.setText("[需求消息]");
                    } else {
                        holder.tvContent.setText("");
                    }
                } else {
                    holder.tvContent.setText("");
                }
            } else {
                holder.tvContent.setText("");
            }
        }
        return convertView;
    }

    class ViewHolder {
        RoundedImageView ivHead;
        TextView tvName;
        TextView tvContent;
        TextView tvTime;
        TextView tvUnReadCount;
        ImageView ivServerType;
        ImageView ivState;
        ImageView ivSendFail;
    }
}
