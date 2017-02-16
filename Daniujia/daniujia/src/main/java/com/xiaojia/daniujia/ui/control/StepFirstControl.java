package com.xiaojia.daniujia.ui.control;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.squareup.okhttp.FormEncodingBuilder;
import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.base.App;
import com.xiaojia.daniujia.dlgs.BaseBottomDialog;
import com.xiaojia.daniujia.domain.resp.BaseRespVo;
import com.xiaojia.daniujia.domain.resp.BeExpertBaseInfo;
import com.xiaojia.daniujia.managers.OkHttpClientManager;
import com.xiaojia.daniujia.managers.ThreadWorker;
import com.xiaojia.daniujia.ui.frag.StepFirstToBeExpertNew;
import com.xiaojia.daniujia.utils.LogUtil;
import com.xiaojia.daniujia.utils.QiniuUtil;
import com.xiaojia.daniujia.utils.WUtil;

import org.json.JSONObject;

import java.io.File;
import java.util.List;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * Created by Administrator on 2016/5/24.
 */
public class StepFirstControl extends BaseControl<StepFirstToBeExpertNew> implements View.OnClickListener {

    private BaseBottomDialog mDialog;
    private static final int HEADER_IMAGE_REQUEST = 1010;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void requestBaseInfo() {
        String url = Config.WEB_API_SERVER_V3 + "/become/base/info";
        OkHttpClientManager.getInstance(getAttachedActivity()).getWithToken(url, new OkHttpClientManager.ResultCallback<BeExpertBaseInfo>() {
            @Override
            public void onResponse(BeExpertBaseInfo result) {
                getFragment().refreshDate(result);
            }
        });
    }

    public void modifyAvatar() {
        LinearLayout mView = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.dlg_bottom_choose_pic, null);
        Button mPickPhoto = (Button) mView.findViewById(R.id.choose_from_album);
        Button mTakePhoto = (Button) mView.findViewById(R.id.take_photo);
        Button mCancel = (Button) mView.findViewById(R.id.dlg_cancel);
        mCancel.setOnClickListener(this);
        mPickPhoto.setOnClickListener(this);
        mTakePhoto.setOnClickListener(this);

        mDialog = new BaseBottomDialog(getAttachedActivity(), mView);
        mDialog.show();
    }

    public void saveBigAvatarUrl(String imgUrl) {
        String url = Config.WEB_API_SERVER_V3 +"/user/images";
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("fullAvatarUrl", imgUrl);
        OkHttpClientManager.getInstance(getAttachedActivity()).postWithToken(false, url, new OkHttpClientManager.ResultCallback<BaseRespVo>() {
            @Override
            public void onResponse(BaseRespVo result) {
                if (result.getReturncode().equals("SUCCESS")) {
                    LogUtil.d("ZLOVE", "upload full avatar success");
                } else {
                    showShortToast(result.getReturnmsg());
                }
            }
        }, builder);
    }

    private void saveAvatarSourcePic(String sourcePath) {
        final File file = new File(sourcePath);
        ThreadWorker.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    QiniuUtil.upload(file, WUtil.genAvatarFileName(), new UpCompletionHandler() {
                        @Override
                        public void complete(String s, ResponseInfo responseInfo, JSONObject jsonObject) {
                            if (responseInfo != null && responseInfo.isOK()) {
                                String url = QiniuUtil.getResUrl(s);
                                saveBigAvatarUrl(url);
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.choose_from_album:
                App.initHeaderConfig();
                GalleryFinal.openGallerySingle(HEADER_IMAGE_REQUEST, mOnHandlerResultCallback);
                break;
            case R.id.take_photo:
                App.initHeaderConfig();
                GalleryFinal.openCamera(HEADER_IMAGE_REQUEST, mOnHandlerResultCallback);
                break;
            case R.id.dlg_cancel:
                break;
        }
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    GalleryFinal.OnHandlerResultCallback mOnHandlerResultCallback = new GalleryFinal.OnHandlerResultCallback() {
        @Override
        public void onHandlerSuccess(int requestCode, List<PhotoInfo> resultList) {
            getFragment().refreshHeader(resultList.get(0).getPhotoPath());
            saveAvatarSourcePic(resultList.get(0).getSourcePhotoPath());
        }

        @Override
        public void onHandlerFailure(int requestCode, String errorMsg) {}

    };



}
