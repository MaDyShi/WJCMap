package com.wjc.wjcmap.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {
    // yyyy-MM-dd HH:mm
    public static String fourTime(Long time) {

        Date currentTime = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm");
        return formatter.format(currentTime);
    }
}
