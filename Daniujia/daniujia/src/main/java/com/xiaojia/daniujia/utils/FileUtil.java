package com.xiaojia.daniujia.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.xiaojia.daniujia.FileStorage;
import com.xiaojia.daniujia.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;

/**
 * Created by Administrator on 2016/4/6.
 */
public class FileUtil {

    public static void copy(File srcFile, File destFile) throws IOException {
        final long CHUNK_SIZE = 4 * 1024;

        if (destFile.exists() && destFile.isDirectory()) {
            throw new IOException("Destination '" + destFile + "' exists but is a directory");
        }

        FileInputStream fis = null;
        FileOutputStream fos = null;
        FileChannel input = null;
        FileChannel output = null;
        try {
            fis = new FileInputStream(srcFile);
            fos = new FileOutputStream(destFile);
            input = fis.getChannel();
            output = fos.getChannel();
            long size = input.size();
            long pos = 0;
            long count = 0;
            while (pos < size) {
                count = (size - pos) > CHUNK_SIZE ? CHUNK_SIZE : (size - pos);
                pos += output.transferFrom(input, pos, count);
            }
        } finally {
            if (output != null) {
                output.force(true);
                output.close();
            }
            if (fos != null) {
                fos.flush();
                fos.close();
            }
            if (input != null)
                input.close();
            if (fis != null)
                fis.close();
        }

        if (srcFile.length() != destFile.length()) {
            throw new IOException("Failed to copy full contents from '" + srcFile + "' to '" + destFile + "'");
        }
    }

    public static String urlToLocalPath(String url) {
        try {
            return FileStorage.APP_IMG_DIR + File.separator + Base64.encodeBytes(url.getBytes(), Base64.URL_SAFE);
        } catch (IOException e) {
            e.printStackTrace();
            return url;
        }
    }

    public static String getUnDecodePath(String path) {
        // remove magic string we add in GalleryBrowser
        if (path.startsWith("file://")) {
            path = path.replace("file://", "");
        }
        return path;
    }

    public static boolean isGifPicture(String filePath) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(filePath);
            byte[] buffer = new byte[3];
            inputStream.read(buffer, 0, 3);
            String type = new String(buffer);
            if ("GIF".equals(type)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    /**
     * @param size 精确到byte的文件大小
     * @return
     */
    public static String formatFileSize(long size) {
        DecimalFormat df = new DecimalFormat("#.0");
        String fileSizeString;
        if (size < 1024) {
            fileSizeString = df.format((double) size) + "B";
        } else if (size < 1048576) {
            fileSizeString = df.format((double) size / 1024) + "K";
        } else if (size < 1073741824) {
            fileSizeString = df.format((double) size / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) size / 1073741824) + "G";
        }
        return fileSizeString;
    }

    public static boolean isBiggerTo5M(long size){
        return size >= 5 * 1024 * 1024;
    }

    public static String getFilePrefix(String fileName) {
        if (fileName == null) {
            return "";
        }

        String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
        return prefix;
    }

    public static int getFileType(String fileName,boolean isBig){
        String type = getFilePrefix(fileName);

        if (type.equals("doc") || type.equals("docx")){
            if (isBig){
                return R.drawable.word_big;
            } else {
                return R.drawable.word_small;
            }
        } else if (type.equals("pdf")){
            if (isBig){
                return R.drawable.pdf_big;
            } else {
                return R.drawable.pdf_small;
            }
        } else if(type.equals("txt")){
            if (isBig){
                return R.drawable.txt_big;
            } else {
                return R.drawable.txt_small;
            }
        } else if(type.equals("ppt")){
            if (isBig){
                return R.drawable.pdf_big;
            } else {
                return R.drawable.pdf_small;
            }
        } else if (type.equals("xlsx") || type.equals("xls")){
            if (isBig){
                return R.drawable.exle_big;
            } else {
                return R.drawable.exle_small;
            }
        } else{
            if (isBig){
                return R.drawable.unknown_big;
            } else {
                return R.drawable.unknown_small;
            }
        }
    }

    public static void openFile(String localPath, Context context,String mimeType) {
        if (localPath == null) {
            return;
        }

        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        File file = new File(localPath);

        Uri fileUri = Uri.fromFile(file);
        if (!TextUtils.isEmpty(mimeType)) {
            intent.setDataAndType(fileUri, mimeType);
        } else {
            intent.setDataAndType(fileUri, "*/*");
        }

        try {
            context.startActivity(intent);
        } catch (android.content.ActivityNotFoundException e) {
            UIUtil.showShortToast(R.string.no_handler_for_this_type_of_file);
        }
    }

    /**
     * 获取指定文件大小
     * @return
     * @throws Exception
     */
    public static long getFileSize(File file) {
        long size = 0;
        try {
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                size = fis.available();
            } else {
                file.createNewFile();
                LogUtil.e("ZLOVE", "文件不存在!");
            }
        } catch (Exception e) {

        }
        return size;
    }
}
