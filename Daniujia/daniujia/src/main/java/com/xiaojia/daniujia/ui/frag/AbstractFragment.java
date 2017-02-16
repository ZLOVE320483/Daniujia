package com.xiaojia.daniujia.ui.frag;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.xiaojia.daniujia.ui.views.toast.ToastCompat;
import com.xiaojia.daniujia.utils.ApplicationUtil;

/**
 * Created by ZLOVE on 2016/4/10.
 */
public abstract class AbstractFragment extends Fragment {

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = configureLayoutInflater(inflater).inflate(getInflateLayout(), container, false);
        setUpView(view);
        return view;
    }

    /**
     *
     * @param inflater
     * @return
     */
    protected LayoutInflater configureLayoutInflater(LayoutInflater inflater) {
        return inflater;
    }

    /**
     * @return R.layout.*** 加载页面的布局定义
     */
    protected abstract int getInflateLayout();

    /**
     * 用于获取界面上的控件 for example, view.findViewById(R.id.***);
     *
     * @param view 加载页面的父View
     */
    protected abstract void setUpView(View view);

    /**
     * 显示Toast
     *
     * @param message
     */
    public void showShortToast(String message) {
        Toast.makeText(ApplicationUtil.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示Toast
     *
     * @param message
     */
    public void showShortToast(int message) {
        String msg = ApplicationUtil.getApplicationContext().getResources().getString(message);
        Toast.makeText(ApplicationUtil.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    protected void showShortToastForce(String msg) {
        ToastCompat.makeText(ApplicationUtil.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 设置默认的点击响应，getActivity().finish();
     *
     * @param view, back button view
     */
    protected void setBackButton(View view) {
        if (view != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            });
        }
    }
}
