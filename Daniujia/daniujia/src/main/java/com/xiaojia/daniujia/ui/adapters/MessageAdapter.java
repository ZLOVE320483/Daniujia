package com.xiaojia.daniujia.ui.adapters;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xiaojia.daniujia.ExtraConst;
import com.xiaojia.daniujia.IMMsgFlag;
import com.xiaojia.daniujia.InsideMsg;
import com.xiaojia.daniujia.IntentKey;
import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.base.App;
import com.xiaojia.daniujia.db.DatabaseConstants;
import com.xiaojia.daniujia.db.DatabaseManager;
import com.xiaojia.daniujia.db.executor.BaseDatabaseExecutor;
import com.xiaojia.daniujia.db.executor.DatabaseUpdateExecutor;
import com.xiaojia.daniujia.domain.SerializableMap;
import com.xiaojia.daniujia.domain.UserInfo;
import com.xiaojia.daniujia.domain.resp.MessageEntity;
import com.xiaojia.daniujia.mqtt.MqttUtils;
import com.xiaojia.daniujia.msg.MsgHelper;
import com.xiaojia.daniujia.msg.OnMsgCallback;
import com.xiaojia.daniujia.msg.annotation.OnMsg;
import com.xiaojia.daniujia.provider.DaniujiaUris;
import com.xiaojia.daniujia.ui.act.ActExpertHome;
import com.xiaojia.daniujia.ui.act.ActFilePreView;
import com.xiaojia.daniujia.ui.act.ActRequirementDetail;
import com.xiaojia.daniujia.ui.act.ActUserHome;
import com.xiaojia.daniujia.ui.act.ImagePreActivity;
import com.xiaojia.daniujia.ui.act.WebActivity;
import com.xiaojia.daniujia.ui.frag.MessageFragment;
import com.xiaojia.daniujia.ui.views.imageview.BubberImageView.BubbleImageView;
import com.xiaojia.daniujia.ui.views.imageview.RoundedImageView;
import com.xiaojia.daniujia.ui.views.load.LoadableVoiceView;
import com.xiaojia.daniujia.utils.ApplicationUtil;
import com.xiaojia.daniujia.utils.DisplayUtil;
import com.xiaojia.daniujia.utils.FileUtil;
import com.xiaojia.daniujia.utils.ListUtils;
import com.xiaojia.daniujia.utils.MessageUtil;
import com.xiaojia.daniujia.utils.ScreenUtils;
import com.xiaojia.daniujia.utils.SysUtil;
import com.xiaojia.daniujia.utils.TimeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ZLOVE on 2016/4/10
 */
public class MessageAdapter extends ArrayAdapter<MessageEntity> implements View.OnClickListener, View.OnLongClickListener {

    private static final int type_send_none = 0;
    private static final int type_send_text = 1;
    private static final int type_rec_text = 2;
    private static final int type_send_img = 3;
    private static final int type_rec_img = 4;
    private static final int type_send_voice = 5;
    private static final int type_rec_voice = 6;
    private static final int type_send_question = 7;
    private static final int type_rec_question = 8;
    private static final int type_event = 9;
    private static final int type_send_attach = 10;
    private static final int type_rec_attach = 11;
    private static final int type_rec_requirement = 12;
    private static final int type_send_requirement = 13;
    private static final int type_max = 14;
    private static final SparseArray<Object> map = new SparseArray<>();

    private String avatarUrl = "";
    private Animation sendAnim;
    private List<MessageEntity> list;
    private View clickView;

    private int otherSideId = 0;
    private int otherSideEntity = 1;
    private boolean isContinuePlaying;
    private List<Integer> continueVoiceList;
    private static final int DICTATE_VOICE_PLAY = 0;
    private HashMap<Integer, ViewHolder> holders_map = new HashMap<>();
    private MessageFragment messageFragment;

    public void setOtherSideEntity(int otherSideEntity) {
        this.otherSideEntity = otherSideEntity;
    }

    public void setOtherSideId(int otherSideId) {
        this.otherSideId = otherSideId;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
        sendAnim = AnimationUtils.loadAnimation(ApplicationUtil.getApplicationContext(), R.anim.send_message_rotate);
        LinearInterpolator lin = new LinearInterpolator();
        sendAnim.setInterpolator(lin);
    }

    public void setList(List<MessageEntity> list) {
        this.list = list;
    }

    public List<MessageEntity> getList() {
        if (list != null)
            return this.list;

        return null;
    }

    private Activity activity;

    public MessageAdapter(Activity context) {
        super(context, -1);
        activity = context;
        MsgHelper.getInstance().registMsg(this);
    }

    public void setMessageFragment(MessageFragment messageFragment) {
        this.messageFragment = messageFragment;
    }

    public void clearVoice() {
        if (clickView != null) {
            ViewHolder viewHolder = (ViewHolder) clickView.getTag(R.id.id_view_holder);
            if (viewHolder != null) {
                viewHolder.ivVoice.stopPlay();
            }
        }
    }

    public void clearAnimation() {
        if (clickView != null) {
            ViewHolder viewHolder = (ViewHolder) clickView.getTag(R.id.id_view_holder);
            if (viewHolder != null) {
                viewHolder.ivVoice.clearAnimation();
            }
        }
    }

    @OnMsg(msg = {InsideMsg.CONTINUE_PLAY_VOICE, InsideMsg.STOP_CONTINUE_PLAY_VOICE, InsideMsg.VOICE_PLAYING})
    OnMsgCallback onMsgCallback = new OnMsgCallback() {
        @Override
        public boolean handleMsg(Message msg) {
            switch (msg.what) {
                case InsideMsg.CONTINUE_PLAY_VOICE:
                    if (isContinuePlaying) {
                        startRollPlayVoice();
                    }

                    break;
                case InsideMsg.STOP_CONTINUE_PLAY_VOICE:
                    isContinuePlaying = false;
                    continueVoiceList = null;
                    break;
            }
            return false;
        }
    };

    @Override
    public int getItemViewType(int position) {
        UserInfo userInfo = SysUtil.getUsrInfo();
        MessageEntity dataEntity = getItem(position);
        if (dataEntity != null) {
            MessageEntity.ContentEntity contentEntity = dataEntity.getContent();
            if (contentEntity != null) {
                if (contentEntity.getCtype() == IMMsgFlag.MSG_CHAT_TEXT) {
                    if (dataEntity.getTo().equals(userInfo.getUsername())) {
                        return type_rec_text;
                    } else {
                        return type_send_text;
                    }
                } else if (contentEntity.getCtype() == IMMsgFlag.MSG_CHAT_IMAGE) {
                    if (dataEntity.getTo().equals(userInfo.getUsername())) {
                        return type_rec_img;
                    } else {
                        return type_send_img;
                    }
                } else if (contentEntity.getCtype() == IMMsgFlag.MSG_CHAT_VOICE) {
                    if (dataEntity.getTo().equals(userInfo.getUsername())) {
                        return type_rec_voice;
                    } else {
                        return type_send_voice;
                    }
                } else if (contentEntity.getCtype() == IMMsgFlag.MSG_CHAT_SERVICE_QUESTION || contentEntity.getCtype() == IMMsgFlag.MSG_CHAT_EXPERT_QUESTION) {
                    if (dataEntity.getTo().equals(userInfo.getUsername())) {
                        return type_rec_question;
                    } else {
                        return type_send_question;
                    }
                } else if (contentEntity.getCtype() == IMMsgFlag.MSG_CHAT_EVENT) {
                    return type_event;
                } else if (contentEntity.getCtype() == IMMsgFlag.MSG_CHAT_ATTACH) {
                    if (dataEntity.getTo().equals(userInfo.getUsername())) {
                        return type_rec_attach;
                    } else {
                        return type_send_attach;
                    }
                } else if (contentEntity.getCtype() == IMMsgFlag.MSG_CHAT_REQUIREMENT) {
                    if (dataEntity.getTo().equals(userInfo.getUsername())) {
                        return type_rec_requirement;
                    } else {
                        return type_send_requirement;
                    }
                }
            }
        }
        return type_send_none;
    }

    public void doImageClick(int position) {
        int key = -1;
        for (MessageEntity data : list) {
            if (data.getContent().getCtype() == IMMsgFlag.MSG_CHAT_IMAGE) {
                String image_path = data.getContent().getMsg();
                if (!TextUtils.isEmpty(image_path)) {
                    map.put(++key, image_path);
                }
            }
        }
        String urlFromPosition = getUrlFromPosition(position);
        if (TextUtils.isEmpty(urlFromPosition)) {
            return;
        }
        SerializableMap myMap = new SerializableMap();
        Intent intent = new Intent(getContext(), ImagePreActivity.class);
        Bundle bundle = new Bundle();
        myMap.setMap(map);
        bundle.putParcelable(IntentKey.INTENT_KEY_IMAGE_MAP, myMap);
        bundle.putString(IntentKey.INTENT_CLICK_VALUE, urlFromPosition);
        intent.putExtra("bundle", bundle);
        getContext().startActivity(intent);
    }

    public String getUrlFromPosition(int position) {
        MessageEntity item = getItem(position);
        String url = item.getContent().getMsg();
        return url;
    }

    @Override
    public int getViewTypeCount() {
        return type_max;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int itemViewType = getItemViewType(position);
        ViewHolder holder;
        if (convertView == null) {
            int layout = getItemLayout(itemViewType);
            if (layout == -1) {
                convertView = new TextView(getContext());
            } else {
                convertView = LayoutInflater.from(getContext()).inflate(layout, null);
            }
            holder = new ViewHolder();
            holder.findView(convertView);
            convertView.setId(R.id.id_content_message_holder);
            convertView.setTag(R.id.id_view_holder, holder);
        } else {
            holder = (ViewHolder) convertView.getTag(R.id.id_view_holder);
        }
        convertView.setTag(R.id.id_position, position);

        if (itemViewType == type_rec_voice) {
            holders_map.put(position, holder);
        }
        if (holder.ivAvatar != null) {
            holder.ivAvatar.setTag(R.id.id_position, position);
            holder.ivAvatar.setOnClickListener(this);
        }
        if (holder.recVoice != null) {
            holder.recVoice.setTag(R.id.id_position, position);
            holder.recVoice.setOnClickListener(this);
            holder.recVoice.setTag(R.id.id_view_holder, holder);
        } else if (holder.sendVoice != null) {
            holder.sendVoice.setTag(R.id.id_position, position);
            holder.sendVoice.setOnClickListener(this);
            holder.sendVoice.setTag(R.id.id_view_holder, holder);
        }
        if (holder.ivImage != null) {
            holder.ivImage.setTag(R.id.id_position, position);
            holder.ivImage.setOnClickListener(this);
        }
        if (holder.tvReSend != null) {
            holder.tvReSend.setTag(R.id.id_position, position);
            holder.tvReSend.setOnClickListener(this);
        }
        if (holder.attachView != null) {
            holder.attachView.setTag(R.id.id_position, position);
            holder.attachView.setOnClickListener(this);
        }
        if (holder.tvEventDesc != null) {
            holder.tvEventDesc.setTag(R.id.id_position, position);
            holder.tvEventDesc.setOnClickListener(this);
        }
        if (holder.tvRequirementDetail != null) {
            holder.tvRequirementDetail.setTag(R.id.id_position, position);
            holder.tvRequirementDetail.setOnClickListener(this);
        }
        if (holder.tvMessage != null) {
            holder.tvMessage.setTag(R.id.id_position, position);
            holder.tvMessage.setOnLongClickListener(this);
        }

        holder.bindView(position);
        return convertView;
    }

    public void joinContinuePlay() {
        if (isContinuePlaying) {
            continueVoiceList.add(list.size());
        }
    }

    class ViewHolder {

        private RoundedImageView ivAvatar;
        private TextView tvTime;
        private BubbleImageView ivImage;
        private LoadableVoiceView ivVoice;
        private ImageView ivIsPlay;
        private TextView tvVoiceDuration;
        private TextView tvMessage;
        private TextView tvIsRead;
        private ImageView ivSendProgress;
        private ImageView ivSendFail;
        private TextView tvReSend;
        private TextView tvEventDesc;
        private TextView tvQuestionDesc;
        private LinearLayout sendVoice;
        private LinearLayout recVoice;
        private View attachView;
        private ImageView ivAttachIcon;
        private TextView tvAttachName;
        private TextView tvAttachSize;
        private TextView tvAttachSource;
        private TextView tvRequirementTitle;
        private TextView tvRequirementContent;
        private View tvRequirementDetail;

        private void findView(View parentView) {
            ivAvatar = (RoundedImageView) parentView.findViewById(R.id.id_avatar);
            tvTime = (TextView) parentView.findViewById(R.id.id_time);
            ivImage = (BubbleImageView) parentView.findViewById(R.id.id_content_message_image);
            ivVoice = (LoadableVoiceView) parentView.findViewById(R.id.id_content_message_voice);
            ivIsPlay = (ImageView) parentView.findViewById(R.id.id_is_play);
            tvVoiceDuration = (TextView) parentView.findViewById(R.id.id_content_message_voice_duration);
            tvMessage = (TextView) parentView.findViewById(R.id.id_content_message_txt);
            tvIsRead = (TextView) parentView.findViewById(R.id.id_read);
            ivSendProgress = (ImageView) parentView.findViewById(R.id.id_refresh);
            ivSendFail = (ImageView) parentView.findViewById(R.id.id_fail_img);
            tvReSend = (TextView) parentView.findViewById(R.id.id_resend);
            tvEventDesc = (TextView) parentView.findViewById(R.id.id_event_desc);
            tvQuestionDesc = (TextView) parentView.findViewById(R.id.id_question_desc);
            sendVoice = (LinearLayout) parentView.findViewById(R.id.voice_send);
            recVoice = (LinearLayout) parentView.findViewById(R.id.voice_re);
            attachView = parentView.findViewById(R.id.attach_container);
            ivAttachIcon = (ImageView) parentView.findViewById(R.id.attach_icon);
            tvAttachName = (TextView) parentView.findViewById(R.id.attach_name);
            tvAttachSize = (TextView) parentView.findViewById(R.id.attach_size);
            tvAttachSource = (TextView) parentView.findViewById(R.id.attach_source);
            tvRequirementTitle = (TextView) parentView.findViewById(R.id.id_require_title);
            tvRequirementContent = (TextView) parentView.findViewById(R.id.id_require_content);
            tvRequirementDetail = parentView.findViewById(R.id.id_require_detail);
        }

        private void bindView(int position) {
            UserInfo userInfo = SysUtil.getUsrInfo();
            MessageEntity dataEntity = getItem(position);
            if (dataEntity == null) {
                hideAll();
                return;
            }
            if (ivAvatar != null) {
                if (dataEntity.getTo().equals(userInfo.getUsername()) && !TextUtils.isEmpty(avatarUrl)) {
                    Picasso.with(App.get()).load(avatarUrl)
                            .error(R.drawable.ic_avatar_default)
                            .resize(ScreenUtils.dip2px(40.0f), ScreenUtils.dip2px(40.0f))
                            .config(Bitmap.Config.RGB_565)
                            .into(ivAvatar);
                } else {
                    if (!TextUtils.isEmpty(userInfo.getHead())) {
                        Picasso.with(App.get()).load(userInfo.getHead())
                                .error(R.drawable.ic_avatar_default)
                                .resize(ScreenUtils.dip2px(40.0f), ScreenUtils.dip2px(40.0f))
                                .config(Bitmap.Config.RGB_565)
                                .into(ivAvatar);
                    } else {
                        Picasso.with(App.get()).load(R.drawable.ic_avatar_default)
                                .resize(ScreenUtils.dip2px(40.0f), ScreenUtils.dip2px(40.0f))
                                .config(Bitmap.Config.RGB_565)
                                .into(ivAvatar);
                    }
                }
            }
            if (tvTime != null) {
                tvTime.getBackground().setAlpha(150);
                if (position == 0) {
                    tvTime.setVisibility(View.VISIBLE);
                    tvTime.setText(TimeUtils.timeFormateShow(dataEntity.getDnjts(), 1));
                } else if (position > 0) {
                    MessageEntity lastEntity = getItem(position - 1);
                    if (lastEntity != null && (dataEntity.getDnjts() - lastEntity.getDnjts() > 2 * 60 * 1000)) {
                        tvTime.setVisibility(View.VISIBLE);
                        tvTime.setText(TimeUtils.timeFormateShow(dataEntity.getDnjts(), 1));
                    } else {
                        tvTime.setVisibility(View.GONE);
                    }
                } else {
                    tvTime.setVisibility(View.GONE);
                }
            }
            if (ivImage != null) {
                MessageEntity.ContentEntity content = dataEntity.getContent();
                try {
                    ivImage.setUrl(content);
                    ivImage.setProgress(dataEntity.getUploadProgress());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (ivVoice != null && tvVoiceDuration != null) {
                String voicePath = dataEntity.getContent().getUrl();
                int duration = dataEntity.getContent().getDuration();
                tvVoiceDuration.setText(duration + "''");
                tvVoiceDuration.setTag(voicePath);
                ivVoice.setTag(R.id.id_position, position);
                int width = DisplayUtil.getVoiceItemWidth(duration);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT);
                if (dataEntity.getTo().equals(userInfo.getUsername())) {
                    params.setMargins(10, 0, 0, 0);
                } else {
                    params.setMargins(0, 0, 1, 0);
                }
                if (sendVoice != null) {
                    sendVoice.setLayoutParams(params);
                }
                if (recVoice != null) {
                    recVoice.setLayoutParams(params);
                }
                if (dataEntity.getTo().equals(userInfo.getUsername())) {
                    ivVoice.setTag(R.id.is_send, false);
                    if (dataEntity.getIsDown() == 0) {
                        ivVoice.setVisibility(View.GONE);
                    } else {
                        ivVoice.setVisibility(View.VISIBLE);
                        if (dataEntity.isPlaying() && ivVoice.isPlaying()) {
                            ivVoice.playAnimation();
                        } else {
                            ivVoice.setImageResource(R.drawable.receive_voice3);
                        }
                    }
                } else {
                    ivVoice.setTag(R.id.is_send, true);
                    ivVoice.setVisibility(View.VISIBLE);
                    if (dataEntity.isPlaying() && ivVoice.isPlaying()) {
                        ivVoice.playAnimation();
                    } else {
                        ivVoice.setImageResource(R.drawable.send_voice3);
                    }
                }
            }
            if (ivIsPlay != null) {
                if (dataEntity.getIsPlay() == 0) {
                    ivIsPlay.setVisibility(View.VISIBLE);
                } else {
                    ivIsPlay.setVisibility(View.GONE);
                }
            }
            if (tvMessage != null) {
                SpannableString spannableString = MessageUtil.toSpannableString(dataEntity.getContent().getMsg(), ApplicationUtil.getApplicationContext(), null, false);
                tvMessage.setText(spannableString);
                tvMessage.setMovementMethod(LinkMovementMethod.getInstance());
                CharSequence text = tvMessage.getText();
                if (text instanceof Spannable) {
                    int end = text.length();
                    Spannable sp = (Spannable) tvMessage.getText();
                    URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);
                    for (URLSpan url : urls) {
                        MyURLSpan myURLSpan = new MyURLSpan(url.getURL());
                        spannableString.setSpan(myURLSpan, sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    tvMessage.setText(spannableString);
                }
            }
            if (tvIsRead != null) {
                if (dataEntity.getIsRead() == 1) {
                    if (dataEntity.getMessageId() == getMyLastMessageId()) {
                        tvIsRead.setVisibility(View.VISIBLE);
                    } else {
                        tvIsRead.setVisibility(View.GONE);
                    }
                } else {
                    tvIsRead.setVisibility(View.GONE);
                }
            }
            if (ivSendProgress != null) {
                if (dataEntity.getMsgState() == 0) {
                    ivSendProgress.clearAnimation();
                    ivSendProgress.startAnimation(sendAnim);
                    ivSendProgress.setVisibility(View.VISIBLE);
                } else {
                    ivSendProgress.clearAnimation();
                    ivSendProgress.setVisibility(View.INVISIBLE);
                }
            }
            if (ivSendFail != null) {
                if (dataEntity.getMsgState() == 2) {
                    ivSendFail.setVisibility(View.VISIBLE);
                } else {
                    ivSendFail.setVisibility(View.GONE);
                }
            }
            if (tvReSend != null) {
                if (dataEntity.getMsgState() == 2) {
                    tvReSend.setVisibility(View.VISIBLE);
                } else {
                    tvReSend.setVisibility(View.GONE);
                }
            }
            if (tvEventDesc != null) {
                String eventType = dataEntity.getContent().getType();
                if (eventType.equals("order:requested")) {
                    if (dataEntity.getFrom().equals(userInfo.getUsername())) {
                        tvEventDesc.setText(R.string.order_request_normal);
                    } else {
                        tvEventDesc.setText(R.string.order_request_profession);
                    }
                } else if (eventType.equals("order:confirmed")) {
                    if (dataEntity.getFrom().equals(userInfo.getUsername())) {
                        tvEventDesc.setText(R.string.order_confirm_profession);
                    } else {
                        tvEventDesc.setText(R.string.order_confirm_normal);
                    }
                } else if (eventType.equals("order:paid")) {
                    if (dataEntity.getFrom().equals(userInfo.getUsername())) {
                        tvEventDesc.setText(R.string.order_paid_normal);
                    } else {
                        tvEventDesc.setText(R.string.order_paid_profession);
                    }
                } else if (eventType.equals("order:cancelled")) {
                    if (dataEntity.getFrom().equals(userInfo.getUsername())) {
                        tvEventDesc.setText(R.string.order_cancel_normal);
                    } else {
                        tvEventDesc.setText(R.string.order_cancel_profession);
                    }
                } else if (eventType.equals("order:rejected")) {
                    if (dataEntity.getFrom().equals(userInfo.getUsername())) {
                        tvEventDesc.setText(R.string.order_reject_profession);
                    } else {
                        tvEventDesc.setText(R.string.order_reject_normal);
                    }
                } else if (eventType.equals("order:resolved")) {
                    if (dataEntity.getFrom().equals(userInfo.getUsername())) {
                        tvEventDesc.setText(R.string.order_resolve_profession);
                    } else {
                        tvEventDesc.setText(R.string.order_resolve_normal);
                    }
                } else if (eventType.equals("order:evaluated")) {
                    if (dataEntity.getFrom().equals(userInfo.getUsername())) {
                        tvEventDesc.setText(R.string.order_evaluate_normal);
                    } else {
                        tvEventDesc.setText(R.string.order_evaluate_profession);
                    }
                } else if (eventType.equals("order:replied")) {
                    if (dataEntity.getFrom().equals(userInfo.getUsername())) {
                        tvEventDesc.setText(R.string.order_reply_profession);
                    } else {
                        tvEventDesc.setText(R.string.order_reply_normal);
                    }
                }
            }
            if (tvQuestionDesc != null) {
                String questionContent = dataEntity.getContent().getMessage().getQuestion_content();
                if (!TextUtils.isEmpty(questionContent)) {
                    tvQuestionDesc.setText(questionContent);
                }
            }
            if (ivAttachIcon != null) {
                ivAttachIcon.setImageResource(FileUtil.getFileType(dataEntity.getContent().getFilename(), false));
            }
            if (tvAttachName != null) {
                tvAttachName.setText(dataEntity.getContent().getFilename());
            }
            if (tvAttachSize != null) {
                tvAttachSize.setText(FileUtil.formatFileSize(dataEntity.getContent().getSize()));
            }
            if (tvAttachSource != null) {
                tvAttachSource.setText(dataEntity.getContent().getSourcePlatform());
            }
            if (tvRequirementTitle != null) {
                String name;
                if (SysUtil.getPref(PrefKeyConst.PREF_USER_NAME).equals("eric")) {
                    name = messageFragment.name;
                } else {
                    name = TextUtils.isEmpty(SysUtil.getPref(PrefKeyConst.PREF_NAME)) ? SysUtil.getPref(PrefKeyConst.PREF_USER_NAME) : SysUtil.getPref(PrefKeyConst.PREF_NAME);
                }
                tvRequirementTitle.setText(name + " 您好：");
            }
            if (tvRequirementContent != null) {
                tvRequirementContent.setText(dataEntity.getContent().getNotice());
            }
        }

        private void hideAll() {
            if (ivAvatar != null) {
                ivAvatar.setImageResource(R.drawable.ic_avatar_default);
            }
            if (tvTime != null) {
                tvTime.setText("");
            }
            if (ivImage != null) {
                ivImage.setImageDrawable(null);
            }
            if (ivVoice != null && tvVoiceDuration != null) {
                ivVoice.setVisibility(View.GONE);
                tvVoiceDuration.setText("");
            }
            if (ivIsPlay != null) {
                ivIsPlay.setVisibility(View.GONE);
            }
            if (tvMessage != null) {
                tvMessage.setText("");
            }
        }
    }

    private int getItemLayout(int type) {
        switch (type) {
            case type_send_text:
                return R.layout.list_item_message_text_send;
            case type_rec_text:
                return R.layout.list_item_message_text_rec;
            case type_send_img:
                return R.layout.list_item_message_img_send;
            case type_rec_img:
                return R.layout.list_item_message_img_rec;
            case type_send_voice:
                return R.layout.list_item_message_voice_send;
            case type_rec_voice:
                return R.layout.list_item_message_voice_rec;
            case type_rec_question:
                return R.layout.list_item_message_question_rec;
            case type_send_question:
                return R.layout.list_item_message_question_send;
            case type_event:
                return R.layout.list_item_message_event;
            case type_rec_attach:
                return R.layout.list_item_message_attach_rec;
            case type_send_attach:
                return R.layout.list_item_message_attach_send;
            case type_rec_requirement:
                return R.layout.list_item_message_requirement_rec;
            case type_send_requirement:
                return R.layout.list_item_message_requirement_send;
            default:
                return -1;
        }
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag(R.id.id_position);
        MessageEntity item = getItem(position);
        if (item == null) {
            return;
        }
        switch (v.getId()) {
            case R.id.id_content_message_image:
                doImageClick(position);
                break;
            case R.id.voice_re:
                if (continueVoiceList == null) {
                    continueVoiceList = new ArrayList<>();
                }

                if (list.get(position).getIsPlay() == 0 && position < (list.size() - 1)) {
                    continueVoiceList.clear();
                    for (int i = position; i < list.size(); i++) {
                        MessageEntity messageEntity = list.get(i);
                        if (messageEntity.getIsPlay() == 0 && messageEntity.getContent().getCtype() == IMMsgFlag.MSG_CHAT_VOICE) {
                            continueVoiceList.add(i);
                        }
                    }
                } else {
                    continueVoiceList.clear();
                    isContinuePlaying = false;
                }

                if (list.get(position).getIsPlay() == 0 && continueVoiceList.size() > 0) {
                    isContinuePlaying = true;
                    startRollPlayVoice();
                } else {
                    clickView = v;
                    doVoiceClick(v, item);
                }
                break;
            case R.id.voice_send:
                clickView = v;
                doVoiceClick(v, item);
                break;
            case R.id.id_avatar:
                UserInfo userInfo = SysUtil.getUsrInfo();
                if (item.getFrom().equals(userInfo.getUsername())) {
                    if (userInfo.getIdentity() == 2) {
                        Intent intent = new Intent(getContext(), ActExpertHome.class);
                        intent.putExtra(IntentKey.INTENT_KEY_EXPERT_ID, userInfo.getUserId());
                        getContext().startActivity(intent);
                    } else {
                        Intent intent = new Intent(getContext(), ActUserHome.class);
                        intent.putExtra(IntentKey.INTENT_KEY_CHAT_USER_ID, userInfo.getUserId());
                        getContext().startActivity(intent);
                    }
                } else {
                    if (otherSideEntity == 2) {
                        Intent intent = new Intent(getContext(), ActExpertHome.class);
                        intent.putExtra(IntentKey.INTENT_KEY_EXPERT_ID, otherSideId);
                        getContext().startActivity(intent);
                    } else {
                        Intent intent = new Intent(getContext(), ActUserHome.class);
                        intent.putExtra(IntentKey.INTENT_KEY_CHAT_USER_ID, otherSideId);
                        getContext().startActivity(intent);
                    }
                }
                break;
            case R.id.id_resend:
                ContentValues contentValues = new ContentValues();
                contentValues.put(DatabaseConstants.MessageColumn.COLUMN_NAME_MSG_STATE, 0);
                contentValues.put(DatabaseConstants.MessageColumn.COLUMN_NAME_MSG_STAMP, String.valueOf(System.currentTimeMillis()));
                List<BaseDatabaseExecutor.SQLSentence> updateSql = new ArrayList<>();
                updateSql.add(new BaseDatabaseExecutor.SQLSentence(DatabaseConstants.MessageColumn.COLUMN_NAME_MSG_CODE + "=? AND " + DatabaseConstants.MessageColumn.COLUMN_NAME_MSG_STATE + "=?", new String[]{item.getCode(), "2"}, contentValues));
                DatabaseUpdateExecutor updateExecutor = new DatabaseUpdateExecutor(DatabaseConstants.Tables.TABLE_NAME_MESSAGE, updateSql, DaniujiaUris.URI_MESSAGE);
                updateExecutor.execute(updateExecutor);
                MqttUtils.sendChatMsg_newVersion(item);
                break;
            case R.id.attach_container:
                Intent intent = new Intent(getContext(), ActFilePreView.class);
                intent.putExtra(IntentKey.INTENT_KEY_ATTACH_ITEM, item.getContent());
                getContext().startActivity(intent);

//                LogUtil.d("ZLOVE", "url---" + item.getContent().getUrl());
//                Intent intent = new Intent(getContext(), WebActivity.class);
//                intent.putExtra(ExtraConst.EXTRA_WEB_TITLE, "文件预览");
//                intent.putExtra(ExtraConst.EXTRA_WEB_URL, item.getContent().getUrl());
//                getContext().startActivity(intent);
                break;

            case R.id.id_event_desc:
                if (messageFragment != null) {
                    messageFragment.performOrderHeadContainer();
                }
                break;

            case R.id.id_require_detail:
                Intent intent1 = new Intent(getContext(), ActRequirementDetail.class);
                intent1.putExtra(IntentKey.INTENT_KEY_DEMAND_ID, item.getContent().getDemandId());
                getContext().startActivity(intent1);
                break;
        }
    }

    private void prepareContinuePlaying(int position) {
        if (position >= list.size()) {
            return;
        }
        continueVoiceList = new ArrayList<>();
        isContinuePlaying = true;
        for (int i = position; i < list.size(); i++) {
            MessageEntity messageEntity = list.get(i);
            if (messageEntity.getContent().getCtype() == IMMsgFlag.MSG_CHAT_VOICE) {
                continueVoiceList.add(i);
            }
        }
        startRollPlayVoice();
    }

    private void doVoiceClick(View v, MessageEntity messageEntity) {
        ViewHolder viewHolder = (ViewHolder) v.getTag(R.id.id_view_holder);
        if (viewHolder != null) {
            String link = (String) viewHolder.tvVoiceDuration.getTag();
            boolean isSend = (boolean) viewHolder.ivVoice.getTag(R.id.is_send);
            viewHolder.ivVoice.setActivity(activity);
            resetPlayingStatus();
            viewHolder.ivVoice.setMessageEntity(messageEntity);
            viewHolder.ivVoice.setUrl(link, isSend);
            if (viewHolder.ivIsPlay != null) {
                String url = messageEntity.getContent().getUrl();
                if (TextUtils.isEmpty(url)) {
                    return;
                }
                String sql = "UPDATE " + DatabaseConstants.Tables.TABLE_NAME_MESSAGE + " set " + DatabaseConstants.MessageColumn.COLUMN_NAME_VOICE_IS_PLAY + " = 1 where " + DatabaseConstants.MessageColumn.COLUMN_NAME_URL + " = '" + url + "';";
                DatabaseManager.getInstance().execSQLAction(sql);
                messageEntity.setIsPlay(1);
                if (messageEntity.getIsDown() == 0) {
                    String sql2 = "UPDATE " + DatabaseConstants.Tables.TABLE_NAME_MESSAGE + " set " + DatabaseConstants.MessageColumn.COLUMN_NAME_IS_DOWN + " = 1 where " + DatabaseConstants.MessageColumn.COLUMN_NAME_URL + " = '" + url + "';";
                    DatabaseManager.getInstance().execSQLAction(sql2);
                    messageEntity.setIsDown(1);
                }
                viewHolder.ivIsPlay.setVisibility(View.GONE);
            }
        }
    }

    private void startRollPlayVoice() {
        if (ListUtils.isEmpty(continueVoiceList)) {
            continueVoiceList = null;
            isContinuePlaying = false;
            return;
        }
        MessageEntity entity = list.get(continueVoiceList.get(DICTATE_VOICE_PLAY));
        String url = entity.getContent().getUrl();
        if (TextUtils.isEmpty(url)) {
            return;
        }

        String sql = "UPDATE " + DatabaseConstants.Tables.TABLE_NAME_MESSAGE + " set " + DatabaseConstants.MessageColumn.COLUMN_NAME_VOICE_IS_PLAY + " = 1 where " + DatabaseConstants.MessageColumn.COLUMN_NAME_URL + " = '" + url + "';";
        DatabaseManager.getInstance().execSQLAction(sql);
        entity.setIsPlay(1);

        int positionWithTag = continueVoiceList.get(DICTATE_VOICE_PLAY);
        ViewHolder mHolder = holders_map.get(positionWithTag);
        if (mHolder != null) {
            resetPlayingStatus();
            mHolder.ivVoice.setMessageEntity(entity);
            mHolder.ivVoice.setUrl(url, false);
            if (mHolder.ivIsPlay != null) {
                mHolder.ivIsPlay.setVisibility(View.GONE);
            }
            continueVoiceList.remove(DICTATE_VOICE_PLAY);
        }
    }

    private long getMyLastMessageId() {
        long msgId = 0;
        if (ListUtils.isEmpty(list)) {
            return 0;
        }
        int size = list.size();
        for (int i = size - 1; i >= 0; i--) {
            MessageEntity entity = list.get(i);
            if (entity.getFrom().equals(SysUtil.getPref(PrefKeyConst.PREF_USER_NAME)) && entity.getIsRead() == 1) {
                msgId = entity.getMessageId();
                break;
            }
        }
        return msgId;
    }

    private void resetPlayingStatus() {
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).isPlaying()) {
                    list.get(i).setPlaying(false);
                    break;
                }

            }
        }

    }

    private PopupWindow popupWindow;
    TextView tvCopy;

    @Override
    public boolean onLongClick(View view) {
        int position = (int) view.getTag(R.id.id_position);
        final MessageEntity item = getItem(position);
        if (item == null) {
            return false;
        }
        if (view.getId() == R.id.id_content_message_txt) {
            if (null == popupWindow) {
                View popView = LayoutInflater.from(getContext()).inflate(R.layout.layout_pop_txt_copy, null);
                tvCopy = (TextView) popView.findViewById(R.id.id_copy);
                popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setAnimationStyle(R.style.CenterScaleAnimation);
                popupWindow.setOutsideTouchable(true);
                popupWindow.setBackgroundDrawable(new BitmapDrawable());
            }
            tvCopy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                    ClipboardManager cm = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(item.getContent().getMsg());
                }
            });

            if (popupWindow.isShowing())
                popupWindow.dismiss();

            //第一次显示控件的时候宽高会为0
            int deleteHeight = tvCopy.getHeight() == 0 ? 145 : tvCopy.getHeight();
            int deleteWidth = tvCopy.getWidth() == 0 ? 212 : tvCopy.getWidth();

            popupWindow.showAsDropDown(view, (view.getWidth() - deleteWidth) / 2, -view.getHeight() - deleteHeight);
        }
        return true;
    }

    private class MyURLSpan extends ClickableSpan {

        private String url;

        public MyURLSpan(String url) {
            this.url = url;
        }

        @Override
        public void onClick(View arg0) {
            Intent intent = new Intent(getContext(), WebActivity.class);
            intent.putExtra(ExtraConst.EXTRA_WEB_TITLE, "");
            intent.putExtra(ExtraConst.EXTRA_WEB_URL, url);
            getContext().startActivity(intent);
        }
    }
}
