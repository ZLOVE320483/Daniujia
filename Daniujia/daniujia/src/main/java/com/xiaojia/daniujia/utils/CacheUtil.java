package com.xiaojia.daniujia.utils;

import com.xiaojia.daniujia.FileStorage;

import java.io.File;

/**
 * Created by ZLOVE on 2016/8/9.
 */
public class CacheUtil {

    public static String getCacheSize() {
        // 计算缓存大小
        long fileSize = 0;
        String cacheSize = "0M";

        File picFileDir = new File(FileStorage.APP_IMG_DIR);
        File attachDir = new File(FileStorage.APP_ATTACH_DIR);
        fileSize += getDirSize(picFileDir);
        fileSize += getDirSize(attachDir);
        if (fileSize > 0)
            cacheSize = formatFileSize(fileSize);
        return cacheSize;
    }

    /**
     * 获取目录文件大小
     */
    public static long getDirSize(File dir) {
        if (dir == null) {
            return 0;
        }
        if (!dir.isDirectory()) {
            return 0;
        }
        long dirSize = 0;
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                dirSize += file.length();
            } else if (file.isDirectory()) {
                dirSize += file.length();
                dirSize += getDirSize(file); // 递归调用继续统计
            }
        }
        return dirSize;
    }

    /**
     * 转换文件大小
     *
     * @return B/KB/MB/GB
     */
    public static String formatFileSize(long fileS) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
        String fileSizeString;
        if (fileS < 1048576) {
            fileSizeString = "0M";
        } else {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        }
        return fileSizeString;
    }

    /**
     * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理
     */
    public static void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                if (item.isDirectory()) {
                    deleteFilesByDirectory(item);
                }
                item.delete();
            }
            directory.delete();
        }
    }
}
