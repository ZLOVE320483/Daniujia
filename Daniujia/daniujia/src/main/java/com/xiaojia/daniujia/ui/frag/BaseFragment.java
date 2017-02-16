package com.xiaojia.daniujia.ui.frag;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.xiaojia.daniujia.ui.control.BaseControl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by ZLOVE on 2016/4/10
 */
public abstract class BaseFragment extends AbstractFragment {

    protected List<Uri> notifyUris = new ArrayList<>();
    private Set<BaseControl<?>> controlSet = new HashSet<BaseControl<?>>();
    public void removeControl(BaseControl<?> control) {
        controlSet.remove(control);
    }

    /**
     * 设置对应的控制器
     * @param control
     */
    @SuppressWarnings("unchecked")
    protected void setControl(BaseControl control) {
        control.setFragment(this);
        controlSet.add(control);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (controlSet != null) {
            for (BaseControl<?> control : controlSet) {
                control.onSaveInstanceState(outState);
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (controlSet != null) {
            for (BaseControl<?> control : controlSet) {
                control.onActivityCreated(savedInstanceState);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (controlSet != null) {
            for (BaseControl<?> control : controlSet) {
                control.onResume();
            }
        }
    }

    @Override
    public void onPause() {
        if (controlSet != null) {
            for (BaseControl<?> control : controlSet) {
                control.onPause();
            }
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (controlSet != null) {
            for (BaseControl<?> control : controlSet) {
                control = null;
            }
        }
        super.onDestroy();
    }

    /**
     * 销毁当前的Activity
     */
    protected void finishActivity() {
        finishActivity(null);
    }

    /**
     * 销毁当前的Activity
     * @param result, 返回的结果
     */
    protected void finishActivity(Intent result) {
        try {
            if (getActivity() != null) {
                if (result != null) {
                    getActivity().setResult(Activity.RESULT_OK, result);
                }
                getActivity().finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将数据添加到AdapterView中，用于刷新数据
     * @param objList 数据List
     * @param arrayAdapter
     */
    public static <T extends Object> void setListToArrayAdapter(List<T> objList, ArrayAdapter<T> arrayAdapter) {
        if (arrayAdapter == null) {
            return;
        }

        arrayAdapter.clear();
        appendListToArrayAdapter(objList, arrayAdapter);
    }

    /**
     * 将数据添加到AdapterView中，用于加载更多数据
     * @param objList
     * @param arrayAdapter
     */
    public static <T extends Object> void appendListToArrayAdapter(List<T> objList, ArrayAdapter<T> arrayAdapter) {
        if (objList != null) {
            for (T object : objList) {
                if (object != null) {
                    arrayAdapter.add(object);
                }
            }
        }
    }
}
