package com.xiaojia.daniujia.utils;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Selection;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xiaojia.daniujia.FileStorage;
import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.base.App;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WUtil {
    public static boolean isPhoneNO(String mobile) {
        Pattern p = Pattern.compile("^1\\d{10}$");
        // Pattern p =
        // Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobile);
        return m.matches();
    }

    public static boolean isPwdFormat(String pwd) {
        Pattern p = Pattern.compile("[a-z0-9A-Z_]+");
        Matcher m = p.matcher(pwd);
        return m.matches();
    }

    public static void setWarnText(TextView et, int resId) {
        et.setText("");
        et.setHintTextColor(Color.RED);
        et.setHint(resId);
    }

    private static void endEtCursor(EditText et) {
        Editable editable = et.getText();
        Selection.setSelection(editable, editable.length());
    }

    public static void setTextEndCursor(EditText et, String s) {
        if (s == null) {
            return;
        }
        et.setText(s);
        endEtCursor(et);
    }

    public static void setTextEndCursor(EditText et, int resId) {
        et.setText(resId);
        endEtCursor(et);
    }

    public static Bitmap scaleBitmapFitWidth(Bitmap bm) {
        int bmWidth = bm.getWidth();
        int bmHeight = bm.getHeight();

        int screenWidth = SysUtil.getDisplayMetrics().widthPixels;
        int screenHeight = SysUtil.getDisplayMetrics().heightPixels;

        float zoomScale = ((float) screenWidth) / bmWidth;
        if (zoomScale <= 1 && ((float) bmHeight) / zoomScale > screenHeight || zoomScale > 1
                && ((float) bmHeight) * zoomScale > screenHeight) {// 高比例太大
            zoomScale = ((float) screenHeight) / bmHeight;
        }
        bm = BitmapUtil.zoomImage(bm, zoomScale);
        return bm;
    }

    // 复制文件
    public static void copyFile(File sourceFile, File targetFile) {
        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;
        try {
            // 新建文件输入流并对它进行缓冲
            inBuff = new BufferedInputStream(new FileInputStream(sourceFile));
            // 新建文件输出流并对它进行缓冲
            outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));
            byte[] b = new byte[1024 * 5];// 缓冲数组
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            outBuff.flush();// 刷新此缓冲的输出流
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inBuff != null) {
                try {
                    inBuff.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outBuff != null) {
                try {
                    outBuff.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 不存在,返回file对象；存在,返回null
     */
    // private static File isImageNotCached(String imgUrl) {
    // // http:\/\/7xlqoz.com1.z0.glb.clouddn.com\/C1366339582002.jpg
    // File imgFile = getImageFileFromUrl(imgUrl);
    // if (!imgFile.exists()) {
    // return imgFile;
    // }
    // return null;
    // }
    //
    // public static void asyncCacheAndDisplayImg(final ImageView imageView,
    // String imgUrl, final boolean toCircel) {
    // final int defImgResId = R.drawable.consult_icon;
    // imageView.setImageResource(defImgResId);// 预加载图片
    // if (TextUtils.isEmpty(imgUrl)) {
    // return;
    // }
    // final File imgFile = isImageNotCached(imgUrl);
    // if (imgFile != null) {
    // HttpUtil.downloadFile(imgUrl, imgFile, new RequestCallBack<File>() {
    // @Override
    // public void onSuccess(ResponseInfo<File> responseInfo) {
    // if (imgFile.length() == 0) {
    // imageView.setImageResource(defImgResId);
    // imgFile.delete();
    // } else {
    // Bitmap imgBm = BitmapUtil.getCompressedBitmapFromFile(imgFile);
    // if (imgBm == null) {
    // LogUtil.e("test", "getBitmapFromFile: NULL");
    // return;
    // }
    // if (toCircel) {
    // imgBm = BitmapUtil.toRoundCorner(imgBm);
    // }
    // imageView.setImageBitmap(imgBm);
    // }
    // }
    //
    // @Override
    // public void onFailure(HttpException error, String msg) {
    // imageView.setImageResource(defImgResId);
    // LogUtil.d("test", error + "," + msg);
    // }
    // });
    // } else {
    // File cachedFile = getImageFileFromUrl(imgUrl);
    // if (cachedFile.length() == 0) {
    // imageView.setImageResource(defImgResId);
    // cachedFile.delete();
    // } else {
    // Bitmap bm = BitmapUtil.getCompressedBitmapFromFile(cachedFile);
    // if (toCircel) {
    // bm = BitmapUtil.toRoundCorner(bm);
    // }
    // imageView.setImageBitmap(bm);
    // }
    // }
    // }
    //
    // public static void asyncCacheAndDisplayImg(final ImageView imageView,
    // String imgUrl) {
    // asyncCacheAndDisplayImg(imageView, imgUrl, false);
    // }

    /**
     * 获取组件绝对坐标位置
     *
     * @param view
     * @return [x, y]
     */
    public static int[] getAbsolutePosition(View view) {
        int[] xy = new int[2];
        view.getLocationOnScreen(xy);
        return xy;
    }

    /**
     * 获取组件尺寸
     *
     * @param view
     * @return [width, height]
     */
    public static int[] getViewMeasure(View view) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        int[] measure = new int[]{view.getMeasuredWidth(), view.getMeasuredHeight()};
        return measure;
    }

    /**
     * positions:左上角x、右下角x、左上角y、右下角y
     */
    public static int[] getWindowPosition(View view) {
        int[] positions = new int[4];
        int width = SysUtil.getDisplayMetrics().widthPixels;
        int height = SysUtil.getDisplayMetrics().heightPixels;
        int[] wh = getViewMeasure(view);
        positions[0] = width / 2 - wh[0] / 2;// 左上角x
        positions[2] = width / 2 + wh[0] / 2;// 右下角x
        positions[1] = height / 2 - wh[1] / 2;// 左上角y
        positions[3] = height / 2 + wh[1] / 2;// 右下角y
        return positions;
    }

    public static String getFloatStr(float value) {
        String s = value + "";
        if (s.endsWith(".0")) {
            return s.substring(0, s.lastIndexOf(".0"));
        }
        return s;
    }

    // 软键盘管理类
    private static InputMethodManager imm = (InputMethodManager) App.get().getSystemService(
            Context.INPUT_METHOD_SERVICE);

    public static void handleSoftInputWindow(EditText et, boolean show) {
        if (show) {
            imm.showSoftInputFromInputMethod(et.getWindowToken(), 0);
        } else {
            imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
        }
    }

    private static final int VERSION_CODES_KITKAT = 19;// Android 4.4.2

    @SuppressLint("NewApi")
    public static String getFilePath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= VERSION_CODES_KITKAT;
        if (!isKitKat) {
            return getFilePath(uri);
        }
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    private static String getFilePath(Uri uri) {
        try {
            if (uri.getScheme().equals("file")) {
                return uri.getPath();
            } else {
                Cursor cursor = SysUtil.getContentResolver().query(uri, null, null, null, null);
                cursor.moveToFirst();
                String imgPath = cursor.getString(1);
                cursor.close();
                return imgPath;
            }
        } catch (Exception e) {
            LogUtil.e("test", e);
            return null;
        }
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    private static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static JSONObject getAssetsDataJson(String assetName) {
        InputStream is = null;
        try {
            StringBuffer sb = new StringBuffer();
            is = App.get().getAssets().open(assetName);
            int len = -1;
            byte[] buf = new byte[1024];
            while ((len = is.read(buf)) != -1) {
                sb.append(new String(buf, 0, len, "gbk"));
            }
            is.close();
            String rawJson = sb.toString();
            JSONObject jobj = JSON.parseObject(rawJson);
            return jobj;
        } catch (Exception e) {
            if (is != null) {
                is = null;
            }
            LogUtil.e("test", e);
        }
        return null;
    }

    public static void sysShare(String text, Bitmap bm) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        if (bm != null) {// 分享图片
            String picName = "share" + System.currentTimeMillis() + ".png";
            File imgFile = new File(FileStorage.baseDir, FileStorage.buildCachePath(picName));
            try {
                bm.compress(CompressFormat.PNG, 100, new FileOutputStream(imgFile));
            } catch (Exception e) {
                LogUtil.e("test", e);
            }
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(imgFile));
        } else {
            intent.setType("text/plain");
        }
        intent.putExtra(Intent.EXTRA_SUBJECT, "选择分享方式");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        App.get().startActivity(intent);
    }

    public static void startNewTaskActivity(Class<?> clz) {
        Intent intent = new Intent(App.get(), clz);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        App.get().startActivity(intent);
    }

    public static void startNewTaskActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(App.get(), clz);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtras(bundle);
        App.get().startActivity(intent);
    }

    @SuppressWarnings("deprecation")
    public static boolean isTopActivity() {
        ActivityManager activityMgr = (ActivityManager) App.get().getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasksInfo = activityMgr.getRunningTasks(1);
        if (tasksInfo.size() > 0) {
            PackageInfo pkgInfo = SysUtil.getPkgInfo();
            String pkgName = pkgInfo.packageName;
            if (pkgName.equals(tasksInfo.get(0).topActivity.getPackageName())) { // 应用程序位于堆栈的顶层
                return true;
            }
        }
        return false;
    }

    public static void openUrl(String url) {
        if (url.startsWith("www")) {// 要以http开头
            url = "http://" + url;
        }
        try {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            App.get().startActivity(intent);
        } catch (Throwable e) {
        }
    }

    /*
     * 需要是登录状态
     */
    public static String genAvatarFileName() {
        return "avatar-" + SysUtil.getIntPref(PrefKeyConst.PREF_USER_ID) + "-" + System.currentTimeMillis() + ".jpg";
    }

    public static String genVoiceFileName() {
        return "avatar-" + SysUtil.getIntPref(PrefKeyConst.PREF_USER_ID) + "-" + System.currentTimeMillis() + ".amr";
    }

    public static String genAdFileName() {
        return "ad-" + SysUtil.getIntPref(PrefKeyConst.PREF_USER_ID) + "-" + System.currentTimeMillis() + ".jpg";
    }

    public static void launchAppCommentPage() {
        try {
            Uri uri = Uri.parse("market://details?id=" + App.get().getPackageName());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            App.get().startActivity(intent);
        } catch (ActivityNotFoundException e) {
            LogUtil.e("test", e);
        }
    }

    public static String transformDegree(int degree) {
        String degreeStr = "其他";
        if (degree == 1) {
            degreeStr = "学士";
        } else if (degree == 2) {
            degreeStr = "硕士";
        } else if (degree == 3) {
            degreeStr = "博士";
        }
        return degreeStr;
    }


    public static String keepTowDecimal(String result){
        float decimal = Float.parseFloat(result);
        DecimalFormat decimalFormat=new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        return decimalFormat.format(decimal);
    }

}
