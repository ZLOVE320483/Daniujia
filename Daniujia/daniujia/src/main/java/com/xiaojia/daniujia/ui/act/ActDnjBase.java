package com.xiaojia.daniujia.ui.act;

import android.os.Bundle;
import android.widget.Toast;

import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.base.AbsBaseActivity;
import com.xiaojia.daniujia.ui.views.toast.ToastCompat;
import com.xiaojia.daniujia.utils.ApplicationUtil;

/**
 * Created by ZLOVE on 2016/4/10.
 */
    public class ActDnjBase extends AbsBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (toggleOverridePendingTransition()) {
            switch (getOverridePendingTransitionMode()) {
                case LEFT:
                    overridePendingTransition(R.anim.left_in, R.anim.left_out);
                    break;

                case RIGHT:
                    overridePendingTransition(R.anim.right_in, R.anim.right_out);
                    break;

                case TOP:
                    overridePendingTransition(R.anim.top_in, R.anim.top_out);
                    break;

                case BOTTOM:
                    overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out);
                    break;

                case SCALE:
                    overridePendingTransition(R.anim.scale_in, R.anim.scale_out);
                    break;

                case FADE:
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    break;
            }
        }
    }


    @Override
    public void finish() {
        super.finish();
        if (toggleOverridePendingTransition()) {
            switch (getOverridePendingTransitionMode()) {
                case LEFT:
                    overridePendingTransition(R.anim.left_in, R.anim.left_out);
                    break;

                case RIGHT:
                    overridePendingTransition(R.anim.right_in, R.anim.right_out);
                    break;

                case TOP:
                    overridePendingTransition(R.anim.top_in, R.anim.top_out);
                    break;

                case BOTTOM:
                    overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out);
                    break;

                case SCALE:
                    overridePendingTransition(R.anim.scale_in, R.anim.scale_out);
                    break;

                case FADE:
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    break;
            }
        }
    }

    protected void showShortToast(String msg) {
        ToastCompat.makeText(ApplicationUtil.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    protected void showShortToast(int message) {
        String msg = ApplicationUtil.getApplicationContext().getResources().getString(message);
        ToastCompat.makeText(ApplicationUtil.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }


    /**
     * 标识是否需要启用自定义的页面跳转方式
     * @return
     */
    protected boolean toggleOverridePendingTransition() {
        return false;
    }

    /**
     * 获取页面跳转动画类型
     * @return
     */
    protected TransitionMode getOverridePendingTransitionMode() {
        return TransitionMode.RIGHT;
    }

    public enum TransitionMode {
        LEFT, RIGHT, TOP, BOTTOM, SCALE, FADE
    }
}
