package com.xiaojia.daniujia.db.bean;

import android.content.ContentValues;
import android.database.Cursor;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ZLOVE on 2016/4/8
 */
public abstract class BaseBeanDB {

    public BaseBeanDB() {}

    public abstract BaseBeanDB readFromCursor(Cursor cursor);
    public abstract ContentValues getContentValues(BaseBeanDB bean);

    public static <T extends BaseBeanDB> List<T> readListFromCursor(Cursor cursor, T serverBeanDB) {
        return readListFromCursor(cursor, serverBeanDB, false);
    }

    @SuppressWarnings("unchecked")
    public static <T extends BaseBeanDB> List<T> readListFromCursor(Cursor cursor, T serverBeanDB, boolean reverseOrder) {

        if (cursor == null) {
            new IllegalStateException("readListFromCursor cursor null").printStackTrace();
            return null;
        }

        List<T> result = new LinkedList<>();
        if (!reverseOrder) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                T item = (T) serverBeanDB.readFromCursor(cursor);
                if (item != null) {
                    result.add(item);
                }
            }
        } else {
            for (cursor.moveToLast(); !cursor.isBeforeFirst(); cursor.moveToPrevious()) {
                T item = (T) serverBeanDB.readFromCursor(cursor);
                if (item != null) {
                    result.add(item);
                }
            }
        }
        return result;
    }
}
