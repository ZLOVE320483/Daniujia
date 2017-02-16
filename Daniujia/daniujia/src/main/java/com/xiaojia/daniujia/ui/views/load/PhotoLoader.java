package com.xiaojia.daniujia.ui.views.load;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import android.util.Log;

import com.xiaojia.daniujia.utils.ApplicationUtil;
import com.xiaojia.daniujia.utils.FileUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by ZLOVE on 2016/4/14.
 */
public class PhotoLoader implements Handler.Callback {

    private static final String LOG_TAG = PhotoLoader.class.getSimpleName();

    private static PhotoLoader mPhotoLoader;

    private static boolean DEBUG = false;// 设置成true可以打调试日志

    public static final String PREFIX_GALLERY = "gallery_";// 对于有些相同的url，但是需要生成不同大小的bitmap加上前缀以生成不同的url

    public static final String PREFIX_ROUND = "round_";// 同上

    public static final String SD_CARD_PATH = Environment.getExternalStorageDirectory().getPath();

    /**
     * Type of message sent by the UI thread to itself to indicate that some photos need to be loaded.
     */
    private static final int MESSAGE_REQUEST_LOADING = 1;

    private static final int MAX_LOAD_NUMBER = 2;

    private static final int MAX_ATTEMP_LOAD_TIMES = 3;// 尝试下载连接的最大次数

    private static final int DEFALUT_WIDTH_SIZE = 480;

    private static final int DEFALUT_HEIGHT_SIZE = 800;

    private static final int DEFAULT_MEMORY_CACHE_SIZE = 10 * 1024 * 1024;// 默认是10m内存的缓存

    private int maxCacheSize;

    private enum UrlType {
        LOCAL, CAHCE, HTTP
    };

    public static final int TYPE_NORMAL = 0;

    public static final int TYPE_GIF = 1;

    /**
     * Maintains the state of a particular photo.
     */
    private static class BitmapHolder {

        private static final int NEEDED = 0;

        private static final int LOADING = 1;

        private static final int LOADED = 2;

        private static final int ERROR = 3;

        long timeStamp;

        int state;

        int type = TYPE_NORMAL;

        Bitmap bitmapRef;
    }

    /**
     * A soft cache for photos.
     */
    // private final Map<String, BitmapHolder> mBitmapCache;
    // private Cache mCache;
    private Cache mBitmapCache;

    private PhotoPriorityBlockingQueue mPriorityBlockingQueue = new PhotoPriorityBlockingQueue();

    private PhotoPriorityBlockingQueue mLocalPriorityBlockingQueue = new PhotoPriorityBlockingQueue();

    private LinkedBlockingQueue<PendingPhoto> mBlockingQueue = new LinkedBlockingQueue<PhotoLoader.PendingPhoto>();

    private PendWorkThread mPendWorkThread;

    /**
     * Handler for messages sent to the UI thread.
     */
    private final Handler mMainThreadHandler = new Handler(this);

    /**
     * A gate to make sure we only send one instance of MESSAGE_PHOTOS_NEEDED at a time.
     */
    private boolean mLoadingRequested;

    /**
     * Flag indicating if the image loading is paused.
     */
    private boolean mPaused;
    static {
        if (Build.VERSION.SDK_INT < 8) {
            System.setProperty("http.keepAlive", "false");
        }
    }

    /**
     * Constructor.
     *
     * @param context content context
     */
    private PhotoLoader(Context context) {
        maxCacheSize = (int) Math.min(DEFAULT_MEMORY_CACHE_SIZE, Runtime.getRuntime().maxMemory() / 5);
        mBitmapCache = new Cache(maxCacheSize);
        requestLoading();
        mPendWorkThread = new PendWorkThread("PendWorkThread");
        mPendWorkThread.start();
        if (DEBUG) {
            Log.i(LOG_TAG, "max size is:" + (Runtime.getRuntime().maxMemory() / 1024 / 1024));
        }

    }

    public synchronized static PhotoLoader getInstance() {
        if (mPhotoLoader == null) {
            mPhotoLoader = new PhotoLoader(ApplicationUtil.getApplicationContext());
        }
        return mPhotoLoader;
    }

    private void reLoadPhoto(ImageCachable imageCachable, String url, int priority) {
        loadPhoto(imageCachable, url, priority);
    }

    /**
     * Load photo into the supplied image view. If the photo is already cached, it is displayed immediately. Otherwise a
     * request is sent to load the photo from the database.
     */
    public void loadPhoto(ImageCachable imgCachable, String url, int priority) {
        final PendingPhoto pendingPhoto = new PendingPhoto(imgCachable, url, priority);
        mPriorityBlockingQueue.clean(pendingPhoto);// 清除网络队列中相同的view
        mLocalPriorityBlockingQueue.clean(pendingPhoto);// 清除本地加载队列中相同的view
        mBlockingQueue.remove(pendingPhoto);// 清除队列中相同的view
        if (!TextUtils.isEmpty(url)) {
            boolean loaded = loadCachedPhoto(imgCachable, url);// 从缓存中加载
            if (!loaded) {
                mBlockingQueue.add(pendingPhoto);// 缓存没有命中加入请求队列中
            }
        }
    }

    /**
     * Checks if the photo is present in cache. If so, sets the photo on the view, otherwise sets the state of the photo
     * to {@link BitmapHolder#NEEDED} and temporarily set the image to the default resource ID.
     */
    private boolean loadCachedPhoto(ImageCachable imageCachable, String url) {
        BitmapHolder holder = mBitmapCache.get(url);
        if (holder == null) {
        } else if (holder.state == BitmapHolder.LOADED) {
            // Null bitmap reference means that database contains no bytes for the photo
            holder.timeStamp = System.currentTimeMillis();
            if (holder.bitmapRef != null) {
                Bitmap bitmap = holder.bitmapRef;
                if (bitmap != null) {
                    imageCachable.setPictureType(holder.type);
                    imageCachable.setBitmap(bitmap, false, url);
                    return true;
                }
            }
            holder.bitmapRef = null;
            mBitmapCache.remove(url);
        }
        imageCachable.onStartLoad(url);
        return false;
    }

    /**
     * Stops loading images, kills the image loader thread and clears all caches.
     */
    public void stop() {
        pause();
        mLoadingRequested = false;
        for (int i = 0; i < mThreads.size(); i++) {
            WorkThread thread = (WorkThread) mThreads.get(i);
            if (!thread.isInterrupted()) {
                thread.setStop(mLoadingRequested);
                thread.interrupt();
            }
        }
        mThreads.clear();
        mPriorityBlockingQueue.clear();
        mLocalPriorityBlockingQueue.clear();
        try {
            mBitmapCache.evictAll();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        mMainThreadHandler.removeCallbacks(runnable);
        //mMainThreadHandler.removeCallbacks(clearCacheRunale);
        System.gc();
    }

    /**
     * 清除缓存，并且清除请求队列，调用gc
     */
    public void clear() {
        mPriorityBlockingQueue.clear();
        requestLowMemory();
    }

    private Object dummy = new Object();

    /**
     * 清除缓存，调用gc
     */
    public void requestLowMemory() {
        synchronized (dummy) {
            try {
                mBitmapCache.evictAll();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.gc();
    }

    /**
     * Temporarily stops loading photos from the database.
     */
    public void pause() {
        mPaused = true;
    }

    /**
     * Resumes loading photos from the database.
     */
    public void resume() {
        if (mLoadingRequested) {
            mLoadingRequested = false;
            requestLoading();

        }
        mPaused = false;
        mPendWorkThread.onResume();

    }

    /**
     * Sends a message to this thread itself to start loading images. If the current view contains multiple image views,
     * all of those image views will get a chance to request their respective photos before any of those requests are
     * executed. This allows us to load images in bulk.
     */
    private void requestLoading() {
        if (!mLoadingRequested) {
            mLoadingRequested = true;
            mMainThreadHandler.post(runnable);
            //mMainThreadHandler.post(clearCacheRunale);
            mMainThreadHandler.sendEmptyMessage(MESSAGE_REQUEST_LOADING);
        }
    }

    private List<Thread> mThreads = Collections.synchronizedList(new ArrayList<Thread>());

    /**
     * Processes requests on the main thread.
     */
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MESSAGE_REQUEST_LOADING: {
                mLoadingRequested = false;
                if (!mPaused) {
                    if (mThreads.size() <= 0) {
                        for (int i = 0; i < MAX_LOAD_NUMBER; i++) {
                            Thread workThread = new WorkThread(false, "http_load");// http请求线程
                            mThreads.add(workThread);
                            workThread.start();
                        }
                        for (int i = 0; i < MAX_LOAD_NUMBER; i++) {
                            Thread workThread = new WorkThread(true, "local_load");// 本地请求线程
                            mThreads.add(workThread);
                            workThread.start();
                        }
                    }

                }
                return true;
            }
        }
        return false;
    }

    /**
     * Stores the supplied bitmap in cache.
     */
    private void cacheBitmap(String url, BitmapHolder holder) {
        if (mPaused) {
            return;
        }
        holder.state = BitmapHolder.LOADED;
        mBitmapCache.put(url, holder);
    }

    private Bitmap loadBitmap(String url, PendingPhoto pendingPhoto, BitmapHolder holder) {
        String resorceUrl = url;
        url = url.replace(PREFIX_GALLERY, "").replace(PREFIX_ROUND, "");// replace前缀获得真实的url
        Bitmap bitmap = null;
        final ImageCachable imageCachable = pendingPhoto.imageView.get();
        if (imageCachable == null) {
            return null;
        }
        String fileName = url;
        if (pendingPhoto.urlType != UrlType.LOCAL) {
            url = imageCachable.getDownLoadUrl(url);
            fileName = url;
            if (!url.toLowerCase().startsWith("http:")) {
                // 拼接下载地址
                url = "http://" + (url.charAt(0) == '/' ? url : "/" + url);
            }
        }
        String file = imageCachable.getCacheUrl(fileName);
        if (pendingPhoto.urlType == UrlType.LOCAL) {
            bitmap = getBtimapFromFileCache(url, imageCachable, pendingPhoto, holder);// 从sd卡中加载本地图片
        } else {
            if (pendingPhoto.urlType == UrlType.CAHCE) {
                bitmap = getLocalBitmap(file, imageCachable, pendingPhoto, holder);// 从缓存中加载网络图片
            } else {
                bitmap = getHttpBitmap(url, imageCachable, pendingPhoto, file, resorceUrl, holder);// 从网络加载网络图片
            }
        }
        return bitmap;
    }

    /**
     * 判断图片是从本地加载、网络缓存加载、网路下载加载
     */
    private UrlType isDiskExist(String url, PendingPhoto pendingPhoto) {
        UrlType isLocal = UrlType.LOCAL;
        url = url.replace(PREFIX_GALLERY, "");
        final ImageCachable imageCachable = pendingPhoto.imageView.get();
        if (imageCachable == null) {
            return UrlType.LOCAL;
        }
        String fileName = url;
        boolean isLocalFile = isLocalFile(fileName);
        if (!isLocalFile) {
            url = imageCachable.getDownLoadUrl(url);
            fileName = url;
            if (!url.toLowerCase().startsWith("http:")) {
                url = "http://" + (url.charAt(0) == '/' ? url : "/" + url);
            }
        } else {
            return UrlType.LOCAL;
        }
        String file = imageCachable.getCacheUrl(fileName);
        if (new File(file).exists()) {
            isLocal = UrlType.CAHCE;
        } else {
            isLocal = UrlType.HTTP;
        }
        return isLocal;
    }

    private Bitmap getBtimapFromFileCache(String path, ImageCachable imageCachable, PendingPhoto pendingPhoto, BitmapHolder holder) {
        return getLocalBitmap(path, imageCachable, pendingPhoto, holder);
    }

    public static boolean isLocalFile(String path) {
        if (path.contains(SD_CARD_PATH) || path.startsWith("file://")) {
            return true;
        }
        return new File(path).exists();
    }

    private Bitmap getLocalBitmap(String path, ImageCachable imageCachable, PendingPhoto pendingPhoto, BitmapHolder holder) {
        holder.type = FileUtil.isGifPicture(FileUtil.getUnDecodePath(path)) ? TYPE_GIF : TYPE_NORMAL;
        return imageCachable.reSizeBitmap(path, pendingPhoto.maxWidth, pendingPhoto.maxHeight);
    }

    private Bitmap getHttpBitmap(String url, ImageCachable imageCachable, PendingPhoto pendingPhoto, String path, String resorceUrl,
                                 BitmapHolder holder) {
        if (DEBUG)
            Log.d(LOG_TAG, "downLoad from http: " + url);

        Bitmap bitmap = null;
        File cacheFile = new File(path);
        boolean downloaded = downLoadCacheFile(url, imageCachable, cacheFile, resorceUrl);
        if (downloaded) {
            bitmap = getLocalBitmap(path, imageCachable, pendingPhoto, holder);
        }
        return bitmap;
    }

    private boolean downLoadCacheFile(String url, ImageCachable imageCachable, File cacheFile, String resorceUrl) {
        URL myFileUrl = null;
        InputStream is = null;
        try {
            myFileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setConnectTimeout(3000);
            conn.setDoInput(true);
            if (conn != null && conn.getResponseCode() == 200) {
                is = conn.getInputStream();
                if (is != null) {
                    return saveBitmapToLocalCache(is, cacheFile, imageCachable, url, conn.getContentLength(), resorceUrl);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {

            requestLowMemory();
            try {
                Thread.sleep(200);
            } catch (InterruptedException e1) {
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (conn != null) {
                    conn.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    // 下载图片的thread，mIsLocal==true是专门加载本地、本地缓存图片，mIsLocal==false通过http加载图片
    private class WorkThread extends Thread {

        private boolean mIsLocal;

        WorkThread(boolean isLocal, String name) {
            super(name);
            mIsLocal = isLocal;
        }

        private boolean stop = false;

        @Override
        public void run() {
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
            while (!stop) {
                try {
                    PhotoPriorityBlockingQueue penBlockingQueue = mIsLocal ? mLocalPriorityBlockingQueue : mPriorityBlockingQueue;
                    PendingPhoto pendingPhoto = penBlockingQueue.take();
                    Bitmap bitmap = null;
                    String url = pendingPhoto.url;
                    BitmapHolder holder = mBitmapCache.get(url);
                    int attempTimes = 0;
                    boolean flag = false;
                    holder = holder == null ? new BitmapHolder() : holder;
                    if (holder.state == BitmapHolder.NEEDED) {
                        holder.state = BitmapHolder.LOADING;
                        while (bitmap == null && !stop) {

                            Log.d("ZLOVE", "run。。。。");

                            flag = checkIsSameView(pendingPhoto);
                            if (DEBUG)
                                Log.i(LOG_TAG, "do loading:" + pendingPhoto.url);
                            if (attempTimes >= MAX_ATTEMP_LOAD_TIMES || !flag) {
                                break;
                            }
                            try {
                                bitmap = loadBitmap(url, pendingPhoto, holder);
                            } catch (OutOfMemoryError e) {
                                e.printStackTrace();
                                requestLowMemory();
                                Thread.sleep(200);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            attempTimes++;
                        }
                    }
                    if (bitmap != null) {
                        holder.bitmapRef = bitmap;
                        cacheBitmap(url, holder);
                    }
                    if (attempTimes >= MAX_ATTEMP_LOAD_TIMES && bitmap == null) {
                        holder.state = BitmapHolder.ERROR;
                    } else {
                        holder.state = BitmapHolder.LOADED;
                    }
                    holder.timeStamp = System.currentTimeMillis();
                    flag = checkIsSameView(pendingPhoto);
                    final ImageCachable imageCachable = pendingPhoto.imageView.get();
                    if (flag && imageCachable != null && !stop) {
                        final Bitmap finalBitmap = bitmap;
                        final BitmapHolder bitmapHolder = holder;
                        final String photoUrl = pendingPhoto.url;
                        final int pendingPriority = pendingPhoto.priority;
                        mMainThreadHandler.post(new Runnable() {

                            @Override
                            public void run() {
                                if (bitmapHolder.state != BitmapHolder.ERROR && finalBitmap != null) {
                                    imageCachable.setPictureType(bitmapHolder.type);
                                    Log.i(LOG_TAG, "type: " + bitmapHolder.type);
                                    imageCachable.setBitmap(finalBitmap, false, photoUrl);
                                }
                                if (finalBitmap != null) {
                                    return;
                                }
                                if (bitmapHolder.state == BitmapHolder.ERROR) {
                                    imageCachable.onFail(photoUrl);
                                    bitmapHolder.state = BitmapHolder.NEEDED;
                                } else {
                                    bitmapHolder.state = BitmapHolder.NEEDED;
                                    reLoadPhoto(imageCachable, photoUrl, pendingPriority);
                                }
                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

        public void setStop(boolean stop) {
            this.stop = stop;
        }
    }

    private class PhotoPriorityBlockingQueue extends LinkedBlockingQueue<PendingPhoto> {

        private static final long serialVersionUID = 1L;

        public boolean clean(PendingPhoto photo) {
            return remove(photo);
        }

        @Override
        public boolean offer(PendingPhoto e) {
            if (size() > 100) {
                poll();
            }
            return super.offer(e);
        }
    }

    @SuppressWarnings("rawtypes")
    private static class PendingPhoto implements Comparable {

        public WeakReference<ImageCachable> imageView;

        public int priority = 0;

        public String url;

        public int maxHeight;

        public int maxWidth;

        public UrlType urlType;

        public PendingPhoto(ImageCachable imgCachable, String url, int priority) {
            this.url = url;
            this.imageView = new WeakReference<ImageCachable>(imgCachable);
            this.priority = priority;
            this.maxHeight = imgCachable.getMaxRequiredHeight();
            this.maxWidth = imgCachable.getMaxRequiredWidth();
            this.maxHeight = maxHeight <= 0 ? DEFALUT_HEIGHT_SIZE : maxHeight;
            this.maxWidth = maxWidth <= 0 ? DEFALUT_WIDTH_SIZE : maxWidth;
        }

        @Override
        public int compareTo(Object obj) {
            if ((obj instanceof PendingPhoto)) {
                final PendingPhoto pendingPhoto = (PendingPhoto) obj;
                return this.priority - pendingPhoto.priority;
            }
            return -1;

        }

        @Override
        public boolean equals(Object obj) {

            if ((obj instanceof PendingPhoto)) {
                final PendingPhoto pendingPhoto = (PendingPhoto) obj;
                return pendingPhoto.imageView.get() == imageView.get();
            }
            return false;
        }
    }

    private boolean saveBitmapToLocalCache(InputStream in, File cachefile, ImageCachable imageCachable, String url, int fileLength, String resorceUrl)
            throws IOException {
        if (in == null) {
            return false;
        }

        OutputStream outStream = null;
        File tmpfile = new File(cachefile.getPath() + ".tmp"); // write to temp
        int totalLen = 0;
        try {
            File parent = tmpfile.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }
            tmpfile.createNewFile();
            outStream = new BufferedOutputStream(new FileOutputStream(tmpfile), 8192);
            byte[] buffer = new byte[1024 * 4];
            int len = 0;
            int percent = 0;
            while ((len = in.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
                totalLen += len;
                int p = totalLen * 100 / fileLength;
                if (p > percent) {
                    percent = p;
                    imageCachable.onProgress(resorceUrl, percent);
                }
            }
            outStream.flush();
            outStream.close();
            if (tmpfile.exists()) {
                tmpfile.renameTo(cachefile);
                return true;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            // Clear cache file directory.
        } finally {
            if (outStream != null) {
                outStream.close();
            }
            // Caculate Size.
        }
        return false;
    }

    public void removeCacheForUri(String uri) {
        try {
            mBitmapCache.remove(uri);
            String localCachePath = FileUtil.urlToLocalPath(uri);
            new File(localCachePath).delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkIsSameView(PendingPhoto pendingPhoto) {
        boolean flag = pendingPhoto.imageView.get() != null && pendingPhoto.url.equals(pendingPhoto.imageView.get().getUrl());
        return flag;
    }

    // 如果根据图片的时间戳发现超过一分钟没有再次被调用清除bitmap缓存
    private Runnable runnable = new Runnable() {

        @Override
        public void run() {
            mMainThreadHandler.removeCallbacks(this);
            long curTimeStamp = System.currentTimeMillis();
            Set<String> keySet = new HashSet<String>(mBitmapCache.snapshot().keySet());
            boolean needGc = false;
            for (Iterator<String> it = keySet.iterator(); it.hasNext();) {
                String key = it.next();
                BitmapHolder bitmapHolder = mBitmapCache.get(key);
                if (bitmapHolder != null && bitmapHolder.state == BitmapHolder.LOADED && (curTimeStamp - bitmapHolder.timeStamp) > 60 * 1000) {
                    mBitmapCache.remove(key);
                    if (DEBUG)
                        Log.d(LOG_TAG, "removed not used cache:" + key);
                    needGc = true;
                }
            }
            if (needGc) {
                System.gc();
            }
            if (DEBUG)
                Log.d(LOG_TAG, "cache size is:" + (mBitmapCache.size() / (float) 1024 / 1024));
            mMainThreadHandler.postDelayed(this, 50 * 1000);
        }
    };

    private class Cache extends LruCache<String, BitmapHolder> {

        public Cache(int maxSize) {
            super(maxSize);
        }

        @Override
        protected int sizeOf(String key, BitmapHolder t) {
            final Bitmap bitmap = t.bitmapRef;
            if (bitmap == null) {
                return 0;
            }
            return bitmap.getWidth() * bitmap.getWidth() * 4;
        }
    }

    public int getMaxCacheSize() {
        return maxCacheSize;
    }

    // 该线程是为了判断请求加载的图片是本地还是网络的然后再分发到不同的队列中
    class PendWorkThread extends Thread {

        private boolean pasuse = false;

        public void onResume() {
            pasuse = false;
            synchronized (this) {
                notify();
            }
        }

        public void onPause() {
            pasuse = true;
        }

        public PendWorkThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
            while (true) {
                synchronized (this) {
                    while (pasuse) {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                try {
                    PendingPhoto pendingPhoto = mBlockingQueue.take();
                    String url = pendingPhoto.url;
                    pendingPhoto.urlType = isDiskExist(url, pendingPhoto);
                    if (UrlType.HTTP != pendingPhoto.urlType) {
                        mLocalPriorityBlockingQueue.offer(pendingPhoto);
                    } else {
                        mPriorityBlockingQueue.offer(pendingPhoto);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    mBlockingQueue.clear();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

}
