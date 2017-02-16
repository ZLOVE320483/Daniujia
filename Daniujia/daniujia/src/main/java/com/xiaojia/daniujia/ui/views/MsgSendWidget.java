package com.xiaojia.daniujia.ui.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.base.App;
import com.xiaojia.daniujia.ui.views.audio.AudioRecorder;
import com.xiaojia.daniujia.ui.views.audio.RecordButton;
import com.xiaojia.daniujia.ui.views.emotion.Emotion;
import com.xiaojia.daniujia.ui.views.emotion.MMFlipper;
import com.xiaojia.daniujia.utils.BitmapUtil;
import com.xiaojia.daniujia.utils.EmotionUtil;
import com.xiaojia.daniujia.utils.ListUtils;
import com.xiaojia.daniujia.utils.LogUtil;
import com.xiaojia.daniujia.utils.MessageUtil;
import com.xiaojia.daniujia.utils.NetUtils;
import com.xiaojia.daniujia.utils.UIUtil;

import java.io.File;
import java.util.List;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * Created by ZLOVE on 2016/4/11
 */
public class MsgSendWidget extends RelativeLayout implements View.OnClickListener, MMFlipper.OnFaceSelectedListener, RecordButton.RecordListener {

    private static final String LOG_TAG = MsgSendWidget.class.getSimpleName();

    private boolean initDone = false;

    private ImageButton iBEditMode;
    private ImageButton iBSendEmo;
    private ImageButton iBSendImg;

    private RecordButton vVoiceRecord;
    private EditText etContent;
    private MsgSendListener msgSendListener;

    private View vAttachGroup;
    private View vImgChooseContainer;

    private MMFlipper mmFlipper;

    private View vSend;

    private boolean isTextEdit = true;

    private Fragment fragment;

    public MsgSendWidget(Context context) {
        super(context);
        init();
    }

    public MsgSendWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MsgSendWidget(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        if (initDone) {
            return;
        }
        initDone = true;
        LayoutInflater.from(getContext()).inflate(R.layout.layout_widget_msgsend, this);

        iBEditMode = (ImageButton) findViewById(R.id.id_msg_send_mode);
        iBEditMode.setOnClickListener(this);
        iBSendEmo = (ImageButton) findViewById(R.id.id_msg_send_emo);
        iBSendEmo.setOnClickListener(this);
        iBSendImg = (ImageButton) findViewById(R.id.id_msg_send_img);
        iBSendImg.setOnClickListener(this);
        vVoiceRecord = (RecordButton) findViewById(R.id.id_msg_send_voice);
        vVoiceRecord.setAudioRecord(new AudioRecorder());
        vVoiceRecord.setRecordListener(this);
        etContent = (EditText) findViewById(R.id.id_msg_send_text);
        etContent.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        etContent.setGravity(Gravity.TOP);
        etContent.setSingleLine(false);
        etContent.setMaxLines(5);
        etContent.setHorizontallyScrolling(false);
        vSend = findViewById(R.id.id_msg_send);
        vSend.setOnClickListener(this);
        vSend.setVisibility(View.GONE);

        vAttachGroup = findViewById(R.id.id_attach_group);
        vAttachGroup.setVisibility(View.GONE);

        vImgChooseContainer = findViewById(R.id.id_msg_img_container);
        View vImgTake = findViewById(R.id.id_img_take);
        vImgTake.setOnClickListener(this);
        View vImgPic = findViewById(R.id.id_img_pic);
        vImgPic.setOnClickListener(this);

        mmFlipper = (MMFlipper) findViewById(R.id.id_attach_choose_emo);
        mmFlipper.setEmoSelectedListener(this);

        etContent.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_UP == event.getAction()) {
                    togAttachGroup(View.GONE);
                    togMMFliper(View.GONE);
                    togEdit();
                }
                return false;
            }
        });

        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String tmpTxt = s.toString().trim();
                if (TextUtils.isEmpty(tmpTxt)) {
                    vSend.setVisibility(GONE);
                    iBSendImg.setVisibility(VISIBLE);
                } else {
                    vSend.setVisibility(VISIBLE);
                    iBSendImg.setVisibility(GONE);
                }
            }
        });
    }

    /**
     * @param outState
     */
    public void onSaveInstanceState(Bundle outState) {
    }

    /**
     * @param
     */
    public void setCameraPhoto(File cameraPhoto) {

    }

    /**
     * @param
     */
    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public void onDestory() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.id_msg_send_mode) {
            vAttachGroup.setVisibility(View.GONE);
            if (!isTextEdit) {
                togEdit();
                etContent.requestFocus();
                UIUtil.showKeyboard((Activity) getContext(), etContent);
            } else {
                togVoice();
                UIUtil.hideKeyboard((Activity) getContext());
            }
            togMMFliper(View.GONE);
        } else if (v.getId() == R.id.id_msg_send) {
            onClickSend();
        } else if (v.getId() == R.id.id_img_pic) {
            togAttachGroup(View.GONE);
            App.initNormalConfig();
            GalleryFinal.openGalleryMuti(1111, 3, mOnHandlerResultCallback);
        } else if (v.getId() == R.id.id_img_take) {
            App.initNormalConfig();
            GalleryFinal.openCamera(1111, mOnHandlerResultCallback);
        } else if (v.getId() == R.id.id_msg_send_emo) {
            UIUtil.hideKeyboard((Activity) getContext());
            vImgChooseContainer.setVisibility(View.GONE);
            mmFlipper.initControl();
            togMMFliper(View.VISIBLE);
            if (!isTextEdit) {
                togEdit();
            }
            if(msgSendListener != null){
                msgSendListener.onEmotionClick();
            }
        } else if (v.getId() == R.id.id_msg_send_img) {
            togAttachGroup(View.VISIBLE);
            togMMFliper(View.GONE);
            UIUtil.hideKeyboard((Activity) getContext());
        }
    }

    private GalleryFinal.OnHandlerResultCallback mOnHandlerResultCallback = new GalleryFinal.OnHandlerResultCallback() {
        @Override
        public void onHandlerSuccess(int requestCode, List<PhotoInfo> resultList) {
            if (!ListUtils.isEmpty(resultList)) {
                for (PhotoInfo info : resultList) {
                    String filePath = info.getPhotoPath();
                    final File file = new File(filePath);
                    if (!file.exists() || file.length() == 0) {
                        return;
                    }
                    File compressedImgFile = BitmapUtil.getCompressedImgFile(file);
                    Bitmap bitmapFromFile = BitmapUtil.getBitmapFromFile(compressedImgFile);
                    if (bitmapFromFile == null) {
                        msgSendListener.onSendImage(compressedImgFile, 200, 200);
                    } else {
                        msgSendListener.onSendImage(compressedImgFile, bitmapFromFile.getWidth(), bitmapFromFile.getHeight());
                    }
                }
                togAttachGroup(View.GONE);
            }
        }

        @Override
        public void onHandlerFailure(int requestCode, String errorMsg) {
        }
    };


    private void togAttachGroup(int visibility) {
        vAttachGroup.setVisibility(visibility);
        vImgChooseContainer.setVisibility(visibility);
    }

    private void togMMFliper(int visibility) {
        mmFlipper.setVisibility(visibility);
    }

    private void togEdit() {
        isTextEdit = true;
        iBEditMode.setImageResource(R.drawable.ic_msg_voice);
        vVoiceRecord.setVisibility(View.GONE);
        etContent.setVisibility(View.VISIBLE);
    }

    private void togVoice() {
        isTextEdit = false;
        iBEditMode.setImageResource(R.drawable.ic_msg_txt);
        vVoiceRecord.setVisibility(View.VISIBLE);
        etContent.setVisibility(View.GONE);
    }

    private void onClickSend() {
        if (msgSendListener != null) {
            msgSendListener.onSendText(etContent.getText().toString());
        }
        if (NetUtils.isNetAvailable(getContext())) {
            etContent.setText("");
        }
    }

    public interface MsgSendListener {

        void onSendText(String text);

        void onSendVoice(String voiceFilePath, int duration);

        void onSendImage(File file, int width, int height);

        void onEmotionClick();
    }

    /**
     * @return
     */
    public MsgSendListener getMsgSendListener() {
        return msgSendListener;
    }

    /**
     * @param
     */
    public void setMsgSendListener(MsgSendListener msgSendListener) {
        this.msgSendListener = msgSendListener;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onFaceSelected(Emotion faceObject, boolean gifEmo) {
        LogUtil.d(LOG_TAG, "onFaceSelected, desc:" + faceObject.getDesc() + ", face:" + faceObject.getFace());
        if (gifEmo) {
            if (msgSendListener != null) {
                msgSendListener.onSendText(faceObject.getDesc());
            }
        } else {
            SpannableStringBuilder spannable = new SpannableStringBuilder(faceObject.getDesc());

            ImageSpan faceImageSpan = MessageUtil.getImageSpaceInText(getContext(),
                    getContext().getResources().getDrawable(faceObject.getRes()).getConstantState().newDrawable(), true);
            spannable.setSpan(faceImageSpan, 0, faceObject.getDesc().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            int start = etContent.getSelectionStart();
            int end = etContent.getSelectionEnd();
            etContent.getText().replace(Math.min(start, end), Math.max(start, end), spannable, 0, spannable.length());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDeleteClicked() {
        int currentSelect = etContent.getSelectionEnd();
        String currentText = etContent.getText().toString();
        if (currentText.length() == 0) {
            return;
        }

        String text = currentText.substring(0, currentSelect);
        String reminText = currentText.substring(currentSelect, etContent.length());

        int index = text.lastIndexOf("]");
        int indexPre = text.lastIndexOf("[");
        if (index > indexPre && index == text.length() - 1) {
            String emoStr = text.substring(indexPre, index + 1);
            Emotion emo = new Emotion(null, emoStr, -1);
            if (EmotionUtil.emotionSearchList.indexOf(emo) != -1) {
                reminText = text.substring(0, indexPre) + reminText;
                currentSelect = indexPre;
            } else {
                reminText = text.substring(0, text.length() - 1) + reminText;
                currentSelect = currentSelect - 1;
            }
        } else {
            reminText = text.substring(0, text.length() - 1) + reminText;
            currentSelect = currentSelect - 1;
        }
        etContent.setText(MessageUtil.toSpannableString(reminText, getContext(), null, true));
        etContent.setSelection(currentSelect);
    }

    /**
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO
    }

    @Override
    public void recordEnd(String filePath, int duration) {
        getMsgSendListener().onSendVoice(filePath, duration);
    }

    public void setEditable(boolean edit) {
        etContent.setEnabled(edit);
    }

    public void restore() {
        if (isTextEdit) {
            togAttachGroup(View.GONE);
            togMMFliper(View.GONE);
            togEdit();
        }
    }
}
