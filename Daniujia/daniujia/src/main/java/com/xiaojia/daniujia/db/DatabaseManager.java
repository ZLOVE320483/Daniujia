package com.xiaojia.daniujia.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.db.executor.BaseDatabaseExecutor;
import com.xiaojia.daniujia.db.executor.DatabaseDeleteExecutor;
import com.xiaojia.daniujia.provider.DaniujiaFactory;
import com.xiaojia.daniujia.provider.DaniujiaUris;
import com.xiaojia.daniujia.utils.ApplicationUtil;
import com.xiaojia.daniujia.utils.LogUtil;
import com.xiaojia.daniujia.utils.SysUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ZLOVE on 2016/4/8
 */
public class DatabaseManager extends SQLiteOpenHelper {

    protected static final String LOG_TAG = DatabaseManager.class.getSimpleName();
    private static DatabaseManager instance;
    private String databaseName;

    public DatabaseManager(Context context, String name) {
        super(context, name, null, DatabaseConstants.DATABASE_VERSION);
        this.databaseName = name;
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            synchronized (LOG_TAG) {
                if (instance == null) {
                    String account = SysUtil.getBooleanPref(PrefKeyConst.PREF_IS_LOGIN) ? SysUtil.getPref(PrefKeyConst.PREF_USER_NAME) : ApplicationUtil.getDeviceId(ApplicationUtil.getApplicationContext());
                    initDataBase(account);
                }
            }
        }
        return instance;
    }

    public static void initDataBase(String account) {
        instance = DaniujiaFactory.getInstance().getDatabaseManager(ApplicationUtil.getApplicationContext(),
                String.format(DatabaseConstants.DATABASE_NAME_FORMAT, account));
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createConversationTable(db);
        createMessageTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 1) {
            onDatabaseUpdateFrom1to2(db);
            oldVersion++;
        }
        if (oldVersion == 2) {
            onDatabaseUpdateFrom2To3(db);
            oldVersion++;
        }
        if (oldVersion == 3) {
            onDatabaseUpdateFrom3to4(db);
            oldVersion++;
        }
        if (oldVersion == 4) {
            onDatabaseUpdateFrom4to5(db);
            oldVersion++;
        }
    }

    private void onDatabaseUpdateFrom1to2(SQLiteDatabase db) {
        db.execSQL("ALTER TABLE " + DatabaseConstants.Tables.TABLE_NAME_MESSAGE + " ADD COLUMN" + DatabaseConstants.MessageColumn.COLUMN_NAME_VOICE_IS_PLAY + " int");

        ContentValues values = new ContentValues();
        values.put(DatabaseConstants.MessageColumn.COLUMN_NAME_VOICE_IS_PLAY, 1);
        long row = db.update(DatabaseConstants.Tables.TABLE_NAME_MESSAGE, values, "1=1", null);
        LogUtil.d("DB", "update corps rows count: " + row);
    }

    private void onDatabaseUpdateFrom2To3(SQLiteDatabase db) {
        db.execSQL("ALTER TABLE " + DatabaseConstants.Tables.TABLE_NAME_CONVERSATION + " ADD COLUMN" + DatabaseConstants.ConversationColumn.COLUMN_NAME_SERVICE_STATUS + " text");
        db.execSQL("ALTER TABLE " + DatabaseConstants.Tables.TABLE_NAME_MESSAGE + " ADD COLUMN" + DatabaseConstants.MessageColumn.COLUMN_NAME_ATTACH_FILE_NAME + " text");
        db.execSQL("ALTER TABLE " + DatabaseConstants.Tables.TABLE_NAME_MESSAGE + " ADD COLUMN" + DatabaseConstants.MessageColumn.COLUMN_NAME_ATTACH_SIZE + " long");
        db.execSQL("ALTER TABLE " + DatabaseConstants.Tables.TABLE_NAME_MESSAGE + " ADD COLUMN" + DatabaseConstants.MessageColumn.COLUMN_NAME_MIME_TYPE + " text");
        db.execSQL("ALTER TABLE " + DatabaseConstants.Tables.TABLE_NAME_MESSAGE + " ADD COLUMN" + DatabaseConstants.MessageColumn.COLUMN_NAME_LOCAL_PATH + " text");
        db.execSQL("ALTER TABLE " + DatabaseConstants.Tables.TABLE_NAME_MESSAGE + " ADD COLUMN" + DatabaseConstants.MessageColumn.COLUMN_NAME_ATTACH_SOURCE + " text");
    }


    private void onDatabaseUpdateFrom3to4(SQLiteDatabase db) {
        db.execSQL("DROP TRIGGER cal_unread_count");
        db.execSQL("CREATE TRIGGER cal_unread_count AFTER INSERT " +
                "ON _message " +
                "BEGIN " +
                "UPDATE _conversation SET _unread_count = (CASE WHEN new._from = _target_name AND new._is_read = 0 THEN _unread_count + 1 ELSE _unread_count END); " +
                "END;");
        db.execSQL("ALTER TABLE " + DatabaseConstants.Tables.TABLE_NAME_MESSAGE + " ADD COLUMN" + DatabaseConstants.MessageColumn.COLUMN_NAME_IMG_UPLOAD_PROGRESS + " int DEFAULT 100");
    }

    private void onDatabaseUpdateFrom4to5(SQLiteDatabase db) {
        db.execSQL("ALTER TABLE " + DatabaseConstants.Tables.TABLE_NAME_MESSAGE + " ADD COLUMN" + DatabaseConstants.MessageColumn.COLUMN_NAME_DEMAND_ID + " int DEFAULT 0");
        db.execSQL("ALTER TABLE " + DatabaseConstants.Tables.TABLE_NAME_MESSAGE + " ADD COLUMN" + DatabaseConstants.MessageColumn.COLUMN_NAME_DEMAND_NOTICE + " text");
        db.execSQL("ALTER TABLE " + DatabaseConstants.Tables.TABLE_NAME_MESSAGE + " ADD COLUMN" + DatabaseConstants.MessageColumn.COLUMN_NAME_FILE_PRE_VIEW_URL + " text");
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        try {
            return super.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.w("db", "getWritableDatabase exception. ReCreate DB...");
        ApplicationUtil.getApplicationContext().deleteDatabase(getDatabaseName());
        return super.getWritableDatabase();
    }

    @Override
    public SQLiteDatabase getReadableDatabase() {
        try {
            return super.getReadableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.w("db", "getReadableDatabase exception. ReCreate DB...");
        ApplicationUtil.getApplicationContext().deleteDatabase(getDatabaseName());
        return super.getReadableDatabase();
    }

    public void finalize() throws Throwable {
        this.close();
        if (null != instance) {
            instance = null;
        }
        super.finalize();
    }

    public void clearDB() {
        ApplicationUtil.getApplicationContext().deleteDatabase(getDatabaseName());
    }

    public Cursor doQueryAction(String sql, String[] selectionArgs) {
        try {
            return getReadableDatabase().rawQuery(sql,selectionArgs);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (selectionArgs != null) {
                    Log.e(LOG_TAG, "query_exception:" + sql + "," + Arrays.asList(selectionArgs).toString() + " " + e.getMessage());
                } else {
                    Log.e(LOG_TAG, "query_exception:" + sql + ", " + e.getMessage());
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }

    public void execSQLAction(String sql) {
        SQLiteDatabase database = getWritableDatabase();
        if (database != null) {
            database.execSQL(sql);
        }
    }

    @Override
    public String getDatabaseName() {
        return databaseName;
    }

    public static void releaseInstance() {
        synchronized (LOG_TAG) {
            if (instance != null) {
                instance.close();
                instance = null;
            }
        }
    }

    private void createConversationTable(SQLiteDatabase db) {
        String createSql = "CREATE TABLE " + DatabaseConstants.Tables.TABLE_NAME_CONVERSATION
                + "("
                + DatabaseConstants.ConversationColumn.COLUMN_NAME_TARGET_NAME + " text PRIMARY KEY,"
                + DatabaseConstants.ConversationColumn.COLUMN_NAME_NAME + " text,"
                + DatabaseConstants.ConversationColumn.COLUMN_NAME_TARGET_IDENTITY + " int,"
                + DatabaseConstants.ConversationColumn.COLUMN_NAME_TARGET_AVATAR + " text,"
                + DatabaseConstants.ConversationColumn.COLUMN_NAME_TARGET_USER_ID + " integer DEFAULT 0,"
                + DatabaseConstants.ConversationColumn.COLUMN_NAME_CONVERSATION_TYPE + " text,"
                + DatabaseConstants.ConversationColumn.COLUMN_NAME_IS_TO_WAITER + " text,"
                + DatabaseConstants.ConversationColumn.COLUMN_NAME_CREATE_AT + " text,"
                + DatabaseConstants.ConversationColumn.COLUMN_NAME_UNREAD_COUNT + " integer DEFAULT 0,"
                + DatabaseConstants.ConversationColumn.COLUMN_NAME_MSG_ID + " int,"
                + DatabaseConstants.ConversationColumn.COLUMN_NAME_MSG_TYPE + " int,"
                + DatabaseConstants.ConversationColumn.COLUMN_NAME_FROM + " text,"
                + DatabaseConstants.ConversationColumn.COLUMN_NAME_TO + " text,"
                + DatabaseConstants.ConversationColumn.COLUMN_NAME_SERVICE_STATUS + " text,"
                + DatabaseConstants.ConversationColumn.COLUMN_NAME_MSG_STAMP + " long,"
                + DatabaseConstants.ConversationColumn.COLUMN_NAME_MSG_CONTENT_TYPE + " int,"
                + DatabaseConstants.ConversationColumn.COLUMN_NAME_MSG_CONTENT + " text"
                + ");";
        db.execSQL(createSql);
    }

    private void createMessageTable(SQLiteDatabase db) {
        String createSql = "CREATE TABLE " + DatabaseConstants.Tables.TABLE_NAME_MESSAGE
                + "("
                + DatabaseConstants.MessageColumn.COLUMN_NAME_MSG_ID + " long,"
                + DatabaseConstants.MessageColumn.COLUMN_NAME_MSG_TYPE + " int,"
                + DatabaseConstants.MessageColumn.COLUMN_NAME_FROM + " varchar(20),"
                + DatabaseConstants.MessageColumn.COLUMN_NAME_TO + " varchar(20),"
                + DatabaseConstants.MessageColumn.COLUMN_NAME_CONVERSATION_TYPE + " text,"
                + DatabaseConstants.MessageColumn.COLUMN_NAME_MSG_STAMP + " long,"
                + DatabaseConstants.MessageColumn.COLUMN_NAME_MSG_CODE + " text,"
                + DatabaseConstants.MessageColumn.COLUMN_NAME_MSG_CONTENT_TYPE + " int,"
                + DatabaseConstants.MessageColumn.COLUMN_NAME_MSG_EVENT_TYPE + " text,"
                + DatabaseConstants.MessageColumn.COLUMN_NAME_MSG_EVENT_TARGET + " text,"
                + DatabaseConstants.MessageColumn.COLUMN_NAME_MSG_CONTENT + " text,"
                + DatabaseConstants.MessageColumn.COLUMN_NAME_MSG_IS_READ + " integer DEFAULT 0,"
                + DatabaseConstants.MessageColumn.COLUMN_NAME_MSG_STATE + " integer DEFAULT 1,"
                + DatabaseConstants.MessageColumn.COLUMN_NAME_IMG_HEIGHT + " integer DEFAULT 0,"
                + DatabaseConstants.MessageColumn.COLUMN_NAME_IMG_WIDTH + " integer DEFAULT 0,"
                + DatabaseConstants.MessageColumn.COLUMN_NAME_URL + " text,"
                + DatabaseConstants.MessageColumn.COLUMN_NAME_DURATION + " integer DEFAULT 0,"
                + DatabaseConstants.MessageColumn.COLUMN_NAME_QUESTION_ID + " long DEFAULT 0,"
                + DatabaseConstants.MessageColumn.COLUMN_NAME_QUESTION_CONTENT + " text,"
                + DatabaseConstants.MessageColumn.COLUMN_NAME_VOICE_IS_PLAY + " INTEGER DEFAULT 1,"
                + DatabaseConstants.MessageColumn.COLUMN_NAME_ATTACH_FILE_NAME + " text,"
                + DatabaseConstants.MessageColumn.COLUMN_NAME_ATTACH_SIZE + " long DEFAULT 0,"
                + DatabaseConstants.MessageColumn.COLUMN_NAME_MIME_TYPE + " text,"
                + DatabaseConstants.MessageColumn.COLUMN_NAME_LOCAL_PATH + " text,"
                + DatabaseConstants.MessageColumn.COLUMN_NAME_ATTACH_SOURCE + " text,"
                + DatabaseConstants.MessageColumn.COLUMN_NAME_IMG_UPLOAD_PROGRESS + " int DEFAULT 100,"
                + DatabaseConstants.MessageColumn.COLUMN_NAME_IS_DOWN + " long DEFAULT 0,"
                + DatabaseConstants.MessageColumn.COLUMN_NAME_DEMAND_ID + " int DEFAULT 0,"
                + DatabaseConstants.MessageColumn.COLUMN_NAME_DEMAND_NOTICE + " text,"
                + DatabaseConstants.MessageColumn.COLUMN_NAME_FILE_PRE_VIEW_URL + " text"
                + ");";

        db.execSQL(createSql);

        db.execSQL("CREATE TRIGGER cal_unread_count AFTER INSERT " +
                "ON _message " +
                "BEGIN " +
                "UPDATE _conversation SET _unread_count = (CASE WHEN new._from = _target_name AND new._is_read = 0 THEN _unread_count + 1 ELSE _unread_count END); " +
                "END;");
    }

    public void clearConversationTable() {
        String clearSql = "DELETE FROM _conversation";
        getWritableDatabase().execSQL(clearSql);
    }

    public void clearMessageTable(String topic) {
        BaseDatabaseExecutor.SQLSentence sql = new BaseDatabaseExecutor.SQLSentence(DatabaseConstants.MessageColumn.COLUMN_NAME_FROM + "=? OR " + DatabaseConstants.MessageColumn.COLUMN_NAME_TO + "=?",
                new String[]{topic, topic}, null);
        List<BaseDatabaseExecutor.SQLSentence> sqlSentences = new ArrayList<>();
        sqlSentences.add(sql);
        DatabaseDeleteExecutor executor = new DatabaseDeleteExecutor(DatabaseConstants.Tables.TABLE_NAME_MESSAGE, sqlSentences, DaniujiaUris.URI_MESSAGE);
        executor.execute(executor);
    }

    public int getConversationUnReadCount(String target) {
        String sql = "select * from _conversation where _target_name = '" + target + "'";
        Cursor cursor = doQueryAction(sql, null);
        if (cursor != null && cursor.moveToFirst()) {
            int index = cursor.getColumnIndex(DatabaseConstants.ConversationColumn.COLUMN_NAME_UNREAD_COUNT);
            if (index != -1) {
                return cursor.getInt(index);
            }
            LogUtil.d("ZLOVE", "index---" + index);
        }
        return 0;
    }

    public boolean isExistConversation(String target) {
        String sql = "select * from _conversation where _target_name = '" + target + "'";
        Cursor cursor = doQueryAction(sql, null);
        if (cursor != null && cursor.moveToFirst()) {
            return true;
        }
        return false;
    }

    public long getMaxMsgId(String msgTo) {
        String sql = "select max(_msg_id) from _message where (_to = '" + msgTo + "'" + " or _from = '" + msgTo + "') AND _msg_state = 1";
        Cursor cursor = doQueryAction(sql, null);
        if (cursor != null && cursor.moveToFirst()) {
            long maxId = cursor.getLong(0);
            return maxId;
        }
        return 0;
    }


    public long getMaxMsgIdFromMsgFrom(String msgFrom) {
        String sql = "select max(_msg_id) from _message where _from = '" + msgFrom + "'";
        Cursor cursor = doQueryAction(sql, null);
        if (cursor != null && cursor.moveToFirst()) {
            long maxId = cursor.getLong(0);
            return maxId;
        }
        return 0;
    }
}
