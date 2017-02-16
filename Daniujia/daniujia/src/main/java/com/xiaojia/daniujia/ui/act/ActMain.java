package com.xiaojia.daniujia.ui.act;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.InsideMsg;
import com.xiaojia.daniujia.IntentKey;
import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.db.DatabaseConstants;
import com.xiaojia.daniujia.db.DatabaseManager;
import com.xiaojia.daniujia.domain.resp.UpdateVersionRespVo;
import com.xiaojia.daniujia.managers.OkHttpClientManager;
import com.xiaojia.daniujia.mqtt.MqttUtils;
import com.xiaojia.daniujia.mqtt.service.MqttServiceConstants;
import com.xiaojia.daniujia.msg.MsgHelper;
import com.xiaojia.daniujia.msg.OnMsgCallback;
import com.xiaojia.daniujia.msg.annotation.OnMsg;
import com.xiaojia.daniujia.ui.frag.ConversationFragment;
import com.xiaojia.daniujia.ui.frag.HomeFragment;
import com.xiaojia.daniujia.ui.frag.MeFragment;
import com.xiaojia.daniujia.ui.frag.RequirementFragment;
import com.xiaojia.daniujia.utils.DialogManager;
import com.xiaojia.daniujia.utils.LogUtil;
import com.xiaojia.daniujia.utils.SysUtil;

import org.json.JSONObject;

/**
 * Created by ZLOVE on 2016/4/25.
 */
public class ActMain extends ActDnjBase implements View.OnClickListener, DialogManager.UpdateVersionListener, HomeFragment.JumpMeTabListener {

    public static final int TAB_HOME = 1;
    public static final int TAB_REQUIREMENT = 2;
    public static final int TAB_CONVERSATION = 3;
    public static final int TAB_ME = 4;

    private String homeTag = "_home_tag";
    private String conversationTag = "_conversation_tag";
    private String requirementTag = "_requirement_tag";
    private String meTag = "_me_tag";
    private FragmentManager fragmentManager;

    private HomeFragment homeFragment;
    private ConversationFragment conversationFragment;
    private RequirementFragment requirementFragment;
    private MeFragment meFragment;

    private RadioButton rbTabHome;
    private RadioButton rbTabConversation;
    private RadioButton rbTabRequirement;
    private RadioButton rbTabMe;
    private TextView tvUnReadCount;
    private int force;
    private static final int REQ_UPDATE_VERSION = 100;
    private int currentTab = 0;

    private String updateUrl;

    private String topic;

    private int userId = 0;
    private int orderId = 0;
    private int flag = 0;
    private int demandId = 0;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            if (intent.hasExtra(IntentKey.INTENT_KEY_TO_BE_EXPERT_COMPLETE)) {
                String fromWhere = intent.getStringExtra(IntentKey.FROM_WHERE);
                if (fromWhere.equals(IntentKey.INTENT_VALUE_TO_BE_EXPERT_FROM_SEARCH)) {
                    if (homeFragment != null) {
                        homeFragment.jumpToLookFor();
                    }
                } else if (fromWhere.equals(IntentKey.INTENT_VALUE_TO_BE_EXPERT_FROM_REQUIRE)) {
                    if (requirementFragment != null) {
                        showFragment(TAB_REQUIREMENT);
                        requirementFragment.jumpToDetail();
                    }
                }
            }
            if (intent.hasExtra(IntentKey.INTENT_KEY_CHAT_TOPIC)) {
                topic = intent.getStringExtra(IntentKey.INTENT_KEY_CHAT_TOPIC);
                if (!TextUtils.isEmpty(topic)) {
                    showFragment(TAB_CONVERSATION);
                    conversationFragment.setTopic(topic);
                }
            }
            if (intent.hasExtra(IntentKey.INTENT_KEY_CHAT_USER_ID)) {
                userId = intent.getIntExtra(IntentKey.INTENT_KEY_CHAT_USER_ID, 0);
            }
            if (intent.hasExtra(IntentKey.INTENT_KEY_ORDER_ID)) {
                orderId = intent.getIntExtra(IntentKey.INTENT_KEY_ORDER_ID, 0);
            }
            if (intent.hasExtra(IntentKey.INTENT_KEY_CHAT_USER_IDENTITY)) {
                flag = intent.getIntExtra(IntentKey.INTENT_KEY_CHAT_USER_IDENTITY, 0);
            }
            if (intent.hasExtra(IntentKey.INTENT_KEY_DEMAND_ID)) {
                demandId = intent.getIntExtra(IntentKey.INTENT_KEY_DEMAND_ID, 0);
            }
            judgeJumpOrderDetail();
            jumpToRequirementDetail();
        }
        if (meFragment != null) {
            meFragment.onResume();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_dnj_main);

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(IntentKey.INTENT_KEY_CHAT_USER_ID)) {
                userId = intent.getIntExtra(IntentKey.INTENT_KEY_CHAT_USER_ID, 0);
            }
            if (intent.hasExtra(IntentKey.INTENT_KEY_ORDER_ID)) {
                orderId = intent.getIntExtra(IntentKey.INTENT_KEY_ORDER_ID, 0);
            }
            if (intent.hasExtra(IntentKey.INTENT_KEY_CHAT_USER_IDENTITY)) {
                flag = intent.getIntExtra(IntentKey.INTENT_KEY_CHAT_USER_IDENTITY, 0);
            }
            if (intent.hasExtra(IntentKey.INTENT_KEY_DEMAND_ID)) {
                demandId = intent.getIntExtra(IntentKey.INTENT_KEY_DEMAND_ID, 0);
            }
        }

        if(fragmentManager == null){
            fragmentManager = getSupportFragmentManager();
        }
        MsgHelper.getInstance().registMsg(this);

        rbTabHome = (RadioButton) findViewById(R.id.tab_home);
        rbTabConversation = (RadioButton) findViewById(R.id.tab_conversation);
        rbTabRequirement = (RadioButton) findViewById(R.id.tab_requirement);
        rbTabMe = (RadioButton) findViewById(R.id.tab_me);
        tvUnReadCount = (TextView) findViewById(R.id.id_unread_count);

        rbTabHome.setOnClickListener(this);
        rbTabConversation.setOnClickListener(this);
        rbTabRequirement.setOnClickListener(this);
        rbTabMe.setOnClickListener(this);

        homeFragment = (HomeFragment) fragmentManager.findFragmentByTag(homeTag);
        conversationFragment = (ConversationFragment) fragmentManager.findFragmentByTag(conversationTag);
        requirementFragment = (RequirementFragment) fragmentManager.findFragmentByTag(requirementTag);
        meFragment = (MeFragment) fragmentManager.findFragmentByTag(meTag);

        FragmentTransaction ft = fragmentManager.beginTransaction();
        hideFragments(ft);

        showFragment(TAB_HOME);
        doRequestVersionInfo();
        judgeJumpOrderDetail();
        jumpToRequirementDetail();

        if (SysUtil.getBooleanPref(PrefKeyConst.PREF_IS_LOGIN)) {
            if (!MqttUtils.isConnected()) {
                MqttUtils.connect();
                LogUtil.d("ZLOVE", "do Connect 111111");
            }
            showUnReadCount();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!SysUtil.getBooleanPref(PrefKeyConst.PREF_IS_LOGIN)) {
            tvUnReadCount.setVisibility(View.GONE);
        } else {
            showUnReadCount();
        }

        if (homeFragment != null && !homeFragment.isHidden()){
            homeFragment.setRollStop(false);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (homeFragment != null){
            homeFragment.setRollStop(true);
        }
    }

    private void hideFragments(FragmentTransaction ft) {
        if (homeFragment != null) {
            ft.hide(homeFragment);
        }
        if (conversationFragment != null) {
            ft.hide(conversationFragment);
        }
        if (requirementFragment != null) {
            ft.hide(requirementFragment);
        }
        if (meFragment != null) {
            ft.hide(meFragment);
        }
    }

    private void showFragment(int tabIndex) {
        if (currentTab == tabIndex) {
            return;
        }
        this.currentTab = tabIndex;
        FragmentTransaction ft = fragmentManager.beginTransaction();
        hideFragments(ft);
        switch (currentTab) {
            case TAB_HOME: {
                if (homeFragment == null || homeFragment.isDetached()) {
                    homeFragment = new HomeFragment();
                    homeFragment.setListener(ActMain.this);
                    ft.add(R.id.id_framework, homeFragment, homeTag);
                } else {
                    ft.show(homeFragment);
                }
                break;
            }
            case TAB_REQUIREMENT: {
                if (requirementFragment == null || requirementFragment.isDetached()) {
                    requirementFragment = new RequirementFragment();
                    ft.add(R.id.id_framework, requirementFragment, requirementTag);
                } else {
                    ft.show(requirementFragment);
                }
                break;
            }
            case TAB_CONVERSATION: {
                if (conversationFragment == null || conversationFragment.isDetached()) {
                    conversationFragment = new ConversationFragment();
                    ft.add(R.id.id_framework, conversationFragment, conversationTag);
                } else {
                    ft.show(conversationFragment);
                }
                break;
            }
            case TAB_ME: {
                if (meFragment == null || meFragment.isDetached()) {
                    meFragment = new MeFragment();
                    ft.add(R.id.id_framework, meFragment, meTag);
                } else {
                    ft.show(meFragment);
                }
                break;
            }
            default:
                break;
        }
        ft.commitAllowingStateLoss();
        onTabChange();
    }

    private void onTabChange() {
        rbTabHome.setChecked(currentTab == TAB_HOME);
        rbTabConversation.setChecked(currentTab == TAB_CONVERSATION);
        rbTabRequirement.setChecked(currentTab == TAB_REQUIREMENT);
        rbTabMe.setChecked(currentTab == TAB_ME);
    }

    @Override
    public void onClick(View v) {
        int changeIndex;
        if (v == rbTabHome) {
            changeIndex = TAB_HOME;
            showFragment(changeIndex);
        } else if (v == rbTabConversation) {
            changeIndex = TAB_CONVERSATION;
            showFragment(changeIndex);
        } else if (v == rbTabMe) {
            changeIndex = TAB_ME;
            showFragment(changeIndex);
        } else if(v == rbTabRequirement){
            changeIndex = TAB_REQUIREMENT;
            showFragment(changeIndex);
        }
    }

    private void doRequestVersionInfo() {
        final String curVersion = SysUtil.getVerName();
        String url = Config.WEB_API_SERVER + "/android/latestVersion/" + curVersion;

        OkHttpClientManager.getInstance(this).getWithPlatform(false, url, new OkHttpClientManager.ResultCallback<UpdateVersionRespVo>() {
            @Override
            public void onResponse(UpdateVersionRespVo result) {
                if (result == null) {
                    return;
                }
                if (result.getReturncode().equals("SUCCESS")) {
                    if (result.getUpdateFlag() == 0) {
                        return;
                    }
                    if (!result.getLatestVersion().equals(curVersion)) {
                        boolean needShowDialog = SysUtil.getBooleanPref(result.getLatestVersion(), true);
                        if (needShowDialog) {
                            updateUrl = result.getDownloadUrl();
                            force = result.getForce();
                            DialogManager.showUpdateVersionDialog(ActMain.this, result.getForce(), result.getLatestVersion(), result.getContent(), ActMain.this);
                        }
                    }
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

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (reqCode == REQ_UPDATE_VERSION) {
            if (force == 1) {
                sendBroadcast(new Intent(ACTION_EXIT));
            }
        } else if (reqCode == IntentKey.REQ_CODE_ORDER_LOGIN) {
            if (data != null) {
                judgeJumpOrderDetail();
            }
        }
    }


    private long firstTime = 0;
    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - firstTime) > 2000) {
            showShortToast(R.string.click_once_more_exit);
            firstTime = System.currentTimeMillis();
        } else {
            SysUtil.savePref(PrefKeyConst.PREF_isMulityLogin, false);
            MqttUtils.publish(MqttServiceConstants.USER_LOGOUT_REQUEST, new JSONObject(), 0);
            MqttUtils.handleConnectionLost();
            MqttUtils.disconnect();
            finish();
        }
    }

    @Override
    public void updateAction() {
        Intent browserIntent = new Intent();
        browserIntent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(updateUrl);
        browserIntent.setData(content_url);
        // 用户点击下载链接跳转到浏览器页面之后返回了
        startActivityForResult(browserIntent,REQ_UPDATE_VERSION);
    }

    @Override
    public void jumpMe() {
        showFragment(TAB_ME);
    }

    @OnMsg(msg = { InsideMsg.NOTIFY_PUSH_NEW_MESSAGE, InsideMsg.NOTIFY_JUMP_TO_ME }, useLastMsg = false)
    OnMsgCallback onMsgCallback = new OnMsgCallback() {
        @Override
        public boolean handleMsg(Message msg) {
            switch (msg.what) {
                case InsideMsg.NOTIFY_PUSH_NEW_MESSAGE:
                    showUnReadCount();
                    break;
                case InsideMsg.NOTIFY_JUMP_TO_ME:
                    showFragment(TAB_ME);
                    break;
            }
            return false;
        }
    };

    private void showUnReadCount() {
        String select = "select sum(_unread_count) from " + DatabaseConstants.Tables.TABLE_NAME_CONVERSATION;
        Cursor myCursor = DatabaseManager.getInstance().doQueryAction(select, null);
        try {
            if (myCursor != null && myCursor.moveToFirst() && tvUnReadCount != null) {
                long count = myCursor.getLong(0);
                if (count <= 0) {
                    tvUnReadCount.setText("");
                    tvUnReadCount.setVisibility(View.GONE);
                } else if (count > 0 && count <= 99) {
                    tvUnReadCount.setText(String.valueOf(count));
                    tvUnReadCount.setVisibility(View.VISIBLE);
                } else {
                    tvUnReadCount.setText("99+");
                    tvUnReadCount.setVisibility(View.VISIBLE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void judgeJumpOrderDetail() {
        if (userId != 0 && orderId != 0) {
            if (SysUtil.getBooleanPref(PrefKeyConst.PREF_IS_LOGIN)) {
                if (userId != SysUtil.getIntPref(PrefKeyConst.PREF_USER_ID)) {
                    showShortToast("当前用户没有此约谈订单，请切换其他账号进行查看");
                } else {
                    Intent intent = new Intent();
                    if (flag == 0) {
                        intent.setClass(ActMain.this, ActUserOrderDetail.class);
                    } else if (flag == 1) {
                        intent.setClass(ActMain.this, ActExpertOrderDetail.class);
                    }
                    intent.putExtra(IntentKey.INTENT_KEY_ORDER_ID, orderId);
                    startActivity(intent);
                }
            } else {
                Intent intent = new Intent(this, ActLogin.class);
                startActivityForResult(intent, IntentKey.REQ_CODE_ORDER_LOGIN);
            }
        }
    }

    private void jumpToRequirementDetail() {
        if (demandId == 0) {
            return;
        }
        Intent intent = new Intent(this, ActRequirementDetail.class);
        intent.putExtra(IntentKey.INTENT_KEY_DEMAND_ID, demandId);
        startActivity(intent);
    }
}
