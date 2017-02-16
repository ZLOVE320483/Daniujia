package com.xiaojia.daniujia.ui.control;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;

import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.db.DatabaseConstants;
import com.xiaojia.daniujia.db.DatabaseManager;
import com.xiaojia.daniujia.db.bean.BaseBeanDB;
import com.xiaojia.daniujia.domain.resp.PullConversationsVo;
import com.xiaojia.daniujia.provider.DaniujiaUris;
import com.xiaojia.daniujia.ui.frag.TransmitListFragment;
import com.xiaojia.daniujia.utils.ApplicationUtil;

import java.util.List;

/**
 * Created by Administrator on 2016/9/13.
 */
public class TransmitListControl extends BaseLoaderControl<TransmitListFragment> {
    @Override
    public void onResume() {
        super.onResume();
        initLoader(R.id.id_loader_conversations, null, new ConversationLoaderCallback());
    }

    private class ConversationLoaderCallback implements LoaderManager.LoaderCallbacks<Cursor> {

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new CursorLoader(ApplicationUtil.getApplicationContext(), DaniujiaUris.URI_CONVERSATION, null,
                    DatabaseConstants.ConversationColumn.COLUMN_NAME_SERVICE_STATUS + "=? OR " + DatabaseConstants.ConversationColumn.COLUMN_NAME_SERVICE_STATUS + " = ?", new String[]{"paid", "pending"}, DatabaseConstants.ConversationColumn.COLUMN_NAME_MSG_STAMP + " DESC");
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

    public void searchName(String name) {
        if (name == null)
            return;
        String sql;
        Cursor cursor;
        if (TextUtils.isEmpty(name)) {
            sql = "select * from " + DatabaseConstants.Tables.TABLE_NAME_CONVERSATION + " where "
                    + DatabaseConstants.ConversationColumn.COLUMN_NAME_SERVICE_STATUS + " = ? OR " + DatabaseConstants.ConversationColumn.COLUMN_NAME_SERVICE_STATUS + " = ?";
            cursor = DatabaseManager.getInstance().doQueryAction(sql, new String[]{"paid", "pending"});
        } else {
            sql = "select * from " + DatabaseConstants.Tables.TABLE_NAME_CONVERSATION + " where " + DatabaseConstants.ConversationColumn.COLUMN_NAME_NAME +  " like ? and ("
                    + DatabaseConstants.ConversationColumn.COLUMN_NAME_SERVICE_STATUS + " = ? OR " + DatabaseConstants.ConversationColumn.COLUMN_NAME_SERVICE_STATUS + " = ?)";
            cursor = DatabaseManager.getInstance().doQueryAction(sql, new String[]{"%" + name + "%", "paid", "pending"});
        }

        if (cursor != null) {
            List<PullConversationsVo.DataEntity> recordList = BaseBeanDB.readListFromCursor(cursor, new PullConversationsVo.DataEntity());
            getFragment().showRecordList(recordList);
            if (cursor != null && !cursor.isClosed()){
                cursor.close();
            }

        }
    }
}
