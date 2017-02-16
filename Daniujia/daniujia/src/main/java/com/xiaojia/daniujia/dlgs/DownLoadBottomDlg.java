package com.xiaojia.daniujia.dlgs;

import android.app.Activity;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;

import com.xiaojia.daniujia.ExtraConst;
import com.xiaojia.daniujia.R;

/**
 * Created by Administrator on 2016/6/8.
 */
public class DownLoadBottomDlg extends BaseBottomDialog implements View.OnClickListener {

    private String url;

    public DownLoadBottomDlg(Activity context) {
        super(context, LayoutInflater.from(context).inflate(R.layout.dlg_bottom_download_image, null));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mView.findViewById(R.id.cancel).setOnClickListener(this);
        mView.findViewById(R.id.download).setOnClickListener(this);
    }


    public void setData(String url){
        this.url = url;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                break;
            case R.id.download:
                if (mClickListener != null) {
                    Bundle b = new Bundle();
                    b.putString(ExtraConst.DOWNLOAD_URL,url);
                    mClickListener.onOK(b);
                }
                break;
        }
        dismiss();
    }
}
