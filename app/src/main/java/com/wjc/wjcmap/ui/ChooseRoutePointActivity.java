package com.wjc.wjcmap.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.wjc.wjcmap.R;
import com.wjc.wjcmap.adapter.HistoryPointListAdapter;
import com.wjc.wjcmap.base.BaseActivity;
import com.wjc.wjcmap.bean.Bean;
import com.wjc.wjcmap.util.EditUtil;
import com.wjc.wjcmap.util.GsonInstance;
import com.wjc.wjcmap.util.LocationBean;
import com.wjc.wjcmap.util.MapConstant;

import java.util.ArrayList;
import java.util.List;

public class ChooseRoutePointActivity extends BaseActivity implements PoiSearch.OnPoiSearchListener {

    private EditText inputadd_et;
    private RecyclerView pointlist;
    private List<Bean> history = new ArrayList<>();
    private HistoryPointListAdapter listAdapter;

    @Override
    protected int bindLayout() {
        return R.layout.activity_chooseroute_layout;
    }

    @Override
    protected void initView(View mContextView, @Nullable Bundle savedInstanceState) {
        inputadd_et = findViewById(R.id.inputadd_et);
        EditUtil.setInPutChinese(inputadd_et,20);
        pointlist = findViewById(R.id.point_list);
    }

    @Override
    protected void initListener() {
        super.initListener();
        findViewById(R.id.title_backbt).setOnClickListener(view -> {
            finish();
        });
        inputadd_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable != null && !TextUtils.isEmpty(editable.toString())) {
                    searchKey(editable.toString());
                }
            }
        });
    }

    @Override
    protected void doBusiness(Context context) {
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listAdapter = new HistoryPointListAdapter(LayoutInflater.from(this), history);
        com.wjc.wjcmap.bean.Point point = GsonInstance.getInstance().fromJson(LocationBean.s, com.wjc.wjcmap.bean.Point.class);
        if (point != null && point.getBody() != null) {
            List<Bean> list = point.getBody();
            history.addAll(list);
        }
        pointlist.setLayoutManager(manager);
        pointlist.setAdapter(listAdapter);
        listAdapter.setOnPointItemClickListener(bean -> {
            String s = GsonInstance.getInstance().toJson(bean);
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString(MapConstant.POINT_FLAG,s);
            intent.putExtras(bundle);
            setResult(Activity.RESULT_OK,intent);
            finish();
        });
    }

    // 搜索地址
    private void searchKey(String key) {
        PoiSearch.Query query = new PoiSearch.Query(key, "","北京");
        query.setPageSize(15);
        query.setDistanceSort(true);
        PoiSearch search = new PoiSearch(this, query);
        search.setOnPoiSearchListener(this);
        search.searchPOIAsyn();
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {
        if (i == 1000) {
            if (poiResult.getPois() != null && poiResult.getPois().size() > 0) {
                history.clear();
                ArrayList<PoiItem> pois = poiResult.getPois();
                for (int j = 0; j < pois.size(); j++) {
                    Bean bean = new Bean();
                    bean.setTitle(pois.get(j).getTitle());
                    bean.setContent(pois.get(j).getCityName()+pois.get(j).getSnippet());
                    bean.setLat(String.valueOf(pois.get(j).getLatLonPoint().getLatitude()));
                    bean.setLon(String.valueOf(pois.get(j).getLatLonPoint().getLongitude()));
                    history.add(bean);
                    listAdapter.notifyDataSetChanged();
                }
            }

        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }
}
