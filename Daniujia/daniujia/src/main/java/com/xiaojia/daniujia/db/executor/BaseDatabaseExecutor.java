package com.xiaojia.daniujia.db.executor;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.xiaojia.daniujia.db.DatabaseManager;
import com.xiaojia.daniujia.utils.ApplicationUtil;

import java.util.List;

/**
 * Created by ZLOVE on 2016/4/8
 */
public abstract class BaseDatabaseExecutor extends DatabaseExecutor.ExecuteRobin {

    protected String tableName;
    protected List<SQLSentence> sqlSentences;
    protected Uri notifyUri;

    public void execute(BaseDatabaseExecutor executor) {
        DatabaseExecutor.getInstance().execute(executor);
    }

    public BaseDatabaseExecutor(String tableName, List<SQLSentence> sqlSentences, Uri notifyUri) {
        this.tableName = tableName;
        this.sqlSentences = sqlSentences;
        this.notifyUri = notifyUri;
    }

    @Override
    protected boolean runImp() throws Exception {
        SQLiteDatabase database = DatabaseManager.getInstance().getWritableDatabase();
        runImp(database);
        notifyUri();
        return false;
    }

    protected void notifyUri() {
        if (notifyUri == null) {
            return;
        }
        ApplicationUtil.getContentResolver().notifyChange(notifyUri, null);
    }

    public static class SQLSentence {
        public String whereClause;
        public String[] whereArgs;
        public ContentValues contentValues;

        public SQLSentence(String whereClause, String[] whereArgs, ContentValues contentValues) {
            this.whereClause = whereClause;
            this.whereArgs = whereArgs;
            this.contentValues = contentValues;
        }
    }
}
