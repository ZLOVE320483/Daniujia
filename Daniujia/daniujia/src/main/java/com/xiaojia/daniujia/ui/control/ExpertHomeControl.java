package com.xiaojia.daniujia.ui.control;


import android.text.TextUtils;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Request;
import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.dlgs.AlertBigMsgDlg;
import com.xiaojia.daniujia.domain.resp.BaseRespVo;
import com.xiaojia.daniujia.domain.resp.ExpertHomeVo;
import com.xiaojia.daniujia.domain.resp.QRCodeVo;
import com.xiaojia.daniujia.managers.OkHttpClientManager;
import com.xiaojia.daniujia.ui.frag.ExpertHomeFragment;
import com.xiaojia.daniujia.utils.SysUtil;

import org.json.JSONObject;

/**
 * Created by ZLOVE on 2016/5/5.
 */
public class ExpertHomeControl extends BaseControl<ExpertHomeFragment> {

    private AlertBigMsgDlg dlg;

    private String txtConsultTip;
    private String phoneConsultTip;
    private String offlineConsultTip;

    public void requestExpertInfo(int expertId) {
        String url = Config.WEB_API_SERVER + "/consultant/detail/" + expertId + "/" + SysUtil.getIntPref(PrefKeyConst.PREF_USER_ID);
        if (SysUtil.getBooleanPref(PrefKeyConst.PREF_IS_LOGIN)) {
            OkHttpClientManager.getInstance(getAttachedActivity()).getWithToken(true, url, new OkHttpClientManager.ResultCallback<ExpertHomeVo>() {
                @Override
                public void onResponse(ExpertHomeVo result) {
                    if (result.getReturncode().equals("SUCCESS")) {
                        getFragment().setExpertData(result);
                    } else {
                        showShortToast(result.getReturnmsg());
                    }
                }

                @Override
                public void onError(Request paramRequest, Exception e) {
                    super.onError(paramRequest, e);
                }

                @Override
                public void onFail(String errorMsg) {
                    super.onFail(errorMsg);
                }
            });
        } else {
            OkHttpClientManager.getInstance(getAttachedActivity()).getWithPlatform(true, url, new OkHttpClientManager.ResultCallback<ExpertHomeVo>() {
                @Override
                public void onResponse(ExpertHomeVo result) {
                    if (result.getReturncode().equals("SUCCESS")) {
                        getFragment().setExpertData(result);
                    } else {
                        showShortToast(result.getReturnmsg());
                    }
                }

                @Override
                public void onError(Request paramRequest, Exception e) {
                    super.onError(paramRequest, e);
                }

                @Override
                public void onFail(String errorMsg) {
                    super.onFail(errorMsg);
                }
            });
        }

    }

    public void reqeustQRCode(int expertId) {
        String url = Config.WEB_API_SERVER + "/user/qrCode?userId=" + expertId;
        OkHttpClientManager.getInstance(getAttachedActivity()).getWithPlatform(true, url, new OkHttpClientManager.ResultCallback<QRCodeVo>() {
            @Override
            public void onResponse(QRCodeVo result) {
                if (result.getReturncode().equals("SUCCESS")) {
                    getFragment().setQRCode(result);
                } else {
                    showShortToast(result.getReturnmsg());
                }
            }
        });
    }

    public void collectExpertRequest(int expertId, final boolean isCollected) {
        String url = Config.WEB_API_SERVER;
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("userid", String.valueOf(SysUtil.getIntPref(PrefKeyConst.PREF_USER_ID)));
        builder.add("consultant_id", String.valueOf(expertId));
        if (!isCollected) {    //  未收藏
            url += "/user/consultuser/favorite/add";
        } else {    //已收藏
            url += "/user/consultuser/favorite/del";
        }
        OkHttpClientManager.getInstance(getAttachedActivity()).postWithToken(url, new OkHttpClientManager.ResultCallback<BaseRespVo>() {
            @Override
            public void onResponse(BaseRespVo result) {
                if (result.getReturncode().equals("SUCCESS")) {
                    getFragment().setCollectResp();
                } else {
                    showShortToast(result.getReturnmsg());
                }
            }

            @Override
            public void onError(Request paramRequest, Exception e) {
                super.onError(paramRequest, e);
                if (!isCollected) {
                    showShortToast(R.string.favor_failed);
                } else {
                    showShortToast(R.string.del_favor_failed);
                }
            }

            @Override
            public void onFail(String errorMsg) {
                super.onFail(errorMsg);
                if (!isCollected) {
                    showShortToast(R.string.favor_failed);
                } else {
                    showShortToast(R.string.del_favor_failed);
                }
            }
        }, builder);
    }

    public void getTxtConsultTip() {
        if (TextUtils.isEmpty(txtConsultTip)) {
            String url = Config.WEB_API_SERVER_V3 + "/app/tip/buy_message_tip";
            OkHttpClientManager.getInstance(getAttachedActivity()).getWithPlatform(url, new OkHttpClientManager.ResultCallback<String>() {
                @Override
                public void onResponse(String result) {
                    if (TextUtils.isEmpty(result)) {
                        return;
                    }
                    result = result.replace("\\n", "n");
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        txtConsultTip = jsonObject.getString("tipContent");
                        showTipDialog(1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            showTipDialog(1);
        }
    }

    public void getPhoneConsultTip() {
        if (TextUtils.isEmpty(phoneConsultTip)) {
            String url = Config.WEB_API_SERVER_V3 + "/app/tip/buy_tel_tip";
            OkHttpClientManager.getInstance(getAttachedActivity()).getWithPlatform(url, new OkHttpClientManager.ResultCallback<String>() {
                @Override
                public void onResponse(String result) {
                    if (TextUtils.isEmpty(result)) {
                        return;
                    }
                    result = result.replace("\\n", "n");
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        phoneConsultTip = jsonObject.getString("tipContent");
                        showTipDialog(2);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            showTipDialog(2);
        }
    }

    public void getOfflineConsultTip() {
        if (TextUtils.isEmpty(offlineConsultTip)) {
            String url = Config.WEB_API_SERVER_V3 + "/app/tip/buy_offline_tip";
            OkHttpClientManager.getInstance(getAttachedActivity()).getWithPlatform(url, new OkHttpClientManager.ResultCallback<String>() {
                @Override
                public void onResponse(String result) {
                    if (TextUtils.isEmpty(result)) {
                        return;
                    }
                    result = result.replace("\\n", "n");
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        offlineConsultTip = jsonObject.getString("tipContent");
                        showTipDialog(3);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            showTipDialog(3);
        }
    }

    private void showTipDialog(int type) {
        if (dlg == null) {
            dlg = new AlertBigMsgDlg(getAttachedActivity());
        }
        if (type == 1) {
            dlg.initDlg(R.drawable.ic_question_blue, "图文约谈", txtConsultTip);
        } else if (type == 2) {
            dlg.initDlg(R.drawable.ic_question_blue, "电话约谈", phoneConsultTip);
        } else if (type == 3) {
            dlg.initDlg(R.drawable.ic_question_blue, "线下约谈", offlineConsultTip);
        }
        dlg.show();
    }


    public void callPhone(int demandId, int expertId, String type) {
        String url = Config.WEB_API_SERVER + "/demand/pstn";
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("demandId", String.valueOf(demandId));
        builder.add("expertId", String.valueOf(expertId));
        builder.add("type", String.valueOf(type));
        OkHttpClientManager.getInstance(getAttachedActivity()).postWithToken(url, new OkHttpClientManager.ResultCallback<BaseRespVo>() {
            @Override
            public void onResponse(BaseRespVo result) {
                if (result.getReturncode().equals("SUCCESS")) {
                } else {
                    showShortToast(result.getReturnmsg());
                }
            }
        }, builder);
    }
}
