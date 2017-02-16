package com.xiaojia.daniujia.utils;

import android.graphics.Color;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import com.xiaojia.daniujia.R;

import org.xml.sax.XMLReader;

import java.util.HashMap;

/**
 * Created by ZLOVE on 2016/9/8.
 */
public class SelfTagHandler implements Html.TagHandler  {

    private int startIndex = 0;
    private int stopIndex = 0;
    private String mapKey;
    private HashMap<String, String> map;
    private ClickLinkListener linkListener;

    public SelfTagHandler(ClickLinkListener linkListener) {
        map = new HashMap<>();
        this.linkListener = linkListener;
    }

    @Override
    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
        if (tag.toLowerCase().equals("desc")) {
            if (opening) {
                startDesc(tag, output, xmlReader);
            } else {
                endDesc(tag, output, xmlReader);
            }
        } else if(tag.toLowerCase().equals("url")) {
            if(opening) {//获取当前标签的内容开始位置
                startUrl(output);
            } else {
                endOutPut(output);
            }
        }
    }

    public void startDesc(String tag, Editable output, XMLReader xmlReader) {
        startIndex = output.length();
    }

    public void endDesc(String tag, Editable output, XMLReader xmlReader) {
        stopIndex = output.length();
        mapKey = output.subSequence(startIndex, stopIndex).toString();
        output.setSpan(new GameSpan(mapKey), startIndex, stopIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    public void startUrl(Editable output) {
        startIndex = output.length();
    }

    public void endOutPut(Editable output) {
        stopIndex = output.length();
        String content = output.subSequence(startIndex, stopIndex).toString();
        SpannableString spanStr = new SpannableString(content);
        spanStr.setSpan(new ForegroundColorSpan(Color.GREEN), 0, content.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        output.replace(startIndex, stopIndex, "");
        map.put(mapKey, content);
    }

    private class GameSpan extends ClickableSpan implements View.OnClickListener {
        private String key;
        public GameSpan(String key) {
            this.key = key;
        }

        @Override
        public void onClick(View v) {
            // 跳转某页面
            if (linkListener != null) {
                linkListener.clickLink(map.get(key));
            }
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(ApplicationUtil.getApplicationContext().getResources().getColor(R.color.blue_text));
            ds.setUnderlineText(true);
        }
    }

    public interface ClickLinkListener {
        void clickLink(String url);
    }
}
