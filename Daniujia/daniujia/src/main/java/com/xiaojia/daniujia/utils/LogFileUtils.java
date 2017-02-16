package com.xiaojia.daniujia.utils;

import android.annotation.SuppressLint;

import com.xiaojia.daniujia.FileStorage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogFileUtils {

    protected static final String LOG_TAG = LogFileUtils.class.getSimpleName();
    private static LogFileUtils instance;

    public static LogFileUtils getInstance() {
        if (instance == null) {
            synchronized (LOG_TAG) {
                if (instance == null) {
                    instance = new LogFileUtils();
                }
            }
        }
        return instance;
    }

    private LogFileUtils() {

    }

    @SuppressLint("SimpleDateFormat")
    File getFile() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String name = sdf.format(new Date());
        File logDir = new File(FileStorage.APP_LOG_DIR);
        if (!logDir.exists()) {
            logDir.mkdirs();
        }
        return new File(FileStorage.APP_LOG_DIR, name);
    }

    @SuppressLint("SimpleDateFormat")
    public void log2File(String msg, Exception e) {
        try {
            PrintWriter pw = new PrintWriter(new FileOutputStream(getFile(), true));
            pw.println(new SimpleDateFormat("yyyyMMdd HH:mm:ss").format(new Date()));
            pw.println(msg);
            e.printStackTrace(pw);

            pw.println();
            pw.println();
            pw.println();
            pw.println();

            pw.flush();
            pw.close();
        } catch (Exception ex) {

        }
    }
}
