package com.xiaojia.daniujia.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.db.DatabaseConstants;
import com.xiaojia.daniujia.db.DatabaseManager;
import com.xiaojia.daniujia.utils.LogUtil;
import com.xiaojia.daniujia.utils.SysUtil;

/**
 * Created by ZLOVE on 2016/4/8.
 */
public class DaniujiaProvider extends ContentProvider {

    private static String AUTHORITY = null;
    protected final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    @Override
    public boolean onCreate() {
        AUTHORITY = getContext().getPackageName();
        initMatcher();
        return true;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        LogUtil.d("ZLOVE", "1111111111111");
        String tableName = getTableNameFromUri(uri);
        if(!TextUtils.isEmpty(tableName)) {
            SQLiteDatabase db = DatabaseManager.getInstance().getWritableDatabase();
            if (db != null) {
                db.update(tableName,values,selection,selectionArgs);
            }
        }
        return 0;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        LogUtil.d("ZLOVE", "22222222222");
        String tableName = getTableNameFromUri(uri);
        if (!TextUtils.isEmpty(tableName)) {
            SQLiteDatabase db = DatabaseManager.getInstance().getWritableDatabase();
            if (db != null) {
                long insertId = db.insert(tableName,null,values);
                if (insertId == -1) {
                    db.replace(tableName,null,values);
                }
            }
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        LogUtil.d("ZLOVE", "33333333333");
        String tableName = getTableNameFromUri(uri);
        if (!TextUtils.isEmpty(tableName)) {
            SQLiteDatabase db = DatabaseManager.getInstance().getWritableDatabase();
            if (db != null) {
                return db.delete(tableName,selection,selectionArgs);
            }
        }
        return 0;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if (DaniujiaUris.URI_RAW_SQL.equals(uri) && SysUtil.getBooleanPref(PrefKeyConst.PREF_IS_LOGIN)) {
            Cursor cursor =  DatabaseManager.getInstance().doQueryAction(selection, selectionArgs);
            try {
                // A Kind Of Hacker
                if (cursor != null && sortOrder != null) {
                    cursor.setNotificationUri(getContext().getContentResolver(), Uri.parse(sortOrder));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return cursor;
        }else{
            Cursor cursor = null;
            String tableName = getTableNameFromUri(uri);
            if (tableName != null) {
                SQLiteDatabase db = DatabaseManager.getInstance().getReadableDatabase();
                if (db != null){
                    cursor = db.query(tableName, projection, selection, selectionArgs, null, null, sortOrder);
                    cursor.setNotificationUri(getContext().getContentResolver(), uri);
                }
            }
            return cursor;
        }
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        LogUtil.d("ZLOVE", "55555555555");
        String tableName = getTableNameFromUri(uri);
        if (!TextUtils.isEmpty(tableName)) {
            SQLiteDatabase db = DatabaseManager.getInstance().getWritableDatabase();
            if (db != null) {
                try {
                    db.beginTransaction();
                    for (ContentValues cv : values) {
                        db.insert(tableName, null, cv);
                    }
                    db.setTransactionSuccessful();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    db.endTransaction();
                }
            }
        }
        return values.length;
    }

    protected void initMatcher() {
        uriMatcher.addURI(AUTHORITY, DaniujiaUris.PATH_CONVERSATION, TokenConstants.TOKEN_CONVERSATION);
        uriMatcher.addURI(AUTHORITY, DaniujiaUris.PATH_MESSAGE, TokenConstants.TOKEN_MESSAGE);
    }

    protected String getTableNameFromUri(Uri uri) {
        int match = uriMatcher.match(uri);
        String tableName = null;
        switch (match) {
            case TokenConstants.TOKEN_CONVERSATION: {
                tableName = DatabaseConstants.Tables.TABLE_NAME_CONVERSATION;
                break;
            }
            case TokenConstants.TOKEN_MESSAGE: {
                tableName = DatabaseConstants.Tables.TABLE_NAME_MESSAGE;
                break;
            }
        }
        return tableName;
    }
}
