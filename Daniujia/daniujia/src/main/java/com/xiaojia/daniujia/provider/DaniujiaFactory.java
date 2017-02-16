package com.xiaojia.daniujia.provider;

import android.content.Context;

import com.xiaojia.daniujia.db.DatabaseManager;

/**
 * Created by ZLOVE on 2016/4/20.
 */
public class DaniujiaFactory {

    private static DaniujiaFactory instance;

    public static void initFactory(DaniujiaFactory factory) {
        instance = factory;
    }

    public static DaniujiaFactory getInstance() {
        return instance;
    }

    public DatabaseManager getDatabaseManager(Context applicationContext, String databaseName) {
        return new DatabaseManager(applicationContext, databaseName);
    }
}
