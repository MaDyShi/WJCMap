package com.wjc.wjcmap.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.wjc.wjcmap.R;
import com.wjc.wjcmap.base.BaseActivity;
import com.wjc.wjcmap.bean.Bean;
import com.wjc.wjcmap.map.AMapUtil;
import com.wjc.wjcmap.map.DrivingRouteOverlay;
import com.wjc.wjcmap.util.GsonInstance;
import com.wjc.wjcmap.util.TimeUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
/**
 * 方式一：直线距离排序（速度快，偏差高）
 * 方式二：路线距离排序（速度慢，偏差低）
 */
public class ShowRouteActivity extends BaseActivity implements RouteSearch.OnRouteSearchListener {


    private MapView map_view;
    private AMap map;
    private DrivingRouteOverlay drivingRouteOverlay;

    private List<LatLonPoint> throughList = new ArrayList<>(); // 经途点集合
    private LatLonPoint start,end;
    private List<View> throughMarkers = new ArrayList<>(); // 经途点marker集合
    private List<Bean> list;

    @Override
    protected int bindLayout() {
        return R.layout.activity_showroute_layout;
    }

    @Override
    protected void initView(View mContextView, @Nullable Bundle savedInstanceState) {
        map_view = findViewById(R.id.map_view);
        map_view.onCreate(savedInstanceState);
    }

    @Override
    protected void initListener() {
        super.initListener();
        findViewById(R.id.send_emailbt).setOnClickListener(view -> {
            if (!TextUtils.isEmpty(email)) {
                Toast.makeText(this, email, Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    protected void doBusiness(Context context) {
        if (map == null) {
            map = map_view.getMap();
            map.setMapType(AMap.MAP_TYPE_BUS);
            map.setMapCustomEnable(true);
            UiSettings uiSettings = map.getUiSettings();
            uiSettings.setLogoBottomMargin(-50);
            uiSettings.setZoomControlsEnabled(false);
        }
        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            Bean start1 = GsonInstance.getInstance().fromJson(bundle.getString("start"),Bean.class);
            list = (List<Bean>) bundle.getSerializable("list");
            sortList(list);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(new BigDecimal(start1.getLat()).doubleValue(),new BigDecimal(start1.getLon()).doubleValue()), 16));
            for (int i = 0; i < list.size()-1; i++) {
                throughList.add(new LatLonPoint(new BigDecimal(list.get(i).getLat()).doubleValue(),new BigDecimal(list.get(i).getLon()).doubleValue()));
            }
            start = new LatLonPoint(new BigDecimal(start1.getLat()).doubleValue(),new BigDecimal(start1.getLon()).doubleValue());
            end = new LatLonPoint(new BigDecimal(list.get(list.size()-1).getLat()).doubleValue(),new BigDecimal(list.get(list.size()-1).getLon()).doubleValue());
            AMapUtil.doRoutePlan(start,end,throughList,this,this);


            list.add(start1);
        }

    }

    private void sortList(List<Bean> list) {



    }

    @Override
    protected void onResume() {
        super.onResume();
        map_view.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        map_view.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        map_view.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        map_view.onDestroy();
    }


    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {
        if (i == 1000) {
            if (map != null && driveRouteResult != null && driveRouteResult.getPaths() != null && driveRouteResult.getPaths().size() > 0) {

                map.clear();
                DrivePath drivePath = driveRouteResult.getPaths().get(0);
                // 定义图层
                drivingRouteOverlay = new DrivingRouteOverlay(
                        this,
                        map,
                        drivePath,
                        start,
                        end,
                        throughList,setMapLineColor(), null);
                drivingRouteOverlay.setNodeIconVisibility(false);//隐藏转弯的节点

                drivingRouteOverlay.addToMap(
                        R.mipmap.line_startimg,
                        R.mipmap.line_endimg,
                        "#6ebcf7"
                );
                addThroughPointMarker();
            }
        }
    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }

    // 设置急件地图线路颜色
    public static List<Integer> setMapLineColor(){
        List<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#569efd"));
        return colors;
    }
    String email = "";
    private void addThroughPointMarker() {
        long l = System.currentTimeMillis();
        for (int i = 0; i < list.size(); i++) {
            Bean bean = list.get(i);
            if (i == 0) {
                bean.setContent("起点"+ TimeUtil.fourTime(l)+"出发预计1小时后到达下一个地点");
            }else if (i == list.size() - 1){
                bean.setContent("终点"+ TimeUtil.fourTime(l+i*60*60*1000)+"到达，预计游览30分钟，预计1小时后到达下一个地点");
            }else {
                bean.setContent(bean.getContent()+TimeUtil.fourTime(l+i*60*60*1000)+"到达终点，休息1小时后准备返程");
            }
            map.addMarker(new MarkerOptions()
                    .position(new LatLng(new BigDecimal(bean.getLat()).doubleValue(),new BigDecimal(bean.getLon()).doubleValue()))
                    .visible(true)
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.wait_img))
                    .title(TextUtils.isEmpty(bean.getContent()) ? "" : bean.getContent()));

            email += bean.getContent();

        }

    }
}
