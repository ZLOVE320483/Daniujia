package com.xiaojia.daniujia.ui.frag;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaojia.daniujia.ExtraConst;
import com.xiaojia.daniujia.FileStorage;
import com.xiaojia.daniujia.IntentKey;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.dlgs.AttachBottomDlg;
import com.xiaojia.daniujia.dlgs.BaseBottomDialog;
import com.xiaojia.daniujia.domain.resp.MessageEntity;
import com.xiaojia.daniujia.ui.act.ActChatFilePreView;
import com.xiaojia.daniujia.ui.act.ActTransmitList;
import com.xiaojia.daniujia.ui.act.WebActivity;
import com.xiaojia.daniujia.ui.views.orderprogress.HorizontalProgressBar;
import com.xiaojia.daniujia.utils.ApplicationUtil;
import com.xiaojia.daniujia.utils.FileUtil;
import com.xiaojia.daniujia.utils.SysUtil;
import com.xiaojia.daniujia.utils.download.DownloadProgressListener;
import com.xiaojia.daniujia.utils.download.FileDownloader;

import java.io.File;

/**
 * Created by Administrator on 2016/9/19.
 */
public class FilePreViewFragment extends BaseFragment implements View.OnClickListener {

    private static final int DOWNLOAD_NOT_START = 0;
    private static final int DOWNLOAD_IN_PROCESS = 1;
    public static final int DOWNLOAD_SUCCESS = 2;

    private int downloadStatus = DOWNLOAD_NOT_START;

    private static final int PROCESSING = 1;
    private static final int FAILURE = -1;

    private MessageEntity.ContentEntity contentEntity;
    private ImageView mIvIcon;
    private TextView mTvFileName;
    private TextView mTvFileSize;
    private HorizontalProgressBar progressBar;
    private ImageView mIvCancel;
    private ImageView mIvRight;
    private RelativeLayout mRlDownload;
    private TextView mTvDownLoad;

    private String preViewUrl;
    private String otherUrl;
    private String localFileName;
    private String localPath;
    private File saveDir;
    boolean isFileBig = false;
    private String fileSize;

    private BaseBottomDialog.OnDialogClickListener listener = new BaseBottomDialog.OnDialogClickListener() {
        @Override
        public void onOK(String s) {
            if (s != null) {
                if (TextUtils.equals("open", s)) {
                    FileUtil.openFile(localPath, getActivity(), contentEntity.getMimeType());
                } else if (TextUtils.equals("send", s)) {
                    Intent intent = new Intent(getActivity(), ActTransmitList.class);
                    intent.putExtra(IntentKey.INTENT_KEY_ATTACH_ITEM, contentEntity);
                    startActivity(intent);
                }
            }
        }
    };

    @Override
    protected int getInflateLayout() {
        return R.layout.frag_file_pre_view;
    }

    @Override
    protected void setUpView(View view) {
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            if (intent.hasExtra(IntentKey.INTENT_KEY_ATTACH_ITEM)) {
                contentEntity = (MessageEntity.ContentEntity) intent.getSerializableExtra(IntentKey.INTENT_KEY_ATTACH_ITEM);
            }
        }
        setBackButton(view.findViewById(R.id.id_back));
        ((TextView) view.findViewById(R.id.id_title)).setText("文件预览");
        mIvIcon = (ImageView) view.findViewById(R.id.preview_iv_icon);
        mTvFileName = (TextView) view.findViewById(R.id.preview_tv_name);
        mTvFileSize = (TextView) view.findViewById(R.id.preview_tv_size);
        mIvRight = (ImageView) view.findViewById(R.id.id_expert_home);
        mRlDownload = (RelativeLayout) view.findViewById(R.id.preview_rl_download);
        mTvDownLoad = (TextView) view.findViewById(R.id.preview_tv_download);
        mIvCancel = (ImageView) view.findViewById(R.id.preview_iv_cancel);
        progressBar = (HorizontalProgressBar) view.findViewById(R.id.preview_hp_download_rate);

        mTvDownLoad.setOnClickListener(this);
        mIvRight.setOnClickListener(this);
        mIvCancel.setOnClickListener(this);

        initData();
    }

    private void initData() {
        saveDir = new File(FileStorage.APP_ATTACH_DIR);
        mIvRight.setImageResource(R.drawable.document_more);
        progressBar.setProgress(0);

        if (contentEntity != null) {
            if (!TextUtils.isEmpty(contentEntity.getFilename())) {
                mTvFileName.setText(contentEntity.getFilename());
                mIvIcon.setImageResource(FileUtil.getFileType(contentEntity.getFilename(), true));
            }
            if (!TextUtils.isEmpty(contentEntity.getPreviewUrl())) {
                preViewUrl = contentEntity.getPreviewUrl();
            }
            if (!TextUtils.isEmpty(contentEntity.getUrl())) {
                otherUrl = contentEntity.getUrl();
            }
            fileSize = FileUtil.formatFileSize(contentEntity.getSize());
            mTvFileSize.setText(fileSize);
        }
        preViewUrl = TextUtils.isEmpty(preViewUrl) ? otherUrl : preViewUrl;
        localFileName = preViewUrl.substring(preViewUrl.lastIndexOf("/") + 1);
        isFileBig = FileUtil.isBiggerTo5M(contentEntity.getSize());

        if (!TextUtils.isEmpty(localFileName)) {
            localPath = FileStorage.APP_ATTACH_DIR + File.separator + localFileName;
            File localFile = new File(localPath);
            if (localFile.exists()) {
                if (SysUtil.getIntPref(preViewUrl) == DOWNLOAD_SUCCESS) {
                    downloadStatus = DOWNLOAD_SUCCESS;
                    mRlDownload.setVisibility(View.GONE);
                    mTvDownLoad.setVisibility(View.VISIBLE);
                    mTvDownLoad.setText("预览文件");
                    preViewFile();
                } else {
                    downloadStatus = DOWNLOAD_IN_PROCESS;
                    mRlDownload.setVisibility(View.GONE);
                    mTvDownLoad.setVisibility(View.VISIBLE);
                    mTvDownLoad.setText("继续下载");
                    String localFileSize = FileUtil.formatFileSize(FileUtil.getFileSize(localFile));
                    mTvFileSize.setText(localFileSize + "/" + fileSize);
                }
            } else {
                if (isFileBig) {
                    mRlDownload.setVisibility(View.GONE);
                    mTvDownLoad.setVisibility(View.VISIBLE);
                    mTvDownLoad.setText("开始下载");
                } else {
                    // 直接下载
                    startDownload();
                }
            }
        }
    }

    private AttachBottomDlg bottomDlg = null;

    @Override
    public void onClick(View v) {
        if (v == mTvDownLoad) {
            if (downloadStatus == DOWNLOAD_NOT_START || downloadStatus == DOWNLOAD_IN_PROCESS) {
                startDownload();
            } else {
                preViewFile();
            }
        } else if (v == mIvRight) {
            if (bottomDlg == null) {
                bottomDlg = new AttachBottomDlg(getActivity());
                bottomDlg.setOnDialogClickListener(listener);
            }
            bottomDlg.initOpenButton(downloadStatus);
            bottomDlg.show();
        } else if (v == mIvCancel) {
            stopDownload();
        }
    }

    private void preViewFile() {
        if (localFileName.endsWith(".pdf") || !preViewUrl.equals(otherUrl)) {
            Intent intent = new Intent(getActivity(), ActChatFilePreView.class);
            intent.putExtra(IntentKey.INTENT_KEY_PRE_VIEW_FILE_PATH, localPath);
            intent.putExtra(IntentKey.INTENT_KEY_ATTACH_ITEM, contentEntity);
            startActivity(intent);
        } else {
            if (localFileName.endsWith("txt")) {
                Intent intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra(ExtraConst.EXTRA_WEB_TITLE, "文件预览");
                intent.putExtra(ExtraConst.EXTRA_WEB_URL, otherUrl);
                startActivity(intent);
            } else {
                FileUtil.openFile(localPath, getActivity(), contentEntity.getMimeType());
            }
        }
        finishActivity();
    }

    private void startDownload() {
        downloadStatus = DOWNLOAD_IN_PROCESS;
        mRlDownload.setVisibility(View.VISIBLE);
        mTvDownLoad.setVisibility(View.GONE);
        download(preViewUrl, saveDir);
    }

    private void stopDownload() {
        downloadStatus = DOWNLOAD_IN_PROCESS;
        exit();
        mRlDownload.setVisibility(View.GONE);
        mTvDownLoad.setVisibility(View.VISIBLE);
        mTvDownLoad.setText("继续下载");
    }



    private DownloadTask task;
    private Handler handler = new UIHandler();

    private void exit() {
        if (task != null)
            task.exit();
    }

    private void download(String path, File savDir) {
        task = new DownloadTask(path, savDir);
        new Thread(task).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SysUtil.savePref(preViewUrl, downloadStatus);
        stopDownload();
    }

    /**
     *
     * UI控件画面的重绘(更新)是由主线程负责处理的，如果在子线程中更新UI控件的值，更新后的值不会重绘到屏幕上
     * 一定要在主线程里更新UI控件的值，这样才能在屏幕上显示出来，不能在子线程中更新UI控件的值
     *
     */
    private final class DownloadTask implements Runnable {
        private String path;
        private File saveDir;
        private FileDownloader loader;

        public DownloadTask(String path, File saveDir) {
            this.path = path;
            this.saveDir = saveDir;
        }

        /**
         * 退出下载
         */
        public void exit() {
            if (loader != null)
                loader.exit();
        }

        DownloadProgressListener downloadProgressListener = new DownloadProgressListener() {
            @Override
            public void onDownloadSize(int size) {
                Message msg = new Message();
                msg.what = PROCESSING;
                msg.getData().putInt("size", size);
                handler.sendMessage(msg);
            }
        };

        public void run() {
            try {
                // 实例化一个文件下载器
                loader = new FileDownloader(ApplicationUtil.getApplicationContext(), path, saveDir, 3);
                // 设置进度条最大值
                progressBar.setMax(loader.getFileSize());
                loader.download(downloadProgressListener);
            } catch (Exception e) {
                e.printStackTrace();
//                handler.sendMessage(handler.obtainMessage(FAILURE)); // 发送一条空消息对象
            }
        }
    }

    private final class UIHandler extends Handler {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PROCESSING: // 更新进度
                    int size = msg.getData().getInt("size");
                    mTvFileSize.setText(FileUtil.formatFileSize(size) + "/" + FileUtil.formatFileSize(contentEntity.getSize()));
                    progressBar.setProgress(size);
                    if (progressBar.getProgress() == progressBar.getMax()) {
                        mTvFileSize.setText(fileSize + "/" + fileSize);
                        downloadStatus = DOWNLOAD_SUCCESS;
                        mRlDownload.setVisibility(View.GONE);
                        mTvDownLoad.setVisibility(View.VISIBLE);
                        mTvDownLoad.setText("预览文件");
                    }
                    break;
                case FAILURE: // 下载失败
                    showShortToast("下载失败");
                    break;
            }
        }
    }

}
