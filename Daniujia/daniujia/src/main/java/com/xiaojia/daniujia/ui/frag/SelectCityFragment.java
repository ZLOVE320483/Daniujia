package com.xiaojia.daniujia.ui.frag;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.IntentKey;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.domain.resp.AllCityRespVo;
import com.xiaojia.daniujia.domain.resp.SelectCityBean;
import com.xiaojia.daniujia.managers.OkHttpClientManager;
import com.xiaojia.daniujia.ui.adapters.SelectCityAdapter;
import com.xiaojia.daniujia.ui.views.AlphaBetIndexerBar;
import com.xiaojia.daniujia.ui.views.PinnedSectionListView;
import com.xiaojia.daniujia.utils.ListUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ZLOVE on 2016/6/29.
 */
public class SelectCityFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    private PinnedSectionListView listView;
    private List<SelectCityBean> list = new ArrayList<>();
    private SelectCityAdapter adapter;

    private AlphaBetIndexerBar indexerBar;
    private Map<String, Integer> sectionMap = new HashMap<>();

    private String curCityCode = "";

    @Override
    protected int getInflateLayout() {
        return R.layout.frag_select_city;
    }

    @Override
    protected void setUpView(View view) {
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            if (intent.hasExtra(IntentKey.INTENT_KEY_CUR_CITY_CODE)) {
                curCityCode = intent.getStringExtra(IntentKey.INTENT_KEY_CUR_CITY_CODE);
            }
        }
        setBackButton(view.findViewById(R.id.id_back));
        ((TextView) view.findViewById(R.id.id_title)).setText("城市选择");

        listView = (PinnedSectionListView) view.findViewById(R.id.id_list);
        listView.setOnItemClickListener(this);
        listView.setPullLoadEnable(false);
        listView.setPullRefreshEnable(false);
        adapter = new SelectCityAdapter(list, getActivity());
        adapter.setCurCityCode(curCityCode);
        listView.setAdapter(adapter);

        indexerBar = (AlphaBetIndexerBar) view.findViewById(R.id.id_alphabet_indexer);
        indexerBar.setOnTouchingLetterChangedListener(new AlphaBetIndexerBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                if (sectionMap != null && sectionMap.get(s) != null) {
                    listView.setSelection(sectionMap.get(s));
                }
            }
        });
        requestCityData();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (ListUtils.isEmpty(list)) {
            return;
        }
        int headerCount = listView.getHeaderViewsCount();
        SelectCityBean item = adapter.getItem(position - headerCount);
        if (item == null) {
            return;
        }
        if (item.getSectionType() == SelectCityBean.SECTION) {
            return;
        }
        curCityCode = item.getCityCode();
        adapter.setCurCityCode(curCityCode);
        adapter.notifyDataSetChanged();

        Intent data = new Intent();
        data.putExtra(IntentKey.INTENT_KEY_CUR_CITY_CODE, curCityCode);
        data.putExtra(IntentKey.INTENT_KEY_CUR_CITY_NAME, item.getSectionText());
        finishActivity(data);
    }

    private void setData(AllCityRespVo respVo) {
        if (respVo == null) {
            return;
        }
        List<AllCityRespVo.RecommendCity> recommendCities = respVo.getRecommendCitys();
        if (!ListUtils.isEmpty(recommendCities)) {
            sectionMap.put("热门", 0);
            SelectCityBean cityBean = new SelectCityBean();
            cityBean.setSectionType(SelectCityBean.SECTION);
            cityBean.setSectionText("热门城市");
            cityBean.setCityCode("-1");
            list.add(cityBean);
            for (AllCityRespVo.RecommendCity city : recommendCities) {
                SelectCityBean bean = new SelectCityBean();
                bean.setSectionType(SelectCityBean.ITEM);
                bean.setSectionText(city.getName());
                bean.setCityCode(city.getCityCode());
                list.add(bean);
            }
        }
        List<AllCityRespVo.CommonCity> commonCities = respVo.getCitys();
        if (!ListUtils.isEmpty(commonCities)) {
            AllCityRespVo.CommonCity firstCity = commonCities.get(0);
            sectionMap.put(firstCity.getPhonetic(), list.size());
            SelectCityBean cityBean = new SelectCityBean();
            cityBean.setSectionType(SelectCityBean.SECTION);
            cityBean.setSectionText(firstCity.getPhonetic());
            cityBean.setCityCode("-1");
            list.add(cityBean);

            SelectCityBean firstCityBean = new SelectCityBean();
            firstCityBean.setSectionType(SelectCityBean.ITEM);
            firstCityBean.setSectionText(firstCity.getName());
            firstCityBean.setCityCode(firstCity.getCityCode());
            list.add(firstCityBean);
            for (int i = 1; i < commonCities.size(); i++) {
                if (!commonCities.get(i).getPhonetic().equals(commonCities.get(i - 1).getPhonetic())) {
                    sectionMap.put(commonCities.get(i).getPhonetic(), list.size());
                    SelectCityBean sectionBean = new SelectCityBean();
                    sectionBean.setSectionType(SelectCityBean.SECTION);
                    sectionBean.setSectionText(commonCities.get(i).getPhonetic());
                    sectionBean.setCityCode("-1");
                    list.add(sectionBean);
                }
                AllCityRespVo.CommonCity commonCity = commonCities.get(i);
                SelectCityBean bean = new SelectCityBean();
                bean.setSectionType(SelectCityBean.ITEM);
                bean.setSectionText(commonCity.getName());
                bean.setCityCode(commonCity.getCityCode());
                list.add(bean);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void requestCityData() {
        String url = Config.WEB_API_SERVER + "/user/citys/sort/bypinyin";
        OkHttpClientManager.getInstance(getActivity()).getWithToken(true, url, new OkHttpClientManager.ResultCallback<AllCityRespVo>() {
            @Override
            public void onResponse(AllCityRespVo result) {
                if (result.getReturncode().equals("SUCCESS")) {
                    setData(result);
                } else {
                    showShortToast(result.getReturnmsg());
                }
            }

            @Override
            public void onError(Request paramRequest, Exception e) {
                super.onError(paramRequest, e);
            }

            @Override
            public void onFail(String errorMsg) {
                super.onFail(errorMsg);
            }
        });
    }
}
