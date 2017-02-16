package com.xiaojia.daniujia.db.executor;

import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.xiaojia.daniujia.utils.ListUtils;

import java.util.List;

/**
 * Created by ZLOVE on 2016/4/18.
 */
public class DatabaseUpdateExecutor extends BaseDatabaseExecutor {

    public DatabaseUpdateExecutor(String tableName, List<SQLSentence> sqlSentences, Uri notifyUri) {
        super(tableName, sqlSentences, notifyUri);
    }

    @Override
    public boolean runImp(SQLiteDatabase db) throws Exception {
        if (!ListUtils.isEmpty(sqlSentences)) {
            if (db != null) {
                for(SQLSentence sql : sqlSentences) {
                    db.update(tableName, sql.contentValues, sql.whereClause, sql.whereArgs);
                }
            }
        }
        return false;
    }
}
