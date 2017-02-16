package com.xiaojia.daniujia.ui.frag;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.xiaojia.daniujia.IntentKey;
import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.domain.UserInfo;
import com.xiaojia.daniujia.domain.resp.LoginFailResp;
import com.xiaojia.daniujia.domain.resp.LoginSuccessBean;
import com.xiaojia.daniujia.mqtt.MqttMsgCallbackHandler;
import com.xiaojia.daniujia.mqtt.MqttUtils;
import com.xiaojia.daniujia.mqtt.service.MqttServiceConstants;
import com.xiaojia.daniujia.ui.act.ActForgetPwd;
import com.xiaojia.daniujia.ui.act.ActMain;
import com.xiaojia.daniujia.ui.act.ActRegister;
import com.xiaojia.daniujia.ui.act.PersonInfoBindActivity;
import com.xiaojia.daniujia.ui.control.LoginControl;
import com.xiaojia.daniujia.ui.views.CommonDialog;
import com.xiaojia.daniujia.utils.BitmapUtil;
import com.xiaojia.daniujia.utils.DialogManager;
import com.xiaojia.daniujia.utils.JsonUtil;
import com.xiaojia.daniujia.utils.LogUtil;
import com.xiaojia.daniujia.utils.SysUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * Created by ZLOVE on 2016/4/29.
 */
public class LoginFragment extends BaseFragment implements View.OnClickListener, TagAliasCallback, View.OnLayoutChangeListener {

    private static final int ANIMATION_DURATION = 400;
    private static final int REQ_BIND_USER = 1;

    private LoginControl loginControl;
    private Handler mHandler = new Handler();

    //Activity最外层的Layout视图
    private View activityRootView;
    //屏幕高度
    private int screenHeight = 0;
    //软件盘弹起后所占高度阀值
    private int keyHeight = 0;

    private View logoView;
    private Animation showAnim;
    private Animation hideAnim;

    private View titleView;
    private View backView;

    private EditText etAccount;
    private ImageView ivAccountDelete;
    private EditText etPassword;
    private ImageView ivPasswordDelete;
    private View verCodeContainer;
    private EditText etVerCode;
    private ImageView ivVerifyCode;
    private ImageView ivRefresh;
    private TextView tvForgetPwd;
    private TextView tvRegister;
    private Button btnLogin;
    private ImageView ivWeiboLogin;
    private ImageView ivWeiXinLogin;
    private ImageView ivQqLogin;

    private String account;
    private String password;
    private String verCode = "";
    private boolean isFromConflict = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginControl = new LoginControl();
        setControl(loginControl);
        ShareSDK.initSDK(getActivity());
    }

    @Override
    protected int getInflateLayout() {
        return R.layout.frag_login_new;
    }

    @Override
    protected void setUpView(View view) {
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            if (intent.hasExtra(IntentKey.INTENT_KEY_IS_FORM_CONFLICT)) {
                isFromConflict = intent.getBooleanExtra(IntentKey.INTENT_KEY_IS_FORM_CONFLICT, false);
            }
        }

        activityRootView = view.findViewById(R.id.root_layout);
        //获取屏幕高度
        screenHeight = getActivity().getWindowManager().getDefaultDisplay().getHeight();
        //阀值设置为屏幕高度的1/3
        keyHeight = screenHeight / 3;
        activityRootView.addOnLayoutChangeListener(this);

        titleView = view.findViewById(R.id.title_container);
        backView = view.findViewById(R.id.id_back);
        backView.setOnClickListener(this);
        logoView = view.findViewById(R.id.logo_container);
        etAccount = (EditText) view.findViewById(R.id.id_account);
        ivAccountDelete = (ImageView) view.findViewById(R.id.iv_account_delete);
        etPassword = (EditText) view.findViewById(R.id.id_password);
        ivPasswordDelete = (ImageView) view.findViewById(R.id.iv_pwd_delete);
        verCodeContainer = view.findViewById(R.id.verify_code_container);
        etVerCode = (EditText) view.findViewById(R.id.id_verify_code);
        ivVerifyCode = (ImageView) view.findViewById(R.id.iv_verify_code);
        ivRefresh = (ImageView) view.findViewById(R.id.iv_refresh);
        tvForgetPwd = (TextView) view.findViewById(R.id.id_forget_pwd);
        tvRegister = (TextView) view.findViewById(R.id.id_register);
        btnLogin = (Button) view.findViewById(R.id.id_login);
        ivWeiboLogin = (ImageView) view.findViewById(R.id.iv_weibo_login);
        ivWeiXinLogin = (ImageView) view.findViewById(R.id.iv_weixin_login);
        ivQqLogin = (ImageView) view.findViewById(R.id.iv_qq_login);

        ivAccountDelete.setOnClickListener(this);
        ivPasswordDelete.setOnClickListener(this);
        ivRefresh.setOnClickListener(this);
        tvForgetPwd.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        ivWeiboLogin.setOnClickListener(this);
        ivWeiXinLogin.setOnClickListener(this);
        ivQqLogin.setOnClickListener(this);

        account = SysUtil.getPref(PrefKeyConst.PREF_ACCOUNT);
        password = SysUtil.getPref(PrefKeyConst.PREF_PASSWORD);
        if (!TextUtils.isEmpty(account)) {
            etAccount.setText(account);
        }
        showAnim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        hideAnim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, -1.0f);
        showAnim.setDuration(ANIMATION_DURATION);
        hideAnim.setDuration(ANIMATION_DURATION);

        showAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                titleView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                logoView.setVisibility(View.VISIBLE);
                backView.setVisibility(View.VISIBLE);
                titleView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        hideAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                backView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                logoView.setVisibility(View.GONE);
                titleView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        etAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 0) {
                    etPassword.setText("");
                    ivAccountDelete.setVisibility(View.GONE);
                } else {
                    ivAccountDelete.setVisibility(View.VISIBLE);
                }
            }
        });

        etPassword.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 0) {
                    ivPasswordDelete.setVisibility(View.GONE);
                } else {
                    ivPasswordDelete.setVisibility(View.VISIBLE);
                }
            }
        });

        etAccount.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String str = etAccount.getText().toString();
                if (str.length() > 0) {
                    Editable etext = etAccount.getText();
                    int position = etext.length();
                    Selection.setSelection(etext, position);
                }
                if (hasFocus && str.length() > 0) {
                    ivAccountDelete.setVisibility(View.VISIBLE);
                } else {
                    ivAccountDelete.setVisibility(View.GONE);
                }
            }
        });

        etPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String str = etPassword.getText().toString();
                if (str.length() > 0) {
                    Editable etext = etPassword.getText();
                    int position = etext.length();
                    Selection.setSelection(etext, position);
                }
                if (hasFocus && str.length() > 0) {
                    ivPasswordDelete.setVisibility(View.VISIBLE);
                } else {
                    ivPasswordDelete.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == ivAccountDelete) {
            etAccount.setText("");
            etPassword.setText("");
        } else if (v == ivPasswordDelete) {
            etPassword.setText("");
        } else if (v == btnLogin) {
            account = etAccount.getText().toString().trim();
            password = etPassword.getText().toString().trim();
            verCode = etVerCode.getText().toString().trim();
            loginControl.doLogin(account, password, verCode);
        } else if (v == ivWeiboLogin) {
            Platform sinaWeibo = ShareSDK.getPlatform(SinaWeibo.NAME);
            authorize(sinaWeibo);
        } else if (v == ivWeiXinLogin) {
            IWXAPI wxApi = WXAPIFactory.createWXAPI(getActivity(), null);
            if (wxApi.isWXAppInstalled() && wxApi.isWXAppSupportAPI()) {
                Platform weixin = ShareSDK.getPlatform(Wechat.NAME);
                authorize(weixin);
            } else {
                showShortToast("请先安装微信");
            }
        } else if (v == ivQqLogin) {
            Platform qq = ShareSDK.getPlatform(QQ.NAME);
            authorize(qq);
        } else if (v == tvForgetPwd) {
            Intent intent = new Intent(getActivity(), ActForgetPwd.class);
            startActivity(intent);
        } else if (v == tvRegister) {
            Intent intent = new Intent(getActivity(), ActRegister.class);
            startActivityForResult(intent, IntentKey.REQ_CODE_REGISTER);
        } else if (v == backView) {
            if (isFromConflict) {
                Intent mainIntent = new Intent(getActivity(), ActMain.class);
                startActivity(mainIntent);
            }
            finishActivity();
        } else if (v == ivRefresh) {
            account = etAccount.getText().toString().trim();
            loginControl.checkMobileAndGetImgCode(account);
        }
    }

    private void authorize(Platform plat) {
        plat.setPlatformActionListener(platformActionListener);
        // 关闭SSO授权
        plat.SSOSetting(false);
        plat.showUser(null);
    }

    PlatformActionListener platformActionListener = new PlatformActionListener() {
        @Override
        public void onError(Platform platform, int action, Throwable t) {
            LogUtil.d("ZLOVE", "platformActionListener---Throwable---" + t.toString());
            Message msg = new Message();
            msg.what = 1;
            msg.obj = platform;
            myHandler.sendMessage(msg);
            if (action == Platform.ACTION_USER_INFOR) {
            }
            t.printStackTrace();
        }

        @Override
        public void onComplete(final Platform platform, int action, HashMap<String, Object> res) {
            final String userId = platform.getDb().getUserId();
            final String head = platform.getDb().getUserIcon();
            String weixinId = "";
            if (action == Platform.ACTION_USER_INFOR) {
                if (platform.getName().equals(Wechat.NAME)) {
                    weixinId = res.get("unionid").toString();
                }
                final String unionId = weixinId;
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        loginControl.doRequestThirdInfo(platform, userId, head, unionId);
                    }
                });
            }
        }

        @Override
        public void onCancel(Platform platform, int action) {
            if (action == Platform.ACTION_USER_INFOR) {
            }
        }
    };

    public void responseSocialRegister(String result, Platform platform, String head, String userId, String unionId) {
        try {
            LoginSuccessBean bean = JsonUtil.getCorrectJsonResult(result, LoginSuccessBean.class).getData();
            int redirectToRegister = bean.getItem().getRedirectToRegister();
            if (redirectToRegister != -1) {
                if (redirectToRegister == 1) {
                    Intent it = new Intent(getActivity(), PersonInfoBindActivity.class);
                    if (platform.getName().equals(QQ.NAME)) {
                        it.putExtra("qquid", userId);
                    } else if (platform.getName().equals(SinaWeibo.NAME)) {
                        it.putExtra("weibouid", userId);
                    } else if (platform.getName().equals(Wechat.NAME)) {
                        it.putExtra("wxopenid", userId);
                        it.putExtra("unionid", unionId);
                    }
                    it.putExtra("socialimage", head);
                    startActivityForResult(it, REQ_BIND_USER);
                } else {
                    UserInfo userInfo = bean.getItem();
                    SysUtil.saveUserInfo(userInfo);
                    SysUtil.savePref(PrefKeyConst.PREF_IS_LOGIN, true);
                    SysUtil.savePref(PrefKeyConst.PREF_ACCOUNT, userInfo.getUsername());
                    SysUtil.savePref(PrefKeyConst.PREF_PASSWORD, userInfo.getPassword());
                    MqttUtils.setLoginCallback(loginCallback, hashCode());
                    JPushInterface.setAlias(getActivity(), userInfo.getUsername(), this);
                    if (!MqttUtils.isConnected()) {
                        MqttUtils.connect();
                        LogUtil.d("ZLOVE", "do Connect 666666666666");
                    }
                    if (isFromConflict) {
                        Intent mainIntent = new Intent(getActivity(), ActMain.class);
                        startActivity(mainIntent);
                    }
                    Intent intent = new Intent();
                    finishActivity(intent);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (reqCode == REQ_BIND_USER) {
                MqttUtils.setLoginCallback(loginCallback, hashCode());
                if (!MqttUtils.isConnected()) {
                    MqttUtils.connect();
                    LogUtil.d("ZLOVE", "do Connect 5555555555");
                }
                finishActivity();
            } else if (reqCode == IntentKey.REQ_CODE_REGISTER) {
                if (data != null) {
                    loginRequest(SysUtil.getPref(PrefKeyConst.PREF_ACCOUNT), SysUtil.getPref(PrefKeyConst.PREF_PASSWORD));
                }
            }
        }
    }

    public void loginSuccess(LoginSuccessBean bean) {
        if (bean == null) {
            return;
        }
        UserInfo userInfo = bean.getItem();
        userInfo.setPassword(password);
        SysUtil.savePref(PrefKeyConst.PREF_IS_LOGIN, true);
        SysUtil.saveUserInfo(userInfo);
        SysUtil.savePref(PrefKeyConst.PREF_ACCOUNT, userInfo.getUsername());
        SysUtil.savePref(PrefKeyConst.PREF_PASSWORD, password);
        MqttUtils.setLoginCallback(loginCallback, hashCode());
        if (!MqttUtils.isConnected() && !isFromConflict) {
            MqttUtils.connect();
            LogUtil.d("ZLOVE", "do Connect 44444444");
        }
        JPushInterface.setAlias(getActivity(), userInfo.getUsername(), this);
        if (isFromConflict) {
            Intent mainIntent = new Intent(getActivity(), ActMain.class);
            startActivity(mainIntent);
        }
        Intent intent = new Intent();
        finishActivity(intent);
    }

    MqttMsgCallbackHandler.MqttLoginCallback loginCallback = new MqttMsgCallbackHandler.MqttLoginCallback() {
        @Override
        public void onConnect(boolean connected) {
            /**
             *  pull all conversations
             */
            MqttUtils.publish_request(MqttServiceConstants.PULL_CONVERSATIONS_REQUEST, new JSONObject());
        }

        @Override
        public void onSubscirbe(boolean isSuccess) {
        }
    };

    @Override
    public void gotResult(int i, String s, Set<String> set) {
        if (i == 0) {
            LogUtil.d("JPUSH", "设置别名成功");
        }
    }

    public void loginRequest(String account, String password) {
        this.account = account;
        this.password = password;
        loginControl.doLogin(account, password, verCode);
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        //现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起
        if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {
            logoView.setVisibility(View.GONE);
            backView.setVisibility(View.GONE);
            titleView.setVisibility(View.VISIBLE);
        } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
            logoView.setVisibility(View.VISIBLE);
            backView.setVisibility(View.VISIBLE);
            titleView.setVisibility(View.GONE);
        }
    }


    Handler myHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                Platform platform = (Platform) msg.obj;
                String platName = "微信";
                if (platform.getName().equals(Wechat.NAME)) {
                    platName = "微信";
                } else if (platform.getName().equals(QQ.NAME)) {
                    platName = "QQ";
                } else if (platform.getName().equals(SinaWeibo.NAME)) {
                    platName = "微博";
                }
                String toastContent = platName + "登陆异常,请再次点击,重新授权登陆";
                CommonDialog dialog = new CommonDialog(getActivity(), null, "提示", toastContent, true);
                dialog.showdialog();
                if (platform.isAuthValid() || platform.isValid()) {
                    platform.removeAccount();
                }
            }
        }
    };

    public void loginFail(String errMsg) {
        try {
            LoginFailResp resp = JsonUtil.getFailJsonResult(errMsg, LoginFailResp.class).getData();
            if (resp != null) {
                LoginFailResp.Extra extra = resp.getExtra();
                if (extra != null) {
                    int errCount = resp.getExtra().getFailLoginTimes();
                    if (errCount < 10) {
                        if (!resp.getExtra().isNeedCaptcha()) {
                            showShortToast(resp.getErrmsg());
                        }
                        if (errCount > 3 ) {
                            verCodeContainer.setVisibility(View.VISIBLE);
                        }
                        Bitmap bitmap = BitmapUtil.base64ToBitmap(resp.getExtra().getImageData());
                        ivVerifyCode.setImageBitmap(bitmap);
                    } else {
                        DialogManager.showActsDialogWithMessage(getActivity(), resp.getErrmsg());
                    }
                } else {
                    showShortToast(resp.getErrmsg());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showImgCodeDialog(String imageData) {
        Bitmap bitmap = BitmapUtil.base64ToBitmap(imageData);
        ivVerifyCode.setImageBitmap(bitmap);
    }
}
