package com.wjc.wjcmap.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.wjc.wjcmap.R;
import com.wjc.wjcmap.base.BaseActivity;
import com.wjc.wjcmap.bean.Bean;
import com.wjc.wjcmap.bean.Point;
import com.wjc.wjcmap.map.AMapUtil;
import com.wjc.wjcmap.util.GsonInstance;
import com.wjc.wjcmap.util.LocationBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 程序入口
 *
 * ShowMapActivity 展示地图页面 （仅支持固定点，地点信息点击后展示详细信息）
 * ShowRouteActivity 展示地图路线页面  （展示所有点，并回到起点）
 * ChooseRoutePointActivity 选择路线地点  （支持固定点和高德搜索固定点以外的点）
 * RouteSettingActivity 设置起点终点和途径点 （支持设置起点，终点，途径点，仅显示地址；例：北京市.北京市人民政府）
 */
public class ShowMapActivity extends BaseActivity {
    private MapView main_map;
    private AMap aMap;

    @Override
    protected int bindLayout() {
        return R.layout.activity_showmap;
    }

    @Override
    protected void initView(View mContextView, @Nullable Bundle savedInstanceState) {
        main_map = findViewById(R.id.main_map);
        main_map.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = main_map.getMap();
            aMap.setMapType(AMap.MAP_TYPE_BUS);
            aMap.setMapCustomEnable(true);
            UiSettings uiSettings = aMap.getUiSettings();
            uiSettings.setLogoBottomMargin(-50);
            uiSettings.setZoomControlsEnabled(false);
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
        findViewById(R.id.map_navibt).setOnClickListener(view -> {
            startActivity(RouteSettingActivity.class,false);
          //  startActivity(ShowRouteActivity.class,true);
        });
    }

    @Override
    protected void doBusiness(Context context) {
        Point point = GsonInstance.getInstance().fromJson(LocationBean.s, Point.class);
        Log.d("nade", "doBusiness: 点的集合="+GsonInstance.getInstance().toJson(point));
        if (point != null && point.getBody() != null) {
            List<Bean> list = point.getBody();
            addMarkerToMap(getMarkerList(list));
        }
    }


    // 获取自定义marker
    public MarkerOptions getMapMarker(LatLng latLng,String title, View view){
        if (latLng == null || view == null) return null;
        MarkerOptions options = new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromView(view))
                .position(latLng)
                .visible(true)
                .title(title);

        return options;
    }
    // 获取自定义marker集合
    public ArrayList<MarkerOptions> getMarkerList(List<Bean> points){
        ArrayList<MarkerOptions> list = new ArrayList<>();
        for (Bean bean : points){
            MarkerOptions marker = getMapMarker(new LatLng(Double.valueOf(bean.getLat()), Double.valueOf(bean.getLon())), TextUtils.isEmpty(bean.getContent()) ? "" : bean.getContent(),AMapUtil.getMarkerView(this,bean.getTitle(),0));
            if (marker != null) {
                list.add(marker);
            }
        }
        return list;
    }
    // 添加marker到地图
    public void addMarkerToMap(ArrayList<MarkerOptions> list){
        if (aMap != null) {
            aMap.clear();
            aMap.addMarkers(list,true);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        main_map.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        main_map.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        main_map.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        main_map.onSaveInstanceState(outState);
    }
}
