package com.wjc.wjcmap.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.model.LatLng;
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
import com.wjc.wjcmap.bean.Point;
import com.wjc.wjcmap.map.AMapUtil;
import com.wjc.wjcmap.map.DrivingRouteOverlay;
import com.wjc.wjcmap.tsp.TSPUtil;
import com.wjc.wjcmap.util.GsonInstance;
import com.wjc.wjcmap.util.LocationBean;
import com.wjc.wjcmap.util.MapConstant;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class RouteSettingActivity extends BaseActivity implements RouteSearch.OnRouteSearchListener {

    private TextView title_hint,settting_starttv;
    private Bean start = null; // 起点
    private List<Bean> throughList  = new ArrayList<>(); // 途径点
    private List<LatLonPoint> list  = new ArrayList<>(); // 途径点
    private List<Bean> allList  = new ArrayList<>(); //
    private TextView[] throughs = new TextView[5];
    @Override
    protected int bindLayout() {
        return R.layout.activity_routesetting_layout;
    }

    @Override
    protected void initView(View mContextView, @Nullable Bundle savedInstanceState) {

        title_hint = findViewById(R.id.titlt_hinttv);
        settting_starttv = findViewById(R.id.settting_starttv);
        throughs[0] = findViewById(R.id.settting_throughp1);
        throughs[1] = findViewById(R.id.settting_throughp2);
        throughs[2] = findViewById(R.id.settting_throughp3);
        throughs[3] = findViewById(R.id.settting_throughp4);
        throughs[4] = findViewById(R.id.settting_throughp5);

    }

    @Override
    protected void initListener() {
        super.initListener();
        findViewById(R.id.titlt_backbt).setOnClickListener(view -> {
            finish();
        });
        settting_starttv.setOnClickListener(view -> {
            startActivityForResult(ChooseRoutePointActivity.class, MapConstant.REQUESTCODE);
        });
        throughs[0].setOnClickListener(view -> {
            if (start != null) {
                // 重选时  清空数据和展示
                if (throughList.size() > 0) {
                    throughList.remove(0);
                    throughs[0].setText("");
                }
                startActivityForResult(ChooseRoutePointActivity.class, MapConstant.REQUESTCODE);
            }else {
                Toast.makeText(this, "请选择起点", Toast.LENGTH_SHORT).show();
            }
        });
        throughs[1].setOnClickListener(view -> {
            if (throughList.size() >= 1) {

                // 重选时  清空数据和展示
                if (throughList.size() >= 2) {
                    throughList.remove(1);
                    throughs[1].setText("");
                }

                startActivityForResult(ChooseRoutePointActivity.class, MapConstant.REQUESTCODE);
            }else {
                Toast.makeText(this, "请选择第一个途径点", Toast.LENGTH_SHORT).show();
            }
        });
        throughs[2].setOnClickListener(view -> {
            if (throughList.size() >= 2) {
                if (throughList.size() >= 3) {
                    throughList.remove(2);
                    throughs[2].setText("");
                }
                startActivityForResult(ChooseRoutePointActivity.class, MapConstant.REQUESTCODE);
            }else {
                Toast.makeText(this, "请选择第二个途径点", Toast.LENGTH_SHORT).show();
            }
        });
        throughs[3].setOnClickListener(view -> {
            if (throughList.size() >= 3) {

                if (throughList.size() >= 4) {
                    throughList.remove(3);
                    throughs[3].setText("");
                }
                startActivityForResult(ChooseRoutePointActivity.class, MapConstant.REQUESTCODE);
            }else {
                Toast.makeText(this, "请选择第三个途径点", Toast.LENGTH_SHORT).show();
            }
        });
        throughs[4].setOnClickListener(view -> {
            if (throughList.size() >= 4) {
                if (throughList.size() >= 5) {
                    throughList.remove(4);
                    throughs[4].setText("");
                }
                startActivityForResult(ChooseRoutePointActivity.class, MapConstant.REQUESTCODE);
            }else {
                Toast.makeText(this, "请选择第四个途径点", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.commit_navibt).setOnClickListener(view -> {
            if (start == null) {
                Toast.makeText(this, "请选择起点", Toast.LENGTH_SHORT).show();
                return;
            }
            if (throughList.size() == 0) {
                Toast.makeText(this, "请添加途径点", Toast.LENGTH_SHORT).show();
                return;
            }

            if (list.size() > 0) {
                list.clear();
            }
            for (int i = 0; i < throughList.size(); i++) {
                list.add(new LatLonPoint(new BigDecimal(throughList.get(i).getLat()).doubleValue(),new BigDecimal(throughList.get(i).getLon()).doubleValue()));
            }
            LatLonPoint startTip = new LatLonPoint(new BigDecimal(start.getLat()).doubleValue(), new BigDecimal(start.getLon()).doubleValue());
            AMapUtil.doRoutePlan(startTip,list.get(0),list,this,this);
            Bundle bundle = new Bundle();
            bundle.putString("start",GsonInstance.getInstance().toJson(start));
            bundle.putString("end",GsonInstance.getInstance().toJson(throughList.get(0)));
            bundle.putSerializable("list", (Serializable) throughList);
            sortPoint(throughList,startTip);
           // startActivity(ShowRouteActivity.class,bundle,true);
        });
    }

        // 排序
    private void sortPoint(List<Bean> allList,LatLonPoint start) {
        String s = "";
        for (int i = 0; i < allList.size(); i++) {
            s += allList.get(i).getId();
        }
        Log.d(TAG, "sortPoint: 路线id="+s);
        char[] shu = s.toCharArray();
        List<String> list1 = TSPUtil.sortPoint(shu, 0);
        Log.d(TAG, "sortPoint: 路线集合="+list1.size());
        for (int i = 0; i < list1.size(); i++) {
            plan(allList,list1.get(i),start);
        }
    }
    int count = 0;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);


        }
    };
    List<LatLonPoint> through1 = new ArrayList<>();
    List<Bean> through2 = new ArrayList<>();

    private void plan(List<Bean> points,String s,LatLonPoint start) {
        Log.d(TAG, "sortPoint: 路线集合="+s);
        char[] array = s.toCharArray();
        for (int i = 0; i < array.length; i++) {
            through1.clear();
            through2.clear();

            // 得到策略后所有点的集合
            for (int j = 0; j < points.size(); j++) {
                if (points.get(j).getId().equals(String.valueOf(array[i]))) {
                    through2.add(points.get(j));
                }
            }
            // 填充规划集合
            for (int j = 0; j < through2.size(); j++) {
                through1.add(new LatLonPoint(new BigDecimal(through2.get(j).getLat()).doubleValue(),new BigDecimal(through2.get(j).getLon()).doubleValue()));
            }
        }
        AMapUtil.doRoutePlan(start,through1.get(through1.size()-1),through1,this,this);
    }

    @Override
    protected void doBusiness(Context context) {
        title_hint.setText("路线设置");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MapConstant.REQUESTCODE && resultCode == Activity.RESULT_OK && data.getExtras() != null) {
            Bundle bundle = data.getExtras();
            if (!TextUtils.isEmpty(bundle.getString(MapConstant.POINT_FLAG))) {
                String s = bundle.getString(MapConstant.POINT_FLAG);
                Bean bean = GsonInstance.getInstance().fromJson(s, Bean.class);
                Log.d(TAG, "onActivityResult: 地址="+GsonInstance.getInstance().toJson(bean));
                if (start == null) {
                    start = bean;
                    settting_starttv.setText("起点："+(TextUtils.isEmpty(bean.getTitle()) ? "" : bean.getTitle()));
                }else {
                    if (clearData(throughList,bean)) {
                        throughList.add(bean);
                        throughs[throughList.size() - 1].setText("途径点："+(TextUtils.isEmpty(bean.getTitle()) ? "" : bean.getTitle()));
                    }else {
                        Toast.makeText(this, "途径点已存在，请重新添加", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }



    public boolean clearData(List<Bean> list,Bean bean){
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getTitle().equals(bean.getTitle()) && list.get(i).getLat().equals(bean.getLat())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {
        if (i == 1000) {
            if (driveRouteResult != null && driveRouteResult.getPaths() != null) {
                Log.d(TAG, "onDriveRouteSearched: 规划成果="+driveRouteResult.getPaths().get(0).getTollDistance());

            }
        }
    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {
        Log.d(TAG, "onDriveRouteSearched: 规划成果="+i);
        Log.d(TAG, "onDriveRouteSearched: 规划成果="+walkRouteResult.getPaths().get(0).getSteps().get(0).getDistance());

    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }
}
