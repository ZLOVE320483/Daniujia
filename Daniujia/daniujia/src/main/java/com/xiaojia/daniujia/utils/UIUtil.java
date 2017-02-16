package com.xiaojia.daniujia.utils;

import android.app.Activity;
import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.xiaojia.daniujia.ui.views.toast.ToastCompat;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ZLOVE on 2016/4/11.
 */
public class UIUtil {

    public static void hideKeyboard(Activity context) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(context.getWindow().getDecorView()
                    .getWindowToken(), 0);
        }
    }

    public static void showKeyboard(Activity context, EditText view) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, 0);
        }
    }

    //强制显示或者关闭系统键盘
    public static void forceShowOrHideKeyBoard(final EditText txtSearchKey, final String status) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager m = (InputMethodManager)
                        txtSearchKey.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (status.equals("open")) {
                    m.showSoftInput(txtSearchKey, 0);
                } else {
                    m.hideSoftInputFromWindow(txtSearchKey.getWindowToken(), 0);
                }
            }
        }, 300);
    }

    public static void showShortToast(String msg) {
        ToastCompat.makeText(ApplicationUtil.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public static void showShortToast(int message) {
        String msg = ApplicationUtil.getApplicationContext().getResources().getString(message);
        ToastCompat.makeText(ApplicationUtil.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public static InputFilter getTwoDecimalFilter() {
        InputFilter lengthFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                if ("".equals(source.toString())) {
                    return null;
                }
                String dValue = dest.toString();
                String[] splitArray = dValue.split("\\.");
                if (splitArray.length > 1) {
                    String dotValue = splitArray[1];
                    int diff = dotValue.length() + 1 - 2;
                    if (diff > 0) {
                        // (zp)代表满足条件后，把当前输入的字符串替换成return返回的值，网上
                        //     大部分资料是用的return source.subSequence(start, end - diff);这是不符合过滤小数的规则的
                        return "";
                    }
                }
                return null;
            }
        };

        return lengthFilter;
    }

    public static int getResColor(int color) {
        return ApplicationUtil.getApplicationContext().getResources().getColor(color);
    }

}
