package com.xiaojia.daniujia.db.executor;

import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.xiaojia.daniujia.InsideMsg;
import com.xiaojia.daniujia.msg.MsgHelper;
import com.xiaojia.daniujia.utils.ListUtils;

import java.util.List;

/**
 * Created by ZLOVE on 2016/4/8.
 */
public class DatabaseInsertExecutor extends BaseDatabaseExecutor {

    public DatabaseInsertExecutor(String tableName, List<SQLSentence> sqlSentences, Uri notifyUri) {
        super(tableName, sqlSentences, notifyUri);
    }

    @Override
    public boolean runImp(SQLiteDatabase db) throws Exception {
        if (!ListUtils.isEmpty(sqlSentences)) {
            if (db != null) {
                for(SQLSentence sql : sqlSentences) {
                    db.insert(tableName, null, sql.contentValues);
                }
            }
            MsgHelper.getInstance().sendMsg(InsideMsg.NOTIFY_PUSH_NEW_MESSAGE);
        }
        return false;
    }
}
