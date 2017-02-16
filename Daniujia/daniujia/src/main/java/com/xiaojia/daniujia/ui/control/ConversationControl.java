package com.xiaojia.daniujia.ui.control;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;

import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.db.DatabaseConstants;
import com.xiaojia.daniujia.db.DatabaseManager;
import com.xiaojia.daniujia.db.bean.BaseBeanDB;
import com.xiaojia.daniujia.domain.resp.PullConversationsVo;
import com.xiaojia.daniujia.mqtt.MqttUtils;
import com.xiaojia.daniujia.mqtt.service.MqttServiceConstants;
import com.xiaojia.daniujia.provider.DaniujiaUris;
import com.xiaojia.daniujia.ui.frag.ConversationFragment;
import com.xiaojia.daniujia.utils.ApplicationUtil;
import com.xiaojia.daniujia.utils.LogUtil;
import com.xiaojia.daniujia.utils.SysUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by ZLOVE on 2016/4/10
 */
public class ConversationControl extends BaseLoaderControl<ConversationFragment> {

    @Override
    public void onResume() {
        super.onResume();
        initLoader(R.id.id_loader_conversations, null, new ConversationLoaderCallback());
    }

    private class ConversationLoaderCallback implements LoaderManager.LoaderCallbacks<Cursor> {

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new CursorLoader(ApplicationUtil.getApplicationContext(), DaniujiaUris.URI_CONVERSATION,
                    null, null, null, DatabaseConstants.ConversationColumn.COLUMN_NAME_MSG_STAMP + " DESC");
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            if (cursor != null) {
                List<PullConversationsVo.DataEntity> recordList = BaseBeanDB.readListFromCursor(cursor, new PullConversationsVo.DataEntity());
                getFragment().showRecordList(recordList);
            } else {
                getFragment().clearData();
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
        }
    }

    public void getHistoryConversationList() {
        if (!SysUtil.getBooleanPref(PrefKeyConst.PREF_IS_LOGIN)) {
            return;
        }
        MqttUtils.publish_request(MqttServiceConstants.PULL_CONVERSATIONS_REQUEST, new JSONObject());
    }

    public void deleteConversationItem(String topic) {
        if (TextUtils.isEmpty(topic)) {
            return;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("target", topic);
            jsonObject.put("type", "singleChat");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MqttUtils.publish_request(MqttServiceConstants.DELETE_CONVERSATION_REQUEST, jsonObject);
    }

    public void judgeConversation(String target) {
        String sql = "select * from _conversation where _target_name = ?";
        Cursor cursor = DatabaseManager.getInstance().doQueryAction(sql, new String[]{target});
        if (cursor == null || !cursor.moveToFirst()) {
            JSONObject object = new JSONObject();
            try {
                object.put("target", target);
                object.put("type", "singleChat");
                object.put("allowEmpty", true);
            } catch (JSONException e) {
                LogUtil.d("ZLOVE", "JSONException---" + e.toString());
            }
            MqttUtils.publish_request(MqttServiceConstants.PULL_CONVERSATIONS_REQUEST, object);
        } else {
            List<PullConversationsVo.DataEntity> recordList = BaseBeanDB.readListFromCursor(cursor, new PullConversationsVo.DataEntity());
            getFragment().goChat(recordList.get(0));
        }
    }
}
