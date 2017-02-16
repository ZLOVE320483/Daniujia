package com.xiaojia.daniujia.ui.control;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.squareup.okhttp.FormEncodingBuilder;
import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.base.App;
import com.xiaojia.daniujia.dlgs.BaseBottomDialog;
import com.xiaojia.daniujia.domain.resp.BaseRespVo;
import com.xiaojia.daniujia.domain.resp.UserBasicInfoRespVo;
import com.xiaojia.daniujia.managers.OkHttpClientManager;
import com.xiaojia.daniujia.managers.ThreadWorker;
import com.xiaojia.daniujia.ui.frag.UserBasicInfoFragment;
import com.xiaojia.daniujia.utils.LogUtil;
import com.xiaojia.daniujia.utils.QiniuUtil;
import com.xiaojia.daniujia.utils.SysUtil;
import com.xiaojia.daniujia.utils.WUtil;

import org.json.JSONObject;

import java.io.File;
import java.util.List;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * Created by ZLOVE on 2016/6/29.
 */
public class UserBasicInfoControl extends BaseControl<UserBasicInfoFragment> implements View.OnClickListener {

    private BaseBottomDialog mDialog;
    private static final int HEADER_IMAGE_REQUEST = 1010;

    public void initData() {
        String url = Config.WEB_API_SERVER_V3 + "/become/base/info";
        OkHttpClientManager.getInstance(getAttachedActivity()).getWithToken(true, url, new OkHttpClientManager.ResultCallback<UserBasicInfoRespVo>() {
            @Override
            public void onResponse(UserBasicInfoRespVo result) {
                getFragment().setData(result);
            }
        });
    }

    public void saveBasicInfoRequest(final String imgUrl, final String name, String workAge, String company, String position, String cityName, String cityCode, int gender) {
        String url = Config.WEB_API_SERVER_V3 +"/become/base/info";
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("flag", "1");
        builder.add("imgurl", imgUrl);
        builder.add("name", name);
        builder.add("workage", workAge);
        builder.add("company", company);
        builder.add("position", position);
        builder.add("cityName", cityName);
        builder.add("cityCode", cityCode);
        builder.add("gender", String.valueOf(gender));
        OkHttpClientManager.getInstance(getAttachedActivity()).postWithToken(url, new OkHttpClientManager.ResultCallback<BaseRespVo>() {
            @Override
            public void onResponse(BaseRespVo result) {
                if (result.getReturncode().equals("SUCCESS")) {
                    showShortToast("保存成功");
                    SysUtil.savePref(PrefKeyConst.PREF_USER_AVATAR, imgUrl);
                    SysUtil.savePref(PrefKeyConst.PREF_NAME, name);
                    finishActivity();
                } else {
                    showShortToast(result.getReturnmsg());
                }
            }
        }, builder);
    }

    public void saveBigAvatarUrl(String imgUrl) {
        String url = Config.WEB_API_SERVER_V3 +"/user/images";
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("fullAvatarUrl", imgUrl);
        OkHttpClientManager.getInstance(getAttachedActivity()).postWithToken(url, new OkHttpClientManager.ResultCallback<BaseRespVo>() {
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

    public void modifyAvatar() {
        LinearLayout mView = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.dlg_bottom_choose_pic, null);
        Button mPickPhoto = (Button) mView.findViewById(R.id.choose_from_album);
        Button mTakePhoto = (Button) mView.findViewById(R.id.take_photo);
        Button mCancel = (Button) mView.findViewById(R.id.dlg_cancel);
        mCancel.setOnClickListener(this);
        mPickPhoto.setOnClickListener(this);
        mTakePhoto.setOnClickListener(this);

        if (mDialog == null) {
            mDialog = new BaseBottomDialog(getAttachedActivity(), mView);
        }
        mDialog.show();
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

}
