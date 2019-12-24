package com.wjc.wjcmap.map;

import android.util.Log;

import com.wjc.wjcmap.bean.Bean;
import com.wjc.wjcmap.bean.Point;

import java.util.ArrayList;
import java.util.List;

public class SortPointUtil {

    private int count = 0;
    public void sortPointForLine(List<Bean> list){
        List<Bean> points = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.size(); j++) {
                if (!list.get(i).getTitle().equals(list.get(j).getTitle())) {
                    count++;
                    Log.d("nade", "sortPointForLine: 计算了="+(count));
                }

            }
        }
     //   DrivingRouteOverlay.calculateDistance()
    }

    public void sortPointForRoute(List<Bean> list){}

}
