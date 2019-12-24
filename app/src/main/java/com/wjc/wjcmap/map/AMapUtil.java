/**
 * 
 */
package com.wjc.wjcmap.map;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.RouteSearch;
import com.wjc.wjcmap.R;
import com.wjc.wjcmap.bean.Point;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AMapUtil {

	// 获取自定义marker视图
	public static View getMarkerView(Context context,String address, @Nullable int imgId){
		View view = LayoutInflater.from(context).inflate(R.layout.map_marker_layout, null);
		TextView addresstv  = view.findViewById(R.id.marker_addresstv);
		ImageView marker_img  = view.findViewById(R.id.marker_img);
		marker_img.setImageResource(imgId == 0 ? R.mipmap.app_maplocation : imgId);
		addresstv.setText(TextUtils.isEmpty(address) ? "" : address);
		return view;
	}

	// 路线规划方法
	public static void doRoutePlan(LatLonPoint startTip, LatLonPoint endTip,List<LatLonPoint> list, Context context, RouteSearch.OnRouteSearchListener listener){
		if (startTip == null){
			Toast.makeText(context, "请选择出发地", Toast.LENGTH_SHORT).show();
			return;
		}
		if (endTip == null){
			Toast.makeText(context, "请选择目的地", Toast.LENGTH_SHORT).show();
			return;
		}

		RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(startTip,endTip);
		RouteSearch routeSearch = new RouteSearch(context);
		routeSearch.setRouteSearchListener(listener);
		RouteSearch.WalkRouteQuery query = new RouteSearch.WalkRouteQuery(fromAndTo, RouteSearch.DRIVING_SINGLE_SHORTEST);
		routeSearch.calculateWalkRouteAsyn(query);
		/*RouteSearch.DriveRouteQuery driveRouteQuery =
				new RouteSearch.DriveRouteQuery(fromAndTo,RouteSearch.DRIVING_SINGLE_SHORTEST,list,null,"");
		routeSearch.calculateDriveRouteAsyn(driveRouteQuery);*/
	}


	/**
	 * 判断edittext是否null
	 */
	public static String checkEditText(EditText editText) {
		if (editText != null && editText.getText() != null
				&& !(editText.getText().toString().trim().equals(""))) {
			return editText.getText().toString().trim();
		} else {
			return "";
		}
	}

	public static Spanned stringToSpan(String src) {
		return src == null ? null : Html.fromHtml(src.replace("\n", "<br />"));
	}

	public static String colorFont(String src, String color) {
		StringBuffer strBuf = new StringBuffer();

		strBuf.append("<font color=").append(color).append(">").append(src)
				.append("</font>");
		return strBuf.toString();
	}

	public static String makeHtmlNewLine() {
		return "<br />";
	}

	public static String makeHtmlSpace(int number) {
		final String space = "&nbsp;";
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < number; i++) {
			result.append(space);
		}
		return result.toString();
	}



	public static boolean IsEmptyOrNullString(String s) {
		return (s == null) || (s.trim().length() == 0);
	}

	/**
	 * 把LatLng对象转化为LatLonPoint对象
	 */
	public static LatLonPoint convertToLatLonPoint(LatLng latlon) {
		return new LatLonPoint(latlon.latitude, latlon.longitude);
	}

	/**
	 * 把LatLonPoint对象转化为LatLon对象
	 */
	public static LatLng convertToLatLng(LatLonPoint latLonPoint) {
		return new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
	}

	/**
	 * 把集合体的LatLonPoint转化为集合体的LatLng
	 */
	public static ArrayList<LatLng> convertArrList(List<LatLonPoint> shapes) {
		ArrayList<LatLng> lineShapes = new ArrayList<LatLng>();
		for (LatLonPoint point : shapes) {
			LatLng latLngTemp = AMapUtil.convertToLatLng(point);
			lineShapes.add(latLngTemp);
		}
		return lineShapes;
	}
	public static ArrayList<LatLonPoint> convert2PointList(ArrayList<LatLng> latLngs){
		ArrayList<LatLonPoint> lineShapes = new ArrayList<LatLonPoint>();
		for (LatLng point : latLngs) {
			LatLonPoint latLngTemp = AMapUtil.convertToLatLonPoint(point);
			lineShapes.add(latLngTemp);
		}
		return lineShapes;
	}

	/**
	 * long类型时间格式化
	 */
	public static String convertToTime(long time) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(time);
		return df.format(date);
	}

	public static final String HtmlBlack = "#000000";
	public static final String HtmlGray = "#808080";
	
	public static String getFriendlyTime(int second) {
		if (second > 3600) {
			int hour = second / 3600;
			int miniate = (second % 3600) / 60;
			return hour + "小时" + miniate + "分钟";
		}
		if (second >= 60) {
			int miniate = second / 60;
			return miniate + "分钟";
		}
		return second + "秒";
	}

		public static String getSimpleBusLineName(String busLineName) {
			if (busLineName == null) {
				return String.valueOf("");
			}
			return busLineName.replaceAll("\\(.*?\\)", "");
		}


}
