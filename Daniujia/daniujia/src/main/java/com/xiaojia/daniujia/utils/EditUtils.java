package com.xiaojia.daniujia.utils;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/5/3.
 */
public class EditUtils {
    private final int DEF_COUNT = 500;
    private int textCount = DEF_COUNT;
    private String tipContent = "";

    /**
     * 字数小于等于0的时，为默认值
     * @param textCount
     */
    public EditUtils(int textCount) {
        if (textCount > 0) {
            this.textCount = textCount;
        }
    }

    public EditUtils(int textCount, String tipContent) {
        if (textCount > 0) {
            this.textCount = textCount;
        }
        this.tipContent = tipContent;
    }

    public void init(final EditText et_question_content, final TextView textInput) {

        if (et_question_content == null) {
            return;
        }
        et_question_content.addTextChangedListener(new TextWatcher() {
            private int temp;
            private int selectionStart;
            private int selectionEnd;
            boolean isShow = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                temp = s.length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                selectionStart = et_question_content.getSelectionStart();
                selectionEnd = et_question_content.getSelectionEnd();
                if (s.length() > textCount) {
                    if (!isShow) {
                        if (!TextUtils.isEmpty(tipContent)) {
                            UIUtil.showShortToast(tipContent);
                        } else {
                            UIUtil.showShortToast("字数已超过"+textCount+"字！");
                        }
                        isShow = true;
                    }
                    if (selectionEnd > textCount) {
                        s.delete(textCount, selectionEnd);
                        et_question_content.setText(s);
                        et_question_content.setSelection(textCount);
                        if (textInput != null) {
                            textInput.setText("" + textCount);
                        }
                    }
                } else {
                    if (textInput != null) {
                        textInput.setText("" + s.length());
                    }
                }
            }
        });
    }

}
