package com.example.osmdroiddemo.utils;

import org.mapsforge.core.model.Point;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;

/**
 * 屏幕坐标转经纬度
 */
public class Screen2GeoPoint {

    /**
     *
     * @param mapView
     * @param x     屏幕点的x
     * @param y    屏幕点的y
     * @return  带经纬度的点
     */
    public static IGeoPoint transform(MapView mapView, float x, float y){

        // 创建一个Point对象来保存屏幕坐标
        Point screenPoint = new Point(x, y);

        // 获取投影对象
        Projection projection = mapView.getProjection();

        // 将屏幕坐标转换为经纬度坐标
        IGeoPoint geoPoint = projection.fromPixels((int) screenPoint.x, (int) screenPoint.y);

        // 获取经纬度坐标
//        double latitude = geoPoint.getLatitude();
//        double longitude = geoPoint.getLongitude();
        return geoPoint;
    }
}
