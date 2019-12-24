package com.wjc.wjcmap.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;


import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 作者：nade,时间 2018/7/5
 * 综述：(获取资源文件)
 */

public class UiUtils {





    /**
     * 屏幕宽度
     **/
    public static int screenWidth = 0;
    /**
     * 屏幕高度
     **/
    public static int screenHight = 0;
    /**
     * 屏幕密度，dp占屏幕大小计算方法：dp*(screenDip/160)
     **/
    public static int screenDip = 0;// // 屏幕密度（每寸像素：120/160/240/320）

    public static Context getContext() {
        return Utils.getContext();
    }

    //获取字符串
    public static String getString(int id) {
        return getContext().getResources().getString( id );
    }

    //获取字符串数组
    public static String[] getStringArray(int id) {
        return getContext().getResources().getStringArray( id );
    }

    //获取图片资源
    public static Drawable getDrawable(int id) {
        return getContext().getResources().getDrawable( id );
    }

    //获取颜色
    public static int getColor(int id) {
        return getContext().getResources().getColor( id );
    }

    //获取尺寸
    public static int getDimen(int id) {
        return getContext().getResources().getDimensionPixelSize( id );
    }
    // 获取最大宽度
    public static int getMaxWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService( Context.WINDOW_SERVICE );
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics( dm );
        return dm.widthPixels;
    }
    // 获取最大高度
    public static int getMaxHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService( Context.WINDOW_SERVICE );
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics( dm );
        return dm.heightPixels;
    }

    /**
     * dp与px之间的转换
     *
     * @param dip
     * @return
     */
    public static int dip2px(float dip) {
        //获取屏幕的密度
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f);
    }

    public static int px2dip(float px) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return new BigDecimal(px / density).intValue();
    }


    /**
     * 判断SDK版本
     *
     * @param SDKVersion
     * @return
     */
    public static boolean judgeSDK(int SDKVersion) {
        return Build.VERSION.SDK_INT >= SDKVersion;
    }

    /*判断当前时间*/
    public static long getTime() {
        return System.currentTimeMillis();
    }

    public static long accuracyTime() {
        return getTime();
    }

    /**
     * 设置屏幕参数（屏幕精度、高度、宽度）
     *
     * @param context
     * @return
     */
    public static void setContextScreenVaules(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService( Context.WINDOW_SERVICE );
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics( dm );
        screenDip = dm.densityDpi;
        screenWidth = dm.widthPixels;
        screenHight = dm.heightPixels;
    }

    /**
     * 获取屏幕的宽高
     */
    public static int getStatusBarHeight() {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 0;
        try {
            c = Class.forName( "com.android.internal.R$dimen" );
            obj = c.newInstance();
            field = c.getField( "status_bar_height" );
            x = Integer.parseInt( field.get( obj ).toString() );
            sbar = getContext().getResources().getDimensionPixelSize( x );
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return sbar;
    }

    /**
     * 取出image
     */
    public static String[] splitImage(String images) {
        if (!TextUtils.isEmpty( images )) {
            return images.split( "," );
        }
        return new String[0];
    }

    /**
     * long转换年月日
     */
    private static DateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd" );
    private static DateFormat dateFormathour = new SimpleDateFormat( "yyyy-MM-dd    HH:mm" );

    public static String formatDate(String millisecond) {
        return dateFormat.format( new Date( Long.parseLong( millisecond ) ) );
    }

    public static String formatDateForhour(String time) {
        return dateFormathour.format( new Date( Long.parseLong( time ) ) );
    }

    public static String formatDateForhourByLong(Long time) {
        return dateFormathour.format( new Date( time ) );
    }

    /**
     * 随机生成验证码
     *
     * @return
     */
    public static String getAuthCode() {
        StringBuilder string = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 4; i++)
            string.append( random.nextInt( 10 ) );
        return string.toString();
    }

    /**
     * 检查是否是电话号码
     *
     * @return
     */
    public static boolean isMobileNum(String mobiles) {
        Pattern p = Pattern
                .compile( "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147)|(19[8|9]))\\d{8}$" );
        Matcher m = p.matcher( mobiles );
        return m.matches();
    }


    public static String GetNetIp() {
        String IP = "";
        try {
            String address = "http://ip.taobao.com/service/getIpInfo2.php?ip=myip";
            URL url = new URL( address );
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setUseCaches( false );
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream in = connection.getInputStream();
                // 将流转化为字符串
                BufferedReader reader = new BufferedReader( new InputStreamReader( in ) );
                String tmpString = "";
                StringBuilder retJSON = new StringBuilder();
                while ((tmpString = reader.readLine()) != null) {
                    retJSON.append( tmpString + "\n" );
                }
                JSONObject jsonObject = new JSONObject( retJSON.toString() );
                String code = jsonObject.getString( "code" );
                if (code.equals( "0" )) {
                    JSONObject data = jsonObject.getJSONObject( "data" );
                    IP = data.getString( "ip" ) + "(" + data.getString( "country" ) + data.getString( "area" ) + "区" + data.getString( "region" ) + data.getString( "city" ) + data.getString( "isp" ) + ")";

                    Log.d("nade", "您的IP地址是: "+IP);
                } else {
                    IP = "";
                    Log.d("nade", "IP接口异常，无法获取IP地址！: ");

                }
            } else {
                IP = "";
                Log.d("nade", "网络连接异常，无法获取IP地址！:");

            }
        } catch (Exception e) {
            IP = "";
            Log.d("nade", "获取IP地址时出现异常，异常信息是: "+e.toString());

        }
        return IP;
    }

    /**
     * 获取外网的IP(要访问Url，要放到后台线程里处理)
     *
     * @param @return
     * @return String
     * @throws
     * @Title: GetNetIp
     * @Description:
     */
    public static String getNetIp() {
        URL infoUrl = null;
        InputStream inStream = null;
        String ipLine = "";
        HttpURLConnection httpConnection = null;
        try {
            //            infoUrl = new URL("http://ip168.com/");
            infoUrl = new URL( "http://pv.sohu.com/cityjson?ie=utf-8" );
            URLConnection connection = infoUrl.openConnection();
            httpConnection = (HttpURLConnection) connection;
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inStream = httpConnection.getInputStream();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader( inStream, "utf-8" ) );
                StringBuilder strber = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    strber.append( line + "\n" );
                }
                Pattern pattern = Pattern
                        .compile( "((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))" );
                Matcher matcher = pattern.matcher( strber.toString() );
                if (matcher.find()) {
                    ipLine = matcher.group();
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inStream.close();
                httpConnection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return ipLine;
    }

    // 获取外网IP
    public static String GetNetIp2() {
        URL infoUrl = null;
        InputStream inStream = null;
        try {           // http://iframe.ip138.com/ic.asp           // infoUrl = new URL("http://city.ip138.com/city0.asp");
            infoUrl = new URL( "http://ip38.com" );
            URLConnection connection = infoUrl.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inStream = httpConnection.getInputStream();
                BufferedReader reader = new BufferedReader( new InputStreamReader( inStream, "utf-8" ) );
                StringBuilder strber = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null)
                    strber.append( line + "\n" );
                inStream.close();
                // 从反馈的结果中提取出IP地址
                //int start = strber.indexOf( "[" );
                // Log.d("zph", "" + start);
                // int end = strber.indexOf("]", start + 1);
                // Log.d("zph", "" + end);
                line = strber.substring( 378, 395 );
                line.replaceAll( " ", "" );
                return line;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    //1,判断是否有网络连接
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 获取版本号
     * @param context
     * @return
     */
    public static int getPackageVersionCode(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context
                    .getPackageName(), 0);
            int versionCode = packageInfo.versionCode;
            return versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 判断context是否有效
     * @param context
     * @return
     */
    public static boolean isValidContext(final Context context) {
        if (context == null) {
            return false;
        }
        if (context instanceof Activity) {
            final Activity activity = (Activity) context;
            if (activity.isDestroyed() || activity.isFinishing()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取commonPopupWindow背景透明度
     * @return
     */
    public static float getDialogBackgroudLevel(){
        return 0.45f;
    }
}
