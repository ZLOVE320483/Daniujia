package com.xiaojia.daniujia.ui.views;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.ui.views.imageview.RoundedImageView;
import com.xiaojia.daniujia.utils.LogUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


/**
 * Created by Administrator on 2016/8/9.
 */
public class QRCodeDialog extends Dialog {
    private RoundedImageView mRivUserIcon;
    private TextView mTvName;
    private ImageView mIvQRCode;
    private String nameText;
    private Context context;
    private TextView mTvLongClickMessage;

    public QRCodeDialog(Context context, String name, String iconUrl) {
        super(context, R.style.Translucent_NoTitle);
        nameText = name;
        this.context = context;
        init();
    }

    public QRCodeDialog(Context context) {
        super(context, R.style.Translucent_NoTitle);
        this.context = context;
        init();
    }

    public void setName(String name) {
        this.nameText = name;
        mTvName.setText(this.nameText);
    }

    private void init() {
        setContentView(R.layout.dialog_qr_code);
        initView();
        clickEvent();
    }

    public void showdialog() {
        // 设置窗口弹出动画
        getWindow().setWindowAnimations(R.style.centerDialogWindowAnim);
        WindowManager.LayoutParams wl = getWindow().getAttributes();
        wl.gravity = Gravity.CENTER;
        getWindow().setAttributes(wl);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        show();
    }

    private void initView() {
        mRivUserIcon = (RoundedImageView) findViewById(R.id.dialog_qr_code_ri_user_icon);
        mTvName = (TextView) findViewById(R.id.dialog_qr_code_tv_name);
        mIvQRCode = (ImageView) findViewById(R.id.dialog_qr_code_iv_qr_picture);
        mTvLongClickMessage = (TextView) findViewById(R.id.dialog_qr_code_tv_long_click_message);

        mTvName.setText(nameText == null ? "" : nameText);
    }

    public void setQRCodeBitmap(Bitmap bitmap) {
        if (bitmap == null)
            return;
        mIvQRCode.setImageBitmap(bitmap);
    }

    public ImageView getmRivUserIcon() {
        return mRivUserIcon;
    }

    private void clickEvent() {
        mIvQRCode.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mTvLongClickMessage.setVisibility(View.INVISIBLE);
                saveImageToGallery(context);
                mTvLongClickMessage.setVisibility(View.VISIBLE);
                return false;
            }
        });
    }

    public void saveImageToGallery(Context context) {
        getWindow().getDecorView().setDrawingCacheEnabled(true);
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "Boohee");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            getWindow().getDecorView().getDrawingCache().compress(Bitmap.CompressFormat.JPEG, 80, fos);
            try {
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));
        Toast.makeText(context, "二维码名片保存成功", Toast.LENGTH_SHORT).show();
        getWindow().getDecorView().setDrawingCacheEnabled(false);
    }


}
