package com.xiaojia.daniujia.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;

import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.ui.views.emotion.Emotion;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ZLOVE on 2016/4/11.
 */
public class MessageUtil {
    private static Pattern emoPattern = Pattern.compile("\\[([^\\[|^\\]]+)\\]");
    private static Pattern urlPattern = Pattern.compile("^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+$");

    public static SpannableString toSpannableString(CharSequence content, Context context, SpannableString spanString, boolean small) {
        if (TextUtils.isEmpty(content)) {
            return new SpannableString("");
        }
        if (spanString == null) {
            spanString = new SpannableString(content);
        }

        replaceFaceTextToEmo(context, content, spanString, small);
        return spanString;
    }

    private static void replaceFaceTextToEmo(Context context, CharSequence content, SpannableString ss, boolean small) {
        Matcher matcher = emoPattern.matcher(content);
        while (matcher.find()) {
            Emotion emo = new Emotion(null, matcher.group(), -1);
            int index = EmotionUtil.emotionSearchList.indexOf(emo);
            if (index != -1) {
                emo = EmotionUtil.emotionSearchList.get(index);
            }
            /*else {
                index = EmotionUtil.gifEmotionSearchList.indexOf(emo);
                if (index != -1) {
                    emo = EmotionUtil.gifEmotionSearchList.get(index);
                }
            }*/

            if (emo.getRes() != 0 && emo.getRes() != -1) {
                ss.setSpan(getImageSpaceInText(context, emo.getRes(), small), matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    public static ImageSpan getImageSpaceInText(Context context, int drawableRes, boolean small) {
        return getImageSpaceInText(context, context.getResources().getDrawable(drawableRes), small);
    }

    public static ImageSpan getImageSpaceInText(Context context, Drawable drawable, boolean small) {
        int hw = 0;
        if (small) {
            hw = context.getResources().getDimensionPixelSize(R.dimen.in_text_emoticons_small);
        } else {
            hw = context.getResources().getDimensionPixelSize(R.dimen.in_text_emoticons);
        }
        drawable.setBounds(0, 0, hw, hw);
        return new ImageSpan(drawable);
    }
}
