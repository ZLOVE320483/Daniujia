package com.xiaojia.daniujia.ui.control;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;

import com.xiaojia.daniujia.ui.frag.BaseFragment;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by ZLOVE on 2016/4/10
 */
public class BaseLoaderControl<F extends BaseFragment> extends BaseControl<F> {


    private Set<Integer> loaderIdSet;

    /**
     * initialize loader and start to execute
     *
     * @param id
     * @param args
     * @param callback
     */
    protected void initLoader(int id, Bundle args, LoaderManager.LoaderCallbacks<?> callback) {
        if (loaderIdSet == null) {
            loaderIdSet = new HashSet<>();
        }
        loaderIdSet.add(id);
        getAttachedActivity().getSupportLoaderManager().initLoader(id, args, callback);
    }

    /**
     * restart loader to execute
     *
     * @param id
     * @param args
     * @param callback
     */
    protected void restartLoader(int id, Bundle args, LoaderManager.LoaderCallbacks callback) {
        destroyLoader(id);
        initLoader(id, args, callback);
    }

    /**
     * Stops and removes the loader with the given ID
     *
     * @param id
     */
    protected void destroyLoader(int id) {
        getAttachedActivity().getSupportLoaderManager().destroyLoader(id);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (loaderIdSet != null) {
            for (Integer loaderId : loaderIdSet) {
                if (loaderId != -1) {
                    destroyLoader(loaderId);
                }
            }
        }
    }
}
