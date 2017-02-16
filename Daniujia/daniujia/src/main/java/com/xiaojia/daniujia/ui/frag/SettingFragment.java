package com.xiaojia.daniujia.ui.frag;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.FileStorage;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.base.App;
import com.xiaojia.daniujia.db.DatabaseManager;
import com.xiaojia.daniujia.ui.act.ChangePwdActivity;
import com.xiaojia.daniujia.ui.control.SettingControl;
import com.xiaojia.daniujia.ui.views.CommonDialog;
import com.xiaojia.daniujia.utils.ApplicationUtil;
import com.xiaojia.daniujia.utils.CacheUtil;
import com.xiaojia.daniujia.utils.FileUtil;
import com.xiaojia.daniujia.utils.LogUtil;

import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 2016/4/29.
 */
public class SettingFragment extends BaseFragment implements View.OnClickListener, CommonDialog.ConfirmAction {

    private SettingControl settingControl;

    private View changePwdView;
    private Button btnExit;
    private View clearCache;
    private TextView tvCacheSize;
    private View adminView;
    private View exportDbView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingControl = new SettingControl();
        setControl(settingControl);
    }

    @Override
    protected int getInflateLayout() {
        return R.layout.fg_setting;
    }

    @Override
    protected void setUpView(View view) {
        setBackButton(view.findViewById(R.id.id_back));
        ((TextView) view.findViewById(R.id.id_title)).setText("设置");

        changePwdView = view.findViewById(R.id.update_password);
        changePwdView.setOnClickListener(this);
        clearCache = view.findViewById(R.id.clear_cache);
        clearCache.setOnClickListener(this);
        tvCacheSize = (TextView) view.findViewById(R.id.cache_size);
        btnExit = (Button) view.findViewById(R.id.id_exit);
        btnExit.setOnClickListener(this);

        adminView = view.findViewById(R.id.admin_view);
        exportDbView = view.findViewById(R.id.export_db);
        exportDbView.setOnClickListener(this);
        if (Config.DEBUG) {
            adminView.setVisibility(View.VISIBLE);
        } else {
            adminView.setVisibility(View.GONE);
        }

        tvCacheSize.setText(CacheUtil.getCacheSize());
    }

    @Override
    public void onClick(View v) {
        if (v == btnExit) {
            settingControl.logOut();
        } else if (v == exportDbView) {
            new ExportDatabaseAsyncTask().execute();
        } else if (v == changePwdView) {
            Intent intent = new Intent(getActivity(), ChangePwdActivity.class);
            startActivity(intent);
        } else if (v == clearCache) {
            CommonDialog dialog = new CommonDialog(getActivity(), this, "提示", "确定删除缓存吗?");
            dialog.showdialog();
        }
    }

    private static class ExportDatabaseAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            Toast.makeText(App.get(), "Export Begin...", Toast.LENGTH_SHORT).show();
        }

        @SuppressLint("NewApi")
        @Override
        protected Void doInBackground(Void... params) {
            SQLiteDatabase database = DatabaseManager.getInstance().getReadableDatabase();
            LogUtil.d("ZLOVE", "database.getPath()---" + database.getPath());
            String dbName = DatabaseManager.getInstance().getDatabaseName();
            File dbFile = ApplicationUtil.getApplicationContext().getDatabasePath(dbName);
            try {
                FileUtil.copy(dbFile, new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "copy_" + dbName));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Toast.makeText(App.get(), "Export Complete", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void confirm() {
        CacheUtil.deleteFilesByDirectory(new File(FileStorage.APP_IMG_DIR));
        CacheUtil.deleteFilesByDirectory(new File(FileStorage.APP_ATTACH_DIR));
        tvCacheSize.setText("0M");
    }

    @Override
    public void cancel() {
    }
}
