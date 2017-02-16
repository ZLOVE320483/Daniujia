package com.xiaojia.daniujia.ui.act;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.artifex.mupdf.MuPDFCore;
import com.artifex.mupdf.MuPDFPageAdapter;
import com.xiaojia.daniujia.IntentKey;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.dlgs.AttachBottomDlg;
import com.xiaojia.daniujia.dlgs.BaseBottomDialog;
import com.xiaojia.daniujia.domain.resp.MessageEntity;
import com.xiaojia.daniujia.utils.FileUtil;
import com.xiaojia.daniujia.utils.LogUtil;

/**
 * Created by ZLOVE on 2017/1/19.
 */
public class ActChatFilePreView extends FragmentActivity implements View.OnClickListener {

    private ListView mListView;
    private String path;
    private ImageView ivRight;
    private MessageEntity.ContentEntity contentEntity;

    private BaseBottomDialog.OnDialogClickListener listener = new BaseBottomDialog.OnDialogClickListener() {
        @Override
        public void onOK(String s) {
            if (s != null && contentEntity != null) {
                if (TextUtils.equals("open", s)) {
                    FileUtil.openFile(path, ActChatFilePreView.this, contentEntity.getMimeType());
                } else if (TextUtils.equals("send", s)) {
                    Intent intent = new Intent(ActChatFilePreView.this, ActTransmitList.class);
                    intent.putExtra(IntentKey.INTENT_KEY_ATTACH_ITEM, contentEntity);
                    startActivity(intent);
                }
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_common_list);
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(IntentKey.INTENT_KEY_PRE_VIEW_FILE_PATH)) {
                path = getIntent().getStringExtra(IntentKey.INTENT_KEY_PRE_VIEW_FILE_PATH);
            }
            if (intent.hasExtra(IntentKey.INTENT_KEY_ATTACH_ITEM)) {
                contentEntity = (MessageEntity.ContentEntity) intent.getSerializableExtra(IntentKey.INTENT_KEY_ATTACH_ITEM);
            }
        }
        ((TextView) findViewById(R.id.id_title)).setText("文件预览");
        mListView = (ListView) findViewById(R.id.id_list);
        ivRight = (ImageView) findViewById(R.id.iv_more);
        ivRight.setImageResource(R.drawable.document_more);
        ivRight.setVisibility(View.VISIBLE);
        ivRight.setOnClickListener(this);
        LogUtil.d("ZLOVE", "path---" + path);
        if (!TextUtils.isEmpty(path)) {
            MuPDFCore core = null;
            try{
                core = new MuPDFCore(this,path);
            }catch (Exception e){
                LogUtil.d("ZLOVE", "e----" + e.toString());
            }
            MuPDFPageAdapter adapter = new MuPDFPageAdapter(this, core);
            mListView.setAdapter(adapter);
        }

        findViewById(R.id.id_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private AttachBottomDlg bottomDlg = null;
    @Override
    public void onClick(View v) {
        if (v == ivRight) {
            if (bottomDlg == null) {
                bottomDlg = new AttachBottomDlg(this);
                bottomDlg.setOnDialogClickListener(listener);
            }
            bottomDlg.initOpenButton(2);
            bottomDlg.show();
        }
    }
}
