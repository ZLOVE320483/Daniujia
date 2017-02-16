package com.xiaojia.daniujia.ui.control;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.xiaojia.daniujia.ui.frag.BaseFragment;
import com.xiaojia.daniujia.ui.views.toast.ToastCompat;
import com.xiaojia.daniujia.utils.ApplicationUtil;

import java.util.Collection;
import java.util.HashMap;

/**
 * Created by ZLOVE on 2016/4/10.
 */
public class BaseControl<F extends BaseFragment> {

    // Fragment
    private F fragment;

    // PresenterContentObserver 的统一处理
    private HashMap<Uri, PresenterContentObserver> observerMap;

    // 获取Context
    protected Context getContext() {
        return getAttachedActivity();
    }

    // 获取Activity
    protected FragmentActivity getAttachedActivity() {
        return fragment != null ? fragment.getActivity() : null;
    }

    // 获取当前的Fragment
    public F getFragment() {
        return fragment;
    }

    // 设置当前Fragment
    public void setFragment(F fragment) {
        this.fragment = fragment;
    }

    /**
     * 由{@link BaseFragment#onSaveInstanceState(Bundle)}是调用
     *
     * @param outState
     */
    public void onSaveInstanceState(Bundle outState) {
    }

    /**
     * 由{@link BaseFragment#onActivityCreated(Bundle)}是调用
     *
     * @param savedInstanceState
     */
    public void onActivityCreated(Bundle savedInstanceState) {
    }

    /**
     * Fragment onResume
     */
    public void onResume() {
    }

    /**
     * Fragment onPause
     */
    public void onPause() {
    }

    /**
     * Fragment onDestroy
     */
    public void onDestroy() {
    }

    /**
     * 由{@link BaseFragment#onActivityResult(int, int, Intent)}是调用
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    /**
     * 关闭当前的Activity
     */
    protected void finishActivity() {
        finishActivity(null);
    }

    /**
     * 关闭当前的Activity
     *
     * @param result， activity result
     */
    protected void finishActivity(Intent result) {
        try {
            if (getAttachedActivity() != null) {
                if (result != null) {
                    getAttachedActivity().setResult(Activity.RESULT_OK, result);
                }
                getAttachedActivity().finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Register content observer by uri
     *
     * @param uri
     */
    protected void registerContentObserver(Uri uri) {
        if (!observerMap.containsKey(uri)) {
            PresenterContentObserver observer = new PresenterContentObserver(new Handler());
            observer.setUri(uri);
            observerMap.put(uri, observer);
            getContext().getContentResolver().registerContentObserver(uri, false, observer);
        }
    }

    /**
     * Unregister content observer by uri
     *
     * @param uri
     */
    protected void unregisterContentObserver(Uri uri) {
        if (observerMap.containsKey(uri)) {
            PresenterContentObserver observer = observerMap.get(uri);
            getContext().getContentResolver().unregisterContentObserver(observer);
            observerMap.remove(uri);
        }
    }

    /**
     * Unregister all observers
     */
    protected void unregisterAllContentObservers() {
        Collection<PresenterContentObserver> observers = observerMap.values();
        ContentResolver cr = getContext().getContentResolver();
        for (PresenterContentObserver observer : observers) {
            cr.unregisterContentObserver(observer);
        }
        observerMap.clear();
    }

    /**
     * Notify that content has changed <br/>
     * Override for child class
     *
     * @param uri
     */
    protected void onContentChange(Uri uri) {

    }

    /**
     * PresenterContentObserver
     *
     * @author OCEAN
     */
    private class PresenterContentObserver extends ContentObserver {

        private Uri uri;

        /**
         * onChange() will happen on the provider Handler.
         *
         * @param handler The handler to run {@link #onChange} on.
         */
        public PresenterContentObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            onContentChange(uri);
        }

        public void setUri(Uri uri) {
            this.uri = uri;
        }
    }

    protected void showShortToast(String msg) {
        ToastCompat.makeText(ApplicationUtil.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    protected void showShortToast(int message) {
        String msg = ApplicationUtil.getApplicationContext().getResources().getString(message);
        ToastCompat.makeText(ApplicationUtil.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
