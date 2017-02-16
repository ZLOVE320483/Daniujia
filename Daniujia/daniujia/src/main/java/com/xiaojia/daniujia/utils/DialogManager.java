package com.xiaojia.daniujia.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.domain.resp.ExpertHomeVo;
import com.xiaojia.daniujia.ui.views.SelectServiceTypeWindow;

/**
 * Created by ZLOVE on 2016/6/2.
 */
public class DialogManager {

    public static void showToBeExpertDialog(Context context, final NotifyListener listener) {
        final Dialog dialog = new Dialog(context, R.style.MessageBox);
        View view = LayoutInflater.from(context).inflate(R.layout.dlg_center_admire, null);

        View close = view.findViewById(R.id.back);
        View confirm = view.findViewById(R.id.confirm);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (listener != null) {
                    listener.notifyTobeExpert();
                }
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (listener != null) {
                    listener.notifyTobeExpert();
                    listener.tobeExpert();
                }
            }
        });

        dialog.setContentView(view);
        dialog.getWindow().setWindowAnimations(R.style.centerDialogWindowAnim);
        WindowManager.LayoutParams wl = dialog.getWindow().getAttributes();
        wl.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(wl);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
    }

    public static void showBecomeExpertDialog(Context context, final NotifyListener listener) {
        final Dialog dialog = new Dialog(context, R.style.MessageBox);
        View view = LayoutInflater.from(context).inflate(R.layout.dlg_become_expert, null);

        View close = view.findViewById(R.id.back);
        View confirm = view.findViewById(R.id.confirm);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (listener != null) {
                    listener.notifyBecomeExpert();
                }
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (listener != null) {
                    listener.notifyBecomeExpert();
                }
            }
        });

        dialog.setContentView(view);
        dialog.getWindow().setWindowAnimations(R.style.centerDialogWindowAnim);
        WindowManager.LayoutParams wl = dialog.getWindow().getAttributes();
        wl.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(wl);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
    }

    public interface NotifyListener {
        void notifyTobeExpert();
        void tobeExpert();
        void notifyBecomeExpert();
    }

    public static void showActDialog(Context context, final JoinActListener listener, String imgUrl) {
        final Dialog dialog = new Dialog(context, R.style.MessageBox);
        View view = LayoutInflater.from(context).inflate(R.layout.dlg_act, null);

        View closeView = view.findViewById(R.id.close);
        ImageView joinView = (ImageView) view.findViewById(R.id.act_img);

        if (!TextUtils.isEmpty(imgUrl)) {
            Picasso.with(context).load(imgUrl).into(joinView);
        }

        closeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        joinView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.joinAct();
                }
                dialog.dismiss();
            }
        });

        dialog.setContentView(view);
        dialog.getWindow().setWindowAnimations(R.style.centerDialogWindowAnim);
        WindowManager.LayoutParams wl = dialog.getWindow().getAttributes();
        wl.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(wl);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
    }

    public interface JoinActListener {
        void joinAct();
    }

    public static void showAnonymousDialog(Context context) {
        final Dialog dialog = new Dialog(context, R.style.MessageBox);
        View view = LayoutInflater.from(context).inflate(R.layout.dlg_view_anonymous_tip, null);
        View confirm = view.findViewById(R.id.confirm);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setContentView(view);
        dialog.getWindow().setWindowAnimations(R.style.centerDialogWindowAnim);
        WindowManager.LayoutParams wl = dialog.getWindow().getAttributes();
        wl.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(wl);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
    }

    public static void showActsDialogWithMessage(Context context,String message) {
        final Dialog dialog = new Dialog(context, R.style.MessageBox);
        View view = LayoutInflater.from(context).inflate(R.layout.dlg_view_anonymous_tip, null);
        View confirm = view.findViewById(R.id.confirm);
        TextView tvMessage = (TextView)view.findViewById(R.id.id_content);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tvMessage.setText(message);
        dialog.setContentView(view);
        dialog.getWindow().setWindowAnimations(R.style.centerDialogWindowAnim);
        WindowManager.LayoutParams wl = dialog.getWindow().getAttributes();
        wl.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(wl);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
    }

    public static void showUpdateVersionDialog(Context context, int forceUpdate, final String version, String content, final UpdateVersionListener listener) {
        final Dialog dialog = new Dialog(context, R.style.MessageBox);
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_view_update_version, null);
        ImageView ivUpdateBb = (ImageView) contentView.findViewById(R.id.update_version_bg);
        TextView tvForceVersion = (TextView) contentView.findViewById(R.id.force_version);
        TextView tvVersion = (TextView) contentView.findViewById(R.id.id_version);
        TextView tvContent = (TextView) contentView.findViewById(R.id.id_content);
        Button btnForceUpdate = (Button) contentView.findViewById(R.id.force_update);
        View updateContainer = contentView.findViewById(R.id.update_container);
        Button btnCancel = (Button) contentView.findViewById(R.id.id_cancel);
        Button btnUpdate = (Button) contentView.findViewById(R.id.update);

        if (forceUpdate == 1) {
            ivUpdateBb.setImageResource(R.drawable.bg_update_version_force);
            tvForceVersion.setVisibility(View.VISIBLE);
            tvForceVersion.setText(version);
            btnForceUpdate.setVisibility(View.VISIBLE);
            updateContainer.setVisibility(View.GONE);
        } else {
            ivUpdateBb.setImageResource(R.drawable.bg_update_version);
            tvForceVersion.setVisibility(View.GONE);
            btnForceUpdate.setVisibility(View.GONE);
            updateContainer.setVisibility(View.VISIBLE);
        }
        tvVersion.setText("版本号: " + version);
        tvContent.setText(content);

        btnForceUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.updateAction();
                }
                dialog.dismiss();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.updateAction();
                }
                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                SysUtil.savePref(version, false);
            }
        });

        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                    return true;
                } else {
                    return false;
                }
            }
        });
        dialog.setContentView(contentView);
        dialog.getWindow().setWindowAnimations(R.style.centerDialogWindowAnim);
        WindowManager.LayoutParams wl = dialog.getWindow().getAttributes();
        wl.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(wl);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
    }

    public interface UpdateVersionListener {
        void updateAction();
    }

    public static void showTipDialog(Context context, String tip) {
        final Dialog dialog = new Dialog(context, R.style.MessageBox);
        View view = LayoutInflater.from(context).inflate(R.layout.dlg_view_anonymous_tip, null);
        View confirm = view.findViewById(R.id.confirm);
        TextView tvTipContent = (TextView) view.findViewById(R.id.id_content);
        if (!TextUtils.isEmpty(tip)) {
            tvTipContent.setText(tip);
        }

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setContentView(view);
        dialog.getWindow().setWindowAnimations(R.style.centerDialogWindowAnim);
        WindowManager.LayoutParams wl = dialog.getWindow().getAttributes();
        wl.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(wl);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
    }


    public static void showForgetPwdDialog(Context context, final ConfirmActionListener listener) {
        final Dialog dialog = new Dialog(context, R.style.MessageBox);
        View view = LayoutInflater.from(context).inflate(R.layout.forget_pwd_dialog, null);
        View confirm = view.findViewById(R.id.id_confirm);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.confirm();
                }
                dialog.dismiss();
            }
        });

        dialog.setContentView(view);
        dialog.getWindow().setWindowAnimations(R.style.centerDialogWindowAnim);
        WindowManager.LayoutParams wl = dialog.getWindow().getAttributes();
        wl.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(wl);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
    }

    public interface ConfirmActionListener {
        void confirm();
    }

    public static SelectServiceTypeWindow getSelectServiceWindow(final Activity context, ExpertHomeVo.ServiceInfo serviceInfo, SelectServiceTypeWindow.SelectServiceTypeListener listener) {
        SelectServiceTypeWindow window;

        View view = LayoutInflater.from(context).inflate(R.layout.dlg_view_select_service_type, null);
        window =  new SelectServiceTypeWindow(view, serviceInfo, listener);
        window.setFocusable(true);
        window.setTouchable(true);
        window.setOutsideTouchable(true);
        window.setAnimationStyle(R.style.MenuDialogAnimation);
        window.setBackgroundDrawable(new BitmapDrawable());
        backgroundAlpha(context, 0.5f);

        window.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                backgroundAlpha(context, 1f);
            }
        });
        window.initView();
        window.initData();
        return window;
    }

    private static void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        if (bgAlpha == 1) {
            context.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        lp.alpha = bgAlpha;
        context.getWindow().setAttributes(lp);
    }
}
