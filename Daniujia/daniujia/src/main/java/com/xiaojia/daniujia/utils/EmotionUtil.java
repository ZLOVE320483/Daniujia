package com.xiaojia.daniujia.utils;

import android.content.res.Resources;

import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.ui.views.emotion.Emotion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ZLOVE on 2016/4/11.
 */
public class EmotionUtil {

    private static String[] emoNormalDesc;
    private static int[] resId;

    public static Map<String, Emotion> emotionSearchMap = new HashMap<>();

    public static List<Emotion> emotionSearchList = new ArrayList<>();

    static {
        emoNormalDesc = ApplicationUtil.getApplicationContext().getResources().getStringArray(R.array.emo_noral);
        resId = new int[emoNormalDesc.length];
        Resources res = ApplicationUtil.getApplicationContext().getResources();
        for (int i = 0; i < emoNormalDesc.length; i++) {
            resId[i] = res.getIdentifier("face" + i,"raw",ApplicationUtil.getApplicationContext().getPackageName());
        }

        for (int i = 0; i < emoNormalDesc.length; i++) {
            if (resId[i] != 0) {
                emotionSearchMap.put("" + i, new Emotion("" + i, emoNormalDesc[i], resId[i]));
            }
        }

        /*暂时存放，以备不时之需
        emotionSearchMap.put("0", new Emotion("0", emoNormalDesc[0], R.raw.face0));
        emotionSearchMap.put("1", new Emotion("1", emoNormalDesc[1], R.raw.face1));
        emotionSearchMap.put("2", new Emotion("2", emoNormalDesc[2], R.raw.face2));
        emotionSearchMap.put("3", new Emotion("3", emoNormalDesc[3], R.raw.face3));
        emotionSearchMap.put("4", new Emotion("4", emoNormalDesc[4], R.raw.face4));
        emotionSearchMap.put("5", new Emotion("5", emoNormalDesc[5], R.raw.face5));
        emotionSearchMap.put("6", new Emotion("6", emoNormalDesc[6], R.raw.face6));
        emotionSearchMap.put("7", new Emotion("7", emoNormalDesc[7], R.raw.face7));
        emotionSearchMap.put("8", new Emotion("8", emoNormalDesc[8], R.raw.face8));
        emotionSearchMap.put("9", new Emotion("9", emoNormalDesc[9], R.raw.face9));

        emotionSearchMap.put("10", new Emotion("10", emoNormalDesc[10], R.raw.face10));
        emotionSearchMap.put("11", new Emotion("11", emoNormalDesc[11], R.raw.face11));
        emotionSearchMap.put("12", new Emotion("12", emoNormalDesc[12], R.raw.face12));
        emotionSearchMap.put("13", new Emotion("13", emoNormalDesc[13], R.raw.face13));
        emotionSearchMap.put("14", new Emotion("14", emoNormalDesc[14], R.raw.face14));
        emotionSearchMap.put("15", new Emotion("15", emoNormalDesc[15], R.raw.face15));
        emotionSearchMap.put("16", new Emotion("16", emoNormalDesc[16], R.raw.face16));
        emotionSearchMap.put("17", new Emotion("17", emoNormalDesc[17], R.raw.face17));
        emotionSearchMap.put("18", new Emotion("18", emoNormalDesc[18], R.raw.face18));
        emotionSearchMap.put("19", new Emotion("19", emoNormalDesc[19], R.raw.face19));

        emotionSearchMap.put("20", new Emotion("20", emoNormalDesc[20], R.raw.face20));
        emotionSearchMap.put("21", new Emotion("21", emoNormalDesc[21], R.raw.face21));
        emotionSearchMap.put("22", new Emotion("22", emoNormalDesc[22], R.raw.face22));
        emotionSearchMap.put("23", new Emotion("23", emoNormalDesc[23], R.raw.face23));
        emotionSearchMap.put("24", new Emotion("24", emoNormalDesc[24], R.raw.face24));
        emotionSearchMap.put("25", new Emotion("25", emoNormalDesc[25], R.raw.face25));
        emotionSearchMap.put("26", new Emotion("26", emoNormalDesc[26], R.raw.face26));
        emotionSearchMap.put("27", new Emotion("27", emoNormalDesc[27], R.raw.face27));
        emotionSearchMap.put("28", new Emotion("28", emoNormalDesc[28], R.raw.face28));
        emotionSearchMap.put("29", new Emotion("29", emoNormalDesc[29], R.raw.face29));

        emotionSearchMap.put("30", new Emotion("30", emoNormalDesc[30], R.raw.face30));
        emotionSearchMap.put("31", new Emotion("31", emoNormalDesc[31], R.raw.face31));
        emotionSearchMap.put("32", new Emotion("32", emoNormalDesc[32], R.raw.face32));
        emotionSearchMap.put("33", new Emotion("33", emoNormalDesc[33], R.raw.face33));
        emotionSearchMap.put("34", new Emotion("34", emoNormalDesc[34], R.raw.face34));
        emotionSearchMap.put("35", new Emotion("35", emoNormalDesc[35], R.raw.face35));
        emotionSearchMap.put("36", new Emotion("36", emoNormalDesc[36], R.raw.face36));
        emotionSearchMap.put("37", new Emotion("37", emoNormalDesc[37], R.raw.face37));
        emotionSearchMap.put("38", new Emotion("38", emoNormalDesc[38], R.raw.face38));
        emotionSearchMap.put("39", new Emotion("39", emoNormalDesc[39], R.raw.face39));

        emotionSearchMap.put("40", new Emotion("40", emoNormalDesc[40], R.raw.face40));
        emotionSearchMap.put("41", new Emotion("41", emoNormalDesc[41], R.raw.face41));
        emotionSearchMap.put("42", new Emotion("42", emoNormalDesc[42], R.raw.face42));
        emotionSearchMap.put("43", new Emotion("43", emoNormalDesc[43], R.raw.face43));
        emotionSearchMap.put("44", new Emotion("44", emoNormalDesc[44], R.raw.face44));
        emotionSearchMap.put("45", new Emotion("45", emoNormalDesc[45], R.raw.face45));

        emotionSearchMap.put("46", new Emotion("46", emoNormalDesc[46], R.raw.face46));
        emotionSearchMap.put("47", new Emotion("47", emoNormalDesc[47], R.raw.face47));
        emotionSearchMap.put("48", new Emotion("48", emoNormalDesc[48], R.raw.face48));
        emotionSearchMap.put("49", new Emotion("49", emoNormalDesc[49], R.raw.face49));
        emotionSearchMap.put("50", new Emotion("50", emoNormalDesc[50], R.raw.face50));
        emotionSearchMap.put("51", new Emotion("51", emoNormalDesc[51], R.raw.face51));
        emotionSearchMap.put("52", new Emotion("52", emoNormalDesc[52], R.raw.face52));
        emotionSearchMap.put("53", new Emotion("53", emoNormalDesc[53], R.raw.face53));
        emotionSearchMap.put("54", new Emotion("54", emoNormalDesc[54], R.raw.face54));
        emotionSearchMap.put("55", new Emotion("55", emoNormalDesc[55], R.raw.face55));
        emotionSearchMap.put("56", new Emotion("56", emoNormalDesc[56], R.raw.face56));
        emotionSearchMap.put("57", new Emotion("57", emoNormalDesc[57], R.raw.face57));
        emotionSearchMap.put("58", new Emotion("58", emoNormalDesc[58], R.raw.face58));
        emotionSearchMap.put("59", new Emotion("59", emoNormalDesc[59], R.raw.face59));
        emotionSearchMap.put("60", new Emotion("60", emoNormalDesc[60], R.raw.face60));
        emotionSearchMap.put("61", new Emotion("61", emoNormalDesc[61], R.raw.face61));
        emotionSearchMap.put("62", new Emotion("62", emoNormalDesc[62], R.raw.face62));

        emotionSearchMap.put("63", new Emotion("63", emoNormalDesc[63], R.raw.face63));
        emotionSearchMap.put("64", new Emotion("64", emoNormalDesc[64], R.raw.face64));
        emotionSearchMap.put("65", new Emotion("65", emoNormalDesc[65], R.raw.face65));
        emotionSearchMap.put("66", new Emotion("66", emoNormalDesc[66], R.raw.face66));
        emotionSearchMap.put("67", new Emotion("67", emoNormalDesc[67], R.raw.face67));

        emotionSearchMap.put("68", new Emotion("68", emoNormalDesc[68], R.raw.face68));
        emotionSearchMap.put("69", new Emotion("69", emoNormalDesc[69], R.raw.face69));
        emotionSearchMap.put("70", new Emotion("70", emoNormalDesc[70], R.raw.face70));
        emotionSearchMap.put("71", new Emotion("71", emoNormalDesc[71], R.raw.face71));

        emotionSearchMap.put("72", new Emotion("72", emoNormalDesc[72], R.raw.face72));
        emotionSearchMap.put("73", new Emotion("73", emoNormalDesc[73], R.raw.face73));
        emotionSearchMap.put("74", new Emotion("74", emoNormalDesc[74], R.raw.face74));
        emotionSearchMap.put("75", new Emotion("75", emoNormalDesc[75], R.raw.face75));
        emotionSearchMap.put("76", new Emotion("76", emoNormalDesc[76], R.raw.face76));
        emotionSearchMap.put("77", new Emotion("77", emoNormalDesc[77], R.raw.face77));
        emotionSearchMap.put("78", new Emotion("78", emoNormalDesc[78], R.raw.face78));*/

        emotionSearchList.addAll(emotionSearchMap.values());
        Collections.sort(emotionSearchList);
    }

}
