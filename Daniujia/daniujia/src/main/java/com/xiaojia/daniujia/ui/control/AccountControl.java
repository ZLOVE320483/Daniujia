package com.xiaojia.daniujia.ui.control;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.squareup.okhttp.FormEncodingBuilder;
import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.base.App;
import com.xiaojia.daniujia.dlgs.BaseBottomDialog;
import com.xiaojia.daniujia.dlgs.IndustryAndFunctionDlg;
import com.xiaojia.daniujia.domain.resp.BaseInfoVo;
import com.xiaojia.daniujia.domain.resp.IndustryAndFunctionVo;
import com.xiaojia.daniujia.domain.resp.MyTags;
import com.xiaojia.daniujia.managers.OkHttpClientManager;
import com.xiaojia.daniujia.ui.frag.AccountFragment;
import com.xiaojia.daniujia.utils.SysUtil;

import java.util.List;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * Created by Administrator on 2016/5/9
 */
public class AccountControl extends BaseControl<AccountFragment> implements View.OnClickListener {

    private BaseBottomDialog mDialog;
    private static final int HEADER_IMAGE_REQUEST = 1010;
    private static IndustryAndFunctionDlg dlg;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void initData() {
        String url = Config.WEB_API_SERVER_V3 + "/user/base/info/detail/" + SysUtil.getIntPref(PrefKeyConst.PREF_USER_ID);
        OkHttpClientManager.getInstance(getAttachedActivity()).getWithToken(true, url, new OkHttpClientManager.ResultCallback<BaseInfoVo>() {
            @Override
            public void onResponse(BaseInfoVo result) {
                getFragment().refreshData(result);
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

    GalleryFinal.OnHandlerResultCallback mOnHandlerResultCallback = new GalleryFinal.OnHandlerResultCallback() {
        @Override
        public void onHandlerSuccess(int requestCode, List<PhotoInfo> resultList) {
            getFragment().refreshHeader(resultList.get(0).getPhotoPath());
        }

        @Override
        public void onHandlerFailure(int requestCode, String errorMsg) {}

    };

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

    public void showIndustry() {
        if (getFragment().result != null) {
            dlg = new IndustryAndFunctionDlg(getAttachedActivity(), getFragment().userInfo.getNovationName(),
                    getFragment().userInfo.getFunctionName(), getFragment().result);
            dlg.setOnDialogClickListener(getFragment().bottomDlgClickListener);
            if (!dlg.isShowing()) {
                dlg.show();
            }
            return;
        }
        String url = Config.WEB_API_SERVER_V3 + "/novation/list/and/function/list";
        OkHttpClientManager.getInstance(getAttachedActivity()).getWithPlatform(true, url,
                new OkHttpClientManager.ResultCallback<IndustryAndFunctionVo>() {
                    @Override
                    public void onResponse(IndustryAndFunctionVo result) {
                        dlg = new IndustryAndFunctionDlg(getAttachedActivity(),
                                getFragment().userInfo.getNovationName(), getFragment().userInfo.getFunctionName(), result);
                        getFragment().cacheIndustryData(result);
                        dlg.setOnDialogClickListener(getFragment().bottomDlgClickListener);
                        if (!dlg.isShowing()) {
                            dlg.show();
                        }
                    }
                });
    }

    public void saveData(final BaseInfoVo.UserEntity userInfo) {
        String url = Config.WEB_API_SERVER_V3 +"/user/base/info/detail";
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("userId", SysUtil.getPref(PrefKeyConst.PREF_USER_ID) + "");
        builder.add("imgurl", userInfo.getImgurl());
        builder.add("name", userInfo.getName());
        builder.add("workage", userInfo.getWorkage() + "");
        builder.add("functionId",userInfo.getFunctionId()+"");
        builder.add("company", userInfo.getCompany());
        builder.add("position", userInfo.getPosition() + "");
        builder.add("gender", userInfo.getGender()+"");
        OkHttpClientManager.getInstance(getAttachedActivity()).postWithToken(url, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onResponse(String result) {
                showShortToast("更新成功");
                SysUtil.savePref(PrefKeyConst.PREF_USER_AVATAR, userInfo.getImgurl());
                SysUtil.savePref(PrefKeyConst.PREF_NAME, userInfo.getName());
                finishActivity();
            }
        }, builder);
    }

    public void modifyTag(int functionId) {
        String url = Config.WEB_API_SERVER_V3 + "/user/my/tags/by/function/" +functionId;
        OkHttpClientManager.getInstance(getAttachedActivity()).getWithToken(url, new OkHttpClientManager.ResultCallback<MyTags>() {
            @Override
            public void onResponse(MyTags myTags ) {
                getFragment().modifyLabels(myTags.getMyTags());
            }
        });
    }
}
